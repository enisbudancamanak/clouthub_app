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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.model.NetworkConnection;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class FortniteStatsTransfer extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;

    private JsonObjectRequest requestFortniteNews ;
    private RequestQueue requestQueue ;
    EditText usernameInput;
    Button fortniteStatsButton;

    String[] data = {"PC", "PS4", "XBOX ONE"};
    String plattform = "null";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortnite_stats_transfer);


        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Fortnite Stats");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, data);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);

        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    plattform = "pc";
                } else if(position == 1) {
                    plattform = "psn";
                } else if(position == 2) {
                    plattform = "xbl";
                }

                System.out.println("Position: " + position + " " + plattform);

            }
        });


        fortniteStatsButton = findViewById(R.id.fortniteStatsButton);
        fortniteStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statistikenAuslesen(String.valueOf(usernameInput.getText()));
            }
        });


        usernameInput = findViewById(R.id.usernameFortnite);
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
                            startActivity(new Intent(FortniteStatsTransfer.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(FortniteStatsTransfer.this, HomeActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(FortniteStatsTransfer.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };




    public void statistikenAuslesen(String username){
        if(usernameInput.length()!=0 && !plattform.equals("null") && NetworkConnection.isNetworkStatusAvialable(getApplicationContext())) {
            System.out.println(username);
            Intent i = new Intent(this, FortniteStatsShow.class);
            i.putExtra("url", "https://api.fortnitetracker.com/v1/profile/" + plattform + "/" + username);
            i.putExtra("username", username);
            i.putExtra("plattform", plattform);
            startActivity(i);
        } else if(!NetworkConnection.isNetworkStatusAvialable(getApplicationContext())){
            Toast.makeText(this, "Überprüfen Sie Ihre Internetverbindung", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Bitte füllen Sie die Felder aus", Toast.LENGTH_SHORT).show();
        }
    }




}
