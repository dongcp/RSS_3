package com.framgia.rssfeed.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.framgia.rssfeed.util.NetworkUtil;
import com.framgia.rssfeed.util.WorkerThread;

import org.jsoup.helper.StringUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by yue on 27/04/2016.
 */
public class ShowDetailFragment extends BaseFragment {

    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_HISTORY = 1;
    public final static int TYPE_FAVORITE = 2;
    private final int LIMIT_TIME = 15000;
    private TextView mTextContent;
    private TextView mTextTitle;
    private LayoutNotifyState mNotifyStateLayout;
    private News mNews;
    private int mType;
    private boolean mShowable;
    private WorkerThread.OnWorkListener mOnWorkListener = new WorkerThread.OnWorkListener() {
        @Override
        public void onWorkDone(final ArrayList<Object> objects) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mShowable) {
                        if (objects != null) {
                            final String html = (String) objects.get(0);
                            final String doc = (String) objects.get(1);
                            if (doc == null) {
                                mNotifyStateLayout.show(mNotifyStateLayout.TYPE_NO_DATA_LAYOUT, mTextContent);
                            } else {
                                enableMenuItem();
                                showHtml(doc);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DatabaseHandler.getInstance(getActivity()).insertNewsInfo(mNews, doc, html);
                                    }
                                }).start();
                            }
                        } else {
                            mNotifyStateLayout.show(LayoutNotifyState.TYPE_NO_DATA_LAYOUT, mTextContent);
                        }
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
        mShowable = true;
        switch (mType) {
            case TYPE_NORMAL:
                if (DatabaseHandler.getInstance(getActivity()).isHistory(mNews.getLink()) ||
                        DatabaseHandler.getInstance(getActivity()).isFavorite(mNews.getLink())) {
                    mNotifyStateLayout.show(LayoutNotifyState.TYPE_LOADING_LAYOUT, mTextContent);
                    WorkerThread workerThread = new WorkerThread(getActivity(),
                            WorkerThread.Work.LOAD_DOC_LOCAL, mNews, WorkerThread.WorkPriority.NORMAL);
                    workerThread.setOnWorkListener(mOnWorkListener);
                    MonitorWorkerThreadUtil.getInstance().assign(workerThread);
                } else if (NetworkUtil.isNetworkAvailable(getActivity())) {
                    mNotifyStateLayout.show(LayoutNotifyState.TYPE_LOADING_LAYOUT, mTextContent);
                    WorkerThread workerThread = new WorkerThread(getActivity(),
                            WorkerThread.Work.LOAD_DOC_REMOTE, mNews, WorkerThread.WorkPriority.NORMAL);
                    workerThread.setOnWorkListener(mOnWorkListener);
                    MonitorWorkerThreadUtil.getInstance().assign(workerThread);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mNotifyStateLayout.getVisibility() == View.VISIBLE) {
                                mNotifyStateLayout.show(LayoutNotifyState.TYPE_TIME_OUT, mTextContent);
                                mShowable = false;
                            }
                        }
                    }, LIMIT_TIME);
                } else {
                    if (DatabaseHandler.getInstance(getActivity()).isHistory(mNews.getLink())) {
                        String doc = DatabaseHandler.getInstance(getActivity()).getHistoryDocByUrl(mNews.getLink(), 1);
                        showHtml(doc);
                    } else if (DatabaseHandler.getInstance(getActivity()).isFavorite(mNews.getLink())) {
                        String doc = DatabaseHandler.getInstance(getActivity()).getFavoriteDocByUrl(mNews.getLink(), 1);
                        showHtml(doc);
                    } else {
                        mNotifyStateLayout.show(mNotifyStateLayout.TYPE_NETWORK_ERROR_LAYOUT, mTextContent);
                    }
                }
                break;
            case TYPE_HISTORY:
                String doc = DatabaseHandler.getInstance(getActivity()).getHistoryDocByUrl(mNews.getLink(), 1);
                if (!StringUtil.isBlank(doc)) showHtml(doc);
                else mNotifyStateLayout.show(LayoutNotifyState.TYPE_NO_DATA_LAYOUT, mTextContent);
                break;
            case TYPE_FAVORITE:
                doc = DatabaseHandler.getInstance(getActivity()).getFavoriteDocByUrl(mNews.getLink(), 1);
                if (!StringUtil.isBlank(doc)) showHtml(doc);
                else mNotifyStateLayout.show(LayoutNotifyState.TYPE_NO_DATA_LAYOUT, mTextContent);
                break;
        }
    }

    @Override
    protected boolean enableToolbar() {
        switch (mType) {
            case TYPE_NORMAL:
                return false;
            default:
                return true;
        }
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
        ArrayList<News> newsList = new ArrayList<>();
        newsList.add(mNews);
        WorkerThread.Work work;
        if (item.isChecked()) {
            work = WorkerThread.Work.CACHE;
            WorkerThread.sIsCaching = true;
        } else {
            work = WorkerThread.Work.REMOVE;
            WorkerThread.sIsRemoving = true;
        }
        WorkerThread worker = new WorkerThread(getActivity(), work, mNews, WorkerThread.WorkPriority.NORMAL);
        MonitorWorkerThreadUtil.getInstance().assign(worker);
    }

    private void showHtml(String doc) {
        mNotifyStateLayout.hide(mTextContent);
        mTextContent.setText(Html.fromHtml(doc, new ImageGetter(getActivity()), null));
        mTextTitle.setText(mNews.getTitle());
    }

    private void getData() {
        Bundle bundle = getArguments();
        mNews = (News) bundle.getSerializable(Constants.BUNDLE_NEWS);
        mType = bundle.getInt(Constants.BUNDLE_TYPE, TYPE_NORMAL);
    }

    private void findViews(View rootView) {
        mNotifyStateLayout = (LayoutNotifyState) rootView.findViewById(R.id.notify_state_layout);
        mTextContent = (TextView) rootView.findViewById(R.id.text_content);
        mTextTitle = (TextView) rootView.findViewById(R.id.text_title);
    }

    private static class ImageGetter implements Html.ImageGetter {

        private Context mContext;

        public ImageGetter(Context context) {
            mContext = context;
        }

        @Override
        public Drawable getDrawable(String source) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point screenSize = new Point();
            display.getSize(screenSize);
            int imageWidth = screenSize.x;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = calculateInSampleSize(source, imageWidth);
            Bitmap bitmap;
            Drawable drawable;
            File file = new File(source);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(source, options);
                drawable = new BitmapDrawable(mContext.getResources(), bitmap);
                if (drawable == null || drawable.getIntrinsicWidth() == 0) {
                    drawable = ContextCompat.getDrawable(mContext, R.drawable.image_not_available);
                }
            } else {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.image_not_available);
            }
            float ratio = imageWidth / drawable.getIntrinsicWidth();
            drawable.setBounds(0, 0, imageWidth, (int) (drawable.getIntrinsicHeight() * ratio));
            return drawable;
        }

        private int calculateInSampleSize(String url, int reqWidth) {
            int inSampleSize = 1;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            File file = new File(url);
            if (file.exists()) {
                BitmapFactory.decodeFile(url, options);
            } else {
                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.image_not_available);
            }
            int realWidth = options.outWidth;
            if (realWidth > reqWidth) {
                final int halfWidth = realWidth / 2;
                while ((halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }
    }
}
