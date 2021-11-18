package com.example.rss_reader_android_kms.modules.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rss_reader_android_kms.R;

public class ListWebNewActivity extends AppCompatActivity {

    public static final String KEY_WEBNEWS = "KEY_WEBNEWS";
    public static final int REQUEST_CODE = 1;
    public RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_web_new);
        TextView tvVnExpress = findViewById(R.id.tvVnExpress);
        TextView tvTinhTe = findViewById(R.id.tvTinhTe);
        mRecyclerView = findViewById(R.id.rcvListSeeLater);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvVnExpress.setOnClickListener(v -> {
            Intent intent = new Intent(this, RSSReaderActivity.class);
            intent.putExtra(KEY_WEBNEWS, RSSReaderActivity.VNEXPRESS);
            startActivityForResult(intent, REQUEST_CODE);
        });
        tvTinhTe.setOnClickListener(v -> {
            Intent intent = new Intent(this, RSSReaderActivity.class);
            intent.putExtra(KEY_WEBNEWS, RSSReaderActivity.TINHTE);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(RSSReaderActivity.listNewsLater);
        mRecyclerView.setAdapter(recyclerViewAdapter);
    }
}