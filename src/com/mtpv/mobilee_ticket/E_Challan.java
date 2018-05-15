package com.mtpv.mobilee_ticket;

import it.sauronsoftware.ftp4j.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.ServiceHelper;
import com.mtpv.mobilee_ticket_services.Utils;

public class E_Challan extends Activity implements OnClickListener, LocationListener {
	
	public static final int PROGRESS_DIALOG = 0;
	final int WHEELER_CODE = 1;
	final int VIOLATIONS_DIALOG = 2;
	final int DYNAMIC_VIOLATIONS = 5;

	ImageView imgv_camera, imgv_gallery;
	WebView webView_image ;
	EditText edt_regncid, edt_regncidname, edt_regncid_lastnum ;
	Button btngetrtadetails, btn_whlr_code, btn_violation, btn_cancel,
			btn_submit;
	
	/* RTA COMPLETE DETAILS */
	public static TextView tv_vehicle_details_header_spot;
	public static TextView tv_vhle_no_spot;
	public static TextView tv_owner_name_spot;
	public static TextView tv_address_spot;
	public static TextView tv_city_spot;
	public static TextView tv_maker_name_spot;
	public static TextView tv_maker_class_spot;
	public static TextView tv_color_spot;
	public static TextView tv_engine_no_spot;
	public static TextView tv_chasis_no_spot;
	public static TextView tv_violation_amnt;

	RelativeLayout rl_detailsresponse_spotchallan_xml ;
	
	/* PENDING CHALLANS DETAILS */
	public static TextView tv_total_pending_challans;
	public static TextView tv_toal_amount_pending_challans;
	public static TextView selected_pendingamnt_spotchallan;
	public static TextView tv_grand_total_spot;
	
	String NETWORK_TXT = "";
	String imei_send = "";
	String simid_send = "";
	String macAddress = "";
	String wheler_code = "Select Wheeler";
	String whlr_code_send = "";
	String cam_imag = "";
	
	ArrayList<String> violation_list;// to bind to violation to dialog
	ArrayList<String> violation_description;
	ArrayList<String> violation_section;
	ArrayList<String> violation_offence_Code;
	ArrayList<String> violation_min_amount;
	ArrayList<String> violation_max_amount;
	ArrayList<String> violation_avg_amount;
	ArrayList<String> violation_positions;
	ArrayList<String> violation_rg_ids;
	ArrayList<String> violation_checked_violations;
	// ArrayList<Boolean> violation_checked_items_status;
	LinkedHashMap<String, String> check_map;
	HashMap<String, String> check_all_ids;
	HashMap<String, String> vioCodeDescMap;
	
	StringBuffer violations_details_send;
	StringBuffer violation_desc_append;
	
	String[] wheeler_code_arr_spot, wheeler_name_arr_spot;
	
	int j = 1;// for checked violations

	int selected_wheller_code = -1;
	int selected_violation_list = -1;
	
	public static Double total = 0.0 ;
	
	/* FOR IMAGES FROM GALLER OR CAMERA */
	int RESULT_LOAD_IMAGE = 0;
	private static final int CAMERA_REQUEST = 1888;
	WebviewLoader webviewloader;
	String picturePath = "";
	WebView wv_generate;
	FileOutputStream fo;
	
	/* DETAILS TO PUSH TO SERVICE FOR MOBILE SPOT PAYMENT */
	String imgEvidence = "0";// if image then "1" else "0"
	
	TelephonyManager telephonyManager;
	Utils utils;
	
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	
	LocationManager m_locationlistner;
	android.location.Location location;
	public static double latitude = 0.0;
	public static double longitude = 0.0;
	public static double total_amount = 0;
	public static double grand_total = 0;
	DBHelper db;
	Cursor c_whlr;

	public static String[] rta_details_spot_master;
	String provider = "";
	public static String completeVehicle_num_send = "", regncode_send = "",
			regnName_send = "", vehicle_num_send = "" , fake_veh_chasisNo ="";
	
	int edt_regncid_spotchallanMAX_LENGTH = 4;
	int edt_regncidname_spotchallanLENGTH = 4;
	int edt_regncid_lastnum_spotchallanMAX_LENGTH = 4;
	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	
	String bookedPScode_send_from_settings;
	String bookedPSname_send_from_settings;
	String point_code_send_from_settings;
	String point_name_send_from_settings;
	String exact_location_send_from_settings;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	/* FTP UPLOAD DETAILS */
	FTPClient client;
	String[] FTP_HOST_PORT_SPOT;
	String ftp_host_spot = "";
	
	/* DYNAMIC LAYOUTS START */
	LinearLayout ll_dynamic_violations_root_static;
	LinearLayout[] ll_dynamic_vltns;
	Spinner[] spinner_violation;
	// String[] spinner_selectors;
	TextView[] tv_dynamic_vltn_name;
	CheckBox[] check_dynamic_vltn;
	RadioGroup[] rg_dynamic_vltn;
	RadioButton[] rbtn_dynamic_vltn;
	/* DYNAMIC LAYOUTS END */
	
	public static CheckBox check ;
	
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_e__challan);
		
		violation_list = new ArrayList<String>();
		violation_description = new ArrayList<String>();
		violation_section = new ArrayList<String>();
		violation_positions = new ArrayList<String>();
		violation_max_amount = new ArrayList<String>();
		violation_avg_amount = new ArrayList<String>();
		violation_min_amount = new ArrayList<String>();
		violation_offence_Code = new ArrayList<String>();
		violation_checked_violations = new ArrayList<String>();
		violation_rg_ids = new ArrayList<String>();
		
		
		db = new DBHelper(this);

		/* TO GET WHEELER CODE DETAILS */
		try {
			db.open();
			// WHEELER CODE
			c_whlr = DBHelper.db.rawQuery("select * from "
					+ DBHelper.wheelercode_table, null);
			if (c_whlr.getCount() == 0) {
				Log.i("WHEELER DB DETAILS", "0");
			} else {

				wheeler_code_arr_spot = new String[c_whlr.getCount()];
				wheeler_name_arr_spot = new String[c_whlr.getCount()];

				int count = 0;
				while (c_whlr.moveToNext()) {
					wheeler_code_arr_spot[count] = c_whlr.getString(1);
					wheeler_name_arr_spot[count] = c_whlr.getString(2);
					count++;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			c_whlr.close();
			db.close();
		}
		c_whlr.close();
		db.close();

		/* PREFERENCES */
		preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
		editor = preferences.edit();
		/* FOR CHECKING FTP DETAILS */
		ftp_host_spot = preferences.getString("ftpurl", "host");
		if (!ftp_host_spot.equals("host")) {
			FTP_HOST_PORT_SPOT = ftp_host_spot.split("\\:");
			Log.i("DYNAMIC FTP DETAILS", "" + FTP_HOST_PORT_SPOT[0]
					+ "\nPort : " + FTP_HOST_PORT_SPOT[1]);
		}
		
		/* PENDING CHALLANS */
		tv_total_pending_challans = (TextView) findViewById(R.id.tv_pendingchallans_total_spotchallan_xml);
		tv_toal_amount_pending_challans = (TextView) findViewById(R.id.tv_pendingamount_spotchallan_xml);
		tv_grand_total_spot = (TextView) findViewById(R.id.tv_grand_totalamnt_spotchallan_xml);
		tv_grand_total_spot.setText("Rs . " + total_amount);
		
		/* CAMERA & GALLERY */
		imgv_camera = (ImageButton) findViewById(R.id.imgv_camera_capture_spotchallan_xml);
		imgv_gallery = (ImageButton) findViewById(R.id.imgv_gallery_spotchallan_xml);

		wv_generate = (WebView) findViewById(R.id.webView_image_spotchallan_xml);
		
		edt_regncid = (EditText) findViewById(R.id.edt_regncid_spotchallan_xml);
		edt_regncidname = (EditText) findViewById(R.id.edt_regncidname_spotchallan_xml);
		edt_regncid_lastnum = (EditText) findViewById(R.id.edt_regncid_lastnum_spotchallan_xml);
		
		edt_regncid.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				 if (edt_regncid.getText().toString().length() == edt_regncid_spotchallanMAX_LENGTH) { 
					 edt_regncidname.requestFocus(); 
	             }
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
			@Override
			public void afterTextChanged(Editable s) {  }
		});
		edt_regncidname.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				 if (edt_regncidname.getText().toString().length() == edt_regncidname_spotchallanLENGTH) { 
					 edt_regncid_lastnum.requestFocus(); 
	             }
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {	}
			@Override
			public void afterTextChanged(Editable s) {}
		});
		edt_regncid_lastnum.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				 if (edt_regncid_lastnum.getText().toString().length() == edt_regncid_lastnum_spotchallanMAX_LENGTH) { 
	             }
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) { }
			@Override
			public void afterTextChanged(Editable s) { }
		});

		rl_detailsresponse_spotchallan_xml = (RelativeLayout)findViewById(R.id.rl_detailsresponse_spotchallan_xml);
		rl_detailsresponse_spotchallan_xml.setVisibility(View.GONE);
		
		btngetrtadetails = (Button) findViewById(R.id.btngetrtadetails_spotchallan_xml);
		btn_whlr_code = (Button) findViewById(R.id.btn_whlr_code_spotchallan_xml);
		btn_violation = (Button) findViewById(R.id.btn_violation_spotchallan_xml);
		
		btn_cancel = (Button)findViewById(R.id.btn_cancel_spotchallan_xml);
		btn_submit = (Button)findViewById(R.id.btn_next_spotchallan_xml);
		
		/* TO SHOW vehicel DETAILS AND LICENCE DETAILS FOUND OR NOT */
		tv_vehicle_details_header_spot = (TextView) findViewById(R.id.textView_regdetails_header_spotchallan_xml);

		/* RTA COMPLETE DETAILS */
		tv_vhle_no_spot = (TextView) findViewById(R.id.tvregno_spotchallan_xml);
		tv_owner_name_spot = (TextView) findViewById(R.id.tvownername_spotchallan_xml);
		tv_address_spot = (TextView) findViewById(R.id.tv_addr_spotchallan_xml);
		//tv_city_spot = (TextView) findViewById(R.id.tv_city_spotchallan_xml);
		tv_maker_name_spot = (TextView) findViewById(R.id.tv_makername_spotchallan_xml);
		//tv_maker_class_spot = (TextView) findViewById(R.id.tv_makerclass_spotchallan_xml);
		//tv_color_spot = (TextView) findViewById(R.id.tv_color_spotchallan_xml);
		tv_engine_no_spot = (TextView) findViewById(R.id.tv_engineno_spotchallan_xml);
		tv_chasis_no_spot = (TextView) findViewById(R.id.tv_chasis_spotchallan_xml);
		
		tv_violation_amnt = (TextView) findViewById(R.id.tv_violtaionamnt_spotchallan_xml);
		
		imgv_camera.setOnClickListener(this);
		imgv_gallery.setOnClickListener(this);
		
		btngetrtadetails.setOnClickListener(this);
		btn_whlr_code.setOnClickListener(this);
		btn_violation.setOnClickListener(this);
		
		btn_submit.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		
	}

	@SuppressWarnings({ "deprecation", "unused" })
	@SuppressLint("DefaultLocale")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_whlr_code_spotchallan_xml:
			/*------is_driver/is_owner only for spot_challan : 29-01-2015------*/
			if(edt_regncid.getText().toString().equals("")||
					edt_regncidname.getText().toString().equals("")){
				showToast("Please Enter Proper Vehicle Number");
				
			}else{
				E_Challan.tv_grand_total_spot.setText("");
				grand_total = 0;
				showDialog(WHEELER_CODE);
			}
			break;
		case R.id.btn_violation_spotchallan_xml:
			tv_grand_total_spot.setText("") ;
			VehicleHistoryPendingChallans.total_amount_selected_challans = 0.0;
			if ((!btn_whlr_code.getText().toString().trim().equals("" + getString(R.string.select_wheeler_code)))
					&& (violation_offence_Code.size() > 0)) {

				/* TO CLEAR THE CHECKED & UNC-CHECKED POEISITONS */
				violation_positions.removeAll(violation_positions);
				violation_rg_ids.removeAll(violation_rg_ids);
				violation_checked_violations
						.removeAll(violation_checked_violations);
				grand_total = 0.0;

				/* CHECK MAP INTITALISATION */
				check_map = new LinkedHashMap<String, String>();
				check_all_ids = new HashMap<String, String>();
				check_all_ids.clear();

				vioCodeDescMap = new HashMap<String, String>();
				vioCodeDescMap.clear();

				violations_details_send = new StringBuffer();
				violations_details_send.delete(0, violations_details_send.length());

				/* TO APPEND THE SLECTED VILATIONS TO BUTTON */
				violation_desc_append = new StringBuffer();
				violation_desc_append.delete(0, violation_desc_append.length());

				removeDialog(DYNAMIC_VIOLATIONS);
				showDialog(DYNAMIC_VIOLATIONS);
			} else {
				showToast("Select Wheeler Code");
			}

			break;
			
		case R.id.imgv_camera_capture_spotchallan_xml:
			cam_imag = "";
			cam_imag = "camera";
			if (isDeviceSupportCamera()) {
			/*	Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);*/


				if (Build.VERSION.SDK_INT<=23) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, 1);
				}else{
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(E_Challan.this,
							BuildConfig.APPLICATION_ID + ".fileProvider",f));
					intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					startActivityForResult(intent, 1);
				}

			} else {
				showToast("Sorry! Your device doesn't support camera");
			}

			break;
		case R.id.imgv_gallery_spotchallan_xml:

			cam_imag = "";
			cam_imag = "browse";

			Intent in_gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(in_gallery, RESULT_LOAD_IMAGE);
			break;	
		case R.id.btngetrtadetails_spotchallan_xml :
			
			VehicleHistoryPendingChallans.total_amount_selected_challans = 0.0;
			/*tv_total_pending_challans.setText("0");
			tv_toal_amount_pending_challans.setText("0");
			tv_grand_total_spot.setText("") ;*/
			tv_grand_total_spot.setText("") ;
			tv_violation_amnt.setText("Rs . "+ grand_total+"");
			
			regncode_send = "";// AP09
			regnName_send = "";// CC
			vehicle_num_send = "";// 3014
			
			regncode_send = edt_regncid.getText().toString().trim().toUpperCase();
			regnName_send = ""+ edt_regncidname.getText().toString().toUpperCase();
			vehicle_num_send = edt_regncid_lastnum.getText().toString().trim().toUpperCase();
			completeVehicle_num_send = "";// AP09CC3014
			completeVehicle_num_send = ("" + regncode_send + ""+ regnName_send + "" + vehicle_num_send);
			Log.i("**VEHCILE NUM***", ""+ completeVehicle_num_send);
			
			getLocation(); 
			total_amount = 0;
			VerhoeffCheckDigit ver=new VerhoeffCheckDigit();
			
			btn_whlr_code.setText(getString(R.string.select_wheeler_code));
			btn_violation.setText(getString(R.string.select_violation));
			
			
			if(edt_regncid.getText().toString().equals("") ||
				edt_regncid_lastnum.getText().toString().equals("")){
				showToast("Please Enter Proper Vehicle No");
			}else if (isOnline()) {
				Dashboard.rta_details_request_from = "ECHALLAN";
				new Async_getRTADetails().execute();
			}else {
				showToast("" + NETWORK_TXT);
			}
		
			break ;
		case R.id.btn_cancel_spotchallan_xml:
			startActivity(new Intent(E_Challan.this, Dashboard.class));
			this.finish();
			break;	
		case R.id.btn_next_spotchallan_xml:
			bookedPScode_send_from_settings = preferences.getString("psname_code", "0");
			bookedPSname_send_from_settings = preferences.getString("psname_name", "psname");

			point_code_send_from_settings = preferences.getString("point_code", "0");
			point_name_send_from_settings = preferences.getString("point_name", "pointname");

			exact_location_send_from_settings = preferences.getString("exact_location", "location");

			Log.i("***FROM THE CLASS DETAILS ARE : **", ""+ Dashboard.check_vhleHistory_or_Spot);

			if (edt_regncid.getText().toString().trim().equals("") ||
				edt_regncid_lastnum.getText().toString().trim().equals("")){
				showToast("Please Enter Proper Vehicle No");
			}else if(btn_whlr_code.getText().toString().trim().equals("" + getString(R.string.select_wheeler_code))) {
				showToast("Please Select Wheeler Code");
			}else if(btn_violation.getText().toString().trim().equals("" + getString(R.string.select_violation))) {
				showToast("Please Select Violation");
			}else if (cam_imag.equals("")) {
				showToast("Please Select Image");
			}else{
				new Async_e_challan().execute();
			}
			break ;
		default:
			break;
		}
	}
	
	/* TO GET RTA DETAILS */
	public class Async_getRTADetails extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			completeVehicle_num_send = (regncode_send + regnName_send + vehicle_num_send);
			ServiceHelper.getRTADetails(""+ completeVehicle_num_send);
		
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
			tv_vehicle_details_header_spot.setVisibility(View.VISIBLE);
			
			if ((!ServiceHelper.Opdata_Chalana.equals("0"))
					&& (rta_details_spot_master.length > 1)) {

				rl_detailsresponse_spotchallan_xml.setVisibility(View.VISIBLE);

				Log.i("REgNO ::::", ""+rta_details_spot_master[0]);
				
				
				tv_vhle_no_spot.setText("" + rta_details_spot_master[0]);
				tv_owner_name_spot.setText("" + rta_details_spot_master[1]);
				tv_address_spot.setText("" + rta_details_spot_master[2]);
				tv_city_spot.setText("" + rta_details_spot_master[3]);
				tv_maker_name_spot.setText("" + rta_details_spot_master[4]);
				tv_maker_class_spot.setText("" + rta_details_spot_master[5]);
				tv_color_spot.setText("" + rta_details_spot_master[6]);
				tv_engine_no_spot.setText("" + rta_details_spot_master[7]);
				tv_chasis_no_spot.setText("" + rta_details_spot_master[8]);
				tv_vehicle_details_header_spot.setText("VEHICLE DETAILS");
				
				//********************Dynamic Wheeler code Assignment***************************
				Log.i("Response wheeler :::", ""+rta_details_spot_master[0]);
				if(rta_details_spot_master[0].equals("NA")){
						tv_vehicle_details_header_spot.setText("VEHICLE DETAILS NOT FOUND!");
						rl_detailsresponse_spotchallan_xml.setVisibility(View.GONE);
								
					}else if(rta_details_spot_master!=null && rta_details_spot_master[0].split("\\:")[1]!=null && !"NA".equals(rta_details_spot_master[0].split("\\:")[1].trim())	 ){
					
				//if(rta_details_master!=null && rta_details_master[0].split("\\:")[1]!=null &&!"NA".equalsIgnoreCase(rta_details_master[0].split("\\:")[1])){		
						whlr_code_send=rta_details_spot_master[0].split("\\:")[1];
						Log.i("whlr_code_send DYNAMIC::::", whlr_code_send);
						if(whlr_code_send!=null){
							btn_whlr_code.setText(""+whlr_code_send);
							new Async_getViolations().execute();
						}
							
				}	
			}
		}
	}
	
	/* TO GET VIOLATIONS */
	public class Async_getViolations extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			Log.i("Async_getViolations :::::::", "Called");
			ServiceHelper.getViolationDetails("" + whlr_code_send);
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

			if (ServiceHelper.violation_detailed_views.length > 0) {

				violation_list.removeAll(violation_list);
				violation_offence_Code.removeAll(violation_offence_Code);
				violation_section.removeAll(violation_section);
				violation_description.removeAll(violation_description);
				violation_min_amount.removeAll(violation_min_amount);
				violation_avg_amount.removeAll(violation_avg_amount);
				violation_max_amount.removeAll(violation_max_amount);
				violation_rg_ids.remove(violation_rg_ids);

				for (int i = 0; i < ServiceHelper.violation_detailed_views.length; i++) {
					violation_list.add(""
							+ ServiceHelper.violation_detailed_views[i][2].toString().trim()
							+ " ("
							+ ServiceHelper.violation_detailed_views[i][1].toString().trim()
							+ ") "
							+ " Rs:"
							+ ServiceHelper.violation_detailed_views[i][4].toString().trim());

					violation_offence_Code.add("" + ServiceHelper.violation_detailed_views[i][0].toString().trim());
					violation_section.add("" + ServiceHelper.violation_detailed_views[i][1].toString().trim());
					violation_description.add("" + ServiceHelper.violation_detailed_views[i][2].toString().trim());
					violation_min_amount.add("" + ServiceHelper.violation_detailed_views[i][3].toString().trim());
					violation_max_amount.add("" + ServiceHelper.violation_detailed_views[i][4].toString().trim());
					violation_avg_amount.add("" + ServiceHelper.violation_detailed_views[i][5].toString().trim());
				}
			}
		}
	}
	/* SERVICE CALL TO MOBILE SPOT PAYMENT */
	public class Async_e_challan extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			
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
			removeDialog(PROGRESS_DIALOG);
		}
	}
	@SuppressWarnings("deprecation")
	@SuppressLint({ "ResourceAsColor", "NewApi" })
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case PROGRESS_DIALOG:
			ProgressDialog pd = ProgressDialog.show(this, "", "",	true);
			pd.setContentView(R.layout.custom_progress_dialog);
			pd.setCancelable(false); 
			
			return pd;
		case WHEELER_CODE:
			TextView title = new TextView(this);
			title.setText("Select Wheeler");
			title.setBackgroundColor(Color.parseColor("#007300"));
			title.setGravity(Gravity.CENTER);
			title.setTextColor(Color.WHITE);
			title.setTextSize(26);
			title.setTypeface(title.getTypeface(), Typeface.BOLD);
			title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
			title.setPadding(20, 0, 20, 0);
			title.setHeight(70);
			
			AlertDialog.Builder ad_whle_code_name = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
			ad_whle_code_name.setCustomTitle(title);//
			ad_whle_code_name.setSingleChoiceItems((wheeler_name_arr_spot),
					selected_wheller_code,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							selected_wheller_code = which;
							Log.i("selected_wheller_code ::::", ""+selected_wheller_code);
							btn_whlr_code.setText(""+ wheeler_name_arr_spot[which]);
							Log.i("wheeler_name_arr_spot[which] ::::", ""+wheeler_name_arr_spot[which]);
							
							removeDialog(WHEELER_CODE);

							whlr_code_send = wheeler_code_arr_spot[which];
							Log.i("****whlr_code_send***", "" + whlr_code_send);
							
							btn_violation.setText(""+ getResources().getString(R.string.select_violation));

							if (isOnline()) {
								// total_amount = 0;
								new Async_getViolations().execute();
							} else {
								showToast("" + NETWORK_TXT);
							}
						}
					});
			Dialog dg_whle_code_name = ad_whle_code_name.create();
			return dg_whle_code_name;
		case VIOLATIONS_DIALOG:
			
			TextView title5 = new TextView(this);
			title5.setText("Select Violations");
			title5.setBackgroundColor(Color.parseColor("#007300"));
			title5.setGravity(Gravity.CENTER);
			title5.setTextColor(Color.WHITE);
			title5.setTextSize(26);
			title5.setTypeface(title5.getTypeface(), Typeface.BOLD);
			title5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
			title5.setPadding(20, 0, 20, 0);
			title5.setHeight(70);
			
			TextView title6 = new TextView(this);
			title6.setTextSize(22);
			title6.setText("Ok");
			title6.setTextColor(Color.WHITE);
			title6.setTypeface(title6.getTypeface(), Typeface.BOLD);
			title6.setBackgroundColor(Color.RED);
			
			
			AlertDialog.Builder ad_violations = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
			ad_violations.setCustomTitle(title5);//"Select Violations"
			ad_violations.setMultiChoiceItems(violation_list.toArray(new String[violation_list.size()]),
					null, new DialogInterface.OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							// TODO Auto-generated method stub

							if (isChecked) {
								Log.i("CHEKCED : ",	"" + violation_offence_Code.get(which));
								violation_positions.add("" + which);
								// violation_checked_items_status.add(true);
							} else if (!isChecked) {
								Log.i("REMOVED : ",	"" + violation_offence_Code.get(which));
								violation_positions.remove("" + which);
								// violation_checked_items_status.remove(w);
							}
						}
					});
			
			ad_violations.setPositiveButton(""+title6,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String desc = "";
							for (int i = 0; i < violation_positions.size(); i++) {
								if (i == 0) {
									desc = violation_description.get(Integer.parseInt(violation_positions.get(i))).toString().trim();
								} else {
									desc = desc	+ ","+ violation_description.get(Integer.parseInt(violation_positions.get(i))).toString().trim();
								}
								grand_total = grand_total+ Integer.parseInt(violation_max_amount.get(Integer.parseInt(violation_positions.get(i))));
							}

							if (!desc.equals("")) {
								btn_violation.setText("" + desc);
							} else {
								btn_violation.setText(""+ getResources().getString(R.string.select_violation));
							}
							total = 0.0;
							total = total_amount + grand_total;
							tv_grand_total_spot.setText("Rs . " + total);
							removeDialog(VIOLATIONS_DIALOG);

						}
					});
			
			
			
			Dialog dg_violation = ad_violations.create();
			return dg_violation;

		case DYNAMIC_VIOLATIONS:
			Dialog dg_dynmic_violtns = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);
			dg_dynmic_violtns.setContentView(R.layout.dynamic_violations);

			TextView tv_sub_header = (TextView) dg_dynmic_violtns.findViewById(R.id.textView_header_spot_challan_xml);

			TextView tv_title = (TextView) dg_dynmic_violtns.findViewById(R.id.textView_title_header_dynmicvltns_xml);
			tv_title.setText(""+ getResources().getString(R.string.select_violation));

			ll_dynamic_violations_root_static = (LinearLayout) dg_dynmic_violtns.findViewById(R.id.ll_dynamic_violations_xml);

			if (Dashboard.check_vhleHistory_or_Spot.equals("spot")) {
				tv_sub_header.setText(""+ getResources().getString(R.string.spot_challan));
			} else if (Dashboard.check_vhleHistory_or_Spot.equals("towing")) {
				tv_sub_header.setText(""+ getResources().getString(R.string.towing_one_line));
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);

			/* DYNAMIC LAYOUTS START */
			if (violation_offence_Code.size() > 0) {

				ll_dynamic_vltns = new LinearLayout[violation_offence_Code.size()];
				spinner_violation = new Spinner[ServiceHelper.violation_detailed_views.length];
				tv_dynamic_vltn_name = new TextView[ServiceHelper.violation_detailed_views.length];
				check_dynamic_vltn = new CheckBox[violation_offence_Code.size()];

				Log.i("Binding SIZE", "" + violation_offence_Code.size());
				for (int i = 0; i < violation_offence_Code.size(); i++) {

					Log.i("vio det view lendght ", ""+ ServiceHelper.violation_detailed_views.length);
					String[] spinner_selectors = new String[3];

					/* TO SHOW IT IN SPINNER DROPDOWN */
					spinner_selectors[0] = "MIN :"+ ServiceHelper.violation_detailed_views[i][3];
					spinner_selectors[1] = "AVG :"+ ServiceHelper.violation_detailed_views[i][5];
					spinner_selectors[2] = "MAX :"+ ServiceHelper.violation_detailed_views[i][4];

					Log.i("J", "" + j);

					ll_dynamic_vltns[i] = new LinearLayout(getApplicationContext());
					ll_dynamic_vltns[i].setOrientation(LinearLayout.HORIZONTAL);
					ll_dynamic_vltns[i].setLayoutParams(params);

					spinner_violation[i] = new Spinner(getApplicationContext());
					tv_dynamic_vltn_name[i] = new TextView(getApplicationContext());
					check_dynamic_vltn[i] = new CheckBox(getApplicationContext());

					LinearLayout.LayoutParams sp_params = new LinearLayout.LayoutParams(90,
							android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
					sp_params.setMargins(0, 15, 5, 15);

					spinner_violation[i].setId(Integer.parseInt(violation_offence_Code.get(i)));
					spinner_violation[i].setGravity(Gravity.CENTER_VERTICAL);

					ArrayAdapter<String> ap_adapter = new ArrayAdapter<String>(
							this, R.layout.spinner_item, android.R.id.text1,
							spinner_selectors);
					ap_adapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_violation[i].setAdapter(ap_adapter);

					// spinner_violation[i].setAdapter(new MyAdapter(this,
					// R.layout.spinner_item, spinner_selectors));

					spinner_violation[i]
							.setBackgroundColor(R.drawable.navi_blue_btn_style);
					spinner_violation[i].setPopupBackgroundResource(R.drawable.navi_blue_btn_style);
					spinner_violation[i].setLayoutParams(sp_params);

					ll_dynamic_vltns[i].addView(spinner_violation[i]);
					Log.i("SPINNER IDS", "" + spinner_violation[i].getId());

					vioCodeDescMap.put("" + spinner_violation[i].getId(),ServiceHelper.violation_detailed_views[i][2]+ " ( "
											+ ServiceHelper.violation_detailed_views[i][1]
											+ " ) ");

					/* CHECKBOX START */
					int identifier = getResources().getIdentifier(
							getApplicationContext().getPackageName()
									+ ":drawable/custom_chec_box", null, null);
					LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT);
					params1.weight = 1.0f;

					check_dynamic_vltn[i].setText("  "
							+ ServiceHelper.violation_detailed_views[i][2]
							+ " ( "
							+ ServiceHelper.violation_detailed_views[i][1]
							+ " ) ");
					check_dynamic_vltn[i].setTextAppearance(getApplicationContext(), R.style.navi_text_style);

					check_dynamic_vltn[i].setId(Integer
							.parseInt(violation_offence_Code.get(i)));

					Log.i("CHECK ID AFTR", "" + check_dynamic_vltn[i].getId());

					check_dynamic_vltn[i].setButtonDrawable(identifier);
					check_dynamic_vltn[i].setLayoutParams(params1);
					ll_dynamic_vltns[i].addView(check_dynamic_vltn[i]);
					/* CHECKBOX END */

					check_all_ids.put("" + i, "" + spinner_violation[i].getId());
					//
					// /* RADIO GROUP END */
					//
					ll_dynamic_violations_root_static.addView(ll_dynamic_vltns[i]);

					/* DYNAMIC RADIO BUTTONS CLICK EVENT END */
					/*----------------------------------------------------------*/
					
					check_dynamic_vltn[i].setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									check = (CheckBox) v;

									if (check.isChecked()) {

										Log.i("checked val", "" + check.getId());

										violation_checked_violations.add(""+ check.getId());
										
										if((check.getId() == 7 || check.getId() == 07) || (check.getId() == 49)){
											
											Intent extra_pasnger = new Intent(getApplicationContext(), ExtraPassengers.class);
											startActivity(extra_pasnger);
											Log.i("extraPassengers****************** ::", SpotChallan.extraPassengers);
										}


									} else {
										Log.i("check removed" + "",	"" + check.getId());
										// }
										violation_checked_violations.remove(""+ check.getId());

									}
									Log.i("violation_checked_violations",	""+ violation_checked_violations.size());

								}
							});

				}

			} else {
				removeDialog(DYNAMIC_VIOLATIONS);
			}

			dg_dynmic_violtns.setOnKeyListener(new Dialog.OnKeyListener() {

				@SuppressWarnings("unused")
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					// TODO Auto-generated method stub

					if (KeyEvent.KEYCODE_BACK == keyCode) {

						for (String key : violation_checked_violations) {// 77,01,02
							// getting key for offence code from all
							for (Entry<String, String> entry : check_all_ids.entrySet()) {// all key:index ; value :
								if (entry.getValue().equals(key)) {
									Log.i(" offence code",	""+ key	+ "--index --"+ (Integer.parseInt(entry.getKey())));
									String selectedId = spinner_violation[Integer.parseInt(entry.getKey())].getSelectedItem().toString();

									Log.i("SPINNER selectedId",""+ selectedId.substring(5,selectedId.length()));

									/*Log.i("Values original::::", "called");
									grand_total = grand_total+ (Integer.parseInt(selectedId.substring(5,selectedId.length())));// total
									Log.i("grand_total Value original::::", "Value OF grand_total::: "+ grand_total);
									
									tv_violation_amnt.setText("Rs . "+ grand_total);
									 FOR DISPLAYING TOTAL CALCULATED AMOUNT 
									Double total = 0.0;
									total = grand_total + VehicleHistoryPendingChallans.total_amount_selected_challans;
									Log.i("total Value original::::", "Value OF total::: "+ total);
									tv_grand_total_spot.setText("Rs . " + total);
									
									
									
									
									
									if(("7".equals(key)|| "07".equals(key))){
										passngerFLG = true ;
												
										Log.i("Integer.parseInt(extraPassengers)  ::::", extraPassengers+" is :"+Integer.parseInt(extraPassengers));
										Log.i("Integer.parseInt(selectedId.substring)  ::::", extraPassengers+" is :"+(Integer.parseInt(selectedId.substring(5,selectedId.length()))));
										Log.i("grand_total ::::", ""+ grand_total);
										
										grand_total = grand_total+ ((Integer.parseInt(selectedId.substring(5,selectedId.length()))
												* Integer.parseInt(extraPassengers)));// total
										Log.i("Grand Total Value************ ::", grand_total+"");
										
										 //FOR DISPLAYING TOTAL CALCULATED AMOUNT 
//										SpotChallan.total = 0.0;
//										SpotChallan.total = grand_total + total_amount;
										Log.i("VehicleHistoryPendingChallans ::::", ""+ VehicleHistoryPendingChallans.total_amount_selected_challans);
										
										total = 0.0;
										total = grand_total + VehicleHistoryPendingChallans.total_amount_selected_challans;
										tv_violation_amnt.setText("Rs . "+ grand_total);
										tv_grand_total_spot.setText("Rs . " + total) ;
										
									}else {
										*/
										grand_total = grand_total+ (Integer.parseInt(selectedId.substring(5,selectedId.length())));// total
										Log.i("grand_total Value original::::", "Value OF grand_total::: "+ grand_total);
										
										// FOR DISPLAYING TOTAL CALCULATED AMOUNT 
										total = 0.0;
										total = grand_total + VehicleHistoryPendingChallans.total_amount_selected_challans;
										Log.i("total Value original::::", "Value OF total::: "+ total);

										tv_violation_amnt.setText("Rs . "+ grand_total);
										tv_grand_total_spot.setText("Rs . " + total);
									//}	
									
									/* framing violation buffer */

									violations_details_send.append(key.trim()+ "@"
											+ selectedId.substring(5,selectedId.length()).trim()
											+ "@"+ selectedId.substring(5,selectedId.length()).trim()
											+ "@");

									for (String offenceCodes : vioCodeDescMap.keySet()) {
										if (offenceCodes.trim().equals(key.trim())) {
											violations_details_send.append(""+ vioCodeDescMap.get(offenceCodes.trim()).trim());

											violation_desc_append.append(""	+ vioCodeDescMap.get(offenceCodes.trim()).trim());
											violation_desc_append.append(",");
										}
									}
									violations_details_send.append("!");

									btn_violation.setText(violation_desc_append);

									Log.i("violations string to sent ",	violations_details_send.toString().trim());
								}
							}
						}

						removeDialog(DYNAMIC_VIOLATIONS);

						/* NO OF PEOPLE CALCULATING */
						removeDialog(DYNAMIC_VIOLATIONS);

						/*---------TO ENABLE EDITEXT WHEN EXTRA PASSENGERS : 07 IS SELECTED---------*/
						
						int status = 0;
						for (int i = 0; i < violation_checked_violations.size(); i++) {
							Log.i("FINAL CODES TO ENABLE EDITTEXT", ""+ violation_checked_violations.get(i));
							if ((violation_checked_violations.get(i).toString().equals("7"))) {
								status = 1;
							}
						}
						/*if (status == 1) {
							ll_extra_people.setVisibility(View.GONE);
						} else {
							ll_extra_people.setVisibility(View.GONE);
						}*/
					}

					return true;

				}
			});

			/* DYNAMIC LAYOUTS END */
			return dg_dynmic_violtns;

		}
		return super.onCreateDialog(id);
	}

	
	public Boolean isOnline() {
		ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
		return nwInfo != null;
	}

	public void getLocation() {
		// TODO Auto-generated method stub

		try {
			m_locationlistner = (LocationManager) this.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = m_locationlistner.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = m_locationlistner.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				latitude = 0.0;
				longitude = 0.0;
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (isNetworkEnabled) {
					m_locationlistner.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (m_locationlistner != null) {
						location = m_locationlistner.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						} else {
							latitude = 0.0;
							longitude = 0.0;
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						m_locationlistner.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (m_locationlistner != null) {
							location = m_locationlistner.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							} else {
								latitude = 0.0;
								longitude = 0.0;
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei_send = telephonyManager.getDeviceId();// TO GET IMEI NUMBER

		if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
			simid_send = "" + telephonyManager.getSimSerialNumber();
		} else {
			simid_send = "";
		}

		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		macAddress = wInfo.getMacAddress();

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
	
	/* CAMERA DEVICE CHECKING */
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device

			return false;
		}
	}
	
	/* ACTIVITYRESULT FOR ACCESSING IMAGES FROM GALLERY AND CAMERA */
	@SuppressLint("SetJavaScriptEnabled")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("IMAGE FROM", "" + cam_imag);
		if (cam_imag == "browse") {
			if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
					&& null != data) {

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

				picturePath = "";
				picturePath = cursor.getString(columnIndex);
				cursor.close();

				imgEvidence = "1";// if image then "1" else "0"

				Log.i("gallery pic path", "" + picturePath);
				webviewloader = new WebviewLoader();
				wv_generate.setBackgroundColor(0x00000000);
				wv_generate.setHorizontalScrollBarEnabled(true);
				wv_generate.setVerticalScrollBarEnabled(true);
				WebSettings webSettings = wv_generate.getSettings();
				wv_generate.setInitialScale(10);
				webSettings.setJavaScriptEnabled(true);
				wv_generate.getSettings().setLoadWithOverviewMode(true);
				wv_generate.getSettings().setUseWideViewPort(true);
				wv_generate.getSettings().setBuiltInZoomControls(true);
				wv_generate.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
				webviewloader.DisplayImage("file://" + picturePath, wv_generate);

			}
		} else if (cam_imag == "camera") {
			if (resultCode == RESULT_OK) {
				if (requestCode == CAMERA_REQUEST) {
					Bitmap photo = (Bitmap) data.getExtras().get("data");
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
					Random randomGenerator = new Random();
					int num = randomGenerator.nextInt(100);
					String newimagename = num + ".jpg";
					File f = null;
					f = new File(Environment.getExternalStorageDirectory()
							+ File.separator + newimagename);
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// write the bytes in file

					try {
						fo = new FileOutputStream(f.getAbsoluteFile());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						fo.write(bytes.toByteArray());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					imgEvidence = "1";// if image then "1" else "0"

					picturePath = "";
					picturePath = f.getAbsolutePath();

					Log.i("Camera pic path", picturePath);
					webviewloader = new WebviewLoader();
					wv_generate.setBackgroundColor(0x00000000);
					wv_generate.setHorizontalScrollBarEnabled(true);
					wv_generate.setVerticalScrollBarEnabled(true);
					WebSettings webSettings = wv_generate.getSettings();
					wv_generate.setInitialScale(50);
					webSettings.setJavaScriptEnabled(true);
					wv_generate.getSettings().setLoadWithOverviewMode(true);
					wv_generate.getSettings().setUseWideViewPort(true);
					wv_generate.getSettings().setBuiltInZoomControls(true);
					wv_generate.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
					webviewloader.DisplayImage("file://" + picturePath, wv_generate);
				}
			}
		}
	}
	
	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight) { // BEST QUALITY MATCH

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize, Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		int inSampleSize = 1;

		if (height > reqHeight) {
			inSampleSize = Math.round((float) height / (float) reqHeight);
		}
		int expectedWidth = width / inSampleSize;

		if (expectedWidth > reqWidth) {
			// if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
			// If bigger SampSize..
			inSampleSize = Math.round((float) width / (float) reqWidth);
		}

		options.inSampleSize = inSampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		if (location != null) {
			latitude = (float) location.getLatitude();
			longitude = (float) location.getLongitude();
			// speed = location.getSpeed();
		} else {
			latitude = 0.0;
			longitude = 0.0;
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
