package net.jiaobaowang.visitor.entity;

import java.util.List;

/**
 * 登录后返回的RspDataModel
 * Created by rocka on 2018/1/22.
 */

public class SchoolLoginRspData {
    private int schid;//学校id
    private String schname;//学校名称
    private String areano;//地区编号
    private int utid;//用户id
    private String uid;//登录账号
    private String utname;//昵称？？？
    private int sex;//性别
    private String imgurl;//头像
    private List<SchoolDepartModel> dpts;//部门列表
    private List<SchoolGradeModel> grds;//年级列表
    private List<SchoolClassModel> clss;//班级列表
    private List<SchoolSubsModel> subs;//
    private int utp;//

    private String urolestr;
    private String urolestrext;
    private String utoken;
    private String appeditstat;
    private String appxxtservstat;
    private String appxxtname;
    private String appxxtico;
    private String appxxtdouser;
    private String isadmin;
    private String urolestrsoure;
    private String urolestrxxt;

    public int getSchid() {
        return schid;
    }

    public void setSchid(int schid) {
        this.schid = schid;
    }

    public String getSchname() {
        return schname;
    }

    public void setSchname(String schname) {
        this.schname = schname;
    }

    public int getUtid() {
        return utid;
    }

    public void setUtid(int utid) {
        this.utid = utid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUtname() {
        return utname;
    }

    public void setUtname(String utname) {
        this.utname = utname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public List<SchoolDepartModel> getDpts() {
        return dpts;
    }

    public void setDpts(List<SchoolDepartModel> dpts) {
        this.dpts = dpts;
    }

    public List<SchoolGradeModel> getGrds() {
        return grds;
    }

    public void setGrds(List<SchoolGradeModel> grds) {
        this.grds = grds;
    }

    public List<SchoolClassModel> getClss() {
        return clss;
    }

    public void setClss(List<SchoolClassModel> clss) {
        this.clss = clss;
    }

    public int getUtp() {
        return utp;
    }

    public void setUtp(int utp) {
        this.utp = utp;
    }

    public String getUrolestr() {
        return urolestr;
    }

    public void setUrolestr(String urolestr) {
        this.urolestr = urolestr;
    }

    public String getUrolestrext() {
        return urolestrext;
    }

    public void setUrolestrext(String urolestrext) {
        this.urolestrext = urolestrext;
    }

    public String getUtoken() {
        return utoken;
    }

    public void setUtoken(String utoken) {
        this.utoken = utoken;
    }

    public String getAreano() {
        return areano;
    }

    public void setAreano(String areano) {
        this.areano = areano;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public List<SchoolSubsModel> getSubs() {
        return subs;
    }

    public void setSubs(List<SchoolSubsModel> subs) {
        this.subs = subs;
    }

    public String getAppeditstat() {
        return appeditstat;
    }

    public void setAppeditstat(String appeditstat) {
        this.appeditstat = appeditstat;
    }

    public String getAppxxtservstat() {
        return appxxtservstat;
    }

    public void setAppxxtservstat(String appxxtservstat) {
        this.appxxtservstat = appxxtservstat;
    }

    public String getAppxxtname() {
        return appxxtname;
    }

    public void setAppxxtname(String appxxtname) {
        this.appxxtname = appxxtname;
    }

    public String getAppxxtico() {
        return appxxtico;
    }

    public void setAppxxtico(String appxxtico) {
        this.appxxtico = appxxtico;
    }

    public String getAppxxtdouser() {
        return appxxtdouser;
    }

    public void setAppxxtdouser(String appxxtdouser) {
        this.appxxtdouser = appxxtdouser;
    }

    public String getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(String isadmin) {
        this.isadmin = isadmin;
    }

    public String getUrolestrsoure() {
        return urolestrsoure;
    }

    public void setUrolestrsoure(String urolestrsoure) {
        this.urolestrsoure = urolestrsoure;
    }

    public String getUrolestrxxt() {
        return urolestrxxt;
    }

    public void setUrolestrxxt(String urolestrxxt) {
        this.urolestrxxt = urolestrxxt;
    }
}
