package com.mtpv.mobilee_ticket;

import android.app.Activity;
import android.drm.DrmStore;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mtpv.mobilee_ticket_services.DateUtil;

public class RefusalToPly extends Activity {

    public static EditText et_RefsalName, et_RefsalCtntNo;
    Button btn_Refsl_OK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.refusaltofly_dialog);
        this.setFinishOnTouchOutside(false);
        et_RefsalName=findViewById(R.id.pass_RefsalName);
        et_RefsalCtntNo=findViewById(R.id.pass_RefsalCtntNo);
        btn_Refsl_OK=findViewById(R.id.btn_Refsl_OK);

        btn_Refsl_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cntctNo=et_RefsalCtntNo.getText().toString().trim();
                if (et_RefsalName.getText().toString().isEmpty()) {
                    et_RefsalName.setError("Please enter the Name !");
                    et_RefsalName.requestFocus();
                } else if (et_RefsalCtntNo.getText().toString().isEmpty()) {
                    et_RefsalCtntNo.setError("Please enter Phone Number !");
                    et_RefsalCtntNo.requestFocus();
                } else if (10!=et_RefsalCtntNo.getText().toString().trim().length() ||
                        new DateUtil().allCharactersSame(et_RefsalCtntNo.getText().toString().trim())|| ((cntctNo.charAt(0) != '7') &&
                        (cntctNo.charAt(0) != '8')&& (cntctNo.charAt(0) != '9') && (cntctNo.charAt(0) != '6'))){
                    et_RefsalCtntNo.setError("Please enter Valid Phone Number !");
                    et_RefsalCtntNo.requestFocus();
                }else {
                    SpotChallan.refsal_Info = et_RefsalName.getText().toString().trim()+"|"+et_RefsalCtntNo.getText().toString().trim();

                   finish();
                }
            }
        });




    }
}
