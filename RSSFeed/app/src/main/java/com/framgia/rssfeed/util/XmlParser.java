package com.framgia.rssfeed.util;

import android.content.Context;
import android.util.Log;

import com.framgia.rssfeed.data.bean.News;
import com.framgia.rssfeed.data.local.DatabaseHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yue on 22/04/2016.
 */
public class XmlParser {

    public final static String TAG_TITLE = "title";
    public final static String TAG_DESCRIPTION = "description";
    public final static String TAG_LINK = "link";
    public final static String TAG_ITEM = "item";
    public final static String TAG_IMG = "img";
    public final static String ATTR_SRC = "src";
    public final static String HTTP_STR = "http";
    public final static String DIV_NAME_1 = "blockquote";
    public final static String DIV_NAME_2 = "div [class=\"fck_detail width_common\"]";

    public static ArrayList<Object> getNewsList(Context context, String urlString) throws XmlPullParserException, IOException {
        ArrayList<Object> data = new ArrayList<>();
        XmlPullParser parser = HttpRequest.getInstance().fetchXml(urlString);
        boolean beginParse = false;
        if (parser != null) {
            String text = "";
            News news = new News();
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName() != null) {
                            if (parser.getName().equals(TAG_ITEM)) {
                                beginParse = true;
                                news = new News();
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                    case XmlPullParser.END_TAG:
                        if (parser.getName() != null) {
                            switch (parser.getName()) {
                                case TAG_ITEM:
                                    beginParse = false;
                                    news.setFavorite(DatabaseHandler.getInstance(context).isFavorite(news.getLink()));
                                    data.add(news);
                                    break;
                                case TAG_TITLE:
                                    if (beginParse) {
                                        news.setTitle(text);
                                    }
                                    break;
                                case TAG_DESCRIPTION:
                                    if (beginParse) {
                                        news.setDescription(text);
                                    }
                                    break;
                                case TAG_LINK:
                                    news.setLink(text);
                                    break;
                            }
                        }
                        break;
                }
                event = parser.next();
            }
            HttpRequest.getInstance().closeStream();
        }
        return data;
    }

    public static ArrayList<Object> getDocumentDescription(String url) throws IOException {
        ArrayList<Object> docs = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements description = doc.select(DIV_NAME_1);
        if (description.size() == 0) description = doc.select(DIV_NAME_2);
        if (description.size() == 0) return null;
        Elements imgTag = description.eq(0).select(TAG_IMG);
        if (imgTag.size() > 0) {
            int numberOfImages = imgTag.size();
            for (int i = 0; i < numberOfImages; i++) {
                String imageUrl = imgTag.eq(i).attr(ATTR_SRC);
                if (imageUrl.contains(HTTP_STR)) {
                    imgTag.eq(i).attr(ATTR_SRC, UrlCacheUtil.getInstance().cacheImageIfNeed(imageUrl));
                } else {
                    imgTag.remove(i);
                    numberOfImages = imgTag.size();
                }
            }
        }
        docs.add(description.eq(0).html());
        return docs;
    }
}
