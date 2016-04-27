package com.framgia.rssfeed;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yue on 25/04/2016.
 */
public class ListViewItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public ListViewItemDecoration(Context context) {
        mSpace = context.getResources().getDimensionPixelSize(R.dimen.recycler_view_item_default_space);
    }

    public ListViewItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = mSpace;
        } else {
            outRect.top = 0;
        }
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
    }

}
