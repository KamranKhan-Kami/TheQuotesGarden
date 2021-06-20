package com.axdev.thequotesgarden;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class JokesWithoutAd extends ActionBarActivity {
    private ArrayList<Quote> imageArry = new ArrayList<>();
    private QuotesListAdapter adapter;
    private String Activitytype;
    private DataBaseHandler db;
    private ListView dataList;
    private int count;
    private ImageView noQuotes;
    Context context;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes_without_ad);


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        db = new DataBaseHandler(this);

        dataList = (ListView) findViewById(R.id.jokesList);
        //ArrayList<Joke> jokes =  new JokesDatabaseHandler(JokesActivity.this).getAllJokes("10");





        List<Quote> contacts = db.getAllJokes(" LIMIT 50");
        for (Quote cn : contacts) {

            imageArry.add(cn);

        }

        adapter = new QuotesListAdapter(JokesWithoutAd.this, R.layout.quote_items, imageArry);
        dataList.setAdapter(adapter);

        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long idInDB) {

                Intent i = new Intent(getApplicationContext(),
                        JokeActivity.class);
                Quote srr = imageArry.get(position);
                i.putExtra("id",position);
                i.putExtra("array", imageArry);
                i.putExtra("mode", "");

                startActivity(i);

            }

        });




        //dataList.addFooterView(btnLoadMore);



        adView = new AdView(this);
        adView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        adView.setAdSize(AdSize.BANNER);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layAdsQuotes);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quotes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;


    }
}
