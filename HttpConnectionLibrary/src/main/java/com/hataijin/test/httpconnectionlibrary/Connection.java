package com.hataijin.test.httpconnectionlibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by User on 2017-10-16.
 */

class Connection {
    protected int mConnectionTimeout = 15000;
    protected int mResponseCode = 0;

    public String get(String urlStr, Map<String, String> requestHeader) {return null;}
    public <T> String post(String urlStr, T postData, Map<String, String> requestHeader) {return null;}
    public String delete(String urlStr, Map<String, String> requestHeader) {return null;}
    public <T> String put(String urlStr, T postData, Map<String, String> requestHeader) {return null;}

    public void setConnectionTimeout(int timeout) {
        mConnectionTimeout = timeout;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    protected String getStringFromInputStream(InputStream inputStream) {
        if(inputStream == null) {
            return null;
        }

        StringBuffer stringBuffer = new StringBuffer();

        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (IOException e) {

        }

        return stringBuffer.toString();
    }
}
