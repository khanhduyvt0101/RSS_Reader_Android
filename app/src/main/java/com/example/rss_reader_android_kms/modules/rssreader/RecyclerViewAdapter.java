package com.example.rss_reader_android_kms.modules.rssreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rss_reader_android_kms.items.ItemRecyclerView;
import com.example.rss_reader_android_kms.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.FeedModelViewHolder> {
    private List<ItemRecyclerView> mRssFeedModels;

    public RecyclerViewAdapter(List<ItemRecyclerView> rssFeedModels) {
        mRssFeedModels = rssFeedModels;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyclerview, parent, false);
        return new FeedModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final ItemRecyclerView itemRecyclerView = mRssFeedModels.get(position);
        ((TextView) holder.rssFeedView.findViewById(R.id.titleText)).setText(itemRecyclerView.title);
        ((TextView) holder.rssFeedView.findViewById(R.id.descriptionText)).setText(itemRecyclerView.description);
        ((TextView) holder.rssFeedView.findViewById(R.id.linkText)).setText("Links: " + itemRecyclerView.link);
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private final View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }
    }
}
