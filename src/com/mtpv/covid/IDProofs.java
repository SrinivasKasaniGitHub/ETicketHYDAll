package com.mtpv.covid;

import java.io.Serializable;

public class IDProofs implements Serializable {

    private String IdCode;
    private String IdName;

    public String getIdCode() {
        return IdCode;
    }

    public void setIdCode(String idCode) {
        IdCode = idCode;
    }

    public String getIdName() {
        return IdName;
    }

    public void setIdName(String idName) {
        IdName = idName;
    }
}
