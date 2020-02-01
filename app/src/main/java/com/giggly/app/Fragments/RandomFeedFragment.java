package com.giggly.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.Activity.FirstActivity;
import com.giggly.app.Adapters.FeedAdapter;
import com.giggly.app.AnalyticsTrackers;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Models.FeedListModel.CategoriesList;
import com.giggly.app.Models.FeedListModel.Feeds;
import com.giggly.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomFeedFragment extends Fragment {

    View rootView;


    private RecyclerView recyclerView;
    private FeedAdapter adapter;
    public static List<Feeds> feedList;
    ArrayList<CategoriesList> categoriesarrayList;
    ArrayList<String> feed_remain_array;
    LinearLayoutManager mLayoutManager;
    FragmentTransaction transaction;
    boolean isLoading =true ;
    ProgressBar progressBar,progress;
    SharedPreferences mPrefs;
    private SwipeRefreshLayout swipeContainer;
    public static String positionForRefresh="";
    public static String current_add="one";
    public static int show_add_after=0;

    public static ArrayList<String> category_history_array;
    public static ArrayList<String> category_id_history;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        rootView = inflater.inflate(R.layout.fragment_random_feed, container, false);


        // analytics code

        AnalyticsTrackers.initialize(getContext());
//        Log.d("analyticsID", );
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-126547086-1");
        AnalyticsTrackers.getInstance().trackScreenView("Jokes Feed Activity");

        // analytics code




        transaction = getFragmentManager().beginTransaction();

        setRetainInstance(true);
        feedList = new ArrayList<>();
        category_history_array = new ArrayList<>();
        category_id_history = new ArrayList<>();

        feed_remain_array = new ArrayList<>();
        progressBar=rootView.findViewById(R.id.progressBar4);
        progress=rootView.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(Color.parseColor("#ba275e"));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//                Toast.makeText(getActivity(), " Swipe To Refresh ", Toast.LENGTH_SHORT).show();
                getFeedApi(Constants.URL.FEED_LIST);
            }
        });



        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        recyclerView.setNestedScrollingEnabled(false);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.getItemAnimator().endAnimations();

        progressBar.setVisibility(View.VISIBLE);
        getFeedApi(Constants.URL.FEED_LIST);

        return rootView;
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {

                AnalyticsTrackers.getInstance().trackScreenView("Jokes Feed Activity");

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

                        JSONArray files = jsonObject.getJSONArray("files");

//                        ArrayList<Feeds> feeds_arr=new ArrayList<>();
//                        isLoading=true;
                        if (files.length() > 0) {

                            for (int a = 0; a < files.length(); a++) {


                                Feeds feed_obj = new Feeds();
                                JSONObject jsonObject1 = files.getJSONObject(a);

                                String feed_id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                String large_image = jsonObject1.getString("large_image");
                                String small_image = jsonObject1.getString("small_image");
                                String createdAt = jsonObject1.getString("createdAt");

                                boolean liked_by_me=jsonObject1.getBoolean("liked_by_me");
                                int liked_count = jsonObject1.getInt("liked_count");
                                int comments_count = jsonObject1.getInt("comments_count");

                                JSONObject author = new JSONObject(jsonObject1.getString("author"));

                                String a_id = author.getString("_id");
                                String a_name = author.getString("name");
                                String a_img = author.getString("img");


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


//                                String a_id = "_id";
//                                String a_name = "name";
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
                                feed_obj.setCategoriesArrayList(categoriesarrayList);






                                feedList.add(feed_obj);




                            }

                        } else {


                        }

                        String load_more_url = jsonObject.getString("load_more_url");

                        feed_remain_array.clear();
                        feed_remain_array.add(load_more_url);

                        adapter = new FeedAdapter(getActivity(), feedList, transaction);

//                        recyclerView.setHasFixedSize(true);
                        recyclerView.setItemViewCacheSize(20);
                        recyclerView.setDrawingCacheEnabled(true);
                        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);



                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(mLayoutManager);

                        recyclerView.setOnScrollListener(recyclerViewOnScrollListener);


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

                        JSONArray media_files = jsonObject.getJSONArray("files");

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
                                feed_obj.setCategoriesArrayList(categoriesarrayList);


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



    private void loadMoreItems() {
        isLoading = false;


        getFeedApi2(feed_remain_array.get(0));
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!feedList.isEmpty() && feedList != null){

//            adapter.notifyDataSetChanged();
//            adapter.refresh();
            if(positionForRefresh .equals("")){

            }
            else {
                adapter.notifyItemChanged(Integer.parseInt(positionForRefresh));
                positionForRefresh="";
            }
//            adapter.notifyItemChanged(11);
        }
        else {

        }
    }
}
