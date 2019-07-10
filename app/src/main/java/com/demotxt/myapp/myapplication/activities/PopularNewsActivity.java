package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
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


        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            setContentView(R.layout.activity_popular_news_full);
        } else {
            setContentView(R.layout.activity_popular_news_full_smaller);
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Recieve data

        final String title  = getIntent().getExtras().getString("title");
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

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            buttonToBrowser.setTooltipText(url);
        } else {

        }



        // setting values to each view

        title_aa.setText(title);
        if(description.equals("null") || description.isEmpty()){
            description_aa.setText("Derzeit keine Beschreibung verfügbar");
        } else {
            description_aa.setText(description);
        }
        source_aa.setText("Quelle: " + source);

        if(author.equals("null")){
            author_aa.setText("Keine Informationen über den Autor");
        } else {
            author_aa.setText("Autor: " + author);
        }


        final TextView textView = findViewById(R.id.collapsingtoolbar_id_title);
        final CollapsingToolbarLayout collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar_id);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout_id);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    textView.setText(title);
                    isShow = true;
                } else if(isShow) {
                    textView.setText(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

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
        getMenuInflater().inflate(R.menu.menu_fortnite_news, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
