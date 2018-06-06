package com.mtpv.mobilee_ticket;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatImageView;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.DateUtil;
import com.mtpv.mobilee_ticket_services.ServiceHelper;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("InlinedApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Dashboard extends Activity implements OnClickListener {

    String server = "192.168.11.9", username = "ftpuser", password = "Dk0r$l1qMp6", filename = "Version-1.5.1.apk", netwrk_info_txt = "";
    int port = 99;

    public static String otpbuttoncheckinspldrive = null;
    @SuppressWarnings("unused")
    private static final int BUFFER_SIZE = 4096;
    ProgressBar progress;
    Dialog dialog;
    int downloadedSize = 0, totalSize = 0;
    TextView cur_val;

    TextView tv_login_username, echallan, echallan_reports, tv_drunk_and_drive, tv_spot_challan, tv_vehicle_history, tv_echallannon;
    TextView tv_towing_cp_act, tv_release_document, tv_reports, tv_duplicate_print, tv_settings, tv_sync;
    ImageButton ibtn_logout;
    ImageView offline_btn, aadhaar;

    TextView tv_about_version, tv_special_drive;

    final int EXIT_DIALOG = 0, ALERT_USER = 1, PROGRESS_DIALOG = 2, MASTER_DOWNLOAD = 3;

    String version = null, address_spot;

    public static boolean rtaFLG = false;

    DBHelper db;
    Cursor c, cursor_psnames, c_dup_res, c_whlr_details, c_bar_details, c_occup_details;

    public static String[][] whlr_code_name;
    public static ArrayList<String> whlr_name_arr;

    public static String[][] vchleCat_code_name;
    public static ArrayList<String> vchleCat_name_arr_list;

    public static String[][] vchle_MainCat_code_name;
    public static ArrayList<String> vchle_MainCat_name_arr_list;

    public static String[][] ocuptn_code_name;
    public static ArrayList<String> ocuptn_name_arr_list;

    public static String[][] bar_code_name;
    public static ArrayList<String> bar_name_arr_list;

    public static String[][] qlfctn_code_name;
    public static ArrayList<String> qlfctn_name_arr_list;

    public static String modified_url = "";

    public static String UNIT_CODE = "";
    public static String UNIT_NAME = "Hyderabad";
    public static String VEH_CAT_FIX = "99";
    public static String VEH_MAINCAT_FIX = "99";
    public static String VEH_SUBCAT_FIX = "9999";

    ArrayList<String> ps_codes_fr_names_arr;
    ArrayList<String> ps_names_arr;
    String[][] psname_name_code_arr;

    ArrayList<String> vio_points_fr_names_arr;
    ArrayList<String> vio_points_names_arr;
    String[][] vio_points_name_code_arr;

    ArrayList<String> occupation_fr_names_arr;
    ArrayList<String> occupation_names_arr;
    String[][] occupation_code_arr;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String psname_settings = "";
    String pointnameBycode_settings = "";
    String psResName_settings = "";

    public static String rta_details_request_from = "";
    public static String licence_details_request_from = "";

    String[] violation_details_master;
    String[][] violation_detailed_views;

    String[] wheeler_code_arr_spot;
    Cursor c_whlr;

    public static String check_vhleHistory_or_Spot = "";
    public static String check_specialdrive_or_Spot = "";

    String under_process_msg = "Under Process!";

    public static boolean activty_status = true;

    Calendar calendar;
    int present_date;
    int present_month;
    int present_year;
    SimpleDateFormat date_format;
    String present_date_toSend = "";
    TelephonyManager telephonyManager;
    String imei_send = "";
    String simid_send = "";


    public static String terminalID, bluetoothName, bluetoothAddress;

    TextView versin_txt;
    static String current_version = "Y";

    public static String CHALLAN_TYPE = null, CASES_LIMIT = null, CASES_BOOKED = null;

    AppCompatImageView img_DD, img_Spot, img_VehcleHis, img_CraneAct, img_ReleaseDoc, img_Reports, img_DuplctePrint,
            img_DwldMasters, img_Settings, img_About;

    ImageView img_logo;
    TextView officer_Name, officer_Cadre, officer_PS;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dashboard);

        UNIT_CODE = MainActivity.arr_logindetails[0].substring(0, 2);

        LoadUIComponents();

        db = new DBHelper(getApplicationContext());

        netwrk_info_txt = "" + getResources().getString(R.string.newtork_txt);

        versin_txt = (TextView) findViewById(R.id.textView2);

        version = versin_txt.getText().toString().trim().split("\\-")[1];

        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        current_version = sharedPreference.getString("CURRENT_VERSION", "");

        img_logo = (ImageView) findViewById(R.id.img_logo);
        if (MainActivity.uintCode.equals("22")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.cyb_logo));
        } else if (MainActivity.uintCode.equals("23")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.htp_left));
        } else if (MainActivity.uintCode.equals("24")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.rac_logo));
        } else {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.htp_left));
        }
        officer_Name = (TextView) findViewById(R.id.officer_Name);
        officer_Cadre = (TextView) findViewById(R.id.officer_cadre);
        officer_PS = (TextView) findViewById(R.id.officer_PS);

        officer_Name.setText(MainActivity.pidName + "(" + MainActivity.cadre_name + ")");
        officer_Cadre.setText(MainActivity.cadre_name);
        officer_PS.setText(MainActivity.psName);


        if (current_version.equals("N")) {

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

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this,
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
                    current_version = "Y";
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

        if (Build.VERSION.SDK_INT > 11) {
            StrictMode.ThreadPolicy polocy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(polocy);
        }

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

                            TextView title = new TextView(Dashboard.this);
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

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this,
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

                                cur_val.setText((int) per / 225000 + "%");
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
/*                        System.out.println("File #1 has been downloaded successfully.");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
*//*                        intent.setDataAndType(
                                Uri.fromFile(new File(
                                        Environment.getExternalStorageDirectory() + "/download/" + "ETicketHYD.apk")),
                                "application/vnd.android.package-archive");*//*
                        intent.setType("application/vnd.android.package-archive");

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Dashboard.this,
                                BuildConfig.APPLICATION_ID + ".provider",new File(Environment.getExternalStorageDirectory() + "/download/" + "ETicketHYD.apk")));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/


                        if (Build.VERSION.SDK_INT <= 23) {

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(
                                    Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "ETicketHYD.apk")),
                                    "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {

                            Uri apkUri = FileProvider.getUriForFile(Dashboard.this, BuildConfig.APPLICATION_ID +
                                    ".provider", new File(Environment.getExternalStorageDirectory() + "/download/" + "ETicketHYD.apk"));
                            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                            intent.setData(apkUri);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                        }

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
        dialog = new Dialog(Dashboard.this);
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

    private void LoadUIComponents() {
        // TODO Auto-generated method stub

        tv_login_username = (TextView) findViewById(R.id.tv_login_username_dashboard_xml);
        tv_drunk_and_drive = (TextView) findViewById(R.id.tv_generateticket_dashboard_xml);// drunk&drive
        tv_spot_challan = (TextView) findViewById(R.id.tv_spot_dashboard_xml);
        tv_vehicle_history = (TextView) findViewById(R.id.tv_vhclehistory_dashboard_xml);
        tv_towing_cp_act = (TextView) findViewById(R.id.tv_towing_dashboard_xml);
        tv_release_document = (TextView) findViewById(R.id.tv_releasedocuments_dashboard_xml);
        tv_reports = (TextView) findViewById(R.id.tv_reports_dashboard_xml);
        tv_duplicate_print = (TextView) findViewById(R.id.tvduplicateprint_dashboad_xml);
        tv_settings = (TextView) findViewById(R.id.tv_settings_dashboard_xml);

        tv_sync = (TextView) findViewById(R.id.tv_sync_dashboard_xml);

        tv_about_version = (TextView) findViewById(R.id.tv_about_version);
        tv_special_drive = (TextView) findViewById(R.id.tv_special_drive);
        // tv_dd_basic=(TextView)findViewById(R.id.tv_dd_basic);
        tv_echallannon = (TextView) findViewById(R.id.tv_echallannon);


        aadhaar = (ImageView) findViewById(R.id.aadhaar);

        echallan = (TextView) findViewById(R.id.echallan);
        echallan_reports = (TextView) findViewById(R.id.echallan_reports);


        img_DD = (AppCompatImageView) findViewById(R.id.img_DrunkDrive);
        img_Spot = (AppCompatImageView) findViewById(R.id.img_Spot);
        img_VehcleHis = (AppCompatImageView) findViewById(R.id.img_VehicleHistory);
        img_CraneAct = (AppCompatImageView) findViewById(R.id.img_CraneAct);
        img_ReleaseDoc = (AppCompatImageView) findViewById(R.id.img_ReleaseDoc);
        img_Reports = (AppCompatImageView) findViewById(R.id.img_Reports);
        img_DuplctePrint = (AppCompatImageView) findViewById(R.id.img_DuplicatePrint);
        img_DwldMasters = (AppCompatImageView) findViewById(R.id.img_DownldMastr);
        img_Settings = (AppCompatImageView) findViewById(R.id.img_Settings);
        img_About = (AppCompatImageView) findViewById(R.id.img_About);


        ibtn_logout = (ImageButton) findViewById(R.id.imgbtn_logout_dashboard_xml);
        ibtn_logout.setOnClickListener(this);

        img_DD.setOnClickListener(this);
        img_Spot.setOnClickListener(this);
        img_VehcleHis.setOnClickListener(this);
        img_CraneAct.setOnClickListener(this);
        img_ReleaseDoc.setOnClickListener(this);
        img_Reports.setOnClickListener(this);
        img_DuplctePrint.setOnClickListener(this);
        img_Settings.setOnClickListener(this);
        img_DwldMasters.setOnClickListener(this);
        img_About.setOnClickListener(this);


        //tv_drunk_and_drive.setOnClickListener(this);
        //tv_settings.setOnClickListener(this);
        //tv_sync.setOnClickListener(this);
       // tv_duplicate_print.setOnClickListener(this);
       // tv_spot_challan.setOnClickListener(this);
       // tv_vehicle_history.setOnClickListener(this);
        //tv_towing_cp_act.setOnClickListener(this);
       // tv_release_document.setOnClickListener(this);
       // tv_reports.setOnClickListener(this);
        tv_special_drive.setOnClickListener(this);
       // tv_about_version.setOnClickListener(this);
        //tv_dd_basic.setOnClickListener(this);
        tv_echallannon.setOnClickListener(this);

        //aadhaar.setOnClickListener(this);

//        echallan.setOnClickListener(this);
        //echallan_reports.setOnClickListener(this);

        if (MainActivity.arr_logindetails != null && MainActivity.arr_logindetails.length > 0) {
            tv_login_username.setText("Welcome : " + MainActivity.arr_logindetails[1]);
        }
        db = new DBHelper(getApplicationContext());
    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

    @SuppressWarnings({"static-access", "deprecation"})
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.img_DrunkDrive:

                check_vhleHistory_or_Spot = "drunkdrive";
                Dashboard.otpbuttoncheckinspldrive = "dd";
                ddCommonMethod();

                break;


          /*  case R.id.tv_dd_basic:

                check_vhleHistory_or_Spot = "drunkdrive_withourDlAadhar";
                Dashboard.otpbuttoncheckinspldrive="dd";
                ddCommonMethod();

                break;*/


            case R.id.tv_echallannon:

                check_vhleHistory_or_Spot = "noncontact";
                //    Dashboard.otpbuttoncheckinspldrive="dd";

                rtaFLG = true;
                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {


                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    }
                   /* else if(psResName_settings.equals("location")){
                        showToast("Configure Settings!");
                    }*/

                    else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            //    Dashboard.otpbuttoncheckinspldrive="Spot";
                            startActivity(new Intent(Dashboard.this, E_Challan_Non_ContactModule.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();
                break;


            case R.id.img_Settings:
                try {

                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);
                    if (cursor_psnames.getCount() == 0) {
                        showToast("Please download master's !");
                    } else {
                        startActivity(new Intent(this, Settings_New.class));
                    }

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                cursor_psnames.close();
                db.close();
                break;

            case R.id.imgbtn_logout_dashboard_xml:
                showDialog(EXIT_DIALOG);
                break;

            case R.id.img_DownldMastr:


                showDialog(ALERT_USER);

                break;

            case R.id.img_DuplicatePrint:
                check_vhleHistory_or_Spot = "";

                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {

                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    }
                    /*else if(psResName_settings.equals("location")){
                        showToast("Configure Settings!");
                    }*/
                    else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, DuplicatePrint.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();
                break;

            case R.id.img_Spot:
                rtaFLG = true;
                check_vhleHistory_or_Spot = "spot";
                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {


                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    }
                   /* else if(Settings_New.btn_ResponsiblePSName.getText().toString().equals("Responsible PS Name")){
                        showToast("Configure Settings!");
                    }*/
                    else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            Dashboard.otpbuttoncheckinspldrive = "Spot";
                            startActivity(new Intent(Dashboard.this, SpotChallan.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();
                break;

            case R.id.img_VehicleHistory:
                check_vhleHistory_or_Spot = "vehiclehistory";
                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {

                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);
                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    } else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, SpotChallan.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();
                break;

            case R.id.img_CraneAct:
                check_vhleHistory_or_Spot = "towing";
                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {


                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);
                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    } else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, SpotChallan.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();
                break;

            case R.id.img_ReleaseDoc:
                check_vhleHistory_or_Spot = "releasedocuments";

                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {

                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);
                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    } else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, SpotChallan.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();

                break;

            case R.id.img_Reports:
                check_vhleHistory_or_Spot = "reports";

                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {

                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    } else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, DuplicatePrint.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();

                break;

            case R.id.img_About:
                if (isOnline()) {
                    new Async_About_Version().execute();
                } else {
                    showToast("Please Check Your Network Connection");
                }

                break;

            case R.id.tv_special_drive:

                if (isOnline()) {

                    getPreferenceValues();

                    preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                    editor = preferences.edit();
                    address_spot = preferences.getString("btaddress", "btaddr");

                    try {
                        db.open();
                        cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                        c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                        if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                            showToast("Please download master's !");
                        } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                            showToast("Configure Settings!");
                        } else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                            showToast("Configure BlueTooth Settings!");
                        } else {
                            if (isOnline()) {

                                Dashboard.otpbuttoncheckinspldrive = "mtpv_SpecialDrive";
                                startActivity(new Intent(Dashboard.this, mtpv_SpecialDrive.class));
                            } else {
                                showToast("" + netwrk_info_txt);
                            }
                        }

                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        c_whlr_details.close();
                        cursor_psnames.close();
                        db.close();
                    }
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();

                } else {
                    showToast("" + netwrk_info_txt);
                }
                break;


            case R.id.echallan:

                check_vhleHistory_or_Spot = "echallan";
                startActivity(new Intent(Dashboard.this, E_Challan.class));

                break;

            case R.id.echallan_reports:

                check_vhleHistory_or_Spot = "echallanreports";
                startActivity(new Intent(Dashboard.this, E_Challan_Reports.class));

                break;

            case R.id.aadhaar:

                startActivity(new Intent(Dashboard.this, AadhaarUpdate.class));

                break;
            default:
                break;
        }
    }

    public class Async_About_Version extends AsyncTask<Void, Void, String> {

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getversiondetails(UNIT_CODE, "TAB", version);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (ServiceHelper.version_response != null && !ServiceHelper.version_response.equals("NA")) {
                Intent i = new Intent(getApplicationContext(), ReleaseversionActivity.class);
                startActivity(i);
            } else {
                showToast("No Updates Found !!!");
            }
        }
    }

    @SuppressWarnings("unused")
    private void getSimImeiNo() {
        // TODO Auto-generated method stub
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei_send = telephonyManager.getDeviceId();// TO GET IMEI NUMBER
        if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            simid_send = "" + telephonyManager.getSimSerialNumber();
        } else {
            simid_send = "";
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("WorldReadableFiles")
    private void getPreferenceValues() {
        // TODO Auto-generated method stub
        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = preferences.edit();
        psname_settings = preferences.getString("psname_name", "psname");

        pointnameBycode_settings = preferences.getString("point_name", "pointname");

        psResName_settings = preferences.getString("ps_res_name_name", "location");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
            case EXIT_DIALOG:
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

                String otp_message = "\n Are You Sure,\nDo You Want To Exit? \n";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this,
                        AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(otp_message);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();
                    }
                });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        removeDialog(EXIT_DIALOG);

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

                Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btn2.setTextSize(22);
                btn2.setTextColor(Color.WHITE);
                btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
                btn2.setBackgroundColor(Color.RED);
                return alertDialog;

            case ALERT_USER:
                TextView title2 = new TextView(this);
                title2.setText("ALERT");
                title2.setBackgroundColor(Color.RED);
                title2.setGravity(Gravity.CENTER);
                title2.setTextColor(Color.WHITE);
                title2.setTextSize(26);
                title2.setTypeface(title2.getTypeface(), Typeface.BOLD);
                title2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title2.setPadding(20, 0, 20, 0);
                title2.setHeight(70);

                String network_message = "\nPlease Check Your Network Connection Properly. This Process Takes Some Time To Download Masters\n";

                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this, AlertDialog.THEME_HOLO_LIGHT);
                builder.setCustomTitle(title2);
                builder.setIcon(R.drawable.alert);
                builder.setMessage(network_message);
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @SuppressWarnings("static-access")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (isOnline()) {
                            try {
                                db.open();
                                db.Delete_ReadingData("" + db.wheelercode_table);
                                db.Delete_ReadingData("" + db.psName_table);
                                db.Delete_ReadingData("" + db.occupation_table);
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                db.close();
                            }
                            db.close();

                            if (isOnline()) {
                                Async_wheler_details wheeler_task = new Async_wheler_details();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    wheeler_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    wheeler_task.execute();
                                }
                            } else {
                                showToast("Please Check Your Network Connection For Downloading Masters Fully Download");
                            }

                            if (isOnline()) {
                                Async_getPsDetails ps_task = new Async_getPsDetails();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    ps_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    ps_task.execute();
                                }

                            } else {
                                showToast("Please Check Your Network Connection For Downloading Masters Fully Download");
                            }
                            if (isOnline()) {
                                Async_ocuptn ocptn_task = new Async_ocuptn();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    ocptn_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    ocptn_task.execute();
                                }
                            } else {
                                showToast("Please Check Your Network Connection For Downloading Masters Fully Download");
                            }

                            if (isOnline()) {
                                Async_qlfctn qlfcn_task = new Async_qlfctn();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    qlfcn_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    qlfcn_task.execute();
                                }
                            } else {
                                showToast("Please Check Your Network Connection For Downloading Masters Fully Download");
                            }

                            if (isOnline()) {
                                Async_BarDetails bar_task = new Async_BarDetails();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    bar_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    bar_task.execute();
                                }
                            } else {
                                showToast("Please Check Your Network Connection For Downloading Masters Fully Download");
                            }

                            if (isOnline()) {
                                Async_getViolationPoint_SystemMasterData violation_points_task = new Async_getViolationPoint_SystemMasterData();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    violation_points_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    violation_points_task.execute();
                                }
                            } else {
                                showToast("Please Check Your Network Connection For Downloading Masters Fully Download");
                            }

                            if (isOnline()) {

                                Async_GetTerminalDetails terminal_details_task = new Async_GetTerminalDetails();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    terminal_details_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    terminal_details_task.execute();
                                }
                            } else {
                                showToast("Please Check Your Network Connection For Downloading Masters Fully Download");
                            }

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    showToast("Successfully Downloaded Master's !");
                                }
                            }, 1000);

                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        removeDialog(ALERT_USER);

                    }
                });

                AlertDialog alert_Dialog = builder.create();
                alert_Dialog.show();

                alert_Dialog.getWindow().getAttributes();

                TextView text_View = (TextView) alert_Dialog.findViewById(android.R.id.message);
                text_View.setTextSize(28);
                text_View.setTypeface(title2.getTypeface(), Typeface.BOLD);
                text_View.setGravity(Gravity.CENTER);

                Button btn1 = alert_Dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn1.setTextSize(22);
                btn1.setTextColor(Color.WHITE);
                btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
                btn1.setBackgroundColor(Color.RED);

                Button btn3 = alert_Dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btn3.setTextSize(22);
                btn3.setTextColor(Color.WHITE);
                btn3.setTypeface(btn3.getTypeface(), Typeface.BOLD);
                btn3.setBackgroundColor(Color.RED);
                return alert_Dialog;

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

    public class Async_getViolationPoint_SystemMasterData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getViolationPoint_SystemMasterData();
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings({"static-access"})
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            //  new Async_GetTerminalDetails().execute();
            if (ServiceHelper.violation_points_masters != null && ServiceHelper.violation_points_masters.length > 0) {

                vio_points_name_code_arr = new String[ServiceHelper.violation_points_masters.length][2];
                for (int i = 1; i < ServiceHelper.violation_points_masters.length; i++) {
                    vio_points_name_code_arr[i] = ServiceHelper.violation_points_masters[i].split("\\|");
                }

                try {
                    db.open();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.violationPointsTable);
                db2.execSQL(DBHelper.violatioPointsDetaisCreation);

                vio_points_fr_names_arr = new ArrayList<String>();
                vio_points_names_arr = new ArrayList<String>();
                vio_points_fr_names_arr.clear();
                vio_points_names_arr.clear();

                for (int j = 1; j < vio_points_name_code_arr.length; j++) {
                    vio_points_fr_names_arr.add(vio_points_name_code_arr[j][0]);
                    vio_points_names_arr.add(vio_points_name_code_arr[j][1]);
                    db.insertViolationPointDetails("" + vio_points_name_code_arr[j][0],
                            "" + vio_points_name_code_arr[j][1], "" + vio_points_name_code_arr[j][2]);

                }
                db.close();
                db2.close();
            }
        }
    }

    public class Async_wheler_details extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getWheeler();
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (ServiceHelper.Opdata_Chalana != null && ServiceHelper.Opdata_Chalana != "0") {

                if (ServiceHelper.whlr_details_master != null && ServiceHelper.whlr_details_master.length > 0) {

                    whlr_code_name = new String[ServiceHelper.whlr_details_master.length][2];
                    whlr_name_arr = new ArrayList<String>();
                    whlr_name_arr.clear();

                    try {
                        db.open();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                    db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.wheelercode_table);
                    db2.execSQL(DBHelper.wheelerCodeCreation);

                    for (int i = 1; i < ServiceHelper.whlr_details_master.length; i++) {
                        whlr_code_name[i] = (ServiceHelper.whlr_details_master[i].split("@"));
                        whlr_name_arr.add(whlr_code_name[i][1]);
                        db.insertWheelerDetails("" + whlr_code_name[i][0], "" + whlr_code_name[i][1]);
                    }
                    db.close();
                    db2.close();
                } else {
                    showToast("Try again");
                  /*  whlr_code_name = new String[0][0];
                    whlr_name_arr.clear();*/
                }

            } else {
                showToast("Try again");
              /*  whlr_code_name = new String[0][0];
                whlr_name_arr.clear();*/
            }
        }
    }

    public class Async_getPsDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            if (null != MainActivity.arr_logindetails[0]) {
                String unitCode = MainActivity.arr_logindetails[0].substring(0, 2);
                ServiceHelper.getPsNames(unitCode);
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (ServiceHelper.Opdata_Chalana != null) {
                if (ServiceHelper.psNames_master.length > 0) {
                    psname_name_code_arr = new String[ServiceHelper.psNames_master.length][2];
                    for (int i = 1; i < ServiceHelper.psNames_master.length; i++) {
                        psname_name_code_arr[i] = ServiceHelper.psNames_master[i].split("@");
                    }
                    try {
                        db.open();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                    db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.psName_table);
                    db2.execSQL(DBHelper.psNamesCreation);

                    ps_codes_fr_names_arr = new ArrayList<String>();
                    ps_names_arr = new ArrayList<String>();
                    ps_codes_fr_names_arr.clear();
                    ps_names_arr.clear();

                    for (int j = 1; j < psname_name_code_arr.length; j++) {
                        ps_codes_fr_names_arr.add(psname_name_code_arr[j][0]);
                        ps_names_arr.add(psname_name_code_arr[j][1]);
                        db.insertPsNameDetails("" + psname_name_code_arr[j][0], "" + psname_name_code_arr[j][1]);
                    }
                    db.close();
                    db2.close();
                }
            } else {
                showToast("Try Again");
            }
        }
    }

    @SuppressWarnings("unused")
    private class AsyncTask_Occupation extends AsyncTask<Void, Void, String> {

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            ServiceHelper.getOccupationdetails();
            return null;
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (ServiceHelper.Opdata_Chalana != null) {
                if (ServiceHelper.occupationlist_master.length > 0) {
                    occupation_code_arr = new String[ServiceHelper.occupationlist_master.length][2];
                    for (int i = 1; i < ServiceHelper.occupationlist_master.length; i++) {
                        occupation_code_arr[i] = ServiceHelper.occupationlist_master[i].split("@");
                    }

                    try {
                        db.open();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                    db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.Occupation_table);
                    db2.execSQL(DBHelper.occupationCreation);

                    occupation_fr_names_arr = new ArrayList<String>();
                    occupation_names_arr = new ArrayList<String>();
                    occupation_fr_names_arr.clear();
                    occupation_names_arr.clear();

                    for (int j = 1; j < occupation_code_arr.length; j++) {
                        occupation_fr_names_arr.add(occupation_code_arr[j][0]);
                        occupation_names_arr.add(occupation_code_arr[j][1]);
                        db.insertOccupation("" + occupation_code_arr[j][0], "" + occupation_code_arr[j][1]);

                    }
                    db.close();
                    db2.close();
                }
            } else {
                showToast("Try Again");
            }
        }
    }

    public void getWheelerCodeFromDB() {
        // TODO Auto-generated method stub

        try {
            db.open();
            c_whlr = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);
            if (c_whlr.getCount() == 0) {
            } else {
                wheeler_code_arr_spot = new String[c_whlr.getCount()];
                int count = 0;
                while (c_whlr.moveToNext()) {
                    wheeler_code_arr_spot[count] = c_whlr.getString(1);
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

    public class Async_getViolationDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getAllWheelerViolations();
            return null;
        }

        @SuppressWarnings("deprecation")
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
        }
    }

    public class Async_ocuptn extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getOccupation_Details();
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (ServiceHelper.Opdata_Chalana != null && ServiceHelper.Opdata_Chalana != "0") {

                if (ServiceHelper.occupation_master != null && ServiceHelper.occupation_master.length > 0) {

                    try {
                        db.open();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    ocuptn_code_name = new String[ServiceHelper.occupation_master.length][2];
                    ocuptn_name_arr_list = new ArrayList<String>();
                    ocuptn_name_arr_list.clear();

                    for (int i = 1; i < ServiceHelper.occupation_master.length; i++) {
                        ocuptn_code_name[i] = (ServiceHelper.occupation_master[i].split("@"));
                        ocuptn_name_arr_list.add(ocuptn_code_name[i][1]);
                        db.insertOccupation("" + ocuptn_code_name[i][0], "" + ocuptn_code_name[i][1]);
                    }
                    db.close();
                }
            } else {
                showToast("Please Check Your Network Connection For Downloading Masters Fully Download");
            }
        }
    }

    public class Async_BarDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getBar_Details();
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (ServiceHelper.Opdata_Chalana != null && ServiceHelper.Opdata_Chalana != "0") {

                if (ServiceHelper.bar_master != null && ServiceHelper.bar_master.length > 0) {

                    try {
                        db.open();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                    db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.bar_table);
                    db2.execSQL(DBHelper.barTableCreation);

                    bar_code_name = new String[ServiceHelper.bar_master.length][2];
                    bar_name_arr_list = new ArrayList<String>();

                    for (int i = 1; i < ServiceHelper.bar_master.length; i++) {

                        bar_code_name[i] = (ServiceHelper.bar_master[i].split("@"));
                        bar_name_arr_list.add(bar_code_name[i][1]);
                        db.insertBarDetails("" + bar_code_name[i][0], "" + bar_code_name[i][1]);
                    }
                    db.close();
                    db2.close();
                }
            } else {
                showToast("Please Check Your Network Connection and Try Again");
            }
        }
    }

    public class Async_GetTerminalDetails extends AsyncTask<Void, Void, String> {

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getTerminalID("" + UNIT_CODE);
            return null;
        }

        @SuppressWarnings({"deprecation", "unused"})
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if (ServiceHelper.terminaldetails_resp != null && ServiceHelper.terminaldetails_resp != "0") {
                if (ServiceHelper.terminaldetails_resp != null || ServiceHelper.terminaldetails_resp.length() > 0) {
                    String strJson = ServiceHelper.terminaldetails_resp;
                    SQLiteDatabase db2 = null;

                    try {
                        db.open();
                        DBHelper helper = new DBHelper(getApplicationContext());

                        db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                        db2.execSQL(DBHelper.termailDetailsCreation);
                        db2.execSQL("delete from " + DBHelper.TERMINAL_DETAILS_TABLE);

                        db.execSQL(DBHelper.CREATE_BT_PRINTER);
                        db.execSQL("delete from " + DBHelper.BT_PRINTER_TABLE);

                        JSONObject jsonRootObject = new JSONObject(strJson);// array
                        JSONArray jsonArray = jsonRootObject.optJSONArray("TERMINAL_DETAILS");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            terminalID = jsonObject.optString("TERMINAL_ID").toString();
                            bluetoothName = jsonObject.optString("BLUETOOTH_NAME").toString();
                            bluetoothAddress = jsonObject.optString("BLUETOOTH_ADDRESS").toString();
                            ContentValues values2 = new ContentValues();
                            values2.put("TERMINAL_ID", "" + terminalID);
                            values2.put("BT_NAME", "" + bluetoothName);
                            values2.put("BT_ADDRESS", "" + bluetoothAddress);
                            db2.insert(DBHelper.TERMINAL_DETAILS_TABLE.trim(), null, values2);
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();

                    } finally {
                        if (db2 != null) {
                            db2.close();
                        }
                    }
                }
            } else {
                showToast("Downloaded Masters Successfully");
            }
        }
    }

    /* TO GET QUALIFICATION DETAILS */
    public class Async_qlfctn extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getQualifications_Details();
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings({"deprecation", "static-access"})
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            if (ServiceHelper.Opdata_Chalana != null) {

                if (ServiceHelper.qualification_master.length > 0) {
                    try {
                        db.open();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    qlfctn_code_name = new String[ServiceHelper.qualification_master.length][2];
                    qlfctn_name_arr_list = new ArrayList<String>();

                    for (int i = 1; i < ServiceHelper.qualification_master.length; i++) {

                        qlfctn_code_name[i] = (ServiceHelper.qualification_master[i].split("@"));
                        qlfctn_name_arr_list.add(qlfctn_code_name[i][1]);
                        db.insertQualificationDetails("" + qlfctn_code_name[i][0], "" + qlfctn_code_name[i][1]);
                    }
                    db.close();
                }
            } else {
                showToast("Try Again");
            }
        }
    }

    public class Async_vchle_Cat extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getVechileCategory("" + ServiceHelper.VECHILE_CATEGORY);
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings({"deprecation", "static-access"})
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            if (ServiceHelper.Opdata_Chalana != null) {
                if (ServiceHelper.vchle_cat_master.length > 0) {

                    vchleCat_code_name = new String[0][0];
                    vchleCat_code_name = new String[ServiceHelper.vchle_cat_master.length][2];
                    vchleCat_name_arr_list = new ArrayList<String>();// FOR
                    vchleCat_name_arr_list.clear();
                    try {
                        db.open();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    for (int i = 1; i < ServiceHelper.vchle_cat_master.length; i++) {
                        vchleCat_code_name[i] = (ServiceHelper.vchle_cat_master[i].split("@"));
                        vchleCat_name_arr_list.add(vchleCat_code_name[i][1]);
                        db.insertVehicleCatDetails("" + vchleCat_code_name[i][0], "" + vchleCat_code_name[i][1]);
                    }
                    db.close();
                }
            } else {
                showToast("Try again");
                vchleCat_code_name = new String[0][0];
                vchleCat_name_arr_list.clear();
            }
        }
    }

    public class Async_vchle_mainCat extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getVechileCategory("" + ServiceHelper.VECHILE_MAIN_CAT_METHOD_NAME);
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings({"deprecation", "static-access"})
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            if (ServiceHelper.Opdata_Chalana != null) {
                if (ServiceHelper.vchle_mainCat_master.length > 0) {

                    vchle_MainCat_code_name = new String[0][0];
                    vchle_MainCat_code_name = new String[ServiceHelper.vchle_mainCat_master.length][2];
                    vchle_MainCat_name_arr_list = new ArrayList<String>();// FOR
                    vchle_MainCat_name_arr_list.clear();

                    try {
                        db.open();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    for (int i = 1; i < ServiceHelper.vchle_mainCat_master.length; i++) {
                        vchle_MainCat_code_name[i] = (ServiceHelper.vchle_mainCat_master[i].split("@"));
                        vchle_MainCat_name_arr_list.add(vchle_MainCat_code_name[i][1]);
                        db.insertVehicleMainCatDetails("" + vchle_MainCat_code_name[i][0],
                                "" + vchle_MainCat_code_name[i][1]);
                    }
                    db.close();
                }
            } else {
                showToast("Try again");
                vchle_MainCat_code_name = new String[0][0];
                vchle_MainCat_name_arr_list.clear();
            }
        }
    }

    /* DATE DETAILS START */
    @SuppressWarnings("deprecation")
    @SuppressLint("SimpleDateFormat")
    protected void getPresentDate() {
        // TODO Auto-generated method stub

        calendar = Calendar.getInstance();

        present_date = calendar.get(Calendar.DAY_OF_MONTH);
        present_month = calendar.get(Calendar.MONTH);
        present_year = calendar.get(Calendar.YEAR);

        date_format = new SimpleDateFormat("dd-MMM-yyyy");
        present_date_toSend = date_format.format(new Date(present_year - 1900, present_month, present_date));

    }

    @SuppressWarnings("deprecation")
    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        preferences = getSharedPreferences("preference", Context.MODE_PRIVATE);
        editor = preferences.edit();
        psname_settings = preferences.getString("psname_name", "psname");
        pointnameBycode_settings = preferences.getString("point_name", "pointname");
        psResName_settings = preferences.getString("ps_res_name_name", "location");

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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this, AlertDialog.THEME_HOLO_LIGHT);
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

        Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btn.setTextSize(22);
        btn.setTextColor(Color.WHITE);
        btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
        btn.setBackgroundColor(Color.RED);

        Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        btn2.setTextSize(22);
        btn2.setTextColor(Color.WHITE);
        btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
        btn2.setBackgroundColor(Color.RED);
    }


    public class async_getIDProofsExceptionLimit extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;

            if (MainActivity.arr_logindetails != null && MainActivity.arr_logindetails.length > 0) {
                result = ServiceHelper.exceptionLimit(MainActivity.arr_logindetails[0], new DateUtil().getTodaysDate());

            }

            return result;
        }


        @Override
        protected void onPostExecute(String officerslimit) {
            super.onPostExecute(officerslimit);
            removeDialog(PROGRESS_DIALOG);

            if (officerslimit != null && officerslimit != "0") {

                // String strJson = ServiceHelper.officerslimit;
                try {
                    JSONObject jsonRootObject = new JSONObject(officerslimit);

                    JSONArray jsonArray = jsonRootObject.optJSONArray("IDProof_Exemption");//array object

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        CHALLAN_TYPE = jsonObject.optString("CHALLAN_TYPE").toString() != null ? jsonObject.optString("CHALLAN_TYPE").toString() : "0";
                        CASES_LIMIT = jsonObject.optString("CASES_LIMIT").toString() != null ? jsonObject.optString("CASES_LIMIT").toString() : "0";
                        CASES_BOOKED = jsonObject.optString("CASES_BOOKED").toString() != null ? jsonObject.optString("CASES_BOOKED").toString() : "0";

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    CHALLAN_TYPE = "0";
                    CASES_LIMIT = "0";
                    CASES_BOOKED = "0";
                }


                startActivity(new Intent(Dashboard.this, Drunk_Drive.class));


            }
        }


    }


    public void ddCommonMethod() {
        getPreferenceValues();
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = preferences.edit();
        address_spot = preferences.getString("btaddress", "btaddr");

        try {


            db.open();
            cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

            c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

            if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                showToast("Please download master's !");
            } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                showToast("Configure Settings!");
            }
            /*else if(Settings_New.btn_ResponsiblePSName.getText().toString().equals("Responsible PS Name")){
                showToast("Configure Settings!");
            }*/
            else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                showToast("Configure BlueTooth Settings!");
            } else {
                if (isOnline()) {
                    startActivity(new Intent(Dashboard.this, Drunk_Drive.class));
                    //   new async_getIDProofsExceptionLimit().execute();

                } else {
                    showToast("" + netwrk_info_txt);
                }
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            c_whlr_details.close();
            cursor_psnames.close();
            db.close();
        }
        c_whlr_details.close();
        cursor_psnames.close();
        db.close();
    }
}