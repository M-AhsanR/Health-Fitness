package com.giggly.app.Models.SearchPeopleApiModel;

public class SearchPeople {

    String _id;
    String full_name;
    String about;
    String gender;
    int status;
    int user_level;
    int gold;
    int gems;
    int score;
    int badge_count;
    int followers_count;
    String active_dp;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGems() {
        return gems;
    }

    public void setGems(int gems) {
        this.gems = gems;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBadge_count() {
        return badge_count;
    }

    public void setBadge_count(int badge_count) {
        this.badge_count = badge_count;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public String getActive_dp() {
        return active_dp;
    }

    public void setActive_dp(String active_dp) {
        this.active_dp = active_dp;
    }
}
