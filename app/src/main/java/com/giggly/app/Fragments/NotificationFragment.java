package com.giggly.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.Activity.FirstActivity;
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


public class NotificationFragment extends Fragment {


    View rootView;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    List<Notifications> notificationList;
    TextView no_noti;
    ProgressBar progress,progressBar;
    private SwipeRefreshLayout swipeContainer;
    boolean isLoading =true ;
    LinearLayoutManager mLayoutManager;
    String loadMoreUrl;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);



        initialization();
        
        progress.setVisibility(View.VISIBLE);
        // clear badge count
        SharedPreferences mPrefs=getContext().getSharedPreferences("NotificationBadgeCount",Context.MODE_PRIVATE);


        SharedPreferences.Editor editor=mPrefs.edit();
        editor.putInt("count",0);
        editor.apply();


        // clear badge count


        notificationList = new ArrayList<>();

        swipeContainer.setColorSchemeColors(Color.parseColor("#26a658"));


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


        prepareNotificationList();



        return rootView;
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

        recyclerView = rootView.findViewById(R.id.notification_recyclerview);
        no_noti =  rootView.findViewById(R.id.no_noti);
        progress =  rootView.findViewById(R.id.progress);
        
        progressBar=rootView.findViewById(R.id.progressBar4);
        swipeContainer = rootView.findViewById(R.id.swipeContainer);



    }




    private void prepareNotificationList() {



        // hit api............

        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.NOTIFICATION, getContext(), postParam, headers, new ServerCallback() {
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

                                adapter = new NotificationAdapter(getContext(), notificationList);

                                mLayoutManager = new LinearLayoutManager(getContext());
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

    private void getNotificationApi2(String url) {



        // hit api............

        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BASE_URL+url, getContext(), postParam, headers, new ServerCallback() {
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


    private void loadMoreItems() {
        isLoading = false;


        getNotificationApi2(loadMoreUrl);
    }
    
}