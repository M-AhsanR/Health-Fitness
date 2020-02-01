package com.giggly.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.giggly.app.AnalyticsTrackers;
import com.giggly.app.R;

import io.fabric.sdk.android.Fabric;

public class FirstActivity extends AppCompatActivity {

    TextView have_account,text;
    Button get_started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());


        // for tranparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        // for tranparent status bar

        // analytics code

        AnalyticsTrackers.initialize(this);
//        Log.d("analyticsID", );
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-126547086-1");
        AnalyticsTrackers.getInstance().trackScreenView("First Activity");
        // analytics code


        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String emaill = mPrefs.getString("USER_EMAIL", "");
        String passwordd = mPrefs.getString("USER_PASSWORD", "");
        String token = mPrefs.getString("USER_TOKEN", "");

        if (!emaill.isEmpty() && !passwordd.isEmpty()) {

            final ProgressDialog ringProgressDialog;

            ringProgressDialog = ProgressDialog.show(FirstActivity.this, "", "please wait", true);
            ringProgressDialog.setCancelable(false);
            ringProgressDialog.show();

//            Map<String, String> postParam = new HashMap<String, String>();
//            postParam.put("email", emaill);
//            postParam.put("password", passwordd);
//
//            HashMap<String, String> headers = new HashMap<String, String>();
//            headers.put("Content-Type", "application/json; charset=utf-8");

//            ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.LOGIN, FirstActivity.this, postParam, headers, new ServerCallback() {
//                @Override
//                public void onSuccess(JSONObject result) {





//                    Intent in = new Intent(FirstActivity.this, NavigationDrawerActivity.class);
                    Intent in = new Intent(FirstActivity.this, HomeActivity.class);
                    startActivity(in);
                    finish();
                    ringProgressDialog.dismiss();
//                }
//            });
        } else {


            setContentView(R.layout.activity_first);

            Initialization();

            text.setText("Bigger. better. Let’s smile. Get funny and entertaining jokes daily to bring the positive hilarity in your life.");
            String text = "Already have an account ?  LOG IN";
            SpannableString ss = new SpannableString(text);

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Intent intt = new Intent(FirstActivity.this, LoginActivity.class);
                    startActivity(intt);
//                    overridePendingTransition(R.anim.exit, R.anim.enter);

                    finish();


                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };

            ss.setSpan(clickableSpan, 27, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.WHITE),

                    text.indexOf("LOG IN"),
                    text.indexOf("LOG IN") + String.valueOf("LOG IN").length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new StyleSpan(Typeface.BOLD), 27, 33, 0);   // for bolt
            ss.setSpan(new UnderlineSpan(), 27, ss.length(), 33);

//        ss.setSpan(new RelativeSizeSpan(1.3f), 26, 32, 0);    // for size increse
            have_account.setText(ss);

            have_account.setMovementMethod(LinkMovementMethod.getInstance());
            have_account.setHighlightColor(Color.TRANSPARENT);


            get_started.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intt = new Intent(FirstActivity.this, SignUpActivity.class);
                    startActivity(intt);
                    finish();
                }
            });
//        Login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intt=new Intent(FirstActivity.this,LoginActivity.class);
//                startActivity(intt);
//            }
//        });

        }
    }



    private void Initialization() {

        have_account=(TextView)findViewById(R.id.have_account);
        text=(TextView)findViewById(R.id.text);
        get_started= findViewById(R.id.get_started);
    }
}
