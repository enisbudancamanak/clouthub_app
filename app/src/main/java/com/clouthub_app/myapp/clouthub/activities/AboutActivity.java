package com.clouthub_app.myapp.clouthub.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.clouthub_app.myapp.clouthub.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element newsapiElement = new Element();
        newsapiElement.setTitle("Thanks to newsapi.org for the news-API");

        Element icons8Element = new Element();
        icons8Element.setTitle("Thanks to icons8.de for the awesome icons");


        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("Über CloutHUB")
                .setImage(R.drawable.ic_cloud)
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(newsapiElement)
                .addItem(icons8Element)
                .addGroup("Kontakt")
                .addWebsite("https://clouthub.tk")
                .addEmail("Enis.Budancamanak@hotmail.com")
                .addGitHub("enisbudancamanak/clouthub_app")
                .addItem(createCopyright())
                .create();

        setContentView(aboutPage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_top, R.anim.slide_in_top);
    }

    private Element createCopyright() {
        Element copyright = new Element();
        final String copyrightString = String.format("Copyright %d by Enis Budancamanak", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIcon(R.drawable.ic_cloud);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutActivity.this, copyrightString, Toast.LENGTH_SHORT).show();
            }
        });
        return  copyright;
    }

}
