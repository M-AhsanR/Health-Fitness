package com.giggly.app.Adapters;

import android.content.Context;
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
import com.giggly.app.Activity.SelectCatagoryActivity;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.Models.CatagoriesApiModel.Catagories;
import com.giggly.app.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectCatagoryAdapter extends RecyclerView.Adapter<SelectCatagoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<Catagories> catagoriesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cat_img;
        TextView title;
        ImageView tick;
        LinearLayout parent_lin;
        RelativeLayout select_all_rel;
        CheckBox chkbox;

        public MyViewHolder(View view) {
            super(view);
            cat_img =  view.findViewById(R.id.cat_img);
            tick = view.findViewById(R.id.tick);
            title = (TextView) view.findViewById(R.id.title);
            parent_lin =  view.findViewById(R.id.parent_lin);
            select_all_rel =  view.findViewById(R.id.select_all_rel);
            chkbox =  view.findViewById(R.id.chkbox);

        }
    }


    public SelectCatagoryAdapter(Context mContext, List<Catagories> catagoriesList) {
        this.catagoriesList = catagoriesList;
        this.mContext = mContext;

    }

    @Override
    public SelectCatagoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_catagory_adapter, parent, false);

        return new SelectCatagoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectCatagoryAdapter.MyViewHolder holder, final int position) {


        if(catagoriesList.get(position).isSelected_by_user()){
            holder.tick.setVisibility(View.VISIBLE);

        }
        else {
            holder.tick.setVisibility(View.GONE);
        }

        if(catagoriesList.get(0).isSelect_all_chkbox()){
//            holder.chkbox.setImageResource(R.drawable.green_chk_box);
            holder.chkbox.setChecked(true);
        }
        else {
//            holder.chkbox.setImageResource(R.drawable.empty_chk_box);
            holder.chkbox.setChecked(false);
        }

        if(position == 0){
            holder.select_all_rel.setVisibility(View.VISIBLE);
        }
        else {
            holder.select_all_rel.setVisibility(View.GONE);

        }

//        if(catagoriesList.get(position).isSelect_all_chkbox()){
//            holder.chkbox.setChecked(true);
//        }else {
//            holder.chkbox.setChecked(false);
//        }

        holder.chkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                boolean chk=holder.chkbox.isChecked();

//                if(catagoriesList.get(0).isSelect_all_chkbox()){
//                }
//                else {
//                }
//
                if(!catagoriesList.get(0).isSelect_all_chkbox()) {
                    catagoriesList.get(0).setSelect_all_chkbox(true);


                    SelectCatagoryActivity.selected_catagories.clear();


                    for (int b = 0; b < catagoriesList.size(); b++) {


                        catagoriesList.get(b).setSelected_by_user(true);
                        catagoriesList.get(b).setSelect_all_chkbox(true);

                        SelectCatagoryActivity.selected_catagories.add(catagoriesList.get(b).get_id());

                    }
                    notifyDataSetChanged();
                    ((SelectCatagoryActivity)mContext).enable_done();

                }
                else {
                    catagoriesList.get(0).setSelect_all_chkbox(false);

//                    holder.chkbox.toggle();

                    for (int b = 0; b < catagoriesList.size(); b++) {

                        catagoriesList.get(b).setSelected_by_user(false);
                        catagoriesList.get(b).setSelect_all_chkbox(false);



                    }
                    SelectCatagoryActivity.selected_catagories.clear();

                    notifyDataSetChanged();
                    ((SelectCatagoryActivity)mContext).disable_done();

                }

            }
        });



        Glide
                .with( mContext )
                .load( Constants.URL.BASE_URL + catagoriesList.get(position).getCat_img() )
                .error(R.drawable.center_big_pic)
                .into( holder.cat_img );


        holder.title.setText(catagoriesList.get(position).getTitle());


        holder.parent_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selected_id=catagoriesList.get(position).get_id();

                if(!catagoriesList.get(position).isSelected_by_user()){

                    catagoriesList.get(position).setSelected_by_user(true);
                    holder.tick.setVisibility(View.VISIBLE);
                    SelectCatagoryActivity.selected_catagories.add(catagoriesList.get(position).get_id());

                    // see if all selected then all chkbox selected checked othervise not

                    if(catagoriesList.size() == SelectCatagoryActivity.selected_catagories.size()){
//                        holder.chkbox.setChecked(true);

                        catagoriesList.get(0).setSelect_all_chkbox(true);

                    }
                    else {
                        catagoriesList.get(0).setSelect_all_chkbox(false);

                        holder.chkbox.setChecked(false);
                    }

                    notifyDataSetChanged();

                    if(SelectCatagoryActivity.selected_catagories.isEmpty()){
                        ((SelectCatagoryActivity)mContext).disable_done();

                    }
                    else {
                        ((SelectCatagoryActivity)mContext).enable_done();

                    }

                }
                else {
                    catagoriesList.get(position).setSelected_by_user(false);

                    holder.tick.setVisibility(View.GONE);
                    for(int a=0;a<SelectCatagoryActivity.selected_catagories.size();a++){

                        String item=SelectCatagoryActivity.selected_catagories.get(a);

                        if(selected_id.equals(item)){

                            SelectCatagoryActivity.selected_catagories.remove(a);
                        }
                    }
                    // see if all selected then all chkbox selected checked othervise not

                    if(catagoriesList.size() == SelectCatagoryActivity.selected_catagories.size()){
//                        holder.chkbox.setChecked(true);
                        catagoriesList.get(0).setSelect_all_chkbox(true);


                    }
                    else {
//                        holder.chkbox.setChecked(false);
                        catagoriesList.get(0).setSelect_all_chkbox(false);

                    }

                    notifyDataSetChanged();

                    if(SelectCatagoryActivity.selected_catagories.isEmpty()){
                        ((SelectCatagoryActivity)mContext).disable_done();

                    }
                    else {
                        ((SelectCatagoryActivity)mContext).enable_done();

                    }
                }



            }
        });

    }


    @Override
    public int getItemCount() {
        return catagoriesList.size();
    }
}