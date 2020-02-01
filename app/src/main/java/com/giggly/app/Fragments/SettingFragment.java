package com.giggly.app.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.Activity.ChangePasswordActivity;
import com.giggly.app.Activity.EditProfileActivity;
import com.giggly.app.Activity.FirstActivity;
import com.giggly.app.Activity.HomeActivity;
import com.giggly.app.Activity.PrivacyPolicyActivity;
import com.giggly.app.Activity.PushNotificationActivity;
import com.giggly.app.Activity.ReportProblemActivity;
import com.giggly.app.Activity.SelectCatagoryActivity;
import com.giggly.app.Activity.TermsOfServiceActivity;
import com.giggly.app.AnalyticsTrackers;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Models.ProfileApiModel.Profile;
import com.giggly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SettingFragment extends Fragment {

    View rootView;
    TextView edit_profile,add_account,change_password,push_notification,privacy_policy,logout,terms_services,
            report_problem,share_friends,select_catagories,rate_us;

    public static Profile profile_data;
    public static boolean COMMENT_LIKE_NOTIFICATION;
    public static boolean FEED_ADD_NOTIFICATION;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_setting, container, false);

        initialization();


        // analytics code

        AnalyticsTrackers.initialize(getContext());
//        Log.d("analyticsID", );
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-126547086-1");
        AnalyticsTrackers.getInstance().trackScreenView("Setting Activity");


        // analytics code


        // hit api............

        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");
        String appUser = mPrefs.getString("APP_USER","");


        if(appUser.equals("false")){

            edit_profile.setVisibility(View.GONE);
            change_password.setVisibility(View.GONE);
            add_account.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);



        }
        if(appUser.equals("true")){

            edit_profile.setVisibility(View.VISIBLE);
            change_password.setVisibility(View.VISIBLE);
            add_account.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);


        }


        Map<String, String> postParam = new HashMap<String, String>();
//        postParam.put("problem",problem.getText().toString());


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GET_PROFILE, getContext(), postParam, headers, new ServerCallback() {
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

                        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(getContext(), FirstActivity.class);
                        startActivity(intt);
                        getActivity().finish();
                    } else {

                        Toast.makeText(getContext(), ERROR, Toast.LENGTH_SHORT).show();

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


        add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (HomeActivity) getContext();

                Intent intt = new Intent(getContext(), FirstActivity.class);
                startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Activity activity = (HomeActivity) getContext();

                Intent intt = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (HomeActivity) getContext();

                Intent intt = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingChangePassword(),"InProfileSettingChangePassword");

            }
        });
        push_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (HomeActivity) getContext();

                Intent intt = new Intent(getContext(), PushNotificationActivity.class);
                startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingPushNotification(),"InProfileSettingPushNotification");

            }
        });
        select_catagories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = (HomeActivity) getContext();


                Intent intt = new Intent(getContext(), SelectCatagoryActivity.class);
                intt.putExtra("FromSetting",true);
                startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingEmailSmsNotification(),"InProfileSettingEmailSmsNotification");

            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (HomeActivity) getContext();

                Intent intt = new Intent(getContext(), PrivacyPolicyActivity.class);
                startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingPrivacyPolicy(),"InProfileSettingPrivacyPolicy");

            }
        });
        terms_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = (HomeActivity) getContext();

                Intent intt = new Intent(getContext(), TermsOfServiceActivity.class);
                startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingTermsOfService(),"InProfileSettingTermsOfService");

            }
        });

        report_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Activity activity = (HomeActivity) getContext();

                Intent intt = new Intent(getContext(), ReportProblemActivity.class);
                startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



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


                getContext().startActivity(Intent.createChooser(share, "Share link!"));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





//                    hit api ---------------------------------------------------



                SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                String token = mPrefs.getString("USER_TOKEN", "");


                Map<String, String> postParam = new HashMap<String, String>();


                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-sh-auth", token);

                ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.LOGOUT, getContext(), postParam, headers, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String ERROR) {

                        if (ERROR.isEmpty()) {

                            try {


                                JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                int code = jsonObject.getInt("code");
                                if (code == 200) {






                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {


                            if (ERROR.equals("401")) {

                                SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                SharedPreferences.Editor mEditor = mPrefs.edit();
                                mEditor.putString("USER_EMAIL", "");
                                mEditor.putString("USER_PASSWORD", "");
                                mEditor.putString("USER_TOKEN", "");
                                mEditor.putString("USER_PIC", "");
                                mEditor.apply();
                                Intent intt = new Intent(getContext(), FirstActivity.class);
                                getContext().startActivity(intt);
                                getActivity().finish();

                            } else {

                                Toast.makeText(getContext(), ERROR, Toast.LENGTH_SHORT).show();

                            }

                        }

                    }
                });




//                    hit api ---------------------------------------------------


                // clear badge count
                SharedPreferences mPrefs1=getContext().getSharedPreferences("Notification_badges_count",Context.MODE_PRIVATE);


                SharedPreferences.Editor editor1=mPrefs1.edit();
                editor1.putInt("count",0);
                editor1.apply();


                // clear badge count

                mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString("USER_EMAIL", "");
                mEditor.putString("USER_PASSWORD", "");
                mEditor.putString("USER_TOKEN", "");
                mEditor.putString("USER_PIC", "");


                mEditor.putString("USER_EMAIL", "");
                mEditor.putString("APP_USER", "false");

                mEditor.putString("USER_PASSWORD", "");
                mEditor.putString("USER_TOKEN", "");
                mEditor.putString("USER_ID", "");
                mEditor.putString("USER_DP", "");
                mEditor.putString("USER_NAME", "");
                mEditor.putString("session_id", "");


                mEditor.apply();

                Activity activity = (HomeActivity) getContext();

                Intent intent = new Intent(getContext(), FirstActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                getContext().finish();


//                getActivity().finish();



            }
        });





        return  rootView;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {


                AnalyticsTrackers.getInstance().trackScreenView("Setting Activity");

//                Toast.makeText(getActivity(), "Challenge fragmen comes", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){

            }
        }
    }



    private void hitApiForPushNotification() {

        // hit api............

        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GET_NOTIFICATION_STATUS, getContext(), postParam, headers, new ServerCallback() {
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

                        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(getContext(), FirstActivity.class);
                        startActivity(intt);
                        getActivity().finish();
                    } else {

                        Toast.makeText(getContext(), ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------



    }


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String appUser = mPrefs.getString("APP_USER","");


        if(appUser.equals("false")){

            edit_profile.setVisibility(View.GONE);
            change_password.setVisibility(View.GONE);
            add_account.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);



        }
        if (appUser.equals("true")){

            edit_profile.setVisibility(View.VISIBLE);
            change_password.setVisibility(View.VISIBLE);
            add_account.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);


        }

    }

    private void initialization() {


        rate_us = rootView.findViewById(R.id.rate_us);
        edit_profile = rootView.findViewById(R.id.edit_profile);
        add_account = rootView.findViewById(R.id.add_account);
        change_password = rootView.findViewById(R.id.change_password);
        push_notification = rootView.findViewById(R.id.push_notification);
        privacy_policy = rootView.findViewById(R.id.privacy_policy);
        terms_services = rootView.findViewById(R.id.terms_services);
        report_problem = rootView.findViewById(R.id.report_problem);
        share_friends = rootView.findViewById(R.id.share_friends);
        select_catagories = rootView.findViewById(R.id.select_catagories);
        logout = rootView.findViewById(R.id.logout);


    }


}
