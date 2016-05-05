package com.framgia.rssfeed.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.framgia.rssfeed.data.bean.News;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by VULAN on 4/26/2016.
 */
public class DatabaseHandler {

    public static final String TABLE = "news_information";
    public static final String TITLE = "title";
    public static final String IMAGE_URL = "imageUrl";
    public static final String LINK = "link";
    public static final String DESCRIPTION = "description";
    private static DatabaseHandler sInstance;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public DatabaseHandler(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public static DatabaseHandler getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DatabaseHandler.class) {
                if (sInstance == null) {
                    sInstance = new DatabaseHandler(context);
                }
            }
        }
        return sInstance;
    }

    public void open() throws SQLException {
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void close() throws SQLException {
        mDatabaseHelper.close();
    }

    public void insertNewsInfo(News news) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, news.getTitle());
        contentValues.put(IMAGE_URL, news.getImageUrl());
        contentValues.put(LINK, news.getLink());
        contentValues.put(DESCRIPTION, news.getDescription());
        mSQLiteDatabase.insert(TABLE, null, contentValues);
        close();
    }

    public ArrayList<News> getHistoryNews() {
        open();
        ArrayList<News> newsList = new ArrayList<>();
        String[] columns = {TITLE, IMAGE_URL, LINK, DESCRIPTION};
        Cursor cursor = mSQLiteDatabase.query(true, TABLE, columns,
                null, null, null, null, null, null);
        cursor.moveToFirst();
        HashMap<String, Integer> collumnCache = new HashMap<>();
        collumnCache.put(TITLE, cursor.getColumnIndex(TITLE));
        collumnCache.put(IMAGE_URL, cursor.getColumnIndex(IMAGE_URL));
        collumnCache.put(LINK, cursor.getColumnIndex(LINK));
        collumnCache.put(DESCRIPTION, cursor.getColumnIndex(DESCRIPTION));
        while (!cursor.isAfterLast()) {
            News news = new News();
            news.setTitle(cursor.getString(collumnCache.get(TITLE)));
            news.setImageUrl(cursor.getString(collumnCache.get(IMAGE_URL)));
            news.setLink(cursor.getString(collumnCache.get(LINK)));
            news.setDescription(cursor.getString(collumnCache.get(DESCRIPTION)));
            newsList.add(news);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return newsList;
    }
}
