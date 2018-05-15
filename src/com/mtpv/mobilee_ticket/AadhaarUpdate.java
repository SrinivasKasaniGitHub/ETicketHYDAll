package com.mtpv.mobilee_ticket;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.mobilee_ticket_services.ServiceHelper;

public class AadhaarUpdate extends Activity {
	
	EditText edt_regncid, edt_regncidname, edt_regncid_lastnum, aadhaar_no ;
	Button get_details, get_aadhaar, update_aadhaar, print ;
	LinearLayout vehicle_details_layout, ll_aadhardetails_spot_challan_xml, print_layout ;
	TextView e_ticketNo, offence_dt, offence_time, location, ps_name, voilation_desc, detained_items ;
	CheckBox release_items ;
	
	final static int PROGRESS_DIALOG = 1 ;
	
	public static String complete_vehicleNo = "", release_detained_items ="" ;

	int edt_regncid_spotchallanMAX_LENGTH = 4;
	int edt_regncidname_spotchallanLENGTH = 4;
	int edt_regncid_lastnum_spotchallanMAX_LENGTH = 4;
	
	
	/* AADHAR public static TextView*/
	public static TextView tv_aadhar_header;
	public static TextView tv_aadhar_user_name;
	public static TextView tv_aadhar_care_off;
	public static TextView tv_aadhar_address;
	public static TextView tv_aadhar_mobile_number;
	public static TextView tv_aadhar_gender;
	public static TextView tv_aadhar_dob;
	public static TextView tv_aadhar_uid;
	public static TextView tv_aadhar_eid;
	public static TextView tv_violation_amnt, response;
	ImageView img_aadhar_image;
	
	
	public static String unit_name, eticketNo, Offence_dt,
			offenceTime, booked_psname, pointName, violations, detainedItems, 
			lic_no, veh_owner, driver_name, driver_addr, driver_city, contctNo, challanType,
			pidCd, pidName, cadre ;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_aadhaar_update);
		
		edt_regncid = (EditText)findViewById(R.id.edt_regncid);
		edt_regncidname = (EditText)findViewById(R.id.edt_regncidname);
		edt_regncid_lastnum = (EditText)findViewById(R.id.edt_regncid_lastnum);
		
		aadhaar_no = (EditText)findViewById(R.id.aadhaar_no);
		
		get_details = (Button)findViewById(R.id.get_details);
		update_aadhaar = (Button)findViewById(R.id.update_aadhaar);
		
		get_aadhaar = (Button)findViewById(R.id.get_aadhaar);
		print = (Button)findViewById(R.id.print);
		
		vehicle_details_layout = (LinearLayout)findViewById(R.id.vehicle_details_layout);
		ll_aadhardetails_spot_challan_xml = (LinearLayout)findViewById(R.id.ll_aadhardetails_spot_challan_xml);
		print_layout = (LinearLayout)findViewById(R.id.print_layout );
		
		/* AADHRA DETAILS */
		tv_aadhar_header = (TextView) findViewById(R.id.tvadhardetails_header_label_spotchallan_xml);
		tv_aadhar_user_name = (TextView) findViewById(R.id.tvaadharname_spotchallan_xml);
		tv_aadhar_care_off = (TextView) findViewById(R.id.tvcareof_spotchallan_xml);
		tv_aadhar_address = (TextView) findViewById(R.id.tvaddress_spotchallan_xml);
		tv_aadhar_mobile_number = (TextView) findViewById(R.id.tvmobilenumber_spotchallan_xml);
		tv_aadhar_gender = (TextView) findViewById(R.id.tvgender_spotchallan_xml);
		tv_aadhar_dob = (TextView) findViewById(R.id.tvdob_spotchallan_xml);
		tv_aadhar_uid = (TextView) findViewById(R.id.tvuid_spotchallan_xml);
		//tv_aadhar_eid = (TextView) findViewById(R.id.tveid_spotchallan_xml);
		img_aadhar_image = (ImageView) findViewById(R.id.imgv_aadhar_photo_spotchallan_xml);

		response = (TextView)findViewById(R.id.response);
		
		e_ticketNo = (TextView)findViewById(R.id.e_ticketNo);
		offence_dt = (TextView)findViewById(R.id.offence_dt);
		offence_time = (TextView)findViewById(R.id.offence_time);
		location = (TextView)findViewById(R.id.location);
		ps_name = (TextView)findViewById(R.id.ps_name);
		voilation_desc = (TextView)findViewById(R.id.voilation_desc);
		detained_items = (TextView)findViewById(R.id.detained_items);
		
		release_items = (CheckBox)findViewById(R.id.release_detained);
		
		release_items.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (release_items.isChecked()) {
					Log.i("detained_items  :::::", ""+detained_items);
					release_detained_items = ""+detained_items.getText().toString().trim() ;
				}else {
					release_detained_items = "";
				}
			}
		});
		
		print.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
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
					// et_driver_lcnce_num_spot.requestFocus(); 
	             }
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) { }
			@Override
			public void afterTextChanged(Editable s) { }
		});
		
		final VerhoeffCheckDigit ver=new VerhoeffCheckDigit();
		
		get_details.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edt_regncid.getText().toString().trim().equals("")) {
					edt_regncid.setError(Html.fromHtml("<font color='black'>Enter Proper Registration Number</font>"));
					edt_regncid.requestFocus();
				}else if (edt_regncid_lastnum.getText().toString().trim().equals("")) {
					edt_regncid_lastnum.setError(Html.fromHtml("<font color='black'>Enter Proper Registration Number</font>"));
					edt_regncid_lastnum.requestFocus();
				}else {
					if (isOnline()) {
						complete_vehicleNo = ""+edt_regncid.getText().toString().trim() +
								edt_regncidname.getText().toString().trim() + edt_regncid_lastnum.getText().toString().trim();
						new Async_GetDetails().execute();
					}else {
						showToast("Check your Internet Connection !!!");
					}
				}
			}
		});
		
		get_aadhaar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (aadhaar_no.getText().toString().trim().equals("")) {
					aadhaar_no.setError(Html.fromHtml("<font color='black'>Enter Aadhaar Number</font>"));
					aadhaar_no.requestFocus();
				}else if (aadhaar_no.getText().toString().trim().length()>0 &&
						aadhaar_no.getText().toString().trim().length()!= 12) {
					aadhaar_no.setError(Html.fromHtml("<font color='black'>Enter 12 digit Aadhaar Number</font>"));
					aadhaar_no.requestFocus();
				}else if (aadhaar_no.getText().toString().trim().length() == 12 && 
						!ver.isValid(aadhaar_no.getText().toString().trim())) {
					aadhaar_no.setError(Html.fromHtml("<font color='black'>Enter 12 digit Valid Aadhaar Number</font>"));
					aadhaar_no.requestFocus();
				}else {
					if (isOnline()) {
						complete_vehicleNo = ""+edt_regncid.getText().toString().trim() +
								edt_regncidname.getText().toString().trim() + edt_regncid_lastnum.getText().toString().trim();
						new Async_GetAadhaar().execute();	
					}else {
						showToast("Check your Internet Connection !!!");
					}
				}
			}
		});
		
		
		update_aadhaar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (aadhaar_no.getText().toString().trim().equals("")) {
					aadhaar_no.setError(Html.fromHtml("<font color='black'>Enter Aadhaar Number</font>"));
					aadhaar_no.requestFocus();
				}else if (aadhaar_no.getText().toString().trim().length()>0 &&
						aadhaar_no.getText().toString().trim().length()!= 12) {
					aadhaar_no.setError(Html.fromHtml("<font color='black'>Enter 12 digit Aadhaar Number</font>"));
					aadhaar_no.requestFocus();
				}else if (aadhaar_no.getText().toString().trim().length() == 12 && 
						!ver.isValid(aadhaar_no.getText().toString().trim())) {
					aadhaar_no.setError(Html.fromHtml("<font color='black'>Enter 12 digit Valid Aadhaar Number</font>"));
					aadhaar_no.requestFocus();
				}else {
					if (isOnline()) {
						complete_vehicleNo = ""+edt_regncid.getText().toString().trim() +
								edt_regncidname.getText().toString().trim() + edt_regncid_lastnum.getText().toString().trim();
						new Async_UpdateAadhaar().execute();	
					}else {
						showToast("Check your Internet Connection !!!");
					}
				}
			}
		});
	}
	
	public Boolean isOnline() {
		ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
		return nwInfo != null;
	}
	
	public class Async_GetDetails extends AsyncTask<Void, Void, String>{
		
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
			//Log.i("complete_vehicleNo ::::", ""+complete_vehicleNo);
			
			ServiceHelper.getVehileDetails(""+Dashboard.UNIT_CODE, ""+complete_vehicleNo, "", "");
			
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			if (ServiceHelper.aadhaarVehicle_resp.equals("NA")) {
				showToast("No Details Found");
				vehicle_details_layout.setVisibility(View.GONE);
				
				e_ticketNo.setText("");
				offence_dt.setText("");
				offence_time.setText("");
				location.setText("");
				ps_name.setText("");
				voilation_desc.setText("");
				detained_items.setText("");
			
			}else if (ServiceHelper.aadhaarVehicle_resp.equals("EXIST")) {
				
				showToast(""+ServiceHelper.aadhaarVehicle_resp);
				
			}else {
				
				try {
					String resp_json = ServiceHelper.aadhaarVehicle_resp ;
					
					JSONObject  jsonRootObject = new JSONObject(resp_json);//array method 
					
					JSONArray jsonArray = jsonRootObject.optJSONArray("ETICKET_DETAILS");//array object
					//Log.i("jsonArray.length() ", ""+jsonArray.length());
					if (jsonArray.length()==0) {
							//Log.i("jsonArray.length()", ""+jsonArray.length());
							vehicle_details_layout.setVisibility(View.GONE);
							showToast("No Challan Details Found");
							
					}else {
						vehicle_details_layout.setVisibility(View.VISIBLE);
						for(int i=0; i < jsonArray.length(); i++){
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							
							unit_name = jsonObject.optString("UNIT_NAME").toString();
							eticketNo = jsonObject.optString("ETICKET_NO").toString(); 
							Offence_dt = jsonObject.optString("OFFENCE_DT").toString(); 
							offenceTime = jsonObject.optString("OFF_TIME").toString();
							booked_psname =jsonObject.optString("BOOKED_PSNAME").toString(); 
							pointName =jsonObject.optString("POINT_NAME").toString(); 
							violations =jsonObject.optString("VIOLATIONS").toString(); 
							detainedItems = jsonObject.optString("DETAINED_ITEMS").toString(); 
							lic_no = jsonObject.optString("LICENSE_NO").toString(); 
							veh_owner = jsonObject.optString("VEHICLE_OWNER_NAME").toString(); 
							driver_name = jsonObject.optString("DRIVER_NAME").toString(); 
							driver_addr = jsonObject.optString("DRIVER_ADDR").toString(); 
							driver_city = jsonObject.optString("DRIVER_CITY").toString(); 
							contctNo = jsonObject.optString("CONTACT_NO").toString(); 
							challanType =jsonObject.optString("CHALLAN_TYPE").toString(); 
							pidCd =jsonObject.optString("PID_CD").toString(); 
							pidName = jsonObject.optString("PID_NAME").toString(); 
							cadre = jsonObject.optString("CADRE").toString(); 
							
							//18@200@200@Lane/line Crossing@!
							String[] split_violation = violations.split("\\@");
							
							
							e_ticketNo.setText(""+eticketNo);
							offence_dt.setText(""+Offence_dt);
							offence_time.setText(""+offenceTime);
							location.setText(""+pointName);
							ps_name.setText(""+booked_psname);
							voilation_desc.setText(""+split_violation[3]);
							detained_items.setText(""+detainedItems);
							
						}
					}
					
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
			
		}
	}
	
public class Async_GetAadhaar extends AsyncTask<Void, Void, String>{
		
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
			//Log.i("complete_vehicleNo ::::", ""+complete_vehicleNo);
			ServiceHelper.getAadharDetails(""+aadhaar_no.getText().toString().trim(), "");
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@SuppressLint("DefaultLocale")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			
			if (ServiceHelper.aadhar_details.length > 0) {
				//tv_aadhar_header.setVisibility(View.VISIBLE);
				ll_aadhardetails_spot_challan_xml.setVisibility(View.VISIBLE);
				update_aadhaar.setVisibility(View.VISIBLE);
				
				tv_aadhar_user_name.setText("" + ServiceHelper.aadhar_details[0]!=null?ServiceHelper.aadhar_details[0].trim().toUpperCase():"");
				tv_aadhar_care_off.setText(""+ (!ServiceHelper.aadhar_details[1].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[1].trim().toUpperCase(): "NA"));
				tv_aadhar_address.setText(""
						+ (!ServiceHelper.aadhar_details[2].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[2].trim().toUpperCase(): "")+ ","
						+ (!ServiceHelper.aadhar_details[3].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[3].trim().toUpperCase()+ ",": "")
						+ (!ServiceHelper.aadhar_details[4].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[4].trim().toUpperCase()+ ",": "")
						+ (!ServiceHelper.aadhar_details[5].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[5].trim().toUpperCase()+ ",": "")
						+ (!ServiceHelper.aadhar_details[6].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[6].trim().toUpperCase()+ ",": "")
						+ (!ServiceHelper.aadhar_details[7].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[7].trim().toUpperCase()+ ",": ""));
				tv_aadhar_mobile_number.setText(""+ (!ServiceHelper.aadhar_details[8].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[8].trim().toUpperCase(): "NA"));
				tv_aadhar_gender.setText(""+ (!ServiceHelper.aadhar_details[9].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[9].trim().toUpperCase(): "NA"));
				tv_aadhar_dob.setText(""+ (!ServiceHelper.aadhar_details[10].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[10].trim().toUpperCase(): "NA"));
				tv_aadhar_uid.setText(""+ (!ServiceHelper.aadhar_details[11].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[11].trim().toUpperCase(): "NA"));
				//tv_aadhar_eid.setText(""+ (!ServiceHelper.aadhar_details[12].equalsIgnoreCase("0") ? ServiceHelper.aadhar_details[12].trim().toUpperCase(): "NA"));
					
			if (ServiceHelper.aadhar_details[13].toString().trim().equals("0")) {
				img_aadhar_image.setImageResource(R.drawable.photo);
			} else {
				byte[] decodestring = Base64.decode(""+ ServiceHelper.aadhar_details[13].toString().trim(), Base64.DEFAULT);
				Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
				img_aadhar_image.setImageBitmap(decocebyte);
			}
			}else {
				ll_aadhardetails_spot_challan_xml.setVisibility(View.GONE);
				update_aadhaar.setVisibility(View.GONE);
				showToast("No Addhaar Details Found");
				
				tv_aadhar_user_name.setText("");
				tv_aadhar_care_off.setText("");
				tv_aadhar_address.setText("");
				tv_aadhar_mobile_number.setText("");
				tv_aadhar_gender.setText("");
				tv_aadhar_dob.setText("");
				tv_aadhar_uid.setText("");
				tv_aadhar_eid.setText(""); 
			}
		}
	}
	
	
	public class Async_UpdateAadhaar extends AsyncTask<Void, Void, String>{
		
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
			
			/*public String aadharUpdateForChallanGeneration(String unitCode,String regnNo,String engNo,
            String chasisNo,String aadharNo,String detainedItems,String violations,String dlNo,String ownerName,
            String driverName,String driverAddress,String driverCity,String driverContactNo,String challanType,
            String pidCode,String pidName,String cadre,String unitName,String offenceDate,
            String offenceTime,String eticketNo,String bookedPsName,String pointName,String  drvierLicNo);*/
			Log.i("release_detained_items at getAadhaarUpdate *************",""+release_detained_items);
			ServiceHelper.getAadhaarUpdate(""+Dashboard.UNIT_CODE, ""+complete_vehicleNo, "", "", ""+aadhaar_no.getText().toString().trim(), 
					""+release_detained_items,""+violations,""+lic_no, ""+veh_owner, ""+driver_name,""+driver_addr,""+driver_city, ""+contctNo, 
					""+challanType,""+pidCd,""+pidName, ""+cadre, ""+unit_name,""+Offence_dt,""+offenceTime, ""+eticketNo, ""+booked_psname,""+pointName,
					""+lic_no);
			
//aadharUpdateForChallanGeneration(String unitCode,String regnNo,String engNo,String chasisNo,String aadharNo,String detainedItems,String violations);
			
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			
			if (!ServiceHelper.UpdateAadhaar_resp.equals("NA")) {
				
				/*response.setText(""+ServiceHelper.UpdateAadhaar_resp);
				response.setVisibility(View.VISIBLE);
				print_layout.setVisibility(View.VISIBLE);*/
				
				Intent print = new Intent(AadhaarUpdate.this, Aadhaar_Update_Print.class);
				startActivity(print);
				
				//showToast(""+ServiceHelper.UpdateAadhaar_resp);
			}else {
				print_layout.setVisibility(View.GONE);
				showToast("Unable to Update Aadhaar Number");
			}
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		e_ticketNo.setText(""+eticketNo);
		offence_dt.setText(""+Offence_dt);
		offence_time.setText(""+offenceTime);
		location.setText(""+pointName);
		ps_name.setText(""+booked_psname);
		voilation_desc.setText(""+violations);
		detained_items.setText(""+detainedItems);
		
		tv_aadhar_user_name.setText("");
		tv_aadhar_care_off.setText("");
		tv_aadhar_address.setText("");
		tv_aadhar_mobile_number.setText("");
		tv_aadhar_gender.setText("");
		tv_aadhar_dob.setText("");
		tv_aadhar_uid.setText("");
		tv_aadhar_eid.setText(""); 
		
		finish();
		
	}
}
