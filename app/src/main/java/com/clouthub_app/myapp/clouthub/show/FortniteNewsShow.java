package com.clouthub_app.myapp.clouthub.show;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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
import com.clouthub_app.myapp.clouthub.activities.AboutActivity;
import com.clouthub_app.myapp.clouthub.activities.HomeActivity;
import com.clouthub_app.myapp.clouthub.chooser.NewsChooser;
import com.clouthub_app.myapp.clouthub.R;
import com.clouthub_app.myapp.clouthub.adapters.RecyclerViewAdapterFortniteNews;
import com.clouthub_app.myapp.clouthub.chooser.StatsChooser;
import com.clouthub_app.myapp.clouthub.model.FortniteNews;

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
    private List<FortniteNews> lstFortniteNews;
    private RecyclerView recyclerView ;
    String url;
    String shop;

    Spinner spinner;
    Spinner spinnerCount;
    private Menu menu;
    String language;

    FloatingActionButton scrollTopButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_news);
        spinnerCount = findViewById(R.id.toolbar_spinner_count);
        spinnerCount.setVisibility(View.GONE);
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && scrollTopButton.getVisibility() == View.VISIBLE) {
                    scrollTopButton.hide();
                } else if (dy > 0 && scrollTopButton.getVisibility() != View.VISIBLE) {
                    scrollTopButton.show();
                }
            }
        });

        scrollTopButton = findViewById(R.id.buttonScrollUp);
        scrollTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView
                        .getLayoutManager();
                layoutManager.smoothScrollToPosition(recyclerView, null, 0);
                AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.appbarlayout_id);
                appBarLayout.setExpanded(true, true);
            }
        });

        language = getIntent().getExtras().getString("language");
        shop = getIntent().getExtras().getString("shop");
        try {
            url = getIntent().getExtras().getString("url");
        } catch (Exception e) {
            url = "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json";
        }
        jsonrequestFortniteNews(url);


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
        getMenuInflater().inflate(R.menu.menu_fortnite_news, menu);
        MenuItem iconShop = menu.findItem(R.id.fortniteShop);
        if(shop.equals("shop")){
            iconShop.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_news));
        } else {
            iconShop.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_shop));

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.switchLanguage:
                if(url.equals("https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json")) {
                    finish();
                    Intent i = new Intent(this, FortniteNewsShow.class);
                    i.putExtra("url", "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=en&format=json" );
                    i.putExtra("shop", "no");
                    i.putExtra("language", "en");
                    startActivity(i);
                    break;
                } else if (url.equals("https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=en&format=json")){
                    finish();
                    Intent i = new Intent(this, FortniteNewsShow.class);
                    i.putExtra("url", "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json" );
                    i.putExtra("shop", "no");
                    i.putExtra("language", "de");
                    startActivity(i);
                    break;
                } else if (url.equals("https://fortnite-public-api.theapinetwork.com/store/get?language=de&format=json")){
                    finish();
                    Intent i = new Intent(this, FortniteNewsShow.class);
                    i.putExtra("url", "https://fortnite-public-api.theapinetwork.com/store/get?language=en&format=json" );
                    i.putExtra("shop", "shop");
                    i.putExtra("language", "en");
                    startActivity(i);
                    break;
                } else if (url.equals("https://fortnite-public-api.theapinetwork.com/store/get?language=en&format=json")){
                    finish();
                    Intent i = new Intent(this, FortniteNewsShow.class);
                    i.putExtra("url", "https://fortnite-public-api.theapinetwork.com/store/get?language=de&format=json" );
                    i.putExtra("shop", "shop");
                    i.putExtra("language", "de");
                    startActivity(i);
                    break;
                }
            case R.id.aboutMenu:
                startActivity(new Intent(FortniteNewsShow.this, AboutActivity.class));

            case R.id.fortniteShop:
                if(shop.equals("shop")) {
                    finish();
                    Intent i = new Intent(this, FortniteNewsShow.class);
                    if(language.equals("de")){
                        i.putExtra("url", "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json" );
                        i.putExtra("language", "de");
                    } else {
                        i.putExtra("url", "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=en&format=json" );
                        i.putExtra("language", "en");
                    }
                    i.putExtra("shop", "no");
                    startActivity(i);
                    break;
                } else {
                    finish();
                    Intent i = new Intent(this, FortniteNewsShow.class);
                    if(language.equals("de")){
                        i.putExtra("url", "https://fortnite-public-api.theapinetwork.com/store/get?language=de&format=json" );
                        i.putExtra("language", "de");
                    } else {
                        i.putExtra("url", "https://fortnite-public-api.theapinetwork.com/store/get?language=en&format=json" );
                        i.putExtra("language", "en");
                    }
                    i.putExtra("shop", "shop");
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

                if(shop.equals("no")){
                    try {
                        JSONArray jsonArray = response.getJSONArray("entries");

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            FortniteNews fortniteNews = new FortniteNews();
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
                } else if(shop.equals("shop")){
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject itemObject = jsonObject.getJSONObject("item");
                            JSONObject pictureObject = itemObject.getJSONObject("images");
                            JSONObject pictureFeaturedObject = pictureObject.getJSONObject("featured");

                            FortniteNews fortniteNews = new FortniteNews();
                            try{
                                fortniteNews.setTitle(jsonObject.getString("name") + "\nV-Bucks: " + jsonObject.getString("cost") + "\n");
                            } catch (Exception e) {
                                fortniteNews.setTitle("Noch kein Name verfügbar" + "\n V-Bucks:" + jsonObject.getString("cost"));
                            }
                            fortniteNews.setBody(itemObject.getString("description"));
                            fortniteNews.setImage_url(pictureObject.getString("transparent"));

                            lstFortniteNews.add(fortniteNews);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    setuprecyclerviewFortniteNews(lstFortniteNews);
                }



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


    private void setuprecyclerviewFortniteNews(List<FortniteNews> fortniteNews) {

        RecyclerViewAdapterFortniteNews myadapter = new RecyclerViewAdapterFortniteNews(FortniteNewsShow.this,fortniteNews) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);
        if(url.equals("https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json") || url.equals("https://fortnite-public-api.theapinetwork.com/store/get?language=de&format=json")) {
            Toast.makeText(this, "News wurden geladen", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "News loaded", Toast.LENGTH_SHORT).show();

        }

    }
}
