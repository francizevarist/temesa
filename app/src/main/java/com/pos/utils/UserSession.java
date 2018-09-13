package com.pos.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {

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
    private static final String PREF_NAME = "usersession";

    // All Shared Preferences Keys
    //Login information
    public static final String KEY_USER_STATUS        = "userstatus";
    public static final String KEY_USER_STATUSDESC    = "statusdesc";
    public static final String KEY_USER_NAME          = "username";
    public static final String KEY_USER_FULLNAME      = "fullname";
    public static final String KEY_USER_ROLE          = "role";
    public static final String KEY_USER_BALANCE       = "balance";
    public static final String KEY_LOGINTIME          = "logintime";
    public static final String KEY_SERVERTIME         = "servertime";
    public static final String KEY_TIMEZONE           = "timezone";


    //Account state [user sign in] [user sign out]
    private static final String IS_LOGIN = "IsLoggedIn";

    // Constructor
    public UserSession(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //save session
    public void setKeyUserStatus(String userStatus) {
        editor.putString(KEY_USER_STATUS, userStatus);
        // commit changes
        editor.commit();
    }

    public void setKeyUserStatusdesc(String userStatusdesc) {
        editor.putString(KEY_USER_STATUSDESC, userStatusdesc);
        // commit changes
        editor.commit();
    }

    public void setKeyUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        // commit changes
        editor.commit();
    }

    public void setKeyUserFullname(String userFullname) {
        editor.putString(KEY_USER_FULLNAME, userFullname);
        // commit changes
        editor.commit();
    }

    public void setKeyUserRole(String userRole) {
        editor.putString(KEY_USER_ROLE, userRole);
        // commit changes
        editor.commit();
    }

    public void setKeyUserBalance(String userBalance) {
        editor.putString(KEY_USER_BALANCE, userBalance);
        // commit changes
        editor.commit();
    }


    public void setKeyUserLoginTime(String loginTime) {
        editor.putString(KEY_LOGINTIME, loginTime);
        // commit changes
        editor.commit();
    }

    public void setKeyServertime(String servertime) {
        editor.putString(KEY_SERVERTIME, servertime);
        // commit changes
        editor.commit();
    }

    public void setKeyTimezone(String timezone) {
        editor.putString(KEY_TIMEZONE, timezone);
        // commit changes
        editor.commit();
    }



    /**
     * Get session
     * */

    public  String getKeyUserStatus(){
        return pref.getString(KEY_USER_STATUS,default_string);
    }

    public  String getKeyUserStatusdesc(){
        return pref.getString(KEY_USER_STATUSDESC,default_string);
    }

    public  String getKeyUserName(){
        return pref.getString(KEY_USER_NAME,default_string);
    }

    public  String getKeyUserFullname(){
        return pref.getString(KEY_USER_FULLNAME,default_string);
    }

    public  String getKeyUserRole(){
        return pref.getString(KEY_USER_ROLE,default_string);
    }

    public  String getKeyUserBalance(){
        return pref.getString(KEY_USER_BALANCE,default_string);
    }

    public  String getKeyLogintime(){
        return pref.getString(KEY_LOGINTIME,default_string);
    }

    public  String getKeyServertime(){
        return pref.getString(KEY_SERVERTIME,default_string);
    }

    public  String getKeyTimezone(){
        return pref.getString(KEY_TIMEZONE,default_string);
    }

    //sign in
    public void LoginUser(){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // commit changes
        editor.commit();
    }

    //check login
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    /**
     * Clear session details
     * */

    public void clearSession(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

}
