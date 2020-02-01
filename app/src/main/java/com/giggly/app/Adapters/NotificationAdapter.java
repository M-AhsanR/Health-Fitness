package com.giggly.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.giggly.app.Activity.FeedDetailedActivity;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.Models.NotificationApiModel.Notifications;
import com.giggly.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by M on 3/25/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

private Context mContext;
private List<Notifications> notificationList;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView notification_image;
    TextView notiText, date;
    LinearLayout parent_lin;
    CircleImageView profile_img;

    public MyViewHolder(View view) {
        super(view);
        notification_image = (ImageView) view.findViewById(R.id.notification_image);

        notiText = (TextView) view.findViewById(R.id.noti_text);
        date = (TextView) view.findViewById(R.id.date);
        parent_lin =  view.findViewById(R.id.parent_lin);
        profile_img =  view.findViewById(R.id.profile_img);

    }
}


    public NotificationAdapter(Context mContext, List<Notifications> notificationList) {
        this.notificationList = notificationList;
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notifications, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       /* Album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(album.getNumOfSongs() + " songs");

        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });*/

        Glide
                .with(mContext)
                .load(Constants.URL.BASE_URL + notificationList.get(position).getNotify_from_image())
                .error(R.drawable.profile_icon)
                .into(holder.profile_img);


        String name = "<font color='#000000'>"+notificationList.get(position).getLiker_name()+ " </font>";
        String bold_name = "<b>" + name + "</b> ";


        String final_string=bold_name+notificationList.get(position).getTitle();
        holder.notiText.setText(Html.fromHtml(final_string));

        holder.notiText.setMaxLines(2);

        // set time //////////////////////////

        String tim = notificationList.get(position).getCreatedAt();
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

                holder.date.setText(final_date);


            } else {

                if (days >= 28) {
                    holder.date.setText("4 Weeks ago");

                } else {
                    if (days >= 21) {
                        holder.date.setText("3 Weeks ago");

                    } else {
                        if (days >= 14) {
                            holder.date.setText("2 Weeks ago");

                        } else {
                            if (days >= 7) {
                                holder.date.setText("1 Week ago");

                            } else {

                                if (days == 1) {
                                    holder.date.setText(days + " Day ago");
                                } else {
                                    if (days > 1) {
                                        holder.date.setText(days + " Days ago");

                                    } else {
                                        if (hours == 1) {
                                            holder.date.setText(hours + " Hour ago");
                                        } else {
                                            if (hours > 1) {
                                                holder.date.setText(hours + " Hours ago");

                                            } else {
                                                if (minutes == 1) {
                                                    holder.date.setText(minutes + " Minute ago");

                                                } else {
                                                    if (minutes > 1) {
                                                        holder.date.setText(minutes + " Minutes ago");

                                                    } else {
                                                        if (seconds < 0) {
                                                            holder.date.setText(" Now");

                                                        } else {
                                                            holder.date.setText(seconds + " Seconds ago");
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


        Glide
                .with(mContext)
                .load(Constants.URL.BASE_URL + notificationList.get(position).getFeed_img())
                .error(R.drawable.profile_icon)
                .into(holder.notification_image);


        holder.parent_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inn=new Intent(mContext, FeedDetailedActivity.class);
                inn.putExtra("KEYBOADR_STATUS", "feed_img");
                inn.putExtra("Feed_id", notificationList.get(position).getFeed_id());
                inn.putExtra("Notification", "false");


                mContext.startActivity(inn);
            }
        });






//        String one = "<font color='#000000'>Haseeb </font>";
//        String two = "<b>" + one + "</b> ";
//        int noti_size = notificationList.get(position).length();
//        if (noti_size > 62) {
//
//            String noti_data = notificationList.get(position);
//            String fina = noti_data.substring(0, 55);
//            fina = fina + ".....";
//            String three = "<font color='#666666'>" + fina + "</font>";
//            holder.notiText.setText(Html.fromHtml(two + three));
//        } else {
//            String three = "<font color='#666666'>" + notificationList.get(position) + "</font>";
//            holder.notiText.setText(Html.fromHtml(two + three));
//
//        }



    }
    public void addfeed(ArrayList<Notifications> noti_array){

//        int add_at_podition=notificationList.size();
        for(int a=0;a<noti_array.size(); a++){
            notificationList.add(noti_array.get(a));
        }
        notifyItemChanged(getItemCount());

    }



    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}