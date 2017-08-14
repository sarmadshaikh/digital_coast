package com.sarmad.digitalcoast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;
import android.os.StrictMode;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sarmad on 8/4/17.
 */

public class DatabaseFunctions extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DigitalCoast.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "BUILDINGS";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_PHOTO = "PHOTO";
    public static final String COLUMN_TYPE = "BUILDING_TYPE";
    public static final String COLUMN_CAPACITY = "BUILDING_CAPACITY";
    public static final String COLUMN_LENGTH = "BUILDING_LENGTH";
    public static final String COLUMN_WIDTH = "BUILDING_WIDTH";
    public static final String COLUMN_HEIGHT = "BUILDING_HEIGHT";
    public static final String COLUMN_DISTANCE = "DISTANCE_FROM_SHORE";
    public static final String COLUMN_LONGITUDE = "LOCATION_LONGITUDE";
    public static final String COLUMN_LATITUDE = "LOCATION_LATITUDE";
    public static final String COLUMN_DATE_CREATED = "CREATED_ON";

    SQLiteDatabase db;

    public DatabaseFunctions(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql_create_table = "CREATE TABLE "+TABLE_NAME+"("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PHOTO +
                " TEXT, " + COLUMN_TYPE +" TEXT, " + COLUMN_LATITUDE + " TEXT, " + COLUMN_LONGITUDE + " TEXT, " + COLUMN_LENGTH +
                " TEXT, " + COLUMN_WIDTH + " TEXT, " + COLUMN_HEIGHT + " TEXT, " + COLUMN_CAPACITY + " INTEGER, " +
                COLUMN_DISTANCE + " TEXT, " + COLUMN_DATE_CREATED + " TEXT)";

        try {
            db.execSQL(sql_create_table);
            Log.d("TABLE CREATION: ","TABLE CREATED SUCCESSFULLY");
        }
        catch (Exception e)
        {
            Log.e("TABLE CREATION ERROR:", "UNABLE TO CREATE TABLE");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql_upgrade_database = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql_upgrade_database);
        onCreate(db);
        Log.d("TABLE UPGRADATION: ","TABLE UPGRADED SUCCESSFULLY");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("DATABASE OPEN: ", "DATABASE HAS BEEN ACCESSED");
    }

    public long insertRecords(String[] data)
    {
        db = getWritableDatabase();
        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        ContentValues cntValues = new ContentValues();
        cntValues.put(COLUMN_TYPE, data[0]);
        cntValues.put(COLUMN_PHOTO,data[1]);
        cntValues.put(COLUMN_LATITUDE, data[2]);
        cntValues.put(COLUMN_LONGITUDE, data[3]);
        cntValues.put(COLUMN_LENGTH, data[4]);
        cntValues.put(COLUMN_WIDTH, data[5]);
        cntValues.put(COLUMN_HEIGHT, data[6]);
        cntValues.put(COLUMN_CAPACITY, (data[7].equals("") ? 0 : Integer.parseInt(data[7])) );
        cntValues.put(COLUMN_DISTANCE, data[8]);
        cntValues.put(COLUMN_DATE_CREATED, date);

        long rowsAffected = db.insert(TABLE_NAME, null, cntValues);
        return rowsAffected;
    }

    public Cursor getRecords()
    {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATE_CREATED + " DESC", null);
        return cursor;
    }

    public Cursor getRecords(String selection)
    {
        db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_TYPE, COLUMN_PHOTO, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_LENGTH,
                COLUMN_WIDTH, COLUMN_HEIGHT, COLUMN_CAPACITY, COLUMN_DISTANCE, COLUMN_DATE_CREATED}, COLUMN_ID+"=?", new String[]{selection}, null, null, COLUMN_DATE_CREATED);
        return cursor;
    }

    public long updateRecord(String selection, String[] data)
    {
        db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TYPE, data[0]);
        contentValues.put(COLUMN_LENGTH, data[1]);
        contentValues.put(COLUMN_WIDTH, data[2]);
        contentValues.put(COLUMN_HEIGHT, data[3]);
        contentValues.put(COLUMN_CAPACITY, data[4]);
        contentValues.put(COLUMN_DISTANCE, data[5]);
        return db.update(TABLE_NAME, contentValues,COLUMN_ID+"=?", new String[]{selection});
    }

    public long deleteRecord(String selection)
    {
        db = getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID+"=?", new String[]{selection});
    }
}
