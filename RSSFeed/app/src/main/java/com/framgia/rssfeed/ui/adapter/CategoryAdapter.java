package com.framgia.rssfeed.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.data.bean.Category;

import java.util.List;

/**
 * Created by VULAN on 4/23/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> mListNews;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public CategoryAdapter(Context context, List<Category> listNews) {
        mInflater = LayoutInflater.from(context);
        this.mListNews = listNews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_category, parent, false);
        if (view == null) {
            view = mInflater.inflate(R.layout.item_category, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Category category = mListNews.get(position);
        holder.mTitle.setText(category.getText());
        holder.mImageCategory.setImageBitmap(category.getImage());
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClickItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListNews == null ? 0 : mListNews.size();
    }

    public void setOnItemListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClickItem(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLinearLayout;
        private ImageView mImageCategory;
        private TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageCategory = (ImageView) itemView.findViewById(R.id.image_news);
            mTitle = (TextView) itemView.findViewById(R.id.text_news);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.layout_item_category);
        }
    }
}
