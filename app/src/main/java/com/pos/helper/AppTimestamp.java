package com.pos.helper;

import com.pos.utils.AppConfig;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AppTimestamp {

    private String date;

    public AppTimestamp() {
    }

    public void inputDate(String date){
        this.date = date;
    }

    public Long getStartTimeStamp(){
        String input = date+" "+ AppConfig.inTime;
        Timestamp ts = Timestamp.valueOf(input);
        //return start timestamp for particular date
        return ts.getTime();
    }

    public Long getEndTimeStamp(){
        String input = date+" "+AppConfig.outTime;
        Timestamp ts = Timestamp.valueOf(input);
        //return end timestamp for particular date
        return ts.getTime();
    }

    public Long getCurrentTimeStamp(){
        return new Timestamp(System.currentTimeMillis()).getTime();
    }

    public String getDate(Long timestamp){
        Timestamp ts = new Timestamp(timestamp);
        Date date = new Date(ts.getTime());
        //String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa").format(date);
        String dateTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return dateTime;
    };

    public String getDateTime(Long timestamp){
        Timestamp ts = new Timestamp(timestamp);
        Date date = new Date(ts.getTime());
        String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa").format(date);
        //String dateTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return dateTime;
    };

    public String getDateTimeSimple(Long timestamp){
        Timestamp ts = new Timestamp(timestamp);
        Date date = new Date(ts.getTime());
        String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
        //String dateTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return dateTime;
    };

    public String getTime(Long timestamp){
        Timestamp ts = new Timestamp(timestamp);
        Date date = new Date(ts.getTime());
        String dateTime = new SimpleDateFormat("hh:mm:ss").format(date);
        //String dateTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return dateTime;
    };

    /*public int getTimeDifference(Long serverTime){

        String servertime = Long.toString(serverTime);
        String[] st = servertime.split(":");
        String[] sy = getTime(getCurrentTimeStamp()).split(":");

        int hd = Integer.parseInt(st[0]) - Integer.parseInt(sy[0]);
        return  Integer.parseInt(st[0]);
    }*/

    public Long getDifference(String d1){
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = new GregorianCalendar();
            c.setTime(sf.parse(d1));
            return Math.abs(c.getTimeInMillis() - System.currentTimeMillis());
        }catch(Exception ex){
           ex.printStackTrace();
        }

        return 0L;
    }
    public int getTimeDifference(Long TS){

        int state = 0;
        int hour1 = 0;
        int hour2 = 0;

        int minute1 = 0;
        int minute2 = 0;

        String ts = getTime(TS);
        String[] byte_server = ts.split(":");


        String sy = getTime(getCurrentTimeStamp());
        String[] byte_system = sy.split(":");

        hour1 = Integer.parseInt(byte_server[0].replaceFirst("^0+(?!$)",""));
        hour2 = Integer.parseInt(byte_system[0].replaceFirst("^0+(?!$)",""));

        minute1 = Integer.parseInt(byte_server[1].replaceFirst("^0+(?!$)",""));
        minute2 = Integer.parseInt(byte_system[1].replaceFirst("^0+(?!$)",""));


        int hd = hour1 - hour2;
        int md = minute1 - minute2;

        if(hd==0){
            if( (md == 1 || md > 1) || (md == 1 || md < -1)){
                state = 0;
            }else {
                state = 1;
            }
        }else {
            state = 0;
        }

        return  state;
    }


}
