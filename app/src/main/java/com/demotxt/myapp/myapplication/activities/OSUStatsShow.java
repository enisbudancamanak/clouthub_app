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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.model.FortniteStats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class OSUStatsShow extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;

    ListView listViewNews;
    int[] statsIcons = {R.drawable.ic_repeat, R.drawable.ic_earth, R.drawable.ic_country, R.drawable.ic_country, R.drawable.ic_ss, R.drawable.ic_s, R.drawable.ic_repeat };
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayNames = new ArrayList<>();



    private JsonArrayRequest requestOSUStats;
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
        jsonrequestOSUStats(url);

        listViewNews = findViewById(R.id.listViewNews);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(username + "'s OSU! Stats");
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
                            startActivity(new Intent(OSUStatsShow.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(OSUStatsShow.this, HomeActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(OSUStatsShow.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };



    private void jsonrequestOSUStats(String url) {
        requestOSUStats = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {


                    JSONObject jsonObject = (JSONObject) response.get(0);


                    String accuaracy = jsonObject.getString("accuracy");
                    double accu = Double.parseDouble(accuaracy);
                    String pp_rank = jsonObject.getString("pp_rank");
                    String pp_country_rank = jsonObject.getString("pp_country_rank");
                    String country = jsonObject.getString("country");
                    String count_rank_ss = jsonObject.getString("count_rank_ss");
                    String count_rank_s = jsonObject.getString("count_rank_s");

                    //seconds to hours
                    int secondsPlayed = jsonObject.getInt("total_seconds_played");
                    int hoursPlayed = (int) TimeUnit.SECONDS.toHours(secondsPlayed);


                    arrayList.add(f.format(accu));
                    arrayList.add(pp_rank);
                    arrayList.add(pp_country_rank);
                    arrayList.add(country);
                    arrayList.add(count_rank_ss);
                    arrayList.add(count_rank_s);
                    arrayList.add("" + hoursPlayed);

                    arrayNames.add("Präzision: ");
                    arrayNames.add("PP Rank: ");
                    arrayNames.add("PP " + country + " Rank: ");
                    arrayNames.add("Land: ");
                    arrayNames.add(country + " Rank SS:");
                    arrayNames.add(country + " Rank S:");
                    arrayNames.add("Gespielte Stunden: ");

                    System.out.println("Gespielt: " + hoursPlayed);

                    String[] arrayValues = arrayList.toArray(new String[0]);
                    String[] marrayNames = arrayNames.toArray(new String[0]);



                    OSUStatsShow.CustomAdapter customAdapter = new OSUStatsShow.CustomAdapter(arrayValues, marrayNames);
                    listViewNews.setAdapter(customAdapter);


                        Toast.makeText(getApplicationContext(),"Statistiken erfolgreich ausgelesen!",Toast.LENGTH_SHORT).show();




                } catch (JSONException e) {
                    System.out.println("FEHLER");
                    e.printStackTrace();
                    String[] strArray = {""};
                    String[] arrayError = {"Keinen Spieler gefunden"};
                    statsIcons = new int[] {R.drawable.ic_kills};
                    OSUStatsShow.CustomAdapter customAdapter = new OSUStatsShow.CustomAdapter(strArray, arrayError);
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
                OSUStatsShow.CustomAdapter customAdapter = new OSUStatsShow.CustomAdapter(strArray, arrayError);
                listViewNews.setAdapter(customAdapter);
                Toast.makeText(getApplicationContext(),"Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!",Toast.LENGTH_LONG).show();
            }
        });
            requestQueue = Volley.newRequestQueue(OSUStatsShow.this);
            requestQueue.add(requestOSUStats) ;
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
