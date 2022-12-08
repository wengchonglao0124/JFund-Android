package com.example.jacaranda.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jacaranda.Modle.UserCredential;

import java.util.ArrayList;
import java.util.List;

public class LoginDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Jacaranda.db";
    private static final String TABLE_NAME = "UserCredential";
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
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "email VARCHAR NOT NULL," +
                "password VARCHAR NOT NULL" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void save(UserCredential userCredential){
        try{
            mWDB.beginTransaction();
            delete(userCredential);
            insert(userCredential);
            mWDB.setTransactionSuccessful();
            Log.i(TAG, "save success");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mWDB.endTransaction();
        }
    }

    public long insert(UserCredential userCredential){
        ContentValues values = new ContentValues();
        values.put("email", userCredential.email);
        values.put("password", userCredential.password);
        return mWDB.insert(TABLE_NAME, null, values);
    }

    public long delete(UserCredential userCredential){
        return mWDB.delete(TABLE_NAME, "email=?", new String[]{userCredential.email});
    }


    public long update(UserCredential userCredential){
        ContentValues values = new ContentValues();
        values.put("email", userCredential.email);
        values.put("password", userCredential.password);
        return mWDB.update(TABLE_NAME, values, "email=?",
                new String[]{userCredential.email});
    }

    public List<UserCredential> queryAll(){
        List<UserCredential> list = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_NAME,
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
        Cursor cursor = mRDB.query(TABLE_NAME,
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
    public UserCredential queryOnTop(){
        UserCredential userCredential = null;
        String sql = "SELECT * FROM " + TABLE_NAME +
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
}
