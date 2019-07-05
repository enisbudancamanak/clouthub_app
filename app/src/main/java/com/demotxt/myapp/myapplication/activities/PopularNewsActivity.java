package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.demotxt.myapp.myapplication.R;

public class PopularNewsActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton buttonToBrowser;
    String url;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_news_full);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Recieve data

        String title  = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("description");
        String content = getIntent().getExtras().getString("content");
        String author = getIntent().getExtras().getString("author");
        String source = getIntent().getExtras().getString("source");
        String urlImage = getIntent().getExtras().getString("urlImage");
        url = getIntent().getExtras().getString("url");



        // ini views

        TextView title_aa = findViewById(R.id.title);
        TextView description_aa = findViewById(R.id.description);
        TextView source_aa = findViewById(R.id.source) ;
        TextView author_aa = findViewById(R.id.author) ;
        ImageView thumbnail_aa = findViewById(R.id.thumbnail);
        buttonToBrowser = findViewById(R.id.buttonNewsToBrowser);
        buttonToBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        buttonToBrowser.setTooltipText(url);

        // setting values to each view

        title_aa.setText(title);
        description_aa.setText(description);
        source_aa.setText("Quelle: " + source);

        if(author.equals("null")){
            author_aa.setText("Keine Informationen über den Autor");
        } else {
            author_aa.setText("Autor: " + author);
        }


        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);


        // set image using Glide

        if(urlImage.equals("null") || urlImage.isEmpty()){
            thumbnail_aa.setImageResource(R.drawable.ic_no_picture);
        } else {
            Glide.with(this).load(urlImage).apply(requestOptions).into(thumbnail_aa);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
