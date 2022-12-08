package com.example.jacaranda.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jacaranda.Modle.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Jacaranda.db";
    private static final String TABLE_NAME = "Activity";
    private static final int DB_VERSION = 1;
    private static final String TAG = "ActivityDBHelper";
    private static ActivityDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private ActivityDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    //implementing singleton pattern
    public static ActivityDBHelper getInstance(Context context){
        if (mHelper == null){
            mHelper = new ActivityDBHelper(context);
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
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" +
                "receipt LONG PRIMARY KEY NOT NULL," +
                "receiveUser VARCHAR NOT NULL," +
                "receiveUsername VARCHAR NOT NULL," +
                "payUser VARCHAR NOT NULL," +
                "payUsername VARCHAR NOT NULL," +
                "amount VARCHAR NOT NULL," +
                "type VARCHAR NOT NULL," +
                "dateString VARCHAR NOT NULL" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void save(Activity activity){
        try{
            mWDB.beginTransaction();
            delete(activity);
            insert(activity);
            mWDB.setTransactionSuccessful();
            Log.i(TAG, "save success");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mWDB.endTransaction();
        }
    }

    public long insert(Activity activity){
        ContentValues values = new ContentValues();
        values.put("receipt", activity.getReceipt());
        values.put("receiveUser", activity.getReceiveUser());
        values.put("receiveUsername", activity.getReceiveUsername());
        values.put("payUser", activity.getPayUser());
        values.put("payUsername", activity.getPayUsername());
        values.put("amount", activity.getAmount());
        values.put("type", activity.getType());
        values.put("dateString", activity.getDateString());
        return mWDB.insert(TABLE_NAME, null, values);
    }

    public boolean insertActivities(List<Activity> activitys){
       for (Activity activity:activitys){
           if (insert(activity) == -1){
               return false;
           }
       }
       return true;
    }

    public long delete(Activity activity){
        return mWDB.delete(TABLE_NAME, "receipt=?", new String[]{activity.getReceipt()});
    }

    public List<Activity> queryAll(){
        List<Activity> list = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()){
            Activity activity = new Activity();
            activity.setReceipt(cursor.getString(0));
            activity.setReceiveUser(cursor.getString(1));
            activity.setReceiveUsername(cursor.getString(2));
            activity.setAmount(cursor.getString(3));
            activity.setType(cursor.getString(4));
            activity.setDateString(cursor.getString(5));
            list.add(activity);
        }
        return list;
    }


    public List<Activity> queryByEmail(String email){
        Log.i(TAG, "findPassByEmail");
        List<Activity> list = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_NAME,
                null,
                "receiveUser=", new String[]{email},
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()){
            Activity activity = new Activity();
            activity.setReceipt(cursor.getString(0));
            activity.setReceiveUser(cursor.getString(1));
            activity.setReceiveUsername(cursor.getString(2));
            activity.setAmount(cursor.getString(3));
            activity.setType(cursor.getString(4));
            activity.setDateString(cursor.getString(5));
            list.add(activity);
        }
        return list;
    }

}
