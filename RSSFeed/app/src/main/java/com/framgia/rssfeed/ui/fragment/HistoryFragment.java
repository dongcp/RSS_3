package com.framgia.rssfeed.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.data.bean.News;
import com.framgia.rssfeed.data.local.DatabaseHandler;
import com.framgia.rssfeed.ui.adapter.ListNewsAdapter;
import com.framgia.rssfeed.ui.base.BaseFragment;
import com.framgia.rssfeed.ui.base.Constants;
import com.framgia.rssfeed.ui.decoration.ListViewItemDecoration;
import com.framgia.rssfeed.util.OnRecyclerViewItemClickListener;

public class HistoryFragment extends Fragment implements OnRecyclerViewItemClickListener {

    private RecyclerView mRecyclerView;
    private ListNewsAdapter mListHistoryAdapter;

    @Override
    public void onItemClickListener(View view, int position) {
        News news = mListHistoryAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BUNDLE_NEWS, news);
        bundle.putInt(Constants.BUNDLE_TYPE, ShowDetailFragment.TYPE_HISTORY);
        ShowDetailFragment fragment = new ShowDetailFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment, ListNewsFragment.TAG_LIST_NEWS_FRAGMENT);
    }

    public void findView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_news);
    }

    public void setupRecyclerView(Context context) {
        ListViewItemDecoration mListViewItemDecoration = new ListViewItemDecoration(context);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(mListViewItemDecoration);
        mListHistoryAdapter = new ListNewsAdapter(context, mRecyclerView.getLayoutManager());
        mListHistoryAdapter.addItems(DatabaseHandler.getInstance(context).getHistoryNews());
        mListHistoryAdapter.setOnRecyclerViewItemClickListener(this);
        mRecyclerView.setAdapter(mListHistoryAdapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        findView(view);
        setupRecyclerView(getActivity());
        return view;
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
