package com.giggly.app.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.giggly.app.Activity.EditCommentActivity;
import com.giggly.app.Activity.FeedDetailedActivity;
import com.giggly.app.Activity.FirstActivity;
import com.giggly.app.Activity.NavigationDrawerActivity;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Fragments.RandomFeedFragment;
import com.giggly.app.Models.FeedListModel.Comments;
import com.giggly.app.Models.MyBounceInterpolator;
import com.giggly.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by M on 3/26/2018.
 */

public class FeedDetailAdapterWithHeader extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String et;
    private Context mContext;
    private List<Comments > commentList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    String title = "";
    String USER_ID = "";
    ProgressDialog dialog;





    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView comment_text,comment_like_count,comment_time;
//        TextView date_reply;
        ImageView comment_heart_img;

        CircleImageView comment_img;
        RelativeLayout comment_heart_rel,dots,comment_img_rel;



        public MyViewHolder(View view) {
            super(view);

            comment_img = (CircleImageView)itemView.findViewById(R.id.comment_img);

            comment_text = (TextView) view.findViewById(R.id.comment_text);
            comment_like_count = (TextView) view.findViewById(R.id.comment_like_count);
            comment_time = (TextView) view.findViewById(R.id.comment_time);
//
            comment_heart_img=(ImageView)view.findViewById(R.id.comment_heart_img);
            comment_heart_rel=(RelativeLayout)view.findViewById(R.id.comment_heart_rel);
            dots=(RelativeLayout)view.findViewById(R.id.dots);
            comment_img_rel= view.findViewById(R.id.comment_img_rel);



        }
    }

    class VHHeader extends RecyclerView.ViewHolder {

        CircleImageView feed_profile_img;
        TextView feed_description,feed_profile_name,feed_profile_date,like_count,comment_count,feed_like_tv;
        ImageView   feed_img,like_heart_img;
        RelativeLayout like_heart_rel,comment_rel,share_rel,heart_count_img_rel,heart_count_rel,comment_count_img_rel,
                comment_count_rel,share_text_rel,profile_img_rel;
        LinearLayout count_linear_parent,count_below_line_lin;

        public VHHeader(View itemView) {
            super(itemView);
             feed_profile_img = itemView.findViewById(R.id.feed_profile_img);
             feed_img =  itemView.findViewById(R.id.feed_img);
            feed_description =  itemView.findViewById(R.id.feed_description);
            feed_profile_name =  itemView.findViewById(R.id.feed_profile_name);
            feed_profile_date =  itemView.findViewById(R.id.feed_profile_date);
            like_heart_rel =  itemView.findViewById(R.id.like_heart_rel);
            like_heart_img =  itemView.findViewById(R.id.like_heart_img);
            like_count =  itemView.findViewById(R.id.like_count);
            comment_rel =  itemView.findViewById(R.id.comment_rel);
            comment_count =  itemView.findViewById(R.id.comment_count);
            share_rel =  itemView.findViewById(R.id.share_rel);
            count_linear_parent =  itemView.findViewById(R.id.count_linear_parent);
            heart_count_img_rel =  itemView.findViewById(R.id.heart_count_img_rel);
            heart_count_rel =  itemView.findViewById(R.id.heart_count_rel);
            comment_count_img_rel =  itemView.findViewById(R.id.comment_count_img_rel);
            comment_count_rel =  itemView.findViewById(R.id.comment_count_rel);
            count_below_line_lin =  itemView.findViewById(R.id.count_below_line_lin);
            share_text_rel =  itemView.findViewById(R.id.share_text_rel);
            feed_like_tv =  itemView.findViewById(R.id.feed_like_tv);
            profile_img_rel =  itemView.findViewById(R.id.profile_img_rel);



        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public FeedDetailAdapterWithHeader(Context mContext, List<Comments> commentList, String et) {
        this.mContext = mContext;
        this.commentList = commentList;
        this.et = et;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_feed_detail, parent, false);

            return new MyViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_feed_detail, parent, false);
            //inflate your layout and pass it to view holder
            return new VHHeader(itemView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof MyViewHolder) {

            SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            USER_ID = mPrefs.getString("USER_ID", "");



            if (commentList.get(position).getUser_id().equals(USER_ID)) {
                ((MyViewHolder) holder).dots.setVisibility(View.VISIBLE);
            }
            else {

                ((MyViewHolder) holder).dots.setVisibility(View.GONE);
            }




            ((MyViewHolder) holder).dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (commentList.get(position).getUser_id().equals(USER_ID)) {

                        PopupMenu menu=new PopupMenu(mContext,((MyViewHolder) holder).dots);
                        menu.getMenuInflater().inflate(R.menu.feed_edit_delete_menu,menu.getMenu() );

                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if(item.getTitle().equals("Edit")){

//                                    Toast.makeText(mContext, "edit", Toast.LENGTH_SHORT).show();


                                    Intent intt = new Intent(mContext, EditCommentActivity.class);
                                    intt.putExtra("comment",commentList.get(position).getComment());
                                    intt.putExtra("id",commentList.get(position).getComment_id());
                                    intt.putExtra("feed_id",commentList.get(position).getFeed_id());
                                    intt.putExtra("position",position);


                                    mContext.startActivity(intt);
                                    ((FeedDetailedActivity)mContext).finish();

                                }
                                if(item.getTitle().equals("Delete")){
//                                    Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();

                                    // aleert for delete

                                    AlertDialog.Builder mbuilder=new AlertDialog.Builder(mContext);
                                    View mview = LayoutInflater.from(mContext).inflate(R.layout.confirm_delete_dialog,null);
                                    RelativeLayout cancel_rel=mview.findViewById(R.id.cancel_rel);
                                    RelativeLayout yes_rel=mview.findViewById(R.id.yes_rel);
                                    mbuilder.setView(mview);
                                    final AlertDialog dialog2= mbuilder.create();
                                    dialog2.setCanceledOnTouchOutside(false);
                                    dialog2.show();

                                    cancel_rel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog2.dismiss();


                                        }
                                    });


                                    yes_rel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


//                     hit api............

                                            final SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                            String token = mPrefs.getString("USER_TOKEN", "");
                                            final String USER_PIC = mPrefs.getString("USER_PIC", "");


                                            final Map<String, String> postParam = new HashMap<String, String>();


                                            HashMap<String, String> headers = new HashMap<String, String>();
                                            headers.put("x-sh-auth", token);

//                            String feed_Id=commentList.get(position).getFeed_id();
                                            final String feed_Id = FeedDetailedActivity.Feed_Id;
                                            String comment_Id = commentList.get(position).getComment_id();


                                            ApiModelClass.GetApiResponse(Request.Method.GET,  Constants.URL.DELETE_COMMENT + feed_Id + "/" + comment_Id, mContext, postParam, headers, new ServerCallback() {
                                                @Override
                                                public void onSuccess(JSONObject result, String ERROR) {

                                                    if (ERROR.isEmpty()) {

                                                        try {


                                                            JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                                            JSONObject media = jsonObject.getJSONObject("media");
                                                            String feed__id = media.getString("_id");
                                                            int comments_count = media.getInt("comments_count");

                                                            FeedDetailedActivity.feed_detail_obj.setComments_count(comments_count);

                                                            // set counts to  subscribed feed list

//                                                            for(int a = 0; a< SubscribedFeedFragment.feedList.size(); a++){
//
//                                                                String id=SubscribedFeedFragment.feedList.get(a).get_id();
//                                                                if(id.equals(feed__id)){
//
//
//                                                                    SubscribedFeedFragment.feedList.get(a).setComments_count(comments_count);
//                                                                    break;
//                                                                }
//
//                                                            }

                                                            // set counts to  subscribed feed list

                                                            for(int a = 0; a< RandomFeedFragment.feedList.size(); a++){

                                                                String id=RandomFeedFragment.feedList.get(a).get_id();
                                                                if(id.equals(feed__id)){

                                                                    RandomFeedFragment.feedList.get(a).setComments_count(comments_count);

                                                                    break;
                                                                }

                                                            }

                                                            // set counts to  subscribed feed list

                                                            commentList.clear();
                                                            Comments enter_empty_obj_first = new Comments();
                                                            commentList.add(enter_empty_obj_first);
                                                            JSONArray comments_array = media.getJSONArray("comments");


                                                            for (int b = 0; b < comments_array.length(); b++) {

                                                                Comments comment_obj = new Comments();
                                                                JSONObject jsonObject2 = comments_array.getJSONObject(b);

                                                                String comment_id = jsonObject2.getString("_id");

                                                                try {
                                                                    JSONObject comment_user = new JSONObject(jsonObject2.getString("user"));
                                                                    String comment_full_name = comment_user.getString("full_name");
                                                                    String comment_user_id = comment_user.getString("_id");
                                                                    String dp_active_file = comment_user.getString("dp_active_file");
                                                                    comment_obj.setFull_name(comment_full_name);
                                                                    comment_obj.setUser_id(comment_user_id);
                                                                    comment_obj.setActive_dp(dp_active_file);
                                                                    comment_obj.setFeed_id(feed__id);
//                                                                    comment_obj.setActive_dp(comment_active_dp);

                                                                } catch (Exception e) {
                                                                    comment_obj.setFull_name("api ma masla ha ");
                                                                    comment_obj.setUser_id("api ma masla ha ");
//                                                                    comment_obj.setActive_dp("api ma masla ha ");

                                                                }
                                                                String comment = jsonObject2.getString("comment");
                                                                String createdAt = jsonObject2.getString("created_at");
                                                                int liked_count = jsonObject2.getInt("liked_count");
                                                                boolean liked_by_me = jsonObject2.getBoolean("liked_by_me");

                                                                comment_obj.setComment_id(comment_id);

                                                                comment_obj.setComment(comment);
                                                                comment_obj.setCreated_at(createdAt);
                                                                comment_obj.setLiked_by_me(liked_by_me);
                                                                comment_obj.setLiked_count(liked_count);


                                                                commentList.add(comment_obj);

                                                            }


//                                                            notifyDataSetChanged();
//                                                            notifyItemChanged(position);
                                                            notifyItemRemoved(position);
                                                            notifyItemRangeChanged(position,commentList.size());


                                                            dialog2.dismiss();


                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        Toast.makeText(mContext, ERROR, Toast.LENGTH_SHORT).show();


                                                    }
                                                }
                                            });
                                            // hit api--------------------

                                            dialog2.dismiss();









                                        }
                                    });

                                    // aleert for delete
                                }

                                return false;
                            }
                        });
//                        Toast.makeText(mContext,"Edit Delete Screen ",Toast.LENGTH_LONG).show();
                        menu.show();
//                        mDropdown.showAsDropDown(pop, 5, 5);


                    }

                }
            });


//            if( !commentList.get(position).getActive_dp().isEmpty()){

//            circular image ka black khatam krny k liye

            ((MyViewHolder) holder).comment_img_rel.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            Picasso.with(mContext).load(Constants.URL.BASE_URL+ commentList.get(position).getActive_dp())
                        .error(R.drawable.profile_icon)
                        .into(((MyViewHolder) holder).comment_img);

//            }
//            else {
//
//
//
//            }


            String bolt_name = "<b>"+commentList.get(position).getFull_name()+"</b>";
            String comment=String.valueOf(commentList.get(position).getComment());

            ((MyViewHolder) holder).comment_text.setText(Html.fromHtml(bolt_name+ " " +comment ));


//            ((MyViewHolder) holder).comment_text.setText(String.valueOf(commentList.get(position).getComment()));
            ((MyViewHolder) holder).comment_like_count.setText(String.valueOf(commentList.get(position).getLiked_count())+" Likes");
//            ((MyViewHolder) holder).comment_time.setText(String.valueOf(commentList.get(position).getTime()));


            // set time //////////////////////////

            //  boom   2018-04-04 08:08:51
            //  pos    2018-04-06T09:48:40.540Z

            String tim = commentList.get(position).getCreated_at();
            String dat=tim.substring(0,10);
            String time=tim.substring(11,19);
            String fin=dat+" "+time;


            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            dff.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            try {
                date = dff.parse(fin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dff.setTimeZone(TimeZone.getDefault());
            String formattedDatee = dff.format(date);
            fin=formattedDatee;


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            String final_date;
            SimpleDateFormat ddf = new SimpleDateFormat("dd-MMM-yyyy");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            final_date = ddf.format(calendar.getTime());

            try {
                Date commentOldDate = dateFormat.parse(fin);

                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                Date currentDate = dateFormat.parse(formattedDate);

                long diff = currentDate.getTime() - commentOldDate.getTime();

                long days = diff / (24 * 60 * 60 * 1000);
                diff -= days * (24 * 60 * 60 * 1000);

                long hours = diff / (60 * 60 * 1000);
                diff -= hours * (60 * 60 * 1000);

                long minutes = diff / (60 * 1000);
                diff -= minutes * (60 * 1000);

                long seconds = diff / 1000;


                if (days > 30) {

                    ((VHHeader) holder).feed_profile_date.setText(final_date);


                } else {

                    if (days >= 28) {
                        ((MyViewHolder) holder).comment_time.setText("4 Weeks ago");

                    } else {
                        if (days >= 21) {
                            ((MyViewHolder) holder).comment_time.setText("3 Weeks ago");

                        } else {
                            if (days >= 14) {
                                ((MyViewHolder) holder).comment_time.setText("2 Weeks ago");

                            } else {
                                if (days >= 7) {
                                    ((MyViewHolder) holder).comment_time.setText("1 Week ago");

                                } else {


                                    if (days == 1) {
                                        ((MyViewHolder) holder).comment_time.setText(days + " Day ago");
                                    } else {
                                        if (days > 1) {
                                            ((MyViewHolder) holder).comment_time.setText(days + " Days ago");

                                        } else {
                                            if (hours == 1) {
                                                ((MyViewHolder) holder).comment_time.setText(hours + " Hour ago");
                                            } else {
                                                if (hours > 1) {
                                                    ((MyViewHolder) holder).comment_time.setText(hours + " Hours ago");

                                                } else {
                                                    if (minutes == 1) {
                                                        ((MyViewHolder) holder).comment_time.setText(minutes + " Minute ago");

                                                    } else {
                                                        if (minutes > 1) {
                                                            ((MyViewHolder) holder).comment_time.setText(minutes + " Minutes ago");

                                                        } else {
                                                            if (seconds < 0) {
                                                                ((MyViewHolder) holder).comment_time.setText(" Now");

                                                            } else {
                                                                ((MyViewHolder) holder).comment_time.setText(seconds + " Seconds ago");
                                                            }
                                                        }
                                                    }
                                                }

                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



            // set time //////////////////////////
//            commentList.get(position).isLiked_by_me();

            if(commentList.get(position).isLiked_by_me()){

                ((MyViewHolder) holder).comment_heart_img.setImageResource(R.drawable.like_selected_icon);
                ((MyViewHolder) holder).comment_like_count.setTextColor(mContext.getResources().getColor(R.color.giggly_status_bar_color));

//                    Toast.makeText(mContext, "like", Toast.LENGTH_LONG).show();
            }
            if(!commentList.get(position).isLiked_by_me()) {
                ((MyViewHolder) holder).comment_heart_img.setImageResource(R.drawable.like_unselect_icon);
                ((MyViewHolder) holder).comment_like_count.setTextColor(mContext.getResources().getColor(R.color.black));

//                    Toast.makeText(mContext, "not like", Toast.LENGTH_LONG).show();


            }



//            / comment heart action---------------------
            ((MyViewHolder) holder).comment_heart_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                    String token = mPrefs.getString("USER_TOKEN", "");

                    ((MyViewHolder) holder).comment_heart_rel.setEnabled(false);

                    if(!commentList.get(position).isLiked_by_me()){


//                    bounce animation------------------------------------------------------

                        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                        myAnim.setInterpolator(interpolator);
                        ((MyViewHolder) holder).comment_heart_img.startAnimation(myAnim);

                        //                    bounce animation------------------------------------------------------


                        ((MyViewHolder) holder).comment_heart_img.setImageResource(R.drawable.like_selected_icon);
                        ((MyViewHolder) holder).comment_like_count.setTextColor(mContext.getResources().getColor(R.color.giggly_status_bar_color));

                        commentList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(commentList.get(position).getLiked_count())) + 1)));
                        ((MyViewHolder) holder).comment_like_count.setText(String.valueOf(commentList.get(position).getLiked_count())+" Likes");



//                    hit api ---------------------------------------------------




                        Map<String, String> postParam = new HashMap<String, String>();


                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("x-sh-auth", token);


                        ApiModelClass.GetApiResponse(Request.Method.GET,  Constants.URL.LIKE_COMMENT+FeedDetailedActivity.feed_detail_obj.getFeed_id()+"/"+commentList.get(position).getComment_id(), mContext, postParam, headers, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result, String ERROR) {

                                if (ERROR.isEmpty()) {

                                    ((MyViewHolder) holder).comment_heart_rel.setEnabled(true);

                                    try {

                                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

//                                    feedList.get(position).setLikes_count(feedList.get(position).getLikes_count()+1);
                                        commentList.get(position).setLiked_by_me(true);




                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {

                                    ((MyViewHolder) holder).comment_heart_rel.setEnabled(true);

                                    Toast.makeText(mContext, ERROR, Toast.LENGTH_SHORT).show();


                                }
                            }
                        });
                        // hit api--------------------


//                    hit api ---------------------------------------------------





                    }
                    else{



//                    like_img_status=0;

                        //                    bounce animation------------------------------------------------------
                        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                        myAnim.setInterpolator(interpolator);
                        ((MyViewHolder) holder).comment_heart_img.startAnimation(myAnim);

                        //                    bounce animation------------------------------------------------------

                        ((MyViewHolder) holder).comment_heart_img.setImageResource(R.drawable.like_unselect_icon);
                        ((MyViewHolder) holder).comment_like_count.setTextColor(mContext.getResources().getColor(R.color.black));

                        commentList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(commentList.get(position).getLiked_count())) - 1)));
                        ((MyViewHolder) holder).comment_like_count.setText(String.valueOf(commentList.get(position).getLiked_count())+" Likes");




//                    hit api ---------------------------------------------------




                        Map<String, String> postParam = new HashMap<String, String>();


                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("x-sh-auth", token);


                        ApiModelClass.GetApiResponse(Request.Method.GET,  Constants.URL.UNLIKE_COMMENT+FeedDetailedActivity.feed_detail_obj.getFeed_id()+"/"+commentList.get(position).getComment_id(), mContext, postParam, headers, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result, String ERROR) {

                                if (ERROR.isEmpty()) {

                                    ((MyViewHolder) holder).comment_heart_rel.setEnabled(true);

                                    try {

                                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

//                                    feedList.get(position).setLikes_count(feedList.get(position).getLikes_count()+1);
                                        commentList.get(position).setLiked_by_me(false);





                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {

                                    ((MyViewHolder) holder).comment_heart_rel.setEnabled(true);

                                    Toast.makeText(mContext, ERROR, Toast.LENGTH_SHORT).show();


                                }
                            }
                        });
                        // hit api--------------------




                    }




                }
            });


//            / comment heart action---------------------



        } else if (holder instanceof VHHeader) {


//            circular image ka black khatam krny k liye

            ((VHHeader) holder).profile_img_rel.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            Picasso.with(mContext).load(Constants.URL.BASE_URL+ FeedDetailedActivity.feed_detail_obj.getAuther_img())
                    .error(R.drawable.profile_icon)
                    .into(((VHHeader) holder).feed_profile_img);

            ((VHHeader) holder).feed_profile_name.setText(FeedDetailedActivity.feed_detail_obj.getAuther_name());




            if(FeedDetailedActivity.feed_detail_obj.getTitle().isEmpty()){
                ((VHHeader) holder).feed_description.setVisibility(View.GONE);
            }
            else {

                ((VHHeader) holder).feed_description.setVisibility(View.VISIBLE);
                ((VHHeader) holder).feed_description.setText(FeedDetailedActivity.feed_detail_obj.getTitle());
            }



            // set time //////////////////////////

            //  boom   2018-04-04 08:08:51
            //  pos    2018-04-06T09:48:40.540Z

            String tim = FeedDetailedActivity.feed_detail_obj.getCreatedAt();
            String dat=tim.substring(0,10);
            String time=tim.substring(11,19);
            String fin=dat+" "+time;



            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            dff.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            try {
                date = dff.parse(fin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dff.setTimeZone(TimeZone.getDefault());
            String formattedDatee = dff.format(date);
            fin=formattedDatee;


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            String final_date;
            SimpleDateFormat ddf = new SimpleDateFormat("dd-MMM-yyyy");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            final_date = ddf.format(calendar.getTime());

            try {
                Date commentOldDate = dateFormat.parse(fin);

                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                Date currentDate = dateFormat.parse(formattedDate);

                long diff = currentDate.getTime() - commentOldDate.getTime();

                long days = diff / (24 * 60 * 60 * 1000);
                diff -= days * (24 * 60 * 60 * 1000);

                long hours = diff / (60 * 60 * 1000);
                diff -= hours * (60 * 60 * 1000);

                long minutes = diff / (60 * 1000);
                diff -= minutes * (60 * 1000);

                long seconds = diff / 1000;


                if (days > 30) {

                    ((VHHeader) holder).feed_profile_date.setText(final_date);


                } else {

                    if (days >= 28) {
                        ((VHHeader) holder).feed_profile_date.setText("4 Weeks ago");

                    } else {
                        if (days >= 21) {
                            ((VHHeader) holder).feed_profile_date.setText("3 Weeks ago");

                        } else {
                            if (days >= 14) {
                                ((VHHeader) holder).feed_profile_date.setText("2 Weeks ago");

                            } else {
                                if (days >= 7) {
                                    ((VHHeader) holder).feed_profile_date.setText("1 Week ago");

                                } else {


                                    if (days == 1) {
                                        ((VHHeader) holder).feed_profile_date.setText(days + " Day ago");
                                    } else {
                                        if (days > 1) {
                                            ((VHHeader) holder).feed_profile_date.setText(days + " Days ago");

                                        } else {
                                            if (hours == 1) {
                                                ((VHHeader) holder).feed_profile_date.setText(hours + " Hour ago");
                                            } else {
                                                if (hours > 1) {
                                                    ((VHHeader) holder).feed_profile_date.setText(hours + " Hours ago");

                                                } else {
                                                    if (minutes == 1) {
                                                        ((VHHeader) holder).feed_profile_date.setText(minutes + " Minute ago");

                                                    } else {
                                                        if (minutes > 1) {
                                                            ((VHHeader) holder).feed_profile_date.setText(minutes + " Minutes ago");

                                                        } else {
                                                            if (seconds < 0) {
                                                                ((VHHeader) holder).feed_profile_date.setText(" Now");

                                                            } else {
                                                                ((VHHeader) holder).feed_profile_date.setText(seconds + " Seconds ago");
                                                            }
                                                        }
                                                    }
                                                }

                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



            // set time //////////////////////////









            try {
                String vv = Constants.URL.BASE_URL + FeedDetailedActivity.feed_detail_obj.getLarge_image();

                Glide
                        .with(mContext)
                        .load(vv)
                        .error(R.drawable.center_big_pic)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(((VHHeader) holder).feed_img);

            }
            catch (Exception e){
            }

            if(FeedDetailedActivity.feed_detail_obj.isLiked_by_me()){

                ((VHHeader) holder).like_heart_img.setImageResource(R.drawable.like_selected_icon);
                ((VHHeader) holder).feed_like_tv.setTextColor(mContext.getResources().getColor(R.color.giggly_status_bar_color));
                ((VHHeader) holder).feed_like_tv.setText("Liked");
            }
            else{


                ((VHHeader) holder).like_heart_img.setImageResource(R.drawable.like_unselect_icon);
                ((VHHeader) holder).feed_like_tv.setTextColor(mContext.getResources().getColor(R.color.black));
                ((VHHeader) holder).feed_like_tv.setText("Like");


            }



            String like_count= String.valueOf(FeedDetailedActivity.feed_detail_obj.getLiked_count());
            String comment_count= String.valueOf(FeedDetailedActivity.feed_detail_obj.getComments_count());

            int chk_like_count,chk_comment_count;
            chk_like_count= Integer.parseInt(like_count);
            chk_comment_count= Integer.parseInt(comment_count);

            if(chk_like_count == 0 && chk_comment_count == 0){

                ((VHHeader) holder).count_linear_parent.setVisibility(View.GONE);
                ((VHHeader) holder).count_below_line_lin.setVisibility(View.GONE);


            }
            else {
                ((VHHeader) holder).count_linear_parent.setVisibility(View.VISIBLE);
                ((VHHeader) holder).count_below_line_lin.setVisibility(View.VISIBLE);

                if(chk_like_count == 0){

                    ((VHHeader) holder).heart_count_img_rel.setVisibility(View.GONE);
                    ((VHHeader) holder).heart_count_rel.setVisibility(View.GONE);
                }
                else {
                    ((VHHeader) holder).heart_count_img_rel.setVisibility(View.VISIBLE);
                    ((VHHeader) holder).heart_count_rel.setVisibility(View.VISIBLE);
                    ((VHHeader) holder).like_count.setText(like_count);

                }

                if(chk_comment_count == 0){
                    ((VHHeader) holder).comment_count_img_rel.setVisibility(View.GONE);
                    ((VHHeader) holder).comment_count_rel.setVisibility(View.GONE);

                }
                else {
                    ((VHHeader) holder).comment_count_img_rel.setVisibility(View.VISIBLE);
                    ((VHHeader) holder).comment_count_rel.setVisibility(View.VISIBLE);

                    ((VHHeader) holder).comment_count.setText(comment_count);


                }



            }


            // like button action


            ((VHHeader) holder).like_heart_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ((VHHeader) holder).like_heart_rel.setEnabled(false);


                    if(!FeedDetailedActivity.feed_detail_obj.isLiked_by_me()){


                        FeedDetailedActivity.feed_detail_obj.setLiked_by_me(true);
//                    bounce animation------------------------------------------------------

                        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                        myAnim.setInterpolator(interpolator);
                        ((VHHeader) holder).like_heart_img.startAnimation(myAnim);
                        //                    bounce animation------------------------------------------------------

//                        like_img_status=1;

                        ((VHHeader) holder).like_heart_img.setImageResource(R.drawable.like_selected_icon);
                        ((VHHeader) holder).feed_like_tv.setTextColor(mContext.getResources().getColor(R.color.giggly_status_bar_color));
                        ((VHHeader) holder).feed_like_tv.setText("Liked");


                        FeedDetailedActivity.feed_detail_obj.setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(FeedDetailedActivity.feed_detail_obj.getLiked_count())) + 1)));

                        ((VHHeader) holder).like_count.setText(String.valueOf(FeedDetailedActivity.feed_detail_obj.getLiked_count()));


                        int like_count=FeedDetailedActivity.feed_detail_obj.getLiked_count();
                        int comment_count=FeedDetailedActivity.feed_detail_obj.getComments_count();

                        if(like_count == 0 && comment_count == 0 ){
                            ((VHHeader) holder).count_linear_parent.setVisibility(View.GONE);
                            ((VHHeader) holder).count_below_line_lin.setVisibility(View.GONE);
                        }
                        else {
                            ((VHHeader) holder).count_linear_parent.setVisibility(View.VISIBLE);
                            ((VHHeader) holder).count_below_line_lin.setVisibility(View.VISIBLE);

                            if (like_count == 0) {
                                ((VHHeader) holder).heart_count_img_rel.setVisibility(View.GONE);
                                ((VHHeader) holder).heart_count_rel.setVisibility(View.GONE);
                            } else {
                                ((VHHeader) holder).heart_count_img_rel.setVisibility(View.VISIBLE);
                                ((VHHeader) holder).heart_count_rel.setVisibility(View.VISIBLE);


                            }

                        }


//                    hit api ---------------------------------------------------



                        SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        String token = mPrefs.getString("USER_TOKEN", "");


                        Map<String, String> postParam = new HashMap<String, String>();


                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("x-sh-auth", token);

                        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.ADD_FAVOURITS+FeedDetailedActivity.feed_detail_obj.getFeed_id(), mContext, postParam, headers, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result, String ERROR) {

                                if (ERROR.isEmpty()) {

                                    try {


                                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                        int code = jsonObject.getInt("code");
                                        if (code == 200) {


                                            FeedDetailedActivity.feed_detail_obj.setLiked_by_me(true);


                                            // set counts to  subscribed feed list

//                                            for(int a=0;a<SubscribedFeedFragment.feedList.size();a++){
//
//                                                String id=SubscribedFeedFragment.feedList.get(a).get_id();
//                                                if(id.equals(FeedDetailedActivity.feed_detail_obj.getFeed_id())){
//
//                                                    SubscribedFeedFragment.feedList.get(a).setLiked_by_me(true);
//                                                    SubscribedFeedFragment.feedList.get(a).setLiked_count(SubscribedFeedFragment.feedList.get(a).getLiked_count()+1);
//                                                    break;
//                                                }
//
//                                            }

                                            // set counts to  subscribed feed list

                                            if (FeedDetailedActivity.notification.equals("true")) {

                                            }
                                            else {

                                            for (int a = 0; a < RandomFeedFragment.feedList.size(); a++) {

                                                String id = RandomFeedFragment.feedList.get(a).get_id();
                                                if (id.equals(FeedDetailedActivity.feed_detail_obj.getFeed_id())) {
                                                    RandomFeedFragment.feedList.get(a).setLiked_by_me(true);
                                                    RandomFeedFragment.feedList.get(a).setLiked_count(RandomFeedFragment.feedList.get(a).getLiked_count() + 1);

                                                    break;
                                                }

                                            }
                                        }

                                            // set counts to  subscribed feed list



                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    ((VHHeader) holder).like_heart_rel.setEnabled(true);


                                } else {

                                    ((VHHeader) holder).like_heart_img.setImageResource(R.drawable.like_unselect_icon);
                                    ((VHHeader) holder).feed_like_tv.setTextColor(mContext.getResources().getColor(R.color.black));
                                    ((VHHeader) holder).feed_like_tv.setText("Like");

                                    FeedDetailedActivity.feed_detail_obj.setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(FeedDetailedActivity.feed_detail_obj.getLiked_count())) - 1)));
                                    ((VHHeader) holder).like_count.setText(String.valueOf(FeedDetailedActivity.feed_detail_obj.getLiked_count()));

                                    int like_count=FeedDetailedActivity.feed_detail_obj.getLiked_count();
                                    int comment_count=FeedDetailedActivity.feed_detail_obj.getComments_count();

                                    if(like_count == 0 && comment_count == 0 ){
                                        ((VHHeader) holder).count_linear_parent.setVisibility(View.GONE);
                                        ((VHHeader) holder).count_below_line_lin.setVisibility(View.GONE);
                                    }
                                    else {
                                        ((VHHeader) holder).count_linear_parent.setVisibility(View.VISIBLE);
                                        ((VHHeader) holder).count_below_line_lin.setVisibility(View.VISIBLE);

                                        if (like_count == 0) {
                                            ((VHHeader) holder).heart_count_img_rel.setVisibility(View.GONE);
                                            ((VHHeader) holder).heart_count_rel.setVisibility(View.GONE);
                                        } else {
                                            ((VHHeader) holder).heart_count_img_rel.setVisibility(View.VISIBLE);
                                            ((VHHeader) holder).heart_count_rel.setVisibility(View.VISIBLE);


                                        }

                                    }


                                    ((VHHeader) holder).like_heart_rel.setEnabled(true);

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

                        FeedDetailedActivity.feed_detail_obj.setLiked_by_me(false);


//                        like_img_status=0;

                        //                    bounce animation------------------------------------------------------
                        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                        myAnim.setInterpolator(interpolator);
                        ((VHHeader) holder).like_heart_img.startAnimation(myAnim);

                        //                    bounce animation------------------------------------------------------

                        ((VHHeader) holder).like_heart_img.setImageResource(R.drawable.like_unselect_icon);
                        ((VHHeader) holder).feed_like_tv.setTextColor(mContext.getResources().getColor(R.color.black));
                        ((VHHeader) holder).feed_like_tv.setText("Like");

                        FeedDetailedActivity.feed_detail_obj.setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(FeedDetailedActivity.feed_detail_obj.getLiked_count())) - 1)));
                        ((VHHeader) holder).like_count.setText(String.valueOf(FeedDetailedActivity.feed_detail_obj.getLiked_count()));


                        int like_count=FeedDetailedActivity.feed_detail_obj.getLiked_count();
                        int comment_count=FeedDetailedActivity.feed_detail_obj.getComments_count();

                        if(like_count == 0 && comment_count == 0 ){
                            ((VHHeader) holder).count_linear_parent.setVisibility(View.GONE);
                            ((VHHeader) holder).count_below_line_lin.setVisibility(View.GONE);
                        }
                        else {
                            ((VHHeader) holder).count_linear_parent.setVisibility(View.VISIBLE);
                            ((VHHeader) holder).count_below_line_lin.setVisibility(View.VISIBLE);

                            if (like_count == 0) {
                                ((VHHeader) holder).heart_count_img_rel.setVisibility(View.GONE);
                                ((VHHeader) holder).heart_count_rel.setVisibility(View.GONE);
                            } else {
                                ((VHHeader) holder).heart_count_img_rel.setVisibility(View.VISIBLE);
                                ((VHHeader) holder).heart_count_rel.setVisibility(View.VISIBLE);


                            }

                        }


//                    hit api ---------------------------------------------------



                        SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        String token = mPrefs.getString("USER_TOKEN", "");


                        Map<String, String> postParam = new HashMap<String, String>();


                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("x-sh-auth", token);

                        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.REMOVE_FAVOURITS+FeedDetailedActivity.feed_detail_obj.getFeed_id(), mContext, postParam, headers, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result, String ERROR) {

                                if (ERROR.isEmpty()) {

                                    try {


                                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                        int code = jsonObject.getInt("code");
                                        if (code == 200) {


                                            FeedDetailedActivity.feed_detail_obj.setLiked_by_me(false);
                                            // set counts to  subscribed feed list

//                                            for(int a=0;a<SubscribedFeedFragment.feedList.size();a++){
//
//                                                String id=SubscribedFeedFragment.feedList.get(a).get_id();
//                                                if(id.equals(FeedDetailedActivity.feed_detail_obj.getFeed_id())){
//
//                                                    SubscribedFeedFragment.feedList.get(a).setLiked_by_me(false);
//                                                    SubscribedFeedFragment.feedList.get(a).setLiked_count(SubscribedFeedFragment.feedList.get(a).getLiked_count()-1);
//                                                    break;
//                                                }
//
//                                            }

                                            // set counts to  subscribed feed list

                                            if (FeedDetailedActivity.notification.equals("true")) {

                                            }
                                            else {

                                                for (int a = 0; a < RandomFeedFragment.feedList.size(); a++) {

                                                String id = RandomFeedFragment.feedList.get(a).get_id();
                                                if (id.equals(FeedDetailedActivity.feed_detail_obj.getFeed_id())) {
                                                    RandomFeedFragment.feedList.get(a).setLiked_by_me(false);
                                                    RandomFeedFragment.feedList.get(a).setLiked_count(RandomFeedFragment.feedList.get(a).getLiked_count() - 1);

                                                    break;
                                                }

                                            }
                                        }

                                            // set counts to  subscribed feed list



                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    ((VHHeader) holder).like_heart_rel.setEnabled(true);

                                } else {
                                    ((VHHeader) holder).like_heart_img.setImageResource(R.drawable.like_selected_icon);
                                    ((VHHeader) holder).feed_like_tv.setTextColor(mContext.getResources().getColor(R.color.giggly_status_bar_color));
                                    ((VHHeader) holder).feed_like_tv.setText("Liked");


                                    FeedDetailedActivity.feed_detail_obj.setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(FeedDetailedActivity.feed_detail_obj.getLiked_count())) + 1)));
                                    ((VHHeader) holder).like_count.setText(String.valueOf(FeedDetailedActivity.feed_detail_obj.getLiked_count()));
                                    ((VHHeader) holder).like_heart_rel.setEnabled(true);

                                    int like_count=FeedDetailedActivity.feed_detail_obj.getLiked_count();
                                    int comment_count=FeedDetailedActivity.feed_detail_obj.getComments_count();

                                    if(like_count == 0 && comment_count == 0 ){
                                        ((VHHeader) holder).count_linear_parent.setVisibility(View.GONE);
                                        ((VHHeader) holder).count_below_line_lin.setVisibility(View.GONE);
                                    }
                                    else {
                                        ((VHHeader) holder).count_linear_parent.setVisibility(View.VISIBLE);
                                        ((VHHeader) holder).count_below_line_lin.setVisibility(View.VISIBLE);

                                        if (like_count == 0) {
                                            ((VHHeader) holder).heart_count_img_rel.setVisibility(View.GONE);
                                            ((VHHeader) holder).heart_count_rel.setVisibility(View.GONE);
                                        } else {
                                            ((VHHeader) holder).heart_count_img_rel.setVisibility(View.VISIBLE);
                                            ((VHHeader) holder).heart_count_rel.setVisibility(View.VISIBLE);


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


            // share button action

            ((VHHeader) holder).share_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {





//              sahi
//                String img=Constants.URL.BASE_URL+feedList.get(position).getMedia().get(0).getMedia();
//                Uri uri=Uri.parse(img);
//
//                Bitmap b = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.app_icon );
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("image/jpeg");
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
//                        b, "Title", null);
//                Uri imageUri =  Uri.parse(path);
//                share.putExtra(Intent.EXTRA_STREAM, imageUri);
//                share.putExtra(Intent.EXTRA_SUBJECT,"hellooo the image");
//                share.putExtra(Intent.EXTRA_TEXT, "the");
//                mContext.startActivity(Intent.createChooser(share, "Select"));
                    //              sahi

                    dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);

                    String img=Constants.URL.BASE_URL+FeedDetailedActivity.feed_detail_obj.getLarge_image();
                    new FeedDetailAdapterWithHeader.AsyncTaskLoadImage(FeedDetailedActivity.feed_detail_obj.getTitle()).execute(img);


//                 old code
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("text/plain");
//                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                share.putExtra(Intent.EXTRA_TEXT, "Title Of The Post: \n"+feedList.get(position).getText()+" \n "+"Post image is : "+"\n"+Constants.URL.BASE_URL+feedList.get(position).getMedia().get(0).getMedia());
//                mContext.startActivity(Intent.createChooser(share, "Share link!"));
//                  old code

                }
            });

 ((VHHeader) holder).share_text_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {






                    dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);

                    String img=Constants.URL.BASE_URL+FeedDetailedActivity.feed_detail_obj.getLarge_image();
                    new FeedDetailAdapterWithHeader.AsyncTaskLoadImage(FeedDetailedActivity.feed_detail_obj.getTitle()).execute(img);



                }
            });


            // share button action


        }


    }

    // share work

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

    // permission
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
            ActivityCompat.requestPermissions(((FeedDetailedActivity)mContext),listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    // permission
    // share work

//    private Comment getItem(int position) {
//        return commentList.get(position - 1);
//    }


    @Override
    public int getItemCount() {

        if (commentList.size()==0) {
            return 1;
        } else
            return commentList.size() ;

//        return commentList.size();
    }


}

