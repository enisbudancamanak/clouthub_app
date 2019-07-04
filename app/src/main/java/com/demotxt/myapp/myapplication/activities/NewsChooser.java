package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.model.FortniteNews;

import java.util.List;

public class NewsChooser extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;

    private JsonObjectRequest requestFortniteNews ;
    private RequestQueue requestQueue ;
    private List<FortniteNews> lstFortniteNews;
    private RecyclerView recyclerView ;
    String urlLanguage;

    Button buttonFortniteNews;
    Button buttonPopularNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_chooser);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Wähle deine News aus!");
        setSupportActionBar(toolbar);

        buttonFortniteNews = findViewById(R.id.buttonFortniteStats);
        buttonFortniteNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkConnection.isNetworkStatusAvialable(getApplicationContext())) {
                    startActivity(new Intent(NewsChooser.this, FortniteNewsShow.class));
                } else {
                    Toast.makeText(NewsChooser.this, "Überprüfen Sie Ihre Internetverbindung", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonPopularNews = findViewById(R.id.buttonPopularNews);
        buttonPopularNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkConnection.isNetworkStatusAvialable(getApplicationContext())) {
                    startActivity(new Intent(NewsChooser.this, PopularNewsShow.class));
                } else {
                    Toast.makeText(NewsChooser.this, "Überprüfen Sie Ihre Internetverbindung", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.statsMenu:
                            startActivity(new Intent(NewsChooser.this, StatsChooser.class));
                            break;
                        case R.id.homeMenu:
                            startActivity(new Intent(NewsChooser.this, MainActivity.class));
                            break;
                    }
                    return true;
                }
            };



}
