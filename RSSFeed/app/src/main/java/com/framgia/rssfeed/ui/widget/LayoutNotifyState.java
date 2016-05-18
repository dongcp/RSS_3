package com.framgia.rssfeed.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.framgia.rssfeed.R;

/**
 * Created by yue on 26/04/2016.
 */
public class LayoutNotifyState extends RelativeLayout {

    public final static int TYPE_LOADING_LAYOUT = 1;
    public final static int TYPE_NO_DATA_LAYOUT = 2;
    public final static int TYPE_NETWORK_ERROR_LAYOUT = 3;
    public final static int TYPE_TIME_OUT = 4;
    private ProgressBar mProgressLoading;
    private LinearLayout mNoDataLayout;
    private LinearLayout mNetworkErrorLayout;
    private LinearLayout mTimeOutLayout;
    private int mType;

    public LayoutNotifyState(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.layout_notify_state, this);
        findViews();
        TypedArray arr = null;
        if (attrs != null) {
            arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LayoutNotifyState, 0, 0);
        }
        mType = arr.getInt(R.styleable.LayoutNotifyState_type, TYPE_LOADING_LAYOUT);
        changeLayout(mType);
    }

    public void show(View hiddenView) {
        hiddenView.setVisibility(GONE);
        this.setVisibility(VISIBLE);
        if (mType == TYPE_LOADING_LAYOUT) {
            mProgressLoading.setIndeterminate(true);
        }
    }

    public void show(int type, View hiddenView) {
        changeType(type);
        show(hiddenView);
    }

    public void hide(View showedView) {
        showedView.setVisibility(VISIBLE);
        this.setVisibility(GONE);
        if (mType == TYPE_LOADING_LAYOUT) {
            mProgressLoading.setIndeterminate(false);
        }
    }

    public void hide(int type, View showedView) {
        changeType(type);
        hide(showedView);
    }

    public void changeType(int type) {
        mType = type;
        changeLayout(mType);
    }

    private void makeAllGone() {
        mProgressLoading.setVisibility(GONE);
        mNoDataLayout.setVisibility(GONE);
        mNetworkErrorLayout.setVisibility(GONE);
        mTimeOutLayout.setVisibility(GONE);
    }

    private void changeLayout(int type) {
        switch (type) {
            case TYPE_LOADING_LAYOUT:
                makeAllGone();
                mProgressLoading.setVisibility(VISIBLE);
                break;
            case TYPE_NO_DATA_LAYOUT:
                makeAllGone();
                mNoDataLayout.setVisibility(VISIBLE);
                break;
            case TYPE_NETWORK_ERROR_LAYOUT:
                makeAllGone();
                mNetworkErrorLayout.setVisibility(VISIBLE);
                break;
            case TYPE_TIME_OUT:
                makeAllGone();
                mTimeOutLayout.setVisibility(VISIBLE);
                break;
        }
    }

    private void findViews() {
        mProgressLoading = (ProgressBar) findViewById(R.id.progress_loading);
        mNoDataLayout = (LinearLayout) findViewById(R.id.no_item_layout);
        mNetworkErrorLayout = (LinearLayout) findViewById(R.id.network_error_layout);
        mTimeOutLayout = (LinearLayout) findViewById(R.id.connection_timeout_layout);
    }
}
