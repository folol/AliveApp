package com.userapp.alive.aliveapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Devansh on 6/29/2015.
 */
public class DeviceList extends ArrayAdapter<Devices>{
        DeviceList(Context context,ArrayList<Devices> devices){
            super(context,0,devices);
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Devices device = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.device_name);
        TextView tvHome = (TextView) convertView.findViewById(R.id.device_add);
        // Populate the data into the template view using the data object
        tvName.setText(device.name);
        tvHome.setText(device.address);
        // Return the completed view to render on screen
        return convertView;


    }
}