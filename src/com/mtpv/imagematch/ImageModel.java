package com.mtpv.imagematch;

import java.io.Serializable;

public class ImageModel implements Serializable {

    private String driverDOB;

    private String vehicleNo;

    private Double score;

    private String challanNo;

    private String OffenceDate;

    private String psName;

    private String driverBase64ImgData;

    private String driverLicenceNo;

    private String location;

    private String driverName;

    private Double conf;

    private String id;

    private boolean isSelected;

    public String getDriverDOB() {
        return driverDOB;
    }

    public void setDriverDOB(String driverDOB) {
        this.driverDOB = driverDOB;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getOffenceDate() {
        return OffenceDate;
    }

    public void setOffenceDate(String offenceDate) {
        OffenceDate = offenceDate;
    }

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }

    public String getDriverBase64ImgData() {
        return driverBase64ImgData;
    }

    public void setDriverBase64ImgData(String driverBase64ImgData) {
        this.driverBase64ImgData = driverBase64ImgData;
    }

    public String getDriverLicenceNo() {
        return driverLicenceNo;
    }

    public void setDriverLicenceNo(String driverLicenceNo) {
        this.driverLicenceNo = driverLicenceNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Double getConf() {
        return conf;
    }

    public void setConf(Double conf) {
        this.conf = conf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
