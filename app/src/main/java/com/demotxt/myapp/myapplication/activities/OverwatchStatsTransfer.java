package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.model.NetworkConnection;

public class OverwatchStatsTransfer extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationView bottomNav;

    private JsonObjectRequest requestFortniteNews ;
    private RequestQueue requestQueue ;
    EditText usernameInput;
    Button button;

    String[] data = {"PC", "PS4", "XBOX ONE"};
    String plattform = "null";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overwatch_stats_transfer);


        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Overwatch Stats");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        button = findViewById(R.id.owStatsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statistikenAuslesen(String.valueOf(usernameInput.getText()));
            }
        });


        usernameInput = findViewById(R.id.usernameOverwatch);
        usernameInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    statistikenAuslesen(String.valueOf(usernameInput.getText()));
                    return true;
                }
                return false;
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

        return super.onOptionsItemSelected(item);
    }




    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.newsMenu:
                            startActivity(new Intent(OverwatchStatsTransfer.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(OverwatchStatsTransfer.this, HomeActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(OverwatchStatsTransfer.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };




    public void statistikenAuslesen(String username){
        username = username.replace("#", "-");
        if(usernameInput.length()!=0 && NetworkConnection.isNetworkStatusAvialable(getApplicationContext())) {
            Intent i = new Intent(this, OverwatchStatsShow.class);
            i.putExtra("url", "https://ow-api.com/v1/stats/pc/us/" + username + "/complete");
            i.putExtra("username", username);
            startActivity(i);
        } else if(!NetworkConnection.isNetworkStatusAvialable(getApplicationContext())){
            Toast.makeText(this, "Überprüfen Sie Ihre Internetverbindung", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Bitte füllen Sie das Feld aus", Toast.LENGTH_SHORT).show();
        }
    }




}
