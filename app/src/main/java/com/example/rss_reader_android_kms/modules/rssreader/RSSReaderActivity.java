package com.example.rss_reader_android_kms.modules.rssreader;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.rss_reader_android_kms.R;

import java.util.ArrayList;
import java.util.List;

public class RSSReaderActivity extends AppCompatActivity {

    private static final String TAG = "RSSReaderActivity";
    public static final String VNEXPRESS = "https://vnexpress.net/";
    public static final String TINHTE = "https://tinhte.vn/";

    private final List<String> listRss = new ArrayList<>();
    private String webNews;

    private TextView tvHome;
    private TextView tvWorld;
    private TextView tvNews;
    private TextView tvBusiness;
    private TextView tvStartUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssreader);
        ViewPager viewPager = findViewById(R.id.viewPager);
        tvHome = findViewById(R.id.tvHome);
        tvWorld = findViewById(R.id.tvWorld);
        tvNews = findViewById(R.id.tvNews);
        tvBusiness = findViewById(R.id.tvBusiness);
        tvStartUp = findViewById(R.id.tvStartUp);
        webNews = getIntent().getStringExtra(ListWebNewActivity.KEY_WEBNEWS);
        setupMenuBarAndListRSS();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, listRss);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setupMenuBarAndListRSS() {
        if (webNews.equals(VNEXPRESS)) {
            listRss.add("https://vnexpress.net/rss/tin-moi-nhat.rss");
            tvHome.setText("Trang chủ");
            listRss.add("https://vnexpress.net/rss/the-gioi.rss");
            tvWorld.setText("Thế giới");
            listRss.add("https://vnexpress.net/rss/thoi-su.rss");
            tvNews.setText("Thời sự");
            listRss.add("https://vnexpress.net/rss/kinh-doanh.rss");
            tvWorld.setText("Kinh doanh");
            listRss.add("https://vnexpress.net/rss/startup.rss");
            tvWorld.setText("Star up");
        }
        if (webNews.equals(TINHTE)){
            listRss.add("https://tinhte.vn/rss");
            tvHome.setText("Trang chủ");
        }
    }
}