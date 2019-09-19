package com.clouthub_app.myapp.clouthub.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.clouthub_app.myapp.clouthub.activities.FortniteNewsActivity;
import com.clouthub_app.myapp.clouthub.show.FortniteNewsShow;
import com.clouthub_app.myapp.clouthub.R;
import com.clouthub_app.myapp.clouthub.model.FortniteNews;

import java.util.List;


/**
 * Created by Aws on 11/03/2018.
 */

public class RecyclerViewAdapterFortniteNews extends RecyclerView.Adapter<RecyclerViewAdapterFortniteNews.MyViewHolder> {

    private Context mContext ;
    private List<FortniteNews> mData ;
    RequestOptions option;



    public RecyclerViewAdapterFortniteNews(FortniteNewsShow mContext, List<FortniteNews> mData) {
        this.mContext = mContext;
        this.mData = mData;

        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.fortnite_news_row_item,parent,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view) ;
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, FortniteNewsActivity.class);
                i.putExtra("fortnite_titleNews",mData.get(viewHolder.getAdapterPosition()).getTitle());
                i.putExtra("fortnite_descriptionNews",mData.get(viewHolder.getAdapterPosition()).getBody());
                i.putExtra("fortnite_categorieNews","Fortnite");
                i.putExtra("fortnite_img",mData.get(viewHolder.getAdapterPosition()).getImage_url());

                mContext.startActivity(i);


            }
        });




        return viewHolder;
    }


    //ROW-Items
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getTitle());

        if(mData.get(position).getBody().isEmpty() || mData.get(position).getBody().equals("null")){
            holder.tv_description.setText("");
        } else {
            holder.tv_description.setText(mData.get(position).getBody());
        }

        // Load Image from the internet and set it into Imageview using Glide

        Glide.with(mContext).load(mData.get(position).getImage_url()).apply(option).into(holder.img_thumbnail);



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name ;
        TextView tv_description;
        ImageView img_thumbnail;
        LinearLayout view_container;





        public MyViewHolder(View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            tv_name = itemView.findViewById(R.id.news_name);
            tv_description = itemView.findViewById(R.id.description);
            img_thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }

}
