package com.clouthub_app.myapp.clouthub.model;

public class FortniteNews {

    private String title;
    private String body;
    private String image_url;

    public FortniteNews(){

    }


    public FortniteNews(String title, String body, String image_url) {
        this.title = title;
        this.body = body;
        this.image_url = image_url;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
