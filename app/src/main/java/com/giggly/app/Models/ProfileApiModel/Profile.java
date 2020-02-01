package com.giggly.app.Models.ProfileApiModel;

public class Profile {


    String profile_id;
    String email;
    boolean confirmed;
    String full_name;
    String phone_number;
    String gender;
    boolean admin_access;
    String active_dp;

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isAdmin_access() {
        return admin_access;
    }

    public void setAdmin_access(boolean admin_access) {
        this.admin_access = admin_access;
    }

    public String getActive_dp() {
        return active_dp;
    }

    public void setActive_dp(String active_dp) {
        this.active_dp = active_dp;
    }
}
