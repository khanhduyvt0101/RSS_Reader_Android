package com.example.rss_reader_android_kms.listener;

import android.view.View;

import com.example.rss_reader_android_kms.items.ItemRecyclerView;

public interface OnEventControl {

    void onEventControl(int action, ItemRecyclerView item, View view, int position);
}
