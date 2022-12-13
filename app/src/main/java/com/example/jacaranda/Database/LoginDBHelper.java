package com.example.jacaranda.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.jacaranda.Modle.Activity;
import com.example.jacaranda.Modle.RecentActivity;
import com.example.jacaranda.Modle.UserCredential;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LoginDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Jacaranda.db";
    private static final String USER_TABLE_NAME = "UserCredential";
    private static final String ACTIVITY_TABLE_NAME = "Activity";
    private static final int DB_VERSION = 1;
    private static final String TAG = "LoginDBHelper";
    private static LoginDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private LoginDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    //implementing singleton pattern
    public static LoginDBHelper getInstance(Context context){
        if (mHelper == null){
            mHelper = new LoginDBHelper(context);
        }
        return mHelper;
    }

    //open db read link
    public SQLiteDatabase openReadLink(){
        if (mRDB == null || !mRDB.isOpen()){
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    //open db write link
    public SQLiteDatabase openWriteLink(){
        if (mWDB == null || !mWDB.isOpen()){
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    // close link
    public void closeLink(){
        if (mRDB != null && mRDB.isOpen()){
            mRDB.close();
            mRDB = null;
        }

        if (mWDB != null && mWDB.isOpen()){
            mWDB.close();
            mWDB = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_login = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME +
                "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "email VARCHAR NOT NULL," +
                "password VARCHAR NOT NULL" +
                ");";

        String sql_activity = "CREATE TABLE IF NOT EXISTS " + ACTIVITY_TABLE_NAME +
                "(" +
                "receipt LONG PRIMARY KEY NOT NULL," +
                "receiveUser VARCHAR NOT NULL," +
                "receiveUsername VARCHAR NOT NULL," +
                "payUser VARCHAR NOT NULL," +
                "payUsername VARCHAR NOT NULL," +
                "amount VARCHAR NOT NULL," +
                "type VARCHAR NOT NULL," +
                "dateTime VARCHAR NOT NULL," +
                "payColor VARCHAR NOT NULL," +
                "receiveColor VARCHAR NOT NULL" +
                ");";
        db.execSQL(sql_login);

        db.execSQL(sql_activity);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveUser(UserCredential userCredential){
        try{
            mWDB.beginTransaction();
            deleteUser(userCredential);
            insertUser(userCredential);
            mWDB.setTransactionSuccessful();
            Log.i(TAG, "save success");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mWDB.endTransaction();
        }
    }

    public long insertUser(UserCredential userCredential){
        ContentValues values = new ContentValues();
        values.put("email", userCredential.email);
        values.put("password", userCredential.password);
        return mWDB.insert(USER_TABLE_NAME, null, values);
    }

    public long deleteUser(UserCredential userCredential){
        return mWDB.delete(USER_TABLE_NAME, "email=?", new String[]{userCredential.email});
    }


    public long updateUser(UserCredential userCredential){
        ContentValues values = new ContentValues();
        values.put("email", userCredential.email);
        values.put("password", userCredential.password);
        return mWDB.update(USER_TABLE_NAME, values, "email=?",
                new String[]{userCredential.email});
    }

    public List<UserCredential> queryAll(){
        List<UserCredential> list = new ArrayList<>();
        Cursor cursor = mRDB.query(USER_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()){
            UserCredential userCredential = new UserCredential();
            userCredential.email = cursor.getString(1);
            userCredential.password = cursor.getString(2);
            list.add(userCredential);
        }
        return list;
    }

    //return password if found in SQLite else return ""
    public String findPasswordByEmail(String email){
        Log.i(TAG, "findPassByEmail");
        Cursor cursor = mRDB.query(USER_TABLE_NAME,
                null,
                "email=?", new String[]{email},
                null,
                null,
                null);
        if (cursor.moveToNext()){
            Log.i(TAG, cursor.getString(1));
            return cursor.getString(2);
        }
        return "";
    }

    //find the userCredential of user logged in the latest
    public UserCredential queryUserOnTop(){
        UserCredential userCredential = null;
        String sql = "SELECT * FROM " + USER_TABLE_NAME +
                " ORDER BY id DESC limit 1";

        Cursor cursor = mRDB.rawQuery(sql, null);
        if (cursor.moveToNext()){
            Log.i(TAG, cursor.getString(1));
            userCredential = new UserCredential();
            userCredential.email = cursor.getString(1);
            userCredential.password = cursor.getString(2);
        }
        return userCredential;
    }

    public long insertActivity(Activity activity){
        ContentValues values = new ContentValues();
        values.put("receipt", activity.getReceipt());
        values.put("receiveUser", activity.getReceiveUser());
        values.put("receiveUsername", activity.getReceiveUsername());
        values.put("payUser", activity.getPayUser());
        values.put("payUsername", activity.getPayUsername());
        values.put("amount", activity.getAmount());
        values.put("type", activity.getType());
        values.put("dateTime", activity.getDateString());
        values.put("payColor", activity.getPayColor());
        values.put("receiveColor", activity.getReceiveColor());
        return mWDB.insertOrThrow(ACTIVITY_TABLE_NAME, null, values);
    }

    public boolean insertActivities(List<Activity> activitys){
        for (Activity activity:activitys){
            if (insertActivity(activity) == -1){
                return false;
            }
        }
        return true;
    }

    public List<Activity> queryAllActivity(){
        Log.i(TAG, "queryAllActivity");
        List<Activity> list = new ArrayList<>();
        Cursor cursor = mRDB.query(ACTIVITY_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()){
            Log.i(TAG, cursor.getString(0));
            Activity activity = new Activity();
            activity.setReceipt(cursor.getString(0));
            activity.setReceiveUser(cursor.getString(1));
            activity.setReceiveUsername(cursor.getString(2));
            activity.setPayUser(cursor.getString(3));
            activity.setPayUsername(cursor.getString(4));
            activity.setAmount(cursor.getDouble(5));
            activity.setType(cursor.getString(6));
            activity.setDateString(cursor.getString(7));
            list.add(activity);
        }
        return list;
    }

    public List<Activity> queryById(String id){
        Log.i(TAG, "queryById");
        String sql = "SELECT * FROM " + ACTIVITY_TABLE_NAME +
                " WHERE receiveUser= "+ id + " OR " + "payUser= "+ id +
                " ORDER BY dateTime DESC";
        List<Activity> list = new ArrayList<>();
        Cursor cursor = mRDB.rawQuery(sql, null);
//        Cursor cursor = mRDB.query(ACTIVITY_TABLE_NAME,
//                null,
//                "receiveUser=? OR payUser=?", new String[]{id},
//                null,
//                null,
//                null,
//                null);
        while (cursor.moveToNext()){
            Log.i(TAG, cursor.getString(0));
            Activity activity = new Activity();
            activity.setReceipt(cursor.getString(0));
            activity.setReceiveUser(cursor.getString(1));
            activity.setReceiveUsername(cursor.getString(2));
            activity.setPayUser(cursor.getString(3));
            activity.setPayUsername(cursor.getString(4));
            activity.setAmount(cursor.getDouble(5));
            activity.setType(cursor.getString(6));
            activity.setDateString(cursor.getString(7));
            list.add(activity);
        }
        return list;
    }

//    public List<Activity> queryByIdTop(String id){
//        Log.i(TAG, "queryById");
//        String sql = "SELECT * FROM " + ACTIVITY_TABLE_NAME +
//                " WHERE receiveUser= "+ id + " OR " + "payUser= "+ id +
//             " ORDER BY date DESC limit 4";
//        List<Activity> list = new ArrayList<>();
//        Cursor cursor = mRDB.rawQuery(sql, null);
////        Cursor cursor = mRDB.query(ACTIVITY_TABLE_NAME,
////                null,
////                "receiveUser=? OR payUser=?", new String[]{id},
////                null,
////                null,
////                null,
////                null);
//        while (cursor.moveToNext()){
//            Log.i(TAG, cursor.getString(0));
//            Activity activity = new Activity();
//            activity.setReceipt(cursor.getString(0));
//            activity.setReceiveUser(cursor.getString(1));
//            activity.setReceiveUsername(cursor.getString(2));
//            activity.setPayUser(cursor.getString(3));
//            activity.setPayUsername(cursor.getString(4));
//            activity.setAmount(cursor.getDouble(5));
//            activity.setType(cursor.getString(6));
//            activity.setDate(cursor.getString(7));
//            list.add(activity);
//        }
//        return list;
//    }

    public String queryLatestActivityDate(){
        String sql = "SELECT * FROM " + ACTIVITY_TABLE_NAME +
                " ORDER BY dateTime DESC limit 1";

        Cursor cursor = mRDB.rawQuery(sql, null);
        if (cursor.moveToNext()){
            Log.i(TAG, cursor.getString(7));
            return cursor.getString(7);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<RecentActivity> queryByIdTop(String id) throws ParseException {
        Log.i(TAG, "queryById");
        String sql = "SELECT * FROM " + ACTIVITY_TABLE_NAME +
                " WHERE receiveUser= "+ id + " OR " + "payUser= "+ id +
                " ORDER BY dateTime DESC limit 4";
        List<RecentActivity> list = new ArrayList<>();
        Cursor cursor = mRDB.rawQuery(sql, null);
//        Cursor cursor = mRDB.query(ACTIVITY_TABLE_NAME,
//                null,
//                "receiveUser=? OR payUser=?", new String[]{id},
//                null,
//                null,
//                null,
//                null);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

        String payUser, receiveUser, dateTime;
        while (cursor.moveToNext()){
            Log.i(TAG, cursor.getString(0));
            RecentActivity activity = new RecentActivity();
            activity.setReceipt(cursor.getString(0));

            receiveUser = cursor.getString(1);
            payUser = cursor.getString(3);

            if (receiveUser.equals(id)){
                if (payUser.equals(id)){
                    activity.setName("topUp");
                    activity.setType("topUp");
                    activity.setProfilePic("topUp");
                }else{
                    activity.setName(cursor.getString(1));
                    activity.setType("receive");
                    activity.setProfilePic(cursor.getString(8));
                }
            }else {
                activity.setName(cursor.getString(2));
                activity.setType("pay");
                activity.setProfilePic(cursor.getString(9));
            }

            activity.setAmount(cursor.getDouble(5));
            activity.setType(cursor.getString(6));

            dateTime = cursor.getString(7);
            activity.setDateTime(dateTime);
            activity.setDateString(sdf.format(Objects.requireNonNull(df.parse(dateTime))));

            list.add(activity);
        }
        return list;
    }
}
