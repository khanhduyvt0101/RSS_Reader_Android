package com.example.rss_reader_android_kms.modules.rssreader;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rss_reader_android_kms.items.ItemRecyclerView;
import com.example.rss_reader_android_kms.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSReaderFragment extends Fragment {

    private static final String KEY_LINK_RSS = "LINK_RSS";

    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout swipeRefreshLayout;
    protected View rootView;
    private String linkRss;
    private List<ItemRecyclerView> listItemRecyclerView = new ArrayList<>();

    public RSSReaderFragment() {

    }

    public static RSSReaderFragment newInstance(String link_rss) {
        RSSReaderFragment fragment = new RSSReaderFragment();
        Bundle args = new Bundle();
        args.putString(KEY_LINK_RSS, link_rss);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            linkRss = getArguments().getString(KEY_LINK_RSS);
        }
    }

    public void initLayout() {
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new FetchFeedTask().execute((Void) null);
        swipeRefreshLayout.setOnRefreshListener(() -> new FetchFeedTask().execute((Void) null));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rssreader, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
    }

    public List<ItemRecyclerView> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        List<ItemRecyclerView> items = new ArrayList<>();

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
                        ItemRecyclerView item = new ItemRecyclerView(title, link, description);
                        items.add(item);
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

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(linkRss))
                return false;

            try {
                if (!linkRss.startsWith("http://") && !linkRss.startsWith("https://"))
                    linkRss = "http://" + linkRss;

                URL url = new URL(linkRss);
                InputStream inputStream = url.openConnection().getInputStream();
                listItemRecyclerView = parseFeed(inputStream);
                return true;
            } catch (IOException | XmlPullParserException e) {
                Toast.makeText(getContext(),
                        "Error",
                        Toast.LENGTH_LONG).show();
            }
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean success) {
            swipeRefreshLayout.setRefreshing(false);

            if (success) {
                mRecyclerView.setAdapter(new RecyclerViewAdapter(listItemRecyclerView));
            } else {
                Toast.makeText(getContext(),
                        "Enter a valid url",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}