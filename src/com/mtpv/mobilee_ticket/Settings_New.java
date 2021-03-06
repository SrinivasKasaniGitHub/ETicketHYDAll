package com.mtpv.mobilee_ticket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.imagematch.BleAdapter;
import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.ServiceHelper;
import com.mtpv.mobilee_ticket_services.SharedPrefsHelper;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.justec.com.bleoperator.BleManager;
import app.justec.com.bleoperator.callback.BleGattCallback;
import app.justec.com.bleoperator.callback.BleRssiCallback;
import app.justec.com.bleoperator.callback.BleScanCallback;
import app.justec.com.bleoperator.comm.BleDevice;
import app.justec.com.bleoperator.data.ReceiveFormDataResult;
import app.justec.com.bleoperator.data.RecordForm;
import app.justec.com.bleoperator.event.EventManger;
import app.justec.com.bleoperator.exception.BleException;
import app.justec.com.bleoperator.helper.DataSource;
import app.justec.com.bleoperator.helper.RepeatCommand;
import app.justec.com.bleoperator.scan.BleScanRuleConfig;

@SuppressWarnings("unused")
@SuppressLint("SdCardPath")
public class Settings_New extends Activity implements OnClickListener, DataSource.DataCallBack<ArrayList<RecordForm>>,
		RepeatCommand {

	/*String server = "192.168.11.9";*/
	String server = null;
	int port = 99;
	String username = "ftpuser";
	String password = "Dk0r$l1qMp6";
	String filename = "Version-1.5.1.apk";

	private static final int BUFFER_SIZE = 4096;
	ProgressBar progress;
	Dialog dialog;
	int downloadedSize = 0;
	int totalSize = 0;
	TextView cur_val;

	Button btn_ps_name;
	public static Button btn_pointby_ps_name;
	public static Button btn_ResponsiblePSName;
	Button btn_save;
	Button btn_cancel;
	Button btn_back;
	EditText et_exact_location;

	EditText et_analyser;
	EditText et_web_url;

	final int PS_NAME_DIALOG = 0;
	final int PS_CODE_DIALOG = 1;
	final int PROGRESS_DIALOG = 2;
	final int PS_RESPONSIBLE_NAME_DIALOG=3;

	int selected_ps_name = -1;
	int selected_responsible_ps_name=-1;
	int selected_pointby_psname = -1;
	int ps_code_pos,ps_responsible_Code;


	String ps_name_title = "Select PS Name";
	String ps_code_title = "Select PS By Point Name";

	String[][] psname_name_code_arr;// to get names and ps codes ex :
	// psname_name_code_arr[0][0]="uppal"; and
	// psname_name_code_arr[0][1]="2301";
	ArrayList<String> ps_names_arr, ps_codes_fr_names_arr;// to get ps names
	// from
	// psname_name_code_arr

	String[][] pointNameBYpsname_name_code_arr;
	ArrayList<String> pointNameBy_PsName_arr;// point name for second dialog
	ArrayList<String> pointNameBy_PsName_code_arr;// point code for second
	// dialog

	GenerateDrunkDriveCase dashboard;
	DBHelper db;
	Cursor c_psnames, pinpad_cursor, bt_cursor;
	String[] psname_code, psname_name;

	ListView lv_bt_items;
	TextView tv_stateBluetooth;
	Button btn_scan_bluetooth,btnscan_BreathAnalyser;
	EditText et_bt_address;

	TextView appversion, ffd;

	public static ImageView update_apk;

	BluetoothAdapter bluetoothAdapter;
	private BluetoothAdapter mBluetoothAdapter = null;
	static final UUID MY_UUID = UUID.randomUUID();
	ArrayAdapter<String> btArrayAdapter;
	private static final int REQUEST_ENABLE_BT = 1;
	String address = "";

	public static String changepwdotpresp = "";
	public static String apkurl;
	public static String version;

	Button btn_pinpadscan_xml;
	public static EditText et_pinpad;
	public static ImageView change_password;
	public static String BLT_Name = "", PINpad_Name = "", PINpad_Adress = "",
			BTprinter_Name = "", BTprinter_Adress = "", blue_Adress = null,
			blue_Name = null;

	public static boolean bluetoothFLG = false, pinpadFLG = false;

	ImageView img_logo;
	TextView officer_Name,officer_Cadre,officer_PS,textView_header_spot_challan_xml;

	private ListView list_BleDevice;
	private BleAdapter adapter;
	private List<BleDevice> dataList = new ArrayList<>();
	private DataSource.DataCallBack<String> callBack;
	AlertDialog dlg_BleDevice;
	ProgressDialog progressDialog;

	String contact_no;


	@SuppressWarnings("deprecation")
	@SuppressLint("WorldReadableFiles")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);
		progressDialog=new ProgressDialog(this);

		bluetoothFLG = false;
		pinpadFLG = false;

		appversion = (TextView) findViewById(R.id.textView2);
		version = appversion.getText().toString().trim() + ".apk";

		Log.i("APP_Version :::", ""+ version);

		apkurl = "ftp://192.168.11.9:99/23/TabAPK/" + version;

		LoadUIComponents();

		textView_header_spot_challan_xml=(TextView)findViewById(R.id.textView_header_spot_challan_xml);
		textView_header_spot_challan_xml.setText("Settings");

		img_logo=(ImageView)findViewById(R.id.img_logo);
		if (MainActivity.uintCode.equals("22")){
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
		}else if (MainActivity.uintCode.equals("23")){
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
		}else if (MainActivity.uintCode.equals("24")){
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
		}else if (MainActivity.uintCode.equals("44")) { //44 Warangal
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
		}else {//  69 Siddipet
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
		}
		officer_Name=(TextView)findViewById(R.id.officer_Name);
		officer_Cadre=(TextView)findViewById(R.id.officer_cadre);
		officer_PS=(TextView)findViewById(R.id.officer_PS);

		officer_Name.setText(MainActivity.pidName+"("+MainActivity.cadre_name+")");
		officer_Cadre.setText(MainActivity.cadre_name);
		officer_PS.setText(MainActivity.psName);

		dashboard = new GenerateDrunkDriveCase();
		db = new DBHelper(getApplicationContext());
		try {
			db.open();
			c_psnames = DBHelper.db.rawQuery("select * from "+ DBHelper.psName_table, null);
			if (c_psnames.getCount() == 0) {
				Log.i("WHEELER DB DETAILS", "0");
			} else {
				psname_code = new String[c_psnames.getCount()];
				psname_name = new String[c_psnames.getCount()];

				int count = 0;
				while (c_psnames.moveToNext()) {
					psname_code[count] = c_psnames.getString(1);
					psname_name[count] = c_psnames.getString(2);
					Log.i("code", ""+ psname_code[count]);
					Log.i("name", ""+ psname_name[count]);
					count++;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		et_pinpad = (EditText)findViewById(R.id.edt_pinpad_xml);
		
		/*if (et_pinpad.getText().toString().trim().equals("")) {
			try {
				db.open();
				android.database.sqlite.SQLiteDatabase db = openOrCreateDatabase(
						DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
				String selectQuery = "SELECT  * FROM "
						+ DBHelper.TERMINAL_DETAILS_TABLE;
				pinpad_cursor = db.rawQuery(selectQuery, null);

				if (pinpad_cursor.moveToFirst()) {
					do {
						PINpad_Adress = pinpad_cursor.getString(3);
						PINpad_Name = pinpad_cursor.getString(2);

						Log.i("PINpad_Adress :",
								"" + pinpad_cursor.getString(3));
						Log.i("PINpad_Name :", "" + pinpad_cursor.getString(2));

						
						 * Log.i("0 :",""+ pinpad_cursor.getString(0));
						 * Log.i("1 :",""+ pinpad_cursor.getString(1));
						 * Log.i("2 :",""+ pinpad_cursor.getString(2));
						 * Log.i("3 :",""+ pinpad_cursor.getString(3));
						 

						et_pinpad.setText(PINpad_Adress);

					} while (pinpad_cursor.moveToNext());
				}
				db.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (db != null)
					db.close();
				if (pinpad_cursor != null)
					pinpad_cursor.close();
			}

		}*/
		/*---------------------------------------------*/

		dashboard.preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
		dashboard.editor = dashboard.preferences.edit();

		/* FOR PS NAMES */
		dashboard.ps_code_set = dashboard.preferences.getInt("psname_code_toSet", selected_ps_name);
		dashboard.ps_name = dashboard.preferences.getString("psname_name", "psname");
		Log.i("PS CODE", "" + dashboard.ps_code_set);
		Log.i("PS NAME", "" + dashboard.ps_name);
		/*---------------------------------------------*/

		/* FOR PS POINT NAME */
		dashboard.psnameby_point_code_set = dashboard.preferences.getInt("point_code_toSet", selected_pointby_psname);
		dashboard.point_name = dashboard.preferences.getString("point_name","pointname");

		Log.i("POINT CODE", ""+ dashboard.psnameby_point_code_set);
		Log.i("POINT NAME", ""+ dashboard.point_name);
		/*---------------------------------------------*/

		/* GETTING BLUETOOTH ADDRESS */
		dashboard.bluetooth_address = dashboard.preferences.getString("btaddress", "bt");
		/*---------------------------------------------*/

		/* GETTING WEB URL */
		Dashboard.modified_url = dashboard.preferences.getString("weburl", "myurl");

		if (Dashboard.modified_url.equals("myurl")) {
		} else {
			et_web_url.setText("" + Dashboard.modified_url);
		}

		/* FOR PS NAME START */
		if (dashboard.ps_code_set == -1) {
		} else {
			selected_ps_name = dashboard.ps_code_set;
		}

		if (dashboard.ps_name.equals("psname")) {
		} else {
			btn_ps_name.setText("" + dashboard.ps_name);
			btn_ResponsiblePSName.setText("" + dashboard.ps_name);
			if (isOnline()) {
				selected_pointby_psname = -1;
				btn_pointby_ps_name.setText(""+ getResources().getString(R.string.select_pointbypsname));
				if (psname_code.length >=selected_ps_name) {
					new Async_getPointNameByPsName().execute();
				}else{
					showToast("Please check the Download Masters!");
					dashboard.editor.clear();


				}
			} else {
				showToast("Please check your network connection!");
			}
		}
		/* FOR PS NAME END */

		/* FOR PS POINT NAME START */
		if (dashboard.psnameby_point_code_set == -1) {
		} else {
			selected_pointby_psname = dashboard.psnameby_point_code_set;
		}

		if (dashboard.point_name.equals("pointname")) {
		} else {
			btn_pointby_ps_name.setText(""+ dashboard.point_name);
		}
		/* FOR PS POINT NAME END */

		dashboard.exact_location = dashboard.preferences.getString("ps_res_name_code", "location");



		Long anly_id = dashboard.preferences.getLong("analyser_id", 0);

		if (dashboard.exact_location.equals("location")) {
		} else {
			//et_exact_location.setText("" + dashboard.exact_location);
			btn_ResponsiblePSName.setText(""+ dashboard.preferences.getString("ps_res_name_name", "location"));

		}

		if (anly_id == 0) {
		} else {
			et_analyser.setText("" + anly_id);
		}

		/* GETTING BLUETOOTH ADDRESS */

		if (dashboard.bluetooth_address.equals("bt")) {
			et_bt_address.setText("");
		} else {
			et_bt_address.setText("" + dashboard.bluetooth_address);
		}

		/* BLUETOOTH CONNECTIVITY */
		// if (et_bt_address.getText().toString().trim().equals("")) {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		CheckBlueToothState();
		registerReceiver(ActionFoundReceiver, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));
		btArrayAdapter = new ArrayAdapter<String>(Settings_New.this,
				android.R.layout.simple_list_item_1);
		lv_bt_items.setAdapter(btArrayAdapter);

		lv_bt_items.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				if (bluetoothFLG) {

					String selection = (String) (lv_bt_items
							.getItemAtPosition(position));



					Toast.makeText(getApplicationContext(),
							"BLUETOOTH ADDRESS IS SAVED SUCCESSFULLY",
							Toast.LENGTH_SHORT).show();
					address = "";
					address = selection.substring(0, 17);
					et_bt_address.setText(address);

					mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					Alertmessage();
				} else if (pinpadFLG) {

					String selection = (String) (lv_bt_items
							.getItemAtPosition(position));



					Toast.makeText(getApplicationContext(),
							"PINPAD ADDRESS IS SAVED SUCCESSFULLY",
							Toast.LENGTH_SHORT).show();
					address = "";
					address = selection.substring(0, 17);
					et_pinpad.setText(address);

					mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					Alertmessage();
				}

			}
		});

		// }

	}


	@Override
	public void onDataNotAvailed(int i) {

	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onDataLoaded(ArrayList<RecordForm> recordForms) {


	}

	@Override
	public void OnRepeatCommand(String s) {

	}

	private void initBle() {

		BleManager.getInstance().init(getApplication());
		BleManager.getInstance()
				.enableLog(true)
				.setMaxConnectCount(3)
				.setOperateTimeout(1500);
		BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
				.setScanTimeOut(6000)
				.build();
		BleManager.getInstance()
				.initScanRule(scanRuleConfig);
	}

	private void initView() {
		dlg_BleDevice = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.blescandevice_dialog, null);
		dlg_BleDevice.setView(dialogView);
		list_BleDevice = dialogView.findViewById(R.id.list_BleDevice);
		adapter = new BleAdapter(this, dataList);
		list_BleDevice.setAdapter(adapter);
		progressDialog.dismiss();
		dlg_BleDevice.show();
		dlg_BleDevice.setCancelable(true);

		list_BleDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (!BleManager.getInstance().isConnected((BleDevice) adapter.getItem(position))) {
					BleManager.getInstance().cancelScan();
					BleDevice bleDevice=(BleDevice) adapter.getItem(position);
					SharedPrefsHelper.saveObjectToSharedPreference(getApplicationContext(), "mPreference", "mLoginRes", bleDevice);
					connect(bleDevice);
					dlg_BleDevice.dismiss();
				}
			}
		});
	}

	private void startScan() {
		BleManager.getInstance().scan(new BleScanCallback() {
			@Override
			public void onScanStarted(boolean success) {
				dataList.clear();
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onLeScan(BleDevice bleDevice) {
				super.onLeScan(bleDevice);
                /*if (!TextUtils.isEmpty(bleDevice.getName())) {
                    Log.d("Ble Name",""+bleDevice.getName());
                    adapter.add(bleDevice);
                }*/


			}

			@Override
			public void onScanning(BleDevice bleDevice) {
				if (!TextUtils.isEmpty(bleDevice.getName())) {
					Log.d("Ble Name", "" + bleDevice.getName());
					adapter.add(bleDevice);
				}
			}

			@Override
			public void onScanFinished(List<BleDevice> scanResultList) {
               /* adapter = new BleAdapter(MainActivity.this, scanResultList);
                listView.setAdapter(adapter);*/
			}
		});
	}

	private void connect(final BleDevice bleDevice) {
		BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
			@Override
			public void onStartConnect() {

			}

			@Override
			public void onConnectFail(BleException exception) {

				Toast.makeText(Settings_New.this, "fail", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
				Toast.makeText(Settings_New.this, "ConnectSuccess", Toast.LENGTH_LONG).show();
				readRssi(bleDevice);
				EventManger.getInstance()
						.initDeviceInfo(bleDevice, true, callBack);
			}

			@Override
			public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {

			}

			@Override
			public void onReConnect(BleDevice bleDevice) {

			}
		});
	}

	private void readRssi(BleDevice bleDevice) {
		BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
			@Override
			public void onRssiFailure(BleException exception) {
				Log.i("bleble", "onRssiFailure" + exception.toString());
			}

			@Override
			public void onRssiSuccess(int rssi) {
				Log.i("bleble", "onRssiSuccess: " + rssi);
			}
		});
	}

	@Subscribe(sticky = true)
	public void instantRecordData(ReceiveFormDataResult receiveFormDataResult) {
		//  TAG_INSTANT_RECEIVE=10004;
		if (receiveFormDataResult != null && receiveFormDataResult.getTag() == EventManger.getInstance().TAG_INSTANT_RECEIVE) {
			Log.i("bleble5", "recordFormsReceive=" + receiveFormDataResult.getRecordFormArrayList().size());
			ArrayList<RecordForm> recordForms = receiveFormDataResult.getRecordFormArrayList();
			//这里是通过eventBus接受的具体数据
			//This is the detail data received through eventBus
			for (int i = 0; i < receiveFormDataResult.getRecordFormArrayList().size(); i++) {
				Log.i("blebleRecordData", "RecordFormNum:" + recordForms.get(i).getRecordFormNum());
				Log.i("blebleRecordData", "RecordFormDate:" + recordForms.get(i).getRecordFormDate());
				Log.i("blebleRecordData", "RecordFormTime:" + recordForms.get(i).getRecordFormTime());
				Log.i("blebleRecordData", "RecordFormDeviceNum:" + recordForms.get(i).getRecordFormDeviceNum());//device model
				Log.i("blebleRecordData", "RecordFormSerialNum:" + recordForms.get(i).getRecordFormSerialNum());//serial number
				Log.i("blebleRecordData", "RecordFormMeasureMode:" + recordForms.get(i).getRecordFormMeasureMode());
				Log.i("blebleRecordData", "RecordFormMeasureNum:" + recordForms.get(i).getRecordFormMeasureNum());
				Log.i("blebleRecordData", "RecordFormCarNum:" + recordForms.get(i).getRecordFormCarNum());
				Log.i("blebleRecordData", "RecordFormPoliceNum:" + recordForms.get(i).getRecordFormPoliceNum());
				Log.i("blebleRecordData", "RecordFormCarLicense:" + recordForms.get(i).getRecordFormCarLicense());
				Log.i("blebleRecordData", "RecordFormEditor:" + recordForms.get(i).getRecordFormEditor());
				Log.i("blebleRecordData", "RecordFormAddress:" + recordForms.get(i).getRecordFormAddress());
			}
			if (EventManger.getInstance().isInitFinsh){

			}
		}
	}


	private void LoadUIComponents() {
		// TODO Auto-generated method stub
		btn_ps_name = (Button) findViewById(R.id.btnpsname_settings_xml);
		btn_pointby_ps_name = (Button) findViewById(R.id.btnpointby_psname_settings_xml);
		btn_ResponsiblePSName=(Button)findViewById(R.id.btn_responsiblePS_settings_xml);

		et_exact_location = (EditText) findViewById(R.id.edt_exctlocation_settings_xml);
		et_analyser = (EditText) findViewById(R.id.edt_breatheanalyser_settings_xml);

		lv_bt_items = (ListView) findViewById(R.id.listview_devicesfound);
		btn_scan_bluetooth = (Button) findViewById(R.id.btnscan_settings_xml);
		btnscan_BreathAnalyser=findViewById(R.id.btnscan_BreathAnalyser);
		tv_stateBluetooth = (TextView) findViewById(R.id.tv_bluetoothState);
		et_bt_address = (EditText) findViewById(R.id.edt_bluetoothid_settings_xml);
		et_web_url = (EditText) findViewById(R.id.edt_weburl_settings_xml);

		btn_cancel = (Button) findViewById(R.id.btncancel_settings_xml);
		btn_save = (Button) findViewById(R.id.btnsubmit_settings_xml);
		btn_back = (Button) findViewById(R.id.btnback_settings_xml);

		change_password = (ImageView) findViewById(R.id.change_pswd);

		et_pinpad = (EditText) findViewById(R.id.edt_pinpad_xml);
		btn_pinpadscan_xml = (Button) findViewById(R.id.btn_pinpadscan_xml);

		update_apk = (ImageView) findViewById(R.id.update_apk);
//		if (null != MainActivity.services_url && MainActivity.services_url.equals("https://www.echallan.org/eTicketMobileHyd")) {
//			update_apk.setEnabled(false);
//			update_apk.setClickable(false);
//		}else{
//			update_apk.setClickable(true);
//			update_apk.setEnabled(true);
//
//		}

		ffd = (TextView) findViewById(R.id.ffd);



		btn_ps_name.setOnClickListener(this);
		btn_ResponsiblePSName.setOnClickListener(this);
		// update_apk.setOnClickListener(this);
		btn_pointby_ps_name.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		btn_scan_bluetooth.setOnClickListener(this);
		btn_pinpadscan_xml.setOnClickListener(this);
		btnscan_BreathAnalyser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				progressDialog
						.setMessage("Please wait \n BlueTooth Scan is in Process!!!");
				progressDialog.setCancelable(false);
				progressDialog.show();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						startScan();
						initView();

					}
				});
			}
		});
		EventBus.getDefault().register(this);
		initBle();

		EventManger.getInstance().receieDataCallback(this);
		EventManger.getInstance().repeatCallback(this);
		EventManger.getInstance().searchDataCallback(this, 10006);
		callBack = new DataSource.DataCallBack<String>() {
			@Override
			public void onDataNotAvailed(int i) {

			}

			@Override
			public void onDataLoaded(String s) {
				Log.i("bleble", s);
				if (s.equals("1")){
					Toast.makeText(Settings_New.this, "initDeviceInfo", Toast.LENGTH_LONG).show();
					//初始化设备成功之后需要去初始化一些设备信息
					//After successfully initializing the device, you need to initialize some device information

					//获取仪器用到的字符显示配置信息 连接成功callback返回3
					//Get configure information of font show on the device.if connection is ok,the return of callback is 3
					Log.i("bleble0", "13");
					EventManger.getInstance().deviceDisplayConfiger(callBack);

				}else if (s.equals("3")){
					//获取仪器信息 连接成功callback返回4
					// Get device information,if connection is ok,the return of callback is 4
					Log.i("bleble0", "14");
					EventManger.getInstance().fetchDeviceInfo(callBack);
				}else if (s.equals("4")){
					//获取仪器记录信息列表头 callback返回5
					// Get the header of the record list,if connection is ok,the return of callback is 5
					Log.i("bleble0", "15");
					EventManger.getInstance().recordFormDisplay(callBack);

					//现在你可以调用comandForRecordForm()方法来获取记录了
					//Now you can call the comandForRecordForm ()  to get the record
				}else if (s.equals("5")){
					//第一次初始化连接获取最新记录
					//Initialize the connection for the first time to get the latest record


					if (Integer.valueOf(EventManger.getInstance().getMaxStartNum()) > 0) {
						EventManger.getInstance().comandForRecordForm(EventManger.getInstance().getMaxStartNum()
								, EventManger.getInstance().getMaxStartNum(), EventManger.getInstance().TAG_INSTANT_RECEIVE);
					}
				}

			}
		};

		change_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				SharedPreferences sharedPreference = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				contact_no = sharedPreference.getString("OFF_PHONE_NO",
						"");
				if (contact_no != null && contact_no.trim().length() == 10) {
					new Async_sendOTP_to_mobile().execute();
				} else {
					showToast("Please Update your Contact number \n"
							+ "to change your password\n"
							+ "Contact eChallan Admin(040-27852721)");
				}
			}
		});

		update_apk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Async_UpdateApk().execute();
			}
		});

		pointNameBy_PsName_code_arr = new ArrayList<String>();// point code
		pointNameBy_PsName_arr = new ArrayList<String>();// point name
	}

	public class Async_sendOTP_to_mobile extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SharedPreferences sharedPreference = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			String pid_code = sharedPreference.getString("PID_CODE", "");
			String security_code = sharedPreference.getString("PASS_WORD", "");

			String contact_no = sharedPreference.getString("OFF_PHONE_NO", "");

			ServiceHelper.getChange_PWDotp("" + pid_code, "" + security_code,
					"" + contact_no);
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			Intent i = new Intent(Settings_New.this, ChangePassword.class);
			i.putExtra("CntNo",""+ contact_no);
			startActivity(i);
		}
	}

	public class Async_VerifyOTP extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);

		}
	}

	/* BLUETOOTH CONNECTIVITY */
	private void CheckBlueToothState() {
		// TODO Auto-generated method stub
		if (bluetoothAdapter == null) {
			tv_stateBluetooth.setText("Bluetooth NOT support");
		} else {
			if (bluetoothAdapter.isEnabled()) {
				if (bluetoothAdapter.isDiscovering()) {
					tv_stateBluetooth
							.setText("Bluetooth is currently in device discovery process.");
				} else {
					tv_stateBluetooth.setText("Bluetooth is Enabled.");
					btn_scan_bluetooth.setEnabled(true);
				}
			} else {
				tv_stateBluetooth.setText("Bluetooth is NOT Enabled!");
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	/* BLUETOOTH CONNECTIVITY */
	private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				btArrayAdapter.add(device.getAddress() + "\n"
						+ device.getName());
				btArrayAdapter.notifyDataSetChanged();

				BTprinter_Adress = device.getAddress();
				BTprinter_Name = device.getName();
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_ENABLE_BT) {
			CheckBlueToothState();
		}
	}

	public Boolean isOnline() {
		ConnectivityManager conManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
		return nwInfo != null;
	}

	@SuppressWarnings("deprecation")
    @SuppressLint({"CommitPrefEdits", "WorldReadableFiles", "SetTextI18n"})
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		/* FIRST BUTTON DIALOG */
			case R.id.btnpsname_settings_xml:
				showDialog(PS_NAME_DIALOG);
				break;
			case R.id.btn_responsiblePS_settings_xml:
				showDialog(PS_RESPONSIBLE_NAME_DIALOG);
				break;

			case R.id.btnpointby_psname_settings_xml:

			/* SECOND BUTTON DIALOG */
				if (btn_ps_name
						.getText()
						.toString()
						.equals(""
								+ getResources().getString(R.string.select_ps_name))) {
					showToast("Select PS Name");
				} else {
					Log.i("point by", ""
							+ pointNameBy_PsName_arr.size());
					showDialog(PS_CODE_DIALOG);
				}
				break;

			case R.id.btncancel_settings_xml:
				dashboard.preferences.edit().clear().apply();
				btn_ps_name.setText(""+ getResources().getString(R.string.select_ps_name));
				btn_pointby_ps_name.setText(""+ getResources().getString(R.string.select_pointbypsname));
				btn_ResponsiblePSName.setText("Responsible PS Name");
				et_exact_location.setText("");
				et_analyser.setText("");
				et_bt_address.setText("");
				et_pinpad.setText("");
				selected_ps_name = -1;
				selected_pointby_psname = -1;
				pointNameBy_PsName_code_arr.clear();
				pointNameBy_PsName_arr.clear();
				break;

			case R.id.btnback_settings_xml:

				SharedPreferences shared_pc = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());

				String cadre_code = shared_pc.getString("CADRE_CODE", "");

				if(cadre_code!=null && Integer.parseInt(cadre_code)<=13){
					startActivity(new Intent(getApplicationContext(), Dashboard.class));
				} else {
					startActivity(new Intent(getApplicationContext(), Dashboard_PC.class));
				}
				break;

			case R.id.btnsubmit_settings_xml:
				if (btn_ps_name.getText().toString().trim().equals(""+ getResources().getString(R.string.select_ps_name))) {
					showToast("Select PS Name");
				} else if (btn_pointby_ps_name.getText().toString().trim().equals(""+ getResources().getString(R.string.select_pointbypsname))) {
					showToast("Select Point Name");
				} else if (btn_ResponsiblePSName.getText().toString().trim().equals("Responsible PS Name")) {
					showToast("Select Responsible PS name!");
				}else if ((et_bt_address.getText().toString().trim().equals(""))
						&& (et_bt_address.getText().toString().trim().length() < 10)) {
					// et_bt_address.setError("Check Bluetooth Details Properly");
					et_bt_address.setError(Html.fromHtml("<font color='black'>Check Bluetooth Details Properly</font>"));
					et_bt_address.requestFocus();
				}
				else {
					dashboard.preferences = getSharedPreferences("preferences",
							Context.MODE_PRIVATE);
					dashboard.editor = dashboard.preferences.edit();

				//Changed to edt TExt to BTn
					if (btn_ResponsiblePSName.getText().toString().trim().equals("")) {
						dashboard.editor.putString("exact_location", "");
					} else {
						dashboard.editor.putString("exact_location",
								btn_ResponsiblePSName.getText().toString().trim());
					}

				/* ANALYSER ID PREF VALUES */
					if (et_analyser.getText().toString().trim().equals("")) {
						dashboard.editor.putLong("analyser_id", 0);
					} else {
						dashboard.editor.putLong("analyser_id",Integer.parseInt(et_analyser.getText().toString().trim()));
					}

					if (et_bt_address.getText().toString().trim().length()<15) {
						showToast("Please Pair Valid Bluetooth Device");
					}else{
						dashboard.editor.putString("btaddress", ""+ et_bt_address.getText().toString().trim());
						dashboard.editor.commit();

						showToast("Details Saved Successfully");
						finish();
					}

					SharedPreferences case_Vals = getSharedPreferences("PS_POINT_NAMES", MODE_PRIVATE);
					SharedPreferences.Editor edit = case_Vals.edit() ;

					edit.putString("BOOKED_PS", ""+btn_ps_name.getText().toString().trim());
					edit.putString("BOOKED_POINT", ""+btn_pointby_ps_name.getText().toString().trim());
					edit.putString("BOOKED_RESPONSIBLE_Ps_NAME", ""+btn_ResponsiblePSName.getText().toString().trim());


					edit.commit();

					// dashboard.editor.putString("weburl", ""
					// + et_web_url.getText().toString().trim());



				}
				break;

			case R.id.btnscan_settings_xml:

				bluetoothFLG = true;
				pinpadFLG = false;

				ffd.setVisibility(View.VISIBLE);
				final ProgressDialog progressDialog = new ProgressDialog(
						Settings_New.this);
				progressDialog
						.setMessage("Please wait \n BlueTooth Scan is in Process!!!");
				progressDialog.setCancelable(false);
				progressDialog.show();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						btArrayAdapter.clear();
						bluetoothAdapter.cancelDiscovery();
						bluetoothAdapter.startDiscovery();
					}
				});

				new Thread() {
					@Override
					public void run() {
						super.run();
						try {
							Thread.sleep(5000);
							if (progressDialog.isShowing())
								progressDialog.dismiss();

						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
				break;

			case R.id.btn_pinpadscan_xml:
				pinpadFLG = true;
				bluetoothFLG = false;

				ffd.setVisibility(View.VISIBLE);
				final ProgressDialog progressDialog2 = new ProgressDialog(
						Settings_New.this);
				progressDialog2
						.setMessage("Please wait BlueTooth Scan is in Process!!!");
				progressDialog2.setCancelable(false);
				progressDialog2.show();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						btArrayAdapter.clear();
						bluetoothAdapter.cancelDiscovery();
						bluetoothAdapter.startDiscovery();
					}
				});

				new Thread() {
					@Override
					public void run() {
						super.run();
						try {
							Thread.sleep(6000);
							if (progressDialog2.isShowing())
								progressDialog2.dismiss();

						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
				break;

			default:
				break;
		}
	}

	@SuppressWarnings("deprecation")
	private void showProgress(String server) {

		dialog = new Dialog(Settings_New.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.myprogressdialog);
		dialog.setTitle("Download Progress");
		dialog.setCancelable(false);

		TextView text = (TextView) dialog.findViewById(R.id.tv1);
		text.setText("Downloading file ... ");
		cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
		cur_val.setText("It may Take Few Minutes.....");
		dialog.show();

		progress = (ProgressBar) dialog.findViewById(R.id.progress_bar);
		progress.setProgress(0);
		progress.setMax(100);
		progress.setIndeterminate(true);
		progress.setProgressDrawable(getResources().getDrawable(
				R.drawable.green_progress));
	}

	class Async_UpdateApk extends AsyncTask<Void, Void, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);

		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			FTPClient ftpClient = new FTPClient();

			try {

				if (null != MainActivity.service_type&& MainActivity.service_type.contains("live")) {
					server = IPSettings.open_ftp_fix;
				}else{
					server = IPSettings.open_ftp_fix;
				}
				Log.i("server URL ::", ""+server);

				ftpClient.connect(server, port);
				ftpClient.login(username, password);
				ftpClient.enterLocalPassiveMode();
				ftpClient.setBufferSize(1024 * 1024);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

				// ftp://192.168.11.9:99/23/TabAPK/Version-1.5.1.txt
				File downloadFile1 = new File("/sdcard/Download/ETicketHYD.apk");
				//String remoteFile1 = "/23/TabAPK" + "/" + version;
				String remoteFile1 = "/23/TabAPK" + "/ETicketHYD.apk";
				OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile1));
				boolean success = ftpClient.retrieveFile(remoteFile1, outputStream);
				FileOutputStream fileOutput = new FileOutputStream(downloadFile1);
				InputStream inputStream = ftpClient.retrieveFileStream(remoteFile1);

				if (inputStream == null || ftpClient.getReplyCode() == 550) {
					// it means that file doesn't exist.
					fileOutput.close();
					outputStream.close();

					runOnUiThread(new Runnable() {

						@SuppressWarnings("deprecation")
						@Override
						public void run() {
							// TODO Auto-generated method stub
							removeDialog(PROGRESS_DIALOG);

							// showToast("your is Upto Date");
							TextView title = new TextView(Settings_New.this);
							title.setText("Hyderabad E-Ticket");
							title.setBackgroundColor(Color.RED);
							title.setGravity(Gravity.CENTER);
							title.setTextColor(Color.WHITE);
							title.setTextSize(26);
							title.setTypeface(title.getTypeface(), Typeface.BOLD);
							title.setCompoundDrawablesWithIntrinsicBounds(
									R.drawable.dialog_logo, 0,
									R.drawable.dialog_logo, 0);
							title.setPadding(20, 0, 20, 0);
							title.setHeight(70);

							String otp_message = "\n Your Application is Upto Date \n No Need to Update \n";

							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									Settings_New.this, AlertDialog.THEME_HOLO_LIGHT);
							alertDialogBuilder.setCustomTitle(title);
							alertDialogBuilder.setIcon(R.drawable.dialog_logo);
							alertDialogBuilder.setMessage(otp_message);
							alertDialogBuilder.setCancelable(false);
							alertDialogBuilder.setPositiveButton("Ok",
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
						}
					});
				}
				else {
					try {
						Log.i("SUCCess LOG 1::::::::", "***********ENTERED*******");
						SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
						db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.psName_table);
						db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.wheelercode_table);
						db2.execSQL(DBHelper.psNamesCreation);
						db2.execSQL(DBHelper.wheelerCodeCreation);
						db2.close();

						SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
						SharedPreferences.Editor editor = preferences.edit();
						editor.clear();
						editor.commit();

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					totalSize = remoteFile1.length();

					runOnUiThread(new Runnable() {
						@SuppressWarnings("deprecation")
						public void run() {
							removeDialog(PROGRESS_DIALOG);
							showProgress(server);
							progress.setMax(totalSize);
						}
					});

					// create a buffer...
					byte[] buffer = new byte[1024];
					int bufferLength = 0;

					while ((bufferLength = inputStream.read(buffer)) > 0) {
						fileOutput.write(buffer, 0, bufferLength);
						downloadedSize += bufferLength;
						// update the progressbar //
						runOnUiThread(new Runnable() {
							@SuppressLint("SetTextI18n")
							public void run() {
								progress.setProgress(downloadedSize);
								float per = ((float) downloadedSize / totalSize) * 100;
								cur_val.setText((int) per / 460000 + "%");
							}
						});
					}

					// close the output stream when complete //
					fileOutput.close();
					outputStream.close();

					if (success) {
						ftpClient.logout();
						ftpClient.disconnect();

						try {

							//olc concept like this
							Log.i("SUCCess LOG ::::::::", "***********ENTERED*******");
							db.open();
							db.execSQL("delete from " + DBHelper.psName_table);
							db.execSQL("delete from " + DBHelper.wheeler_code);

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							Log.i("CATCH BLOG ::::::::", "***********ENTERED*******");
							db.close();
						}

						finish();


						if (Build.VERSION.SDK_INT <= 23) {

							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(
									Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "ETicketHYD.apk")),
									"application/vnd.android.package-archive");
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}

						else {

							Uri apkUri = FileProvider.getUriForFile(Settings_New.this, BuildConfig.APPLICATION_ID +
									".provider", new File(Environment.getExternalStorageDirectory() + "/download/" + "ETicketHYD.apk"));
							Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
							intent.setData(apkUri);
							intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
							startActivity(intent);
						}

					}
				}

			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                removeDialog(PROGRESS_DIALOG);
                showToast("Please contact e-Challan team !");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                removeDialog(PROGRESS_DIALOG);
                showToast("Please contact e-Challan team !");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                removeDialog(PROGRESS_DIALOG);
                showToast("Please contact e-Challan team !");
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// removeDialog(PROGRESS_DIALOG);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "NewApi", "WorldReadableFiles" })
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {

			case PS_NAME_DIALOG:
				TextView title = new TextView(this);
				title.setText("Select PS Name");
				title.setBackgroundColor(Color.parseColor("#007300"));
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.WHITE);
				title.setTextSize(26);
				title.setTypeface(title.getTypeface(), Typeface.BOLD);
				title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
				title.setPadding(20, 0, 20, 0);
				title.setHeight(70);

				AlertDialog.Builder ad_ps_name = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
				ad_ps_name.setCustomTitle(title);
				ad_ps_name.setSingleChoiceItems(psname_name, selected_ps_name,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								selected_ps_name = which;
								btn_ps_name.setText(""+ psname_name[which].toString().trim());
								ps_code_pos = which;

								dashboard.preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
								dashboard.editor = dashboard.preferences.edit();
								dashboard.editor.putInt("psname_code_toSet", which);
								dashboard.editor.putString("psname_code", psname_code[which].toString());
								dashboard.editor.putString("psname_name", psname_name[which].toString());
								dashboard.editor.commit();
								removeDialog(PS_NAME_DIALOG);

								// if ((pointNameBy_PsName_code_arr.size() > 0)
								// && (pointNameBy_PsName_arr.size() > 0)) {
								// pointNameBy_PsName_code_arr.clear();
								// pointNameBy_PsName_arr.clear();
								// }

								if (isOnline()) {
									selected_pointby_psname = -1;
									dashboard.preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
									dashboard.editor = dashboard.preferences.edit();
									dashboard.editor.putInt("point_code_toSet", selected_pointby_psname);
									dashboard.editor.commit();
									btn_pointby_ps_name.setText(""+ getResources().getString(R.string.select_pointbypsname));
									new Async_getPointNameByPsName().execute();

								} else {
									showToast("Please check your network connection!");
								}
							}
						});
				Dialog dg_ps_name = ad_ps_name.create();
				return dg_ps_name;


			case PS_RESPONSIBLE_NAME_DIALOG:
				TextView title_responsiblePS = new TextView(this);
				title_responsiblePS.setText("Select RESPONSIBLE PS Name");
				title_responsiblePS.setBackgroundColor(Color.parseColor("#007300"));
				title_responsiblePS.setGravity(Gravity.CENTER);
				title_responsiblePS.setTextColor(Color.WHITE);
				title_responsiblePS.setTextSize(26);
				title_responsiblePS.setTypeface(title_responsiblePS.getTypeface(), Typeface.BOLD);
				title_responsiblePS.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
				title_responsiblePS.setPadding(20, 0, 20, 0);
				title_responsiblePS.setHeight(70);

				AlertDialog.Builder ad_responsible_ps_name = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
				ad_responsible_ps_name.setCustomTitle(title_responsiblePS);
				ad_responsible_ps_name.setSingleChoiceItems(psname_name, selected_responsible_ps_name,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								selected_responsible_ps_name = which;
								btn_ResponsiblePSName.setText(""+ psname_name[which].toString().trim());
								ps_responsible_Code = which;

								dashboard.preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
								dashboard.editor = dashboard.preferences.edit();
								dashboard.editor.putInt("ps_res_name_code_toSet", which);
								dashboard.editor.putString("ps_res_name_code", psname_code[which].toString());
								dashboard.editor.putString("ps_res_name_name", psname_name[which].toString());
								dashboard.editor.commit();
								removeDialog(PS_RESPONSIBLE_NAME_DIALOG);

								// if ((pointNameBy_PsName_code_arr.size() > 0)
								// && (pointNameBy_PsName_arr.size() > 0)) {
								// pointNameBy_PsName_code_arr.clear();
								// pointNameBy_PsName_arr.clear();
								// }


							}
						});
				Dialog dg_res_ps_name = ad_responsible_ps_name.create();
				return dg_res_ps_name;

			case PS_CODE_DIALOG:
				TextView title2 = new TextView(this);
				title2.setText("Select PS By Point Name");
				title2.setBackgroundColor(Color.parseColor("#007300"));
				title2.setGravity(Gravity.CENTER);
				title2.setTextColor(Color.WHITE);
				title2.setTextSize(26);
				title2.setTypeface(title2.getTypeface(), Typeface.BOLD);
				title2.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
				title2.setPadding(20, 0, 20, 0);
				title2.setHeight(70);

				AlertDialog.Builder ad_ps_code = new AlertDialog.Builder(this,
						AlertDialog.THEME_HOLO_LIGHT);
				ad_ps_code.setCustomTitle(title2);
				ad_ps_code.setSingleChoiceItems((pointNameBy_PsName_arr.toArray(new String[pointNameBy_PsName_arr.size()])),
						selected_pointby_psname,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								selected_pointby_psname = which;
								btn_pointby_ps_name.setText(""+ pointNameBy_PsName_arr.get(which).toString().trim());
								dashboard.preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
								dashboard.editor = dashboard.preferences.edit();

								dashboard.editor.putInt("point_code_toSet", which);
								dashboard.editor.putString("point_code", pointNameBy_PsName_code_arr.get(which).toString().trim());// for sending
								// to service
								dashboard.editor.putString("point_name", pointNameBy_PsName_arr.get(which));
								dashboard.editor.commit();
								removeDialog(PS_CODE_DIALOG);
							}
						});
				Dialog dg_ps_code = ad_ps_code.create();
				return dg_ps_code;
			// for (int i = 0; i < pointNameBy_PsName_arr.size(); i++) {
			// showToast("" + pointNameBy_PsName_arr.get(i));
			// }
			// break;

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

	/* TO GET POINT NAME BY PS-NAME */
	@SuppressLint("StaticFieldLeak")
	private class Async_getPointNameByPsName extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ServiceHelper.getPointNameByPsNames(""+ psname_code[selected_ps_name].toString().trim());
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);

			if (ServiceHelper.PointNamesBypsNames_master.length > 0) {

				try {

					pointNameBYpsname_name_code_arr = new String[0][0];
					pointNameBYpsname_name_code_arr = new String[ServiceHelper.PointNamesBypsNames_master.length][2];

					for (int i = 1; i < ServiceHelper.PointNamesBypsNames_master.length; i++) {
						pointNameBYpsname_name_code_arr[i] = ServiceHelper.PointNamesBypsNames_master[i].toString().trim().split("@");
					}

					pointNameBy_PsName_code_arr.clear();
					pointNameBy_PsName_arr.clear();

					for (int j = 1; j < pointNameBYpsname_name_code_arr.length; j++) {
						try {
							pointNameBy_PsName_code_arr.add(pointNameBYpsname_name_code_arr[j][0]);
							pointNameBy_PsName_arr.add(pointNameBYpsname_name_code_arr[j][1]);
						}catch (Exception e)
						{
							e.printStackTrace();
						}
					}
					Log.i("**PS NAMES**", ""+ pointNameBy_PsName_arr.size());
					btn_pointby_ps_name.setClickable(true);

				}catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			/*------------TO CLEAR POINT CODE OF SECOND BUTTON-----------*/

			// showDialog(PS_NAME_DIALOG);
		}
	}

	public void Alertmessage() {

		if (mBluetoothAdapter == null) {
			showToast("Bluetooth is not available");
			return;
		}
		if (!mBluetoothAdapter.isEnabled()) {
			showToast("Please enable your BT and re-run this program");
			finish();
			return;
		}
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

	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		System.runFinalizersOnExit(true);
		// System.exit(0);
		super.onDestroy();
		unregisterReceiver(ActionFoundReceiver);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		// showToast("Please Click on Back Button to go Back");
	}
}
