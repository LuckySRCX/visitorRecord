package net.jiaobaowang.visitor.common;

import okhttp3.MediaType;

/**
 * Created by ShangLinMo on 2018/1/5.
 */

public class VisitorConfig {
    //学校用户系统各接口
    private static final String VISIT_SCHCOOL_MAIN="https://jsypay.jiaobaowang.net/useradminwebapi/api/data/";
    public static final String VISIT_SCHOOL_SHAKEHAND=VISIT_SCHCOOL_MAIN+"ShakeHand";
    public static final String VISIT_SCHOOL_LOGIN=VISIT_SCHCOOL_MAIN+"Login";
    /**
     * 获取token
     */
    public static final String VISITOR_GET_TOKEN = "http://139.129.252.49:8080/visitor/api/getToken";
    /**
     * 主URL
     */
    public static final String VISITOR_MAIN_URL = "http://139.129.252.49:8080/visitor";
    /**
     * 访客添加
     */
    public static final String VISITOR_API_ADD = VISITOR_MAIN_URL + "/api/add";
    /**
     * 访客签离
     */
    public static final String VISITOR_API_LEAVE = VISITOR_MAIN_URL + "/api/leave";
    /**
     * 获取访客列表
     */
    public static final String VISITOR_API_LIST = VISITOR_MAIN_URL + "/api/list";
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
    public static final String VISIT_LOCAL_SCHOOL_ID = "net.jiaobangwang.visitor.school_id";//学校id
    public static final String VISIT_LOCAL_TOKEN = "net.jiaobaowang.visitor.token";//token
    public static final String VISIT_LOCAL_USERINFO = "net.jiaobangwang.visitor.userInfo";//账号信息
}
