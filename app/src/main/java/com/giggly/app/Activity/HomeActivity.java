package com.giggly.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giggly.app.Adapters.PagerAdapterr;
import com.giggly.app.AnalyticsTrackers;
import com.giggly.app.NonSwipeableViewPager;
import com.giggly.app.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;


    ImageView tab_icon;
    RelativeLayout tab_badge;
    TextView badgeText;
    NonSwipeableViewPager viewPager;
    PagerAdapterr adapter;
    AdView banner_add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Initialization();

        // analytics code

        AnalyticsTrackers.initialize(this);
//        Log.d("analyticsID", );
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-126547086-1");
        AnalyticsTrackers.getInstance().trackScreenView("Home Tabs Activity");

        // analytics code


        MobileAds.initialize(HomeActivity.this, String.valueOf(R.string.add_app_id));
        // app id original
//            MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));


        AdRequest ad_req = new AdRequest.Builder().build();
        banner_add.loadAd(ad_req);




        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.custom_tab).setIcon(R.drawable.home_selected_icon));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.custom_tab).setIcon(R.drawable.favourite_unselect));
//        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.custom_tab_center).setIcon(R.drawable.search_green));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.custom_tab).setIcon(R.drawable.category_unselected_icon));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.custom_tab).setIcon(R.drawable.setting_unselect));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        for(int i = 0; i < 4; i++) {
            TabLayout.Tab tabb = tabLayout.getTabAt(i); // fourth tab
            View tabView = tabb.getCustomView();
            tab_icon = tabView.findViewById(R.id.tab_icon);
            tab_badge = tabView.findViewById(R.id.tab_badge);
//            badgeText.setText("4");
            if (i == 0) {tab_icon.setImageResource(R.drawable.home_selected_icon);}
//            if (i == 1) {tab_icon.setImageResource(R.drawable.search_unselect);}
            if (i == 1) {
//                setBadges(i);
                tab_icon.setImageResource(R.drawable.favourite_unselect);
            }
            if (i == 2) {tab_icon.setImageResource(R.drawable.category_unselected_icon);}
            if (i == 3) {tab_icon.setImageResource(R.drawable.setting_unselect);}

        }

//        setBadges(2);
//        setBadgesForServerNotifications(3);
        adapter = new PagerAdapterr
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(4);         /* limit is a fixed integer*/



        viewPager.setAdapter(adapter);



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                int pos=tab.getPosition();

                if (tab.getPosition() == 0){
                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.home_selected_icon);
//                    tab.setIcon(R.drawable.home_blue);
//                    setBadges(2);
//                    setBadgesForServerNotifications(3);


                }
                if(tab.getPosition() == 1){

                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition()); // fourth tab
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);

                    tab_icon.setImageResource(R.drawable.favourite_selected_icon);

//                    tab.setIcon(R.drawable.search_blue);
//                    setBadges(2);
//                    setBadgesForServerNotifications(3);


                }
                if(tab.getPosition() == 2){
                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.category_selected_icon);

                    // clear notification  count ///

                    SharedPreferences mPrefs=getSharedPreferences("Challenges_badges_count",Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor=mPrefs.edit();
                    editor.putInt("total_challanges_count",0);
                    editor.apply();
                    // clear notification  count ///

//                    setBadges(tab.getPosition());
//                    setBadgesForServerNotifications(3);

                }
                if(tab.getPosition() == 3){

                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.setting_selected_icon);

                    // clear notification  count ///

                    SharedPreferences mPrefs=getSharedPreferences("ServerNotification_counts",Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor=mPrefs.edit();
                    editor.putInt("counts",0);
                    editor.apply();
                    // clear notification  count ///


//                    setBadges(2);
//                    setBadgesForServerNotifications(3);


                }
//                if(tab.getPosition() == 4){
//
//                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
//                    View tabView = tabb.getCustomView();
//                    tab_icon = tabView.findViewById(R.id.tab_icon);
//                    tab_icon.setImageResource(R.drawable.tick);
//
////                    FeedAdapter.COMING_FROM_FEED_ADAPTER = false;
////                    FeedAdapter.OPEN_PROFILE_FROM_FEED_ADAPTER=false;
////                    SearchAdapter.COMMING_FROM_SEARCH=false;
////                    SearchPeopleAdapter.COMING_FROMSEARCH_PEOPLE=false;
//
//                    setBadges(2);
//                    setBadgesForServerNotifications(3);
//
//
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                int pos=tab.getPosition();

                if(tab.getPosition() == 0){
                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.home_unselect);
                }
                if(tab.getPosition() == 1){
//                    tab.setIcon(R.drawable.search_gray);
                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.favourite_unselect);

                }
                if(tab.getPosition() == 2){

                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.category_unselected_icon);
                }
                if(tab.getPosition() == 3){

                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.setting_unselect);

                }
//                if(tab.getPosition() == 4){
//
//                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
//                    View tabView = tabb.getCustomView();
//                    tab_icon = tabView.findViewById(R.id.tab_icon);
//                    tab_icon.setImageResource(R.drawable.setting_unselect);
//
//                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });





    }

    void setBadges(int position){
        // for badges////

        SharedPreferences mPrefs=getSharedPreferences("Challenges_badges_count",Context.MODE_PRIVATE);
//        int menuCount=mPrefs.getInt("menu_count",0);
//        int missedCount=mPrefs.getInt("missed_count",0);
        int total_challanges_count=mPrefs.getInt("total_challanges_count",0);



        TabLayout.Tab tabb = tabLayout.getTabAt(position); // fourth tab
        View tabView = tabb.getCustomView();
        tab_badge = tabView.findViewById(R.id.tab_badge);
        badgeText = tabView.findViewById(R.id.tab_count);

        if(total_challanges_count <= 0){

            if(total_challanges_count <= 0){

                SharedPreferences.Editor editor1 = mPrefs.edit();
                editor1.putInt("total_challanges_count", 0);
                editor1.apply();

            }


            badgeText.setVisibility(View.GONE);
        }
        else {

            badgeText.setVisibility(View.VISIBLE);
            badgeText.setText(String.valueOf(total_challanges_count));

        }



        // for badges////



    }

    void setBadgesForServerNotifications(int position){
        // for badges////

        SharedPreferences mPrefs=getSharedPreferences("ServerNotification_counts",Context.MODE_PRIVATE);
        int counts=mPrefs.getInt("counts",0);



        TabLayout.Tab tabb = tabLayout.getTabAt(position); // fourth tab
        View tabView = tabb.getCustomView();
        tab_badge = tabView.findViewById(R.id.tab_badge);
        badgeText = tabView.findViewById(R.id.tab_count);

        if(counts <= 0){

            if(counts <= 0){

                SharedPreferences.Editor editor1 = mPrefs.edit();
                editor1.putInt("total_challanges_count", 0);
                editor1.apply();

            }


            badgeText.setVisibility(View.GONE);
        }
        else {

            badgeText.setVisibility(View.VISIBLE);
            badgeText.setText(String.valueOf(counts));

        }



        // for badges////



    }



    private void Initialization() {


        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);
        banner_add= findViewById(R.id.banner_add);


    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        AnalyticsTrackers.getInstance().trackScreenView("Home Tabs Activity");


    }
}
