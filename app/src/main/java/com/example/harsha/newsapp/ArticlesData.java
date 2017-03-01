package com.example.harsha.newsapp;

/**
 * Created by harsha on 09/02/17.
 */
public class ArticlesData {

    String title;

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

    String description;
    String urlImage;
    String url;

    public ArticlesData(String title, String description, String imageURI, String url) {
        this.title=title;
        this.description=description;
        this.urlImage=imageURI;
        this.url=url;
    }
}
