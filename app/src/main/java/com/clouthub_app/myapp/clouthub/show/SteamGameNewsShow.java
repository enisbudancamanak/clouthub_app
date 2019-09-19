package com.clouthub_app.myapp.clouthub.show;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.AdapterView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.clouthub_app.myapp.clouthub.activities.AboutActivity;
import com.clouthub_app.myapp.clouthub.activities.HomeActivity;
import com.clouthub_app.myapp.clouthub.chooser.NewsChooser;
import com.clouthub_app.myapp.clouthub.R;
import com.clouthub_app.myapp.clouthub.chooser.StatsChooser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SteamGameNewsShow extends AppCompatActivity implements AdapterView.OnItemClickListener {


    Toolbar toolbar;
    BottomNavigationView bottomNav;

    private JsonObjectRequest requestSteamAppNews;
    private RequestQueue requestQueue ;

    ListView listViewNews;
    int[] statsIcons = {R.drawable.ic_controller_lightblue, R.drawable.ic_metacritic, R.drawable.ic_categorie, R.drawable.ic_plus, R.drawable.ic_news_bigger, R.drawable.ic_news_bigger, R.drawable.ic_news_bigger};
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayNames = new ArrayList<>();
    ArrayList<String> arrayNewsTitles = new ArrayList<>();
    ArrayList<String> arrayNewsURLs = new ArrayList<>();

    String urlInfos;
    String appId;
    DecimalFormat f = new DecimalFormat("#0.00" + "");



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_stats);

        Spinner spinner = findViewById(R.id.fortnite_chooser);
        spinner.setVisibility(View.GONE);

        ListView listview = (ListView) findViewById(R.id.listViewStats);
        listview.setOnItemClickListener(this);

        appId = getIntent().getExtras().getString("appId");
        urlInfos = getIntent().getExtras().getString("urlInfo");
        arrayNewsTitles = getIntent().getExtras().getStringArrayList("ListTitles");
        arrayNewsURLs = getIntent().getExtras().getStringArrayList("ListURLs");

        jsonrequestSteamNews(urlInfos);

        listViewNews = findViewById(R.id.listViewStats);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        toolbar = findViewById(R.id.toolbar);

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


    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        if(position == 4) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayNewsURLs.get(0)));
            startActivity(browserIntent);
        } else if (position == 5){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayNewsURLs.get(1)));
            startActivity(browserIntent);
        } else if (position == 6){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayNewsURLs.get(2)));
            startActivity(browserIntent);
        }
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
                startActivity(new Intent(SteamGameNewsShow.this, AboutActivity.class));
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
                            startActivity(new Intent(SteamGameNewsShow.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(SteamGameNewsShow.this, HomeActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(SteamGameNewsShow.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };



    private void jsonrequestSteamNews(String url) {
        requestSteamAppNews = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String genres = "";

                    JSONObject jsonObjectStart = new JSONObject(response.toString());
                    JSONObject jsonObject1 = jsonObjectStart.getJSONObject(appId);
                    JSONObject jsonObject = jsonObject1.getJSONObject("data");
                    JSONObject metacritic = jsonObject.getJSONObject("metacritic");
                    JSONArray genresArray = jsonObject.getJSONArray("genres");
                    JSONObject release_dateObject = jsonObject.getJSONObject("release_date");


                   for(int i = 0; i < genresArray.length(); i++){
                       JSONObject genresObject = genresArray.getJSONObject(i);
                       genres += genresObject.getString("description") + ", ";
                   }


                    toolbar.setSubtitle(jsonObject.getString("name") + " News");

                    arrayList.add(jsonObject.getString("name"));
                    arrayList.add(metacritic.getString("score"));
                    arrayList.add(genres);
                    arrayList.add(release_dateObject.getString("date"));
                    arrayList.add("");
                    arrayList.add("");
                    arrayList.add("");


                    arrayNames.add("Name: ");
                    arrayNames.add("Score: ");
                    arrayNames.add("Genres: ");
                    arrayNames.add("Release: ");

                    String[] newsTitles = arrayNewsTitles.toArray(new String[0]);

                    arrayNames.add(newsTitles[0]);
                    arrayNames.add(newsTitles[1]);
                    arrayNames.add(newsTitles[2]);

                    String[] arrayValues = arrayList.toArray(new String[0]);
                    String[] marrayNames = arrayNames.toArray(new String[0]);



                    SteamGameNewsShow.CustomAdapter customAdapter = new SteamGameNewsShow.CustomAdapter(arrayValues, marrayNames);
                    listViewNews.setAdapter(customAdapter);


                    Toast.makeText(getApplicationContext(),"Statistiken erfolgreich ausgelesen!",Toast.LENGTH_SHORT).show();




                } catch (JSONException e) {
                    System.out.println("FEHLER");
                    e.printStackTrace();
                    String[] strArray = {""};
                    String[] arrayError = {"Kein Spiel gefunden"};
                    statsIcons = new int[] {R.drawable.ic_kills};
                    SteamGameNewsShow.CustomAdapter customAdapter = new SteamGameNewsShow.CustomAdapter(strArray, arrayError);
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
                String[] arrayError = {"Kein Spiel gefunden"};
                statsIcons = new int[] {R.drawable.ic_kills};
                SteamGameNewsShow.CustomAdapter customAdapter = new SteamGameNewsShow.CustomAdapter(strArray, arrayError);
                listViewNews.setAdapter(customAdapter);
                Toast.makeText(getApplicationContext(),"Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(SteamGameNewsShow.this);
        requestQueue.add(requestSteamAppNews) ;
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
            View view1 = getLayoutInflater().inflate(R.layout.row_steamitem, null);

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
