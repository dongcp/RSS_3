package com.framgia.rssfeed.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by VULAN on 4/26/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_history_news";
    public static final int VERSION = 14;
    public static final String CREATING_TABLE_HISTORY = "CREATE TABLE [news_information]" +
            "(title TEXT, imageUrl TEXT,link TEXT,description TEXT, detail TEXT,category INTEGER)";
    public static final String CREATING_TABLE_FAVORITE = "CREATE TABLE [favorite]" +
            "(title TEXT, imageUrl TEXT,link TEXT,description TEXT,detail TEXT,category INTEGER)";
    public static final String DATABASE_ALTER_COLUMN_IN_HISTORY = "ALTER TABLE [news_information] ADD COLUMN" +
            "category INTEGER ";

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
        if (VERSION < newVersion) {
            db.execSQL(DATABASE_ALTER_COLUMN_IN_HISTORY);
        }
    }
}
