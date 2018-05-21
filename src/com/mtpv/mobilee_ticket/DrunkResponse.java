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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.mtpv.mobilee_ticket.R;
import com.mtpv.mobilee_ticket.DD_Response_Print.Async_Print;
import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.ServiceHelper;

@SuppressWarnings("unused")
public class DrunkResponse extends Activity implements OnClickListener {

//	TextView tv_date_time;
//	TextView tv_eTicket;
//	TextView tv_regno;
//	TextView tv_breath_anlysr;
//	TextView tv_check_sino;
//	TextView tv_alchl_redng;
//	// TextView tv_viloation;
//	TextView tv_drivername;
//	TextView tv_driver_fname;
//	TextView tv_address;
//	TextView tv_police_station;
//	TextView tv_place_of_violation;
//	TextView tv_office_code;
//	TextView tv_officer_name;
//	TextView tv_drvr_dl_no;
//	TextView tv_mobile_no;
//	TextView tv_remarks;

	TextView Tv_printresponse;

	Button btn_print;
	Button btn_back;

	BluetoothAdapter bluetoothAdapter;
	private BluetoothAdapter mBluetoothAdapter = null;
	private static final int REQUEST_ENABLE_BT = 1;
	public static String printdata = "";

	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	String address = "";

	final AnalogicsThermalPrinter actual_printer = new AnalogicsThermalPrinter();
	final Bluetooth_Printer_3inch_ThermalAPI bth_printer = new Bluetooth_Printer_3inch_ThermalAPI();

	DBHelper db;
	
	final int PROGRESS_DIALOG = 1;
	String address_spot = "";
	public static String printTicket ;

	ImageView img_logo;
	TextView officer_Name,officer_Cadre,officer_PS;

	@SuppressLint({ "DefaultLocale", "WorldReadableFiles" })
	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.drunk_drive_response);
		loadUiComponents();

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

//		tv_date_time.setText("" + ServiceHelper.final_response_master[2]+ "   " + ServiceHelper.final_response_master[3]);
//		tv_eTicket.setText("" + ServiceHelper.final_response_master[7]);
//		tv_regno.setText(""+ (ServiceHelper.final_response_master[1].toString().trim().toUpperCase()));
//		tv_breath_anlysr.setText("" + ServiceHelper.final_response_master[4]);
//		tv_check_sino.setText("" + ServiceHelper.final_response_master[5]);
//		tv_alchl_redng.setText("" + ServiceHelper.final_response_master[6]);
//		// tv_viloation.setText(""+ServiceHelper.final_response_master[0]);
//		tv_drivername.setText("" + ServiceHelper.final_response_master[9]);
//		tv_driver_fname.setText("" + ServiceHelper.final_response_master[10]);
//		tv_address.setText("");
//		tv_police_station.setText("" + ServiceHelper.final_response_master[11]);
//		tv_place_of_violation.setText(""
//				+ ServiceHelper.final_response_master[12]);
//		tv_office_code.setText("" + ServiceHelper.final_response_master[13]);
//		tv_officer_name.setText("" + ServiceHelper.final_response_master[14]);
//		tv_drvr_dl_no.setText("" + ServiceHelper.final_response_master[15]);
//		tv_mobile_no.setText("" + ServiceHelper.final_response_master[16]);
//		tv_remarks.setText("");

		try {
			if (ServiceHelper.final_reponse_split.length > 0) {
				Tv_printresponse.setText(ServiceHelper.final_reponse_split[0]);
			}
		}catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			Tv_printresponse.setText("No Data");
		}


		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		CheckBlueToothState();
		registerReceiver(null, new IntentFilter(BluetoothDevice.ACTION_FOUND));

		preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
		editor = preferences.edit();
		address = preferences.getString("btaddress", "btaddr");
		// showToast(address);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_ENABLE_BT) {
			CheckBlueToothState();
		}
	}

	private void loadUiComponents() {
		// TODO Auto-generated method stub
//		tv_date_time = (TextView) findViewById(R.id.tvdatetime_resp_xml);
//		tv_eTicket = (TextView) findViewById(R.id.tveticket_resp_xml);
//		tv_regno = (TextView) findViewById(R.id.tvregno_resp_xml);
//		tv_breath_anlysr = (TextView) findViewById(R.id.tvbreathanly_resp_xml);
//		tv_check_sino = (TextView) findViewById(R.id.tvchecksino_resp_xml);
//		tv_alchl_redng = (TextView) findViewById(R.id.tvalchl_Reading_resp_xml);
//		// tv_viloation=(TextView)findViewById(R.id.tv);
//		tv_drivername = (TextView) findViewById(R.id.tvdrivename_resp_xml);
//		tv_driver_fname = (TextView) findViewById(R.id.tvdrivefname_resp_xml);
//		tv_address = (TextView) findViewById(R.id.tvaddress_resp_xml);
//		tv_police_station = (TextView) findViewById(R.id.tvpolicestan_resp_xml);
//		tv_place_of_violation = (TextView) findViewById(R.id.tvplceviolation_resp_xml);
//		tv_office_code = (TextView) findViewById(R.id.tvofficercode_resp_xml);
//		tv_officer_name = (TextView) findViewById(R.id.tvofficername_resp_xml);
//		tv_drvr_dl_no = (TextView) findViewById(R.id.tvdriver_dl_no_resp_xml);
//		tv_mobile_no = (TextView) findViewById(R.id.tvmob_no_resp_xml);
//		tv_remarks = (TextView) findViewById(R.id.tvremarks_resp_xml);

		Tv_printresponse=(TextView)findViewById(R.id.Tv_printresponse);


		btn_print = (Button) findViewById(R.id.btnprint_res_xml);
		btn_back = (Button) findViewById(R.id.btnhome_res_xml);
		btn_back.setOnClickListener(this);
		btn_print.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnprint_res_xml:
			

			new Async_Print().execute();

			break;
		case R.id.btnhome_res_xml:
			startActivity(new Intent(this, Drunk_Drive.class));
			printdata = "" ;
			GenerateDrunkDriveCase.otpStatus = null;
			
			finish();
			break;

		default:
			break;
		}
	}

public class Async_Print extends AsyncTask<Void, Void, String>{
		
		ProgressDialog dialog = new ProgressDialog(DrunkResponse.this);
		
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
			/*if (bluetoothAdapter.isEnabled()) {
				if (address_spot.equals("btaddr")) {
					showToast("Please set bluetooth address in setting");
				} else {
					try {
						String printdata = bth_printer.font_Courier_41("" + printTicket);
						actual_printer.Call_PrintertoPrint("" + address_spot, "" + printdata);
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
			}*/
			

			if (bluetoothAdapter.isEnabled()) {
				if (address.equals("btaddr")) {
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showToast("Please set bluetooth address in setting");
						}
					});
					
				} else {
					try {
						/*printdata = bth_printer.font_Courier_41(""+ ServiceHelper.final_reponse_split[0]);
						actual_printer.Call_PrintertoPrint("" + address, ""+ printdata);*/

						if(ServiceHelper.final_reponse_split!=null && ServiceHelper.final_reponse_split.length>0)  {
							Bluetooth_Printer_3inch_ThermalAPI printer = new Bluetooth_Printer_3inch_ThermalAPI();

							String print_data = printer.font_Courier_41("" + ServiceHelper.final_reponse_split[0]);
							actual_printer.openBT(address);

							actual_printer.printData(print_data);
							Thread.sleep(5000);
							actual_printer.closeBT();
						}
					} catch (Exception e) {
						// TODO: handle exception
						
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								showToast("Check Bluetooth Details!");
							}
						});
					}
				}
			} else {
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showToast("Enable Bluetooth");
					}
				});
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
		showToast("Pleas Click On Back Button to Get Back");
	}

}
