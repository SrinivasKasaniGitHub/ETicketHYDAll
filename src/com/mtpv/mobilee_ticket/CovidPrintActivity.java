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

public class CovidPrintActivity extends Activity implements View.OnClickListener {


    TextView Tv_printresponse;

    Button btn_print;
    Button btn_back;

    BluetoothAdapter bluetoothAdapter;
    private BluetoothAdapter mBluetoothAdapter = null;
    private static final int REQUEST_ENABLE_BT = 1;
    public static String printdata = "";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String address = "";

    final AnalogicsThermalPrinter actual_printer = new AnalogicsThermalPrinter();
    final Bluetooth_Printer_3inch_ThermalAPI bth_printer = new Bluetooth_Printer_3inch_ThermalAPI();

    DBHelper db;

    final int PROGRESS_DIALOG = 1;
    String address_spot = "";
    public static String printTicket;

    ImageView img_logo;
    TextView officer_Name, officer_Cadre, officer_PS;

    @SuppressLint({"DefaultLocale", "WorldReadableFiles"})
    @SuppressWarnings({"static-access", "deprecation"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.drunk_drive_response);
        loadUiComponents();

        img_logo = (ImageView) findViewById(R.id.img_logo);
        if (MainActivity.uintCode.equals("22")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.cyb_logo));
        } else if (MainActivity.uintCode.equals("23")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.htp_left));
        } else if (MainActivity.uintCode.equals("24")) {
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.rac_logo));
        } else if (MainActivity.uintCode.equals("44")) { //44 Warangal
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.wgl_logo));
        } else {//  69 Siddipet
            img_logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        }
        officer_Name = (TextView) findViewById(R.id.officer_Name);
        officer_Cadre = (TextView) findViewById(R.id.officer_cadre);
        officer_PS = (TextView) findViewById(R.id.officer_PS);

        officer_Name.setText(MainActivity.pidName + "(" + MainActivity.cadre_name + ")");
        officer_Cadre.setText(MainActivity.cadre_name);
        officer_PS.setText(MainActivity.psName);

        try {
            if (ServiceHelper.strCV_Responce.length() > 0 ) {
                Tv_printresponse.setText(ServiceHelper.strCV_Responce.split("\\^")[1]);
                printdata = ServiceHelper.strCV_Responce.split("\\^")[1];
            }else {
                Tv_printresponse.setText("No Data");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            Tv_printresponse.setText("No Data");
        }


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBlueToothState();
        registerReceiver(null, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = preferences.edit();
        address = preferences.getString("btaddress", "btaddr");
        // showToast(address);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_ENABLE_BT) {
            CheckBlueToothState();
        }
    }

    private void loadUiComponents() {
        Tv_printresponse = (TextView) findViewById(R.id.Tv_printresponse);
        btn_print = (Button) findViewById(R.id.btnprint_res_xml);
        btn_back = (Button) findViewById(R.id.btnhome_res_xml);
        btn_back.setOnClickListener(this);
        btn_print.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnprint_res_xml:


                new Async_Print().execute();

                break;
            case R.id.btnhome_res_xml:
                Intent intent=new Intent(this, CovidChallanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // startActivity(new Intent(this, Drunk_Drive.class));
                printdata = "";

                finish();
                break;

            default:
                break;
        }
    }

    public class Async_Print extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog = new ProgressDialog(CovidPrintActivity.this);

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }


        @Override
        protected String doInBackground(Void... params) {


            if (bluetoothAdapter.isEnabled()) {
                if (address.equals("btaddr")) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            showToast("Please set bluetooth address in setting");
                        }
                    });

                } else {
                    try {


                        if (ServiceHelper.strCV_Responce != null && ServiceHelper.strCV_Responce.length() > 0) {

                            if (MainActivity.dev_Model.equalsIgnoreCase(Utils.dev_Model)) {
                                PrintHandler printHandler = new PrintHandler();
                                printHandler.printChallan("" + ServiceHelper.strCV_Responce.split("\\^")[1]);
                            } else {
                                Bluetooth_Printer_3inch_ThermalAPI printer = new Bluetooth_Printer_3inch_ThermalAPI();

                                String print_data = printer.font_Courier_41("" + printdata);
                                actual_printer.openBT(address);

                                actual_printer.printData(print_data);
                                Thread.sleep(5000);
                                actual_printer.closeBT();
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                showToast("Check Bluetooth Details!");
                            }
                        });
                    }
                }
            } else {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showToast("Enable Bluetooth");
                    }
                });
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
        showToast("Pleas Click On Back Button to Get Back");
    }

}
