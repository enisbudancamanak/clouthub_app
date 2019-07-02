package com.demotxt.myapp.myapplication.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.demotxt.myapp.myapplication.R;

public class FortniteNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortnite);

        // hide the default actionbar
        getSupportActionBar().hide();

        // Recieve data

        String name  = getIntent().getExtras().getString("fortnite_titleNews");
        String description = getIntent().getExtras().getString("fortnite_descriptionNews");
        String category = getIntent().getExtras().getString("fortnite_categorieNews");
        String image_url = getIntent().getExtras().getString("fortnite_img") ;

        // ini views

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);

        TextView tv_name = findViewById(R.id.aa_titleNews);
        TextView tv_description = findViewById(R.id.aa_descriptionNews);
        TextView tv_category = findViewById(R.id.aa_categorieNews) ;
        ImageView img_thumbnail = findViewById(R.id.aa_thumbnailNews);

        // setting values to each view

        tv_name.setText(name);
        tv_category.setText(category);
        tv_description.setText(description);



        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);


        // set image using Glide
        Glide.with(this).load(image_url).apply(requestOptions).into(img_thumbnail);





    }
}
