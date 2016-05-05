package com.framgia.rssfeed.ui.fragment;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.data.bean.News;
import com.framgia.rssfeed.data.local.DatabaseHandler;
import com.framgia.rssfeed.ui.base.BaseFragment;
import com.framgia.rssfeed.ui.base.Constants;
import com.framgia.rssfeed.ui.widget.LayoutNotifyState;
import com.framgia.rssfeed.util.MonitorWorkerThreadUtil;
import com.framgia.rssfeed.util.UrlCacheUtil;
import com.framgia.rssfeed.util.WorkerThread;

import java.util.ArrayList;

/**
 * Created by yue on 27/04/2016.
 */
public class ShowDetailFragment extends BaseFragment {

    public final static int TYPE_HISTORY = 1;
    public final static int TYPE_FAVORITE = 2;
    private TextView mTextContent;
    private LayoutNotifyState mNotifyStateLayout;
    private News mNews;
    private WorkerThread.OnWorkListener mOnWorkListener = new WorkerThread.OnWorkListener() {
        @Override
        public void onWorkDone(final ArrayList<Object> objects) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (objects != null) {
                        mNotifyStateLayout.hide(mTextContent);
                        final String doc = (String) objects.get(0);
                        mTextContent.setText(Html.fromHtml(doc, new ImageGetter(getActivity()), null));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DatabaseHandler.getInstance(getActivity()).insertNewsInfo(mNews, doc);
                            }
                        }).start();
                    } else {
                        mNotifyStateLayout.show(LayoutNotifyState.TYPE_NO_DATA_LAYOUT, mTextContent);
                    }
                }
            });
        }
    };

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
        WorkerThread workerThread = new WorkerThread(getActivity(), WorkerThread.WORK_LOAD_DOC, mNews);
        workerThread.setOnWorkListener(mOnWorkListener);
        MonitorWorkerThreadUtil.getInstance().assign(workerThread);
        mNotifyStateLayout.show(LayoutNotifyState.TYPE_LOADING_LAYOUT, mTextContent);
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

    @Override
    protected void onMenuItemClick(MenuItem item) {
        if (item.isChecked()) {
            UrlCacheUtil.getInstance().cache(mNews);
        } else {
            UrlCacheUtil.getInstance().remove(mNews);
        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        mNews = (News) bundle.getSerializable(Constants.BUNDLE_NEWS);
    }

    private void findViews(View rootView) {
        mNotifyStateLayout = (LayoutNotifyState) rootView.findViewById(R.id.notify_state_layout);
        mTextContent = (TextView) rootView.findViewById(R.id.text_content);
    }

    private static class ImageGetter implements Html.ImageGetter {

        private Context mContext;

        public ImageGetter(Context context) {
            mContext = context;
        }

        @Override
        public Drawable getDrawable(String source) {
            Drawable drawable = Drawable.createFromPath(source);
            if (drawable == null) {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.image_not_available);
            }
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point screenSize = new Point();
            display.getSize(screenSize);
            int imageWidth = screenSize.x;
            float ratio = imageWidth / drawable.getIntrinsicWidth();
            drawable.setBounds(0, 0, imageWidth, (int) (drawable.getIntrinsicHeight() * ratio));
            return drawable;
        }
    }
}
