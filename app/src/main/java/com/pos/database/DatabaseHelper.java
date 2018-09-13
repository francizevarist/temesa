package com.pos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pos.model.LastTicket;
import com.pos.model.Sales;
import com.pos.model.SalesReport;
import com.pos.model.Tickets;
import com.pos.model.Users;
import com.pos.utils.Session;
import com.pos.utils.UserSession;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "pos_db";

    //session
    UserSession session;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.session = new UserSession(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Tickets.CREATE_TABLE);
        db.execSQL(Users.CREATE_TABLE);
        db.execSQL(Sales.CREATE_TABLE);
        db.execSQL(LastTicket.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Tickets.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Users.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Sales.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LastTicket.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    //************************** Users *************************************
    public long insertUser(String user_id,String user_name,String user_fullname,String user_email,String user_password,String user_phone) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Users.COLUMN_USER_ID, user_id);
        values.put(Users.COLUMN_USER_NAME, user_name);
        values.put(Users.COLUMN_USER_FULLNAME, user_fullname);
        values.put(Users.COLUMN_USER_EMAIL, user_email);
        values.put(Users.COLUMN_USER_PASSWORD, user_password);
        values.put(Users.COLUMN_USER_PHONE, user_phone);

        // insert row
        long id = db.insert(Users.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Users getUser(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Tickets.TABLE_NAME,
                new String[]{Users.COLUMN_ID, Users.COLUMN_USER_ID,Users.COLUMN_USER_NAME, Users.COLUMN_USER_FULLNAME,Users.COLUMN_USER_EMAIL,Users.COLUMN_USER_PASSWORD,Users.COLUMN_USER_PHONE},
                Users.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Users user = new Users(
                cursor.getInt(cursor.getColumnIndex(Users.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_NAME)),
                cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_FULLNAME)),
                cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_EMAIL)),
                cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_PASSWORD)),
                cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_PHONE)));

        // close the db connection
        cursor.close();

        return user;
    }

    public List<Users> getAllUsers() {
        List<Users> users = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Users.TABLE_NAME + " ORDER BY " +
                Users.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Users user = new Users();
                user.setId(cursor.getInt(cursor.getColumnIndex(Users.COLUMN_ID)));
                user.setUser_id(cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_ID)));
                user.setUser_name(cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_NAME)));
                user.setUser_fullname(cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_FULLNAME)));
                user.setUser_email(cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_EMAIL)));
                user.setUser_password(cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_PASSWORD)));
                user.setUser_phone(cursor.getString(cursor.getColumnIndex(Users.COLUMN_USER_PHONE)));

                users.add(user);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return users;
    }

    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + Users.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Users.TABLE_NAME, Users.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }



    //************************** Sales ***********************************
    public long insertSale(String sale_id,
                           String sale_ticket_id,
                           String sale_amount,
                           String sale_sequancenumber,
                           String sale_status,
                           String sale_prockey,
                           String sale_serialnumber,
                           String sale_time,
                           String sale_datetime,
                           String sale_username,
                           String sale_terminal_id) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Sales.COLUMN_SALE_TICKET_ID, sale_ticket_id);
        values.put(Sales.COLUMN_SALE_AMOUNT, sale_amount);
        values.put(Sales.COLUMN_SALE_SEQUANCENUMBER, sale_sequancenumber);
        values.put(Sales.COLUMN_SALE_STATUS, sale_status);
        values.put(Sales.COLUMN_SALE_PROCKEY, sale_prockey);
        values.put(Sales.COLUMN_SALE_SERIALNUMBER, sale_serialnumber);
        values.put(Sales.COLUMN_SALE_TIME, sale_time);
        values.put(Sales.COLUMN_SALE_DATETIME, sale_datetime);
        values.put(Sales.COLUMN_SALE_USERNAME, sale_username);
        values.put(Sales.COLUMN_SALE_TERMINAL_ID, sale_terminal_id);

        // insert row
        long id = db.insert(Sales.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public Sales getSale(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Sales.TABLE_NAME,
                new String[]{
                        Sales.COLUMN_SALE_ID,
                        Sales.COLUMN_SALE_TICKET_ID,
                        Sales.COLUMN_SALE_AMOUNT,
                        Sales.COLUMN_SALE_SEQUANCENUMBER,
                        Sales.COLUMN_SALE_STATUS,
                        Sales.COLUMN_SALE_PROCKEY,
                        Sales.COLUMN_SALE_SERIALNUMBER,
                        Sales.COLUMN_SALE_TIME,
                        Sales.COLUMN_SALE_USERNAME,
                        Sales.COLUMN_SALE_TERMINAL_ID},
                Sales.COLUMN_SALE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        /*Sales sale = new Sales(
                cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_ID)),
                cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_TICKET_ID)),
                cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_AMOUNT)),
                cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_PROCKEY)),
                cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_TIME)),
                cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_USERNAME)),
                cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_TERMINAL_ID)));*/

        // close the db connection
        cursor.close();

        return null;
    }

    public List<Sales> getAllSales() {
        List<Sales> sales = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Sales.TABLE_NAME + " ORDER BY " +
                Sales.COLUMN_SALE_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sales sale = new Sales();
                sale.setSale_id(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_TERMINAL_ID)));
                sale.setSale_ticket_id(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_TICKET_ID)));
                sale.setSale_amount(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_AMOUNT)));
                /*sale.setSale_balance_before(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_BALANCE_BEFORE)));
                sale.setSale_balance_after(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_BALANCE_AFTER)));*/
                sale.setSale_prockey(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_PROCKEY)));
                sale.setSale_time(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_TIME)));
                sale.setSale_username(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_USERNAME)));
                sale.setSale_terminal_id(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_TERMINAL_ID)));
                sales.add(sale);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return sales;
    }

    public List<SalesReport> getSalesReport(String time){
        List<SalesReport> salesReports = new ArrayList<>();

        // Select All Query
        //:updated --> filter ticket by time
        String selectQuery = "SELECT  "+Sales.COLUMN_SALE_USERNAME+","+Sales.COLUMN_SALE_TIME+","+Sales.COLUMN_SALE_AMOUNT+" "+Sales.COLUMN_DENOM+","+"count("+Sales.COLUMN_SALE_TICKET_ID
                +") quantity FROM " + Sales.TABLE_NAME + " WHERE  "+Sales.COLUMN_SALE_USERNAME+"='"+session.getKeyUserName()+"' AND "+ Sales.COLUMN_SALE_TIME +"='"+time+ "' GROUP BY  "+Sales.COLUMN_SALE_AMOUNT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SalesReport salesItem = new SalesReport();
                salesItem.setUsername(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_USERNAME)));
                salesItem.setTimestamp(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_TIME)));
                salesItem.setAmount(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_DENOM)));
                salesItem.setCount(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_QUANTITY)));
                salesReports.add(salesItem);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return salesReports;
    }

    public int getSalesCount() {
        String countQuery = "SELECT  * FROM " + Sales.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteSale(Sales sale) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Sales.TABLE_NAME, Sales.COLUMN_SALE_ID + " = ?",
                new String[]{String.valueOf(sale.getSale_id())});
        db.close();
    }


    public void updateprokey(String key){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "UPDATE "+Sales.TABLE_NAME+" SET "+Sales.COLUMN_SALE_PROCKEY+"='"+key+"' WHERE "+Sales.COLUMN_SALE_STATUS+"='0' AND "+Sales.COLUMN_SALE_PROCKEY+" = '0' ";
        //AND ROWID=(SELECT MIN(ROWID) FROM "+Sales.TABLE_NAME+")
        db.execSQL(Query);

        // close db connection
        db.close();
    }

    public void updateticketstatus(String status,String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "UPDATE "+Sales.TABLE_NAME+" SET "+Sales.COLUMN_SALE_STATUS+"='"+status+"' WHERE "+Sales.COLUMN_SALE_ID+" = '"+id+"' ";
        //AND ROWID=(SELECT MIN(ROWID) FROM "+Sales.TABLE_NAME+")
        db.execSQL(Query);

        // close db connection
        db.close();
    }

    public void resetprokeyvalue(String prokey){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "UPDATE "+Sales.TABLE_NAME+" SET "+Sales.COLUMN_SALE_PROCKEY+"='0' WHERE "+Sales.COLUMN_SALE_PROCKEY+" = '"+prokey+"' ";
        //AND ROWID=(SELECT MIN(ROWID) FROM "+Sales.TABLE_NAME+")
        db.execSQL(Query);

        // close db connection
        db.close();
    }

    public List<Sales> getRowsalesPending() {
        List<Sales> usersArrayList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Sales.TABLE_NAME +
                " WHERE " +
                Sales.COLUMN_SALE_STATUS  + "='0'"+" ORDER BY "+Sales.COLUMN_SALE_ID +" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sales item = new Sales();
                item.setSale_id(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_ID)));
                item.setSale_ticket_id(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_TICKET_ID)));
                item.setSale_amount(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_AMOUNT)));
                item.setSale_sequancenumber(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_SEQUANCENUMBER)));
                item.setSale_status(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_STATUS)));
                item.setSale_prockey(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_PROCKEY)));
                item.setSale_serialnumber(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_SERIALNUMBER)));
                item.setSale_time(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_TIME)));
                item.setSale_datetime(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_DATETIME)));
                item.setSale_username(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_USERNAME)));
                item.setSale_terminal_id(cursor.getString(cursor.getColumnIndex(Sales.COLUMN_SALE_TERMINAL_ID)));

                usersArrayList.add(item);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
        // return notes list
        return usersArrayList;
    }


    public int get_transactionSize(){
        return getSalesCount();
    }

    public int getTicketSolidCount(String ticket_id){
        String countQuery = "SELECT  * FROM " + Sales.TABLE_NAME + " WHERE "+Sales.COLUMN_SALE_TICKET_ID+"="+ticket_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int get_denomSize(){
        int denomCount[] = new int[getTicketsCount()];
        int sum = 0;
        Arrays.fill(denomCount,0);

        for(int i=0; i<getTicketsCount(); i++){
            denomCount[i] = getTicketSolidCount(getAllTickets().get(i).getTicket_id());
        }

        for(int i: denomCount){
            sum += i;
        }
        return sum;
    }

    public int get_denom(){
        return 0;
    }

    //************************** tkt *************************************
    public long insertTicket(String ticket_id,String title,String price,String icon) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Tickets.COLUMN_TICKET_ID, ticket_id);
        values.put(Tickets.COLUMN_TITLE, title);
        values.put(Tickets.COLUMN_PRICE, price);
        values.put(Tickets.COLUMN_ICON, icon);

        // insert row
        long id = db.insert(Tickets.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    //**insert last ticket**
    public long insertLastTicket(String ferryregion,
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
                                 String serialnnumber) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(LastTicket.COLUMN_FERRYREGION, ferryregion);
        values.put(LastTicket.COLUMN_FERRYNAME, ferryname);
        values.put(LastTicket.COLUMN_FERRYADDRESS, ferryaddress);
        values.put(LastTicket.COLUMN_FERRYDISTRICT, ferrydistrict);
        values.put(LastTicket.COLUMN_FERRYROUTE, ferryroute);
        values.put(LastTicket.COLUMN_CASHIERNAME, cashiername);
        values.put(LastTicket.COLUMN_TERMINALID, terminalid);
        values.put(LastTicket.COLUMN_TICKETTITTLE, tickettitle);
        values.put(LastTicket.COLUMN_TICKETPRICE, ticketprice);
        values.put(LastTicket.COLUMN_ITEM, item);
        values.put(LastTicket.COLUMN_QUANTITY, quantity);
        values.put(LastTicket.COLUMN_DATETIME, datetime);
        values.put(LastTicket.COLUMN_SERIALNUMBER, serialnnumber);

        // insert row
        long id = db.insert(LastTicket.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }



    public Tickets getTicket(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Tickets.TABLE_NAME,
                new String[]{Tickets.COLUMN_ID, Tickets.COLUMN_TICKET_ID,Tickets.COLUMN_TITLE, Tickets.COLUMN_PRICE},
                Tickets.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Tickets ticket = new Tickets(
                cursor.getInt(cursor.getColumnIndex(Tickets.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Tickets.COLUMN_TICKET_ID)),
                cursor.getString(cursor.getColumnIndex(Tickets.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(Tickets.COLUMN_PRICE)),
                cursor.getString(cursor.getColumnIndex(Tickets.COLUMN_ICON)));

        // close the db connection
        cursor.close();

        return ticket;
    }


    public List<Tickets> getAllTickets() {
        List<Tickets> tickets = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Tickets.TABLE_NAME + " ORDER BY " +
                Tickets.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Tickets ticket = new Tickets();
                ticket.setId(cursor.getInt(cursor.getColumnIndex(Tickets.COLUMN_ID)));
                ticket.setTicket_id(cursor.getString(cursor.getColumnIndex(Tickets.COLUMN_TICKET_ID)));
                ticket.setTicket_title(cursor.getString(cursor.getColumnIndex(Tickets.COLUMN_TITLE)));
                ticket.setTicket_price(cursor.getString(cursor.getColumnIndex(Tickets.COLUMN_PRICE)));
                ticket.setTicket_icon(cursor.getString(cursor.getColumnIndex(Tickets.COLUMN_ICON)));

                tickets.add(ticket);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return tickets;
    }


    public List<LastTicket> getLastTickets() {
        List<LastTicket> tickets = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + LastTicket.TABLE_NAME + " ORDER BY " +
                LastTicket.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                /*values.put(LastTicket.COLUMN_FERRYREGION, ferryregion);
                values.put(LastTicket.COLUMN_FERRYNAME, ferryname);
                values.put(LastTicket.COLUMN_FERRYADDRESS, ferryaddress);
                values.put(LastTicket.COLUMN_FERRYDISTRICT, ferrydistrict);
                values.put(LastTicket.COLUMN_FERRYROUTE, ferryroute);
                values.put(LastTicket.COLUMN_CASHIERNAME, cashiername);
                values.put(LastTicket.COLUMN_TERMINALID, terminalid);
                values.put(LastTicket.COLUMN_TICKETTITTLE, tickettitle);
                values.put(LastTicket.COLUMN_TICKETPRICE, ticketprice);
                values.put(LastTicket.COLUMN_ITEM, item);
                values.put(LastTicket.COLUMN_QUANTITY, quantity);
                values.put(LastTicket.COLUMN_DATETIME, datetime);
                values.put(LastTicket.COLUMN_SERIALNUMBER, serialnnumber);*/

                LastTicket ticket = new LastTicket();
                ticket.setId(cursor.getInt(cursor.getColumnIndex(LastTicket.COLUMN_ID)));
                ticket.setFerryregion(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_FERRYREGION)));
                ticket.setFerryname(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_FERRYNAME)));
                ticket.setFerryaddress(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_FERRYADDRESS)));
                ticket.setFerrydistrict(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_FERRYDISTRICT)));
                ticket.setFerryroute(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_FERRYROUTE)));
                ticket.setCashiername(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_CASHIERNAME)));
                ticket.setTerminalid(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_TERMINALID)));
                ticket.setTickettitle(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_TICKETTITTLE)));
                ticket.setTicketprice(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_TICKETPRICE)));
                ticket.setItemticket(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_ITEM)));
                ticket.setQuantityticket(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_QUANTITY)));
                ticket.setDatetimeticket(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_DATETIME)));
                ticket.setSerialnnumber(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_SERIALNUMBER)));
                ticket.setSequancenumber(cursor.getString(cursor.getColumnIndex(LastTicket.COLUMN_SEQUANCENUMBER)));

                tickets.add(ticket);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return tickets;
    }


    public int getTicketsCount() {
        String countQuery = "SELECT  * FROM " + Tickets.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateTicket(Tickets ticket) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tickets.COLUMN_TICKET_ID, ticket.getTicket_id());
        values.put(Tickets.COLUMN_TITLE, ticket.getTicket_title());
        values.put(Tickets.COLUMN_PRICE, ticket.getTicket_price());

        // updating row
        return db.update(Tickets.TABLE_NAME, values, Tickets.COLUMN_ID + " = ?",
                new String[]{String.valueOf(ticket.getTicket_id())});
    }

    public void deleteTicket(Tickets ticket) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Tickets.TABLE_NAME, Tickets.COLUMN_ID + " = ?",
                new String[]{String.valueOf(ticket.getId())});
        db.close();
    }




    public void truncateTicket() {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "DELETE FROM "+Tickets.TABLE_NAME;
        db.execSQL(Query);

        // close db connection
        db.close();
    }

}
