package net.jiaobaowang.visitor.entity;

import java.io.Serializable;

/**
 * Created by rocka on 2018/1/12.
 */

public class VisitRecord implements Serializable {

    private String visitor_goods;//:null,（随行物品)
    private String note;//:null,（备注）
    private String teacher_name;//:msyt,（老师姓名）
    private String create_user_name;//:null,（创建人姓名）
    private boolean leave_flag;//:true,（是否已经签离）
    private int visitor_sex;//:0,（访客性别）
    private int teacher_id;//:null,（老师id）
    private String visitor_counter;//:0,（随行人数）
    private int class_id;//:null,（班级id）
    private String certificate_number;//:null,（证件号码）
    private String student_name;//:null,（学生姓名）
    private String update_time;//:2018-01-05 10;//:06,（更新时间）
    private int school_id;
    private int grade_id;//:null,（年级id）
    private int id;//:2018000003,（访客记录id）
    private String visitor_phone;//:null,（访问者电话）
    private String class_name;//:null,（班级名称）
    private int create_user_id;//:null,（创建用户id）
    private String leave_time;//:2018-12-05 15;//:15,（离开时间）
    private String address;//:null,（访客的地址）
    private String create_time;//:2018-01-05 10;//:06,（创建时间）
    private int interviewee_type;//:0,（被访者类型）0老师 1学生
    private int department_id;//:null,（部门id）
    private String department_name;//:政教处,（部门名称）
    private int student_id;//:null,（学生id）
    private String visitor_for;//:0,（访问缘由）
    private String visitor_name;//:msy2,（访客姓名）
    private String unit_name;//:null,（单位名称）
    private String in_time;//:2018-04-09 15;//:11,（进入时间）
    private String img_url;
    private String visitor_birthday;//:null,（访客生日）
    private String head_teacher_name;//:null,（班主任名称）
    private String certificate_type;//:0,（证件类型）
    private String plate_number;//:京A8888,（车牌号）
    private String grade_name;//:null,（年级名称）
    private int head_teacher_id;//:null（班主任id）

    public String getVisitor_goods() {
        return visitor_goods;
    }

    public void setVisitor_goods(String visitor_goods) {
        this.visitor_goods = visitor_goods;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public boolean isLeave_flag() {
        return leave_flag;
    }

    public void setLeave_flag(boolean leave_flag) {
        this.leave_flag = leave_flag;
    }

    public int getVisitor_sex() {
        return visitor_sex;
    }

    public void setVisitor_sex(int visitor_sex) {
        this.visitor_sex = visitor_sex;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getVisitor_counter() {
        return visitor_counter;
    }

    public void setVisitor_counter(String visitor_counter) {
        this.visitor_counter = visitor_counter;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getCertificate_number() {
        return certificate_number;
    }

    public void setCertificate_number(String certificate_number) {
        this.certificate_number = certificate_number;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVisitor_phone() {
        return visitor_phone;
    }

    public void setVisitor_phone(String visitor_phone) {
        this.visitor_phone = visitor_phone;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public int getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(int create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getLeave_time() {
        return leave_time;
    }

    public void setLeave_time(String leave_time) {
        this.leave_time = leave_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getInterviewee_type() {
        return interviewee_type;
    }

    public void setInterviewee_type(int interviewee_type) {
        this.interviewee_type = interviewee_type;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getVisitor_for() {
        return visitor_for;
    }

    public void setVisitor_for(String visitor_for) {
        this.visitor_for = visitor_for;
    }

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getVisitor_birthday() {
        return visitor_birthday;
    }

    public void setVisitor_birthday(String visitor_birthday) {
        this.visitor_birthday = visitor_birthday;
    }

    public String getHead_teacher_name() {
        return head_teacher_name;
    }

    public void setHead_teacher_name(String head_teacher_name) {
        this.head_teacher_name = head_teacher_name;
    }

    public String getCertificate_type() {
        return certificate_type;
    }

    public void setCertificate_type(String certificate_type) {
        this.certificate_type = certificate_type;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public int getHead_teacher_id() {
        return head_teacher_id;
    }

    public void setHead_teacher_id(int head_teacher_id) {
        this.head_teacher_id = head_teacher_id;
    }
}
