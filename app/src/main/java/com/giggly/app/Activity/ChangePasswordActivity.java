package com.giggly.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.AnalyticsTrackers;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    RelativeLayout tick_rel,back_rel;
    EditText current_password,new_password,confirm_password;
    String current_pass,new_pass,cnfrm_pass;
    ImageView tick;
    ProgressBar progress_tick;

    public static String updated_token;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initialization();

        // analytics code

        AnalyticsTrackers.initialize(ChangePasswordActivity.this);
//        Log.d("analyticsID", );
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-126547086-1");
        AnalyticsTrackers.getInstance().trackScreenView("Change Password Activity");


        // analytics code


        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                hideKeyboard(v);
            }
        });

        tick_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_pass=current_password.getText().toString();
                new_pass=new_password.getText().toString();
                cnfrm_pass=confirm_password.getText().toString();


                if(!current_pass.isEmpty()){
                    if(!new_pass.isEmpty()){
                        if(!cnfrm_pass.isEmpty()){

                            if(new_pass.equals(cnfrm_pass)){

                                tick.setVisibility(View.GONE);
                                progress_tick.setVisibility(View.VISIBLE);
                                // api call

                                mPrefs = ChangePasswordActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                String token = mPrefs.getString("USER_TOKEN", "");
                                String USER_EMAIL = mPrefs.getString("USER_EMAIL", "");
                                String DEVICE_TOKEN= mPrefs.getString("TOKEN","");



                                Map<String, String> postParam = new HashMap<String, String>();
                                postParam.put("email",USER_EMAIL);
                                postParam.put("old_password",current_pass);
                                postParam.put("password",cnfrm_pass);
                                postParam.put("platform","android");
                                postParam.put("device_token",DEVICE_TOKEN);



                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("x-sh-auth", token);

                                ApiModelClass.GetApiResponse(Request.Method.POST,  Constants.URL.CHANGE_PASSWORD, ChangePasswordActivity.this, postParam, headers, new ServerCallback() {
                                    @Override
                                    public void onSuccess(JSONObject result, String ERROR) {

                                        if (ERROR.isEmpty()) {

                                            try {

                                                JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                                String message=jsonObject.getString("message");
                                                String token=jsonObject.getString("token");

                                                updated_token=token;

                                                Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
//                                                final InputMethodManager imm = (InputMethodManager) ChangePasswordActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//                                                imm.hideSoftInputFromWindow(this..getWindowToken(), 0);

                                                finish();
//                                                replaceFragment(new SettingFragment(),"SettingFragment");



                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }



//                                            SharedPreferences mPrefs = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor mEditor = mPrefs.edit();
                                            mEditor.putString("USER_PASSWORD", cnfrm_pass);
                                            mEditor.putString("USER_TOKEN", updated_token);
                                            mEditor.apply();

                                            tick.setVisibility(View.VISIBLE);
                                            progress_tick.setVisibility(View.GONE);

                                        } else {
                                            tick.setVisibility(View.VISIBLE);
                                            progress_tick.setVisibility(View.GONE);
//                    ringProgressDialog.dismiss();
                                            Toast.makeText(ChangePasswordActivity.this, ERROR, Toast.LENGTH_SHORT).show();


                                        }
                                    }
                                });



                            }
                            else{
                                Toast.makeText(ChangePasswordActivity.this, "Password Not Match", Toast.LENGTH_SHORT).show();


                            }

                        }
                        else {
                            Toast.makeText(ChangePasswordActivity.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();

                        }

                    }
                    else{
                        Toast.makeText(ChangePasswordActivity.this, "Enter New Password", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(ChangePasswordActivity.this, "Enter Current Password", Toast.LENGTH_SHORT).show();

                }
            }
        });









    }

    protected void hideKeyboard(View view)
    {

//
//        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


    private void initialization() {

        tick_rel=findViewById(R.id.tick_rel);
        back_rel=findViewById(R.id.back_rel);
        current_password=findViewById(R.id.current_password);
        new_password=findViewById(R.id.new_password);
        confirm_password=findViewById(R.id.confirm_password);
        progress_tick=findViewById(R.id.progress_tick);
        tick=findViewById(R.id.tick);

    }

}
