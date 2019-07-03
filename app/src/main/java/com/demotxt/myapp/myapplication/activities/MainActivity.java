package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.adapters.RecyclerViewAdapterFortniteNews;
import com.demotxt.myapp.myapplication.model.Anime;
import com.demotxt.myapp.myapplication.model.FortniteNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private JsonObjectRequest requestFortniteNews ;
    private RequestQueue requestQueue ;
    private List<FortniteNews> lstFortniteNews;
    private RecyclerView recyclerView ;
    String urlLanguage;

    Toolbar toolbar;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Fortnite News");
        setSupportActionBar(toolbar);

        try {
            urlLanguage  = getIntent().getExtras().getString("urlLanguage");
        } catch (Exception e) {
            urlLanguage = "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json";
        }
        jsonrequestFortniteNews(urlLanguage);

        lstFortniteNews = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recyclerviewid);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.newsMenu:
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            break;
                    }
                    return true;
                }
            };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.switchLanguage:
                if(urlLanguage.equals("https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json")) {
                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("urlLanguage", "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=en&format=json" );
                    startActivity(i);
                    Toast.makeText(this, "Auf englisch gewechselt", Toast.LENGTH_SHORT).show();
                break;
                } else {
                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("urlLanguage", "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json" );
                    startActivity(i);
                    Toast.makeText(this, "Auf deutsch gewechselt", Toast.LENGTH_SHORT).show();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void openActivity(){
        if(active == true) {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        }
    }

    static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }


    private void jsonrequestFortniteNews(String urlLanguage) {

        requestFortniteNews = new JsonObjectRequest(Request.Method.GET, urlLanguage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(requestFortniteNews) ;

    }


    private void setuprecyclerviewFortniteNews(List<FortniteNews> fortniteNews) {

        RecyclerViewAdapterFortniteNews myadapter = new RecyclerViewAdapterFortniteNews(this,fortniteNews) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);

    }



}
