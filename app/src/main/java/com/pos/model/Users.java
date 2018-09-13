package com.pos.model;

public class Users {

    public static final String TABLE_NAME = "users";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_FULLNAME = "user_fullname";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_PASSWORD = "user_password";
    public static final String COLUMN_USER_PHONE = "user_phone";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_ID + " TEXT,"
                    + COLUMN_USER_NAME + " TEXT,"
                    + COLUMN_USER_FULLNAME + " TEXT,"
                    + COLUMN_USER_EMAIL + " TEXT,"
                    + COLUMN_USER_PASSWORD + " TEXT,"
                    + COLUMN_USER_PHONE + " TEXT"
                    + ")";

    private int id;
    private String user_id;
    private String user_name;
    private String user_fullname;
    private String user_email;
    private String user_password;
    private String user_phone;

    public Users() {
    }

    public Users(int id, String user_id, String user_name, String user_fullname, String user_email, String user_password, String user_phone) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_fullname = user_fullname;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_phone = user_phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
}
