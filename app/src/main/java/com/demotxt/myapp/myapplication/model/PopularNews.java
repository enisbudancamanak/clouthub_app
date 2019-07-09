package com.demotxt.myapp.myapplication.model;

public class PopularNews {



    String author;
    String source;
    String title;
    String description;
    String content;
    String urlImage;
    String url;
    String publishedAt;

    public PopularNews(){

    }


    public PopularNews(String author, String source, String title, String description, String content, String urlImage, String url, String publishedAt) {
        this.author = author;
        this.source = source;
        this.title = title;
        this.description = description;
        this.content = content;
        this.urlImage = urlImage;
        this.url = url;
        this.publishedAt = publishedAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }







}
