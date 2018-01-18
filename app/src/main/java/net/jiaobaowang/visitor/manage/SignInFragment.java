package net.jiaobaowang.visitor.manage;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.other.BeepManager;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.idcard.IdCard;
import com.telpo.tps550.api.idcard.IdentityInfo;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.common.VisitorConfig;
import net.jiaobaowang.visitor.common.VisitorConstant;
import net.jiaobaowang.visitor.entity.AddFormResult;
import net.jiaobaowang.visitor.printer.PrinterActivity;
import net.jiaobaowang.visitor.utils.DialogUtils;
import net.jiaobaowang.visitor.utils.ToastUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * 访客登记
 */
public class SignInFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "SignInFragment";

    private boolean isNeedPrint = false;//是否需要打印
    private IdentityInfo idCardInfo;//二代身份证信息
    private Bitmap headImage;//身份证头像
    private BeepManager beepManager;//bee声音
    private ArrayAdapter<String> departmentAdapter, gradeAdapter, classesAdapter, teacherNameAdapter, studentNameAdapter, headMasterAdapter;
    private FormBody.Builder params;//保存的数据
    private OkHttpClient mOkHttpClient = new OkHttpClient();

    private Context mContext;
    private LinearLayout typeTeacherLL;//教职工区域
    private LinearLayout typeStudentLL;//学生区域
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
    private AutoCompleteTextView credentialsTypeAc;//证件类型
    private AutoCompleteTextView reasonAc;//访问事由
    private AutoCompleteTextView visitorNumberAc;//访客人数
    private AutoCompleteTextView departmentAc;//教职工部门
    private AutoCompleteTextView teacherNameAc;//教职工姓名
    private AutoCompleteTextView gradeAc;//年级
    private AutoCompleteTextView classesAc;//班级
    private AutoCompleteTextView studentNameAc;//学生姓名
    private AutoCompleteTextView headMasterAc;//班主任姓名


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

    private void initView(View view) {
        view.findViewById(R.id.save_btn).setOnClickListener(this);
        view.findViewById(R.id.print_tape_btn).setOnClickListener(this);
        view.findViewById(R.id.cancel_btn).setOnClickListener(this);
        view.findViewById(R.id.department_tv).setOnClickListener(this);
        view.findViewById(R.id.teacher_name_tv).setOnClickListener(this);
        view.findViewById(R.id.grade_tv).setOnClickListener(this);
        view.findViewById(R.id.classes_tv).setOnClickListener(this);
        view.findViewById(R.id.student_name_tv).setOnClickListener(this);
        view.findViewById(R.id.credentials_type_tv).setOnClickListener(this);
        view.findViewById(R.id.reason_tv).setOnClickListener(this);
        view.findViewById(R.id.visitor_number_tv).setOnClickListener(this);
        view.findViewById(R.id.head_master_tv).setOnClickListener(this);
        typeTeacherLL = view.findViewById(R.id.type_teacher_ll);
        typeStudentLL = view.findViewById(R.id.type_student_ll);

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
        visitorNumberAc = view.findViewById(R.id.visitor_number_ac);
        credentialsTypeAc = view.findViewById(R.id.credentials_type_ac);
        reasonAc = view.findViewById(R.id.reason_ac);
        departmentAc = view.findViewById(R.id.department_ac);
        gradeAc = view.findViewById(R.id.grade_ac);
        classesAc = view.findViewById(R.id.classes_ac);
        teacherNameAc = view.findViewById(R.id.teacher_name_ac);
        studentNameAc = view.findViewById(R.id.student_name_ac);
        headMasterAc = view.findViewById(R.id.head_master_ac);
        idCardReadBtn.setOnClickListener(this);
        typeTeacherRb.setOnCheckedChangeListener(this);
        typeStudentRb.setOnCheckedChangeListener(this);
        //证件类型
        String[] credentialsType = getResources().getStringArray(R.array.credentials_type);
        ArrayAdapter<String> credentialAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item, credentialsType);
        credentialsTypeAc.setAdapter(credentialAdapter);
        //访问事由类型
        String[] reason = getResources().getStringArray(R.array.reason_type);
        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item, reason);
        reasonAc.setAdapter(reasonAdapter);
        //随行人数类型
        String[] visitorNumber = getResources().getStringArray(R.array.visitor_number);
        ArrayAdapter<String> visitorNumberAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item, visitorNumber);
        visitorNumberAc.setAdapter(visitorNumberAdapter);
        //部门
        departmentAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item);
        departmentAc.setAdapter(departmentAdapter);
        //教职工姓名
        teacherNameAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item);
        teacherNameAc.setAdapter(teacherNameAdapter);
        //年级
        gradeAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item);
        gradeAc.setAdapter(gradeAdapter);
        //班级
        classesAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item);
        classesAc.setAdapter(classesAdapter);
        //学生姓名
        studentNameAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item);
        studentNameAc.setAdapter(studentNameAdapter);
        //班主任
        headMasterAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item);
        headMasterAc.setAdapter(headMasterAdapter);
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
                isNeedPrint = false;
                checkSaveData();
                break;
            case R.id.print_tape_btn://保存并打印
                isNeedPrint = true;
                checkSaveData();
                break;
            case R.id.cancel_btn:
                getActivity().finish();
                break;
            case R.id.id_card_read_btn://读取身份证
                clearVisitorInfo();
                new GetIDInfoTask().execute();
                break;
            case R.id.teacher_name_tv:
                teacherNameAc.showDropDown();
                break;
            case R.id.department_tv:
                departmentAc.showDropDown();
                break;
            case R.id.grade_tv:
                gradeAc.showDropDown();
                break;
            case R.id.classes_tv:
                classesAc.showDropDown();
                break;
            case R.id.student_name_tv:
                studentNameAc.showDropDown();
                break;
            case R.id.credentials_type_tv:
                credentialsTypeAc.showDropDown();
                break;
            case R.id.reason_tv:
                reasonAc.showDropDown();
                break;
            case R.id.visitor_number_tv:
                visitorNumberAc.showDropDown();
                break;
            case R.id.head_master_tv:
                headMasterAc.showDropDown();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (typeTeacherRb.isChecked()) {
            //显示教职工
            typeTeacherLL.setVisibility(View.VISIBLE);
            typeStudentLL.setVisibility(View.GONE);
        }
        if (typeStudentRb.isChecked()) {
            //显示学生
            typeTeacherLL.setVisibility(View.GONE);
            typeStudentLL.setVisibility(View.VISIBLE);
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
    private void clearVisitorInfo() {
        idCardInfo = null;
        headImage = null;
        idCardHeadTv.setText(getResources().getString(R.string.id_card_image));
        nameEt.setText("");
        maleRb.setChecked(true);
        femaleRb.setChecked(false);
        dateOfBirthEt.setText("");
        credentialsTypeAc.setText("");
        idNumberEt.setText("");
        addressEt.setText("");
        reasonAc.setText("");
        phoneNumberEt.setText("");
        visitorNumberAc.setText("");
        belongingsEt.setText("");
        organizationEt.setText("");
        plateNumberEt.setText("");
        remarksEt.setText("");
    }

    /**
     * 清除教职工和学生信息
     */
    private void clearUserInfo() {
        departmentAc.setText("");
        teacherNameAc.setText("");
        gradeAc.setText("");
        classesAc.setText("");
        studentNameAc.setText("");
        headMasterAc.setText("");
    }

    /**
     * 输入身份证信息
     */
    private void inputIdCardInfo() {
        if (beepManager != null) {
            beepManager.playBeepSoundAndVibrate();
        }
        credentialsTypeAc.setText("身份证");
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
     * 验证需要保存的信息
     */
    private void checkSaveData() {
        if ("".equals(nameEt.getText().toString().trim())) {
            DialogUtils.showAlert(mContext, "请输入访客姓名");
            return;
        }
        if ("".equals(reasonAc.getText().toString().trim())) {
            DialogUtils.showAlert(mContext, "请输入访问事由");
            return;
        }
        if ("".equals(visitorNumberAc.getText().toString().trim())) {
            DialogUtils.showAlert(mContext, "请输入随行人数");
            return;
        }
        if (typeTeacherRb.isChecked()) {
            //教职工
            if ("".equals(teacherNameAc.getText().toString().trim())) {
                DialogUtils.showAlert(mContext, "请输入教职工姓名");
                return;
            }
        } else {
            if ("".equals(studentNameAc.getText().toString().trim())) {
                DialogUtils.showAlert(mContext, "请输入学生姓名");
                return;
            }
            if ("".equals(headMasterAc.getText().toString().trim())) {
                DialogUtils.showAlert(mContext, "请输入班主任姓名");
                return;
            }
        }
        setSubmitData();
    }

    /**
     * 设置需要保存的数据
     */
    private void setSubmitData(){
        params = new FormBody.Builder();
        SharedPreferences sp = getActivity().getSharedPreferences(VisitorConfig.VISIT_LOCAL_STORAGE, MODE_PRIVATE);
        String token = sp.getString(VisitorConfig.VISIT_LOCAL_TOKEN, "");
        params.add("token", token);
        //访客姓名
        params.add("visitor_name", nameEt.getText().toString().trim());
        //访客性别
        String visitor_sex="0";
        if (femaleRb.isChecked()) {
            visitor_sex = "1";
        }
        params.add("visitor_sex", visitor_sex);
        //访问事由
        params.add("visitor_for", reasonAc.getText().toString().trim());
        //随行人数
        params.add("visitor_counter", visitorNumberAc.getText().toString().trim());
        if (typeTeacherRb.isChecked()) {
            //教职工
            params.add("interviewee_type", "0");
            params.add("teacher_name", teacherNameAc.getText().toString().trim());

            String department_name = departmentAc.getText().toString().trim();
            if (!"".equals(department_name)) {
                params.add("department_name", department_name);
            }
        } else {
            params.add("interviewee_type", "1");
            params.add("student_name", studentNameAc.getText().toString().trim());
            params.add("head_teacher_name", headMasterAc.getText().toString().trim());
            String grade_name = gradeAc.getText().toString().trim();
            if (!"".equals(grade_name)) {
                params.add("grade_name", grade_name);
            }
            String class_name = classesAc.getText().toString().trim();
            if (!"".equals(class_name)) {
                params.add("class_name", class_name);
            }
        }
        //出生日期
        String visitor_birthday = dateOfBirthEt.getText().toString().trim();
        if (!"".equals(visitor_birthday)) {
            params.add("visitor_birthday", visitor_birthday);
        }
        //证件类型
        String certificate_type = credentialsTypeAc.getText().toString().trim();
        if (!"".equals(certificate_type)) {
            params.add("certificate_type", certificate_type);
        }
        //证件号码
        String certificate_Int = idNumberEt.getText().toString().trim();
        if (!"".equals(certificate_Int)) {
            params.add("certificate_Int", certificate_Int);
        }
        //地址
        String address = addressEt.getText().toString().trim();
        if (!"".equals(address)) {
            params.add("address", address);
        }
        //备注
        String note = remarksEt.getText().toString().trim();
        if (!"".equals(note)) {
            params.add("note", note);
        }
        //手机
        String visitor_phone=phoneNumberEt.getText().toString().trim();
        if (!"".equals(visitor_phone)) {
            params.add("visitor_phone", visitor_phone);
        }
        //随身物品
        String visitor_goods = belongingsEt.getText().toString().trim();
        if (!"".equals(visitor_goods)) {
            params.add("visitor_goods", visitor_goods);
        }
        //来访单位
        String unit_name = organizationEt.getText().toString().trim();
        if (!"".equals(unit_name)) {
            params.add("unit_name", unit_name);
        }
        //车牌号
        String plate_Int = plateNumberEt.getText().toString().trim();
        if (!"".equals(plate_Int)) {
            params.add("plate_Int", plate_Int);
        }
        //进入时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String in_time = sdf.format(curDate);
        params.add("in_time", in_time);
        new SubmitDataTask().execute();
    }

    private class SubmitDataTask extends AsyncTask<Void, Void, String[]> {
        ProgressDialog submitDataDialog;

        @Override
        protected void onPreExecute() {
            submitDataDialog = new ProgressDialog(mContext);
            submitDataDialog.setMessage("正在提交数据，请等待...");
            submitDataDialog.setCancelable(false);
            submitDataDialog.show();
        }

        @Override
        protected String[] doInBackground(Void... Void) {
            String result[] = new String[2];
            try {
                Request request = new Request.Builder()
                        .url(VisitorConfig.VISITOR_API_ADD)
                        .post(params.build())
                        .build();
                Response response = mOkHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    result[0] = "1";
                    result[1] = response.body().string();
                    return result;
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (Exception e) {
                e.printStackTrace();

                result[0] = "0";
                result[1] = e.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String resultStr[]) {
            submitDataDialog.dismiss();
            Log.i(TAG, resultStr[0] + " " + resultStr[1]);
            if (resultStr[0].equals("1")) {
                Gson gson = new Gson();
                AddFormResult result = gson.fromJson(resultStr[1], AddFormResult.class);
                if (result.getCode().equals("0000")) {
                    ToastUtils.showMessage(mContext, "保存访客记录成功");
                    clearVisitorInfo();
                    clearUserInfo();
                    if (isNeedPrint) {
                        Intent intent = new Intent(mContext, PrinterActivity.class);
                        intent.putExtra(VisitorConstant.INTENT_PUT_EXTRA_DATA, result.getVisitor());
                        startActivity(intent);
                    }
                } else {
                    DialogUtils.showAlert(mContext, "保存访客记录失败：" + result.getMsg());
                }
            } else {
                DialogUtils.showAlert(mContext, "保存访客记录失败：" + resultStr[1]);
            }
        }
    }
}
