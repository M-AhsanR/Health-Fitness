package com.giggly.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.giggly.app.AnalyticsTrackers;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.Fragments.FeedFragment;
import com.giggly.app.Models.CircleTransform;
import com.giggly.app.R;

public class NavigationDrawerActivity extends AppCompatActivity {


    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    MenuItem notification_menuitem,search_menuitem;
    TextView noti_count;
    ImageView noti_img,search_img;
    RelativeLayout search_img_rel,noti_img_rel;

//    private FloatingActionButton fab;

    // urls to load navigation header background image

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_FAVOURITE = "Favourites";
    private static final String TAG_NOTIFICATION = "Notifications";
    private static final String TAG_SETTING = "Setting";
    private static final String TAG_HELP = "Help";
    private static final String APP_TITLE = "Positivity Vibes";
    public static String CURRENT_TAG = APP_TITLE;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    String USER_DP,USER_NAME;
    Menu menuu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        toolbar =  findViewById(R.id.toolbar);
//        Toast.makeText(NavigationDrawerActivity.this,"Create",Toast.LENGTH_SHORT).show();

        setSupportActionBar(toolbar);


        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        USER_DP = mPrefs.getString("USER_DP", "");
        USER_NAME = mPrefs.getString("USER_NAME", "");

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = APP_TITLE;
            loadHomeFragment();
        }

        AnalyticsTrackers.initialize(this);
//        Log.d("analyticsID", );
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-126547086-1");
        AnalyticsTrackers.getInstance().trackScreenView("NavigationDrawer Activity");

    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */



    private void loadNavHeader() {
        // name, website

        txtName.setText(USER_NAME);
//        txtWebsite.setText("www.androidhive.info");

        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);

        imgNavHeaderBg.setImageResource(R.drawable.profile_background);






        // Loading profile image
        Glide.with(this).load(Constants.URL.BASE_URL+USER_DP)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.profile_icon)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot);
        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot);
        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot);
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
//                Fragment fragment = FeedFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                FeedFragment homeFragment = new FeedFragment();
                return homeFragment;
            case 1:
                // photos
//                ChallengeFragment photosFragment = new ChallengeFragment();
//                return photosFragment;
            case 2:

//                ChallengeFragment photoFragment = new ChallengeFragment();
//                return photoFragment;
                // movies fragment
//                MoviesFragment moviesFragment = new MoviesFragment();
//                return moviesFragment;
            case 3:
//                ChallengeFragment photosFrament = new ChallengeFragment();
//                return photosFrament;
                // notifications fragment
//                NotificationsFragment notificationsFragment = new NotificationsFragment();
//                return notificationsFragment;

            case 4:
//                ChallengeFragment photosFagment = new ChallengeFragment();
//                return photosFagment;
                // settings fragment
//                SettingsFragment settingsFragment = new SettingsFragment();
//                return settingsFragment;
            default:
                return new FeedFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
//        getSupportActionBar().setTitle("Positivity Vibes");
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;

                    case R.id.nav_home:
                        navItemIndex = 0;
//                        CURRENT_TAG = TAG_HOME;
                        CURRENT_TAG = APP_TITLE;
                        break;
                        case R.id.nav_favourite:
                        navItemIndex = 1;
//                        CURRENT_TAG = TAG_FAVOURITE;
                            startActivity(new Intent(NavigationDrawerActivity.this, FavouritesActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            break;

                    case R.id.nav_notification:
                        navItemIndex = 2;
//                        CURRENT_TAG = TAG_NOTIFICATION;
                        startActivity(new Intent(NavigationDrawerActivity.this, NotificationActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                        break;
                        case R.id.nav_search:
                        navItemIndex = 3;
//                        CURRENT_TAG = TAG_NOTIFICATION;
                        startActivity(new Intent(NavigationDrawerActivity.this, SearchMainActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                        break;

                    case R.id.nav_setting:
                        navItemIndex = 4;
//                        CURRENT_TAG = TAG_SETTING;
                        startActivity(new Intent(NavigationDrawerActivity.this, SettingActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



                        break;

                     case R.id.nav_share:
                        navItemIndex =5;
//                        CURRENT_TAG = TAG_SETTING;
                         Intent share = new Intent(Intent.ACTION_SEND);
                         share.setType("text/plain");
                         share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                         // Add data to the intent, the receiving app will decide
                         // what to do with it.

//                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post: ");
                         share.putExtra(Intent.EXTRA_TEXT, "Positive Vibes \n"+" Download this great App to bring a positive change in your life and society \n"+"\n"+"https://play.google.com/store/apps/details?id=com.positivevibes.app");

//        share.putExtra(Intent.EXTRA_TEXT, Constants.URL.BASE_URL+feed.getPhoto_url()+" \n "+feed.getFeed_description());
//                share.putExtra(Intent.EXTRA_TEXT, "hi how r u ");


                         NavigationDrawerActivity.this.startActivity(Intent.createChooser(share, "Share link!"));


                        break;

                    case R.id.nav_rate_us:
                        navItemIndex = 6;
//                        CURRENT_TAG = TAG_SETTING;

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.positivevibes.app")));


                        break;

                        default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        Toast.makeText(NavigationDrawerActivity.this,"Menu",Toast.LENGTH_SHORT).show();

//        custom layout
        menuu=menu;

            getMenuInflater().inflate(R.menu.main, menu);

            notification_menuitem=menu.findItem(R.id.notification);
            search_menuitem=menu.findItem(R.id.search);

            View actionView=notification_menuitem.getActionView();
            View actionViewSearch=search_menuitem.getActionView();

            if(actionView != null){
                noti_count=actionView.findViewById(R.id.count_tv);
                noti_img=actionView.findViewById(R.id.noti_img);
                noti_img_rel=actionView.findViewById(R.id.noti_img_rel);

                // get badge count
                SharedPreferences mPrefs=getSharedPreferences("NotificationBadgeCount",Context.MODE_PRIVATE);

                int count=mPrefs.getInt("count",0);


                // get badge count

                if(count > 0){
                    noti_count.setVisibility(View.VISIBLE);

                    String stringCount= String.valueOf(count);

                    noti_count.setText(stringCount);


                }
                else {
//                    clear count
                    noti_count.setVisibility(View.GONE);


                }


            }
            if(actionViewSearch != null){
                search_img=actionViewSearch.findViewById(R.id.search_img);
                search_img_rel=actionViewSearch.findViewById(R.id.search_img_rel);

            }


        search_img_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(NavigationDrawerActivity.this, SearchMainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                }
            });

        noti_img_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // clear badge count
                SharedPreferences mPrefs=getSharedPreferences("NotificationBadgeCount",Context.MODE_PRIVATE);


                SharedPreferences.Editor editor=mPrefs.edit();
                editor.putInt("count",0);
                editor.apply();


                // clear badge count

                startActivity(new Intent(NavigationDrawerActivity.this, NotificationActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                updateBadgeCount();
//                navItemIndex = 3;
//                CURRENT_TAG = TAG_NOTIFICATION;

//                loadHomeFragment();

//                Toast.makeText(getApplicationContext(), "Notification NEW !", Toast.LENGTH_LONG).show();

//                noti_count.setText("22");
            }
        });
//        custom layout




        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
//        if (navItemIndex == 0) {
//            getMenuInflater().inflate(R.menu.main, menu);
//        }
//
//        // when fragment is notifications, load the menu created for notifications
//        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
//        }


        // test

////        void tintMenuIcons(Menu menu)
////        {
//            MenuItem item;
//            int color;
//
//            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//
//            for(int i = 0; i < menu.size(); i++)
//            {
//                item  = menu.getItem(i);
//                color = Color.parseColor("#26a658");
//
//                DrawableCompat.setTint(item.getIcon(), color);
//            }
////        }
        // test
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }


        if (id == R.id.search) {
            Toast.makeText(getApplicationContext(), "Search !", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.notification) {
            Toast.makeText(getApplicationContext(), "Notification !", Toast.LENGTH_LONG).show();

            noti_count.setText("22");
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
//        if (navItemIndex == 0)
//            fab.show();
//        else
//            fab.hide();
    }

    public void updateBadgeCount() {



            SharedPreferences mPrefs = getSharedPreferences("NotificationBadgeCount", Context.MODE_PRIVATE);

            final int count = mPrefs.getInt("count", 0);

            //
            if (count > 0) {
                noti_count.setVisibility(View.VISIBLE);

                String stringCount = String.valueOf(count);

                noti_count.setText(stringCount);


            } else {
                noti_count.setVisibility(View.GONE);


            }



    }
    @Override
    protected void onResume() {
        super.onResume();

        AnalyticsTrackers.getInstance().trackScreenView("NavigationDrawer Activity ");


        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        USER_DP = mPrefs.getString("USER_DP", "");
        USER_NAME = mPrefs.getString("USER_NAME", "");

        txtName.setText(USER_NAME);


        // Loading profile image
        Glide.with(this).load(Constants.URL.BASE_URL+USER_DP)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.profile_icon)
                .into(imgProfile);



//        updateHotCount();

//        Toast.makeText(NavigationDrawerActivity.this,"Resume",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateBadgeCount();

//        Toast.makeText(NavigationDrawerActivity.this,"Restart",Toast.LENGTH_SHORT).show();
    }

}