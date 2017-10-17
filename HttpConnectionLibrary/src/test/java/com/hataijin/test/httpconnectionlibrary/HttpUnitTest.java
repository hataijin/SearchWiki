package com.hataijin.test.httpconnectionlibrary;

import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class HttpUnitTest {
    @Test
    public void get() throws Exception {
        TestHttpConnection testHttpConnection = new TestHttpConnection();
        String result = testHttpConnection.get("http://httpbin.org", null);
        int response = testHttpConnection.getReponseCode();

        assertNotNull(result);
        assertTrue(response == HttpURLConnection.HTTP_OK);
    }

    @Test
    public void post() throws Exception {
        TestHttpConnection testHttpConnection = new TestHttpConnection();
        String result = testHttpConnection.post("http://httpbin.org/post", "", null);
        int response = testHttpConnection.getReponseCode();

        assertNotNull(result);
        assertTrue(response == HttpURLConnection.HTTP_OK || response == HttpURLConnection.HTTP_CREATED);
    }

    @Test
    public void delete() throws Exception {
        TestHttpConnection testHttpConnection = new TestHttpConnection();
        String result = testHttpConnection.delete("http://httpbin.org/delete", null);
        int response = testHttpConnection.getReponseCode();
        assertTrue(response == HttpURLConnection.HTTP_OK || response == HttpURLConnection.HTTP_NO_CONTENT
                || response == HttpURLConnection.HTTP_ACCEPTED );

        assertNotNull(result);
    }

    @Test
    public void put() throws Exception {
        TestHttpConnection testHttpConnection = new TestHttpConnection();
        String result = testHttpConnection.put("http://httpbin.org/put", "", null);
        int response = testHttpConnection.getReponseCode();

        assertTrue(response == HttpURLConnection.HTTP_OK || response == HttpURLConnection.HTTP_NO_CONTENT
                || response == HttpURLConnection.HTTP_ACCEPTED );
        assertNotNull(result);
    }

    @Test
    public void putVariable() throws Exception {
        TestHttpConnection testHttpConnection = new TestHttpConnection();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Test").append("VALUE");

        Map<String, String> header = new HashMap<String, String>() ;
        header.put("TEST", "TEST");

        String result = testHttpConnection.put("http://httpbin.org/put", stringBuilder, header);
        int response = testHttpConnection.getReponseCode();

        assertTrue(response == HttpURLConnection.HTTP_OK || response == HttpURLConnection.HTTP_NO_CONTENT
                || response == HttpURLConnection.HTTP_ACCEPTED );
        assertNotNull(result);
    }
}