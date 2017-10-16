package com.hataijin.test.httpconnectionlibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by User on 2017-10-16.
 */

class Connection {
    public String get(String urlStr) {return null;}

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
