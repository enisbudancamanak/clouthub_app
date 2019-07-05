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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.model.FortniteStats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OverwatchStatsShow extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;

    ListView listViewNews;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayNames = new ArrayList<>();


    int[] statsIcons = {R.drawable.ic_rangliste, R.drawable.ic_endorsement, R.drawable.ic_wins, R.drawable.ic_rangliste, R.drawable.ic_heilung, R.drawable.ic_schaden };
    private JsonObjectRequest requestOverwatchStats;
    private RequestQueue requestQueue ;
    String url;
    String username;
    FortniteStats fortniteStats;
    DecimalFormat f = new DecimalFormat("#0.00" + "");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);


        username = getIntent().getExtras().getString("username");
        url = getIntent().getExtras().getString("url");
        System.out.println("URL: "  + url);
        jsonrequestOverwatchStats(url);

        listViewNews = findViewById(R.id.listViewNews);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(username.replace("-","#") + "'s Overwatch Stats");
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

        return super.onOptionsItemSelected(item);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.newsMenu:
                            startActivity(new Intent(OverwatchStatsShow.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(OverwatchStatsShow.this, HomeActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(OverwatchStatsShow.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };




    private void jsonrequestOverwatchStats(String url) {
        requestOverwatchStats = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response.toString());
                    JSONObject jsonObject = jsonObject1.getJSONObject("competitiveStats");

                    JSONObject topHeroesObject = jsonObject.getJSONObject("topHeroes");
                    JSONObject careerStatsObject = jsonObject.getJSONObject("careerStats");
                    JSONObject allHeroesObject = careerStatsObject.getJSONObject("allHeroes");
                    JSONObject assistsObject = allHeroesObject.getJSONObject("assists");
                    JSONObject combatObject = allHeroesObject.getJSONObject("combat");


                    String level = jsonObject1.getInt("prestige") + "" + jsonObject1.getInt("level");
                    String endorsement = String.valueOf(jsonObject1.getInt("endorsement"));
                    String gamesWon = String.valueOf(jsonObject1.getInt("gamesWon"));
                    Iterator<String> keys = topHeroesObject.keys();
                    String healing = String.valueOf(assistsObject.getString("healingDone"));
                    String damage = String.valueOf(combatObject.getString("damageDone"));


                    String topHeroes = keys.next();


                    arrayList.add(level);
                    arrayList.add(endorsement);
                    arrayList.add(gamesWon);
                    arrayList.add(topHeroes.substring(0,1).toUpperCase() + topHeroes.substring(1));
                    arrayList.add(healing);
                    arrayList.add(damage);

                    arrayNames.add("Level: ");
                    arrayNames.add("Endorsement: ");
                    arrayNames.add("Spiele gewonnen: ");
                    arrayNames.add("Bester Held: ");
                    arrayNames.add("Geheilt: ");
                    arrayNames.add("Schaden: ");

                    String[] arrayValues = arrayList.toArray(new String[0]);
                    String[] marrayNames = arrayNames.toArray(new String[0]);

                    OverwatchStatsShow.CustomAdapter customAdapter = new OverwatchStatsShow.CustomAdapter(arrayValues, marrayNames);
                    listViewNews.setAdapter(customAdapter);

                    Toast.makeText(getApplicationContext(),"Statistiken erfolgreich ausgelesen!",Toast.LENGTH_SHORT).show();



                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("FEHLER");
                    String[] strArray = {""};
                    String[] arrayError = {"Keinen Spieler gefunden"};
                    statsIcons = new int[] {R.drawable.ic_kills};
                    OverwatchStatsShow.CustomAdapter customAdapter = new OverwatchStatsShow.CustomAdapter(strArray, arrayError);
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
                OverwatchStatsShow.CustomAdapter customAdapter = new OverwatchStatsShow.CustomAdapter(strArray, arrayError);
                listViewNews.setAdapter(customAdapter);
                Toast.makeText(getApplicationContext(),"Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(OverwatchStatsShow.this);
        requestQueue.add(requestOverwatchStats) ;
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
