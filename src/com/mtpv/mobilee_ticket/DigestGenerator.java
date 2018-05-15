package com.mtpv.mobilee_ticket;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.Signature;
/**
 * Created by kranthi on 27/02/16.
 */
@SuppressWarnings("unused")
public class DigestGenerator {
    public static final int SHA_512=512;
    public static final int SHA_386=386;

    public String generateSha(String inputData,int algorithm) throws java.security.NoSuchAlgorithmException
    {
        if(inputData.isEmpty())
            return "";
        MessageDigest digestObj=null;
        switch (algorithm) {
            case SHA_512:
             digestObj=   MessageDigest.getInstance("SHA-512");
                break;

        }
        if(digestObj==null)
            return "";
        digestObj.update(inputData.getBytes());
        return convertBytesToHex(digestObj.digest());
    }

    public String convertBytesToBase64(byte[] inputBytes){
        return Base64.encodeToString(inputBytes, Base64.NO_WRAP);
    }
    public static String convertBytesToHex(byte data[])
    {
        StringBuffer hexData = new StringBuffer();
        for (int byteIndex = 0; byteIndex < data.length; byteIndex++)
            hexData.append(Integer.toString((data[byteIndex] & 0xff) + 0x100, 16).substring(1));
        return hexData.toString();
    }
}
