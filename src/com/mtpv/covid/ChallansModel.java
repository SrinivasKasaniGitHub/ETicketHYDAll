package com.mtpv.covid;

import java.io.Serializable;

public class ChallansModel implements Serializable {

    private String ChallanNo;
    private String Date;
    private String FINE;
    private String Location;
    private String PSName;

    public String getChallanNo() {
        return ChallanNo;
    }

    public void setChallanNo(String challanNo) {
        ChallanNo = challanNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFINE() {
        return FINE;
    }

    public void setFINE(String FINE) {
        this.FINE = FINE;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPSName() {
        return PSName;
    }

    public void setPSName(String PSName) {
        this.PSName = PSName;
    }
}
