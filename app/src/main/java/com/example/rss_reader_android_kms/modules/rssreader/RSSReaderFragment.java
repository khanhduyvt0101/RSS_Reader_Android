package com.example.rss_reader_android_kms.modules.rssreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rss_reader_android_kms.R;
import com.example.rss_reader_android_kms.items.ItemRecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RSSReaderFragment extends Fragment {

    private static final String KEY_LINK_RSS = "LINK_RSS";

    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout swipeRefreshLayout;
    protected View rootView;
    private String linkRss;
    private List<ItemRecyclerView> listItemRecyclerView = new ArrayList<>();
    private RSSReaderActivity rssReaderActivity;
    private RecyclerViewAdapter recyclerViewAdapter;

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
        mRecyclerView = rootView.findViewById(R.id.rcvListRSS);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            setupAdapter();
            swipeRefreshLayout.setRefreshing(false);
        });
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            fetchDataToXml();
            handler.post(this::setupAdapter);
        });
    }

    private void fetchDataToXml() {
        if (!TextUtils.isEmpty(linkRss)) {
            try {
                if (!linkRss.startsWith("http://") && !linkRss.startsWith("https://"))
                    linkRss = "http://" + linkRss;

                URL url = new URL(linkRss);
                URLConnection con = url.openConnection();
                con.addRequestProperty("User-Agent", "firefox");
                InputStream inputStream = con.getInputStream();
                listItemRecyclerView = parseFeed(inputStream);
            } catch (IOException | XmlPullParserException e) {
                Log.d("ERROR", e.getMessage());
            }
        }
    }

    private void setupAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(listItemRecyclerView);
        if (!RSSReaderActivity.listNewsLater.isEmpty()) {
            for (int i = 0; i < listItemRecyclerView.size(); i++) {
                for (int j = 0; j < RSSReaderActivity.listNewsLater.size(); j++) {
                    if (RSSReaderActivity.listNewsLater.get(j).getTitle().equals(
                            listItemRecyclerView.get(i).getTitle()
                    )) {
                        listItemRecyclerView.get(i).setShowSeeLater(false);
                    }
                }
            }
        }
        mRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setListener((action, item, view, position) -> {
            if (action == 1) {
                if (item != null) {
                    rssReaderActivity.addListSeeLater(item);
                    item.setShowSeeLater(false);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }
            if (action == 2) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rssreader, container, false);
        rssReaderActivity = (RSSReaderActivity) getActivity();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
    }

    public List<ItemRecyclerView> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {

        List<ItemRecyclerView> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);
            xmlPullParser.nextTag();
            mapXmlToItem(xmlPullParser, items);
            return items;
        } finally {
            inputStream.close();
        }
    }

    private void mapXmlToItem(XmlPullParser xmlPullParser, List<ItemRecyclerView> items) throws IOException, XmlPullParserException {
        String title = null;
        String image = null;
        String link = null;
        String description = null;
        boolean firstLink = false;
        boolean firstDescription = false;
        boolean isItem = false;
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

            String result = "";
            if (xmlPullParser.next() == XmlPullParser.TEXT) {
                result = xmlPullParser.getText();
                xmlPullParser.nextTag();
            }

            if (name.equalsIgnoreCase("title")) {
                title = result.trim();
            } else if (name.equalsIgnoreCase("link")) {
                if (!firstLink) {
                    firstLink = true;
                } else {
                    if (!result.equals(linkRss)) {
                        link = result.trim();
                    }
                }
            } else if (name.equalsIgnoreCase("description")) {
                if (!firstDescription) {
                    firstDescription = true;
                } else {
                    image = parserLink(result);
                    description = parserDescription(result);
                }
            }

            if (title != null && link != null && description != null) {
                if (isItem) {
                    ItemRecyclerView item = new ItemRecyclerView(title, link, image, description);
                    items.add(item);
                }
                title = null;
                link = null;
                description = null;
                isItem = false;
            }
        }
    }

    private String parserDescription(String result) {
        String desc;
        if (!result.contains("cafebiz.vn")) {
            int temp = 0;
            for (int i = 0; i < result.length(); i++) {
                if (result.charAt(i) == '>') {
                    temp = i;
                }
            }
            if (result.contains("></a></br>")) {
                desc = result.substring(temp + 1).trim();
            } else {
                desc = result.substring(temp).trim();
            }
        } else {
            desc = result.substring(result.indexOf("<span>") + 6, result.indexOf("</span>")).trim();
        }
        return desc;
    }

    private String parserLink(String result) {
        String image = "";
        if (!result.contains("cafebiz.vn")) {
            if (result.contains("img src=")) {
                image = result.substring(result.indexOf("src=") + 5, result.indexOf("></a>") - 2).trim();
            }
        } else {
            if (result.contains(".jpg")) {
                image = result.substring(result.indexOf("src=") + 5, result.indexOf(".jpg") + 4).trim();
            }
            if (result.contains(".jpeg")) {
                image = result.substring(result.indexOf("src=") + 5, result.indexOf(".jpeg") + 5).trim();
            }
        }
        return image;
    }
}

