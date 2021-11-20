package com.example.rss_reader_android_kms.modules.rssreader;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rss_reader_android_kms.R;
import com.example.rss_reader_android_kms.items.ItemRecyclerView;
import com.example.rss_reader_android_kms.listener.OnEventControl;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.FeedModelViewHolder> {
    private final List<ItemRecyclerView> mRssFeedModels;
    protected OnEventControl listener;

    public RecyclerViewAdapter(List<ItemRecyclerView> rssFeedModels) {
        mRssFeedModels = rssFeedModels;
    }

    @NonNull
    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_recyclerview, parent, false);
        FeedModelViewHolder feedModelViewHolder = new FeedModelViewHolder(view);
        feedModelViewHolder.setListener(listener);
        return feedModelViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final ItemRecyclerView itemRecyclerView = mRssFeedModels.get(position);
        holder.bindData(itemRecyclerView, position);
        if (itemRecyclerView.isShowSeeLater()) {
            holder.rssFeedView.findViewById(R.id.tvSeeLater).setVisibility(View.VISIBLE);
        } else {
            holder.rssFeedView.findViewById(R.id.tvSeeLater).setVisibility(View.GONE);
        }
        ((TextView) holder.rssFeedView.findViewById(R.id.titleText)).setText(itemRecyclerView.getTitle());
        ((TextView) holder.rssFeedView.findViewById(R.id.descriptionText)).setText(itemRecyclerView.getDescription());

        if (itemRecyclerView.getLinkImage() != null && !itemRecyclerView.getLinkImage().isEmpty()) {
            holder.rssFeedView.findViewById(R.id.imageView).setVisibility(View.VISIBLE);
            Glide.with(holder.getView())
                    .load(itemRecyclerView.getLinkImage())
                    .into(((ImageView) holder.rssFeedView.findViewById(R.id.imageView)));
        }
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

    public void setListener(OnEventControl listener) {
        this.listener = listener;
    }

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private final View rssFeedView;
        protected OnEventControl listener;
        protected ItemRecyclerView data;
        protected int position;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
            TextView tvSeeLater = v.findViewById(R.id.tvSeeLater);
            TextView tvViewDetails = v.findViewById(R.id.tvViewDetails);
            tvViewDetails.setOnClickListener(v12 -> invokeAction(2));
            tvSeeLater.setOnClickListener(v1 -> invokeAction(1));
        }

        public void bindData(ItemRecyclerView data, int position) {
            this.data = data;
            this.position = position;
        }

        public void setListener(OnEventControl listener) {
            this.listener = listener;
        }

        protected void invokeAction(int action) {
            if (listener != null) {
                listener.onEventControl(action, data, getView(), position);
            }
        }

        View getView() {
            return rssFeedView;
        }
    }
}
