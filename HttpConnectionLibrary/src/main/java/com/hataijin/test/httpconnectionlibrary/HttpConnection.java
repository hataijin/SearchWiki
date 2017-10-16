package com.hataijin.test.httpconnectionlibrary;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by User on 2017-10-16.
 */

class HttpConnection extends Connection {
    public String get(String urlStr) {
        HttpURLConnection httpUrlConnection = null;

        try {
            URL url = new URL(urlStr);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.connect();

            InputStream inputStream = new BufferedInputStream(httpUrlConnection.getInputStream());

            return getStringFromInputStream(inputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }

        return null;
    }
}
