package com.framgia.rssfeed.data.bean;

import java.io.Serializable;

/**
 * Created by yue on 21/04/2016.
 */
public class News implements Serializable {

    private final static String START_SUBSTRING = "src";
    private final static String TAG_IMAGE = "img";
    private String mTitle;
    private String mImageUrl;
    private String mLink;
    private String mDescription;
    private int mCategory;
    private boolean mIsFavorite;

    public News() {
        mTitle = "";
        mImageUrl = "";
        mDescription = "";
        mLink = "";
    }

    public News(String link) {
        mTitle = "";
        mImageUrl = "";
        mDescription = "";
        mLink = link;
    }

    public News(String title, String link, String imageUrl, boolean isFavorite) {
        mTitle = title;
        mLink = link;
        mImageUrl = imageUrl;
        mIsFavorite = isFavorite;
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
        mDescription = retrieveDescription(description);
        mImageUrl = retrieveImageUrl(description);
    }

    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int category) {
        this.mCategory = category;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        mIsFavorite = isFavorite;
    }

    private String retrieveImageUrl(String description) {
        String tmp = "";
        if (!description.contains(START_SUBSTRING))
            return tmp;
        int startIndex = description.indexOf(START_SUBSTRING);
        int count = 0;
        for (int i = startIndex; i < description.length(); i++) {
            if (description.charAt(i) == '\"') {
                count++;
                continue;
            }
            if (count == 1) tmp += description.charAt(i);
            if (count == 2) break;
        }
        return tmp;
    }

    private String retrieveDescription(String description) {
        if (!description.contains(TAG_IMAGE))
            return description;
        int startIndex = description.indexOf(TAG_IMAGE) - 1;
        int endIndex = 0;
        for (int i = startIndex; i < description.length(); i++) {
            if (description.charAt(i) == '>') {
                endIndex = i;
                break;
            }
        }
        return (description.substring(0, startIndex) + description.substring(endIndex + 1, description.length()));
    }
}
