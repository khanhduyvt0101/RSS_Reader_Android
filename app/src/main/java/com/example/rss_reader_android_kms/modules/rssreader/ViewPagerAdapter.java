package com.example.rss_reader_android_kms.modules.rssreader;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> listLinkRSS;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<String> listLinkRSS) {
        super(fm, behavior);
        this.listLinkRSS = listLinkRSS;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (listLinkRSS == null || listLinkRSS.isEmpty()) {
            return null;
        }
        String linkRSS = listLinkRSS.get(position);
        return RSSReaderFragment.newInstance(linkRSS);
    }

    @Override
    public int getCount() {
        if (listLinkRSS != null) {
            return listLinkRSS.size();
        }
        return 0;
    }
}
