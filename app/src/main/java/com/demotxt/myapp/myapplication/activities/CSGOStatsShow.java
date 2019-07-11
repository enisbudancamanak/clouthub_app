package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.demotxt.myapp.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CSGOStatsShow extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;

    ListView listViewNews;
    int[] statsIcons = {R.drawable.ic_kills, R.drawable.ic_deaths, R.drawable.ic_kd, R.drawable.ic_schaden, R.drawable.ic_deaths, R.drawable.ic_repeat, R.drawable.ic_wins };
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayNames = new ArrayList<>();



    private JsonObjectRequest requestCSGOStats;
    private RequestQueue requestQueue ;
    String url;
    String username;
    DecimalFormat f = new DecimalFormat("#0.00" + "");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_stats);


        Spinner spinner = findViewById(R.id.fortnite_chooser);
        spinner.setVisibility(View.GONE);

        username = getIntent().getExtras().getString("username");
        url = getIntent().getExtras().getString("url");
        System.out.println("URL: "  + url);

        try{
            jsonrequestOSUStats(url);
        } catch (Exception e) {
            jsonrequestOSUStats(username);
        }

        listViewNews = findViewById(R.id.listViewStats);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(username + "'s CSGO! Stats");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh,R.color.refresh1, R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        finish();
                        startActivity(getIntent());
                    }
                },100);
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
                startActivity(new Intent(CSGOStatsShow.this, AboutActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }




    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.newsMenu:
                            startActivity(new Intent(CSGOStatsShow.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(CSGOStatsShow.this, HomeActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(CSGOStatsShow.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };



    private void jsonrequestOSUStats(String url) {
        requestCSGOStats = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    JSONObject jsonObjectStart = response.getJSONObject("playerstats");
                    JSONArray jsonArray = jsonObjectStart.getJSONArray("stats");

                    String[] value = new String[jsonArray.length()];

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        value[i] = String.valueOf(jsonObject.getInt("value"));

                    }

                    Double kd = Double.parseDouble(value[0]) / Double.parseDouble(value[1]);

                    arrayList.add(value[0]); //Kills
                    arrayList.add(value[1]); //Deaths
                    arrayList.add(String.valueOf(f.format(kd))); //KD
                    arrayList.add(value[6]); //Damage
                    arrayList.add(value[25]); //Headshots
                    arrayList.add(value[48]); //RoundsPlayed
                    arrayList.add(value[5]); //Wins

                    arrayNames.add("Kills: ");
                    arrayNames.add("Deaths: ");
                    arrayNames.add("KD: ");
                    arrayNames.add("Damage: ");
                    arrayNames.add("Headshots: ");
                    arrayNames.add("Rounds played: ");
                    arrayNames.add("Wins: ");


                    String[] arrayValues = arrayList.toArray(new String[0]);
                    String[] marrayNames = arrayNames.toArray(new String[0]);



                    CSGOStatsShow.CustomAdapter customAdapter = new CSGOStatsShow.CustomAdapter(arrayValues, marrayNames);
                    listViewNews.setAdapter(customAdapter);


                    Toast.makeText(getApplicationContext(),"Statistiken erfolgreich ausgelesen!",Toast.LENGTH_SHORT).show();




                } catch (JSONException e) {
                    System.out.println("FEHLER");
                    e.printStackTrace();
                    String[] strArray = {""};
                    String[] arrayError = {"Keinen Spieler gefunden"};
                    statsIcons = new int[] {R.drawable.ic_kills};
                    CSGOStatsShow.CustomAdapter customAdapter = new CSGOStatsShow.CustomAdapter(strArray, arrayError);
                    listViewNews.setAdapter(customAdapter);
                    Toast.makeText(getApplicationContext(),"Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("FEHLER");
                error.printStackTrace();
                String[] strArray = {""};
                String[] arrayError = {"Keinen Spieler gefunden"};
                statsIcons = new int[] {R.drawable.ic_kills};
                CSGOStatsShow.CustomAdapter customAdapter = new CSGOStatsShow.CustomAdapter(strArray, arrayError);
                listViewNews.setAdapter(customAdapter);
                Toast.makeText(getApplicationContext(),"Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(CSGOStatsShow.this);
        requestQueue.add(requestCSGOStats) ;
    }





    private class CustomAdapter extends BaseAdapter {

        String[] valuesArray;
        String[] descriptionArray;

        public CustomAdapter(String[] strArrayParam, String[] descriptionArrayParam) {
            valuesArray = strArrayParam;
            descriptionArray = descriptionArrayParam;

        }

        @Override
        public int getCount() {
            return statsIcons.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.row_statsitem, null);

            TextView description = view1.findViewById(R.id.statsName);
            TextView name = view1.findViewById(R.id.statsValue);
            ImageView image = view1.findViewById(R.id.iconStats);

            description.setText(descriptionArray[i]);
            name.setText(valuesArray[i]);
            image.setImageResource(statsIcons[i]);

            return view1;
        }
    }
}