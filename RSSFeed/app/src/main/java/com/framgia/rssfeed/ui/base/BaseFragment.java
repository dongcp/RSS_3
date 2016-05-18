package com.framgia.rssfeed.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.ui.fragment.FavoriteFragment;

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
        } else {
            getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_switch).setVisible(enableSwitchButton());
        menu.findItem(R.id.action_refresh).setVisible(enableRefreshButton());
        menu.findItem(R.id.action_open_favorite_list).setVisible(enableFavoriteList());
        MenuItem item = menu.findItem(R.id.action_favorite);
        if (enableFavoriteButton()) {
            item.setVisible(true);
            if (isFavorite()) {
                item.setChecked(true);
                item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_highlight_star));
            } else {
                item.setChecked(false);
                item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_normal_star));
            }
        } else {
            item.setVisible(false);
        }
        if (enableToolbar()) enableMenuItem();
        else disableMenuItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (enableBackButton()) {
                    onBackPressed();
                }
                break;
            case R.id.action_open_favorite_list:
                if (enableFavoriteList()) {
                    getBaseActivity().replaceFragment(new FavoriteFragment()
                            , FavoriteFragment.TAG_FAVORITE_FRAGMENT);
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
        getBaseActivity().popFragment();
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

    protected boolean enableFavoriteList() {
        return false;
    }

    protected boolean enableFavoriteButton() {
        return false;
    }

    protected boolean enableSwitchButton() {
        return false;
    }

    protected boolean enableRefreshButton() {
        return false;
    }

    protected boolean isFavorite() {
        return false;
    }

    public void setFavorite(boolean isFavorite) {
        if (getBaseActivity().getToolbar() != null) {
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

    protected boolean enableToolbar() {
        return true;
    }

    public void disableMenuItem() {
        if (getBaseActivity().getToolbar() != null) {
            getBaseActivity().getToolbar().getMenu().setGroupEnabled(R.id.action_group, false);
        }
    }

    public void enableMenuItem() {
        if (getBaseActivity().getToolbar() != null) {
            getBaseActivity().getToolbar().getMenu().setGroupEnabled(R.id.action_group, true);
        }
    }
}
