package com.example.core.networking.vertx.rest.model;

public class Article {

    private final String id;
    private final String name;
    private final String author;
    private final String publishedDate;

    public Article(String id, String name, String author, String publishedDate) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publishedDate = publishedDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishedDate() {
        return publishedDate;
    }
}
