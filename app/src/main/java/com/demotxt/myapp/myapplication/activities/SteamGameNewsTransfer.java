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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.model.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SteamGameNewsTransfer extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;
    private JsonObjectRequest requestSteamAppNews;
    private RequestQueue requestQueue ;

    EditText usernameInput;
    Button steamGameNewsButton;

    public static final String SHARED_PREFS_STEAM = "sharedPrefsStean";
    public static final String TEXT = "";
    public static final String SWITCH = "switch";
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steam_game_news_transfer);


        checkBox = findViewById(R.id.checkBoxSteamNews);
        usernameInput = findViewById(R.id.steamAppIdInput);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Steam-Spiel News");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUsername();

        steamGameNewsButton = findViewById(R.id.steamNewsTransferButton);
        steamGameNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameInput.getText().length() != 0) {
                    jsonrequestSteamNews("https://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid=" + usernameInput.getText() + "&count=3&maxlength=300&format=json");
                } else {
                    Toast.makeText(SteamGameNewsTransfer.this, "Bitte geben Sie eine Steam APP-ID ein!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        usernameInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(usernameInput.getText().length() != 0) {
                        jsonrequestSteamNews("https://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid=" + usernameInput.getText() + "&count=3&maxlength=300&format=json");
                        return true;
                    } else {
                        Toast.makeText(SteamGameNewsTransfer.this, "Bitte geben Sie eine Steam APP-ID ein!", Toast.LENGTH_SHORT).show();
                    }
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
        switch (item.getItemId()) {
            case R.id.aboutMenu:
                startActivity(new Intent(SteamGameNewsTransfer.this, AboutActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_STEAM, MODE_PRIVATE);
        usernameInput.setText(sharedPreferences.getString(TEXT, ""));
        checkBox.setChecked(sharedPreferences.getBoolean(SWITCH, false));
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.statsMenu:
                            startActivity(new Intent(SteamGameNewsTransfer.this, StatsChooser.class));
                            break;
                        case R.id.homeMenu:
                            startActivity(new Intent(SteamGameNewsTransfer.this, HomeActivity.class));
                            break;
                        case R.id.newsMenu:
                            startActivity(new Intent(SteamGameNewsTransfer.this, NewsChooser.class));
                            break;
                    }
                    return true;
                }
            };


    private void jsonrequestSteamNews(String url) {
        if (checkBox.isChecked()) {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_STEAM, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(TEXT, String.valueOf(usernameInput.getText()));
            editor.putBoolean(SWITCH, checkBox.isChecked());
            editor.apply();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_STEAM, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(TEXT, "");
            editor.putBoolean(SWITCH, checkBox.isChecked());
            editor.apply();
        }
        requestSteamAppNews = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    ArrayList<String> arrayTitle = new ArrayList<>();
                    ArrayList<String> arrayURL = new ArrayList<>();

                    JSONObject jsonObjectStart = new JSONObject(response.toString());
                    JSONObject jsonObject1 = jsonObjectStart.getJSONObject("appnews");
                    JSONArray jsonArray = jsonObject1.getJSONArray("newsitems");


                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        arrayTitle.add(jsonObject.getString("title"));
                        arrayURL.add(jsonObject.getString("url"));
                    }

                    statistikenAuslesen(usernameInput.getText().toString(), arrayTitle, arrayURL);

                } catch (JSONException e) {
                    Toast.makeText(SteamGameNewsTransfer.this, "Spiel konnte nicht gefunden werden", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SteamGameNewsTransfer.this, "Spiel konnte nicht gefunden werden", Toast.LENGTH_LONG).show();

            }
        });
        requestQueue = Volley.newRequestQueue(SteamGameNewsTransfer.this);
        requestQueue.add(requestSteamAppNews) ;
    }


    public void statistikenAuslesen(String appId, ArrayList<String> arrayTitle, ArrayList<String> arrayURL) {
        if (usernameInput.length() != 0 && NetworkConnection.isNetworkStatusAvialable(getApplicationContext())) {
            Intent i = new Intent(this, SteamGameNewsShow.class);
            i.putExtra("urlInfo", "https://store.steampowered.com/api/appdetails?appids=" + appId);
            i.putExtra("appId", appId);
            i.putExtra("ListTitles", arrayTitle);
            i.putExtra("ListURLs", arrayURL);
            startActivity(i);
        } else if (!NetworkConnection.isNetworkStatusAvialable(getApplicationContext())) {
            Toast.makeText(this, "Überprüfen Sie Ihre Internetverbindung", Toast.LENGTH_SHORT).show();
        }

    }
}
