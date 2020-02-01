package com.giggly.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.AnalyticsTrackers;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Fragments.SettingFragment;
import com.giggly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PushNotificationActivity extends AppCompatActivity {


    RelativeLayout back_rel;
    Switch comment_notification,feed_add_noti;
    TextView comment_likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);

        initialization();
        comment_likes.setText("Comments & Likes");

        // analytics code

        AnalyticsTrackers.initialize(PushNotificationActivity.this);
//        Log.d("analyticsID", );
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-126547086-1");
        AnalyticsTrackers.getInstance().trackScreenView("Push Notification Activity");


        // analytics code

        comment_notification.setChecked(SettingFragment.COMMENT_LIKE_NOTIFICATION);
        feed_add_noti.setChecked(SettingFragment.FEED_ADD_NOTIFICATION);



        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //hit api
                saveChanges();

                //hit api


                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }


    private void initialization() {

        back_rel=findViewById(R.id.back_rel);
        comment_notification=findViewById(R.id.comment_notification);
        feed_add_noti=findViewById(R.id.feed_add_noti);
        comment_likes=findViewById(R.id.comment_likes);

    }

    private void saveChanges() {

        // hit api............

        final String comment_status= String.valueOf(comment_notification.isChecked());
        final String feed_add_status= String.valueOf(feed_add_noti.isChecked());

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("comment_like_notification",comment_status );
        postParam.put("feed_add_notification",feed_add_status );



        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATE_NOTIFICATION_STATUS, PushNotificationActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        if (code == 200) {


                            SettingFragment.COMMENT_LIKE_NOTIFICATION = Boolean.parseBoolean(comment_status);
                            SettingFragment.FEED_ADD_NOTIFICATION = Boolean.parseBoolean(feed_add_status);


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
                        Intent intt = new Intent(PushNotificationActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(PushNotificationActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------



    }


}
