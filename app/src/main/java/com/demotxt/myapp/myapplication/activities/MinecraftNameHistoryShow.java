package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.demotxt.myapp.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MinecraftNameHistoryShow extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;

    private JsonArrayRequest requestSteamAppNews;
    private RequestQueue requestQueue;

    ListView listViewNews;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayNames = new ArrayList<>();

    String url;
    String uuid;
    String username;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_stats);

        Spinner spinner = findViewById(R.id.fortnite_chooser);
        spinner.setVisibility(View.GONE);


        uuid = getIntent().getExtras().getString("uuid");
        url = getIntent().getExtras().getString("url");
        username = getIntent().getExtras().getString("username");


        jsonrequestSteamNews(url);

        listViewNews = findViewById(R.id.listViewStats);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color.refresh2);
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
                }, 100);
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
                startActivity(new Intent(MinecraftNameHistoryShow.this, AboutActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.newsMenu:
                            startActivity(new Intent(MinecraftNameHistoryShow.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(MinecraftNameHistoryShow.this, HomeActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(MinecraftNameHistoryShow.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };


    private void jsonrequestSteamNews(String url) {
        requestSteamAppNews = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    String usernameFromJSON;
                    String changedToAt;
                    int[] statsIcons = new int[response.length()];


                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        usernameFromJSON = jsonObject.getString("name");

                        try {
                            changedToAt = getDate(jsonObject.getLong("changedToAt"), "dd/MM/yyyy");
                        } catch (Exception e) {
                            changedToAt = "Kein Datum bekannt";
                        }

                        arrayNames.add(usernameFromJSON);
                        arrayList.add(changedToAt);
                    }

                    for(int in = response.length()-1; in < response.length(); in++) {
                        JSONObject jsonObject = response.getJSONObject(in);

                        toolbar.setSubtitle(jsonObject.getString("name") + "'s Name history");
                    }


                    String[] arrayValues = arrayList.toArray(new String[0]);
                    String[] marrayNames = arrayNames.toArray(new String[0]);


                    MinecraftNameHistoryShow.CustomAdapter customAdapter = new MinecraftNameHistoryShow.CustomAdapter(arrayValues, marrayNames, statsIcons, "success");
                    listViewNews.setAdapter(customAdapter);


                    Toast.makeText(getApplicationContext(), "Statistiken erfolgreich ausgelesen!", Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    System.out.println("FEHLER");
                    e.printStackTrace();
                    String[] strArray = {""};
                    String[] arrayError = {"Kein Spiel gefunden"};
                    int[] statsIcons = {R.drawable.ic_kills};
                    MinecraftNameHistoryShow.CustomAdapter customAdapter = new MinecraftNameHistoryShow.CustomAdapter(strArray, arrayError, statsIcons, "error");
                    listViewNews.setAdapter(customAdapter);
                    Toast.makeText(getApplicationContext(), "Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("FEHLER");
                error.printStackTrace();
                String[] strArray = {""};
                String[] arrayError = {"Kein Spiel gefunden"};
                int[] statsIcons = new int[]{R.drawable.ic_kills};
                MinecraftNameHistoryShow.CustomAdapter customAdapter = new MinecraftNameHistoryShow.CustomAdapter(strArray, arrayError, statsIcons, "error");
                listViewNews.setAdapter(customAdapter);
                Toast.makeText(getApplicationContext(), "Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(MinecraftNameHistoryShow.this);
        requestQueue.add(requestSteamAppNews);
    }


    private class CustomAdapter extends BaseAdapter {

        String[] valuesArray;
        String[] descriptionArray;
        int[] statsIcons;
        String status;

        public CustomAdapter(String[] strArrayParam, String[] descriptionArrayParam, int[] icons, String success) {
            valuesArray = strArrayParam;
            descriptionArray = descriptionArrayParam;
            statsIcons = icons;
            status = success;
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
            View view1 = getLayoutInflater().inflate(R.layout.row_minecraft_history_item, null);

            TextView description = view1.findViewById(R.id.statsName);
            TextView name = view1.findViewById(R.id.statsValue);
            ImageView image = view1.findViewById(R.id.iconStats);




            description.setText(descriptionArray[i]);
            name.setText(valuesArray[i]);

            if(status.equals("error")){
                image.setImageResource(statsIcons[i]);
            } else if(status.equals("success")){
                Glide.with(MinecraftNameHistoryShow.this).load("https://minotar.net/armor/body/" + username + "/300.png").into(image);
            }

            return view1;
        }
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }




}