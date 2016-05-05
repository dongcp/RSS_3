package com.framgia.rssfeed.ui.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.framgia.rssfeed.R;

/**
 * Created by yue on 25/04/2016.
 */
public class GridViewItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public GridViewItemDecoration(Context context) {
        mSpace = context.getResources().getDimensionPixelSize(R.dimen.recycler_view_item_default_space);
    }

    public GridViewItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (position == 0 || position == 1) {
            outRect.top = mSpace;
        } else {
            outRect.top = 0;
        }
        if (position % 2 == 0) {
            outRect.left = mSpace;
            outRect.right = mSpace / 2;
        } else {
            outRect.left = mSpace / 2;
            outRect.right = mSpace;
        }
        outRect.bottom = mSpace;
    }
}
