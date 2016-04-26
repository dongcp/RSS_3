package com.framgia.rssfeed.base;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.framgia.rssfeed.R;

/**
 * Created by yue on 21/04/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private NavigationView mNavigationView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setSupportActionBar(mToolbar);
        addFragment();
        onCreateContentView();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getCurrentFragment().onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public void addFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, getFragment())
                .commit();
    }

    public void replaceFragment(BaseFragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment, tag)
                .setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_left_enter, R.animator.fragment_slide_right_exit)
                .addToBackStack("")
                .commit();
    }

    public void popFragment() {
        getFragmentManager().popBackStack();
    }

    public void popToSpecifyFragment(String tag) {
        getFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    protected abstract BaseFragment getFragment();

    protected abstract void onCreateContentView();

    protected BaseFragment getCurrentFragment() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        }
        return null;
    }

    private void findViews() {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

}
