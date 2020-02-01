package com.giggly.app.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.Adapters.SelectFromSearchGridAdapter;
import com.giggly.app.AnalyticsTrackers;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Fragments.RandomFeedFragment;
import com.giggly.app.Models.FeedListModel.CategoriesList;
import com.giggly.app.Models.SearchApiModel.CatagoryFeeds;
import com.giggly.app.NestedScroll;
import com.giggly.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectFromSearchActivity extends AppCompatActivity {

    RelativeLayout back_rel,home;
    RecyclerView recyclerView;
    SelectFromSearchGridAdapter adapter;
    ArrayList<CatagoryFeeds> cat_feed_list;
    public static String cat_id, activity_name;
    String cat_name;
    ProgressBar progress,progressBar;
    TextView no_img,catagory_name;
    boolean isLoading =true ;
    NestedScrollView nestedScroll;
    String loadMoreUrl;
    LinearLayoutManager mLayoutManager;
    ArrayList<CategoriesList> categoriesarrayList;
    public static String current_add="one";
    ArrayList<String> category_history_array;
    ArrayList<String> category_id_array;




    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (category_history_array.size() == 1){
            category_history_array.clear();
            category_id_array.clear();
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }else {

            Intent intent = new Intent(SelectFromSearchActivity.this, SelectFromSearchActivity.class);
            intent.putExtra("CAT_ID", category_id_array.get(category_id_array.size() -2));
            intent.putExtra("CAT_NAME", category_history_array.get(category_history_array.size() -2));
            startActivity(intent);
            if (category_history_array.size() > 0){
                category_history_array.remove(category_history_array.size() -1);
            }
            if (category_id_array.size() > 0){
                category_id_array.remove(category_id_array.size() -1);
            }
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_search);

        cat_id = getIntent().getStringExtra("CAT_ID");
        cat_name = getIntent().getStringExtra("CAT_NAME");
        activity_name = getIntent().getStringExtra("ACTIVITY");
        category_history_array = RandomFeedFragment.category_history_array;
        category_id_array = RandomFeedFragment.category_id_history;

        initialization();

        AnalyticsTrackers.initialize(this);

        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-126547086-1");
        AnalyticsTrackers.getInstance().trackScreenView(cat_name + " Category Activity");

        home.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);

        catagory_name.setText(cat_name);
        cat_feed_list=new ArrayList<>();
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(this);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(MyLayoutManager);
//        int spacing = 5; // 50px
//
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacing, false));

        nestedScroll.setOnScrollChangeListener(new NestedScroll() {
            @Override
            public void onScroll() {


                if (isLoading) {
                    isLoading = false;
                    Log.d("info data object", "sdfasdfadsasdfasdfasdf");

//                    if (hasNextPage) {
                    progressBar.setVisibility(View.VISIBLE);

                    loadMoreItems();
//                    }
                }
            }
        });


        getData();

        home.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                SearchMainActivity activity = (SearchMainActivity) SearchMainActivity.context;
                cat_id = null;
                Intent intent = new Intent(SelectFromSearchActivity.this, NavigationDrawerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        back_rel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                if (category_history_array.size() == 1){
                    category_history_array.clear();
                    category_id_array.clear();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }else {

                    Intent intent = new Intent(SelectFromSearchActivity.this, SelectFromSearchActivity.class);
                    intent.putExtra("CAT_ID", category_id_array.get(category_id_array.size() -2));
                    intent.putExtra("CAT_NAME", category_history_array.get(category_history_array.size() -2));
                    startActivity(intent);
                    if (category_history_array.size() > 0){
                        category_history_array.remove(category_history_array.size() -1);
                    }
                    if (category_id_array.size() > 0){
                        category_id_array.remove(category_id_array.size() -1);
                    }
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }

            }
        });

    }

    private void getData() {





        // hit api............

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.SELECT_CAT_FROM_SEARCH+cat_id, SelectFromSearchActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        String load_more_url = jsonObject.getString("load_more_url");

                        JSONObject categoryobj = jsonObject.getJSONObject("category");
                        String category_id = categoryobj.getString("_id");
                        String category_title = categoryobj.getString("title");
                        boolean category_is_active = categoryobj.getBoolean("is_active");
                        int category_order_number = categoryobj.getInt("order_number");
                        String category_img = categoryobj.getString("cat_img");

                        loadMoreUrl=load_more_url;

                        if (code == 200) {


                            JSONArray media_files = jsonObject.getJSONArray("media_files");

                            for (int a = 0; a < media_files.length(); a++) {

                                CatagoryFeeds obj = new CatagoryFeeds();

                                JSONObject jsonObject1 = media_files.getJSONObject(a);
                                JSONObject authorObj = jsonObject1.getJSONObject("author");
                                String author_id = authorObj.getString("_id");
                                String author_name = authorObj.getString("name");
                                String author_img = authorObj.getString("img");

                                String id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                boolean liked_by_me = jsonObject1.getBoolean("liked_by_me");
                                String createdAt = jsonObject1.getString("createdAt");
                                String active_at = jsonObject1.getString("active_at");
                                int liked_count = jsonObject1.getInt("liked_count");
                                int comments_count = jsonObject1.getInt("comments_count");
                                String large_image = jsonObject1.getString("large_image");
                                String small_image = jsonObject1.getString("small_image");

                                JSONArray categories_ref = jsonObject1.getJSONArray("categories_ref");

                                categoriesarrayList = new ArrayList<>();

                                for (int i = 0; i < categories_ref.length(); i++){


                                    JSONObject categories_obj =categories_ref.getJSONObject(i);
                                    JSONObject categories = categories_obj.getJSONObject("category");
                                    CategoriesList categoriesList = new CategoriesList();

                                    String cate_id = categories.getString("_id");
                                    String cate_title = categories.getString("title");
                                    String cate_image = categories.getString("cat_img");

                                    categoriesList.setCate_id(cate_id);
                                    categoriesList.setCate_title(cate_title);
                                    categoriesList.setCate_img(cate_image);
                                    categoriesarrayList.add(categoriesList);
                                }

                                obj.set_id(id);
                                obj.setLarge_image(large_image);
                                obj.setSmall_image(small_image);
                                obj.setActive_at(active_at);
                                obj.setCategories_ref(categoriesarrayList);
                                obj.setComments_count(comments_count);
                                obj.setCreatedAt(createdAt);
                                obj.setLiked_by_me(liked_by_me);
                                obj.setTitle(title);
                                obj.setLiked_count(liked_count);
                                obj.setAuth_id(author_id);
                                obj.setAuth_name(author_name);
                                obj.setAuth_img(author_img);

                                cat_feed_list.add(obj);


                            }


                            if (cat_feed_list.isEmpty()) {

                                no_img.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);

                            } else {
                                no_img.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                                adapter = new SelectFromSearchGridAdapter(SelectFromSearchActivity.this, cat_feed_list);
                                recyclerView.setAdapter(adapter);


                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progress.setVisibility(View.GONE);

                } else {

                    progress.setVisibility(View.GONE);

                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(SelectFromSearchActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(SelectFromSearchActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------





    }
    private void getData2(String url) {





        // hit api............

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BASE_URL+url, SelectFromSearchActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        isLoading=true;


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        String load_more_url = jsonObject.getString("load_more_url");

                        JSONObject categoryobj = jsonObject.getJSONObject("category");
                        String category_id = categoryobj.getString("_id");
                        String category_title = categoryobj.getString("title");
                        boolean category_is_active = categoryobj.getBoolean("is_active");
                        int category_order_number = categoryobj.getInt("order_number");
                        String category_img = categoryobj.getString("cat_img");

                        loadMoreUrl=load_more_url;

                        if (code == 200) {


                            JSONArray media_files = jsonObject.getJSONArray("media_files");

                            ArrayList<CatagoryFeeds> next_array=new ArrayList<>();
                            for (int a = 0; a < media_files.length(); a++) {

                                CatagoryFeeds obj = new CatagoryFeeds();

                                JSONObject jsonObject1 = media_files.getJSONObject(a);
                                JSONObject authorObj = jsonObject1.getJSONObject("author");
                                String author_id = authorObj.getString("_id");
                                String author_name = authorObj.getString("name");
                                String author_img = authorObj.getString("img");

                                String id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                boolean liked_by_me = jsonObject1.getBoolean("liked_by_me");
                                String createdAt = jsonObject1.getString("createdAt");
                                String active_at = jsonObject1.getString("active_at");
                                int liked_count = jsonObject1.getInt("liked_count");
                                int comments_count = jsonObject1.getInt("comments_count");
                                String large_image = jsonObject1.getString("large_image");
                                String small_image = jsonObject1.getString("small_image");

                                JSONArray categories_ref = jsonObject1.getJSONArray("categories_ref");

                                categoriesarrayList = new ArrayList<>();

                                for (int i = 0; i < categories_ref.length(); i++){


                                    JSONObject categories_obj =categories_ref.getJSONObject(i);
                                    JSONObject categories = categories_obj.getJSONObject("category");
                                    CategoriesList categoriesList = new CategoriesList();

                                    String cate_id = categories.getString("_id");
                                    String cate_title = categories.getString("title");
                                    String cate_image = categories.getString("cat_img");

                                    categoriesList.setCate_id(cate_id);
                                    categoriesList.setCate_title(cate_title);
                                    categoriesList.setCate_img(cate_image);
                                    categoriesarrayList.add(categoriesList);
                                }

                                obj.set_id(id);
                                obj.setLarge_image(large_image);
                                obj.setSmall_image(small_image);
                                obj.setActive_at(active_at);
                                obj.setCategories_ref(categoriesarrayList);
                                obj.setComments_count(comments_count);
                                obj.setCreatedAt(createdAt);
                                obj.setLiked_by_me(liked_by_me);
                                obj.setTitle(title);
                                obj.setLiked_count(liked_count);
                                obj.setAuth_id(author_id);
                                obj.setAuth_name(author_name);
                                obj.setAuth_img(author_img);

                                next_array.add(obj);


                            }


                            adapter.addfeed(next_array);



                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);

                } else {

                    progressBar.setVisibility(View.GONE);

                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(SelectFromSearchActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(SelectFromSearchActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------





    }

    private void loadMoreItems() {
        isLoading = false;


        getData2(loadMoreUrl);
    }


    private void initialization() {

        back_rel=findViewById(R.id.back_rel);
        recyclerView=findViewById(R.id.rv);
        home=findViewById(R.id.home);
        progress=findViewById(R.id.progress);
        no_img=findViewById(R.id.no_img);
        progressBar=findViewById(R.id.progressBar4);
        nestedScroll = findViewById(R.id.scrollView);
        catagory_name = findViewById(R.id.catagory_name);




    }
}