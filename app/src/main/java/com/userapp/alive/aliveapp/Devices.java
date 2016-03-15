package com.userapp.alive.aliveapp;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Devansh on 7/3/2015.
 */
public class Devices {
    public String name , address;
    public BluetoothDevice deviceFound;

    Devices(String na_me,String add_ress,BluetoothDevice device_Found){
        this.name = na_me;
        this.address = add_ress;
        this.deviceFound = device_Found;

    }


}
