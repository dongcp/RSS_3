package com.framgia.rssfeed;

import com.framgia.rssfeed.bean.News;
import com.framgia.rssfeed.utility.HttpRequest;

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
    private boolean beginParse;

    public XmlParser() {
        beginParse = false;
    }

    public ArrayList<Object> getNewsList(String urlString) throws XmlPullParserException, IOException {
        ArrayList<Object> data = new ArrayList<>();
        XmlPullParser parser = HttpRequest.getInstance().fetchXml(urlString);
        String text = "";
        News news = new News();
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    if (name.equals(TAG_ITEM)) {
                        beginParse = true;
                        news = new News();
                    }
                    break;
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    switch (name) {
                        case TAG_ITEM:
                            beginParse = false;
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
                    break;
            }
            event = parser.next();
        }
        HttpRequest.getInstance().closeStream();
        return data;
    }
}
