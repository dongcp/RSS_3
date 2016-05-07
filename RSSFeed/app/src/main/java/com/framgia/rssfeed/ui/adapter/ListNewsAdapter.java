package com.framgia.rssfeed.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.rssfeed.R;
import com.framgia.rssfeed.data.bean.News;
import com.framgia.rssfeed.util.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

/**
 * Created by VULAN on 4/27/2016.
 */
public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsAdapter.ItemHolder> {

    private Context mContext;
    private ArrayList<News> mNewsList;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public ListNewsAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        mNewsList = new ArrayList<>();
        mLayoutManager = layoutManager;
        mContext = context;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (mLayoutManager instanceof GridLayoutManager) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_news, parent, false);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_list_news, parent, false);
        }
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        News news = mNewsList.get(position);
        if (TextUtils.isEmpty(news.getImageUrl())) {
            holder.imageNews.setVisibility(View.GONE);
        } else {
            holder.imageNews.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(news.getImageUrl()).centerCrop().into(holder.imageNews);
        }
        holder.newsTitle.setText(news.getTitle());
        holder.description.setText(Html.fromHtml(news.getDescription()));
        holder.linearItemNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClickListener(holder.linearItemNews, position);
                }
            }
        });
        if (news.isFavorite()) {
            holder.imageFavorite.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_highlight_star));
        } else {
            holder.imageFavorite.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_normal_star));
        }
        holder.imageFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClickListener(holder.imageFavorite, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void addItem(News news) {
        mNewsList.add(news);
        notifyItemInserted(mNewsList.size() - 1);
    }

    public void addItems(ArrayList<News> newsList) {
        mNewsList.addAll(newsList);
        notifyDataSetChanged();
    }

    public News getItem(int position) {
        return mNewsList.get(position);
    }

    public ArrayList<News> getNewsList() {
        return mNewsList;
    }

    public void removeAllItemsIfExist() {
        if (mNewsList != null) {
            mNewsList.clear();
            notifyDataSetChanged();
        }
    }

    public void changeLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        notifyDataSetChanged();
    }

    public boolean isAllFavorite() {
        int arraySize = mNewsList.size();
        for (int i = 0; i < arraySize; i++) {
            if (!mNewsList.get(i).isFavorite()) return false;
        }
        return true;
    }

    public void removeItem(int position) {
        mNewsList.remove(position);
        this.notifyDataSetChanged();
    }
    
    class ItemHolder extends RecyclerView.ViewHolder {

        public TextView newsTitle;
        public ImageView imageNews;
        public TextView description;
        public LinearLayout linearItemNews;
        public ImageView imageFavorite;

        public ItemHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            imageNews = (ImageView) itemView.findViewById(R.id.image_news);
            description = (TextView) itemView.findViewById(R.id.description);
            linearItemNews = (LinearLayout) itemView.findViewById(R.id.linear_item_news);
            imageFavorite = (ImageView) itemView.findViewById(R.id.image_favorite);
            newsTitle.setSelected(true);
        }
    }
}
