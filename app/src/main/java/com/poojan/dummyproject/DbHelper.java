package com.poojan.dummyproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by VNurtureTechnologies on 14/03/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME="userdata";
    public static final String TABLE_NAME="user";
    public static final String KEY_FNAME="rname";
    public static final String KEY_LNAME="rdatetime";
    public static final String KEY_ID="id";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_FNAME+" TEXT, "+KEY_LNAME+" TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
