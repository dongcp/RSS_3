package com.framgia.rssfeed.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by VULAN on 4/26/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_history_news";
    public static final int VERSION = 22;
    public static final String CREATING_TABLE_HISTORY = "CREATE TABLE [news_information]" +
            "(title TEXT, imageUrl TEXT,link TEXT, description TEXT, detail TEXT, category INTEGER, html TEXT)";
    public static final String CREATING_TABLE_FAVORITE = "CREATE TABLE [favorite]" +
            "(title TEXT, imageUrl TEXT,link TEXT, description TEXT, detail TEXT, category INTEGER, html TEXT)";
    public static final String DROP_TABLE_HISTORY = "DROP TABLE IF EXISTS " + DatabaseHandler.TABLE_NEWS;
    public static final String DROP_TABLE_FAVORITE = "DROP TABLE IF EXISTS " + DatabaseHandler.TABLE_FAVORITE;

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
        if (oldVersion < newVersion) {
            db.execSQL(DROP_TABLE_HISTORY);
            db.execSQL(DROP_TABLE_FAVORITE);
            onCreate(db);
        }
    }
}
