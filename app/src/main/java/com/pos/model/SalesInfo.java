package com.pos.model;

public class SalesInfo {

    private String serialno;
    private String tickettype;
    private String ticketcount;
    private String amount;

    public SalesInfo() {
    }

    public SalesInfo(String serialno, String tickettype, String ticketcount, String amount) {
        this.serialno = serialno;
        this.tickettype = tickettype;
        this.ticketcount = ticketcount;
        this.amount = amount;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getTickettype() {
        return tickettype;
    }

    public void setTickettype(String tickettype) {
        this.tickettype = tickettype;
    }

    public String getTicketcount() {
        return ticketcount;
    }

    public void setTicketcount(String ticketcount) {
        this.ticketcount = ticketcount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
