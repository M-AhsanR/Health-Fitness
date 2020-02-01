package com.giggly.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.Adapters.FavouriteAdapter;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.GridSpacingItemDecoration;
import com.giggly.app.Models.FavouritesApiModel.Favourites;
import com.giggly.app.NestedScroll;
import com.giggly.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavouritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FavouriteAdapter adapter;
    ArrayList<Favourites> favourites_list;
    private SwipeRefreshLayout swipeContainer;
    ProgressBar progress,progressBar;

    RelativeLayout back_rel;
    TextView no_fav;
    boolean isLoading =true ;
    NestedScrollView nestedScroll;
    String loadMoreUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);



        Initialization();
        progress.setVisibility(View.VISIBLE);

        favourites_list=new ArrayList<>();



        ViewCompat.setNestedScrollingEnabled(recyclerView, false);




        GridLayoutManager MyLayoutManager = new GridLayoutManager(this, 2);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(MyLayoutManager);
        int spacing = 5; // 50px

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacing, false));



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

        getFavouriteData();

        swipeContainer.setColorSchemeColors(Color.parseColor("#26a658"));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getFavouriteData();

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


    private void getFavouriteData() {


//                    hit api ---------------------------------------------------



        SharedPreferences mPrefs = FavouritesActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GET_FAVOURITES, FavouritesActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        favourites_list.clear();

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        String load_more_url = jsonObject.getString("load_more_url");

                        loadMoreUrl=load_more_url;

                        JSONArray favourite_images = jsonObject.getJSONArray("favourite_images");

                        for (int a = 0; a < favourite_images.length(); a++) {

                            Favourites fav_obj=new Favourites();
                            JSONObject jsonObject1 = favourite_images.getJSONObject(a);


                            String _id = jsonObject1.getString("_id");
                            String large_image = jsonObject1.getString("large_image");
                            String small_image = jsonObject1.getString("small_image");


                            fav_obj.set_id(_id);
                            fav_obj.setLarge_image(large_image);
                            fav_obj.setSmall_image(small_image);


                            favourites_list.add(fav_obj);

                        }
                        if(favourite_images.length() == 0){
                            Favourites fav_ob=new Favourites();

                            no_fav.setVisibility(View.VISIBLE);
                            swipeContainer.setVisibility(View.GONE);
                        }
                        else {
                            no_fav.setVisibility(View.GONE);
                            swipeContainer.setVisibility(View.VISIBLE);


//                            adapter = new FavouriteAdapter(FavouritesActivity.this, favourites_list);
//                            gridView.setAdapter(adapter);

                            adapter = new FavouriteAdapter(FavouritesActivity.this, favourites_list);
                            recyclerView.setAdapter(adapter);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    swipeContainer.setRefreshing(false);
                    progress.setVisibility(View.GONE);

                } else {

                    progress.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);

                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = FavouritesActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(FavouritesActivity.this, FirstActivity.class);
                        FavouritesActivity.this.startActivity(intt);
                        finish();

                    } else {

                        Toast.makeText(FavouritesActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });




//                    hit api ---------------------------------------------------


    }
    private void getFavouriteData2(String url) {


//                    hit api ---------------------------------------------------



        SharedPreferences mPrefs = FavouritesActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BASE_URL+url, FavouritesActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        isLoading=true;


                        ArrayList<Favourites> next_array=new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));




                        JSONArray favourite_images = jsonObject.getJSONArray("favourite_images");

                        for (int a = 0; a < favourite_images.length(); a++) {

                            String load_more_url = jsonObject.getString("load_more_url");

                            loadMoreUrl=load_more_url;

                            Favourites fav_obj=new Favourites();
                            JSONObject jsonObject1 = favourite_images.getJSONObject(a);


                            String _id = jsonObject1.getString("_id");
                            String large_image = jsonObject1.getString("large_image");
                            String small_image = jsonObject1.getString("small_image");


                            fav_obj.set_id(_id);
                            fav_obj.setLarge_image(large_image);
                            fav_obj.setSmall_image(small_image);


                            next_array.add(fav_obj);

                        }
//                        if(favourite_images.length() == 0){
//                            Favourites fav_ob=new Favourites();
//
//                            no_fav.setVisibility(View.VISIBLE);
//                            swipeContainer.setVisibility(View.GONE);
//                        }
//                        else {
//                            no_fav.setVisibility(View.GONE);
//                            swipeContainer.setVisibility(View.VISIBLE);
//
//
//                            adapter = new FavouriteAdapter(FavouritesActivity.this, favourites_list);
//                            gridView.setAdapter(adapter);
//                        }

                        adapter.addfeed(next_array);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);


                } else {

                    progressBar.setVisibility(View.GONE);


                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = FavouritesActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(FavouritesActivity.this, FirstActivity.class);
                        FavouritesActivity.this.startActivity(intt);
                        finish();

                    } else {

                        Toast.makeText(FavouritesActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });




//                    hit api ---------------------------------------------------


    }

    private void loadMoreItems() {
        isLoading = false;


        getFavouriteData2(loadMoreUrl);
    }



    private void Initialization() {

        recyclerView=findViewById(R.id.rv);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        progress =  findViewById(R.id.progress);
        back_rel =  findViewById(R.id.back_rel);
        no_fav =  findViewById(R.id.no_fav);
        progressBar=findViewById(R.id.progressBar4);
        nestedScroll = findViewById(R.id.scrollView);




    }

}
