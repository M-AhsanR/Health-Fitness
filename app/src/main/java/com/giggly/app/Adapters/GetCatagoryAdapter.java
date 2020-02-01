package com.giggly.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.giggly.app.Activity.SearchMainActivity;
import com.giggly.app.Activity.SelectFromSearchActivity;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.Fragments.RandomFeedFragment;
import com.giggly.app.Models.CatagoriesApiModel.Catagories;
import com.giggly.app.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetCatagoryAdapter extends RecyclerView.Adapter<GetCatagoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<Catagories> catagoriesList;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cat_img;
        TextView title;
        LinearLayout parent_lin;

        public MyViewHolder(View view) {
            super(view);
            cat_img =  view.findViewById(R.id.cat_img);
            title = (TextView) view.findViewById(R.id.title);
            parent_lin =  view.findViewById(R.id.parent_lin);

        }
    }


    public GetCatagoryAdapter(Context mContext, List<Catagories> catagoriesList) {
        this.catagoriesList = catagoriesList;
        this.mContext = mContext;

    }

    @Override
    public GetCatagoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.get_catagory_adapter, parent, false);

        return new GetCatagoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GetCatagoryAdapter.MyViewHolder holder, final int position) {



        Glide
                .with( mContext )
                .load( Constants.URL.BASE_URL + catagoriesList.get(position).getCat_img() )
                .error(R.drawable.profile_icon)
                .into( holder.cat_img );


        holder.title.setText(catagoriesList.get(position).getTitle());


        holder.parent_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RandomFeedFragment.category_history_array.add(catagoriesList.get(position).getTitle());
                RandomFeedFragment.category_id_history.add(catagoriesList.get(position).get_id());

                SearchMainActivity activity = (SearchMainActivity) mContext;

                Intent intt = new Intent(mContext, SelectFromSearchActivity.class);
                intt.putExtra("CAT_ID",catagoriesList.get(position).get_id());
                intt.putExtra("CAT_NAME",catagoriesList.get(position).getTitle());
                mContext.startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);




            }
        });

    }

    public void addfeed(ArrayList<Catagories> new_array){

        int add_at_podition=catagoriesList.size();
        for(int a=0;a<new_array.size(); a++){
            catagoriesList.add(new_array.get(a));
        }
        notifyItemChanged(getItemCount());

    }


    @Override
    public int getItemCount() {
        return catagoriesList.size();
    }
}