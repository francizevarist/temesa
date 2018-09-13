package com.pos.model;

public class Sales {

    public static final String TABLE_NAME = "sales";

    public static final String COLUMN_SALE_ID = "sale_id";
    public static final String COLUMN_SALE_TICKET_ID = "sale_ticket_id";
    public static final String COLUMN_SALE_AMOUNT = "sale_amount";
    public static final String COLUMN_SALE_SEQUANCENUMBER = "sale_sequancenumber";
    public static final String COLUMN_SALE_STATUS = "sale_status";
    public static final String COLUMN_SALE_PROCKEY = "sale_prockey";
    public static final String COLUMN_SALE_SERIALNUMBER = "sale_serialnumber";
    public static final String COLUMN_SALE_TIME = "sale_time";
    public static final String COLUMN_SALE_DATETIME = "sale_datetime";
    public static final String COLUMN_SALE_USERNAME = "sale_username";
    public static final String COLUMN_SALE_TERMINAL_ID = "sale_terminal_id";

    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_DENOM = "denom";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_SALE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SALE_TICKET_ID + " TEXT,"
                    + COLUMN_SALE_AMOUNT + " TEXT,"
                    + COLUMN_SALE_SEQUANCENUMBER + " TEXT,"
                    + COLUMN_SALE_STATUS + " TEXT,"
                    + COLUMN_SALE_PROCKEY + " TEXT,"
                    + COLUMN_SALE_SERIALNUMBER + " TEXT,"
                    + COLUMN_SALE_TIME + " TEXT,"
                    + COLUMN_SALE_DATETIME + " TEXT,"
                    + COLUMN_SALE_USERNAME + " TEXT,"
                    + COLUMN_SALE_TERMINAL_ID + " TEXT"
                    + ")";

    private String sale_id;
    private String sale_ticket_id;
    private String sale_amount;
    private String sale_sequancenumber;
    private String sale_status;
    private String sale_prockey;
    private String sale_serialnumber;
    private String sale_time;
    private String sale_datetime;
    private String sale_username;
    private String sale_terminal_id;

    public Sales() {
    }

    public Sales(String sale_ticket_id, String sale_amount, String sale_sequancenumber, String sale_status, String sale_prockey, String sale_serialnumber, String sale_time, String sale_datetime, String sale_username, String sale_terminal_id) {
        this.sale_ticket_id = sale_ticket_id;
        this.sale_amount = sale_amount;
        this.sale_sequancenumber = sale_sequancenumber;
        this.sale_status = sale_status;
        this.sale_prockey = sale_prockey;
        this.sale_serialnumber = sale_serialnumber;
        this.sale_time = sale_time;
        this.sale_datetime = sale_datetime;
        this.sale_username = sale_username;
        this.sale_terminal_id = sale_terminal_id;
    }

    public String getSale_id() {
        return sale_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }

    public String getSale_ticket_id() {
        return sale_ticket_id;
    }

    public void setSale_ticket_id(String sale_ticket_id) {
        this.sale_ticket_id = sale_ticket_id;
    }

    public String getSale_amount() {
        return sale_amount;
    }

    public void setSale_amount(String sale_amount) {
        this.sale_amount = sale_amount;
    }

    public String getSale_sequancenumber() {
        return sale_sequancenumber;
    }

    public void setSale_sequancenumber(String sale_sequancenumber) {
        this.sale_sequancenumber = sale_sequancenumber;
    }

    public String getSale_status() {
        return sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public String getSale_prockey() {
        return sale_prockey;
    }

    public void setSale_prockey(String sale_prockey) {
        this.sale_prockey = sale_prockey;
    }

    public String getSale_serialnumber() {
        return sale_serialnumber;
    }

    public void setSale_serialnumber(String sale_serialnumber) {
        this.sale_serialnumber = sale_serialnumber;
    }

    public String getSale_time() {
        return sale_time;
    }

    public void setSale_time(String sale_time) {
        this.sale_time = sale_time;
    }

    public String getSale_datetime() {
        return sale_datetime;
    }

    public void setSale_datetime(String sale_datetime) {
        this.sale_datetime = sale_datetime;
    }

    public String getSale_username() {
        return sale_username;
    }

    public void setSale_username(String sale_username) {
        this.sale_username = sale_username;
    }

    public String getSale_terminal_id() {
        return sale_terminal_id;
    }

    public void setSale_terminal_id(String sale_terminal_id) {
        this.sale_terminal_id = sale_terminal_id;
    }
}
