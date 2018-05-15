package com.mtpv.mobilee_ticket;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.mtpv.mobilee_ticket_services.ServiceHelper;

public class Aadhaar_Update_Print extends Activity {
	
	TextView update_print ;
	Button back, print ;
	
	BluetoothAdapter bluetoothAdapter;
	String address_spot = "";
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	final AnalogicsThermalPrinter actual_printer = new AnalogicsThermalPrinter();
	final Bluetooth_Printer_3inch_ThermalAPI bth_printer = new Bluetooth_Printer_3inch_ThermalAPI();
	
	public static String printdata = null ;
	
	final int PROGRESS_DIALOG = 1;

	@SuppressWarnings("deprecation")
	@SuppressLint("WorldReadableFiles")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_aadhaar__update__print);
		
		update_print = (TextView)findViewById(R.id.update_print);
		
		back = (Button)findViewById(R.id.back);
		print = (Button)findViewById(R.id.print); 
		
		update_print.setVisibility(View.GONE);
		
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		CheckBlueToothState();
		registerReceiver(null, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		
		preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
		editor = preferences.edit();
		address_spot = preferences.getString("btaddress", "btaddr");
		
		if (!ServiceHelper.UpdateAadhaar_resp.equals("NA")) {
			
			update_print.setText(""+ServiceHelper.UpdateAadhaar_resp);
			update_print.setVisibility(View.VISIBLE);
			//showToast(""+ServiceHelper.UpdateAadhaar_resp);
		}else {
			update_print.setVisibility(View.GONE);
			showToast("Unable to Update Aadhaar Number");
		}
		
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				update_print.setText("");
				Intent back = new Intent(Aadhaar_Update_Print.this, Dashboard.class);
				startActivity(back);
			}
		});
		
		
		print.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!update_print.getText().toString().trim().equals("0") || update_print.getText().toString().trim().length()>0) {
					printdata = update_print.getText().toString().trim();
					new Async_AdhaarUpdatePrint().execute();	
				}else {
					showToast("No Print Found");
				}
				
			}
		});
	}
	
	public class Async_AdhaarUpdatePrint extends AsyncTask<Void, Void, String>{

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			showDialog(PROGRESS_DIALOG);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (bluetoothAdapter.isEnabled()) {
						if (address_spot.equals("btaddr")) {
							showToast("Please set bluetooth address in setting");
						} else {
							try {
								/*String printing_data = bth_printer.font_Courier_41("" + printdata);
								actual_printer.Call_PrintertoPrint("" + address_spot, "" + printing_data);*/
								
								Bluetooth_Printer_3inch_ThermalAPI printer = new Bluetooth_Printer_3inch_ThermalAPI();

				                String print_data = printer.font_Courier_41(""+ printdata);
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
				}
			});
			
			return null;
		}
		
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
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
			
			
			break;

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
