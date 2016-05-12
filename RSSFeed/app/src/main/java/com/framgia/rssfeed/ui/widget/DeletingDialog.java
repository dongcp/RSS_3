package com.framgia.rssfeed.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.framgia.rssfeed.R;

/**
 * Created by VULAN on 5/9/2016.
 */
public class DeletingDialog extends Dialog implements View.OnClickListener {
    public static final int OK = 1;
    public static final int CANCEL = 0;
    private TextView mTextViewOK;
    private TextView mTextViewCanCel;
    private onDeletingDialogItemClickListener mOnDeletingDialogItemClickListener;

    public DeletingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_deleting);
        findView();
    }

    public void findView() {
        mTextViewOK = (TextView) this.findViewById(R.id.button_ok);
        mTextViewCanCel = (TextView) this.findViewById(R.id.button_cancel);
        mTextViewOK.setOnClickListener(this);
        mTextViewCanCel.setOnClickListener(this);
    }

    public void setOnDialogItemClickListener(onDeletingDialogItemClickListener mOnDeletingDialogItemClickListener) {
        this.mOnDeletingDialogItemClickListener = mOnDeletingDialogItemClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                mOnDeletingDialogItemClickListener.deleteItem(OK);
                break;
            case R.id.button_cancel:
                mOnDeletingDialogItemClickListener.deleteItem(CANCEL);
                break;
        }
    }

    public interface onDeletingDialogItemClickListener {
        void deleteItem(int code);
    }
}
