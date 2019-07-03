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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.model.FortniteStats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FortniteStatsShow extends AppCompatActivity {


    Toolbar toolbar;
    BottomNavigationView bottomNav;

    private JsonObjectRequest requestFortniteNews ;
    private RequestQueue requestQueue ;
    String url;
    String username;

    EditText usernameInput;
    TextView kills;
    TextView deaths;
    TextView playedGames;
    TextView kd;

    //ListViewTutorial= https://www.youtube.com/watch?v=5Tm--PHhbJo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortnite_stats_show);


        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Fortnite Stats");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            username = getIntent().getExtras().getString("username");
            url = getIntent().getExtras().getString("url") + username;
            System.out.println("URL: " + url);
            jsonrequestFortniteStats(url);


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
                            startActivity(new Intent(FortniteStatsShow.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(FortniteStatsShow.this, MainActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(FortniteStatsShow.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };


    private void jsonrequestFortniteStats(String url) {

        requestFortniteNews = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("lifeTimeStats");

                    String[] value = new String[jsonArray.length()];


                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        value[i] = String.valueOf(jObj.getString("value"));
                    }

                    FortniteStats fortniteStats = new FortniteStats();
                    fortniteStats.setKills(value[10]);
                    fortniteStats.setWins(value[8]);
                    fortniteStats.setKd(value[11]);
                    System.out.println(value[11]);


                } catch (JSONException e) {
                    System.out.println("FEHLER");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("FEHLER");

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("TRN-Api-Key", "2fe254f7-9852-4d31-9f63-f66460ebd96a");
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(FortniteStatsShow.this);
        requestQueue.add(requestFortniteNews) ;

    }

}
