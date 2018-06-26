package com.mtpv.mobilee_ticket;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.ServiceHelper;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.fabric.sdk.android.BuildConfig;
import io.fabric.sdk.android.Fabric;
import mother.com.test.PidSecEncrypt;

@SuppressLint({"WorldReadableFiles", "NewApi", "SimpleDateFormat"})
public class MainActivity extends Activity implements OnClickListener, LocationListener {

    final int SPLASH_DIALOG = 0, PROGRESS_DIALOG = 1;

    EditText et_pid, et_pid_pwd;

    Button btn_cancel, btn_submit;

    TextView tv_ip_settings;
    TextView cur_val;

    int downloadedSize = 0, totalSize = 0;
    Dialog dialog;

    String server = "192.168.11.9", username = "ftpuser", password = "Dk0r$l1qMp6", filename = "Version-1.5.1.apk";
    int port = 99;


    DBHelper db;

    public static String otpno = null, pidCodestatic = null;

    public static String[] arr_logindetails;

    LocationManager m_locationlistner;
    android.location.Location location;

    String officerLogin_Otp = null;

    public static double latitude = 0.0, longitude = 0.0;

    public static String UNIT_CODE = "", UNIT_NAME = "", IMEI = "", URL = "", user_id = "", appVersion = null,
            user_pwd = "", e_user_id = null, sim_No = null, e_user_tmp = "";

    ProgressBar progress;

    boolean isGPSEnabled = false, isNetworkEnabled = false, canGetLocation = false;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10, MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    SharedPreferences preference;
    SharedPreferences.Editor editor;
    public static String service_type = "", services_url = "", ftps_url = "";

    TextView textView2;


    //private String url_to_fix = "/services/MobileEticketServiceImpl?wsdl";

    private String url_to_fix = "/services/MobileEticketServiceImpl?wsdl";
    @SuppressWarnings("unused")
    private String test_service_url = "http://192.168.11.55:8080/eTicketMobileHyd";
    @SuppressWarnings("unused")
    private String live_service_url = "http://192.168.11.4/eTicketMobileHyd";
    public String open_NW_URL = "https://www.echallan.org/eTicketMobileHyd";

    private static final String[] requiredPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.INSTALL_SHORTCUT
    };

    private static final int REQUEST_PERMISSIONS = 20;
    private SparseIntArray mErrorString;

    public static String psName, cadre_name, pidName, uintCode;

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.login);

        mErrorString = new SparseIntArray();

        if (Build.VERSION.SDK_INT > 22 && !hasPermissions(requiredPermissions)) {

            MainActivity.this.requestAppPermissions(new
                            String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET,
                            Manifest.permission.CAMERA,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INSTALL_SHORTCUT}, R.string
                            .runtime_permissions_txt
                    , REQUEST_PERMISSIONS);
        }

        showDialog(SPLASH_DIALOG);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                removeDialog(SPLASH_DIALOG);
            }
        }, 2500);

        textView2 = (TextView) findViewById(R.id.textView2);
        appVersion = textView2.getText().toString().trim();

/*      if(isOnline()) {
            asyn_Version_Check asyn_version_check = new asyn_Version_Check();
            asyn_version_check.execute();
        }else {
            showToast("Please Check Your Network Connection For Checking Patch Details");
        }*/


        LoadUIcomponents();


        if (android.os.Build.VERSION.SDK_INT > 11) {
            StrictMode.ThreadPolicy polocy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(polocy);
        }


        preference = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        service_type = preference.getString("servicetype", "test");
        services_url = preference.getString("serviceurl", "url1");
        ftps_url = preference.getString("ftpurl", "url2");

        SharedPreferences prefs = getSharedPreferences("ShortCutPrefs", MODE_PRIVATE);
        if (!prefs.getBoolean("isFirstTime", false)) {
            addShortcut();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstTime", true);
            editor.commit();
        }

        if ((!services_url.equals("url1") && (service_type.equals("live")))) {
            URL = "" + services_url + "" + url_to_fix;
        } else if ((!services_url.equals("url1") && (service_type.equals("test")))) {
            URL = "" + services_url + "" + url_to_fix;
        }
    }

    private void addShortcut() {
        Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
        shortcutIntent.addFlags(flags);

        Intent addIntent = new Intent();
        addIntent.putExtra("duplicate", false);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.logo));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }

    private void LoadUIcomponents() {
        // TODO Auto-generated method stub

        et_pid = (EditText) findViewById(R.id.edtpidcode_login_xml);
        et_pid_pwd = (EditText) findViewById(R.id.edtpidpwd_login_xml);
        btn_cancel = (Button) findViewById(R.id.btncancel_login_xml);
        btn_submit = (Button) findViewById(R.id.btnsubmit_login_xml);
        tv_ip_settings = (TextView) findViewById(R.id.tv_ipsettings);

       /* et_pid.setText("23001004");
        et_pid_pwd.setText("Ranga2018");*/
        btn_cancel.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_ip_settings.setOnClickListener(this);

        db = new DBHelper(getApplicationContext());
    }

    @SuppressWarnings("unused")
    public void getLocation() {

        try {
            m_locationlistner = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = m_locationlistner.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = m_locationlistner.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                latitude = 0.0;
                longitude = 0.0;
            } else {
                this.canGetLocation = true;

                String current_time = null;
                String current_date = null;

                if (isNetworkEnabled) {
                    m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

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
                            if (gps_Date == null) {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                            }
                        } else {
                            latitude = 0.0;
                            longitude = 0.0;
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        m_locationlistner.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

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
                                if (gps_Date == null) {
                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS),
                                            0);
                                }

                            } else {
                                latitude = 0.0;
                                longitude = 0.0;
                            }
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            showToast("Please check the GPS Location !");

        } catch (Exception e) {
            e.printStackTrace();
            showToast("Please check the GPS Location !");
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = getDeviceID(telephonyManager);
        if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            sim_No = "" + telephonyManager.getSimSerialNumber();
//            String phone_Number=""+telephonyManager.getLine1Number();
        } else {
            sim_No = "";
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }
    }


    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    String getDeviceID(TelephonyManager phonyManager) {

        String id = phonyManager.getDeviceId();
        if (id == null) {
            id = "not available";
        }

        int phoneType = phonyManager.getPhoneType();

        switch (phoneType) {

            case TelephonyManager.PHONE_TYPE_NONE:
                return id;

            case TelephonyManager.PHONE_TYPE_GSM:
                return id;

            case TelephonyManager.PHONE_TYPE_CDMA:
                return id;

            default:
                return "UNKNOWN:ID=" + id;

        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.btncancel_login_xml:
                clearInputFields();
                break;

            case R.id.btnsubmit_login_xml:
                String pidcode = et_pid.getText().toString();
                String password = et_pid_pwd.getText().toString();

                String PS_CODE = null;
                String PS_NAME = null;
                String CADRE_CODE = null;
                String CADRE_NAME = null;
                String UNIT_CODE = null;
                String UNIT_NAME = null;

                DBHelper helper = new DBHelper(getApplicationContext());
                ContentValues values = new ContentValues();
                values.put("PIDCODE", pidcode);
                values.put("PASSWORD", password);
                values.put("PIDNAME", pidcode);
                values.put("PS_CODE", PS_CODE);
                values.put("PS_NAME", PS_NAME);
                values.put("CADRE_CODE", CADRE_CODE);
                values.put("CADRE_NAME", CADRE_NAME);
                values.put("UNIT_CODE", UNIT_CODE);
                values.put("UNIT_NAME", UNIT_NAME);

                SQLiteDatabase db = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                db.execSQL(DBHelper.CREATE_USER_TABLE);
                db.execSQL("delete from " + DBHelper.USER_TABLE);
                db.insert(DBHelper.USER_TABLE, null, values); // Inserting Row

                if (et_pid.getText().toString().trim().equals("")) {
                    et_pid.setError(Html.fromHtml("<font color='black'>Enter PID</font>"));

                } else if (et_pid_pwd.getText().toString().trim().equals("")) {
                    et_pid_pwd.setError(Html.fromHtml("<font color='black'>Enter password</font>"));

                } else {

                    if ((!services_url.equals("url1")) && (!ftps_url.equals("url2"))) {

                        if (isOnline()) {
                            user_id = "" + et_pid.getText().toString().trim();
                            user_pwd = "" + et_pid_pwd.getText().toString().trim();

                            getLocation();

                            try {
                                e_user_id = PidSecEncrypt.encryptmd5(user_id);
                                e_user_tmp = PidSecEncrypt.encryptmd5(user_pwd);

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            showToast("Please check your network connection !");
                        }

                        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            if (isOnline()) {
                                new Async_task_login().execute();
                            } else {
                                showToast("Please check your network connection !");
                            }

                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setMessage("GPS is Disabled in your Device \nPlease Enable LOCATION ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent callGPSSettingIntent = new Intent(
                                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivity(callGPSSettingIntent);
                                        }
                                    });
                            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alert = alertDialogBuilder.create();
                            alert.show();
                        }
                    } else {
                        showToast("Set IP Settings!");
                    }
                }
                break;

            case R.id.tv_ipsettings:
                startActivity(new Intent(MainActivity.this, IPSettings.class));
                break;

            default:
                break;
        }
    }

    protected void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("    GPS is Disabled in your Device \n            Please Enable GPS?")
                .setCancelable(false).setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        if (nwInfo != null && nwInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public Boolean dataConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mobileInfo != null;
    }

    private void clearInputFields() {
        // TODO Auto-generated method stub
        et_pid.setText("");
        et_pid_pwd.setText("");
    }

    public class Async_task_login extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            String[] version_split = appVersion.split("\\-");
            ServiceHelper.login("" + user_id, "" + e_user_tmp, "" + IMEI, "" + sim_No, "" + latitude, "" + longitude,
                    "" + version_split[1]);
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

            try {


                if (ServiceHelper.Opdata_Chalana != null && "0" != ServiceHelper.Opdata_Chalana.toString()) {
                    if (ServiceHelper.Opdata_Chalana.toString().trim().equals("1")) {
                        showToast("Invalid Login ID");
                    } else if (ServiceHelper.Opdata_Chalana.toString().trim().equals("2")) {
                        showToast("Invalid Password");
                    } else if (ServiceHelper.Opdata_Chalana.toString().trim().equals("3")) {
                        showToast("Unauthorized Device");

                    } else if (ServiceHelper.Opdata_Chalana.toString().trim().equals("4")) {
                        showToast("Error, Please Contact E Challan Team at 040-27852721");
                    } else if (ServiceHelper.Opdata_Chalana.toString().trim().equals("5")) {
                        showToast(
                                "You have Exceeded Number of \n Attempts with Wrong Password,\n Please Contact E Challan Team at 040-27852721 ");
                    } else if (ServiceHelper.Opdata_Chalana.toString().trim().equals("0")) {
                        showToast("Please Check Your Network And Try Again");
                    } else {

                        MainActivity.arr_logindetails = ServiceHelper.Opdata_Chalana.split(":");

                        for (int i = 0; i < MainActivity.arr_logindetails.length; i++) {
                        }


                        try {
                            SharedPreferences sharedPreferences = PreferenceManager
                                    .getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editors = sharedPreferences.edit();

                            String pidCode = "" + arr_logindetails[0];
                            uintCode = pidCode.substring(0, 2);
                            pidCodestatic = "" + arr_logindetails[0];
                            pidName = "" + arr_logindetails[1];
                            String psCd = "" + arr_logindetails[2];
                            psName = "" + arr_logindetails[3];
                            String cadre_code = "" + arr_logindetails[4];
                            cadre_name = "" + arr_logindetails[5];
                            String pass_word = "" + user_pwd;
                            String off_phone_no = "" + arr_logindetails[6];
                            String current_version = "" + arr_logindetails[7];
                            String rta_data_flg = "" + arr_logindetails[8];
                            Log.i("", "" + rta_data_flg);
                            String dl_data_flg = "" + arr_logindetails[9];
                            Log.i("", "" + dl_data_flg);
                            String aadhaar_data_flg = "" + arr_logindetails[10];
                            Log.i("", "" + aadhaar_data_flg);
                            String otp_no_flg = "" + arr_logindetails[11];
                            String cashless_flg = "" + arr_logindetails[12];
                            String mobileNo_flg = "" + arr_logindetails[13];

                            MainActivity.otpno = "" + arr_logindetails[14];

                            Log.i("Otp for Login", "" + MainActivity.otpno);

                            if (arr_logindetails != null && arr_logindetails.length == 16) {
                                officerLogin_Otp = "" + arr_logindetails[15];
                            }
                            editors.putString("PID_CODE", pidCode);
                            editors.putString("PID_NAME", pidName);
                            editors.putString("PS_CODE", psCd);
                            editors.putString("PS_NAME", psName);
                            editors.putString("CADRE_CODE", cadre_code);
                            editors.putString("CADRE_NAME", cadre_name);
                            editors.putString("PASS_WORD", pass_word);
                            editors.putString("OFF_PHONE_NO", off_phone_no);
                            editors.putString("CURRENT_VERSION", current_version);
                            editors.putString("RTA_DATA_FLAG", rta_data_flg);
                            editors.putString("DL_DATA_FLAG", dl_data_flg);
                            editors.putString("AADHAAR_DATA_FLAG", aadhaar_data_flg);
                            editors.putString("OTP_NO_FLAG", otp_no_flg);
                            editors.putString("CASHLESS_FLAG", cashless_flg);
                            editors.putString("MOBILE_NO_FLAG", mobileNo_flg);
                            editors.putString("officerLogin_Otp", officerLogin_Otp);
                            editors.commit();

                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }


                        if (!"null".equals(officerLogin_Otp) && officerLogin_Otp.equalsIgnoreCase("Y")) {

                            Intent i = new Intent(MainActivity.this, Login_otp.class);
                            startActivity(i);

                        } else {

                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            finish();
                        }


                    }
                } else {
                    showToast("Please Check Your Network And Try Again Login Failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
            case SPLASH_DIALOG:
                Dialog dg_splash = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dg_splash.setCancelable(false);
                dg_splash.setContentView(R.layout.splash);
                return dg_splash;

            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "", true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);
                return pd;


            default:
                break;

        }
        return super.onCreateDialog(id);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        preference = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        services_url = preference.getString("serviceurl", "url1");
        ftps_url = preference.getString("ftpurl", "url2");

        if (!services_url.equals("urls1")) {
            URL = "" + services_url + "" + url_to_fix;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        preference = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        services_url = preference.getString("serviceurl", "url1");
        ftps_url = preference.getString("ftpurl", "url2");
    }

    private void showToast(String msg) {
        // TODO Auto-generated method stub
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_LONG);
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
        TextView title = new TextView(this);
        title.setText("Hyderabad E-Ticket");
        title.setBackgroundColor(Color.RED);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(26);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
        title.setPadding(20, 0, 20, 0);
        title.setHeight(70);

        String otp_message = "\n Are you sure, You want to Leave Application...! \n";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this,
                AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setCustomTitle(title);
        alertDialogBuilder.setIcon(R.drawable.dialog_logo);
        alertDialogBuilder.setMessage(otp_message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

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

        Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        btn2.setTextSize(22);
        btn2.setTextColor(Color.WHITE);
        btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
        btn2.setBackgroundColor(Color.RED);
    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + MainActivity.this.checkSelfPermission(permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || MainActivity.this.shouldShowRequestPermissionRationale(permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar.make(findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MainActivity.this.requestPermissions(requestedPermissions, requestCode);
                            }
                        }).show();
            } else {
                MainActivity.this.requestPermissions(requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    public void onPermissionsGranted(final int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    public boolean hasPermissions(String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != checkCallingOrSelfPermission(permission))
                return false;
        return true;
    }


    public class asyn_Version_Check extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {

            String[] version_split = appVersion.split("\\-");

            String result = ServiceHelper.VersionCheck(version_split[1]);

            return result;
        }


        @Override
        protected void onPostExecute(String current_version) {
            super.onPostExecute(current_version);
            removeDialog(PROGRESS_DIALOG);

            if (current_version != null && current_version != "0") {

                if (current_version.equalsIgnoreCase("N")) {
                    showMessageforUpdatePatch();
                }
            }


        }
    }

    public void showMessageforUpdatePatch() {

        TextView title = new TextView(this);
        title.setText("Hyderabad E-Ticket");
        title.setBackgroundColor(Color.RED);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(26);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
        title.setPadding(20, 0, 20, 0);
        title.setHeight(70);

        String otp_message = "\nPlease Update your Application...! \n";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this,
                AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setCustomTitle(title);
        alertDialogBuilder.setIcon(R.drawable.dialog_logo);
        alertDialogBuilder.setMessage(otp_message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                new Async_UpdateApk().execute();


            }
        });

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

    }


    public class Async_UpdateApk extends AsyncTask<Void, Void, String> {

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);

        }

        @SuppressLint("SdCardPath")
        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            FTPClient ftpClient = new FTPClient();

            try {
                if (null != MainActivity.services_url && MainActivity.services_url.equals("https://www.echallan.org/eTicketMobileHyd")) {
                    server = "125.16.1.69";
                } else {
                    server = "192.168.11.9";
                }
                Log.i("server URL ::", "" + server);
                ftpClient.connect(server, port);
                ftpClient.login(username, password);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setBufferSize(1024 * 1024);
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                File downloadFile1 = new File("/sdcard/Download/ETicketHYD.apk");
                String remoteFile1 = "/23/TabAPK" + "/ETicketHYD.apk";

                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                boolean success = ftpClient.retrieveFile(remoteFile1, outputStream);

                FileOutputStream fileOutput = new FileOutputStream(downloadFile1);
                InputStream inputStream = ftpClient.retrieveFileStream(remoteFile1);
                if (inputStream == null || ftpClient.getReplyCode() == 550) {
                    fileOutput.close();
                    outputStream.close();
                    runOnUiThread(new Runnable() {

                        @SuppressWarnings("deprecation")
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            removeDialog(PROGRESS_DIALOG);

                            TextView title = new TextView(MainActivity.this);
                            title.setText("Hyderabad E-Ticket");
                            title.setBackgroundColor(Color.RED);
                            title.setGravity(Gravity.CENTER);
                            title.setTextColor(Color.WHITE);
                            title.setTextSize(26);
                            title.setTypeface(title.getTypeface(), Typeface.BOLD);
                            title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0,
                                    R.drawable.dialog_logo, 0);
                            title.setPadding(20, 0, 20, 0);
                            title.setHeight(70);

                            String otp_message = "\n Your Application is Upto Date \n No Need to Update \n";

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this,
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
                } else {

                    try {
                        SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                        db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.psName_table);
                        db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.wheelercode_table);
                        db2.execSQL(DBHelper.psNamesCreation);
                        db2.execSQL(DBHelper.wheelerCodeCreation);
                        db2.close();
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    totalSize = remoteFile1.length();

                    runOnUiThread(new Runnable() {
                        @SuppressWarnings("deprecation")
                        public void run() {
                            removeDialog(PROGRESS_DIALOG);
                            showProgress(server);
                            progress.setMax(totalSize);
                        }
                    });

                    byte[] buffer = new byte[1024];
                    int bufferLength = 0;

                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        fileOutput.write(buffer, 0, bufferLength);
                        downloadedSize += bufferLength;

                        runOnUiThread(new Runnable() {
                            public void run() {
                                progress.setProgress(downloadedSize);
                                float per = ((float) downloadedSize / totalSize) * 100;

                                cur_val.setText((int) per / 1500000 + "%");
                            }
                        });
                    }
                    fileOutput.close();
                    outputStream.close();

                    if (success) {
                        ftpClient.logout();
                        ftpClient.disconnect();

                        try {
                            db.open();
                            db.execSQL("delete from " + DBHelper.psName_table);
                            db.execSQL("delete from " + DBHelper.wheeler_code);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                            db.close();
                        }

                        finish();
                        System.out.println("File #1 has been downloaded successfully.");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(
                                Uri.fromFile(new File(
                                        Environment.getExternalStorageDirectory() + "/download/" + "ETicketHYD.apk")),
                                "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                }

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }


    @SuppressWarnings("deprecation")
    private void showProgress(String server) {
        // TODO Auto-generated method stub
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");
        dialog.setCancelable(false);

        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Downloading file ... ");
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("It may Take Few Minutes.....");
        dialog.show();

        progress = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        progress.setProgress(0);
        progress.setMax(100);
        progress.setIndeterminate(true);
        progress.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }

}
