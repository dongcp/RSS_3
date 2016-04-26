package com.framgia.rssfeed.fragment;

import android.view.View;
import android.widget.Button;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.base.BaseFragment;

/**
 * Created by yue on 21/04/2016.
 */
public class HomeFragment extends BaseFragment {

    public final static String TAG_HOME_FRAGMENT = "home fragment";
    private Button mButtonNext;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onCreateContentView(View rootView) {
        mButtonNext = (Button) rootView.findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().replaceFragment(new ListNewsFragment(), TAG_HOME_FRAGMENT);
            }
        });
    }

    @Override
    protected boolean enableBackButton() {
        return false;
    }
}
