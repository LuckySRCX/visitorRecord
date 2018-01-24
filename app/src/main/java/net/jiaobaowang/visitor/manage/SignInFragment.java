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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.telpo.tps550.api.idcard.IdentityInfo;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.common.VisitorConfig;
import net.jiaobaowang.visitor.common.VisitorConstant;
import net.jiaobaowang.visitor.entity.AddFormResult;
import net.jiaobaowang.visitor.entity.QiNiuCommand;
import net.jiaobaowang.visitor.entity.SchoolClassModel;
import net.jiaobaowang.visitor.entity.SchoolClassStuModel;
import net.jiaobaowang.visitor.entity.SchoolClassStuResult;
import net.jiaobaowang.visitor.entity.SchoolClassTeaModel;
import net.jiaobaowang.visitor.entity.SchoolClassTeaResult;
import net.jiaobaowang.visitor.entity.SchoolDepartModel;
import net.jiaobaowang.visitor.entity.SchoolDepartResult;
import net.jiaobaowang.visitor.entity.SchoolDepartUserModel;
import net.jiaobaowang.visitor.entity.SchoolDepartUserResult;
import net.jiaobaowang.visitor.entity.SchoolGradeClassResult;
import net.jiaobaowang.visitor.entity.SchoolGradeModel;
import net.jiaobaowang.visitor.entity.SchoolGradeResult;
import net.jiaobaowang.visitor.printer.PrinterActivity;
import net.jiaobaowang.visitor.utils.DialogUtils;
import net.jiaobaowang.visitor.utils.EncryptUtil;
import net.jiaobaowang.visitor.utils.ToastUtils;
import net.jiaobaowang.visitor.utils.Tools;
import net.jiaobaowang.visitor.visitor_interface.OnGetIdentityInfoListener;
import net.jiaobaowang.visitor.visitor_interface.OnGetIdentityInfoResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * 访客登记
 */
public class SignInFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, OnGetIdentityInfoResult {
    private static final String TAG = "SignInFragment";
    private static final String REQUEST_FLAG_DEPARTMENT = "0";//部门
    private static final String REQUEST_FLAG_DEPARTMENT_USER = "1";//教职工
    private static final String REQUEST_FLAG_GRADE = "2";//年级
    private static final String REQUEST_FLAG_CLASS = "3";//班级
    private static final String REQUEST_FLAG_CLASS_STUDENT = "4";//学生
    private static final String REQUEST_FLAG_CLASS_TEACHER = "5";//任课老师
    private SignInTask teacherTask;
    private SignInTask studentTask;
    private SignInTask headMasterTask;
    private SchoolDepartModel selectDepart;//选取的部门
    private SchoolDepartUserModel selectTeacher;//选取的教职工
    private SchoolGradeModel selectGrade;//选取的年级
    private SchoolClassModel selectClass;//选取的班级
    private SchoolClassStuModel selectStudent;//选取的学生
    private SchoolClassTeaModel selectHeadMaster;//选取的班主任
    private boolean isNeedPrint = false;//是否需要打印
    private IdentityInfo idCardInfo;//二代身份证信息
    private Bitmap headImage;//身份证头像

    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private OnGetIdentityInfoListener onGetIdentityInfoListener;

    private Context mContext;
    private LinearLayout typeTeacherLL, typeStudentLL;//教职工区域//学生区域
    private Button idCardReadBtn;//读取身份证
    private TextView idCardHeadTv;//身份证头像
    private EditText nameEt, dateOfBirthEt, idNumberEt, addressEt, phoneNumberEt;//姓名//出生日期//证件号码//地址//电话号码
    private EditText belongingsEt, organizationEt, plateNumberEt, remarksEt;//随身物品//单位名称//车牌号//备注
    private RadioButton maleRb, femaleRb, typeTeacherRb, typeStudentRb;//男//女//教职工类型//学生类型
    private AutoCompleteTextView credentialsTypeAc, reasonAc, visitorNumberAc;//证件类型//访问事由//访客人数
    private AutoCompleteTextView departmentAc, teacherNameAc;//教职工部门//教职工姓名
    private AutoCompleteTextView gradeAc, classesAc, studentNameAc, headMasterAc;//年级//班级//学生姓名//班主任姓名
    private ArrayAdapter<SchoolDepartModel> departmentAdapter;
    private ArrayAdapter<SchoolDepartUserModel> teacherNameAdapter;
    private ArrayAdapter<SchoolGradeModel> gradeAdapter;
    private ArrayAdapter<SchoolClassModel> classesAdapter;
    private ArrayAdapter<SchoolClassStuModel> studentNameAdapter;
    private ArrayAdapter<SchoolClassTeaModel> headMasterAdapter;

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
        initData();
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
        departmentAc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                teacherTask.cancel(true);
                selectDepart = (SchoolDepartModel) parent.getItemAtPosition(position);
                teacherNameAdapter.clear();
                Log.i(TAG, "点击部门：" + selectDepart.getDptid() + " " + selectDepart.getDptname());
                teacherTask = new SignInTask(REQUEST_FLAG_DEPARTMENT_USER, String.valueOf(selectDepart.getDptid()));
                teacherTask.execute();
            }
        });

        //教职工姓名
        teacherNameAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item);
        teacherNameAc.setAdapter(teacherNameAdapter);
        teacherNameAc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectTeacher = (SchoolDepartUserModel) parent.getItemAtPosition(position);
            }
        });
        //年级
        gradeAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item);
        gradeAc.setAdapter(gradeAdapter);
        gradeAc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                studentTask.cancel(true);
                selectGrade = (SchoolGradeModel) parent.getItemAtPosition(position);
                classesAdapter.clear();
                studentNameAdapter.clear();
                headMasterAdapter.clear();
                Log.i(TAG, "点击年级：" + selectGrade.getGrdcode() + " " + selectGrade.getGrdname());
                studentTask = new SignInTask(REQUEST_FLAG_CLASS, String.valueOf(selectGrade.getGrdcode()));
                studentTask.execute();
            }
        });
        //班级
        classesAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item);
        classesAc.setAdapter(classesAdapter);
        classesAc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                studentTask.cancel(true);
                selectClass = (SchoolClassModel) parent.getItemAtPosition(position);
                studentNameAdapter.clear();
                headMasterAdapter.clear();
                Log.i(TAG, "点击班级：" + selectClass.getClsid() + " " + selectClass.getClsname());
                studentTask = new SignInTask(REQUEST_FLAG_CLASS_STUDENT, String.valueOf(selectClass.getClsid()));
                studentTask.execute();
                headMasterTask = new SignInTask(REQUEST_FLAG_CLASS_TEACHER, String.valueOf(selectClass.getClsid()));
                headMasterTask.execute();
            }
        });
        //学生姓名
        studentNameAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item);
        studentNameAc.setAdapter(studentNameAdapter);
        studentNameAc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectStudent = (SchoolClassStuModel) parent.getItemAtPosition(position);
            }
        });
        //班主任
        headMasterAdapter = new ArrayAdapter<>(mContext, R.layout.visit_drop_down_item);
        headMasterAc.setAdapter(headMasterAdapter);
        headMasterAc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectHeadMaster = (SchoolClassTeaModel) parent.getItemAtPosition(position);
            }
        });
        onGetIdentityInfoListener = (OnGetIdentityInfoListener) getActivity();
    }

    private void initData() {
        teacherTask = new SignInTask(REQUEST_FLAG_DEPARTMENT, "");
        teacherTask.execute();
        studentTask = new SignInTask(REQUEST_FLAG_GRADE, "");
        studentTask.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn://保存
                isNeedPrint = false;
                checkSaveData();
                //new GetQiNiuTokenTask().execute();
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
                onGetIdentityInfoListener.getIdentityInfo();
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

    /**
     * 返回身份证信息和身份证头像
     *
     * @param code          0，失败；1，成功
     * @param msg           失败信息
     * @param identityInfo  身份证信息
     * @param identityImage 身份证头像
     */
    @Override
    public void getIdentityInfoResult(int code, String msg, IdentityInfo identityInfo, Bitmap identityImage) {
        Log.i(TAG, "getIdentityInfoResult");
        if (code == 0) {
            DialogUtils.showAlert(mContext, msg);
        } else {
            idCardInfo = identityInfo;
            headImage = identityImage;
            inputIdCardInfo();
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
    private void setSubmitData() {
        FormBody.Builder params = new FormBody.Builder();
        SharedPreferences sp = getActivity().getSharedPreferences(VisitorConfig.VISIT_LOCAL_STORAGE, MODE_PRIVATE);
        String token = sp.getString(VisitorConfig.VISIT_LOCAL_TOKEN, "");
        params.add("token", token);
        //访客姓名
        params.add("visitor_name", nameEt.getText().toString().trim());
        //访客性别
        String visitor_sex = "0";
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
        String visitor_phone = phoneNumberEt.getText().toString().trim();
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
        new SubmitDataTask(params).execute();
    }

    private class SubmitDataTask extends AsyncTask<Void, Void, String[]> {
        private FormBody.Builder params;
        ProgressDialog submitDataDialog;

        SubmitDataTask(FormBody.Builder params) {
            this.params = params;
        }

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

    private class GetQiNiuTokenTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... Void) {
            Log.i(TAG, "doInBackground");
            String result[] = new String[2];
            String Key = "idcardimage/" + System.currentTimeMillis() + (int) (Math.random() * 1000) + ".jpg";

            QiNiuCommand command = new QiNiuCommand("pb", "idcardimage/1234567890.jpg", "", "");
            List<QiNiuCommand> commands = new ArrayList<>();
            commands.add(command);
            Gson gson = new Gson();
            String commandJson = gson.toJson(commands);
            Log.i(TAG, "commandJson:" + commandJson);
            try {
                String desStr = EncryptUtil.desEncrypt(commandJson, "jsy01170");
                Log.i(TAG, "desStr1:" + desStr);
                String Param = stringToHexString(desStr);
                Log.i(TAG, "Param:" + Param);
                //Param = "B4B4B921A4AE7A434DC7C1BBF11ACD25EB3C58227E55A06BF94DE8F359CE400FA8414A9F5B20BF34DF4405D29F4D3270D00465D3FB48799968DE48798B34AD2C1019C0C72FA8FD19CC4FEA9E6F3253C9394BA0C064770E62F5B751343FED28403B990CD71B176B605A3F750D3B07CC4579F305020351F295B3874589B5B3948F83B4C244B64815F23BB727A5C4DD4C0CC253395868D4CA4D38A7739D46152AC6";
                Log.i(TAG, "Url:" + VisitorConfig.QINIU_GET_UPLOAD_TOKEN);
                TreeMap<String, String> map = new TreeMap<>();
                map.put("AppID", "10");
                map.put("Param", Param);
                String json = gson.toJson(map);
                Log.i(TAG, "json:" + json);
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(VisitorConfig.QINIU_GET_UPLOAD_TOKEN)
                        .post(body)
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
            Log.i(TAG, "onPostExecute:" + resultStr[0] + " " + resultStr[1]);
        }
    }

    /**
     * 字符串转16进制字符串
     *
     * @param strPart 字符串
     * @return 16进制字符串
     */
    public static String stringToHexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }

    private class SignInTask extends AsyncTask<Void, Void, String[]> {
        private String flag;
        private String data;

        SignInTask(String flag, String data) {
            this.flag = flag;
            this.data = data;
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground:");
            String result[] = new String[2];
            String url = "";
            SharedPreferences sp = getActivity().getSharedPreferences(VisitorConfig.VISIT_LOCAL_STORAGE, MODE_PRIVATE);
            String utoken = sp.getString(VisitorConfig.VISIT_LOCAL_TOKEN, "");
            TreeMap<String, String> map = new TreeMap<>();
            map.put("uuid", Tools.getDeviceId(getActivity()));
            map.put("appid", Tools.getAppId(getActivity()));
            map.put("utoken", utoken);
            switch (flag) {
                case REQUEST_FLAG_DEPARTMENT:
                    Log.i(TAG, "doInBackground:获取部门");
                    url = VisitorConfig.VISITOR_API_DEPARTMENT;
                    break;
                case REQUEST_FLAG_DEPARTMENT_USER:
                    Log.i(TAG, "doInBackground:获取部门成员");
                    url = VisitorConfig.VISITOR_API_DEPARTMENT_USER;
                    map.put("dptids", data);
                    break;
                case REQUEST_FLAG_GRADE:
                    Log.i(TAG, "doInBackground:获取年级");
                    url = VisitorConfig.VISITOR_API_GRADE;
                    break;
                case REQUEST_FLAG_CLASS:
                    Log.i(TAG, "doInBackground:获取年级下的班级");
                    url = VisitorConfig.VISITOR_API_GRADE_CLASS;
                    map.put("gradecodes", data);
                    break;
                case REQUEST_FLAG_CLASS_STUDENT:
                    Log.i(TAG, "doInBackground:获取班级下的学生");
                    url = VisitorConfig.VISITOR_API_CLASS_STUDENT;
                    map.put("classids", data);
                    break;
                case REQUEST_FLAG_CLASS_TEACHER:
                    Log.i(TAG, "doInBackground:获取班级下的任课老师");
                    url = VisitorConfig.VISITOR_API_CLASS_TEACHER;
                    map.put("classids", data);
                    break;
            }
            try {
                map.put("sign", Tools.getSign(map));
                Gson gson = new Gson();
                String json = gson.toJson(map);
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
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
        protected void onPostExecute(String[] result) {
            Log.i(TAG, "onPostExecute:flag:" + flag + " result[0]:" + result[0] + " result[1]:" + result[1]);
            switch (flag) {
                case REQUEST_FLAG_DEPARTMENT://部门
                    if ("1".equals(result[0])) {
                        Gson gson = new Gson();
                        SchoolDepartResult schoolDepartResult = gson.fromJson(result[1], SchoolDepartResult.class);
                        if (schoolDepartResult.getRspCode().equals("0000")) {
                            if (schoolDepartResult.getRspData() != null) {
                                List<SchoolDepartModel> departModelList = schoolDepartResult.getRspData().getDpts();
                                departmentAdapter.addAll(departModelList);
                            }
                        } else {
                            ToastUtils.showMessage(mContext, "获取部门失败：" + schoolDepartResult.getRspTxt());
                        }
                    } else {
                        ToastUtils.showMessage(mContext, "获取部门失败：" + result[1]);
                    }
                    break;
                case REQUEST_FLAG_DEPARTMENT_USER://部门成员
                    //progressDialog.dismiss();
                    if ("1".equals(result[0])) {
                        Gson gson = new Gson();
                        SchoolDepartUserResult schoolDepartUserResult = gson.fromJson(result[1], SchoolDepartUserResult.class);
                        if (schoolDepartUserResult.getRspCode().equals("0000")) {
                            if (schoolDepartUserResult.getRspData() != null) {
                                List<SchoolDepartUserModel> departUserModelList = schoolDepartUserResult.getRspData().getUsers();
                                teacherNameAdapter.addAll(departUserModelList);
                            }
                        } else {
                            ToastUtils.showMessage(mContext, "获取部门成员失败：" + schoolDepartUserResult.getRspTxt());
                        }
                    } else {
                        ToastUtils.showMessage(mContext, "获取部门成员失败：" + result[1]);
                    }
                    break;
                case REQUEST_FLAG_GRADE://年级
                    if ("1".equals(result[0])) {
                        Gson gson = new Gson();
                        SchoolGradeResult schoolGradeResult = gson.fromJson(result[1], SchoolGradeResult.class);
                        if (schoolGradeResult.getRspCode().equals("0000")) {
                            if (schoolGradeResult.getRspData() != null) {
                                List<SchoolGradeModel> gradeModelList = schoolGradeResult.getRspData().getGrds();
                                gradeAdapter.addAll(gradeModelList);
                            }
                        } else {
                            ToastUtils.showMessage(mContext, "获取年级失败：" + schoolGradeResult.getRspTxt());
                        }
                    } else {
                        ToastUtils.showMessage(mContext, "获取年级失败：" + result[1]);
                    }
                    break;
                case REQUEST_FLAG_CLASS://班级
                    if ("1".equals(result[0])) {
                        Gson gson = new Gson();
                        SchoolGradeClassResult schoolGradeClassResult = gson.fromJson(result[1], SchoolGradeClassResult.class);
                        if (schoolGradeClassResult.getRspCode().equals("0000")) {
                            if (schoolGradeClassResult.getRspData() != null) {
                                List<SchoolClassModel> schoolClassModelList = schoolGradeClassResult.getRspData().getClss();
                                classesAdapter.addAll(schoolClassModelList);
                            }
                        } else {
                            ToastUtils.showMessage(mContext, "获取班级失败：" + schoolGradeClassResult.getRspTxt());
                        }
                    } else {
                        ToastUtils.showMessage(mContext, "获取班级失败：" + result[1]);
                    }
                    break;
                case REQUEST_FLAG_CLASS_STUDENT://班级学生
                    if ("1".equals(result[0])) {
                        Gson gson = new Gson();
                        SchoolClassStuResult schoolClassStuResult = gson.fromJson(result[1], SchoolClassStuResult.class);
                        if (schoolClassStuResult.getRspCode().equals("0000")) {
                            if (schoolClassStuResult.getRspData() != null) {
                                List<SchoolClassStuModel> schoolClassModelList = schoolClassStuResult.getRspData().getClssstus();
                                studentNameAdapter.addAll(schoolClassModelList);
                            }
                        } else {
                            ToastUtils.showMessage(mContext, "获取学生失败：" + schoolClassStuResult.getRspTxt());
                        }
                    } else {
                        ToastUtils.showMessage(mContext, "获取学生失败：" + result[1]);
                    }
                    break;
                case REQUEST_FLAG_CLASS_TEACHER://班级任课老师
                    if ("1".equals(result[0])) {
                        Gson gson = new Gson();
                        SchoolClassTeaResult schoolClassTeaResult = gson.fromJson(result[1], SchoolClassTeaResult.class);
                        if (schoolClassTeaResult.getRspCode().equals("0000")) {
                            if (schoolClassTeaResult.getRspData() != null) {
                                List<SchoolClassTeaModel> schoolClassTeaModelList = schoolClassTeaResult.getRspData().getClssusers();
                                List<SchoolClassTeaModel> headMasterList = new ArrayList<>();
                                for (int i = 0; i < schoolClassTeaModelList.size(); i++) {
                                    if (schoolClassTeaModelList.get(i).getIsms() == 1) {
                                        headMasterList.add(schoolClassTeaModelList.get(i));
                                        break;
                                    }
                                }
                                headMasterAdapter.addAll(headMasterList);
                            }
                        } else {
                            ToastUtils.showMessage(mContext, "获取老师失败：" + schoolClassTeaResult.getRspTxt());
                        }
                    } else {
                        ToastUtils.showMessage(mContext, "获取老师失败：" + result[1]);
                    }
                    break;
            }
        }
    }
}
