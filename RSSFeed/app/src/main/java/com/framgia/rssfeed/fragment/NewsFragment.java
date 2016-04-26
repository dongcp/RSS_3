package com.framgia.rssfeed.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.framgia.rssfeed.GridViewItemDecoration;
import com.framgia.rssfeed.R;
import com.framgia.rssfeed.adapter.CategoryAdapter;
import com.framgia.rssfeed.base.BaseFragment;
import com.framgia.rssfeed.bean.Category;
import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends BaseFragment implements CategoryAdapter.OnItemListener {

    private CategoryAdapter mCategoryAdapter;
    private RecyclerView mRecyclerView;
    private List<Category> mListNews;
    public static final String TAG_NEWS_FRAGMENT = "NEW_FRAGMENT";
    public final int INDEX_BUSINESS = 0;
    public final int INDEX_EDUCATION = 1;
    public final int INDEX_ENTERTAINMENT = 2;
    public final int INDEX_TECHNOLOGY = 3;
    public final int INDEX_LAWS = 4;
    public final int INDEX_NEWS = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_new;
    }

    @Override
    protected void onCreateContentView(View rootView) {
        findView(rootView);
    }

    private void findView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_news);
        mListNews = new ArrayList<>();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        addItemListNews(mListNews);
        mCategoryAdapter = new CategoryAdapter(getActivity(), mListNews);
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.addItemDecoration(new GridViewItemDecoration(getContext()));
        mCategoryAdapter.setmOnItemListener(this);
    }

    private void addItemListNews(List<Category> list) {
        Bitmap image_business = BitmapFactory.decodeResource(this.getResources(), R.drawable.business);
        Bitmap image_education = BitmapFactory.decodeResource(this.getResources(), R.drawable.education);
        Bitmap image_entertainment = BitmapFactory.decodeResource(this.getResources(), R.drawable.entertainment);
        Bitmap image_technology = BitmapFactory.decodeResource(this.getResources(), R.drawable.technology);
        Bitmap image_law = BitmapFactory.decodeResource(this.getResources(), R.drawable.law);
        Bitmap image_new = BitmapFactory.decodeResource(this.getResources(), R.drawable.news);

        Category category_business = new Category(image_business, getString(R.string.business));
        Category category_education = new Category(image_education, getString(R.string.education));
        Category category_entertainment = new Category(image_entertainment, getString(R.string.entertainment));
        Category category_technology = new Category(image_technology, getString(R.string.technology));
        Category category_law = new Category(image_law, getString(R.string.law));
        Category category_new = new Category(image_new, getString(R.string.news));
        list.add(category_business);
        list.add(category_education);
        list.add(category_entertainment);
        list.add(category_technology);
        list.add(category_law);
        list.add(category_new);

    }

    @Override
    public void onClickItem(int position) {
        switch (position) {
            case INDEX_TECHNOLOGY:
                getBaseActivity().replaceFragment(new ListNewsFragment(), TAG_NEWS_FRAGMENT);
                break;
        }
    }

}
