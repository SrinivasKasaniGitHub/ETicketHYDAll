package com.mtpv.mobilee_ticket;

import com.mtpv.mobilee_ticket.DigestGenerator;

import java.security.NoSuchAlgorithmException;

/**
 * Created by kranthi on 27/02/16.
 */
public class RequestCreator {
    public final String invocationUrl="pos://lastmile.cc";
    public final String mkey="sLBdrX5hjJ7YKEENbS7ug==";//merchant key
    public final String mid="8000001";//merchant id
    public final String pkey="1234567890";//partner key
    public final String pid="1234567890";//partner id
    public final String cid="10000002";//client id
    public final String crefid="367766667637489";
    public final String delimeter="&";
    public final String return_url="";
    public final String urltype="1";
    public final String KF1="";
    public final String KF2="";
    public final String KF3="";
    public final String tagKF1="KF1=155.00";
    public final String tagKF2="KF2=85.00";
    public final String tagKF3="KF3=9192065961,9192066026,9192066123";
    public final String tagCID="CID=";
    public final String tagMID="MID=";
    public final String amt="120.00";
    public final String tagCREFID="CREFID=";
    public final String tagRETURL="RETURL=";
    public final String tagURLTYPE="URLTYPE=";
    public final String tagPKEY="PKEY=";
    public final String tagPID="PID=";
    public final String tagAMT="AMT=";
    public final String tagSIG="SIG=";
    public final String tagURL ="RETURL=";
    public final String tagMKEY="MKEY=";
    public final String questionMark="?";

    public String getRequestFormat(int type)
    {
        String result="";
        switch (type){
            case 1:
              return getMerchantRequest();
            case 2:
               return getLogisticsPartnerRequest();
        }
        return result;
    }

    public String getMerchantRequest()
    {
        StringBuilder reqObj=new StringBuilder();
        StringBuilder digestString=new StringBuilder();
        digestString.append(mid).append(mkey).append(amt);
        DigestGenerator gen=new DigestGenerator();
        try {
            reqObj.append(invocationUrl).append(questionMark).append(tagMID).append(mid).append(delimeter).append(tagRETURL).append(return_url).append(delimeter)
                    .append(tagURLTYPE).append(urltype).append(delimeter)
                    .append(tagPKEY).append(pkey).append(delimeter)
                    .append(tagPID).append(pid).append(delimeter)
                    .append(tagKF1).append("").append(delimeter)
                    .append(tagKF2).append("").append(delimeter)
                    .append(tagKF3).append("").append(delimeter)
                    .append(tagAMT).append("120.00").append(delimeter)
                    .append(tagSIG).append(gen.generateSha(digestString.toString(),512));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reqObj.toString();
    }

    public String getLogisticsPartnerRequest()
    {
        StringBuilder reqObj=new StringBuilder();
        StringBuilder digestString=new StringBuilder();
        digestString.append(tagPID).append(pkey).append(amt);
        DigestGenerator gen=new DigestGenerator();
        try {
            reqObj.append(invocationUrl).append(questionMark).append(tagCID).append(cid).append(delimeter)
                    .append(tagCREFID).append(crefid).append(delimeter).append(tagRETURL).append(tagURL)
                    .append(return_url).append(delimeter).append(tagURLTYPE).append(urltype).append(delimeter)
                    .append(tagPKEY).append(pkey).append(delimeter).append(tagPID).append(pid).append(delimeter)
                    .append(tagKF1).append("").append(delimeter)
                    .append(tagKF2).append("").append(delimeter).append(tagAMT).append("120.00").append(delimeter)
                    .append(tagSIG).append(gen.generateSha(digestString.toString(),512));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reqObj.toString();
    }
}
