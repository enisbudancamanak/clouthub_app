package com.clouthub_app.myapp.clouthub.transfer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.clouthub_app.myapp.clouthub.chooser.NewsChooser;
import com.clouthub_app.myapp.clouthub.chooser.StatsChooser;
import com.clouthub_app.myapp.clouthub.model.NetworkConnection;
import com.clouthub_app.myapp.clouthub.show.MinecraftNameHistoryShow;

import org.json.JSONException;
import org.json.JSONObject;

public class MinecraftNameHistoryTransfer extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;

    private JsonObjectRequest requestMinecraftStats;
    private RequestQueue requestQueue;
    EditText usernameInput;
    Button minecraftStatsButton;


    public static final String SHARED_PREFS_MINECRAFT = "sharedPrefsMinecraft";
    public static final String TEXT = "";
    public static final String SWITCH = "switch";
    CheckBox checkBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minecraft_name_history_transfer);

        checkBox = findViewById(R.id.checkBoxMinecraft);
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        usernameInput = findViewById(R.id.usernameMinecraft);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Minecraft Stats");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUsername();



        minecraftStatsButton = findViewById(R.id.minecraftStatsButton);
        minecraftStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameInput.getText().length() != 0) {
                    jsonrequestMinecraftStats("https://api.mojang.com/users/profiles/minecraft/" + usernameInput.getText());
                } else {
                    Toast.makeText(MinecraftNameHistoryTransfer.this, "Bitte geben Sie einen Benutzernamen ein!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        usernameInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(usernameInput.getText().length() != 0) {
                        jsonrequestMinecraftStats("https://api.mojang.com/users/profiles/minecraft/" + usernameInput.getText());
                        return true;
                    } else {
                        Toast.makeText(MinecraftNameHistoryTransfer.this, "Bitte geben Sie einen Benutzernamen ein!", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
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
                startActivity(new Intent(MinecraftNameHistoryTransfer.this, AboutActivity.class));
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
                            startActivity(new Intent(MinecraftNameHistoryTransfer.this, NewsChooser.class));
                            break;

                        case R.id.homeMenu:
                            startActivity(new Intent(MinecraftNameHistoryTransfer.this, HomeActivity.class));
                            break;

                        case R.id.statsMenu:
                            startActivity(new Intent(MinecraftNameHistoryTransfer.this, StatsChooser.class));
                            break;
                    }
                    return true;
                }
            };


    public void setUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_MINECRAFT, MODE_PRIVATE);
        usernameInput.setText(sharedPreferences.getString(TEXT, "")); //"" = default Value
        checkBox.setChecked(sharedPreferences.getBoolean(SWITCH, false)); //false = default Value
    }


    private void jsonrequestMinecraftStats(String url) {
        if (checkBox.isChecked()) {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_MINECRAFT, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(TEXT, String.valueOf(usernameInput.getText()));
            editor.putBoolean(SWITCH, checkBox.isChecked());
            editor.apply();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_MINECRAFT, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(TEXT, "");
            editor.putBoolean(SWITCH, checkBox.isChecked());
            editor.apply();
        }
        requestMinecraftStats = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject jsonObject = new JSONObject(response.toString());

                    String uuid = jsonObject.getString("id");


                    statistikenAuslesen(uuid, usernameInput.getText().toString());

                } catch (JSONException e) {
                    Toast.makeText(MinecraftNameHistoryTransfer.this, "Benutzername konnte nicht gefunden werden", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MinecraftNameHistoryTransfer.this, "Benutzername konnte nicht gefunden werden", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(MinecraftNameHistoryTransfer.this);
        requestQueue.add(requestMinecraftStats) ;
    }


    public void statistikenAuslesen(String uuid, String username) {
        if (checkBox.isChecked()) {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_MINECRAFT, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(TEXT, String.valueOf(usernameInput.getText()));
            editor.putBoolean(SWITCH, checkBox.isChecked());
            editor.apply();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_MINECRAFT, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TEXT, "");
            editor.putBoolean(SWITCH, checkBox.isChecked());
            editor.apply();
        }

        if (usernameInput.length() != 0 && NetworkConnection.isNetworkStatusAvialable(getApplicationContext())) {
            System.out.println(username);
            Intent i = new Intent(this, MinecraftNameHistoryShow.class);
            i.putExtra("url", "https://api.mojang.com/user/profiles/" + uuid + "/names");
            i.putExtra("username", username);
            i.putExtra("uuid", uuid);
            startActivity(i);
        } else if (!NetworkConnection.isNetworkStatusAvialable(getApplicationContext())) {
            Toast.makeText(this, "Überprüfen Sie Ihre Internetverbindung", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Bitte geben Sie einen Benutzernamen ein!", Toast.LENGTH_SHORT).show();
        }
    }
}