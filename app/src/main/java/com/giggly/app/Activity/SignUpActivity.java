package com.giggly.app.Activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.AnalyticsTrackers;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Fragments.SettingFragment;
import com.giggly.app.Models.RegisterApiModel.Registration;
import com.giggly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class SignUpActivity extends AppCompatActivity {

    RelativeLayout main_rel;
    LinearLayout back_lin;
    Button sign_up_btn;
    TextView log_in;
    EditText user_name_et,email_et,password_et;
    ScrollView scrool;
    ArrayList<Registration> REG_API_DATA_ARR;
    String email,password;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        getWindow().setBackgroundDrawableResource(R.drawable.background);

        Initialization();
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
        AnalyticsTrackers.getInstance().trackScreenView("SignUp Activity");
        // analytics code


        back_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(SignUpActivity.this, FirstActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                finish();
            }
        });

//        log_in.setText("Testing main kr rha hn ");
//        log_in.setTextColor(R.color.terms_policy);
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(in);
                finish();
            }
        });

//        String one = "By signing up, you agree to the ";
//        String two = "<font color='#82aefe'>Terms of Service </font>";
//        String three = "and ";
//        String four = "<font color='#82aefe'>Privacy Policy, </font>";
//        String five = "including ";
//        String six = "<font color='#82aefe'>Cookie Use. </font>";
//        String seven = "Others will be able to find you by email or phone number when provided. Privacy Options ";
//
//                terms_text.setText(Html.fromHtml(one + two+three+four+five+six+seven));
//

        // set action on textview privay policy........
        String text="By signing up, you agree to the Terms of Service and Privacy Policy, including Cookie Use. Others will be able to find you by email or phone number when provided. Privacy Options";
        SpannableString ss = new SpannableString(text);


        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Toast.makeText(SignUpActivity.this,"  Terms of Service ", Toast.LENGTH_SHORT).show();

//                startActivity(new Intent(SignUpActivity.this, NextActivity.class));


            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Toast.makeText(SignUpActivity.this,"Privacy Policy ", Toast.LENGTH_SHORT).show();

//                startActivity(new Intent(SignUpActivity.this, NextActivity.class));


            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan3 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Toast.makeText(SignUpActivity.this,"Cookies Use ", Toast.LENGTH_SHORT).show();

//                startActivity(new Intent(SignUpActivity.this, NextActivity.class));


            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 32, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, 54, 67, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan3, 80, 89, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new ForegroundColorSpan(R.color.terms_policy),
        text.indexOf("Terms of Service"),
        text.indexOf("Terms of Service") + String.valueOf("Terms of Service").length(),
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new ForegroundColorSpan(R.color.terms_policy),
                text.indexOf("Privacy Policy"),
                text.indexOf("Privacy Policy") + String.valueOf("Privacy Policy").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new ForegroundColorSpan(R.color.terms_policy),
                text.indexOf("Cookie Use"),
                text.indexOf("Cookie Use") + String.valueOf("Cookie Use").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


//        TextView textView = (TextView) findViewById(R.id.hello);
//        terms_text.setText(ss);
//
//        terms_text.setMovementMethod(LinkMovementMethod.getInstance());
//        terms_text.setHighlightColor(Color.TRANSPARENT);

        // set action on textview privay policy........



        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences mPrefs_token = getSharedPreferences("Token", Context.MODE_PRIVATE );
                String DEVICE_TOKEN= mPrefs_token.getString("TOKEN","");




                String full_name=user_name_et.getText().toString();
                 email=email_et.getText().toString();
                 password=password_et.getText().toString();
//                String cnfrm_password=confirm_password_et.getText().toString();
                if (!full_name.isEmpty()) {

                    if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        if(!password.isEmpty()  ) {

                            if (password.length() >= 6) {

                                SharedPreferences mPrefs1 = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

                                String USER_ID= mPrefs1.getString("USER_ID","");


                                final ProgressDialog ringProgressDialog;

                            ringProgressDialog = ProgressDialog.show(SignUpActivity.this, "", "please wait", true);
                            ringProgressDialog.setCancelable(false);
                            ringProgressDialog.show();

                            // hit api
                            Map<String, String> postParam = new HashMap<String, String>();
                            postParam.put("full_name", full_name);
                            postParam.put("email", email);
                            postParam.put("_id", USER_ID);
                            postParam.put("password", password);
                            postParam.put("platform", "android");
                            postParam.put("device_token", DEVICE_TOKEN);
                            postParam.put("time_zone", TimeZone.getDefault().getID());

                            HashMap<String, String> headers = new HashMap<String, String>();
//                                headers.put("Content-Type", "application/json");


                            ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.REGISTER, SignUpActivity.this, postParam, headers, new ServerCallback() {
                                @Override
                                public void onSuccess(JSONObject result, String ERROR) {


                                    if (ERROR.isEmpty()) {

                                        JSONObject jsonObject = null;
                                        try {
                                            Registration reg_obj = new Registration();


                                            jsonObject = new JSONObject(String.valueOf(result));

                                            String token = jsonObject.getString("token");
                                            String session_id = jsonObject.getString("session_id");


                                            // saving email and password in shared pref......

                                            SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor mEditor = mPrefs.edit();
                                            mEditor.putString("USER_EMAIL", email);
                                            mEditor.putString("USER_PASSWORD", password);
                                            mEditor.putString("USER_TOKEN", token);
                                            mEditor.apply();

                                            // saving email and password in shared pref......


                                            JSONObject user_object = jsonObject.getJSONObject("user");

                                            String _id = user_object.getString("_id");
                                            boolean app_user = user_object.getBoolean("app_user");

                                            String email = user_object.getString("email");
                                            boolean confirmed = user_object.getBoolean("confirmed");
                                            String full_name = user_object.getString("full_name");
                                            String gender = user_object.getString("gender");
                                            String dp_active_file = user_object.getString("dp_active_file");

                                            boolean admin_access = user_object.getBoolean("admin_access");


                                            reg_obj.setToken(token);

                                            reg_obj.set_id(_id);
                                            reg_obj.setApp_user(app_user);
                                            reg_obj.setEmail(email);
                                            reg_obj.setConfirmed(confirmed);
                                            reg_obj.setFull_name(full_name);
                                            reg_obj.setGender(gender);
                                            reg_obj.setAdmin_access(admin_access);
                                            reg_obj.setDp_active_file(dp_active_file);


                                            REG_API_DATA_ARR.add(reg_obj);
                                            int a = 10;
                                            int b = a;


                                            SharedPreferences mPrefs1 = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor mEditor1 = mPrefs1.edit();
                                            mEditor1.putString("USER_EMAIL", email);
                                            mEditor1.putString("APP_USER", String.valueOf(app_user));

                                            mEditor1.putString("USER_PASSWORD", password);
                                            mEditor1.putString("USER_TOKEN", token);
                                            mEditor1.putString("USER_ID", _id);
                                            mEditor1.putString("USER_DP", dp_active_file);
                                            mEditor1.putString("USER_NAME", full_name);
                                            mEditor1.putString("session_id", session_id);

                                            mEditor1.apply();

                                            SettingFragment.profile_data.setFull_name(full_name);


                                            Toast.makeText(SignUpActivity.this, "Account Registered", Toast.LENGTH_SHORT).show();
//                                            Intent in = new Intent(SignUpActivity.this, SelectCatagoryActivity.class);
//                                            Intent in = new Intent(SignUpActivity.this, NavigationDrawerActivity.class);
//                                            Intent in = new Intent(SignUpActivity.this, HomeActivity.class);
//                                            in.putExtra("FromSetting", false);

//                                            startActivity(in);
                                            finish();
                                            ringProgressDialog.dismiss();





                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        ringProgressDialog.dismiss();


                                        Toast.makeText(SignUpActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }

                                else {
                                Toast.makeText(SignUpActivity.this,"Password should be greater then 5 ", Toast.LENGTH_SHORT).show();

                            }

                        }
                        else{
                            Toast.makeText(SignUpActivity.this,"Enter password ", Toast.LENGTH_SHORT).show();


                        }
                    }
                    else {
                        Toast.makeText(SignUpActivity.this,"Invalid email", Toast.LENGTH_SHORT).show();

                    }
                    }
                else{
                    Toast.makeText(SignUpActivity.this,"Enter Full name ", Toast.LENGTH_SHORT).show();

                }


            }
        });


       main_rel.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               hideKeyboard(v);
               return false;
           }
       });

       scrool.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

//               InputMethodManager im =(InputMethodManager)getSystemService(SignUpActivity.INPUT_METHOD_SERVICE);
//               im.hideSoftInputFromWindow(v.getWindowToken(), 0);
           }
       });




    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void Initialization(){

        back_lin=(LinearLayout)findViewById(R.id.back_lin);
        main_rel=(RelativeLayout)findViewById(R.id.main_rel);
//        terms_text=(TextView)findViewById(R.id.terms_text);
        log_in=(TextView)findViewById(R.id.log_in);

//        register_container=(RelativeLayout)findViewById(R.id.register_container);
        user_name_et=(EditText)findViewById(R.id.user_name_et);
        email_et=(EditText)findViewById(R.id.email_et);
        password_et=(EditText)findViewById(R.id.password_et);
        sign_up_btn=(Button) findViewById(R.id.sign_up_btn);
        scrool=findViewById(R.id.scrool);
//        confirm_password_et=(EditText)findViewById(R.id.confirm_password_et);
//        full_name_et=(EditText)findViewById(R.id.full_name_et);


        REG_API_DATA_ARR=new ArrayList<>();


    }
}
