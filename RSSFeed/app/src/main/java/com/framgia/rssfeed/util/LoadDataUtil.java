package com.framgia.rssfeed.util;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yue on 25/04/2016.
 */
public class LoadDataUtil {

    private static LoadDataUtil sInstance;
    private XmlParser mParser;
    private OnLoadingListener mOnLoadingListener;

    private LoadDataUtil() {
        mParser = new XmlParser();
    }

    public static LoadDataUtil getInstance() {
        if (sInstance == null) {
            synchronized (LoadDataUtil.class) {
                if (sInstance == null) {
                    sInstance = new LoadDataUtil();
                }
            }
        }
        return sInstance;
    }

    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        this.mOnLoadingListener = onLoadingListener;
    }

    public void getDataFromNetwork(String urlString) {
        AsyncTask<String, Void, ArrayList<Object>> asyncTask = new AsyncTask<String, Void, ArrayList<Object>>() {
            @Override
            protected ArrayList<Object> doInBackground(String... params) {
                try {
                    if (mOnLoadingListener != null) {
                        mOnLoadingListener.onLoading();
                    }
                    return mParser.getNewsList(params[0]);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Object> objects) {
                super.onPostExecute(objects);
                if (mOnLoadingListener != null) {
                    mOnLoadingListener.onLoadComplete(objects);
                }
            }
        };
        asyncTask.execute(urlString);
    }

    public interface OnLoadingListener {
        void onLoading();

        void onLoadComplete(ArrayList<Object> objects);
    }
}
