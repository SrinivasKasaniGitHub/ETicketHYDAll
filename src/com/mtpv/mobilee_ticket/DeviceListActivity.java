package com.mtpv.mobilee_ticket;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.mobilee_ticket_services.DBHelper;

/**
 * Device list.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class DeviceListActivity extends Activity {
	private ListView mListView;
	private DeviceListAdapter mAdapter;
	private ArrayList<BluetoothDevice> mDeviceList;
	
	TextView tv_device_name, bt_device_Address ;
	BluetoothDevice device ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_paired_devices);
		this.setFinishOnTouchOutside(false);
		mDeviceList		= getIntent().getExtras().getParcelableArrayList("device.list");
		
		mListView		= (ListView) findViewById(R.id.lv_paired);
		
		tv_device_name = (TextView)findViewById(R.id.tv_device);

		bt_device_Address = (TextView)findViewById(R.id.bt_device_Address);
		
		mAdapter		= new DeviceListAdapter(this);
		
		mAdapter.setData(mDeviceList);
		mAdapter.setListener(new DeviceListAdapter.OnPairButtonClickListener() {			
			@Override
			public void onPairButtonClick(int position) {
				device = mDeviceList.get(position);
				
				if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
					unpairDevice(device);
				} else {
					showToast("Pairing...");
					
					pairDevice(device);
				}
			}
		});
		
		mListView.setAdapter(mAdapter);
		
		registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)); 
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(mPairReceiver);
		
		super.onDestroy();
	}
	
	
	private void showToast(String msg) {
		Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		View toastView = toast.getView();
		
		ViewGroup group = (ViewGroup) toast.getView();
	    TextView messageTextView = (TextView) group.getChildAt(0);
	    messageTextView.setTextSize(24);
		
    	toastView.setBackgroundResource(R.drawable.toast_background);
	    toast.show();
		
	}
	
    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
	    @SuppressWarnings("unused")
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        
	        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {	        	
	        	  final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
	        	  final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
	        	 
	        	 if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
	        		 showToast("Paired");
	        		 
	        		 try {
	        			 tv_device_name.setText(""+device.getName());
	        			 bt_device_Address.setText(""+device.getAddress());
	        			 String BT_Device = tv_device_name.getText().toString().trim();
	        			 String BT_Address = bt_device_Address.getText().toString().trim();
			        		 
			        		 DBHelper helper= new DBHelper(getApplicationContext());
		        				ContentValues values = new ContentValues();
		        				values.put("BT_NAME", ""+BT_Device);
		        				values.put("BT_ADDRESS", ""+BT_Address);
		        				
		        			 SQLiteDatabase db = openOrCreateDatabase(DBHelper.DATABASE_NAME,MODE_PRIVATE, null);
		        				db.execSQL(DBHelper.termailDetailsCreation);
		        				db.execSQL("delete from " + DBHelper.TERMINAL_DETAILS_TABLE);            			
		        	  			db.insert(DBHelper.TERMINAL_DETAILS_TABLE, null, values); // Inserting Row
		        				
		        				Log.i("TERMINAL_DETAILS_TABLE ::::", "**************INSERTED*******************");
		        				db.close();
		        				
		        				finish();
		        				
		        				Settings_New.et_pinpad.setText(""+BT_Address);
		        				Log.i("BT_Address  ::::",""+BT_Address);
		        				/*Intent pin = new Intent(getApplicationContext(), Settings_New.class);
		        				startActivity(pin);*/
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
	        	 } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
	        		 showToast("Unpaired");
	        	 }
	        	 
	        	 mAdapter.notifyDataSetChanged();
	        }
	    }
	};
}