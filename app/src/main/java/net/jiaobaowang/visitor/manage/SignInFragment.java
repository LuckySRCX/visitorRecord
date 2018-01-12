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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import net.jiaobaowang.visitor.entity.Department;
import net.jiaobaowang.visitor.entity.PrintForm;
import net.jiaobaowang.visitor.printer.PrinterActivity;
import net.jiaobaowang.visitor.utils.DialogUtils;
import net.jiaobaowang.visitor.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 访客登记
 */
public class SignInFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "SignInFragment";

    //身份证
    private IdentityInfo idCardInfo;//二代身份证信息
    private Bitmap headImage;//身份证头像
    private BeepManager beepManager;//bee声音
    private ArrayAdapter<Department> departmentAdapter, gradeAdapter, classesAdapter, teacherNameAdapter, studentNameAdapter;


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
    private Spinner credentialsSpinner;//证件类型
    private Spinner reasonSpinner;//事由类型
    private Spinner visitorNumberSpinner;//访客人数
    private Spinner departmentSpinner;//部门
    private Spinner gradeSpinner;//年级
    private Spinner classesSpinner;//班级
    private AutoCompleteTextView teacherNameAc;//教职工姓名
    private AutoCompleteTextView studentNameAc;//教职工姓名

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
        initDepartmentData();
        initGradeData();
        return view;
    }


    private void initView(View view) {
        view.findViewById(R.id.save_btn).setOnClickListener(this);
        view.findViewById(R.id.print_tape_btn).setOnClickListener(this);
        view.findViewById(R.id.cancel_btn).setOnClickListener(this);
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
        credentialsSpinner = view.findViewById(R.id.credentials_spinner);
        reasonSpinner = view.findViewById(R.id.reason_spinner);
        visitorNumberSpinner = view.findViewById(R.id.visitor_number_spinner);
        departmentSpinner = view.findViewById(R.id.department_spinner);
        gradeSpinner = view.findViewById(R.id.grade_spinner);
        classesSpinner = view.findViewById(R.id.classes_spinner);
        teacherNameAc = view.findViewById(R.id.teacher_name_ac);
        studentNameAc = view.findViewById(R.id.student_name_ac);
        idCardReadBtn.setOnClickListener(this);
        typeTeacherRb.setOnCheckedChangeListener(this);
        typeStudentRb.setOnCheckedChangeListener(this);
        //证件类型
        ArrayAdapter<String> credentialAdapter = new ArrayAdapter<>(mContext, R.layout.visitor_spinner_item, getResources().getStringArray(R.array.credentials_type));
        credentialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        credentialsSpinner.setAdapter(credentialAdapter);
        //访问事由类型
        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<>(mContext, R.layout.visitor_spinner_item, getResources().getStringArray(R.array.reason_type));
        reasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonSpinner.setAdapter(reasonAdapter);
        //随行人数类型
        ArrayAdapter<String> visitorNumberAdapter = new ArrayAdapter<>(mContext, R.layout.visitor_spinner_item, getResources().getStringArray(R.array.visitor_number));
        visitorNumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        visitorNumberSpinner.setAdapter(visitorNumberAdapter);
        //部门
        departmentAdapter = new ArrayAdapter<>(mContext, R.layout.visitor_spinner_item);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentAdapter);
        departmentSpinner.setOnItemSelectedListener(this);
        //教职工姓名
        teacherNameAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item);
        teacherNameAc.setAdapter(teacherNameAdapter);
        teacherNameAc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AutoCompleteTextView view = (AutoCompleteTextView) v;
                    view.showDropDown();
                }
            }
        });
        teacherNameAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                view.showDropDown();
            }
        });
        //年级
        gradeAdapter = new ArrayAdapter<>(mContext, R.layout.visitor_spinner_item);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(gradeAdapter);
        gradeSpinner.setOnItemSelectedListener(this);
        //班级
        classesAdapter = new ArrayAdapter<>(mContext, R.layout.visitor_spinner_item);
        classesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        classesSpinner.setAdapter(classesAdapter);
        classesSpinner.setOnItemSelectedListener(this);
        //学生姓名
        studentNameAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item);
        studentNameAc.setAdapter(studentNameAdapter);
        studentNameAc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AutoCompleteTextView view = (AutoCompleteTextView) v;
                    view.showDropDown();
                }
            }
        });
    }

    /**
     * 获取部门列表
     */
    private void initDepartmentData() {
        List<Department> data = new ArrayList<>();
        data.add(new Department("0", "行政部"));
        data.add(new Department("1", "开发部"));
        data.add(new Department("2", "财务部"));
        data.add(new Department("3", "工程部"));
        departmentAdapter.addAll(data);
    }

    /**
     * 获取年级列表
     */
    private void initGradeData() {
        List<Department> data = new ArrayList<>();
        data.add(new Department("1", "1年级"));
        data.add(new Department("2", "2年级"));
        data.add(new Department("3", "3年级"));
        data.add(new Department("4", "4年级"));
        data.add(new Department("5", "5年级"));
        data.add(new Department("6", "6年级"));
        gradeAdapter.addAll(data);
    }

    /**
     * 获取班级列表
     */
    private void initClassesData() {
        List<Department> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(new Department(i + "", i + "班"));
        }
        classesAdapter.addAll(data);
    }

    /**
     * 获取老师姓名列表
     */
    private void initTeacherNameData() {
        List<Department> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(new Department(i + "", "教职工" + i));
        }
        teacherNameAdapter.addAll(data);
    }

    /**
     * 获取学生姓名列表
     */
    private void initStudentNameData() {
        List<Department> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(new Department(i + "", "学生" + i));
        }
        teacherNameAdapter.addAll(data);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.department_spinner:
                Department dpart = (Department) parent.getSelectedItem();
                Log.i(TAG, "------onItemSelected:部门---" + dpart.toString() + "------");
                teacherNameAdapter.clear();
                initTeacherNameData();
                teacherNameAc.setText("");
                break;
            case R.id.grade_spinner:
                Department grade = (Department) parent.getSelectedItem();
                Log.i(TAG, "------onItemSelected:年级---" + grade.toString() + "------");
                classesAdapter.clear();
                initClassesData();
                classesSpinner.setSelection(0);
                break;
            case R.id.classes_spinner:
                Department classes = (Department) parent.getSelectedItem();
                Log.i(TAG, "------onItemSelected:班级---" + classes.toString() + "------");
                studentNameAdapter.clear();
                initStudentNameData();
                studentNameAc.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.i(TAG, "------onNothingSelected------");
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
    private void inputIdCardInfo() {
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
    private void showPrintTape() {
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
