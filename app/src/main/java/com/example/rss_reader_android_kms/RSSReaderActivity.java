package com.example.rss_reader_android_kms;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSReaderActivity extends AppCompatActivity {

    private static final String TAG = "RSSReaderActivity";

    public RecyclerView mRecyclerView;
    public EditText edtInputUrl;
    public Button btnFetchContent;
    public SwipeRefreshLayout swipeRefreshLayout;
    public TextView tvTitle;
    public TextView tvDescription;
    public TextView tvLink;

    private List<ModelRecyclerView> listModelRecyclerView = new ArrayList<>();
    private String mTitle;
    private String mLink;
    private String mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssreader);

        mRecyclerView = findViewById(R.id.recyclerView);
        edtInputUrl = findViewById(R.id.edtInputUrl);
        btnFetchContent = findViewById(R.id.btnFetchContent);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvLink = findViewById(R.id.tvLink);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnFetchContent.setOnClickListener(view -> new FetchFeedTask().execute((Void) null));
        swipeRefreshLayout.setOnRefreshListener(() -> new FetchFeedTask().execute((Void) null));
    }

    public List<ModelRecyclerView> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        List<ModelRecyclerView> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MainActivity", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    int temp = 0;
                    for (int i = 0; i < result.length(); i++) {
                        if (result.charAt(i) == '>') {
                            temp = i;
                        }
                    }
                    description = result.substring(temp + 1);
                }

                if (title != null && link != null && description != null) {
                    if (isItem) {
                        ModelRecyclerView item = new ModelRecyclerView(title, link, description);
                        items.add(item);
                    } else {
                        mTitle = title;
                        mLink = link;
                        mDescription = description;
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
            mTitle = null;
            mLink = null;
            mDescription = null;
            tvTitle.setText("Tên trang tin tức: " + mTitle);
            tvDescription.setText("Mô tả: " + mDescription);
            tvLink.setText("Link: " + mLink);
            urlLink = edtInputUrl.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                listModelRecyclerView = parseFeed(inputStream);
                return true;
            } catch (IOException | XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean success) {
            swipeRefreshLayout.setRefreshing(false);

            if (success) {
                tvTitle.setText("Tên trang tin tức: " + mTitle);
                tvDescription.setText("Mô tả: " + mDescription);
                tvLink.setText("Link: " + mLink);
                // Fill RecyclerView
                mRecyclerView.setAdapter(new RecyclerViewAdapter(listModelRecyclerView));
            } else {
                Toast.makeText(RSSReaderActivity.this,
                        "Enter a valid url",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}