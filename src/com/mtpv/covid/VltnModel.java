package com.mtpv.covid;

import java.io.Serializable;

public class VltnModel implements Serializable {

    private String OffenceCode;
    private String Section;
    private String Violation;
    private String Fine;
    private String Detain;

    public String getOffenceCode() {
        return OffenceCode;
    }

    public void setOffenceCode(String offenceCode) {
        OffenceCode = offenceCode;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getViolation() {
        return Violation;
    }

    public void setViolation(String violation) {
        Violation = violation;
    }

    public String getFine() {
        return Fine;
    }

    public void setFine(String fine) {
        Fine = fine;
    }

    public String getDetain() {
        return Detain;
    }

    public void setDetain(String detain) {
        Detain = detain;
    }
}
