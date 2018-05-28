package net.jiaobaowang.visitor.entity;

import java.util.List;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2018/5/28.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */

public class SchInfoSoureRespData {
    private int schid;
    private String schname;
    private String adminname;
    private String adminimg;
    private String adminmobile;
    private String sourename;
    private String sourelogo;
    private int sourestat;

    public String getBaseapps() {
        return baseapps;
    }

    public void setBaseapps(String baseapps) {
        this.baseapps = baseapps;
    }

    private String baseapps;
    private String soureappstat;
    private String xxtapps;
    private String percode;
    private String subcode;
    private String matercodes;

    private List<persubmat> persubmat;

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

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getAdminimg() {
        return adminimg;
    }

    public void setAdminimg(String adminimg) {
        this.adminimg = adminimg;
    }

    public String getAdminmobile() {
        return adminmobile;
    }

    public void setAdminmobile(String adminmobile) {
        this.adminmobile = adminmobile;
    }

    public String getSourename() {
        return sourename;
    }

    public void setSourename(String sourename) {
        this.sourename = sourename;
    }

    public String getSourelogo() {
        return sourelogo;
    }

    public void setSourelogo(String sourelogo) {
        this.sourelogo = sourelogo;
    }

    public int getSourestat() {
        return sourestat;
    }

    public void setSourestat(int sourestat) {
        this.sourestat = sourestat;
    }

    public String getSoureappstat() {
        return soureappstat;
    }

    public void setSoureappstat(String soureappstat) {
        this.soureappstat = soureappstat;
    }

    public String getXxtapps() {
        return xxtapps;
    }

    public void setXxtapps(String xxtapps) {
        this.xxtapps = xxtapps;
    }

    public String getPercode() {
        return percode;
    }

    public void setPercode(String percode) {
        this.percode = percode;
    }

    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public String getMatercodes() {
        return matercodes;
    }

    public void setMatercodes(String matercodes) {
        this.matercodes = matercodes;
    }

    public List<net.jiaobaowang.visitor.entity.persubmat> getPersubmat() {
        return persubmat;
    }

    public void setPersubmat(List<net.jiaobaowang.visitor.entity.persubmat> persubmat) {
        this.persubmat = persubmat;
    }
}
 class persubmat{
    private int percode;
    private int subcode;
    private String matercodes;

     public int getPercode() {
         return percode;
     }

     public void setPercode(int percode) {
         this.percode = percode;
     }

     public int getSubcode() {
         return subcode;
     }

     public void setSubcode(int subcode) {
         this.subcode = subcode;
     }

     public String getMatercodes() {
         return matercodes;
     }

     public void setMatercodes(String matercodes) {
         this.matercodes = matercodes;
     }
 }