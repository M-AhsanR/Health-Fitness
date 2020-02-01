package com.giggly.app.Models.NotificationApiModel;

public class Notifications {


    String title;
    String notify_to;
    String liker_name;
    String createdAt;
    String feed_id;
    String notify_from_image;
    String feed_img;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotify_to() {
        return notify_to;
    }

    public void setNotify_to(String notify_to) {
        this.notify_to = notify_to;
    }

    public String getLiker_name() {
        return liker_name;
    }

    public void setLiker_name(String liker_name) {
        this.liker_name = liker_name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getNotify_from_image() {
        return notify_from_image;
    }

    public void setNotify_from_image(String notify_from_image) {
        this.notify_from_image = notify_from_image;
    }

    public String getFeed_img() {
        return feed_img;
    }

    public void setFeed_img(String feed_img) {
        this.feed_img = feed_img;
    }
}
