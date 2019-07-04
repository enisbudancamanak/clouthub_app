package com.demotxt.myapp.myapplication.adapters;

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
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.activities.FortniteNewsActivity;
import com.demotxt.myapp.myapplication.activities.FortniteNewsShow;
import com.demotxt.myapp.myapplication.activities.PopularNewsShow;
import com.demotxt.myapp.myapplication.model.FortniteNews;
import com.demotxt.myapp.myapplication.model.PopularNews;

import java.util.List;


/**
 * Created by Aws on 11/03/2018.
 */

public class RecyclerViewAdapterPopularNews extends RecyclerView.Adapter<RecyclerViewAdapterPopularNews.MyViewHolder> {

    private Context mContext ;
    private List<PopularNews> mData ;
    RequestOptions option;



    public RecyclerViewAdapterPopularNews(PopularNewsShow mContext, List<PopularNews> mData) {
        this.mContext = mContext;
        this.mData = mData;

        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.fortnite_row_item,parent,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view) ;
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, FortniteNewsActivity.class);
                i.putExtra("title",mData.get(viewHolder.getAdapterPosition()).getTitle());
                i.putExtra("description",mData.get(viewHolder.getAdapterPosition()).getDescription());
                i.putExtra("content",mData.get(viewHolder.getAdapterPosition()).getContent());
                i.putExtra("author",mData.get(viewHolder.getAdapterPosition()).getAuthor());
                i.putExtra("source",mData.get(viewHolder.getAdapterPosition()).getSource());
                i.putExtra("urlImage",mData.get(viewHolder.getAdapterPosition()).getUrlImage());
                i.putExtra("url",mData.get(viewHolder.getAdapterPosition()).getUrl());




                mContext.startActivity(i);


            }
        });




        return viewHolder;
    }


    //ROW-Items
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getTitle());
        holder.tv_description.setText(mData.get(position).getDescription());

        // Load Image from the internet and set it into Imageview using Glide

        Glide.with(mContext).load(mData.get(position).getUrlImage()).apply(option).into(holder.img_thumbnail);



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
