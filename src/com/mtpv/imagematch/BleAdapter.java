package com.mtpv.imagematch;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mtpv.mobilee_ticket.R;

import java.util.List;

import app.justec.com.bleoperator.comm.BleDevice;

public class BleAdapter extends BaseAdapter {

    private List<BleDevice> blueNameList;
    private Context context;

    public BleAdapter(Context context, List<BleDevice> blueNameList) {
        this.blueNameList = blueNameList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return blueNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return blueNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_listbledevice, null);
        }
        TextView textView = convertView.findViewById(R.id.blue_name);

        textView.setText(blueNameList.get(position).getDevice().getName());
        return convertView;
    }

    public void add(BleDevice b) {
        blueNameList.add(b);
        notifyDataSetChanged();
    }

}
