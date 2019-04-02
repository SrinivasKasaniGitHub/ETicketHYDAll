package com.mtpv.imagematch;

import java.io.Serializable;

public class Case_info implements Serializable {

    private String ps;

    private String fir_no;

    public String getPs ()
    {
        return ps;
    }

    public void setPs (String ps)
    {
        this.ps = ps;
    }

    public String getFir_no ()
    {
        return fir_no;
    }

    public void setFir_no (String fir_no)
    {
        this.fir_no = fir_no;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ps = "+ps+", fir_no = "+fir_no+"]";
    }
}
