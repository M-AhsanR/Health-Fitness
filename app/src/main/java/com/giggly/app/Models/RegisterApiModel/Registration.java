package com.giggly.app.Models.RegisterApiModel;

/**
 * Created by M on 3/7/2018.
 */

public class Registration {



        String _id;
        boolean app_user;


    String email;
        boolean confirmed;
        String full_name;
        String gender;
        boolean admin_access;

        String token;
        String dp_active_file;

    public boolean isApp_user() {
        return app_user;
    }

    public void setApp_user(boolean app_user) {
        this.app_user = app_user;
    }

    public String getDp_active_file() {
        return dp_active_file;
    }

    public void setDp_active_file(String dp_active_file) {
        this.dp_active_file = dp_active_file;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


