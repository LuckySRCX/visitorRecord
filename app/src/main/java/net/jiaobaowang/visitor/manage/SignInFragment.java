package net.jiaobaowang.visitor.manage;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.other.BeepManager;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.idcard.IdCard;
import com.telpo.tps550.api.idcard.IdentityInfo;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.entity.PrintForm;
import net.jiaobaowang.visitor.printer.PrinterActivity;
import net.jiaobaowang.visitor.utils.DialogUtils;
import net.jiaobaowang.visitor.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 访客登记
 */
public class SignInFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "RegistrationFragment";

    //身份证
    private IdentityInfo idCardInfo;//二代身份证信息
    private Bitmap headImage;//身份证头像
    private BeepManager beepManager;//bee声音

    private Context mContext;
    private LinearLayout typeTeacherLl;//教职工区域
    private LinearLayout typeStudentLl;//学生区域
    private Button idCardReadBtn;//读取身份证
    private TextView idCardHeadTv;//身份证头像
    private EditText nameEt;//姓名
    private EditText dateOfBirthEt;//出生日期
    private EditText idNumberEt;//证件号码
    private EditText addressEt;//地址
    private EditText phoneNumberEt;//电话号码
    private EditText belongingsEt;//随身物品
    private EditText organizationEt;//单位名称
    private EditText plateNumberEt;//车牌号
    private EditText remarksEt;//备注
    private RadioButton maleRb;//男
    private RadioButton femaleRb;//女
    private RadioButton typeTeacherRb;//教职工类型
    private RadioButton typeStudentRb;//学生类型
    private Spinner credentialsSpinner;//证件类型
    private Spinner reasonSpinner;//事由类型
    private Spinner visitorNumberSpinner;//访客人数

    public SignInFragment() {
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mContext = getActivity();
        initView(view);
        return view;
    }

    public void initView(View view) {
        typeTeacherLl = view.findViewById(R.id.type_teacher_ll);
        typeStudentLl = view.findViewById(R.id.type_student_ll);
        Button saveBtn = view.findViewById(R.id.save_btn);
        Button printTapeBtn = view.findViewById(R.id.print_tape_btn);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);
        idCardReadBtn = view.findViewById(R.id.id_card_read_btn);
        idCardHeadTv = view.findViewById(R.id.id_card_head_tv);
        nameEt = view.findViewById(R.id.name_et);
        dateOfBirthEt = view.findViewById(R.id.date_of_birth_et);
        idNumberEt = view.findViewById(R.id.id_number_et);
        addressEt = view.findViewById(R.id.address_et);
        phoneNumberEt = view.findViewById(R.id.phone_number_et);
        belongingsEt = view.findViewById(R.id.belongings_et);
        organizationEt = view.findViewById(R.id.organization_et);
        plateNumberEt = view.findViewById(R.id.plate_number_et);
        remarksEt = view.findViewById(R.id.remarks_et);
        maleRb = view.findViewById(R.id.male_rb);
        femaleRb = view.findViewById(R.id.female_rb);
        typeTeacherRb = view.findViewById(R.id.type_teacher_rb);
        typeStudentRb = view.findViewById(R.id.type_student_rb);
        credentialsSpinner = view.findViewById(R.id.credentials_spinner);
        reasonSpinner = view.findViewById(R.id.reason_spinner);
        visitorNumberSpinner = view.findViewById(R.id.visitor_number_spinner);

        saveBtn.setOnClickListener(this);
        printTapeBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        idCardReadBtn.setOnClickListener(this);
        typeTeacherRb.setOnCheckedChangeListener(this);
        typeStudentRb.setOnCheckedChangeListener(this);
        //证件类型
        ArrayAdapter<String> credentialAadapter = new ArrayAdapter<>(mContext, R.layout.visitor_spinner_item, getResources().getStringArray(R.array.credentials_type));
        credentialAadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        credentialsSpinner.setAdapter(credentialAadapter);
        //访问事由类型
        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<>(mContext, R.layout.visitor_spinner_item, getResources().getStringArray(R.array.reason_type));
        reasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonSpinner.setAdapter(reasonAdapter);
        //随行人数类型
        ArrayAdapter<String> visitorNumberAdapter = new ArrayAdapter<>(mContext, R.layout.visitor_spinner_item, getResources().getStringArray(R.array.visitor_number));
        visitorNumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        visitorNumberSpinner.setAdapter(visitorNumberAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        beepManager = new BeepManager(getActivity(), R.raw.beep);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    IdCard.open(getActivity());
                } catch (TelpoException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            idCardReadBtn.setEnabled(false);
                            ToastUtils.showMessage(mContext, R.string.identify_read_fail);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        beepManager.close();
        beepManager = null;
        IdCard.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn://保存
                break;
            case R.id.print_tape_btn://显示凭条
                showPrintTape();
                break;
            case R.id.cancel_btn:
                getActivity().finish();
                break;
            case R.id.id_card_read_btn://读取身份证
                clearVisitorInfo();
                new GetIDInfoTask().execute();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (typeTeacherRb.isChecked()) {
            typeTeacherLl.setVisibility(View.VISIBLE);
            typeStudentLl.setVisibility(View.GONE);
        }
        if (typeStudentRb.isChecked()) {
            typeTeacherLl.setVisibility(View.GONE);
            typeStudentLl.setVisibility(View.VISIBLE);
        }

    }

    private class GetIDInfoTask extends AsyncTask<Void, Integer, TelpoException> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            //在execute被调用后立即执行
            super.onPreExecute();
            idCardReadBtn.setEnabled(false);
            dialog = new ProgressDialog(mContext);
            dialog.setTitle("操作中");
            dialog.setMessage("连接读卡器...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected TelpoException doInBackground(Void... voids) {
            //在onPreExecute()完成后立即执行
            TelpoException result = null;
            try {
                publishProgress(1);
                idCardInfo = IdCard.checkIdCard(1600);// luyq modify
                if (idCardInfo != null) {
                    byte[] image = IdCard.getIdCardImage();
                    headImage = IdCard.decodeIdCardImage(image);
                }
            } catch (TelpoException e) {
                e.printStackTrace();
                result = e;
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //在调用publishProgress时此方法被执行
            super.onProgressUpdate(values);
            if (values[0] == 1) {
                dialog.setMessage("获取身份证信息");
            }
        }

        @Override
        protected void onPostExecute(TelpoException result) {
            //当后台操作结束时，此方法将会被调用
            super.onPostExecute(result);
            dialog.dismiss();
            idCardReadBtn.setEnabled(true);
            if (result == null) {
                inputIdCardInfo();
            } else {
                String errorStr = result.toString();
                if (errorStr.equals("com.telpo.tps550.api.TimeoutException")) {
                    errorStr = "超时，请重新尝试";
                } else if (errorStr.equals("com.telpo.tps550.api.DeviceNotOpenException")) {
                    errorStr = "读卡器未打开";
                }
                DialogUtils.showAlert(mContext, errorStr);
            }
        }
    }

    /**
     * 清空访客信息
     */
    public void clearVisitorInfo() {
        idCardInfo = null;
        headImage = null;
        idCardHeadTv.setText(getResources().getString(R.string.id_card_image));
        nameEt.setText("");
        dateOfBirthEt.setText("");
        idNumberEt.setText("");
        addressEt.setText("");
        phoneNumberEt.setText("");
        belongingsEt.setText("");
        organizationEt.setText("");
        plateNumberEt.setText("");
        remarksEt.setText("");
    }

    /**
     * 输入身份证信息
     */
    public void inputIdCardInfo() {
        if (beepManager != null) {
            beepManager.playBeepSoundAndVibrate();
        }
        credentialsSpinner.setSelection(0, true);
        ImageSpan imgSpan = new ImageSpan(mContext, headImage);
        SpannableString spanString = new SpannableString("icon");
        spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        idCardHeadTv.setText(spanString);
        nameEt.setText(idCardInfo.getName());
        String sex = idCardInfo.getSex();
        if ("男 / M".equals(sex)) {
            maleRb.setChecked(true);
            femaleRb.setChecked(false);
        } else if ("女 / F".equals(sex)) {
            maleRb.setChecked(false);
            femaleRb.setChecked(true);
        }
        dateOfBirthEt.setText(idCardInfo.getBorn());
        idNumberEt.setText(idCardInfo.getNo());
        addressEt.setText(idCardInfo.getAddress());
        Log.i(TAG, "---身份证---" + "\n"
                + "姓名：" + idCardInfo.getName() + "\n"
                + "性别：" + idCardInfo.getSex() + "\n"
                + "民族：" + idCardInfo.getNation() + "\n"
                + "出生日期：" + idCardInfo.getBorn() + "\n"
                + "地址：" + idCardInfo.getAddress() + "\n"
                + "签发机关：" + idCardInfo.getApartment() + "\n"
                + "有效期限：" + idCardInfo.getPeriod() + "\n"
                + "身份证号码：" + idCardInfo.getNo() + "\n"
                + "国籍或所在地区代码：" + idCardInfo.getCountry() + "\n"
                + "中文姓名：" + idCardInfo.getCn_name() + "\n"
                + "证件类型：" + idCardInfo.getCard_type() + "\n"
                + "保留信息：" + idCardInfo.getReserve());
    }

    /**
     * 显示打印凭条dialog
     */
    public void showPrintTape() {
        //访客单
        PrintForm printForm = new PrintForm();
        printForm.setFormId("2017321466841265");

        printForm.setVisName(nameEt.getText().toString());
        if (maleRb.isChecked()) {
            printForm.setVisGender(0);
        } else {
            printForm.setVisGender(1);
        }
        printForm.setVisOrg(organizationEt.getText().toString());
        printForm.setVisReason(reasonSpinner.getSelectedItem().toString());

        printForm.setUserType(1);
        printForm.setUserName("张三");
        printForm.setUserGradeName("一年级");
        printForm.setUserClassName("1303班");
        printForm.setUserHeadMaster("王浩");

        printForm.setRegisterName("刘室温");
        printForm.setRemarks(remarksEt.getText().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        printForm.setEntryTime(sdf.format(curDate));
        Intent intent = new Intent(mContext, PrinterActivity.class);
        intent.putExtra("printForm", printForm);
        startActivity(intent);
    }
}
