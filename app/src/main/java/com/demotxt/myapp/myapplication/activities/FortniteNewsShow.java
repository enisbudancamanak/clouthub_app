package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.adapters.RecyclerViewAdapterFortniteNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FortniteNewsShow extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;

    private JsonObjectRequest requestFortniteNews ;
    private RequestQueue requestQueue ;
    private List<com.demotxt.myapp.myapplication.model.FortniteNews> lstFortniteNews;
    private RecyclerView recyclerView ;
    String urlLanguage;

    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_news);
        spinner = findViewById(R.id.toolbar_spinner);
        spinner.setVisibility(View.GONE);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Fortnite News");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lstFortniteNews = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recyclerviewid);

        try {
            urlLanguage  = getIntent().getExtras().getString("url");
        } catch (Exception e) {
            urlLanguage = "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json";
        }
        jsonrequestFortniteNews(urlLanguage);


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
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.switchLanguage:
                if(urlLanguage.equals("https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json")) {
                    Intent i = new Intent(this, FortniteNewsShow.class);
                    i.putExtra("url", "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=en&format=json" );
                    startActivity(i);
                    break;
                } else {
                    Intent i = new Intent(this, FortniteNewsShow.class);
                    i.putExtra("url", "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json" );
                    startActivity(i);
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.newsMenu:
                            startActivity(new Intent(FortniteNewsShow.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(FortniteNewsShow.this, HomeActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(FortniteNewsShow.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };


    private void jsonrequestFortniteNews(String urlLanguage) {

        requestFortniteNews = new JsonObjectRequest(Request.Method.GET, urlLanguage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("entries");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        com.demotxt.myapp.myapplication.model.FortniteNews fortniteNews = new com.demotxt.myapp.myapplication.model.FortniteNews();
                        fortniteNews.setTitle(jsonObject.getString("title"));
                        fortniteNews.setBody(jsonObject.getString("body"));
                        fortniteNews.setImage_url(jsonObject.getString("image"));
                        System.out.println(jsonObject.getString("title"));
                        lstFortniteNews.add(fortniteNews);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setuprecyclerviewFortniteNews(lstFortniteNews);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue = Volley.newRequestQueue(FortniteNewsShow.this);
        requestQueue.add(requestFortniteNews) ;

    }


    private void setuprecyclerviewFortniteNews(List<com.demotxt.myapp.myapplication.model.FortniteNews> fortniteNews) {

        RecyclerViewAdapterFortniteNews myadapter = new RecyclerViewAdapterFortniteNews(FortniteNewsShow.this,fortniteNews) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);
        if(urlLanguage.equals("https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json")) {
            Toast.makeText(this, "News wurden geladen", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "News loaded", Toast.LENGTH_SHORT).show();

        }

    }
}
