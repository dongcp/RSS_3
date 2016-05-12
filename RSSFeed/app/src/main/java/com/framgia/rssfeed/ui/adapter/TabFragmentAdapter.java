package com.framgia.rssfeed.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VULAN on 4/23/2016.
 */
public class TabFragmentAdapter extends android.support.v13.app.FragmentPagerAdapter {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleFragmentList = new ArrayList<>();

    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mTitleFragmentList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleFragmentList.get(position);
    }
}
