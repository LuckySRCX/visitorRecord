package net.jiaobaowang.visitor.printer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.common.VisitorConstant;
import net.jiaobaowang.visitor.entity.VisitRecord;

public class VisitorFormDetailsActivity extends AppCompatActivity {
    private VisitRecord visitRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitRecord = (VisitRecord) getIntent().getSerializableExtra(VisitorConstant.INTENT_PUT_EXTRA_DATA);
        setContentView(R.layout.activity_visitor_form_details);
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.print_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisitorFormDetailsActivity.this, PrinterActivity.class);
                intent.putExtra(VisitorConstant.INTENT_PUT_EXTRA_DATA, visitRecord);
                startActivity(intent);
            }
        });
        //访客姓名
        ((EditText) findViewById(R.id.visitor_name_et)).setText(visitRecord.getVisitor_name());
        //访客性别
        if (visitRecord.getVisitor_sex() == 0) {
            ((EditText) findViewById(R.id.visitor_gender_et)).setText("男");
        } else {
            ((EditText) findViewById(R.id.visitor_gender_et)).setText("女");
        }
        //访客出生日期
        ((EditText) findViewById(R.id.visitor_born_et)).setText(visitRecord.getVisitor_birthday());
        //证件类型
        if (visitRecord.getCertificate_type() != null) {
            ((EditText) findViewById(R.id.visitor_credentials_type_et)).setText(visitRecord.getCertificate_type());
        }
        //证件号码
        ((EditText) findViewById(R.id.visitor_credentials_type_et)).setText(visitRecord.getCertificate_number());
        //访客单号
        ((EditText) findViewById(R.id.visitor_form_id_et)).setText(String.valueOf(visitRecord.getId()));
        //访问事由
        ((EditText) findViewById(R.id.visitor_reason_et)).setText(visitRecord.getVisitor_for());
        //手机
        ((EditText) findViewById(R.id.visitor_phone_number_et)).setText(visitRecord.getVisitor_phone());
        //随行人数
        ((EditText) findViewById(R.id.visitor_number_et)).setText(visitRecord.getVisitor_counter());
        //随身物品
        ((EditText) findViewById(R.id.visitor_belongings_et)).setText(visitRecord.getVisitor_goods());
        //来访单位
        ((EditText) findViewById(R.id.visitor_org_et)).setText(visitRecord.getUnit_name());
        //地址
        ((EditText) findViewById(R.id.visitor_address_et)).setText(visitRecord.getAddress());
        //车牌号
        ((EditText) findViewById(R.id.visitor_plate_number_et)).setText(visitRecord.getPlate_number());
        //进入时间
        ((EditText) findViewById(R.id.visitor_entry_time_et)).setText(visitRecord.getIn_time());
        //是否离开
        if (visitRecord.isLeave_flag()) {
            ((EditText) findViewById(R.id.visitor_is_leave_et)).setText("已离开");
        } else {
            ((EditText) findViewById(R.id.visitor_is_leave_et)).setText("未离开");
        }
        //离开时间
        ((EditText) findViewById(R.id.visitor_leave_time_et)).setText(visitRecord.getLeave_time());
        //备注
        ((EditText) findViewById(R.id.visitor_remarks_et)).setText(visitRecord.getNote());

        if (visitRecord.getInterviewee_type() == 0) {
            ((TextView) findViewById(R.id.user_type_tv)).setText("被访教职工信息");
            findViewById(R.id.user_department_ll).setVisibility(View.VISIBLE);
            findViewById(R.id.user_grade_ll).setVisibility(View.GONE);
            findViewById(R.id.user_classes_ll).setVisibility(View.GONE);
            findViewById(R.id.user_head_teacher_ll).setVisibility(View.GONE);
            //被访教职工 部门
            ((EditText) findViewById(R.id.user_department_et)).setText(visitRecord.getDepartment_name());
            //被访教职工 姓名
            ((EditText) findViewById(R.id.user_name_et)).setText(visitRecord.getTeacher_name());
        } else {
            //学生 年级
            ((EditText) findViewById(R.id.user_grade_et)).setText(visitRecord.getGrade_name());
            //学生 班级
            ((EditText) findViewById(R.id.user_classes_et)).setText(visitRecord.getClass_name());
            //学生 姓名
            ((EditText) findViewById(R.id.user_name_et)).setText(visitRecord.getStudent_name());
            //学生 班主任
            ((EditText) findViewById(R.id.user_head_teacher_et)).setText(visitRecord.getHead_teacher_name());
        }

        //登记人 姓名
        ((EditText) findViewById(R.id.registrant_name_et)).setText(visitRecord.getCreate_user_name());
        //登记时间
        ((EditText) findViewById(R.id.registrant_time_et)).setText(visitRecord.getCreate_time());
    }
}
