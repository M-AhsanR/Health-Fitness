package com.giggly.app.Models.FeedDetailModel;

import com.giggly.app.Models.FeedListModel.Comments;

import java.util.ArrayList;

public class FeedDetail {

    String feed_id;
    String title;
    boolean liked_by_me;
    String large_image;
    String small_image;
    int comments_count;
    int liked_count;
    ArrayList<Comments> comments;

    String auther_name;
    String auther_img;
    String auther_id;
    String createdAt;


    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getLiked_count() {
        return liked_count;
    }

    public void setLiked_count(int liked_count) {
        this.liked_count = liked_count;
    }

    public String getAuther_name() {
        return auther_name;
    }

    public void setAuther_name(String auther_name) {
        this.auther_name = auther_name;
    }

    public String getAuther_img() {
        return auther_img;
    }

    public void setAuther_img(String auther_img) {
        this.auther_img = auther_img;
    }

    public String getAuther_id() {
        return auther_id;
    }

    public void setAuther_id(String auther_id) {
        this.auther_id = auther_id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLiked_by_me() {
        return liked_by_me;
    }

    public void setLiked_by_me(boolean liked_by_me) {
        this.liked_by_me = liked_by_me;
    }

    public String getLarge_image() {
        return large_image;
    }

    public void setLarge_image(String large_image) {
        this.large_image = large_image;
    }

    public String getSmall_image() {
        return small_image;
    }

    public void setSmall_image(String small_image) {
        this.small_image = small_image;
    }

    public ArrayList<Comments> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comments> comments) {
        this.comments = comments;
    }
}
