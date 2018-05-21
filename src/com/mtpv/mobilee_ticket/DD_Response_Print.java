package com.mtpv.mobilee_ticket;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.ServiceHelper;

public class DD_Response_Print extends Activity {

	TextView text_to_print, tv_sucess_text_header;
	Button back, print, make_paymnt ;
	
	final int PROGRESS_DIALOG = 1;
	
	BluetoothAdapter bluetoothAdapter;
	@SuppressWarnings("unused")
	private BluetoothAdapter mBluetoothAdapter = null;
	@SuppressWarnings("unused")
	private static final int REQUEST_ENABLE_BT = 1;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	String address_spot = "";
	
	public static String printTicket ;
	DBHelper db;
	
	final AnalogicsThermalPrinter actual_printer = new AnalogicsThermalPrinter();
	final Bluetooth_Printer_3inch_ThermalAPI bth_printer = new Bluetooth_Printer_3inch_ThermalAPI();
	String challan_detail ;

	ImageView img_logo;
	TextView officer_Name,officer_Cadre,officer_PS;
	
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dd__response__print);
		
		text_to_print = (TextView)findViewById(R.id.text_to_print);
		text_to_print.setText("");
		
		tv_sucess_text_header = (TextView)findViewById(R.id.textView_header_success_text);
		
		back = (Button)findViewById(R.id.back);
		print = (Button)findViewById(R.id.print);
		make_paymnt = (Button)findViewById(R.id.make_paymnt);

		img_logo=(ImageView)findViewById(R.id.img_logo);
		if (MainActivity.uintCode.equals("22")){
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.cyb_logo));
		}else if (MainActivity.uintCode.equals("23")){
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.htp_left));
		}else if (MainActivity.uintCode.equals("24")){
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.rac_logo));
		}else{
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.htp_left));
		}
		officer_Name=(TextView)findViewById(R.id.officer_Name);
		officer_Cadre=(TextView)findViewById(R.id.officer_cadre);
		officer_PS=(TextView)findViewById(R.id.officer_PS);

		officer_Name.setText(MainActivity.pidName+"("+MainActivity.cadre_name+")");
		officer_Cadre.setText(MainActivity.cadre_name);
		officer_PS.setText(MainActivity.psName);

			
		db = new DBHelper(getApplicationContext());
		
		try {
			db.open();
			db.deleteDuplicateRecords(DBHelper.duplicatePrint_table, ""
					+ getResources().getString(R.string.dup_drunk_drive));
			// db.insertDuplicatePrintDetails(""
			// +
			// ServiceHelper.final_reponse_split[0],""+ServiceHelper.GENERATE_DRUNK_DRIVE_CASE);
			db.insertDuplicatePrintDetails(""
					+ ServiceHelper.final_reponse_split[0], ""
					+ getResources().getString(R.string.dup_drunk_drive));
			db.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.close();
		}

		if (Dashboard.check_vhleHistory_or_Spot.equals("vehiclehistory")) {
			try {
				db.open();
				db.deleteDuplicateRecords(DBHelper.duplicatePrint_table,""+ ""+ getResources().getString(R.string.dup_vhcle_hstry));
				db.insertDuplicatePrintDetails(""+ ServiceHelper.final_spot_reponse_master[0], ""+ getResources().getString(R.string.dup_vhcle_hstry));
				db.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				db.close();
			}
		} else if (Dashboard.check_vhleHistory_or_Spot.equals("spot")) {
			try {
				db.open();
				db.deleteDuplicateRecords(DBHelper.duplicatePrint_table, ""+ getResources().getString(R.string.dup_spot_challan));
				db.insertDuplicatePrintDetails(""+ ServiceHelper.final_spot_reponse_master[0], ""+ getResources().getString(R.string.dup_spot_challan));
				db.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				db.close();
			}
		} else if (Dashboard.check_vhleHistory_or_Spot.equals("towing")) {
			try {
				db.open();
				db.deleteDuplicateRecords(DBHelper.duplicatePrint_table, ""+ getResources().getString(R.string.towing_one_line));
				db.insertDuplicatePrintDetails(""+ ServiceHelper.final_spot_reponse_master[0], ""+ getResources().getString(R.string.towing_one_line));
				db.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				db.close();
			}
		} else if (Dashboard.check_vhleHistory_or_Spot.equals("releasedocuments")) {
			try {
				db.open();
				db.deleteDuplicateRecords(DBHelper.duplicatePrint_table,""+ getResources().getString(R.string.release_documents_one_line));
				db.insertDuplicatePrintDetails("" + ServiceHelper.final_spot_reponse_master[0],""+ getResources().getString(R.string.release_documents_one_line));
				db.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				db.close();
			}
		}
		
		
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		CheckBlueToothState();
		registerReceiver(null, new IntentFilter(BluetoothDevice.ACTION_FOUND));

		preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
		editor = preferences.edit();
		address_spot = preferences.getString("btaddress", "btaddr");
		
		text_to_print.setText(""+ServiceHelper.final_response_master[0]);
		
		Log.i("ServiceHelper.final_response_master[0] :::::", ""+ServiceHelper.final_response_master[0]);
		Log.i("ServiceHelper.final_response_master[1] :::::", ""+ServiceHelper.final_response_master[0]);
		Log.i("ServiceHelper.final_response_master[0] :::::", ""+ServiceHelper.final_response_master[0]);
		Log.i("ServiceHelper.final_response_master[0] :::::", ""+ServiceHelper.final_response_master[0]);
		Log.i("ServiceHelper.final_response_master[0] :::::", ""+ServiceHelper.final_response_master[0]);
		Log.i("ServiceHelper.final_response_master[0] :::::", ""+ServiceHelper.final_response_master[0]);
		Log.i("ServiceHelper.final_response_master[0] :::::", ""+ServiceHelper.final_response_master[0]);
		
		if(text_to_print.getText().toString().length() >15){
			tv_sucess_text_header.setText("" + getResources().getString(R.string.ticket_generated_successfully));	
		}		

		printTicket = text_to_print.getText().toString();
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DD_Response_Print.this, SpotChallan.class));
				DD_Response_Print.this.finish();
				printTicket = "" ;
				text_to_print.setText("");
				
				GenerateDrunkDriveCase.otpStatus = null;
				
			}
		});
		
		make_paymnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent popup = new Intent(getApplicationContext(), PopupDetails.class);
				popup.putExtra("CHALLANS", ""+challan_detail);
				startActivity(popup);
			}
		});
		
		print.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Async_Print().execute();
			}
		});
	}
	
	
	public class Async_Print extends AsyncTask<Void, Void, String>{
		
		ProgressDialog dialog = new ProgressDialog(DD_Response_Print.this);
		
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
			if (bluetoothAdapter.isEnabled()) {
				if (address_spot.equals("btaddr")) {
					showToast("Please set bluetooth address in setting");
				} else {
					try {
						/*String printdata = bth_printer.font_Courier_41("" + printTicket);
						actual_printer.Call_PrintertoPrint("" + address_spot, "" + printdata);*/
						
						Bluetooth_Printer_3inch_ThermalAPI printer = new Bluetooth_Printer_3inch_ThermalAPI();

		                String print_data = printer.font_Courier_41(""+ printTicket);
		                actual_printer.openBT(address_spot);
		                
		                actual_printer.printData(print_data);
		                 Thread.sleep(5000);
		                actual_printer.closeBT();
					} catch (Exception e) {
						// TODO: handle exception
						runOnUiThread(new Runnable(){
					          public void run() {
					        	 showToast("Please set bluetooth Address in Setting");
					          }
					     });
					}
				}
			} else {
				showToast("Enable Bluetooth");
			}
			
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
		}
		
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


	/* BLUETOOTH CONNECTIVITY */
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
				showToast("Bluetooth is NOT Enabled!");

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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		showToast("Please Click on Back Button to go Back");
	}
}