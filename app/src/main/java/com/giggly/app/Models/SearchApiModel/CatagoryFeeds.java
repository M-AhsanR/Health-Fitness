package com.giggly.app.Models.SearchApiModel;

import com.giggly.app.Models.FeedListModel.CategoriesList;

import java.util.ArrayList;

public class CatagoryFeeds {

    String _id;
    String large_image;
    String small_image;
    String title;
    boolean liked_by_me;
    String createdAt;
    String active_at;
    int liked_count;
    int comments_count;

    String auth_id;
    String auth_name;
    String auth_img;

    ArrayList<CategoriesList> categories_ref;


    public ArrayList<CategoriesList> getCategories_ref() {
        return categories_ref;
    }

    public void setCategories_ref(ArrayList<CategoriesList> categories_ref) {
        this.categories_ref = categories_ref;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getActive_at() {
        return active_at;
    }

    public void setActive_at(String active_at) {
        this.active_at = active_at;
    }

    public int getLiked_count() {
        return liked_count;
    }

    public void setLiked_count(int liked_count) {
        this.liked_count = liked_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }

    public String getAuth_name() {
        return auth_name;
    }

    public void setAuth_name(String auth_name) {
        this.auth_name = auth_name;
    }

    public String getAuth_img() {
        return auth_img;
    }

    public void setAuth_img(String auth_img) {
        this.auth_img = auth_img;
    }
}
