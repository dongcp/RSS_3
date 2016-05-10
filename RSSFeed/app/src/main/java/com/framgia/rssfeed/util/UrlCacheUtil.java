package com.framgia.rssfeed.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.framgia.rssfeed.data.bean.News;
import com.framgia.rssfeed.data.local.DatabaseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by yue on 05/05/2016.
 */
public class UrlCacheUtil {

    private static UrlCacheUtil sInstance;
    private Context mContext;
    private boolean mIsCachable;

    private UrlCacheUtil() {
        mIsCachable = true;
    }

    public static UrlCacheUtil getInstance() {
        if (sInstance == null) {
            synchronized (UrlCacheUtil.class) {
                if (sInstance == null) sInstance = new UrlCacheUtil();
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        mContext = context;
    }

    public String cacheImageIfNeed(String imageUrl) throws IOException {
        File imageFile = getImageFile(imageUrl);
        if (!imageFile.exists()) {
            downloadFile(imageUrl, imageFile);
        }
        HttpRequest.getInstance().disconnect(HttpRequest.getInstance().getConnection());
        return imageFile.getAbsolutePath();
    }

    public void cache(News news) {
        AsyncTask<Object, Void, Void> asyncTask = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                if (!mIsCachable) cancel(true);
                try {
                    ArrayList<Object> objects = XmlParser.getDocumentDescription(((News) params[0]).getLink());
                    if (objects != null) {
                        cacheImageIfNeed(((News) params[0]).getLink());
                        DatabaseHandler.getInstance(mContext).insertFavoriteInfo(((News) params[0]), (String) objects.get(0));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, news);
    }

    public void cache(ArrayList<News> newsList) {
        int size = newsList.size();
        for (int i = 0; i < size; i++) {
            if (!mIsCachable) break;
            cache(newsList.get(i));
        }
    }

    public void remove(News news) {
        AsyncTask<News, Void, Void> asyncTask = new AsyncTask<News, Void, Void>() {
            @Override
            protected Void doInBackground(News... params) {
                DatabaseHandler.getInstance(mContext).removeFavorite(params[0].getLink());
                return null;
            }
        };
        asyncTask.execute(news);
    }

    public void remove(ArrayList<News> newsList) {
        mIsCachable = false;
        int arraySize = newsList.size();
        for (int i = 0; i < arraySize; i++) {
            remove(newsList.get(i));
        }
        mIsCachable = true;
    }

    public File getImageFile(String imageUrl) {
        File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File appFolder = new File(downloadFolder, mContext.getPackageName());
        if (!appFolder.exists()) {
            appFolder.mkdirs();
        }
        String fileName = getFileName(imageUrl);
        return new File(appFolder, fileName);
    }

    private void downloadFile(String imgUrl, File file) throws IOException {
        URL url = new URL(imgUrl);
        HttpRequest.getInstance().makeConnection(imgUrl);
        file.createNewFile();
        FileOutputStream fileOutput = new FileOutputStream(file);
        InputStream inputStream = url.openStream();
        byte[] buffer = new byte[1024];
        int bufferLength = 0;
        while ((bufferLength = inputStream.read(buffer)) > 0) {
            fileOutput.write(buffer, 0, bufferLength);
        }
        fileOutput.close();
        inputStream.close();
    }

    private String getFileName(String imageUrl) {
        String imageName = "";
        for (int i = imageUrl.length() - 1; i >= 0; i--) {
            if (imageUrl.charAt(i) != '/') {
                imageName += imageUrl.charAt(i);
            } else break;
        }
        return new StringBuilder(imageName).reverse().toString().trim();
    }
}
