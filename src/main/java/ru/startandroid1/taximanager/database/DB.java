package ru.startandroid1.taximanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import ru.startandroid1.taximanager.R;

public class DB {

    private static final String DB_NAME = "taximanager"; //DataBase name
    private static final int DB_VERSION = 1; //Database version
    private static final String DB_TABLE_USERS = "users"; //Table "Users"
    private static final String DB_TABLE_REQUESTS = "requests"; //Table "Requests"
    private static final String DB_TABLE_FEEDBACKS = "feedbacks"; //Table "Feedbacks"

    public static final String COLUMN_ID = "_id"; //Table's column id

    //Table "Users"
    public static final String COLUMN_IMG = "img"; //User's icon
    public static final String COLUMN_LOGIN = "login"; //User's login
    public static final String COLUMN_PASSWORD = "password"; //User's password
    public static final String COLUMN_USERLEVEL = "userlevel"; //User's level(0 is Traffic manager, 1 is Driver, 2 is Customer)
    public static final String COLUMN_NAME = "username"; //User's name
    public static final String COLUMN_USERSTATE = "userstate"; //User's state(free, busy)

    //Table "Requests"
    public static final String COLUMN_ICON = "icon"; //User's icon
    public static final String COLUMN_STATE = "state"; //Request's state(processed, perfomed, completed, not completed, canceled)
    public static final String COLUMN_ROUTE_FROM = "routefrom"; //User's routefrom
    public static final String COLUMN_ROUTE_TO = "routeto"; //User's routeto
    public static final String COLUMN_DISTANCE = "distance"; //User's distance
    public static final String COLUMN_CUSTOMER = "customer"; //User's login
    public static final String COLUMN_DRIVER = "driver"; //User's driver
    public static final String COLUMN_COST = "cost"; //User's cost
    public static final String COLUMN_TIME = "time"; //User's time

    //Table "Feedbacks"
    public static final String COLUMN_PIC = "picture"; //User's picture
    public static final String COLUMN_USER = "user"; //User's name
    public static final String COLUMN_FEEDBACK = "feedback"; //User's feedback

    //Create table "Users"
    private static final String DB_CREATE_USERS =
            "create table " + DB_TABLE_USERS + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_IMG + " integer, " +
                    COLUMN_LOGIN + " text, " +
                    COLUMN_PASSWORD + " text, " +
                    COLUMN_USERLEVEL + " text, " +
                    COLUMN_NAME + " text, " +
                    COLUMN_USERSTATE + " text" +
                    ");";

    //Create table "Requests"
    private static final String DB_CREATE_REQUESTS =
            "create table " + DB_TABLE_REQUESTS + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_ICON + " integer, " +
                    COLUMN_STATE + " text, " +
                    COLUMN_ROUTE_FROM + " text, " +
                    COLUMN_ROUTE_TO + " text, " +
                    COLUMN_DISTANCE + " text, " +
                    COLUMN_DRIVER + " text, " +
                    COLUMN_COST + " text, " +
                    COLUMN_TIME + " text, " +
                    COLUMN_CUSTOMER + " text" +
                    ");";

    //Create table "Feedbacks"
    private static final String DB_CREATE_FEEDBACKS =
            "create table " + DB_TABLE_FEEDBACKS + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_PIC + " integer, " +
                    COLUMN_USER + " text, " +
                    COLUMN_FEEDBACK + " text" +
                    ");";

    private final Context mCtx;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы "Users"
    public Cursor getAllDataUsers() {
        return mDB.query(DB_TABLE_USERS, null, null, null, null, null, null);
    }

    // получить все данные из таблицы "Requests"
    public Cursor getAllDataRequests() {
        return mDB.query(DB_TABLE_REQUESTS, null, null, null, null, null, null);
    }

    // получить по логину активные заявки из таблицы "Requests"
    public Cursor getCurrentUserRequest(String login, String status, String status2) {
        return mDB.rawQuery("SELECT * FROM " + DB_TABLE_REQUESTS + " WHERE " + COLUMN_CUSTOMER + " LIKE '" + login + "'" + " AND "
                + COLUMN_STATE + " LIKE '" + status + "'" + " OR " + COLUMN_STATE + " LIKE '" + status2 + "'", null);
    }

    // получить по логину все заявки из таблицы "Requests"
    public Cursor getAllUserRequest(String login) {
        return mDB.rawQuery("SELECT * FROM " + DB_TABLE_REQUESTS + " WHERE " + COLUMN_CUSTOMER + " LIKE '" + login + "'", null);
    }

    // получить по логину все заявки из таблицы "Requests"
    public Cursor getAllDriverRequest(String login) {
        return mDB.rawQuery("SELECT * FROM " + DB_TABLE_REQUESTS + " WHERE " + COLUMN_DRIVER + " LIKE '" + login + "'", null);
    }

    // получить все данные из таблицы "Feedbacks"
    public Cursor getAllDataFeedbacks() {
        return mDB.query(DB_TABLE_FEEDBACKS, null, null, null, null, null, null);
    }

    // получить по логину все отзывы из таблицы "Feedbacks"
    public Cursor getCurrentUserFeedbacks(String login) {
        return mDB.rawQuery("SELECT * FROM " + DB_TABLE_FEEDBACKS + " WHERE " + COLUMN_USER + " LIKE '" + login + "'", null);
    }

    // проверка на используемость логина
    public boolean ifExisting(String login) {
        Cursor c = mDB.rawQuery("SELECT * FROM " + DB_TABLE_USERS + " WHERE " + COLUMN_LOGIN + " LIKE '" + login + "'", null);
        if(c.getCount() == 0)//do not existing
            return false;
        else {
            return true;
        }
    }

    // проверка на корректный логин и пароль
    public String[] ifCorrect(String login, String pass) {
        String[] mass_name = new String[2];
        Cursor c = mDB.rawQuery("SELECT * FROM " + DB_TABLE_USERS + " WHERE " + COLUMN_LOGIN + " LIKE '" + login + "'" + " AND "
                + COLUMN_PASSWORD + " LIKE '" + pass + "'", null);
        if(c.getCount() == 0)
            mass_name[0] = "Error to get the record";
        else {
            while (c.moveToNext()) {
                mass_name[0] = c.getString(c.getColumnIndex(COLUMN_LOGIN));
                mass_name[1] = c.getString(c.getColumnIndex(COLUMN_NAME));
            }
        }
        return mass_name;
    }

    // проверка на корректный логин и пароль
    public String[] getInfoRequest(long id) {
        String[] mass_name = new String[8];
        Cursor c = mDB.query(DB_TABLE_REQUESTS, null, COLUMN_ID + " = " + id, null, null, null, null);
        if(c.getCount() == 0)
            mass_name[0] = "Error to get the record";
        else {
            while (c.moveToNext()) {
                mass_name[0] = c.getString(c.getColumnIndex(COLUMN_STATE));
                mass_name[1] = c.getString(c.getColumnIndex(COLUMN_ROUTE_FROM));
                mass_name[2] = c.getString(c.getColumnIndex(COLUMN_ROUTE_TO));
                mass_name[3] = c.getString(c.getColumnIndex(COLUMN_DISTANCE));
                mass_name[4] = c.getString(c.getColumnIndex(COLUMN_CUSTOMER));
                mass_name[5] = c.getString(c.getColumnIndex(COLUMN_DRIVER));
                mass_name[6] = c.getString(c.getColumnIndex(COLUMN_COST));
                mass_name[7] = c.getString(c.getColumnIndex(COLUMN_TIME));
            }
        }
        return mass_name;
    }

    // добавить запись в "Users"
    public long addRecToUsers(int img, String log, String pass, String userlevel, String name, String phonenumber) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IMG, img);
        cv.put(COLUMN_LOGIN, log);
        cv.put(COLUMN_PASSWORD, pass);
        cv.put(COLUMN_USERLEVEL, userlevel);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_USERSTATE, phonenumber);
        long dBAnewRowId = mDB.insert(DB_TABLE_USERS, null, cv);
        return dBAnewRowId;
    }

    // добавить запись в "Requests"
    public long addRecToRequests(int icon, String state, String route_from, String route_to, String distance, String customer,
    String drive, String cost, String time) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ICON, icon);
        cv.put(COLUMN_STATE, state);
        cv.put(COLUMN_ROUTE_FROM, route_from);
        cv.put(COLUMN_ROUTE_TO, route_to);
        cv.put(COLUMN_DISTANCE, distance);
        cv.put(COLUMN_CUSTOMER, customer);
        cv.put(COLUMN_DRIVER, drive);
        cv.put(COLUMN_COST, cost);
        cv.put(COLUMN_TIME, time);
        long dBAnewRowId = mDB.insert(DB_TABLE_REQUESTS, null, cv);
        return dBAnewRowId;
    }

    // добавить запись в "Feedbacks"
    public long addRecToFeedbacks(int pic, String user, String feedback) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PIC, pic);
        cv.put(COLUMN_USER, user);
        cv.put(COLUMN_FEEDBACK, feedback);
        long dBAnewRowId = mDB.insert(DB_TABLE_FEEDBACKS, null, cv);
        return dBAnewRowId;
    }

    // удалить запись из "Users"
    public void delRecFromUsers(long id) {
        mDB.delete(DB_TABLE_USERS, COLUMN_ID + " = " + id, null);
    }

    // обновить запись в "Requests"
    public void updRequestFromUsers(String id) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATE, "canceled");
        mDB.update(DB_TABLE_REQUESTS, cv, COLUMN_ID + " = ?",
                new String[] { id });
    }

    // обновить запись в "Requests"
    public void cmpRequestFromUsers(String id) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATE, "completed");
        mDB.update(DB_TABLE_REQUESTS, cv, COLUMN_ID + " = ?",
                new String[] { id });
    }

    // принять заявку от заказчика
    public void acceptRequestFromCustomer(String id, String name) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATE, "perfomed");
        cv.put(COLUMN_DRIVER, name);
        mDB.update(DB_TABLE_REQUESTS, cv, COLUMN_ID + " = ?",
                new String[] { id });
    }

    // удалить запись из "Requests"
    public void delRecFromRequests(long id) {
        mDB.delete(DB_TABLE_REQUESTS, COLUMN_ID + " = " + id, null);
    }

    // удалить запись из "Feedbacks"
    public void delRecFromFeedbacks(long id) {
        mDB.delete(DB_TABLE_FEEDBACKS, COLUMN_ID + " = " + id, null);
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_USERS);

            ContentValues cv = new ContentValues();
            for (int i = 1; i < 5; i++) {
                cv.put(COLUMN_IMG, R.mipmap.ic_launcher);
                cv.put(COLUMN_LOGIN, "log " + i);
                cv.put(COLUMN_PASSWORD, "pass " + i);
                cv.put(COLUMN_USERLEVEL, "2");
                cv.put(COLUMN_NAME, "name " + i);
                cv.put(COLUMN_USERSTATE, "free");
                db.insert(DB_TABLE_USERS, null, cv);
            }

            db.execSQL(DB_CREATE_REQUESTS);

            ContentValues cv2 = new ContentValues();
            for (int i = 1; i < 2; i++) {
                cv2.put(COLUMN_ICON, R.drawable.ic_distance);
                cv2.put(COLUMN_STATE, "state " + i);
                cv2.put(COLUMN_ROUTE_FROM, "route from " + i);
                cv2.put(COLUMN_ROUTE_TO, "route to " + i);
                cv2.put(COLUMN_DISTANCE, "distance " + i);
                cv2.put(COLUMN_CUSTOMER, "customer " + i);
                cv2.put(COLUMN_DRIVER, "driver " + i);
                cv2.put(COLUMN_COST, "cost " + i);
                cv2.put(COLUMN_TIME, "time " + i);
                db.insert(DB_TABLE_REQUESTS, null, cv2);
            }

            db.execSQL(DB_CREATE_FEEDBACKS);

            ContentValues cv3 = new ContentValues();
            for (int i = 1; i < 2; i++) {
                cv3.put(COLUMN_PIC, R.mipmap.ic_launcher);
                cv3.put(COLUMN_USER, "default");
                cv3.put(COLUMN_FEEDBACK, "default");
                db.insert(DB_TABLE_FEEDBACKS, null, cv3);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
