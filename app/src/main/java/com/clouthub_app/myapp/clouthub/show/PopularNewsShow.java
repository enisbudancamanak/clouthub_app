package com.clouthub_app.myapp.clouthub.show;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.clouthub_app.myapp.clouthub.activities.HomeActivity;
import com.clouthub_app.myapp.clouthub.R;
import com.clouthub_app.myapp.clouthub.activities.AboutActivity;
import com.clouthub_app.myapp.clouthub.adapters.RecyclerViewAdapterPopularNews;
import com.clouthub_app.myapp.clouthub.chooser.NewsChooser;
import com.clouthub_app.myapp.clouthub.chooser.StatsChooser;
import com.clouthub_app.myapp.clouthub.model.PopularNews;

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
    int positionCategory;
    int positionCount;
    int count=10;

    FloatingActionButton scrollTopButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_news);


        recyclerView = findViewById(R.id.recyclerviewid);
        scrollTopButton = findViewById(R.id.buttonScrollUp);
        scrollTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView
                        .getLayoutManager();
                layoutManager.smoothScrollToPosition(recyclerView, null, 0);
            }
        });



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && scrollTopButton.getVisibility() == View.VISIBLE) {
                    scrollTopButton.hide();
                    AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.appbarlayout_id);
                    appBarLayout.setExpanded(true, true);
                } else if (dy > 0 && scrollTopButton.getVisibility() != View.VISIBLE) {
                    scrollTopButton.show();
                }
            }
        });

        try {
            count = getIntent().getExtras().getInt("count");
            positionCategory  = getIntent().getExtras().getInt("positionCategory");
            positionCount = getIntent().getExtras().getInt("positionCount");
            url  = getIntent().getExtras().getString("url");
        } catch (Exception e) {
            url = "https://newsapi.org/v2/top-headlines?country=de&pageSize="+count+"&sortBy=popularity&apiKey=37134d9a41c24f379a5c8ecac307e923";
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Nachrichten");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.dropdownMenuPopular, R.layout.spinner_layout);
        final Spinner navigationSpinner =  findViewById(R.id.toolbar_spinner);
        navigationSpinner.setAdapter(spinnerAdapter);
        navigationSpinner.setSelection(positionCategory);

        SpinnerAdapter spinnerAdapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.dropdownMenuCount, R.layout.spinner_layout);
        final Spinner counterSpinner =  findViewById(R.id.toolbar_spinner_count);
        counterSpinner.setAdapter(spinnerAdapter1);
        counterSpinner.setSelection(positionCount);

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



        counterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(counterSpinner.getSelectedItemId()==0 && count!=10){
                    finish();
                    Intent intent = new Intent(PopularNewsShow.this, PopularNewsShow.class);
                    intent.putExtra("url", url);
                    intent.putExtra("positionCount", i);
                    intent.putExtra("positionCategory", positionCategory);
                    intent.putExtra("count", 10);
                    startActivity(intent);
                } else if(counterSpinner.getSelectedItemId()==1 && count!=20){
                    finish();
                    Intent intent = new Intent(PopularNewsShow.this, PopularNewsShow.class);
                    intent.putExtra("url", url);
                    intent.putExtra("positionCount", i);
                    intent.putExtra("positionCategory", positionCategory);
                    intent.putExtra("count", 20);
                    startActivity(intent);
                } else if(counterSpinner.getSelectedItemId()==2 && count!=30){
                    finish();
                    Intent intent = new Intent(PopularNewsShow.this, PopularNewsShow.class);
                    intent.putExtra("url", url);
                    intent.putExtra("positionCount", i);
                    intent.putExtra("positionCategory", positionCategory);
                    intent.putExtra("count", 30);
                    startActivity(intent);
                    startActivity(intent);
                } else if(counterSpinner.getSelectedItemId()==3 && count!=40){
                    finish();
                    Intent intent = new Intent(PopularNewsShow.this, PopularNewsShow.class);
                    intent.putExtra("url", url);
                    intent.putExtra("positionCount", i);
                    intent.putExtra("positionCategory", positionCategory);
                    intent.putExtra("count", 40);
                    startActivity(intent);
                } else if(counterSpinner.getSelectedItemId()==4 && count!=50){
                    finish();
                    Intent intent = new Intent(PopularNewsShow.this, PopularNewsShow.class);
                    intent.putExtra("url", url);
                    intent.putExtra("positionCategory", positionCategory);
                    intent.putExtra("positionCount", i);
                    intent.putExtra("count", 50);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(navigationSpinner.getSelectedItemId()==0 && !url.equals("https://newsapi.org/v2/top-headlines?country=de&pageSize="+count+"&sortBy=popularity&apiKey=37134d9a41c24f379a5c8ecac307e923")){
                    finish();
                    Intent i = new Intent(PopularNewsShow.this, PopularNewsShow.class);
                    i.putExtra("url", "https://newsapi.org/v2/top-headlines?country=de&pageSize="+count+"&sortBy=popularity&apiKey=37134d9a41c24f379a5c8ecac307e923");
                    i.putExtra("positionCategory", position);
                    i.putExtra("positionCount", positionCount);
                    i.putExtra("count", count);
                    startActivity(i);
                } else if(navigationSpinner.getSelectedItemId()==1 && !url.equals("https://newsapi.org/v2/top-headlines?country=de&pageSize="+count+"&category=business&apiKey=37134d9a41c24f379a5c8ecac307e923")) {
                    finish();
                    Intent i = new Intent(PopularNewsShow.this, PopularNewsShow.class);
                    i.putExtra("url", "https://newsapi.org/v2/top-headlines?country=de&pageSize="+count+"&category=business&apiKey=37134d9a41c24f379a5c8ecac307e923");
                    i.putExtra("positionCategory", position);
                    i.putExtra("positionCount", positionCount);
                    i.putExtra("count", count);
                    startActivity(i);
                } else if(navigationSpinner.getSelectedItemId()==2 && !url.equals("https://newsapi.org/v2/top-headlines?country=de&pageSize="+count+"&category=sport&apiKey=37134d9a41c24f379a5c8ecac307e923")) {
                    finish();
                    Intent i = new Intent(PopularNewsShow.this, PopularNewsShow.class);
                    i.putExtra("url", "https://newsapi.org/v2/top-headlines?country=de&pageSize="+count+"&category=sport&apiKey=37134d9a41c24f379a5c8ecac307e923");
                    i.putExtra("positionCategory", position);
                    i.putExtra("positionCount", positionCount);
                    i.putExtra("count", count);
                    startActivity(i);
                } else if(navigationSpinner.getSelectedItemId()==3 && !url.equals("https://newsapi.org/v2/top-headlines?country=de&pageSize="+count+"&category=technology&apiKey=37134d9a41c24f379a5c8ecac307e923")) {
                    finish();
                    Intent i = new Intent(PopularNewsShow.this, PopularNewsShow.class);
                    i.putExtra("url", "https://newsapi.org/v2/top-headlines?country=de&pageSize="+count+"&category=technology&apiKey=37134d9a41c24f379a5c8ecac307e923");
                    i.putExtra("positionCategory", position);
                    i.putExtra("positionCount", positionCount);
                    i.putExtra("count", count);
                    startActivity(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        lstPopularNews = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recyclerviewid);


        jsonrequestPopularNews(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_popular_news, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.aboutMenu:
                startActivity(new Intent(PopularNewsShow.this, AboutActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
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
                            startActivity(new Intent(PopularNewsShow.this, HomeActivity.class));
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
                        PopularNews popularNews = new PopularNews();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        popularNews.setPublishedAt(jsonObject.getString("publishedAt"));
                        JSONObject source = jsonObject.getJSONObject("source");
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
        Toast.makeText(this, "News erfolgreich ausgelesen!", Toast.LENGTH_SHORT).show();



    }
}