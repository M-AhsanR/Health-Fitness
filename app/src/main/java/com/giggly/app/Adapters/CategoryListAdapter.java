package com.giggly.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giggly.app.Activity.SelectFromSearchActivity;
import com.giggly.app.Fragments.RandomFeedFragment;
import com.giggly.app.Models.FeedListModel.CategoriesList;
import com.giggly.app.R;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<CategoriesList> cate_Arr;
    private SharedPreferences sp;
    private int a;



    public CategoryListAdapter(Context mContext, ArrayList<CategoriesList> cate_Arr) {
        this.cate_Arr = cate_Arr;
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        RandomFeedFragment.category_history_array = RandomFeedFragment.category_history_array;
        RandomFeedFragment.category_id_history = RandomFeedFragment.category_id_history;

        if (RandomFeedFragment.category_history_array == null){
            RandomFeedFragment.category_history_array = new ArrayList<>();
            RandomFeedFragment.category_id_history = new ArrayList<>();
        }

        if (!RandomFeedFragment.category_history_array.isEmpty()) {
            String last_category_name = RandomFeedFragment.category_history_array.get(RandomFeedFragment.category_history_array.size() - 1);
            String cate_id = cate_Arr.get(position).getCate_title();
            if (cate_id.equals(last_category_name)) {
                holder.hidden_layout.setVisibility(View.VISIBLE);
                holder.main_layout.setVisibility(View.GONE);
                holder.feed_category_list_text_hidden.setText(cate_Arr.get(position).getCate_title());

            }
            else {

                holder.hidden_layout.setVisibility(View.GONE);
                holder.main_layout.setVisibility(View.VISIBLE);

                holder.feed_category_list_text.setText(cate_Arr.get(position).getCate_title());

            }

        }else {
            holder.feed_category_list_text.setText(cate_Arr.get(position).getCate_title());

        }

        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RandomFeedFragment.category_history_array.add(cate_Arr.get(position).getCate_title());
                RandomFeedFragment.category_id_history.add(cate_Arr.get(position).getCate_id());

                Intent intt = new Intent(mContext, SelectFromSearchActivity.class);
                intt.putExtra("CAT_ID",cate_Arr.get(position).getCate_id());
                intt.putExtra("CAT_NAME",cate_Arr.get(position).getCate_title());
                mContext.startActivity(intt);
                if (RandomFeedFragment.category_history_array.size() != 1) {
                    ((Activity) mContext).finish();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return cate_Arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView feed_category_list_text, feed_category_list_text_hidden;
        LinearLayout hidden_layout, main_layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            feed_category_list_text = itemView.findViewById(R.id.feed_category_list_text);
            hidden_layout = itemView.findViewById(R.id.hidden_layout);
            feed_category_list_text_hidden = itemView.findViewById(R.id.feed_category_list_text_hidden);
            main_layout = itemView.findViewById(R.id.main_layout);

        }
    }
}