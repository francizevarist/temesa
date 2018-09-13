package com.pos.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
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
    //Registration information
    public static final String KEY_USER_ID            = "userid";
    public static final String KEY_USER_NAME          = "username";
    public static final String KEY_USER_FULLNAME      = "userfullname";
    public static final String KEY_USEREMAIL          = "email";
    public static final String KEY_USERPASSWORD       = "password";
    public static final String KEY_USERPHONE          = "phone";
    public static final String KEY_LOGINTIME          = "logintime";

    //Account state [user sign in] [user sign out]
    private static final String IS_LOGIN = "IsLoggedIn";

    // Constructor
    public Session(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //save session
    public void setKeyUserId(String userId) {
        editor.putString(KEY_USER_ID, userId);
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

    public void setKeyUseremail(String userEmail) {
        editor.putString(KEY_USEREMAIL, userEmail);
        // commit changes
        editor.commit();
    }

    public void setKeyUserpassword(String userPassword) {
        editor.putString(KEY_USERPASSWORD, userPassword);
        // commit changes
        editor.commit();
    }

    public void setKeyUserphone(String userPhone) {
        editor.putString(KEY_USERPHONE, userPhone);
        // commit changes
        editor.commit();
    }

    public void setKeyUserLoginTime(String loginTime) {
        editor.putString(KEY_LOGINTIME, loginTime);
        // commit changes
        editor.commit();
    }

    /**
     * Get session
     * */

    public  String getKeyUserId(){
        return pref.getString(KEY_USER_ID,default_string);
    }

    public  String getKeyUserName(){
        return pref.getString(KEY_USER_NAME,default_string);
    }

    public  String getKeyUserFullname(){
        return pref.getString(KEY_USER_FULLNAME,default_string);
    }

    public  String getKeyUseremail(){
        return pref.getString(KEY_USEREMAIL,default_string);
    }

    public  String getKeyUserpassword(){
        return pref.getString(KEY_USERPASSWORD,default_string);
    }

    public  String getKeyLogintime(){
        return pref.getString(KEY_LOGINTIME,default_string);
    }

    public  String getKeyUserphone(){
        return pref.getString(KEY_USERPHONE,default_string);
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
