package com.framgia.rssfeed.fragment;

import android.view.View;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.base.BaseFragment;


public class HistoryFragment extends BaseFragment {

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void onCreateContentView(View rootView) {
    }

    @Override
    protected boolean enableBackButton() {
        return false;
    }
}
