package com.framgia.rssfeed;

import com.framgia.rssfeed.base.BaseActivity;
import com.framgia.rssfeed.base.BaseFragment;
import com.framgia.rssfeed.fragment.HomeFragment;

public class MainActivity extends BaseActivity{

    @Override
    protected BaseFragment getFragment() {
        return new HomeFragment();
    }

    @Override
    protected void onCreateContentView() {
    }
}
