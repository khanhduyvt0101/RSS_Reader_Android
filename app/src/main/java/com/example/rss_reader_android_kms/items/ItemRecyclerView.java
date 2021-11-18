package com.example.rss_reader_android_kms.items;

public class ItemRecyclerView {

    private String title;
    private String link;
    private String linkImage;
    private String description;
    private boolean isShowSeeLater;

    public ItemRecyclerView(String title, String link, String linkImage, String description) {
        this.title = title;
        this.link = link;
        this.linkImage = linkImage;
        this.description = description;
        this.isShowSeeLater = true;
    }

    public boolean isShowSeeLater() {
        return isShowSeeLater;
    }

    public void setShowSeeLater(boolean showSeeLater) {
        isShowSeeLater = showSeeLater;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
