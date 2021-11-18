package com.example.rss_reader_android_kms.modules.rssreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "MAIN";
                break;
            case 1:
                title = "World";
                break;
            case 2:
                title = "News";
                break;
            case 3:
                title = "Business";
                break;
            case 4:
                title = "Start up";
                break;
        }
        return title;
    }

    @Override
    public int getCount() {
        if (listLinkRSS != null) {
            return listLinkRSS.size();
        }
        return 0;
    }
}
