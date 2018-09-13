package com.pos.utils;

public class AppConfig {

    public static String URL_PREFIX = "http://41.59.225.123:8024/nsstms/index.php/engine/";
    //public static String URL_PREFIX = "http://192.168.43.243:8989/temesa1/index.php/engine/";

    public static String URL_VALIDATELOGINCREDENTIALS = URL_PREFIX+"validateLoginCredentials";
    public static String URL_DOWNLOADTICKETS = URL_PREFIX+"dowloadTicketCategory";
    public static String URL_TERMINALINFO = URL_PREFIX+"getTerminalInfo";
    public static String URL_UPLOADTICKET = URL_PREFIX+"uploadtickets";
    public static String URL_RESETPASSWORD = URL_PREFIX+"resetpass";

    //check status
    public static String URL_CHECK_LOGIN_CREDENTIALS = URL_PREFIX+"checklogincredential";

    public static String inTime = "00:00:00";
    public static String outTime = "23:59:59";

    public static String machNo = "0002";
    public static String TerminalID = "1";

    public static String BULK_LIMIT = "100";
}
