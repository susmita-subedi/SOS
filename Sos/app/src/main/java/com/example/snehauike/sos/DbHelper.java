package com.example.snehauike.sos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by snehauike on 2/3/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CS595_capstone.db";
    public static final String TABLE_NAME = "tblUserDetails";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "PHONENO";
    public static final String COL_4 = "EMAILID";
    public static final String COL_5 = "USERNAME";
    public static final String COL_6 = "PASSWORD";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID integer primary key autoincrement, NAME varchar(16) not null, PHONENO integer not null, EMAILID text, USERNAME text, PASSWORD text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop tabe if exists "+TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String name, String phoneNo, String emailId, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();//create database and table
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,phoneNo);
        contentValues.put(COL_4,emailId);
        contentValues.put(COL_5,username);
        contentValues.put(COL_6,password);

        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    String password;
    public String getSingleEntry(String username){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,null,"USERNAME=?", new String[]{username},null,null,null,null);
        if(cursor.getCount()<1){
            cursor.close();
            return "Not Exist";
        }
        else if(cursor.getCount()>=1 && cursor.moveToFirst()){

           password = cursor.getString(cursor.getColumnIndex(COL_6));
            cursor.close();

        }
        return password;


    }

    public void viewData(){
        SQLiteDatabase db = this.getReadableDatabase();

         Cursor result = db.rawQuery("Select * from "+TABLE_NAME, null);
        if (result.getCount() == 0){
            System.out.println("No data in database");
            return;
        }
        StringBuffer res = new StringBuffer();
        while(result.moveToNext()){
            res.append("Id: " +result.getString(0)+"\n");
            res.append("Name: " +result.getString(1)+"\n");
            res.append("Phone: " +result.getString(2)+"\n");
            res.append("Email: " +result.getString(3)+"\n");
            res.append("UserName: " +result.getString(4)+"\n");
            res.append("Password: " +result.getString(5)+"\n");
        }
        System.out.println(res);
    }

    public StringBuffer encrypt(String password){
        MessageDigest md= null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(password.getBytes());

        byte byteData[]=md.digest();

        StringBuffer hexString=new StringBuffer();
        for(int i=0;i<byteData.length;i++){
            String hex=Integer.toHexString(0xff&byteData[i]);
            if(hex.length()==1)hexString.append('0');
            hexString.append(hex);
        }
        return hexString;
    }

    public boolean isDataAlreadyPresent(String dbField, String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("Select * from " + TABLE_NAME + " where " + dbField + "='"+ fieldValue+"'", null);
        if (result.moveToFirst()) {
            result.close();
            return true;
        } else {
            result.close();
            return false;
        }
    }



}
