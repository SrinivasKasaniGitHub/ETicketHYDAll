package com.mtpv.mobilee_ticket;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.mtpv.mobilee_ticket.R;

public class ShowToast {
	public static void showCustomToast(Context context, String msg) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View layout = inflater.inflate(R.layout.cust_toast,	(ViewGroup) ((Activity) context).findViewById(R.id.rl_custtoast));

		TextView text = (TextView) layout.findViewById(R.id.tvcusttoast);
		text.setText("" + msg);

		Toast toast = new Toast(context);
		toast.setView(layout);
		toast.setDuration(Toast.LENGTH_SHORT);
		// toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
		// 0, 108);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
	}

	public static void showNormalToast(Context context, String msg) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "" + msg, Toast.LENGTH_LONG).show();
	}
}
