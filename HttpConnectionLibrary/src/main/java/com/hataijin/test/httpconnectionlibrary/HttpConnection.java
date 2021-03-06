package com.hataijin.test.httpconnectionlibrary;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by User on 2017-10-16.
 */

class HttpConnection extends Connection {
    @Override
    public String get(String urlStr, Map<String, String> requestHeader) {
        HttpURLConnection httpUrlConnection = null;

        try {
            URL url = new URL(urlStr);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(mConnectionTimeout);
            putHeader(httpUrlConnection, requestHeader);

            httpUrlConnection.connect();

            mResponseCode = httpUrlConnection.getResponseCode();
            if (mResponseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = new BufferedInputStream(httpUrlConnection.getInputStream());

                return getStringFromInputStream(inputStream);
            } else {
                return "";
            }
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

    @Override
    public <T> String post(String urlStr, T postData, Map<String, String> requestHeader) {
        HttpURLConnection httpUrlConnection = null;

        try {
            URL url = new URL(urlStr);
            httpUrlConnection = (HttpURLConnection) url.openConnection();

            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded" );
            httpUrlConnection.setConnectTimeout(mConnectionTimeout);
            putHeader(httpUrlConnection, requestHeader);

            OutputStream os = httpUrlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(postData.toString());

            writer.flush();
            writer.close();
            os.close();

            httpUrlConnection.connect();

            mResponseCode = httpUrlConnection.getResponseCode();
            if (mResponseCode == HttpURLConnection.HTTP_OK || mResponseCode == HttpURLConnection.HTTP_CREATED) {
                InputStream inputStream = new BufferedInputStream(httpUrlConnection.getInputStream());

                return getStringFromInputStream(inputStream);
            } else {
                return "";
            }
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

    @Override
    public String delete(String urlStr, Map<String, String> requestHeader) {
        HttpURLConnection httpUrlConnection = null;

        try {
            URL url = new URL(urlStr);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("DELETE");
            putHeader(httpUrlConnection, requestHeader);

            httpUrlConnection.connect();
            httpUrlConnection.setConnectTimeout(mConnectionTimeout);

            mResponseCode = httpUrlConnection.getResponseCode();
            if (mResponseCode == HttpURLConnection.HTTP_OK || mResponseCode == HttpURLConnection.HTTP_NO_CONTENT
                    || mResponseCode == HttpURLConnection.HTTP_ACCEPTED ) {
                InputStream inputStream = new BufferedInputStream(httpUrlConnection.getInputStream());

                return getStringFromInputStream(inputStream);
            } else {
                return "";
            }
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

    @Override
    public <T> String put(String urlStr, T postData, Map<String, String> requestHeader) {
        HttpURLConnection httpUrlConnection = null;

        try {
            URL url = new URL(urlStr);
            httpUrlConnection = (HttpURLConnection) url.openConnection();

            httpUrlConnection.setRequestMethod("PUT");
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded" );
            httpUrlConnection.setConnectTimeout(mConnectionTimeout);
            putHeader(httpUrlConnection, requestHeader);

            OutputStream os = httpUrlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(postData.toString());

            writer.flush();
            writer.close();
            os.close();

            httpUrlConnection.connect();

            mResponseCode = httpUrlConnection.getResponseCode();
            if (mResponseCode == HttpURLConnection.HTTP_OK || mResponseCode == HttpURLConnection.HTTP_NO_CONTENT
                    || mResponseCode == HttpURLConnection.HTTP_ACCEPTED ) {
                InputStream inputStream = new BufferedInputStream(httpUrlConnection.getInputStream());

                return getStringFromInputStream(inputStream);
            } else {
                return "";
            }
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

    protected void putHeader(HttpURLConnection httpURLConnection, Map<String, String> requestHeader) {
        if(requestHeader == null)
            return;

        for(Map.Entry<String,String> value : requestHeader.entrySet()) {
            httpURLConnection.setRequestProperty(value.getKey(), value.getValue());
        }
    }
}
