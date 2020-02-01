package com.giggly.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.Activity.FirstActivity;
import com.giggly.app.Adapters.SubscribedFeedAdapter;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Models.FeedListModel.Feeds;
import com.giggly.app.NestedScroll;
import com.giggly.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscribedFeedFragment extends Fragment {

    View rootView;


    private RecyclerView recyclerView;
    private SubscribedFeedAdapter adapter;
    public static List<Feeds> feedList;
    ArrayList<String> feed_remain_array;
    LinearLayoutManager mLayoutManager;
    FragmentTransaction transaction;
    boolean isLoading =true ;
    NestedScrollView nestedScroll;
    ProgressBar progressBar,progress;
    SharedPreferences mPrefs;
    private SwipeRefreshLayout swipeContainer;
    public static String positionForRefresh="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_subscribed_feed, container, false);



        transaction = getFragmentManager().beginTransaction();

        setRetainInstance(true);
        feedList = new ArrayList<>();
        feed_remain_array = new ArrayList<>();
        progressBar=rootView.findViewById(R.id.progressBar4);
        progress=rootView.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        swipeContainer.setColorSchemeColors(Color.parseColor("#26a658"));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//                Toast.makeText(getActivity(), " Swipe To Refresh ", Toast.LENGTH_SHORT).show();
                getFeedApi(Constants.URL.SUBSCRIBED_FEED);
            }
        });


//        feedList.add("one");
//        feedList.add("two");
//        feedList.add("three");



        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        nestedScroll = rootView.findViewById(R.id.scrollView);



        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.getItemAnimator().endAnimations();

//        recyclerView.setHasFixedSize(true);


//        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(mLayoutManager);

//        recyclerView.setOnScrollListener(recyclerViewOnScrollListener);




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
//        recyclerView.setOnScrollListener(recyclerViewOnScrollListener);
        progressBar.setVisibility(View.VISIBLE);
        getFeedApi(Constants.URL.SUBSCRIBED_FEED);

        return rootView;
    }





    private void getFeedApi(String url) {


        // hit api............
//        final ProgressDialog ringProgressDialog;
//
//        ringProgressDialog = ProgressDialog.show(getActivity(), "", "please wait", true);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.show();

        mPrefs = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");

//        if(token.equals(InProfileSettingChangePassword.updated_token)){
//            Toast.makeText(getActivity(), "token same", Toast.LENGTH_SHORT).show();
//
//
//        }
//
//        else {
//            Toast.makeText(getActivity(), "token changed............", Toast.LENGTH_SHORT).show();
//
//
//        }

        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, url, getActivity(), postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        feedList.clear();
//                        Feeds feed_list_obj = new Feeds();


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        JSONArray media_files = jsonObject.getJSONArray("media_files");

//                        ArrayList<Feeds> feeds_arr=new ArrayList<>();
//                        isLoading=true;
                        if (media_files.length() > 0) {

                            for (int a = 0; a < media_files.length(); a++) {


                                Feeds feed_obj = new Feeds();
                                JSONObject jsonObject1 = media_files.getJSONObject(a);

                                String feed_id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                String large_image = jsonObject1.getString("large_image");
                                String small_image = jsonObject1.getString("small_image");
                                String createdAt = jsonObject1.getString("createdAt");
                                int liked_count = jsonObject1.getInt("liked_count");
                                int comments_count = jsonObject1.getInt("comments_count");
                                boolean liked_by_me=jsonObject1.getBoolean("liked_by_me");

                                JSONObject author = new JSONObject(jsonObject1.getString("author"));

                                String a_id = author.getString("_id");
                                String a_name = author.getString("name");
                                String a_img = author.getString("img");

//                                String a_id = "_id";
//                                String a_name = "subscribed";
//                                String a_img = "/media/large_img/serve/20143fa0-958f-11e8-8889-9d6eb51cd902.png";
//





                                feed_obj.set_id(feed_id);
                                feed_obj.setTitle(title);
                                feed_obj.setLarge_image(large_image);
                                feed_obj.setSmall_image(small_image);
                                feed_obj.setLiked_by_me(liked_by_me);
                                feed_obj.setLiked_count(liked_count);
                                feed_obj.setComments_count(comments_count);
                                feed_obj.setAuther_id(a_id);
                                feed_obj.setAuther_name(a_name);
                                feed_obj.setAuther_img(a_img);
                                feed_obj.setCreatedAt(createdAt);








                                feedList.add(feed_obj);




                            }

                        } else {


                        }


                        String load_more_url = jsonObject.getString("load_more_url");

                        feed_remain_array.clear();
                        feed_remain_array.add(load_more_url);

                        adapter = new SubscribedFeedAdapter(getActivity(), feedList, transaction);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(mLayoutManager);

//                        ringProgressDialog.dismiss();

                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    swipeContainer.setRefreshing(false);
                    progress.setVisibility(View.GONE);

                } else {
                    progress.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);

                    progressBar.setVisibility(View.GONE);

                    if(ERROR.equals("401")){

//                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(getActivity(), FirstActivity.class);
                        startActivity(intt);
                        getActivity().finish();
                    }
                    else {

//                    ringProgressDialog.dismiss();
                        Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------


    }

    private void getFeedApi2(String url) {

//        feedList.clear();
//        isLoading=true;
        // hit api............
//        final ProgressDialog ringProgressDialog;
//
//        ringProgressDialog = ProgressDialog.show(getActivity(), "", "please wait", true);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.show();

        SharedPreferences mPrefs = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BASE_URL + url, getActivity(), postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

//                        Feeds feed_list_obj = new Feeds();
                        isLoading=true;

                        ArrayList<Feeds> test=new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        JSONArray media_files = jsonObject.getJSONArray("media_files");

//                        ArrayList<Feeds> feeds_arr=new ArrayList<>();
//                        isLoading=true;
                        if (media_files.length() > 0) {

                            for (int a = 0; a < media_files.length(); a++) {


                                Feeds feed_obj = new Feeds();
                                JSONObject jsonObject1 = media_files.getJSONObject(a);

                                String feed_id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                String large_image = jsonObject1.getString("large_image");
                                String small_image = jsonObject1.getString("small_image");
                                String createdAt = jsonObject1.getString("createdAt");
                                int liked_count = jsonObject1.getInt("liked_count");
                                int comments_count = jsonObject1.getInt("comments_count");
                                boolean liked_by_me=jsonObject1.getBoolean("liked_by_me");

                                JSONObject author = new JSONObject(jsonObject1.getString("author"));

                                String a_id = author.getString("_id");
                                String a_name = author.getString("name");
                                String a_img = author.getString("img");

//                                String a_id = "_id";
//                                String a_name = "subscribed";
//                                String a_img = "/media/large_img/serve/20143fa0-958f-11e8-8889-9d6eb51cd902.png";





                                feed_obj.set_id(feed_id);
                                feed_obj.setTitle(title);
                                feed_obj.setLarge_image(large_image);
                                feed_obj.setSmall_image(small_image);
                                feed_obj.setLiked_by_me(liked_by_me);
                                feed_obj.setLiked_count(liked_count);
                                feed_obj.setComments_count(comments_count);
                                feed_obj.setAuther_id(a_id);
                                feed_obj.setAuther_name(a_name);
                                feed_obj.setAuther_img(a_img);
                                feed_obj.setCreatedAt(createdAt);



                                test.add(feed_obj);




                            }


                            String load_more_url = jsonObject.getString("load_more_url");

                            feed_remain_array.clear();
                            feed_remain_array.add(load_more_url);
//
//

                            progressBar.setVisibility(View.GONE);


                            adapter.addfeed(test);



                        } else {

                            progressBar.setVisibility(View.GONE);

                        }

//                            ringProgressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);

//                    ringProgressDialog.dismiss();
                    Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();


                }

            }
        });
        // hit api--------------------


    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {

//                Toast.makeText(getActivity(), "Visible", Toast.LENGTH_SHORT).show();
//                refresh if favourite occur

                if(!FeedFragment.refresh.equals("")) {
                    adapter.notifyDataSetChanged();
                    FeedFragment.refresh = "";
                }


            }
            catch (Exception e){

            }
        }
    }

    private void loadMoreItems() {
        isLoading = false;


        getFeedApi2(feed_remain_array.get(0));
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!feedList.isEmpty() && feedList != null){

            if(positionForRefresh .equals("")){

//                adapter.notifyDataSetChanged();
            }
            else {
                adapter.notifyItemChanged(Integer.parseInt(positionForRefresh));
                positionForRefresh="";
            }        }
        else {

        }

    }
}
