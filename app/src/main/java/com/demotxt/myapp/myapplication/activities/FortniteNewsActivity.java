package com.demotxt.myapp.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.demotxt.myapp.myapplication.R;

public class FortniteNewsActivity extends AppCompatActivity {


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortnite_news_full);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Recieve data

        String name  = getIntent().getExtras().getString("fortnite_titleNews");
        String description = getIntent().getExtras().getString("fortnite_descriptionNews");
        String image_url = getIntent().getExtras().getString("fortnite_img") ;

        // ini views

        TextView tv_name = findViewById(R.id.title);
        TextView tv_description = findViewById(R.id.description);
        ImageView img_thumbnail = findViewById(R.id.thumbnail);

        // setting values to each view

        tv_name.setText(name);
        tv_description.setText(description);



        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);


        // set image using Glide
        Glide.with(this).load(image_url).apply(requestOptions).into(img_thumbnail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fortnite_news, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
