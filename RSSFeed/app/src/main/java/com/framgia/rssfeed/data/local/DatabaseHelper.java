package com.framgia.rssfeed.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by VULAN on 4/26/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    
    public static final String DATABASE_NAME = "db_history_news";
    public static final int VERSION = 10;
    public static final String CREATING_TABLE_HISTORY = "CREATE TABLE [news_information]" +
            "(title TEXT, imageUrl TEXT,link TEXT,description TEXT)";
    public static final String CREATING_TABLE_FAVORITE = "CREATE TABLE [favorite]" +
            "(title TEXT, imageUrl TEXT,link TEXT,description TEXT,category INTEGER)";
    public static final String DROPPING_TABLE_HISTORY = "DROP TABLE IF EXITS [news_information]";
    public static final String DROPPING_TABLE_FAVORITE = "DROP TABLE IF EXITS [favorite]";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATING_TABLE_HISTORY);
        db.execSQL(CREATING_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROPPING_TABLE_HISTORY);
        db.execSQL(DROPPING_TABLE_FAVORITE);
        onCreate(db);
    }
}
