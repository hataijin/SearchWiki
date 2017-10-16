package com.hataijin.test.httpconnectionlibrary;

/**
 * Created by User on 2017-10-16.
 */

public class TestHttpConnection {
    private HttpsConnection httpsConnection = new HttpsConnection();
    private HttpConnection httpConnection = new HttpConnection();

    public TestHttpConnection() {
    }

    public String get(String url) {
        Connection connection;
        if(url.toLowerCase().startsWith("https")) {
            connection = httpsConnection;
        } else {
            connection = httpConnection;
        }

        return connection.get(url);
    }
}
