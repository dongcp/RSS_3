package com.framgia.rssfeed.bean;

import android.graphics.Bitmap;

/**
 * Created by VULAN on 4/23/2016.
 */
public class Category {

    private Bitmap mImage;
    private String mText;

    public Category(Bitmap mImage, String mText) {
        setImage(mImage);
        setText(mText);
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap mImage) {
        this.mImage = mImage;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

}
