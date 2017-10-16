package com.hataijin.test.httpconnection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hataijin.test.httpconnectionlibrary.TestHttpConnection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by User on 2017-10-16.
 */

public class DetailActivity extends AppCompatActivity {
    private final static String URL_SEARCH_DETAIL = "https://en.wikipedia.org/api/rest_v1/page/html/";
    public static final String EXTRA_SEARCH = "searchtext";

    Context mContext;
    String mSearchText;
    WebView mWebView;

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mContext = this;

        Intent intent = getIntent();
        if(intent != null) {
            mSearchText = intent.getStringExtra(EXTRA_SEARCH);
        }

        setTitle(mSearchText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

        });

        try {
            mWebView.loadUrl(URL_SEARCH_DETAIL + URLEncoder.encode(mSearchText, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
