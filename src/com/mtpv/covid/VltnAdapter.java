package com.mtpv.covid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mtpv.mobilee_ticket.R;

import java.util.List;

public class VltnAdapter extends BaseAdapter {
    public Context context;
    private static LayoutInflater inflater = null;
    List<VltnModel> list_VltnModel;
    String title;

    public VltnAdapter(Context context, List<VltnModel> list_VltnModel) {
        this.context = context;
        this.list_VltnModel = list_VltnModel;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list_VltnModel.size();
    }

    @Override
    public Object getItem(int i) {
        return list_VltnModel.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = null;

        view = inflater.inflate(R.layout.single_text_row_item, null);
        TextView txt_Title = view.findViewById(R.id.value);
        txt_Title.setText("" +list_VltnModel.get(i).getViolation());

        return view;
    }
}