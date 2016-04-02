package com.timotiusoktorio.popularmovies.models;

/**
 * Created by Timotius on 2016-04-01.
 */

public class Review {

    String author;
    String content;

    public Review() {}

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}