package com.framgia.rssfeed.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.framgia.rssfeed.data.bean.News;
import com.framgia.rssfeed.util.XmlParser;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
    public final static String HTML = "html";
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

    public void insertNewsInfo(News news, String detail, String html) {
        if (!isHistory(news.getLink())) {
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TITLE, news.getTitle());
            contentValues.put(IMAGE_URL, news.getImageUrl());
            contentValues.put(LINK, news.getLink());
            contentValues.put(DESCRIPTION, news.getDescription());
            contentValues.put(DETAIL, detail);
            contentValues.put(CATEGORY, news.getCategory());
            contentValues.put(HTML, html);
            mSQLiteDatabase.insert(TABLE_NEWS, null, contentValues);
        }
    }

    public void insertFavoriteInfo(News news, String detail, String html) {
        if (!isFavorite(news.getLink())) {
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TITLE, news.getTitle());
            contentValues.put(IMAGE_URL, news.getImageUrl());
            contentValues.put(LINK, news.getLink());
            contentValues.put(DESCRIPTION, news.getDescription());
            contentValues.put(DETAIL, detail);
            contentValues.put(CATEGORY, news.getCategory());
            contentValues.put(HTML, html);
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

    public ArrayList<News> getHistoryNewsList() {
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
        Collections.reverse(newsList);
        return newsList;
    }

    public String getHistoryDocByUrl(String url, int type) {
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String[] columns = null;
        String selection = LINK + "=?";
        String[] selectionArgs = {url};
        switch (type) {
            case 1:
                columns = new String[]{DETAIL};
                break;
            case 2:
                columns = new String[]{HTML};
                break;
        }
        Cursor cursor = mSQLiteDatabase.query(true, TABLE_NEWS, columns,
                selection, selectionArgs, null, null, null, null);
        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        int detailColumnIndex;
        if (type == 1) {
            detailColumnIndex = cursor.getColumnIndex(DETAIL);
        } else {
            detailColumnIndex = cursor.getColumnIndex(HTML);
        }
        String detail = cursor.getString(detailColumnIndex);
        cursor.close();
        return detail;
    }

    public String getFavoriteDocByUrl(String url, int type) {
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String[] columns = null;
        String selection = LINK + "=?";
        String[] selectionArgs = {url};
        switch (type) {
            case 1:
                columns = new String[]{DETAIL};
                break;
            case 2:
                columns = new String[]{HTML};
                break;
        }
        Cursor cursor = mSQLiteDatabase.query(true, TABLE_FAVORITE, columns,
                selection, selectionArgs, null, null, null, null);
        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        int detailColumnIndex;
        if (type == 1) {
            detailColumnIndex = cursor.getColumnIndex(DETAIL);
        } else {
            detailColumnIndex = cursor.getColumnIndex(HTML);
        }
        String detail = cursor.getString(detailColumnIndex);
        cursor.close();
        return detail;
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

    public void updateFavorite(String url, String localDoc, String remoteDoc) {
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        String whereClause = LINK + "=?";
        String[] whereArgs = {url};
        ContentValues contentValues = new ContentValues();
        contentValues.put(DETAIL, localDoc);
        contentValues.put(HTML, remoteDoc);
        mSQLiteDatabase.update(TABLE_FAVORITE, contentValues, whereClause, whereArgs);
    }

    public ArrayList<News> getFavoriteNews(int category) {
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        ArrayList<News> newsList = new ArrayList<>();
        String selection = CATEGORY + "=?";
        String[] selectionArgs = {"" + category};
        String[] columns = {TITLE, IMAGE_URL, LINK, DESCRIPTION, CATEGORY};
        Cursor cursor = mSQLiteDatabase.query(true, TABLE_FAVORITE, columns, selection,
                selectionArgs, null, null, null, null);
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
            news.setFavorite(true);
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
        String[] columns = {HTML};
        Cursor cursor = mSQLiteDatabase.query(TABLE_FAVORITE, columns, selection, selectionArgs, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            int detailColumnIndex = cursor.getColumnIndex(HTML);
            String detail = cursor.getString(detailColumnIndex);
            cursor.close();
            return !StringUtil.isBlank(detail);
        }
        return false;
    }

    public boolean isHistory(String url) {
        if (url == null) return false;
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String selection = LINK + "=?";
        String[] selectionArgs = {url};
        String[] columns = {HTML};
        Cursor cursor = mSQLiteDatabase.query(TABLE_NEWS, columns, selection, selectionArgs, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            int detailColumnIndex = cursor.getColumnIndex(HTML);
            String detail = cursor.getString(detailColumnIndex);
            cursor.close();
            return !StringUtil.isBlank(detail);
        }
        return false;
    }

    public void deleteHistory() {
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mSQLiteDatabase.delete(TABLE_NEWS, null, null);
    }

    public boolean isAnyItemInHistory() {
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String[] columns = {TITLE, IMAGE_URL, LINK, DESCRIPTION};
        Cursor cursor = mSQLiteDatabase.query(TABLE_NEWS, columns, null, null, null, null, null);
        int tmp = cursor.getCount();
        cursor.close();
        mDatabaseHelper.close();
        return tmp > 0;
    }
}
