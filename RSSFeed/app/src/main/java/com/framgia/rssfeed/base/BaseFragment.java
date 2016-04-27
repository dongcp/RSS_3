package com.framgia.rssfeed.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(enableBackButton());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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

    protected boolean enableBackButton() {
        return true;
    }

}
