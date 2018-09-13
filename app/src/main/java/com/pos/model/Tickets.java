package com.pos.model;

public class Tickets {

    public static final String TABLE_NAME = "tickets";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TICKET_ID = "ticket_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_ICON = "icon";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TICKET_ID + " TEXT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_PRICE + " TEXT,"
                    + COLUMN_ICON + " TEXT"
                    + ")";

    private int id;
    private String ticket_id;
    private String ticket_title;
    private String ticket_price;
    private String ticket_icon;

    public Tickets() {
    }

    public Tickets(int id, String ticket_id, String ticket_title, String ticket_price, String ticket_icon) {
        this.id = id;
        this.ticket_id = ticket_id;
        this.ticket_title = ticket_title;
        this.ticket_price = ticket_price;
        this.ticket_icon = ticket_icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getTicket_title() {
        return ticket_title;
    }

    public void setTicket_title(String ticket_title) {
        this.ticket_title = ticket_title;
    }

    public String getTicket_price() {
        return ticket_price;
    }

    public void setTicket_price(String ticket_price) {
        this.ticket_price = ticket_price;
    }

    public String getTicket_icon() {
        return ticket_icon;
    }

    public void setTicket_icon(String ticket_icon) {
        this.ticket_icon = ticket_icon;
    }
}
