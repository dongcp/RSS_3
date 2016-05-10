package com.framgia.rssfeed.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.framgia.rssfeed.data.bean.News;
import com.framgia.rssfeed.util.XmlParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by VULAN on 4/26/2016.
 */
public class DatabaseHandler {

    public static final String TABLE_NEWS = "news_information";
    public static final String TABLE_FAVORITE = "favorite";
    public static final String TITLE = "title";
    public static final String IMAGE_URL = "imageUrl";
    public static final String LINK = "link";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY = "category";
    public final static String DETAIL = "detail";
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

    public void insertNewsInfo(News news, String detail) {
        if (!isHistory(news.getLink())) {
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TITLE, news.getTitle());
            contentValues.put(IMAGE_URL, news.getImageUrl());
            contentValues.put(LINK, news.getLink());
            contentValues.put(DESCRIPTION, news.getDescription());
            contentValues.put(DETAIL, detail);
            contentValues.put(CATEGORY, news.getCategory());
            mSQLiteDatabase.insert(TABLE_NEWS, null, contentValues);
        }
    }

    public void insertFavoriteInfo(News news, String html) {
        if (!isFavorite(news.getLink())) {
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TITLE, news.getTitle());
            contentValues.put(IMAGE_URL, news.getImageUrl());
            contentValues.put(LINK, news.getLink());
            contentValues.put(DESCRIPTION, news.getDescription());
            contentValues.put(DETAIL, html);
            contentValues.put(CATEGORY, news.getCategory());
            mSQLiteDatabase.insert(TABLE_FAVORITE, null, contentValues);
        }
    }

    public void removeFavorite(String url) {
        String favoriteHtml = getFavoriteUrlDetail(url);
        String historyHtml = getHistoryUrlDetail(url);
        if (historyHtml == null) {
            Document doc = Jsoup.parse(favoriteHtml);
            Elements img = doc.select(XmlParser.TAG_IMG);
            int numberOfImage = img.size();
            for (int i = 0; i < numberOfImage; i++) {
                String src = img.eq(i).attr(XmlParser.ATTR_SRC);
                File file = new File(src);
                file.delete();
            }
        }
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        String where = LINK + "=?";
        String[] whereArgs = {url};
        mSQLiteDatabase.delete(TABLE_FAVORITE, where, whereArgs);
    }

    public ArrayList<News> getHistoryNews() {
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        ArrayList<News> newsList = new ArrayList<>();
        String[] columns = {TITLE, IMAGE_URL, LINK, DESCRIPTION, CATEGORY};
        Cursor cursor = mSQLiteDatabase.query(true, TABLE_NEWS, columns,
                null, null, null, null, null, null);
        cursor.moveToFirst();
        HashMap<String, Integer> columnCache = new HashMap<>();
        columnCache.put(TITLE, cursor.getColumnIndex(TITLE));
        columnCache.put(IMAGE_URL, cursor.getColumnIndex(IMAGE_URL));
        columnCache.put(LINK, cursor.getColumnIndex(LINK));
        columnCache.put(DESCRIPTION, cursor.getColumnIndex(DESCRIPTION));
        columnCache.put(CATEGORY, cursor.getColumnIndex(CATEGORY));
        while (!cursor.isAfterLast()) {
            News news = new News();
            news.setTitle(cursor.getString(columnCache.get(TITLE)));
            news.setImageUrl(cursor.getString(columnCache.get(IMAGE_URL)));
            news.setLink(cursor.getString(columnCache.get(LINK)));
            news.setDescription(cursor.getString(columnCache.get(DESCRIPTION)));
            news.setCategory(cursor.getInt(columnCache.get(CATEGORY)));
            newsList.add(news);
            cursor.moveToNext();
        }
        cursor.close();
        return newsList;
    }

    public String getHistoryUrlDetail(String url) {
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String tmp = null;
        String[] columns = {DETAIL};
        String selection = LINK + "=?";
        String[] selectionArgs = {url};
        Cursor cursor = mSQLiteDatabase.query(TABLE_NEWS, columns, selection, selectionArgs, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int detailColumnIndex = cursor.getColumnIndex(DETAIL);
            tmp = cursor.getString(detailColumnIndex);
        }
        return tmp;
    }

    public ArrayList<News> getFavoriteNews(int category) {
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        ArrayList<News> newsList = new ArrayList<>();
        String selection = CATEGORY + "=?";
        String[] selectionArgs = {"" + category};
        String[] columns = {TITLE, IMAGE_URL, LINK, DESCRIPTION, CATEGORY};
        Cursor cursor = mSQLiteDatabase.query(TABLE_FAVORITE, columns,
                selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        HashMap<String, Integer> columnCache = new HashMap<>();
        columnCache.put(TITLE, cursor.getColumnIndex(TITLE));
        columnCache.put(IMAGE_URL, cursor.getColumnIndex(IMAGE_URL));
        columnCache.put(LINK, cursor.getColumnIndex(LINK));
        columnCache.put(DESCRIPTION, cursor.getColumnIndex(DESCRIPTION));
        while (!cursor.isAfterLast()) {
            News news = new News();
            news.setTitle(cursor.getString(columnCache.get(TITLE)));
            news.setImageUrl(cursor.getString(columnCache.get(IMAGE_URL)));
            news.setLink(cursor.getString(columnCache.get(LINK)));
            news.setDescription(cursor.getString(columnCache.get(DESCRIPTION)));
            news.setCategory(category);
            newsList.add(news);
            cursor.moveToNext();
        }
        cursor.close();
        return newsList;
    }

    public String getFavoriteUrlDetail(String url) {
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String tmp = null;
        String[] columns = {DETAIL};
        String selection = LINK + "=?";
        String[] selectionArgs = {url};
        Cursor cursor = mSQLiteDatabase.query(TABLE_FAVORITE, columns, selection, selectionArgs, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int detailColumnIndex = cursor.getColumnIndex(DETAIL);
            tmp = cursor.getString(detailColumnIndex);
        }
        return tmp;
    }

    public boolean isFavorite(String url) {
        if (url == null) return false;
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String selection = LINK + "=?";
        String[] selectionArgs = {url};
        String[] columns = {LINK};
        Cursor cursor = mSQLiteDatabase.query(TABLE_FAVORITE, columns, selection, selectionArgs, null, null, null);
        int tmp = cursor.getCount();
        cursor.close();
        return tmp > 0;
    }

    public boolean isHistory(String url) {
        if (url == null) return false;
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String selection = LINK + "=?";
        String[] selectionArgs = {url};
        String[] columns = {LINK};
        Cursor cursor = mSQLiteDatabase.query(TABLE_NEWS, columns, selection, selectionArgs, null, null, null);
        int tmp = cursor.getCount();
        cursor.close();
        return tmp > 0;
    }
}
