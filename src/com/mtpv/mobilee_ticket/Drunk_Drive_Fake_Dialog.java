package com.mtpv.mobilee_ticket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Drunk_Drive_Fake_Dialog extends Activity {

	EditText last_chasisNo_input;
	Button cancel_dialog, ok_dialog;

	public static String match_chasis = null;

	public static String fake_action = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fake_no_alert);

		this.setFinishOnTouchOutside(false);
		last_chasisNo_input = (EditText) findViewById(R.id.last_chasisNo_input);
		// last_chasisNo_input.setText("983HA");

		cancel_dialog = (Button) findViewById(R.id.cancel_dialog);
		ok_dialog = (Button) findViewById(R.id.ok_dialog);

		/*
		 * cancel_dialog.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub finish(); } });
		 */

		ok_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (last_chasisNo_input.getText().toString().trim() != null
						&& last_chasisNo_input.getText().toString().trim().length() > 1
						&& last_chasisNo_input.getText().toString().trim().length() != 5) {
					showToast("Please Enter Last Five Digits of Chasis No");
				} else if (last_chasisNo_input.getText().toString().trim().length() == 0) {
					last_chasisNo_input.setError(
							Html.fromHtml("<font color='black'>Please Enter Last Five Digits of Chasis No</font>"));
				} else {

					/*
					 * if
					 * ((last_chasisNo_input.getText().toString()).equals("")) {
					 * last_chasisNo_input.setError(Html.fromHtml(
					 * "<font color='black'>Enter Chasis No</font>")); }
					 */
					match_chasis = last_chasisNo_input.getText().toString();
					Log.i("*******fake_veh_chasisNo******", SpotChallan.fake_veh_chasisNo);
					Log.i("*******match_chasis******", match_chasis);

					if (match_chasis.equals(Drunk_Drive.fake_veh_chasisNo)) {
						Log.i("*******match_chasis******", match_chasis);
						// TODO Auto-generated method stub
						fake_action = "not fake";

						TextView title = new TextView(Drunk_Drive_Fake_Dialog.this);
						title.setText("Hyderabad E-Ticket");
						title.setBackgroundColor(Color.RED);
						title.setGravity(Gravity.CENTER);
						title.setTextColor(Color.WHITE);
						title.setTextSize(26);
						title.setTypeface(title.getTypeface(), Typeface.BOLD);
						title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo,
								0);
						title.setPadding(20, 0, 20, 0);
						title.setHeight(70);

						String otp_message = "\nOwner is Genuine\n";

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Drunk_Drive_Fake_Dialog.this,
								AlertDialog.THEME_HOLO_LIGHT);
						alertDialogBuilder.setCustomTitle(title);
						alertDialogBuilder.setIcon(R.drawable.dialog_logo);
						alertDialogBuilder.setMessage(otp_message);
						alertDialogBuilder.setCancelable(false);
						alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								finish();
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

						// vinay_code
					}

					else if (!match_chasis.equals(Drunk_Drive.fake_veh_chasisNo)) {
						Log.i("*******match_chasis******", match_chasis);
						// TODO Auto-generated method stub

						fake_action = "fake";

						TextView title = new TextView(Drunk_Drive_Fake_Dialog.this);
						title.setText("Hyderabad E-Ticket");
						title.setBackgroundColor(Color.RED);
						title.setGravity(Gravity.CENTER);
						title.setTextColor(Color.WHITE);
						title.setTextSize(26);
						title.setTypeface(title.getTypeface(), Typeface.BOLD);
						title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo,
								0);
						title.setPadding(20, 0, 20, 0);
						title.setHeight(70);

						String otp_message = "\nOwner is Fake\nPlease Take Proper Action!!!\n";

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Drunk_Drive_Fake_Dialog.this,
								AlertDialog.THEME_HOLO_LIGHT);
						alertDialogBuilder.setCustomTitle(title);
						alertDialogBuilder.setIcon(R.drawable.dialog_logo);
						alertDialogBuilder.setMessage(otp_message);
						alertDialogBuilder.setCancelable(false);
						alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								startActivity(new Intent(getApplicationContext(), Drunk_Drive.class));
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

				}
			}
		});
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showToast("Enter Last Five Digits of Vehicle Chasis No.");
		last_chasisNo_input
				.setError(Html.fromHtml("<font color='black'>Please Enter Last Five Digits of Chasis No</font>"));
	}

}
