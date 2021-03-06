package com.hataijin.test.httpconnection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hataijin.test.httpconnectionlibrary.TestHttpConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GoogleWebViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private final static String URL_SEARCH_SUMMARY = "https://en.wikipedia.org/api/rest_v1/page/summary/";
    private final static String URL_SEARCH_RELATED = "https://en.wikipedia.org/api/rest_v1/page/related/";

    private Context mContext;
    private TestHttpConnection mTestHttpConnection;
    private EditText mEditText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private ListViewAdapter mListViewAdapter;

    private String mSearchResultSummary = null;
    private String mSearchResultRelated = null;

    private float mDownPosX;
    private float mDownPosY;
    private float MOVE_THRESHOLD_DP;

    private int mRequestNum = 0;

    private class ListHeader {
        String displaytitle;
        String extract_html;
        String thumbnail_source;
    }
    private ListHeader mListHeader;

    private class ListItem {
        String title;
        String extract;
        String thumbnail_source;
    }
    private List<ListItem> mListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_web_view);
        mContext = this;

        mEditText = (EditText) findViewById(R.id.SearchEditText);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    searchWiki();
                }
                return false;
            }
        });

        findViewById(R.id.SearchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWiki();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.LayoutSwipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView = (ListView) findViewById(R.id.SearchListView);
        mListViewAdapter = new ListViewAdapter();
        mListView.setAdapter(mListViewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    startDetailActivity();
                } else {
                    String searchText = (String) view.getTag();

                    mEditText.setText(searchText);
                    mEditText.setSelection(searchText.length());
                    searchWiki();
                }
            }
        });

        MOVE_THRESHOLD_DP = 20.0F * getResources().getDisplayMetrics().density;

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
        if(mRequestNum > 0) {
            return;
        }
        mSwipeRefreshLayout.setRefreshing(true);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String searchText = mEditText.getText().toString();

        if(TextUtils.isEmpty(searchText)) {
            Toast.makeText(mContext, "검색어가 비어 있습니다", Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        try {
            new AsyncGet(AsyncGet.MODE_SUMMARY, URL_SEARCH_SUMMARY + URLEncoder.encode(searchText, "utf-8")).execute();
            new AsyncGet(AsyncGet.MODE_RELATED, URL_SEARCH_RELATED + URLEncoder.encode(searchText, "utf-8")).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private class AsyncGet extends AsyncTask {
        static final int MODE_SUMMARY = 0;
        static final int MODE_RELATED = 1;

        private String mUrl;
        private int mMode;

        AsyncGet(int mode, String url) {
            mUrl = url;
            mMode = mode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mRequestNum++;
        }

        @Override
        protected String doInBackground(Object[] objects) {
            return mTestHttpConnection.get(mUrl, null);
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

                if(mMode == MODE_SUMMARY || mMode == MODE_RELATED) {
                    refreshResult();
                }
            }
        }
    }

    private void refreshResult() {
        parseListHeader();
        parseListItems();
        mListView.smoothScrollToPosition(0);
        mListViewAdapter.notifyDataSetChanged();
    }

    private void parseListHeader() {
        try {
            JSONObject jObject = new JSONObject(mSearchResultSummary);
            mListHeader = new ListHeader();
            mListHeader.displaytitle = jObject.getString("displaytitle");
            mListHeader.extract_html = jObject.getString("extract_html");

            if(jObject.has("thumbnail")) {
                JSONObject jObjectThumb = jObject.getJSONObject("thumbnail");
                mListHeader.thumbnail_source = jObjectThumb.getString("source");
            } else {
                mListHeader.thumbnail_source = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            mListHeader = null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            mListHeader = null;
        }
    }

    private void parseListItems() {
        try {
            JSONObject jObject = new JSONObject(mSearchResultRelated);
            JSONArray jArray = jObject.getJSONArray("pages");

            mListItems.clear();

            for(int i = 0; i < jArray.length(); i++) {
                JSONObject jSubObject = jArray.getJSONObject(i);

                ListItem listItem = new ListItem();
                listItem.title = jSubObject.getString("title");
                listItem.extract = jSubObject.getString("extract");

                if(jSubObject.has("thumbnail")) {
                    JSONObject jObjectThumb = jSubObject.getJSONObject("thumbnail");
                    listItem.thumbnail_source = jObjectThumb.getString("source");
                } else {
                    listItem.thumbnail_source = null;
                }

                mListItems.add(listItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mListItems.clear();
        } catch (NullPointerException e) {
            e.printStackTrace();
            mListItems.clear();
        }
    }

    private void startDetailActivity() {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_SEARCH, mEditText.getText().toString());
        startActivity(intent);
    }

    private class ListViewAdapter extends BaseAdapter {
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(getItemViewType(i) == TYPE_HEADER) {
                //ListHeader
                if(mListHeader == null) {
                    return null;
                }

                if(view == null) {
                    view = getLayoutInflater().inflate(R.layout.listheader, mListView, false);
                }

                ImageView iv = view.findViewById(R.id.thumbnail);
                iv.setImageDrawable(null);

                if(mListHeader.thumbnail_source != null) {
                    new DownloadImageTask(iv)
                            .execute(mListHeader.thumbnail_source);
                }

                WebView webView = view.findViewById(R.id.webview);
                webView.loadDataWithBaseURL(null, mListHeader.extract_html, "text/html", "utf-8", null);
                webView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mDownPosX = motionEvent.getX();
                                mDownPosY = motionEvent.getY();
                                break;
                            case MotionEvent.ACTION_UP:
                                float upPosX = motionEvent.getX();
                                float upPosY = motionEvent.getY();
                                if ((Math.abs(upPosX - mDownPosX) < MOVE_THRESHOLD_DP) && (Math.abs(upPosY - mDownPosY) < MOVE_THRESHOLD_DP)) {
                                    startDetailActivity();
                                }

                                break;
                        }
                        return false;
                    }
                });

                return view;
            } else {
                if(view == null) {
                    view = getLayoutInflater().inflate(R.layout.listitem, mListView, false);
                }

                ListItem listItem = mListItems.get(i - 1);

                ImageView iv = view.findViewById(R.id.thumbnail);
                iv.setImageDrawable(null);

                if(!TextUtils.isEmpty(listItem.thumbnail_source)) {
                    new DownloadImageTask(iv)
                            .execute(listItem.thumbnail_source);
                }

                TextView titleTv = view.findViewById(R.id.title);
                titleTv.setText(listItem.title);

                TextView bodyTv = view.findViewById(R.id.body);
                bodyTv.setText(listItem.extract);

                view.setTag(listItem.title);

                return view;
            }
        }

        @Override
        public int getCount() {
            int num = 0;

            if(!TextUtils.isEmpty(mSearchResultSummary)
                    && mListHeader != null) {
                num++;
            }

            num += mListItems.size();
            return num;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_ITEM;
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
