package com.framgia.rssfeed.ui.base;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.ui.fragment.FavoriteFragment;
import com.framgia.rssfeed.ui.fragment.HomeFragment;
import com.framgia.rssfeed.ui.fragment.ListNewsFragment;

/**
 * Created by yue on 21/04/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private NavigationView.OnNavigationItemSelectedListener mNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            ListNewsFragment fragment = new ListNewsFragment();
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {
                case R.id.nav_news:
                    bundle.putInt(Constants.BUNDLE_INDEX, Constants.INDEX_NEWS);
                    break;
                case R.id.nav_technology:
                    bundle.putInt(Constants.BUNDLE_INDEX, Constants.INDEX_TECHNOLOGY);
                    break;
                case R.id.nav_business:
                    bundle.putInt(Constants.BUNDLE_INDEX, Constants.INDEX_BUSINESS);
                    break;
                case R.id.nav_law:
                    bundle.putInt(Constants.BUNDLE_INDEX, Constants.INDEX_LAWS);
                    break;
                case R.id.nav_entertainment:
                    bundle.putInt(Constants.BUNDLE_INDEX, Constants.INDEX_ENTERTAINMENT);
                    break;
                case R.id.nav_education:
                    bundle.putInt(Constants.BUNDLE_INDEX, Constants.INDEX_EDUCATION);
                    break;
            }
            item.setChecked(true);
            getDrawerLayout().closeDrawer(GravityCompat.START);
            fragment.setArguments(bundle);
            if (item.getItemId() == R.id.nav_favorite) {
                replaceFragment(new FavoriteFragment(), FavoriteFragment.TAG_FAVORITE_FRAGMENT);
            } else {
                replaceFragment(fragment, HomeFragment.TAG_HOME_FRAGMENT);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setSupportActionBar(mToolbar);
        addFragment();
        onCreateContentView();
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getCurrentFragment().onBackPressed();
        } else {
            if (getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
                getDrawerLayout().closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    public void addFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, getFragment())
                .commit();
    }

    public void replaceFragment(BaseFragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit)
                .replace(R.id.fragmentContainer, fragment, tag)
                .addToBackStack("")
                .commit();
    }

    public void popFragment() {
        getSupportFragmentManager().popBackStack();
    }

    public void popToSpecifyFragment(String tag) {
        getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    public Toolbar getToolbar() {
        return mToolbar;
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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationView.setNavigationItemSelectedListener(mNavigationItemSelectedListener);
    }
}
