package com.giggly.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.giggly.app.Activity.HomeActivity;
import com.giggly.app.Activity.SearchMainActivity;
import com.giggly.app.Activity.SelectCatagoryActivity;
import com.giggly.app.Activity.SelectFromSearchActivity;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.Fragments.RandomFeedFragment;
import com.giggly.app.Models.CatagoriesApiModel.Catagories;
import com.giggly.app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryFragmentAdapter extends RecyclerView.Adapter<CategoryFragmentAdapter.MyViewHolder> {

    private Context mContext;
    private List<Catagories> catagoriesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cat_name_tv,cat_items_tv;
        RelativeLayout parent_rel;
        ImageView background_img;

        public MyViewHolder(View view) {
            super(view);
            parent_rel =  view.findViewById(R.id.parent_rel);
            cat_name_tv =  view.findViewById(R.id.cat_name_tv);
            cat_items_tv =  view.findViewById(R.id.cat_items_tv);
            background_img =  view.findViewById(R.id.background_img);

        }
    }


    public CategoryFragmentAdapter(Context mContext, List<Catagories> catagoriesList) {
        this.catagoriesList = catagoriesList;
        this.mContext = mContext;

    }

    @Override
    public CategoryFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_fragment_adapter_layout, parent, false);

        return new CategoryFragmentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryFragmentAdapter.MyViewHolder holder, final int position) {


        Picasso.with(mContext)
                .load(Constants.URL.BASE_URL + catagoriesList.get(position).getBackground_img())
                .error(R.drawable.profile_icon)

                .into(holder.background_img);
//

//        holder.parent_rel.setBackground();




        holder.cat_name_tv.setText(catagoriesList.get(position).getTitle());
        holder.cat_items_tv.setText(catagoriesList.get(position).getMedia_count() + " Jokes");

        holder.parent_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RandomFeedFragment.category_history_array.add(catagoriesList.get(position).getTitle());
                RandomFeedFragment.category_id_history.add(catagoriesList.get(position).get_id());


                HomeActivity activity = (HomeActivity) mContext;

                Intent intt = new Intent(mContext, SelectFromSearchActivity.class);
                intt.putExtra("CAT_ID",catagoriesList.get(position).get_id());
                intt.putExtra("CAT_NAME",catagoriesList.get(position).getTitle());
                mContext.startActivity(intt);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



            }
        });


    }


    @Override
    public int getItemCount() {
        return catagoriesList.size();
    }
}