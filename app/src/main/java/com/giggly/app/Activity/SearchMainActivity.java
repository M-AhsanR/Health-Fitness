package com.giggly.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.Adapters.GetCatagoryAdapter;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Models.CatagoriesApiModel.Catagories;
import com.giggly.app.NestedScroll;
import com.giggly.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchMainActivity extends AppCompatActivity {


    private RecyclerView catagory_rv;
    EditText search_edittext;
    RelativeLayout back_rel;
    private GetCatagoryAdapter adapter;
    List<Catagories> catagoriesList;
    public static Context context;
    boolean isLoading =true ;
    NestedScrollView nestedScroll;
    ProgressBar progressBar,progress;
    String loadMoreUrl;
    TextView no_result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);

        initializations();

        catagoriesList=new ArrayList<>();
        progress.setVisibility(View.VISIBLE);


        context=SearchMainActivity.this;

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


        search_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });

        search_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String word=s.toString();
                searchAPi(word);
//                Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
            }
        });



        searchAPi("");
        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    private void initializations() {

        catagory_rv =  findViewById(R.id.catagory_rv);
        search_edittext =  findViewById(R.id.search_edittext);
        back_rel =  findViewById(R.id.back_rel);
        progress =  findViewById(R.id.progress);
        progressBar=findViewById(R.id.progressBar4);
        nestedScroll = findViewById(R.id.scrollView);
        no_result = findViewById(R.id.no_result);





    }

    protected void hideKeyboard(View view)
    {

//
//        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        InputMethodManager imm = (InputMethodManager)SearchMainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


    private void searchAPi(String word) {






        // hit api............

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.SEARCH_KEY+word, SearchMainActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        catagoriesList.clear();
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        String load_more_url = jsonObject.getString("load_more_url");
                        loadMoreUrl=load_more_url;
                        if (code == 200) {


                            JSONArray categories = jsonObject.getJSONArray("categories");

                            for (int a = 0; a < categories.length(); a++) {

                                Catagories cat_obj = new Catagories();

                                JSONObject jsonObject1 = categories.getJSONObject(a);
                                String id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                boolean is_active = jsonObject1.getBoolean("is_active");
                                String cat_img = jsonObject1.getString("cat_img");

                                cat_obj.set_id(id);
                                cat_obj.setTitle(title);
                                cat_obj.setIs_active(is_active);
                                cat_obj.setCat_img(cat_img);

                                catagoriesList.add(cat_obj);


                            }

                            if (catagoriesList.isEmpty()) {
                                no_result.setVisibility(View.VISIBLE);
                                catagory_rv.setVisibility(View.GONE);

                            } else {
                                no_result.setVisibility(View.GONE);
                                catagory_rv.setVisibility(View.VISIBLE);


                            adapter = new GetCatagoryAdapter(SearchMainActivity.this, catagoriesList);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchMainActivity.this);

                            catagory_rv.setLayoutManager(mLayoutManager);

                            ViewCompat.setNestedScrollingEnabled(catagory_rv, false);
                            catagory_rv.setNestedScrollingEnabled(false);
                            catagory_rv.getItemAnimator().endAnimations();
                            catagory_rv.setAdapter(adapter);


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
                        Intent intt = new Intent(SearchMainActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(SearchMainActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------

    }

    private void prepareList2(String url) {






        // hit api............

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BASE_URL+url, SearchMainActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        isLoading = true;

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        String load_more_url = jsonObject.getString("load_more_url");
                        loadMoreUrl=load_more_url;
                        if (code == 200) {


                            JSONArray categories = jsonObject.getJSONArray("categories");

                            ArrayList<Catagories> new_array=new ArrayList<>();
                            for (int a = 0; a < categories.length(); a++) {

                                Catagories cat_obj=new Catagories();

                                JSONObject jsonObject1 = categories.getJSONObject(a);
                                String id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                boolean is_active=jsonObject1.getBoolean("is_active");
                                String cat_img = jsonObject1.getString("cat_img");

                                cat_obj.set_id(id);
                                cat_obj.setTitle(title);
                                cat_obj.setIs_active(is_active);
                                cat_obj.setCat_img(cat_img);

                                new_array.add(cat_obj);


                            }


                            adapter.addfeed(new_array);






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
                        Intent intt = new Intent(SearchMainActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(SearchMainActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------






    }


    private void loadMoreItems() {
        isLoading = false;


        prepareList2(loadMoreUrl);
    }


}
