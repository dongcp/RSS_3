package com.framgia.rssfeed.utility;

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

    private final static String REQUEST_METHOD = "GET";
    private final static int READ_TIMEOUT = 10000;
    private final static int CONNECT_TIMEOUT = 15000;
    private HttpURLConnection mConnection;
    private InputStream mStream;
    private static HttpRequest sInstance;

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
        disconnect();
        URL url = new URL(urlString);
        mConnection = (HttpURLConnection) url.openConnection();
        mConnection.setReadTimeout(READ_TIMEOUT);
        mConnection.setConnectTimeout(CONNECT_TIMEOUT);
        mConnection.setRequestMethod(REQUEST_METHOD);
        mConnection.setDoInput(true);
        connect();
    }

    public void connect() throws IOException {
        mConnection.connect();
    }

    public void disconnect() {
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }

    public void closeStream() throws IOException {
        if (mStream != null) {
            mStream.close();
        }
    }

}
