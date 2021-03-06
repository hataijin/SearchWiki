package com.hataijin.test.httpconnectionlibrary;

import java.util.Map;

/**
 * Created by User on 2017-10-16.
 */

public class TestHttpConnection {
    private int responseCode = 0;
    private HttpsConnection httpsConnection = new HttpsConnection();
    private HttpConnection httpConnection = new HttpConnection();

    public TestHttpConnection() {
    }

    public int getReponseCode() {
        return responseCode;
    }

    public String get(String url, Map<String, String> requestHeader) {
        Connection connection;
        if(url.toLowerCase().startsWith("https")) {
            connection = httpsConnection;
        } else {
            connection = httpConnection;
        }

        String result =  connection.get(url, requestHeader);
        responseCode = connection.getResponseCode();
        return result;
    }

    public <T> String put(String url, T postdata, Map<String, String> requestHeader) {
        Connection connection;
        if(url.toLowerCase().startsWith("https")) {
            connection = httpsConnection;
        } else {
            connection = httpConnection;
        }

        String result =  connection.put(url, postdata, requestHeader);
        responseCode = connection.getResponseCode();
        return result;
    }

    public <T> String post(String url, T postdata, Map<String, String> requestHeader) {
        Connection connection;
        if(url.toLowerCase().startsWith("https")) {
            connection = httpsConnection;
        } else {
            connection = httpConnection;
        }

        String result =  connection.post(url, postdata, requestHeader);
        responseCode = connection.getResponseCode();
        return result;
    }

    public String delete(String url, Map<String, String> requestHeader) {
        Connection connection;
        if(url.toLowerCase().startsWith("https")) {
            connection = httpsConnection;
        } else {
            connection = httpConnection;
        }

        String result =  connection.delete(url, requestHeader);
        responseCode = connection.getResponseCode();
        return result;
    }
}
