package com.framgia.rssfeed.bean;

import java.io.Serializable;

/**
 * Created by yue on 21/04/2016.
 */
public class News implements Serializable {

    private final static String START_SUBSTRING = "src";
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
        mImageUrl = retrieveImageUrl(description);
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
}
