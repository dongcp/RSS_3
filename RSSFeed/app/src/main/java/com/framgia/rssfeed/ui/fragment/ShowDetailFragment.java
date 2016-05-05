package com.framgia.rssfeed.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.data.bean.News;
import com.framgia.rssfeed.ui.base.BaseFragment;
import com.framgia.rssfeed.ui.base.Constants;
import com.framgia.rssfeed.ui.widget.LayoutNotifyState;
import com.framgia.rssfeed.util.NetworkUtil;

/**
 * Created by yue on 27/04/2016.
 */
public class ShowDetailFragment extends BaseFragment {

    private WebView mWebView;
    private LayoutNotifyState mNotifyStateLayout;
    private News mNews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_show_detail;
    }

    @Override
    protected void onCreateContentView(View rootView) {
        findViews(rootView);
        String appCachePath = getActivity().getCacheDir().getAbsolutePath();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        if (NetworkUtil.getInstance(getActivity()).isNetworkAvailable()) {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(appCachePath);
        mWebView.setWebViewClient(new RSSWebViewClient());
        mWebView.loadUrl(mNews.getLink());
    }

    @Override
    protected boolean enableNavigationDrawer() {
        return false;
    }

    @Override
    protected boolean enableFavoriteButton() {
        return true;
    }

    @Override
    protected boolean isFavorite() {
        return mNews.isFavorite();
    }

    private void getData() {
        Bundle bundle = getArguments();
        mNews = (News) bundle.getSerializable(Constants.BUNDLE_NEWS);
    }

    private void findViews(View rootView) {
        mWebView = (WebView) rootView.findViewById(R.id.web_view);
        mNotifyStateLayout = (LayoutNotifyState) rootView.findViewById(R.id.notify_state_layout);
    }

    private class RSSWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mNotifyStateLayout.show(LayoutNotifyState.TYPE_LOADING_LAYOUT, mWebView);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mNotifyStateLayout.hide(mWebView);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
