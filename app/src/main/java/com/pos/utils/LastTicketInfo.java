package com.pos.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LastTicketInfo {

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
    private static final String PREF_NAME = "lastticket";

    // All Shared Preferences Keys
    //Registration information
    public static final String KEY_FERRY_REGION       = "ferryregion";
    public static final String KEY_FERRY_NAME         = "ferryname";
    public static final String KEY_FERRY_ADDRESS      = "ferryaddress";
    public static final String KEY_FERRY_DISTRICT     = "ferrydistrict";
    public static final String KEY_FERRY_ROUTE        = "ferryroute";
    public static final String KEY_CASHIERNAME        = "cashiername";
    public static final String KEY_TERMINALID         = "terminalid";
    public static final String KEY_TICKETTITLE        = "tickettitle";
    public static final String KEY_TICKETPRICE        = "ticketprice";
    public static final String KEY_ITEM               = "item";
    public static final String KEY_QUANTITY           = "quantity";
    public static final String KEY_DATETIME           = "datetime";
    public static final String KEY_SERIALNUMBER       = "serialnnumber";
    public static final String KEY_SEQUANCENUMBER     = "sequancenumber";
    public static final String KEY_TRACKNO            = "trackno";


    // Constructor
    public LastTicketInfo(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //save ticket
    public void setKeyFerryRegion(String ferryRegion) {
        editor.putString(KEY_FERRY_REGION, ferryRegion);
        // commit changes
        editor.commit();
    }

    public void setKeyFerryName(String ferryName) {
        editor.putString(KEY_FERRY_NAME, ferryName);
        // commit changes
        editor.commit();
    }

    public void setKeyFerryAddress(String ferryAddress) {
        editor.putString(KEY_FERRY_ADDRESS, ferryAddress);
        // commit changes
        editor.commit();
    }

    public void setKeyFerryDistrict(String ferryDistrict) {
        editor.putString(KEY_FERRY_DISTRICT, ferryDistrict);
        // commit changes
        editor.commit();
    }


    public void setKeyFerryRoute(String ferryRoute) {
        editor.putString(KEY_FERRY_ROUTE, ferryRoute);
        // commit changes
        editor.commit();
    }

    public void setKeyCashiername(String cashiername) {
        editor.putString(KEY_CASHIERNAME, cashiername);
        // commit changes
        editor.commit();
    }

    public void setKeyTerminalid(String terminalid) {
        editor.putString(KEY_TERMINALID, terminalid);
        // commit changes
        editor.commit();
    }

    public void setKeyTickettitle(String cashiername) {
        editor.putString(KEY_TICKETTITLE, cashiername);
        // commit changes
        editor.commit();
    }

    public void setKeyTicketprice(String ticketprice) {
        editor.putString(KEY_TICKETPRICE, ticketprice);
        // commit changes
        editor.commit();
    }

    public void setKeyItem(String item) {
        editor.putString(KEY_ITEM, item);
        // commit changes
        editor.commit();
    }

    public void setKeyQuantity(String quantity) {
        editor.putString(KEY_QUANTITY, quantity);
        // commit changes
        editor.commit();
    }

    public void setKeyDatetime(String datetime) {
        editor.putString(KEY_DATETIME, datetime);
        // commit changes
        editor.commit();
    }

    public void setKeySerialnumber(String serialnumber) {
        editor.putString(KEY_SERIALNUMBER, serialnumber);
        // commit changes
        editor.commit();
    }

    public void setKeySequancenumber(String sequancenumber) {
        editor.putString(KEY_SEQUANCENUMBER, sequancenumber);
        // commit changes
        editor.commit();
    }

    public void setKeyTrackno(String trackno) {
        editor.putString(KEY_TRACKNO, trackno);
        // commit changes
        editor.commit();
    }

    /**
     * Get session
     * */

    public  String getKeyFerryRegion(){
        return pref.getString(KEY_FERRY_REGION,default_string);
    }

    public  String getKeyFerryName(){
        return pref.getString(KEY_FERRY_NAME,default_string);
    }

    public  String getKeyFerryAddress(){
        return pref.getString(KEY_FERRY_ADDRESS,default_string);
    }

    public  String getKeyFerryDistrict(){
        return pref.getString(KEY_FERRY_DISTRICT,default_string);
    }

    public  String getKeyFerryRoute(){
        return pref.getString(KEY_FERRY_ROUTE,default_string);
    }

    public  String getKeyCashiername(){
        return pref.getString(KEY_CASHIERNAME,default_string);
    }

    public  String getKeyTerminalid(){
        return pref.getString(KEY_TERMINALID,default_string);
    }

    public  String getKeyTickettitle(){
        return pref.getString(KEY_TICKETTITLE,default_string);
    }

    public  String getKeyTicketprice(){
        return pref.getString(KEY_TICKETPRICE,default_string);
    }

    public  String getKeyItem(){
        return pref.getString(KEY_ITEM,default_string);
    }

    public  String getKeyQuantity(){
        return pref.getString(KEY_QUANTITY,default_string);
    }

    public  String getKeyDatetime(){
        return pref.getString(KEY_DATETIME,default_string);
    }

    public  String getKeySerialnumber(){
        return pref.getString(KEY_SERIALNUMBER,default_string);
    }

    public  String getKeySequancenumber(){
        return pref.getString(KEY_SEQUANCENUMBER,default_string);
    }

    public  String getKeyTrackno(){
        return pref.getString(KEY_TRACKNO,default_string);
    }

    /**
     * Clear lastticket details
     * */

    public void clearLastticket(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

}
