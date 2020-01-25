package com.mtpv.mobilee_ticket;

import android.annotation.SuppressLint;
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
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.DateUtil;
import com.mtpv.mobilee_ticket_services.RealPathUtil;
import com.mtpv.mobilee_ticket_services.ServiceHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by MANOHAR on 11/21/2017.
 */

@SuppressWarnings("ResourceAsColor")
public class E_Challan_Non_ContactModule extends Activity implements LocationListener {

    String current_date = null, filenameselected, dateselected;
    public static final int PROGRESS_DIALOG = 0;
    ImageView encroachment_image;
    Button next_image, btngetrtadetails, btn_cancel, btn_submit, btn_whlr_code, btn_violation, ed_date, ed_time;
    TextView tvregno_spotchallan_xml, tvownername_spotchallan_xml, tv_addr_spotchallan_xml, tv_makername_spotchallan_xml, tv_chasis_spotchallan_xml,
            tv_engineno_spotchallan_xml, tv_vehicle_details_header_spot;
    CheckBox cb_fake;
    EditText edt_regncid_spotchallan_xml, edt_regncidname_spotchallan_xml, edt_regncid_lastnum_spotchallan_xml;
    public static double latitude = 0.0;
    public static double longitude = 0.0, grand_total = 0;
    byte[] byteArray;
    String imgString = null;
    RelativeLayout rl_rta_details_layout;
    DatePickerDialog datePickerDialog;
    /* GPS VALUES */
    // flag for GPS status
    public static String completeVehicle_num_send = "", regncode_send = "",
            regnName_send = "", vehicle_num_send = "", whlr_code_send = "", extraPassengers = "1";
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    LinearLayout ll_dynamic_violations_root_static;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    LocationManager m_locationlistner;
    android.location.Location location;
    StringBuffer message = new StringBuffer();
    public static String[] rta_details_spot_master, Wheeler_check, vehicle_remarks_resp_master;
    final int WHEELER_CODE = 1, DYNAMIC_VIOLATIONS = 2;
    private int mYear, mMonth, mDay;
    String[] wheeler_code_arr_spot, wheeler_name_arr_spot;
    Bitmap mutableBitmap;
    DBHelper db;
    Cursor c_whlr, c_occptn;
    int selected_wheller_code = -1, edt_regncid_spotchallanMAX_LENGTH = 4,
            edt_regncidname_spotchallanLENGTH = 4, edt_regncid_lastnum_spotchallanMAX_LENGTH = 4;
    LinkedHashMap<String, String> check_map;
    public static boolean passngerFLG = false;

    ArrayList<String> violation_list, violation_description, violation_section, violation_offence_Code, violation_min_amount,
            violation_max_amount, violation_avg_amount, violation_positions, violation_rg_ids, violation_checked_violations;
    HashMap<String, String> check_all_ids, vioCodeDescMap;
    static String vioDetainCheckFlag = null;
    static HashMap<String, String> vioDetainFlags;
    StringBuffer violations_details_send, violation_desc_append, pointsreachedviolation;
    LinearLayout[] ll_dynamic_vltns;
    Spinner[] spinner_violation;

    TextView[] tv_dynamic_vltn_name;
    CheckBox[] check_dynamic_vltn;
    String violation_code_value;
    TelephonyManager telephonyManager;
    public static CheckBox check;

    String regCd, vehiclenumber, offenceDateandTime, pidCode = null, pidName = null, psCd = null, psName = null,
            bookedPScode_send_from_settings, bookedPSname_send_from_settings, point_code_send_from_settings,
            point_name_send_from_settings, imei_send, simid_send, macAddress = null, vehRemarks, tot = null, cadre_code = null, cadre_name = null,
            rtaOname, rtaAdres, city, mobileNo;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String realPath;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_noncontactmodule);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        hideSoftKeyboard();
        getLocation();
        grand_total = 0;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pidCode = sharedPreferences.getString("PID_CODE", "");
        pidName = sharedPreferences.getString("PID_NAME", "");
        psCd = sharedPreferences.getString("PS_CODE", "");
        psName = sharedPreferences.getString("PS_NAME", "");
        cadre_code = sharedPreferences.getString("CADRE_CODE", "");
        cadre_name = sharedPreferences.getString("CADRE_NAME", "");

        imgString = null;


        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        bookedPScode_send_from_settings = preferences.getString("psname_code", "0");
        bookedPSname_send_from_settings = preferences.getString("psname_name", "psname");
        point_code_send_from_settings = preferences.getString("point_code", "0");
        point_name_send_from_settings = preferences.getString("point_name", "pointname");

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei_send = telephonyManager.getDeviceId();// TO GET IMEI NUMBER
        if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            simid_send = "" + telephonyManager.getSimSerialNumber();
        } else {
            simid_send = "";
        }


        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        macAddress = wInfo.getMacAddress();


        LoadUIcomponents();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                regCd = null;
                vehiclenumber = null;
                offenceDateandTime = null;
                vehRemarks = null;
                tot = "0";

                regCd = edt_regncid_spotchallan_xml.getText().toString() + edt_regncidname_spotchallan_xml.getText().toString();
                vehiclenumber = edt_regncid_lastnum_spotchallan_xml.getText().toString();
                offenceDateandTime = ed_date.getText().toString() + " " + ed_time.getText().toString();


                try {
                    tot = String.valueOf(grand_total);
                } catch (Exception e) {
                    e.printStackTrace();
                    tot = "0";
                }

                if (btn_submit.getText().toString().equalsIgnoreCase("Update Fake Info")) {
                    if ("0".equals(imgString)) {
                        showToast("Please Select Photo !");
                    } else if (edt_regncid_spotchallan_xml.getText().toString().trim().equals("")) {
                        edt_regncid_spotchallan_xml.setError(Html.fromHtml("<font color='black'>Enter Registration Code</font>"));

                    } else if (edt_regncid_lastnum_spotchallan_xml.getText().toString().trim().equals("")) {
                        edt_regncid_lastnum_spotchallan_xml.setError(Html.fromHtml("<font color='black'>Enter Vehicle Code</font>"));

                    } else {
                        vehRemarks = "Y";
                        Async_generateChallan async_generateChallan = new Async_generateChallan();
                        async_generateChallan.execute();
                    }

                } else {
                    if ("0".equals(imgString)) {
                        showToast("Please Select Photo !");
                    } else if (edt_regncid_spotchallan_xml.getText().toString().trim().equals("")) {
                        edt_regncid_spotchallan_xml.setError(Html.fromHtml("<font color='black'>Enter Registration Code</font>"));

                    } else if (edt_regncid_lastnum_spotchallan_xml.getText().toString().trim().equals("")) {
                        edt_regncid_lastnum_spotchallan_xml.setError(Html.fromHtml("<font color='black'>Enter Vehicle Code</font>"));

                    } else if (btn_whlr_code.getText().toString().trim()
                            .equals("" + getString(R.string.select_wheeler_code))) {
                        showToast("Select Wheeler Code");

                    } else if (btn_violation.getText().toString().equalsIgnoreCase("" + getString(R.string.select_violation))) {

                        showToast("Select Violation");

                    } else {

                        vehRemarks = "N";
                        Async_generateChallan async_generateChallan = new Async_generateChallan();
                        async_generateChallan.execute();
                    }

                }


            }
        });

        cb_fake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (cb_fake.isChecked()) {
                    btn_whlr_code.setVisibility(View.GONE);
                    btn_submit.setText("Update Fake Info");
                    btn_violation.setVisibility(View.GONE);

                } else {
                    btn_whlr_code.setVisibility(View.VISIBLE);
                    btn_submit.setText("Submit");
                    btn_violation.setVisibility(View.VISIBLE);
                }


            }
        });

        db = new DBHelper(this);
        try {
            db.open();
            c_whlr = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);
            if (c_whlr.getCount() == 0) {
            } else {

                wheeler_code_arr_spot = new String[c_whlr.getCount()];
                wheeler_name_arr_spot = new String[c_whlr.getCount()];

                int count = 0;
                while (c_whlr.moveToNext()) {
                    wheeler_code_arr_spot[count] = c_whlr.getString(1);
                    wheeler_name_arr_spot[count] = c_whlr.getString(2);
                    count++;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            c_whlr.close();
            db.close();
        }
        c_whlr.close();
        db.close();

        violation_list = new ArrayList<String>();
        violation_description = new ArrayList<String>();
        violation_section = new ArrayList<String>();
        violation_positions = new ArrayList<String>();
        violation_max_amount = new ArrayList<String>();
        violation_avg_amount = new ArrayList<String>();
        violation_min_amount = new ArrayList<String>();
        violation_offence_Code = new ArrayList<String>();
        violation_checked_violations = new ArrayList<String>();
        violation_rg_ids = new ArrayList<String>();

        btn_violation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraPassengers = "1";
                //       tv_grand_total_spot.setText("");
                // tv_violation_amnt.setText("");
                VehicleHistoryPendingChallans.total_amount_selected_challans = 0.0;

                if (isOnline()) {

                    if ((!btn_whlr_code.getText().toString().trim().equals("" + getString(R.string.select_wheeler_code)))
                            && (violation_offence_Code.size() > 0)) {

                        /* TO CLEAR THE CHECKED & UNC-CHECKED POEISITONS */
                        violation_positions.removeAll(violation_positions);
                        violation_rg_ids.removeAll(violation_rg_ids);
                        violation_checked_violations.removeAll(violation_checked_violations);
                        grand_total = 0.0;

                        /* CHECK MAP INTITALISATION */
                        check_map = new LinkedHashMap<String, String>();
                        check_all_ids = new HashMap<String, String>();
                        check_all_ids.clear();

                        vioCodeDescMap = new HashMap<String, String>();
                        vioCodeDescMap.clear();

                        vioDetainCheckFlag = "0";

                        vioDetainFlags = new HashMap<String, String>();
                        vioDetainFlags.clear();

                        violations_details_send = new StringBuffer();
                        violations_details_send.delete(0, violations_details_send.length());


                        pointsreachedviolation = new StringBuffer();
                        pointsreachedviolation.delete(0, pointsreachedviolation.length());


                        /* TO APPEND THE SLECTED VILATIONS TO BUTTON */
                        violation_desc_append = new StringBuffer();
                        violation_desc_append.delete(0, violation_desc_append.length());

                        removeDialog(DYNAMIC_VIOLATIONS);
                        showDialog(DYNAMIC_VIOLATIONS);

                    } else {
                        showToast("Select Wheeler Code");
                    }
                } else {
                    showToast("No Internet Connection");
                }
            }
        });


        ed_date.setEnabled(false);
        ed_time.setEnabled(false);

        ed_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imgString != null) {

                    {
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);


                        datePickerDialog = new DatePickerDialog(E_Challan_Non_ContactModule.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @SuppressWarnings("deprecation")
                                    @SuppressLint({"SimpleDateFormat", "DefaultLocale"})

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                        SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");

                                        SimpleDateFormat date_parse = new SimpleDateFormat("dd/MM/yyyy");
                                        String dtdob = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                        try {
                                            dateselected = date_format.format(date_parse.parse(dtdob));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }


                                        ed_date.setText(dateselected);
                                        String todaysdate = new DateUtil().getTodaysDate();

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                        datePickerDialog.show();
                    }

                } else {
                    showToast("Please Capture Image to fill date and time As per the image");
                }


            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
            }
        });
        ed_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(E_Challan_Non_ContactModule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        Calendar datetime = Calendar.getInstance();
                        Calendar c = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        datetime.set(Calendar.MINUTE, selectedMinute);
                        ed_time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });
        encroachment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ed_time.setText("SELECT TIME");
                ed_date.setText("SELECT DATE");
                encroachment_image.setRotation(0);
                selectImage();
            }
        });

        next_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgString != null) {
                    encroachment_image.setRotation(0);
                    encroachment_image.setImageDrawable(getResources().getDrawable(R.drawable.photo));
                    ed_time.setText("SELECT TIME");
                    ed_date.setText("SELECT DATE");
                    imgString = null;
                    showToast("Captured Image Saved Succesufuly !!!");
                } else {
                    showToast("Please Capture Image to Move to Next Image");
                }
            }
        });


        btn_whlr_code.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (edt_regncid_spotchallan_xml.getText().toString().equals("") || edt_regncid_lastnum_spotchallan_xml.getText().toString().equals("")) {
                    showToast("Please Enter Proper Vehicle Number");
                } else {
                    if (isOnline()) {
                        showDialog(WHEELER_CODE);
                    } else {
                        showToast("No Internet Connection");
                    }

                }

            }
        });
        btngetrtadetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeVehicle_num_send = "";
                completeVehicle_num_send = ("" + edt_regncid_spotchallan_xml.getText().toString() + ""
                        + edt_regncidname_spotchallan_xml.getText().toString() + "" + edt_regncid_lastnum_spotchallan_xml.getText().toString());


                regncode_send = "";// AP09
                regnName_send = "";// CC
                vehicle_num_send = "";// 3014

                city = "";
                rtaOname = "";
                rtaAdres = "";
                mobileNo = "";

                cb_fake.setChecked(false);

                if (cb_fake.isChecked()) {
                    btn_whlr_code.setVisibility(View.GONE);
                    btn_submit.setText("Update Fake Info");
                    btn_violation.setVisibility(View.GONE);
                } else {
                    btn_whlr_code.setVisibility(View.VISIBLE);
                    btn_submit.setText("Submit");
                    btn_violation.setVisibility(View.VISIBLE);
                }

                if (edt_regncid_spotchallan_xml.getText().toString().equals("")) {
                    edt_regncid_spotchallan_xml.setError(Html.fromHtml("<font color='black'>Enter Registration Code</font>"));
                    edt_regncid_spotchallan_xml.requestFocus();

                } else if (edt_regncidname_spotchallan_xml.getText().toString().equals("")) {
                    edt_regncidname_spotchallan_xml.setError(Html.fromHtml("<font color='black'>Enter Vehicle Code</font>"));
                    edt_regncidname_spotchallan_xml.requestFocus();

                } else if (!edt_regncid_lastnum_spotchallan_xml.getText().toString().equals("")
                        && !edt_regncid_lastnum_spotchallan_xml.getText().toString().equals("")) {


                    regncode_send = "" + edt_regncid_spotchallan_xml.getText().toString().trim().toUpperCase();
                    regnName_send = "" + edt_regncidname_spotchallan_xml.getText().toString().toUpperCase();
                    vehicle_num_send = "" + edt_regncid_lastnum_spotchallan_xml.getText().toString().trim().toUpperCase();

                    if (isOnline()) {

                        rl_rta_details_layout.setVisibility(View.GONE);
                        tvregno_spotchallan_xml.setText("");
                        tvownername_spotchallan_xml.setText("");
                        tv_addr_spotchallan_xml.setText("");
                        tv_makername_spotchallan_xml.setText("");
                        tv_chasis_spotchallan_xml.setText("");
                        tv_engineno_spotchallan_xml.setText("");
                        tv_vehicle_details_header_spot.setText("");


                        if (isOnline()) {
                            Async_getRTADetails rta_task = new Async_getRTADetails();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                                rta_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                rta_task.execute();
                            }
                        }
                    }
                }

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

            case WHEELER_CODE:
                TextView title = new TextView(this);
                title.setText("Select Wheeler");
                title.setBackgroundColor(Color.parseColor("#007300"));
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                AlertDialog.Builder ad_whle_code_name = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
                ad_whle_code_name.setCustomTitle(title);//
                ad_whle_code_name.setSingleChoiceItems((wheeler_name_arr_spot), selected_wheller_code,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                selected_wheller_code = which;
                                btn_whlr_code.setText("" + wheeler_name_arr_spot[which]);

                                removeDialog(WHEELER_CODE);

                                whlr_code_send = wheeler_code_arr_spot[which];

                                btn_violation.setText("" + getResources().getString(R.string.select_violation));

                                if (isOnline()) {
                                    // new SpotChallan.Async_getViolations().execute();
                                } else {
                                    showToast("Please Check Your Network Connection");
                                }
                            }
                        });
                Dialog dg_whle_code_name = ad_whle_code_name.create();
                return dg_whle_code_name;

            case DYNAMIC_VIOLATIONS:

                Dialog dg_dynmic_violtns = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
                dg_dynmic_violtns.setContentView(R.layout.dynamic_violations);

                TextView tv_sub_header = (TextView) dg_dynmic_violtns.findViewById(R.id.textView_header_spot_challan_xml);
                TextView tv_title = (TextView) dg_dynmic_violtns.findViewById(R.id.textView_title_header_dynmicvltns_xml);
                tv_title.setText("" + getResources().getString(R.string.select_violation));
                ll_dynamic_violations_root_static = (LinearLayout) dg_dynmic_violtns
                        .findViewById(R.id.ll_dynamic_violations_xml);

                if (Dashboard.check_vhleHistory_or_Spot.equals("spot")) {
                    tv_sub_header.setText("" + getResources().getString(R.string.spot_challan));

                } else if (Dashboard.check_vhleHistory_or_Spot.equals("towing")) {
                    tv_sub_header.setText("" + getResources().getString(R.string.towing_one_line));

                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);

                /* DYNAMIC LAYOUTS START */
                if (violation_offence_Code.size() > 0 && ServiceHelper.violation_detailed_views != null && ServiceHelper.violation_detailed_views.length > 0) {

                    ll_dynamic_vltns = new LinearLayout[violation_offence_Code.size()];
                    spinner_violation = new Spinner[ServiceHelper.violation_detailed_views.length];
                    tv_dynamic_vltn_name = new TextView[ServiceHelper.violation_detailed_views.length];
                    check_dynamic_vltn = new CheckBox[violation_offence_Code.size()];


                    for (int i = 0; i < violation_offence_Code.size(); i++) {
                        String[] spinner_selectors = new String[3];

                        /* TO SHOW IT IN SPINNER DROPDOWN */
                        spinner_selectors[0] = "MIN :" + ServiceHelper.violation_detailed_views[i][3];
                        spinner_selectors[1] = "AVG :" + ServiceHelper.violation_detailed_views[i][5];
                        spinner_selectors[2] = "MAX :" + ServiceHelper.violation_detailed_views[i][4];

                        if (violation_offence_Code.get(i).equals("63")) {
                            pointsreachedviolation = new StringBuffer();

                            pointsreachedviolation.append("63" + "@" + spinner_selectors[0].substring(5, spinner_selectors[0].length()).trim()
                                    + "@" + spinner_selectors[2].substring(5, spinner_selectors[2].length()) + "@" + ServiceHelper.violation_detailed_views[i][2]);

                        }


                        ll_dynamic_vltns[i] = new LinearLayout(getApplicationContext());
                        ll_dynamic_vltns[i].setOrientation(LinearLayout.HORIZONTAL);
                        ll_dynamic_vltns[i].setLayoutParams(params);

                        spinner_violation[i] = new Spinner(getApplicationContext());
                        tv_dynamic_vltn_name[i] = new TextView(getApplicationContext());
                        check_dynamic_vltn[i] = new CheckBox(getApplicationContext());

                        LinearLayout.LayoutParams sp_params = new LinearLayout.LayoutParams(90,
                                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
                        sp_params.setMargins(0, 15, 5, 15);

                        spinner_violation[i].setId(Integer.parseInt(violation_offence_Code.get(i)));
                        spinner_violation[i].setGravity(Gravity.CENTER_VERTICAL);
                        spinner_violation[i].setVisibility(View.GONE);

                        ArrayAdapter<String> ap_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
                                android.R.id.text1, spinner_selectors);
                        ap_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_violation[i].setAdapter(ap_adapter);

                        // spinner_violation[i].setAdapter(new MyAdapter(this,
                        // R.layout.spinner_item, spinner_selectors));

                        spinner_violation[i].setBackgroundColor(R.drawable.navi_blue_btn_style);
                        spinner_violation[i].setPopupBackgroundResource(R.drawable.navi_blue_btn_style);
                        spinner_violation[i].setLayoutParams(sp_params);

                        ll_dynamic_vltns[i].addView(spinner_violation[i]);

                        vioCodeDescMap.put("" + spinner_violation[i].getId(), ServiceHelper.violation_detailed_views[i][2]
                                + " ( " + ServiceHelper.violation_detailed_views[i][1] + " ) ");
                        vioDetainFlags.put("" + spinner_violation[i].getId(), ServiceHelper.violation_detailed_views[i][7]);


                        //  if(ServiceHelper.violation_detailed_views!=null && ServiceHelper.violation_detailed_views.length==8) {
                        //     vioDetainFlags.put("" + spinner_violation[i].getId(), ServiceHelper.violation_detailed_views[i][7]);
                        //   }

                        /* CHECKBOX START */
                        int identifier = getResources().getIdentifier(
                                getApplicationContext().getPackageName() + ":drawable/custom_chec_box", null, null);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.FILL_PARENT);
                        params1.weight = 1.0f;

                        check_dynamic_vltn[i].setText("  " + ServiceHelper.violation_detailed_views[i][2] + " ( "
                                + ServiceHelper.violation_detailed_views[i][1] + " ) ");

                        check_dynamic_vltn[i].setTextAppearance(getApplicationContext(), R.style.navi_text_style);

                        check_dynamic_vltn[i].setId(Integer.parseInt(violation_offence_Code.get(i)));


                        check_dynamic_vltn[i].setButtonDrawable(identifier);

                        check_dynamic_vltn[i].setLayoutParams(params1);

					/*// default 41(CP) enable for towing
					if (Dashboard.check_vhleHistory_or_Spot.equals("towing")) {
						check_dynamic_vltn[0].setChecked(true);
						violation_checked_violations.add("" +check_dynamic_vltn[0]);
					}*/

                        ll_dynamic_vltns[i].addView(check_dynamic_vltn[i]);

                        /* CHECKBOX END */

                        check_all_ids.put("" + i, "" + spinner_violation[i].getId());
                        //
                        // /* RADIO GROUP END */
                        //
                        ll_dynamic_violations_root_static.addView(ll_dynamic_vltns[i]);

                        /* DYNAMIC RADIO BUTTONS CLICK EVENT END */
                        /*----------------------------------------------------------*/

                        check_dynamic_vltn[i].setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                check = (CheckBox) v;

                                // check.append("75");

     /*                           if (!et_driver_lcnce_num_spot.getText().toString().equalsIgnoreCase("")
                                        || !et_driver_lcnce_num_spot.getText().toString().equalsIgnoreCase(null)) {

                                    if (et_driver_lcnce_num_spot.getText().length() >= 5) {


                                        if (check.getId() == 64 || check.getId() == 123) {
                                            ShowMessageDL(
                                                    "\nWith out DL Section is not allowed when Offender had Driving License !\n");
                                        }

                                    }

                                }*/

                                if (check.isChecked()) {
                                    violation_checked_violations.add("" + check.getId());
                                    //

                                    if ((check.getId() == 7 || check.getId() == 07) || (check.getId() == 9)
                                            || (check.getId() == 49)) {
                                        Intent extra_pasnger = new Intent(getApplicationContext(), ExtraPassengers.class);
                                        startActivity(extra_pasnger);
                                    }
                                } else {

                                    violation_checked_violations.remove("" + check.getId());
                                }
                            }
                        });
                    }
                } else {
                    removeDialog(DYNAMIC_VIOLATIONS);
                }

                dg_dynmic_violtns.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        // TODO Auto-generated method stub

                        if (KeyEvent.KEYCODE_BACK == keyCode) {

                            for (String key : violation_checked_violations) {// 77,01,02
                                // getting key for offence code from all
                                for (Map.Entry<String, String> entry : check_all_ids.entrySet()) {// all

                                    if (entry.getValue().equals(key)) {

                                        String selectedId = spinner_violation[Integer.parseInt(entry.getKey())]
                                                .getSelectedItem().toString();

                                        DBHelper db1 = new DBHelper(E_Challan_Non_ContactModule.this);
                                        try {
                                            String query = "select * from " + DBHelper.violationPointsTable + "  ";
                                            db1.open();
                                            Cursor cursor = DBHelper.db.rawQuery(query, null);
                                            boolean validPid = false;

                                            if (cursor.moveToFirst()) {
                                                validPid = true;

                                                do {
                                                    String offence_vptable = cursor.getString(1);
                                                    String wheelerCOde = cursor.getString(2);
                                                    String penalityPoints = cursor.getString(3);


                                                } while (cursor.moveToNext());
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            // showToast(e.getMessage());
                                        } finally {
                                            db1.close();
                                        }

                                        if (("7".equals(key) || "07".equals(key))
                                                || ("9".equals(key) || "09".equals(key))) {
                                            passngerFLG = true;

                                            grand_total = grand_total
                                                    + ((Integer.parseInt(selectedId.substring(5, selectedId.length()))))
                                                    + (Integer.parseInt(extraPassengers) * 100);


                                            /*total = 0.0;
                                            total = grand_total
                                                    + VehicleHistoryPendingChallans.total_amount_selected_challans;
                                            tv_violation_amnt.setText("Rs . " + grand_total);
                                            tv_grand_total_spot.setText("Rs . " + total);*/

                                        } else {
                                            passngerFLG = false;

                                            grand_total = grand_total
                                                    + (Integer.parseInt(selectedId.substring(5, selectedId.length())));

                                          /*  total = 0.0;
                                            total = grand_total
                                                    + VehicleHistoryPendingChallans.total_amount_selected_challans;

                                            tv_violation_amnt.setText("Rs . " + grand_total);
                                            tv_grand_total_spot.setText("Rs . " + total);*/
                                        }

                                        /* framing violation buffer */

                                        violations_details_send.append(
                                                key.trim() + "@" + selectedId.substring(5, selectedId.length()).trim() + "@"
                                                        + selectedId.substring(5, selectedId.length()).trim() + "@");

                                        for (String offenceCodes : vioCodeDescMap.keySet()) {
                                            if (offenceCodes.trim().equals(key.trim())) {
                                                violations_details_send
                                                        .append("" + vioCodeDescMap.get(offenceCodes.trim()).trim());

                                                violation_desc_append
                                                        .append("" + vioCodeDescMap.get(offenceCodes.trim()).trim());
                                                violation_desc_append.append(",");
                                            }
                                        }


                                        for (String offenceCodes : vioDetainFlags.keySet()) {

                                            for (String code : violation_checked_violations) {
                                                if (code.equals(offenceCodes)) {

                                                    if ("1".equals(vioDetainFlags.get(offenceCodes))) {
                                                        vioDetainCheckFlag = "1";
                                                        break;
                                                    }
                                                }
                                            }
                                        }


                                        violations_details_send.append("!");
                                        btn_violation.setText(violation_desc_append);
                                    }
                                }
                            }
                            removeDialog(DYNAMIC_VIOLATIONS);
                            /* NO OF PEOPLE CALCULATING */
                            removeDialog(DYNAMIC_VIOLATIONS);

                            /*---------TO ENABLE EDITEXT WHEN EXTRA PASSENGERS : 07 IS SELECTED---------*/
                            int status = 0;
                            for (int i = 0; i < violation_checked_violations.size(); i++) {
                                violation_code_value = violation_checked_violations.get(i);
                                // String ccc =
                                // ""+violationPoints_offnce_code[count];
                                if ((violation_checked_violations.get(i).toString().equals("7"))) {
                                    status = 1;
                                }
                            }
                           /* if (status == 1) {
                                ll_extra_people.setVisibility(View.GONE);
                            } else {
                                ll_extra_people.setVisibility(View.GONE);
                            }*/
                        }
                        return true;
                    }
                });
                /* DYNAMIC LAYOUTS END */
                return dg_dynmic_violtns;

            default:
                break;
        }

        return super.onCreateDialog(id);
    }

    public class Async_getvehRemarks extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                ServiceHelper.getVehRemarks(completeVehicle_num_send, "", "");
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
            if (ServiceHelper.offender_remarks != null && !"0".equals(ServiceHelper.offender_remarks)) {

                try {

                    ShowMessage(ServiceHelper.offender_remarks);

                    if (ServiceHelper.offender_remarks.contains("FAKE NO PLATE")) {
                        cb_fake.setChecked(true);

                        btn_whlr_code.setVisibility(View.GONE);
                        btn_violation.setVisibility(View.GONE);
                        //   btn_submit.setText("Update Fake Info");
                        btn_submit.setVisibility(View.GONE);


                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();


                }
            }
        }
    }


    public class Async_getRTADetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            message = new StringBuffer();


            completeVehicle_num_send = ("" + regncode_send + "" + regnName_send + "" + vehicle_num_send);

            try {
                Dashboard.rta_details_request_from = "";
                ServiceHelper.getRTADetails("" + completeVehicle_num_send);
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

            if ((ServiceHelper.rta_data != null && !ServiceHelper.rta_data.equals("0"))) {


                if (isOnline()) {
                    Async_getvehRemarks getvehRemarks = new Async_getvehRemarks();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

                        getvehRemarks.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        getvehRemarks.execute();
                    }

                }

                try {

                    E_Challan_Non_ContactModule.rta_details_spot_master = new String[0];


                    E_Challan_Non_ContactModule.rta_details_spot_master = ServiceHelper.rta_data.split("!");

                    E_Challan_Non_ContactModule.Wheeler_check = rta_details_spot_master[0].split(":");

                    String Wheeler_Enable_check = E_Challan_Non_ContactModule.Wheeler_check[1].toString();

                    if (!Wheeler_Enable_check.equalsIgnoreCase("NA")) {
                        btn_whlr_code.setClickable(false);
                    } else {
                        btn_whlr_code.setClickable(true);
                    }


                    // start to display details as card system
                    rl_rta_details_layout.setVisibility(View.VISIBLE);
                    tv_vehicle_details_header_spot.setText("VEHICLE DETAILS");
                    tvregno_spotchallan_xml.setText("" + completeVehicle_num_send);
                    tvownername_spotchallan_xml.setText("" + rta_details_spot_master[1] != null ? rta_details_spot_master[1] : "NA");
                    tv_addr_spotchallan_xml.setText("" + rta_details_spot_master[2] != null ? rta_details_spot_master[2] : "NA" + ", " + rta_details_spot_master[3] != null ? rta_details_spot_master[3] : "NA");
                    // tv_city_spot.setText("" + rta_details_spot_master[3]);
                    tv_makername_spotchallan_xml.setText("" + rta_details_spot_master[4] != null ? rta_details_spot_master[4] : "NA" + ", " + rta_details_spot_master[5] != null ? rta_details_spot_master[5] : "NA" + ", "
                            + rta_details_spot_master[6] != null ? rta_details_spot_master[6] : "NA");
                    // tv_maker_class_spot.setText("" + rta_details_spot_master[5]);
                    // tv_color_spot.setText("" + rta_details_spot_master[6]);
                    tv_engineno_spotchallan_xml.setText("" + rta_details_spot_master[7] != null ? rta_details_spot_master[7] : "NA");
                    tv_chasis_spotchallan_xml.setText("" + rta_details_spot_master[8] != null ? rta_details_spot_master[8] : "NA");

                    rtaOname = tvownername_spotchallan_xml.getText().toString();
                    rtaAdres = tv_addr_spotchallan_xml.getText().toString();
                    city = rta_details_spot_master[3] != null ? rta_details_spot_master[3] : "NA";
                    mobileNo = rta_details_spot_master[14] != null ? rta_details_spot_master[14] : "NA";


                    if (rta_details_spot_master[0].equals("NA")) {
                        tv_vehicle_details_header_spot.setText("VEHICLE DETAILS NOT FOUND!");
                        rl_rta_details_layout.setVisibility(View.GONE);

                    } else if (rta_details_spot_master != null && rta_details_spot_master[9] != null
                            && !"NA".equals(rta_details_spot_master[9])) {

                        whlr_code_send = rta_details_spot_master[9] != null ? rta_details_spot_master[9] : "NA";// WHEELER CODE
                        if (whlr_code_send != null) {
                            btn_whlr_code.setText("" + whlr_code_send);
//                            start = System.currentTimeMillis();
//                            Log.i("TIME>>>END>>>Async_getViolations",String.valueOf(start));


                            if (isOnline()) {
                                new Async_getViolations().execute();
                            } else {
                                showToast("Please Check Your Network Connection For Getting Violation Details");
                            }
                        } else {
                            btn_whlr_code.setClickable(true);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                // tv_vehicle_details_header_spot.setText("VEHICLE DETAILS NOT FOUND!");
                rl_rta_details_layout.setVisibility(View.GONE);
                // showToast("No Details Found !");
            }
        }
    }

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
            if (ServiceHelper.violation_detailed_views != null && ServiceHelper.violation_detailed_views.length > 0) {
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
            } else {
                showToast("Violation Data Not Found");
            }

        }
    }

    public class Async_generateChallan extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            ServiceHelper.generateNonContactChallan(regCd, vehiclenumber, whlr_code_send, rtaOname, rtaAdres, city, mobileNo, offenceDateandTime, pidCode, pidName, cadre_code, cadre_name, psCd, psName,
                    Dashboard.UNIT_CODE, Dashboard.UNIT_NAME, point_code_send_from_settings, point_name_send_from_settings,
                    violations_details_send + "", vehRemarks, imgString, imei_send, macAddress, String.valueOf(latitude), String.valueOf(longitude));


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

            Log.i("Final Submit Result", ServiceHelper.noncontactresponse);
            if (ServiceHelper.noncontactresponse != null && ServiceHelper.noncontactresponse.length() > 0) {

                if ("0".equals(ServiceHelper.noncontactresponse)) {
                    showToast("Ticket Genration Failed Due to Error");
                } else if ("1".equalsIgnoreCase(ServiceHelper.noncontactresponse)) {
                    showToast("Fake Number Updated");
                    resetAftersubmit();
                } else if ("2".equalsIgnoreCase(ServiceHelper.noncontactresponse)) {
                    showToast("Ticket Generation Failed Due to Image Data error");
                } else {
                    showToast("Ticket Genereted Successfully");
                    resetAftersubmit();
                }

            } else {
                showToast("Ticket Genration Failed Due to Network");
            }

        }
    }

    public void LoadUIcomponents() {

        encroachment_image = (ImageView) findViewById(R.id.encroachment_image);
        next_image = (Button) findViewById(R.id.next_image);
        btngetrtadetails = (Button) findViewById(R.id.btngetrtadetails);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_whlr_code = (Button) findViewById(R.id.btn_whlr_code);
        btn_violation = (Button) findViewById(R.id.btn_violation);
        cb_fake = (CheckBox) findViewById(R.id.cb_fake);
        ed_date = (Button) findViewById(R.id.ed_date);
        ed_time = (Button) findViewById(R.id.ed_time);
        edt_regncid_spotchallan_xml = (EditText) findViewById(R.id.edt_regncid_spotchallan_xml);
        edt_regncidname_spotchallan_xml = (EditText) findViewById(R.id.edt_regncidname_spotchallan_xml);
        edt_regncid_lastnum_spotchallan_xml = (EditText) findViewById(R.id.edt_regncid_lastnum_spotchallan_xml);

        tvregno_spotchallan_xml = (TextView) findViewById(R.id.tvregno_spotchallan_xml);
        tvownername_spotchallan_xml = (TextView) findViewById(R.id.tvownername_spotchallan_xml);
        tv_addr_spotchallan_xml = (TextView) findViewById(R.id.tv_addr_spotchallan_xml);
        tv_makername_spotchallan_xml = (TextView) findViewById(R.id.tv_makername_spotchallan_xml);
        tv_chasis_spotchallan_xml = (TextView) findViewById(R.id.tv_chasis_spotchallan_xml);
        tv_engineno_spotchallan_xml = (TextView) findViewById(R.id.tv_engineno_spotchallan_xml);

        rl_rta_details_layout = (RelativeLayout) findViewById(R.id.rl_detailsresponse_spotchallan_xml);
        tv_vehicle_details_header_spot = (TextView) findViewById(R.id.textView_regdetails_header_spotchallan_xml);


        edt_regncid_spotchallan_xml.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edt_regncid_spotchallan_xml.getText().toString().length() == edt_regncid_spotchallanMAX_LENGTH) {
                    edt_regncidname_spotchallan_xml.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        edt_regncidname_spotchallan_xml.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edt_regncidname_spotchallan_xml.getText().toString().length() == edt_regncidname_spotchallanLENGTH) {
                    edt_regncid_lastnum_spotchallan_xml.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edt_regncid_lastnum_spotchallan_xml.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edt_regncid_lastnum_spotchallan_xml.getText().toString().length() == edt_regncid_lastnum_spotchallanMAX_LENGTH) {
                    btngetrtadetails.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

    protected void selectImage() {
        // TODO Auto-generated method stub
        final CharSequence[] options = {"Open Camera", "Open FileManager", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(E_Challan_Non_ContactModule.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Open Camera")) {
                    if (Build.VERSION.SDK_INT <= 23) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 1);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(E_Challan_Non_ContactModule.this,
                                BuildConfig.APPLICATION_ID + ".provider", f));
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(intent, 1);
                    }

                } else if (options[item].equals("Open FileManager")) {

              /*      Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);*/

                  /*  File root = new File(Environment.getExternalStorageDirectory().getPath() + "/e_challan/");
                    Uri uri =  FileProvider.getUriForFile(E_Challan_Non_ContactModule.this,
                            BuildConfig.APPLICATION_ID + ".provider",root);


                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivityForResult(intent, 7);*/


                    if (Build.VERSION.SDK_INT <= 23) {

                        File root = new File(Environment.getExternalStorageDirectory() + "/e_challan/");
                        Uri uri = Uri.fromFile(root);


                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setDataAndType(uri, "resource/folder");
                        startActivityForResult(Intent.createChooser(intent, "Open folder"), 7);

                    } else {


                        File root = new File(Environment.getExternalStorageDirectory() + "/e_challan/");
                        Uri uri = FileProvider.getUriForFile(E_Challan_Non_ContactModule.this,
                                BuildConfig.APPLICATION_ID + ".provider", root);


                        Intent intent = new Intent();

                        //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setDataAndType(uri, "image/*" + "");

                        startActivityForResult(Intent.createChooser(intent, "Open folder"), 7);


                    }

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            String picturePath = "";
            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    //      current_date = new DateUtil().getPresentDateandTime();

                    String presentdate = new DateUtil().getTodaysDate();
                    String presenttime = new DateUtil().getPresentTime();

                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    String path = android.os.Environment
                            .getExternalStorageDirectory() + "/"
                            + "e_challan" + "/" + new DateUtil().getPresentyear()
                            + "/" + new DateUtil().getPresentMonth() + "/" + new DateUtil().getPresentDay();
                    File camerapath = new File(path);

                    if (!camerapath.exists()) {
                        camerapath.mkdirs();
                    }
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, new DateUtil().getPresentDateandTime() + ".jpg");
                    try {

                        outFile = new FileOutputStream(file);
                        mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into

                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(80);
                        paint.setTextAlign(Paint.Align.CENTER);

                        int xPos = (canvas.getWidth() / 2);
                        int yPos = (int) ((canvas.getHeight() / 2) - ((paint
                                .descent() + paint.ascent()) / 2));

                       /* canvas.drawText("Date & Time: " + presentdate+" "+presenttime, xPos,
                                yPos + 800, paint);
                        canvas.drawText("Lat :" + latitude , xPos, yPos + 900, paint);
                        canvas.drawText("Long :"+ longitude, xPos, yPos + 1000, paint);
                        canvas.drawText("Date & Time: "+ presentdate+" "+presenttime+"\n"+" Lat :"+latitude+ " Long :"+longitude,1250, 1500, paint);
*/
                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Date & Time: " + presentdate + " " + presenttime, xPos + 10, yPos, paint);
                        canvas.restore();

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 900, paint);
                        canvas.restore();

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 1000, paint);
                        canvas.rotate(90);
                        canvas.restore();

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

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //   Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into

                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(80);
                    paint.setTextAlign(Paint.Align.CENTER);
                    int xPos = (canvas.getWidth() / 2);
                    int yPos = (int) ((canvas.getHeight() / 2) - ((paint
                            .descent() + paint.ascent()) / 2));

               /*     canvas.drawText("Date & Time: " + presentdate+" "+presenttime, xPos,
                            yPos + 800, paint);
                    canvas.drawText("Lat :" + latitude , xPos, yPos + 900, paint);
                    canvas.drawText("Long :"+ longitude, xPos, yPos + 1000, paint);
*/
                /*    canvas.save();
                    canvas.rotate(270f, xPos, yPos);
                    canvas.drawText("Date & Time: " +  presentdate+" "+presenttime, xPos + 10, yPos, paint);
                    canvas.restore();

                    canvas.save();
                    canvas.rotate(270f, xPos, yPos);
                    canvas.drawText("Lat :" + latitude, xPos, yPos + 900, paint);
                    canvas.restore();

                    canvas.save();
                    canvas.rotate(270f, xPos, yPos);
                    canvas.drawText("Long :" + longitude, xPos, yPos + 1000, paint);
                    canvas.rotate(90);
                    canvas.restore();*/
                    //canvas.drawText("Date & Time: "+Current_Date+"\n"+" Lat :"+latitude+ " Long :"+longitude,1250, 1500, paint);

                    encroachment_image.setImageBitmap(mutableBitmap);
                    //  encroachment_image.setRotation(encroachment_image.getRotation() + 90);

                    //picture1.setRotation(90);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);

                    byteArray = bytes.toByteArray();

                    imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                    ed_time.setText(presenttime);
                    ed_date.setText(presentdate);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
           /* else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into

                Paint paint= new Paint();
                paint.setColor(Color.RED);
                paint.setTextSize(100);
                paint.setTextAlign(Paint.Align.CENTER);
                //canvas.drawText("Date & Time: "+Current_Date+"\n"+" Lat :"+latitude+ " Long :"+longitude,1250, 1500, paint);

                int xPos = (canvas.getWidth() / 2);
                int yPos = (int) ((canvas.getHeight() / 2) - ((paint
                        .descent() + paint.ascent()) / 2));

//                	canvas.drawText("Date & Time: " + Current_Date, xPos,yPos + 800, paint);
//                	canvas.drawText("Lat :" + latitude , xPos, yPos + 900, paint);
//                	canvas.drawText("Long :"+ longitude, xPos, yPos + 1000, paint);

                encroachment_image.setImageBitmap(mutableBitmap);
                //picture1.setRotation(90);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mutableBitmap.compress(Bitmap.CompressFormat.JPEG,20, bytes);

                byteArray = bytes.toByteArray();

                imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);

            }*/
            else if (requestCode == 7) {
             /*   String[]  filePath = {data.getData().getPath()};
                Uri selectedImage = data.getData();*/

     /*           Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into

                Paint paint= new Paint();
                paint.setColor(Color.RED);
                paint.setTextSize(100);
                paint.setTextAlign(Paint.Align.CENTER);
                //canvas.drawText("Date & Time: "+Current_Date+"\n"+" Lat :"+latitude+ " Long :"+longitude,1250, 1500, paint);

                int xPos = (canvas.getWidth() / 2);
                int yPos = (int) ((canvas.getHeight() / 2) - ((paint
                        .descent() + paint.ascent()) / 2));

//                	canvas.drawText("Date & Time: " + Current_Date, xPos,yPos + 800, paint);
//                	canvas.drawText("Lat :" + latitude , xPos, yPos + 900, paint);
//                	canvas.drawText("Long :"+ longitude, xPos, yPos + 1000, paint);

                encroachment_image.setImageBitmap(mutableBitmap);
                //picture1.setRotation(90);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mutableBitmap.compress(Bitmap.CompressFormat.JPEG,20, bytes);

                byteArray = bytes.toByteArray();

                imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
*/


                // SDK < API11
                if (Build.VERSION.SDK_INT < 11)
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                setTextViews(Build.VERSION.SDK_INT, data.getData().getPath(), realPath);
            }

        }

    }

    private void setTextViews(int sdk, String uriPath, String realPath) {

/*        this.txtSDK.setText("Build.VERSION.SDK_INT: "+sdk);
        this.txtUriPath.setText("URI Path: "+uriPath);
        this.txtRealPath.setText("Real Path: "+realPath);*/

        Uri uriFromPath = Uri.fromFile(new File(realPath));

        // you have two ways to display selected image

        // ( 1 ) imageView.setImageURI(uriFromPath);

        // ( 2 ) imageView.setImageBitmap(bitmap);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));

            mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (realPath.contains("e_challan")) {
            try {
                filenameselected = realPath.substring(realPath.lastIndexOf("/") + 1);

                String[] dateandtimefromfile = filenameselected.split("@");
                String time = dateandtimefromfile[1].substring(0, 5);

                ed_date.setText(dateandtimefromfile[0].toString());
                ed_date.setEnabled(false);
                ed_time.setText(time);
                ed_time.setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
           /* ed_date.setEnabled(true);
            ed_time.setEnabled(true);*/

            Canvas canvas = new Canvas(mutableBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(60);
            paint.setTextAlign(Paint.Align.CENTER);
            int xPos = (canvas.getWidth() / 2);
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

            String presentdate = new DateUtil().getTodaysDate();
            String presenttime = new DateUtil().getPresentTime();

            canvas.drawText("Date & Time: " + presentdate + " " + presenttime, xPos, yPos + 300, paint);
            canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
            canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);

            ed_date.setText(presentdate);
            ed_time.setText(presenttime);


        }
        encroachment_image.setImageBitmap(bitmap);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
        byteArray = bytes.toByteArray();
        imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);

    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

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
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

    private void getLocation() {
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
                String current_time;
                String current_date = null;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (m_locationlistner != null) {
                        location = m_locationlistner.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            long time = location.getTime();
                            Date date = new Date(time);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String gps_Date = sdf.format(date);
                            System.out.println(gps_Date);


                            //new Async_GetGPS_Address().execute();

                        } else {
                            latitude = 0.0;
                            longitude = 0.0;
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        m_locationlistner.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (m_locationlistner != null) {
                            location = m_locationlistner.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                long time = location.getTime();
                                Date date = new Date(time);


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String gps_Date = sdf.format(date);
                                System.out.println(gps_Date);

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

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void ShowMessage(final String Message) {

        E_Challan_Non_ContactModule.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView title = new TextView(E_Challan_Non_ContactModule.this);
                title.setText("ALERT");
                title.setBackgroundColor(Color.RED);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(E_Challan_Non_ContactModule.this,
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

    public void resetAftersubmit() {
       /* File file = new File(realPath);
        boolean deleted = file.delete();
        Log.d("gdjFsd",""+deleted);*/

        completeVehicle_num_send = "";
        edt_regncid_spotchallan_xml.setText("");
        edt_regncidname_spotchallan_xml.setText("");
        edt_regncid_lastnum_spotchallan_xml.setText("");
        regncode_send = "";
        regnName_send = "";
        vehicle_num_send = "";
        city = "";
        rtaOname = "";
        rtaAdres = "";
        mobileNo = "";
        regCd = null;
        vehiclenumber = null;
        offenceDateandTime = null;
        vehRemarks = null;
        tot = "0";
        tvregno_spotchallan_xml.setText("");
        tvownername_spotchallan_xml.setText("");
        tv_addr_spotchallan_xml.setText("");
        tv_makername_spotchallan_xml.setText("");
        tv_chasis_spotchallan_xml.setText("");
        tv_engineno_spotchallan_xml.setText("");
        tv_vehicle_details_header_spot.setText("");
        ed_date.setText("DATE");
        ed_time.setText("TIME");
        cb_fake.setText("IS IT FAKE VEHICLE");
        cb_fake.setChecked(false);
        imgString = "";
        btn_whlr_code.setText("Select Wheeler Code");
        btn_violation.setText("Select Violation");
        encroachment_image.setRotation(0);
        encroachment_image.setImageDrawable(getResources().getDrawable(R.drawable.camera));
        rl_rta_details_layout.setVisibility(View.GONE);
    }
}

