package com.framgia.rssfeed.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.rssfeed.R;

/**
 * Created by yue on 21/04/2016.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getFragmentLayoutId(), container, false);
        onCreateContentView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().getSupportActionBar().setTitle(getTitle());

        if (enableBackButton()) {
            getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getBaseActivity().getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
            if (enableNavigationDrawer()) {
                getBaseActivity().getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                getBaseActivity().getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        } else {
            if (enableNavigationDrawer()) {
                getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getBaseActivity().getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
                getBaseActivity().getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getBaseActivity().getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_switch).setVisible(enableSwitchButton());
        MenuItem item = menu.findItem(R.id.action_favorite);
        if (enableFavoriteButton()) {
            item.setVisible(true);
            if (isFavorite()) {
                item.setChecked(true);
                item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_highlight_star));
            } else {
                item.setChecked(true);
                item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_normal_star));
            }
        } else {
            item.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (enableBackButton()) {
                    onBackPressed();
                } else {
                    if (enableNavigationDrawer()) {
                        getBaseActivity().getDrawerLayout().openDrawer(GravityCompat.START);
                    }
                }
                break;
            case R.id.action_favorite:
                if (item.isChecked()) {
                    item.setChecked(false);
                    item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_normal_star));
                } else {
                    item.setChecked(true);
                    item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_highlight_star));
                }
            default:
                onMenuItemClick(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getFragmentLayoutId();

    protected abstract void onCreateContentView(View rootView);

    protected void onBackPressed() {
        if (getBaseActivity().getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            getBaseActivity().getDrawerLayout().closeDrawer(GravityCompat.START);
        } else {
            getBaseActivity().popFragment();
        }
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected String getTitle() {
        return getString(R.string.app_name);
    }

    protected void onMenuItemClick(MenuItem item) {
    }

    protected boolean enableBackButton() {
        return true;
    }

    protected boolean enableNavigationDrawer() {
        return true;
    }

    protected boolean enableFavoriteButton() {
        return false;
    }

    protected boolean enableSwitchButton() {
        return false;
    }

    protected boolean isFavorite() {
        return false;
    }

    public void setFavorite(boolean isFavorite) {
        MenuItem item = getBaseActivity().getToolbar().getMenu().findItem(R.id.action_favorite);
        item.setChecked(isFavorite);
        if (isFavorite) {
            item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_highlight_star));
        } else {
            item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_normal_star));
        }
        getBaseActivity().getToolbar().invalidate();
    }
}
