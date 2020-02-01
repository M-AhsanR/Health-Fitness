package com.giggly.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.Adapters.SelectCatagoryAdapter;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Models.CatagoriesApiModel.Catagories;
import com.giggly.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectCatagoryActivity extends AppCompatActivity {


    private RecyclerView catagory_rv;
    private SelectCatagoryAdapter adapter;
    List<Catagories> catagoriesList;
    RelativeLayout save,back_rel;
    public static List<String> selected_catagories;

    boolean fromSetting=false;
    ProgressBar progress,progress_done;
    boolean apiHitFail=false;
    TextView done_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_catagory);


        fromSetting = getIntent().getBooleanExtra("FromSetting",false);



        catagory_rv =  findViewById(R.id.catagory_rv);
        save =  findViewById(R.id.save);
        back_rel =  findViewById(R.id.back_rel);
        progress =  findViewById(R.id.progress);
        done_tv =  findViewById(R.id.done_tv);
        progress_done =  findViewById(R.id.progress_done);

        if(fromSetting == true){

            save.setVisibility(View.GONE);
            back_rel.setVisibility(View.VISIBLE);

        }
        else {

            save.setVisibility(View.VISIBLE);
            back_rel.setVisibility(View.GONE);
        }
        progress.setVisibility(View.VISIBLE);
        selected_catagories=new ArrayList<>();

        catagoriesList = new ArrayList<>();
        prepareFeedList();


        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (apiHitFail == true) {

                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {


//                     hit update api


                SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                String token = mPrefs.getString("USER_TOKEN", "");


                Map<String, List<String>> postParam = new HashMap<String, List<String>>();
                postParam.put("categories", selected_catagories);


                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-sh-auth", token);

                ApiModelClass.GetApiResponseArray(Request.Method.POST, Constants.URL.EDIT_SELECTED_CATAGORY, SelectCatagoryActivity.this, postParam, headers, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String ERROR) {

                        if (ERROR.isEmpty()) {

                            try {


                                JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                int code = jsonObject.getInt("code");
                                if (code == 200) {

                                    String message = jsonObject.getString("message");
//                                        Toast.makeText(SelectCatagoryActivity.this, message, Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                                Intent intt = new Intent(SelectCatagoryActivity.this, FirstActivity.class);
                                startActivity(intt);
                                finish();
                            } else {

                                Toast.makeText(SelectCatagoryActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                            }

                        }

                    }
                });


//                     hit update api

            }
        }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (selected_catagories.isEmpty()) {

                } else {



                progress_done.setVisibility(View.VISIBLE);
                done_tv.setVisibility(View.GONE);

                // hit api............

                SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                String token = mPrefs.getString("USER_TOKEN", "");


                Map<String, List<String>> postParam = new HashMap<String, List<String>>();
                postParam.put("categories", selected_catagories);


                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-sh-auth", token);

                ApiModelClass.GetApiResponseArray(Request.Method.POST, Constants.URL.SELECT_CATAGORY, SelectCatagoryActivity.this, postParam, headers, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String ERROR) {

                        if (ERROR.isEmpty()) {

                            try {


                                JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                int code = jsonObject.getInt("code");
                                if (code == 200) {

                                    String message = jsonObject.getString("message");
//                                    Toast.makeText(SelectCatagoryActivity.this, message, Toast.LENGTH_SHORT).show();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            Intent in = new Intent(SelectCatagoryActivity.this, NavigationDrawerActivity.class);

                            startActivity(in);
                            finish();

                            progress_done.setVisibility(View.GONE);
                            done_tv.setVisibility(View.VISIBLE);

                        } else {
                            progress_done.setVisibility(View.GONE);
                            done_tv.setVisibility(View.VISIBLE);

                            if (ERROR.equals("401")) {

                                SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                SharedPreferences.Editor mEditor = mPrefs.edit();
                                mEditor.putString("USER_EMAIL", "");
                                mEditor.putString("USER_PASSWORD", "");
                                mEditor.putString("USER_TOKEN", "");
                                mEditor.putString("USER_PIC", "");
                                mEditor.apply();
                                Intent intt = new Intent(SelectCatagoryActivity.this, FirstActivity.class);
                                startActivity(intt);
                                finish();
                            } else {

                                Toast.makeText(SelectCatagoryActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                            }

                        }

                    }
                });
                // hit api--------------------


            }



            }
        });


    }

    private void prepareFeedList() {






        // hit api............

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.CATAGORY, SelectCatagoryActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        if (code == 200) {

                            apiHitFail=false;

                            selected_catagories.clear();


                            JSONArray categories = jsonObject.getJSONArray("categories");

                            for (int a = 0; a < categories.length(); a++) {

                                Catagories cat_obj=new Catagories();

                                JSONObject jsonObject1 = categories.getJSONObject(a);
                                String id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                boolean is_active=jsonObject1.getBoolean("is_active");
                                boolean subscribed_by_me=jsonObject1.getBoolean("subscribed_by_me");
                                String cat_img = jsonObject1.getString("cat_img");

                                cat_obj.set_id(id);
                                cat_obj.setTitle(title);
                                cat_obj.setIs_active(is_active);
                                cat_obj.setCat_img(cat_img);

                                // test
                                if(fromSetting == false){
                                    if(a==0 || a == 1 || a== 2){

                                        cat_obj.setSelected_by_user(true);
                                        subscribed_by_me =true;

                                    }
                                    else {
                                        cat_obj.setSelected_by_user(subscribed_by_me);

                                    }

                                }

                                else {
                                    cat_obj.setSelected_by_user(subscribed_by_me);

                                }
                                // test
//                                cat_obj.setSelect_all_chkbox(false);

                                if(subscribed_by_me == true){

                                    selected_catagories.add(id);
                                }
                                catagoriesList.add(cat_obj);


                            }

                            for (int a = 0; a < catagoriesList.size(); a++) {

                                boolean allSelected=false;

                                if(catagoriesList.get(a).isSelected_by_user()){
                                    allSelected=true;
                                    catagoriesList.get(0).setSelect_all_chkbox(true);

                                }
                                else {
                                    allSelected=false;
                                    catagoriesList.get(0).setSelect_all_chkbox(false);
                                    break;
                                }

                            }



                                progress.setVisibility(View.GONE);
                            adapter = new SelectCatagoryAdapter(SelectCatagoryActivity.this, catagoriesList);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SelectCatagoryActivity.this);

                            catagory_rv.setLayoutManager(mLayoutManager);

                            catagory_rv.setItemAnimator(new DefaultItemAnimator());

                            catagory_rv.setAdapter(adapter);




                            }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    progress.setVisibility(View.GONE);

                    apiHitFail=true;

                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(SelectCatagoryActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(SelectCatagoryActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------






    }

    public void disable_done(){

        done_tv.setTextColor(Color.parseColor("#bcbebd"));

    }
    public void enable_done(){
        done_tv.setTextColor(Color.parseColor("#FFFFFF"));

    }
}
