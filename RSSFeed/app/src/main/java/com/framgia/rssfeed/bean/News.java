package com.framgia.rssfeed.bean;

/**
 * Created by yue on 21/04/2016.
 */
public class News {

    private String mTitle;
    private String mImageUrl;
    private String mLink;
    private String mDescription;

    public News() {
    }

    public News(String title, String link, String imageUrl) {
        mTitle = title;
        mLink = link;
        mImageUrl = imageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
