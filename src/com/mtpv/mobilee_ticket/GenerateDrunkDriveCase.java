package com.mtpv.mobilee_ticket;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mtpv.imagematch.ImageMatchAdapter;
import com.mtpv.imagematch.ImageMatchPojo;
import com.mtpv.imagematch.Results;
import com.mtpv.imagematch.Telangana;
import com.mtpv.imagematch.Touch;
import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.ServiceHelper;
import com.mtpv.mobilee_ticket_services.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import mother.com.test.PidSecEncrypt;

@SuppressWarnings("deprecation")
@SuppressLint({"NewApi", "SetJavaScriptEnabled", "DefaultLocale"})
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class GenerateDrunkDriveCase extends Activity implements OnClickListener, LocationListener,
        OnItemSelectedListener, android.widget.CompoundButton.OnCheckedChangeListener {

    TextView tv_login_username;
    TextView tv_ticket_generate;
    TextView tv_settings;
    ImageButton ibtn_logout;

    LinearLayout ll_verify;

    final int OFFENCE_DATE = 1;
    final int OFFENCE_TIME_PICKER = 2;
    final int PROGRESS_DIALOG = 4;
    final int OCCUPATION_DIALOG = 6;
    final int BAR_DIALOG = 16;
    final int QUALIFICATION_DIALOG = 7;
    final int THIRD_SCREEN = 8;
    final int VECHILE_CATEGORY_DIALOG = 9;
    final int VECHILE_MAIN_CATEGORY_DIALOG = 10;
    final int VECHILE_SUB_CATEGORY_DIALOG = 11;
    final int ID_PROOF_DIALOG = 12;
    final int ERROR_DIALOG = 13;
    final int OTP_CNFRMTN_DIALOG = 14;

    public static boolean liquorFLG = false, professionFLG = false;

    /* FOR GENERATING E-TICKET */
    /* FOR REG.NO START */
    EditText et_regn_cid;
    EditText et_regn_cid_name;
    EditText et_regn_last_num;
    /* FOR REG.NO END */

    /* START OFF FIRST SCREEEN */
    EditText et_owner_dl_no;
    EditText et_driver_dl_no;
    EditText et_driver_name;
    EditText et_driver_fname;
    EditText et_driver_contact_no;
    EditText et_id_proof;

    EditText et_alcohol_reading;
    EditText et_age;
    EditText edt_identification_mark;
    EditText et_check_sino;
    EditText et_address;
    EditText et_city;
    EditText et_verify_otp_from_mobile;
    EditText et_dd_address;
    EditText et_dd_city;

    /* END FIRST SCREEEN */

    //RadioGroup radiogrp_finedBy;// radiogroup for court or policesatation
    //RadioButton radio_finedBy_subcat;// court or policesatation

    RadioGroup radiogrp_gender;
    RadioButton radio_male_female;

    /*
     * CheckBox chck_detainedItems_rc; CheckBox chck_detainedItems_vhcle;
     * CheckBox chck_detainedItems_licence; CheckBox chck_detainedItems_permit;
     */

    CheckBox chck_detainedItems_rc;
    CheckBox chck_detainedItems_vhcle;
    CheckBox chck_detainedItems_licence;
    CheckBox chck_detainedItems_permit;

    Button btn_offence_date;
    Button btn_offence_time;
    Button btn_idproof;
    Button btn_cancel;
    Button btn_next;
    Button btn_cancel_final;
    Button btn_final_submittion;
    public static Button btn_send_otp_to_mobile;
    Button btn_verify_otp_from_mobile;

    String[] id_proof_arr = {"Aadhar Number", "Pancard Number", "Passport Number", "VoterId Number"};

    String[] id_proof_hints_arr = {"Enter Aadhar Number", "Enter Pancard Number", "Enter Passport Number",
            "Enter VoterId Number"};

    int selected_finedBy_sub;// for getting finedby value by radio_group
    int val_gender;// for getting male or female value by radio_group

    /* FOR OFFENCE_DATE */
    int offnce_year, offnce_month, offnce_day;// for offence date
    int cnslng_year, cnslng_month, cnslng_day;// for counselling date
    Calendar cal;
    SimpleDateFormat format;

    /* FOR TIME PICKER */
    String hour, minute;
    String date_from;// like offence_date and counselling_date

    Context context;
    int selected_qlfctn = -1;
    int selected_ocuptn = -1;
    int selected_bar = -1;
    int selected_vchle_cat = -1;
    int selected_vchle_MainCat = -1;
    int selected_vchle_SubCat = -1;
    int selected_id_proof = -1;

    String bar_title = "Select Liquor Seller Type";
    String ocuptn_title = "Select Occupation";
    String qlfctn_title = "Select Qualification";
    String vchle_cat_title = "Select Vechile Category";
    String vchle_Maincat_title = "Select Vechile MainCategory";
    String vchle_Subcat_title = "Select Vechile SubCategory";

    /* FOR IMAGES FROM GALLER OR CAMERA */
    int RESULT_LOAD_IMAGE = 0;
    private static final int CAMERA_REQUEST = 1888;
    String cam_imag = "";// to check wether image or camera
    static String picturePath_dd = "0";
    int img_found = 0;
    Bitmap bt;
    Bitmap bitmap;
    String base64_str = "";
    FileOutputStream fo;

    WebView wv_generate;
    WebviewLoader webviewloader;

    String driver_fname = "";
    String owner_dl_no = "";
    String driver_dlno = "";
    String aadhar = "";
    String pan_no = "";
    String passport = "";
    String voterid = "";
    String finedby_val_send = "";
    String gender_send = "";

    /* SHARED PREF START */
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String ps_name;
    String point_name;
    String exact_location;
    Long breath_anlysr;
    String exct_lctn_send;// for getting pref values
    String anlyser_id_send;// for getting breath analyder id

    int ps_code_set;// FOR SETTING THE WHICH IN SETTINGS
    int psnameby_point_code_set;// FOR SETTING THE WHICH IN SETTINGS
    String ps_code;
    String psnameby_point_code;
    String bluetooth_address = "";

    /* SHARED PREF END */

    String netwrk_info_txt = "";
    /* OFFENCE CODE TO SEND TO SERVICE */
    String offence_code_send = "33";
    String offence_Amount_send = "0";

    /* DETAINED ITEMS TO SEND */
    StringBuilder sb_detaneditems_send;

    TelephonyManager telephonyManager;
    String IMEI_send;
    String macAddress;
    String simID = "";

    LocationManager m_locationlistner;
    android.location.Location location;
    double latitude = 0.0;
    double longitude = 0.0;
    String provider = "";

    String counselling_date_send = "";
    String oocupation_send = "";
    String qualification_send = "";
    String vhle_cat_send = "";
    String vhle_catMain_send = "";
    String vhle_catSub_send = "";
    String value_toGet_subCat = "";
    String otp_status = "";
    String otpValue = "";
    String otp_msg = "";
    String vStatusConfirmationYN = "";

    DBHelper db;
    Cursor c_whlr, c_qlfctn, c_occptn, c_vhlecat, c_vhleMainCat, c_bartype;

    // String[] wheeler_code_arr, wheeler_name_arr;
    String[] occuptn_code_arr, occuptn_name_arr;
    String[] qlfctn_code_arr, qlfctn_name_arr;
    String[] vhlecat_code_arr, vhlecat_name_arr;
    String[] vhleMaincat_code_arr, vhleMaincat_name_arr;

    String[][] vchle_SubCat_code_name;
    ArrayList<String> vchle_SubCat_name_arr_list;

    /* TO SHOW SUCESS OR FAILURE TICKET GENERATIONS */

    String ticket_response = "";

    /* GPS VALUES */
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    byte[] ba = null;
    FTPClient client;
    String[] image_path_splitter;// to rename picture path to save to FTP
    String image_ToBe_renamed = "";
    String ftp_host = "";
    String ftp_port = "";
    /* THESE ARE USED AFTER SPLITTING */
    String[] FTP_HOST_PORT;

    Button btn_select_profession, btn_select_bar;
    EditText edt_prfession_name, edt_prfession_Address;

    public static String profession_code = "", bar_type_code = "", otpStatus = null;
    int d, m, y, person_age;

    RelativeLayout drunk_location;
    RadioButton bar, permit_room, wine_shop, others;
    EditText bar_name, barAddress, edt_email_ID;
    String[] occup_code_arr, occup_name_arr;

    String[] bar_code_arr, bar_name_arr;

    private boolean isValidEmaillId(String email) {

        return Pattern
                .compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
                .matcher(email).matches();
    }

    public static ImageView offender_image;
    public static String final_image_data_tosend = null;
    byte[] byteArray;
    int totaldl_points = 0;
    static String date;
    public static String Current_Date;
    public static String image_data = null;

    ImageView img_logo;
    TextView officer_Name, officer_Cadre, officer_PS;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    File file;
    String mimeType;
    String boundary = "apiclient-" + System.currentTimeMillis();

    ImageMatchPojo imageMatchPojo;

    public static List<Telangana> telanganaList;

    private Dialog dialog;

    AppCompatImageView dd_Img, imgFromCapture, imgFromLink;
    ListView listDDImgmatch;
    public Bitmap imgMatchBitmap;
    AppCompatButton btn_ImgMatchCancel, btn_ImgMatchOk;
    AlertDialog imgMatchBuilder;
    String imgMatchingData;

    @SuppressLint({"NewApi", "WorldReadableFiles"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.generate_dd);

        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        liquorFLG = false;
        professionFLG = false;
        et_age = (EditText) findViewById(R.id.edt_age_reading_dd2_xml);
        getLocation();

        img_logo = (ImageView) findViewById(R.id.img_logo);
        if (MainActivity.uintCode.equals("22")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.cyb_logo));
        } else if (MainActivity.uintCode.equals("23")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.htp_left));
        } else if (MainActivity.uintCode.equals("24")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.rac_logo));
        } else if (MainActivity.uintCode.equals("44")) { //44 Warangal
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.wgl_logo));
        } else {//  69 Siddipet
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        }
        officer_Name = (TextView) findViewById(R.id.officer_Name);
        officer_Cadre = (TextView) findViewById(R.id.officer_cadre);
        officer_PS = (TextView) findViewById(R.id.officer_PS);

        officer_Name.setText(MainActivity.pidName + "(" + MainActivity.cadre_name + ")");
        officer_Cadre.setText(MainActivity.cadre_name);
        officer_PS.setText(MainActivity.psName);

        otpStatus = null;
        final_image_data_tosend = null;

        date = (DateFormat.format("dd/MM/yyyy hh:mm:ss", new java.util.Date()).toString());

        Calendar c1 = Calendar.getInstance();
        int mSec = c1.get(Calendar.MILLISECOND);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strdate1 = sdf1.format(c1.getTime());
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        // id_date.setText(strdate1);
        Current_Date = strdate1;

        offender_image = (ImageView) findViewById(R.id.offender_image);
        offender_image.setVisibility(View.GONE);

        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String image = sharedPreference.getString("IMAGE", "");

        if (image != null) {
            try {
                byte[] decodedString = Base64.decode(image, Base64.NO_WRAP);
                InputStream inputStream = new ByteArrayInputStream(decodedString);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                offender_image.setImageBitmap(bitmap);

                offender_image.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                showToast("Image Not Selected");
            }
        } else {
            offender_image.setVisibility(View.GONE);
        }

        LoadUIComponents();

        if (android.os.Build.VERSION.SDK_INT > 11) {
            StrictMode.ThreadPolicy polocy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(polocy);
        }
        /* FIRST IT WILL MOVE TO RTADETAILS.CLASS */

        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = preferences.edit();
        /* FOR CHECKING FTP DETAILS */
        /*
         * ftp_host = preferences.getString("ftpurl", "host"); if
         * (!ftp_host.equals("host")) { FTP_HOST_PORT = ftp_host.split("\\:");
         * Log.i("DYNAMIC FTP DETAILS", "" + FTP_HOST_PORT[0] + "\nPort : "+
         * FTP_HOST_PORT[1]); }
         */

        db = new DBHelper(getApplicationContext());
        Log.i("****WHELLER DETAILS GEN CLASS***", "" + Drunk_Drive.whlr_code_send);

        m_locationlistner = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /* TO GET OCCUPATION CODE DETAILS */
        try {
            db.open();
            // WHEELER CODE
            c_occptn = DBHelper.db.rawQuery("select * from " + DBHelper.occupation_table, null);
            if (c_occptn.getCount() == 0) {
                Log.i("OCCUPATION DB DETAILS", "0");
            } else {

                occup_code_arr = new String[c_occptn.getCount()];
                occup_name_arr = new String[c_occptn.getCount()];

                int count = 0;
                while (c_occptn.moveToNext()) {
                    occup_code_arr[count] = c_occptn.getString(1);
                    occup_name_arr[count] = c_occptn.getString(2);
                    count++;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            c_occptn.close();
            db.close();
        }
        c_occptn.close();
        db.close();

        /* TO GET BAR TYPE CODE DETAILS */
        try {
            db.open();
            // WHEELER CODE
            c_bartype = DBHelper.db.rawQuery("select * from " + DBHelper.bar_table, null);
            if (c_bartype.getCount() == 0) {
                Log.i("BAR DB DETAILS", "0");
            } else {

                bar_code_arr = new String[c_bartype.getCount()];
                bar_name_arr = new String[c_bartype.getCount()];

                int count = 0;
                while (c_bartype.moveToNext()) {
                    bar_code_arr[count] = c_bartype.getString(1);
                    bar_name_arr[count] = c_bartype.getString(2);
                    count++;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            c_bartype.close();
            db.close();
        }
        c_bartype.close();
        db.close();

    }

    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    private void LoadUIComponents() {
        // TODO Auto-generated method stub
        netwrk_info_txt = "" + getResources().getString(R.string.newtork_txt);

        et_regn_cid = (EditText) findViewById(R.id.edt_regncid_dd_xml);
        et_regn_cid_name = (EditText) findViewById(R.id.edt_regncidname_dd_xml);
        et_regn_last_num = (EditText) findViewById(R.id.edt_regncid_lastnum_dd_xml);
        et_id_proof = (EditText) findViewById(R.id.edt_idproff_dd_xml);
        et_owner_dl_no = (EditText) findViewById(R.id.edt_owner_dlno_dd_xml);
        et_driver_dl_no = (EditText) findViewById(R.id.edt_driver_dlno_dd_xml);

        et_driver_name = (EditText) findViewById(R.id.edt_driver_name_dd_xml);
        et_driver_fname = (EditText) findViewById(R.id.edt_driver_fname_dd_xml);
        et_driver_contact_no = (EditText) findViewById(R.id.edt_drvr_cnctno_dd_xml);
        et_verify_otp_from_mobile = (EditText) findViewById(R.id.edt_confrmotp_dd_xml);
        ll_verify = (LinearLayout) findViewById(R.id.ll_confirmotp_dd_xml);

        // radiogrp_finedBy = (RadioGroup)
        // findViewById(R.id.radioGroup_finedby);
        radiogrp_gender = (RadioGroup) findViewById(R.id.radioGroup_gender);

        btn_idproof = (Button) findViewById(R.id.btn_select_idproff_dd_xml);
        btn_offence_date = (Button) findViewById(R.id.btn_offence_date_dd_xml);
        btn_offence_time = (Button) findViewById(R.id.btn_offence_time_dd_xml);
        btn_send_otp_to_mobile = (Button) findViewById(R.id.btn_sendOTPtoMobile_dd_xml);

        if (!SpotChallan.OtpStatus.trim().equals(null) && !SpotChallan.OtpStatus.trim().equals("") && "Y".equals(SpotChallan.OtpStatus.trim().toUpperCase())) {
            btn_send_otp_to_mobile.setVisibility(View.VISIBLE);
        } else if (!SpotChallan.OtpStatus.trim().equals(null) && !SpotChallan.OtpStatus.trim().equals("") && "N".equals(SpotChallan.OtpStatus.trim().toUpperCase())) {
            btn_send_otp_to_mobile.setVisibility(View.GONE);
        }

        btn_verify_otp_from_mobile = (Button) findViewById(R.id.btn_confrmotp_dd_xml);

        offender_image = (ImageView) findViewById(R.id.offender_image);
        offender_image.setVisibility(View.GONE);

        /* second screen */
        sb_detaneditems_send = new StringBuilder();
        et_alcohol_reading = (EditText) findViewById(R.id.edt_alchl_reading_dd2_xml);
        et_age = (EditText) findViewById(R.id.edt_age_reading_dd2_xml);
        edt_identification_mark = (EditText) findViewById(R.id.edt_identification_mark);
        et_check_sino = (EditText) findViewById(R.id.edt_checkslno_dd2_xml);
        et_address = (EditText) findViewById(R.id.edt_address_dd2_xml);
        et_city = (EditText) findViewById(R.id.edt_city_dd2_xml);

        et_dd_address = (EditText) findViewById(R.id.et_dd_address);
        et_dd_city = (EditText) findViewById(R.id.et_dd_city);

        chck_detainedItems_rc = (CheckBox) findViewById(R.id.checkBox_dt_rc_dd2_xml);
        chck_detainedItems_vhcle = (CheckBox) findViewById(R.id.checkBox_dt_vchle_dd2_xml);
        chck_detainedItems_vhcle.setChecked(true);
        chck_detainedItems_vhcle.setClickable(false);
        chck_detainedItems_licence = (CheckBox) findViewById(R.id.checkBox_dt_lcns_dd2_xml);
        // chck_detainedItems_licence.setChecked(false);
        chck_detainedItems_permit = (CheckBox) findViewById(R.id.checkBox_dt_permit_dd2_xml);

        // btn_counselling_date = (Button)
        // findViewById(R.id.btn_cslng_date_dd2_xml);
        // btn_occupation = (Button) findViewById(R.id.btn_ocuptn_dd2_xml);
        // btn_qualification = (Button) findViewById(R.id.btn_qlfctn_dd2_xml);
        // btn_maker_code = (Button) findViewById(R.id.btn_maker_code_dd2_xml);
        // btn_vhcle_cat = (Button) findViewById(R.id.btn_vhcle_cat_dd2_xml);
        // btn_veh_main_cat = (Button)
        // findViewById(R.id.btn_vchle_maincat_dd2_xml);
        // btn_veh_sub_cat = (Button)
        // findViewById(R.id.btn_vchle_mainsub_dd2_xml);

        /* THIRD SCREEN START */
        // wv_generate = (WebView) findViewById(R.id.wv_image_dd3_xml);
        ImageButton ibtn_gallery = (ImageButton) findViewById(R.id.imgbtn_browseimage_dd3_xml);
        ImageButton ibtn_capture = (ImageButton) findViewById(R.id.imgbtn_capture_dd3_xml);
        btn_cancel_final = (Button) findViewById(R.id.btn_cancel_dd3_xml);
        btn_final_submittion = (Button) findViewById(R.id.btn_finalsubmit_dd3_xml);
        /* THIRS SCREEN SNED */

        btn_select_bar = (Button) findViewById(R.id.btn_select_bar);

        btn_select_profession = (Button) findViewById(R.id.btn_select_profession);
        edt_prfession_name = (EditText) findViewById(R.id.edt_candidate_name);
        edt_prfession_Address = (EditText) findViewById(R.id.edt_candidate_Address);

        // drunk_location = (RelativeLayout)findViewById(R.id.drunk_location);
        // bar = (RadioButton)findViewById(R.id.bar);
        /*
         * permit_room = (RadioButton)findViewById(R.id.permit_room); wine_shop
         * = (RadioButton)findViewById(R.id.wine_shop); others =
         * (RadioButton)findViewById(R.id.others);
         */

        bar_name = (EditText) findViewById(R.id.edt_bar_name);
        barAddress = (EditText) findViewById(R.id.edt_bar_Address);
        edt_email_ID = (EditText) findViewById(R.id.edt_email_ID);

        bar_name.setHint("Enter Bar/Restaurant Name");
        barAddress.setHint("Enter Address of Bar/Restaurant");

        bar_name.setEnabled(false);
        barAddress.setEnabled(false);

        edt_prfession_name.setEnabled(false);
        edt_prfession_Address.setEnabled(false);
        edt_email_ID.setEnabled(false);

        et_regn_cid.setKeyListener(null);
        et_regn_cid_name.setKeyListener(null);
        et_regn_last_num.setKeyListener(null);

        // Spinner click listener
        // btn_select_profession.setOnItemSelectedListener(GenerateDrunkDriveCase.this);
        btn_select_profession.setOnClickListener(this);
        btn_select_bar.setOnClickListener(this);

        /* CHECK BOX ONXHANGE LISTENERS */
        chck_detainedItems_rc.setOnCheckedChangeListener(this);
        chck_detainedItems_licence.setOnCheckedChangeListener(this);
        chck_detainedItems_permit.setOnCheckedChangeListener(this);

        /*
         * bar.setOnClickListener(this); permit_room.setOnClickListener(this);
         * wine_shop.setOnClickListener(this); others.setOnClickListener(this);
         */

        /* first screen */
        btn_idproof.setOnClickListener(this);
        btn_offence_date.setOnClickListener(this);
        btn_offence_time.setOnClickListener(this);
        btn_final_submittion.setOnClickListener(this);
        btn_send_otp_to_mobile.setOnClickListener(this);
        btn_verify_otp_from_mobile.setOnClickListener(this);
        /* second screen */
        btn_cancel_final.setOnClickListener(this);
        // btn_counselling_date.setOnClickListener(this);
        // btn_occupation.setOnClickListener(this);
        // btn_qualification.setOnClickListener(this);
        // btn_maker_code.setOnClickListener(this);
        // btn_vhcle_cat.setOnClickListener(this);
        // btn_veh_main_cat.setOnClickListener(this);
        // btn_veh_sub_cat.setOnClickListener(this);
        /* Third Screen */
        ibtn_gallery.setOnClickListener(this);
        ibtn_capture.setOnClickListener(this);

        Log.i("Bar Name :::", "" + bar_name);
        Log.i("Address :::", "" + barAddress);

        /* VALUES FROM RTA ENTRY DETAILS */
        et_regn_cid.setText("" + (Drunk_Drive.details_regncid.toUpperCase()));
        et_regn_cid_name.setText("" + (Drunk_Drive.details_regncid_name.toUpperCase()));
        et_regn_last_num.setText("" + (Drunk_Drive.details_regn_last_num.toUpperCase()));

        if (ServiceHelper.license_data.equals(null) || ServiceHelper.license_data.equals("")) {

            et_driver_name.setText("");
            et_driver_fname.setText("");
            et_driver_contact_no.setText("");
            et_dd_address.setText("");

        } else if (ServiceHelper.license_data != null && !"0".equals(ServiceHelper.license_data) && SpotChallan.licence_details_spot_master.length > 4) {

            et_driver_name.setText("" + SpotChallan.licence_details_spot_master != null
                    ? SpotChallan.licence_details_spot_master[0].toUpperCase() : "");
            et_driver_fname.setText("" + SpotChallan.licence_details_spot_master != null
                    ? SpotChallan.licence_details_spot_master[1].toUpperCase() : "");
            // et_driver_contact_no.setText(""+RtaDetails.licene_details_master!=null?RtaDetails.licene_details_master[2].toUpperCase():"");
            et_driver_contact_no.setText("");
            et_dd_address.setText("" + SpotChallan.licence_details_spot_master != null
                    ? SpotChallan.licence_details_spot_master[4].toUpperCase() : "");

        }

        if (ServiceHelper.aadhar_data.equals("")) {

            et_driver_name.setText("");
            et_driver_fname.setText("");
            et_driver_contact_no.setText("");
            et_dd_address.setText("");

        } else if (!"null".equals(ServiceHelper.aadhar_data) && !"0".equals(ServiceHelper.aadhar_data) && ServiceHelper.aadhar_details.length > 0) {
            // et_driver_name.setText(""+ServiceHelper.aadhar_details[0].toUpperCase());
            et_driver_name.setText("" + (!ServiceHelper.aadhar_details[0].equalsIgnoreCase("0")
                    ? ServiceHelper.aadhar_details[0].toUpperCase() : ""));
            et_driver_fname.setText("" + (!ServiceHelper.aadhar_details[1].equalsIgnoreCase("0")
                    ? ServiceHelper.aadhar_details[1].toUpperCase() : ""));
            et_dd_address.setText(
                    "" + (!ServiceHelper.aadhar_details[2].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[2] : "")
                            + ","
                            + (!ServiceHelper.aadhar_details[3].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[3] + "," : "")
                            + (!ServiceHelper.aadhar_details[4].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[4] + "," : "")
                            + (!ServiceHelper.aadhar_details[5].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[5] + "," : "")
                            + (!ServiceHelper.aadhar_details[6].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[6] + "," : "")
                            + (!ServiceHelper.aadhar_details[7].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[7] + "," : ""));
            et_driver_contact_no.setText("");
            if (person_age != 0) {
                et_age.setText("" + person_age);
            } else {
                et_age.setText("");
            }
            et_dd_city.setText("" + ServiceHelper.aadhar_details[7].toUpperCase());

        }
        try {

            if (Drunk_Drive.details_owner_dl_no.toString().trim().equals("")) {
                owner_dl_no = "";
            } else {
                owner_dl_no = "" + Drunk_Drive.details_owner_dl_no.toString().trim();
            }


            if (Drunk_Drive.details_driver_dl_no.toString().trim().equals("")) {
                driver_dlno = "";
            } else {
                driver_dlno = "" + Drunk_Drive.details_driver_dl_no.toString().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
            owner_dl_no = "";
            driver_dlno = "";
        }


        try {
            if (Drunk_Drive.rta_details_master != null && Drunk_Drive.rta_details_master.length >= 1) {
                et_address.setText("");
            } else if (Drunk_Drive.rta_details_master != null && Drunk_Drive.rta_details_master.length == 0) {
                et_address.setText("");

            } else {
                et_address.setText("" + Drunk_Drive.rta_details_master[2].toString() != null ? Drunk_Drive.rta_details_master[2].toString() : "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et_address.setText("");
        }

        try {
            /* CITY */
            if (Drunk_Drive.rta_details_master != null && Drunk_Drive.rta_details_master.length > 0 && Drunk_Drive.rta_details_master.length == 1) {
                et_city.setText("");
            } else if (Drunk_Drive.rta_details_master != null && Drunk_Drive.rta_details_master.length == 0) {
                et_city.setText("");

            } else {
                et_city.setText("" + Drunk_Drive.rta_details_master[3].toString().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
            et_city.setText("");
        }


        getInitialDetails();


        /* FOR DEFAULT DATE TO BUTTON */
        format = new SimpleDateFormat("dd-MMM-yyyy");

        String inspectiondate = format.format(new Date(offnce_year - 1900, (offnce_month), offnce_day));

        btn_offence_date.setText("" + inspectiondate.toUpperCase());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(new Date());

        btn_offence_time.setText(time);

        /* SET IMAGE FROM RTA DETAILS CLASS */
        picturePath_dd = "0";
        Log.i("LOADUICOMPONENT IMAGE", "" + picturePath_dd);
        Log.i(" Oncreate RTA IMAGE", "" + Drunk_Drive.picturePath);
        // attachImageFromRta();

    }
    @SuppressWarnings("unused")
    private void updateDisplay() {
        if (!Drunk_Drive.tv_aadhar_dob.getText().toString().trim().equals("")) {
            String age_by_date = Drunk_Drive.tv_aadhar_dob.getText().toString().trim();
            String[] items1 = age_by_date.split("-");
            String d1 = items1[0];
            String m1 = items1[1];
            String y1 = items1[2];
            d = Integer.parseInt(d1);
            m = Integer.parseInt(m1);
            y = Integer.parseInt(y1);

            Log.i("Day ::::", "" + d1);
            Log.i("month :::::", "" + m1);
            Log.i("year ::::::", "" + y1);

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);

            Log.i("c month :::::", "" + month);
            Log.i("c year ::::::", "" + year);

            person_age = year - y;
        }
    }

    @SuppressWarnings("unused")

    private void addItemsOnSpinner2() {
        // TODO Auto-generated method stub

    }

    private void attachImageFromRta() {
        // TODO Auto-generated method stub

        // if (bt != null) {
        // bt.recycle();
        // bt = null;
        // }
        //
        // if (bitmap != null) {
        // bitmap.recycle();
        // bitmap = null;
        // }
        Log.i("LOADING IMAGE...", "FROM RTA CLASS");
        // wv_generate.loadUrl("about:blank");
        webviewloader = new WebviewLoader();
        wv_generate.setBackgroundColor(0x00000000);
        wv_generate.setHorizontalScrollBarEnabled(true);
        wv_generate.setVerticalScrollBarEnabled(true);
        WebSettings webSettings = wv_generate.getSettings();
        wv_generate.setInitialScale(10);
        webSettings.setJavaScriptEnabled(true);
        wv_generate.getSettings().setLoadWithOverviewMode(true);
        wv_generate.getSettings().setUseWideViewPort(true);
        wv_generate.getSettings().setBuiltInZoomControls(true);
        wv_generate.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

        // Log.i("RTA IMAGE INGENRATION", "" + RtaDetails.picturePath);

        if (!Drunk_Drive.picturePath.equals("")) {
            Log.i("GD RtaDetails.picturePath != null", "" + img_found);
            img_found = 1;
            picturePath_dd = Drunk_Drive.picturePath;

            webviewloader.DisplayImage("file://" + picturePath_dd, wv_generate);

            // bitmap = BitmapFactory.decodeFile(RtaDetails.picturePath);
            //
            // ByteArrayOutputStream bao = new ByteArrayOutputStream(1000);
            // bt = Bitmap.createScaledBitmap(bitmap, 512, 384, true);
            // bt.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            //
            // ba = bao.toByteArray();
            // Log.i("byte array", "" + ba);
            // // baNew = bao.toByteArray();
            // base64_str = "";
            // base64_str = Base64.encodeToString(ba, Base64.DEFAULT);
            // bt.recycle();
        } else {
            // img_found = 0;
            picturePath_dd = "0";
            Drunk_Drive.picturePath = "";
        }
        Log.i("attachImageFromRta", "" + img_found);
    }
    // LIKE SIMID , IMEI PRESENT_DATE AND PRESENT_TIME ,HOURS MINUTES ,
    // MAC-ADDRESS

    private void getInitialDetails() {
        // TODO Auto-generated method stub
        try {

            cal = Calendar.getInstance();
            /* FOR DATE PICKER */
            offnce_year = cal.get(Calendar.YEAR);
            offnce_month = cal.get(Calendar.MONTH);
            offnce_day = cal.get(Calendar.DAY_OF_MONTH);

            /* FOR TIME PICKER */
            hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
            if (hour.length() < 2) {
                hour = "0" + hour;
            }
            minute = String.valueOf(cal.get(Calendar.MINUTE));

            if (minute.length() < 2) {
                minute = "0" + minute;
            }

            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            IMEI_send = telephonyManager.getDeviceId();// TO GET IMEI NUMBER
            // String simId=telephonyManager.getSimSerialNumber();

            if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
                simID = "" + telephonyManager.getSimSerialNumber();
            } else {
                simID = "";
            }

            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            macAddress = wInfo.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
            macAddress = "0";
            simID = "0";
            IMEI_send = "0";
        }
    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

    // http://www.reliancemart.com/index.php?show_price=yes

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bar:
                bar.setChecked(true);
                if (bar.isChecked()) {
                    permit_room.setChecked(false);
                    wine_shop.setChecked(false);
                    others.setChecked(false);

                    bar_name.setHint("Enter Bar/Restaurant Name");
                    barAddress.setHint("Enter Address of Bar/Restaurant");
                }
                break;
            /*
             * case R.id.permit_room: permit_room.setChecked(true); if
             * (permit_room.isChecked()) { bar.setChecked(false);
             * wine_shop.setChecked(false); others.setChecked(false);
             *
             * bar_name.setHint("Enter Permit Room Name");
             * barAddress.setHint("Enter Address of Permit Room"); } break; case
             * R.id.wine_shop: wine_shop.setChecked(true); if
             * (wine_shop.isChecked()) { bar.setChecked(false);
             * permit_room.setChecked(false); others.setChecked(false);
             *
             * bar_name.setHint("Enter Wine Shop Name");
             * barAddress.setHint("Enter Address of Wine Shop"); } break; case
             * R.id.others: others.setChecked(true); if (others.isChecked()) {
             * bar.setChecked(false); permit_room.setChecked(false);
             * wine_shop.setChecked(false);
             *
             * bar_name.setHint("Enter Name of Others");
             * barAddress.setHint("Enter Address of Others"); } break;
             */

            case R.id.btn_select_profession:

                showDialog(OCCUPATION_DIALOG);

                break;
            case R.id.btn_select_bar:

                showDialog(BAR_DIALOG);

                break;
            case R.id.btn_offence_date_dd_xml:
                date_from = "offence";
                showDialog(OFFENCE_DATE);
                break;
            case R.id.btn_offence_time_dd_xml:
                showDialog(OFFENCE_TIME_PICKER);
                break;

            case R.id.btn_cslng_date_dd2_xml:
                date_from = "counselling";
                showDialog(OFFENCE_DATE);
                break;
            case R.id.btn_ocuptn_dd2_xml:
                showDialog(OCCUPATION_DIALOG);
                break;
            case R.id.btn_qlfctn_dd2_xml:
                showDialog(QUALIFICATION_DIALOG);
                break;
            case R.id.btn_vhcle_cat_dd2_xml:
                showDialog(VECHILE_CATEGORY_DIALOG);
                break;

            case R.id.btn_vchle_maincat_dd2_xml:
                showDialog(VECHILE_MAIN_CATEGORY_DIALOG);
                break;
            case R.id.btn_vchle_mainsub_dd2_xml:

                if ((vchle_SubCat_code_name.length > 0) && (vchle_SubCat_name_arr_list.size() > 0)) {
                    showDialog(VECHILE_SUB_CATEGORY_DIALOG);
                } else {
                    showToast("Try Again!");
                }
                break;
            case R.id.btn_select_idproff_dd_xml:
                showDialog(ID_PROOF_DIALOG);
                break;

            case R.id.btn_finalsubmit_dd3_xml:
                /* PS_NAME AND POINT_NAME FROM SETTINGS */

                finalsubmit();


                break;
            case R.id.btn_cancel_dd3_xml:
                Drunk_Drive.dd_dobFLG = false;
                Drunk_Drive.dd_dob_DL = null;
                totaldl_points = 0;

                finish();
                break;
            case R.id.imgbtn_browseimage_dd3_xml:
                /*
                 * cam_imag = ""; cam_imag = "browse"; picturePath_dd = "0";
                 *
                 * TO CLEAR BITMAP FILEDS clearBitmapFields();
                 *
                 * Intent in_gallery = new
                 * Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.
                 * Media.EXTERNAL_CONTENT_URI); startActivityForResult(in_gallery,
                 * RESULT_LOAD_IMAGE);
                 */
                if (Drunk_Drive.image_data_tosend != null) {
                    showToast("Already Captured image in first Screen");
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }

                break;
            case R.id.imgbtn_capture_dd3_xml:
                /*
                 * cam_imag = ""; cam_imag = "camera"; picturePath_dd = "0"; if
                 * (isDeviceSupportCamera()) {
                 *
                 * TO CLEAR BITMAP FILEDS clearBitmapFields();
                 *
                 * Intent cameraIntent = new
                 * Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 * startActivityForResult(cameraIntent, CAMERA_REQUEST); } else {
                 * showToast("Sorry! Your device doesn't support camera"); }
                 */
                if (Drunk_Drive.image_data_tosend != null) {
                    showToast("Already Captured image in first Screen");
                } else {
                    selectImage();
                }

                break;
            case R.id.btn_sendOTPtoMobile_dd_xml:

                /*---------------------------------------------------------*/
                String tempContactNumber = et_driver_contact_no.getText().toString().trim();
                if (tempContactNumber.equals("")) {
                    et_driver_contact_no
                            .setError(Html.fromHtml("<font color='black'>Enter mobile number to send OTP!!</font>"));
                } else if (tempContactNumber.trim() != null && tempContactNumber.trim().length() > 1
                        && tempContactNumber.trim().length() != 10) {
                    // showToast("Enter correct mobile number!!");
                    et_driver_contact_no
                            .setError(Html.fromHtml("<font color='black'>Enter Valid mobile number to send OTP!!</font>"));
                } else if (tempContactNumber.length() == 10) {
                    // Length =10 case starting digit should start with
                    // 7/8/9
                    // Length=11 case starting digit should start with 0
                    if ((tempContactNumber.charAt(0) == '7') || (tempContactNumber.charAt(0) == '8')
                            || (tempContactNumber.charAt(0) == '9') || (tempContactNumber.charAt(0) == '6')) {
                        if (isOnline()) {
                            otp_status = "send";
                            if (isOnline()) {
                                SpotChallan.mobilenumber = "";
                                SpotChallan.mobilenumber = tempContactNumber;

                                new Async_sendOTP_to_mobile().execute();
                            }
                        } else {
                            showToast("Please check your network connection!");
                        }
                    } else {
                        showError(et_driver_contact_no, "Check Contact No.");
                        et_driver_contact_no.setError(Html.fromHtml("<font color='black'>Check Contact No.!</font>"));
                    }
                } else if (tempContactNumber.length() == 11) {
                    if (tempContactNumber.charAt(0) == '0') {
                        if (isOnline()) {
                            otp_status = "send";
                            if (isOnline()) {
                                SpotChallan.mobilenumber = "";
                                SpotChallan.mobilenumber = tempContactNumber;
                                new Async_sendOTP_to_mobile().execute();
                            }
                        } else {
                            showToast("Please check your network connection!");
                        }
                    } else {
                        showError(et_driver_contact_no, "Check Contact No.");
                        et_driver_contact_no.setError(Html.fromHtml("<font color='black'>Check Contact Number!</font>"));
                    }
                }
                /*-------------------------------------------------------------*/
                break;
            case R.id.btn_confrmotp_dd_xml:
                if (et_verify_otp_from_mobile.getText().toString().equals("")) {
                    showToast("Please enter OTP");
                } else if (et_verify_otp_from_mobile.getText().toString().equals("" + otpValue)) {
                    vStatusConfirmationYN = "Y";
                    if (isOnline()) {
                        Log.i("***OTP CONFIRMATION ENTERED", "" + vStatusConfirmationYN);
                        otp_status = "verify";

                        new Async_sendOTP_to_mobile().execute();
                    } else {
                        showToast("Please check your network connection!");
                    }
                } else {
                    vStatusConfirmationYN = "N";
                    if (isOnline()) {
                        Log.i("***OTP CONFIRMATION ENTERED", "" + vStatusConfirmationYN);
                        otp_status = "verify";
                        new Async_sendOTP_to_mobile().execute();
                    } else {
                        showToast("Please check your network connection!");
                    }
                }
                break;
            default:
                break;
        }
    }

    protected void selectImage() {
/*        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
      //  intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(),
                BuildConfig.APPLICATION_ID + ".provider",f));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, 1);*/


        if (Build.VERSION.SDK_INT <= 23) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(GenerateDrunkDriveCase.this,
                    BuildConfig.APPLICATION_ID + ".provider", f));
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 1);
        }

    }

    /*---------------SEND OTP TO MOBILE start---------*/
    public class Async_sendOTP_to_mobile extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            otp_status = "send";

            String completeVehicle_num_send = et_regn_cid.getText().toString().trim()
                    + et_regn_cid_name.getText().toString().trim() + et_regn_last_num.getText().toString().trim();

            ServiceHelper.sendOTPtoMobile(completeVehicle_num_send, et_driver_contact_no.getText().toString().trim(),
                    "" + btn_offence_date.getText().toString().toUpperCase());

            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            otp_msg = "";
            otpValue = "";
            if (otp_status.equals("send")) {
                if (ServiceHelper.Opdata_Chalana.toLowerCase().equals("na")) {
                    ll_verify.setVisibility(View.GONE);
                    et_verify_otp_from_mobile.setText("");
                } else {
                    otpValue = "" + ServiceHelper.Opdata_Chalana;
                    showToast("OTP is sent to your mobile number");
                    Log.i("****OTP VALUE******", "" + ServiceHelper.Opdata_Chalana);
                    ll_verify.setVisibility(View.GONE);
                    et_verify_otp_from_mobile.setText("");

                    Intent dialogbox = new Intent(getApplicationContext(), OTP_input.class);

                    dialogbox.putExtra("regNO",
                            "" + et_regn_cid.getText().toString().trim() + et_regn_cid_name.getText().toString().trim()
                                    + et_regn_last_num.getText().toString().trim());
                    dialogbox.putExtra("MobileNo", et_driver_contact_no.getText().toString().trim());
                    dialogbox.putExtra("otp_date", "" + btn_offence_date.getText().toString().toUpperCase());
                    dialogbox.putExtra("OTP_value", otpValue);

                    startActivity(dialogbox);
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

    /*---------------------------------------------------------------*/

    private void clearBitmapFields() {
        // TODO Auto-generated method stub
        // if (bt != null) {
        // bt.recycle();
        // bt = null;
        // }
        //
        // if (bitmap != null) {
        // bitmap.recycle();
        // bitmap = null;
        // }
        picturePath_dd = "0";
        base64_str = "";
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {

            case OFFENCE_DATE:
                DatePickerDialog dp_offence_date = new DatePickerDialog(this, md1, offnce_year, offnce_month, offnce_day);
                dp_offence_date.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dp_offence_date;
            case OFFENCE_TIME_PICKER:


                return new TimePickerDialog(this, timePickerListener, Integer.parseInt(hour), Integer.parseInt(minute), true);

            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "", true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);

                return pd;

            case OCCUPATION_DIALOG:

                edt_prfession_name.setEnabled(true);
                edt_prfession_Address.setEnabled(true);
                edt_email_ID.setEnabled(true);

                AlertDialog.Builder ad_ocuptn = new AlertDialog.Builder(this);
                ad_ocuptn.setTitle("" + ocuptn_title);

                ad_ocuptn.setSingleChoiceItems(occup_name_arr, selected_ocuptn, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selected_ocuptn = which;
                        btn_select_profession.setText("" + occup_name_arr[which].toString().trim());
                        // showToast("" + occup_code_arr[which]);
                        if (btn_select_profession.getText().toString().contains("PRIVATE EMPLOYEE")) {
                            professionFLG = true;
                            profession_code = "6";
                            edt_prfession_name.setHint("Enter Name of Company");
                            edt_prfession_Address.setHint("Enter Address of Company");
                            edt_email_ID.setHint("Enter Email ID of Company");
                            removeDialog(OCCUPATION_DIALOG);
                        } else if (btn_select_profession.getText().toString().contains("BUSINESS")) {
                            professionFLG = true;
                            profession_code = "3";
                            edt_prfession_name.setHint("Enter Name of Business");
                            edt_prfession_Address.setHint("Enter Address of Business");
                            edt_email_ID.setHint("Enter Email ID of Bussiness");
                            removeDialog(OCCUPATION_DIALOG);
                        } else if (btn_select_profession.getText().toString().contains("SELF EMPLOYEE")) {
                            professionFLG = true;
                            profession_code = "18";
                            edt_prfession_name.setHint("Enter Name of Employment");
                            edt_prfession_Address.setHint("Enter Address of Employment ");
                            edt_email_ID.setHint("Enter Email ID of Employment");
                            removeDialog(OCCUPATION_DIALOG);
                        } else if (btn_select_profession.getText().toString().contains("GOVT EMPLOYEE")) {
                            professionFLG = true;
                            profession_code = "7";
                            edt_prfession_name.setHint("Enter Name of Department");
                            edt_prfession_Address.setHint("Enter Address of Department ");
                            edt_email_ID.setHint("Enter Email ID of Department");
                            removeDialog(OCCUPATION_DIALOG);
                        } else if (btn_select_profession.getText().toString().contains("STUDENT")) {
                            professionFLG = true;
                            profession_code = "1";
                            edt_prfession_name.setHint("Enter Name of College");
                            edt_prfession_Address.setHint("Enter Address of College");
                            edt_email_ID.setHint("Enter Email ID of College");
                            removeDialog(OCCUPATION_DIALOG);
                        } else if (btn_select_profession.getText().toString().contains("OTHERS")) {
                            professionFLG = true;
                            profession_code = "11";
                            edt_prfession_name.setHint("Enter Name of Profession");
                            edt_prfession_Address.setHint("Enter Address of Profession");
                            edt_email_ID.setHint("Enter Email ID of Profession");
                            removeDialog(OCCUPATION_DIALOG);
                        } else {
                            professionFLG = false;
                            profession_code = "";
                            /*
                             * edt_prfession_name.setHint("Enter Name of Profession"
                             * ); edt_prfession_Address.
                             * setHint("Enter Address of Profession");
                             * edt_email_ID.setHint("Enter Email ID of Profession");
                             */
                            removeDialog(OCCUPATION_DIALOG);
                        }
                    }
                });
                Dialog dg_ocuptn = ad_ocuptn.create();

                return dg_ocuptn;
            case BAR_DIALOG:

                bar_name.setEnabled(true);
                barAddress.setEnabled(true);

                AlertDialog.Builder ad_bar = new AlertDialog.Builder(this);
                ad_bar.setTitle("" + bar_title);
                ad_bar.setSingleChoiceItems(bar_name_arr, selected_bar, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selected_bar = which;
                        btn_select_bar.setText("" + bar_name_arr[which].toString().trim());
                        // showToast("" + occup_code_arr[which]);
                        Log.i("", "" + bar_name_arr[which]);
                        // Log.i("CODE :::", ""+bar_code_arr);
                        if (btn_select_bar.getText().toString().contains("BAR")) {
                            liquorFLG = true;
                            bar_type_code = "1";
                            bar_name.setHint("Enter Name of " + btn_select_bar.getText().toString());
                            barAddress.setHint("Enter Address of " + btn_select_bar.getText().toString());
                            removeDialog(BAR_DIALOG);
                        } else if (btn_select_bar.getText().toString().contains("WINES PERMIT ROOM")) {
                            liquorFLG = true;
                            bar_type_code = "2";
                            bar_name.setHint("Enter Name of " + btn_select_bar.getText().toString());
                            barAddress.setHint("Enter Address of " + btn_select_bar.getText().toString());
                            removeDialog(BAR_DIALOG);
                        } else if (btn_select_bar.getText().toString().contains("PUB")) {
                            liquorFLG = true;
                            bar_type_code = "3";
                            bar_name.setHint("Enter Name of " + btn_select_bar.getText().toString());
                            barAddress.setHint("Enter Address of " + btn_select_bar.getText().toString());
                            removeDialog(BAR_DIALOG);
                        } else if (btn_select_bar.getText().toString().contains("OTHERS")) {
                            liquorFLG = true;
                            bar_type_code = "4";
                            bar_name.setHint("Enter Name of " + btn_select_bar.getText().toString());
                            barAddress.setHint("Enter Address of " + btn_select_bar.getText().toString());
                            removeDialog(BAR_DIALOG);
                        } else if (btn_select_bar.getText().toString().contains("REFUSAL TO GIVE INFO")) {
                            liquorFLG = false;
                            bar_type_code = "5";
                            bar_name.setEnabled(false);
                            barAddress.setEnabled(false);
                            // bar_name.setHint("Enter Name of "+
                            // btn_select_bar.getText().toString());
                            // barAddress.setHint("Enter Address of "+
                            // btn_select_bar.getText().toString());
                            removeDialog(BAR_DIALOG);
                        } else {
                            liquorFLG = false;
                            profession_code = "";
                            /*
                             * bar_name.setHint("Enter Name of "+
                             * btn_select_bar.getText().toString());
                             * barAddress.setHint("Enter Address of "+
                             * btn_select_bar.getText().toString());
                             */
                            removeDialog(BAR_DIALOG);
                        }
                    }
                });
                Dialog dg_bar = ad_bar.create();
                return dg_bar;

            case ID_PROOF_DIALOG:

                TextView title = new TextView(this);
                title.setText("Select ID Proof");
                title.setBackgroundColor(Color.parseColor("#007300"));
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                AlertDialog.Builder ad_proof = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
                ad_proof.setCustomTitle(title);//
                ad_proof.setSingleChoiceItems(id_proof_arr, selected_id_proof, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selected_id_proof = which;
                        btn_idproof.setText("" + id_proof_arr[which]);
                        et_id_proof.setHint("" + id_proof_hints_arr[which]);
                        removeDialog(ID_PROOF_DIALOG);

                    }
                });
                Dialog dg_proof = ad_proof.create();
                return dg_proof;
            case ERROR_DIALOG:

                TextView title2 = new TextView(this);
                title2.setText("Hyderabad \n E-Ticket");
                title2.setBackgroundColor(Color.RED);
                title2.setGravity(Gravity.CENTER);
                title2.setTextColor(Color.WHITE);
                title2.setTextSize(26);
                title2.setTypeface(title2.getTypeface(), Typeface.BOLD);
                title2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title2.setPadding(20, 0, 20, 0);
                title2.setHeight(70);

                String ticket_message = "\n" + "" + ticket_response + "\n";

                AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
                alertDialog_Builder.setCustomTitle(title2);
                alertDialog_Builder.setIcon(R.drawable.dialog_logo);
                alertDialog_Builder.setMessage(ticket_message);
                alertDialog_Builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        removeDialog(ERROR_DIALOG);
                    }
                });
                alertDialog_Builder.setCancelable(false);

                AlertDialog alert_Dialog = alertDialog_Builder.create();
                alert_Dialog.show();

                alert_Dialog.getWindow().getAttributes();

                TextView textView2 = (TextView) alert_Dialog.findViewById(android.R.id.message);
                textView2.setTextSize(28);
                textView2.setTypeface(textView2.getTypeface(), Typeface.BOLD);
                textView2.setGravity(Gravity.CENTER);

                Button btn2 = alert_Dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn2.setTextSize(22);
                btn2.setTextColor(Color.WHITE);
                btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
                btn2.setBackgroundColor(Color.RED);

                // Dialog dg_err = ad.create();
                return alert_Dialog;

            case OTP_CNFRMTN_DIALOG:

                TextView title3 = new TextView(this);
                title3.setText("Hyderabad \n E-Ticket");
                title3.setBackgroundColor(Color.RED);
                title3.setGravity(Gravity.CENTER);
                title3.setTextColor(Color.WHITE);
                title3.setTextSize(26);
                title3.setTypeface(title3.getTypeface(), Typeface.BOLD);
                title3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title3.setPadding(20, 0, 20, 0);
                title3.setHeight(70);

                String otp_message = "\n" + "" + otp_msg + "\n";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title3);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(otp_message);
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        removeDialog(ERROR_DIALOG);
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

                Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn.setTextSize(22);
                btn.setTextColor(Color.WHITE);
                btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
                btn.setBackgroundColor(Color.RED);

                return alertDialog;
            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    /* TO GET VECHILE SUB CATEGORY DETAILS */
    public class Async_vchle_subCat extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getVechileSubCategory("" + value_toGet_subCat.replace(" ", "%20"));
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

            if (ServiceHelper.vchle_subCat_master.length > 0) {

                vchle_SubCat_code_name = new String[0][0];
                vchle_SubCat_code_name = new String[ServiceHelper.vchle_subCat_master.length][2];
                vchle_SubCat_name_arr_list = new ArrayList<String>();// FOR
                // SDISPLAYING
                // IN
                // DIALOG

                vchle_SubCat_name_arr_list.clear();

                for (int i = 1; i < ServiceHelper.vchle_subCat_master.length; i++) {

                    vchle_SubCat_code_name[i] = (ServiceHelper.vchle_subCat_master[i].split("@"));
                    vchle_SubCat_name_arr_list.add(vchle_SubCat_code_name[i][1]);

                    Log.i("***vchle sub cat****", "" + vchle_SubCat_code_name[i][0]);
                }
            } else {
                showToast("Try again");

                vchle_SubCat_code_name = new String[0][0];
                vchle_SubCat_name_arr_list.clear();
            }
        }
    }

    // 00
    /*
     * private OnCheckedChangeListener listener = new OnCheckedChangeListener()
     * {
     *
     * public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
     *
     *
     *
     * } };
     */
    /* FOR OFFENSE DATE */
    DatePickerDialog.OnDateSetListener md1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            if (date_from.equals("offence")) {
                offnce_year = selectedYear;
                offnce_month = monthOfYear;
                offnce_day = dayOfMonth;
            } else if (date_from.equals("counselling")) {
                cnslng_year = selectedYear;
                cnslng_month = monthOfYear;
                cnslng_day = dayOfMonth;
            }

            UpdateDate_toUI();
        }
    };

    @SuppressWarnings({"unused"})
    protected void UpdateDate_toUI() {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub

        Date date111 = null;
        Date date2222 = null;

        StringBuilder sb = new StringBuilder();
        if (offnce_day < 10) {
            sb.append("0").append(offnce_day).append("/");
        } else {
            sb.append(offnce_day).append("/");
        }
        if (offnce_month < 9) {
            sb.append("0").append(offnce_month + 1).append("/");
        } else {
            sb.append(offnce_month + 1).append("/");
        }
        sb.append(offnce_year);
        // date_value = sb.toString();

        if (date_from.equals("offence")) {
            String inspectiondate = format.format(new Date(offnce_year - 1900, (offnce_month), offnce_day));
            btn_offence_date.setText("" + inspectiondate.toUpperCase());
        }
        removeDialog(OFFENCE_DATE);
    }

    TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
            // TODO Auto-generated method stub

            hour = String.valueOf(hourOfDay);
            minute = String.valueOf(minuteOfDay);

            try {

                if (minuteOfDay < 10) {
                    minute = "0" + minute;
                }
                if (hourOfDay < 10) {
                    hour = "0" + hour;
                }
            } catch (Exception e) {
                e.printStackTrace();
                hour = hour;
                minute = minute;
            }

            btn_offence_time.setText((hour + ":" + minute).toString().trim());
            removeDialog(OFFENCE_TIME_PICKER);
        }
    };

    /* ACTIVITY RESULT FOR ACCESSING IMAGES FROM GALLERY AND CAMERA */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

               /* Uri picUri = data.getData();

                String filePath = getPath(picUri);
                UploadFile(filePath);*/

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    String current_date = Drunk_Drive.date;
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Hyd-E Ticket"
                            + File.separator + "Drunk&Drive" + File.separator + current_date;
                    File camerapath = new File(path);

                    if (!camerapath.exists()) {
                        camerapath.mkdirs();
                    }
                    f.delete();
                    OutputStream outFile = null;
                    file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        Log.i("Camera Path :::", "" + file.getAbsolutePath());


                        outFile = new FileOutputStream(file);
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        imgMatchBitmap = mutableBitmap;
                        Canvas canvas = new Canvas(mutableBitmap);
                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(80);
                        paint.setTextAlign(Paint.Align.CENTER);

                        int xPos = (canvas.getWidth() / 2);
                        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Date & Time: " + Current_Date, xPos + 10, yPos + 300, paint);
                        canvas.restore();

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.restore();

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);
                        canvas.rotate(90);
                        canvas.restore();

                       /* canvas.drawText("Date & Time: " + Current_Date, xPos, yPos + 300, paint);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);*/

                        Display d = getWindowManager().getDefaultDisplay();
                        int x = d.getWidth();
                        int y = d.getHeight();
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap, y, x, true);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        mutableBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                        outFile.flush();
                        outFile.close();

                        new SingleMediaScanner(this, file);

                        /*File compresedFile=compressedToFile(file);
                        Log.d("MathcnigFile",""+compresedFile.getAbsolutePath());*/
                        UploadFile(file.getAbsolutePath());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mutableBitmap); // bmp is the
                    // bitmap to
                    // dwaw into

                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(80);
                    paint.setTextAlign(Paint.Align.CENTER);
                    int xPos = (canvas.getWidth() / 2);
                    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

                    canvas.save();
                    canvas.rotate(270f, xPos, yPos);
                    canvas.drawText("Date & Time: " + Current_Date, xPos + 10, yPos, paint);
                    canvas.restore();

                    canvas.save();
                    canvas.rotate(270f, xPos, yPos);
                    canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                    canvas.restore();

                    canvas.save();
                    canvas.rotate(270f, xPos, yPos);
                    canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);
                    canvas.rotate(90);
                    canvas.restore();

                   /* canvas.drawText("Date & Time: " + Current_Date, xPos, yPos + 300, paint);
                    canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                    canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);*/

                    Display d = getWindowManager().getDefaultDisplay();
                    int x = d.getWidth();
                    int y = d.getHeight();
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap, y, x, true);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    mutableBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

                    offender_image.setImageBitmap(mutableBitmap);

                    offender_image.setVisibility(View.VISIBLE);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);

                    // imageUploadForMatch(compressImage(mutableBitmap));2201130007 0007

                    byteArray = bytes.toByteArray();

                    final_image_data_tosend = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                    Log.i("final_image_data_tosend ::", "" + final_image_data_tosend);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                String picturePath = "";
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                file = new File(picturePath, String.valueOf(System.currentTimeMillis()) + ".jpg");
                UploadFile(picturePath);
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath + "");

                Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(mutableBitmap); // bmp is the bitmap
                // to dwaw into

                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setTextSize(100);
                paint.setTextAlign(Paint.Align.CENTER);
                // canvas.drawText("Date & Time: "+Current_Date+"\n"+" Lat
                // :"+latitude+ " Long :"+longitude,1250, 1500, paint);

                int xPos = (canvas.getWidth() / 2);
                int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

                // canvas.drawText("Date & Time: " + Current_Date, xPos,
                // yPos + 800, paint);
                // canvas.drawText("Lat :" + latitude , xPos, yPos + 900,
                // paint);
                // canvas.drawText("Long :"+ longitude, xPos, yPos + 1000,
                // paint);

                offender_image.setImageBitmap(mutableBitmap);
                // picture1.setRotation(90);
                offender_image.setVisibility(View.VISIBLE);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                imgMatchBitmap = mutableBitmap;

                byteArray = bytes.toByteArray();

                final_image_data_tosend = Base64.encodeToString(byteArray, Base64.NO_WRAP);

            } else {
                final_image_data_tosend = "";
                final_image_data_tosend = null;
            }
        }

    }

    public File compressedToFile(File file) {

        try {

            String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Hyd-E Ticket"
                    + File.separator + "Drunk&Drive" + File.separator + Drunk_Drive.date;

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 200;        // x............

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            // File outPutFile = File.createTempFile("abc","image");
            File outPutFile = new File(path, String.valueOf(System.currentTimeMillis()) + ".png");
            Log.i("Camera Path outPutFile:::", "" + outPutFile.getAbsolutePath());
            FileOutputStream outputStream = new FileOutputStream(outPutFile);
            // y.......
            Objects.requireNonNull(selectedBitmap).compress(Bitmap.CompressFormat.JPEG, 95, outputStream);

            outputStream.flush();
            outputStream.close();
            new SingleMediaScanner(this, outPutFile);
            return outPutFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        while (baos.toByteArray().length / 1024 > 500) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }

  /*  public void imageUploadForMatch(final Bitmap bitmap) {
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String url = "http://54.91.91.117:50146/match";
        VolleyMultipartRequest spotGenReq = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressDialog.dismiss();

                try {
                    String resPonse = new String(response.data);
                    Log.d("ResImg", resPonse);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user", "telangana");
                params.put("auth_token", "23easd12wedf34f3hjkbnaaa1");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart("img_name.jpg", getFileDataFromDrawable(bitmap), "image/jpeg"));
                return params;
            }

        };
        requestQueue.add(spotGenReq);
    }*/

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void UploadFile(final String imagePath) {
        imageMatchPojo = new ImageMatchPojo();
        telanganaList = new ArrayList<>();

        String requestURL = "http://54.91.91.117:50146/match";
        showDialog(PROGRESS_DIALOG);

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, requestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        removeDialog(PROGRESS_DIALOG);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status_Code = jsonObject.getInt("status");
                            if (1 == status_Code) {
                                JSONObject object = jsonObject.getJSONObject("results");
                                JSONArray jsonArray = new JSONArray(object.getString("telangana"));
                                telanganaList = new Gson().fromJson(String.valueOf(jsonArray), Results.class);
                                ShowImageMatchDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                removeDialog(PROGRESS_DIALOG);
            }
        });

        smr.addFile("file", imagePath);
        smr.addStringParam("user", "telangana");
        smr.addStringParam("auth_token", "23easd12wedf34f3hjkbnaaa1");
        requestQueue.add(smr);
    }

    @SuppressLint("SetTextI18n")
    private void ShowImageMatchDialog() {

        try {

            imgMatchBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
            LayoutInflater inflater = this.getLayoutInflater();

            View dialogView = inflater.inflate(R.layout.imagematch_dialog, null);
            imgMatchBuilder.setView(dialogView);
            dd_Img = dialogView.findViewById(R.id.dd_Img);
            officer_Name = dialogView.findViewById(R.id.officer_Name);
            officer_Cadre = dialogView.findViewById(R.id.officer_cadre);
            officer_PS = dialogView.findViewById(R.id.officer_PS);
            officer_Name.setText(MainActivity.pidName + "(" + MainActivity.cadre_name + ")");
            officer_Cadre.setText(MainActivity.cadre_name);
            officer_PS.setText(MainActivity.psName);
            dd_Img.setRotation(0);
            dd_Img.setImageBitmap(imgMatchBitmap);
            dd_Img.setRotation(90);
            listDDImgmatch = dialogView.findViewById(R.id.listDDImgmatch);
            ImageMatchAdapter imageMatchAdapter = new ImageMatchAdapter(GenerateDrunkDriveCase.this, (ArrayList<Telangana>) telanganaList);
            listDDImgmatch.setAdapter(imageMatchAdapter);
            imgMatchBuilder.show();
            listDDImgmatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String imgLink = telanganaList.get(position).getLink();
                    Telangana telangana = telanganaList.get(position);
                    imgMatchingData = new Gson().toJson(telangana);
                    imgComparisionDialog(imgLink);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Please check the Network And try Again! ");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void imgComparisionDialog(final String imgLink) {
        try {
            final AlertDialog builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dView = inflater.inflate(R.layout.imagecomparision_dialog, null);
            builder.setView(dView);
            builder.setCancelable(false);
            imgFromCapture = dView.findViewById(R.id.imgFromCapture);
            imgFromLink = dView.findViewById(R.id.imgFromLink);
            btn_ImgMatchCancel = dView.findViewById(R.id.btn_ImgMatchCancel);
            btn_ImgMatchOk = dView.findViewById(R.id.btn_ImgMatchOk);
            imgFromCapture.setRotation(0);
            imgFromCapture.setImageBitmap(imgMatchBitmap);
            imgFromCapture.setRotation(90);
            imgFromCapture.setOnTouchListener(new ImageMatrixTouchHandler(dView.getContext()));
            Glide.with(this).load(imgLink).into(imgFromLink);
            imgFromLink.setOnTouchListener(new ImageMatrixTouchHandler(dView.getContext()));
            builder.show();
            btn_ImgMatchCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });

            btn_ImgMatchOk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                    imgMatchBuilder.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Please check the Network And try Again! ");
        }

    }

    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST
        // QUALITY
        // MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            // if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
            // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    // decodes image and scales it to reduce memory consumption
    @SuppressWarnings("unused")
    private Bitmap decodeUri(String selectedImage) {
        try {
            // Decode image size
            // fm_ll.setBackgroundResource(0);
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImage, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 400;
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
                Log.i("Scale bule Values", " " + scale);
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Runtime.getRuntime().gc();
            return BitmapFactory.decodeFile(selectedImage, o2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public class Async_generate_dd_case extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // String split_wheeler[]= RtaDetails.whlr_code_send.split("\\-");
            // Log.i("split_string_array :::", ""+split_wheeler[0].trim());


            try {

                String emailID = (edt_email_ID.getText() != null && edt_email_ID.getText().toString().trim().length() > 0)
                        ? edt_email_ID.getText().toString().trim() : "NA";

                String barNAME = (bar_name.getText() != null && bar_name.getText().toString().trim().length() > 0)
                        ? bar_name.getText().toString().trim() : "NA";

                String liquor_Address = (barAddress.getText() != null
                        && barAddress.getText().toString().trim().length() > 0) ? barAddress.getText().toString().trim()
                        : "NA";

                String liquor_code = (bar_type_code != null && bar_type_code.length() > 0) ? bar_type_code : "NA";

                String profession_name = (edt_prfession_name.getText() != null
                        && edt_prfession_name.getText().toString().trim().length() > 0)
                        ? edt_prfession_name.getText().toString().trim() : "NA";

                String profession_Addres = (edt_prfession_Address.getText() != null
                        && edt_prfession_Address.getText().toString().trim().length() > 0)
                        ? edt_prfession_Address.getText().toString().trim() : "NA";

                String professionCode = (profession_code != null && profession_code.length() > 0) ? profession_code : "NA";

                String identification_mark = (edt_identification_mark.getText() != null
                        && edt_identification_mark.getText().toString().trim().length() > 0)
                        ? edt_identification_mark.getText().toString().trim() : "NA";

                aadhar = (Drunk_Drive.et_aadharnumber.getText() != null
                        && Drunk_Drive.et_aadharnumber.getText().toString().trim().length() == 12)
                        ? Drunk_Drive.et_aadharnumber.getText().toString().trim() : "NA";

                SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String psCd = sharedPreference.getString("PS_CODE", "");
                String psName = sharedPreference.getString("PS_NAME", "");
                String pidCd = sharedPreference.getString("PID_CODE", "");
                String pidName = sharedPreference.getString("PID_NAME", "");
                String cadre = sharedPreference.getString("CADRE_NAME", "");
                String cadreCd = sharedPreference.getString("CADRE_CODE", "");

                String pswd = sharedPreference.getString("PASS_WORD", "");


                if (Drunk_Drive.image_data_tosend != null) {
                    image_data = "" + Drunk_Drive.image_data_tosend;
                } else if (final_image_data_tosend != null) {
                    image_data = "" + final_image_data_tosend;
                } else {
                    image_data = "" + final_image_data_tosend;
                }

                try {
                    pswd = PidSecEncrypt.encryptmd5(pswd);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                String dob = Drunk_Drive.dd_dob_DL;


                String rtaresponse = "NA|NA|NA";

                if (!ServiceHelper.rtaapproovedresponse.equalsIgnoreCase(null) && ServiceHelper.rtaapproovedresponse.length() > 3) {
                    rtaresponse = ServiceHelper.rtaapproovedresponse.trim().toString();

                }
                passport=imgMatchingData;

                ServiceHelper.generateDrunDriveCase_1_5_2(
                        "" + "" + et_regn_cid.getText().toString() + "" + et_regn_cid_name.getText().toString(),
                        "" + et_regn_last_num.getText().toString(),
                        "" + et_regn_cid.getText().toString() + "" + et_regn_cid_name.getText().toString() + ""
                                + et_regn_last_num.getText().toString(),
                        pidCd, // operator code or pid code
                        pidName, // operator name or pid name
                        pidCd, // operator code or pid code
                        pidName, // operator name or pid name
                        psCd, // arr[2]- ps-code @ arr[3]- ps-name
                        psName, "" + ps_code, "" + ps_name, "" + psnameby_point_code, "" + point_name, exct_lctn_send,
                        "" + btn_offence_date.getText().toString().trim(),
                        "" + btn_offence_time.getText().toString().trim(), "" + finedby_val_send, "" + owner_dl_no,
                        "" + driver_dlno, "" + et_driver_name.getText().toString().trim(), "" + driver_fname, "" + aadhar,
                        "" + pan_no, "" + passport, "" + dob, "" + et_driver_contact_no.getText().toString().trim(),
                        "" + offence_code_send, "" + offence_Amount_send, "" + sb_detaneditems_send,
                        "" + counselling_date_send, "" + et_alcohol_reading.getText().toString().trim(),
                        "" + et_age.getText().toString().trim(), "" + oocupation_send, "" + qualification_send,
                        "" + gender_send, "" + et_check_sino.getText().toString().trim(), "" + Drunk_Drive.whlr_code_send,
                        "" + vhle_cat_send, "" + vhle_catMain_send, "" + rtaresponse,
                        "" + et_address.getText().toString().trim(), "" + et_city.getText().toString().trim(),
                        "" + Dashboard.UNIT_CODE, "" + Dashboard.UNIT_NAME, "" + cadreCd, "" + cadre,
                        " " + final_image_data_tosend, "" + IMEI_send, "" + latitude, "" + longitude, "" + macAddress,
                        "" + simID, "" + breath_anlysr, "" + Drunk_Drive.licence_status, "" + liquor_code + "|" + barNAME,
                        "" + liquor_Address, "" + professionCode + "|" + profession_name,
                        "" + profession_Addres + "|" + emailID + "|" + identification_mark);

            } catch (Exception e) {
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

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            totaldl_points = 0;

            if (ServiceHelper.Opdata_Chalana != null && ServiceHelper.Opdata_Chalana.equals("0")) {
                Drunk_Drive.picturePath = "";
                ticket_response = "Ticket Generation Failed";
                showDialog(ERROR_DIALOG);
            } else if (ServiceHelper.Opdata_Chalana != null && ServiceHelper.Opdata_Chalana.equals("1")) {
                Drunk_Drive.picturePath = "";
                ticket_response = "Already Challan Generated";
                showDialog(ERROR_DIALOG);
            } else if (ServiceHelper.final_response_master != null && !ServiceHelper.Opdata_Chalana.equals("0") && ServiceHelper.final_response_master.length > 0) {

                if (ServiceHelper.final_response_master != null && ServiceHelper.final_response_master.length > 18 && !ServiceHelper.final_response_master[17].toString().trim().equals("")
                        && !ServiceHelper.final_response_master[18].toString().trim().equals("")) {

                    Intent intent = new Intent(GenerateDrunkDriveCase.this, DrunkResponse.class);
                    startActivity(intent);
                }


            } else {
                ticket_response = "Ticket Generation Failed";
                showDialog(ERROR_DIALOG);
            }
        }

    }

    protected boolean validateMobileNo(String mobileNo) {
        boolean flg = false;
        try {
            if (mobileNo != null && mobileNo.trim().length() == 10 && ("7".equals(mobileNo.trim().substring(0, 1))
                    || "8".equals(mobileNo.trim().substring(0, 1)) || "9".equals(mobileNo.trim().substring(0, 1)))
                    || "6".equals(mobileNo.trim().substring(0, 1))) {
                flg = true;
            } else if (mobileNo != null && mobileNo.trim().length() == 11
                    && "0".equals(mobileNo.trim().substring(0, 1))) {
                flg = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flg = false;
        }
        return flg;
    }

    /***********************
     * Mobile Number Validation Method Ends
     ***************************/

    public void uploadFile(String fileName) {
        try {
            // String actual_image_name = "";
            String replacing_image_name = "";

            /* WHICH IS GETTING FROM MOBILE AS IMG_09_123 */
            // actual_image_name = (image_ToBe_renamed.substring(0,
            // image_ToBe_renamed.length()));

            /* GETTING ONLY IMAGE NAME FROM SERVICE AFTER SPLITTING */
            replacing_image_name = ServiceHelper.final_response_master[18].toString().trim().substring(0,
                    ServiceHelper.final_response_master[18].toString().trim().length());

            Log.i("reaplcaing image name", "" + replacing_image_name);

            File file_tosend = new File(fileName);// sdcard/o/DCIM/IMG_67.jpg
            Log.i("getAbsolutePath()", "" + file_tosend.getAbsolutePath());
            Log.i("getPath()", "" + file_tosend.getPath());

            client = new FTPClient();
            client.connect(FTP_HOST_PORT[0].toString().trim(), Integer.parseInt(FTP_HOST_PORT[1].toString().trim()));
            client.login(Utils.FTP_USERNAME, Utils.FTP_PWD);
            client.setType(FTPClient.TYPE_BINARY);

            Log.i("parent file", file_tosend.getParentFile() + "");

            File oldfile = new File(new File(file_tosend.getParentFile() + ""), file_tosend.getName());
            File newfile = new File(new File(file_tosend.getParentFile() + ""), replacing_image_name);

            // File oldfile =new File(new
            // File(sdcard.getPath()+"/DCIM"+"/Camera/"),actual_image_name);
            // File newfile =new File(new
            // File(sdcard.getPath()+"/DCIM"+"/Camera/"),replacing_image_name);

            if (oldfile.renameTo(newfile)) {
                System.out.println("Rename succesful");
            } else {
                System.out.println("Rename failed");
            }

            Log.i("NEW PATH", "" + newfile);

            if (file_tosend.getParentFile() != null) {
                client.changeDirectory("" + ServiceHelper.final_response_master[17].toString().trim());

                client.upload(newfile, new MyTransferListener());
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.logout();
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    /******* Used to file upload and show progress **********/

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {

            // Transfer started
            showDialog(PROGRESS_DIALOG);
            System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {

            // Yet other length bytes has been transferred since the last time
            // this
            // method was called
            showDialog(PROGRESS_DIALOG);
            System.out.println(" transferred ..." + length);
        }

        public void completed() {

            // Transfer completed

            removeDialog(PROGRESS_DIALOG);
            if (ServiceHelper.final_response_master.length > 0) {
                if (!Drunk_Drive.picturePath.equals("")) {
                    Drunk_Drive.picturePath = "";
                }
                /* TO LOGOUT THE FTP */
                try {
                    client.logout();
                    client.disconnect(true);
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (FTPIllegalReplyException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (FTPException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                startActivity(new Intent(getApplicationContext(), DrunkResponse.class));
                finish();

            }
            System.out.println(" completed ...");
        }

        public void aborted() {

            // Transfer aborted
            removeDialog(PROGRESS_DIALOG);

            /* TO LOGOUT THE FTP */
            try {
                client.logout();
                client.disconnect(true);
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FTPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println(" aborted ...");
        }

        public void failed() {

            // Transfer failed
            removeDialog(PROGRESS_DIALOG);
            /* TO LOGOUT THE FTP */
            try {
                client.logout();
                client.disconnect(true);
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FTPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(" failed ...");
        }

    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device

            return false;
        }
    }

    public void ClearFirstScreenValues() {
        et_regn_cid.setText("");
        et_regn_cid_name.setText("");
        et_regn_last_num.setText("");
        et_owner_dl_no.setText("");
        et_driver_dl_no.setText("");
        et_driver_name.setText("");
        et_driver_fname.setText("");
        et_driver_contact_no.setText("");
        btn_offence_date.setText("" + getResources().getString(R.string.select));
        btn_offence_time.setText("" + getResources().getString(R.string.select));
    }

    private void showToast(String msg) {
        // TODO Auto-generated method stub
        // Toast.makeText(getApplicationContext(), "" + msg,
        // Toast.LENGTH_SHORT).show();
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();

        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(24);

        toastView.setBackgroundResource(R.drawable.toast_background);
        toast.show();
    }

    private void showError(EditText et, String msg) {
        // TODO Auto-generated method stub
        et.setError("" + msg);
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
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
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
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        m_locationlistner.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
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

    }

    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub

    }

    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (bt != null) {
            bt.recycle();
            bt = null;
        }

        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        super.onDestroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        // Toast.makeText(parent.getContext(), "Selected: " + item,
        // Toast.LENGTH_LONG).show();
        if (item.equals("STUDENT")) {
            profession_code = "1";
            edt_prfession_name.setHint("Enter Name of " + btn_select_profession.getText().toString());
            edt_prfession_Address.setHint("Enter Address of " + btn_select_profession.getText().toString());
            edt_email_ID.setHint("Enter Email ID of " + btn_select_profession.getText().toString());
        } else if (item.equals("BUSSINESS")) {
            profession_code = "3";
            edt_prfession_name.setHint("Enter Name of " + btn_select_profession.getText().toString());
            edt_prfession_Address.setHint("Enter Address of " + btn_select_profession.getText().toString());
            edt_email_ID.setHint("Enter Email ID of " + btn_select_profession.getText().toString());
        } else if (item.equals("PRIVATE EMPLOYEE")) {
            profession_code = "6";
            edt_prfession_name.setHint("Enter Name of " + btn_select_profession.getText().toString());
            edt_prfession_Address.setHint("Enter Address of " + btn_select_profession.getText().toString());
            edt_email_ID.setHint("Enter Email ID of " + btn_select_profession.getText().toString());
        } else if (item.equals("GOVT EMPLOYEE")) {
            profession_code = "7";
            edt_prfession_name.setHint("Enter Name of " + btn_select_profession.getText().toString());
            edt_prfession_Address.setHint("Enter Address of " + btn_select_profession.getText().toString());
            edt_email_ID.setHint("Enter Email ID of " + btn_select_profession.getText().toString());
        } else if (item.equals("SELF EMPLOYEE")) {
            profession_code = "18";
            edt_prfession_name.setHint("Enter Name of " + btn_select_profession.getText().toString());
            edt_prfession_Address.setHint("Enter Address of " + btn_select_profession.getText().toString());
            edt_email_ID.setHint("Enter Email ID of " + btn_select_profession.getText().toString());
        } else if (item.equals("OTHERS")) {
            profession_code = "11";
            edt_prfession_name.setHint("Enter Name of " + btn_select_profession.getText().toString());
            edt_prfession_Address.setHint("Enter Address of " + btn_select_profession.getText().toString());
            edt_email_ID.setHint("Enter Email ID of " + btn_select_profession.getText().toString());
        } else if (item.equals("--SELECT PROFESSION--")) {
            profession_code = "";
        }

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void onBackPressed() {
        showToast("Please Click on Cancel Button");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub

        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.checkBox_dt_rc_dd2_xml:
                    if (isChecked) {
                        chck_detainedItems_rc.setChecked(true);
                        // sb_detaneditems_send.append("02:VEHICLE@");

                    } else {
                        chck_detainedItems_rc.setChecked(false);
                        // sb_detaneditems_send.delete(0,
                        // sb_detaneditems_send.length());
                    }

                    break;

                case R.id.checkBox_dt_lcns_dd2_xml:
                    if (isChecked) {
                        chck_detainedItems_licence.setChecked(true);

                        /*
                         * if
                         * (!sb_detaneditems_send.toString().contains("03:LICENCE@")
                         * ) { sb_detaneditems_send.append("03:LICENCE@");
                         *
                         * }
                         */

                    } else {

                        // sb_detaneditems_send.delete(0,
                        // sb_detaneditems_send.length());

                        chck_detainedItems_licence.setChecked(false);
                    }

                    break;
                case R.id.checkBox_dt_permit_dd2_xml:
                    if (isChecked) {
                        chck_detainedItems_permit.setChecked(true);
                        // sb_detaneditems_send.append("02:VEHICLE@");

                    } else {
                        // sb_detaneditems_send.delete(0,
                        // sb_detaneditems_send.length());

                        chck_detainedItems_permit.setChecked(false);
                    }

                    break;

            }
        }

    }


    public void finalsubmit() {
        ps_name = preferences.getString("psname_name", "psname");
        point_name = preferences.getString("point_name", "pointname");

        if ((ps_name.equals("psname")) && (point_name.equals("pointname"))) {
            showToast("Set PS Details in Settings!");
        } else {

			/*	selected_finedBy_sub = radiogrp_finedBy.getCheckedRadioButtonId();
				radio_finedBy_subcat = (RadioButton) findViewById(selected_finedBy_sub);*/

            /* GENDER VALUES */
            val_gender = radiogrp_gender.getCheckedRadioButtonId();
            radio_male_female = (RadioButton) findViewById(val_gender);
            Log.i("GENDER", "" + radio_male_female.getText().toString().trim());
            driver_fname = "";
            // owner_dl_no = "";
            // driver_dlno = "";

            if (et_driver_fname.getText().toString().trim().equals("")) {
                driver_fname = "";
            } else {
                driver_fname = "" + et_driver_fname.getText().toString();
            }

            /* CHEKING ID PROOF */
            if (btn_idproof.getText().toString().trim()
                    .equals("" + getResources().getString(R.string.select_idproof))) {
                aadhar = "";
                pan_no = "";
                passport = "";
                voterid = "";
            } else {

                if (btn_idproof.getText().toString().trim().equals("" + id_proof_arr[0])) {
                    aadhar = "" + et_id_proof.getText().toString().trim();
                    pan_no = "";
                    passport = "";
                    voterid = "";
                } else if (btn_idproof.getText().toString().trim().equals("" + id_proof_arr[1])) {
                    aadhar = "";
                    pan_no = "" + et_id_proof.getText().toString().trim();
                    passport = "";
                    voterid = "";
                } else if (btn_idproof.getText().toString().trim().equals("" + id_proof_arr[2])) {
                    aadhar = "";
                    pan_no = "";
                    passport = "" + et_id_proof.getText().toString().trim();
                    voterid = "";
                } else if (btn_idproof.getText().toString().trim().equals("" + id_proof_arr[3])) {
                    aadhar = "";
                    pan_no = "";
                    passport = "";
                    voterid = "" + et_id_proof.getText().toString().trim();
                }
            }

			/*	 FOR CHECKING FINED BY VALUES TO PUSH
				if (radio_finedBy_subcat.getText().toString().trim()
						.equals("" + getResources().getString(R.string.finedby_policestation))) {
					finedby_val_send = "2";// police station
				} else if (radio_finedBy_subcat.getText().toString().trim()
						.equals("" + getResources().getString(R.string.finedby_court))) {
					finedby_val_send = "1";
				}*/

            /* GENDER DETAILS TO SEND */

            if (radio_male_female.getText().toString().trim()
                    .equals("" + getResources().getString(R.string.gen_male))) {
                gender_send = "1";
            } else if (radio_male_female.getText().toString().trim()
                    .equals("" + getResources().getString(R.string.gen_female))) {
                gender_send = "2";
            } else if (radio_male_female.getText().toString().trim()
                    .equals("" + getResources().getString(R.string.gen_others))) {
                gender_send = "3";
            }

            /* SCECOND SCREEN VALUES */
            sb_detaneditems_send = new StringBuilder();

            /*
             * DISABLE CHECKED DETAINED ITEMS AND ONLY SENDING VECHILE
             * DETAILS START
             */
            // sb_detaneditems_send.append("02:VEHICLE@").append("03:LICENCE@");

            sb_detaneditems_send.delete(0, sb_detaneditems_send.length());
            if (chck_detainedItems_rc.isChecked()) {
                sb_detaneditems_send.append("01:RC@");
            }

            if (chck_detainedItems_vhcle.isChecked()) {
                sb_detaneditems_send.append("02:VEHICLE@");
            }

            if (chck_detainedItems_licence.isChecked()) {
                sb_detaneditems_send.append("03:LICENCE@");
            }

            if (chck_detainedItems_permit.isChecked()) {
                sb_detaneditems_send.append("04:PERMIT@");
            }

            /* THIRD SCREEN VALUES */
            ps_name = preferences.getString("psname_name", "psname");
            point_name = preferences.getString("point_name", "pointname");
            exact_location = preferences.getString("ps_res_name_code", "location");
            breath_anlysr = preferences.getLong("analyser_id", 0);

            /* FOR CHECKING THE PREF VALUES TO PUSH */
            if (exact_location.equals("location")) {
                exct_lctn_send = "";
            } else {
                exct_lctn_send = preferences.getString("ps_res_name_code", "location");
            }

            /* FOR CHECKING THE PREF VALUES TO PUSH */
            if (breath_anlysr == 0) {
                anlyser_id_send = "";
            } else {
                anlyser_id_send = Long.toString(preferences.getLong("analyser_id", 0));
            }


            if (isOnline()) {

                ps_code = preferences.getString("psname_code", "0");
                psnameby_point_code = preferences.getString("point_code", "0");

                getLocation();

                if (btn_offence_date.getText().toString().trim()
                        .equals("" + getResources().getString(R.string.select))) {
                    showToast("Select Offence Date");
                } else if (btn_offence_time.getText().toString().trim()
                        .equals("" + getResources().getString(R.string.select))) {
                    showToast("Select Offence Time");
                } else if (et_driver_name.getText().toString().trim().equals("")) {
                    et_driver_name.requestFocus();
                    et_driver_name.setError(Html.fromHtml("<font color='black'>Enter Driver Name</font>"));
                    // showToastssbuilder.toString() Driver Name");
                } else if (et_driver_contact_no.getText().toString().trim().equals("")) {
                    // showError(et_driver_contact_no, "Enter Driver Contact
                    // No.");
                    et_driver_contact_no.requestFocus();
                    et_driver_contact_no
                            .setError(Html.fromHtml("<font color='black'>Enter Driver Contact No.</font>"));
                } else if (!validateMobileNo(et_driver_contact_no.getText().toString().trim())) {
                    // showToast("Enter correct mobile number!!");
                    et_driver_contact_no.requestFocus();
                    et_driver_contact_no
                            .setError(Html.fromHtml("<font color='black'>Enter Valid Contact No.</font>"));
                } else if (et_alcohol_reading.getText().toString().trim().equals("")) {
                    et_alcohol_reading.requestFocus();
                    et_alcohol_reading.setError(Html.fromHtml("<font color='black'>Enter Alcohol Reading</font>"));
                } else if (et_alcohol_reading.getText() != null
                        && Integer.parseInt(et_alcohol_reading.getText().toString().trim()) <= 35) {

                    String bac_message = "\nBAC reading must be 36 and above\n";
                    TextView title = new TextView(this);
                    title.setText("BAC Status");
                    title.setBackgroundColor(Color.RED);
                    title.setGravity(Gravity.CENTER);
                    title.setTextColor(Color.WHITE);
                    title.setTextSize(26);
                    title.setTypeface(title.getTypeface(), Typeface.BOLD);
                    title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo,
                            0);
                    title.setPadding(20, 0, 20, 0);
                    title.setHeight(70);

                    AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(this,
                            AlertDialog.THEME_HOLO_LIGHT);
                    alertDialog_Builder.setCustomTitle(title);
                    alertDialog_Builder.setIcon(R.drawable.dialog_logo);
                    alertDialog_Builder.setMessage(bac_message);
                    alertDialog_Builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
                    alertDialog_Builder.setCancelable(false);

                    AlertDialog alert_Dialog = alertDialog_Builder.create();
                    alert_Dialog.show();

                    alert_Dialog.getWindow().getAttributes();

                    TextView textView = (TextView) alert_Dialog.findViewById(android.R.id.message);
                    textView.setTextSize(28);
                    textView.setTypeface(title.getTypeface(), Typeface.BOLD);
                    textView.setGravity(Gravity.CENTER);

                    Button btn1 = alert_Dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    btn1.setTextSize(22);
                    btn1.setTextColor(Color.WHITE);
                    btn1.setTypeface(title.getTypeface(), Typeface.BOLD);
                    btn1.setBackgroundColor(Color.RED);
                } else if (et_age.getText().toString().trim().equals("")) {
                    Log.i("Calling ", "In Age Validation");
                    et_age.requestFocus();
                    et_age.setError(Html.fromHtml("<font color='black'>Enter Age</font>"));

                } else if (liquorFLG && bar_name.getText().toString().trim().equals("")) {
                    bar_name.setError(
                            Html.fromHtml("<font color='black'>Enter Area Name of Alcohol Consumed</font>"));
                    bar_name.requestFocus();
                } else if (liquorFLG && barAddress.getText().toString().trim().equals("")) {
                    barAddress.setError(
                            Html.fromHtml("<font color='black'>Enter Address of Alcohol Consumed</font>"));
                    barAddress.requestFocus();
                } else if (btn_select_profession.getText().toString().trim().equals("Select Profession")) {
                    showToast("" + Html.fromHtml("<font color='black'>Select Profession</font>"));
                } else if (professionFLG && edt_prfession_name.getText().toString().trim().equals("")) {
                    edt_prfession_name.setError(Html.fromHtml("<font color='black'>Enter Profession Name</font>"));
                    edt_prfession_name.requestFocus();
                } else if (professionFLG && edt_prfession_Address.getText().toString().trim().equals("")) {
                    edt_prfession_Address
                            .setError(Html.fromHtml("<font color='black'>Enter Profession Address</font>"));
                    edt_prfession_Address.requestFocus();
                } else if (edt_email_ID.getText().toString().trim() != null
                        && edt_email_ID.getText().toString().trim().length() > 1
                        && !isValidEmaillId(edt_email_ID.getText().toString().trim())) {
                    edt_email_ID.setError(Html.fromHtml("<font color='black'>Enter Valid Email ID</font>"));
                    edt_email_ID.requestFocus();

                } else if (final_image_data_tosend == null) {
                    showToast("Please Capture the Image! ");
                } else {
                    if ("Y".equals(SpotChallan.OtpStatus.trim().toUpperCase()) && otpStatus != null) {
                        if (otpStatus.equals("0")) {
                            showToast("OTP Verification Failed");
                        } else if (otpStatus.equals("1")) {


                            int presentviolatedpoints = 0;

                            if (Drunk_Drive.rtaAprroved_Master != null && Drunk_Drive.rtaAprroved_Master.length > 0) {

                                try {
                                    presentviolatedpoints = Integer.parseInt(Drunk_Drive.rtaAprroved_Master[1].toString().trim());

                                    totaldl_points = Integer.parseInt(Drunk_Drive.dl_points.trim()) + presentviolatedpoints;

                                    //    totaldl_points= String.valueOf(presentviolatedpoints);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                    //  dl_points=dl_points;

                                    totaldl_points = 0;
                                }
                            }

                            if (totaldl_points > 0) {

                                pointsAlert();

                            } else {

                                if (isOnline()) {
                                    new Async_generate_dd_case().execute();
                                } else {
                                    showToast("Please Check Your Network Connection");
                                }
                            }

                        } else if (otpStatus.equals("2")) {


                            int presentviolatedpoints = 0;

                            if (Drunk_Drive.rtaAprroved_Master != null && Drunk_Drive.rtaAprroved_Master.length > 0) {

                                try {
                                    presentviolatedpoints = Integer.parseInt(Drunk_Drive.rtaAprroved_Master[1].toString().trim());
                                    totaldl_points = Integer.parseInt(Drunk_Drive.dl_points.trim()) + presentviolatedpoints;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    totaldl_points = 0;
                                }
                            }

                            if (totaldl_points > 0) {

                                pointsAlert();

                            } else {
                                if (isOnline()) {
                                    new Async_generate_dd_case().execute();
                                } else {
                                    showToast("Please Check Your Network Connection");
                                }
                            }
                        }
                    } else if ("Y".equals(SpotChallan.OtpStatus.trim().toUpperCase()) && otpStatus == null) {
                        otpStatus = null;
                        showToast("Please Verify Number with OTP");
                        TextView title = new TextView(GenerateDrunkDriveCase.this);
                        title.setText("ALERT");
                        title.setBackgroundColor(Color.RED);
                        title.setGravity(Gravity.CENTER);
                        title.setTextColor(Color.WHITE);
                        title.setTextSize(26);
                        title.setTypeface(title.getTypeface(), Typeface.BOLD);
                        title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0,
                                R.drawable.dialog_logo, 0);
                        title.setPadding(20, 0, 20, 0);
                        title.setHeight(70);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                GenerateDrunkDriveCase.this, AlertDialog.THEME_HOLO_LIGHT);
                        alertDialogBuilder.setCustomTitle(title);
                        alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                        alertDialogBuilder.setMessage(
                                "\n Please Click on Send OTP Button to Verify Offender Mobile Number!!\n");
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                et_driver_contact_no.requestFocus();
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
                    } else {


                        int presentviolatedpoints = 0;

                        if (Drunk_Drive.rtaAprroved_Master != null && Drunk_Drive.rtaAprroved_Master.length > 0) {

                            try {
                                presentviolatedpoints = Integer.parseInt(Drunk_Drive.rtaAprroved_Master[1].toString().trim());

                                totaldl_points = Integer.parseInt(Drunk_Drive.dl_points.trim()) + presentviolatedpoints;

                            } catch (Exception e) {
                                e.printStackTrace();

                                totaldl_points = 0;
                                //  dl_points=dl_points;
                            }
                        }

                        if (totaldl_points > 0) {

                            pointsAlert();

                        } else {

                            if (isOnline()) {
                                new Async_generate_dd_case().execute();
                            }
                        }
                    }

                }
            } else {
                showToast("" + netwrk_info_txt);
            }

        }
    }


    public void pointsAlert() {

        TextView title = new TextView(GenerateDrunkDriveCase.this);
        title.setText("ALERT");
        title.setBackgroundColor(Color.BLUE);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(26f);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
        title.setPadding(20, 0, 20, 0);
        title.setHeight(70);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GenerateDrunkDriveCase.this,
                AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setCustomTitle(title);
        alertDialogBuilder.setIcon(R.drawable.dialog_logo);
        if (totaldl_points > 12) {
            alertDialogBuilder.setMessage("\nTotal Penalty Points on Driving License are " + totaldl_points +
                    ", Detain the Original Driving License\n");

        } else if (totaldl_points > 0) {
            alertDialogBuilder.setMessage("\nTotal Penalty Points on Driving License are " + totaldl_points + "\n");
        }

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                if (totaldl_points > 12) {

                    if (chck_detainedItems_licence.isChecked()) {

                        if (isOnline()) {
                            new Async_generate_dd_case().execute();
                        } else {
                            showToast("Please Check Your Network Connection");
                        }
                    } else if (!chck_detainedItems_licence.isChecked() && chck_detainedItems_vhcle.isChecked()) {
                        if (isOnline()) {
                            new Async_generate_dd_case().execute();
                        } else {
                            showToast("Please Check Your Network Connection");
                        }
                    } else {
                        detainAlert();
                    }
                } else {
                    if (isOnline()) {
                        new Async_generate_dd_case().execute();
                    } else {
                        showToast("Please Check Your Network Connection");
                    }
                }
            }
        });


        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().getAttributes();

        TextView textView1 = (TextView) alertDialog.findViewById(android.R.id.message);
        textView1.setTextSize(28f);
        textView1.setTypeface(textView1.getTypeface(), Typeface.BOLD);
        textView1.setGravity(Gravity.CENTER);

        Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btn1.setTextSize(22f);
        btn1.setTextColor(Color.WHITE);
        btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
        btn1.setBackgroundColor(Color.BLUE);


    }


    public void detainAlert() {
        TextView title = new TextView(GenerateDrunkDriveCase.this);
        title.setText("ALERT");
        title.setBackgroundColor(Color.RED);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(26);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
        title.setPadding(20, 0, 20, 0);
        title.setHeight(70);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GenerateDrunkDriveCase.this,
                AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setCustomTitle(title);
        alertDialogBuilder.setIcon(R.drawable.dialog_logo);

        alertDialogBuilder.setMessage("\nDetain the Vehicle Until Original DL Is Submitted !\n");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

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

}
