package com.giggly.app.Activity;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.giggly.app.Models.LoginApiModel.Login;
import com.giggly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class LoginActivity extends AppCompatActivity {

    EditText email_et,password_et;
    Button login_btn;
    TextView forgot_password,sign_up;
    ArrayList<Login> LOGIN_API_DATA_ARR;
    LinearLayout back_lin,child_scrool;
    RelativeLayout parent_layout;
    ScrollView login_scrool;
    String email,password;
    Dialog d;
    int code_send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


            setContentView(R.layout.activity_login);
            getWindow().setBackgroundDrawableResource(R.drawable.background);

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
        AnalyticsTrackers.getInstance().trackScreenView("Login Activity");
        // analytics code

//        Bitmap bmImg = BitmapFactory.decodeResource(getResources(), R.drawable.login_background);
//
//
//
//        BitmapDrawable background = new BitmapDrawable(bmImg);
//        background.setGravity(Gravity.TOP);
//        parent_layout.setBackgroundDrawable(background);


            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    email = email_et.getText().toString();
                    password = password_et.getText().toString();



                    SharedPreferences mPrefs_token = getSharedPreferences("Token", Context.MODE_PRIVATE );
                    String DEVICE_TOKEN= mPrefs_token.getString("TOKEN","");


                    if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        if (!password.isEmpty()) {


                            final ProgressDialog ringProgressDialog;

                            ringProgressDialog = ProgressDialog.show(LoginActivity.this, "", "please wait", true);
                            ringProgressDialog.setCancelable(false);
                            ringProgressDialog.show();


                            // hit api............


                            Map<String, String> postParam = new HashMap<String, String>();
                            postParam.put("email", email_et.getText().toString());
                            postParam.put("password", password_et.getText().toString());
                            postParam.put("platform", "android");
                            postParam.put("device_token", DEVICE_TOKEN);
                            postParam.put("time_zone", TimeZone.getDefault().getID());

                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");


                            ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.LOGIN, LoginActivity.this, postParam, headers, new ServerCallback() {
                                @Override
                                public void onSuccess(JSONObject result,String ERROR) {

                                    if(ERROR.isEmpty()){

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


                                        LOGIN_API_DATA_ARR.add(login_obj);

                                        SettingFragment.profile_data.setFull_name(full_name);


                                        // saving email and password in shared pref......

                                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor mEditor = mPrefs.edit();
                                        mEditor.putString("USER_EMAIL", email);
                                        mEditor.putString("APP_USER", String.valueOf(app_user));

                                        mEditor.putString("USER_PASSWORD", password);
                                        mEditor.putString("USER_TOKEN", token);
                                        mEditor.putString("USER_ID", _id);
                                        mEditor.putString("USER_DP", dp_active_file);
                                        mEditor.putString("USER_NAME", full_name);
                                        mEditor.putString("session_id", session_id);
                                        mEditor.apply();

                                        // saving email and password in shared pref......


//                                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

//                                        Intent intt = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
//                                        Intent intt = new Intent(LoginActivity.this, HomeActivity.class);
//                                        startActivity(intt);
                                        finish();
                                        ringProgressDialog.dismiss();



//                                        // if this is the only activity open then create home activity
//
//                                        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
//
//                                        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
//
//                                        int activites=taskList.get(0).numActivities;
//                                        if( activites == 1) {
//
//                                            Intent inn = new Intent(LoginActivity.this, HomeActivity.class);
////                                            inn.putExtra("FromNotification",false);
//
//                                            startActivity(inn);
////                                            finish();
//                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//
//
//
////                    Toast.makeText(FeedDetailedActivity.this,"This is last activity in the stack",Toast.LENGTH_LONG).show();
//                                        }
//
//                                        // if this is the only activity open then create home activity


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                        ringProgressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                                        password_et.setText("");



                                    }
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            });


            forgot_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    forgetPassword();


                }
            });
            back_lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intt = new Intent(LoginActivity.this, FirstActivity.class);
                    startActivity(intt);
                    overridePendingTransition(R.anim.enter, R.anim.exit);


                    finish();
                }
            });

            parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard(v);
                }
            });

            child_scrool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard(v);
                }
            });

//        login_scrool.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hideKeyboard(v);
//            }
//        });
//        login_scrool.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                hideKeyboard(v);
//                return false;
//            }
//        });
        }


    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }



    private void forgetPassword() {


          d = new Dialog(LoginActivity.this, getChangingConfigurations());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.forgot_password_dialog_box);
        final EditText email_Edititext = (EditText) d.findViewById(R.id.email);
        Button send = (Button) d.findViewById(R.id.send);
        Button cancel = (Button) d.findViewById(R.id.cancel);
        d.setCanceledOnTouchOutside(false);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((email_Edititext.getText() != null) || (email_Edititext.getText().length() != 0)) {
                    String emailId = email_Edititext.getText().toString();

                    if (!TextUtils.isEmpty(emailId) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {

                        forgotPasswordApi(emailId);


                    } else {
                        Toast.makeText(LoginActivity.this,"Invalid email", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(LoginActivity.this,"Enter Email Id First", Toast.LENGTH_SHORT).show();

                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
        Window window = d.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);



    }

    private void ResetPasswordApi(String pin,String email,String pass) {

        SharedPreferences mPrefs_token = getSharedPreferences("Token", Context.MODE_PRIVATE );
        String DEVICE_TOKEN= mPrefs_token.getString("TOKEN","");



        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("pin", pin);
        postParam.put("email", email);
        postParam.put("password", pass);
        postParam.put("platform", "android");
        postParam.put("time_zone", TimeZone.getDefault().getID());
        postParam.put("device_token", DEVICE_TOKEN);




        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");

        ApiModelClass.GetApiResponse(Request.Method.POST,Constants.URL.RESET_PASSWORD,LoginActivity.this,postParam,headers,new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result,String ERROR) {

//                JSONObject jsonObject = null;
                try {

                    Login login_obj = new Login();

                    JSONObject jsonObject = new JSONObject(String.valueOf(result));
                    int code = jsonObject.getInt("code");
                    String token = jsonObject.getString("token");
                    String session_id = jsonObject.getString("session_id");





                    JSONObject user_object = jsonObject.getJSONObject("user");

                    String _id = user_object.getString("_id");
                    String email = user_object.getString("email");
                    boolean app_user = user_object.getBoolean("app_user");

                    boolean confirmed = user_object.getBoolean("confirmed");
                    String full_name = user_object.getString("full_name");
                    String gender = user_object.getString("gender");
                    String dp_active_file = user_object.getString("dp_active_file");
                    boolean admin_access = user_object.getBoolean("admin_access");




                    login_obj.set_id(_id);
                    login_obj.setEmail(email);
                    login_obj.setConfirmed(confirmed);
                    login_obj.setFull_name(full_name);
                    login_obj.setGender(gender);
                    login_obj.setAdmin_access(admin_access);
                    login_obj.setDp_active_file(dp_active_file);


                    LOGIN_API_DATA_ARR.add(login_obj);



                    // saving email and password in shared pref......

                    SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putString("USER_EMAIL", email);
                    mEditor.putString("APP_USER", String.valueOf(app_user));

                    mEditor.putString("USER_PASSWORD", password);
                    mEditor.putString("USER_TOKEN", token);
                    mEditor.putString("USER_ID", _id);
                    mEditor.putString("USER_DP", dp_active_file);
                    mEditor.putString("USER_NAME", full_name);
                    mEditor.putString("session_id", session_id);
                    mEditor.apply();

                    // saving email and password in shared pref......


//                                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

//                    Intent intt = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
//                    startActivity(intt);
                    finish();



//                    Toast.makeText(LoginActivity.this,message, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
    }

    private void forgotPasswordApi(final String emailId) {

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("email", emailId);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");

        ApiModelClass.GetApiResponse(Request.Method.POST,Constants.URL.FORGOT_PASSWORD,LoginActivity.this,postParam,headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result,String ERROR) {

                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(result));
                    String message=jsonObject.getString("message");
                     code_send=jsonObject.getInt("code");

                    Toast.makeText(LoginActivity.this,message, Toast.LENGTH_LONG).show();


                    if (code_send != 200) {
//                            toast
                    } else {
//                            show nect dialog


                        d.dismiss();

                        final Dialog d2 = new Dialog(LoginActivity.this, getChangingConfigurations());
                        d2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        d2.setContentView(R.layout.reset_password_dialog_box);
                        d2.setCanceledOnTouchOutside(false);


                        final EditText email_d2 = (EditText) d2.findViewById(R.id.email_d2);
                        final EditText pin_d2 = (EditText) d2.findViewById(R.id.pin_d2);
                        final EditText password_d2 = (EditText) d2.findViewById(R.id.password_d2);
                        final EditText confirm_password_d2 = (EditText) d2.findViewById(R.id.confirm_password_d2);
                        Button ok_d2 = (Button) d2.findViewById(R.id.ok_d2);
                        Button cancel_d2 = (Button) d2.findViewById(R.id.cancel_d2);

                        email_d2.setText(emailId);


                        d2.show();
                        Window window2 = d2.getWindow();
                        window2.setLayout(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


                        ok_d2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String emailId = email_d2.getText().toString();

                                if (!TextUtils.isEmpty(emailId) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {

                                    if (!TextUtils.isEmpty(pin_d2.getText())) {

                                        if (!TextUtils.isEmpty(password_d2.getText()) && !TextUtils.isEmpty(confirm_password_d2.getText())) {

                                            String pass = password_d2.getText().toString();
                                            String cnfrm_pass = confirm_password_d2.getText().toString();
                                            if (pass.equals(cnfrm_pass)) {

                                                //api hit
                                                ResetPasswordApi(pin_d2.getText().toString(), emailId, pass);

                                                d2.dismiss();

                                            } else {

                                                Toast.makeText(LoginActivity.this, " Password not matched ", Toast.LENGTH_SHORT).show();


                                            }


                                        } else {

                                            Toast.makeText(LoginActivity.this, " Enter Password ", Toast.LENGTH_SHORT).show();

                                        }

                                    } else {
                                        Toast.makeText(LoginActivity.this, " Enter pin ", Toast.LENGTH_SHORT).show();

                                    }

                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();

                                }


                            }
                        });

                        cancel_d2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                d2.dismiss();
                            }
                        });

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }


    private void Initialization(){

        email_et=(EditText)findViewById(R.id.email_et);
        password_et=(EditText)findViewById(R.id.password_et);
        login_btn=(Button)findViewById(R.id.login_btn);
        forgot_password=(TextView)findViewById(R.id.forgot_password);
        sign_up=(TextView)findViewById(R.id.sign_up);
        back_lin=(LinearLayout)findViewById(R.id.back_lin);
        child_scrool=(LinearLayout)findViewById(R.id.child_scrool);
        parent_layout=(RelativeLayout)findViewById(R.id.parent_layout);
        LOGIN_API_DATA_ARR=new ArrayList<>();
        login_scrool=findViewById(R.id.login_scrool);


    }
}
