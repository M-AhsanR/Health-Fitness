package com.giggly.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.Adapters.NotificationAdapter;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Models.NotificationApiModel.Notifications;
import com.giggly.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    List<Notifications> notificationList;
    RelativeLayout back_rel;
    TextView no_noti;
    ProgressBar progress,progressBar;
    private SwipeRefreshLayout swipeContainer;
    boolean isLoading =true ;
    LinearLayoutManager mLayoutManager;
    String loadMoreUrl;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);



        initialization();
        // clear badge count
        SharedPreferences mPrefs=getSharedPreferences("NotificationBadgeCount",Context.MODE_PRIVATE);


        SharedPreferences.Editor editor=mPrefs.edit();
        editor.putInt("count",0);
        editor.apply();

        // app icon badge remove

        ShortcutBadger.removeCount(NotificationActivity.this); //for 1.1.4+
//        ShortcutBadger.with(getApplicationContext()).remove();  //for 1.1.3

        // app icon badge remove

        // clear badge count


        notificationList = new ArrayList<>();

        swipeContainer.setColorSchemeColors(Color.parseColor("#ba275e"));


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//                Toast.makeText(getActivity(), " Swipe To Refresh ", Toast.LENGTH_SHORT).show();
                prepareNotificationList();
            }
        });


//        nestedScroll.setOnScrollChangeListener(new NestedScroll() {
//            @Override
//            public void onScroll() {
//
//
//                if (isLoading) {
//                    isLoading = false;
//                    Log.d("info data object", "sdfasdfadsasdfasdfasdf");
//
////                    if (hasNextPage) {
//                    progressBar.setVisibility(View.VISIBLE);
//
//                    loadMoreItems();
////                    }
//                }
//            }
//        });

        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        prepareNotificationList();




    }


    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount ,totalItemCount,pastVisiblesItems,lastVisibleItem,threshhold=1;
            if(dy > 0) {

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();


//            int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                int a = 10;
                int b = a;

//                if (!isLoading && !isLastPage) {
//                    if (visibleItemCount >= totalItemCount
//
//                            && totalItemCount >= PAGE_SIZE) {
//                        loadMoreItems();
//                    }
//                }

                if (isLoading) {

                    if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)

//                    if ( totalItemCount  <= (lastVisibleItem + threshhold))

                    {
                        isLoading = false;

                        loadMoreItems();
                    }

                }
            }
        }
    };


    private void initialization() {

        recyclerView = (RecyclerView) findViewById(R.id.notification_recyclerview);
        back_rel =  findViewById(R.id.back_rel);
        no_noti =  findViewById(R.id.no_noti);
        progress =  findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        progressBar=findViewById(R.id.progressBar4);
        swipeContainer = findViewById(R.id.swipeContainer);



    }




    private void prepareNotificationList() {



        // hit api............

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.NOTIFICATION, NotificationActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {
                        notificationList.clear();


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        if (code == 200) {


                            JSONArray notifications = jsonObject.getJSONArray("notifications");
                            String  load_more_url = jsonObject.getString("load_more_url");
                            loadMoreUrl=load_more_url;

                            for (int a = 0; a < notifications.length(); a++) {

                                Notifications obj=new Notifications();

                                JSONObject jsonObject1 = notifications.getJSONObject(a);
                                String title = jsonObject1.getString("title");
                                String notify_to = jsonObject1.getString("notify_to");
                                String liker_name = jsonObject1.getString("liker_name");
                                String createdAt = jsonObject1.getString("createdAt");
                                String feed_id = jsonObject1.getString("feed_id");
                                String notify_from_image = jsonObject1.getString("notify_from_image");
                                String feed_img = jsonObject1.getString("feed_img");

                                obj.setTitle(title);
                                obj.setNotify_to(notify_to);
                                obj.setLiker_name(liker_name);
                                obj.setCreatedAt(createdAt);
                                obj.setFeed_id(feed_id);
                                obj.setNotify_from_image(notify_from_image);
                                obj.setFeed_img(feed_img);

                                notificationList.add(obj);




                            }


                            if(notifications.length() == 0){
                                no_noti.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            else {
                                no_noti.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                                adapter = new NotificationAdapter(NotificationActivity.this, notificationList);

                                 mLayoutManager = new LinearLayoutManager(NotificationActivity.this);
                                 mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                ViewCompat.setNestedScrollingEnabled(recyclerView, false);
                                recyclerView.setNestedScrollingEnabled(false);
                                recyclerView.getItemAnimator().endAnimations();
                                recyclerView.setLayoutManager(mLayoutManager);

//                                recyclerView.setItemAnimator(new DefaultItemAnimator());

                                recyclerView.setAdapter(adapter);


                                recyclerView.setOnScrollListener(recyclerViewOnScrollListener);


                                //
//                                mLayoutManager = new LinearLayoutManager(getActivity());

                            }




                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progress.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);


                } else {

                    progress.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);


                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(NotificationActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(NotificationActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------






    }

    private void getNotificationApi2(String url) {



        // hit api............

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BASE_URL+url, NotificationActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        isLoading=true;
                        ArrayList<Notifications> more_noti_array=new ArrayList<>();


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        if (code == 200) {


                            JSONArray notifications = jsonObject.getJSONArray("notifications");
                            String load_more_url = jsonObject.getString("load_more_url");
                            loadMoreUrl=load_more_url;

                            for (int a = 0; a < notifications.length(); a++) {

                                Notifications obj=new Notifications();

                                JSONObject jsonObject1 = notifications.getJSONObject(a);
                                String title = jsonObject1.getString("title");
                                String notify_to = jsonObject1.getString("notify_to");
                                String liker_name = jsonObject1.getString("liker_name");
                                String createdAt = jsonObject1.getString("createdAt");
                                String feed_id = jsonObject1.getString("feed_id");
                                String notify_from_image = jsonObject1.getString("notify_from_image");
                                String feed_img = jsonObject1.getString("feed_img");

                                obj.setTitle(title);
                                obj.setNotify_to(notify_to);
                                obj.setLiker_name(liker_name);
                                obj.setCreatedAt(createdAt);
                                obj.setFeed_id(feed_id);
                                obj.setNotify_from_image(notify_from_image);
                                obj.setFeed_img(feed_img);

                                more_noti_array.add(obj);




                            }



                            adapter.addfeed(more_noti_array);




                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    progress.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);


                } else {

//                    progress.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);


                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(NotificationActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(NotificationActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------






    }


    private void loadMoreItems() {
        isLoading = false;


        getNotificationApi2(loadMoreUrl);
    }

}
