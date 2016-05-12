package com.framgia.rssfeed.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.data.bean.Category;
import com.framgia.rssfeed.ui.adapter.CategoryAdapter;
import com.framgia.rssfeed.ui.base.BaseFragment;
import com.framgia.rssfeed.ui.base.Constants;
import com.framgia.rssfeed.ui.decoration.GridViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements CategoryAdapter.OnItemClickListener {

    public static final String TAG_NEWS_FRAGMENT = "NEW_FRAGMENT";
    private CategoryAdapter mCategoryAdapter;
    private RecyclerView mRecyclerView;
    private List<Category> mListNews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        findView(view);
        return view;
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
        GridViewItemDecoration gridViewItemDecoration = new GridViewItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.common_size_5));
        mRecyclerView.addItemDecoration(gridViewItemDecoration);
        mCategoryAdapter.setOnItemListener(this);
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
        ListNewsFragment fragment = new ListNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_INDEX, position);
        fragment.setArguments(bundle);
        replaceFragment(fragment, TAG_NEWS_FRAGMENT);
    }

    private void replaceFragment(BaseFragment fragment, String tag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit)
                .replace(R.id.fragmentContainer, fragment, tag)
                .addToBackStack("")
                .commit();
    }
}
