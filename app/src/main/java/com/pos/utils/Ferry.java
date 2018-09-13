package com.pos.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Ferry {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context context;

    // Shared pref mode ->default values
    int PRIVATE_MODE = 0;
    int Default_null = 0;
    String default_string="null";


    // Sharedpref file name
    private static final String PREF_NAME = "ferry";

    // All Shared Preferences Keys
    //Registration information
    public static final String KEY_FERRY_NAME     = "ferry_name";
    public static final String KEY_FERRY_REGION   = "ferry_region";
    public static final String KEY_FERRY_DISTRICT = "ferry_district";
    public static final String KEY_FERRY_ADDRESS  = "ferry_address";
    public static final String KEY_FERRY_ROUTE    = "ferry_route";

    // Constructor
    public Ferry(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //save ferry
    public void setKeyFerryName(String ferryName) {
        editor.putString(KEY_FERRY_NAME, ferryName);
        // commit changes
        editor.commit();
    }

    public void setKeyFerryRegion(String ferryRegion) {
        editor.putString(KEY_FERRY_REGION, ferryRegion);
        // commit changes
        editor.commit();
    }

    public void setKeyFerryDistrict(String ferryDistrict) {
        editor.putString(KEY_FERRY_DISTRICT, ferryDistrict);
        // commit changes
        editor.commit();
    }

    public void setKeyFerryAddress(String ferryAddress) {
        editor.putString(KEY_FERRY_ADDRESS, ferryAddress);
        // commit changes
        editor.commit();
    }

    public void setKeyFerryRoute(String ferryRoute) {
        editor.putString(KEY_FERRY_ROUTE, ferryRoute);
        // commit changes
        editor.commit();
    }


    /**
     * Get session
     * */

    public  String getKeyFerryName(){
        return pref.getString(KEY_FERRY_NAME,default_string);
    }

    public  String getKeyFerryRegion(){
        return pref.getString(KEY_FERRY_REGION,default_string);
    }

    public  String getKeyFerryDistrict(){
        return pref.getString(KEY_FERRY_DISTRICT,default_string);
    }

    public  String getKeyFerryAddress(){
        return pref.getString(KEY_FERRY_ADDRESS,default_string);
    }

    public  String getKeyFerryRoute(){
        return pref.getString(KEY_FERRY_ROUTE,default_string);
    }

}
