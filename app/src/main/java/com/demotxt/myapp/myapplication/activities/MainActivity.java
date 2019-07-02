package com.demotxt.myapp.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.adapters.RecyclerViewAdapter;
import com.demotxt.myapp.myapplication.adapters.RecyclerViewAdapterFortniteNews;
import com.demotxt.myapp.myapplication.model.Anime;
import com.demotxt.myapp.myapplication.model.FortniteNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final String JSON_URL_ANIME = "https://gist.githubusercontent.com/aws1994/f583d54e5af8e56173492d3f60dd5ebf/raw/c7796ba51d5a0d37fc756cf0fd14e54434c547bc/anime.json" ;
    private final String JSON_URL_FORTNITENEWS = "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get?language=de&format=json" ;

    private JsonArrayRequest requestAnime ;
    private JsonObjectRequest requestFortniteNews ;
    private RequestQueue requestQueue ;
    private List<Anime> lstAnime ;
    private List<FortniteNews> lstFortniteNews;
    private RecyclerView recyclerView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstAnime = new ArrayList<>() ;
        lstFortniteNews = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recyclerviewid);

        jsonrequestFortniteNews();



    }

    private void jsonrequestAnime() {

        requestAnime = new JsonArrayRequest(JSON_URL_ANIME, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject  = null ;

                for (int i = 0 ; i < response.length(); i++ ) {


                    try {
                        jsonObject = response.getJSONObject(i) ;
                        Anime anime = new Anime() ;
                        anime.setName(jsonObject.getString("name"));
                        anime.setDescription(jsonObject.getString("description"));
                        anime.setRating(jsonObject.getString("Rating"));
                        anime.setCategorie(jsonObject.getString("categorie"));
                        anime.setNb_episode(jsonObject.getInt("episode"));
                        anime.setStudio(jsonObject.getString("studio"));
                        anime.setImage_url(jsonObject.getString("img"));
                        lstAnime.add(anime);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                setuprecyclerview(lstAnime);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(requestAnime) ;

    }

    private void setuprecyclerview(List<Anime> lstAnime) {


        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(this,lstAnime) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);

    }


    private void jsonrequestFortniteNews() {

        requestFortniteNews = new JsonObjectRequest(Request.Method.GET, JSON_URL_FORTNITENEWS, null, new Response.Listener<JSONObject>() {
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
