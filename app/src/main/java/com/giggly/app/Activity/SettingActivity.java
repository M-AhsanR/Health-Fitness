package com.giggly.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Models.ProfileApiModel.Profile;
import com.giggly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    RelativeLayout back_rel;
    TextView edit_profile,change_password,push_notification,privacy_policy,logout,terms_services,blog,
            report_problem,share_friends,select_catagories,rate_us;

    public static Profile profile_data;
    public static boolean COMMENT_LIKE_NOTIFICATION;
    public static boolean FEED_ADD_NOTIFICATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initialization();


        // hit api............

        SharedPreferences mPrefs = SettingActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();
//        postParam.put("problem",problem.getText().toString());


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GET_PROFILE, SettingActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        String id = jsonObject.getString("_id");
                        String Email = jsonObject.getString("email");
                        boolean confirmed=jsonObject.getBoolean("confirmed");
                        String fullName = jsonObject.getString("full_name");
                        String Gender = jsonObject.getString("gender");
                        boolean admin_access=jsonObject.getBoolean("admin_access");
                        String dp_active_file = jsonObject.getString("dp_active_file");


                        Profile obj=new Profile();
                        obj.setProfile_id(id);
                        obj.setEmail(Email);
                        obj.setConfirmed(confirmed);
                        obj.setFull_name(fullName);
                        obj.setGender(Gender);
                        obj.setAdmin_access(admin_access);
                        obj.setActive_dp(dp_active_file);


                        profile_data=obj;











                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {


                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = SettingActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(SettingActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(SettingActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------

        hitApiForPushNotification();



        rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.positivevibes.app")));

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intt = new Intent(SettingActivity.this, EditProfileActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingChangePassword(),"InProfileSettingChangePassword");

            }
        });
        push_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(SettingActivity.this, PushNotificationActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingPushNotification(),"InProfileSettingPushNotification");

            }
        });
        select_catagories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intt = new Intent(SettingActivity.this, SelectCatagoryActivity.class);
                intt.putExtra("FromSetting",true);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingEmailSmsNotification(),"InProfileSettingEmailSmsNotification");

            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(SettingActivity.this, PrivacyPolicyActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingPrivacyPolicy(),"InProfileSettingPrivacyPolicy");

            }
        });
        terms_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(SettingActivity.this, TermsOfServiceActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingTermsOfService(),"InProfileSettingTermsOfService");

            }
        });
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://positivitymessage.com/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        report_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intt = new Intent(SettingActivity.this, ReportProblemActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



                // for mail
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("plain/text");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "haseeb@dynamiclogix.com" });
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Report");
////       mContext.startActivity(Intent.createChooser(intent, "Send your email in:"));
//
//                SettingActivity.this.startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                // for mail
            }
        });

        share_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.

//                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post: ");
                share.putExtra(Intent.EXTRA_TEXT, "Positivity Vibes \n"+" Text \n"+"\n"+"http://positivitymessage.com/");

//        share.putExtra(Intent.EXTRA_TEXT, Constants.URL.BASE_URL+feed.getPhoto_url()+" \n "+feed.getFeed_description());
//                share.putExtra(Intent.EXTRA_TEXT, "hi how r u ");


                SettingActivity.this.startActivity(Intent.createChooser(share, "Share link!"));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





//                    hit api ---------------------------------------------------



                SharedPreferences mPrefs = SettingActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                String token = mPrefs.getString("USER_TOKEN", "");


                Map<String, String> postParam = new HashMap<String, String>();


                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-sh-auth", token);

                ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.LOGOUT, SettingActivity.this, postParam, headers, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String ERROR) {

                        if (ERROR.isEmpty()) {

                            try {


                                JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                int code = jsonObject.getInt("code");
                                if (code == 200) {




                                    // clear badge count
                                    SharedPreferences mPrefs=getSharedPreferences("NotificationBadgeCount",Context.MODE_PRIVATE);


                                    SharedPreferences.Editor editor=mPrefs.edit();
                                    editor.putInt("count",0);
                                    editor.apply();


                                    // clear badge count

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {


                            if (ERROR.equals("401")) {

                                SharedPreferences mPrefs = SettingActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                SharedPreferences.Editor mEditor = mPrefs.edit();
                                mEditor.putString("USER_EMAIL", "");
                                mEditor.putString("USER_PASSWORD", "");
                                mEditor.putString("USER_TOKEN", "");
                                mEditor.putString("USER_PIC", "");
                                mEditor.apply();
                                Intent intt = new Intent(SettingActivity.this, FirstActivity.class);
                                SettingActivity.this.startActivity(intt);
                                finish();

                            } else {

                                Toast.makeText(SettingActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                            }

                        }

                    }
                });




//                    hit api ---------------------------------------------------




                mPrefs = SettingActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString("USER_EMAIL", "");
                mEditor.putString("USER_PASSWORD", "");
                mEditor.putString("USER_TOKEN", "");
                mEditor.putString("USER_PIC", "");
                mEditor.apply();

                Intent intent = new Intent(SettingActivity.this, FirstActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                SettingActivity.this.finish();


                finish();



            }
        });

        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });


    }

    private void hitApiForPushNotification() {

        // hit api............

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GET_NOTIFICATION_STATUS, SettingActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        if (code == 200) {

                            boolean comment_like_notification = jsonObject.getBoolean("comment_like_notification");
                            boolean feed_add_notification = jsonObject.getBoolean("feed_add_notification");
                            COMMENT_LIKE_NOTIFICATION =comment_like_notification;
                            FEED_ADD_NOTIFICATION =feed_add_notification;


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {


                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(SettingActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(SettingActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------



    }




    private void initialization() {


        back_rel = findViewById(R.id.back_rel);
        rate_us = findViewById(R.id.rate_us);
        edit_profile = findViewById(R.id.edit_profile);
        change_password = findViewById(R.id.change_password);
        push_notification = findViewById(R.id.push_notification);
        privacy_policy = findViewById(R.id.privacy_policy);
        terms_services = findViewById(R.id.terms_services);
        report_problem = findViewById(R.id.report_problem);
        share_friends = findViewById(R.id.share_friends);
        select_catagories = findViewById(R.id.select_catagories);
        blog = findViewById(R.id.blog);
        logout = findViewById(R.id.logout);


    }


}
