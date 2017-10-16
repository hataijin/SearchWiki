package com.hataijin.test.httpconnectionlibrary;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by User on 2017-10-16.
 */

class HttpsConnection extends Connection {
    @Override
    public String get(String urlStr) {
        HttpsURLConnection httpsUrlConnection = null;

        try {
            URL url = new URL(urlStr);
            httpsUrlConnection = (HttpsURLConnection) url.openConnection();
            httpsUrlConnection.connect();

            InputStream inputStream = new BufferedInputStream(httpsUrlConnection.getInputStream());

            return getStringFromInputStream(inputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(httpsUrlConnection != null) {
                httpsUrlConnection.disconnect();
            }
        }

        return null;
    }
}
