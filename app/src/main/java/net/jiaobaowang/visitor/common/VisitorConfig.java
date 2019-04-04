package net.jiaobaowang.visitor.common;

import okhttp3.MediaType;

/**
 * Created by ShangLinMo on 2018/1/5.
 */

public class VisitorConfig {
    //学校用户系统各接口
    private static final String VISIT_SCHOOL_MAIN = "https://zhxy.jiaobaowang.net:8515/schadminwebapi/api/data/";
//    private static final String VISIT_SCHOOL_MAIN = "https://jsypay.jiaobaowang.net/useradminwebapi/api/data/";//测试地址
    public static final String VISIT_SCHOOL_SHAKEHAND = VISIT_SCHOOL_MAIN + "ShakeHand";
    public static final String VISIT_SCHOOL_LOGIN = VISIT_SCHOOL_MAIN + "Login";
    /**
     * 总部主URL
     */
    public static final String VISITOR_ZB_MAIN_URL = "https://zhxy.jiaobaowang.net:8515/schadminwebapi/api/data";
//    public static final String VISITOR_ZB_MAIN_URL = "https://jsypay.jiaobaowang.net/useradminwebapi/api/data";//测试地址
    /**
     * 广西主URL
     */
    public static final String VISITOR_GX_MAIN_URL = "https://fangke.jiaobaowang.net/School/VisitorApi";
    /**
     * 握手
     */
    public static final String VISITOR_API_SHAKEHAND = VISITOR_ZB_MAIN_URL + "/ShakeHand";
    /**
     * 登录
     */
    public static final String VISITOR_API_LOGIN = VISITOR_ZB_MAIN_URL + "/Login";

    /**
     * 获取权限
     */
    public static final String VISIT_SCHOOL_LOGIN_QX = VISIT_SCHOOL_MAIN + "SchInfoSoure";

    /**
     * 获取学校年级
     */
    public static final String VISITOR_API_GRADE = VISITOR_ZB_MAIN_URL + "/SchGrade";
    /**
     * 获取学校班级
     */
    public static final String VISITOR_API_CLASS = VISITOR_ZB_MAIN_URL + "/SchClass";
    /**
     * 获取学校科目
     */
    public static final String VISITOR_API_SUBJECT = VISITOR_ZB_MAIN_URL + "/SchSub";
    /**
     * 获取学校部门
     */
    public static final String VISITOR_API_DEPARTMENT = VISITOR_ZB_MAIN_URL + "/SchDepart";
    /**
     * 获取学校年级主任
     */
    public static final String VISITOR_API_GRADE_BOSS = VISITOR_ZB_MAIN_URL + "/GradeBoss";
    /**
     * 获取学校年级下的班级
     */
    public static final String VISITOR_API_GRADE_CLASS = VISITOR_ZB_MAIN_URL + "/GradeClass";
    /**
     * 获取班级的任课老师
     */
    public static final String VISITOR_API_CLASS_TEACHER = VISITOR_ZB_MAIN_URL + "/ClassTec";
    /**
     * 获取班级的学生
     */
    public static final String VISITOR_API_CLASS_STUDENT = VISITOR_ZB_MAIN_URL + "/ClassStu";
    /**
     * 获取部门成员
     */
    public static final String VISITOR_API_DEPARTMENT_USER = VISITOR_ZB_MAIN_URL + "/DepartUser";
    /**
     * 总部token续订
     */
    public static final String VISITOR_API_TOKEN_RESET = VISITOR_ZB_MAIN_URL + "/TokenReset";

    /**
     * 添加访客记录
     */
    public static final String VISITOR_API_ADD = VISITOR_GX_MAIN_URL + "/add";
    /**
     * 访客签离
     */
    public static final String VISITOR_API_LEAVE = VISITOR_GX_MAIN_URL + "/leave";
    /**
     * 获取访客列表
     */
    public static final String VISITOR_API_LIST = VISITOR_GX_MAIN_URL + "/list";
    /**
     * 七牛获取上传token
     */
    public static final String QINIU_GET_UPLOAD_TOKEN = "https://jbyc.jiaobaowang.net:8445/Api/QiNiu/GetUpLoadToKen";
    /**
     * 七牛获取下载token
     */
    public static final String QINIU_GET_DOWNLOAD_TOKEN = "https://jbyc.jiaobaowang.net:8445/Api/QiNiu/GetAccess";
    /**
     * 七牛批量删除文件
     */
    public static final String QINIU_DELETE_FILE = "https://jbyc.jiaobaowang.net:8445/Api/QiNiu/Delete";
    /**
     * 七牛公开空间
     */
    public static final String QINIU_PUBLIC_SPACE = "pb";
    /**
     * 七牛私有空间
     */
    public static final String QINIU_PRIVATE_SPACE = "pv";
    /**
     * 访客系统 七牛项目id
     */
    public static final String QINIU_VISITOR_SYSTEM_APP_ID = "10";
    /**
     * 访客系统 七牛密钥
     */
    public static final String QINIU_VISITOR_SYSTEM_SECRET_KEY = "jsy01170";
    /**
     * 访客系统 七牛第一前缀名
     */
    public static final String QINIU_VISITOR_SYSTEM_FILE_FIRST_NAME = "fangkesys/";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String VISIT_LOCAL_STORAGE = "net.jiaobaowang.visitor.localStorage";//存储到本地的key
    public static final String VISIT_LOCAL_STORAGE_UUID = "net.jiaobaowang.visitor.localStorage_uuid";//设备唯一识别码
    public static final String VISIT_LOCAL_SCHOOL_ID = "net.jiaobangwang.visitor.school_id";//学校id
    public static final String VISIT_LOCAL_TOKEN = "net.jiaobaowang.visitor.token";//token
    public static final String VISIT_LOCAL_APPEDITSTAT = "net.jiaobaowang.visitor.appeditstat";//appeditstat
    public static final String VISIT_LOCAL_SHAKEHAND = "net.jiaobaowang.visitor.shakeHand";//token
    public static final String VISIT_LOCAL_USERINFO = "net.jiaobangwang.visitor.userInfo";//账号信息
    public static final String VISIT_LOCAL_USERINFO_UID = "net.jiaobaowang.visitor.userInfo.uid";//uid
    public static final String VISIT_LOCAL_USERINFO_UTID = "net.jiaobaowang.visitor.userInfo.utid";//utid
    public static final String VISIT_LOCAL_USERINFO_UTNAME= "net.jiaobaowang.visitor.userInfo.utname";//uname
    public static final String VISIT_LOCAL_USER_QUERY= "net.jiaobaowang.visitor.user.query";//uname
    public static final String VISIT_LOCAL_USER_SIGN= "net.jiaobaowang.visitor.user.sign";//uname

}
