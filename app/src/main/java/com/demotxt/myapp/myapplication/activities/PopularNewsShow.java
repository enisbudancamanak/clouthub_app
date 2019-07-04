package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.adapters.RecyclerViewAdapterPopularNews;
import com.demotxt.myapp.myapplication.model.PopularNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopularNewsShow extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;

    private JsonObjectRequest requestPopularNews;
    private RequestQueue requestQueue ;
    private List<PopularNews> lstPopularNews;
    private RecyclerView recyclerView ;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Populäre News");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lstPopularNews = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recyclerviewid);

        try {
            url  = getIntent().getExtras().getString("url");
        } catch (Exception e) {
            url = "https://newsapi.org/v2/top-headlines?country=de&apiKey=37134d9a41c24f379a5c8ecac307e923";
        }

        jsonrequestPopularNews(url);

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
                if(url.equals("https://newsapi.org/v2/top-headlines?country=de&apiKey=37134d9a41c24f379a5c8ecac307e923")) {
                    Intent i = new Intent(this, PopularNewsShow.class);
                    i.putExtra("url", "https://newsapi.org/v2/top-headlines?country=us&apiKey=37134d9a41c24f379a5c8ecac307e923" );
                    startActivity(i);
                    break;
                } else {
                    Intent i = new Intent(this, PopularNewsShow.class);
                    i.putExtra("url", "https://newsapi.org/v2/top-headlines?country=de&apiKey=37134d9a41c24f379a5c8ecac307e923" );
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
                            startActivity(new Intent(PopularNewsShow.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(PopularNewsShow.this, MainActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(PopularNewsShow.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };


    private void jsonrequestPopularNews(String url) {

        requestPopularNews = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("articles");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject source = jsonObject.getJSONObject("source");
                        PopularNews popularNews = new PopularNews();
                        popularNews.setSource(source.getString("name"));
                        popularNews.setTitle(jsonObject.getString("title"));
                        popularNews.setDescription(jsonObject.getString("description"));
                        popularNews.setContent(jsonObject.getString("content"));
                        popularNews.setAuthor(jsonObject.getString("author"));
                        popularNews.setUrl(jsonObject.getString("url"));
                        popularNews.setUrlImage(jsonObject.getString("urlToImage"));
                        System.out.println(jsonObject.getString("title"));

                        lstPopularNews.add(popularNews);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setuprecyclerviewPopularNews(lstPopularNews);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue = Volley.newRequestQueue(PopularNewsShow.this);
        requestQueue.add(requestPopularNews) ;

    }


    private void setuprecyclerviewPopularNews(List<PopularNews> popularNews) {

        RecyclerViewAdapterPopularNews myadapter = new RecyclerViewAdapterPopularNews(PopularNewsShow.this,popularNews) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);
        Toast.makeText(this, "News wurden geladen", Toast.LENGTH_SHORT).show();


    }
}
