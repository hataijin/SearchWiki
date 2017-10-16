package com.hataijin.test.httpconnection;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hataijin.test.httpconnectionlibrary.TestHttpConnection;

public class GoogleWebView extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private final static String URL_SEARCH_DETAIL = "https://en.wikipedia.org/api/rest_v1/page/html/";
    private final static String URL_SEARCH_SUMMARY = "https://en.wikipedia.org/api/rest_v1/page/summary/";
    private final static String URL_SEARCH_RELATED = "https://en.wikipedia.org/api/rest_v1/page/related/";

    private Context mContext;
    private TestHttpConnection mTestHttpConnection;
    private EditText mEditText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

    private String mSearchResultSummary;
    private String mSearchResultRelated;

    private int mRequestNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_web_view);
        mContext = this;

        mEditText = (EditText) findViewById(R.id.SearchEditText);
        findViewById(R.id.SearchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWiki();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.LayoutSwipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mTestHttpConnection = new TestHttpConnection();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mRequestNum = 0;
    }

    @Override
    public void onRefresh() {
        searchWiki();
    }

    private void searchWiki() {
        String searchText = mEditText.getText().toString();

        if(TextUtils.isEmpty(searchText)) {
            Toast.makeText(mContext, "검색어가 비어 있습니다", Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        new AsyncGet(AsyncGet.MODE_SUMMARY, URL_SEARCH_SUMMARY + searchText).execute();
        new AsyncGet(AsyncGet.MODE_RELATED, URL_SEARCH_RELATED + searchText).execute();
    }

    private class AsyncGet extends AsyncTask {
        public static final int MODE_SUMMARY = 0;
        public static final int MODE_RELATED = 1;

        private String mUrl;
        private int mMode;

        public AsyncGet(int mode, String url) {
            mUrl = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mRequestNum++;
        }

        @Override
        protected String doInBackground(Object[] objects) {
            return mTestHttpConnection.get(mUrl);
        }

        @Override
        protected void onPostExecute(Object o) {
            mRequestNum--;

            switch(mMode) {
                case MODE_SUMMARY: {
                    mSearchResultSummary = (String) o;
                    break;
                }

                case MODE_RELATED: {
                    mSearchResultRelated = (String) o;
                    break;
                }
            }
            Log.d("HATAIJIN", "Result " + o);

            if(mRequestNum <= 0) {
                mSwipeRefreshLayout.setRefreshing(false);
                refreshResult();
            }
        }
    }

    private void refreshResult() {

    }
}
