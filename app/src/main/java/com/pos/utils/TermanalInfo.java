package com.pos.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TermanalInfo {

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
    private static final String PREF_NAME = "terminalinfo";

    // All Shared Preferences Keys
    //Registration information
    public static final String KEY_TERMINAL_ID             = "terminalid";
    public static final String KEY_TERMINAL_NAME           = "terminalname";
    public static final String KEY_TERMINAL_STATUS         = "terminalstatus";
    public static final String KEY_TERMINAL_REGISTEREDDATE = "terminalregistereddate";
    public static final String KEY_SEQUANCENUMBER          = "sequancenumber";
    public static final String KEY_SYSTEM_TIME             = "systemtime";


    // Constructor
    public TermanalInfo(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //save terminal
    public void setKeyTerminalId(String terminalId) {
        editor.putString(KEY_TERMINAL_ID, terminalId);
        // commit changes
        editor.commit();
    }

    public void setKeyTerminalName(String terminalId) {
        editor.putString(KEY_TERMINAL_NAME, terminalId);
        // commit changes
        editor.commit();
    }

    public void setKeyTerminalStatus(String terminalId) {
        editor.putString(KEY_TERMINAL_STATUS, terminalId);
        // commit changes
        editor.commit();
    }

    public void setKeyTerminalRegistereddate(String terminalId) {
        editor.putString(KEY_TERMINAL_REGISTEREDDATE, terminalId);
        // commit changes
        editor.commit();
    }

    public void setKeySequancenumber(String sequancenumber) {
        editor.putString(KEY_SEQUANCENUMBER, sequancenumber);
        // commit changes
        editor.commit();
    }

    public void setKeySystemTime(String systemTime){
        editor.putString(KEY_SYSTEM_TIME, systemTime);
        // commit changes
        editor.commit();
    }


    /**
     * Get terminal
     * */
    public  String getKeyTerminalId(){
        return pref.getString(KEY_TERMINAL_ID,default_string);
    }

    public  String getKeyTerminalName(){
        return pref.getString(KEY_TERMINAL_NAME,default_string);
    }

    public  String getKeyTerminalStatus(){
        return pref.getString(KEY_TERMINAL_STATUS,default_string);
    }

    public  String getKeyTerminalRegistereddate(){
        return pref.getString(KEY_TERMINAL_REGISTEREDDATE,default_string);
    }

    public  String getKeySequancenumber(){
        return pref.getString(KEY_SEQUANCENUMBER,default_string);
    }

    public  String getKeySystemTime(){
        return pref.getString(KEY_SYSTEM_TIME,default_string);
    }

    /**
     * Clear Terminal details
     * */

    public void clearTerminal(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

}
