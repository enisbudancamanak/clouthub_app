package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.model.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CSGOStatsTransfer extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;

    EditText usernameInput;
    Button csgoStatsButton;



    private JsonObjectRequest requestCSGOStats;
    private RequestQueue requestQueue ;

    public static final String SHARED_PREFS_CSGO ="sharedPrefsCSGO";
    public static final String TEXT = "";
    public  static final String SWITCH = "switch";
    CheckBox checkBox;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csgostats_transfer);

        checkBox = findViewById(R.id.checkBoxCSGO);
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        usernameInput = findViewById(R.id.usernameCSGO);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("CS:GO! Stats");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setUsername();


        csgoStatsButton = findViewById(R.id.csgoStatsButton);
        csgoStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonrequestSteamIDbyUsername("http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=F4095D3305932401E1A227E2EB976F74&vanityurl=" + usernameInput.getText());
            }
        });

        usernameInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    jsonrequestSteamIDbyUsername("http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=F4095D3305932401E1A227E2EB976F74&vanityurl=" + usernameInput.getText());
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
        switch (item.getItemId()){
            case R.id.aboutMenu:
                startActivity(new Intent(CSGOStatsTransfer.this, AboutActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }





    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.newsMenu:
                            startActivity(new Intent(CSGOStatsTransfer.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(CSGOStatsTransfer.this, HomeActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(CSGOStatsTransfer.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };


    public void setUsername(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_CSGO, MODE_PRIVATE);
        usernameInput.setText(sharedPreferences.getString(TEXT, ""));
        checkBox.setChecked(sharedPreferences.getBoolean(SWITCH, false));
    }


    private void jsonrequestSteamIDbyUsername(String url) {
        requestCSGOStats = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String url = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=F4095D3305932401E1A227E2EB976F74&steamids=";

                    if(!usernameInput.getText().toString().matches("[0-9]+")){
                        try {
                            JSONObject jsonObject = response.getJSONObject("response");

                            jsonrequestSteamGetName(url+jsonObject.getString("steamid"),jsonObject.getString("steamid"));

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!",Toast.LENGTH_LONG).show();

                        }
                    } else {
                        jsonrequestSteamGetName(url+ usernameInput.getText().toString(), usernameInput.getText().toString());
                    }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!",Toast.LENGTH_LONG).show();


            }
        });
        requestQueue = Volley.newRequestQueue(CSGOStatsTransfer.this);
        requestQueue.add(requestCSGOStats) ;
    }

    private void jsonrequestSteamGetName(String url, final String steamID) {
        requestCSGOStats = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                    try {
                        JSONObject jsonObjectStart = response.getJSONObject("response");
                        JSONArray jsonArray = jsonObjectStart.getJSONArray("players");

                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        statistikenAuslesen(jsonObject.getString("personaname"), steamID);


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!",Toast.LENGTH_LONG).show();
                    }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!",Toast.LENGTH_LONG).show();


            }
        });
        requestQueue = Volley.newRequestQueue(CSGOStatsTransfer.this);
        requestQueue.add(requestCSGOStats) ;
    }




    public void statistikenAuslesen(String username, String steamID){
        if(checkBox.isChecked()){
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_CSGO, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(TEXT, String.valueOf(usernameInput.getText()));
            editor.putBoolean(SWITCH, checkBox.isChecked());
            editor.apply();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_CSGO, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(TEXT, "");
            editor.putBoolean(SWITCH, checkBox.isChecked());
            editor.apply();
        }
        if(usernameInput.length()!=0 && NetworkConnection.isNetworkStatusAvialable(getApplicationContext())) {
            System.out.println(username);
            Intent i = new Intent(this, CSGOStatsShow.class);
            i.putExtra("url", "http://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=F4095D3305932401E1A227E2EB976F74&steamid=" + steamID + "&format=json%22");
            i.putExtra("steamID", steamID);
            i.putExtra("username", username);
            startActivity(i);
        } else if(!NetworkConnection.isNetworkStatusAvialable(getApplicationContext())){
            Toast.makeText(this, "Überprüfen Sie Ihre Internetverbindung", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Bitte füllen Sie die Felder aus", Toast.LENGTH_SHORT).show();
        }
    }


}

