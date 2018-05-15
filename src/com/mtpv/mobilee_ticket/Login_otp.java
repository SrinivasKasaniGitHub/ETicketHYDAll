package com.mtpv.mobilee_ticket;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.mobilee_ticket_services.ServiceHelper;

/**
 * Created by MANOHAR on 8/8/2017.
 */
@SuppressLint("DefaultLocale")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Login_otp extends Activity {

    EditText otp_input ;
    Button otp_cancel, ok_dialog ;

   static String otpgiven=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_otp_input);
        this.setFinishOnTouchOutside(false);

        otp_input=(EditText)findViewById(R.id.otp_input);
        otp_cancel=(Button)findViewById(R.id.cancel_dialog);
        ok_dialog=(Button)findViewById(R.id.ok_dialog);

        ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login_otp.otpgiven=otp_input.getText().toString().trim();


                 //   Verify_status = "Y";
                    if (isOnline()) {
                   //     SpotChallan.otp_status = "verify";
                        if (Login_otp.otpgiven.equals(MainActivity.otpno)) {
                        new Async_otpverify().execute();

                        }else{
                            showToast("Entered Wrong OTP");
                            otp_input.setText("");
                        }
                    } else {
                        showToast("Please check your network connection!");
                    }

            }
        });



        otp_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = new TextView(Login_otp.this);
                title.setText("Hyderabad E-Ticket");
                title.setBackgroundColor(Color.RED);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                String otp_message = "\n Are you sure, You don't Want to Verify OTP ???\n" ;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login_otp.this, AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(otp_message);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Intent i=new Intent(Login_otp.this,MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

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
        });


    }

    class Async_otpverify extends AsyncTask<Void, Void, String> {

        @SuppressWarnings("deprecation")
        ProgressDialog pd;
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
             pd = ProgressDialog.show(Login_otp.this, "", "",	true);
            pd.setContentView(R.layout.custom_progress_dialog);
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.i("OTP Status", ""+SpotChallan.otp_status);
			/*String regn_no,String mobileNo,String date, String otp,String verify_status*/

            //SpotChallan.otp_status = "send" ;
            ServiceHelper.confirmLoginOTP(MainActivity.pidCodestatic,Login_otp.otpgiven);

            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            pd.dismiss();
            if (ServiceHelper.Opdata_Chalana!=null && "1".equals(ServiceHelper.Opdata_Chalana)) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
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
        //	Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();

        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(24);

        toastView.setBackgroundResource(R.drawable.toast_background);
        toast.show();
    }


}
