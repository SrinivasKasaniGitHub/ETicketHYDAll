package com.mtpv.mobilee_ticket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mtpv.mobilee_ticket_services.DateUtil;
import com.mtpv.mobilee_ticket_services.ServiceHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



/**
 * Created by MANOHAR on 8/31/2017.
 */

public class mtpv_SpecialDrive extends Activity implements View.OnClickListener,LocationListener,CompoundButton.OnCheckedChangeListener {


    public static LinearLayout vehicle_type,ll_3,ll_pendingchallans_spot_xml;

    RelativeLayout rl_detaineditems_spotchallantwo_xml;

    public static CheckBox police_vehcle, govt_vehcle, check;

    boolean isGPSEnabled = false;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public String  present_date_toSend = "";
    public static String[] rta_details_spot_master, offender_remarks_resp_master, helmet_remarks, Wheeler_check,
            otp_Master;

    public static boolean passngerFLG = false, spotPamentFLG = false;

    public static EditText et_regcid_spot, et_vchl_num_spot, et_last_num_spot, et_driver_lcnce_num_spot, et_id_proof_spot, et_driver_contact_spot,
            et_remarks_spot, et_aadharnumber_spot, et_drivername_iOD, et_driverFatherName_iOD, et_driver_address_iOD, et_driver_city_iOD;

    public static TextView tv_spot_or_vhclehistory_header, tv_vehicle_details_header_spot, tv_licence_details_header_spot,
            tv_spotChallanTwo_header, tv_vhle_no_spot, tv_owner_name_spot, tv_address_spot, tv_maker_name_spot, tv_engine_no_spot, tv_chasis_no_spot,
            tv_licnce_ownername_spot, tv_lcnce_father_name_spot, tv_lcnce_phone_number_spot, tv_lcnce_address_spot, dl_no, tv_total_pending_challans,
            tv_toal_amount_pending_challans, tv_grand_total_spot, tv_aadhar_header, tv_aadhar_user_name, tv_aadhar_care_off, tv_aadhar_address,
            tv_aadhar_mobile_number, tv_aadhar_gender, tv_aadhar_dob, tv_aadhar_uid, tv_violation_amnt, tv_dlpoints_spotchallan_xml;

    public static ImageView licence_image;
    ImageView img_aadhar_image, qr_code;

    TelephonyManager telephonyManager;


    public static Button btn_wheller_code, btn_violation, btn_get_details_spot, btn_first_tosecond_spot, btn_first_cancel_spot;

    public static ImageView offender_image;

    EditText ed_engine,ed_chasis;
    ImageButton ibtn_camera;
    ImageButton ibtn_gallery;

    RelativeLayout rl_rta_details_layout;
    RelativeLayout rl_card_details;
    RelativeLayout rl_detained_items;

    String  engine=null,chasis=null;

    int present_date, present_month, present_year, present_hour, present_minutes;

    public static String pidCode = null, pidName = null, psCd = null, psName = null, cadre_code = null,
            cadre_name = null, pass_word = null, off_phone_no = null, current_version = null, rta_data_flg = null,
            dl_data_flg = null, aadhaar_data_flg = null, otp_no_flg = null, cashless_flg = null, mobileNo_flg = null,otp_status = "",otpValue = "",vStatusConfirmationYN = "";
    public static double latitude = 0.0, longitude = 0.0, total_amount = 0, grand_total = 0;

    Calendar calendar;

    RadioGroup radiogrp_release_detained_items;
    RadioButton radioGroupButton_isOwner, radioGroupButton_isDriver,
            radioGroupButton_detaineditems_yes, radioGroupButton_detaineditems_no;

    RadioGroup radiogrp_spot_payment, radiogrp_cash_or_card;

    public static RadioButton radioGroupButton_spotpaymentNo, radioGroupButton_spotpaymentYes, radioGroupButton_cashcard1, radioGroupButton_cashcard0;
    LinearLayout ll_detained_itemlist_layout, ll_grand_total;

    LinearLayout[] ll;

    CheckBox[] cb;

    CheckBox chck_detainedItems_rc, chck_detainedItems_vhcle, chck_detainedItems_licence, chck_detainedItems_permit, chck_detainedItems_none;

    static StringBuilder sb_detained_items;

    TextView textView_header_spot_challan_xml;
    LinearLayout ll_dynamic_violations_root_static;
    LinearLayout[] ll_dynamic_vltns;
    Spinner[] spinner_violation;
    RadioGroup rg_fakegroup;
    RadioButton rd_fake, rd_notfake;

    TextView[] tv_dynamic_vltn_name;
    CheckBox[] check_dynamic_vltn;
    int edt_regncid_lastnum_spotchallanMAX_LENGTH = 4,edt_regncid_spotchallanMAX_LENGTH = 4, edt_regncidname_spotchallanLENGTH = 4;

    private int mYear, mMonth, mDay;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10, MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    LinearLayout ll_extra_people;
    EditText et_extra_people;
    boolean isNetworkEnabled = false, canGetLocation = false;

    public static String btn_otp_flag = "0", helemt_rc_value, helemt_adhar_value, helemt_dl_value, dl_points = "0", spot_Lic_Flag = "", imgSelected = "0",
            is_govt_police = "9", extraPassengers = "1", aadhaar, licence_no, otp_number = "", driver_mobileNo = "", minor_valueCode = "", violation_code,
            Current_Date, final_image_data_tosend = null, DLvalidFLG = "VALID";

    public static String completeVehicle_num_send = "", regncode_send = "", regnName_send = "", vehicle_num_send = "",
            fake_veh_chasisNo = "", whlr_code_send = "";

    StringBuffer message = new StringBuffer();

    public static RadioGroup radioGrp_regNo_EngnNo_Chasis;
    public static RadioButton radioGroupButton_regNo, radioGroupButton_engineNo, radioGroupButton_chasisNo;
    public static LinearLayout ll_mainsub_root, ll_engineNo, ll_chasisNo, ll_regno;
    public static EditText et_engineNo, et_chasisNo;

    public static String Compleate_address ,specialDrive,specialDriveData;

    String NETWORK_TXT = "", imei_send = "", simid_send = "", macAddress = "", cam_imag = "";

    LocationManager m_locationlistner;

    Location location;

    StringBuffer present_time_toSend;

    public static boolean EngneFLG = false, regNoFLG = false, chasisFLG = false, veh_HisFLG = false;
    public static int isitTr = 1,soldOut=0;

    public static Button btn_select_profession;
    public static EditText edt_prfession_name, edt_prfession_Address, edt_email_ID;

    public static int WHITE = 0xFFFFFFFF, BLACK = 0xFF000000;
    public final static int width = 500;

    public static final int PROGRESS_DIALOG = 0,  FAKE_NUMBERPLATE_DIALOG = 1,OTP_CNFRMTN_DIALOG = 2;

    LinearLayout rl_licence_details_layout, ll_pending_challans, ll_cash_or_card, ll_aadhar_layout, ll_camera_gallery,
            ll_detained_items_root, ll_spot_payment_root, ll_is_owner_driver, ll_drivertype_rgbtn;
    VerhoeffCheckDigit ver;
    String dobcheck = "No";
    Button dob_input;
    SimpleDateFormat date_format, date_format2;


    ArrayList<String> violation_list, violation_description, violation_section, violation_offence_Code, violation_min_amount,
            violation_max_amount, violation_avg_amount, violation_positions, violation_rg_ids, violation_checked_violations;

    public static String dob_DL = null, ll_validationString = "",otp_msg = "";

    public  String fakeStatus=null,point_name_send_from_settings;

    public  static Button btn_send_otp_to_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_specialdrive);

        ver = new VerhoeffCheckDigit();



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pidCode = sharedPreferences.getString("PID_CODE", "");
        pidName = sharedPreferences.getString("PID_NAME", "");
        psCd = sharedPreferences.getString("PS_CODE", "");
        psName = sharedPreferences.getString("PS_NAME", "");
        cadre_code = sharedPreferences.getString("CADRE_CODE", "");
        cadre_name = sharedPreferences.getString("CADRE_NAME", "");
        pass_word = sharedPreferences.getString("PASS_WORD", "");
        off_phone_no = sharedPreferences.getString("OFF_PHONE_NO", "");
        current_version = sharedPreferences.getString("CURRENT_VERSION", "");
        rta_data_flg = sharedPreferences.getString("RTA_DATA_FLAG", "");
        dl_data_flg = sharedPreferences.getString("DL_DATA_FLAG", "");
        aadhaar_data_flg = sharedPreferences.getString("AADHAAR_DATA_FLAG", "");
        otp_no_flg = sharedPreferences.getString("OTP_NO_FLAG", "");
        cashless_flg = sharedPreferences.getString("CASHLESS_FLAG", "");
        mobileNo_flg = sharedPreferences.getString("MOBILE_NO_FLAG", "");


        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = preferences.edit();
        point_name_send_from_settings = preferences.getString("point_name", "pointname");

        LoadUIcomponents();


    }



    private void LoadUIcomponents() {
        // TODO Auto-generated method stub

        ed_engine=(EditText)findViewById(R.id.ed_engine);
        ed_engine.requestFocus();

        ll_3=(LinearLayout)findViewById(R.id.ll_3);




        ed_chasis=(EditText)findViewById(R.id.ed_chasis);

        et_regcid_spot = (EditText) findViewById(R.id.edt_regncid_spotchallan_xml);
        // et_regcid_spot.setText("AP29");
        et_vchl_num_spot = (EditText) findViewById(R.id.edt_regncidname_spotchallan_xml);
        // et_vchl_num_spot.setText("BS");
        et_last_num_spot = (EditText) findViewById(R.id.edt_regncid_lastnum_spotchallan_xml);
        // et_last_num_spot.setText("7402");

        offender_image = (ImageView) findViewById(R.id.offender_image);
        offender_image.setVisibility(View.GONE);



        et_regcid_spot.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_regcid_spot.getText().toString().length() == edt_regncid_spotchallanMAX_LENGTH) {
                    et_vchl_num_spot.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_vchl_num_spot.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_vchl_num_spot.getText().toString().length() == edt_regncidname_spotchallanLENGTH) {
                    et_last_num_spot.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_last_num_spot.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_last_num_spot.getText().toString().length() == edt_regncid_lastnum_spotchallanMAX_LENGTH) {
                    et_driver_lcnce_num_spot.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        vehicle_type = (LinearLayout) findViewById(R.id.vehicle_type);
        police_vehcle = (CheckBox) findViewById(R.id.police_vehcle);
        govt_vehcle = (CheckBox) findViewById(R.id.govt_vehcle);

        police_vehcle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (police_vehcle.isChecked() == true && govt_vehcle.isChecked() == true) {
                    police_vehcle.setChecked(true);
                    govt_vehcle.setChecked(false);
                }
            }
        });

        govt_vehcle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (police_vehcle.isChecked() == true && govt_vehcle.isChecked() == true) {
                    govt_vehcle.setChecked(true);
                    police_vehcle.setChecked(false);
                }
            }
        });

        radioGroupButton_isOwner = (RadioButton) findViewById(R.id.radioGroupButton_isOwner);
        radioGroupButton_isDriver = (RadioButton) findViewById(R.id.radioGroupButton_isDriver);

        // hardcode


        // et_driver_lcnce_num_spot.setText("7461WGL1998OD");

        et_driver_lcnce_num_spot = (EditText)findViewById(R.id.edt_driverdlno_spotchallan_xml);
        dob_input = (Button) findViewById(R.id.dob_input);


        et_driver_contact_spot = (EditText)findViewById(R.id.edt_drvr_cnctno_spotchallantwo_xml);
        btn_send_otp_to_mobile = (Button)findViewById(R.id.btn_sendOTPtoMobile_spotchallantwo_xml);



        /***** BUTTON EVENTS *******/
        btn_send_otp_to_mobile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                String tempContactNumber = et_driver_contact_spot.getText().toString();

                if (tempContactNumber.equals("")) {
                    et_driver_contact_spot.setError(
                            Html.fromHtml("<font color='black'>Enter mobile number to send OTP!!</font>"));
                    et_driver_contact_spot.requestFocus();

                } else if (tempContactNumber.trim() != null && tempContactNumber.trim().length() > 1
                        && tempContactNumber.trim().length() != 10) {
                    et_driver_contact_spot
                            .setError(Html.fromHtml("<font color='black'>Enter Valid mobile number!!</font>"));
                    et_driver_contact_spot.requestFocus();

                } else if (tempContactNumber.length() == 10) {
                    if ((tempContactNumber.charAt(0) == '7') || (tempContactNumber.charAt(0) == '8')
                            || (tempContactNumber.charAt(0) == '9')) {
                        if (isOnline()) {
                            otp_status = "send";
                            SpotChallan.mobilenumber="";
                            SpotChallan.mobilenumber=tempContactNumber;
                            new Async_sendOTP_to_mobile().execute();
                        } else {
                            showToast("Please check your network connection!");
                        }
                    } else {
                        et_driver_contact_spot
                                .setError(Html.fromHtml("<font color='black'>Check Contact No.!!</font>"));
                        et_driver_contact_spot.requestFocus();
                    }

                } else if (tempContactNumber.length() == 11) {
                    if (tempContactNumber.charAt(0) == '0') {
                        if (isOnline()) {
                            otp_status = "send";
                            SpotChallan.mobilenumber="";
                            SpotChallan.mobilenumber=tempContactNumber;

                            new Async_sendOTP_to_mobile().execute();
                        } else {
                            showToast("Please check your network connection!");
                        }
                    } else {
                        et_driver_contact_spot
                                .setError(Html.fromHtml("<font color='black'>Check Contact No.!!</font>"));
                        et_driver_contact_spot.requestFocus();
                    }

                }
            }
        });




        et_driver_lcnce_num_spot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!et_driver_lcnce_num_spot.getText().toString().equalsIgnoreCase("")
                        && et_driver_lcnce_num_spot.getText().toString().length() >= 5) {
                    dob_input.setVisibility(View.VISIBLE);


                    dob_input.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            {
                                // Get Current Date
                                final Calendar c = Calendar.getInstance();
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog datePickerDialog = new DatePickerDialog(mtpv_SpecialDrive.this,
                                        new DatePickerDialog.OnDateSetListener() {

                                            @Override
                                            public void onDateSet(DatePicker view, int year,
                                                                  int monthOfYear, int dayOfMonth) {
                                                SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");

                                                SimpleDateFormat date_parse = new SimpleDateFormat("dd/MM/yyyy");
                                                String dtdob = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                                try {
                                                    dob_DL = date_format.format(date_parse.parse(dtdob));
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }


                                                String todaysdate = new DateUtil().getTodaysDate();

                                                long days = new DateUtil().DaysCalucate(dob_DL, todaysdate);

                                                //Minimum Age should be 16
                                                if (days > 5824) {
                                                    dob_input.setText(dob_DL);

                                                } else {
                                                    ShowMessageDateAlert("Please select Date of Birth Atleast Person Should be Age Greater Than 16");
                                                }


                                                //  dob_DL = date_format.format(dayOfMonth  + (monthOfYear + 1) + year);
                                            }
                                        }, mYear, mMonth, mDay);
                                datePickerDialog.show();
                                dobcheck = "Yes";
                            }
                        }
                    });
                } else {

                    dob_input.setText("Select Date of Birth");
                    dob_input.setVisibility(View.GONE);
                    dobcheck = "No";
                    dob_DL = null;

                }
            }
        });

//
        et_aadharnumber_spot = (EditText) findViewById(R.id.edt_aadharno_spotchallan_xml);

        et_aadharnumber_spot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        // et_aadharnumber_spot.setText("322847907255");

        et_drivername_iOD = (EditText) findViewById(R.id.edt_driverdname_isOD);
        et_driverFatherName_iOD = (EditText) findViewById(R.id.edt_driverfathername_isOD);
        et_driver_address_iOD = (EditText) findViewById(R.id.edt_address_isOD);
        et_driver_city_iOD = (EditText) findViewById(R.id.edt_city_isOD);

        //  btn_wheller_code = (Button) findViewById(R.id.btn_whlr_code_spotchallan_xml);
        btn_violation = (Button) findViewById(R.id.btn_violation_spotchallan_xml);
        btn_get_details_spot = (Button) findViewById(R.id.btngetrtadetails_spotchallan_xml);

        btn_first_tosecond_spot = (Button) findViewById(R.id.btn_next_spotchallan_xml);
        btn_first_cancel_spot = (Button) findViewById(R.id.btn_cancel_spotchallan_xml);

        tv_spot_or_vhclehistory_header = (TextView) findViewById(R.id.textView_header_spot_challan_xml);

        tv_spot_or_vhclehistory_header.setText("Special Drive");

		/* TO SHOW vehicel DETAILS AND LICENCE DETAILS FOUND OR NOT */
        tv_vehicle_details_header_spot = (TextView) findViewById(R.id.textView_regdetails_header_spotchallan_xml);
        tv_licence_details_header_spot = (TextView) findViewById(R.id.textView_licence_header_spotchallan_xml);

		/* RTA COMPLETE DETAILS */
        tv_vhle_no_spot = (TextView) findViewById(R.id.tvregno_spotchallan_xml);
        tv_owner_name_spot = (TextView) findViewById(R.id.tvownername_spotchallan_xml);
        tv_address_spot = (TextView) findViewById(R.id.tv_addr_spotchallan_xml);
        // tv_city_spot = (TextView) findViewById(R.id.tv_city_spotchallan_xml);
        tv_maker_name_spot = (TextView) findViewById(R.id.tv_makername_spotchallan_xml);
        // tv_maker_class_spot = (TextView)
        // findViewById(R.id.tv_makerclass_spotchallan_xml);
        // tv_color_spot = (TextView)
        // findViewById(R.id.tv_color_spotchallan_xml);
        tv_engine_no_spot = (TextView) findViewById(R.id.tv_engineno_spotchallan_xml);
        tv_chasis_no_spot = (TextView) findViewById(R.id.tv_chasis_spotchallan_xml);

		/* AADHRA DETAILS */
        ll_aadhar_layout = (LinearLayout) findViewById(R.id.ll_aadhardetails_spot_challan_xml);
        tv_aadhar_header = (TextView) findViewById(R.id.tvadhardetails_header_label_spotchallan_xml);
        tv_aadhar_user_name = (TextView) findViewById(R.id.tvaadharname_spotchallan_xml);
        tv_aadhar_care_off = (TextView) findViewById(R.id.tvcareof_spotchallan_xml);
        tv_aadhar_address = (TextView) findViewById(R.id.tvaddress_spotchallan_xml);
        tv_aadhar_mobile_number = (TextView) findViewById(R.id.tvmobilenumber_spotchallan_xml);
        tv_aadhar_gender = (TextView) findViewById(R.id.tvgender_spotchallan_xml);
        tv_aadhar_dob = (TextView) findViewById(R.id.tvdob_spotchallan_xml);
        tv_aadhar_uid = (TextView) findViewById(R.id.tvuid_spotchallan_xml);
        // tv_aadhar_eid = (TextView) findViewById(R.id.tveid_spotchallan_xml);
        img_aadhar_image = (ImageView) findViewById(R.id.imgv_aadhar_photo_spotchallan_xml);
        qr_code = (ImageView) findViewById(R.id.qr_code);
        ll_aadhar_layout.setVisibility(View.GONE);
        tv_aadhar_header.setVisibility(View.GONE);

		/* LICENCE COMPLETE DETAILS */
        licence_image = (ImageView) findViewById(R.id.imgv_licence_spotchallan_xml);
        tv_licnce_ownername_spot = (TextView) findViewById(R.id.tvlcnceownername_spotchallan_xml);
        tv_dlpoints_spotchallan_xml = (TextView) findViewById(R.id.tv_dlpoints_spotchallan_xml);

        tv_lcnce_father_name_spot = (TextView) findViewById(R.id.tvlcnce_fname_spotchallan_xml);
        tv_lcnce_phone_number_spot = (TextView) findViewById(R.id.tv_lcnce_mobnum_spotchallan_xml);
        tv_lcnce_address_spot = (TextView) findViewById(R.id.tv_lcnce_Address_spotchallan_xml);
        dl_no = (TextView) findViewById(R.id.dl_no);

        rl_rta_details_layout = (RelativeLayout) findViewById(R.id.rl_detailsresponse_spotchallan_xml);
        rl_licence_details_layout = (LinearLayout) findViewById(R.id.rl_licences_spotchallan_xml);
        ll_pending_challans = (LinearLayout) findViewById(R.id.ll_pendingchallans_spot_xml);
        ll_grand_total = (LinearLayout) findViewById(R.id.ll_4_spot_challan_xml);


        tv_total_pending_challans = (TextView) findViewById(R.id.tv_pendingchallans_total_spotchallan_xml);
        tv_toal_amount_pending_challans = (TextView) findViewById(R.id.tv_pendingamount_spotchallan_xml);
        tv_grand_total_spot = (TextView) findViewById(R.id.tv_grand_totalamnt_spotchallan_xml);
        tv_violation_amnt = (TextView) findViewById(R.id.tv_violtaionamnt_spotchallan_xml);
        //    tv_grand_total_spot.setText("Rs . " + total_amount);

		/* CAMERA & GALLERY */
        ibtn_camera = (ImageButton) findViewById(R.id.imgv_camera_capture_spotchallan_xml);
        ibtn_gallery = (ImageButton) findViewById(R.id.imgv_gallery_spotchallan_xml);

        //  wv_generate = (WebView) findViewById(R.id.webView_image_spotchallan_xml);

        // star = (TextView) findViewById(R.id.star);

		/* VEHICLE HISTORY */
        ll_camera_gallery = (LinearLayout) findViewById(R.id.ll_main_root_spotchallan_xml);

        // rg_isOwner_isDriver = (RadioGroup) findViewById(R.id.radioGroup_isOwner_isDriver);
        ll_is_owner_driver = (LinearLayout) findViewById(R.id.ll_isOwner_isDriver);
        ll_drivertype_rgbtn = (LinearLayout) findViewById(R.id.ll_drivertype_spot_xml);

		/* CLICK LISTENERS */
        // btn_wheller_code.setOnClickListener(this);
        // btn_violation.setOnClickListener(this);
        btn_get_details_spot.setOnClickListener(this);

        ll_pending_challans.setOnClickListener(this);
        btn_first_tosecond_spot.setOnClickListener(this);
        btn_first_cancel_spot.setOnClickListener(this);

//        ibtn_camera.setOnClickListener(this);
//        ibtn_gallery.setOnClickListener(this);

		/* TO HIDE HEADER FOR DETAILS */
        tv_vehicle_details_header_spot.setVisibility(View.GONE);
        tv_licence_details_header_spot.setVisibility(View.GONE);
        rl_rta_details_layout.setVisibility(View.GONE);
        // rl_licence_details_layout.setVisibility(View.GONE);

		/* NO OF PEOPLE LABEL */
        ll_extra_people = (LinearLayout) findViewById(R.id.ll_extraviolations_spotchallan_xml);
        et_extra_people = (EditText) findViewById(R.id.edt_extarviolat_spotchallan_xml);
        ll_extra_people.setVisibility(View.GONE);

        Dashboard.rta_details_request_from = "";
        ServiceHelper.pending_challans_details = new String[0][0];
        ServiceHelper.pending_challans_master = new String[0];

        // releasedDetained_items_list_toSend.remove(releasedDetained_items_list_toSend);
        ll_regno = (LinearLayout) findViewById(R.id.ll_regno);
        ll_engineNo = (LinearLayout) findViewById(R.id.ll_engineNo);
        ll_chasisNo = (LinearLayout) findViewById(R.id.ll_chasisNo);

        radioGrp_regNo_EngnNo_Chasis = (RadioGroup) findViewById(R.id.radioGrp_regNo_EngnNo_Chasis);

        radioGroupButton_regNo = (RadioButton) findViewById(R.id.radioGroupButton_regNo);






        rl_detained_items = (RelativeLayout) findViewById(R.id.rl_detaineditems_spotchallantwo_xml);// rc,vchle.lcnce,prmt,none
			/* DETAINED ITEMS START */
        chck_detainedItems_rc = (CheckBox) findViewById(R.id.checkBox_dt_rc_spotchallantwo_xml);
        chck_detainedItems_vhcle = (CheckBox) findViewById(R.id.checkBox_dt_vchle_spotchallantwo_xml);
        chck_detainedItems_licence = (CheckBox) findViewById(R.id.checkBox_dt_lcns_spotchallantwo_xml);
        chck_detainedItems_permit = (CheckBox) findViewById(R.id.checkBox_dt_permit_spotchallantwo_xml);
        chck_detainedItems_none = (CheckBox) findViewById(R.id.checkBox_dt_none_spotchallantwo_xml);



        //  radioGroupButton_engineNo.setChecked(true);


    /*    et_engineNo = (EditText) findViewById(R.id.et_engineNo);
        et_chasisNo = (EditText) findViewById(R.id.et_chasisNo);*/

        ll_engineNo.setVisibility(View.GONE);
        ll_regno.setVisibility(View.VISIBLE);
        ll_chasisNo.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btngetrtadetails_spotchallan_xml:
                // enabling default values

                resetData();

//                regncode_send = "";// AP09
//                regnName_send = "";// CC
//                vehicle_num_send = "";// 3014

                // START DRIVER DETAILS MANUAL ENTRIES
                et_drivername_iOD.setEnabled(true);
                et_drivername_iOD.setClickable(true);

                et_driverFatherName_iOD.setEnabled(true);
                et_driverFatherName_iOD.setClickable(true);

                et_driver_address_iOD.setEnabled(true);
                et_driver_address_iOD.setClickable(true);

                et_driver_city_iOD.setEnabled(true);
                et_driver_city_iOD.setClickable(true);

                police_vehcle.setChecked(false);
                govt_vehcle.setChecked(false);


                if((ed_engine.getText()!=null && ed_engine.getText().toString().equalsIgnoreCase("") && (ed_chasis.getText()!=null && ed_chasis.getText().toString().equalsIgnoreCase(""))))
                {

                    ShowMessage("Please Enter Engine Or Chasis Number To Continue");
                }else {

                    engine=ed_engine.getText().toString()!=null?ed_engine.getText().toString():"";
                    chasis=ed_chasis.getText().toString()!=null?ed_chasis.getText().toString():"";



                    if (!et_driver_lcnce_num_spot.getText().toString().equalsIgnoreCase("") && et_driver_lcnce_num_spot.getText().toString().length() >= 5) {

                        if (dobcheck.equalsIgnoreCase("Yes")) {
                            Asyncallsofmethods();

                        } else {
                            showToast("Please Select Date Of Birth !");
                        }
                    } else {
                        Asyncallsofmethods();
                    }
                }
                break;

            case R.id.btn_cancel_spotchallan_xml:

                resetData();
                Intent i=new Intent(mtpv_SpecialDrive.this,Dashboard.class);
                startActivity(i);
                finish();
                break;


            case R.id.btn_next_spotchallan_xml:

                if(isOnline()) {

                    sb_detained_items.delete(0, sb_detained_items.length());
                    if (chck_detainedItems_rc.isChecked()) {
                        sb_detained_items.append("01:RC@");
                    }

                    if (chck_detainedItems_vhcle.isChecked()) {
                        sb_detained_items.append("02:VEHICLE@");
                    }

                    if (chck_detainedItems_licence.isChecked()) {
                        sb_detained_items.append("03:LICENCE@");
                    }

                    if (chck_detainedItems_permit.isChecked()) {
                        sb_detained_items.append("04:PERMIT@");
                    }

                    if (chck_detainedItems_none.isChecked()) {
                        sb_detained_items.append("");
                    }

                    if ("Y".equals(SpotChallan.OtpStatus.trim())
                            && (!Dashboard.check_vhleHistory_or_Spot.equals("towing"))
                            && otp_status != "verify") {
                        showToast("Please Verify OTP");
                    }
                    else {


                        if (isOnline()) {
                            async_insert_DetainItems insert_detainItems = new async_insert_DetainItems();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                                insert_detainItems.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                insert_detainItems.execute();
                            }
                        } else {
                            showToast("Please Check Your Network Connection");
                        }
                    }
                }else {
                    showToast("Please Check Your Network Connection");
                }

                break;

        }
    }


    public void Asyncallsofmethods() {
        if (isitTr == 1 || regNoFLG == true) {

            if (et_regcid_spot.getText().toString().equals("")) {
                et_regcid_spot.setError(Html.fromHtml("<font color='black'>Enter Registration Code</font>"));
                et_regcid_spot.requestFocus();

            } else if (et_last_num_spot.getText().toString().equals("")) {
                et_last_num_spot.setError(Html.fromHtml("<font color='black'>Enter Vehicle Code</font>"));
                et_last_num_spot.requestFocus();

            } else if (!et_regcid_spot.getText().toString().equals("")
                    && !et_last_num_spot.getText().toString().equals("")) {
                // //new Async_checkAadhaarExists().execute();
                if ("N".equals(rta_data_flg.trim().toUpperCase())) {
                    // IF NO DATA FOUND FILL THE DETAILS MANUALLY
                    ll_is_owner_driver.setVisibility(View.VISIBLE);
                } else {

                    regncode_send = "" + et_regcid_spot.getText().toString().trim().toUpperCase();
                    regnName_send = "" + et_vchl_num_spot.getText().toString().toUpperCase();
                    vehicle_num_send = "" + et_last_num_spot.getText().toString().trim().toUpperCase();

                    aadhaar = et_aadharnumber_spot.getText().toString().trim();
                    licence_no = et_driver_lcnce_num_spot.getText().toString().trim();

                    et_drivername_iOD.setText("");
                    et_driverFatherName_iOD.setText("");
                    et_driver_address_iOD.setText("");
                    et_driver_city_iOD.setText("");


                    if (isOnline()) {

                        rl_rta_details_layout.setVisibility(View.GONE);
                        rl_licence_details_layout.setVisibility(View.GONE);
                        ll_aadhar_layout.setVisibility(View.GONE);


//                        start = System.currentTimeMillis();
//                        Log.i("TIME>>>START>>>Async_getRTADetails", String.valueOf(start));

                        if (isOnline()){
                            Async_getRTADetails rta_task = new Async_getRTADetails();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                                rta_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                rta_task.execute();
                            }
                        }


                        if(isOnline())
                        {
                            async_validate_Regno validate_regno = new async_validate_Regno();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                                validate_regno.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                validate_regno.execute();
                            }
                        }


                        if (!et_driver_lcnce_num_spot.getText().toString().trim().equals("")) {

//                            start = System.currentTimeMillis();
//                            Log.i("TIME>>>START>>>Async_getLicenceDetails",String.valueOf(start));

                            if(isOnline()) {
                                Async_getLicenceDetails dl_task = new Async_getLicenceDetails();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                                    dl_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    dl_task.execute();
                                }
                            }
                        }


                        if (et_aadharnumber_spot.getText() != null
                                && et_aadharnumber_spot.getText().toString().trim().length() >= 1
                                && (!ver.isValid(et_aadharnumber_spot.getText().toString()))) {
                            showToast("Please Enter Valid Adhaar Number");
                            et_aadharnumber_spot.setError(
                                    Html.fromHtml("<font color='black'>Please Enter Valid Adhaar Number</font>"));
                        } else if ((et_aadharnumber_spot.getText().toString().trim().length() == 12)
                                || (et_aadharnumber_spot.getText().toString().trim().length() == 28)) {
                            if (isOnline()) {
//                                start = System.currentTimeMillis();
//                                Log.i("TIME>>>START>>>Async_getAadharDetails",String.valueOf(start));
                                Async_getAadharDetails adhar_task = new Async_getAadharDetails();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                                    adhar_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    adhar_task.execute();
                                }
                            }
                        }

                        /*  if (et_aadharnumber_spot.getText() != null
                                && et_aadharnumber_spot.getText().toString().trim().length() >= 1
                                && (!ver.isValid(et_aadharnumber_spot.getText().toString()))) {
                            showToast("Please Enter Valid Adhaar Number");
                            et_aadharnumber_spot.setError(
                                    Html.fromHtml("<font color='black'>Please Enter Valid Adhaar Number</font>"));
                        } else if ((et_aadharnumber_spot.getText().toString().trim().length() == 12)
                                || (et_aadharnumber_spot.getText().toString().trim().length() == 28)) {
                            if (isOnline()) {
//                                start = System.currentTimeMillis();
//                                Log.i("TIME>>>START>>>Async_getAadharDetails",String.valueOf(start));
                               Async_getAadharDetails adhar_task = new Async_getAadharDetails();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                                    adhar_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    adhar_task.execute();
                                }
                            }
                        }*/

//                        start = System.currentTimeMillis();
//                        Log.i("TIME>>>START>>>Async_getPendingChallans",String.valueOf(start));

                        if(isOnline()) {
                            Async_getPendingChallans pending_task = new Async_getPendingChallans();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
                                pending_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                pending_task.execute();
                            }

                            if(isOnline()) {
                                Async_getOtpStatusNTime OtpStatusNTime = new Async_getOtpStatusNTime();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
                                    OtpStatusNTime.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    OtpStatusNTime.execute();
                                }
                            }

                        }
                    } else {
                        showToast("" + getResources().getString(R.string.newtork_txt));
                    }
                }
            }
        }
    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }


    private void showToast(String msg) {
        // TODO Auto-generated method stub
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();

        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(24);

        toastView.setBackgroundResource(R.drawable.toast_background);
        toast.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
            // speed = location.getSpeed();
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    /* LICENCE DETAILS */
    public class Async_getLicenceDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // public String getAADHARData(String uid,String eid,String
            // imei,String simNo,String gpsLattitude,String gpsLongitude);
            try {
                ServiceHelper.getLicenceDetails("" + licence_no, dob_DL);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("unused")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            try {
                String threeWheeler = tv_vhle_no_spot.getText()!=null?tv_vhle_no_spot.getText().toString().trim():"";
                tv_licence_details_header_spot.setVisibility(View.VISIBLE);



                if (ServiceHelper.license_data != null && !ServiceHelper.license_data.equals("0") &&
                        (  SpotChallan.licence_details_spot_master!=null &&  SpotChallan.licence_details_spot_master.length > 0)) {

                    ll_validationString = ServiceHelper.license_data;
                    rl_licence_details_layout.setVisibility(View.VISIBLE);

                    try {

                        tv_licnce_ownername_spot.setText("" +  SpotChallan.licence_details_spot_master[0] != null ?  SpotChallan.licence_details_spot_master[0] : "NA");
                        tv_lcnce_father_name_spot.setText("" +  SpotChallan.licence_details_spot_master[1] != null ?  SpotChallan.licence_details_spot_master[1] : "NA");
                        tv_lcnce_phone_number_spot.setText("" +  SpotChallan.licence_details_spot_master[2] != null ?  SpotChallan.licence_details_spot_master[2] : "NA");
                        tv_lcnce_address_spot.setText("" +  SpotChallan.licence_details_spot_master[4] != null ?  SpotChallan.licence_details_spot_master[4] : "NA");

                        et_drivername_iOD.setText("" +  SpotChallan.licence_details_spot_master[0] != null ?  SpotChallan.licence_details_spot_master[0] : "NA");
                        et_driverFatherName_iOD.setText("" +  SpotChallan.licence_details_spot_master[1] != null ?  SpotChallan.licence_details_spot_master[1] : "NA");
                        et_driver_city_iOD.setText("" +  SpotChallan.licence_details_spot_master[2] != null ?  SpotChallan.licence_details_spot_master[2] : "NA");
                        et_driver_address_iOD.setText("" +  SpotChallan.licence_details_spot_master[4] != null ?  SpotChallan.licence_details_spot_master[4] : "NA");

                        dl_points =  SpotChallan.licence_details_spot_master[7] != null ?  SpotChallan.licence_details_spot_master[7] : "0";

                        if (licence_no != null && (dl_points != null && !"null".equals(dl_points) && Integer.parseInt(dl_points) > 0)) {

                            tv_dlpoints_spotchallan_xml.setText("TOTAL PENALTY POINTS :" + dl_points);

                        } else {
                            tv_dlpoints_spotchallan_xml.setText("TOTAL PENALTY POINTS :" + "0");
                        }

                        /******* start driving license image display ******/
                        if (null !=  SpotChallan.licence_details_spot_master[5] &&  SpotChallan.licence_details_spot_master[5].toString().trim().equals("0")) {
                            licence_image.setImageResource(R.drawable.photo);
                        } else {
                            try {
                                byte[] decodestring = Base64.decode("" +  SpotChallan.licence_details_spot_master[5].toString().trim(),
                                        Base64.DEFAULT);
                                Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                                licence_image.setImageBitmap(decocebyte);
                            } catch (Exception e) {
                                e.printStackTrace();
                                licence_image.setImageDrawable(getResources().getDrawable(R.drawable.camera));
                            }
                        }
                        /******* end driving license image display ******/

                        dl_no.setText("" + et_driver_lcnce_num_spot.getText().toString().trim());
                        tv_licence_details_header_spot.setText("LICENCE DETAILS");

                        DLvalidFLG = "" +  SpotChallan.licence_details_spot_master[6] != null ?  SpotChallan.licence_details_spot_master[6] : "";


                        if ( SpotChallan.licence_details_spot_master.length != 0) {

                            Log.i("Driver ........", "Details");

                            et_drivername_iOD.setText("" +  SpotChallan.licence_details_spot_master[0].toUpperCase() != null ?  SpotChallan.licence_details_spot_master[0] : "NA");
                            et_driverFatherName_iOD.setText("" +  SpotChallan.licence_details_spot_master[1].toUpperCase() != null ?  SpotChallan.licence_details_spot_master[1] : "NA");
                            et_driver_address_iOD.setText("" +  SpotChallan.licence_details_spot_master[4].toUpperCase() != null ?  SpotChallan.licence_details_spot_master[4] : "NA");
                            et_driver_city_iOD.setText("");

                            if (et_drivername_iOD.getText().toString().trim().length() > 0) {
                                et_drivername_iOD.setEnabled(false);
                                et_drivername_iOD.setClickable(false);
                            } else if (et_drivername_iOD.getText().toString().trim().length() == 0) {
                                et_drivername_iOD.setEnabled(true);
                                et_drivername_iOD.setClickable(true);
                            }

                            if (et_driverFatherName_iOD.getText().toString().trim().length() > 0) {
                                et_driverFatherName_iOD.setEnabled(false);
                                et_driverFatherName_iOD.setClickable(false);
                            } else if (et_driverFatherName_iOD.getText().toString().trim().length() == 0) {
                                et_driverFatherName_iOD.setEnabled(true);
                                et_driverFatherName_iOD.setClickable(true);
                            }

                            if (et_driver_address_iOD.getText().toString().trim().length() > 0) {
                                et_driver_address_iOD.setEnabled(false);
                                et_driver_address_iOD.setClickable(false);
                            } else if (et_driver_address_iOD.getText().toString().trim().length() == 0) {
                                et_driver_address_iOD.setEnabled(true);
                                et_driver_address_iOD.setClickable(true);
                            }

                            if (et_driver_city_iOD.getText().toString().trim().length() > 0) {
                                et_driver_city_iOD.setEnabled(false);
                                et_driver_city_iOD.setClickable(false);
                            } else if (et_driver_city_iOD.getText().toString().trim().length() == 0) {
                                et_driver_city_iOD.setEnabled(true);
                                et_driver_city_iOD.setClickable(true);
                            }

                        }

                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    if (DLvalidFLG.equals("INVALID")) {
                        ShowMessage("\n Driving Licence has been Expired\n Please Add Without DL Violation\n ");
                    }

                } else {
                    tv_licence_details_header_spot.setText("DRIVING LICENCE DETAILS NOT FOUND");
                    // rl_licence_details_layout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void ShowMessage(final String Message) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView title = new TextView(mtpv_SpecialDrive.this);
                title.setText("ALERT");
                title.setBackgroundColor(Color.RED);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mtpv_SpecialDrive.this,
                        AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(Message);
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                alertDialogBuilder.setCancelable(false);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getWindow().getAttributes();

                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(28);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setGravity(Gravity.CENTER);

                Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn1.setTextSize(22);
                btn1.setTextColor(Color.WHITE);
                btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
                btn1.setBackgroundColor(Color.RED);
            }
        });


    }


    public void ShowMessageVehicleGenuine(final String Message) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView title = new TextView(mtpv_SpecialDrive.this);
                title.setText("ALERT");
                title.setBackgroundColor(Color.RED);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mtpv_SpecialDrive.this,
                        AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(Message);
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        fakeStatus="0";

                        resetData();
                        ll_3.setVisibility(View.GONE);
                        rl_detained_items.setVisibility(View.GONE);
                        ll_pending_challans.setVisibility(View.GONE);
                        btn_first_tosecond_spot.setVisibility(View.GONE);


                        ed_chasis.setText("");
                        ed_engine.setText("");
                        et_regcid_spot.setText("");
                        et_vchl_num_spot.setText("");
                        et_last_num_spot.setText("");

                    }
                });
                alertDialogBuilder.setCancelable(false);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getWindow().getAttributes();

                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(28);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setGravity(Gravity.CENTER);

                Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn1.setTextSize(22);
                btn1.setTextColor(Color.WHITE);
                btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
                btn1.setBackgroundColor(Color.RED);
            }
        });


    }



    public void ShowDailogforFake() {

        final Dialog dialog = new Dialog(mtpv_SpecialDrive.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fakenoplatealert_specialdrive);
        // dialog.setTitle("FAKE VEHICLE");
        dialog.setCancelable(false);
        // there are a lot of settings, for dialog, check them all out!
        // set up radiobutton


        rg_fakegroup=(RadioGroup)dialog.findViewById(R.id.rg_fakegroup);

        rd_fake = (RadioButton) dialog.findViewById(R.id.rd_fake);
        rd_fake.setChecked(true);
        rd_notfake = (RadioButton) dialog.findViewById(R.id.rd_notfake);


        if(rd_fake.isChecked()) {
            rd_fake.setChecked(true);
            fakeStatus="1";
        }else if(rd_notfake.isChecked())
        {
            rd_notfake.setChecked(true);
            fakeStatus="0";
        }

        rg_fakegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.rd_fake:

                        fakeStatus="1";
/*
                        sb_detained_items.append("02:VEHICLE@");
                        chck_detainedItems_vhcle.setChecked(true);*/
                        break;

                    case R.id.rd_notfake:
                        fakeStatus="0";

                     /*   chck_detainedItems_vhcle.setChecked(false);
                        sb_detained_items=new StringBuilder();

                        resetData();
                        ll_3.setVisibility(View.GONE);
                        rl_detained_items.setVisibility(View.GONE);
                        ll_pending_challans.setVisibility(View.GONE);
                        btn_first_tosecond_spot.setVisibility(View.GONE);


                        ed_chasis.setText("");
                        ed_engine.setText("");
                        et_regcid_spot.setText("");
                        et_vchl_num_spot.setText("");
                        et_last_num_spot.setText("");
*/

                        break;

                    default:
                        break;
                }
            }
        });







        Button ok_dialog=(Button)dialog.findViewById(R.id.ok_dialog);

        ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


                if(rd_fake.isChecked())
                {
                    fakeStatus="1";
                    sb_detained_items.append("02:VEHICLE@");

                    chck_detainedItems_vhcle.setChecked(true);

                    chck_detainedItems_vhcle.setEnabled(false);
                    chck_detainedItems_licence.setEnabled(false);
                    chck_detainedItems_permit.setEnabled(false);
                    chck_detainedItems_rc.setEnabled(false);
                    chck_detainedItems_none.setEnabled(false);


                    ll_3.setVisibility(View.VISIBLE);
                    rl_detained_items.setVisibility(View.VISIBLE);
                    ll_pending_challans.setVisibility(View.VISIBLE);

                    btn_first_tosecond_spot.setVisibility(View.VISIBLE);
                    et_driver_contact_spot.setVisibility(View.VISIBLE);


                    if(SpotChallan.OtpStatus.equalsIgnoreCase("Y"))
                    {
                        btn_send_otp_to_mobile.setVisibility(View.VISIBLE);
                    }else {
                        btn_send_otp_to_mobile.setVisibility(View.GONE);
                    }



                }else if(rd_notfake.isChecked())
                {
                    fakeStatus="0";

                    resetData();
                    ll_3.setVisibility(View.GONE);
                    rl_detained_items.setVisibility(View.GONE);
                    ll_pending_challans.setVisibility(View.GONE);
                    btn_first_tosecond_spot.setVisibility(View.GONE);
                    et_driver_contact_spot.setVisibility(View.GONE);
                    btn_send_otp_to_mobile.setVisibility(View.GONE);



                    ed_chasis.setText("");
                    ed_engine.setText("");
                    et_regcid_spot.setText("");
                    et_vchl_num_spot.setText("");
                    et_last_num_spot.setText("");


                }



            }
        });

        // now that the dialog is set up, it's time to show it
        dialog.show();
    }



    public class Async_getRTADetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            message = new StringBuffer();


            completeVehicle_num_send = ("" + regncode_send + "" + regnName_send + "" + vehicle_num_send);

			/*
			 * if (!(ServiceHelper.onlinebuff==null)) { ServiceHelper.onlinebuff
			 * = new StringBuffer(""); ServiceHelper.onlinebuff.delete(0,
			 * ServiceHelper.onlinebuff.length()); //v }
			 */

            ServiceHelper.getRTADetails("" + completeVehicle_num_send);
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);


            tv_vehicle_details_header_spot.setVisibility(View.VISIBLE);


            if ((ServiceHelper.rta_data != null && !ServiceHelper.rta_data.equals("0"))) {


             /*   if(isOnline())
                {
                    async_getRemarks getRemarks = new async_getRemarks();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

                        getRemarks.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        getRemarks.execute();
                    }

                }else
                {
                    showToast("Please Check Your Network Connection For Getting Remarks");
                }
*/




                try {

                    mtpv_SpecialDrive.rta_details_spot_master = new String[0];


                    mtpv_SpecialDrive.rta_details_spot_master = ServiceHelper.rta_data.split("!");

                    mtpv_SpecialDrive.Wheeler_check = rta_details_spot_master[0].split(":");

                    String Wheeler_Enable_check = mtpv_SpecialDrive.Wheeler_check[1].toString();

                /*    if (!Wheeler_Enable_check.equalsIgnoreCase("NA")) {

                        btn_wheller_code.setClickable(false);
                    } else {
                        btn_wheller_code.setClickable(true);
                    }
*/

                    // start to display details as card system
                    rl_rta_details_layout.setVisibility(View.VISIBLE);
                    tv_vehicle_details_header_spot.setText("VEHICLE DETAILS");
                    tv_vhle_no_spot.setText("" + completeVehicle_num_send);
                    tv_owner_name_spot.setText("" + rta_details_spot_master[1] != null ? rta_details_spot_master[1] : "NA");
                    tv_address_spot.setText("" + rta_details_spot_master[2] != null ? rta_details_spot_master[2] : "NA" + ", " + rta_details_spot_master[3] != null ? rta_details_spot_master[3] : "NA");
                    // tv_city_spot.setText("" + rta_details_spot_master[3]);
                    tv_maker_name_spot.setText("" + rta_details_spot_master[4] != null ? rta_details_spot_master[4] : "NA" + ", " + rta_details_spot_master[5] != null ? rta_details_spot_master[5] : "NA" + ", "
                            + rta_details_spot_master[6] != null ? rta_details_spot_master[6] : "NA");
                    // tv_maker_class_spot.setText("" + rta_details_spot_master[5]);
                    // tv_color_spot.setText("" + rta_details_spot_master[6]);
                    tv_engine_no_spot.setText("" + rta_details_spot_master[7] != null ? rta_details_spot_master[7] : "NA");
                    tv_chasis_no_spot.setText("" + rta_details_spot_master[8] != null ? rta_details_spot_master[8] : "NA");

                    if (rta_details_spot_master[0].equals("NA")) {
                        tv_vehicle_details_header_spot.setText("VEHICLE DETAILS NOT FOUND!");
                        rl_rta_details_layout.setVisibility(View.GONE);

                    } else if (rta_details_spot_master != null && rta_details_spot_master[9] != null
                            && !"NA".equals(rta_details_spot_master[9])) {

                        whlr_code_send = rta_details_spot_master[9] != null ? rta_details_spot_master[9] : "NA";// WHEELER CODE
                        if (whlr_code_send != null) {
                            btn_wheller_code.setText("" + whlr_code_send);
//                            start = System.currentTimeMillis();
//                            Log.i("TIME>>>END>>>Async_getViolations",String.valueOf(start));


                            new Async_getViolations().execute();
                        } else {
                            btn_wheller_code.setClickable(true);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if ((Dashboard.check_vhleHistory_or_Spot.equals("spot"))) {

                } else {
                    et_drivername_iOD.setText("");
                    et_driverFatherName_iOD.setText("");
                    et_driver_address_iOD.setText("");
                    et_driver_city_iOD.setText("");

                    et_drivername_iOD.setEnabled(true);
                    et_drivername_iOD.setClickable(true);

                    et_driverFatherName_iOD.setEnabled(true);
                    et_driverFatherName_iOD.setClickable(true);

                    et_driver_address_iOD.setEnabled(true);
                    et_driver_address_iOD.setClickable(true);

                    et_driver_city_iOD.setEnabled(true);
                    et_driver_city_iOD.setClickable(true);

                    ll_drivertype_rgbtn.setVisibility(View.GONE);
                    ll_is_owner_driver.setVisibility(View.VISIBLE);
                }

                tv_vehicle_details_header_spot.setText("VEHICLE DETAILS NOT FOUND!");
                rl_rta_details_layout.setVisibility(View.GONE);
                // showToast("No Details Found !");
            }
        }
    }


    /* TO GET PENDING CHALLANS BY VECHILE NUMBER */
    public class Async_getPendingChallans extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String pidCd = sharedPreference.getString("PID_CODE", "");
            String pidName = sharedPreference.getString("PID_NAME", "");
            String pswd = sharedPreference.getString("PASS_WORD", ""); // PASS_WORD

            ServiceHelper.getpendingChallansByRegNo("" + completeVehicle_num_send, "" + "", "" + "", "" + pidCd,
                    "" + pidName, "" + pswd, "" + simid_send, "" + imei_send, "" + latitude, "" + longitude, "", "23");
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("unused")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

//            end = System.currentTimeMillis();
//            Log.i("TIME>>>END>>>Async_getPendingChallans",String.valueOf(end));
//
//            res=end-start;
//            Log.i("TIME>>>Async_getPendingChallans :  Taken in seconds>> ", String.valueOf(res/1000));

            if (ServiceHelper.Opdata_Chalana != null && !"nodata".equals(ServiceHelper.Opdata_Chalana)) {

                if ((!ServiceHelper.Opdata_Chalana.equals("0")) && (ServiceHelper.pending_challans_master.length > 0)) {
                    // startActivity(new Intent(getApplicationContext(),
                    // PendingChallans.class));
                    for (int i = 0; i < ServiceHelper.pending_challans_details.length; i++) {
                        total_amount = total_amount
                                + (Double.parseDouble(ServiceHelper.pending_challans_details[i][7].toString().trim()));
                    }
                    tv_total_pending_challans.setText("" + ServiceHelper.pending_challans_details.length);
                    tv_toal_amount_pending_challans.setText("" + total_amount);
                    // grand_total : max violation amount
                    Double total = 0.0;
                    total = total_amount + grand_total;

                    mtpv_SpecialDrive.tv_grand_total_spot.setText("");
                } else if (ServiceHelper.Opdata_Chalana.equals("0")) {
                    total_amount = 0;// violation amount
                    showToast("No Pending Challans");
                    tv_total_pending_challans.setText("0");
                    tv_toal_amount_pending_challans.setText("0");
                    mtpv_SpecialDrive.tv_grand_total_spot.setText("");
                    // tv_grand_total_spot.setText("Rs . " + grand_total);
                }
            } else {
                total_amount = 0;
                //  showToast("Try Again!");
                mtpv_SpecialDrive.tv_grand_total_spot.setText("");
                // tv_grand_total_spot.setText("Rs . " + grand_total);
            }
        }
    }


    public class Async_getAadharDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                if (licence_no!=null && !licence_no.trim().equals("")) {
                    ServiceHelper.onlinebuff = ServiceHelper.onlinebuff.append("3" + "!" + "NA" + "^");
                }

                if(aadhaar!=null && aadhaar.trim().length()>=10) {
                    ServiceHelper.getAadharDetails("" + aadhaar.trim(), "");
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

//            end = System.currentTimeMillis();
//            Log.i("TIME>>>END>>>Async_getAadharDetails",String.valueOf(end));
//
//            res=end-start;
//            Log.i("TIME>>>Async_getAadharDetails :  Taken in seconds>> ", String.valueOf(res/1000));


            if (!"null".equals(ServiceHelper.aadhar_data) && !"0".equals(ServiceHelper.aadhar_data) && ServiceHelper.aadhar_details.length > 0) {

                ll_aadhar_layout.setVisibility(View.VISIBLE);
                tv_aadhar_header.setVisibility(View.VISIBLE);

                String date_birth = "";
                String service_year = "";
                tv_aadhar_header.setText("AADHAR DETAILS");
                if (!"NA".equals(ServiceHelper.aadhar_data)) {

                    Log.i("AAdhar ........", "Card");

                    try {
                        tv_aadhar_user_name.setText("" + ServiceHelper.aadhar_details[0] != null
                                ? ServiceHelper.aadhar_details[0].trim().toUpperCase() : "");
                        tv_aadhar_care_off.setText("" + (!ServiceHelper.aadhar_details[1].equalsIgnoreCase("0")
                                ? ServiceHelper.aadhar_details[1].trim().toUpperCase() : "NA"));
                        tv_aadhar_address.setText(""
                                + (!ServiceHelper.aadhar_details[2].equalsIgnoreCase("0")
                                ? ServiceHelper.aadhar_details[2].trim().toUpperCase() : "")
                                + ", "
                                + (!ServiceHelper.aadhar_details[3].equalsIgnoreCase("0")
                                ? ServiceHelper.aadhar_details[3].trim().toUpperCase() + ", " : "")
                                + (!ServiceHelper.aadhar_details[4].equalsIgnoreCase("0")
                                ? ServiceHelper.aadhar_details[4].trim().toUpperCase() + ", " : "")
                                + (!ServiceHelper.aadhar_details[5].equalsIgnoreCase("0")
                                ? ServiceHelper.aadhar_details[5].trim().toUpperCase() + ", " : "")
                                + (!ServiceHelper.aadhar_details[6].equalsIgnoreCase("0")
                                ? ServiceHelper.aadhar_details[6].trim().toUpperCase() + ", " : "")
                                + (!ServiceHelper.aadhar_details[7].equalsIgnoreCase("0")
                                ? ServiceHelper.aadhar_details[7].trim().toUpperCase() + ", " : ""));
                        tv_aadhar_mobile_number.setText("" + (!ServiceHelper.aadhar_details[8].equalsIgnoreCase("0")
                                ? ServiceHelper.aadhar_details[8].trim().toUpperCase() : "NA"));
                        tv_aadhar_gender.setText("" + (!ServiceHelper.aadhar_details[9].equalsIgnoreCase("0")
                                ? ServiceHelper.aadhar_details[9].trim().toUpperCase() : "NA"));

                        date_birth = "" + ServiceHelper.aadhar_details[10] != null ? ServiceHelper.aadhar_details[10] : "";

                        String[] split_dob = date_birth.split("\\/");
                        service_year = "" + split_dob[2];

                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                        date_birth = "0";
                        service_year = "0";
                    }
                }


                int final_age = present_year - Integer.parseInt(service_year);
                tv_aadhar_dob.setText("" + final_age);
                tv_aadhar_uid.setText("" + (!ServiceHelper.aadhar_details[11].equalsIgnoreCase("0")
                        ? ServiceHelper.aadhar_details[11].trim().toUpperCase() : "NA"));


                if (ServiceHelper.aadhar_details.length > 0
                        && et_driver_lcnce_num_spot.getText().toString().equals("")) {

                    et_drivername_iOD.setText("" + ServiceHelper.aadhar_details[0] != null
                            ? ServiceHelper.aadhar_details[0].trim().toUpperCase() : "");
                    et_driverFatherName_iOD.setText("" + (!ServiceHelper.aadhar_details[1].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[1].trim().toUpperCase() : "NA"));
                    et_driver_address_iOD.setText(""
                            + (!ServiceHelper.aadhar_details[2].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[2].trim().toUpperCase() : "")
                            + ","
                            + (!ServiceHelper.aadhar_details[3].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[3].trim().toUpperCase() + "," : "")
                            + (!ServiceHelper.aadhar_details[4].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[4].trim().toUpperCase() + "," : "")
                            + (!ServiceHelper.aadhar_details[5].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[5].trim().toUpperCase() + "," : "")
                            + (!ServiceHelper.aadhar_details[6].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[6].trim().toUpperCase() + "," : "")
                            + (!ServiceHelper.aadhar_details[7].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[7].trim().toUpperCase() + "" : ""));
                    Compleate_address = "" + ""
                            + (!ServiceHelper.aadhar_details[2].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[2].trim().toUpperCase() : "")
                            + ","
                            + (!ServiceHelper.aadhar_details[3].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[3].trim().toUpperCase() + "," : "")
                            + (!ServiceHelper.aadhar_details[4].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[4].trim().toUpperCase() + "," : "")
                            + (!ServiceHelper.aadhar_details[5].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[5].trim().toUpperCase() + "," : "")
                            + (!ServiceHelper.aadhar_details[6].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[6].trim().toUpperCase() + "," : "")
                            + (!ServiceHelper.aadhar_details[7].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[7].trim().toUpperCase() + "" : "");
                    et_driver_city_iOD.setText((!ServiceHelper.aadhar_details[6].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[6].trim().toUpperCase() + "" : ""));

                    if (et_drivername_iOD.getText().toString().trim().length() > 0) {
                        et_drivername_iOD.setEnabled(false);
                        et_drivername_iOD.setClickable(false);
                    } else if (et_drivername_iOD.getText().toString().trim().length() == 0) {
                        et_drivername_iOD.setEnabled(true);
                        et_drivername_iOD.setClickable(true);
                    }

                    if (et_driverFatherName_iOD.getText().toString().trim().length() > 0) {
                        et_driverFatherName_iOD.setEnabled(false);
                        et_driverFatherName_iOD.setClickable(false);
                    } else if (et_driverFatherName_iOD.getText().toString().trim().length() == 0) {
                        et_driverFatherName_iOD.setEnabled(true);
                        et_driverFatherName_iOD.setClickable(true);
                    }

                    if (et_driver_address_iOD.getText().toString().trim().length() > 0) {
                        et_driver_address_iOD.setEnabled(false);
                        et_driver_address_iOD.setClickable(false);
                    } else if (et_driver_address_iOD.getText().toString().trim().length() == 0) {
                        et_driver_address_iOD.setEnabled(true);
                        et_driver_address_iOD.setClickable(true);
                    }

                    if (et_driver_city_iOD.getText().toString().trim().length() > 0) {
                        et_driver_city_iOD.setEnabled(false);
                        et_driver_city_iOD.setClickable(false);
                    } else if (et_driver_city_iOD.getText().toString().trim().length() == 0) {
                        et_driver_city_iOD.setEnabled(true);
                        et_driver_city_iOD.setClickable(true);
                    }
                }
                if (ServiceHelper.aadhar_details[13].toString().trim().equals("0")) {
                    img_aadhar_image.setImageResource(R.drawable.photo);
                } else {
                    try {
                        byte[] decodestring = Base64.decode("" + ServiceHelper.aadhar_details[13].toString().trim(),
                                Base64.DEFAULT);
                        Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                        img_aadhar_image.setImageBitmap(decocebyte);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String Qrdata = "AADHAAAR NUMBER:" + " " + et_aadharnumber_spot.getText().toString().trim() + "\n"
                        + "NAME:" + " " + ServiceHelper.aadhar_details[0] + "\n" + "FATHER NAME:" + " "
                        + ServiceHelper.aadhar_details[1] + "\n" + "AGE:" + " " + final_age + "\n" + "GENDER:" + " "
                        + ServiceHelper.aadhar_details[9] + "\n" + "ADDRESS:" + " " + "" + Compleate_address;
                try {
                    Bitmap bitmap = encodeAsBitmap(Qrdata);
                    qr_code.setImageBitmap(bitmap);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else if (ServiceHelper.aadhar_details.length == 0) {

                tv_aadhar_header.setVisibility(View.VISIBLE);
                tv_aadhar_header.setText("AADHAR DETAILS NOT FOUND!");
                ll_aadhar_layout.setVisibility(View.GONE);
                et_drivername_iOD.setText("");
                et_driverFatherName_iOD.setText("");
                et_driver_address_iOD.setText("");
                et_driver_city_iOD.setText("");

                et_drivername_iOD.setEnabled(true);
                et_drivername_iOD.setClickable(true);

                et_driverFatherName_iOD.setEnabled(true);
                et_driverFatherName_iOD.setClickable(true);

                et_driver_address_iOD.setEnabled(true);
                et_driver_address_iOD.setClickable(true);

                et_driver_city_iOD.setEnabled(true);
                et_driver_city_iOD.setClickable(true);

            }

        }
    }

    public void ShowMessageDateAlert(final String Message) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView title = new TextView(mtpv_SpecialDrive.this);
                title.setText("ALERT");
                title.setBackgroundColor(Color.RED);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mtpv_SpecialDrive.this,
                        AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(Message);
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        dob_input.setText("Select Date of Birth");
                        dobcheck = "No";
                        dob_DL = null;


                    }
                });
                alertDialogBuilder.setCancelable(false);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getWindow().getAttributes();

                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(28);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setGravity(Gravity.CENTER);

                Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn1.setTextSize(22);
                btn1.setTextColor(Color.WHITE);
                btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
                btn1.setBackgroundColor(Color.RED);
            }
        });


    }


    @SuppressWarnings("unused")
    @SuppressLint({"ResourceAsColor", "CutPasteId"})
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {

            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "", true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);
                return pd;

            default:
                break;

         /*   case FAKE_NUMBERPLATE_DIALOG:


                message = new StringBuffer();


                if (aadhr_point_frm_json != null && dl_point_frm_json != null && !aadhr_point_frm_json.equals("NA")
                        && !dl_point_frm_json.equals("NA")) {

                    message.append("VIOLATION BASED POINTS \n");
                    message.append("------------------------------\n");
                    message.append("AADHAR POINTS \t: " + aadhr_point_frm_json + "\n");
                    message.append("OFFENCE YEAR \t: " + aadhaar_offence_year + "\n\n");
                    message.append("DL POINTS \t: " + dl_point_frm_json + "\n");
                    message.append("OFFENCE YEAR \t: " + dl_offence_year + "\n\n");
                    message.append("" + rta_details_spot_master[10]);

                } else if (aadhr_point_frm_json != null && !aadhr_point_frm_json.equals("NA")) {
                    message.append("VIOLATION BASED POINTS \n");
                    message.append("------------------------------\n");
                    message.append("AADHAR POINTS \t: " + aadhr_point_frm_json + "\n");
                    message.append("OFFENCE YEAR \t: " + aadhaar_offence_year + "\n\n");
                    message.append("" + rta_details_spot_master[10]);

                } else if (dl_point_frm_json != null && !dl_point_frm_json.equals("NA")) {
                    // message.append("VIOLATION BASED POINTS \n");
                    message.append("VIOLATION BASED POINTS \n");
                    message.append("------------------------------\n");
                    message.append("DL POINTS \t: " + dl_point_frm_json + "\n");
                    message.append("OFFENCE YEAR \t: " + dl_offence_year + "\n\n");
                    message.append("" + rta_details_spot_master[10]);

                } else {
                    message.append("" + offender_remarks_resp_master[10]);
                }


                TextView title2 = new TextView(this);
                title2.setText("REMARKS");
                title2.setBackgroundColor(Color.RED);
                title2.setGravity(Gravity.CENTER);
                title2.setTextColor(Color.WHITE);
                title2.setTextSize(26);
                title2.setTypeface(title2.getTypeface(), Typeface.BOLD);
                title2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title2.setPadding(20, 0, 20, 0);
                title2.setHeight(70);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title2);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        removeDialog(FAKE_NUMBERPLATE_DIALOG);

                        if (offender_remarks_resp_master[10] != null
                                && offender_remarks_resp_master[10].contains("FAKE NO")) {

                            fake_veh_chasisNo = (offender_remarks_resp_master.length > 0
                                    && offender_remarks_resp_master[8] != null
                                    && !"".equals(offender_remarks_resp_master[8].trim()))
                                    ? offender_remarks_resp_master[8]
                                    .substring(offender_remarks_resp_master[8].length() - 5)
                                    : "";
                            Intent intent = new Intent(getApplicationContext(), Fake_NO_Dialog.class);
                            intent.putExtra("Flagkey", "S");
                            startActivity(intent);
                        }
                    }
                });
                alertDialogBuilder.setCancelable(false);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.getWindow().getAttributes();

                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(28);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setGravity(Gravity.CENTER);

                Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn1.setTextSize(22);
                btn1.setTextColor(Color.WHITE);
                btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
                btn1.setBackgroundColor(Color.RED);
                return alertDialog;

*/

            case OTP_CNFRMTN_DIALOG:

                String otp_message = "\n" + otp_msg + "\n";

                TextView title3 = new TextView(this);
                title3.setText("ALERT");
                title3.setBackgroundColor(Color.RED);
                title3.setGravity(Gravity.CENTER);
                title3.setTextColor(Color.WHITE);
                title3.setTextSize(26);
                title3.setTypeface(title3.getTypeface(), Typeface.BOLD);
                title3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title3.setPadding(20, 0, 20, 0);
                title3.setHeight(70);

                AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
                alertDialog_Builder.setCustomTitle(title3);
                alertDialog_Builder.setIcon(R.drawable.dialog_logo);
                alertDialog_Builder.setMessage(otp_message);
                alertDialog_Builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        removeDialog(OTP_CNFRMTN_DIALOG);
                    }
                });
                alertDialog_Builder.setCancelable(false);

                AlertDialog alert_Dialog = alertDialog_Builder.create();
                alert_Dialog.show();
                alert_Dialog.getWindow().getAttributes();

                TextView textView2 = (TextView) alert_Dialog.findViewById(android.R.id.message);
                textView2.setTextSize(28);
                textView2.setGravity(Gravity.CENTER);
                textView2.setTypeface(textView2.getTypeface(), Typeface.BOLD);

                Button btn2 = alert_Dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn2.setTextSize(22);
                btn2.setTextColor(Color.WHITE);
                btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
                btn2.setBackgroundColor(Color.RED);
                return alert_Dialog;


        }

        return super.onCreateDialog(id);

    }


    public void getLocation() {

        try {
            m_locationlistner = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = m_locationlistner.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = m_locationlistner.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                latitude = 0.0;
                longitude = 0.0;
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (m_locationlistner != null) {
                        location = m_locationlistner.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        } else {
                            latitude = 0.0;
                            longitude = 0.0;
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {

                    if (location == null) {
                        m_locationlistner.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (m_locationlistner != null) {
                            location = m_locationlistner.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            } else {
                                latitude = 0.0;
                                longitude = 0.0;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei_send = telephonyManager.getDeviceId();// TO GET IMEI NUMBER
        if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            simid_send = "" + telephonyManager.getSimSerialNumber();
        } else {
            simid_send = "";
        }

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        macAddress = wInfo.getMacAddress();

    }



    /* TO GET VIOLATIONS */
    public class Async_getViolations extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            ServiceHelper.getViolationDetails("" + whlr_code_send);

            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if (ServiceHelper.violation_detailed_views!=null && ServiceHelper.violation_detailed_views.length > 0) {
                violation_list.removeAll(violation_list);
                violation_offence_Code.removeAll(violation_offence_Code);
                violation_section.removeAll(violation_section);
                violation_description.removeAll(violation_description);
                violation_min_amount.removeAll(violation_min_amount);
                violation_avg_amount.removeAll(violation_avg_amount);
                violation_max_amount.removeAll(violation_max_amount);
                violation_rg_ids.remove(violation_rg_ids);

                for (int i = 0; i < ServiceHelper.violation_detailed_views.length; i++) {

                    violation_list.add("" + ServiceHelper.violation_detailed_views[i][2].toString().trim() + "("
                            + ServiceHelper.violation_detailed_views[i][1].toString().trim() + ") " + " Rs:"
                            + ServiceHelper.violation_detailed_views[i][4].toString().trim());
                    violation_offence_Code.add("" + ServiceHelper.violation_detailed_views[i][0].toString().trim());
                    violation_section.add("" + ServiceHelper.violation_detailed_views[i][1].toString().trim());
                    violation_description.add("" + ServiceHelper.violation_detailed_views[i][2].toString().trim());
                    violation_min_amount.add("" + ServiceHelper.violation_detailed_views[i][3].toString().trim());
                    violation_max_amount.add("" + ServiceHelper.violation_detailed_views[i][4].toString().trim());
                    violation_avg_amount.add("" + ServiceHelper.violation_detailed_views[i][5].toString().trim());
                }
            }else {
                showToast("Violation Data Not Found");
            }

        }
    }



    /* DETAINED ITEMS LISTENERS */
    protected void getDateAndTime() {
        // TODO Auto-generated method stub
        calendar = Calendar.getInstance();

        present_date = calendar.get(Calendar.DAY_OF_MONTH);
        present_month = calendar.get(Calendar.MONTH);
        present_year = calendar.get(Calendar.YEAR);

        date_format = new SimpleDateFormat("dd-MMM-yyyy");//

        present_date_toSend = date_format.format(new Date(present_year - 1900, present_month, present_date));

		/* TIME START */

        present_hour = calendar.get(Calendar.HOUR_OF_DAY);
        present_minutes = calendar.get(Calendar.MINUTE);

        present_time_toSend = new StringBuffer();
        present_time_toSend.delete(0, present_time_toSend.length());

        if (present_hour < 10) {
            present_time_toSend.append("0").append(present_hour);
        } else {
            present_time_toSend.append(present_hour);
        }
        present_time_toSend.append(":");

        if (present_minutes < 10) {
            present_time_toSend.append("0").append(present_minutes);
        } else {
            present_time_toSend.append(present_minutes);
        }
		/* TIME END */
    }



    Bitmap encodeAsBitmap(String str) throws WriterException {

        BitMatrix result;

        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, width, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        return bitmap;
    }



    public class async_insert_DetainItems extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {

            String result= ServiceHelper.insertDetainItems("NA","NA",
                    completeVehicle_num_send,new DateUtil().getTodaysDate(),sb_detained_items.toString().trim()
                    ,new DateUtil().getTodaysDate(),new DateUtil().getPresentTime(),pidCode,pidName,"", Dashboard.UNIT_CODE,
                    Dashboard.UNIT_NAME,
                    engine!=null?engine:ed_engine.getText().toString()
                    ,chasis!=null?chasis:ed_chasis.getText().toString()
                    ,fakeStatus,licence_no!=null?licence_no:et_driver_lcnce_num_spot.getText().toString(),
                    aadhaar!=null?aadhaar:et_aadharnumber_spot.getText().toString(),psName,point_name_send_from_settings,
                    ServiceHelper.onlinebuff.toString());

            return result;
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(current_version);
            removeDialog(PROGRESS_DIALOG);

            if(result!=null && result!="2")
            {

                if(result.equalsIgnoreCase("0"))
                {
                    showToast("Detain Items Failed");
                }else{

                    resetData();
                    ed_chasis.setText("");
                    ed_engine.setText("");
                    et_regcid_spot.setText("");
                    et_vchl_num_spot.setText("");
                    et_last_num_spot.setText("");
                    ll_3.setVisibility(View.GONE);
                    rl_detained_items.setVisibility(View.GONE);
                    ll_pending_challans.setVisibility(View.GONE);
                    btn_first_tosecond_spot.setVisibility(View.GONE);


                    mtpv_SpecialDrive.specialDriveData=result.trim().toString();
                    mtpv_SpecialDrive.specialDrive="SPDRIVE";

                    showToast("Ticket Generated Successfully");
                    Intent print = new Intent(getApplicationContext(), SpecialDriveResponse_Print.class);
                    startActivity(print);
                    finish();
                }
            }
            else
            {
                showToast("Detain Items Failed");
            }


        }
    }


    public class async_validate_Regno extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {





            String result= ServiceHelper.validateRegno(completeVehicle_num_send!=null?completeVehicle_num_send:""
                    ,licence_no!=null?licence_no:et_driver_lcnce_num_spot.getText().toString(),
                    aadhaar!=null?aadhaar:et_aadharnumber_spot.getText().toString(),
                    engine!=null?engine:ed_engine.getText().toString()
                    ,chasis!=null?chasis:ed_chasis.getText().toString(),pidCode,
                    Dashboard.UNIT_CODE, MainActivity.IMEI, MainActivity.sim_No,String.valueOf(MainActivity.latitude),
                    String.valueOf(MainActivity.longitude),pidName);

            return result;
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(current_version);
            removeDialog(PROGRESS_DIALOG);

            if(result!=null && result!="2")
            {
                if(result.equalsIgnoreCase("0"))
                {

                    //Vehicle Detained checked and Non editable

                    // showToast("Detain the Vehicle");



                    // ShowMessage("It is a Fake Regn Number."+"\n"+" Please Detain The Vehicle");

                    ShowDailogforFake();

                }else if(result.equalsIgnoreCase("1")){



                    ShowMessageVehicleGenuine("Vehicle Is Genuine");


                }




            }


        }
    }


    public class async_getRemarks extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {





            String result= ServiceHelper.getRemarks(completeVehicle_num_send,"NA","NA");

            return result;
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(current_version);
            removeDialog(PROGRESS_DIALOG);

            if(result!=null && result!="2")
            {
                if(result.equalsIgnoreCase("0"))
                {
                    ShowMessage("No Prevoius Remarks Found");

                }else {
                    ShowMessage(result.toString());
                }



            }


        }
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        switch (buttonView.getId()) {

            case R.id.checkBox_dt_rc_spotchallantwo_xml:
                if (isChecked) {
                    chck_detainedItems_rc.setChecked(true);
                } else {
                    chck_detainedItems_rc.setChecked(false);
                }
                break;

            case R.id.checkBox_dt_vchle_spotchallantwo_xml:
                if (isChecked) {
                    chck_detainedItems_vhcle.setChecked(true);
                } else {
                    chck_detainedItems_vhcle.setChecked(false);
                }
                break;

            case R.id.checkBox_dt_lcns_spotchallantwo_xml:
                if (isChecked) {
                    chck_detainedItems_licence.setChecked(true);
                } else {
                    chck_detainedItems_licence.setChecked(false);
                }
                break;

            case R.id.checkBox_dt_permit_spotchallantwo_xml:
                if (isChecked) {
                    chck_detainedItems_permit.setChecked(true);
                } else {
                    chck_detainedItems_permit.setChecked(false);
                }
                break;

            case R.id.checkBox_dt_none_spotchallantwo_xml:
                if (isChecked) {
                    setCheckedValues(false, "donotedit");
                    chck_detainedItems_none.setChecked(true);
                } else {
                    setCheckedValues(true, "canedit");
                    chck_detainedItems_none.setChecked(false);
                }
                break;
        }
    }


    /* TO SET NON-EDITABLE AND TO CLEAR */
    private void setCheckedValues(boolean b_val, String edit_val) {
        // TODO Auto-generated method stub

        chck_detainedItems_rc.setEnabled(b_val);
        chck_detainedItems_vhcle.setEnabled(b_val);
        chck_detainedItems_licence.setEnabled(b_val);
        chck_detainedItems_permit.setEnabled(b_val);

        if (edit_val.equals("donotedit")) {
            chck_detainedItems_rc.setChecked(b_val);
            chck_detainedItems_vhcle.setChecked(b_val);
            chck_detainedItems_licence.setChecked(b_val);
            chck_detainedItems_permit.setChecked(b_val);
        }
    }

    public void resetData()
    {
        ll_drivertype_rgbtn.setVisibility(View.GONE);
        ll_is_owner_driver.setVisibility(View.GONE);
        rl_rta_details_layout.setVisibility(View.GONE);

        et_driver_contact_spot.setText("");

        chck_detainedItems_rc.setChecked(false);
        chck_detainedItems_vhcle.setChecked(false);
        chck_detainedItems_licence.setChecked(false);
        chck_detainedItems_permit.setChecked(false);


        radioGroupButton_isDriver.setChecked(true);

        tv_vehicle_details_header_spot.setText("");
        tv_licence_details_header_spot.setText("");
        soldOut=0;
        fakeStatus="";
        SpotChallan.OtpStatus="";
        SpotChallan.OtpResponseDelayTime="";
        // sold_out_string=-1;

        ServiceHelper.rc_send="";
        ServiceHelper.license_data="";
        ServiceHelper.aadhar_data="";
        sb_detained_items=new StringBuilder();

        tv_vhle_no_spot.setText("");
        tv_owner_name_spot.setText("");
        tv_address_spot.setText("");
        tv_maker_name_spot.setText("");
        tv_chasis_no_spot.setText("");
        tv_engine_no_spot.setText("");
        dl_no.setText("");
        tv_licnce_ownername_spot.setText("");
        tv_lcnce_father_name_spot.setText("");
        tv_lcnce_address_spot.setText("");
        tv_lcnce_phone_number_spot.setText("");
        tv_dlpoints_spotchallan_xml.setText("");
        licence_image.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile_img));
        tv_aadhar_user_name.setText("");
        tv_aadhar_care_off.setText("");
        tv_aadhar_dob.setText("");
        tv_aadhar_gender.setText("");
        tv_aadhar_address.setText("");
        tv_aadhar_mobile_number.setText("");
        tv_aadhar_uid.setText("");
        img_aadhar_image.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile_img));

        ServiceHelper.license_data = "";
        SpotChallan.licence_details_spot_master = new String[0];

        // initial status of vehicle history and fake number plate details
        VehicleHistoryPendingChallans.total_amount_selected_challans = 0.0;
        Fake_NO_Dialog.fake_action = null;

        tv_total_pending_challans.setText("0");
        tv_toal_amount_pending_challans.setText("0");
        //    tv_violation_amnt.setText("Rs . " + grand_total + "");
        tv_grand_total_spot.setText("");

        // FOR DISPLAYING TOTAL CALCULATED AMOUNT
        tv_grand_total_spot.setText("");
        tv_violation_amnt.setText("");
    }


    /*---------------SEND OTP TO MOBILE start---------*/
    public class Async_sendOTP_to_mobile extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                otp_status = "send";

                date_format2 = new SimpleDateFormat("dd-MMM-yyyy");
                present_date_toSend = date_format2.format(new Date(present_year - 1900, present_month, present_date));
            } catch (Exception e) {
                e.printStackTrace();
                otp_status = "send";
                present_date_toSend = "";

            }

            ServiceHelper.sendOTPtoMobile(completeVehicle_num_send, et_driver_contact_spot.getText().toString().trim(),
                    "" + getDate().toUpperCase(),"");
            return null;
        }

        @Override
        protected void onPreExecute() {

            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            otp_msg = "";
            otpValue = "";
            if (otp_status.equals("send")) {
                if (ServiceHelper.Opdata_Chalana.toLowerCase().equals("na")) {

                } else {
                    showToast("OTP is sent to your mobile number");
                    otpValue = "" + ServiceHelper.Opdata_Chalana;

                    Intent dialogbox = new Intent(getApplicationContext(), OTP_input.class);
                    dialogbox.putExtra("regNO", completeVehicle_num_send);
                    dialogbox.putExtra("MobileNo", et_driver_contact_spot.getText().toString().trim());
                    dialogbox.putExtra("otp_date", "" + getDate().toUpperCase());
                    dialogbox.putExtra("OTP_value", otpValue);

                    startActivity(dialogbox);
                }
            }
        }
    }


    public class Async_VerifyOTP extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            otp_status = "verify";


            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if (otp_status.equals("verify")) {

                if ((ServiceHelper.Opdata_Chalana.toLowerCase().equals("0"))
                        || (ServiceHelper.Opdata_Chalana.toLowerCase().equals("na"))) {

                    otp_msg = "Entered OTP is wrong,\nPlease Click on SendOTP once Again ";
                    otp_status = "send";
                    removeDialog(OTP_CNFRMTN_DIALOG);
                    showDialog(OTP_CNFRMTN_DIALOG);
                    otp_popup();

                } else {
                    otp_msg = "OTP is verified";
                    removeDialog(OTP_CNFRMTN_DIALOG);
                    showDialog(OTP_CNFRMTN_DIALOG);
                }
            }
        }
    }

    public static String getDate() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        System.out.println(today.month);
        return today.monthDay + "-" + getMonthName(today.month) + "-" + today.year;
    }

    public static String getMonthName(int month) {

        switch (month + 1) {

            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
    }

    public void otp_popup() {
        // TODO Auto-generated method stub
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.activity_otp_input, null);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.otp_input);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mtpv_SpecialDrive.this,
                AlertDialog.THEME_HOLO_LIGHT);
        // alertDialogBuilder.setCustomTitle(title);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setIcon(R.drawable.dialog_logo);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (userInput.getText().toString().equals("")) {
                    showToast("Please Enter OTP");
                } else {


                    if (userInput.getText().toString().equals("" + otpValue)) {
                        otp_number = userInput.getText().toString();
                        vStatusConfirmationYN = "Y";
                        if (isOnline()) {

                            otp_status = "verify";
                            new Async_VerifyOTP().execute();
                        } else {
                            showToast("Please check your network connection!");
                        }
                    } else {
                        vStatusConfirmationYN = "N";
                        if (isOnline()) {
                            otp_status = "verify";
                            new Async_VerifyOTP().execute();
                        } else {
                            showToast("Please check your network connection!");
                        }
                    }
                }

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().getAttributes();

        Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btn.setTextSize(22);
        btn.setTextColor(Color.WHITE);
        btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
        btn.setBackgroundColor(Color.RED);
    }


    public class Async_getOtpStatusNTime extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {



            String rtaApproverResponse = ServiceHelper.getOtpStatusNTime(Dashboard.UNIT_CODE);

            return rtaApproverResponse;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onPostExecute(String result) {

            removeDialog(PROGRESS_DIALOG);

            if (result != null && !result.equalsIgnoreCase("") && !result.equalsIgnoreCase("NA|NA")) {


                try {

                    otp_Master = new String[0];

                    otp_Master = result.split("\\|");

                    SpotChallan.OtpStatus=otp_Master[0].toString()!= null ? otp_Master[0].toString().trim() : "N";
                    SpotChallan.OtpResponseDelayTime=otp_Master[1].toString()!= null ? otp_Master[1].toString().trim(): "0";


                } catch (Exception e) {
                    e.printStackTrace();
                    SpotChallan.OtpStatus="N";
                    SpotChallan.OtpResponseDelayTime="0";

                }

            }

        }

    }

}
