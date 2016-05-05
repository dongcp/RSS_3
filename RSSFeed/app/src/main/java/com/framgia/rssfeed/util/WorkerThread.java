package com.framgia.rssfeed.util;

import android.content.Context;

import com.framgia.rssfeed.data.bean.News;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yue on 09/05/2016.
 */
public class WorkerThread implements Runnable {

    public final static int WORK_CACHE = 0;
    public final static int WORK_LOAD_DOC = 1;
    public final static int WORK_LOAD_NEWS_LIST = 2;
    private Context mContext;
    private News mNews;
    private OnWorkListener mOnWorkListener;
    private int mWork;

    public WorkerThread(Context context, int work, String url) {
        mContext = context;
        mWork = work;
        mNews = new News();
        mNews.setLink(url);
    }

    public WorkerThread(Context context, int work, News news) {
        mContext = context;
        mWork = work;
        mNews = news;
    }

    public void setOnWorkListener(OnWorkListener onWorkListener) {
        this.mOnWorkListener = onWorkListener;
    }

    @Override
    public void run() {
        try {
            doWork(mWork);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public void doWork(int work) throws IOException, XmlPullParserException {
        switch (work) {
            case WORK_LOAD_DOC:
                ArrayList<Object> docs = XmlParser.getDocumentDescription(mNews.getLink());
                if (mOnWorkListener != null) {
                    mOnWorkListener.onWorkDone(docs);
                }
                break;
            case WORK_LOAD_NEWS_LIST:
                ArrayList<Object> newsList = XmlParser.getNewsList(mContext, mNews.getLink());
                if (mOnWorkListener != null) {
                    mOnWorkListener.onWorkDone(newsList);
                }
                break;
        }
    }

    public interface OnWorkListener {
        void onWorkDone(ArrayList<Object> objects);
    }
}
