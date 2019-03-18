package com.studymate101.newsfront;

/**
 * Created by Brandon on 12/25/2017.
 */

public class ListItem {
    private String head;
    private String desc;
    private String imageUrl;
    private String source;
    private String url_news;
    private String publishedAt;

    public ListItem(String head, String desc, String imageUrl, String source, String url_news, String publishedAt) {
        this.head = head;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.source = source;
        this.url_news = url_news;
        this.publishedAt = publishedAt;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getSource() {
        return source;
    }

    public String getUrl_news() {
        return url_news;
    }

    public String getPublishedAt() {
        return publishedAt;
    }
}
