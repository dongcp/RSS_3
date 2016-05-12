package com.framgia.rssfeed.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.ui.adapter.TabFragmentAdapter;
import com.framgia.rssfeed.ui.base.BaseFragment;

/**
 * Created by yue on 21/04/2016.
 */
public class HomeFragment extends BaseFragment {

    public final static String TAG_HOME_FRAGMENT = "home fragment";
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabFragmentAdapter mTabFragmentAdapter;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onCreateContentView(View rootView) {
        findView(rootView);
    }

    @Override
    protected boolean enableBackButton() {
        return false;
    }

    @Override
    protected boolean enableFavoriteList() {
        return true;
    }

    private void findView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tab_layout_background_color));
        mTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.white)
                , ContextCompat.getColor(getActivity(), R.color.white));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        mTabLayout.setSelectedTabIndicatorHeight(getResources().getDimensionPixelSize(R.dimen.common_size_3));
    }

    private void setupViewPager() {
        mTabFragmentAdapter = new TabFragmentAdapter(getChildFragmentManager());
        mTabFragmentAdapter.addFragment(new NewsFragment(), getString(R.string.news_uppercase));
        mTabFragmentAdapter.addFragment(new HistoryFragment(), getString(R.string.history_uppercase));
        mViewPager.setAdapter(mTabFragmentAdapter);
    }
}
