package com.androidsolutions.shivam.mechanix;

/**
 * Created by Shivam on 10/5/2018.
 */

public class StoreServices {

    String  idd,status;
    String cname,cmob,mechname,mechmob,mechuid,timestamp;
    public StoreServices(String id1,String status, String cn, String cm, String mn, String mmob, String mechuid, String time)
    {
        this.idd=id1;
        this.status=status;
        this.cname=cn;
        this.cmob=cm;
        this.mechname=mn;
        this.mechmob=mmob;
        this.mechuid=mechuid;
        this.timestamp=time;
    }

    public String getIdd() {
        return idd;
    }

    public void setIdd(String idd) {
        this.idd = idd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCmob() {
        return cmob;
    }

    public void setCmob(String cmob) {
        this.cmob = cmob;
    }

    public String getMechname() {
        return mechname;
    }

    public void setMechname(String mechname) {
        this.mechname = mechname;
    }

    public String getMechmob() {
        return mechmob;
    }

    public void setMechmob(String mechmob) {
        this.mechmob = mechmob;
    }

    public String getMechuid() {
        return mechuid;
    }

    public void setMechuid(String mechuid) {
        this.mechuid = mechuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
