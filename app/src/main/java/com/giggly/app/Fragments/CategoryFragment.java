package com.giggly.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.giggly.app.Activity.SelectCatagoryActivity;
import com.giggly.app.Adapters.CategoryFragmentAdapter;
import com.giggly.app.Adapters.SelectCatagoryAdapter;
import com.giggly.app.AnalyticsTrackers;
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

public class CategoryFragment extends Fragment {

    private RecyclerView catagory_rv;
    ProgressBar progress;
    private CategoryFragmentAdapter adapter;



    List<Catagories> catagoriesList;

    View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_category, container, false);


        // analytics code

        AnalyticsTrackers.initialize(getContext());
//        Log.d("analyticsID", );
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-126547086-1");
        AnalyticsTrackers.getInstance().trackScreenView("Categories Activity");


        // analytics code


        catagoriesList = new ArrayList<>();

        catagory_rv =  rootView.findViewById(R.id.catagory_rv);
        progress =  rootView.findViewById(R.id.progress);
        getCategories();




        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {

                AnalyticsTrackers.getInstance().trackScreenView("Categories Activity");



//                Toast.makeText(getActivity(), "Challenge fragmen comes", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){

            }
        }
    }

    private void getCategories() {






        // hit api............

        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.CATAGORY, getContext(), postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        if (code == 200) {

//                            apiHitFail=false;

//                            selected_catagories.clear();


                            JSONArray categories = jsonObject.getJSONArray("categories");

                            for (int a = 0; a < categories.length(); a++) {

                                Catagories cat_obj=new Catagories();

                                JSONObject jsonObject1 = categories.getJSONObject(a);
                                String id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                boolean is_active=jsonObject1.getBoolean("is_active");
                                boolean subscribed_by_me=jsonObject1.getBoolean("subscribed_by_me");
                                String cat_img = jsonObject1.getString("cat_img");
                                String background_img = jsonObject1.getString("background_img");
                                String media_count = jsonObject1.getString("media_count");

                                cat_obj.set_id(id);
                                cat_obj.setTitle(title);
                                cat_obj.setIs_active(is_active);
                                cat_obj.setCat_img(cat_img);
                                cat_obj.setBackground_img(background_img);
                                cat_obj.setMedia_count(media_count);


//                                cat_obj.setSelect_all_chkbox(false);


                                catagoriesList.add(cat_obj);


                            }



                            progress.setVisibility(View.GONE);
                            adapter = new CategoryFragmentAdapter(getContext(), catagoriesList);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

                            catagory_rv.setLayoutManager(mLayoutManager);

                            catagory_rv.setItemAnimator(new DefaultItemAnimator());

                            catagory_rv.setAdapter(adapter);




                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    progress.setVisibility(View.GONE);

//                    apiHitFail=true;

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


}
