package com.example.rss_reader_android_kms.modules.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rss_reader_android_kms.R;

public class ListWebNewActivity extends AppCompatActivity {

    public static final String KEY_WEBNEWS = "KEY_WEBNEWS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_web_new);
        TextView tvVnExpress = findViewById(R.id.tvVnExpress);
        TextView tvTinhTe = findViewById(R.id.tvTinhTe);
        tvVnExpress.setOnClickListener(v -> {
            Intent intent = new Intent(this, RSSReaderActivity.class);
            intent.putExtra(KEY_WEBNEWS, RSSReaderActivity.VNEXPRESS);
            startActivity(intent);
        });
        tvTinhTe.setOnClickListener(v -> {
            Intent intent = new Intent(this, RSSReaderActivity.class);
            intent.putExtra(KEY_WEBNEWS, RSSReaderActivity.TINHTE);
            startActivity(intent);
        });
    }
}