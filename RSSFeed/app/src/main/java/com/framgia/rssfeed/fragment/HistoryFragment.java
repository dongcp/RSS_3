package com.framgia.rssfeed.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.framgia.rssfeed.ListViewItemDecoration;
import com.framgia.rssfeed.OnRecyclerViewItemClickListener;
import com.framgia.rssfeed.R;
import com.framgia.rssfeed.adapter.ListNewsAdapter;
import com.framgia.rssfeed.base.BaseFragment;
import com.framgia.rssfeed.base.Constants;
import com.framgia.rssfeed.bean.News;
import com.framgia.rssfeed.data.local.DatabaseHandler;


public class HistoryFragment extends BaseFragment implements OnRecyclerViewItemClickListener {

    private RecyclerView mRecyclerView;
    private ListNewsAdapter mListHistoryAdapter;


    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_new;
    }

    @Override
    protected void onCreateContentView(View rootView) {
        findView(rootView);
        setupRecyclerView(getContext());
    }

    public void findView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_news);
    }

    public void setupRecyclerView(Context context) {
        ListViewItemDecoration mListViewItemDecoration= new ListViewItemDecoration(context);;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(mListViewItemDecoration);
        mListHistoryAdapter = new ListNewsAdapter(context, mRecyclerView.getLayoutManager());
        mListHistoryAdapter.addItems(DatabaseHandler.getInstance(context).getHistoryNews());
        mListHistoryAdapter.setOnRecyclerViewItemClickListener(this);
        mRecyclerView.setAdapter(mListHistoryAdapter);
    }

    @Override
    protected boolean enableBackButton() {
        return false;
    }


    @Override
    public void onItemClickListener(int position) {
        News news = mListHistoryAdapter.getItem(position);
        DatabaseHandler.getInstance(getActivity()).insertNewsInfo(news);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_URL, news.getLink());
        ShowDetailFragment fragment = new ShowDetailFragment();
        fragment.setArguments(bundle);
        getBaseActivity().replaceFragment(fragment, ListNewsFragment.TAG_LIST_NEWS_FRAGMENT);
    }
}
