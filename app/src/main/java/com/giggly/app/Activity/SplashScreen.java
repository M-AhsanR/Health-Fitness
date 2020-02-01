package com.giggly.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.crashlytics.android.Crashlytics;
import com.giggly.app.AnalyticsTrackers;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Models.LoginApiModel.Login;
import com.giggly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    public static int click=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);






//        Toast.makeText(SplashScreen.this, "out", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity




                /// code

                SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                String userEmail = mPrefs.getString("USER_EMAIL", "");
                if (!userEmail.equals("")) {

                    Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                } else {


                    // hit api............

//                Toast.makeText(SplashScreen.this, "in", Toast.LENGTH_SHORT).show();


                    SharedPreferences mPrefs_token = getSharedPreferences("Token", Context.MODE_PRIVATE);
                    String DEVICE_TOKEN = mPrefs_token.getString("TOKEN", "");
//                String DEVICE_TOKEN= "";


                    Map<String, String> postParam = new HashMap<String, String>();
//                postParam.put("text",problem.getText().toString() );
                    postParam.put("platform", "android");
                    postParam.put("device_token", DEVICE_TOKEN);
                    postParam.put("time_zone", TimeZone.getDefault().getID());


                    HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("x-sh-auth", token);

                    ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.FOREIGN_USER, SplashScreen.this, postParam, headers, new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result, String ERROR) {

                            if (ERROR.isEmpty()) {

                                try {


                                    Login login_obj = new Login();

                                    JSONObject jsonObject = new JSONObject(String.valueOf(result));
                                    int code = jsonObject.getInt("code");
                                    String token = jsonObject.getString("token");
                                    String session_id = jsonObject.getString("session_id");


                                    JSONObject user_object = jsonObject.getJSONObject("user");

                                    String _id = user_object.getString("_id");
                                    boolean app_user = user_object.getBoolean("app_user");
                                    String email = user_object.getString("email");
                                    boolean confirmed = user_object.getBoolean("confirmed");
                                    String full_name = user_object.getString("full_name");
                                    String gender = user_object.getString("gender");
                                    String dp_active_file = user_object.getString("dp_active_file");
                                    boolean admin_access = user_object.getBoolean("admin_access");


                                    login_obj.set_id(_id);
                                    login_obj.setApp_user(app_user);
                                    login_obj.setEmail(email);
                                    login_obj.setConfirmed(confirmed);
                                    login_obj.setFull_name(full_name);
                                    login_obj.setGender(gender);
                                    login_obj.setAdmin_access(admin_access);
                                    login_obj.setDp_active_file(dp_active_file);


//                                LOGIN_API_DATA_ARR.add(login_obj);


                                    // saving email and password in shared pref......

                                    SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor mEditor = mPrefs.edit();
                                    mEditor.putString("USER_EMAIL", email);
                                    mEditor.putString("APP_USER", String.valueOf(app_user));
                                    mEditor.putString("USER_PASSWORD", "");
                                    mEditor.putString("USER_TOKEN", token);
                                    mEditor.putString("USER_ID", _id);
                                    mEditor.putString("USER_DP", dp_active_file);
                                    mEditor.putString("USER_NAME", "");
                                    mEditor.putString("session_id", session_id);
                                    mEditor.apply();

                                    // saving email and password in shared pref......

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


//                            Intent i = new Intent(SplashScreen.this, FirstActivity.class);
                                Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                                startActivity(i);

                                // close this activity
                                finish();


                            } else {


//                            if (ERROR.equals("401")) {
//
//                                SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor mEditor = mPrefs.edit();
//                                mEditor.putString("USER_EMAIL", "");
//                                mEditor.putString("USER_PASSWORD", "");
//                                mEditor.putString("USER_TOKEN", "");
//                                mEditor.putString("USER_PIC", "");
//                                mEditor.apply();
//                                Intent intt = new Intent(SplashScreen.this, FirstActivity.class);
//                                startActivity(intt);
//                                finish();
//                            } else {
//
                                Toast.makeText(SplashScreen.this, ERROR, Toast.LENGTH_SHORT).show();
//
//                            }

                            }

                        }
                    });
                    // hit api--------------------

                }

                /// code




            }
        }, SPLASH_TIME_OUT);


        AnalyticsTrackers.initialize(this);
//        Log.d("analyticsID", );
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-126547086-1");
        AnalyticsTrackers.getInstance().trackScreenView("Splash Activity");

    }
}
