package com.pos.view;

import android.app.AlarmManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.pos.R;
import com.pos.database.DatabaseHelper;
import com.pos.helper.AppTimestamp;
import com.pos.helper.DeviceMac;
import com.pos.helper.Encryption;
import com.pos.helper.MaterialProgressBar;
import com.pos.model.Tickets;
import com.pos.model.Users;
import com.pos.printer.PrintTicket;
import com.pos.utils.AppConfig;
import com.pos.utils.ApplicationContext;
import com.pos.utils.Ferry;
import com.pos.utils.Rememberme;
import com.pos.utils.Session;
import com.pos.utils.TermanalInfo;
import com.pos.utils.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    String url_validatelogindetails = AppConfig.URL_VALIDATELOGINCREDENTIALS;
    String url_downloadticket = AppConfig.URL_DOWNLOADTICKETS;
    String url_terminalinfo = AppConfig.URL_TERMINALINFO;
    String url_resetpassword = AppConfig.URL_RESETPASSWORD;

    //ui reference
    EditText edittext_username;
    EditText edittext_password;
    TextView text_errormessage;
    TextView text_successrmessage;
    RelativeLayout signin;
    MaterialProgressBar progressBar;
    RelativeLayout error;
    RelativeLayout success;

    List<Users> users;
    List<Tickets> tickets;
    UserSession userSession;
    Rememberme rememberme;
    Ferry ferry;
    TermanalInfo termanalInfo;

    private DatabaseHelper db;

    private ApplicationContext mContext;
    private AppTimestamp appTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        edittext_username = (EditText) findViewById(R.id.edittext_username);
        edittext_password = (EditText) findViewById(R.id.edittext_password);
        signin = (RelativeLayout) findViewById(R.id.signin);
        progressBar = (MaterialProgressBar) findViewById(R.id.progress);
        error = (RelativeLayout) findViewById(R.id.error);
        success = (RelativeLayout) findViewById(R.id.success);
        text_errormessage = (TextView) findViewById(R.id.text_errormessage);
        text_successrmessage = (TextView) findViewById(R.id.text_successmessage);

        appTimestamp = new AppTimestamp();
        mContext = (ApplicationContext) getApplicationContext();
        userSession = new UserSession(getApplicationContext());
        rememberme = new Rememberme(getApplicationContext());
        ferry = new Ferry(getApplicationContext());
        termanalInfo = new TermanalInfo(getApplicationContext());
        db = new DatabaseHelper(this);


        if(userSession.isLoggedIn()){
            signin();
        }

        users = new ArrayList<>();
        tickets = new ArrayList<>();
        signin.setOnClickListener(click_signin);

    }

    View.OnClickListener click_signin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //check first if it remember the user
            if(edittext_username.getText().toString().equals(rememberme.getKeyUserName())  &&  edittext_password.getText().toString().equals(rememberme.getKeyUserPassword())){
                //re-save the session
                userSession.setKeyUserStatus(rememberme.getKeyUserStatus());
                userSession.setKeyUserName(rememberme.getKeyUserName());
                userSession.setKeyUserStatusdesc(rememberme.getKeyUserStatusdesc());
                userSession.setKeyUserFullname(rememberme.getKeyUserFullname());
                userSession.setKeyUserBalance(rememberme.getKeyUserBalance());
                userSession.setKeyUserRole(rememberme.getKeyUserRole());
                userSession.setKeyUserLoginTime(Long.toString(appTimestamp.getCurrentTimeStamp()));
                userSession.LoginUser();

                edittext_username.getText().clear();
                edittext_password.getText().clear();
                signin();
            }else {
                getServerData(url_validatelogindetails);
            }
        }
    };


    private void getServerData(String url) {
        //start a new request
        progressBar.setVisibility(View.VISIBLE);
        StringRequest jsonReq = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null)
                {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, response.toString());
                    validateLoginCredentials(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", edittext_username.getText().toString());
                params.put("password", edittext_password.getText().toString());
                params.put("terminalid", termanalInfo.getKeyTerminalId());
                return params;
            }
        };

        // Adding request to volley request queue
        ApplicationContext.getInstance().addToRequestQueue(jsonReq);
    }

    private void DownloadTicket(final String loginresponse){

        StringRequest jsonReq = new StringRequest(Request.Method.POST,url_downloadticket, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null)
                {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, response.toString());

                    try {
                        JSONObject jsonObject = new JSONObject(loginresponse);
                        JSONObject jsondownload = new JSONObject(response);

                        if(jsondownload.getString("status").equals("200")){


                            //delete all item in table ticket
                            db.truncateTicket();

                            //save tickets database
                            JSONArray ticketArray = jsondownload.getJSONArray("ticketdetails");

                            for(int i=0; i<ticketArray.length(); i++){
                                JSONObject ticketObj = (JSONObject) ticketArray.get(i);
                                db.insertTicket(ticketObj.getString("id"),ticketObj.getString("ticket_title"), ticketObj.getString("ticket_price"),ticketObj.getString("ticket_icon"));
                            }

                            if(jsonObject.getString("status").equals("200")) {
                                //clear all validation pops
                                error.setVisibility(View.GONE);
                                success.setVisibility(View.GONE);

                                //save user sassion
                                //create session
                                userSession.setKeyUserStatus(jsonObject.getString("userstatus"));
                                userSession.setKeyUserStatusdesc(jsonObject.getString("statusDesc"));
                                userSession.setKeyUserFullname(jsonObject.getString("fullname"));
                                userSession.setKeyUserName(jsonObject.getString("username"));
                                userSession.setKeyUserBalance(jsonObject.getString("balance"));
                                userSession.setKeyUserRole(jsonObject.getString("role"));
                                userSession.setKeyUserLoginTime(Long.toString(appTimestamp.getCurrentTimeStamp()));
                                userSession.setKeyServertime(jsonObject.getString("servertime"));
                                userSession.LoginUser();
                                 //remember this user
                                //**loging credentials**
                                rememberme.setKeyUserName(edittext_username.getText().toString());
                                rememberme.setKeyUserPassword(edittext_password.getText().toString());

                                rememberme.setKeyUserStatus(jsonObject.getString("userstatus"));
                                rememberme.setKeyUserStatusdesc(jsonObject.getString("statusDesc"));
                                rememberme.setKeyUserFullname(jsonObject.getString("fullname"));
                                rememberme.setKeyUserName(jsonObject.getString("username"));
                                rememberme.setKeyUserBalance(jsonObject.getString("balance"));
                                rememberme.setKeyUserRole(jsonObject.getString("role"));
                                rememberme.setKeyUserLoginTime(Long.toString(appTimestamp.getCurrentTimeStamp()));
                                rememberme.setKeyServertime(jsonObject.getString("servertime"));

                                edittext_username.getText().clear();
                                edittext_password.getText().clear();
                                signin();
                            }else {
                                error.setVisibility(View.VISIBLE);
                                text_errormessage.setText(jsonObject.getString("statusDesc"));
                            }

                        }else {
                            error.setVisibility(View.VISIBLE);
                            text_errormessage.setText(jsondownload.getString("statusDesc"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("terminalid", termanalInfo.getKeyTerminalId());
                return params;
            }
        };

        // Adding request to volley request queue
        ApplicationContext.getInstance().addToRequestQueue(jsonReq);

    }

    private void TerminalInfo(final String terminalid_input){

        StringRequest jsonReq = new StringRequest(Request.Method.POST,url_terminalinfo, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null)
                {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, response.toString());

                    try {
                        JSONObject jsonterminalinfo = new JSONObject(response);
                        if(jsonterminalinfo.getString("status").equals("200")){

                            success.setVisibility(View.VISIBLE);
                            text_successrmessage.setText("Terminal ID Updated");

                          if(jsonterminalinfo.getString("terminalstatus").equalsIgnoreCase("Active")){
                              //Toast.makeText(getApplicationContext(),"good",Toast.LENGTH_LONG).show();
                              //clear current terminal
                              termanalInfo.clearTerminal();
                              //save terminal info
                              termanalInfo.setKeyTerminalId(jsonterminalinfo.getString("terminalid"));
                              termanalInfo.setKeyTerminalName(jsonterminalinfo.getString("name"));
                              termanalInfo.setKeyTerminalStatus(jsonterminalinfo.getString("terminalstatus"));
                              termanalInfo.setKeyTerminalRegistereddate(jsonterminalinfo.getString("registereddate"));
                              termanalInfo.setKeySequancenumber(jsonterminalinfo.getString("seq_no"));


                              JSONObject ferryObj = jsonterminalinfo.getJSONObject("ferry");
                              ferry.setKeyFerryName(ferryObj.getString("ferry_name"));
                              ferry.setKeyFerryRegion(ferryObj.getString("ferry_region"));
                              ferry.setKeyFerryDistrict(ferryObj.getString("ferry_district"));
                              ferry.setKeyFerryAddress(ferryObj.getString("ferry_address"));
                              ferry.setKeyFerryRoute(ferryObj.getString("ferry_route"));
                          }

                        }else {
                            error.setVisibility(View.VISIBLE);
                            text_errormessage.setText(jsonterminalinfo.getString("statusDesc"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("terminalid", terminalid_input);
                params.put("mac_addr", new DeviceMac(getApplicationContext()).getMacAddress());
                return params;
            }
        };

        // Adding request to volley request queue
        ApplicationContext.getInstance().addToRequestQueue(jsonReq);

    }


    private void ResetPassword(final String username, final String password, final DialogInterface dialog){
        StringRequest jsonReq = new StringRequest(Request.Method.POST,url_resetpassword, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                VolleyLog.d(TAG, "Response: " + response.toString());

                if (response != null)
                {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, response.toString());

                    try {
                        JSONObject jsonterminalinfo = new JSONObject(response);
                        if(jsonterminalinfo.getString("status").equals("200")) {
                            dialog.dismiss();
                            success.setVisibility(View.VISIBLE);
                            text_successrmessage.setText("Password Updated Successfuly");
                        }else {
                            Toast.makeText(getApplicationContext(),jsonterminalinfo.getString("statusDesc"),Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        // Adding request to volley request queue
        ApplicationContext.getInstance().addToRequestQueue(jsonReq);

    }


    private void signin(){
        Intent intent = new Intent(getApplicationContext(),ConnectAvtivity.class);
        startActivity(intent);
        finish();
    }


    private void validateLoginCredentials(String response) {

        try {
            //status
            JSONObject  jsonObject = new JSONObject(response);

            if(jsonObject.getString("status").equals("200")) {
                error.setVisibility(View.GONE);


                //check reset password
                switch (jsonObject.getString("resetpass")){
                    case "1":
                        showResetPassword(edittext_username.getText().toString());
                        break;
                    case "0":
                        //check use role
                        switch (jsonObject.getString("role")){
                            case "admin":
                                //config app
                                showTerminalConfig();
                                break;
                            case "cashier":
                                //login
                                if(jsonObject.getString("userstatus").equals("Not Active")){
                                    //user is blocked
                                    error.setVisibility(View.VISIBLE);
                                    text_errormessage.setText("This user is blocked");
                                }else {
                                    //download the ticket
                                    DownloadTicket(response);
                                }
                        }
                        break;
                }

            }else {
                error.setVisibility(View.VISIBLE);
                text_errormessage.setText(jsonObject.getString("statusDesc"));
            }


        }catch (JSONException e) {e.printStackTrace();}

    }

    private void showTerminalConfig(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_terminalid, null);
        dialogBuilder.setView(dialogView);

        final EditText editText_terminalid = (EditText) dialogView.findViewById(R.id.edittext_terminalid);

        //preload data to edittext
        if(termanalInfo.getKeyTerminalId().equals("null")){
            editText_terminalid.getText().clear();
        }else {
            editText_terminalid.setText(termanalInfo.getKeyTerminalId());
        }

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.hide();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //load information
                TerminalInfo(editText_terminalid.getText().toString());
            }
        });
        alertDialog.show();

    }


    private void showResetPassword(final String username){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_resetpassword, null);
        dialogBuilder.setView(dialogView);

        final EditText editText_reset = (EditText) dialogView.findViewById(R.id.edittext_password);
        final EditText editText_confirmresert = (EditText) dialogView.findViewById(R.id.edittext_repassword);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.hide();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //load information

                if(editText_reset.getText().toString().equals("")){
                    error.setVisibility(View.VISIBLE);
                    text_errormessage.setText("Fields Can't be empty");
                }else if(editText_confirmresert.toString().equals("")){
                    error.setVisibility(View.VISIBLE);
                    text_errormessage.setText("Fields Can't be empty");
                }else {
                    if(editText_reset.getText().toString().equals(editText_confirmresert.getText().toString())) {
                        ResetPassword(username, editText_confirmresert.getText().toString(),dialog);
                    }else {
                        error.setVisibility(View.VISIBLE);
                        text_errormessage.setText("Passwords do not match");
                    }
                }
            }
        });
        alertDialog.show();
    }

}
