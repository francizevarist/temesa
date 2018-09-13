package com.pos.model;

public class LastTicket {

    public static final String TABLE_NAME = "lastticket";

    public static final String COLUMN_ID              = "id";
    public static final String COLUMN_FERRYREGION     = "ferryregion";
    public static final String COLUMN_FERRYNAME       = "ferryname";
    public static final String COLUMN_FERRYADDRESS    = "ferryaddress";
    public static final String COLUMN_FERRYDISTRICT   = "ferrydistrict";
    public static final String COLUMN_FERRYROUTE      = "ferryroute";
    public static final String COLUMN_CASHIERNAME     = "cashiername";
    public static final String COLUMN_TERMINALID      = "terminalid";
    public static final String COLUMN_TICKETTITTLE    = "tickettitle";
    public static final String COLUMN_ITEM            = "item";
    public static final String COLUMN_QUANTITY        = "quantity";
    public static final String COLUMN_TICKETPRICE     = "ticketprice";
    public static final String COLUMN_DATETIME        = "datetime";
    public static final String COLUMN_SERIALNUMBER    = "serialnnumber";
    public static final String COLUMN_SEQUANCENUMBER  = "sequancenumber";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FERRYREGION + " TEXT,"
                    + COLUMN_FERRYNAME + " TEXT,"
                    + COLUMN_FERRYADDRESS + " TEXT"
                    + COLUMN_FERRYDISTRICT + " TEXT"
                    + COLUMN_FERRYROUTE + " TEXT"
                    + COLUMN_CASHIERNAME + " TEXT"
                    + COLUMN_TERMINALID + " TEXT"
                    + COLUMN_TICKETTITTLE + " TEXT"
                    + COLUMN_ITEM + " TEXT"
                    + COLUMN_QUANTITY + " TEXT"
                    + COLUMN_TICKETPRICE + " TEXT"
                    + COLUMN_DATETIME + " TEXT"
                    + COLUMN_SERIALNUMBER + " TEXT"
                    + COLUMN_SEQUANCENUMBER + " TEXT"
                    + ")";

    private int id;
    public String ferryregion;
    public String ferryname;
    public String ferryaddress;
    public String ferrydistrict;
    public String ferryroute;
    public String cashiername;
    public String terminalid;
    public String tickettitle;
    public String itemticket;
    public String quantityticket;
    public String ticketprice;
    public String datetimeticket;
    public String serialnnumber;
    public String sequancenumber;

    public LastTicket() {
    }

    public LastTicket(String ferryregion,
                      String ferryname,
                      String ferryaddress,
                      String ferrydistrict,
                      String ferryroute,
                      String cashiername,
                      String terminalid,
                      String tickettitle,
                      String itemticket,
                      String quantityticket,
                      String ticketprice,
                      String datetimeticket,
                      String serialnnumber,
                      String sequancenumber) {
        this.ferryregion = ferryregion;
        this.ferryname = ferryname;
        this.ferryaddress = ferryaddress;
        this.ferrydistrict = ferrydistrict;
        this.ferryroute = ferryroute;
        this.cashiername = cashiername;
        this.terminalid = terminalid;
        this.tickettitle = tickettitle;
        this.itemticket = itemticket;
        this.quantityticket = quantityticket;
        this.ticketprice = ticketprice;
        this.datetimeticket = datetimeticket;
        this.serialnnumber = serialnnumber;
        this.sequancenumber = sequancenumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFerryregion() {
        return ferryregion;
    }

    public void setFerryregion(String ferryregion) {
        this.ferryregion = ferryregion;
    }

    public String getFerryname() {
        return ferryname;
    }

    public void setFerryname(String ferryname) {
        this.ferryname = ferryname;
    }

    public String getFerryaddress() {
        return ferryaddress;
    }

    public void setFerryaddress(String ferryaddress) {
        this.ferryaddress = ferryaddress;
    }

    public String getFerrydistrict() {
        return ferrydistrict;
    }

    public void setFerrydistrict(String ferrydistrict) {
        this.ferrydistrict = ferrydistrict;
    }

    public String getFerryroute() {
        return ferryroute;
    }

    public void setFerryroute(String ferryroute) {
        this.ferryroute = ferryroute;
    }

    public String getCashiername() {
        return cashiername;
    }

    public void setCashiername(String cashiername) {
        this.cashiername = cashiername;
    }

    public String getTerminalid() {
        return terminalid;
    }

    public void setTerminalid(String terminalid) {
        this.terminalid = terminalid;
    }

    public String getTickettitle() {
        return tickettitle;
    }

    public void setTickettitle(String tickettitle) {
        this.tickettitle = tickettitle;
    }

    public String getItemticket() {
        return itemticket;
    }

    public void setItemticket(String itemticket) {
        this.itemticket = itemticket;
    }

    public String getQuantityticket() {
        return quantityticket;
    }

    public void setQuantityticket(String quantityticket) {
        this.quantityticket = quantityticket;
    }

    public String getTicketprice() {
        return ticketprice;
    }

    public void setTicketprice(String ticketprice) {
        this.ticketprice = ticketprice;
    }

    public String getDatetimeticket() {
        return datetimeticket;
    }

    public void setDatetimeticket(String datetimeticket) {
        this.datetimeticket = datetimeticket;
    }

    public String getSerialnnumber() {
        return serialnnumber;
    }

    public void setSerialnnumber(String serialnnumber) {
        this.serialnnumber = serialnnumber;
    }

    public String getSequancenumber() {
        return sequancenumber;
    }

    public void setSequancenumber(String sequancenumber) {
        this.sequancenumber = sequancenumber;
    }
}
