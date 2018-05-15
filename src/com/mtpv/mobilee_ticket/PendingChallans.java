package com.mtpv.mobilee_ticket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.mobilee_ticket.R;
import com.mtpv.mobilee_ticket_services.ServiceHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

@SuppressLint("InflateParams")
public class PendingChallans extends Activity implements OnClickListener {

	final int PROGRESS_DIALOG = 0;
	final int PENDING_CHALLAN_DETAILS = 1;

	ListView lv_pending_challans;
	TextView tv_total_challans;
	TextView tv_total_amnt;
	TextView tv_header;

	String NEWTWRK_TXT = "";
	int pos = 0;
	int total_amount = 0;
	ImageLoader imageLoader;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pen_challans);
		LoadUIComponents();

		if (android.os.Build.VERSION.SDK_INT > 11) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		lv_pending_challans.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				pos = arg2;
				showDialog(PENDING_CHALLAN_DETAILS);

			}
		});

		if (Dashboard.check_vhleHistory_or_Spot.equals("vehiclehistory")) {

		} else if (Dashboard.check_vhleHistory_or_Spot.equals("spot")) {
			tv_header.setText(""
					+ getResources().getString(R.string.spot_challan));
		}
	}

	private void LoadUIComponents() {
		// TODO Auto-generated method stub
		NEWTWRK_TXT = "" + getResources().getString(R.string.newtork_txt);

		lv_pending_challans = (ListView) findViewById(R.id.lvpendingchallans_xml);
		tv_total_challans = (TextView) findViewById(R.id.tv_totalnochallans_pen_challans_xml);
		tv_total_amnt = (TextView) findViewById(R.id.tv_totalamount_pen_challans_xml);
		tv_header = (TextView) findViewById(R.id.textViewheader_pen_challans_xml);

		lv_pending_challans.setCacheColorHint(0);

		lv_pending_challans.setAdapter(new ChallanAdapter(getApplicationContext()));
		tv_total_challans.setText("No. of Challans : "
				+ (ServiceHelper.pending_challans_details.length));

		for (int i = 0; i < ServiceHelper.pending_challans_details.length; i++) {
			total_amount = (total_amount + (Integer
					.parseInt(ServiceHelper.pending_challans_details[i][7])));
		}

		tv_total_amnt.setText("Total Amount : " + total_amount);

		Log.i("sucess", "" + ServiceHelper.pending_challans_details.length);
	}

	public Boolean isOnline() {
		ConnectivityManager conManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
		return nwInfo != null;
	}

	public class ChallanAdapter extends BaseAdapter {

		LayoutInflater inflater;
		Context context;

		public ChallanAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.cust_pen_challans, null);
			}
			TextView tv_eticket_num = (TextView) convertView
					.findViewById(R.id.tv_eticketno_cust_pend_xml);
			TextView tv_point_name = (TextView) convertView
					.findViewById(R.id.tv_pointname_cust_pend_xml);
			TextView tv_amount = (TextView) convertView
					.findViewById(R.id.tv_amnt_cust_pend_xml);
			TextView tv_date_time = (TextView) convertView
					.findViewById(R.id.tv_datetime_cust_pend_xml);
			Log.i("**ADAPTER**", ""
					+ ServiceHelper.pending_challans_details[position][1]);

			tv_eticket_num.setText("E-TicketNo : "
					+ ServiceHelper.pending_challans_details[position][1]);
			// tv_point_name.setText("Offence Date & Time : "
			// + ServiceHelper.pending_challans_details[position][2]
			// + "  "
			// + ServiceHelper.pending_challans_details[position][3]);
			tv_point_name
					.setText(""
							+ (ServiceHelper.pending_challans_details[position][2]
									.equals("") ? " "
									: ServiceHelper.pending_challans_details[position][2])
							+ "  "
							+ (ServiceHelper.pending_challans_details[position][3]
									.equals("") ? " "
									: ServiceHelper.pending_challans_details[position][3]));
			tv_amount
					.setText("Amount : "
							+ (ServiceHelper.pending_challans_details[position][7]
									.equals("") ? " "
									: ServiceHelper.pending_challans_details[position][7]));
			tv_date_time.setText("" + (position + 1));

			Log.i("ADAPTER AMNT", "" + total_amount);
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ServiceHelper.pending_challans_details.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case PROGRESS_DIALOG:
			ProgressDialog pd = ProgressDialog.show(this, "", "Please Wait...",	true);
			pd.setContentView(R.layout.custom_progress_dialog);
			pd.setCancelable(false); 
			
			return pd;
		case PENDING_CHALLAN_DETAILS:
			Dialog dg_details = new Dialog(this,
					android.R.style.Theme_Translucent_NoTitleBar);
			dg_details.setContentView(R.layout.pending_challans_details);
			
			TextView tv_detailed_pen_challan_header = (TextView) dg_details
					.findViewById(R.id.textView_penchallandetails_header);
			tv_detailed_pen_challan_header.setText(""
					+ getResources().getString(R.string.drunk_drive));
			
			TextView tv_vhle_no_details = (TextView) dg_details
					.findViewById(R.id.tvregno_pchallans_details_xml);
			TextView tv_etckt_no_details = (TextView) dg_details
					.findViewById(R.id.tveticketno_pchallans_details_xml);
			TextView tv_offencedate_details = (TextView) dg_details
					.findViewById(R.id.tv_offencedate_pchallans_details_xml);
			TextView tv_offcencetime_details = (TextView) dg_details
					.findViewById(R.id.tv_offencetime_pchallans_details_xml);
			TextView tv_pointname_details = (TextView) dg_details
					.findViewById(R.id.tv_pointname_pchallans_details_xml);
			TextView tv_psname_details = (TextView) dg_details
					.findViewById(R.id.tv_psname_pchallans_details_xml);
			TextView tv_offence_desc_details = (TextView) dg_details
					.findViewById(R.id.tv_offencedesc_pchallans_details_xml);
			TextView tv_amnt_details = (TextView) dg_details
					.findViewById(R.id.tv_amnt_pchallans_details_xml);
			Button btn_ok_details = (Button) dg_details
					.findViewById(R.id.btn_ok_pchallans_details_xml);
			ImageView img_display = (ImageView) dg_details
					.findViewById(R.id.imgv_pchallans_details_xml);

			tv_vhle_no_details.setText(""
					+ (ServiceHelper.pending_challans_details[pos][0]
							.equals("0") ? " "
							: ServiceHelper.pending_challans_details[pos][0]));
			tv_etckt_no_details.setText(""
					+ (ServiceHelper.pending_challans_details[pos][1]
							.equals("0") ? " "
							: ServiceHelper.pending_challans_details[pos][1]));
			tv_offencedate_details.setText(""
					+ (ServiceHelper.pending_challans_details[pos][2]
							.equals("0") ? " "
							: ServiceHelper.pending_challans_details[pos][2]));
			tv_offcencetime_details.setText(""
					+ (ServiceHelper.pending_challans_details[pos][3]
							.equals("0") ? " "
							: ServiceHelper.pending_challans_details[pos][3]));
			tv_pointname_details.setText(""
					+ (ServiceHelper.pending_challans_details[pos][4]
							.equals("0") ? " "
							: ServiceHelper.pending_challans_details[pos][4]));
			tv_psname_details.setText(""
					+ (ServiceHelper.pending_challans_details[pos][5]
							.equals("0") ? " "
							: ServiceHelper.pending_challans_details[pos][5]));
			tv_offence_desc_details.setText(""
					+ (ServiceHelper.pending_challans_details[pos][6]
							.equals("0") ? " "
							: ServiceHelper.pending_challans_details[pos][6]));
			tv_amnt_details.setText("Rs : "
					+ (ServiceHelper.pending_challans_details[pos][7]
							.equals("0") ? " "
							: ServiceHelper.pending_challans_details[pos][7]));

			if (!ServiceHelper.pending_challans_details[pos][8].toString()
					.trim().equals("0")) {
				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
						this).threadPriority(Thread.NORM_PRIORITY - 2)
						.denyCacheImageMultipleSizesInMemory()
						.discCacheFileNameGenerator(new Md5FileNameGenerator())
						.tasksProcessingOrder(QueueProcessingType.LIFO).build();
				ImageLoader.getInstance().init(config);

				imageLoader = ImageLoader.getInstance();

				imageLoader.displayImage(
						ServiceHelper.pending_challans_details[pos][8]
								.toString().trim(), img_display);
			}

			btn_ok_details.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					removeDialog(PENDING_CHALLAN_DETAILS);
				}
			});

			return dg_details;

		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// case value:
		//
		// break;

		default:
			break;
		}
	}

	public void showToast(String msg) {
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

}
