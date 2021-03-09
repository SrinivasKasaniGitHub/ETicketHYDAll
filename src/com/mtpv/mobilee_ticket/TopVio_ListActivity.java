package com.mtpv.mobilee_ticket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.covid.IDProofs;
import com.mtpv.mobilee_ticket_services.ServiceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TopVio_ListActivity extends AppCompatActivity {

    public static String pidCode = null, pidName = null, psCd = null, psName = null, cadre_code = null,
            cadre_name = null, pass_word = null, off_phone_no = null, current_version = null, rta_data_flg = null,
            dl_data_flg = null, aadhaar_data_flg = null, otp_no_flg = null, cashless_flg = null, mobileNo_flg = null;
    public static final int PROGRESS_DIALOG = 0;
    TextView officer_Name, officer_Cadre, officer_PS, textView4;
    Button btn_SlctFDate, btn_SlctTDate, btn_Get;
    private int mYear, mMonth, mDay;
    String str_FrmDate, str_ToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_violators_list);
        getPreferencesData();
        initview();
        btn_SlctFDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(TopVio_ListActivity.this,
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
                                    str_FrmDate = date_format.format(date_parse.parse(dtdob));
                                    btn_SlctFDate.setText(str_FrmDate.toUpperCase());
                                } catch (ParseException e) {
                                    e.printStackTrace();

                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        btn_SlctTDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                @SuppressLint("SimpleDateFormat") final SimpleDateFormat date_parse = new SimpleDateFormat("dd/MM/yyyy");

                DatePickerDialog datePickerDialog = new DatePickerDialog(TopVio_ListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressWarnings("deprecation")
                            @SuppressLint({"SimpleDateFormat", "DefaultLocale"})

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");
                                String dtdob = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                try {
                                    str_ToDate = date_format.format(date_parse.parse(dtdob));
                                    Date date1;
                                    Date date2;
                                    SimpleDateFormat dates = new SimpleDateFormat("dd-MMM-yyyy");

                                    try {
                                        if (null != str_FrmDate) {
                                            date1 = dates.parse(str_FrmDate);
                                            date2 = dates.parse(str_ToDate);
                                            if (date2.after(date1) || date2.equals(date1)) {
                                                btn_SlctTDate.setText(str_ToDate.toUpperCase());
                                            } else {
                                                showToast("Date should be greater than From_Date ");
                                                btn_SlctTDate.setText("TO DATE");
                                            }
                                        } else {
                                            showToast("Please select From date");
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();

                                }
                            }
                        }, mYear, mMonth, mDay);
                // datePickerDialog.getDatePicker().setMinDate(date_parse.parse(dtdob).getTime());
                datePickerDialog.getDatePicker().setSpinnersShown(false);
                datePickerDialog.getDatePicker().setCalendarViewShown(true);
                datePickerDialog.setTitle("Please Select Date");
                datePickerDialog.show();
            }
        });
        btn_Get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async_GetTopVioltrsList().execute();
            }
        });

    }

    @SuppressLint("SetTextI18n")
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
        officer_Name = (TextView) findViewById(R.id.officer_Name);
        officer_Cadre = (TextView) findViewById(R.id.officer_cadre);
        officer_PS = (TextView) findViewById(R.id.officer_PS);
        textView4 = findViewById(R.id.textView4);
        officer_Name.setText(MainActivity.pidName + "(" + MainActivity.cadre_name + ")");
        officer_Cadre.setText(MainActivity.cadre_name);
        officer_PS.setText(MainActivity.psName);

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

            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    private void initview() {
        btn_SlctFDate = findViewById(R.id.btn_SlctFDate);
        btn_SlctTDate = findViewById(R.id.btn_SlctTDate);
        btn_Get = findViewById(R.id.btn_Get);
    }

    @SuppressLint("StaticFieldLeak")
    public class Async_GetTopVioltrsList extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.getCourtTopVioCasesInfo(pidCode, str_FrmDate, str_ToDate);
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
            if (!ServiceHelper.str_TopVltns_List.equalsIgnoreCase("") &&
                    null != ServiceHelper.str_TopVltns_List) {
                try {
                    Log.d("TopVltnsList", "" + ServiceHelper.str_TopVltns_List);
                    JSONObject jsonObject = new JSONObject(ServiceHelper.str_TopVltns_List);
                   /* JSONArray jsonArray = jsonObject.getJSONArray("IDproofMaster");
                    List<IDProofs> idProofsList = new ArrayList<>(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        IDProofs idProofs = new IDProofs();
                        idProofs.setIdCode("" + object.getString("IdCode"));
                        idProofs.setIdName("" + object.getString("IdName"));
                        idProofsList.add(idProofs);
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showToast("Please check the Network and Try again ");
            }
        }
    }

    @SuppressLint("ShowToast")
    private void showToast(String msg) {
        // TODO Auto-generated method stub
        Toast.makeText(TopVio_ListActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

    }

}
