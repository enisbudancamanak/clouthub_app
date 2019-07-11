package com.demotxt.myapp.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.activities.PopularNewsActivity;
import com.demotxt.myapp.myapplication.activities.PopularNewsShow;
import com.demotxt.myapp.myapplication.model.PopularNews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by Aws on 11/03/2018.
 */

public class RecyclerViewAdapterPopularNews extends RecyclerView.Adapter<RecyclerViewAdapterPopularNews.MyViewHolder> {

    Date d1 = null;
    Date d2 = null;
    Date date1 = null;
    String dateInActivity;

    private Context mContext;
    private List<PopularNews> mData;
    RequestOptions option;


    public RecyclerViewAdapterPopularNews(PopularNewsShow mContext, List<PopularNews> mData) {
        this.mContext = mContext;
        this.mData = mData;

        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.popular_news_row_item, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(mContext, PopularNewsActivity.class);
                    i.putExtra("title", mData.get(viewHolder.getAdapterPosition()).getTitle());
                    i.putExtra("description", mData.get(viewHolder.getAdapterPosition()).getDescription());
                    i.putExtra("content", mData.get(viewHolder.getAdapterPosition()).getContent());
                    i.putExtra("author", mData.get(viewHolder.getAdapterPosition()).getAuthor());
                    i.putExtra("source", mData.get(viewHolder.getAdapterPosition()).getSource());
                    i.putExtra("urlImage", mData.get(viewHolder.getAdapterPosition()).getUrlImage());
                    i.putExtra("url", mData.get(viewHolder.getAdapterPosition()).getUrl());
                    i.putExtra("publishedAt", mData.get(viewHolder.getAdapterPosition()).getPublishedAt());

                    mContext.startActivity(i);



            }
        });


        return viewHolder;
    }


    //ROW-Items
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getTitle());


        // Load Image from the internet and set it into Imageview using Glide

        if (mData.get(position).getUrlImage().equals("null") || mData.get(position).getUrlImage().isEmpty()) {
            holder.img_thumbnail.setImageResource(R.drawable.ic_no_picture);
        } else {
            Glide.with(mContext).load(mData.get(position).getUrlImage()).apply(option).into(holder.img_thumbnail);
        }



        String date = mData.get(position).getPublishedAt();
        date = date.replace("T", " ");
        date = date.replace("Z", "");
        date = date.replace("-", "/");

        Date currentDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


        try {

            date1 = format.parse(date);

            d1 = new Date(date1.getTime() + TimeUnit.HOURS.toMillis(2));

            d2 = format.parse(format.format(currentDate));
            dateInActivity = date;

        } catch (Exception e) {
            e.printStackTrace();
        }

        String difference = getDifference(d1, d2);

        holder.tv_published.setText(difference);
    }






    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name ;
        TextView tv_published;
        ImageView img_thumbnail;
        LinearLayout view_container;





        public MyViewHolder(View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            tv_name = itemView.findViewById(R.id.news_name);
            tv_published = itemView.findViewById(R.id.published);
            img_thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }

    private static String getDifference(Date d1, Date d2) {
        String result = null;


        /** in milliseconds */
        long diff = d2.getTime() - d1.getTime();

        /** remove the milliseconds part */
        diff = diff / 1000;

        long days = diff / (24 * 60 * 60);
        long hours = diff / (60 * 60) % 24;
        long minutes = diff / 60 % 60;
        long seconds = diff % 60;

        result = "vor ";
        if(days >= 1){
            result += days + " d";
            return result;
        }
        if(hours >= 1) {
            result += hours + " h";
            return result;
        }
        result += minutes + " m";
        return result;
    }

}
