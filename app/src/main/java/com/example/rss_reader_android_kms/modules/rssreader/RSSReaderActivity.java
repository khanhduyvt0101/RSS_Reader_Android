package com.example.rss_reader_android_kms.modules.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.rss_reader_android_kms.R;
import com.example.rss_reader_android_kms.items.ItemRecyclerView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RSSReaderActivity extends AppCompatActivity {

    public static final String VNEXPRESS = "https://vnexpress.net/";
    public static final String TINHTE = "https://tinhte.vn/";

    public static List<ItemRecyclerView> listNewsLater = new ArrayList<>();
    private final List<String> listRss = new ArrayList<>();
    private String webNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssreader);
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TextView tvHome = findViewById(R.id.tvHome);
        webNews = getIntent().getStringExtra(ListWebNewActivity.KEY_URL);
        setupMenuBarAndListRSS();
        tvHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListWebNewActivity.class);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, listRss);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupMenuBarAndListRSS() {
        if (webNews.equals(VNEXPRESS)) {
            listRss.add("https://vnexpress.net/rss/tin-moi-nhat.rss");
            listRss.add("https://vnexpress.net/rss/the-gioi.rss");
            listRss.add("https://vnexpress.net/rss/thoi-su.rss");
            listRss.add("https://vnexpress.net/rss/kinh-doanh.rss");
            listRss.add("https://vnexpress.net/rss/startup.rss");
        }
        if (webNews.equals(TINHTE)) {
            listRss.add("https://tinhte.vn/rss");
        }
    }

    public void addListSeeLater(ItemRecyclerView itemRecyclerView) {
        listNewsLater.add(itemRecyclerView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ListWebNewActivity.class);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}