package com.mtpv.mobilee_ticket;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class MinorActivity extends Activity {

	public static EditText bar, address ;
	Button ok, clear ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_minor);
		this.setFinishOnTouchOutside(false);
		
		bar = (EditText)findViewById(R.id.bar);
		address = (EditText)findViewById(R.id.bar_address);
		
		ok = (Button)findViewById(R.id.ok_btn);
		clear = (Button)findViewById(R.id.clear_btn);
		
		clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bar.setText("");
				address.setText("");
			}
		});
	
		ok.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (bar.getText().toString().trim().equals("")) {
						bar.setError(Html.fromHtml("<font color='black'>Enter Bar/Wine Shop/Restaurant Name</font>"));
						bar.requestFocus();
					}else if (address.getText().toString().trim().equals("")) {
						address.setError(Html.fromHtml("<font color='black'>Enter Address of Bar/Wine Shop/Restaurant</font>"));
						address.requestFocus();
					}else {
						finish();
					}
				}
			});
		}
	}
