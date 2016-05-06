package com.framgia.rssfeed.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.ui.adapter.FavoriteTabFragment;
import com.framgia.rssfeed.ui.base.BaseFragment;


public class FavoriteFragment extends BaseFragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FavoriteTabFragment mFavoriteTabFragment;
    public static final String TAG_FAVORITE_FRAGMENT = "favorite_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onCreateContentView(View rootView) {
        findViews(rootView);

    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_favorite);
    }

    @Override
    protected boolean enableBackButton() {
        return true;
    }

    private void findViews(View rootView) {
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        setupAdapter();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupAdapter() {
        String[] titles=getResources().getStringArray(R.array.title_all);
        mFavoriteTabFragment = new FavoriteTabFragment(getFragmentManager(),titles);
        mViewPager.setAdapter(mFavoriteTabFragment);
    }
}
