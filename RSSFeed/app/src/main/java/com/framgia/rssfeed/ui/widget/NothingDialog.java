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
public class NothingDialog extends Dialog implements View.OnClickListener {
    private TextView mTextViewOK;

    public NothingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_nothing);
        findView();
    }

    public void findView() {
        mTextViewOK = (TextView) this.findViewById(R.id.button_ok);
        mTextViewOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
