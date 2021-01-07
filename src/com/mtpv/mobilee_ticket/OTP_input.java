package com.mtpv.mobilee_ticket;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.mobilee_ticket_services.ServiceHelper;

import java.util.concurrent.TimeUnit;

@SuppressLint("DefaultLocale")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class OTP_input extends Activity {

	final int PROGRESS_DIALOG = 0;
	final int OTP_CNFRMTN_DIALOG = 7;
	static boolean active = false;
	EditText otp_input ;
	TextView otp_heading,otp_timer;
	Button otp_cancel, ok_dialog ;
	AlertDialog alertDialog;
	Handler handler;
	public static String otp_number ="", reg_No, Mobile_No, OTP_date, OTP_No, Verify_status , close_Decision = "", OTP_status = null ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_otp_input);

		this.setFinishOnTouchOutside(false);
//		this.onTouchEvent(null);

		otp_input = (EditText)findViewById(R.id.otp_input);
		ok_dialog = (Button)findViewById(R.id.ok_dialog);
		otp_cancel = (Button)findViewById(R.id.cancel_dialog);
		otp_heading=(TextView)findViewById(R.id.otp_heading);
		otp_timer=(TextView)findViewById(R.id.otp_timer);


		otp_heading.setText("Enter OTP For "+SpotChallan.mobilenumber);

		Intent get  = getIntent();
		//SpotChallan.OtpResponseDelayTime="90";//
		new CountDownTimer(TimeUnit.SECONDS.toMillis(Long.parseLong(SpotChallan.OtpResponseDelayTime!=null?SpotChallan.OtpResponseDelayTime:"0")), 1000) {

			public void onTick(long millisUntilFinished) {
				otp_timer.setText("Elapse Time :"+ (millisUntilFinished/1000)+" sec");
				//here you can have your logic to set text to edittext
			}

			public void onFinish() {
				otp_timer.setText("done!");
			}

		}.start();

		//if(active) {
		handler = new Handler();
		handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									// do something

									if (otp_input.getText().toString().equalsIgnoreCase(null) ||
											otp_input.getText().toString().equalsIgnoreCase("") || otp_input.getText().toString().length()<4) {
										OtpSessionExpired();
									} else {
										handler.removeCallbacks(this);
									}



								}
							}, TimeUnit.SECONDS.toMillis(Long.parseLong(SpotChallan.OtpResponseDelayTime!=
						null?SpotChallan.OtpResponseDelayTime:"0"))


		);
		//}

		reg_No = get.getExtras().getString("regNO");
		Mobile_No = get.getExtras().getString("MobileNo");
		OTP_date = get.getExtras().getString("otp_date");
		OTP_No = get.getExtras().getString("OTP_value");
		Verify_status = get.getExtras().getString("veify_status");


		otp_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				cancelButtonAlert();


			/*	if (close_Decision.equals("N")) {

				}else if(close_Decision.equals("Y")){
					finish();
				}*/
			}
		});

		ok_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (otp_input.getText().toString().trim().equals(OTP_No)) {
					Verify_status = "Y";
					if (isOnline()) {
						SpotChallan.otp_status = "verify";
						mtpv_SpecialDrive.otp_status="verify";


						new Async_otpverify().execute();
					} else {
						showToast("Please check your network connection!");
					}
				}else{
					Verify_status = "N";
					showToast("Entered Wrong OTP");
					otp_input.setText("");
				}
			}
		});

	}

	public Boolean isOnline() {
		ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
		return nwInfo != null;
	}

	class Async_otpverify extends AsyncTask<Void, Void, String>{

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
			Log.i("OTP Status", ""+SpotChallan.otp_status);
			/*String regn_no,String mobileNo,String date, String otp,String verify_status*/

			//SpotChallan.otp_status = "send" ;
			ServiceHelper.verifyOTP(reg_No , Mobile_No, OTP_date, ""+otp_input.getText().toString().trim(), Verify_status);

			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			if (!ServiceHelper.Opdata_Chalana.equals(null) && !ServiceHelper.Opdata_Chalana.equals("")) {

				if (ServiceHelper.Opdata_Chalana.equals("0"))
				{
					showToast("Entered Wrong OTP");
					otp_input.setText("");
				}
				else if(ServiceHelper.Opdata_Chalana.equals("1")){
					showToast("OTP Verified Successfully");
					OTP_status = ""+ServiceHelper.Opdata_Chalana ;

					if(!Dashboard.otpbuttoncheckinspldrive.equals(null) && Dashboard.otpbuttoncheckinspldrive.equalsIgnoreCase("Spot")){
						//SpotChallan.btn_send_otp_to_mobile.setVisibility(View.GONE);

					}
					else if(!Dashboard.otpbuttoncheckinspldrive.equals(null) && Dashboard.otpbuttoncheckinspldrive.equalsIgnoreCase("mtpv_SpecialDrive"))
					{
						mtpv_SpecialDrive.btn_send_otp_to_mobile.setVisibility(View.GONE);
					}
					else if(!Dashboard.otpbuttoncheckinspldrive.equals(null) && Dashboard.otpbuttoncheckinspldrive.equalsIgnoreCase("dd"))
					{
						//GenerateDrunkDriveCase.btn_send_otp_to_mobile.setVisibility(View.GONE);
					}

					GenerateDrunkDriveCase.otpStatus = OTP_status;
					finish();

				}
				else if(ServiceHelper.Opdata_Chalana.equals("2") ){
					//showToast("OTP Verified Successfully");
					OTP_status = ""+ServiceHelper.Opdata_Chalana ;
					GenerateDrunkDriveCase.otpStatus = OTP_status;

					if(!Dashboard.otpbuttoncheckinspldrive.equals(null) && Dashboard.otpbuttoncheckinspldrive.equalsIgnoreCase("Spot")){
						//SpotChallan.btn_send_otp_to_mobile.setVisibility(View.GONE);

					}
					else if(!Dashboard.otpbuttoncheckinspldrive.equals(null) && Dashboard.otpbuttoncheckinspldrive.equalsIgnoreCase("mtpv_SpecialDrive"))
					{
						mtpv_SpecialDrive.btn_send_otp_to_mobile.setVisibility(View.GONE);
					}
					else if(!Dashboard.otpbuttoncheckinspldrive.equals(null) && Dashboard.otpbuttoncheckinspldrive.equalsIgnoreCase("dd"))
					{
						//GenerateDrunkDriveCase.btn_send_otp_to_mobile.setVisibility(View.GONE);
					}
					finish();

				}else if (ServiceHelper.Opdata_Chalana.equals("NA")){

					OTP_status = ""+ServiceHelper.Opdata_Chalana ;
					GenerateDrunkDriveCase.otpStatus = OTP_status;

					if(!Dashboard.otpbuttoncheckinspldrive.equals(null) && Dashboard.otpbuttoncheckinspldrive.equalsIgnoreCase("Spot")){
						//SpotChallan.btn_send_otp_to_mobile.setVisibility(View.GONE);

					}
					else if(!Dashboard.otpbuttoncheckinspldrive.equals(null) && Dashboard.otpbuttoncheckinspldrive.equalsIgnoreCase("mtpv_SpecialDrive"))
					{
						mtpv_SpecialDrive.btn_send_otp_to_mobile.setVisibility(View.GONE);
					}
					else if(!Dashboard.otpbuttoncheckinspldrive.equals(null) && Dashboard.otpbuttoncheckinspldrive.equalsIgnoreCase("dd"))
					{
						//GenerateDrunkDriveCase.btn_send_otp_to_mobile.setVisibility(View.GONE);
					}
					finish();
				}

			}


		}
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

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
			case PROGRESS_DIALOG:
				ProgressDialog pd = ProgressDialog.show(this, "", "",	true);
				pd.setContentView(R.layout.custom_progress_dialog);
				pd.setCancelable(false);

				return pd;

			default:
				break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//	super.onBackPressed();
		showToast("Please Click on Cancel Button to go Back ..!");
	}

	public  void cancelButtonAlert()
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TextView title = new TextView(OTP_input.this);
				title.setText("Hyderabad E-Ticket");
				title.setBackgroundColor(Color.RED);
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.WHITE);
				title.setTextSize(26);
				title.setTypeface(title.getTypeface(), Typeface.BOLD);
				title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
				title.setPadding(20, 0, 20, 0);
				title.setHeight(70);

				String otp_message = "\n Are you sure, You don't Want to Verify OTP ???\n";

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OTP_input.this, AlertDialog.THEME_HOLO_LIGHT);
				alertDialogBuilder.setCustomTitle(title);
				alertDialogBuilder.setIcon(R.drawable.dialog_logo);
				alertDialogBuilder.setMessage(otp_message);
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub

								handler.removeCallbacks(null);
								handler.removeCallbacksAndMessages(null);
								handler.removeCallbacksAndMessages(null);

								close_Decision = "Y";
								finish();
							}
						});

				alertDialogBuilder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								close_Decision = "N";
							}
						});

				alertDialog = alertDialogBuilder.create();
				if (!OTP_input.this.isFinishing()) {
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


			}
		});

	}

	public void OtpSessionExpired() {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				try {

					TextView title = new TextView(OTP_input.this);
					title.setText("ALERT");
					title.setBackgroundColor(Color.BLUE);
					title.setGravity(Gravity.CENTER);
					title.setTextColor(Color.WHITE);
					title.setTextSize(26f);
					title.setTypeface(title.getTypeface(), Typeface.BOLD);
					title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
					title.setPadding(20, 0, 20, 0);
					title.setHeight(70);

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OTP_input.this,
							AlertDialog.THEME_HOLO_LIGHT);
					alertDialogBuilder.setCustomTitle(title);
					alertDialogBuilder.setIcon(R.drawable.dialog_logo);


					alertDialogBuilder.setMessage("\nIf OTP is not received, Please skip and generate the challan \n");


					alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {

							Verify_status = "S";
							if (isOnline()) {
								SpotChallan.otp_status = "verify";
								mtpv_SpecialDrive.otp_status = "verify";


								new Async_otpverify().execute();
							} else {
								showToast("Please check your network connection!");
							}


						}
					});


					alertDialogBuilder.setCancelable(false);
					AlertDialog alertDialog = alertDialogBuilder.create();
					if (!OTP_input.this.isFinishing()) {


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

				}catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
		active = true;
	}
	@Override
	public void onStop() {
		super.onStop();
		active = false;
	}

	@Override
	public void onDestroy () {

		handler.removeCallbacks(null);
		handler.removeCallbacksAndMessages(null);
		handler.removeCallbacksAndMessages(null);
		super.onDestroy ();

	}
}
