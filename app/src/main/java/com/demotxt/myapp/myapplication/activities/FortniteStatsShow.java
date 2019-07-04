package com.demotxt.myapp.myapplication.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import java.util.Map;

public class FortniteStatsShow extends AppCompatActivity {


    Toolbar toolbar;
    BottomNavigationView bottomNav;

    ListView listViewNews;
    int[] statsIcons = {R.drawable.ic_kills, R.drawable.ic_deaths, R.drawable.ic_kd, R.drawable.ic_controller, R.drawable.ic_wins, R.drawable.ic_prozent };
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayNames = new ArrayList<>();

    private JsonObjectRequest requestFortniteNews ;
    private RequestQueue requestQueue ;
    String url;
    String username;
    FortniteStats fortniteStats;
    DecimalFormat f = new DecimalFormat("#0" + "");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);



        username = getIntent().getExtras().getString("username");
        url = getIntent().getExtras().getString("url");
        System.out.println("URL: "  + url);
        jsonrequestFortniteStats(url);

        listViewNews = findViewById(R.id.listViewNews);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(username + "'s Fortnite Stats");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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
                            startActivity(new Intent(FortniteStatsShow.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(FortniteStatsShow.this, MainActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(FortniteStatsShow.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };


    private void jsonrequestFortniteStats(String url) {
        requestFortniteNews = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("lifeTimeStats");

                    String[] value = new String[jsonArray.length()];


                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        value[i] = String.valueOf(jObj.getString("value"));
                    }


                    Double deaths = Double.parseDouble(value[10]) / Double.parseDouble(value[11]);
                    arrayList.add(value[10]);
                    arrayList.add(String.valueOf(f.format(deaths)));
                    arrayList.add(value[11]);
                    arrayList.add(value[7]);
                    arrayList.add(value[8]);
                    arrayList.add(value[9]);

                    arrayNames.add("Kills: ");
                    arrayNames.add("Deaths: ");
                    arrayNames.add("KD: ");
                    arrayNames.add("Matches: ");
                    arrayNames.add("Wins: ");
                    arrayNames.add("Winratio: ");

                    String[] arrayValues = arrayList.toArray(new String[0]);
                    String[] marrayNames = arrayNames.toArray(new String[0]);






                    CustomAdapter customAdapter = new CustomAdapter(arrayValues, marrayNames);
                    listViewNews.setAdapter(customAdapter);

                    Toast.makeText(getApplicationContext(),"Statistiken erfolgreich ausgelesen!",Toast.LENGTH_SHORT).show();




                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("FEHLER");
                    String[] strArray = {""};
                    String[] arrayError = {"Keinen Spieler gefunden"};
                    statsIcons = new int[] {R.drawable.ic_kills};
                    CustomAdapter customAdapter = new CustomAdapter(strArray, arrayError);
                    listViewNews.setAdapter(customAdapter);
                    Toast.makeText(getApplicationContext(),"Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!",Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("FEHLER");
                String[] strArray = {""};
                String[] arrayError = {"Keinen Spieler gefunden"};
                statsIcons = new int[] {R.drawable.ic_kills};
                CustomAdapter customAdapter = new CustomAdapter(strArray, arrayError);
                listViewNews.setAdapter(customAdapter);
                Toast.makeText(getApplicationContext(),"Spieler konnte nicht gefunden werden! Überprüfen Sie nochmal Ihre Eingabe!",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("TRN-Api-Key", "2fe254f7-9852-4d31-9f63-f66460ebd96a");
                return params;
            }
        };


        requestQueue = Volley.newRequestQueue(FortniteStatsShow.this);
        requestQueue.add(requestFortniteNews) ;

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
            TextView value = view1.findViewById(R.id.statsValue);
            ImageView image = view1.findViewById(R.id.iconStats);

            description.setText(descriptionArray[i]);
            value.setText(valuesArray[i]);
            image.setImageResource(statsIcons[i]);

            return view1;
        }
    }
}
