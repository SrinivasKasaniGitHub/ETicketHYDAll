package com.mtpv.mobilee_ticket;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
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
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatTextView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.AdapterView;
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
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mtpv.imagematch.BleAdapter;
import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.DateUtil;
import com.mtpv.mobilee_ticket_services.ServiceHelper;
import com.mtpv.mobilee_ticket_services.SharedPrefsHelper;
import com.mtpv.mobilee_ticket_services.Utils;
import com.mtpv.mobilee_ticket_services.VibratorUtils;
import com.mtpv.spinnermdl.VehCatAdapter;
import com.mtpv.spinnermdl.VehCatModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import app.justec.com.bleoperator.BleManager;
import app.justec.com.bleoperator.callback.BleGattCallback;
import app.justec.com.bleoperator.callback.BleRssiCallback;
import app.justec.com.bleoperator.callback.BleScanCallback;
import app.justec.com.bleoperator.comm.BleDevice;
import app.justec.com.bleoperator.data.ReceiveFormDataResult;
import app.justec.com.bleoperator.data.RecordForm;
import app.justec.com.bleoperator.event.EventManger;
import app.justec.com.bleoperator.exception.BleException;
import app.justec.com.bleoperator.helper.DataSource;
import app.justec.com.bleoperator.helper.RepeatCommand;
import app.justec.com.bleoperator.scan.BleScanRuleConfig;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("DefaultLocale")
public class Drunk_Drive extends Activity implements OnClickListener, LocationListener,
        DataSource.DataCallBack<ArrayList<RecordForm>>,
        RepeatCommand {

    @SuppressWarnings("unused")
    private String HALLOWEEN_ORANGE = "#FF7F27";

    public static boolean dd_dobFLG = false;

    Button dob_input;
    public static boolean rtaFlG = false, licFLG = false, adhrFLG = false;

    public static LinearLayout vehicle_type;
    public static CheckBox police_vehcle, govt_vehcle;

    public static EditText et_regcid, et_vchl_num, et_last_num, edt_checkslno_, edt_alchl_reading;

    public static EditText et_driver_lcnce_num, et_aadharnumber;

    TextView dl_no, tv_lcnce_address, tv_licnce_ownername, tv_lcnce_phone_number, tv_lcnce_father_name, tv_aadhar_header, tv_aadhar_user_name, tv_aadhar_care_off, tv_aadhar_mobile_number, tv_aadhar_gender, tv_aadhar_uid;
    ImageView dl_img;

    public static String[] offender_remarks_resp_master;

    private int mYear, mMonth, mDay;

    public static TextView tv_aadhar_address, tv_aadhar_dob;
    ImageView img_aadhar_image;

    String driverName = null, driverAddress = null, driverCity = null;
    LinearLayout ll_aadhar_layout;

    RelativeLayout rl_rta_details_layout, rl_lcnce_Details;

    TextView tv_vhle_no, tv_owner_name, tv_address, tv_maker_name, tv_engine_no, tv_chasis_no, tv_licence_details;

    public static TextView tv_vehicle_details, tv_dlpoints_spotchallan_xml;

    Button btn_get_details, btn_get_pending_details, btn_cancel, btn_generate_dd_Case, btn_wheler_code, btn_scan_dd_xml;

    ImageButton ibtn_capture;
    WebView wv_img_captured;

    Utils utils;

    String NETWORK_TXT = "";
    String imei_send = "";
    String simid_send = "";

    final int PROGRESS_DIALOG = 0;
    final int WHEELER_CODE = 1;
    final int FAKE_NUMBERPLATE_DIALOG = 2;
    String fake_plate_details;

    TelephonyManager telephonyManager;

    LocationManager m_locationlistner;
    android.location.Location location;
    double latitude = 0.0;
    double longitude = 0.0;
    String provider = "";

    /* to pass to generate challan */
    public static String details_regncid = "";
    public static String details_regncid_name = "";
    public static String details_regn_last_num = "";
    public static String details_owner_dl_no = "";
    public static String details_driver_dl_no = "";

    public static String[] rta_details_master, Wheeler_check;
    public static String[] licene_details_master, rtaAprroved_Master;
    // public static String licence_status = "1";
    public static String licence_status = "", dl_points = "0";

    public static String completeVehicle_num_send = "", regncode_send = "", regnName_send = "", vehicle_num_send = "",
            fake_veh_chasisNo = "";

    public static String aadhaar;
    public static String licence_no;

    DBHelper db;
    Cursor c_whlr;
    Cursor c_occptn;
    Cursor c_qlfctn;
    Cursor c_vchle_cat;
    Cursor c_vhcle_main;

    String dobcheck = "No";

    private static final int CAMERA_REQUEST = 1888;

    public static String picturePath = "";

    Bitmap bt;
    Bitmap bitmap;
    String base64_str = "";
    FileOutputStream fo;
    WebviewLoader webviewloader;

    int edt_regncid_spotchallanMAX_LENGTH = 4;
    int edt_regncidname_spotchallanLENGTH = 4;
    int edt_regncid_lastnum_spotchallanMAX_LENGTH = 4;

    String[] wheeler_code_arr, wheeler_name_arr;// wheeler code details
    /* WHELLER DETAILS W_CODE & W_NAME */
    String wheler_code = "Select Wheeler";

    int selected_wheller_code = -1;

    public static String whlr_code_send = "";

    public static byte[] ba = null;

    public static RadioGroup radioGrp_regNo_EngnNo_Chasis;

    public static RadioButton radioGroupButton_regNo, radioGroupButton_engineNo, radioGroupButton_chasisNo;

    public static LinearLayout ll_mainsub_root, ll_engineNo, ll_chasisNo, ll_regno;

    public static EditText et_engineNo, et_chasisNo;

    public static String RegNo_EngNo_ChasisNo = "";

    public static boolean EngneFLG = false, regNoFLG = false, chasisFLG = false, veh_HisFLG = false;

    public static int isitTr = 1;

    TextView regnoText;
    String[] wheeler_code_arr_spot, wheeler_name_arr_spot;
    public static String pidCode = null, pidName = null, psCd = null, psName = null, cadre_code = null,
            cadre_name = null, pass_word = null, off_phone_no = null, current_version = null, rta_data_flg = null,
            dl_data_flg = null, aadhaar_data_flg = null, otp_no_flg = null, cashless_flg = null, mobileNo_flg = null;

    public static ImageView offender_image;

    public static String image_data_tosend = null;

    byte[] byteArray;

    static String date;

    public static String Current_Date;

    public static int WHITE = 0xFFFFFFFF;
    public static int BLACK = 0xFF000000;
    public final static int width = 500;

    private Calendar cal;

    @SuppressWarnings("unused")
    private int day;

    @SuppressWarnings("unused")
    private int month;

    private int year;

    ImageView qr_code;

    public static String owner_image_data = null;

    public static byte[] owner_imageByteArray = null;

    public static String dd_dob_DL = null, theftRemarkFlag = "N";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    int challantype = 0, casesLimit = 0, casesBooked = 0;

    String bookedPScode_send_from_settings, bookedPSname_send_from_settings, point_code_send_from_settings, point_name_send_from_settings,
            exact_location_send_from_settings;

    public static String vehCatgryCd = "9";

    ImageView img_logo;
    TextView officer_Name, officer_Cadre, officer_PS;
    TextView textView_header_spot_challan_xml;

    BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private ListView list_BleDevice;
    private BleAdapter adapter;
    private List<BleDevice> dataList = new ArrayList<>();
    private DataSource.DataCallBack<String> callBack;
    AlertDialog dlg_BleDevice;
    ProgressDialog progressDialog;
    LinearLayout lyt_DD_Details;
    BleDevice bleDevice;
    Button btn_vehCategory;

    @SuppressLint({"NewApi", "MissingPermission", "ObsoleteSdkInt", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.drunkdrive);
        LoadUIComponents();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBlueToothState();

        bleDevice = SharedPrefsHelper.getSavedObjectFromPreference(getApplicationContext(), "mPreference", "mLoginRes", BleDevice.class);
       /* if (null != bleDevice) {
            if (!BleManager.getInstance().isConnected(bleDevice)) {
                BleManager.getInstance().disconnect(bleDevice);
                connect(bleDevice);
            }
        } else {
            showToast("Please connect the Breath Analyzer from Settings Module !");
        }*/
        lyt_DD_Details = findViewById(R.id.lyt_DD_Details);
        progressDialog = new ProgressDialog(this);
        dd_dobFLG = false;
        dd_dob_DL = null;
        date = (DateFormat.format("dd/MM/yyyy hh:mm:ss", new java.util.Date()).toString());
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        Calendar c1 = Calendar.getInstance();
        int mSec = c1.get(Calendar.MILLISECOND);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strdate1 = sdf1.format(c1.getTime());
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        // id_date.setText(strdate1);
        Current_Date = strdate1;
        textView_header_spot_challan_xml = (TextView) findViewById(R.id.textView_header_spot_challan_xml);
        textView_header_spot_challan_xml.setText("Drunk drive");
        img_logo = (ImageView) findViewById(R.id.img_logo);
        if (MainActivity.uintCode.equals("22")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        } else if (MainActivity.uintCode.equals("23")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        } else if (MainActivity.uintCode.equals("24")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        } else if (MainActivity.uintCode.equals("44")) { //44 Warangal
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        } else { //  69 Siddipet
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        }

        officer_Name = (TextView) findViewById(R.id.officer_Name);
        officer_Cadre = (TextView) findViewById(R.id.officer_cadre);
        officer_PS = (TextView) findViewById(R.id.officer_PS);

        officer_Name.setText(MainActivity.pidName + "(" + MainActivity.cadre_name + ")");
        officer_Cadre.setText(MainActivity.cadre_name);
        officer_PS.setText(MainActivity.psName);
        btn_vehCategory = findViewById(R.id.btn_vehCategory);
        btn_vehCategory.setOnClickListener(this);


        // licence_status = "1";
        licence_status = "";

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

        bookedPScode_send_from_settings = preferences.getString("psname_code", "0");
        bookedPSname_send_from_settings = preferences.getString("psname_name", "psname");
        point_code_send_from_settings = preferences.getString("point_code", "0");
        point_name_send_from_settings = preferences.getString("point_name", "pointname");
        exact_location_send_from_settings = preferences.getString("ps_res_name_code", "0");
        //exact_location_send_from_settings = preferences.getString("exact_location", "location");


        rl_rta_details_layout.setVisibility(View.GONE);
        rl_lcnce_Details.setVisibility(View.GONE);

        if (android.os.Build.VERSION.SDK_INT > 11) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei_send = MainActivity.IMEI;//telephonyManager.getDeviceId();// TO GET IMEI NUMBER

        if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            simid_send = "" + telephonyManager.getSimSerialNumber();
        } else {
            simid_send = "";
        }

        db = new DBHelper(getApplicationContext());
        try {
            db.open();
            // WHEELER CODE
            c_whlr = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);
            if (c_whlr.getCount() == 0) {
                Log.i("WHEELER DB DETAILS", "0");
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

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup_licence);
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radioGroupButton_licenceyes:
                        licence_status = "";
                        licence_status = "1";
                        et_driver_lcnce_num.setEnabled(true);
                        break;

                    case R.id.radioGroupButton_licenceno:
                        licence_status = "";
                        licence_status = "0";
                        et_driver_lcnce_num.setText("");
                        et_driver_lcnce_num.setEnabled(false);
                        break;
                }
            }
        });

        /* TO GET WHEELER CODE DETAILS */
        try {
            db.open();
            // WHEELER CODE
            c_whlr = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);
            if (c_whlr.getCount() == 0) {
                Log.i("WHEELER DB DETAILS", "0");
            } else {

                wheeler_code_arr = new String[c_whlr.getCount()];
                wheeler_name_arr = new String[c_whlr.getCount()];

                int count = 0;
                while (c_whlr.moveToNext()) {
                    wheeler_code_arr[count] = c_whlr.getString(1);
                    wheeler_name_arr[count] = c_whlr.getString(2);
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

    }

    private void CheckBlueToothState() {
        // TODO Auto-generated method stub
        if (bluetoothAdapter == null) {
            showToast("Bluetooth NOT support");
        } else {
            if (bluetoothAdapter.isEnabled()) {
                if (bluetoothAdapter.isDiscovering()) {
                    showToast("Bluetooth is currently in device discovery process.");
                } else {
                    showToast("Bluetooth is Enabled.");

                }
            } else {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void LoadUIComponents() {

        et_regcid = (EditText) findViewById(R.id.edt_regncid_rtadetails_xml);
        et_vchl_num = (EditText) findViewById(R.id.edt_regncidname_rtadetails_xml);
        et_last_num = (EditText) findViewById(R.id.edt_regncid_lastnum_rtadetails_xml);
        edt_checkslno_ = findViewById(R.id.edt_checkslno_);
        edt_alchl_reading = findViewById(R.id.edt_alchl_reading);
        btn_scan_dd_xml = findViewById(R.id.btn_scan_dd_xml);
        tv_dlpoints_spotchallan_xml = (TextView) findViewById(R.id.tv_dlpoints_spotchallan_xml);

        offender_image = (ImageView) findViewById(R.id.offender_image);
        offender_image.setVisibility(View.GONE);
        et_regcid.requestFocus();

        et_regcid.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_regcid.getText().toString().length() == edt_regncid_spotchallanMAX_LENGTH) {
                    et_vchl_num.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_vchl_num.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_vchl_num.getText().toString().length() == edt_regncidname_spotchallanLENGTH) {
                    et_last_num.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_last_num.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_last_num.getText().toString().length() == edt_regncid_lastnum_spotchallanMAX_LENGTH) {
                    et_driver_lcnce_num.requestFocus();
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

        // hardcode
        dob_input = (Button) findViewById(R.id.dob_input);
        et_driver_lcnce_num = (EditText) findViewById(R.id.edt_driverdlno_rtadetails_xml);
        et_driver_lcnce_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!et_driver_lcnce_num.getText().toString().equalsIgnoreCase("")
                        && et_driver_lcnce_num.getText().toString().length() >= 5) {
                    dob_input.setVisibility(View.VISIBLE);
                    dob_input.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            {
                                final Calendar c = Calendar.getInstance();
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog datePickerDialog = new DatePickerDialog(Drunk_Drive.this,
                                        new DatePickerDialog.OnDateSetListener() {

                                            @Override
                                            public void onDateSet(DatePicker view, int year,
                                                                  int monthOfYear, int dayOfMonth) {
                                                SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");
                                                SimpleDateFormat date_parse = new SimpleDateFormat("dd/MM/yyyy");
                                                String dtdob = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                                try {
                                                    dd_dob_DL = date_format.format(date_parse.parse(dtdob));
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                //  dob_DL = date_format.format(dayOfMonth  + (monthOfYear + 1) + year);
                                                dob_input.setText(dd_dob_DL);
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
                    dd_dob_DL = null;
                }
            }
        });


        //et_driver_lcnce_num.setText("7461WGL1998OD");
        btn_get_details = (Button) findViewById(R.id.btngetrtadetails_rtadetails_xml);
        btn_get_pending_details = (Button) findViewById(R.id.btn_pendingchallans_rtadetails_xml);
        btn_cancel = (Button) findViewById(R.id.btncancel_rta_details_xml);
        btn_generate_dd_Case = (Button) findViewById(R.id.btngeneratechallan_rta_details_xml);
        btn_wheler_code = (Button) findViewById(R.id.btn_whlr_code_dd_xml);

        ibtn_capture = (ImageButton) findViewById(R.id.imgv_camera_capture_rta_details_xml);
        // wv_img_captured = (WebView)
        // findViewById(R.id.webView_rtadetails_xml);
        // wv_img_captured.setVisibility(View.GONE);

        rl_rta_details_layout = (RelativeLayout) findViewById(R.id.rl_detailsresponse_rtadetails_xml);
        rl_lcnce_Details = (RelativeLayout) findViewById(R.id.rl_licences_rtadetails_xml);

        tv_vhle_no = (TextView) findViewById(R.id.tvregno_rtadetails_xml);
        tv_owner_name = (TextView) findViewById(R.id.tvownername_rtadetails_xml);
        tv_address = (TextView) findViewById(R.id.tv_addr_rtadetails_xml);
        // tv_city = (TextView) findViewById(R.id.tv_city_rtadetails_xml);
        tv_maker_name = (TextView) findViewById(R.id.tv_makername_rtadetails_xml);
        // tv_maker_class = (TextView)
        // findViewById(R.id.tv_makerclass_rtadetails_xml);
        // tv_color = (TextView) findViewById(R.id.tv_color_rtadetails_xml);
        tv_engine_no = (TextView) findViewById(R.id.tv_engineno_rtadetails_xml);
        tv_chasis_no = (TextView) findViewById(R.id.tv_chasis_rtadetails_xml);

        /* LICENCE DETAILS */
        tv_licnce_ownername = (TextView) findViewById(R.id.tvlcnceownername_rtadetails_xml);
        tv_lcnce_father_name = (TextView) findViewById(R.id.tvlcnce_fname_rtadetails_xml);
        tv_lcnce_phone_number = (TextView) findViewById(R.id.tv_lcnce_mobnum_rtadetails_xml);
        tv_lcnce_address = (TextView) findViewById(R.id.tv_lcnce_Address_rtadetails_xml);
        dl_img = (ImageView) findViewById(R.id.dl_img);
        dl_no = (TextView) findViewById(R.id.dl_no);

        /* AADHAR DETAILS */
        // hardcode
        et_aadharnumber = (EditText) findViewById(R.id.edt_aadharno_rtadetails_xml);
        //et_aadharnumber.setText("322847907255");
        ll_aadhar_layout = (LinearLayout) findViewById(R.id.ll_aadhardetails_rtadetails_xml);
        tv_aadhar_header = (TextView) findViewById(R.id.tvheader_adhar_rtadetails_xml);
        tv_aadhar_user_name = (TextView) findViewById(R.id.tvaadharname_rtadetails_xml);
        tv_aadhar_care_off = (TextView) findViewById(R.id.tvcareof_rtadetails_xml);
        tv_aadhar_address = (TextView) findViewById(R.id.tvaddress_rtadetails_xml);
        tv_aadhar_mobile_number = (TextView) findViewById(R.id.tvmobilenumber_rtadetails_xml);
        tv_aadhar_gender = (TextView) findViewById(R.id.tvgender_rtadetails_xml);
        tv_aadhar_dob = (TextView) findViewById(R.id.tvdob_rtadetails_xml);
        tv_aadhar_uid = (TextView) findViewById(R.id.tvuid_rtadetails_xml);
        // tv_aadhar_eid = (TextView) findViewById(R.id.tveid_rtadetails_xml);
        qr_code = (ImageView) findViewById(R.id.qr_code);
        img_aadhar_image = (ImageView) findViewById(R.id.imgv_aadhar_photo_rtadetails_xml);
        ll_aadhar_layout.setVisibility(View.GONE);
        tv_aadhar_header.setVisibility(View.GONE);

        /* TO SHOW vehicel DETAILS AND LICENCE DETAILS FOUND OR NOT */
        tv_vehicle_details = (TextView) findViewById(R.id.textView_regdetails_header);
        tv_licence_details = (TextView) findViewById(R.id.textView_licence_header);

        btn_wheler_code.setOnClickListener(this);
        ibtn_capture.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_generate_dd_Case.setOnClickListener(this);
        btn_get_details.setOnClickListener(this);// RTA DETAILS
        btn_get_pending_details.setOnClickListener(this);

        utils = new Utils();
        NETWORK_TXT = getResources().getString(R.string.newtork_txt);

        rta_details_master = new String[0];
        licene_details_master = new String[0];

        ll_mainsub_root = (LinearLayout) findViewById(R.id.ll_mainsub_root);

        btn_scan_dd_xml.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog
                        .setMessage("Please wait \n BlueTooth Scan is in Process!!!");
                progressDialog.setCancelable(false);
                progressDialog.show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startScan();
                        initView();
                    }
                });

            }
        });

        EventBus.getDefault().register(this);
        initBle();
        EventManger.getInstance().receieDataCallback(this);
        EventManger.getInstance().repeatCallback(this);
        EventManger.getInstance().searchDataCallback(this, 10006);
        callBack = new DataSource.DataCallBack<String>() {
            @Override
            public void onDataNotAvailed(int i) {

            }

            @Override
            public void onDataLoaded(String s) {
                Log.i("bleble", s);
                if (s.equals("1")) {
                    Toast.makeText(Drunk_Drive.this, "initDeviceInfo", Toast.LENGTH_LONG).show();
                    Log.i("bleble0", "13");
                    EventManger.getInstance().deviceDisplayConfiger(callBack);

                } else if (s.equals("3")) {

                    Log.i("bleble0", "14");
                    EventManger.getInstance().fetchDeviceInfo(callBack);
                } else if (s.equals("4")) {

                    Log.i("bleble0", "15");
                    EventManger.getInstance().recordFormDisplay(callBack);


                }
            }
        };

    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.btngetrtadetails_rtadetails_xml:

                Fake_NO_Dialog.fake_action = null;
                tv_vehicle_details.setText("");
                tv_licence_details.setText("");
                image_data_tosend = null;


                ServiceHelper.rc_send = "";
                ServiceHelper.license_data = "";
                ServiceHelper.aadhar_data = "";
                SpotChallan.OtpStatus = "";
                SpotChallan.OtpResponseDelayTime = "";


                tv_vhle_no.setText("");
                tv_owner_name.setText("");
                tv_address.setText("");
                tv_maker_name.setText("");
                tv_chasis_no.setText("");
                tv_engine_no.setText("");

                dl_no.setText("");
                tv_licnce_ownername.setText("");
                tv_lcnce_father_name.setText("");
                tv_lcnce_address.setText("");
                tv_lcnce_phone_number.setText("");
                tv_dlpoints_spotchallan_xml.setText("");
                img_aadhar_image.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile_img));

                tv_aadhar_user_name.setText("");
                tv_aadhar_care_off.setText("");
                tv_aadhar_dob.setText("");
                tv_aadhar_gender.setText("");
                tv_aadhar_address.setText("");
                tv_aadhar_mobile_number.setText("");
                tv_aadhar_uid.setText("");

                ll_aadhar_layout.setVisibility(View.GONE);
                tv_aadhar_header.setVisibility(View.GONE);
                rl_lcnce_Details.setVisibility(View.GONE);


                //    if(Dashboard.check_vhleHistory_or_Spot.equalsIgnoreCase("drunkdrive")) {



  /*                  if ((et_driver_lcnce_num.getText().toString().trim().equals(""))
                            && (et_aadharnumber.getText().toString().trim().equals(""))) {

                        ShowMessage("Please Enter Driver License No / Aadhar Number To Continue");
                    }

                    else {*/

                String dateofbirthbut = dob_input.getText().toString();


                if (!et_driver_lcnce_num.getText().toString().equalsIgnoreCase("") && et_driver_lcnce_num.getText().toString().length() >= 5) {
                    if (dobcheck.equalsIgnoreCase("Yes")) {

                        if (isOnline()) {
                            asyncAllsOfMethods();
                        } else {
                            showToast("Please Check Your Network Connection");
                        }

                    } else {
                        showToast("Please Select Date Of Birth !");
                    }
                } else {
                    if (isOnline()) {
                        asyncAllsOfMethods();
                    } else {
                        showToast("Please Check Your Network Connection");
                    }
                }




               /* }else if(Dashboard.check_vhleHistory_or_Spot.equalsIgnoreCase("drunkdrive_withourDlAadhar"))
                {

                  if(casesBooked<casesLimit) {

                      if (et_driver_lcnce_num.getText().toString() != null && !et_driver_lcnce_num.getText().toString().equalsIgnoreCase("") &&
                              et_driver_lcnce_num.getText().toString().length() >= 5) {
                          if (dobcheck.equalsIgnoreCase("Yes")) {

                              if (isOnline()) {
                                  asyncAllsOfMethods();
                              } else {
                                  showToast("Please Check Your Network Connection");
                              }

                          } else {
                              showToast("Please Select Date Of Birth !");
                          }
                      } else {

                          if (isOnline()) {
                              asyncAllsOfMethods();
                          } else {
                              showToast("Please Check Your Network Connection");
                          }
                      }
                  }else {
                      showToast("Your Limit Of Without DL and Aadhar Cases Completed Please Contact Echallan to Enhance the Limit Or Please Enforce With Normal DD Module");
                  }
                }*/
                break;

            case R.id.btn_pendingchallans_rtadetails_xml:

                if (et_regcid.getText().toString().trim().equals("")) {
                    // utils.showError(et_regcid, "Enter Registration Code");
                    et_regcid.setError(Html.fromHtml("<font color='black'>Enter Registration Code</font>"));

                }
                // NOT MANDATORY
                // else if (et_vchl_num.getText().toString().trim().equals("")) {
                // utils.showError(et_vchl_num, "Enter Registration Series");
                // }
                else if (et_last_num.getText().toString().trim().equals("")) {
                    // utils.showError(et_last_num, "Enter Vehicle Number");
                    et_last_num.setError(Html.fromHtml("<font color='black'>Enter Vehicle Number</font>"));
                } else {
                    if (isOnline()) {
                        vehicle_num_send = "";
                        vehicle_num_send = ("" + (et_regcid.getText().toString().trim().toUpperCase()) + ""
                                + (et_vchl_num.getText().toString().trim().toUpperCase()) + ""
                                + (et_last_num.getText().toString().trim().toUpperCase()));
                        if (isOnline()) {
                            new Async_getPendingChallans().execute();
                        }
                    } else {
                        showToast("" + NETWORK_TXT);
                    }
                }
                break;

            case R.id.btncancel_rta_details_xml:
                /*
                 * In API level 11 or greater : use Code : if(Build.VERSION.SDK_INT
                 * >= 11)
                 */
                Intent launch = new Intent(Drunk_Drive.this, Dashboard.class);
                launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(launch);
                break;

            /* TO MOVE TO GENERATE MOBILE TICKET */
            case R.id.btngeneratechallan_rta_details_xml:


                if (et_regcid.getText().toString().trim().equals("")) {
                    et_regcid.setError(Html.fromHtml("<font color='black'>Enter Registration Code</font>"));
                    et_regcid.requestFocus();
                } else if (et_last_num.getText().toString().trim().equals("")) {
                    // utils.showError(et_last_num, "Enter Vehicle Number");
                    et_last_num.setError(Html.fromHtml("<font color='black'>Enter Vehicle Number</font>"));
                } else if (btn_wheler_code.getText().toString().trim()
                        .equals("" + getResources().getString(R.string.select_wheeler_code))) {
                    showToast("Select wheeler code");
                } /*else if (!et_driver_lcnce_num.getText().toString().trim().equals("")
                        && et_driver_lcnce_num.getText().toString().trim().length() >= 3 && dd_dobFLG == false) {
                    startActivity(new Intent(getApplicationContext(), DateOfBirth_Update.class));
                    dd_dobFLG = true;

                }*/ else {
                    details_regncid = et_regcid.getText().toString().trim();
                    details_regncid_name = et_vchl_num.getText().toString().trim();
                    details_regn_last_num = et_last_num.getText().toString().trim();
                    details_driver_dl_no = et_driver_lcnce_num.getText().toString().trim();

                    try {
                        db.open();

                        c_whlr = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                        if ((c_whlr.getCount() > 0)) {

                            licence_status = "";

                            if (Fake_NO_Dialog.fake_action == "not fake") {

                                if (isOnline()) {
                                    Async_getApprovefromRtaforPoint async_getApprovefromRtaforPoint = new Async_getApprovefromRtaforPoint();
                                    async_getApprovefromRtaforPoint.execute();
                                } else {
                                    showToast("Please Check your Network Connectivity");
                                }


                            } else if (Fake_NO_Dialog.fake_action == null) {

//                                SharedPreferences sharedPreference = PreferenceManager
//                                        .getDefaultSharedPreferences(getApplicationContext());
//                                SharedPreferences.Editor edit = sharedPreference.edit();
//                                edit.putString("IMAGE", "" + image_data_tosend);
//                                edit.commit();
//                                startActivity(new Intent(this, GenerateDrunkDriveCase.class));

                                if (isOnline()) {
                                    Async_getApprovefromRtaforPoint async_getApprovefromRtaforPoint = new Async_getApprovefromRtaforPoint();
                                    async_getApprovefromRtaforPoint.execute();
                                } else {
                                    showToast("Please check Network Connectivity ");
                                }

                            } else if (Fake_NO_Dialog.fake_action == "fake") {

                                TextView title = new TextView(Drunk_Drive.this);
                                title.setText("Hyderabad E-Ticket");
                                title.setBackgroundColor(Color.RED);
                                title.setGravity(Gravity.CENTER);
                                title.setTextColor(Color.WHITE);
                                title.setTextSize(22);
                                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0,
                                        R.drawable.dialog_logo, 0);
                                title.setPadding(20, 0, 20, 0);
                                title.setHeight(85);

                                String otp_message = "\n It's a Fake Vehicle !!!\n";

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Drunk_Drive.this,
                                        AlertDialog.THEME_HOLO_LIGHT);
                                alertDialogBuilder.setCustomTitle(title);
                                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                                alertDialogBuilder.setMessage(otp_message);
                                alertDialogBuilder.setCancelable(false);
                                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                alertDialog.getWindow().getAttributes();

                                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                                textView.setTextSize(20);
                                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                                textView.setGravity(Gravity.CENTER);

                                Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                btn1.setTextSize(22);
                                btn1.setTextColor(Color.WHITE);
                                btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
                                btn1.setBackgroundColor(Color.RED);
                            }
                        } else {
                            showToast("" + Utils.MASTER_ELSE_TEXT);
                        }
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    c_whlr.close();
                    db.close();
                }
                break;

            case R.id.imgv_camera_capture_rta_details_xml:
                /*
                 * if (isDeviceSupportCamera()) {
                 *
                 * Intent cameraIntent = new
                 * Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 * startActivityForResult(cameraIntent, CAMERA_REQUEST); } else {
                 * showToast("Sorry! Your device doesn't support camera"); }
                 */
                selectImage();
                break;

            case R.id.btn_whlr_code_dd_xml:
                showDialog(WHEELER_CODE);
                break;

            case R.id.btn_vehCategory:
                new Async_VehicleCategory().execute();
                break;

            default:
                break;
        }
    }

    public class Async_VehicleCategory extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getVehicleCategory();
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

            if (!ServiceHelper.vehCategory.equals("0") && null != ServiceHelper.vehCategory) {
                ArrayList<VehCatModel> vehCatModels;
                try {
                    JSONObject jsonObject = new JSONObject(ServiceHelper.vehCategory);
                    JSONArray jsonArray = jsonObject.getJSONArray("VehCategoryMaster");
                    vehCatModels = new ArrayList<>(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        VehCatModel vehCatModel = new VehCatModel();
                        vehCatModel.setVehCatName("" + object.getString("VehCatName"));
                        vehCatModel.setVehCatCode("" + object.getString("VehCatCode"));
                        vehCatModels.add(vehCatModel);
                    }
                    showVehCatSpinnerDialog("Select Vehicle Category", vehCatModels);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private void showVehCatSpinnerDialog(String title, final List<VehCatModel> vehCatModels) {

        final AlertDialog builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.spinner_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        ListView spinner_List = view.findViewById(R.id.recycle_List);
        AppCompatTextView title_Spinner = view.findViewById(R.id.title_Spinner);
        title_Spinner.setText("" + title);
        VehCatAdapter jobsAdapter = new VehCatAdapter(Drunk_Drive.this, vehCatModels);
        spinner_List.setAdapter(jobsAdapter);
        builder.show();
        spinner_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s_Item = vehCatModels.get(i).getVehCatName();
                vehCatgryCd = vehCatModels.get(i).getVehCatCode();
                btn_vehCategory.setText("" + s_Item);
                builder.dismiss();
            }
        });
    }

    private void asyncAllsOfMethods() {

        if (isOnline()) {

            VerhoeffCheckDigit ver = new VerhoeffCheckDigit();

            if (et_regcid.getText().toString().trim().equals("")) {
                // utils.showError(et_regcid, "Enter Registration Code");
                et_regcid.setError(Html.fromHtml("<font color='black'>Enter Registration Code</font>"));
                et_regcid.requestFocus();

            } else if (et_last_num.getText().toString().trim().equals("")) {
                // utils.showError(et_last_num, "");
                et_last_num.setError(Html.fromHtml("<font color='black'>Enter Vehicle Number</font>"));
                et_last_num.requestFocus();

            } else if (edt_checkslno_.getText().toString().trim().isEmpty()) {
                edt_checkslno_.setError(Html.fromHtml("<font color='black'>Enter Check serial Number !</font>"));
                edt_checkslno_.requestFocus();
            } else if (edt_alchl_reading.getText().toString().trim().isEmpty() ||
                    Integer.parseInt(edt_alchl_reading.getText().toString().trim()) < 36) {
                edt_alchl_reading.setError(Html.fromHtml("<font color='black'>Alchohal reading should be more than 36 </font>"));
                edt_alchl_reading.requestFocus();
            } else {
                if (isOnline()) {
                    vehicle_num_send = "";
                    vehicle_num_send = ("" + (et_regcid.getText().toString().trim().toUpperCase()) + ""
                            + (et_vchl_num.getText().toString().trim().toUpperCase()) + ""
                            + (et_last_num.getText().toString().trim().toUpperCase()));

                    Dashboard.rta_details_request_from = "RTACLASS";
                    completeVehicle_num_send = ("" + et_regcid.getText().toString() + ""
                            + et_vchl_num.getText().toString() + "" + et_last_num.getText().toString());
                    if (isOnline()) {
                        new Async_getRTADetails().execute();
                        lyt_DD_Details.setVisibility(View.VISIBLE);
                    }

                    if (isOnline()) {
                        if (!et_driver_lcnce_num.getText().toString().trim().equals("")) {
                            Dashboard.licence_details_request_from = "RTACLASS";
                            if ("Y".equals(dl_data_flg.trim().toUpperCase())) {

                                if (isOnline()) {
                                    new Async_getLicenceDetails().execute();
                                }
                            }
                        }
                    }

                    /* TO GET AADHAR DETAILS */
                    ll_aadhar_layout.setVisibility(View.GONE);
                    tv_aadhar_header.setVisibility(View.GONE);
                    img_aadhar_image.setImageResource(R.drawable.photo);

                    if (et_aadharnumber.getText() != null && et_aadharnumber.getText().toString().trim().length() >= 1
                            && (!ver.isValid(et_aadharnumber.getText().toString()))) {
                        showToast("Please Enter Valid Adhaar Number");
                        et_aadharnumber
                                .setError(Html.fromHtml("<font color='black'>Please Enter Valid Adhaar Number</font>"));
                    } else if ((et_aadharnumber.getText().toString().trim().length() == 12)
                            || (et_aadharnumber.getText().toString().trim().length() == 28)) {

                        if ("Y".equals(aadhaar_data_flg.trim().toUpperCase())) {
                            if (isOnline()) {
                                new Async_getAadharDetails().execute();
                            }
                        }
                    }
                } else {
                    showToast("" + NETWORK_TXT);
                }
            }
        } else {
            showToast("Please Check Your Network Connection");
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Drunk_Drive.this,
                    BuildConfig.APPLICATION_ID + ".provider", f));
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 1);
        }

    }

    @Override
    public void onDataNotAvailed(int i) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDataLoaded(ArrayList<RecordForm> recordForms) {
        try {
            if (recordForms.size() > 0 && null != recordForms) {
                edt_checkslno_.setText("" + recordForms.get(0).getRecordFormNum());
                edt_alchl_reading.setText("" + recordForms.get(0).getRecordFormMeasureNum());
            }
        } catch (Exception e) {
            e.printStackTrace();
            edt_checkslno_.setText("");
            edt_alchl_reading.setText("");
        }

    }

    @Override
    public void OnRepeatCommand(String s) {

    }

    private void initBle() {

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setMaxConnectCount(3)
                .setOperateTimeout(5000);
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(6000)
                .build();
        BleManager.getInstance()
                .initScanRule(scanRuleConfig);
    }

    private void initView() {
        dlg_BleDevice = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.blescandevice_dialog, null);
        dlg_BleDevice.setView(dialogView);
        list_BleDevice = dialogView.findViewById(R.id.list_BleDevice);
        adapter = new BleAdapter(this, dataList);
        list_BleDevice.setAdapter(adapter);
        progressDialog.dismiss();
        dlg_BleDevice.show();
        dlg_BleDevice.setCancelable(true);

        list_BleDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!BleManager.getInstance().isConnected((BleDevice) adapter.getItem(position))) {

                    Log.d("DeviceBA", "" + adapter.getItem(position));
                    BleManager.getInstance().cancelScan();
                    connect((BleDevice) adapter.getItem(position));
                    dlg_BleDevice.dismiss();
                }
            }
        });
    }

    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                dataList.clear();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
                /*if (!TextUtils.isEmpty(bleDevice.getName())) {
                    Log.d("Ble Name",""+bleDevice.getName());
                    adapter.add(bleDevice);
                }*/


            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                if (!TextUtils.isEmpty(bleDevice.getName())) {
                    Log.d("Ble Name", "" + bleDevice.getName());
                    adapter.add(bleDevice);
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
               /* adapter = new BleAdapter(MainActivity.this, scanResultList);
                listView.setAdapter(adapter);*/
            }
        });
    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(BleException exception) {

                Toast.makeText(Drunk_Drive.this, "fail", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                Toast.makeText(Drunk_Drive.this, "ConnectSuccess", Toast.LENGTH_LONG).show();
                readRssi(bleDevice);
                EventManger.getInstance()
                        .initDeviceInfo(bleDevice, true, callBack);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {

            }

            @Override
            public void onReConnect(BleDevice bleDevice) {

            }
        });
    }

    private void readRssi(BleDevice bleDevice) {
        BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                Log.i("bleble", "onRssiFailure" + exception.toString());
            }

            @Override
            public void onRssiSuccess(int rssi) {
                Log.i("bleble", "onRssiSuccess: " + rssi);
            }
        });
    }

    @Subscribe(sticky = true)
    public void instantRecordData(ReceiveFormDataResult receiveFormDataResult) {
        //  TAG_INSTANT_RECEIVE=10004;
        if (receiveFormDataResult != null && receiveFormDataResult.getTag() == 10004) {
            Log.i("bleble5", "recordFormsReceive=" + receiveFormDataResult.getRecordFormArrayList().size());
        }
    }

    public class Async_getRTADetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            regncode_send = "" + et_regcid.getText().toString().trim().toUpperCase();
            regnName_send = "" + et_vchl_num.getText().toString().toUpperCase();
            vehicle_num_send = "" + et_last_num.getText().toString().trim().toUpperCase();

            aadhaar = et_aadharnumber.getText().toString().trim();
            licence_no = et_driver_lcnce_num.getText().toString().trim();

            completeVehicle_num_send = ("" + regncode_send + "" + regnName_send + "" + vehicle_num_send);

            ServiceHelper.getRTADetails("" + completeVehicle_num_send);
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            // new Async_getOffenderRemarks().execute();

            try {

                if (ServiceHelper.rta_data != null && !ServiceHelper.rta_data.equalsIgnoreCase("NA") && !"0".equals(ServiceHelper.rta_data)) {
                    if ((!ServiceHelper.rta_data.equalsIgnoreCase("0")) && (rta_details_master.length > 1)) {
                        rl_rta_details_layout.setVisibility(View.VISIBLE);


                        rtaFlG = true;
                        licFLG = false;
                        adhrFLG = false;
                        try {
                            tv_vhle_no.setText("" + completeVehicle_num_send);
                            tv_owner_name.setText("" + rta_details_master[1] != null ? rta_details_master[1] : "");
                            tv_address.setText("" + rta_details_master[2] != null ? rta_details_master[2] : "" + "\t" + rta_details_master[3] != null ? rta_details_master[3] : "");
                            tv_maker_name.setText("" + rta_details_master[4] != null ? rta_details_master[4] : "" + "\t" + rta_details_master[6] != null ? rta_details_master[6] : "");
                            tv_engine_no.setText("" + rta_details_master[7] != null ? rta_details_master[7] : "");
                            tv_chasis_no.setText("" + rta_details_master[8] != null ? rta_details_master[8] : "");

                            Log.i("FAKE DETAILS", "" + rta_details_master[10] != null ? rta_details_master[10] : "");

                            tv_vehicle_details.setText("VEHICLE DETAILS");


                            rta_details_master = new String[0];

                            rta_details_master = ServiceHelper.rta_data.split("!");

                            Wheeler_check = rta_details_master[0].split(":");
                            String Wheeler_Enable_check = Wheeler_check[1].toString() != null ? Wheeler_check[1].toString() : "";

                            if (!Wheeler_Enable_check.equalsIgnoreCase("NA")) {
                                btn_wheler_code.setClickable(false);
                            } else {
                                btn_wheler_code.setClickable(true);
                            }

                            // Log.i("Response wheeler :::", ""+rta_details_master[0]);
                            if (rta_details_master != null && rta_details_master.length > 0 && (!rta_details_master[0].equals("NA"))
                                    && rta_details_master[0].split("\\:")[1] != null
                                    && !"NA".equalsIgnoreCase(rta_details_master[0].split("\\:")[1].trim())) {
                                // whlr_code_send = rta_details_master[0].split("\\:")[1];
                                whlr_code_send = rta_details_master[9] != null ? rta_details_master[9] : "";
                                if (whlr_code_send != null) {
                                    btn_wheler_code.setText("" + whlr_code_send);
                                } else {
                                    btn_wheler_code.setClickable(true);
                                }
                            } else {
                                // showToast("DD Toast !!!");
                            }

                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        if (isOnline()) {
                            new Async_getOffenderRemarks().execute();
                        }

                    } else {
                        tv_vehicle_details.setText("VEHICLE DETAILS NOT FOUND!");
                        rl_rta_details_layout.setVisibility(View.GONE);
                        rtaFlG = false;
                        // showToast("No Details Found !");
                    }


//                    if ((et_aadharnumber.getText().toString().trim().length() == 12)
//                            || (et_aadharnumber.getText().toString().trim().length() == 28)) {
//
//                        if(isOnline()) {
//                            new Async_getAadharDetails().execute();
//                        }
//                    }
                } else {
                    tv_vehicle_details.setText("VEHICLE DETAILS NOT FOUND!");
                    rl_rta_details_layout.setVisibility(View.GONE);
                    rtaFlG = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                tv_vehicle_details.setText("VEHICLE DETAILS NOT FOUND!");
                rl_rta_details_layout.setVisibility(View.GONE);
                rtaFlG = false;
            }
        }
    }

    public class Async_getOffenderRemarks extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getOffenderRemarks(completeVehicle_num_send,
                    "" + et_driver_lcnce_num.getText().toString().trim(),
                    "" + et_aadharnumber.getText().toString().trim());
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            if (ServiceHelper.offender_remarks != null && !ServiceHelper.offender_remarks.equalsIgnoreCase("0")) {

                try {
                    Drunk_Drive.offender_remarks_resp_master = new String[0];
                    theftRemarkFlag = ServiceHelper.offender_remarks.split("\\^")[1];

                    Drunk_Drive.offender_remarks_resp_master = ServiceHelper.offender_remarks.split("!");

                    if (!offender_remarks_resp_master[10].toString().trim().equals("NA") && offender_remarks_resp_master.length > 1) {
                        showDialog(FAKE_NUMBERPLATE_DIALOG);
                    } else {
                        showToast("No Remarks Found");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showToast("Remarks Data Not Found");
            }

        }
    }

    public class Async_getAadharDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            // TODO Auto-generated method stub

            if (!licence_no.trim().equals("")) {
                ServiceHelper.onlinebuff = ServiceHelper.onlinebuff.append("3" + "!" + "NA" + "^");
            }
            ServiceHelper.getAadharDetails("" + et_aadharnumber.getText().toString().trim(), "");
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if (!"null".equals(ServiceHelper.aadhar_data) && !"0".equals(ServiceHelper.aadhar_data) && ServiceHelper.aadhar_details.length > 0) {

                ll_aadhar_layout.setVisibility(View.VISIBLE);
                tv_aadhar_header.setVisibility(View.VISIBLE);

                adhrFLG = true;
                // licFLG = false ;
                rtaFlG = false;

                tv_aadhar_header.setText("AADHAR DETAILS");

                try {

                    tv_aadhar_user_name.setText("" + ServiceHelper.aadhar_details[0] != null ? ServiceHelper.aadhar_details[0] : "NA".trim().toUpperCase());
                    tv_aadhar_care_off.setText("" + (!ServiceHelper.aadhar_details[1].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[1] : "NA").trim().toUpperCase());
                    tv_aadhar_address.setText(""
                            + (!ServiceHelper.aadhar_details[2].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[2]
                            : "")
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
                            ? ServiceHelper.aadhar_details[7] + "," : "").trim().toUpperCase());

                    String Compleate_addr = "" + tv_aadhar_address.getText().toString();

                    tv_aadhar_mobile_number.setText("" + (!ServiceHelper.aadhar_details[8].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[8] : "NA").trim().toUpperCase());
                    tv_aadhar_gender.setText("" + (!ServiceHelper.aadhar_details[9].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[9] : "NA").trim().toUpperCase());


                    String date_birth = "0", service_year = "0";


                    try {

                        date_birth = ServiceHelper.aadhar_details[10] != null ? ServiceHelper.aadhar_details[10] : "";
                        if (date_birth != null && !date_birth.equalsIgnoreCase("")) {
                            String[] split_dob = date_birth.split("\\/");
                            service_year = "" + split_dob[2];
                        }


                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();


                        service_year = "0";
                        date_birth = "0";
                    }

                    int final_age = year - Integer.parseInt(service_year);
                    Log.i("final_age ::::::::", "" + final_age);

                    tv_aadhar_dob.setText("" + final_age);

                    tv_aadhar_uid.setText("" + (!ServiceHelper.aadhar_details[11].equalsIgnoreCase("0")
                            ? ServiceHelper.aadhar_details[11] : "NA"));
                    /*
                     * tv_aadhar_eid .setText("" +
                     * (!ServiceHelper.aadhar_details[12] .equalsIgnoreCase("0") ?
                     * ServiceHelper.aadhar_details[12] : "NA"));
                     */

                    if (ServiceHelper.aadhar_details[13].toString().trim().equals("0")) {

                    } else if (ServiceHelper.aadhar_details[13] != null) {
                        try {
                            byte[] decodestring = Base64.decode("" + ServiceHelper.aadhar_details[13].toString().trim(),
                                    Base64.DEFAULT);
                            Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                            img_aadhar_image.setImageBitmap(decocebyte);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ServiceHelper.aadhar_details[13] = null;
                    }
                    if (ServiceHelper.aadhar_details[13] == null) {
                        img_aadhar_image.setImageResource(R.drawable.camera);
                        // img_aadhar_image.setClickable(true);
                    }


                    String Qrdata = "AADHAAAR NUMBER:" + " " + ServiceHelper.aadhar_details[11] != null ? ServiceHelper.aadhar_details[11] : "" + "\n" + "NAME:" + " "
                            + ServiceHelper.aadhar_details[0] != null ? ServiceHelper.aadhar_details[0] : "" + "\n" + "FATHER NAME:" + " "
                            + ServiceHelper.aadhar_details[1] != null ? ServiceHelper.aadhar_details[1] : "" + "\n" + "AGE:" + " " + final_age + "\n" + "GENDER:" + " "
                            + ServiceHelper.aadhar_details[9] != null ? ServiceHelper.aadhar_details[9] : "" + "\n" + "ADDRESS:" + " " + Compleate_addr;
                    try {
                        Bitmap bitmap = encodeAsBitmap(Qrdata);
                        qr_code.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();

                    tv_aadhar_header.setVisibility(View.VISIBLE);
                    tv_aadhar_header.setText("AADHAR DETAILS NOT FOUND!");
                    ll_aadhar_layout.setVisibility(View.GONE);
                    adhrFLG = false;
                }


            } else if (ServiceHelper.aadhar_details.length == 0) {
                tv_aadhar_header.setVisibility(View.VISIBLE);
                tv_aadhar_header.setText("AADHAR DETAILS NOT FOUND!");
                ll_aadhar_layout.setVisibility(View.GONE);
                adhrFLG = false;
            }
        }
    }

    /* LICENCE DETAILS */
    public class Async_getLicenceDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getLicenceDetails("" + licence_no, "" + dd_dob_DL);
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressLint("SetTextI18n")
        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            // showToast("" + licene_details_master.length);

            try {

                dl_no.setText("" + et_driver_lcnce_num.getText().toString().trim());


                if (ServiceHelper.license_data != null && !"0".equals(ServiceHelper.license_data) && SpotChallan.licence_details_spot_master.length > 0) {

                    rl_lcnce_Details.setVisibility(View.VISIBLE);
                    Drunk_Drive.licFLG = true;
                    adhrFLG = false;
                    rtaFlG = false;

                    try {
                        tv_licnce_ownername.setText("" + SpotChallan.licence_details_spot_master[0] != null ? SpotChallan.licence_details_spot_master[0] : "");
                        tv_lcnce_father_name.setText("" + SpotChallan.licence_details_spot_master[1] != null ? SpotChallan.licence_details_spot_master[1] : "");
                        tv_lcnce_phone_number.setText("" + SpotChallan.licence_details_spot_master[2] != null ? SpotChallan.licence_details_spot_master[2] : "");
                        tv_lcnce_address.setText("" + SpotChallan.licence_details_spot_master[4] != null ? SpotChallan.licence_details_spot_master[4] : "");


                        owner_image_data = "" + SpotChallan.licence_details_spot_master[5] != null ? SpotChallan.licence_details_spot_master[5] : "";

                        dl_points = SpotChallan.licence_details_spot_master[7] != null ? SpotChallan.licence_details_spot_master[7] : "0";

                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();

                        dl_points = "0";

                    }

                    if (licence_no != null && (dl_points != null && Integer.parseInt(dl_points) > 0)) {

                        tv_dlpoints_spotchallan_xml.setText("TOTAL PENALTY POINTS :" + dl_points);

                    } else {
                        tv_dlpoints_spotchallan_xml.setText("TOTAL PENALTY POINTS :" + "0");
                    }


                    if (owner_image_data != null && owner_image_data.trim().length() > 100) {

                        try {
                            owner_imageByteArray = Base64.decode(owner_image_data.getBytes(), 1);
                            Log.i("Image 2 byte[]", "" + Base64.decode(owner_image_data.trim().getBytes(), 1));
                            Bitmap bmp = BitmapFactory.decodeByteArray(owner_imageByteArray, 0, owner_imageByteArray.length);
                            dl_img.setImageBitmap(bmp);
                        } catch (Exception e) {
                            e.printStackTrace();
                            dl_img.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile_img));
                        }


                    } else if (owner_image_data == null && owner_image_data.trim().length() == 0) {
                        dl_img.setImageResource(R.drawable.empty_profile_img);
                    }
                    tv_licence_details.setText("LICENCE DETAILS");
                    Log.i("LICENCE DETAILS FOUND", "" + licFLG);

                } else {
                    Drunk_Drive.licFLG = false;
                    Log.i("NO LICENCE", "" + licFLG);
                    tv_licence_details.setText("LICENCE DETAILS NOT FOUND!");
                    rl_lcnce_Details.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Drunk_Drive.licFLG = false;
                tv_licence_details.setText("LICENCE DETAILS NOT FOUND!");
                rl_lcnce_Details.setVisibility(View.GONE);

            }

        }
    }

    /* TO GET PENDING CHALLANS BY VECHILE NUMBER */
    public class Async_getPendingChallans extends AsyncTask<Void, Void, String> {

        @SuppressWarnings("unused")
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SharedPreferences sharedPreference2 = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());
            String psCd = sharedPreference2.getString("PS_CODE", "");
            String psName = sharedPreference2.getString("PS_NAME", "");
            String pidCd = sharedPreference2.getString("PID_CODE", "");
            String pidName = sharedPreference2.getString("PID_NAME", "");
            String cadre = sharedPreference2.getString("CADRE_NAME", "");
            String cadreCd = sharedPreference2.getString("CADRE_CODE", "");
            String pswd = sharedPreference2.getString("PASS_WORD", "");

            ServiceHelper.getpendingChallansByRegNo("" + completeVehicle_num_send, "" + "", "" + "", "" + pidCd,
                    "" + pidName, "" + pswd, "" + simid_send, "" + imei_send, "" + latitude, "" + longitude, "", "23");
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if ((!ServiceHelper.Opdata_Chalana.equals("0")) && (ServiceHelper.pending_challans_master.length > 0)) {
                startActivity(new Intent(getApplicationContext(), PendingChallans.class));
            } else if (ServiceHelper.Opdata_Chalana.equals("0")) {
                showToast("No Pending Challans");
            } else {
                showToast("Try Again!");
            }
            if ((!rta_details_master[10].toString().trim().equals("NA"))) {
                Log.i("FD", "YES");
                showDialog(FAKE_NUMBERPLATE_DIALOG);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
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
                title.setTextSize(20);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                AlertDialog.Builder ad_whle_code_name = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
                ad_whle_code_name.setCustomTitle(title);//
                ad_whle_code_name.setSingleChoiceItems((wheeler_name_arr), selected_wheller_code,

                        new DialogInterface.OnClickListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                selected_wheller_code = which;
                                btn_wheler_code.setText("" + wheeler_name_arr_spot[which]);
                                removeDialog(WHEELER_CODE);
                                whlr_code_send = wheeler_code_arr_spot[which];
                                Log.i("****whlr_code_send***", "" + whlr_code_send);
                            }
                        });
                Dialog dg_whle_code_name = ad_whle_code_name.create();
                return dg_whle_code_name;

            case FAKE_NUMBERPLATE_DIALOG:

                String massge = null;
                try {
                    massge = "\n" + offender_remarks_resp_master[10] + "\n";

                } catch (Exception e) {
                    e.printStackTrace();
                    massge = "OFFENDER REMARKS NOT FOUND PLEASE TRY AGAIN";
                }

                if ("Y".equalsIgnoreCase(theftRemarkFlag)) {
                    new VibratorUtils(getApplicationContext()).vibratePhone(10000);
                }

                TextView title2 = new TextView(this);
                title2.setText("REMARKS");
                title2.setBackgroundColor(Color.RED);
                title2.setGravity(Gravity.CENTER);
                title2.setTextColor(Color.WHITE);
                title2.setTextSize(22);
                title2.setTypeface(title2.getTypeface(), Typeface.BOLD);
                title2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title2.setPadding(20, 0, 20, 0);
                title2.setHeight(80);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title2);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(massge);
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        removeDialog(FAKE_NUMBERPLATE_DIALOG);
                        if ("Y".equalsIgnoreCase(theftRemarkFlag)) {
                            new VibratorUtils(getApplicationContext()).vibrateStopPhone();
                        }
                        if (offender_remarks_resp_master[10].contains("FAKE NO")) {
                            fake_veh_chasisNo = offender_remarks_resp_master[8]
                                    .substring(offender_remarks_resp_master[8].length() - 5);
                            Log.i("Spot chasis No :::", "" + fake_veh_chasisNo);
                            Intent intent = new Intent(getApplicationContext(), Fake_NO_Dialog.class);
                            intent.putExtra("Flagkey", "D");
                            startActivity(intent);
                        }
                    }
                });
                alertDialogBuilder.setCancelable(false);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.getWindow().getAttributes();

                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(20);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setGravity(Gravity.CENTER);

                Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn1.setTextSize(22);
                btn1.setTextColor(Color.WHITE);
                btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
                btn1.setBackgroundColor(Color.RED);
                return alertDialog;

            default:
                break;
        }
        return super.onCreateDialog(id);
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

    @SuppressLint("SetJavaScriptEnabled")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == CAMERA_REQUEST) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                Random randomGenerator = new Random();
                int num = randomGenerator.nextInt(100);
                String newimagename = num + ".jpg";
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + newimagename);

                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    fo = new FileOutputStream(f.getAbsoluteFile());
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (!picturePath.equals("")) {
                    picturePath = "";
                }

                picturePath = f.getAbsolutePath();
                Log.i("pic path", picturePath);
                webviewloader = new WebviewLoader();
                wv_img_captured.setBackgroundColor(0x00000000);
                wv_img_captured.setHorizontalScrollBarEnabled(true);
                wv_img_captured.setVerticalScrollBarEnabled(true);
                WebSettings webSettings = wv_img_captured.getSettings();
                wv_img_captured.setInitialScale(50);
                webSettings.setJavaScriptEnabled(true);
                wv_img_captured.getSettings().setLoadWithOverviewMode(true);
                wv_img_captured.getSettings().setUseWideViewPort(true);
                wv_img_captured.getSettings().setBuiltInZoomControls(true);
                wv_img_captured.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                webviewloader.DisplayImage("file://" + picturePath, wv_img_captured);
                Log.i("pic path", picturePath);
                wv_img_captured.setVisibility(View.VISIBLE);


            } else if (requestCode == 1) {

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
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {
                        Log.i("Camera Path :::", "" + file.getAbsolutePath());

                        outFile = new FileOutputStream(file);
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

                        canvas.drawText("Date & Time: " + Current_Date, xPos, yPos + 300, paint);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);

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

                    canvas.drawText("Date & Time: " + Current_Date, xPos, yPos + 300, paint);
                    canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                    canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);

                    offender_image.setImageBitmap(mutableBitmap);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                    byteArray = bytes.toByteArray();
                    image_data_tosend = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    offender_image.setVisibility(View.VISIBLE);
                    Log.i("image_data_tosend ::", "" + image_data_tosend);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                picturePath = "";
                picturePath = null;
            }
        }
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

    @SuppressLint("MissingPermission")
    @SuppressWarnings("unused")
    private void LocationAndIMEIValues() {
        // TODO Auto-generated method stub
        m_locationlistner = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = m_locationlistner.getBestProvider(criteria, false);
        m_locationlistner.requestLocationUpdates(provider, 0, 0, this);
        location = m_locationlistner.getLastKnownLocation(provider);
        // onLocationChanged(location);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            // speed = location.getSpeed();
        } else {
            latitude = 0.0;
            longitude = 0.0;
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

    public void showToast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();

        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(20);

        toastView.setBackgroundResource(R.drawable.toast_background);
        toast.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("SimpleDateFormat")
    private void showDate(int year, int month, int day) {

        dob_input.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));

        SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");
        @SuppressWarnings("deprecation")
        String present_date_toSend = date_format.format(new Date(year - 1900,
                month, day));

        dob_input.setText(present_date_toSend);
        Log.i("dob_input :::", "" + dob_input.getText().toString());
    }

    public class Async_getApprovefromRtaforPoint extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            try {

                if (licence_no != null && !licence_no.equalsIgnoreCase("") && SpotChallan.licence_details_spot_master.length > 0) {
                    driverName = SpotChallan.licence_details_spot_master[0].toString() != null ? SpotChallan.licence_details_spot_master[0].toString() : "";
                    driverAddress = SpotChallan.licence_details_spot_master[4].toString() != null ? SpotChallan.licence_details_spot_master[4].toString() : "";
                    driverCity = SpotChallan.licence_details_spot_master[4].toString() != null ? SpotChallan.licence_details_spot_master[4].toString() : "";

                } else if (!et_aadharnumber.getText().toString().equalsIgnoreCase("") && !et_aadharnumber.getText().toString().equalsIgnoreCase(null)) {
                    driverName = tv_aadhar_user_name.getText().toString() != null ? tv_aadhar_user_name.getText().toString() : "";
                    driverCity = tv_aadhar_address.getText().toString() != null ? tv_aadhar_address.getText().toString() : "";
                    driverAddress = tv_aadhar_address.getText().toString() != null ? tv_aadhar_address.getText().toString() : "";

                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                driverName = "";
                driverAddress = "";
                driverCity = "";
            }


            String rtaApproverResponse = ServiceHelper.getApprovefromRtaforPoint("", completeVehicle_num_send != null ? completeVehicle_num_send : "NA",
                    new DateUtil().getTodaysDate(), new DateUtil().getPresentTime(),
                    "", Dashboard.UNIT_CODE, bookedPScode_send_from_settings, exact_location_send_from_settings,
                    "63@W/o Documents(S 130/177)@100!33@Drunken Driving(S 185(a))@0", "0", licence_no,
                    driverName != null ? driverName.toString() : "NA", driverAddress != null ? driverAddress.toString() : "NA",
                    driverCity != null ? driverCity.toString() : "NA", "U", "",
                    "", dd_dob_DL != null ? dd_dob_DL.toString() : "NA", point_name_send_from_settings, "", bookedPSname_send_from_settings,
                    pidName, cadre_name, whlr_code_send != null ? whlr_code_send.toString() : "NA", point_code_send_from_settings, "");

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

            if (ServiceHelper.rtaapproovedresponse != null && !ServiceHelper.rtaapproovedresponse.equalsIgnoreCase("") &&
                    !ServiceHelper.rtaapproovedresponse.equalsIgnoreCase("NA|NA|NA")) {

                try {
                    rtaAprroved_Master = new String[0];

                    rtaAprroved_Master = ServiceHelper.rtaapproovedresponse.split("\\|");

                    SpotChallan.OtpStatus = rtaAprroved_Master[3].toString() != null ? rtaAprroved_Master[3].toString().trim() : "N";
                    SpotChallan.OtpResponseDelayTime = rtaAprroved_Master[4].toString() != null ? rtaAprroved_Master[4].toString().trim() : "0";

                    SharedPreferences sharedPreference = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = sharedPreference.edit();
                    edit.putString("IMAGE", "" + image_data_tosend);
                    edit.commit();
                    startActivity(new Intent(Drunk_Drive.this, GenerateDrunkDriveCase.class));


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

                rtaAprroved_Master = new String[0];

                SpotChallan.OtpStatus = "N";
                SpotChallan.OtpResponseDelayTime = "0";

                SharedPreferences sharedPreference = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = sharedPreference.edit();
                edit.putString("IMAGE", "" + image_data_tosend);
                edit.commit();
                startActivity(new Intent(Drunk_Drive.this, GenerateDrunkDriveCase.class));

            }
        }

    }

    public void ShowMessage(final String Message) {

        Drunk_Drive.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView title = new TextView(Drunk_Drive.this);
                title.setText("ALERT");
                title.setBackgroundColor(Color.RED);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(20);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(80);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Drunk_Drive.this,
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
                textView.setTextSize(20);
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

}