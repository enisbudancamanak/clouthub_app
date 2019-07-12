package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.model.OnSwipeTouchListener;

public class StatsChooser extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;


    Button buttonFortniteStats;
    Button buttonOSUStats;
    Button buttonOverwatchStats;
    Button buttonMinecraftStats;
    Button buttonCSGOStats;

    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_chooser);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Wähle deine Stats aus!");
        setSupportActionBar(toolbar);

        buttonFortniteStats = findViewById(R.id.buttonFortniteStats);
        buttonFortniteStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatsChooser.this, FortniteStatsTransfer.class));
            }
        });

        buttonOSUStats = findViewById(R.id.buttonOSUStats);
        buttonOSUStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatsChooser.this, OSUStatsTransfer.class));
            }
        });

        buttonOverwatchStats = findViewById(R.id.buttonOverwatchStats);
        buttonOverwatchStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatsChooser.this, OverwatchStatsTransfer.class));
            }
        });

        buttonMinecraftStats = findViewById(R.id.buttonMinecraftStats);
        buttonMinecraftStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatsChooser.this, MinecraftNameHistoryTransfer.class));
            }
        });

        buttonCSGOStats = findViewById(R.id.buttonCSGOStats);
        buttonCSGOStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatsChooser.this, CSGOStatsTransfer.class));
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.aboutMenu:
                startActivity(new Intent(StatsChooser.this, AboutActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        }
        return super.onOptionsItemSelected(item);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.newsMenu:
                            startActivity(new Intent(StatsChooser.this, NewsChooser.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case R.id.homeMenu:
                            startActivity(new Intent(StatsChooser.this, HomeActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                    }
                    return true;
                }
            };
}
