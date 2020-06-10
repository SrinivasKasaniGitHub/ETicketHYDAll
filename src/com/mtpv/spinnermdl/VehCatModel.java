package com.mtpv.spinnermdl;

import java.io.Serializable;

public class VehCatModel implements Serializable {

    private String VehCatCode;
    private String VehCatName;

    public String getVehCatCode() {
        return VehCatCode;
    }

    public void setVehCatCode(String vehCatCode) {
        VehCatCode = vehCatCode;
    }

    public String getVehCatName() {
        return VehCatName;
    }

    public void setVehCatName(String vehCatName) {
        VehCatName = vehCatName;
    }
}
