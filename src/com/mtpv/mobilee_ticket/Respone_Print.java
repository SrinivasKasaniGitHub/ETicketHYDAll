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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.digitsecure.dsprint.PrintHandler;
import com.mtpv.mobilee_ticket_services.DBHelper;
import com.mtpv.mobilee_ticket_services.ServiceHelper;
import com.mtpv.mobilee_ticket_services.Utils;

import java.sql.SQLException;

@SuppressLint("WorldReadableFiles")
public class Respone_Print extends Activity {

    static TextView text_to_print, tv_sucess_text_header;
    static Button back, print, make_paymnt;

    final int PROGRESS_DIALOG = 1;

    BluetoothAdapter bluetoothAdapter;
    @SuppressWarnings("unused")
    private BluetoothAdapter mBluetoothAdapter = null;
    @SuppressWarnings("unused")
    private static final int REQUEST_ENABLE_BT = 1;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String address_spot = "";

    public static String printTicket;
    DBHelper db;

    final AnalogicsThermalPrinter actual_printer = new AnalogicsThermalPrinter();
    final Bluetooth_Printer_3inch_ThermalAPI bth_printer = new Bluetooth_Printer_3inch_ThermalAPI();
    String challan_detail;
    ImageView img_logo;
    TextView officer_Name, officer_Cadre, officer_PS;


    @SuppressWarnings({"static-access", "deprecation"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_respone__print);

        text_to_print = (TextView) findViewById(R.id.text_to_print);
        text_to_print.setText("");

        img_logo = (ImageView) findViewById(R.id.img_logo);
        if (MainActivity.uintCode.equals("22")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        } else if (MainActivity.uintCode.equals("23")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        } else if (MainActivity.uintCode.equals("24")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        } else if (MainActivity.uintCode.equals("44")) { //44 Warangal
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        } else { //  69 Siddipet
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        }
        officer_Name = (TextView) findViewById(R.id.officer_Name);
        officer_Cadre = (TextView) findViewById(R.id.officer_cadre);
        officer_PS = (TextView) findViewById(R.id.officer_PS);

        officer_Name.setText(MainActivity.pidName + "(" + MainActivity.cadre_name + ")");
        officer_Cadre.setText(MainActivity.cadre_name);
        officer_PS.setText(MainActivity.psName);

        tv_sucess_text_header = (TextView) findViewById(R.id.textView_header_success_text);

        back = (Button) findViewById(R.id.back);
        print = (Button) findViewById(R.id.print);
        make_paymnt = (Button) findViewById(R.id.make_paymnt);

        Log.i("Dashboard.check_vhleHistory_or_Spot  :", ""
                + Dashboard.check_vhleHistory_or_Spot
                + SpotChallan.violation_code);

        if ((SpotChallan.cardFLG && SpotChallan.radioGroupButton_spotpaymentYes.isChecked())
                && (Dashboard.check_vhleHistory_or_Spot.equals("spot")
                || Dashboard.check_vhleHistory_or_Spot.equals("drunkdrive")
                || Dashboard.check_vhleHistory_or_Spot.equals("vehiclehistory"))) {
            Log.i("SpotChallan.cardFLG   1 :", "" + SpotChallan.cardFLG);
            if ((Dashboard.check_vhleHistory_or_Spot.equals("spot")
                    || Dashboard.check_vhleHistory_or_Spot.equals("vehiclehistory"))
                    && SpotChallan.violation_code != null
                    && (SpotChallan.et_aadharnumber_spot.getText().toString().trim().equals("")
                    || SpotChallan.violation_code.equals("30") || SpotChallan.violation_code.equals("64"))) {
                Log.i("Log   1 :", "*Called*");
                make_paymnt.setVisibility(View.GONE);
            } else if (SpotChallan.cardFLG && Dashboard.check_vhleHistory_or_Spot.equals("vehiclehistory")) {
                Log.i("Log   2 :", "*Called*");
                make_paymnt.setVisibility(View.GONE);
            } else if (SpotChallan.cardFLG && Dashboard.check_vhleHistory_or_Spot.equals("spot")) {
                Log.i("Log   3 :", "*Called*");
                make_paymnt.setVisibility(View.GONE);
            } else {
                Log.i("Log   4 :", "*Called*");
                make_paymnt.setVisibility(View.GONE);
            }
        } else {
            Log.i("SpotChallan.cardFLG   2 :", "" + SpotChallan.cardFLG);
            make_paymnt.setVisibility(View.GONE);
        }

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        String all_challans = sharedPreferences.getString("ALL_CHALLANS", "");
        String unitName = sharedPreferences.getString("UNIT_NAME", "");
        String unitCode = sharedPreferences.getString("UNIT_CODE", "");
        String ticket_No = sharedPreferences.getString("TICKET_DETAILS", "");
        String pending_amnt = sharedPreferences.getString(
                "PENDING_CHALLAN_AMNT", "");// PENDING_CHALLAN_AMNT
        String pending_challans = sharedPreferences.getString(
                "PENDING_CHALLANS", "");


        db = new DBHelper(getApplicationContext());

        if (Dashboard.check_vhleHistory_or_Spot.equals("vehiclehistory")) {
            try {
                db.open();
                db.deleteDuplicateRecords(DBHelper.duplicatePrint_table, "" + ""
                        + getResources().getString(R.string.dup_vhcle_hstry));
                if (SpotChallan.isDuplicatePrint) {
                    db.insertDuplicatePrintDetails("" + ServiceHelper.str_SameChlnDuplicatePrint, ""
                            + getResources().getString(R.string.dup_vhcle_hstry));
                } else {
                    db.insertDuplicatePrintDetails("" + ServiceHelper.final_spot_reponse_master[0], ""
                            + getResources().getString(R.string.dup_vhcle_hstry));
                }
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
                if (SpotChallan.isDuplicatePrint) {
                    db.insertDuplicatePrintDetails(""
                            + ServiceHelper.str_SameChlnDuplicatePrint, ""
                            + getResources().getString(R.string.dup_spot_challan));
                } else {
                    db.insertDuplicatePrintDetails(""
                            + ServiceHelper.final_spot_reponse_master[0], ""
                            + getResources().getString(R.string.dup_spot_challan));
                }
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
                if (SpotChallan.isDuplicatePrint) {
                    db.insertDuplicatePrintDetails(""
                            + ServiceHelper.str_SameChlnDuplicatePrint, ""
                            + getResources().getString(R.string.towing_one_line));

                } else {
                    db.insertDuplicatePrintDetails(""
                            + ServiceHelper.final_spot_reponse_master[0], ""
                            + getResources().getString(R.string.towing_one_line));
                }
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
                db.deleteDuplicateRecords(DBHelper.duplicatePrint_table, ""
                        + getResources().getString(R.string.release_documents_one_line));
                db.insertDuplicatePrintDetails("" + ServiceHelper.final_spot_reponse_master[0], "" + getResources().getString(
                        R.string.release_documents_one_line));
                db.close();

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                db.close();
            }
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBlueToothState();
        registerReceiver(null, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = preferences.edit();
        address_spot = preferences.getString("btaddress", "btaddr");


		/*if(!mtpv_SpecialDrive.specialDrive.equalsIgnoreCase(null)
                && mtpv_SpecialDrive.specialDrive.equalsIgnoreCase("SPDRIVE")) {

			text_to_print.setText(mtpv_SpecialDrive.specialDriveData);
			if (text_to_print.getText().toString().length() > 15) {
				tv_sucess_text_header.setText("" + getResources().getString(R.string.ticket_generated_successfully));
			}


			printTicket = text_to_print.getText().toString();
		}else
		{*/
        if (text_to_print.getText().toString().length() > 15) {
            tv_sucess_text_header.setText("" + getResources().getString(R.string.ticket_generated_successfully));
        }
        if (SpotChallan.isDuplicatePrint) {
            text_to_print.setText(ServiceHelper.str_SameChlnDuplicatePrint);
            printTicket = ServiceHelper.str_SameChlnDuplicatePrint;
        } else {
            text_to_print.setText(ServiceHelper.spot_final_res_status.split("\\^")[0].split("\\$")[0]);
            printTicket = ServiceHelper.spot_final_res_status.split("\\^")[0].split("\\$")[0];
        }






        //	}

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
    /*			if(!mtpv_SpecialDrive.specialDrive.equalsIgnoreCase(null)
                        && mtpv_SpecialDrive.specialDrive.equalsIgnoreCase("SPDRIVE")) {

					mtpv_SpecialDrive.specialDrive="";
					mtpv_SpecialDrive.specialDriveData="";
					finish();
					startActivity(new Intent(Respone_Print.this, mtpv_SpecialDrive.class));

				}else
				{*/
                finish();
                startActivity(new Intent(Respone_Print.this, SpotChallan.class));
                // Respone_Print.this.finish();
                SpotChallan.tv_grand_total_spot.setText("");
                SpotChallan.tv_violation_amnt.setText("");
                text_to_print.setText("");
                SpotChallan.grand_total = 0.0;
                SpotChallan.tv_toal_amount_pending_challans.setText("0");
                SpotChallan.total_amount = 0;
                SpotChallan.sb_detained_items = null;
                //}
            }
        });

        make_paymnt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editors = sharedPreferences.edit();
                editors.putString("CHALLAN_INFO", "" + challan_detail);
                Log.i("CHALLAN_INFO  :::::", "" + challan_detail);
                editors.commit();

                Intent popup = new Intent(getApplicationContext(), PopupDetails.class);
                startActivity(popup);
            }
        });

        print.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        new Async_Print().execute();
                    }
                });
            }
        });
    }

    public class Async_Print extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog = new ProgressDialog(Respone_Print.this);

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
            if (bluetoothAdapter.isEnabled()) {
                if (address_spot.equals("btaddr")) {
                    showToast("Please set bluetooth address in setting");
                } else {
                    try {

                        if (MainActivity.dev_Model.equalsIgnoreCase(Utils.dev_Model)) {
                            //  String prtText=printTicket.split("\\$")[1];
                            PrintHandler printHandler = new PrintHandler();
                            printHandler.printChallan(printTicket.split("\\$")[1]);

                        } else {
                            Bluetooth_Printer_3inch_ThermalAPI printer = new Bluetooth_Printer_3inch_ThermalAPI();
                            String print_data = printer.font_Courier_41(printTicket);
                            actual_printer.openBT(address_spot);
                            actual_printer.printData(print_data);
                            Thread.sleep(5000);
                            actual_printer.closeBT();
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                        runOnUiThread(new Runnable() {
                            public void run() {
                                showToast("Please set bluetooth Address in Settings");
                            }
                        });
                    }
                }
            } else {
                //showToast("Enable Bluetooth");
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
                ProgressDialog pd = ProgressDialog.show(this, "", "", true);
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
                    // showToast("Bluetooth is Enabled.");
                }
            } else {
                showToast("Bluetooth is NOT Enabled!");
            }
        }
    }

    private void showToast(final String msg) {
        // TODO Auto-generated method stub
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                View toastView = toast.getView();

                ViewGroup group = (ViewGroup) toast.getView();
                TextView messageTextView = (TextView) group.getChildAt(0);
                messageTextView.setTextSize(24);

                toastView.setBackgroundResource(R.drawable.toast_background);
                toast.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
        showToast("Please Click on Back Button to go Back");
    }

}








