package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.demotxt.myapp.myapplication.R;

public class StatsChooser extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;


    Button buttonFortniteNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_chooser);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Wähle deine Stats aus!");
        setSupportActionBar(toolbar);

        buttonFortniteNews = findViewById(R.id.buttonFortniteStats);
        buttonFortniteNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatsChooser.this, FortniteStatsTransfer.class));
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
                        case R.id.newsMenu:
                            startActivity(new Intent(StatsChooser.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(StatsChooser.this, MainActivity.class));
                            break;
                    }
                    return true;
                }
            };
}
