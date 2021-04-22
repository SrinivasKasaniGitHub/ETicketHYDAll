package com.mtpv.mobilee_ticket_services;

import android.util.Log;

import com.mtpv.mobilee_ticket.Dashboard;
import com.mtpv.mobilee_ticket.Drunk_Drive;
import com.mtpv.mobilee_ticket.GenerateDrunkDriveCase;
import com.mtpv.mobilee_ticket.MainActivity;
import com.mtpv.mobilee_ticket.Settings_New;
import com.mtpv.mobilee_ticket.SpotChallan;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("unused")
public class ServiceHelper {

    private static String METHOD_NAME = "authenticateUser", NAMESPACE = "http://service.et.mtpv.com";

    public static String SOAP_ACTION = NAMESPACE + METHOD_NAME, Opdata_Chalana, getViolationPoint_resp,
            spot_final_res_status, transactionNo_resp, makePayment_resp, terminaldetails_resp, points_resp = null,
            aadhaarVehicle_resp, UpdateAadhaar_resp, aadhaarDetailsCheck_resp, changePswd_otp, changePSWDconfirm,
            version_response, offender_remarks,
            rta_data, license_data, aadhar_data, result = "", output = "", rc_send, dl_send, adhr_send,
            versionData, getOfficerLimit, spot_finalPrintNDevice, str_imgDDInfo, str_SameChlnDuplicatePrint = "",
            profileUpdate,vehCategory,str_TopVltns_List="";

    public static Map<String, String> viodetMap = null;
    public static String rtaapproovedresponse, validregnoresponse, insertDetainItemsresponse, remarksresult, otpStatusnTime, noncontactresponse;
    public static String WHEELER_MEHOD_NAME = "getWheelerDetails", GET_PS_NAMES_MEHOD_NAME = "getPsNames", GET_POINTNAME_BY_PSNAME_MEHOD_NAME = "getPointNamesByPsName";
    public static String OCUPTN_METHOD_NAME = "getOccupations", QLFCTION_METHOD_NAME = "getQualifications", BAR_DETAILS_METHOD_NAME = "getWineSellerDetails";
    public static String VECHILE_CATEGORY = "getVehicleCategory", VECHILE_MAIN_CAT_METHOD_NAME = "getVehicleMainCategory";
    public static String VECHILE_SUB_CAT_METHOD_NAME = "getVehicleSubCategory", GENERATE_DRUNK_DRIVE_CASE = "generateDrunDriveCase";
    public static String GENERATE_DRUNK_DRIVE_CASE1_5_2 = "generateDrunDriveCase_v_1_5_2", RTA_DETAILS_BY_REGNO_METHOD_NAME = "getRTADetailsByVehicleNO";
    public static String RTA_DETAILS_BY_REGNO_LICENSE_AADHAR_METHOD_NAME = "getOffenderRemarks", PENDING_CHALLANS_BY_REGNO = "getPendingChallansByRegnno";
    public static String LICENCE_DETAILS_METHOD_NAME = "getRTADetailsByLicenceNo", GET_VIOLATIONS_POINTS = "getViolationsPoints";
    public static String VIOLATION_DETAILS_METHOD_NAME = "getOffenceDetailsbyWheeler", GET_DETAINED_ITEMS_METHOD_NAME = "getDetainedItems";
    public static String AADHAR_DETAILS_METHOD_NAME = "getAADHARData", ALL_WHEELER_VIOLATION_DETAILS_METHOD_NAME = "getViolationsDetailsbyAllWheeler";
    public static String DUPLCIATE_ONLINE_PRINT_METHOD_NAME = "getDuplicatePrint", DAY_REPORTS_METHOD_NAME = "getDayReports";
    public static String SEND_OTP_TO_MOBILE_METHOD_NAME = "sendOTP", VERIFY_OTP_FROM_MOBILE_METHOD_NAME = "verifyOTP";
    public static String MOBILE_SPOT_CHALLAN_METHOD_NAME = "mobileSpotChallanPaymentNew13", TOWING_CPACT_METHOD_NAME = "mobile41CPAct13";
    public static String VEHICLE_HISTORY_METHOD_NAME = "mobilePendingChallanPayment", VEHICLE_RELEASE_METHOD_NAME = "vehicleRelease15";
    public static String SPOT_CHALLAN_PAYMENT_NEW_15 = "mobileSpotChallanPaymentNew15", SPOT_CHALLAN_PAYMENT_NEW_1_5_2 = "mobileSpotChallanPaymentNew_v_1_5_2";
    public static String GET_TRANSACTION_NO = "getTxnRefNo10", MAKE_ONLINE_PAYMENT = "makeOnlinePayment10";
    public static String GET_TERMINAL_DETAILS = "getTerminalDetails", GET_CHALLAN_DETAILS_FOR_AADHAAR = "getChallanDetailsForAadharUpdate";
    public static String GET_AADHAAR_UPDATE = "aadharUpdateForChallanGeneration", OCCUPATIONS = "getOccupations", QUALIFICATIONS = "getQualifications";
    public static String GET_CHANGE_PSWD_OTP = "aadharUpdateForChallanGeneration", GET_AADHAAR_TICKET = "checkAadharTicket";
    public static String get_OffenceDetailsbyWheelerChallanType = "getOffenceDetailsbyWheelerChallanType", getPrevFRSInfo = "getPrevFRSInfo";

    public static String[] login_details_arr, whlr_details_master, psNames_master, violation_points_masters, violation_points_masters_split,
            occupationlist_master, PointNamesBypsNames_master, occupation_master, qualification_master, bar_master, vchle_cat_master,
            vchle_mainCat_master, vchle_subCat_master, final_reponse_split, final_response_master, violation_details_master, pending_challans_master,
            detained_items_list_master, aadhar_details, final_spot_reponse_master, final_spot_reponse_details, final_spot_reponse_violations_master,
            selected_paid_challans_master;

    public static String[][] detained_items_list_details, pending_challans_details, final_spot_reponse_violations, selected_paid_challans_details, violation_detailed_views;

    static StringBuffer sbuffer_allViolations;

    public static StringBuffer onlinebuff = new StringBuffer();

    private static int timeLimit = 60000 * 2;

    public static String api_key = "AIzaSyDK_kuC51e_BK7T7rShVYT7ukAwLY6ugGU";

    public static String duplicateChaln_Data = "";

    public static String strCV_IdPrfMstr = "", strCV_DtndMstr = "", strCV_VltnsList = "",
            strCV_IdPrfData = "", strCV_Responce = "";

    public static void login(String pid, String pidpwd, String mob_imei, String sim_No, String lat, String log,
                             String appVersion) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "authenticateDeviceNPID");
            request.addProperty("pidCd", pid);
            request.addProperty("password", pidpwd);
            request.addProperty("imei", mob_imei);
            request.addProperty("simNo", sim_No);
            request.addProperty("gpsLattitude", lat);
            request.addProperty("gpsLongitude", log);
            request.addProperty("appVersion", appVersion);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            try {
                // result="U2FsdGVkX194WNCETuiOfxswmwgvecv9ok4AlMFJ+EMEhJuhu0dxEPoer6z56s383MXZF9cT4G7EPxk3TOsuLDjL1/eaBD++9mxxABWqu8v4U21qpKjHMsY4DIvuOqb8+gcm/4iXUZM=";
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                Log.d("PrintDecodedData", "" + Opdata_Chalana);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Opdata_Chalana == "0") {
            } else {
                if (Opdata_Chalana != null && !Opdata_Chalana.equals("NA")) {
                  // Opdata_Chalana = Opdata_Chalana.replace("|", ":");
                    MainActivity.arr_logindetails = Opdata_Chalana.split("|");
                }

            }

        } catch (SoapFault fault) {
            fault.printStackTrace();
            Opdata_Chalana = "0";
        } catch (SocketTimeoutException fault) {
            fault.printStackTrace();
            Opdata_Chalana = "0";
        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }
    }

    public static String VersionCheck(String appVersion) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "appVersion");

            request.addProperty("appVersion", appVersion);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                versionData = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                versionData = "0";
                e.printStackTrace();
            }


        } catch (SoapFault fault) {
            versionData = "0";
            fault.printStackTrace();
        } catch (SocketTimeoutException fault) {
            versionData = "0";
            fault.printStackTrace();
        } catch (Exception E) {
            E.printStackTrace();
            versionData = "0";
        }
        return versionData;
    }

    public static String exceptionLimit(String pidCd, String OffenceDt) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getIDProofsExemptionLimit");

            request.addProperty("pidCd", pidCd);
            request.addProperty("OffenceDt", OffenceDt);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                getOfficerLimit = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                getOfficerLimit = "0";
                e.printStackTrace();
            }


        } catch (SoapFault fault) {
            getOfficerLimit = "0";
            fault.printStackTrace();
        } catch (Exception E) {
            E.printStackTrace();
            getOfficerLimit = "0";
        }
        return getOfficerLimit;
    }

    public static void sendOTPtoMobile(String regn_no, String mobileNo, String date,String smsMode,String challanType) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, SEND_OTP_TO_MOBILE_METHOD_NAME);
            request.addProperty("regn_no", regn_no);
            request.addProperty("mobileNo", mobileNo);
            request.addProperty("date", date);
            request.addProperty("smsMode", smsMode);
            request.addProperty("challanType",challanType);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SoapFault fault) {
        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "NA";
        }
    }

    public static void verifyOTP(String regn_no, String mobileNo, String date, String otp, String verify_status) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, VERIFY_OTP_FROM_MOBILE_METHOD_NAME);
            request.addProperty("regn_no", regn_no);
            request.addProperty("mobileNo", mobileNo);
            request.addProperty("date", date);
            request.addProperty("otp", otp);
            request.addProperty("verify_status", verify_status);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (SoapFault fault) {
        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "NA";
        }
    }

    public static void confirmLoginOTP(String pidCode, String otp) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "confirmLoginOTP");
            request.addProperty("pidCode", pidCode);
            request.addProperty("otp", otp);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(NAMESPACE + "confirmLoginOTP", envelope);
            Object result = envelope.getResponse();
            Opdata_Chalana = "";
            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (SoapFault fault) {
        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "NA";
        }
    }

    public static void getWheeler() {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + WHEELER_MEHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());

                    if (Opdata_Chalana != null && Opdata_Chalana != "0") {

                        whlr_details_master = new String[0];
                        whlr_details_master = Opdata_Chalana.split("!");

                    } else {

                        Opdata_Chalana = "0";
                        whlr_details_master = new String[0];

                    }
                } else {
                    Opdata_Chalana = "0";
                    whlr_details_master = new String[0];
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Opdata_Chalana = "0";
                whlr_details_master = new String[0];
            }


        } catch (SoapFault fault) {
            fault.printStackTrace();
            Opdata_Chalana = "0";
            whlr_details_master = new String[0];
        } catch (SocketTimeoutException fault) {
            fault.printStackTrace();
            Opdata_Chalana = "0";
            whlr_details_master = new String[0];
        } catch (Exception e) {
            // TODO: handle exception
            Opdata_Chalana = "0";
            whlr_details_master = new String[0];
        }

    }

    public static void getVehicleCategory() {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getVehCategoryMaster");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    vehCategory = result.toString();

                } else {
                    vehCategory = "0";
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                vehCategory = "0";
            }


        } catch (SoapFault fault) {
            fault.printStackTrace();
            vehCategory = "0";

        } catch (SocketTimeoutException fault) {
            fault.printStackTrace();
            vehCategory = "0";

        } catch (Exception e) {
            // TODO: handle exception
            vehCategory = "0";

        }

    }

    public static void getViolationPoint_SystemMasterData() {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "getVioPSyst");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    getViolationPoint_resp = new com.mtpv.mobilee_ticket_services.PidSecEncrypt()
                            .decrypt(result.toString());

                    if (getViolationPoint_resp != null && getViolationPoint_resp != "0" && getViolationPoint_resp.length() > 0) {

                        violation_points_masters = new String[0];
                        violation_points_masters = getViolationPoint_resp.split("\\@");
                    }

                } else {
                    getViolationPoint_resp = "0";
                    violation_points_masters = new String[0];
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                getViolationPoint_resp = "0";
                violation_points_masters = new String[0];
                e.printStackTrace();
            }

        } catch (SoapFault fault) {
            getViolationPoint_resp = "0";
            violation_points_masters = new String[0];
        } catch (Exception e) {
            // TODO: handle exception
            bar_master = new String[0];
            getViolationPoint_resp = "0";
            violation_points_masters = new String[0];
        }
    }

    public static void getPsNames(String unit_Code) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + GET_PS_NAMES_MEHOD_NAME);
            request.addProperty("UNIT_CODE", unit_Code);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            try {

                if (result != null) {

                    Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());

                    if (Opdata_Chalana != null && Opdata_Chalana != "0") {

                        psNames_master = new String[0];
                        psNames_master = Opdata_Chalana.split("!");
                    }

                } else {
                    Opdata_Chalana = "0";
                    psNames_master = new String[0];
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Opdata_Chalana = "0";
                psNames_master = new String[0];
                e.printStackTrace();
            }

        } catch (SoapFault fault) {
            Opdata_Chalana = "0";
            psNames_master = new String[0];
        } catch (SocketTimeoutException fault) {
            Opdata_Chalana = "0";
            psNames_master = new String[0];
        } catch (Exception e) {
            // TODO: handle exception
            Opdata_Chalana = "0";
            psNames_master = new String[0];
        }
    }

    public static void getOccupationdetails() {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + OCCUPATIONS);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (Opdata_Chalana == null) {
            } else {
                occupationlist_master = new String[0];
                occupationlist_master = Opdata_Chalana.split("!");
                for (int i = 0; i < ServiceHelper.occupationlist_master.length; i++) {
                    Log.i("**OCCUPATIONS MASTER***", "" + ServiceHelper.occupationlist_master[i]);
                }
            }

        } catch (SoapFault fault) {
        } catch (Exception e) {
            // TODO: handle exception
            occupationlist_master = new String[0];
        }
    }

    public static void getPointNameByPsNames(String ps_code) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + GET_POINTNAME_BY_PSNAME_MEHOD_NAME);
            request.addProperty("psCode", "" + ps_code);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (Opdata_Chalana == null) {
            } else {
                PointNamesBypsNames_master = new String[0];
                PointNamesBypsNames_master = Opdata_Chalana.split("!");
                for (int i = 0; i < ServiceHelper.PointNamesBypsNames_master.length; i++) {
                    Log.i("**POINTPSNAMES MAS", "" + ServiceHelper.PointNamesBypsNames_master[i]);
                }
            }
        } catch (SoapFault fault) {
            PointNamesBypsNames_master = new String[0];
        } catch (SocketTimeoutException fault) {
            PointNamesBypsNames_master = new String[0];
        } catch (Exception e) {
            // TODO: handle exception
            PointNamesBypsNames_master = new String[0];
        }
    }

    public static void getOccupation_Qlfctn_Details(String mName) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + mName);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (Opdata_Chalana == null) {

            } else {
                if (mName.equals("" + OCUPTN_METHOD_NAME)) {
                    occupation_master = new String[0];
                    occupation_master = Opdata_Chalana.split("!");
                    for (int i = 0; i < ServiceHelper.occupation_master.length; i++) {
                        Log.i("**OCCUPATION MASTER***", "" + ServiceHelper.occupation_master[i]);

                    }
                } else if (mName.equals("" + QLFCTION_METHOD_NAME)) {
                    qualification_master = new String[0];
                    qualification_master = Opdata_Chalana.split("!");
                    for (int i = 0; i < ServiceHelper.qualification_master.length; i++) {
                        Log.i("**QLFCTN MASTER***", "" + ServiceHelper.qualification_master[i]);
                    }
                }
            }

        } catch (SoapFault fault) {

        } catch (Exception e) {
            // TODO: handle exception
            qualification_master = new String[0];
        }
    }

    public static void getBar_Details() {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + BAR_DETAILS_METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                    if (Opdata_Chalana != null && Opdata_Chalana != "0") {
                        ServiceHelper.bar_master = new String[0];
                        ServiceHelper.bar_master = Opdata_Chalana.split("!");
                    }
                } else {
                    Opdata_Chalana = "0";
                    ServiceHelper.bar_master = new String[0];
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Opdata_Chalana = "0";
                ServiceHelper.bar_master = new String[0];
                e.printStackTrace();
            }

        } catch (SoapFault fault) {
            Opdata_Chalana = "0";
            ServiceHelper.bar_master = new String[0];
        } catch (Exception e) {
            // TODO: handle exception
            Opdata_Chalana = "0";
            ServiceHelper.bar_master = new String[0];
        }
    }

    public static void getOccupation_Details() {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + OCUPTN_METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {

                if (result != null) {

                    Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());


                    if (Opdata_Chalana != null && !"0".equals(Opdata_Chalana)) {
                        occupation_master = new String[0];
                        occupation_master = Opdata_Chalana.split("!");

                    }
                } else {
                    Opdata_Chalana = "0";
                    occupation_master = new String[0];
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                Opdata_Chalana = "0";
                occupation_master = new String[0];
                e.printStackTrace();
            }


        } catch (SoapFault fault) {
            Opdata_Chalana = "0";
            occupation_master = new String[0];
        } catch (Exception e) {
            // TODO: handle exception
            Opdata_Chalana = "0";
            occupation_master = new String[0];
            qualification_master = new String[0];
        }
    }

    public static void getQualifications_Details() {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + QLFCTION_METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            try {

                if (result != null) {
                    Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());

                    if (Opdata_Chalana != null && Opdata_Chalana != "0") {
                        qualification_master = new String[0];
                        qualification_master = Opdata_Chalana.split("!");
                    }
                } else {
                    Opdata_Chalana = "0";
                    qualification_master = new String[0];
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                Opdata_Chalana = "0";
                qualification_master = new String[0];
                e.printStackTrace();
            }

        } catch (Exception fault) {
            Opdata_Chalana = "0";
            qualification_master = new String[0];
        }
    }

    public static void getVechileCategory(String mName) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + mName);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (Opdata_Chalana == null) {
            } else {
                if (mName.equals("" + VECHILE_CATEGORY)) {
                    vchle_cat_master = new String[0];
                    vchle_cat_master = Opdata_Chalana.split("!");
                    for (int i = 0; i < ServiceHelper.vchle_cat_master.length; i++) {
                        Log.i("**VCHLE CAT MASTER***", "" + ServiceHelper.vchle_cat_master[i]);
                    }
                } else if (mName.equals("" + VECHILE_MAIN_CAT_METHOD_NAME)) {
                    vchle_mainCat_master = new String[0];
                    vchle_mainCat_master = Opdata_Chalana.split("!");
                    for (int i = 0; i < ServiceHelper.vchle_mainCat_master.length; i++) {
                        Log.i("CAT MASTER***", "" + ServiceHelper.vchle_mainCat_master[i]);
                    }
                }
            }
        } catch (Exception e) {
            if (mName.equals("" + VECHILE_CATEGORY)) {
                vchle_cat_master = new String[0];
            } else if (mName.equals("" + VECHILE_MAIN_CAT_METHOD_NAME)) {
                vchle_mainCat_master = new String[0];
            }
        }
    }

    public static void getVechileSubCategory(String mainCCode) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + VECHILE_SUB_CAT_METHOD_NAME);
            request.addProperty("vehicleMainCategoryCode", "" + mainCCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Opdata_Chalana == null) {
            } else {
                vchle_subCat_master = new String[0];
                vchle_subCat_master = Opdata_Chalana.split("!");
                for (int i = 0; i < ServiceHelper.vchle_subCat_master.length; i++) {
                    Log.i("**VCHLE SUB", "" + ServiceHelper.vchle_subCat_master[i]);
                }
            }
        } catch (SoapFault fault) {
        } catch (Exception e) {
            // TODO: handle exception
            vchle_subCat_master = new String[0];
        }
    }

    public static void getPrevFRSInfo(String driverBase64ImgData) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + getPrevFRSInfo);
            request.addProperty("driverBase64ImgData", "" + driverBase64ImgData);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                str_imgDDInfo = result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                str_imgDDInfo = "";
            }
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
            str_imgDDInfo = "";
        } catch (Exception e) {
            e.printStackTrace();
            str_imgDDInfo = "";
        }
    }

    public static void generateDrunDriveCase(String regn_Cd, String vehicle_No, String regn_No, String operater_Cd,
                                             String operater_Name, String pid_Cd, String pid_Name, String ps_Code, String ps_Name, String booked_PsCode,
                                             String booked_PsName, String point_Code, String poin_tName, String exact_Location, String offence_Date,
                                             String offence_Time, String fined_By, String owner_DLNo, String driver_DLNo, String driver_Name,
                                             String father_Name, String aadhar_No, String panCard_No, String passport_No, String voter_Id,
                                             String contact_No, String offence_Code, String offence_Amount, String detained_Items,
                                             String councelling_Date, String alcohol_Reading, String user_age, String user_occupation,
                                             String user_qualification, String user_gendor, String check_SlNo, String wheeler_Cd, String maker_Cd,
                                             String vehMain_Category, String vehSub_Category, String user_address, String user_city, String user_unitCd,
                                             String user_unitName, String cadre_Cd, String cadre_only, String imgEncoded_String, String device_imei,
                                             String gps_Lattitude, String gps_Longitude, String mob_macId, String sim_Id, String breath_AnalyserId,
                                             String lcnce_status) {
        Utils utils = new Utils();
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + GENERATE_DRUNK_DRIVE_CASE);
            request.addProperty(utils.REG_CD, "" + regn_Cd);
            request.addProperty(utils.VEH_NO, "" + vehicle_No);
            request.addProperty(utils.REG_NO, "" + regn_No);
            request.addProperty(utils.OPTR_CD, "" + operater_Cd);
            request.addProperty(utils.OPTR_NAME, "" + operater_Name);
            request.addProperty(utils.PID_CD, "" + pid_Cd);
            request.addProperty(utils.PID_NAME, "" + pid_Name);
            request.addProperty(utils.PS_CODE, "" + ps_Code);
            request.addProperty(utils.PS_NAME, "" + ps_Name);
            request.addProperty(utils.BKD_PS_CODE, "" + booked_PsCode);
            request.addProperty(utils.BKD_PS_NAME, "" + booked_PsName);
            request.addProperty(utils.POINT_CODE, "" + point_Code);
            request.addProperty(utils.POINT_NAME, "" + poin_tName);
            request.addProperty(utils.EXCT_LOCATION, "" + exact_Location);
            request.addProperty(utils.OFFNCE_DATE, "" + offence_Date);
            request.addProperty(utils.OFFENCE_TIME, "" + offence_Time);
            request.addProperty(utils.FINED_BY, "" + fined_By);
            request.addProperty(utils.OWNR_DL_NO, "" + owner_DLNo);
            request.addProperty(utils.DRIVER_DL_NO, "" + driver_DLNo);
            request.addProperty(utils.DRIVER_NAME, "" + driver_Name);
            request.addProperty(utils.DRIVER_FNAME, "" + father_Name);
            request.addProperty(utils.AADHAR_NO, "" + aadhar_No);
            request.addProperty(utils.PAN_NO, "" + panCard_No);
            request.addProperty(utils.PSSPRT_NO, "" + passport_No);
            request.addProperty(utils.VOTER_ID, "" + voter_Id);
            request.addProperty(utils.CONTACT_NO, "" + contact_No);
            request.addProperty(utils.OFENCE_CODE, "" + offence_Code);
            request.addProperty(utils.OFENCE_AMNT, "");
            request.addProperty(utils.DETAINED_ITEMS, "" + detained_Items);
            request.addProperty(utils.COUNCELLING_DATE, "" + councelling_Date);
            request.addProperty(utils.ALCHL_READING, "" + alcohol_Reading);
            request.addProperty(utils.AGE, "" + user_age);
            request.addProperty(utils.OCUPTN, "" + user_occupation);
            request.addProperty(utils.QLFCTN, "" + user_qualification);
            request.addProperty(utils.GENDER, "" + user_gendor);
            request.addProperty(utils.CHECK_SI_NO, "" + check_SlNo);
            request.addProperty(utils.WHLR_CODE, "" + wheeler_Cd);
            request.addProperty(utils.MAKER_CODE, "" + maker_Cd);
            request.addProperty(utils.VEH_MAIN_CAT, "" + vehMain_Category);
            request.addProperty(utils.VEH_SUB_CAT, "" + vehSub_Category);
            request.addProperty(utils.ADDRESS, "" + user_address);
            request.addProperty(utils.CITY, "" + user_city);
            request.addProperty(utils.UNIT_CD, "" + user_unitCd);
            request.addProperty(utils.UNIT_NAME, "" + user_unitName);
            request.addProperty(utils.CADRE_CODE, "" + cadre_Cd);
            request.addProperty(utils.CADRE, "" + cadre_only);
            request.addProperty(utils.BASE_64_IMG, "" + imgEncoded_String);
            request.addProperty(utils.MOB_IMEI, "" + device_imei);
            request.addProperty(utils.GPS_LAT, "" + gps_Lattitude);
            request.addProperty(utils.GPS_LOG, "" + gps_Longitude);
            request.addProperty(utils.MAC_ID, "" + mob_macId);
            request.addProperty(utils.SIM_ID, "" + sim_Id);
            request.addProperty(utils.BREATHE_ANYLSR_ID, "" + breath_AnalyserId);
            request.addProperty(utils.LICENCE_STATUS, "" + lcnce_status);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (Opdata_Chalana.toString().trim().equals("0")) {
                Opdata_Chalana = "0";
                final_reponse_split = new String[0];
                final_response_master = new String[0];
            } else if (Opdata_Chalana.toString().trim().equals("1")) {
                Opdata_Chalana = "1";
                final_reponse_split = new String[0];
                final_response_master = new String[0];
            } else {
                final_response_master = new String[0];
                final_reponse_split = new String[0];
                final_reponse_split = Opdata_Chalana.split("@");
                if (final_reponse_split[1].toString().trim().length() > 0) {
                    final_response_master = (final_reponse_split[1].split("!"));
                }
            }
        } catch (SoapFault fault) {
            if (final_reponse_split.length == 0) {
                final_reponse_split = new String[0];
            }
            if (final_response_master.length == 0) {
                final_response_master = new String[0];
            }
            if ((final_reponse_split.length == 0) && (final_response_master.length == 0)) {
                Opdata_Chalana = "0";
            }
        } catch (SocketTimeoutException fault) {
            if (final_reponse_split.length == 0) {
                final_reponse_split = new String[0];
            }
            if (final_response_master.length == 0) {
                final_response_master = new String[0];
            }
            if ((final_reponse_split.length == 0) && (final_response_master.length == 0)) {
                Opdata_Chalana = "0";
            }
        } catch (Exception e) {
            // TODO: handle exception
            if (final_reponse_split.length == 0) {
                final_reponse_split = new String[0];
            }
            if (final_response_master.length == 0) {
                final_response_master = new String[0];
            }
            if ((final_reponse_split.length == 0) && (final_response_master.length == 0)) {
                Opdata_Chalana = "0";
            }
        }
    }

    public static void generateDrunDriveCase_1_5_2(String regn_Cd, String vehicle_No, String regn_No,
                                                   String operater_Cd, String operater_Name, String pid_Cd, String pid_Name, String ps_Code, String ps_Name,
                                                   String booked_PsCode, String booked_PsName, String point_Code, String poin_tName, String exact_Location,
                                                   String offence_Date, String offence_Time, String fined_By, String owner_DLNo, String driver_DLNo,
                                                   String driver_Name, String father_Name, String aadhar_No, String panCard_No, String passport_No,
                                                   String dldob, String contact_No, String offence_Code, String offence_Amount, String detained_Items,
                                                   String councelling_Date, String alcohol_Reading, String user_age, String user_occupation,
                                                   String user_qualification, String user_gendor, String check_SlNo, String wheeler_Cd, String maker_Cd,
                                                   String vehMain_Category, String rtaresponse, String user_address, String user_city, String user_unitCd,
                                                   String user_unitName, String cadre_Cd, String cadre_only, String imgEncoded_String, String device_imei,
                                                   String gps_Lattitude, String gps_Longitude, String mob_macId, String sim_Id, String breath_AnalyserId,
                                                   String lcnce_status, String nameOfBar_WineShop, String addressOfBar_WineShop, String occupation_Name,
                                                   String occupation_Address,String typeOfVeh) {
        Utils utils = new Utils();
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + GENERATE_DRUNK_DRIVE_CASE1_5_2);
            request.addProperty("typeOfVeh", "" + typeOfVeh);
            request.addProperty(utils.REG_CD, "" + regn_Cd);
            request.addProperty(utils.VEH_NO, "" + vehicle_No);
            request.addProperty(utils.REG_NO, "" + regn_No);
            request.addProperty(utils.OPTR_CD, "" + operater_Cd);
            request.addProperty(utils.OPTR_NAME, "" + operater_Name);
            request.addProperty(utils.PID_CD, "" + pid_Cd);
            request.addProperty(utils.PID_NAME, "" + pid_Name);
            request.addProperty(utils.PS_CODE, "" + ps_Code);
            request.addProperty(utils.PS_NAME, "" + ps_Name);
            request.addProperty(utils.BKD_PS_CODE, "" + booked_PsCode);
            request.addProperty(utils.BKD_PS_NAME, "" + booked_PsName);
            request.addProperty(utils.POINT_CODE, "" + point_Code);
            request.addProperty(utils.POINT_NAME, "" + poin_tName);
            request.addProperty(utils.EXCT_LOCATION, "" + exact_Location);
            request.addProperty(utils.OFFNCE_DATE, "" + offence_Date);
            request.addProperty(utils.OFFENCE_TIME, "" + offence_Time);
            request.addProperty(utils.FINED_BY, "" + fined_By);
            request.addProperty(utils.OWNR_DL_NO, "" + owner_DLNo);
            request.addProperty(utils.DRIVER_DL_NO, "" + driver_DLNo);
            request.addProperty(utils.DRIVER_NAME, "" + driver_Name);
            request.addProperty(utils.DRIVER_FNAME, "" + father_Name);
            request.addProperty(utils.AADHAR_NO, "" + aadhar_No);
            request.addProperty(utils.PAN_NO, "" + panCard_No);
            request.addProperty(utils.PSSPRT_NO, "" + passport_No);
            request.addProperty(utils.TOWING_DOB, "" + dldob);
            request.addProperty(utils.CONTACT_NO, "" + contact_No);
            request.addProperty(utils.OFENCE_CODE, "" + offence_Code);
            request.addProperty(utils.OFENCE_AMNT, "");
            request.addProperty(utils.DETAINED_ITEMS, "" + detained_Items);
            request.addProperty(utils.COUNCELLING_DATE, "" + councelling_Date);
            request.addProperty(utils.ALCHL_READING, "" + alcohol_Reading);
            request.addProperty(utils.AGE, "" + user_age);
            request.addProperty(utils.OCUPTN, "" + user_occupation);
            request.addProperty(utils.QLFCTN, "" + user_qualification);
            request.addProperty(utils.GENDER, "" + user_gendor);
            request.addProperty(utils.CHECK_SI_NO, "" + check_SlNo);
            request.addProperty(utils.WHLR_CODE, "" + wheeler_Cd);
            request.addProperty(utils.MAKER_CODE, "" + maker_Cd);
            request.addProperty(utils.VEH_MAIN_CAT, "" + onlinebuff);
            request.addProperty(utils.VEH_SUB_CAT, "" + rtaresponse);
            request.addProperty(utils.ADDRESS, "" + user_address);
            request.addProperty(utils.CITY, "" + user_city);
            request.addProperty(utils.UNIT_CD, "" + user_unitCd);
            request.addProperty(utils.UNIT_NAME, "" + user_unitName);
            request.addProperty(utils.CADRE_CODE, "" + cadre_Cd);
            request.addProperty(utils.CADRE, "" + cadre_only);
            request.addProperty(utils.BASE_64_IMG, "" + imgEncoded_String);
            request.addProperty(utils.MOB_IMEI, "" + device_imei);
            request.addProperty(utils.GPS_LAT, "" + gps_Lattitude);
            request.addProperty(utils.GPS_LOG, "" + gps_Longitude);
            request.addProperty(utils.MAC_ID, "" + mob_macId);
            request.addProperty(utils.SIM_ID, "" + sim_Id);
            request.addProperty(utils.BREATHE_ANYLSR_ID, "" + breath_AnalyserId);
            request.addProperty(utils.LICENCE_STATUS, "" + lcnce_status);

            request.addProperty(utils.DD_NAME_OF_BAR, "" + nameOfBar_WineShop);
            request.addProperty(utils.DD_ADDRESS_OF_BAR, "" + addressOfBar_WineShop);
            request.addProperty(utils.DD_OCCUPATION, "" + occupation_Name);
            request.addProperty(utils.DD_OCCUP_ADDRSS, "" + occupation_Address);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());

                    if (Opdata_Chalana.toString().trim().equals("0")) {
                        Opdata_Chalana = "0";
                        final_reponse_split = new String[0];
                        final_response_master = new String[0];
                        GenerateDrunkDriveCase.otpStatus = null;

                    } else if (Opdata_Chalana.toString().trim().equals("1")) {
                        Opdata_Chalana = "1";
                        final_reponse_split = new String[0];
                        final_response_master = new String[0];
                        GenerateDrunkDriveCase.otpStatus = null;
                    } else {
                        final_response_master = new String[0];
                        final_reponse_split = new String[0];// TO STORE SPLETTED DATA
                        final_reponse_split = Opdata_Chalana.split("@");
                        if (final_reponse_split[1].toString().trim().length() > 0) {
                            final_response_master = (final_reponse_split[1].split("!"));
                        }
                    }

                } else {
                    Opdata_Chalana = "0";
                    final_reponse_split = new String[0];
                    final_response_master = new String[0];
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Opdata_Chalana = "0";
                final_reponse_split = new String[0];
                final_response_master = new String[0];
            }


        } catch (SoapFault fault) {
            Opdata_Chalana = "0";
            final_reponse_split = new String[0];
            final_response_master = new String[0];
        } catch (SocketTimeoutException fault) {
            Opdata_Chalana = "0";
            final_reponse_split = new String[0];
            final_response_master = new String[0];
        } catch (Exception e) {
            // TODO: handle exception
            Opdata_Chalana = "0";
            //   if (final_reponse_split.length == 0) {
            final_reponse_split = new String[0];
            // }
            // if (final_response_master.length == 0) {
            final_response_master = new String[0];
        }
    }

    public static void getOffenderRemarks(String vhle_num, String license, String aadhar) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + RTA_DETAILS_BY_REGNO_LICENSE_AADHAR_METHOD_NAME);
            request.addProperty("regn_no", vhle_num + "^" + rta_data);
            request.addProperty("licenceNo", "" + license);
            request.addProperty("aadharNo", "" + aadhar);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            offender_remarks = "";

            try {
                if (result != null) {
                    offender_remarks = result.toString();
                    offender_remarks = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(offender_remarks);
                } else {
                    offender_remarks = "0";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                offender_remarks = "0";
            }
        } catch (SoapFault fault) {
            offender_remarks = "0";
        } catch (SocketTimeoutException fault) {
            offender_remarks = "0";
        } catch (Exception e) {
            // TODO: handle exception
            offender_remarks = "0";
        }
    }

    public static void getVehRemarks(String vhle_num, String license, String aadhar) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + "getVehRemarks");
            request.addProperty("regnNo", vhle_num);
            request.addProperty("aadharno", "" + aadhar);
            request.addProperty("dlNo", "" + license);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            offender_remarks = "";

            try {
                if (result != null) {
                    offender_remarks = result.toString();
                    // offender_remarks = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(offender_remarks);
                } else {
                    offender_remarks = "0";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                offender_remarks = "0";
            }
        } catch (SoapFault fault) {
            offender_remarks = "0";
        } catch (SocketTimeoutException fault) {
            offender_remarks = "0";
        } catch (Exception e) {
            // TODO: handle exception
            offender_remarks = "0";
        }
    }

    public static void getRTADetails(String vhle_num) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + RTA_DETAILS_BY_REGNO_METHOD_NAME);
            request.addProperty("regn_no", "" + vhle_num);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {

                if (result != null) {
                    rta_data = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                    rc_send = "1|" + rta_data;

                    if (rta_data != null && !rta_data.equalsIgnoreCase("0")) {
                        try {
                            if (Dashboard.rta_details_request_from.equals("RTACLASS")) {
                                Drunk_Drive.rta_details_master = new String[0];
                                Drunk_Drive.rta_details_master = rta_data.split("!");
                            } else if (Dashboard.rta_details_request_from.equals("SPOT")) {
                                SpotChallan.rta_details_spot_master = new String[0];
                                SpotChallan.rta_details_spot_master = rta_data.split("!");
                            }
                       /*     else if (Dashboard.rta_details_request_from.equals("noncontact")) {
                                SpotChallan.rta_details_spot_master = new String[0];
                                SpotChallan.rta_details_spot_master = rta_data.split("!");
                            }*/
                        } catch (Exception e) {
                            e.printStackTrace();
                            rta_data = "0";
                            if (Dashboard.rta_details_request_from.equals("RTACLASS")) {
                                Drunk_Drive.rta_details_master = new String[0];
                            } else if (Dashboard.rta_details_request_from.equals("SPOT")) {
                                SpotChallan.rta_details_spot_master = new String[0];
                            }
                           /* else if (Dashboard.rta_details_request_from.equals("noncontact")) {
                                SpotChallan.rta_details_spot_master = new String[0];
                            }*/
                        }
                    }
                } else {
                    rta_data = "0";
                    if (Dashboard.rta_details_request_from.equals("RTACLASS")) {
                        Drunk_Drive.rta_details_master = new String[0];
                    } else if (Dashboard.rta_details_request_from.equals("SPOT")) {
                        SpotChallan.rta_details_spot_master = new String[0];
                    }
                   /* else if (Dashboard.rta_details_request_from.equals("noncontact")) {
                        SpotChallan.rta_details_spot_master = new String[0];
                    }*/
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                rta_data = "0";
                if (Dashboard.rta_details_request_from.equals("RTACLASS")) {
                    Drunk_Drive.rta_details_master = new String[0];
                } else if (Dashboard.rta_details_request_from.equals("SPOT")) {
                    SpotChallan.rta_details_spot_master = new String[0];
                }
               /* else if (Dashboard.rta_details_request_from.equals("noncontact")) {
                    SpotChallan.rta_details_spot_master = new String[0];
                }*/
            }

        } catch (SoapFault fault) {

            rta_data = "0";
            if (Dashboard.rta_details_request_from.equals("RTACLASS")) {
                Drunk_Drive.rta_details_master = new String[0];
            } else if (Dashboard.rta_details_request_from.equals("SPOT")) {
                SpotChallan.rta_details_spot_master = new String[0];
            }
            /*else if (Dashboard.rta_details_request_from.equals("noncontact")) {
                SpotChallan.rta_details_spot_master = new String[0];
            }*/
        } catch (SocketTimeoutException fault) {

            rta_data = "0";
            if (Dashboard.rta_details_request_from.equals("RTACLASS")) {
                Drunk_Drive.rta_details_master = new String[0];
            } else if (Dashboard.rta_details_request_from.equals("SPOT")) {
                SpotChallan.rta_details_spot_master = new String[0];
            }
            /*else if (Dashboard.rta_details_request_from.equals("noncontact")) {
                SpotChallan.rta_details_spot_master = new String[0];
            }*/
        } catch (Exception e) {
            // TODO: handle exception
            // Opdata_Chalana = "0";
            rta_data = "0";
            if (Dashboard.rta_details_request_from.equals("RTACLASS")) {
                Drunk_Drive.rta_details_master = new String[0];
            } else if (Dashboard.rta_details_request_from.equals("SPOT")) {
                SpotChallan.rta_details_spot_master = new String[0];
            }
          /*  else if (Dashboard.rta_details_request_from.equals("noncontact")) {
                SpotChallan.rta_details_spot_master = new String[0];
            }*/
        }
    }

    public static void getpendingChallansByRegNo(String regno, String drvr_lcno, String ownr_lcnce_no, String pid,
                                                 String pidname, String pwd, String simid, String imei, String lat, String logn, String ip_val,
                                                 String unit_code) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + PENDING_CHALLANS_BY_REGNO);
            request.addProperty("regnNo", "" + regno);
            request.addProperty("drvierLicNo", "" + drvr_lcno);
            request.addProperty("ownerLicNo", "" + ownr_lcnce_no);
            request.addProperty("pidCd", "" + pid);
            request.addProperty("pidName", "" + pidname);
            request.addProperty("password", "" + pwd);
            request.addProperty("simId", "" + simid);
            request.addProperty("imeiNo", "" + imei);
            request.addProperty("latitude", "" + lat);
            request.addProperty("longitude", "" + logn);
            request.addProperty("ip", "" + ip_val);
            request.addProperty("unitCode", "" + Dashboard.UNIT_CODE);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Opdata_Chalana = "0";

                e.printStackTrace();
            }
            if (Opdata_Chalana.toString().equals("0")) {
                Opdata_Chalana = "0";
                pending_challans_master = new String[0];
                pending_challans_details = new String[0][0];
            } else {
                pending_challans_master = new String[0];
                pending_challans_master = Opdata_Chalana.split("@");
                pending_challans_details = new String[pending_challans_master.length][8];

                for (int i = 0; i < pending_challans_master.length; i++) {
                    pending_challans_details[i] = pending_challans_master[i].split("!");
                }
            }
        } catch (SocketTimeoutException e) {
            Opdata_Chalana = "nodata";
            pending_challans_master = new String[0];
            pending_challans_details = new String[0][0];
        } catch (Exception e) {
            Opdata_Chalana = "nodata";
            pending_challans_master = new String[0];
            pending_challans_details = new String[0][0];
        }
    }

    public static void getLicenceDetails(String lcnce_num, String dob) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + LICENCE_DETAILS_METHOD_NAME);
            request.addProperty("licenseno", "" + (lcnce_num.replace(" ", "")));
            request.addProperty("DOB", dob);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    license_data = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                    if (license_data != null || license_data.trim().length() > 4) {
                        dl_send = lcnce_num + "|" + license_data;
                        onlinebuff = onlinebuff.append("2" + "|" + license_data + "^");
                        SpotChallan.licence_details_spot_master = new String[0];
                        SpotChallan.licence_details_spot_master = license_data.split("!");
                    } else {
                        license_data = "0";
                        if (Dashboard.licence_details_request_from.equals("RTACLASS")) {
                            Drunk_Drive.licene_details_master = new String[0];
                        } else if (Dashboard.licence_details_request_from.equals("SPOT")) {
                            SpotChallan.licence_details_spot_master = new String[0];
                        }
                    }
                } else {
                    license_data = "0";
                    if (Dashboard.licence_details_request_from.equals("RTACLASS")) {
                        Drunk_Drive.licene_details_master = new String[0];
                    } else if (Dashboard.licence_details_request_from.equals("SPOT")) {
                        SpotChallan.licence_details_spot_master = new String[0];
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                license_data = "0";
                if (Dashboard.licence_details_request_from.equals("RTACLASS")) {
                    Drunk_Drive.licene_details_master = new String[0];
                } else if (Dashboard.licence_details_request_from.equals("SPOT")) {
                    SpotChallan.licence_details_spot_master = new String[0];
                }
            }

        } catch (SoapFault f) {
            f.printStackTrace();

            license_data = "0";
            if (Dashboard.licence_details_request_from.equals("RTACLASS")) {
                Drunk_Drive.licene_details_master = new String[0];
            } else if (Dashboard.licence_details_request_from.equals("SPOT")) {
                SpotChallan.licence_details_spot_master = new String[0];
            }

        } catch (SocketTimeoutException f) {
            f.printStackTrace();

            license_data = "0";
            if (Dashboard.licence_details_request_from.equals("RTACLASS")) {
                Drunk_Drive.licene_details_master = new String[0];
            } else if (Dashboard.licence_details_request_from.equals("SPOT")) {
                SpotChallan.licence_details_spot_master = new String[0];
            }

        } catch (Exception e) {
            e.printStackTrace();
            license_data = "0";
            if (Dashboard.licence_details_request_from.equals("RTACLASS")) {
                Drunk_Drive.licene_details_master = new String[0];
            } else if (Dashboard.licence_details_request_from.equals("SPOT")) {
                SpotChallan.licence_details_spot_master = new String[0];
            }
        }
    }

    public static void generateNonContactChallan(String regnCD, String vehicleNo, String wheelerCode, String rtaoName, String rtaAddr, String city, String mobileNo,
                                                 String offenceDtTime, String pidCD, String pidName, String cadre_code, String cadre_name, String psCode, String psName,
                                                 String unitCode, String unitName, String pointCD, String pointName, String vioCode, String vehRemak,
                                                 String evidenceData, String imeiNo, String macID, String gpsLatti, String gpsLong) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "eChallanMobile");
            request.addProperty("regnCD", "" + regnCD);
            request.addProperty("vehicleNo", "" + vehicleNo);
            request.addProperty("wheelerCode", "" + wheelerCode);
            request.addProperty("rtaOname", "" + rtaoName);
            request.addProperty("rtaAdres", "" + rtaAddr);
            request.addProperty("city", "" + city);
            request.addProperty("mobileNo", "" + mobileNo);
            request.addProperty("offenceDtTime", "" + offenceDtTime);
            request.addProperty("pidCD", "" + pidCD);
            request.addProperty("pidName", "" + pidName);
            request.addProperty("cadreCD", "" + cadre_code);
            request.addProperty("cadreName", "" + cadre_name);
            request.addProperty("psCode", "" + psCode);
            request.addProperty("psName", "" + psName);
            request.addProperty("unitCode", "" + unitCode);
            request.addProperty("unitName", "" + unitName);
            request.addProperty("pointCD", "" + pointCD);
            request.addProperty("pointName", "" + pointName);
            request.addProperty("vioCode", "" + vioCode);
            request.addProperty("vehRemak", "" + vehRemak);
            request.addProperty("evidenceData", "" + evidenceData);
            request.addProperty("imeiNo", "" + imeiNo);
            request.addProperty("macID", "" + macID);
            request.addProperty("gpsLatti", "" + gpsLatti);
            request.addProperty("gpsLong", "" + gpsLong);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                //  points_resp = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());

                ServiceHelper.noncontactresponse = "0";
                noncontactresponse = result.toString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                ServiceHelper.noncontactresponse = "0";
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ServiceHelper.noncontactresponse = "0";
        }
    }

    public static void getViolations_Points(String unit_Code, String aadhar_No, String driving_LicenseNo) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + GET_VIOLATIONS_POINTS);
            request.addProperty("unitCode", "" + unit_Code);
            request.addProperty("aadharNo", "" + aadhar_No);
            request.addProperty("drivingLicenseNo", "" + driving_LicenseNo);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                points_resp = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getViolationDetails(String whlr_code) {
        //String challanType
        try {
            SoapObject request = new SoapObject(NAMESPACE, VIOLATION_DETAILS_METHOD_NAME);
            request.addProperty("wheelercode", whlr_code);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            System.out.println(" getviolations by wheeler response :" + result.toString());
            try {

                if (result != null) {
                    Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());

                    if (Opdata_Chalana != null && Opdata_Chalana.toString().trim().equals("0")) {
                        Opdata_Chalana = "0";
                        violation_details_master = new String[0];
                        violation_detailed_views = new String[0][0];
                    } else if (Opdata_Chalana != null && !"0".equals(Opdata_Chalana.toString().trim())) {

                        violation_details_master = new String[0];
                        violation_detailed_views = new String[0][0];
                        violation_details_master = (Opdata_Chalana.substring(1, Opdata_Chalana.length())).split("!");

                        if (violation_details_master != null && violation_details_master.length > 0) {

                            violation_detailed_views = new String[violation_details_master.length][6];
                            for (int i = 0; i < violation_details_master.length; i++) {
                                violation_detailed_views[i] = violation_details_master[i].toString().trim().split("@");
                            }
                        } else {
                            violation_detailed_views = new String[0][0];
                        }
                    }
                } else {
                    Opdata_Chalana = "0";
                    violation_details_master = new String[0];
                    violation_detailed_views = new String[0][0];
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Opdata_Chalana = "0";
                violation_details_master = new String[0];
                violation_detailed_views = new String[0][0];
            }

        } catch (SoapFault fault) {
            Opdata_Chalana = "0";
            violation_details_master = new String[0];
            violation_detailed_views = new String[0][0];
        } catch (Exception e) {
            Opdata_Chalana = "0";
            violation_details_master = new String[0];
            violation_detailed_views = new String[0][0];
        }
    }

    public static void getOffenceDetailsbyWheelerChallanTypeUnit(String wheelercode, String chllanType, String pidCode) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getOffenceDetailsbyWheelerChallanTypeUnit");
            request.addProperty("wheelercode", wheelercode);
            request.addProperty("challanType", chllanType);
            request.addProperty("pidCode", pidCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {

                if (result != null) {
                    Opdata_Chalana = "";
                    Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());

                    if (Opdata_Chalana != null && Opdata_Chalana.toString().trim().equals("0")) {
                        Opdata_Chalana = "0";
                        violation_details_master = new String[0];
                        violation_detailed_views = new String[0][0];
                    } else if (Opdata_Chalana != null && !"0".equals(Opdata_Chalana.toString().trim())) {

                        violation_details_master = new String[0];
                        violation_detailed_views = new String[0][0];
                        violation_details_master = (Opdata_Chalana.substring(1, Opdata_Chalana.length())).split("!");

                        if (violation_details_master != null && violation_details_master.length > 0) {

                            violation_detailed_views = new String[violation_details_master.length][6];
                            for (int i = 0; i < violation_details_master.length; i++) {
                                violation_detailed_views[i] = violation_details_master[i].toString().trim().split("@");
                            }
                        } else {
                            violation_detailed_views = new String[0][0];
                        }
                    }
                } else {
                    Opdata_Chalana = "0";
                    violation_details_master = new String[0];
                    violation_detailed_views = new String[0][0];
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Opdata_Chalana = "0";
                violation_details_master = new String[0];
                violation_detailed_views = new String[0][0];
            }

        } catch (SoapFault fault) {
            Opdata_Chalana = "0";
            violation_details_master = new String[0];
            violation_detailed_views = new String[0][0];
        } catch (SocketTimeoutException fault) {
            Opdata_Chalana = "0";
            violation_details_master = new String[0];
            violation_detailed_views = new String[0][0];
        } catch (Exception e) {
            Opdata_Chalana = "0";
            violation_details_master = new String[0];
            violation_detailed_views = new String[0][0];
        }
    }

    public static void getOffenceDetailsbyWheelerChallanTypeUnitRemark(String wheelercode, String chllanType, String pidCode, String remark) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getOffenceDetailsbyWheelerChallanTypeUnitRemark");
            request.addProperty("wheelercode", wheelercode);
            request.addProperty("challanType", chllanType);
            request.addProperty("pidCode", pidCode);
            request.addProperty("remark", remark);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    Opdata_Chalana = "";
                    Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                    if (Opdata_Chalana != null && Opdata_Chalana.toString().trim().equals("0")) {
                        Opdata_Chalana = "0";
                        violation_details_master = new String[0];
                        violation_detailed_views = new String[0][0];
                    } else if (Opdata_Chalana != null && !"0".equals(Opdata_Chalana.toString().trim())) {

                        violation_details_master = new String[0];
                        violation_detailed_views = new String[0][0];
                        violation_details_master = (Opdata_Chalana.substring(1, Opdata_Chalana.length())).split("!");

                        if (violation_details_master != null && violation_details_master.length > 0) {

                            violation_detailed_views = new String[violation_details_master.length][6];
                            for (int i = 0; i < violation_details_master.length; i++) {
                                violation_detailed_views[i] = violation_details_master[i].toString().trim().split("@");
                            }
                        } else {
                            violation_detailed_views = new String[0][0];
                        }
                    }
                } else {
                    Opdata_Chalana = "0";
                    violation_details_master = new String[0];
                    violation_detailed_views = new String[0][0];
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Opdata_Chalana = "0";
                violation_details_master = new String[0];
                violation_detailed_views = new String[0][0];
            }

        } catch (SoapFault fault) {
            Opdata_Chalana = "0";
            violation_details_master = new String[0];
            violation_detailed_views = new String[0][0];
        } catch (SocketTimeoutException fault) {
            Opdata_Chalana = "0";
            violation_details_master = new String[0];
            violation_detailed_views = new String[0][0];
        } catch (Exception e) {
            Opdata_Chalana = "0";
            violation_details_master = new String[0];
            violation_detailed_views = new String[0][0];
        }
    }

    public static void getOffenceDetailsbyWheelerChallanType(String wheelercode, String chllanType) {
        //String challanType
        try {
            SoapObject request = new SoapObject(NAMESPACE, get_OffenceDetailsbyWheelerChallanType);
            request.addProperty("wheelercode", wheelercode);
            request.addProperty("challanType", chllanType);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {

                if (result != null) {
                    Opdata_Chalana = "";
                    Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());

                    if (Opdata_Chalana != null && Opdata_Chalana.toString().trim().equals("0")) {
                        Opdata_Chalana = "0";
                        violation_details_master = new String[0];
                        violation_detailed_views = new String[0][0];
                    } else if (Opdata_Chalana != null && !"0".equals(Opdata_Chalana.toString().trim())) {

                        violation_details_master = new String[0];
                        violation_detailed_views = new String[0][0];
                        violation_details_master = (Opdata_Chalana.substring(1, Opdata_Chalana.length())).split("!");

                        if (violation_details_master != null && violation_details_master.length > 0) {

                            violation_detailed_views = new String[violation_details_master.length][6];
                            for (int i = 0; i < violation_details_master.length; i++) {
                                violation_detailed_views[i] = violation_details_master[i].toString().trim().split("@");
                            }
                        } else {
                            violation_detailed_views = new String[0][0];
                        }
                    }
                } else {
                    Opdata_Chalana = "0";
                    violation_details_master = new String[0];
                    violation_detailed_views = new String[0][0];
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Opdata_Chalana = "0";
                violation_details_master = new String[0];
                violation_detailed_views = new String[0][0];
            }

        } catch (SoapFault fault) {
            Opdata_Chalana = "0";
            violation_details_master = new String[0];
            violation_detailed_views = new String[0][0];
        } catch (SocketTimeoutException fault) {
            Opdata_Chalana = "0";
            violation_details_master = new String[0];
            violation_detailed_views = new String[0][0];
        } catch (Exception e) {
            Opdata_Chalana = "0";
            violation_details_master = new String[0];
            violation_detailed_views = new String[0][0];
        }
    }

    /* TO GET DETAINED ITEMS LIST */
    public static void getDetainedItemsList(String v_num, String unt_code) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + GET_DETAINED_ITEMS_METHOD_NAME);
            request.addProperty("regnNo", "" + (v_num));
            request.addProperty("unitCode", "" + (unt_code));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {

                    Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());

                    if (Opdata_Chalana != null) {

                        detained_items_list_master = new String[0];
                        detained_items_list_details = new String[0][0];

                        if (Opdata_Chalana != null && Opdata_Chalana.trim().length() > 3) {
                            detained_items_list_master = Opdata_Chalana.split("@");

                            if (detained_items_list_master != null && detained_items_list_master.length > 0) {
                                detained_items_list_details = new String[detained_items_list_master.length][3];

                                for (int i = 0; i < detained_items_list_master.length; i++) {
                                    detained_items_list_details[i] = detained_items_list_master[i].toString().trim().split(":");
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Opdata_Chalana = null;
                detained_items_list_master = new String[0];
                detained_items_list_details = new String[0][0];
                e.printStackTrace();
            }


        } catch (SoapFault fault) {
            Opdata_Chalana = null;
            detained_items_list_master = new String[0];
            detained_items_list_details = new String[0][0];
            fault.printStackTrace();
        } catch (SocketTimeoutException fault) {
            Opdata_Chalana = null;
            detained_items_list_master = new String[0];
            detained_items_list_details = new String[0][0];
            fault.printStackTrace();
        } catch (Exception e) {

            Opdata_Chalana = null;
            detained_items_list_master = new String[0];
            detained_items_list_details = new String[0][0];
            e.printStackTrace();
        }
    }

    public static void getAadharDetails(String uid, String eid) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + AADHAR_DETAILS_METHOD_NAME);
            request.addProperty("uid", "" + (uid.toString().trim()));
            request.addProperty("eid", "" + (eid.toString().trim()));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();


            try {
                if (result != null) {

                    aadhar_data = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                    if (aadhar_data != null && !"NA".equals(aadhar_data.toString().trim()) && !"0".equals(aadhar_data.toString().trim())) {
                        aadhar_details = new String[0];
                        aadhar_details = aadhar_data.split("@");

                    } else {
                        aadhar_details = new String[0];
                    }

                } else {
                    aadhar_data = "0";
                    aadhar_details = new String[0];

                }

            } catch (Exception e) {
                aadhar_data = "0";
                aadhar_details = new String[0];
                e.printStackTrace();
            }


        } catch (SoapFault fault) {
            fault.printStackTrace();
            aadhar_data = "0";
            aadhar_details = new String[0];

        } catch (Exception e) {
            // TODO: handle exception
            Log.i("AAdhar NA........", "NACatch");
            aadhar_data = "0";
            aadhar_details = new String[0];
        }
    }

    public static void getAllWheelerViolations() {
        try {

            SoapObject request = new SoapObject(NAMESPACE, "" + ALL_WHEELER_VIOLATION_DETAILS_METHOD_NAME);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            sbuffer_allViolations = new StringBuffer();
            sbuffer_allViolations.append(Opdata_Chalana);

            if (Opdata_Chalana.toString().trim().equals("0")) {
            } else {
            }

        } catch (SoapFault fault) {
        } catch (Exception e) {
            Opdata_Chalana = "0";
        }
    }

    public static void mobileSpotChallanPayingNew1_5_2(String selectedPendingChallans, String regnCd, String vehicleNo,
                                                       String regnNo, String gtwyCd, String dOfPay, String pmtTime, String unitCode, String unitName,
                                                       String psCode, String psName, String bookedPsCd, String bookedPsName, String pointCode, String pointName,
                                                       String operaterCd, String operaterName, String pidCd, String pidName, String cadreCD, String cadre,
                                                       String totalAmount, String detained, String simId, String imeiNo, String macId, String latitude,
                                                       String longitude, String gpsDt, String gpsTime, String onlineMode, String moduleCd, String releasedItem,
                                                       String challanNo, String serviceCode, String ownerLicNo, String drvierLicNo, String password,
                                                       String imageEvidence, String challanType, String challanCd, String location, String remarks,
                                                       String pancardNo, String aadharNo, String voterId, String passport, String email, String driverContactNo,
                                                       String isItSpotPmt, String offenceDt, String offenceTime, String licStatus, String wheelerCd,
                                                       String vehCategory, String obMkrCD, String vehicleMainType, String violations, String pendingChallans,
                                                       String noOfExtraPassengers, String driverName, String driverFatherName, String driverAddress,
                                                       String driverCity, String typeOfVeh, String occupation_Name, String occupation_Address,
                                                       String occupation_EmailId) {

        Utils utils = new Utils();
        try {
            SoapObject request = null;
            if (Dashboard.check_vhleHistory_or_Spot.equals("spot")) {
                request = new SoapObject(NAMESPACE, "" + SPOT_CHALLAN_PAYMENT_NEW_1_5_2);
                request.addProperty(utils.SPOT_VEHICLE_TYPE, "" + typeOfVeh);
            }

            request.addProperty(utils.VHLE_HIST_SELECTED_PEN_CHALLANS, "" + selectedPendingChallans);
            request.addProperty(utils.SPOT_REGNCD, "" + regnCd);
            request.addProperty(utils.SPOT_VHLE_NO, "" + vehicleNo);
            request.addProperty(utils.SPOT_REG_NO, "" + regnNo);
            request.addProperty(utils.SPOT_GTWY_CD, "" + gtwyCd);
            request.addProperty(utils.SPOT_DT_OF_PAY, "" + dOfPay);
            request.addProperty(utils.SPOT_PYMNT_TIME, "" + pmtTime);
            request.addProperty(utils.SPOT_UNIT_CODE, "" + unitCode);
            request.addProperty(utils.SPOT_UNIT_NAME, "" + unitName);
            request.addProperty(utils.SPOT_PS_CODE, "" + psCode);
            request.addProperty(utils.SPOT_PS_NAME, "" + psName);
            request.addProperty(utils.SPOT_BOOKED_PS_CODE, "" + bookedPsCd);
            request.addProperty(utils.SPOT_BOOBKED_PS_NAME, "" + bookedPsName);
            request.addProperty(utils.SPOT_POINT_CODE, "" + pointCode);
            request.addProperty(utils.SPOT_POINT_NAME, "" + pointName);
            request.addProperty(utils.SPOT_OPERATOR_CODE, "" + operaterCd);
            request.addProperty(utils.SPOT_OPERATOR_NAME, "" + operaterName);
            request.addProperty(utils.SPOT_PID_CODE, "" + pidCd);
            request.addProperty(utils.SPOT_PID_NAME, "" + pidName);
            request.addProperty(utils.SPOT_CADRE_CODE, "" + cadreCD);
            request.addProperty(utils.SPOT_CADRE, "" + cadre);
            request.addProperty(utils.SPOT_TOTAL_AMNT, "" + totalAmount);
            request.addProperty(utils.SPOT_DETAINED, "" + detained);
            request.addProperty(utils.SPOT_SIMID, "" + simId);
            request.addProperty(utils.SPOT_IMEI, "" + imeiNo);
            request.addProperty(utils.SPOT_MACID, "" + macId);
            request.addProperty(utils.SPOT_LAT, "" + latitude);
            request.addProperty(utils.SPOT_LONG, "" + longitude);
            request.addProperty(utils.SPOT_GPS_DATE, "" + gpsDt);
            request.addProperty(utils.SPOT_GPS_TIME, "" + gpsTime);
            request.addProperty(utils.SPOT_ONLINE_MODE, "" + onlineMode);
            request.addProperty(utils.SPOT_MODULE_CODE, "" + moduleCd);
            request.addProperty(utils.SPOT_RELEASE_ITEM, "" + releasedItem);
            request.addProperty(utils.SPOT_CHALLAN_NO, "" + challanNo);
            request.addProperty(utils.SPOT_SERVICE_CODE, "" + serviceCode);
            request.addProperty(utils.SPOT_OWNER_LIC_NO, "" + ownerLicNo);
            request.addProperty(utils.SPOT_DRVR_LIC_NO, "" + drvierLicNo);
            request.addProperty(utils.SPOT_PWD, "" + password);
            request.addProperty(utils.SPOT_IMAGE, "" + imageEvidence);
            request.addProperty(utils.SPOT_CHALLAN_TYPE, "" + challanType);
            request.addProperty(utils.SPOT_CHALLAN_CODE, "" + challanCd);
            request.addProperty(utils.SPOT_LOCATION, "" + location);
            request.addProperty(utils.SPOT_REMARKS, "" + remarks);
            request.addProperty(utils.SPOT_PANCARD_NO, "" + pancardNo);
            request.addProperty(utils.SPOT_AADHAR_NO, "" + aadharNo);
            request.addProperty(utils.SPOT_VOTERID, "" + voterId);
            request.addProperty(utils.SPOT_PASSPORT, "" + passport);
            request.addProperty(utils.SPOT_EMAIL, "" + email);
            request.addProperty(utils.SPOT_DRIVER_CNTCT_NO, "" + driverContactNo);
            request.addProperty(utils.SPOT_IS_IT_SPOT, "" + isItSpotPmt);
            request.addProperty(utils.SPOT_OFFENCE_DATE, "" + offenceDt);
            request.addProperty(utils.SPOT_OFFENCE_TIME, "" + offenceTime);
            request.addProperty(utils.SPOT_LIC_STATUS, "" + licStatus);
            request.addProperty(utils.SPOT_WHEELER_CODE, "" + wheelerCd);
            request.addProperty(utils.SPOT_VEH_CAT, "" + vehCategory);
            request.addProperty(utils.SPOT_VEH_MAIN_TYPE, "" + obMkrCD);
            request.addProperty(utils.SPOT_VEH_SUB, "" + vehicleMainType);
            request.addProperty(utils.SPOT_VIOLATIONS, "" + violations);
            request.addProperty(utils.SPOT_PENDING_CHALLANS, "" + pendingChallans);
            request.addProperty(utils.EXTRA_PASSENGERS, "" + noOfExtraPassengers);
            request.addProperty(utils.SPOT_DRIVERNAME, "" + driverName);
            request.addProperty(utils.SPOT_DRIVER_FNAME, "" + driverFatherName);
            request.addProperty(utils.SPOT_DRIVER_ADDRESS, "" + driverAddress);
            request.addProperty(utils.SPOT_DRIVER_CITY, "" + driverCity);
            request.addProperty(utils.DD_OCCUPATION, occupation_Name);
            request.addProperty(utils.DD_OCCUP_ADDRSS, occupation_Address);
            request.addProperty(utils.DD_OCCUP_MAIL, occupation_EmailId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            spot_final_res_status = "";
            try {
                spot_final_res_status = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            final_spot_reponse_master = new String[0];
            final_spot_reponse_violations_master = new String[0];
            final_spot_reponse_details = new String[0];
            final_spot_reponse_violations = new String[0][0];

            if (spot_final_res_status.toString().trim().equals("0")) {
                if (final_spot_reponse_master.length == 0) {
                    final_spot_reponse_master = new String[0];
                }
                if (final_spot_reponse_violations_master.length == 0) {
                    final_spot_reponse_violations_master = new String[0];
                }
                if (final_spot_reponse_details.length == 0) {
                    final_spot_reponse_details = new String[0];
                }
                if (final_spot_reponse_violations_master.length == 0) {
                    final_spot_reponse_violations = new String[0][0];
                }

            } else {

                final_spot_reponse_master = spot_final_res_status.split("\\^");

                if (final_spot_reponse_master[1].toString().trim().length() > 0) {
                    final_spot_reponse_details = final_spot_reponse_master[1].split("!");
                }

                if (final_spot_reponse_master[2].toString().trim().length() > 0) {

                    final_spot_reponse_violations_master = final_spot_reponse_master[2].split("!");
                    final_spot_reponse_violations = new String[final_spot_reponse_violations_master.length][3];

                    for (int i = 0; i < final_spot_reponse_violations_master.length; i++) {
                        final_spot_reponse_violations[i] = final_spot_reponse_violations_master[i].split("\\@");
                    }
                }
            }
        } catch (SoapFault fault) {

        } catch (Exception e) {
            // TODO: handle exception
            if (final_spot_reponse_master.length == 0) {
                final_spot_reponse_master = new String[0];
            }
            if (final_spot_reponse_violations_master.length == 0) {
                final_spot_reponse_violations_master = new String[0];
            }
            if (final_spot_reponse_details.length == 0) {
                final_spot_reponse_details = new String[0];
            }
            if (final_spot_reponse_violations_master.length == 0) {
                final_spot_reponse_violations = new String[0][0];
            }
        }
    }

    public static void checkSameChallan(String regnNo, String offenceDt, String pointCode, String selectedVioCodes, String pidCd) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "checkSameChallan");
            request.addProperty("regnNo", regnNo);
            request.addProperty("offenceDt", offenceDt);
            request.addProperty("pointCode", pointCode);
            request.addProperty("selectedVioCodes", selectedVioCodes);
            request.addProperty("pidCd", pidCd);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (null != result) {
                    duplicateChaln_Data = result.toString();
                } else {
                    duplicateChaln_Data = "NA";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                duplicateChaln_Data = "NA";
            }
        } catch (SoapFault fault) {
            duplicateChaln_Data = "NA";
        } catch (Exception E) {
            E.printStackTrace();
            duplicateChaln_Data = "NA";
        }
    }

    public static void mobileSpotChallanPayingNew15(String selectedPendingChallans, String regnCd, String vehicleNo,
                                                    String regnNo, String gtwyCd, String dOfPay, String pmtTime, String unitCode, String unitName,
                                                    String psCode, String psName, String bookedPsCd, String bookedPsName, String pointCode, String pointName,
                                                    String operaterCd, String operaterName, String pidCd, String pidName, String cadreCD, String cadre,
                                                    String totalAmount, String detained, String simId, String imeiNo, String macId, String latitude,
                                                    String longitude, String gpsDt, String gpsTime, String onlineMode, String moduleCd, String releasedItem,
                                                    String challanNo, String serviceCode, String ownerLicNo, String drvierLicNo, String password,
                                                    String imageEvidence, String challanType, String challanCd, String location, String remarks,
                                                    String pancardNo, String aadharNo, String voterId, String passport, String email, String driverContactNo,
                                                    String isItSpotPmt, String offenceDt, String offenceTime, String licStatus, String wheelerCd,
                                                    String vehCategory, String obMkrCD, String vehicleMainType, String violations, String pendingChallans,
                                                    String noOfExtraPassengers, String driverName, String driverFatherName, String driverAddress,
                                                    String driverCity, String typeOfVeh, String dl_dob) { // v


        try {

            if (!(onlinebuff == null)) {
                onlinebuff = new StringBuffer("");
                onlinebuff.delete(0, onlinebuff.length());
                onlinebuff.setLength(0); // v
            }
            if ((!drvierLicNo.equals("") && !"NA".equals(drvierLicNo)) && (!aadharNo.equals("") && !"NA".equals(aadharNo))) {


                if (license_data != "null" && !license_data.equals("0") && aadharNo != "null" && !aadharNo.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + license_data + "^" + "3" + "|" + aadhar_data);
                } else if (license_data.equals("0") && !aadhar_data.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + drvierLicNo + "^" + "3" + "|" + aadhar_data);

                } else if (!license_data.equals("0") && aadhar_data.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + license_data + "^" + "3" + "|" + aadharNo);

                } else if (license_data.equals("0") && aadhar_data.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + drvierLicNo + "^" + "3" + "|" + aadharNo);

                }

            } else if ((!drvierLicNo.equals("") && !"NA".equals(drvierLicNo))
                    && (aadharNo.equals("") || "NA".equals(aadharNo))) {

                if (license_data != "null" && !license_data.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + license_data + "^" + "3" + "|" + "NA");
                } else {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + drvierLicNo + "^" + "3" + "|" + "NA");

                }
            } else if ((drvierLicNo.equals("") || "NA".equals(drvierLicNo))
                    && (!aadharNo.equals("") && !"NA".equals(aadharNo))) {

                if (aadhar_data != "null" && !aadhar_data.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + "NA" + "^" + "3" + "|" + aadhar_data);
                } else {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + "NA" + "^" + "3" + "|" + aadharNo);

                }
            } else if ((drvierLicNo.equals(""))
                    && (aadharNo.equals(""))) {


                onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + "NA" + "^" + "3" + "|" + "NA");

            }

            Log.i("Online Buffer", onlinebuff.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Utils utils = new Utils();
        try {
            SoapObject request = null;
            if (Dashboard.check_vhleHistory_or_Spot.equals("spot")) {
                request = new SoapObject(NAMESPACE, "" + SPOT_CHALLAN_PAYMENT_NEW_15);
                request.addProperty("typeOfVeh", "" + typeOfVeh);
            } else if (Dashboard.check_vhleHistory_or_Spot.equals("towing")) {
                request = new SoapObject(NAMESPACE, "" + TOWING_CPACT_METHOD_NAME);
                request.addProperty("typeOfVeh", "" + typeOfVeh);
            }
            request.addProperty(utils.VHLE_HIST_SELECTED_PEN_CHALLANS, "" + selectedPendingChallans);
            request.addProperty(utils.SPOT_REGNCD, "" + regnCd);
            request.addProperty(utils.SPOT_VHLE_NO, "" + vehicleNo);
            request.addProperty(utils.SPOT_REG_NO, "" + regnNo);
            request.addProperty(utils.SPOT_GTWY_CD, "" + gtwyCd);
            request.addProperty(utils.SPOT_DT_OF_PAY, "" + dOfPay);
            request.addProperty(utils.SPOT_PYMNT_TIME, "" + pmtTime);
            request.addProperty(utils.SPOT_UNIT_CODE, "" + unitCode);
            request.addProperty(utils.SPOT_UNIT_NAME, "" + unitName);
            request.addProperty(utils.SPOT_PS_CODE, "" + psCode);
            request.addProperty(utils.SPOT_PS_NAME, "" + psName);
            request.addProperty(utils.SPOT_BOOKED_PS_CODE, "" + bookedPsCd);
            request.addProperty(utils.SPOT_BOOBKED_PS_NAME, "" + bookedPsName);
            request.addProperty(utils.SPOT_POINT_CODE, "" + pointCode);
            request.addProperty(utils.SPOT_POINT_NAME, "" + pointName);
            request.addProperty(utils.SPOT_OPERATOR_CODE, "" + operaterCd);
            request.addProperty(utils.SPOT_OPERATOR_NAME, "" + operaterName);
            request.addProperty(utils.SPOT_PID_CODE, "" + pidCd);
            request.addProperty(utils.SPOT_PID_NAME, "" + pidName);
            request.addProperty(utils.SPOT_CADRE_CODE, "" + cadreCD);
            request.addProperty(utils.SPOT_CADRE, "" + cadre);
            request.addProperty(utils.SPOT_TOTAL_AMNT, "" + totalAmount);
            request.addProperty(utils.SPOT_DETAINED, "" + detained);
            request.addProperty(utils.SPOT_SIMID, "" + simId);
            request.addProperty(utils.SPOT_IMEI, "" + imeiNo);
            request.addProperty(utils.SPOT_MACID, "" + macId);
            request.addProperty(utils.SPOT_LAT, "" + latitude);
            request.addProperty(utils.SPOT_LONG, "" + longitude);
            request.addProperty(utils.SPOT_GPS_DATE, "" + gpsDt);
            request.addProperty(utils.SPOT_GPS_TIME, "" + gpsTime);
            request.addProperty(utils.SPOT_ONLINE_MODE, "" + onlinebuff);
            request.addProperty(utils.SPOT_MODULE_CODE, "" + moduleCd);
            request.addProperty(utils.SPOT_RELEASE_ITEM, "" + releasedItem);
            request.addProperty(utils.SPOT_CHALLAN_NO, "" + challanNo);
            request.addProperty(utils.SPOT_SERVICE_CODE, "" + serviceCode);
            request.addProperty(utils.SPOT_OWNER_LIC_NO, "" + ownerLicNo);
            request.addProperty(utils.SPOT_DRVR_LIC_NO, "" + drvierLicNo);
            request.addProperty(utils.SPOT_PWD, "" + password);
            request.addProperty(utils.SPOT_IMAGE, "" + imageEvidence);
            request.addProperty(utils.SPOT_CHALLAN_TYPE, "" + challanType);
            request.addProperty(utils.SPOT_CHALLAN_CODE, "" + challanCd);
            request.addProperty(utils.SPOT_LOCATION, "" + location);
            request.addProperty(utils.SPOT_REMARKS, "" + remarks);
            request.addProperty(utils.SPOT_PANCARD_NO, "" + pancardNo);
            request.addProperty(utils.SPOT_AADHAR_NO, "" + aadharNo);
            if (Dashboard.check_vhleHistory_or_Spot.equals("spot")) {
                request.addProperty(utils.SPOT_VOTERID, "" + voterId);

            } else if (Dashboard.check_vhleHistory_or_Spot.equals("towing")) {
                request.addProperty(utils.SPOT_VOTERID, "" + voterId);

            }
            request.addProperty(utils.SPOT_PASSPORT, "" + passport);
            request.addProperty(utils.SPOT_EMAIL, "" + email);
            request.addProperty(utils.SPOT_DRIVER_CNTCT_NO, "" + driverContactNo);
            request.addProperty(utils.SPOT_IS_IT_SPOT, "" + isItSpotPmt);
            request.addProperty(utils.SPOT_OFFENCE_DATE, "" + offenceDt);
            request.addProperty(utils.SPOT_OFFENCE_TIME, "" + offenceTime);
            request.addProperty(utils.SPOT_LIC_STATUS, "" + licStatus);

            request.addProperty(utils.SPOT_WHEELER_CODE, "" + wheelerCd);
            request.addProperty(utils.SPOT_VEH_CAT, "" + vehCategory);
            request.addProperty(utils.SPOT_VEH_MAIN_TYPE, "" + obMkrCD);
            request.addProperty(utils.SPOT_VEH_SUB, "" + vehicleMainType);
            request.addProperty(utils.SPOT_VIOLATIONS, "" + violations);
            request.addProperty(utils.SPOT_PENDING_CHALLANS, "" + pendingChallans);
            request.addProperty(utils.EXTRA_PASSENGERS, "" + noOfExtraPassengers);
            request.addProperty(utils.SPOT_DRIVERNAME, "" + driverName);
            request.addProperty(utils.SPOT_DRIVER_FNAME, "" + driverFatherName);
            request.addProperty(utils.SPOT_DRIVER_ADDRESS, "" + driverAddress);
            request.addProperty(utils.SPOT_DRIVER_CITY, "" + driverCity);
            // if (Dashboard.check_vhleHistory_or_Spot.equals("spot")) {
            request.addProperty(utils.TOWING_DOB, "" + dl_dob);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            try {
                HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
                httpTransportSE.call(SOAP_ACTION, envelope);
                SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
                //Object result = (Object) envelope.bodyIn;
                spot_final_res_status = "";
                spot_finalPrintNDevice = "";
                // String resPrint=new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                spot_final_res_status = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                /*spot_final_res_status=resPrint.split("\\*")[0];
                spot_finalPrintNDevice=resPrint.split("\\*")[1];*/

            } catch (SoapFault fault) {
                Log.d("SoapaultExp: ", "" + fault.getMessage());
                spot_final_res_status = "SF";
                final_spot_reponse_master = new String[0];
            } catch (SocketTimeoutException t) {
                t.printStackTrace();
                spot_final_res_status = "0";
                final_spot_reponse_master = new String[0];
                final_spot_reponse_violations_master = new String[0];
                final_spot_reponse_details = new String[0];
                final_spot_reponse_violations = new String[0][0];
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                spot_final_res_status = "0";
            }

            final_spot_reponse_master = new String[0];
            final_spot_reponse_violations_master = new String[0];
            final_spot_reponse_details = new String[0];
            final_spot_reponse_violations = new String[0][0];

            if (spot_final_res_status.toString().trim().equals("0")) {
                // if (final_spot_reponse_master.length == 0) {
                final_spot_reponse_master = new String[0];
                // }
                // if (final_spot_reponse_violations_master.length == 0) {
                final_spot_reponse_violations_master = new String[0];
                //}

                //if (final_spot_reponse_details.length == 0) {
                final_spot_reponse_details = new String[0];
                // }

                //if (final_spot_reponse_violations_master.length == 0) {
                final_spot_reponse_violations = new String[0][0];
                // }

            } else if (spot_final_res_status.toString() != null && !"0".equals(spot_final_res_status.toString()) && !"SF".equals(spot_final_res_status)) {

                final_spot_reponse_master = spot_final_res_status.split("\\^");

                for (int i = 0; i < final_spot_reponse_master.length; i++) {
                }
                if (final_spot_reponse_master[1].trim().length() > 0) {
                    final_spot_reponse_details = final_spot_reponse_master[1].split("!");

                    for (int i = 0; i < final_spot_reponse_details.length; i++) {

                    }
                }

                if (final_spot_reponse_master[2].trim().length() > 0) {
                    final_spot_reponse_violations_master = final_spot_reponse_master[2].split("!");
                    final_spot_reponse_violations = new String[final_spot_reponse_violations_master.length][3];
                    for (int i = 0; i < final_spot_reponse_violations_master.length; i++) {
                        final_spot_reponse_violations[i] = final_spot_reponse_violations_master[i].split("\\@");
                    }

                }

            }

        } catch (Exception e) {
            spot_final_res_status = "0";
            final_spot_reponse_master = new String[0];
            final_spot_reponse_violations_master = new String[0];
            final_spot_reponse_details = new String[0];
            final_spot_reponse_violations = new String[0][0];
        }
    }

    public static void mobileSpotChallanPaying(String spot_selected_penchallans, String regn_Cd, String vehicle_No,
                                               String regn_No, String gtwy_cd, String dateof_pymnt, String pymnt_time, String unit_cd, String unit_name,
                                               String ps_code, String ps_name, String booked_cd, String booked_name, String point_cd, String point_name,
                                               String operator_cd, String operator_name, String pid_cd, String pid_name, String cadre_cd, String cdre,
                                               String total_amnt, String detained_items, String simid, String imei_no, String mac_id, String lat_val,
                                               String long_val, String gps_date, String gps_time, String onlinemode, String module_code,
                                               String released_items, String challan_num, String service_code, String ownr_lic_no, String drvr_lic_no,
                                               String pwd, String image_data, String challan_type, String challan_code, String location, String remarks,
                                               String pan_num, String aadhar_num, String voter_id, String pasprt, String mailid, String drvr_cntct_num,
                                               String is_spot, String ofnce_date, String ofence_time, String lic_status, String whlr_cd, String veh_cat,
                                               String vhle_main_cat, String vhle_sub_cat, String vilatns, String pnding_challans, String epeople,
                                               String dname, String dfname, String daddress, String dcity) {

        Utils utils = new Utils();
        try {
            SoapObject request = null;
            if (Dashboard.check_vhleHistory_or_Spot.equals("spot")) {
                request = new SoapObject(NAMESPACE, "" + MOBILE_SPOT_CHALLAN_METHOD_NAME);
            } else if (Dashboard.check_vhleHistory_or_Spot.equals("towing")) {
                request = new SoapObject(NAMESPACE, "" + TOWING_CPACT_METHOD_NAME);
            }

            request.addProperty(utils.VHLE_HIST_SELECTED_PEN_CHALLANS, "" + spot_selected_penchallans);
            request.addProperty(utils.SPOT_REGNCD, "" + regn_Cd);
            request.addProperty(utils.SPOT_VHLE_NO, "" + vehicle_No);
            request.addProperty(utils.SPOT_REG_NO, "" + regn_No);
            request.addProperty(utils.SPOT_GTWY_CD, "" + gtwy_cd);
            request.addProperty(utils.SPOT_DT_OF_PAY, "" + dateof_pymnt);
            request.addProperty(utils.SPOT_PYMNT_TIME, "" + pymnt_time);
            request.addProperty(utils.SPOT_UNIT_CODE, "" + unit_cd);
            request.addProperty(utils.SPOT_UNIT_NAME, "" + unit_name);
            request.addProperty(utils.SPOT_PS_CODE, "" + ps_code);
            request.addProperty(utils.SPOT_PS_NAME, "" + ps_name);
            request.addProperty(utils.SPOT_BOOKED_PS_CODE, "" + booked_cd);
            request.addProperty(utils.SPOT_BOOBKED_PS_NAME, "" + booked_name);
            request.addProperty(utils.SPOT_POINT_CODE, "" + point_cd);
            request.addProperty(utils.SPOT_POINT_NAME, "" + point_name);
            request.addProperty(utils.SPOT_OPERATOR_CODE, "" + operator_cd);
            request.addProperty(utils.SPOT_OPERATOR_NAME, "" + operator_name);
            request.addProperty(utils.SPOT_PID_CODE, "" + pid_cd);
            request.addProperty(utils.SPOT_PID_NAME, "" + pid_name);
            request.addProperty(utils.SPOT_CADRE_CODE, "" + cadre_cd);
            request.addProperty(utils.SPOT_CADRE, "" + cdre);
            request.addProperty(utils.SPOT_TOTAL_AMNT, "" + total_amnt);
            request.addProperty(utils.SPOT_DETAINED, "" + detained_items);
            request.addProperty(utils.SPOT_SIMID, "" + simid);
            request.addProperty(utils.SPOT_IMEI, "" + imei_no);
            request.addProperty(utils.SPOT_MACID, "" + mac_id);
            request.addProperty(utils.SPOT_LAT, "" + lat_val);
            request.addProperty(utils.SPOT_LONG, "" + long_val);
            request.addProperty(utils.SPOT_GPS_DATE, "" + gps_date);
            request.addProperty(utils.SPOT_ONLINE_MODE, "" + onlinemode);
            request.addProperty(utils.SPOT_MODULE_CODE, "" + module_code);
            request.addProperty(utils.SPOT_RELEASE_ITEM, "" + released_items);
            request.addProperty(utils.SPOT_CHALLAN_NO, "" + challan_num);
            request.addProperty(utils.SPOT_SERVICE_CODE, "" + service_code);
            request.addProperty(utils.SPOT_OWNER_LIC_NO, "" + ownr_lic_no);
            request.addProperty(utils.SPOT_DRVR_LIC_NO, "" + drvr_lic_no);
            request.addProperty(utils.SPOT_PWD, "" + pwd);
            request.addProperty(utils.SPOT_IMAGE, "" + image_data);
            request.addProperty(utils.SPOT_CHALLAN_TYPE, "" + challan_type);
            request.addProperty(utils.SPOT_CHALLAN_CODE, "" + challan_code);
            request.addProperty(utils.SPOT_LOCATION, "" + location);
            request.addProperty(utils.SPOT_REMARKS, "" + remarks);
            request.addProperty(utils.SPOT_PANCARD_NO, "" + pan_num);
            request.addProperty(utils.SPOT_AADHAR_NO, "" + aadhar_num);
            request.addProperty(utils.SPOT_VOTERID, "" + voter_id);
            request.addProperty(utils.SPOT_PASSPORT, "" + pasprt);
            request.addProperty(utils.SPOT_EMAIL, "" + mailid);
            request.addProperty(utils.SPOT_DRIVER_CNTCT_NO, "" + drvr_cntct_num);
            request.addProperty(utils.SPOT_IS_IT_SPOT, "" + is_spot);
            request.addProperty(utils.SPOT_OFFENCE_DATE, "" + ofnce_date);
            request.addProperty(utils.SPOT_OFFENCE_TIME, "" + ofence_time);
            request.addProperty(utils.SPOT_LIC_STATUS, "" + lic_status);
            request.addProperty(utils.SPOT_WHEELER_CODE, "" + whlr_cd);
            request.addProperty(utils.SPOT_VEH_CAT, "" + veh_cat);
            request.addProperty(utils.SPOT_VEH_MAIN_TYPE, "" + vhle_main_cat);
            request.addProperty(utils.SPOT_VEH_SUB, "" + vhle_sub_cat);
            request.addProperty(utils.SPOT_VIOLATIONS, "" + vilatns);
            request.addProperty(utils.SPOT_PENDING_CHALLANS, "" + pnding_challans);
            request.addProperty(utils.EXTRA_PASSENGERS, "" + epeople);
            request.addProperty(utils.SPOT_DRIVERNAME, "" + dname);
            request.addProperty(utils.SPOT_DRIVER_FNAME, "" + dfname);
            request.addProperty(utils.SPOT_DRIVER_ADDRESS, "" + daddress);
            request.addProperty(utils.SPOT_DRIVER_CITY, "" + dcity);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            try {
                spot_final_res_status = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            final_spot_reponse_master = new String[0];
            final_spot_reponse_violations_master = new String[0];

            final_spot_reponse_details = new String[0];
            final_spot_reponse_violations = new String[0][0];

            if (spot_final_res_status.toString().trim().equals("0")) {
                if (final_spot_reponse_master.length == 0) {
                    final_spot_reponse_master = new String[0];
                }

                if (final_spot_reponse_violations_master.length == 0) {
                    final_spot_reponse_violations_master = new String[0];
                }

                if (final_spot_reponse_details.length == 0) {
                    final_spot_reponse_details = new String[0];
                }

                if (final_spot_reponse_violations_master.length == 0) {
                    final_spot_reponse_violations = new String[0][0];
                }

            } else {

                final_spot_reponse_master = spot_final_res_status.split("\\^");

                if (final_spot_reponse_master[1].toString().trim().length() > 0) {
                    final_spot_reponse_details = final_spot_reponse_master[1].split("!");
                }


                if (final_spot_reponse_master[2].toString().trim().length() > 0) {

                    final_spot_reponse_violations_master = final_spot_reponse_master[2].split("!");
                    final_spot_reponse_violations = new String[final_spot_reponse_violations_master.length][3];

                    for (int i = 0; i < final_spot_reponse_violations_master.length; i++) {
                        final_spot_reponse_violations[i] = final_spot_reponse_violations_master[i].split("\\@");
                    }

                }

            }

        } catch (SoapFault fault) {

        } catch (Exception e) {
            // TODO: handle exception

            if (final_spot_reponse_master.length == 0) {
                final_spot_reponse_master = new String[0];
            }

            if (final_spot_reponse_violations_master.length == 0) {
                final_spot_reponse_violations_master = new String[0];
            }

            if (final_spot_reponse_details.length == 0) {
                final_spot_reponse_details = new String[0];
            }

            if (final_spot_reponse_violations_master.length == 0) {
                final_spot_reponse_violations = new String[0][0];
            }

        }
    }

    public static void vehicleHistory(String vhle_hist_penchallans, String pen_challans, String regn_No, String gtwy_cd,
                                      String unit_cd, String ps_code, String ps_name, String pid_cd, String pid_name, String total_amnt,
                                      String detained_items, String dateof_pymnt, String pymnt_time, String simid, String imei_no, String lat_val,
                                      String long_val, String point_cd, String point_name, String onlinemode, String module_code,
                                      String released_detValues, String challan_num, String service_code, String ownr_lic_no, String drvr_lic_no,
                                      String pwd, String image_data, String challan_type, String challan_code, String location, String remarks,
                                      String pan_num, String aadhar_num, String voter_id, String pasprt, String mailid, String drvr_cntct_num,
                                      String is_spot, String cadre_cd, String cdre) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "mobilePendingChallanPayment");
            request.addProperty("selectedPendingChallans",
                    "" + vhle_hist_penchallans != null && vhle_hist_penchallans.length() > 0 ? vhle_hist_penchallans
                            : "");
            request.addProperty("pendingChallans",
                    "" + pen_challans != null && pen_challans.length() > 0 ? pen_challans : "");
            request.addProperty("regnNo", "" + regn_No != null && regn_No.length() > 0 ? regn_No : "");
            request.addProperty("gtwyCd", "" + gtwy_cd != null && gtwy_cd.length() > 0 ? gtwy_cd : "");
            request.addProperty("unitCode", "" + unit_cd != null && unit_cd.length() > 0 ? unit_cd : "");
            request.addProperty("psCode", "" + ps_code != null && ps_code.length() > 0 ? ps_code : "");
            request.addProperty("psName", "" + ps_name != null && ps_name.length() > 0 ? ps_name : "");
            request.addProperty("pidCd", "" + pid_cd != null && pid_cd.length() > 0 ? pid_cd : "");
            request.addProperty("pidName", "" + pid_name != null && pid_name.length() > 0 ? pid_name : "");
            request.addProperty("totalAmount", "" + total_amnt != null && total_amnt.length() > 0 ? total_amnt : "");
            request.addProperty("detained",
                    "" + detained_items != null && detained_items.length() > 0 ? detained_items : "");
            request.addProperty("dOfPay", "" + dateof_pymnt != null && dateof_pymnt.length() > 0 ? dateof_pymnt : "");
            request.addProperty("pmtTime", "" + pymnt_time != null && pymnt_time.length() > 0 ? pymnt_time : "");
            request.addProperty("simId", "" + simid);
            request.addProperty("imeiNo", "" + imei_no);
            request.addProperty("latitude", "" + lat_val);
            request.addProperty("longitude", "" + long_val);
            request.addProperty("pointCode", "" + point_cd != null && point_cd.length() > 0 ? point_cd : "");
            request.addProperty("pointName", "" + point_name != null && point_name.length() > 0 ? point_name : "");
            request.addProperty("onlineMode", "" + onlinemode != null && onlinemode.length() > 0 ? onlinemode : "");
            request.addProperty("moduleCd", "" + module_code != null && module_code.length() > 0 ? module_code : "");
            request.addProperty("releasedDetValues",
                    "" + released_detValues != null && released_detValues.length() > 0 ? released_detValues : "");
            request.addProperty("challanNo", "" + challan_num != null && challan_num.length() > 0 ? challan_num : "");
            request.addProperty("serviceCode",
                    "" + service_code != null && service_code.length() > 0 ? service_code : "");
            request.addProperty("ownerLicNo", "" + ownr_lic_no != null && ownr_lic_no.length() > 0 ? ownr_lic_no : "");
            request.addProperty("driverLicNo", "" + drvr_lic_no != null && drvr_lic_no.length() > 0 ? drvr_lic_no : "");
            request.addProperty("password", "" + pwd != null && pwd.length() > 0 ? pwd : "");
            request.addProperty("imageEvidence", "" + image_data != null && image_data.length() > 0 ? image_data : "");
            request.addProperty("challanType",
                    "" + challan_type != null && challan_type.length() > 0 ? challan_type : "");
            request.addProperty("challanCd",
                    "" + challan_code != null && challan_code.length() > 0 ? challan_code : "");
            request.addProperty("location", "" + location != null && location.length() > 0 ? location : "");
            request.addProperty("remarks", "" + remarks != null && remarks.length() > 0 ? remarks : "");
            request.addProperty("pancardNo", "" + pan_num != null && pan_num.length() > 0 ? pan_num : "");
            request.addProperty("aadharNo", "" + aadhar_num != null && aadhar_num.length() > 0 ? aadhar_num : "");
            request.addProperty("voterId", "" + voter_id != null && voter_id.length() > 0 ? voter_id : "");
            request.addProperty("passport", "" + pasprt != null && pasprt.length() > 0 ? pasprt : "");
            request.addProperty("email", "" + mailid != null && mailid.length() > 0 ? mailid : "");
            request.addProperty("driverContactNo", "" + drvr_cntct_num);
            request.addProperty("isItSpotPmt", "" + is_spot);
            request.addProperty("cadreCd", "" + cadre_cd);
            request.addProperty("cadre", "" + cdre);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);

            Object result = envelope.getResponse();

            try {

                if (result != null) {

                    spot_final_res_status = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());

                    if (spot_final_res_status != null && spot_final_res_status != "0") {

                        final_spot_reponse_master = spot_final_res_status.split("\\^");
                        if (final_spot_reponse_master[1].toString().trim().length() > 0) {
                            final_spot_reponse_details = final_spot_reponse_master[1].split("!");
                        }

                        if ((!final_spot_reponse_master[2].toString().trim().equals("NA"))
                                && (final_spot_reponse_master[2].toString().trim().length() > 0)) {

                            final_spot_reponse_violations_master = final_spot_reponse_master[2].split("!");

                            final_spot_reponse_violations = new String[final_spot_reponse_violations_master.length][3];

                            for (int i = 0; i < final_spot_reponse_violations_master.length; i++) {
                                final_spot_reponse_violations[i] = final_spot_reponse_violations_master[i].split("\\@");

                            }

                        }
                    }
                } else {

                    spot_final_res_status = "0";
                    final_spot_reponse_master = new String[0];
                    final_spot_reponse_violations_master = new String[0];
                    final_spot_reponse_details = new String[0];
                    final_spot_reponse_violations = new String[0][0];
                    selected_paid_challans_master = new String[0];
                    selected_paid_challans_details = new String[0][0];

                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                spot_final_res_status = "0";
                final_spot_reponse_master = new String[0];
                final_spot_reponse_violations_master = new String[0];
                final_spot_reponse_details = new String[0];
                final_spot_reponse_violations = new String[0][0];
                selected_paid_challans_master = new String[0];
                selected_paid_challans_details = new String[0][0];

                e.printStackTrace();
            }


            if (Dashboard.check_vhleHistory_or_Spot.equals("vehiclehistory")) {
                if (final_spot_reponse_master[3].toString() != null && !final_spot_reponse_master[3].toString().trim().equals("NA")
                        && (final_spot_reponse_master[3].toString().trim().length() > 0)) {

                    selected_paid_challans_master = final_spot_reponse_master[3].split("!");

                    selected_paid_challans_details = new String[selected_paid_challans_master.length][2];

                    for (int j = 0; j < selected_paid_challans_master.length; j++) {
                        selected_paid_challans_details[j] = selected_paid_challans_master[j].split("\\@");

                    }
                }
            }

        } catch (SoapFault fault) {
            spot_final_res_status = "0";
            final_spot_reponse_master = new String[0];
            final_spot_reponse_violations_master = new String[0];
            final_spot_reponse_details = new String[0];
            final_spot_reponse_violations = new String[0][0];
            selected_paid_challans_master = new String[0];
            selected_paid_challans_details = new String[0][0];

            fault.printStackTrace();

        } catch (SocketTimeoutException fault) {
            spot_final_res_status = "0";
            final_spot_reponse_master = new String[0];
            final_spot_reponse_violations_master = new String[0];
            final_spot_reponse_details = new String[0];
            final_spot_reponse_violations = new String[0][0];
            selected_paid_challans_master = new String[0];
            selected_paid_challans_details = new String[0][0];

            fault.printStackTrace();

        } catch (Exception E) {

            spot_final_res_status = "0";
            final_spot_reponse_master = new String[0];
            final_spot_reponse_violations_master = new String[0];
            final_spot_reponse_details = new String[0];
            final_spot_reponse_violations = new String[0][0];
            selected_paid_challans_master = new String[0];
            selected_paid_challans_details = new String[0][0];

            E.printStackTrace();
        }
    }

    public static String insertDetainItems(String eticketNo, String challanNo, String regnNo, String offenceDate,
                                           String detainItems, String detainDate, String detainTime, String detainPid,
                                           String detainPidName, String stateCd, String unitCd, String unitName, String chasisNo,
                                           String engineNo, String fakeStatus, String drivingLicense, String aadhaarNO,
                                           String psName, String pointName, String onlineMode) {


        try {

            if (!(onlinebuff == null)) {
                onlinebuff = new StringBuffer("");
                onlinebuff.delete(0, onlinebuff.length());
                onlinebuff.setLength(0); // v
            }
            if ((!drivingLicense.equals("") && !"NA".equals(drivingLicense)) && (!aadhaarNO.equals("") && !"NA".equals(aadhaarNO))) {


                if (license_data != "null" && !license_data.equals("0") && aadhaarNO != "null" && !aadhaarNO.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + license_data + "^" + "3" + "|" + aadhar_data);
                } else if (license_data.equals("0") && !aadhar_data.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + drivingLicense + "^" + "3" + "|" + aadhar_data);

                } else if (!license_data.equals("0") && aadhar_data.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + license_data + "^" + "3" + "|" + aadhaarNO);

                } else if (license_data.equals("0") && aadhar_data.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + drivingLicense + "^" + "3" + "|" + aadhaarNO);

                }

            } else if ((!drivingLicense.equals("") && !"NA".equals(drivingLicense))
                    && (aadhaarNO.equals("") || "NA".equals(aadhaarNO))) {

                if (license_data != "null" && !license_data.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + license_data + "^" + "3" + "|" + "NA");
                } else {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + drivingLicense + "^" + "3" + "|" + "NA");

                }
            } else if ((drivingLicense.equals("") || "NA".equals(drivingLicense))
                    && (!aadhaarNO.equals("") && !"NA".equals(aadhaarNO))) {

                if (aadhar_data != "null" && !aadhar_data.equals("0")) {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + "NA" + "^" + "3" + "|" + aadhar_data);
                } else {
                    onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + "NA" + "^" + "3" + "|" + aadhaarNO);

                }
            } else if ((drivingLicense.equals(""))
                    && (aadhaarNO.equals(""))) {

                onlinebuff = onlinebuff.append(rc_send + "^" + "2" + "|" + "NA" + "^" + "3" + "|" + "NA");

            }

            Log.d("Online Buf", onlinebuff.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            SoapObject request = new SoapObject(NAMESPACE, "insertDetaineItems");
            request.addProperty("eticketNo", eticketNo);
            request.addProperty("challanNo", challanNo);
            request.addProperty("regnNo", regnNo);
            request.addProperty("offenceDate", offenceDate);
            request.addProperty("detainItems", detainItems);
            request.addProperty("detainDate", detainDate);
            request.addProperty("detainTime", detainTime);
            request.addProperty("detainPid", detainPid);
            request.addProperty("detainPidName", detainPidName);
            request.addProperty("stateCd", stateCd);
            request.addProperty("unitCd", unitCd);
            request.addProperty("unitName", unitName);
            request.addProperty("chasisNo", chasisNo);
            request.addProperty("engineNo", engineNo);
            request.addProperty("fakeStatus", fakeStatus);
            request.addProperty("drivingLicense", drivingLicense);
            request.addProperty("aadhaarNO", aadhaarNO);
            request.addProperty("psName", psName);
            request.addProperty("pointName", pointName);
            request.addProperty("onlineMode", onlinebuff.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                insertDetainItemsresponse = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                insertDetainItemsresponse = "2";
                e.printStackTrace();
            }


        } catch (SoapFault fault) {
            insertDetainItemsresponse = "2";
            fault.printStackTrace();
        } catch (Exception E) {
            E.printStackTrace();
            insertDetainItemsresponse = "2";
        }

        return insertDetainItemsresponse;
    }

    public static String validateRegno(String regnNo, String drivingLicense, String aadhaarNO, String chasisNo, String engineNo, String pidCd, String unitCode,
                                       String imei, String simNo, String gpsLattitude, String gpsLongitude, String pidName) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "validateRegnNo");
            request.addProperty("regnNo", regnNo);
            request.addProperty("drivingLicense", drivingLicense);
            request.addProperty("aadhaarNO", aadhaarNO);
            request.addProperty("chasisNo", chasisNo);
            request.addProperty("engineNo", engineNo);
            request.addProperty("pidCd", pidCd);
            request.addProperty("unitCode", unitCode);
            request.addProperty("imei", imei);
            request.addProperty("simNo", simNo);
            request.addProperty("gpsLattitude", gpsLattitude);
            request.addProperty("gpsLongitude", gpsLongitude);
            request.addProperty("pidName", pidName);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    validregnoresponse = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                    Log.d("hgdgeuihuijy", "" + validregnoresponse);
                } else {
                    validregnoresponse = "2";
                }
            } catch (Exception e) {
                e.printStackTrace();
                validregnoresponse = "2";
            }
        } catch (SoapFault fault) {
            validregnoresponse = "2";
            fault.printStackTrace();
        } catch (Exception E) {
            E.printStackTrace();
            validregnoresponse = "2";
        }
        return validregnoresponse;
    }

    public static String getRemarks(String regn_no, String licenceNo, String aadharNo) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getRemarks");
            request.addProperty("regn_no", regn_no);
            request.addProperty("licenceNo", licenceNo);
            request.addProperty("aadharNo", aadharNo);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    remarksresult = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                } else {
                    remarksresult = "2";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                remarksresult = "2";
            }

        } catch (SoapFault fault) {
            remarksresult = "2";
            fault.printStackTrace();
        } catch (SocketTimeoutException fault) {
            remarksresult = "2";
            fault.printStackTrace();
        } catch (Exception E) {
            E.printStackTrace();
            remarksresult = "2";
        }

        return remarksresult;
    }

    public static void releaserDocumentsModule(String vhle_hist_penchallans, String pen_challans, String regn_No,
                                               String gtwy_cd, String unit_cd, String ps_code, String ps_name, String pid_cd, String pid_name,
                                               String total_amnt, String detained_items, String dateof_pymnt, String pymnt_time, String simid,
                                               String imei_no, String lat_val, String long_val, String point_cd, String point_name, String onlinemode,
                                               String module_code, String released_detValues, String challan_num, String service_code, String ownr_lic_no,
                                               String drvr_lic_no, String pwd, String image_data, String challan_type, String challan_code,
                                               String location, String remarks, String pan_num, String aadhar_num, String dl_dob, String pasprt,
                                               String mailid, String drvr_cntct_num, String is_spot, String cadre_cd, String cdre, String passport,
                                               String engNo, String chasisNo) {

        Utils utils = new Utils();
        try {
            SoapObject request = null;

            String passPort_No = SpotChallan.et_passport != null ? SpotChallan.et_passport.getText().toString().trim() : "";

            request = new SoapObject(NAMESPACE, "" + VEHICLE_RELEASE_METHOD_NAME);
            request.addProperty(utils.VHLE_HIST_SELECTED_PEN_CHALLANS, "" + vhle_hist_penchallans);
            request.addProperty(utils.SPOT_PENDING_CHALLANS, "" + pen_challans);
            request.addProperty(utils.SPOT_REG_NO, "" + regn_No);
            request.addProperty(utils.SPOT_DLDETAIN_FLG, "" + gtwy_cd);
            request.addProperty(utils.SPOT_UNIT_CODE, "" + unit_cd);
            request.addProperty(utils.SPOT_PS_CODE, "" + ps_code);
            request.addProperty(utils.SPOT_PS_NAME, "" + ps_name);
            request.addProperty(utils.SPOT_PID_CODE, "" + pid_cd);
            request.addProperty(utils.SPOT_PID_NAME, "" + pid_name);
            request.addProperty(utils.SPOT_TOTAL_AMNT, "" + total_amnt);
            request.addProperty(utils.SPOT_DETAINED, "" + detained_items);
            request.addProperty(utils.SPOT_DT_OF_PAY, "" + dateof_pymnt);
            request.addProperty(utils.SPOT_PYMNT_TIME, "" + pymnt_time);
            request.addProperty(utils.SPOT_SIMID, "" + simid);
            request.addProperty(utils.SPOT_IMEI, "" + imei_no);
            request.addProperty(utils.SPOT_LAT, "" + lat_val);
            request.addProperty(utils.SPOT_LONG, "" + long_val);
            request.addProperty(utils.SPOT_POINT_CODE, "" + point_cd);
            request.addProperty(utils.SPOT_POINT_NAME, "" + point_name);
            request.addProperty(utils.SPOT_ONLINE_MODE, "" + onlinemode);
            request.addProperty(utils.SPOT_MODULE_CODE, "" + module_code);
            request.addProperty(utils.VHLE_RELEASED_DET_VALUES, "" + released_detValues);
            request.addProperty(utils.SPOT_CHALLAN_NO, "" + challan_num);
            request.addProperty(utils.SPOT_SERVICE_CODE, "" + service_code);
            request.addProperty(utils.SPOT_OWNER_LIC_NO, "" + ownr_lic_no);
            request.addProperty(utils.VHLE_DRVR_LIC_NO, "" + drvr_lic_no);
            request.addProperty(utils.SPOT_PWD, "" + pwd);
            request.addProperty(utils.SPOT_IMAGE, "" + image_data);
            request.addProperty(utils.SPOT_CHALLAN_TYPE, "" + challan_type);
            request.addProperty(utils.SPOT_CHALLAN_CODE, "" + challan_code);
            request.addProperty(utils.SPOT_LOCATION, "" + location);
            request.addProperty(utils.SPOT_REMARKS, "" + remarks);
            request.addProperty(utils.SPOT_PANCARD_NO, "" + pan_num);
            request.addProperty(utils.SPOT_AADHAR_NO, "" + aadhar_num);
            request.addProperty(utils.TOWING_DOB, "" + dl_dob);

            Log.i("RELEASE DOCUMENTS", "" + dl_dob);

            request.addProperty(utils.SPOT_PASSPORT, "" + pasprt);
            request.addProperty(utils.SPOT_EMAIL, "" + mailid);
            request.addProperty(utils.SPOT_DRIVER_CNTCT_NO, "" + drvr_cntct_num);
            request.addProperty(utils.SPOT_IS_IT_SPOT, "" + is_spot);
            request.addProperty(utils.VHLE_CADRE_CODE, "" + cadre_cd);
            request.addProperty(utils.SPOT_CADRE, "" + cdre);

            request.addProperty(utils.SPOT_PASSPORT_NO, "" + passPort_No);
            request.addProperty(utils.SPOT_ENGINE, "" + engNo);
            request.addProperty(utils.SPOT_CHASIS, "" + chasisNo);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    spot_final_res_status = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());

                    if (spot_final_res_status != null && spot_final_res_status != "0") {

                        // final_spot_reponse_master = new String[0];
                        final_spot_reponse_violations_master = new String[0];
                        final_spot_reponse_details = new String[0];
                        selected_paid_challans_master = new String[0];
                        selected_paid_challans_details = new String[0][0];


                        final_spot_reponse_master = spot_final_res_status.split("\\^");

                        if (final_spot_reponse_master != null && final_spot_reponse_master[1].toString().trim().length() > 0) {
                            final_spot_reponse_details = final_spot_reponse_master[1].split("!");
                        }
                        if ((final_spot_reponse_master != null && final_spot_reponse_master[2] != null)
                                && (!final_spot_reponse_master[2].toString().trim().equals("NA"))
                                && (final_spot_reponse_master[2].toString().trim().length() > 0)) {

                            final_spot_reponse_violations_master = final_spot_reponse_master[2].split("!");

                            if (final_spot_reponse_violations_master != null) {
                                final_spot_reponse_violations = new String[final_spot_reponse_violations_master.length][3];

                                for (int i = 0; i < final_spot_reponse_violations_master.length; i++) {
                                    final_spot_reponse_violations[i] = final_spot_reponse_violations_master[i].split("\\@");
                                }
                            }
                        }

                        if (Dashboard.check_vhleHistory_or_Spot.equals("vehiclehistory")) {
                            if ((final_spot_reponse_master[3] != null)
                                    && (!final_spot_reponse_master[3].toString().trim().equals("NA"))
                                    && (final_spot_reponse_master[3].toString().trim().length() > 0)) {
                                selected_paid_challans_master = final_spot_reponse_master[3].split("!");

                                selected_paid_challans_details = new String[selected_paid_challans_master.length][2];
                                for (int j = 0; j < selected_paid_challans_master.length; j++) {
                                    selected_paid_challans_details[j] = selected_paid_challans_master[j].split("\\@");
                                }
                            }
                        }
                    }

                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                spot_final_res_status = "0";
                final_spot_reponse_master = new String[0];
                final_spot_reponse_violations_master = new String[0];
                final_spot_reponse_details = new String[0];
                final_spot_reponse_violations = new String[0][0];
                selected_paid_challans_details = new String[0][0];
            }


        } catch (SoapFault fault) {

            spot_final_res_status = "0";
            final_spot_reponse_master = new String[0];
            final_spot_reponse_violations_master = new String[0];
            final_spot_reponse_details = new String[0];
            final_spot_reponse_violations = new String[0][0];
            selected_paid_challans_details = new String[0][0];
            fault.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception

            spot_final_res_status = "0";
            final_spot_reponse_master = new String[0];
            final_spot_reponse_violations_master = new String[0];
            final_spot_reponse_details = new String[0];
            final_spot_reponse_violations = new String[0][0];
            selected_paid_challans_details = new String[0][0];
            e.printStackTrace();
        }
    }

    public static void getDayReports(String unitcode, String pidcd, String pidname, String reportdate, String simid,
                                     String imeino, String reprt_id) {
        Utils utils = new Utils();
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + DAY_REPORTS_METHOD_NAME);
            request.addProperty("" + utils.SPOT_UNIT_CODE, "" + unitcode);
            request.addProperty("" + utils.SPOT_PID_CODE, "" + pidcd);
            request.addProperty("" + utils.SPOT_PID_NAME, "" + pidname);
            request.addProperty("" + utils.REPORT_DATE, "" + reportdate);
            request.addProperty("" + utils.SIM_ID, "" + simid);
            request.addProperty("" + utils.SPOT_IMEI, "" + imeino);
            request.addProperty("" + utils.REPORT_ID, "" + reprt_id);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            Opdata_Chalana = "";
            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (SoapFault fault) {

        } catch (Exception e) {
            // TODO: handle exception
            Opdata_Chalana = "NA";
        }
    }

    public static void getOnlineDuplicatePrint(String unitcode, String pidcd, String pidname, String regno,
                                               String reportdate, String simid, String imeino) {
        Utils utils = new Utils();
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + DUPLCIATE_ONLINE_PRINT_METHOD_NAME);
            request.addProperty("" + utils.SPOT_UNIT_CODE, "" + unitcode);
            request.addProperty("" + utils.SPOT_PID_CODE, "" + pidcd);
            request.addProperty("" + utils.SPOT_PID_NAME, "" + pidname);
            request.addProperty("" + utils.REG_NO, "" + regno);
            request.addProperty("" + utils.REPORT_DATE, "" + reportdate);
            request.addProperty("" + utils.SIM_ID, "" + simid);
            request.addProperty("" + utils.SPOT_IMEI, "" + imeino);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            Opdata_Chalana = "";

            try {
                Opdata_Chalana = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (SoapFault fault) {

        } catch (Exception e) {
            // TODO: handle exception
            Opdata_Chalana = "NA";
        }
    }

    public static void getSameChlnDuplicatePrint(String unitcode, String pidcd, String pidname, String regno,
                                                 String reportdate, String simid, String imeino) {
        Utils utils = new Utils();
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + DUPLCIATE_ONLINE_PRINT_METHOD_NAME);
            request.addProperty("" + utils.SPOT_UNIT_CODE, "" + unitcode);
            request.addProperty("" + utils.SPOT_PID_CODE, "" + pidcd);
            request.addProperty("" + utils.SPOT_PID_NAME, "" + pidname);
            request.addProperty("" + utils.REG_NO, "" + regno);
            request.addProperty("" + utils.REPORT_DATE, "" + reportdate);
            request.addProperty("" + utils.SIM_ID, "" + simid);
            request.addProperty("" + utils.SPOT_IMEI, "" + imeino);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            str_SameChlnDuplicatePrint = "";

            try {
                str_SameChlnDuplicatePrint = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                str_SameChlnDuplicatePrint = "NA";
            }

        } catch (SoapFault fault) {
            str_SameChlnDuplicatePrint = "NA";
        } catch (Exception e) {
            // TODO: handle exception
            str_SameChlnDuplicatePrint = "NA";
        }
    }

    public static void getTransactionNo(String all_challans, String imei_No, String email_ID, String contact_No,
                                        String ter_ID, String bt_ID, String bt_Name, String sim_no, String gps_Latti, String gps_Longi,
                                        String gps_Date) {
        try {

            SoapObject request = new SoapObject(NAMESPACE, "" + GET_TRANSACTION_NO);
            request.addProperty("challans", "" + all_challans);
            request.addProperty("imei", "" + imei_No);
            request.addProperty("email", "" + email_ID);
            request.addProperty("contactNo", "" + contact_No);
            request.addProperty("terID", "" + ter_ID);
            request.addProperty("btID", "" + bt_ID);
            request.addProperty("btName", "" + bt_Name);
            request.addProperty("simNo", "" + sim_no);
            request.addProperty("gpsLatti", "" + gps_Latti);
            request.addProperty("gpsLongi", "" + gps_Longi);
            request.addProperty("gpsDate", "" + gps_Date);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                transactionNo_resp = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (transactionNo_resp == "0") {

            } else {
            }

        } catch (SoapFault fault) {

        } catch (Exception E) {
            E.printStackTrace();
            transactionNo_resp = "0";
        }
    }

    public static void profileUpdate(String pidCd, String dOB, String gENDER, String bLOOD_GRP,
                                        String pRESENT_ADDR, String pERMANENT_ADDR, String dOJ, String sim_no, String gps_Latti, String gps_Longi,
                                        String gps_Date) {
        try {

            SoapObject request = new SoapObject(NAMESPACE, "profileUpdate"); //GET_TRANSACTION_NO
            request.addProperty("pidCd", "" + pidCd);
            request.addProperty("dOB", "" + dOB);
            request.addProperty("gENDER", "" + gENDER);
            request.addProperty("bLOOD_GRP", "" + bLOOD_GRP);
            request.addProperty("pRESENT_ADDR", "" + pRESENT_ADDR);
            request.addProperty("pERMANENT_ADDR", "" + pERMANENT_ADDR);
            request.addProperty("dOJ", "" + dOJ);
            request.addProperty("simNo", "" + sim_no);
            request.addProperty("gpsLatti", "" + gps_Latti);
            request.addProperty("gpsLongi", "" + gps_Longi); // Base64 Image
            request.addProperty("gpsDate", "" + gps_Date);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                profileUpdate = result.toString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                profileUpdate = "0";
            }


        } catch (SoapFault fault) {
            profileUpdate = "0";
        } catch (Exception E) {
            E.printStackTrace();
            profileUpdate = "0";
        }
    }

    public static void make_OnlinePayment10(String eTicket_Info, String pmt_TrId, String current_date,
                                            String current_time, String mercntResp_code, String mercntInvoice_no, String mercntRrn,
                                            String mercntAuth_code, String amt_collectedPid_cd, String amtCollected_pidName, String pmt_unitCd,
                                            String pmt_unitName, String noOf_PendingChallans, String pending_Amount, String card_Details,
                                            String pmnt_result, String merchent_id, String merchent_key, String terminal_id, String batchNo,
                                            String card_holder, String card_type) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + MAKE_ONLINE_PAYMENT);
            request.addProperty("eTicketInfo", "" + eTicket_Info);
            request.addProperty("pmtTrId", "" + pmt_TrId);
            request.addProperty("date", "" + current_date);
            request.addProperty("time", "" + current_time);
            request.addProperty("mercnt_resp_code", "" + mercntResp_code);
            request.addProperty("mercnt_invoice_no", "" + mercntInvoice_no);
            request.addProperty("mercnt_Rrn", "" + mercntRrn);
            request.addProperty("mercnt_auth_code", "" + mercntAuth_code);
            request.addProperty("amt_collected_pid_cd", "" + amt_collectedPid_cd);
            request.addProperty("amt_collected_pid_name", "" + amtCollected_pidName);
            request.addProperty("pmt_unit_cd", "" + pmt_unitCd);
            request.addProperty("pmt_unit_name", "" + pmt_unitName);
            request.addProperty("noOfPendingChallans", "" + noOf_PendingChallans);
            request.addProperty("pendingAmount", "" + pending_Amount);
            request.addProperty("cardDetails", "" + card_Details);
            request.addProperty("pmtResult", "" + pmnt_result);
            request.addProperty("merchantId", "" + merchent_id);
            request.addProperty("merchantKey", "" + merchent_key);

            request.addProperty("terminalId", "" + terminal_id);
            request.addProperty("batchNo", "" + batchNo);
            request.addProperty("cardHolderName", "" + card_holder);
            request.addProperty("cardType", "" + card_type);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            makePayment_resp = "";

            try {
                makePayment_resp = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (SoapFault fault) {

        } catch (Exception e) {
            // TODO: handle exception
            makePayment_resp = "0";
        }
    }

    public static void getTerminalID(String unitCode) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + GET_TERMINAL_DETAILS);
            request.addProperty("unitCode", "" + unitCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            terminaldetails_resp = "";

            try {
                if (result != null && result.toString() != null && result.toString().trim().length() > 10) {
                    terminaldetails_resp = new com.mtpv.mobilee_ticket_services.PidSecEncrypt()
                            .decrypt(result.toString().trim());
                } else {
                    terminaldetails_resp = "0";
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                terminaldetails_resp = "0";
                e.printStackTrace();
            }

        } catch (SoapFault fault) {
            terminaldetails_resp = "0";
        } catch (Exception e) {
            // TODO: handle exception
            terminaldetails_resp = "0";
        }
    }

    public static void getVehileDetails(String unit_Code, String regn_No, String eng_No, String chasis_No) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + GET_CHALLAN_DETAILS_FOR_AADHAAR);
            request.addProperty("unitCode", "" + unit_Code);
            request.addProperty("regnNo", "" + regn_No);
            request.addProperty("engNo", "" + eng_No);
            request.addProperty("chasisNo", "" + chasis_No);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            aadhaarVehicle_resp = "";

            try {
                aadhaarVehicle_resp = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (SoapFault fault) {
            aadhaarVehicle_resp = "0";
        } catch (SocketTimeoutException fault) {
            aadhaarVehicle_resp = "0";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            aadhaarVehicle_resp = "0";
        }
    }

    public static void getAadhaarUpdate(String unit_Code, String regn_No, String eng_No, String chasis_No,
                                        String aadhar_No, String detained_Items, String violations, String dl_No, String owner_Name,
                                        String driver_Name, String driver_Address, String driver_City, String driver_ContactNo, String challan_Type,
                                        String pid_Code, String pid_Name, String cadre, String unit_Name, String offence_Date, String offence_Time,
                                        String eticket_No, String bookedPs_Name, String point_Name, String drvier_LicNo) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + GET_AADHAAR_UPDATE);
            request.addProperty("unitCode", "" + unit_Code);
            request.addProperty("regnNo", "" + regn_No);
            request.addProperty("engNo", "" + eng_No);
            request.addProperty("chasisNo", "" + chasis_No);
            request.addProperty("aadharNo", "" + aadhar_No);
            request.addProperty("detainedItems", "" + detained_Items);
            request.addProperty("violations", "" + violations);
            request.addProperty("dlNo", "" + dl_No);
            request.addProperty("ownerName", "" + owner_Name);
            request.addProperty("driverName", "" + driver_Name);
            request.addProperty("driverAddress", "" + driver_Address);
            request.addProperty("driverCity", "" + driver_City);
            request.addProperty("driverContactNo", "" + driver_ContactNo);
            request.addProperty("challanType", "" + challan_Type);
            request.addProperty("pidCode", "" + pid_Code);
            request.addProperty("pidName", "" + pid_Name);
            request.addProperty("cadre", "" + cadre);
            request.addProperty("unitName", "" + unit_Name);
            request.addProperty("offenceDate", "" + offence_Date);
            request.addProperty("offenceTime", "" + offence_Time);
            request.addProperty("eticketNo", "" + eticket_No);
            request.addProperty("bookedPsName", "" + bookedPs_Name);
            request.addProperty("pointName", "" + point_Name);
            request.addProperty("drvierLicNo", "" + drvier_LicNo);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            UpdateAadhaar_resp = "";

            try {
                UpdateAadhaar_resp = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (SoapFault fault) {

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            UpdateAadhaar_resp = "0";
        }
    }

    public static void getChange_PWDotp(String pid_cd, String security_Code, String contact_No) {

        try {

            SoapObject request = new SoapObject(NAMESPACE, "getChangePWDotp");
            request.addProperty("pidcd", pid_cd);
            request.addProperty("securityCode", security_Code);
            request.addProperty("contactNo", contact_No);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            try {
                changePswd_otp = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                Settings_New.changepwdotpresp = changePswd_otp;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (SoapFault fault) {

        } catch (Exception E) {
            E.printStackTrace();
            changePswd_otp = "NA";

        }
    }

    public static void updateChange_PWD(String pid_cd, String security_Code, String contact_No, String otpflg,
                                        String new_Pwd) {
        try {

            SoapObject request = new SoapObject(NAMESPACE, "updateChangePWD");
            request.addProperty("pidcd", pid_cd);
            request.addProperty("oldPwd", security_Code);
            request.addProperty("contactNo", contact_No);
            request.addProperty("otpFlag", otpflg);// if otp match otpFlag Y / N
            request.addProperty("newPwd", new_Pwd);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            changePSWDconfirm = result.toString();
            try {
                changePSWDconfirm = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(changePSWDconfirm);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (SoapFault fault) {

        } catch (Exception E) {
            E.printStackTrace();
            changePSWDconfirm = "NA";
        }
    }

    public static void checkAadhar_Ticket(String unit_Code, String regn_No, String eng_No, String chasis_No) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + GET_AADHAAR_TICKET);
            request.addProperty("unitCode", "" + unit_Code);
            request.addProperty("regnNo", "" + regn_No);
            request.addProperty("engNo", "" + eng_No);
            request.addProperty("chasisNo", "" + chasis_No);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            aadhaarDetailsCheck_resp = "";

            try {
                aadhaarDetailsCheck_resp = new com.mtpv.mobilee_ticket_services.PidSecEncrypt()
                        .decrypt(result.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (SoapFault fault) {

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            aadhaarDetailsCheck_resp = "0";
        }
    }

    public static void getViolationDetails() {

    }

    public static void getversiondetails(String unit_code, String app_type, String version) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "getNewUpdationDoc");
            request.addProperty("unit_code", unit_code);
            request.addProperty("app_type", app_type);
            request.addProperty("version_code", version);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            try {
                if (result != null) {
                    version_response = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString());
                } else {
                    version_response = "NA";

                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                version_response = "NA";

                e.printStackTrace();
            }
        } catch (SoapFault fault) {
            fault.printStackTrace();

            version_response = "NA";

        } catch (Exception E) {
            E.printStackTrace();
            version_response = "NA";
        }
    }

    public static String getApprovefromRtaforPoint(String eticketNO, String regnNo, String offenceDt, String offenceTime, String stateCd,
                                                   String unitCode, String bookedPsCd, String location, String vioCodes, String curruntVioAmount,
                                                   String licNo, String driverName, String driverAddress, String driverCity, String paymentStatus,
                                                   String dOfPay, String driverContactNo, String dl_dob, String pointName, String dlImpoundFlg,
                                                   String bookedPsName, String pidName, String cadre, String impndPerd, String pointCode,
                                                   String totalVioPoints) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getApprovefromRtaforPoint");
            request.addProperty("eticketNO", eticketNO);
            request.addProperty("regnNo", regnNo);
            request.addProperty("offenceDt", offenceDt);
            request.addProperty("offenceTime", offenceTime);
            request.addProperty("stateCd", stateCd);
            request.addProperty("unitCode", unitCode);
            request.addProperty("bookedPsCd", bookedPsCd);
            request.addProperty("location", location);
            request.addProperty("vioCodes", vioCodes);
            request.addProperty("curruntVioAmount", curruntVioAmount);
            request.addProperty("licNo", licNo);
            request.addProperty("driverName", driverName);
            request.addProperty("driverAddress", driverAddress);
            request.addProperty("driverCity", driverCity);
            request.addProperty("paymentStatus", paymentStatus);
            request.addProperty("dOfPay", dOfPay);
            request.addProperty("driverContactNo", driverContactNo);
            request.addProperty("dl_dob", dl_dob);
            request.addProperty("pointName", pointName);
            request.addProperty("dlImpoundFlg", dlImpoundFlg);
            request.addProperty("bookedPsName", bookedPsName);
            request.addProperty("pidName", pidName);
            request.addProperty("cadre", cadre);
            request.addProperty("impndPerd", impndPerd);
            request.addProperty("pointCode", pointCode);
            request.addProperty("totalVioPoints", totalVioPoints);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL, timeLimit);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    rtaapproovedresponse = result.toString().trim();
                } else {
                    rtaapproovedresponse = "NA|NA|NA";
                }
            } catch (Exception e) {
                e.printStackTrace();
                rtaapproovedresponse = "NA|NA|NA";
            }
        } catch (SoapFault fault) {
            fault.printStackTrace();
            rtaapproovedresponse = "NA|NA|NA";
        } catch (SocketTimeoutException fault) {
            fault.printStackTrace();
            rtaapproovedresponse = "NA|NA|NA";
        } catch (Exception E) {
            E.printStackTrace();
            rtaapproovedresponse = "NA|NA|NA";
        }
        return rtaapproovedresponse;
    }

    public static String getOtpStatusNTime(String unitcode) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "getOtpStatusNTime");
            request.addProperty("unitcCode", unitcode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    otpStatusnTime = new com.mtpv.mobilee_ticket_services.PidSecEncrypt().decrypt(result.toString().trim());
                } else {
                    otpStatusnTime = "NA|NA";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                otpStatusnTime = "NA|NA";
            }
        } catch (SoapFault fault) {
            fault.printStackTrace();
            otpStatusnTime = "NA|NA";
        } catch (Exception E) {
            E.printStackTrace();
            otpStatusnTime = "NA|NA";
        }

        return otpStatusnTime;
    }


    public static void getCourtTopVioCasesInfo(String pidCode, String offenceFrDT, String offenceToDT)  {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "getTopVioCasesInfo");
            request.addProperty("pidCode", pidCode);
            request.addProperty("offenceFrDT", offenceFrDT);
            request.addProperty("offenceToDT", offenceToDT);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    str_TopVltns_List = result.toString().trim();
                } else {
                    str_TopVltns_List = "NA";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                str_TopVltns_List = "NA";
            }
        } catch (SoapFault fault) {
            fault.printStackTrace();
            str_TopVltns_List = "NA";
        } catch (Exception E) {
            E.printStackTrace();
            str_TopVltns_List = "NA";
        }

    }

    public static void getIDprrofMaster() {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getIDproofMaster");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    strCV_IdPrfMstr = "";
                    Log.d("FaceMask", "" + result.toString());
                    strCV_IdPrfMstr = result.toString();

                } else {
                    strCV_IdPrfMstr = "";
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                strCV_IdPrfMstr = "";
            }


        } catch (SoapFault fault) {
            fault.printStackTrace();
            strCV_IdPrfMstr = "";

        } catch (SocketTimeoutException fault) {
            fault.printStackTrace();
            strCV_IdPrfMstr = "";
        } catch (Exception e) {
            // TODO: handle exception
            strCV_IdPrfMstr = "";
        }

    }

    public static void getDetainItemMaster() {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getDetainItemMaster");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    strCV_DtndMstr = "";
                    Log.d("FaceMask", "" + result.toString());
                    strCV_DtndMstr = result.toString();
                } else {
                    strCV_DtndMstr = "";

                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                strCV_DtndMstr = "";

            }


        } catch (SoapFault fault) {
            fault.printStackTrace();
            strCV_DtndMstr = "";

        } catch (SocketTimeoutException fault) {
            fault.printStackTrace();
            strCV_DtndMstr = "";
        } catch (Exception e) {
            // TODO: handle exception
            strCV_DtndMstr = "";
        }

    }

    public static void getViolationMaster(String wheelercode, String chllanType, String pidCode, String remark) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getViolationMaster");
            request.addProperty("wheelercode", wheelercode);
            request.addProperty("challanType", chllanType);
            request.addProperty("pidCode", pidCode);
            request.addProperty("remark", remark);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    strCV_VltnsList = "";
                    Log.d("FACEAPP", "" + result.toString());
                    strCV_VltnsList = result.toString();

                } else {
                    strCV_VltnsList = "";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                strCV_VltnsList = "";
            }

        } catch (SoapFault fault) {
            strCV_VltnsList = "";
        } catch (SocketTimeoutException fault) {
            strCV_VltnsList = "";
        } catch (Exception e) {
            strCV_VltnsList = "";
        }
    }

    public static void getIDproofDetails(String IdCode, String IdValue, String dob, String pidCode) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getIDproofDetails");
            request.addProperty("IdCode", "" + IdCode);
            request.addProperty("IdValue", "" + IdValue);
            request.addProperty("dob", "" + dob);
            request.addProperty("pidCode", "" + pidCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    Log.d("FaceMaskDetails", "" + result.toString());
                    strCV_IdPrfData = "";
                    strCV_IdPrfData = result.toString();

                } else {
                    strCV_IdPrfData = "";
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                strCV_IdPrfData = "";

            }


        } catch (SoapFault fault) {
            fault.printStackTrace();
            strCV_IdPrfData = "";
        } catch (SocketTimeoutException fault) {
            fault.printStackTrace();
            strCV_IdPrfData = "";
        } catch (Exception e) {
            // TODO: handle exception
            strCV_IdPrfData = "";
        }

    }

    public static void generateCovidChallan(String json, String Image_Data) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "generateCovidChallan");
            request.addProperty("json", "" + json);
            request.addProperty("Image_Data", "" + Image_Data);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL, timeLimit);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            try {
                if (result != null) {
                    Log.d("CVRES", "" + result.toString());
                    strCV_Responce = "";
                    strCV_Responce = result.toString();

                } else {
                    strCV_Responce = "";
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                strCV_Responce = "";

            }


        } catch (SoapFault fault) {
            fault.printStackTrace();
            strCV_Responce = "";
        } catch (SocketTimeoutException fault) {
            fault.printStackTrace();
            strCV_Responce = "";
        } catch (Exception e) {
            // TODO: handle exception
            strCV_Responce = "";
        }

    }
}