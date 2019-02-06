package com.mtpv.mobilee_ticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.mobilee_ticket_services.ServiceHelper;

public class ReleaseversionActivity extends Activity {
	
	
	ListView version_details;

	ImageView img_logo;
	TextView officer_Name,officer_Cadre,officer_PS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_releaseversion);
		
		version_details = (ListView)findViewById(R.id.version_details);

		img_logo=(ImageView)findViewById(R.id.img_logo);
		if (MainActivity.uintCode.equals("22")){
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.cyb_logo));
		}else if (MainActivity.uintCode.equals("23")){
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.htp_left));
		}else if (MainActivity.uintCode.equals("24")){
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.rac_logo));
		}else if (MainActivity.uintCode.equals("44")) { //44 Warangal
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.wgl_logo));
		}else {//  69 Siddipet
			img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
		}
		officer_Name=(TextView)findViewById(R.id.officer_Name);
		officer_Cadre=(TextView)findViewById(R.id.officer_cadre);
		officer_PS=(TextView)findViewById(R.id.officer_PS);

		officer_Name.setText(MainActivity.pidName+"("+MainActivity.cadre_name+")");
		officer_Cadre.setText(MainActivity.cadre_name);
		officer_PS.setText(MainActivity.psName);
		
		if (!ServiceHelper.version_response.equals("NA")) {
			String[] resp = ServiceHelper.version_response.split("\\@");
			
			for (String ver:resp) {
				Log.i("version Details :::", ""+ver);
			}
			
			 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	                 android.R.layout.simple_list_item_1, resp);
	     
			 version_details.setAdapter(adapter);
		}else {
			showToast("No Updates Found !!!");
			finish();
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
		Intent i = new Intent(ReleaseversionActivity.this, Dashboard.class);
		startActivity(i);
		Dashboard.current_version = "Y";
	}
}
