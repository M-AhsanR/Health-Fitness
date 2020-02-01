package com.giggly.app.Adapters;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.giggly.app.Activity.FeedDetailedActivity;
import com.giggly.app.Activity.FirstActivity;
import com.giggly.app.Activity.NavigationDrawerActivity;
import com.giggly.app.Activity.SelectFromSearchActivity;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Fragments.RandomFeedFragment;
import com.giggly.app.Models.FeedListModel.CategoriesList;
import com.giggly.app.Models.MyBounceInterpolator;
import com.giggly.app.Models.SearchApiModel.CatagoryFeeds;
import com.giggly.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectFromSearchGridAdapter extends RecyclerView.Adapter<SelectFromSearchGridAdapter.MyViewHolder> {

    private Context mContext;
    private List<CatagoryFeeds> list;
    ProgressDialog dialog;
    int like_img_status=0;
    int poss;
    ArrayList<CategoriesList> category_reference;
    LinearLayoutManager mLayoutManager;
    private CategoryListAdapter cate_adapter;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView search_cate_profile_img;
        TextView search_cate_profile_name, search_cate_date, like_tv,search_cate_des, search_cate_like_count, search_cate_comment_count;
        ImageView search_cate_img, search_cate_like_heart_img;
        RelativeLayout like_heart_rel, category_select_dots;
        RelativeLayout search_cate_comment_rel, search_cate_comment_count_img_rel, search_cate_comment_count_rel, search_cate_share_rel;
        RelativeLayout search_cate_comment_text_rel, search_cate_heart_count_img_rel, search_cate_heart_count_rel, search_cate_share_text_rel;
        RecyclerView search_cate_category_list;
        AdView add_one, add_two, add_three, add_four;
        LinearLayout search_cate_count_linear_parent, cate_count_below_line_lin, add_parent_linear;
        CardView add_card_view;


        public MyViewHolder(View view) {
            super(view);
            search_cate_profile_img = view.findViewById(R.id.search_cate_profile_img);
            search_cate_profile_name = view.findViewById(R.id.search_cate_profile_name);
            search_cate_date = view.findViewById(R.id.search_cate_date);
            search_cate_des = view.findViewById(R.id.search_cate_des);
            search_cate_img = view.findViewById(R.id.search_cate_img);
            search_cate_like_count = view.findViewById(R.id.search_cate_like_count);
            search_cate_comment_count = view.findViewById(R.id.search_cate_comment_count);
            like_heart_rel = view.findViewById(R.id.like_heart_rel);
            search_cate_comment_rel = view.findViewById(R.id.search_cate_comment_rel);
            search_cate_comment_text_rel = view.findViewById(R.id.search_cate_comment_text_rel);
            search_cate_share_rel = view.findViewById(R.id.search_cate_share_rel);
            search_cate_share_text_rel = view.findViewById(R.id.search_cate_share_text_rel);
            search_cate_category_list = view.findViewById(R.id.search_cate_category_list);
            add_parent_linear = view.findViewById(R.id.cate_add_parent_linear);
            add_one = view.findViewById(R.id.cate_add_one);
            add_two = view.findViewById(R.id.cate_add_two);
            add_three = view.findViewById(R.id.cate_add_three);
            add_four = view.findViewById(R.id.cate_add_four);
            search_cate_like_heart_img = view.findViewById(R.id.search_cate_like_heart_img);
            search_cate_count_linear_parent = view.findViewById(R.id.search_cate_count_linear_parent);
            cate_count_below_line_lin = view.findViewById(R.id.cate_count_below_line_lin);
            search_cate_heart_count_img_rel = view.findViewById(R.id.search_cate_heart_count_img_rel);
            search_cate_heart_count_rel = view.findViewById(R.id.search_cate_heart_count_rel);
            search_cate_comment_count_img_rel = view.findViewById(R.id.search_cate_comment_count_img_rel);
            search_cate_comment_count_rel = view.findViewById(R.id.search_cate_comment_count_rel);
            category_select_dots = view.findViewById(R.id.category_select_dots);
            like_tv = view.findViewById(R.id.like_tv);
            add_card_view = view.findViewById(R.id.add_card_view);


        }
    }


    public SelectFromSearchGridAdapter(Context mContext, List<CatagoryFeeds> list) {
        this.list = list;
        this.mContext = mContext;

    }

    @Override
    public SelectFromSearchGridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_from_search_grid_adapter, parent, false);

        return new SelectFromSearchGridAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectFromSearchGridAdapter.MyViewHolder holder, final int position) {

        //        load add

        if (  position != 0) {

            if(position == 1 || position % 6 == 0) {
//            if( position  == 1 || position == 21 || position == 28 || position == 35 || position == 39 || position == 41) {
//            if( position == 1 || position  == RandomFeedFragment.show_add_after ) {

//                RandomFeedFragment.show_add_after=RandomFeedFragment.show_add_after + 6;;
                int add_number=0;

                if(SelectFromSearchActivity.current_add.equals("one")){
                    add_number=1;

                    holder.add_parent_linear.setVisibility(View.VISIBLE);
                    holder.add_card_view.setVisibility(View.VISIBLE);

                    holder.add_one.setVisibility(View.VISIBLE);
                    holder.add_two.setVisibility(View.GONE);
                    holder.add_three.setVisibility(View.GONE);
                    holder.add_four.setVisibility(View.GONE);
                    // test id
                    MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
                    // app id original
//            MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));


                    AdRequest ad_req = new AdRequest.Builder().build();
                    holder.add_one.loadAd(ad_req);

                }
                if(RandomFeedFragment.current_add.equals("two")){
                    add_number=2;

                    holder.add_parent_linear.setVisibility(View.VISIBLE);
                    holder.add_card_view.setVisibility(View.VISIBLE);

                    holder.add_one.setVisibility(View.GONE);
                    holder.add_two.setVisibility(View.VISIBLE);
                    holder.add_three.setVisibility(View.GONE);
                    holder.add_four.setVisibility(View.GONE);
                    // test id
                    MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
                    // app id original
//            MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));


                    AdRequest ad_req = new AdRequest.Builder().build();
                    holder.add_two.loadAd(ad_req);

                }



                if(RandomFeedFragment.current_add.equals("three")){

                    add_number=3;
                    holder.add_parent_linear.setVisibility(View.VISIBLE);
                    holder.add_card_view.setVisibility(View.VISIBLE);

                    holder.add_one.setVisibility(View.GONE);
                    holder.add_two.setVisibility(View.GONE);
                    holder.add_three.setVisibility(View.VISIBLE);
                    holder.add_four.setVisibility(View.GONE);
                    // test id
                    MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
                    // app id original
//            MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));


                    AdRequest ad_req = new AdRequest.Builder().build();
                    holder.add_three.loadAd(ad_req);

                }


                if(RandomFeedFragment.current_add.equals("four")){

                    add_number=4;

                    holder.add_parent_linear.setVisibility(View.VISIBLE);
                    holder.add_card_view.setVisibility(View.VISIBLE);

                    holder.add_one.setVisibility(View.GONE);
                    holder.add_two.setVisibility(View.GONE);
                    holder.add_three.setVisibility(View.GONE);
                    holder.add_four.setVisibility(View.VISIBLE);
                    // test id
                    MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
                    // app id original
//            MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));


                    AdRequest ad_req = new AdRequest.Builder().build();
                    holder.add_four.loadAd(ad_req);

                }

                if(add_number == 1){
                    RandomFeedFragment.current_add="two";

                }


                if(add_number == 2){
                    RandomFeedFragment.current_add="three";

                }


                if(add_number == 3){
                    RandomFeedFragment.current_add="four";

                }


                if(add_number == 4){
                    RandomFeedFragment.current_add="one";

                }


            }
            else{
                holder.add_parent_linear.setVisibility(View.GONE);
                holder.add_card_view.setVisibility(View.GONE);

                holder.add_one.setVisibility(View.GONE);
                holder.add_two.setVisibility(View.GONE);
                holder.add_three.setVisibility(View.GONE);
                holder.add_four.setVisibility(View.GONE);
            }

        }
        else{
            holder.add_parent_linear.setVisibility(View.GONE);
            holder.add_card_view.setVisibility(View.GONE);
            holder.add_one.setVisibility(View.GONE);
            holder.add_two.setVisibility(View.GONE);
            holder.add_three.setVisibility(View.GONE);
            holder.add_four.setVisibility(View.GONE);
        }

//        load add

        poss=holder.getAdapterPosition();

        holder.search_cate_profile_name.setText(list.get(position).getAuth_name());

//        holder.search_cate_profile_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!list.get(position).getAuth_name().equals("Anonymous")) {
//                    Intent intent = new Intent(mContext, AuthorDetail.class);
//                    intent.putExtra("AUTH_ID", list.get(position).getAuth_id());
//                    mContext.startActivity(intent);
//                }
//            }
//        });

        Picasso.with(mContext)
                .load(Constants.URL.BASE_URL + list.get(position).getAuth_img())
                .error(R.drawable.profile_icon)
                .into(holder.search_cate_profile_img);

//        holder.search_cate_profile_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!list.get(position).getAuth_name().equals("Anonymous")) {
//                    Intent intent = new Intent(mContext, AuthorDetail.class);
//                    intent.putExtra("AUTH_ID", list.get(position).getAuth_id());
//                    mContext.startActivity(intent);
//                }
//            }
//        });

        // Category List

        category_reference = list.get(position).getCategories_ref();
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.search_cate_category_list.getItemAnimator().endAnimations();

        cate_adapter = new CategoryListAdapter(mContext, category_reference);

        holder.search_cate_category_list.setAdapter(cate_adapter);
        holder.search_cate_category_list.setLayoutManager(mLayoutManager);

        // Category List

        Glide
                .with(mContext)
                .load(Constants.URL.BASE_URL + list.get(position).getLarge_image())
                .into(holder.search_cate_img);

        holder.search_cate_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inn=new Intent(mContext, FeedDetailedActivity.class);
                inn.putExtra("KEYBOADR_STATUS", "feed_img");
                inn.putExtra("Feed_id", list.get(position).get_id());
                inn.putExtra("Notification", "false");
                mContext.startActivity(inn);
            }
        });

        //// Set Time........................

        holder.search_cate_date.setVisibility(View.GONE);

//        String tim = list.get(position).getCreatedAt();
//        String dat=tim.substring(0,10);
//        String time=tim.substring(11,19);
//        String fin=dat+" "+time;
//
//        // Set Time
//
//        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//        dff.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date date = null;
//
//        try {
//            date = dff.parse(fin);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        dff.setTimeZone(TimeZone.getDefault());
//        String formattedDatee = dff.format(date);
//        fin=formattedDatee;
//
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//
//        String final_date;
//        SimpleDateFormat ddf = new SimpleDateFormat("dd-MMM-yyyy");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        final_date = ddf.format(calendar.getTime());
//
//        try {
//            Date commentOldDate = dateFormat.parse(fin);
//
//            Calendar c = Calendar.getInstance();
//
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String formattedDate = df.format(c.getTime());
//
//            Date currentDate = dateFormat.parse(formattedDate);
//
//            long diff = currentDate.getTime() - commentOldDate.getTime();
//
//            long days = diff / (24 * 60 * 60 * 1000);
//            diff -= days * (24 * 60 * 60 * 1000);
//
//            long hours = diff / (60 * 60 * 1000);
//            diff -= hours * (60 * 60 * 1000);
//
//            long minutes = diff / (60 * 1000);
//            diff -= minutes * (60 * 1000);
//
//            long seconds = diff / 1000;
//
//
//            if (days > 30) {
//
//                holder.search_cate_date.setText(final_date);
//
//
//            } else {
//
//                if (days >= 28) {
//                    holder.search_cate_date.setText("4 Weeks ago");
//
//                } else {
//                    if (days >= 21) {
//                        holder.search_cate_date.setText("3 Weeks ago");
//
//                    } else {
//                        if (days >= 14) {
//                            holder.search_cate_date.setText("2 Weeks ago");
//
//                        } else {
//                            if (days >= 7) {
//                                holder.search_cate_date.setText("1 Week ago");
//
//                            } else {
//
//
//                                if (days == 1) {
//                                    holder.search_cate_date.setText(days + " Day ago");
//                                } else {
//                                    if (days > 1) {
//                                        holder.search_cate_date.setText(days + " Days ago");
//
//                                    } else {
//                                        if (hours == 1) {
//                                            holder.search_cate_date.setText(hours + " Hour ago");
//                                        } else {
//                                            if (hours > 1) {
//                                                holder.search_cate_date.setText(hours + " Hours ago");
//
//                                            } else {
//                                                if (minutes == 1) {
//                                                    holder.search_cate_date.setText(minutes + " Minute ago");
//
//                                                } else {
//                                                    if (minutes > 1) {
//                                                        holder.search_cate_date.setText(minutes + " Minutes ago");
//
//                                                    } else {
//                                                        if (seconds < 0) {
//                                                            holder.search_cate_date.setText(" Now");
//
//                                                        } else {
//                                                            holder.search_cate_date.setText(seconds + " Seconds ago");
//                                                        }
//                                                    }
//                                                }
//                                            }
//
//                                        }
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // set time //////////////////////////

        if(list.get(position).isLiked_by_me()){

            holder.search_cate_like_heart_img.setImageResource(R.drawable.like_selected_icon);
            holder.like_tv.setTextColor(mContext.getResources().getColor(R.color.giggly_status_bar_color));
            holder.like_tv.setText("Liked");

        }
        else{
            holder.search_cate_like_heart_img.setImageResource(R.drawable.like_unselect_icon);
            holder.like_tv.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.like_tv.setText("Like");


        }

        String like_count= String.valueOf(list.get(position).getLiked_count());
        String comment_count= String.valueOf(list.get(position).getComments_count());
        int chk_like_count,chk_comment_count;
        chk_like_count= Integer.parseInt(like_count);
        chk_comment_count= Integer.parseInt(comment_count);

        if(chk_like_count == 0 && chk_comment_count == 0){

            holder.search_cate_count_linear_parent.setVisibility(View.GONE);
            holder.cate_count_below_line_lin.setVisibility(View.GONE);


        }
        else {
            holder.search_cate_count_linear_parent.setVisibility(View.VISIBLE);
            holder.cate_count_below_line_lin.setVisibility(View.VISIBLE);

            if(chk_like_count == 0){

                holder.search_cate_heart_count_img_rel.setVisibility(View.GONE);
                holder.search_cate_heart_count_rel.setVisibility(View.GONE);
            }
            else {
                holder.search_cate_heart_count_img_rel.setVisibility(View.VISIBLE);
                holder.search_cate_heart_count_rel.setVisibility(View.VISIBLE);
                holder.search_cate_like_count.setText(like_count);

            }

            if(chk_comment_count == 0){
                holder.search_cate_comment_count_img_rel.setVisibility(View.GONE);
                holder.search_cate_comment_count_rel.setVisibility(View.GONE);

            }
            else {
                holder.search_cate_comment_count_img_rel.setVisibility(View.VISIBLE);
                holder.search_cate_comment_count_rel.setVisibility(View.VISIBLE);
                holder.search_cate_comment_count.setText(comment_count);
            }
        }

        if(list.get(position).getTitle().isEmpty()){
            holder.search_cate_des.setVisibility(View.GONE);
        }
        else {
            holder.search_cate_des.setVisibility(View.VISIBLE);
            holder.search_cate_des.setText(list.get(position).getTitle());
        }



        // like button action


        holder.like_heart_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.like_heart_rel.setEnabled(false);


                if(!list.get(position).isLiked_by_me()){


                    list.get(position).setLiked_by_me(true);
//                    bounce animation------------------------------------------------------

                    final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                    myAnim.setInterpolator(interpolator);
                    holder.search_cate_like_heart_img.startAnimation(myAnim);
                    //                    bounce animation------------------------------------------------------

                    like_img_status=1;

                    holder.search_cate_like_heart_img.setImageResource(R.drawable.like_selected_icon);
                    holder.like_tv.setTextColor(mContext.getResources().getColor(R.color.giggly_status_bar_color));
                    holder.like_tv.setText("Liked");


                    list.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(list.get(position).getLiked_count())) + 1)));
                    holder.search_cate_like_count.setText(String.valueOf(list.get(position).getLiked_count()));
                    int like_count=list.get(position).getLiked_count();
                    int comment_count=list.get(position).getComments_count();

                    if(like_count == 0 && comment_count == 0 ){
                        holder.search_cate_count_linear_parent.setVisibility(View.GONE);
                        holder.cate_count_below_line_lin.setVisibility(View.GONE);
                    }
                    else {
                        holder.search_cate_count_linear_parent.setVisibility(View.VISIBLE);
                        holder.cate_count_below_line_lin.setVisibility(View.VISIBLE);

                        if (like_count == 0) {
                            holder.search_cate_heart_count_img_rel.setVisibility(View.GONE);
                            holder.search_cate_heart_count_rel.setVisibility(View.GONE);
                        } else {
                            holder.search_cate_heart_count_img_rel.setVisibility(View.VISIBLE);
                            holder.search_cate_heart_count_rel.setVisibility(View.VISIBLE);


                        }

                    }


                    //                    hit api ---------------------------------------------------



                    SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                    String token = mPrefs.getString("USER_TOKEN", "");


                    Map<String, String> postParam = new HashMap<String, String>();


                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-sh-auth", token);

                    ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.ADD_FAVOURITS+list.get(position).get_id(), mContext, postParam, headers, new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result, String ERROR) {

                            if (ERROR.isEmpty()) {

                                try {


                                    JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                    int code = jsonObject.getInt("code");
                                    if (code == 200) {


                                        list.get(position).setLiked_by_me(true);


                                        // set counts to  subscribed feed list

//                                        for(int a = 0; a< SubscribedFeedFragment.feedList.size(); a++){
//
//                                            String id=SubscribedFeedFragment.feedList.get(a).get_id();
//                                            if(id.equals(feedList.get(position).get_id())){
//                                                FeedFragment.refresh="refresh";
//                                                SubscribedFeedFragment.feedList.get(a).setLiked_by_me(true);
//                                                SubscribedFeedFragment.feedList.get(a).setLiked_count(SubscribedFeedFragment.feedList.get(a).getLiked_count()+1);
//                                                break;
//                                            }
//
//                                        }

                                        // set counts to  subscribed feed list


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                holder.like_heart_rel.setEnabled(true);


                            } else {

                                holder.search_cate_like_heart_img.setImageResource(R.drawable.like_unselect_icon);
                                holder.like_tv.setTextColor(mContext.getResources().getColor(R.color.black));
                                holder.like_tv.setText("Like");

                                list.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(list.get(position).getLiked_count())) - 1)));
                                holder.search_cate_like_count.setText(String.valueOf(list.get(position).getLiked_count()));

                                int like_count=list.get(position).getLiked_count();

                                int comment_count=list.get(position).getComments_count();

                                if(like_count == 0 && comment_count == 0 ){
                                    holder.search_cate_count_linear_parent.setVisibility(View.GONE);
                                    holder.cate_count_below_line_lin.setVisibility(View.GONE);
                                }
                                else {
                                    holder.search_cate_count_linear_parent.setVisibility(View.VISIBLE);
                                    holder.cate_count_below_line_lin.setVisibility(View.VISIBLE);

                                    if (like_count == 0) {
                                        holder.search_cate_heart_count_img_rel.setVisibility(View.GONE);
                                        holder.search_cate_heart_count_rel.setVisibility(View.GONE);
                                    } else {
                                        holder.search_cate_heart_count_img_rel.setVisibility(View.VISIBLE);
                                        holder.search_cate_heart_count_rel.setVisibility(View.VISIBLE);


                                    }

                                }

                                holder.like_heart_rel.setEnabled(true);

                                if (ERROR.equals("401")) {

                                    SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor mEditor = mPrefs.edit();
                                    mEditor.putString("USER_EMAIL", "");
                                    mEditor.putString("USER_PASSWORD", "");
                                    mEditor.putString("USER_TOKEN", "");
                                    mEditor.putString("USER_PIC", "");
                                    mEditor.apply();
                                    Intent intt = new Intent(mContext, FirstActivity.class);
                                    mContext.startActivity(intt);
                                    ((NavigationDrawerActivity)mContext).finish();

                                } else {

                                    Toast.makeText(mContext, ERROR, Toast.LENGTH_SHORT).show();

                                }

                            }

                        }
                    });

//                    hit api ---------------------------------------------------

                }
                else{

                    list.get(position).setLiked_by_me(false);


                    like_img_status=0;

                    //                    bounce animation------------------------------------------------------
                    final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                    myAnim.setInterpolator(interpolator);
                    holder.search_cate_like_heart_img.startAnimation(myAnim);

                    //                    bounce animation------------------------------------------------------

                    holder.search_cate_like_heart_img.setImageResource(R.drawable.like_unselect_icon);
                    holder.like_tv.setTextColor(mContext.getResources().getColor(R.color.black));
                    holder.like_tv.setText("Like");


                    list.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(list.get(position).getLiked_count())) - 1)));
                    holder.search_cate_like_count.setText(String.valueOf(list.get(position).getLiked_count()));

                    int like_count=list.get(position).getLiked_count();
                    int comment_count=list.get(position).getComments_count();

                    if(like_count == 0 && comment_count == 0 ){
                        holder.search_cate_count_linear_parent.setVisibility(View.GONE);
                        holder.cate_count_below_line_lin.setVisibility(View.GONE);
                    }
                    else {
                        holder.search_cate_count_linear_parent.setVisibility(View.VISIBLE);
                        holder.cate_count_below_line_lin.setVisibility(View.VISIBLE);

                        if (like_count == 0) {
                            holder.search_cate_heart_count_img_rel.setVisibility(View.GONE);
                            holder.search_cate_heart_count_rel.setVisibility(View.GONE);
                        } else {
                            holder.search_cate_heart_count_img_rel.setVisibility(View.VISIBLE);
                            holder.search_cate_heart_count_rel.setVisibility(View.VISIBLE);


                        }
                    }

//                    hit api ---------------------------------------------------



                    SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                    String token = mPrefs.getString("USER_TOKEN", "");


                    Map<String, String> postParam = new HashMap<String, String>();


                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-sh-auth", token);

                    ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.REMOVE_FAVOURITS+list.get(position).get_id(), mContext, postParam, headers, new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result, String ERROR) {

                            if (ERROR.isEmpty()) {

                                try {


                                    JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                    int code = jsonObject.getInt("code");
                                    if (code == 200) {


                                        list.get(position).setLiked_by_me(false);

                                        // set counts to  subscribed feed list

//                                        for(int a = 0; a< SubscribedFeedFragment.feedList.size(); a++){
//
//                                            String id=SubscribedFeedFragment.feedList.get(a).get_id();
//                                            if(id.equals(feedList.get(position).get_id())){
//                                                FeedFragment.refresh="refresh";
//
//                                                SubscribedFeedFragment.feedList.get(a).setLiked_by_me(false);
//                                                SubscribedFeedFragment.feedList.get(a).setLiked_count(SubscribedFeedFragment.feedList.get(a).getLiked_count()-1);
//                                                break;
//                                            }
//
//                                        }

                                        // set counts to  subscribed feed list

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                holder.like_heart_rel.setEnabled(true);

                            } else {
                                holder.search_cate_like_heart_img.setImageResource(R.drawable.like_selected_icon);
                                holder.like_tv.setTextColor(mContext.getResources().getColor(R.color.giggly_status_bar_color));
                                holder.like_tv.setText("Liked");


                                list.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(list.get(position).getLiked_count())) + 1)));
                                holder.search_cate_like_count.setText(String.valueOf(list.get(position).getLiked_count()));
                                holder.like_heart_rel.setEnabled(true);
                                int like_count=list.get(position).getLiked_count();

                                int comment_count=list.get(position).getComments_count();

                                if(like_count == 0 && comment_count == 0 ){
                                    holder.search_cate_count_linear_parent.setVisibility(View.GONE);
                                    holder.cate_count_below_line_lin.setVisibility(View.GONE);
                                }
                                else {
                                    holder.search_cate_count_linear_parent.setVisibility(View.VISIBLE);
                                    holder.cate_count_below_line_lin.setVisibility(View.VISIBLE);

                                    if (like_count == 0) {
                                        holder.search_cate_heart_count_img_rel.setVisibility(View.GONE);
                                        holder.search_cate_heart_count_rel.setVisibility(View.GONE);
                                    } else {
                                        holder.search_cate_heart_count_img_rel.setVisibility(View.VISIBLE);
                                        holder.search_cate_heart_count_rel.setVisibility(View.VISIBLE);


                                    }
                                }
                                if (ERROR.equals("401")) {

                                    SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor mEditor = mPrefs.edit();
                                    mEditor.putString("USER_EMAIL", "");
                                    mEditor.putString("USER_PASSWORD", "");
                                    mEditor.putString("USER_TOKEN", "");
                                    mEditor.putString("USER_PIC", "");
                                    mEditor.apply();
                                    Intent intt = new Intent(mContext, FirstActivity.class);
                                    mContext.startActivity(intt);
                                    ((NavigationDrawerActivity)mContext).finish();

                                } else {
                                    Toast.makeText(mContext, ERROR, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

//                    hit api ---------------------------------------------------

                }
            }
        });

        // like button action

        // comment_rel action.........................

        holder.search_cate_comment_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent innn=new Intent(mContext, FeedDetailedActivity.class);
                innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                innn.putExtra("Feed_id", list.get(position).get_id());
                innn.putExtra("Notification", "false");

                // for refresh data in random fragment on resume
                RandomFeedFragment.positionForRefresh= String.valueOf(position);

                mContext.startActivity(innn);
            }
        });
        holder.search_cate_comment_text_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent innn=new Intent(mContext, FeedDetailedActivity.class);
                innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                innn.putExtra("Feed_id", list.get(position).get_id());
                innn.putExtra("Notification", "false");

                // for refresh data in random fragment on resume
                RandomFeedFragment.positionForRefresh= String.valueOf(position);

                mContext.startActivity(innn);
            }
        });

        // comment_rel action.........................

        // share action

        holder.search_cate_share_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                String img=Constants.URL.BASE_URL+list.get(position).getLarge_image();
                new AsyncTaskLoadImage(list.get(position).getTitle()).execute(img);
            }
        });

        holder.search_cate_share_text_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                String img=Constants.URL.BASE_URL+list.get(position).getLarge_image();
                new AsyncTaskLoadImage(list.get(position).getTitle()).execute(img);
            }
        });


        // share action

        holder.category_select_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu=new PopupMenu(mContext, holder.category_select_dots);
                menu.getMenuInflater().inflate(R.menu.feed_download_report_menu,menu.getMenu() );

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getTitle().equals("Save Image")) {

                            dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                            if (checkAndRequestPermissions()) {

                            DownLoadTask(list.get(position).getLarge_image(), list.get(position).get_id());
                        }

                        }
//                        if(item.getTitle().equals("Report a problem")){
//
//                            Intent intent = new Intent(mContext, ReportToQuote.class);
//                            intent.putExtra("FEEDID", list.get(position).get_id());
//                            mContext.startActivity(intent);
//
//
//
//                        }

                        return false;
                    }
                });
                menu.show();
            }
        });

    }

    // Download Class

    private void DownLoadTask(String image_name, String image_id){

        if (checkAndRequestPermissions()) {
            String filename = image_id + ".jpg";
            String downloadUrlOfImage = Constants.URL.BASE_URL + image_name;
            File direct =
                    new File(Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + "/" + "Positive Vibes" + "/");


            if (!direct.exists()) {
                direct.mkdir();
                Log.d("First time Msg.", "dir created for first time");
            }

            DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                            File.separator + "Positive Vibes" + File.separator + filename);

            dm.enqueue(request);
            Toast.makeText(mContext, "Download Started...", Toast.LENGTH_SHORT).show();
        }
    }
    // Download Class

    public class AsyncTaskLoadImage  extends AsyncTask<String, String, Bitmap> {
        private final static String TAG = "AsyncTaskLoadImage";
        private ImageView imageView;
        String description;
        public AsyncTaskLoadImage(String description) {
            this.description = description;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap b) {

            if(checkAndRequestPermissions()) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                        b, "Title", null);
                Uri imageUri = Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                share.putExtra(Intent.EXTRA_SUBJECT, "hellooo the image");

                share.putExtra(Intent.EXTRA_TEXT, "Giggly \n "+description+" \n \nDownload this app to get funny and entertaining jokes daily. \nhttps://play.google.com/store/apps/details?id=com.positivevibes.app ");

                mContext.startActivity(Intent.createChooser(share, "Select"));


                dialog.dismiss();

            }
        }
    }
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    private  boolean checkAndRequestPermissions() {
        dialog.dismiss();

//        int camera = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

//        if (camera != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
//        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(((SelectFromSearchActivity)mContext),listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void addfeed(ArrayList<CatagoryFeeds> feed){

        int v=getItemCount();

        for(int a=0;a<feed.size(); a++){
            list.add(feed.get(a));
        }
//            notifyItemChanged(getItemCount());
        notifyItemInserted(getItemCount());

        int fv=getItemCount();
//        notifyDataSetChanged();

    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}

