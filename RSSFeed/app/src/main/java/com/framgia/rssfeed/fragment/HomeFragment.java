package com.framgia.rssfeed.fragment;

import android.util.Log;
import android.view.View;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.base.BaseFragment;
import com.framgia.rssfeed.bean.News;
import com.framgia.rssfeed.utility.LoadDataUtil;

import java.util.ArrayList;

/**
 * Created by yue on 21/04/2016.
 */
public class HomeFragment extends BaseFragment implements LoadDataUtil.OnLoadingListener {

    private final static String URL_TINH_TE = "https://tinhte.vn/rss/";
    private final static String URL_VNEXPRESS = "http://vnexpress.net/rss/thoi-su.rss";
    private ArrayList<News> mNewsList;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onCreateContentView(View rootView) {
        mNewsList = new ArrayList<>();
        LoadDataUtil.getInstance().setOnLoadingListener(this);
        LoadDataUtil.getInstance().getDataFromNetwork(URL_TINH_TE);
        LoadDataUtil.getInstance().getDataFromNetwork(URL_VNEXPRESS);
    }

    @Override
    public void onLoading() {
    }

    @Override
    public void onLoadComplete(ArrayList<Object> objects) {
        for (int i = 0; i < objects.size(); i++) {
            mNewsList.add((News) objects.get(i));
        }
        Log.e("List size", "" + mNewsList.size());
        if (mNewsList.size() > 0) {
            Log.e("0", mNewsList.get(0).getTitle());
        }
    }
}
