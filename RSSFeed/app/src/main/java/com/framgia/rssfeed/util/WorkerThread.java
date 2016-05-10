package com.framgia.rssfeed.util;

import android.content.Context;

import com.framgia.rssfeed.data.bean.News;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yue on 09/05/2016.
 */
public class WorkerThread implements Runnable, Comparable<WorkerThread> {

    private Context mContext;
    private News mNews;
    private OnWorkListener mOnWorkListener;
    private Work mWork;
    private WorkPriority mPriority;

    public WorkerThread(Context context, Work work, String url, WorkPriority priority) {
        mContext = context;
        mWork = work;
        mNews = new News(url);
        mPriority = priority;
    }

    public WorkerThread(Context context, Work work, News news, WorkPriority priority) {
        mContext = context;
        mWork = work;
        mNews = news;
        mPriority = priority;
    }

    public void setOnWorkListener(OnWorkListener onWorkListener) {
        this.mOnWorkListener = onWorkListener;
    }

    public WorkPriority getPriority() {
        return mPriority;
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

    public void doWork(Work work) throws IOException, XmlPullParserException {
        switch (work) {
            case LOAD_DOC:
                ArrayList<Object> docs = new ArrayList<>();
                docs.addAll(XmlParser.getDocumentDescription(mNews.getLink()));
                if (mOnWorkListener != null) {
                    mOnWorkListener.onWorkDone(docs);
                }
                break;
            case LOAD_NEWS_LIST:
                ArrayList<Object> newsList = XmlParser.getNewsList(mContext, mNews.getLink());
                if (mOnWorkListener != null) {
                    mOnWorkListener.onWorkDone(newsList);
                }
                break;
            case CACHE:
                UrlCacheUtil.getInstance().cache(mNews);
                break;
            case REMOVE:
                UrlCacheUtil.getInstance().remove(mNews);
                break;
        }
    }

    @Override
    public int compareTo(WorkerThread another) {
        return another.getPriority().getValue() - this.mPriority.getValue();
    }

    public enum WorkPriority {
        MIN(1),
        NORMAL(5),
        MAX(10);

        private int mValue;

        WorkPriority(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    public enum Work {
        CACHE,
        REMOVE,
        LOAD_DOC,
        LOAD_NEWS_LIST
    }

    public interface OnWorkListener {
        void onWorkDone(ArrayList<Object> objects);
    }
}
