package com.giggly.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.giggly.app.Activity.FeedDetailedActivity;
import com.giggly.app.Activity.FirstActivity;
import com.giggly.app.Activity.NavigationDrawerActivity;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.Models.FavouritesApiModel.Favourites;
import com.giggly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {

    private Context mContext;
    private List<Favourites> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,heart;
        RelativeLayout heart_rel;

        public MyViewHolder(View view) {
            super(view);

            imageView =  view.findViewById(R.id.img);
            heart_rel = view.findViewById(R.id.heart_rel);
            heart = view.findViewById(R.id.heart);

        }
    }


    public FavouriteAdapter(Context mContext, List<Favourites> list) {
        this.list = list;
        this.mContext = mContext;

    }

    @Override
    public FavouriteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourites_adapter, parent, false);

        return new FavouriteAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavouriteAdapter.MyViewHolder holder, final int position) {


        holder.heart_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "heart rel", Toast.LENGTH_SHORT).show();


                holder.heart.setImageResource(R.drawable.favourite_unselect);



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

                                    list.remove(position);
                                    notifyDataSetChanged();
                                    holder.heart.setImageResource(R.drawable.favourite_selected_icon);





                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                            holder.heart.setImageResource(R.drawable.favourite_selected_icon);


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
        });


        Glide
                .with(mContext)
                .load(Constants.URL.BASE_URL + list.get(position).getSmall_image())
                .error(R.drawable.center_big_pic)
//                .transform(new CircleTransform(mContext))
                .into( holder.imageView);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inn=new Intent(mContext, FeedDetailedActivity.class);
                inn.putExtra("KEYBOADR_STATUS", "feed_img");
                inn.putExtra("Feed_id", list.get(position).get_id());
                inn.putExtra("Notification", "false");


                mContext.startActivity(inn);

            }
        });



    }

    public void addfeed(ArrayList<Favourites> array){

        for(int a=0;a<array.size(); a++){
            list.add(array.get(a));
        }
        notifyItemChanged(getItemCount());

    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}









//
//public class FavouriteAdapter extends BaseAdapter {
//    private Context mContext;
//    private ArrayList<Favourites> arr;
//
//
//    // Keep all Images in array
//    public Integer[] mThumbIds = {
//            R.drawable.haseeb, R.drawable.center_big_pic,
//            R.drawable.haseeb, R.drawable.center_big_pic,
//            R.drawable.haseeb, R.drawable.center_big_pic,
//            R.drawable.haseeb, R.drawable.center_big_pic,
//            R.drawable.haseeb, R.drawable.center_big_pic,
//            R.drawable.haseeb, R.drawable.center_big_pic,
//            R.drawable.haseeb, R.drawable.center_big_pic,
//            R.drawable.haseeb
//    };
//
//    // Constructor
//    public FavouriteAdapter(Context c, ArrayList<Favourites> arr){
//        this.arr = arr;
//        mContext = c;
//
//
//    }
//
//    @Override
//    public int getCount() {
//        return arr.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return arr.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//
//        if (convertView == null) {
//            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
//            convertView = layoutInflater.inflate(R.layout.favourites_adapter, null);
//
//        }
//
//        ImageView imageView = convertView.findViewById(R.id.img);
//        RelativeLayout heart_rel = convertView.findViewById(R.id.heart_rel);
//        final ImageView heart = convertView.findViewById(R.id.heart);
//        TextView no_fav = convertView.findViewById(R.id.no_fav);
//
//
//        heart_rel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(mContext, "heart rel", Toast.LENGTH_SHORT).show();
//
//
//                heart.setImageResource(R.drawable.feed_like);
//
//
//
////                    hit api ---------------------------------------------------
//
//
//
//                SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//                String token = mPrefs.getString("USER_TOKEN", "");
//
//
//                Map<String, String> postParam = new HashMap<String, String>();
//
//
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("x-sh-auth", token);
//
//                ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.REMOVE_FAVOURITS+arr.get(position).get_id(), mContext, postParam, headers, new ServerCallback() {
//                    @Override
//                    public void onSuccess(JSONObject result, String ERROR) {
//
//                        if (ERROR.isEmpty()) {
//
//                            try {
//
//
//                                JSONObject jsonObject = new JSONObject(String.valueOf(result));
//
//                                int code = jsonObject.getInt("code");
//                                if (code == 200) {
//
//                                    arr.remove(position);
//                                    notifyDataSetChanged();
//                                    heart.setImageResource(R.drawable.fill_feed_like);
//
//
//
//
//
//                                }
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        } else {
//
//                            heart.setImageResource(R.drawable.fill_feed_like);
//
//
//                            if (ERROR.equals("401")) {
//
//                                SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor mEditor = mPrefs.edit();
//                                mEditor.putString("USER_EMAIL", "");
//                                mEditor.putString("USER_PASSWORD", "");
//                                mEditor.putString("USER_TOKEN", "");
//                                mEditor.putString("USER_PIC", "");
//                                mEditor.apply();
//                                Intent intt = new Intent(mContext, FirstActivity.class);
//                                mContext.startActivity(intt);
//                                ((NavigationDrawerActivity)mContext).finish();
//
//                            } else {
//
//                                Toast.makeText(mContext, ERROR, Toast.LENGTH_SHORT).show();
//
//                            }
//
//                        }
//
//                    }
//                });
//
//
//
//
////                    hit api ---------------------------------------------------
//
//            }
//        });
//
//
//        Glide
//                .with(mContext)
//                .load(Constants.URL.BASE_URL + arr.get(position).getSmall_image())
//                .error(R.drawable.center_big_pic)
////                .transform(new CircleTransform(mContext))
//                .into(imageView);
//
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent inn=new Intent(mContext, FeedDetailedActivity.class);
//                inn.putExtra("KEYBOADR_STATUS", "feed_img");
//                inn.putExtra("Feed_id", arr.get(position).get_id());
//                inn.putExtra("Notification", "false");
//
//
//                mContext.startActivity(inn);
//
//            }
//        });
//
//
//        return convertView;
//    }
//
//}
////3329