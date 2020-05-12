package com.mtpv.covid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mtpv.mobilee_ticket.R;

import java.util.List;

public class IDPrfAdapter extends BaseAdapter {
    public Context context;
    private static LayoutInflater inflater = null;
    List<IDProofs> list_IDProofs;
    String title;

    public IDPrfAdapter(Context context, List<IDProofs> list_IDProofs) {
        this.context = context;
        this.list_IDProofs = list_IDProofs;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list_IDProofs.size();
    }

    @Override
    public Object getItem(int i) {
        return list_IDProofs.get(i);
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
        txt_Title.setText("" + list_IDProofs.get(i).getIdName());

        return view;
    }
}