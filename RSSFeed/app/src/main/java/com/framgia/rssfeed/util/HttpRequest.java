package com.framgia.rssfeed.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yue on 23/04/2016.
 */
public class HttpRequest {

    public final static String REQUEST_METHOD = "GET";
    public final static int READ_TIMEOUT = 10000;
    public final static int CONNECT_TIMEOUT = 30000;
    private static HttpRequest sInstance;
    private HttpURLConnection mConnection;
    private InputStream mStream;

    private HttpRequest() {
    }

    public static HttpRequest getInstance() {
        if (sInstance == null) {
            synchronized (HttpRequest.class) {
                if (sInstance == null) {
                    sInstance = new HttpRequest();
                }
            }
        }
        return sInstance;
    }

    public HttpURLConnection getConnection() {
        return mConnection;
    }

    public XmlPullParser fetchXml(String urlString) throws XmlPullParserException, IOException {
        makeConnection(urlString);
        mStream = mConnection.getInputStream();
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(mStream, null);
        return parser;
    }

    public void makeConnection(String urlString) throws IOException {
        disconnect(mConnection);
        URL url = new URL(urlString);
        mConnection = (HttpURLConnection) url.openConnection();
        mConnection.setReadTimeout(READ_TIMEOUT);
        mConnection.setConnectTimeout(CONNECT_TIMEOUT);
        mConnection.setRequestMethod(REQUEST_METHOD);
        mConnection.setDoInput(true);
        mConnection.setDoOutput(true);
        mConnection.connect();
    }

    public void disconnect(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
    }

    public void closeStream() throws IOException {
        if (mStream != null) {
            mStream.close();
        }
    }
}
