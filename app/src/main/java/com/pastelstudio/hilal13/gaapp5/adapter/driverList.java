package com.pastelstudio.hilal13.gaapp5.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pastelstudio.hilal13.gaapp5.R;
import com.pastelstudio.hilal13.gaapp5.model.driver;

import java.util.List;

/**
 * Created by hilal13 on 09/02/18.
 */

public class driverList extends ArrayAdapter {

    private Activity context;
    List<driver> drv1;

    public driverList(Activity context, List<driver> drv1) {
        super(context, R.layout.layout_driver, drv1);
        this.context = context;
        this.drv1 = drv1;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.layout_driver, null, true);
        TextView textViewTujuan = (TextView) listViewItem.findViewById(R.id.textViewTujuan);
        TextView textViewDesk = (TextView) listViewItem.findViewById(R.id.textViewDeskripsi);

        driver driverObj = drv1.get(position);

        textViewTujuan.setText(driverObj.getNama());
        textViewDesk.setText(driverObj.getTelepon());

        return listViewItem;
    }

}
