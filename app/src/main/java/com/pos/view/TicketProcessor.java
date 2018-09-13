package com.pos.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.pos.R;
import com.pos.adapter.SalesAdapter;
import com.pos.adapter.TicketsAdapter;
import com.pos.database.DatabaseHelper;
import com.pos.helper.AppTimestamp;
import com.pos.helper.DeviceMac;
import com.pos.model.Sales;
import com.pos.model.SalesInfo;
import com.pos.model.SalesReport;
import com.pos.model.Tickets;
import com.pos.utils.AppConfig;
import com.pos.utils.ApplicationContext;
import com.pos.utils.Ferry;
import com.pos.utils.GridSpacingItemDecoration;
import com.pos.utils.LastTicketInfo;
import com.pos.utils.MyDividerItemDecoration;
import com.pos.utils.RecyclerTouchListener;
import com.pos.utils.Rememberme;
import com.pos.utils.Session;
import com.pos.utils.TermanalInfo;
import com.pos.utils.UserSession;
import com.pos.utils.preDefiniation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TicketProcessor extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    String url_checklogincredentials = AppConfig.URL_CHECK_LOGIN_CREDENTIALS;
    String url_updateticket = AppConfig.URL_DOWNLOADTICKETS;
    String url_uploadticket = AppConfig.URL_UPLOADTICKET;

    private TicketsAdapter mAdapter;
    private List<Tickets> ticketsList = new ArrayList<>();
    private List<SalesReport> salesReports = new ArrayList<>();


    private RecyclerView recyclerView;
    private TextView noTicketsView;

    private DatabaseHelper db;

    UserSession session;
    Rememberme rememberme;
    Ferry ferry;
    TermanalInfo termanalInfo;
    LastTicketInfo lastTicketInfo;

    ApplicationContext applicationContext;
    AppTimestamp appTimestamp;

    Button report;
    Button settlement;
    Button signout;
    Button lastticket;
    Button rollout;

    private Calendar calendar;
    private int year, month, day;

    String zmonth;
    String zday;
    int denom = 0;

    //---------------------  background -----------------
    private Handler mHandler;
    private Handler bulkHandler;
    private int IntervalLoginCredentials = 10000; // 5 seconds by default, can be changed later
    private int IntervalPrintBulk = 3000; // 5 seconds by default, can be changed later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_processor);
        mHandler = new Handler();
        session = new UserSession(getApplicationContext());
        rememberme = new Rememberme(getApplicationContext());
        db = new DatabaseHelper(this);
        appTimestamp = new AppTimestamp();
        ferry = new Ferry(getApplicationContext());
        termanalInfo = new TermanalInfo(getApplicationContext());
        lastTicketInfo = new LastTicketInfo(getApplicationContext());
        applicationContext = (ApplicationContext) getApplicationContext();

        //starts background tasks:
        startBackground();

        recyclerView = findViewById(R.id.recycler_view);
        noTicketsView = findViewById(R.id.empty_notes_view);

        report = (Button) findViewById(R.id.button_report);
        settlement = (Button) findViewById(R.id.button_settlement);
        signout = (Button) findViewById(R.id.button_signout);
        lastticket = (Button) findViewById(R.id.button_printlastticket);
        rollout = (Button) findViewById(R.id.button_rollout);

        ticketsList.addAll(db.getAllTickets());

        mAdapter = new TicketsAdapter(this, ticketsList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(0), true));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyTickets();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if(ticketsList != null && ticketsList.size() > position) {
                    Tickets item = ticketsList.get(position);
                    showPrintDialog("Are you sure you want to print this Ticket", item);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        //Menu events
        report.setOnClickListener(click_report);
        settlement.setOnClickListener(click_settlement);
        signout.setOnClickListener(click_signout);
        lastticket.setOnClickListener(click_lastticket);
        rollout.setOnClickListener(click_rollout);

    }


    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyTickets() {
        // you can check notesList.size() > 0
        if (db.getTicketsCount() > 0) {
            noTicketsView.setVisibility(View.GONE);
        } else {
            noTicketsView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    /**
     * Menu events
     */
    View.OnClickListener click_report = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        //Log.e("SALES","date:"+appTimestamp.getDate(appTimestamp.getCurrentTimeStamp()));
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDialog(999);
        }
    };

    View.OnClickListener click_settlement = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(TicketProcessor.this);
            builder.setTitle("UPLOAD YOUR DATA");
            builder.setIcon(R.drawable.ic_error_outline_black_24dp);
            builder.setMessage("This action will upload your data");
            builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startBackground();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    };

    View.OnClickListener click_signout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(TicketProcessor.this);
            builder.setTitle("Sign out");
            builder.setIcon(R.drawable.ic_error_outline_black_24dp);
            builder.setMessage("Are you sure you want to sign out");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    session.clearSession();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                    exitProgram();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    };


    View.OnClickListener click_lastticket = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(TicketProcessor.this);
            builder.setTitle("Print Last Ticket");
            builder.setIcon(R.drawable.ic_error_outline_black_24dp);
            builder.setMessage("This action will print last ticket and upload its data");
            builder.setPositiveButton("Print", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(!lastTicketInfo.getKeyTrackno().equals(lastTicketInfo.getKeySerialnumber())) {
                        printReceipt(lastTicketInfo.getKeyFerryRegion(),
                                lastTicketInfo.getKeyFerryName(),
                                lastTicketInfo.getKeyFerryAddress(),
                                lastTicketInfo.getKeyFerryDistrict(),
                                lastTicketInfo.getKeyFerryRoute(),
                                lastTicketInfo.getKeyCashiername(),
                                lastTicketInfo.getKeyTerminalid(),
                                lastTicketInfo.getKeyTickettitle(),
                                lastTicketInfo.getKeyTicketprice(),
                                lastTicketInfo.getKeyItem(),
                                lastTicketInfo.getKeyQuantity(),
                                lastTicketInfo.getKeyDatetime(),
                                lastTicketInfo.getKeySerialnumber(),
                                lastTicketInfo.getKeySequancenumber(),
                                "last");
                    }else {
                        Toast.makeText(getApplicationContext(),"No last ticket to print",Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    };


    View.OnClickListener click_rollout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            printvoid();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            return true;
        }

        if (id == R.id.action_signout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void printReport(String date,String username){
        int sum_quantity = 0;
        int sum_amount = 0;

        // TODO Auto-generated method stub
        //Graylevel
        setGrayLevel(6);
        // content
        applicationContext.getObject().CON_PageStart(applicationContext.getState(),false,0,0);
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                1,0, 0, 0, "TEMESA - "+ferry.getKeyFerryRegion()+"\n", "gb2312");
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Sales Report\n", "gb2312");


        //************* collection **************

        //#item left Date
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Report Date\t", "gb2312");
        //#value center
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, date+"\n", "gb2312");


        //#item left Retailer
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Retailer Name\t", "gb2312");
        //#value center
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, session.getKeyUserFullname()+"\n", "gb2312");


        //#item left Username
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "User Name\t", "gb2312");
        //#value center
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, session.getKeyUserName()+"\n", "gb2312");


        //#item left Retailer
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Login Time\n", "gb2312");
        //#value center
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, appTimestamp.getDateTime(Long.parseLong(session.getKeyLogintime()))+"\n", "gb2312");

        //#item left Retailer
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "No of Denom : ", "gb2312");

        //calculate denomsize   #update: 26/08/2018 filter by username
        for(int i=0;i<db.getSalesReport(date).size(); i++) {
            SalesReport item = db.getSalesReport(date).get(i);
            if (date.equals(item.getTimestamp()) && username.equals(item.getUsername())) {
               // Toast.makeText(getApplicationContext(),item.getTimestamp()+" Size= "+db.getSalesReport().size()+" user = "+item.getUsername(),Toast.LENGTH_LONG).show();
                denom++;
            }
        }

        //#value center
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, denom+"\n", "gb2312");
        denom=0;


        //************* collection **************
        //#item left
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Denom\t\t", "gb2312");
        //#Quantity center
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Qty\t", "gb2312");

        //#price Right
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_RIGHT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Total\n", "gb2312");


        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "--------------------------------\n", "gb2312");

        db.getAllSales();

        for(int i=0;i<db.getSalesReport(date).size(); i++) {
            SalesReport item = db.getSalesReport(date).get(i);
            //#update: 26/08/2018 filter by username
            if(date.equals(item.getTimestamp()) && username.equals(item.getUsername())) {
                //Log.e("SALES","username:"+item.getUsername()+" Amount:"+item.getAmount()+" Count:"+item.getCount()+" Timestamp:"+item.getTimestamp());
                //#item left
                applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                        preDefiniation.AlignType.AT_LEFT.getValue());
                applicationContext.getObject().ASCII_PrintString(applicationContext.getState(), 0,
                        0, 1, 0, 0, item.getAmount() + "\t\t", "gb2312");
                //#Quantity center
                applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                        preDefiniation.AlignType.AT_CENTER.getValue());
                applicationContext.getObject().ASCII_PrintString(applicationContext.getState(), 0,
                        0, 1, 0, 0, item.getCount() + "\t", "gb2312");

                //#price Right
                applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                        preDefiniation.AlignType.AT_RIGHT.getValue());
                applicationContext.getObject().ASCII_PrintString(applicationContext.getState(), 0,
                        0, 1, 0, 0, Integer.parseInt(item.getAmount()) * Integer.parseInt(item.getCount()) + "\n", "gb2312");


                sum_amount = sum_amount + (Integer.parseInt(item.getAmount()) * Integer.parseInt(item.getCount()));
                sum_quantity = sum_quantity + Integer.parseInt(item.getCount());
            }
        }

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(), 0,
                0, 1, 0, 0, "--------------------------------\n", "gb2312");

        //#item left Total
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "TRANSACTIONS :\t\t", "gb2312");
        //#value center
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_RIGHT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, sum_quantity+"\n", "gb2312");


        //#item left Total
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "TOTAL AMOUNT :\t\t", "gb2312");
        //#value center
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_RIGHT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, sum_amount+".00"+"\n\n", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "** Powered by Noxyt ** \n", "gb2312");

        applicationContext.getObject().ASCII_CtrlFeedLines(applicationContext.getState(),4);
        applicationContext.getObject().ASCII_CtrlReset(applicationContext.getState());
        applicationContext.getObject().CON_PageEnd(applicationContext.getState(),applicationContext.getPrintway());

    }


    private void printReceipt(String ferryregion,
                              String ferryname,
                              String ferryaddress,
                              String ferrydistrict,
                              String ferryroute,
                              String cashiername,
                              String terminalid,
                              String tickettitle,
                              String ticketprice,
                              String item,
                              String quantity,
                              String datetime,
                              String serialnnumber,
                              String sequancenumber,
                              String printstate){
        // TODO Auto-generated method stub
        //Graylevel
        setGrayLevel(6);
        // content
        applicationContext.getObject().CON_PageStart(applicationContext.getState(),false,0,0);
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                1,0, 0, 0, "TEMESA - "+ferryregion+"\n", "gb2312");
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, ferryname+"\n", "gb2312");
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, ferryaddress+"\n", "gb2312");
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, ferrydistrict+" - "+ferryregion+"\n", "gb2312");


        switch (printstate){
            case "last":
                applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                        preDefiniation.AlignType.AT_CENTER.getValue());
                applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                        0, 1, 0, 0, "**** DUPLICATE  DUPLICATE ****\n\n", "gb2312");
                break;
        }


        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "\n\n", "gb2312");
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, ferryroute+"\n", "gb2312");
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Cashier : "+cashiername+"\n", "gb2312");
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Term ID : "+terminalid+"\n", "gb2312");
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Seq  no : "+sequancenumber+"\n", "gb2312");


        //************* collection **************
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "--------------------------------\n", "gb2312");


        //#item left
        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, tickettitle.toUpperCase()+"\n", "gb2312");


        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Item\t\t\t", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_RIGHT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, item+"\n", "gb2312");


        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Quantity\t\t", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_RIGHT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, quantity+"\n", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Price\t\t\t", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_RIGHT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, ticketprice+"\n", "gb2312");


        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "--------------------------------\n", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Item\t\t\t", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_RIGHT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, item+"\n", "gb2312");


        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "TOTAL\t\t\t", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_RIGHT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, ticketprice+"\n", "gb2312");


        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Cash\t\t\t", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_RIGHT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, ticketprice+"\n", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, datetime+"\t\t", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_RIGHT.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "#"+serialnnumber+"\n", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "Enjoy to Travel with us\n\n", "gb2312");

        applicationContext.getObject().ASCII_CtrlAlignType(applicationContext.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        applicationContext.getObject().ASCII_PrintString(applicationContext.getState(),0,
                0, 1, 0, 0, "** Powered by Noxyt ** ", "gb2312");
        applicationContext.getObject().ASCII_CtrlFeedLines(applicationContext.getState(),4);
        applicationContext.getObject().ASCII_CtrlReset(applicationContext.getState());
        applicationContext.getObject().CON_PageEnd(applicationContext.getState(),applicationContext.getPrintway());

        switch (printstate){
            case "last":
                lastTicketInfo.setKeyTrackno(lastTicketInfo.getKeySerialnumber());
                break;
        }

    }

    private void printvoid(){
        setBlackMark(true);
        applicationContext.PrintNLine(4);
    }

    private void setGrayLevel(int level){

        // TODO Auto-generated method stub
        byte[] setCmd = new byte[7];
        setCmd[0] = 0x1b;
        setCmd[1] = 0x06;
        setCmd[2] = 0x1b;
        setCmd[3] = (byte) 0xfd;
        setCmd[4] = (byte) level;// (level - 1);
        setCmd[5] = 0x1b;
        setCmd[6] = 0x16;
        applicationContext.getObject().ASCII_PrintBuffer(applicationContext.getState(), setCmd,
                setCmd.length);
    }


    private void setBlackMark(boolean isOn) {
        if (isOn) {
            applicationContext.getObject().ASCII_PrintBuffer(applicationContext.getState(), new byte[]{0x1B, 0x06},
                    2);
            int printBuffer = applicationContext.getObject().ASCII_PrintBuffer(applicationContext.getState(), new byte[]{0x1B, 0x2B, 0x01},
                    3);

            applicationContext.getObject().ASCII_PrintBuffer(applicationContext.getState(), new byte[]{0x1B, 0x16},
                    2);
        } else {
            applicationContext.getObject().ASCII_PrintBuffer(applicationContext.getState(), new byte[]{0x1B, 0x06},
                    2);
            int printBuffer = applicationContext.getObject().ASCII_PrintBuffer(applicationContext.getState(), new byte[]{0x1B, 0x2B, 0x00},
                    3);
            applicationContext.getObject().ASCII_PrintBuffer(applicationContext.getState(), new byte[]{0x1B, 0x16},
                    2);
        }

    }


    private void showPrintDialog(String message, final Tickets tickets){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(TicketProcessor.this);
        builder1.setTitle(tickets.getTicket_title());
        builder1.setMessage(message);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        //sales data
                        //String sale_ticket_id = tickets.getTicket_id();
                        String sale_amount = tickets.getTicket_price();
                        /*String sale_balance_before = session.getKeyUserBalance();
                        String sale_balance_after =  Integer.toString(Integer.parseInt(session.getKeyUserBalance())+Integer.parseInt(sale_amount));
                        String sale_prockey = "";
                        String sale_time = appTimestamp.getDate(Long.parseLong(session.getKeyServertime()));
                        String sale_username = session.getKeyUserName();
                        String sale_terminal_id = termanalInfo.getKeyTerminalId();
                        String serial_number = Long.toString(appTimestamp.getCurrentTimeStamp());*/

                        //preparedata for printing
                        String ferryregion = ferry.getKeyFerryRegion();
                        String ferryname = ferry.getKeyFerryName();
                        String ferryaddress = ferry.getKeyFerryAddress();
                        String ferrydistrict = ferry.getKeyFerryDistrict();
                        String ferryroute = ferry.getKeyFerryRoute();
                        String cashiername = session.getKeyUserFullname();
                        String username = session.getKeyUserName();
                        String terminalid = termanalInfo.getKeyTerminalId();
                        String tickettitle = tickets.getTicket_title();
                        String ticketprice = tickets.getTicket_price();
                        String item = "1";
                        String quantity = "1";
                        String datetime =  appTimestamp.getDateTime(appTimestamp.getCurrentTimeStamp());
                        String serialnnumber = terminalid+appTimestamp.getCurrentTimeStamp();

                        //sequance number
                        int sequance_number = Integer.parseInt(termanalInfo.getKeySequancenumber())+1;
                        termanalInfo.setKeySequancenumber(Integer.toString(sequance_number));

                        printReceipt(ferryregion,ferryname,ferryaddress,ferrydistrict,ferryroute,cashiername,terminalid,tickettitle,ticketprice,item,quantity,datetime,serialnnumber,Integer.toString(sequance_number),"new");

                        //save last ticket
                        lastTicketInfo.setKeyFerryRegion(ferryregion);
                        lastTicketInfo.setKeyFerryName(ferryname);
                        lastTicketInfo.setKeyFerryAddress(ferryaddress);
                        lastTicketInfo.setKeyFerryDistrict(ferrydistrict);
                        lastTicketInfo.setKeyFerryRoute(ferryroute);
                        lastTicketInfo.setKeyCashiername(cashiername);
                        lastTicketInfo.setKeyTerminalid(terminalid);
                        lastTicketInfo.setKeyTickettitle(tickettitle);
                        lastTicketInfo.setKeyTicketprice(ticketprice);
                        lastTicketInfo.setKeyItem(item);
                        lastTicketInfo.setKeyQuantity(quantity);
                        lastTicketInfo.setKeyDatetime(datetime);
                        lastTicketInfo.setKeySerialnumber(serialnnumber);
                        lastTicketInfo.setKeySequancenumber(Integer.toString(sequance_number));

                        //add sale
                        db.insertSale("",
                                tickets.getTicket_id(),
                                tickets.getTicket_price(),
                                Integer.toString(sequance_number),
                                "0",
                                "0",
                                serialnnumber,
                                appTimestamp.getDate(appTimestamp.getCurrentTimeStamp()),
                                datetime,
                                username,
                                terminalid);
                    }
                });


        builder1.setNeutralButton("Print Bulk",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        printBulk(tickets,AppConfig.BULK_LIMIT);
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断是否按下“BACK”(返回)键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 直接销毁程序
            exitProgram();
            // 返回true以表示消费事件，避免按默认的方式处理“BACK”键的事件
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitProgram() {
        // TODO Auto-generated method stub

        // 先finishConnectActivity
        try {
            ConnectAvtivity.mActivity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TicketProcessor.this.finish();
    }


    //date dialog
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {

            DatePickerDialog pickerDialog = new DatePickerDialog(this,R.style.AppTheme,myDateListener, year, month, day);
            long now = System.currentTimeMillis()-1000;
            pickerDialog.getDatePicker().setMaxDate(now+(1000*60*60*24*1)); //after one day from now
            return pickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {

        if(month/10 == 0){ zmonth = "0"+month; }else { zmonth=Integer.toString(month); }
        if(day/10 == 0){ zday = "0"+day; }else { zday=Integer.toString(day); }

        String getDate = new StringBuilder().append(year).append("-").append(zmonth).append("-").append(zday).toString();

        //updated : 28/08/2018
        if(db.getSalesReport(getDate).size()==0){
            Toast.makeText(getApplicationContext(),"No report to print",Toast.LENGTH_LONG).show();
        }else {
            printReport(getDate, session.getKeyUserName());
        }
    }

    //----------------- Thread : Run Background ----------------
    Runnable Runbackground = new Runnable() {
        @Override
        public void run() {
            try {
                  RequestcheckLogincredential(url_checklogincredentials,session.getKeyUserName());
                  DownloadTicket(url_updateticket,termanalInfo.getKeyTerminalId());
                  backgroundSalesUpload();
                //updateStatus(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(Runbackground, IntervalLoginCredentials);
            }
        }
    };

    void startBackground() {
        Runbackground.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(Runbackground);
    }

    //HTTP REQUESTS: RUN BACKGROUND
    private void RequestcheckLogincredential(String url_checklogincredentials,final String username){

            StringRequest jsonReq = new StringRequest(Request.Method.POST,url_checklogincredentials, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null)
                    {
                        Log.d(TAG, response.toString());
                        try {
                            JSONObject jsonterminalinfo = new JSONObject(response);
                            if(jsonterminalinfo.getString("status").equals("200")){

                                if(jsonterminalinfo.getString("userstatus").equalsIgnoreCase("Active")){
                                    if((appTimestamp.getDifference(jsonterminalinfo.getString("servertime"))/1000L) <= 120L){
                                        //Run app
                                    }else {
                                        Toast.makeText(getApplicationContext(),"Device time Error !!!",Toast.LENGTH_LONG).show();
                                        finish();
                                        exitProgram();
                                    }

                                }else {
                                    session.clearSession();
                                    rememberme.clearSession();
                                    finish();
                                    exitProgram();
                                }

                            }else if(jsonterminalinfo.getString("status").equals("202")){
                                for(int i=0; i<1; i++){
                                    Toast.makeText(getApplicationContext(),"Duplicate Terminal !!!!",Toast.LENGTH_LONG).show();
                                }
                                finish();exitProgram();
                            }else {

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
                    params.put("username",username);
                    params.put("term_id",termanalInfo.getKeyTerminalId());
                    params.put("mac_addr",new DeviceMac(getApplicationContext()).getMacAddress());
                    return params;
                }
            };

            // Adding request to volley request queue
            ApplicationContext.getInstance().addToRequestQueue(jsonReq);
    }


    private void DownloadTicket(final String url_updateticket, final String termanalid){

        StringRequest jsonReq = new StringRequest(Request.Method.POST,url_updateticket, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null)
                {
                    Log.d(TAG, response.toString());

                    try {
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

                            ticketsList.clear();
                            ticketsList.addAll(db.getAllTickets());
                            mAdapter.notifyDataSetChanged();

                        }else {
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
                params.put("terminalid", termanalid);
                return params;
            }
        };

        // Adding request to volley request queue
        ApplicationContext.getInstance().addToRequestQueue(jsonReq);

    }

    private void UploadTicket(final String id, String url_upload, final String sequenseno, final String tickettypeid, final String terminalid, final String amount, final String cashierusername, final String datetime, final String status, final String serialnumber ){
        StringRequest jsonReq = new StringRequest(Request.Method.POST,url_upload, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                VolleyLog.d(TAG, "Response: " + response.toString());

                if (response != null)
                {
                    Log.d(TAG, response.toString());
                    try {
                        JSONObject jsonuploadticket = new JSONObject(response);
                        if(jsonuploadticket.getString("status").equals("200")){
                            db.updateticketstatus("1",id);
                        }else {
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
                params.put("seqno",sequenseno);
                params.put("tickettypeid",tickettypeid);
                params.put("terminalid",terminalid);
                params.put("amount",amount);
                params.put("cashierusername",cashierusername);
                params.put("date",datetime);
                params.put("status",status);
                params.put("serialnumber",serialnumber);
                return params;
            }
        };

        // Adding request to volley request queue
        ApplicationContext.getInstance().addToRequestQueue(jsonReq);
    }


    private void backgroundSalesUpload(){
        List<Sales> uploadsales = new ArrayList<>();
        Random r = new Random();
        uploadsales.addAll(db.getRowsalesPending());

        if(uploadsales.size()> 0){
        UploadTicket(uploadsales.get(0).getSale_id(),url_uploadticket,
                uploadsales.get(0).getSale_sequancenumber(),
                uploadsales.get(0).getSale_ticket_id(),
                uploadsales.get(0).getSale_terminal_id(),
                uploadsales.get(0).getSale_amount(),
                uploadsales.get(0).getSale_username(),
                uploadsales.get(0).getSale_datetime(),
                uploadsales.get(0).getSale_status(),
                uploadsales.get(0).getSale_serialnumber());
        }
    }




    //**********************************************************************************************
    //**************************  BULK PRINT MODULE : UPDATE 28/10/2018  ***************************
    private void printBulk(final Tickets tickets, final String bulklimit){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TicketProcessor.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = (TicketProcessor.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_bulkticket, null);
        dialogBuilder.setView(dialogView);

        TextView title = (TextView) dialogView.findViewById(R.id.cart_title);
        final EditText quantity = (EditText) dialogView.findViewById(R.id.cart_quantity);
        ImageView remove = (ImageView) dialogView.findViewById(R.id.cart_remove);
        ImageView add = (ImageView) dialogView.findViewById(R.id.cart_add);
        RelativeLayout cancel = (RelativeLayout) dialogView.findViewById(R.id.cart_cancel);
        RelativeLayout save = (RelativeLayout) dialogView.findViewById(R.id.cart_save);

        TextView status = (TextView) dialogView.findViewById(R.id.status);

        //set title
        title.setText(tickets.getTicket_title());
        //save quantity default value
        quantity.setText("1");
        quantity.setSelection(quantity.getText().length());

        if(quantity.getText().toString().equals("")){
            quantity.setText("1");
            quantity.setSelection(quantity.getText().length());
        }

        //ontype check:values
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(quantity.getText().toString().equals("")){
                    quantity.setText("1");
                    quantity.setSelection(quantity.getText().length());
                }else {
                    int current_value = Integer.parseInt(quantity.getText().toString().trim());
                    int stocksize_value = Integer.parseInt(bulklimit.trim());
                    if(current_value > stocksize_value) {
                        quantity.setText(bulklimit);
                        quantity.setSelection(quantity.getText().length());
                    }
                }
            }
        });


        //remove item
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current_value = Integer.parseInt(quantity.getText().toString().trim());

                if(current_value > 1) {
                    int decreased_value = current_value - 1;
                    quantity.setText(Integer.toString(decreased_value));
                    quantity.setSelection(quantity.getText().length());
                }

            }
        });

        //add item
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current_value = Integer.parseInt(quantity.getText().toString().trim());

                int stocksize_value = Integer.parseInt(bulklimit.trim());
                if(current_value < stocksize_value) {
                    int added_value = current_value + 1;
                    quantity.setText(Integer.toString(added_value));
                    quantity.setSelection(quantity.getText().length());
                }

            }
        });

        //----------------------------------------------------------------------------------------

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                int q = Integer.parseInt(quantity.getText().toString());
                ProgressDialog progressDialog = new ProgressDialog(TicketProcessor.this);

                if(q==1){
                    progressDialog.setTitle("Printing (" + 1 + ") Ticket");
                }else {
                    progressDialog.setTitle("Printing (" + q + ") Tickets");
                }

                for(int i=0; i<q; i++){

                if(i==(q-1)){
                    progressDialog.hide();
                }

                try {

                    //preparedata for printing
                    String ferryregion = ferry.getKeyFerryRegion();
                    String ferryname = ferry.getKeyFerryName();
                    String ferryaddress = ferry.getKeyFerryAddress();
                    String ferrydistrict = ferry.getKeyFerryDistrict();
                    String ferryroute = ferry.getKeyFerryRoute();
                    String cashiername = session.getKeyUserFullname();
                    String username = session.getKeyUserName();
                    String terminalid = termanalInfo.getKeyTerminalId();
                    String tickettitle = tickets.getTicket_title();
                    String ticketprice = tickets.getTicket_price();
                    String item = "1";
                    String quantity = "1";
                    String datetime =  appTimestamp.getDateTime(appTimestamp.getCurrentTimeStamp());
                    String serialnnumber = terminalid+appTimestamp.getCurrentTimeStamp();

                    //sequance number
                    int sequance_number = Integer.parseInt(termanalInfo.getKeySequancenumber())+1;
                    termanalInfo.setKeySequancenumber(Integer.toString(sequance_number));

                    printReceipt(ferryregion,ferryname,ferryaddress,ferrydistrict,ferryroute,cashiername,terminalid,tickettitle,ticketprice,item,quantity,datetime,serialnnumber,Integer.toString(sequance_number),"new");

                    //save last ticket
                    lastTicketInfo.setKeyFerryRegion(ferryregion);
                    lastTicketInfo.setKeyFerryName(ferryname);
                    lastTicketInfo.setKeyFerryAddress(ferryaddress);
                    lastTicketInfo.setKeyFerryDistrict(ferrydistrict);
                    lastTicketInfo.setKeyFerryRoute(ferryroute);
                    lastTicketInfo.setKeyCashiername(cashiername);
                    lastTicketInfo.setKeyTerminalid(terminalid);
                    lastTicketInfo.setKeyTickettitle(tickettitle);
                    lastTicketInfo.setKeyTicketprice(ticketprice);
                    lastTicketInfo.setKeyItem(item);
                    lastTicketInfo.setKeyQuantity(quantity);
                    lastTicketInfo.setKeyDatetime(datetime);
                    lastTicketInfo.setKeySerialnumber(serialnnumber);
                    lastTicketInfo.setKeySequancenumber(Integer.toString(sequance_number));

                    //add sale
                    db.insertSale("",
                            tickets.getTicket_id(),
                            tickets.getTicket_price(),
                            Integer.toString(sequance_number),
                            "0",
                            "0",
                            serialnnumber,
                            appTimestamp.getDate(appTimestamp.getCurrentTimeStamp()),
                            datetime,
                            username,
                            terminalid);

                    Thread.sleep(IntervalPrintBulk);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                }
            }
        });
    }


}
