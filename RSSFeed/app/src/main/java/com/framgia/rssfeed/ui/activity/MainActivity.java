package com.framgia.rssfeed.ui.activity;

import com.framgia.rssfeed.ui.base.BaseActivity;
import com.framgia.rssfeed.ui.base.BaseFragment;
import com.framgia.rssfeed.ui.fragment.HomeFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected BaseFragment getFragment() {
        return new HomeFragment();
    }

    @Override
    protected void onCreateContentView() {
    }
}
