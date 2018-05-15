package com.mtpv.mobilee_ticket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mtpv.mobilee_ticket_services.ServiceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by MANOHAR on 8/4/2017.
 */

public class UnusedCallsinSpot {


}


//public class Async_checkAadhaarExists extends AsyncTask<Void, Void, String> {
//
//    @Override
//    protected void onPreExecute() {
//        // TODO Auto-generated method stub
//        super.onPreExecute();
//        showDialog(PROGRESS_DIALOG);
//    }
//
//    @Override
//    protected String doInBackground(Void... params) {
//        // TODO Auto-generated method stub
//        // checkAadhar_Ticket(String unit_Code,String regn_No,String
//        // eng_No,String chasis_No)
//        ServiceHelper.checkAadhar_Ticket("" + Dashboard.UNIT_CODE, "" + completeVehicle_num_send, "", "");
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        // TODO Auto-generated method stub
//        super.onPostExecute(result);
//        removeDialog(PROGRESS_DIALOG);
//        if (ServiceHelper.aadhaarDetailsCheck_resp != null) {
//
//            if (ServiceHelper.aadhaarDetailsCheck_resp.equals("NOTEXIST")) {
//                new SpotChallan.Async_getVioPoints().execute();
//                if (et_aadharnumber_spot.getText().toString().length() == 0) {
//                    // new Async_getRTADetails().execute();
//                } else if (et_aadharnumber_spot.getText().toString().trim().length() > 1
//                        && et_aadharnumber_spot.getText().toString().length() == 12
//                        && ver.isValid(et_aadharnumber_spot.getText().toString())) {
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            // new Async_getRTADetails().execute();
//                        }
//                    });
//
//                } else if (et_aadharnumber_spot.getText().toString().trim().length() > 1
//                        && et_aadharnumber_spot.getText().toString().length() == 12
//                        && !ver.isValid(et_aadharnumber_spot.getText().toString())) {
//
//
//                    ShowMessage("\n It's a Invalid Aadhar, \n Please Enter Valid Aadhar Number !! \n");
//
//                } else if (et_regcid_spot.getText().toString().equals("")
//                        || et_last_num_spot.getText().toString().equals("")) {
//
//                    ShowMessage("\nEnter Proper Vehicle Number !! \n");
//
//                }
//
//                if (isOnline()) {
//                    rl_rta_details_layout.setVisibility(View.GONE);
//                    Dashboard.rta_details_request_from = "SPOT";
//
//                    if (et_aadharnumber_spot.getText().toString().trim() != null
//                            && et_aadharnumber_spot.getText().toString().trim().length() > 1
//                            && et_aadharnumber_spot.getText().toString().length() != 12) {
//
//                        ShowMessage("\n Aadhaar Number Length must be 12 digits!! \n");
//
//                    } else if (et_aadharnumber_spot.getText().toString().trim().length() == 12
//                            && (ver.isValid(et_aadharnumber_spot.getText().toString()))) {
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                // TODO Auto-generated method stub
//                                // new Async_getAadharDetails().execute();
//                            }
//                        });
//                    }
//
//
//                    ll_aadhar_layout.setVisibility(View.GONE);
//                    tv_aadhar_header.setVisibility(View.GONE);
//                }
//            } else if (ServiceHelper.aadhaarDetailsCheck_resp.equals("EXIST")) {
//                TextView title = new TextView(SpotChallan.this);
//                title.setText("Hyderabad E-Ticket");
//                title.setBackgroundColor(Color.RED);
//                title.setGravity(Gravity.CENTER);
//                title.setTextColor(Color.WHITE);
//                title.setTextSize(26);
//                title.setTypeface(title.getTypeface(), Typeface.BOLD);
//                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
//                title.setPadding(20, 0, 20, 0);
//                title.setHeight(70);
//
//                String otp_message = "\n On this Vehicle \n " + "Without Aadhaar No Ticket is in Pending, "
//                        + "\n Kindly Update Aadhaar then, \n" + "Generate a New Ticket....! \n";
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SpotChallan.this,
//                        AlertDialog.THEME_HOLO_LIGHT);
//                alertDialogBuilder.setCustomTitle(title);
//                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
//                alertDialogBuilder.setMessage(otp_message);
//                alertDialogBuilder.setCancelable(false);
//                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        Intent intent = new Intent(SpotChallan.this, SpotChallan.class);
//                        startActivity(intent);
//                    }
//                });
//
//                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                    }
//                });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//
//                alertDialog.getWindow().getAttributes();
//
//                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
//                textView.setTextSize(28);
//                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
//                textView.setGravity(Gravity.CENTER);
//
//                Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//                btn.setTextSize(22);
//                btn.setTextColor(Color.WHITE);
//                btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
//                btn.setBackgroundColor(Color.RED);
//
//                Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//                btn2.setTextSize(22);
//                btn2.setTextColor(Color.WHITE);
//                btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
//                btn2.setBackgroundColor(Color.RED);
//            }
//        } else {
//            showToast("Error!");
//        }
//    }



//// Async_getVioPoints
//
//public class Async_getVioPoints extends AsyncTask<Void, Void, String> {
//    @Override
//    protected String doInBackground(Void... params) {
//        // TODO Auto-generated method stub
//        // public String getViolationsPoints(String unitCode,String
//        // aadharNo,String drivingLicenseNo);
//        ServiceHelper.getViolations_Points("" + Dashboard.UNIT_CODE,
//                "" + aadhaar.trim(),
//                "" + licence_no.toString().trim());
//        return null;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        // TODO Auto-generated method stub
//        super.onPreExecute();
//        showDialog(PROGRESS_DIALOG);
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        // TODO Auto-generated method stub
//        super.onPostExecute(result);
//        removeDialog(PROGRESS_DIALOG);
//        if (ServiceHelper.points_resp != null) {
//
//            if (ServiceHelper.points_resp.length() > 0 && !ServiceHelper.points_resp.equals("NA")) {
//                // new Async_getRTADetails().execute();
//
//                try {
//                    String resp_json = ServiceHelper.points_resp;
//                    JSONObject jsonRootObject = new JSONObject(resp_json); // array
//                    // method
//                    JSONArray jsonArray = jsonRootObject.optJSONArray("POINTS_TABLE"); // array
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                        if (i == 0) {
//                            aadhr_point_frm_json = jsonObject.optString("AADHAR_POINTS").toString();
//                            aadhr_Points = "" + aadhr_point_frm_json.trim();
//                            aadhaar_offence_year = jsonObject.optString("OFFENCE_YEAR").toString();
//
//
//                        } else if (i == 1) {
//                            dl_point_frm_json = jsonObject.optString("DL_POINTS").toString();
//                            DL_Points = "" + dl_point_frm_json.trim();
//
//                            dl_offence_year = jsonObject.optString("OFFENCE_YEAR").toString();
//
//
//                        }
//                    }
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            showToast("Error!");
//        }
//    }
//}
