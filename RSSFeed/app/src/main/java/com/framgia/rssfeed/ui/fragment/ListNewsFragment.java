package com.framgia.rssfeed.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.data.bean.News;
import com.framgia.rssfeed.ui.adapter.ListNewsAdapter;
import com.framgia.rssfeed.ui.base.BaseFragment;
import com.framgia.rssfeed.ui.base.Constants;
import com.framgia.rssfeed.ui.decoration.GridViewItemDecoration;
import com.framgia.rssfeed.ui.decoration.ListViewItemDecoration;
import com.framgia.rssfeed.ui.widget.LayoutNotifyState;
import com.framgia.rssfeed.util.MonitorWorkerThreadUtil;
import com.framgia.rssfeed.util.NetworkUtil;
import com.framgia.rssfeed.util.OnRecyclerViewItemClickListener;
import com.framgia.rssfeed.util.WorkerThread;

import java.util.ArrayList;

/**
 * Created by yue on 25/04/2016.
 */
public class ListNewsFragment extends BaseFragment {

    public final static String TAG_LIST_NEWS_FRAGMENT = "list news fragment";
    private final static int NUMBER_OF_COLUMN = 2;
    private RecyclerView mListNews;
    private LayoutNotifyState mNotifyStateLayout;
    private ListNewsAdapter mAdapter;
    private ListViewItemDecoration mListViewItemDecoration;
    private GridViewItemDecoration mGridViewItemDecoration;
    private String mUrl;
    private int mIndex;
    private OnRecyclerViewItemClickListener mOnItemClickListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClickListener(View view, int position) {
            News news = mAdapter.getItem(position);
            if (view instanceof LinearLayout) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_NEWS, news);
                bundle.putInt(Constants.BUNDLE_TYPE, ShowDetailFragment.TYPE_NORMAL);
                ShowDetailFragment fragment = new ShowDetailFragment();
                fragment.setArguments(bundle);
                getBaseActivity().replaceFragment(fragment, TAG_LIST_NEWS_FRAGMENT);
            } else if (view instanceof ImageView) {
                WorkerThread.Work work;
                if (!news.isFavorite()) {
                    work = WorkerThread.Work.CACHE;
                } else {
                    work = WorkerThread.Work.REMOVE;
                }
                WorkerThread worker = new WorkerThread(getActivity(), work, news, WorkerThread.WorkPriority.NORMAL);
                MonitorWorkerThreadUtil.getInstance().assign(worker);
                news.setFavorite(!news.isFavorite());
                mAdapter.notifyItemChanged(position);
            }
        }
    };
    private WorkerThread.OnWorkListener mOnWorkListener = new WorkerThread.OnWorkListener() {
        @Override
        public void onWorkDone(final ArrayList<Object> objects) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (objects != null) {
                        mAdapter.removeAllItemsIfExist();
                        mNotifyStateLayout.hide(mListNews);
                        int arraySize = objects.size();
                        for (int i = 0; i < arraySize; i++) {
                            News news = (News) objects.get(i);
                            news.setCategory(mIndex);
                            mAdapter.addItem(news);
                        }
                        setFavorite(mAdapter.isAllFavorite());
                    } else {
                        mNotifyStateLayout.show(LayoutNotifyState.TYPE_NO_DATA_LAYOUT, mListNews);
                    }
                }
            });
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListViewItemDecoration = new ListViewItemDecoration(getActivity());
        mGridViewItemDecoration = new GridViewItemDecoration(getActivity());
        mAdapter = new ListNewsAdapter(getActivity(), null);
        getData();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_list_news;
    }

    @Override
    protected void onCreateContentView(View rootView) {
        findViews(rootView);
        mListNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListNews.addItemDecoration(mListViewItemDecoration);
        mAdapter.setOnRecyclerViewItemClickListener(mOnItemClickListener);
        mListNews.setAdapter(mAdapter);
        mAdapter.changeLayoutManager(mListNews.getLayoutManager());
        switch (mIndex) {
            case Constants.INDEX_BUSINESS:
                getBaseActivity().getNavigationView().setCheckedItem(R.id.nav_business);
                break;
            case Constants.INDEX_EDUCATION:
                getBaseActivity().getNavigationView().setCheckedItem(R.id.nav_education);
                break;
            case Constants.INDEX_ENTERTAINMENT:
                getBaseActivity().getNavigationView().setCheckedItem(R.id.nav_entertainment);
                break;
            case Constants.INDEX_TECHNOLOGY:
                getBaseActivity().getNavigationView().setCheckedItem(R.id.nav_technology);
                break;
            case Constants.INDEX_LAWS:
                getBaseActivity().getNavigationView().setCheckedItem(R.id.nav_law);
                break;
            case Constants.INDEX_NEWS:
                getBaseActivity().getNavigationView().setCheckedItem(R.id.nav_news);
                break;
        }
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            if (mNotifyStateLayout.getVisibility() == View.VISIBLE) {
                mNotifyStateLayout.hide(mListNews);
            }
            WorkerThread workerThread = new WorkerThread(getActivity(), WorkerThread.Work.LOAD_NEWS_LIST
                    , mUrl, WorkerThread.WorkPriority.MAX);
            workerThread.setOnWorkListener(mOnWorkListener);
            MonitorWorkerThreadUtil.getInstance().assign(workerThread);
            mNotifyStateLayout.show(LayoutNotifyState.TYPE_LOADING_LAYOUT, mListNews);
        } else {
            if (mAdapter.getItemCount() == 0) {
                mNotifyStateLayout.show(LayoutNotifyState.TYPE_NETWORK_ERROR_LAYOUT, mListNews);
            }
        }
    }

    @Override
    protected String getTitle() {
        switch (mIndex) {
            case Constants.INDEX_BUSINESS:
                return getString(R.string.business);
            case Constants.INDEX_EDUCATION:
                return getString(R.string.education);
            case Constants.INDEX_ENTERTAINMENT:
                return getString(R.string.entertainment);
            case Constants.INDEX_NEWS:
                return getString(R.string.news);
            case Constants.INDEX_TECHNOLOGY:
                return getString(R.string.technology);
            case Constants.INDEX_LAWS:
                return getString(R.string.law);
            default:
                return getString(R.string.app_name);
        }
    }

    @Override
    protected boolean enableSwitchButton() {
        return true;
    }

    @Override
    protected boolean enableFavoriteButton() {
        return true;
    }

    @Override
    protected void onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                int arraySize = mAdapter.getItemCount();
                if (item.isChecked()) {
                    for (int i = 0; i < arraySize; i++) {
                        WorkerThread worker = new WorkerThread(getActivity(), WorkerThread.Work.CACHE
                                , mAdapter.getItem(i), WorkerThread.WorkPriority.MIN);
                        MonitorWorkerThreadUtil.getInstance().assign(worker);
                    }
                } else {
                    MonitorWorkerThreadUtil.getInstance().clear();
                    for (int i = 0; i < arraySize; i++) {
                        WorkerThread worker = new WorkerThread(getActivity(), WorkerThread.Work.REMOVE
                                , mAdapter.getItem(i), WorkerThread.WorkPriority.MIN);
                        MonitorWorkerThreadUtil.getInstance().assign(worker);
                    }
                }
                for (int i = 0; i < arraySize; i++) {
                    mAdapter.getItem(i).setFavorite(item.isChecked());
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.action_switch:
                if (mListNews.getLayoutManager() instanceof GridLayoutManager) {
                    mListNews.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mListNews.removeItemDecoration(mGridViewItemDecoration);
                    mListNews.addItemDecoration(mListViewItemDecoration);
                } else {
                    mListNews.setLayoutManager(new GridLayoutManager(getActivity(), NUMBER_OF_COLUMN));
                    mListNews.removeItemDecoration(mListViewItemDecoration);
                    mListNews.addItemDecoration(mGridViewItemDecoration);
                }
                mListNews.setAdapter(mAdapter);
                mAdapter.changeLayoutManager(mListNews.getLayoutManager());
                break;
        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        mIndex = bundle.getInt(Constants.BUNDLE_INDEX, Constants.INDEX_TECHNOLOGY);
        switch (mIndex) {
            case Constants.INDEX_BUSINESS:
                mUrl = Constants.URL_BUSINESS;
                break;
            case Constants.INDEX_EDUCATION:
                mUrl = Constants.URL_EDUCATION;
                break;
            case Constants.INDEX_ENTERTAINMENT:
                mUrl = Constants.URL_ENTERTAINMENT;
                break;
            case Constants.INDEX_TECHNOLOGY:
                mUrl = Constants.URL_TECHNOLOGY;
                break;
            case Constants.INDEX_LAWS:
                mUrl = Constants.URL_LAW;
                break;
            case Constants.INDEX_NEWS:
                mUrl = Constants.URL_NEWS;
                break;
        }
    }

    private void findViews(View rootView) {
        mListNews = (RecyclerView) rootView.findViewById(R.id.list_news);
        mNotifyStateLayout = (LayoutNotifyState) rootView.findViewById(R.id.notify_state_layout);
    }
}
