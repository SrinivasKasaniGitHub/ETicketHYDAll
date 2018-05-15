package com.mtpv.mobilee_ticket;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.mtpv.mobilee_ticket.R;
import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.ServiceHelper;

@SuppressLint({ "WorldReadableFiles", "DefaultLocale" })
public class SpotResponse extends Activity implements OnClickListener {
	TextView tv_regno;
	TextView tv_date_time;
	TextView tv_eticketno;
	TextView tv_ps_name;
	TextView tv_point_name;
	TextView tv_officer_pid;
	TextView tv_officer_name;
	TextView tv_driver_name;
	TextView tv_driver_fname;
	TextView tv_driver_lic_num;
	TextView tv_driver_contact_num;
	TextView tv_header_sub_title;
	TextView tv_sucess_text_header;
	TextView tv_violation_main_header;
	TextView tv_sub_header_left;
	TextView tv_sub_header_right;
	TextView tv_sub_header_date;
	TextView tv_total_pendingchallans_amnt;
	TextView tv_total_paid_amnt;
	/* BUFFER DISPLAY START */
	TextView tv_pendingchallans_buffer_display;
	TextView tv_detained_items_buffer_display;
	TextView tv_released_detained_items_buffer_display;
	/* BUFFER DISPLAY END */

	Button btn_back_spot_res;
	Button btn_print_spot_res;

	BluetoothAdapter bluetoothAdapter;
	@SuppressWarnings("unused")
	private BluetoothAdapter mBluetoothAdapter = null;
	private static final int REQUEST_ENABLE_BT = 1;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	String address_spot = "";

	LinearLayout ll_eticketnum;
	LinearLayout ll_violations;
	LinearLayout[] ll_vltns_dynamic;

	/* Paid CHALLANS */
	LinearLayout ll_paid_challans;
	LinearLayout[] ll_paidChallans_dynamic;
	RelativeLayout rl_paid_challans_root;

	double total_pc_amnt = 0.0;
	double total_paid_amnt = 0.0;

	final AnalogicsThermalPrinter actual_printer = new AnalogicsThermalPrinter();
	final Bluetooth_Printer_3inch_ThermalAPI bth_printer = new Bluetooth_Printer_3inch_ThermalAPI();

	DBHelper db;

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.spot_response);
		LoadUiComponents();
		
		SpotChallan.imgSelected = "0";

		db = new DBHelper(getApplicationContext());

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
				db.deleteDuplicateRecords(DBHelper.duplicatePrint_table, ""
						+ getResources().getString(R.string.dup_spot_challan));
				db.insertDuplicatePrintDetails(""
						+ ServiceHelper.final_spot_reponse_master[0], ""
						+ getResources().getString(R.string.dup_spot_challan));
				db.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				db.close();
			}
		} else if (Dashboard.check_vhleHistory_or_Spot.equals("towing")) {
			try {
				db.open();
				db.deleteDuplicateRecords(DBHelper.duplicatePrint_table, ""
						+ getResources().getString(R.string.towing_one_line));
				db.insertDuplicatePrintDetails(""
						+ ServiceHelper.final_spot_reponse_master[0], ""
						+ getResources().getString(R.string.towing_one_line));
				db.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				db.close();
			}
		} else if (Dashboard.check_vhleHistory_or_Spot
				.equals("releasedocuments")) {
			try {
				db.open();
				db.deleteDuplicateRecords(
						DBHelper.duplicatePrint_table,
						""
								+ getResources().getString(
										R.string.release_documents_one_line));
				db.insertDuplicatePrintDetails(
						"" + ServiceHelper.final_spot_reponse_master[0],
						""
								+ getResources().getString(
										R.string.release_documents_one_line));
				db.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				db.close();
			}
		}
		try {
			bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		} catch (Exception e ) {
			e.printStackTrace();
		}


		CheckBlueToothState();
		registerReceiver(null, new IntentFilter(BluetoothDevice.ACTION_FOUND));

		preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
		editor = preferences.edit();
		address_spot = preferences.getString("btaddress", "btaddr");
	}

	private void LoadUiComponents() {
		// TODO Auto-generated method stub
		tv_header_sub_title = (TextView) findViewById(R.id.textView_header_spot_challan_xml);
		tv_regno = (TextView) findViewById(R.id.tvregno_spot_response_xml);
		tv_date_time = (TextView) findViewById(R.id.tvdatetime_spot_response_xml);
		tv_eticketno = (TextView) findViewById(R.id.tveticket_spot_response_xml);
		tv_ps_name = (TextView) findViewById(R.id.tvpsname_spot_response_xml);
		tv_point_name = (TextView) findViewById(R.id.tvpointname_spot_response_xml);
		tv_officer_pid = (TextView) findViewById(R.id.tvofficerpid_spot_response_xml);
		tv_officer_name = (TextView) findViewById(R.id.tvofficername_spot_response_xml);
		tv_driver_name = (TextView) findViewById(R.id.tvdrivename_spot_response_xml);
		tv_driver_fname = (TextView) findViewById(R.id.tvdrivefname_spot_response_xml);
		tv_driver_lic_num = (TextView) findViewById(R.id.tvdrvr_dlnum_spot_response_xml);
		tv_driver_contact_num = (TextView) findViewById(R.id.tvdrvr_conctnum_spot_response_xml);
		tv_sucess_text_header = (TextView) findViewById(R.id.textView_header_success_text);
		tv_total_pendingchallans_amnt = (TextView) findViewById(R.id.textView_total_pendingchalans);
		tv_total_paid_amnt = (TextView) findViewById(R.id.textView_total_paid_chalans);
		/* BUFFER DISPLAY START */
		tv_pendingchallans_buffer_display = (TextView) findViewById(R.id.textView_pendingchallans_bufferdisplay_spot_reponse_xml);
		tv_detained_items_buffer_display = (TextView) findViewById(R.id.textView_detaioneditems_bufferdisplay_spot_reponse_xml);
		tv_released_detained_items_buffer_display = (TextView) findViewById(R.id.textView_released_detaioneditems_bufferdisplay_spot_reponse_xml);
		tv_pendingchallans_buffer_display.setVisibility(View.GONE);
		tv_detained_items_buffer_display.setVisibility(View.GONE);
		tv_released_detained_items_buffer_display.setVisibility(View.GONE);
		/* BUFFER DISPLAY END */

		ll_violations = (LinearLayout) findViewById(R.id.ll_violation_details_spot_reponse_xml);
		ll_paid_challans = (LinearLayout) findViewById(R.id.Ll_selectedchallans_spotresponse_xml);
		rl_paid_challans_root = (RelativeLayout) findViewById(R.id.rl_selectedchallans_root_spotresponse_xml);
		ll_eticketnum = (LinearLayout) findViewById(R.id.ll_3);// to hide etckt
																// num in vhle
																// history
		rl_paid_challans_root.setVisibility(View.GONE);
		tv_total_pendingchallans_amnt.setVisibility(View.GONE);
		tv_total_paid_amnt.setVisibility(View.GONE);
		ll_eticketnum.setVisibility(View.GONE);

		tv_violation_main_header = (TextView) findViewById(R.id.textView_violation_main_header);
		tv_sub_header_left = (TextView) findViewById(R.id.textView_left_header);// violation
																				// or
																				// eTicket
																				// No
		tv_sub_header_right = (TextView) findViewById(R.id.textView_right_header);// Amount
																					// for
																					// both
		tv_sub_header_date = (TextView) findViewById(R.id.textView_selected_challans_date);

		btn_back_spot_res = (Button) findViewById(R.id.btnhome_spot_reponse_xml);
		btn_print_spot_res = (Button) findViewById(R.id.btnprint_spot_reponse_xml);

		if (ServiceHelper.final_spot_reponse_details.length > 0) {

			tv_regno.setText(!"NA"
					.equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[0]) ? ServiceHelper.final_spot_reponse_details[0]
					.toUpperCase() : "");
			tv_date_time
					.setText(""
							+ (!"NA".equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[1]) ? ServiceHelper.final_spot_reponse_details[1]
									.toUpperCase() : "")
							+ " "
							+ (!"NA".equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[2]) ? ServiceHelper.final_spot_reponse_details[2]
									.toUpperCase() : ""));
			tv_eticketno
					.setText(""
							+ (!"NA".equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[3]) ? ServiceHelper.final_spot_reponse_details[3]
									.toUpperCase() : ""));
			tv_ps_name
					.setText(""
							+ (!"NA".equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[4]) ? ServiceHelper.final_spot_reponse_details[4]
									.toUpperCase() : ""));
			tv_point_name
					.setText(""
							+ (!"NA".equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[5]) ? ServiceHelper.final_spot_reponse_details[5]
									.toUpperCase() : ""));
			tv_officer_pid
					.setText(""
							+ (!"NA".equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[6]) ? ServiceHelper.final_spot_reponse_details[6]
									.toUpperCase() : ""));
			tv_officer_name
					.setText(""
							+ (!"NA".equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[7]) ? ServiceHelper.final_spot_reponse_details[7]
									.toUpperCase() : ""));
			/*tv_driver_name
					.setText(""
							+ (!"NA".equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[8]) ? ServiceHelper.final_spot_reponse_details[8]
									.toUpperCase() : ""));*/
			tv_driver_name.setText(SpotChallan.et_drivername_iOD.getText().toString().toUpperCase());
			tv_driver_fname
					.setText(""
							+ (!"NA".equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[9]) ? ServiceHelper.final_spot_reponse_details[9]
									.toUpperCase() : ""));
			tv_driver_lic_num
					.setText(""
							+ (!"NA".equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[10]) ? ServiceHelper.final_spot_reponse_details[10]
									.toUpperCase() : ""));
			tv_driver_contact_num
					.setText(""
							+ (!"NA".equalsIgnoreCase(ServiceHelper.final_spot_reponse_details[11]) ? ServiceHelper.final_spot_reponse_details[11]
									.toUpperCase() : ""));
		}

		/* TO BIND DYNAMIC VIOLATIONS */
		getDynamicViolations();
		btn_back_spot_res.setOnClickListener(this);
		btn_print_spot_res.setOnClickListener(this);

	}

	private void getDynamicViolations() {
		// TODO Auto-generated method stub

		/* ONLY FOR SPOT_CHALLAN DYNAMIC VIOLATIONS WILL BE THERE */
		/* FOR VECHICLE_HISTORY THERE IS NO VIOLATIONS */

		/* TO SET SUB HEADER SPOT CHALLAN */
		if (Dashboard.check_vhleHistory_or_Spot.equals("spot")) {
			tv_header_sub_title.setText(""
					+ getResources().getString(R.string.spot_challan));

		}
		/* TO SET SUB HEADER CP ACT */
		if (Dashboard.check_vhleHistory_or_Spot.equals("towing")) {
			tv_header_sub_title.setText(""
					+ getResources().getString(R.string.towing_one_line));
		}

		if ((Dashboard.check_vhleHistory_or_Spot.equals("spot"))
				|| (Dashboard.check_vhleHistory_or_Spot.equals("towing"))) {
			Log.i("**getDynamicViolations**", ""
					+ ServiceHelper.final_spot_reponse_violations.length);

			tv_sucess_text_header.setText("" + getResources().getString(R.string.ticket_generated_successfully));

			tv_violation_main_header.setText(""
					+ getResources().getString(R.string.violation_details));// VIOLATION
																			// OR
																			// PENDING
																			// CHALLANS
			tv_sub_header_left.setText(""
					+ getResources().getString(R.string.violation));// VIOLATION
																	// OR TICKET
																	// NO
			tv_sub_header_right.setText(""
					+ getResources().getString(R.string.amnt));// AMOUNT
			tv_sub_header_date.setText(""
					+ getResources().getString(R.string.dates));

			ll_eticketnum.setVisibility(View.VISIBLE);// ETCKT NUM SHOW HERE
			tv_total_paid_amnt.setVisibility(View.GONE);

			Log.i("ServiceHelper.final_spot_reponse_violations.length ::", ""
					+ ServiceHelper.final_spot_reponse_violations.length);
			if (ServiceHelper.final_spot_reponse_violations.length > 0) {
				ll_vltns_dynamic = new LinearLayout[ServiceHelper.final_spot_reponse_violations.length];

				for (int i = 0; i < ServiceHelper.final_spot_reponse_violations.length; i++) {
					Log.i("DynamicViolations VILATAION NAME", ""
							+ ServiceHelper.final_spot_reponse_violations[i][3]
									.toString().trim()
							+ "---"
							+ ServiceHelper.final_spot_reponse_violations[i][2]
									.toString().trim());

					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, 1.0f);

					ll_vltns_dynamic[i] = new LinearLayout(
							getApplicationContext());
					ll_vltns_dynamic[i].setOrientation(LinearLayout.HORIZONTAL);
					ll_vltns_dynamic[i].setId(i);
					// ll_vltns_dynamic[i].setLayoutParams(new LayoutParams(
					// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

					/* FOR SETTING VIOLATION NAME */
					TextView[] tv_violation_name = new TextView[ServiceHelper.final_spot_reponse_violations.length];
					tv_violation_name[i] = new TextView(getApplicationContext());
					tv_violation_name[i].setId(i + 1);
					tv_violation_name[i].setText((i + 1)
							+ " . "
							+ ServiceHelper.final_spot_reponse_violations[i][3]
									.toString().trim());
					tv_violation_name[i].setTextAppearance(
							getApplicationContext(), R.style.navi_text_style);
					tv_violation_name[i].setLayoutParams(param);
					ll_vltns_dynamic[i].addView(tv_violation_name[i]);

					/* FOR SETTING VIOLATION AMOUNT */
					TextView[] tv_violation_amnt = new TextView[ServiceHelper.final_spot_reponse_violations.length];
					tv_violation_amnt[i] = new TextView(getApplicationContext());
					tv_violation_amnt[i].setId(i + 2);
					tv_violation_amnt[i].setText(""
							+ ServiceHelper.final_spot_reponse_violations[i][2]
									.toString().trim());
					tv_violation_amnt[i].setTextAppearance(
							getApplicationContext(), R.style.navi_text_style);
					tv_violation_amnt[i].setLayoutParams(param);
					tv_violation_amnt[i].setGravity(Gravity.CENTER);
					ll_vltns_dynamic[i].addView(tv_violation_amnt[i]);

					ll_violations.addView(ll_vltns_dynamic[i]);

				}
				/* PENDING CHALLANS BUFFER DISPLAY */
				if ((ServiceHelper.final_spot_reponse_master[3] != null)
						&& (!ServiceHelper.final_spot_reponse_master[3]
								.toString().trim().equals("NA"))) {

					tv_pendingchallans_buffer_display.setText(""
							+ ServiceHelper.final_spot_reponse_master[3]
									.toString().trim());
					tv_pendingchallans_buffer_display
							.setVisibility(View.VISIBLE);
				} else {
					tv_pendingchallans_buffer_display.setText("");
					tv_pendingchallans_buffer_display.setVisibility(View.GONE);
				}

				/* DETAINED ITEMS BUFFER DISPLAY */
				if ((ServiceHelper.final_spot_reponse_master[4] != null)
						&& (!ServiceHelper.final_spot_reponse_master[4]
								.toString().trim().equals("NA"))) {
					tv_detained_items_buffer_display.setText(""
							+ ServiceHelper.final_spot_reponse_master[4]
									.toString().trim());
					tv_detained_items_buffer_display
							.setVisibility(View.VISIBLE);
				} else {
					tv_detained_items_buffer_display.setText("");
					tv_detained_items_buffer_display.setVisibility(View.GONE);
				}

				/* RELEASED DETAINED ITEMS BUFFER DISPLAY */
				if ((ServiceHelper.final_spot_reponse_master[5] != null)
						&& (!ServiceHelper.final_spot_reponse_master[5]
								.toString().trim().equals("NA"))) {
					tv_released_detained_items_buffer_display.setText(""
							+ ServiceHelper.final_spot_reponse_master[5]
									.toString().trim());
					tv_released_detained_items_buffer_display
							.setVisibility(View.VISIBLE);
				} else {
					tv_released_detained_items_buffer_display.setText("");
					tv_released_detained_items_buffer_display
							.setVisibility(View.GONE);
				}

			}

		} else if ((Dashboard.check_vhleHistory_or_Spot
				.equals("vehiclehistory"))
				|| (Dashboard.check_vhleHistory_or_Spot
						.equals("releasedocuments"))) {
			ll_eticketnum.setVisibility(View.GONE);// ETCKT NUM HIDE HERE

			/* CASE FOR VEHICLE HISTORY */

			if (Dashboard.check_vhleHistory_or_Spot.equals("vehiclehistory")) {
				tv_header_sub_title.setText(""
						+ getResources().getString(R.string.vehicle_history));

				/* IN VEHICLE HISTORY START */
				/* final_spot_reponse_master[0]--PRINT */
				/* final_spot_reponse_master[1]--DISPLAY */
				/* final_spot_reponse_master[2]--PENING CHALLANS */
				/* final_spot_reponse_master[3]--PAID CHALLANS */
				/* final_spot_reponse_master[4]--DETAINED ITEMS */
				/* final_spot_reponse_master[5]--RELEASED DETAINED ITEMS */

				/* DETAINED ITEMS BUFFER DISPLAY */
				if ((ServiceHelper.final_spot_reponse_master[4] != null)
						&& (!ServiceHelper.final_spot_reponse_master[4]
								.toString().trim().equals("NA"))) {
					tv_detained_items_buffer_display.setText(""
							+ ServiceHelper.final_spot_reponse_master[4]
									.toString().trim());
					tv_detained_items_buffer_display
							.setVisibility(View.VISIBLE);
				} else {
					tv_detained_items_buffer_display.setText("");
					tv_detained_items_buffer_display.setVisibility(View.GONE);
				}

				/* RELEASED DETAINED ITEMS BUFFER DISPLAY */
				if ((ServiceHelper.final_spot_reponse_master[5] != null)
						&& (!ServiceHelper.final_spot_reponse_master[5]
								.toString().trim().equals("NA"))) {
					tv_released_detained_items_buffer_display.setText(""
							+ ServiceHelper.final_spot_reponse_master[5]
									.toString().trim());
					tv_released_detained_items_buffer_display
							.setVisibility(View.VISIBLE);
				} else {
					tv_released_detained_items_buffer_display.setText("");
					tv_released_detained_items_buffer_display
							.setVisibility(View.GONE);
				}
				/* IN VEHICLE HISTORY END */

			} else if (Dashboard.check_vhleHistory_or_Spot
					.equals("releasedocuments")) {
				tv_header_sub_title.setText(""
						+ getResources().getString(
								R.string.release_documents_one_line));

				/* IN RELEASE DOCUMENTS START */
				/* final_spot_reponse_master[0]--PRINT */
				/* final_spot_reponse_master[1]--DISPLAY */
				/* final_spot_reponse_master[2]--PENING CHALLANS */
				/* final_spot_reponse_master[3]--RELEASED DETAINED ITEMS */
				/* IN RELEASE DOCUMENTS END */

				/* RELEASED DETAINED ITEMS BUFFER DISPLAY */
				if ((ServiceHelper.final_spot_reponse_master[3] != null)
						&& (!ServiceHelper.final_spot_reponse_master[3]
								.toString().trim().equals("NA"))) {
					tv_released_detained_items_buffer_display.setText(""
							+ ServiceHelper.final_spot_reponse_master[3]
									.toString().trim());
					tv_released_detained_items_buffer_display
							.setVisibility(View.VISIBLE);
				} else {
					tv_released_detained_items_buffer_display.setText("");
					tv_released_detained_items_buffer_display
							.setVisibility(View.GONE);
				}
				/* IN VEHICLE HISTORY END */

			}

			tv_sucess_text_header.setText(""
					+ getResources().getString(R.string.response));
			// VIOLATION OR PENDING CHALLANS
			tv_violation_main_header.setText(""
					+ getResources().getString(R.string.pending_challans));
			// VIOLATION OR TICKET NO
			tv_sub_header_left.setText(""
					+ getResources().getString(R.string.eticket_no));
			tv_sub_header_right.setText(""
					+ getResources().getString(R.string.amnt));// AMOUNT
			tv_sub_header_date.setText(""
					+ getResources().getString(R.string.dates));

			Log.i("DISPLAYING PENDING CHALLANS REPONSE", ""
					+ ServiceHelper.final_spot_reponse_violations.length);
			Log.i("final_spot_reponse_violations", ""
					+ ServiceHelper.final_spot_reponse_violations);

			/*
			 * FOR DISPLAYING PENDING CHALLANS WITH E-TICKET NO & AMOUNT IN
			 * VECHICLE HISTORY
			 */
			if ((ServiceHelper.final_spot_reponse_violations.length > 0)
					&& (!ServiceHelper.final_spot_reponse_violations
							.equals("0"))) {

				ll_vltns_dynamic = new LinearLayout[ServiceHelper.final_spot_reponse_violations.length];

				for (int i = 0; i < ServiceHelper.final_spot_reponse_violations.length; i++) {
					Log.i("Dynamic PENINDG CHALLANS", ""
							+ ServiceHelper.final_spot_reponse_violations[i][0]
									.toString().trim()
							+ "---"
							+ ServiceHelper.final_spot_reponse_violations[i][1]
									.toString().trim());

					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.MATCH_PARENT, 1.0f);

					ll_vltns_dynamic[i] = new LinearLayout(
							getApplicationContext());
					ll_vltns_dynamic[i].setOrientation(LinearLayout.HORIZONTAL);
					ll_vltns_dynamic[i].setId(i);
					// ll_vltns_dynamic[i].setLayoutParams(new LayoutParams(
					// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

					/* FOR SETTING VIOLATION NAME */
					TextView[] tv_violation_name = new TextView[ServiceHelper.final_spot_reponse_violations.length];
					tv_violation_name[i] = new TextView(getApplicationContext());
					tv_violation_name[i].setId(i + 1);
					tv_violation_name[i].setText((i + 1)
							+ " . "
							+ ServiceHelper.final_spot_reponse_violations[i][0]
									.toString().trim());
					tv_violation_name[i].setTextAppearance(
							getApplicationContext(), R.style.navi_text_style);
					tv_violation_name[i].setGravity(Gravity.LEFT);
					tv_violation_name[i].setLayoutParams(param);

					ll_vltns_dynamic[i].addView(tv_violation_name[i]);

					/* PENDING CHALLAN DATE */
					TextView[] tv_violation_date = new TextView[ServiceHelper.final_spot_reponse_violations.length];
					tv_violation_date[i] = new TextView(getApplicationContext());
					tv_violation_date[i].setId(i + 2);
					tv_violation_date[i].setText(""
							+ ServiceHelper.final_spot_reponse_violations[i][1]
									.toString().trim());
					tv_violation_date[i].setTextAppearance(
							getApplicationContext(), R.style.navi_text_style);
					tv_violation_date[i].setLayoutParams(param);
					tv_violation_date[i].setGravity(Gravity.CENTER);
					ll_vltns_dynamic[i].addView(tv_violation_date[i]);

					/* FOR SETTING VIOLATION AMOUNT */
					TextView[] tv_violation_amnt = new TextView[ServiceHelper.final_spot_reponse_violations.length];
					tv_violation_amnt[i] = new TextView(getApplicationContext());
					tv_violation_amnt[i].setId(i + 3);
					tv_violation_amnt[i].setText(""
							+ ServiceHelper.final_spot_reponse_violations[i][2]
									.toString().trim());
					tv_violation_amnt[i].setTextAppearance(
							getApplicationContext(), R.style.navi_text_style);

					tv_violation_amnt[i].setGravity(Gravity.RIGHT);
					tv_violation_amnt[i].setLayoutParams(param);
					ll_vltns_dynamic[i].addView(tv_violation_amnt[i]);

					ll_violations.addView(ll_vltns_dynamic[i]);
					total_pc_amnt = total_pc_amnt
							+ (Double
									.parseDouble(""
											+ ServiceHelper.final_spot_reponse_violations[i][2]
													.toString().trim()));
				}
				tv_total_pendingchallans_amnt.setVisibility(View.VISIBLE);
				tv_total_pendingchallans_amnt.setText("Total Pending Amount : "
						+ total_pc_amnt);

			} else {
				tv_total_pendingchallans_amnt.setVisibility(View.GONE);
			}

			if (Dashboard.check_vhleHistory_or_Spot.equals("vehiclehistory")) {

				/* FOR DISPLAYING PAID CHALLANS IN VECHICLE HISTORY */
				if ((ServiceHelper.selected_paid_challans_details.length > 0)
						&& (!ServiceHelper.selected_paid_challans_details
								.equals("NA"))) {
					rl_paid_challans_root.setVisibility(View.VISIBLE);
					tv_total_paid_amnt.setVisibility(View.VISIBLE);

					ll_paidChallans_dynamic = new LinearLayout[ServiceHelper.selected_paid_challans_details.length];

					for (int i = 0; i < ServiceHelper.selected_paid_challans_details.length; i++) {

						// Log.i("Dynamic PAID CHALLANS",
						// ""
						// + ServiceHelper.selected_paid_challans_details[i][0]
						// + "---"
						// +
						// ServiceHelper.selected_paid_challans_details[i][1]);

						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT, 1.0f);

						ll_paidChallans_dynamic[i] = new LinearLayout(
								getApplicationContext());
						ll_paidChallans_dynamic[i]
								.setOrientation(LinearLayout.HORIZONTAL);
						ll_paidChallans_dynamic[i].setId(i);
						// ll_paidChallans_dynamic[i].setLayoutParams(new
						// LayoutParams(
						// LayoutParams.MATCH_PARENT,
						// LayoutParams.WRAP_CONTENT));

						/* FOR SETTING PAID E-TICKET NUMBER */
						TextView[] tv_eticketno = new TextView[ServiceHelper.selected_paid_challans_details.length];
						tv_eticketno[i] = new TextView(getApplicationContext());
						tv_eticketno[i].setId(i + 1);
						tv_eticketno[i]
								.setText((i + 1)
										+ " . "
										+ ServiceHelper.selected_paid_challans_details[i][0]
												.toString().trim());
						tv_eticketno[i].setTextAppearance(
								getApplicationContext(),
								R.style.navi_text_style);
						tv_eticketno[i].setLayoutParams(param);
						ll_paidChallans_dynamic[i].addView(tv_eticketno[i]);

						/* FOR SETTING PAID AMOUNT */
						TextView[] tv_paidamnt = new TextView[ServiceHelper.selected_paid_challans_details.length];
						tv_paidamnt[i] = new TextView(getApplicationContext());
						tv_paidamnt[i].setId(i + 2);
						tv_paidamnt[i]
								.setText(""
										+ ServiceHelper.selected_paid_challans_details[i][1]
												.toString().trim());
						tv_paidamnt[i].setTextAppearance(
								getApplicationContext(),
								R.style.navi_text_style);
						tv_paidamnt[i].setLayoutParams(param);
						tv_paidamnt[i].setGravity(Gravity.CENTER);
						ll_paidChallans_dynamic[i].addView(tv_paidamnt[i]);

						ll_paid_challans.addView(ll_paidChallans_dynamic[i]);
						ll_paid_challans.setVisibility(View.VISIBLE);

						total_paid_amnt = total_paid_amnt
								+ (Double
										.parseDouble(""
												+ ServiceHelper.selected_paid_challans_details[i][1]
														.toString().trim()));

					}

					tv_total_paid_amnt.setText("Total Paid Amount : "
							+ total_paid_amnt);
				} else {
					rl_paid_challans_root.setVisibility(View.GONE);
					tv_total_paid_amnt.setVisibility(View.GONE);
				}
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_ENABLE_BT) {
			CheckBlueToothState();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/* CANCEL */
		case R.id.btnhome_spot_reponse_xml:

			startActivity(new Intent(SpotResponse.this, SpotChallan.class));
			this.finish();
			SpotChallan.tv_grand_total_spot.setText("") ;
			SpotChallan.tv_violation_amnt.setText("");
			SpotChallan.grand_total = 0.0;
			SpotChallan.tv_toal_amount_pending_challans.setText("0");
			SpotChallan.total_amount = 0;
			SpotChallan.sb_detained_items = null ;
			SpotChallan.imgSelected = "0";
			break;

		case R.id.btnprint_spot_reponse_xml:
			Log.i("PRINT", "" + ServiceHelper.final_spot_reponse_master[0]);

			if (bluetoothAdapter.isEnabled()) {
				if (address_spot.equals("btaddr")) {
					showToast("Please set bluetooth address in setting");
				} else {
					try {
						/*String printdata = bth_printer.font_Courier_41(""
								+ ServiceHelper.final_spot_reponse_master[0]);
						actual_printer.Call_PrintertoPrint("" + address_spot,
								"" + printdata);*/
						
						Bluetooth_Printer_3inch_ThermalAPI printer = new Bluetooth_Printer_3inch_ThermalAPI();

		                String print_data = printer.font_Courier_41(""+ ServiceHelper.final_spot_reponse_master[0]);
		                actual_printer.openBT(address_spot);
		                
		                actual_printer.printData(print_data);
		                 Thread.sleep(5000);
		                actual_printer.closeBT();
		                
					} catch (Exception e) {
						// TODO: handle exception
						showToast("Check Bluetooth Details!");
					}
				}
			} else {
				showToast("Enable Bluetooth");
			}

			break;

		default:
			break;
		}
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