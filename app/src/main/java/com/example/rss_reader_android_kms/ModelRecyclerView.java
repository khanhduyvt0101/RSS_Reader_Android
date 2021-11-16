package com.example.rss_reader_android_kms;

public class ModelRecyclerView {

    public String title;
    public String link;
    public String description;

    public ModelRecyclerView(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }
}
