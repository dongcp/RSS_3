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
    private boolean mBeginParse;

    public XmlParser() {
        mBeginParse = false;
    }

    public ArrayList<Object> getNewsList(String urlString) throws XmlPullParserException, IOException {
        ArrayList<Object> data = new ArrayList<>();
        XmlPullParser parser = HttpRequest.getInstance().fetchXml(urlString);
        if (parser != null) {
            String text = "";
            News news = new News();
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName() != null) {
                            if (parser.getName().equals(TAG_ITEM)) {
                                mBeginParse = true;
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
                                    mBeginParse = false;
                                    data.add(news);
                                    break;
                                case TAG_TITLE:
                                    if (mBeginParse) {
                                        news.setTitle(text);
                                    }
                                    break;
                                case TAG_DESCRIPTION:
                                    if (mBeginParse) {
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
}
