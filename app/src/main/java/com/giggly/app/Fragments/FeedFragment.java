package com.giggly.app.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giggly.app.Activity.HomeActivity;
import com.giggly.app.Activity.NavigationDrawerActivity;
import com.giggly.app.Activity.NotificationActivity;
import com.giggly.app.Activity.SearchMainActivity;
import com.giggly.app.R;

import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;


public class FeedFragment extends Fragment {

    View rootView;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    RelativeLayout search_img_rel,noti_img_rel;
    TextView count_tv;
    ViewPagerAdapter adapter;
    public static String refresh="";


    private int[] tabIcons_white = {
            // for two tabs
//            R.drawable.subscribed_feed_white,
//            R.drawable.random_feed_white,

            // for two tabs

//            for one tab
            R.drawable.random_feed_white,

//            for one tab
    };
    private int[] tabIcons_grey = {
//            R.drawable.small_heart,
            R.drawable.subscribed_feed_gray,
            R.drawable.random_feed_gray,

    };
 private String[] tabLabels = {
         // for two tabs

//           " My Feed"," All"

         // for two tabs

//         for one tab
         "Feeds"
//         for one tab
    };




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        search_img_rel =  rootView.findViewById(R.id.search_img_rel);
        noti_img_rel =  rootView.findViewById(R.id.noti_img_rel);
        count_tv =  rootView.findViewById(R.id.count_tv);
 // test

        // test
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabTextColors(Color.parseColor("#6d6c6c"), Color.parseColor("#FFFFFF"));
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));

        setupTabIcons();



        // test
//        highLightCurrentTab(0);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {




                TextView v=tab.getCustomView().findViewById(R.id.tab_text);
                ImageView tab_img=tab.getCustomView().findViewById(R.id.tab_img);
                v.setTextColor(Color.parseColor("#FFFFFF"));
                tab_img.setImageResource(tabIcons_white[tab.getPosition()]);

//                setupTabIconsChange(tab.getPosition());

//                NavigationDrawerActivity .updateBadgeCount();
                ((NavigationDrawerActivity)getContext()).updateBadgeCount();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                TextView v=tab.getCustomView().findViewById(R.id.tab_text);
                ImageView tab_img=tab.getCustomView().findViewById(R.id.tab_img);

//        tabLayout.setTabTextColors(Color.parseColor("#6d6c6c"), Color.parseColor("#FFFFFF"));

                v.setTextColor(Color.parseColor("#a6dbbb"));
                tab_img.setImageResource(tabIcons_grey[tab.getPosition()]);

//                tab_img.setImageResource(R.drawable.random_feed_white);


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int a=10;

            }

            @Override
            public void onPageSelected(int position) {
//                highLightCurrentTab(position);
                int a=10;


//                TextView v=tabLayout.getCustomView().findViewById(R.id.tab_text);
//                tabLayout.setTabTextColors(Color.parseColor("#6d6c6c"), Color.parseColor("#FFFFFF"));
//                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
//                setupTabIconsChange(position);



            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int a=10;

            }
        });

        // test


        search_img_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                HomeActivity activity=new HomeActivity();

                Activity activity = (HomeActivity) getContext();

//                startActivity(new Intent(getContext(), SearchMainActivity.class));

                Intent intt = new Intent(getContext(), SearchMainActivity.class);
                startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


            }
        });
        noti_img_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // set badge count
                SharedPreferences mPrefs2=getContext().getSharedPreferences("Notification_badges_count",Context.MODE_PRIVATE);


                SharedPreferences.Editor editor2=mPrefs2.edit();
                editor2.putInt("count",0);
                editor2.apply();


                setNotificationBadges();
                // set badge count


                // app icon badge remove

                ShortcutBadger.removeCount(getContext()); //for 1.1.4+
//        ShortcutBadger.with(getApplicationContext()).remove();  //for 1.1.3

                // app icon badge remove



                Activity activity = (HomeActivity) getContext();

//                startActivity(new Intent(getContext(), SearchMainActivity.class));

                Intent intt = new Intent(getContext(), NotificationActivity.class);
                startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


            }
        });


        setNotificationBadges();


        return rootView;

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {

                setNotificationBadges();


//                Toast.makeText(getActivity(), "Challenge fragmen comes", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){

            }
        }
    }


    private void setupTabIcons() {

        // for two tabs

//        for (int i = 0; i < 2; i++ ) {
//
//
//            RelativeLayout tab = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
//
//            TextView tab_text = (TextView) tab.findViewById(R.id.tab_text);
//            ImageView tab_img = (ImageView) tab.findViewById(R.id.tab_img);
//            tab_text.setText(tabLabels[i]);
//
//            if(i == 0){
//                tab_text.setTextColor(Color.parseColor("#FFFFFF"));
//                tab_img.setImageResource(tabIcons_white[i]);
//
//
//            }
//            else {
//                tab_text.setTextColor(Color.parseColor("#a6dbbb"));
//                tab_img.setImageResource(tabIcons_grey[i]);
//
//
//            }
//
//
//            tabLayout.getTabAt(i).setCustomView(tab);
//
//        }

        // for two tabs

//        for one tab

        RelativeLayout tab = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);

        TextView tab_text = (TextView) tab.findViewById(R.id.tab_text);
        ImageView tab_img = (ImageView) tab.findViewById(R.id.tab_img);
        tab_text.setText(tabLabels[0]);
        tab_text.setTextColor(Color.parseColor("#FFFFFF"));
        tab_img.setImageResource(tabIcons_white[0]);
        tabLayout.getTabAt(0).setCustomView(tab);

//        for one tab


    }




    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

//        adapter.addFragment(new SubscribedFeedFragment(), "My Feed");
        adapter.addFragment(new RandomFeedFragment(), "All");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);

            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }




    }



    public void setNotificationBadges(){
        // for badges////

        SharedPreferences mPrefs = getContext(). getSharedPreferences("Notification_badges_count",Context.MODE_PRIVATE);
//        int menuCount=mPrefs.getInt("menu_count",0);
//        int missedCount=mPrefs.getInt("missed_count",0);
        int count=mPrefs.getInt("count",0);




        if(count <= 0){


                SharedPreferences.Editor editor1 = mPrefs.edit();
                editor1.putInt("count", 0);
                editor1.apply();




            count_tv.setVisibility(View.GONE);
        }
        else {

            count_tv.setVisibility(View.VISIBLE);
            count_tv.setText(String.valueOf(count));

        }



        // for badges////



    }



}


