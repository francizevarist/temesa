package com.pos.helper;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;

public class DeviceMac {

    private Context context;

    public DeviceMac(Context context) {
        this.context = context;
    }

    public String getMacAddress(){
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }

}
