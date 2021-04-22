package com.mtpv.mobilee_ticket;

import android.Manifest;
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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
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
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.covid.ChallansAdapter;
import com.mtpv.covid.ChallansModel;
import com.mtpv.covid.IDPrfAdapter;
import com.mtpv.covid.IDProofs;
import com.mtpv.covid.VltnAdapter;
import com.mtpv.covid.VltnModel;
import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.DateUtil;
import com.mtpv.mobilee_ticket_services.ServiceHelper;
import com.mtpv.mobilee_ticket_services.VibratorUtils;
import com.mtpv.spinnermdl.VltnListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CovidChallanActivity extends Activity implements LocationListener {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String bookedPScode_send_from_settings, booked_RES_PScode_send_from_settings, bookedPSname_send_from_settings, point_code_send_from_settings, point_name_send_from_settings,
            exact_location_send_from_settings, present_date_toSend = "", emailId_to_send = "", pancard_to_send = "", passport_to_send = "",
            VoterId_to_send = "", is_it_spot_send = "0", licStatus_send = "";
    public static String pidCode = null, pidName = null, psCd = null, psName = null, cadre_code = null,
            cadre_name = null, pass_word = null, off_phone_no = null, current_version = null, rta_data_flg = null,
            dl_data_flg = null, aadhaar_data_flg = null, otp_no_flg = null, cashless_flg = null, mobileNo_flg = null;
    ImageView img_logo;
    TextView officer_Name, officer_Cadre, officer_PS, tv_pendingchallans, tv_pendingamount, tv_violtaionamnt, tv_grand_totalamnt, textView4;
    EditText et_Value, edtTxt_Name, edtTxt_FName, et_Address, et_City, edt_Age, et_driver_contact_spot;
    Button btn_SlctId_CV, btn_Get, btn_Otp, btn_OtpCall, btn_violation, btn_Submit, btn_Dob, btn_cancel;
    ImageButton ibtn_camera;
    ImageButton ibtn_gallery;
    public static ImageView offender_image;
    LinearLayout lyt_GetDtls;
    public static final int PROGRESS_DIALOG = 0, OTP_CNFRMTN_DIALOG = 7;
    LocationManager m_locationlistner;
    android.location.Location location;
    public static double latitude = 0.0, longitude = 0.0, total_amount = 0, grand_total = 0;
    boolean isNetworkEnabled = false, canGetLocation = false, isGPSEnabled = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10, MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    String str_IdCode = "", str_OffCode = "", str_Fine = "", final_image_data_tosend = "", dtnd_Cd = "00", dtnd_Name = "None", gender = "M";
    byte[] byteArray;
    RadioGroup rGP_Dtnd, radioGroup_gender;
    RadioButton rBtn_RC, rBtn_VEH, rBtn_LIC, rBtn_None, rBtn_Male, rBtn_FeMale, rBtn_Others;
    JSONObject jsonObject = null;
    String otpValue = "", imei_send = "", simid_send = "", dob_DL = "";

    boolean otp_VefySts = false, img_Sts = false;
    TelephonyManager telephonyManager;
    LinearLayout lyt_DOB, ll_pendingchallans;

    private int mYear, mMonth, mDay;

    int pendingChlns = 0, pndgAmnt = 0, cv_OTP_Time = 0;
    AlertDialog builder;
    CountDownTimer countDownTimer;
    TextView txt_ElpsdTime;

    public ArrayList<ChallansModel> challansModelList;
    public boolean smsMsgCall = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.covid_challan);
        otp_VefySts = false;
        initview();
        getPreferencesData();
        img_logo = findViewById(R.id.img_logo);
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
        tv_pendingchallans = findViewById(R.id.tv_pendingchallans);
        tv_pendingamount = findViewById(R.id.tv_pendingamount);
        tv_violtaionamnt = findViewById(R.id.tv_violtaionamnt);
        tv_grand_totalamnt = findViewById(R.id.tv_grand_totalamnt);
        textView4 = findViewById(R.id.textView4);
        officer_Name.setText(MainActivity.pidName + "(" + MainActivity.cadre_name + ")");
        officer_Cadre.setText(MainActivity.cadre_name);
        officer_PS.setText(MainActivity.psName);
        lyt_GetDtls.setVisibility(View.GONE);
        getLocation();

        btn_SlctId_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async_GetIdProofMaster().execute();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CovidChallanActivity.this, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        et_Value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lyt_GetDtls.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ibtn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImagefrom_Gallery();
            }
        });

        btn_Get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_SlctId_CV.getText().toString().equalsIgnoreCase("SELECT ID")) {
                    showToast("Please Select IdProof ");
                } else if (et_Value.getText().toString().isEmpty()) {
                    showToast("Please enter the IDProof Details ");
                } else if ("12".equals(str_IdCode) && ((et_Value.getText().toString().trim() != null && et_Value.getText().toString().trim().length() > 1
                        && et_Value.getText().toString().trim().length() != 10) ||
                        new DateUtil().allCharactersSame(et_Value.getText().toString().trim()))) {
                    et_Value
                            .setError(Html.fromHtml("<font color='black'>Enter Valid mobile number!!</font>"));
                    et_Value.requestFocus();

                } else if ("12".equals(str_IdCode) && et_Value.getText().toString().length() == 10) {
                    if ((et_Value.getText().toString().charAt(0) == '7') || (et_Value.getText().toString().charAt(0) == '8')
                            || (et_Value.getText().toString().charAt(0) == '9') || (et_Value.getText().toString().charAt(0) == '6')) {
                        if (isOnline()) {
                            new Async_GetIdProofDetails().execute();
                        } else {
                            showToast("Please check your network connection!");
                        }
                    } else {
                        et_Value
                                .setError(Html.fromHtml("<font color='black'>Check Contact No.!!</font>"));
                        et_Value.requestFocus();
                    }

                } else {
                    new Async_GetIdProofDetails().execute();
                }
            }
        });

        ll_pendingchallans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (challansModelList.size() > 0) {
                    showVilationsDetailsDlg("Pending Challans", challansModelList);
                } else {
                    showToast("No Pending Challans ");
                }
            }
        });

        btn_Otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempContactNumber = et_driver_contact_spot.getText().toString();
                smsMsgCall = true;

                if (tempContactNumber.equals("")) {
                    et_driver_contact_spot.setError(
                            Html.fromHtml("<font color='black'>Enter mobile number to send OTP!!</font>"));
                    et_driver_contact_spot.requestFocus();

                } else if ((tempContactNumber.trim() != null && tempContactNumber.trim().length() > 1
                        && tempContactNumber.trim().length() != 10) || new DateUtil().allCharactersSame(tempContactNumber.trim())) {
                    et_driver_contact_spot
                            .setError(Html.fromHtml("<font color='black'>Enter Valid mobile number!!</font>"));
                    et_driver_contact_spot.requestFocus();

                } else if (tempContactNumber.length() == 10) {
                    if ((tempContactNumber.charAt(0) == '7') || (tempContactNumber.charAt(0) == '8')
                            || (tempContactNumber.charAt(0) == '9') || (tempContactNumber.charAt(0) == '6')) {
                        if (isOnline()) {
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

        btn_OtpCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempContactNumber = et_driver_contact_spot.getText().toString();
                smsMsgCall = false;

                if (tempContactNumber.equals("")) {
                    et_driver_contact_spot.setError(
                            Html.fromHtml("<font color='black'>Enter mobile number to send OTP!!</font>"));
                    et_driver_contact_spot.requestFocus();

                } else if ((tempContactNumber.trim() != null && tempContactNumber.trim().length() > 1
                        && tempContactNumber.trim().length() != 10) || new DateUtil().allCharactersSame(tempContactNumber.trim())) {
                    et_driver_contact_spot
                            .setError(Html.fromHtml("<font color='black'>Enter Valid mobile number!!</font>"));
                    et_driver_contact_spot.requestFocus();

                } else if (tempContactNumber.length() == 10) {
                    if ((tempContactNumber.charAt(0) == '7') || (tempContactNumber.charAt(0) == '8')
                            || (tempContactNumber.charAt(0) == '9') || (tempContactNumber.charAt(0) == '6')) {
                        if (isOnline()) {
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


        btn_violation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async_GetViolationDetails().execute();
            }
        });
        ibtn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImagefrom_Camera();
            }
        });

        rGP_Dtnd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                dtnd_Name = "";
                dtnd_Cd = "";
                switch (checkedId) {
                    case R.id.rBtn_RC:
                        dtnd_Cd = "";
                        dtnd_Cd = "02";
                        dtnd_Name = "RC";
                        break;
                    case R.id.rBtn_VEH:
                        dtnd_Cd = "";
                        dtnd_Cd = "03";
                        dtnd_Name = "VEHICLE";
                        break;
                    case R.id.rBtn_LIC:
                        dtnd_Cd = "";
                        dtnd_Cd = "01";
                        dtnd_Name = "LICENSE";
                        break;

                    case R.id.rBtn_None:
                        dtnd_Cd = "";
                        dtnd_Cd = "00";
                        dtnd_Name = "None";
                        break;
                    default:
                        dtnd_Cd = "";
                        dtnd_Cd = "00";
                        dtnd_Name = "None";
                        break;

                }
            }
        });

        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gender = "";
                switch (checkedId) {

                    case R.id.rBtn_Male:
                        gender = "M";
                        break;
                    case R.id.rBtn_FeMale:
                        gender = "F";
                        break;

                    case R.id.rBtn_Others:
                        gender = "O";
                        break;
                    default:
                        gender = "O";
                        break;
                }
            }
        });


        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtTxt_Name.getText().toString().isEmpty()) {
                    edtTxt_Name.setError(Html.fromHtml("<font color='black'>Please enter Name </font>"));
                    edtTxt_Name.requestFocus();
                } /*else if (edtTxt_FName.getText().toString().isEmpty()) {
                    edtTxt_FName.setError(Html.fromHtml("<font color='black'>Please enter Father Name </font>"));
                    edtTxt_FName.requestFocus();
                }*/ else if (et_Address.getText().toString().isEmpty()) {
                    et_Address.setError(Html.fromHtml("<font color='black'>Please enter the Address </font>"));
                    et_Address.requestFocus();
                } else if (et_City.getText().toString().isEmpty()) {
                    et_City.setError(Html.fromHtml("<font color='black'>Please enter the City </font>"));
                    et_City.requestFocus();
                } else if (edt_Age.getText().toString().isEmpty()) {
                    edt_Age.setError(Html.fromHtml("<font color='black'>Please enter the Age </font>"));
                    edt_Age.requestFocus();
                } else if (et_driver_contact_spot.getText().toString().isEmpty()) {
                    et_driver_contact_spot.setError(Html.fromHtml("<font color='black'>Please enter Contact No </font>"));
                    et_driver_contact_spot.requestFocus();
                } else if (!rBtn_Male.isChecked() && !rBtn_FeMale.isChecked() && !rBtn_Others.isChecked()) {
                    showToast("Please select the Gender ");
                } else if (btn_violation.getText().toString().equalsIgnoreCase("Select Violation")) {
                    showToast("Please select the violations");
                } else if (!otp_VefySts) {
                    showToast("Please verify OTP !");
                } else if (!img_Sts) {
                    showToast("Please Capture the Image ");
                } else {
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("CHALLAN_TYPE", "FM");
                        jsonObject.put("OFFENCE_DT", "" + new DateUtil().getPresentDateTime());
                        jsonObject.put("GPS_LATTI", "" + latitude);
                        jsonObject.put("GPS_LONG", "" + longitude);
                        jsonObject.put("GPS_LOCATION", "" + getAddressFromLatLng(latitude, longitude));
                        jsonObject.put("PS_CODE", "" + psCd);
                        jsonObject.put("BOOKED_PSCODE", "" + bookedPScode_send_from_settings);
                        jsonObject.put("PS_NAME", "" + psName);
                        jsonObject.put("BOOKED_PSNAME", "" + bookedPSname_send_from_settings);
                        jsonObject.put("POINT_CODE", "" + point_code_send_from_settings);
                        jsonObject.put("POINT_NAME", "" + point_name_send_from_settings);
                        jsonObject.put("OPERATOR_CD", "" + pidCode);
                        jsonObject.put("OPERATOR_NAME", "" + pidName);
                        jsonObject.put("CADRE", "" + cadre_name);
                        jsonObject.put("CADRE_CD", "" + cadre_code);
                        jsonObject.put("V_NAME", "" + edtTxt_Name.getText().toString().trim());
                        jsonObject.put("FName", "" + edtTxt_FName.getText().toString().trim());
                        jsonObject.put("GENDER", "" + gender);
                        jsonObject.put("CONTACT_NO", "" + et_driver_contact_spot.getText().toString().trim());
                        jsonObject.put("ID_PROOF_CD", "" + str_IdCode);
                        jsonObject.put("ID_PROOF_DATA", "" + et_Value.getText().toString().trim());
                        jsonObject.put("AGE", "" + edt_Age.getText().toString().trim());
                        jsonObject.put("ADDRESS", "" + et_Address.getText().toString().trim());
                        jsonObject.put("OFFENCE_CD", "" + str_OffCode);
                        jsonObject.put("IMEI_NO", "" + imei_send);
                        jsonObject.put("MC_SIM_ID", "" + simid_send);
                        jsonObject.put("CITY", "" + et_City.getText().toString().trim());
                        jsonObject.put(" DETAINED_DT", "" + new DateUtil().getPresentDateTime());
                        jsonObject.put("CMD_AMT", "" + str_Fine);
                        Log.d("JSONOBJ", "" + jsonObject.toString());
                        new Async_GgenerateCovidChallan().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btn_Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CovidChallanActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressWarnings("deprecation")
                            @SuppressLint({"SimpleDateFormat", "DefaultLocale"})

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

                                int age = (int) (days / 365);

                                edt_Age.setText("" + age);

                                //Minimum Age should be 16
                                if (days > 5824) {
                                    btn_Dob.setText(dob_DL);

                                } else {
                                    showToast("Please select Date of Birth Atleast Person Should be Age Greater Than 16");
                                }
                                //  dob_DL = date_format.format(dayOfMonth  + (----------------OfYear + 1) + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });


    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

    public void countTimer(final int time) {

        countDownTimer = new CountDownTimer(time, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                ;
                txt_ElpsdTime.setText("Elapsed Time 00:" + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + "");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                txt_ElpsdTime.setText("Time's Up!");
                builder.dismiss();
                otp_VefySts = false;
                showToast(" Please Resend Otp !");
            }
        }.start();
    }

    private void initview() {
        rGP_Dtnd = findViewById(R.id.rGP_Dtnd);
        rBtn_RC = findViewById(R.id.rBtn_RC);
        rBtn_VEH = findViewById(R.id.rBtn_VEH);
        rBtn_LIC = findViewById(R.id.rBtn_LIC);
        rBtn_None = findViewById(R.id.rBtn_None);
        rBtn_Male = findViewById(R.id.rBtn_Male);
        rBtn_FeMale = findViewById(R.id.rBtn_FeMale);
        rBtn_Others = findViewById(R.id.rBtn_Others);
        radioGroup_gender = findViewById(R.id.radioGroup_gender);
        lyt_GetDtls = findViewById(R.id.lyt_GetDtls);
        btn_SlctId_CV = findViewById(R.id.btn_SlctId_CV);
        btn_Get = findViewById(R.id.btn_Get);
        btn_violation = findViewById(R.id.btn_violation);
        btn_Submit = findViewById(R.id.btn_Submit);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_Dob = findViewById(R.id.btn_Dob);
        btn_Otp = findViewById(R.id.btn_Otp);
        btn_OtpCall = findViewById(R.id.btn_OtpCall);
        et_Value = findViewById(R.id.et_Value);
        edtTxt_Name = findViewById(R.id.edtTxt_Name);
        edtTxt_FName = findViewById(R.id.edtTxt_FName);
        et_Address = findViewById(R.id.et_Address);
        et_City = findViewById(R.id.et_City);
        edt_Age = findViewById(R.id.edt_Age);
        et_driver_contact_spot = findViewById(R.id.et_CntctNo);
        ibtn_camera = (ImageButton) findViewById(R.id.imgv_camera_capture_spotchallan_xml);
        ibtn_gallery = (ImageButton) findViewById(R.id.imgv_gallery_spotchallan_xml);
        offender_image = (ImageView) findViewById(R.id.offender_image);
        offender_image.setVisibility(View.GONE);
        lyt_DOB = findViewById(R.id.lyt_DOB);
        ll_pendingchallans = findViewById(R.id.ll_pendingchallans);
        lyt_DOB.setVisibility(View.GONE);
    }

    public class Async_GgenerateCovidChallan extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.generateCovidChallan("" + jsonObject.toString(), "" + final_image_data_tosend);
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
            if (!ServiceHelper.strCV_Responce.equalsIgnoreCase("") &&
                    null != ServiceHelper.strCV_Responce) {
                try {
                    if (ServiceHelper.strCV_Responce.split("\\^")[0].equals("0")) {
                        Intent print = new Intent(getApplicationContext(), CovidPrintActivity.class);
                        startActivity(print);
                    } else if (ServiceHelper.strCV_Responce.split("\\^")[0].equals("4")) {
                        showToast("Challan Already Generated for today ! ");
                    } else {
                        showToast("Ticket generation Failed and Try again ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showToast("Please check the Network and Try again ");
            }
        }
    }

    public class Async_GetIdProofMaster extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getIDprrofMaster();
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
            if (!ServiceHelper.strCV_IdPrfMstr.equalsIgnoreCase("") &&
                    null != ServiceHelper.strCV_IdPrfMstr) {
                try {
                    JSONObject jsonObject = new JSONObject(ServiceHelper.strCV_IdPrfMstr);
                    JSONArray jsonArray = jsonObject.getJSONArray("IDproofMaster");
                    List<IDProofs> idProofsList = new ArrayList<>(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        IDProofs idProofs = new IDProofs();
                        idProofs.setIdCode("" + object.getString("IdCode"));
                        idProofs.setIdName("" + object.getString("IdName"));
                        idProofsList.add(idProofs);
                    }
                    showIDProofsSpinnerDialog("SELECT ID", idProofsList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showToast("Please check the Network and Try again ");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void showIDProofsSpinnerDialog(String title, final List<IDProofs> courseTypeList) {

        final AlertDialog builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.spinner_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        ListView spinner_List = view.findViewById(R.id.recycle_List);
        AppCompatTextView title_Spinner = view.findViewById(R.id.title_Spinner);
        title_Spinner.setText("" + title);
        IDPrfAdapter jobsAdapter = new IDPrfAdapter(CovidChallanActivity.this, courseTypeList);
        spinner_List.setAdapter(jobsAdapter);
        builder.show();
        spinner_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s_Item = courseTypeList.get(i).getIdName();
                str_IdCode = courseTypeList.get(i).getIdCode();
                if ("2".equals(str_IdCode)) {
                    lyt_DOB.setVisibility(View.VISIBLE);
                    et_Value.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    et_Value.setText("");
                } else {
                    lyt_DOB.setVisibility(View.GONE);
                }

                if ("0".equals(str_IdCode) || "12".equals(str_IdCode)) {
                    textView4.setText("Enter Mobile NO :");
                    et_Value.setInputType(InputType.TYPE_CLASS_NUMBER);
                    str_IdCode = "12";
                    et_Value.setText("");
                } else {
                    textView4.setText("Enter Id Value:");
                    et_Value.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    et_Value.setText("");
                }
                btn_SlctId_CV.setText("" + s_Item);
                builder.dismiss();
            }
        });
    }

    public class Async_GetIdProofDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getIDproofDetails("" + str_IdCode, "" + et_Value.getText().toString().trim(), "" + dob_DL, "" + pidCode);

            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            lyt_GetDtls.setVisibility(View.VISIBLE);
            if (!ServiceHelper.strCV_IdPrfData.equalsIgnoreCase("") &&
                    null != ServiceHelper.strCV_IdPrfData) {
                try {
                    JSONObject jsonObject = new JSONObject(ServiceHelper.strCV_IdPrfData);
                    edtTxt_Name.setText("" + jsonObject.getString("Name"));
                    edtTxt_FName.setText("" + jsonObject.getString("FName"));
                    et_Address.setText("" + jsonObject.getString("Address"));
                    et_City.setText("" + jsonObject.getString("City"));
                    et_driver_contact_spot.setText("" + jsonObject.getString("MobileNo"));
                    et_driver_contact_spot.setEnabled(false);
                    tv_pendingchallans.setText("" + jsonObject.getString("PendingChallans"));
                    tv_pendingamount.setText("" + jsonObject.getString("PendingAmount"));
                    cv_OTP_Time = Integer.parseInt(jsonObject.getString("OtpTime"));
                    pndgAmnt = Integer.parseInt(jsonObject.getString("PendingAmount"));
                    tv_violtaionamnt.setText("0");
                    tv_grand_totalamnt.setText("0");
                    JSONObject object = jsonObject.getJSONObject("PreviousChallans");
                    JSONArray jsonArray = object.getJSONArray("Challans");
                    challansModelList = new ArrayList<>(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ChallansModel challansModel = new ChallansModel();
                        JSONObject js = jsonArray.getJSONObject(i);
                        challansModel.setChallanNo("" + js.getString("ChallanNo"));
                        challansModel.setDate("" + js.getString("Date"));
                        challansModel.setFINE("" + js.getString("FINE"));
                        challansModel.setLocation("" + js.getString("Location"));
                        challansModel.setPSName("" + js.getString("PSName"));
                        challansModelList.add(challansModel);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showToast("Please check the Network and Try again ");
            }
        }
    }

    public class Async_GetViolationDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getViolationMaster("H",
                    "FM", "" + pidCode, "NA");
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            if (!ServiceHelper.strCV_VltnsList.equalsIgnoreCase("") &&
                    null != ServiceHelper.strCV_VltnsList) {
                try {
                    JSONObject jsonObject = new JSONObject(ServiceHelper.strCV_VltnsList);
                    JSONArray jsonArray = jsonObject.getJSONArray("getViolationMaster");
                    List<VltnModel> vltnModelList = new ArrayList<>(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        VltnModel vltnModel = new VltnModel();
                        vltnModel.setOffenceCode("" + object.getString("OffenceCode"));
                        vltnModel.setSection("" + object.getString("Section"));
                        vltnModel.setViolation("" + object.getString("Violation"));
                        vltnModel.setFine("" + object.getString("Fine"));
                        vltnModelList.add(vltnModel);
                    }
                    showVilationsSpinnerDialog("Select Violation", vltnModelList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showToast("Please check the Network and Try again ");
            }
        }
    }

    public class Async_sendOTP_to_mobile extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String smsMode = "";
            if (smsMsgCall) {
                smsMode = "1";
            } else {
                smsMode = "2";
            }
            ServiceHelper.sendOTPtoMobile(et_driver_contact_spot.getText().toString().trim(), et_driver_contact_spot.getText().toString().trim(),
                    "" + new DateUtil().getTodaysDate(), smsMode, "FM");
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

            if (ServiceHelper.Opdata_Chalana.toLowerCase().equalsIgnoreCase("NA")) {
                showToast("Please check the Network And Try again ");
            } else {
                showToast("OTP is sent to your mobile number");
                otpValue = "" + ServiceHelper.Opdata_Chalana;
                showOTPDialog(otpValue);
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private void showVilationsSpinnerDialog(String title, final List<VltnModel> courseTypeList) {

        final AlertDialog builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.spinner_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        ListView spinner_List = view.findViewById(R.id.recycle_List);
        AppCompatTextView title_Spinner = view.findViewById(R.id.title_Spinner);
        title_Spinner.setText("" + title);
        VltnAdapter jobsAdapter = new VltnAdapter(CovidChallanActivity.this, courseTypeList);
        spinner_List.setAdapter(jobsAdapter);
        builder.show();
        spinner_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s_Item = courseTypeList.get(i).getViolation();
                str_OffCode = courseTypeList.get(i).getOffenceCode();
                str_Fine = courseTypeList.get(i).getFine();
                btn_violation.setText("" + s_Item);
                tv_violtaionamnt.setText("" + str_Fine);
                int tot_Amnt = pndgAmnt + Integer.parseInt(str_Fine);
                tv_grand_totalamnt.setText("" + tot_Amnt);
                builder.dismiss();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void showVilationsDetailsDlg(String title, final List<ChallansModel> challansModelList) {

        final AlertDialog builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.challan_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        ListView spinner_List = view.findViewById(R.id.recycle_List);
        AppCompatTextView title_Spinner = view.findViewById(R.id.title_Spinner);//btn_OK
        Button btn_OK = view.findViewById(R.id.btn_OK);
        title_Spinner.setText("" + title);
        ChallansAdapter jobsAdapter = new ChallansAdapter(CovidChallanActivity.this, challansModelList);
        spinner_List.setAdapter(jobsAdapter);
        builder.show();
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void showOTPDialog(final String otpValue) {

        builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.otp_verification, null);

        builder.setView(view);
        builder.setCancelable(false);
        TextView otp_heading = view.findViewById(R.id.otp_heading);
        txt_ElpsdTime = view.findViewById(R.id.txt_ElpsdTime);
        otp_heading.setText("Enter OTP For " + et_driver_contact_spot.getText().toString().trim());
        final EditText edtTxt_Otp = view.findViewById(R.id.et_OTP);

        Button btn_OK = view.findViewById(R.id.ok_dialog);
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtTxt_Otp.getText().toString().isEmpty()) {
                    showToast("Please enter the Otp ");
                }
                if (edtTxt_Otp.getText().toString().trim().equals(otpValue)) {
                    otp_VefySts = true;
                    showToast("OTP Verified Successfully");
                    builder.dismiss();
                    countDownTimer.cancel();
                } else {
                    showToast("Entered Wrong OTP");
                }


            }
        });

        Button btn_Cancel = view.findViewById(R.id.Cancel_dialog);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });
        builder.show();
        countTimer(cv_OTP_Time * 1000);

    }

    private void selectImagefrom_Gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    private void selectImagefrom_Camera() {

        if (Build.VERSION.SDK_INT <= 23) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(CovidChallanActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", f));
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 1);
        }
        //   imgSelected = "1";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            @SuppressWarnings("unused")
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
                    String current_date = SpotChallan.date;
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "E-Ticket"
                            + File.separator + current_date;
                    File camerapath = new File(path);

                    if (!camerapath.exists()) {
                        camerapath.mkdirs();
                    }
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {
                        outFile = new FileOutputStream(file);
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90f);
                        mutableBitmap = Bitmap.createBitmap(mutableBitmap, 0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight(), matrix, true);
                        Canvas canvas = new Canvas(mutableBitmap); // bmp is the

                        TextPaint paint = new TextPaint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(40);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setStyle(Paint.Style.FILL);
                        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
                        canvas.save();
                        canvas.drawText("Date " + new DateUtil().getPresentDateTime(), 50f, canvas.getHeight() - 150, paint);
                        canvas.drawText("" + getAddressFromLatLng(latitude, longitude), 50f, canvas.getHeight() - 70, paint);
                        canvas.restore();


                        offender_image.setVisibility(View.VISIBLE);
                        offender_image.setImageBitmap(mutableBitmap);
                        offender_image.setRotation(0);

                        mutableBitmap = Bitmap.createBitmap(mutableBitmap, 0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight(), matrix, true);
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outFile);

                        outFile.flush();
                        outFile.close();


                        //new SingleMediaScanner(this, file);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (bitmap != null) {
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                        Matrix matrix = new Matrix();
                        matrix.postRotate(90f);
                        mutableBitmap = Bitmap.createBitmap(mutableBitmap, 0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight(), matrix, true);
                        Canvas canvas = new Canvas(mutableBitmap); // bmp is the

                        TextPaint paint = new TextPaint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(40);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setStyle(Paint.Style.FILL);
                        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
                        canvas.save();
                        canvas.drawText("Date " + new DateUtil().getPresentDateTime(), 50f, canvas.getHeight() - 150, paint);
                        canvas.drawText("" + getAddressFromLatLng(latitude, longitude), 50f, canvas.getHeight() - 70, paint);
                        canvas.restore();

                        /* offender_image.setVisibility(View.VISIBLE);
                        offender_image.setImageBitmap(mutableBitmap);
                        offender_image.setRotation(0);*/

                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

                        byteArray = bytes.toByteArray();
                        final_image_data_tosend = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        offender_image.setVisibility(View.VISIBLE);
                        img_Sts = true;

                    } else if (bitmap == null) {
                        showToast("Image Cannot be Loaded !");
                        final_image_data_tosend = null;
                        img_Sts = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                if (thumbnail != null) {
                    Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mutableBitmap); // bmp is the
                    TextPaint paint = new TextPaint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(20);
                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
                    canvas.save();
                    canvas.drawText("Date " + new DateUtil().getPresentDateTime(), 50f, canvas.getHeight() - 150, paint);
                    canvas.drawText("" + getAddressFromLatLng(latitude, longitude), 50f, canvas.getHeight() - 70, paint);
                    canvas.restore();

                    offender_image.setImageBitmap(mutableBitmap);
                    offender_image.setRotation(offender_image.getRotation() + 360);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                    byteArray = bytes.toByteArray();
                    final_image_data_tosend = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    offender_image.setVisibility(View.VISIBLE);
                    img_Sts = true;
                } else if (thumbnail == null) {
                    showToast("Image Cannot be Loaded !");
                    img_Sts = false;
                }
            }
        }
    }

    private String getAddressFromLatLng(double lat, double lng) {
        String getAddress = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                Address address = addresses.get(0);
                StringBuilder addressBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressBuilder.append(address.getAddressLine(i)).append("\n");
                }
                getAddress = addressBuilder.toString();
            } else {
                getAddress = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            getAddress = "";
        }
        return getAddress;
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


    }

    @SuppressWarnings("unused")
    @SuppressLint({"ResourceAsColor", "CutPasteId", "SetTextI18n"})
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {

            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "", true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);
                return pd;


            case OTP_CNFRMTN_DIALOG:

                String otp_message = "\n" + "otp_msg" + "\n";
                TextView title3 = new TextView(this);
                title3.setText("ALERT");
                title3.setBackgroundColor(Color.RED);
                title3.setGravity(Gravity.CENTER);
                title3.setTextColor(Color.WHITE);
                title3.setTextSize(20);
                title3.setTypeface(title3.getTypeface(), Typeface.BOLD);
                title3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title3.setPadding(20, 0, 20, 0);
                title3.setHeight(85);

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

            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    private void showToast(String msg) {
        // TODO Auto-generated method stub
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();

        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(20);
        messageTextView.setPadding(5, 2, 2, 5);
        toastView.setBackgroundResource(R.drawable.toast_background);
        toast.show();
    }

    private void getPreferencesData() {
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
        booked_RES_PScode_send_from_settings = preferences.getString("ps_res_name_code", "0");
        //  exact_location_send_from_settings = preferences.getString("exact_location", "location");
        exact_location_send_from_settings = preferences.getString("ps_res_name_code", "0");

    }

    @Override
    public void onLocationChanged(Location location) {

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
}
