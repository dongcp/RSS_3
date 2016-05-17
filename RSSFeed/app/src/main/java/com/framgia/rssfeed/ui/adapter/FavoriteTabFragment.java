package com.framgia.rssfeed.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.framgia.rssfeed.ui.fragment.DetailFavoriteFragment;

/**
 * Created by VULAN on 5/6/2016.
 */
public class FavoriteTabFragment extends FragmentPagerAdapter {
    private static int NUMB_TAB = 6;
    private String[] titles = new String[NUMB_TAB];

    public FavoriteTabFragment(FragmentManager fm, String[] titles) {
        super(fm);
        for (int i = 0; i < titles.length; i++) {
            this.titles[i] = titles[i];
        }
    }

    @Override
    public Fragment getItem(int position) {
        return DetailFavoriteFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return NUMB_TAB;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
