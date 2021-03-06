package com.axdev.thequotesgarden;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;




public class QuotesActivity extends ActionBarActivity {

    private ArrayList<Quote> imageArry = new ArrayList<>();
    private QuotesListAdapter adapter;
    private String Activitytype;
    private DataBaseHandler db;
    private ListView dataList;
    private int count;
    private ImageView noQuotes;
    private AdView adView;

    public static String MyPrefrences = "MyPrefs";
    SharedPreferences sharedPreferences;
    Button btnLoadMore;
    String title;
    ActionBar actionBar;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        context = this;
        final InterstitialAd mInterstitialAd;
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        final AdRequest ad = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(ad);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });


        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

//        AnalyticsTrackers.initialize(this);
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

        db = new DataBaseHandler(this);
        noQuotes = (ImageView)findViewById(R.id.NoQuotes);

        dataList = (ListView) findViewById(R.id.quotesList);
         btnLoadMore = new Button(this);

        btnLoadMore.setBackgroundResource(R.drawable.btn_green);
        btnLoadMore.setText(getResources().getText(R.string.btn_LoadMore));
        btnLoadMore.setTextColor(0xffffffff);
        title = getIntent().getExtras().getString("title");
        actionBar.setTitle(title);
        Activitytype = getIntent().getExtras().getString("mode");

        loadData(Activitytype);


        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long idInDB) {


                Quote srr = imageArry.get(position);
               if (srr.getCategory().equals("Funny")){
                    Intent i = new Intent(getApplicationContext(),
                            JokeActivity.class);
                    i.putExtra("id",position);
                    i.putExtra("array", imageArry);
                    i.putExtra("mode", "");

                    startActivity(i);
                }else {
                    Intent i = new Intent(getApplicationContext(),
                            QuoteActivity.class);
                    i.putExtra("id",position);
                    i.putExtra("array", imageArry);
                    i.putExtra("mode", "");

                    startActivity(i);

                }


            }

        });

        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Starting a new async task
                new loadMoreListView().execute();
            }
        });
        adView = new AdView(this);
        adView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        adView.setAdSize(AdSize.BANNER);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layAdsQuotes);
        layout.addView(adView);
        AdRequest add = new AdRequest.Builder().build();
  adView.loadAd(add);


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Log","Onpause");


        sharedPreferences = context.getSharedPreferences(MyPrefrences,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type",Activitytype);
        editor.apply();
    }



    @Override
    protected void onRestart() {
        imageArry.clear();
        super.onRestart();
        sharedPreferences = context.getSharedPreferences(MyPrefrences,0);
        Activitytype = sharedPreferences.getString("type",null);
        loadData(Activitytype);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Onstart "," started...");
//        final InterstitialAd mInterstitialAd;
//        mInterstitialAd = new InterstitialAd(getApplicationContext());
//        // set the ad unit ID
//        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
//        final AdRequest adRequest = new AdRequest.Builder()
//                .build();
//        mInterstitialAd.loadAd(adRequest);
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }
//            }
//
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mInterstitialAd.loadAd(adRequest);
//                    }
//                }, 55000);
//            }
//        });



    }
    private class loadMoreListView extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Before starting background task
            // Show Progress Dialog etc,.
        }

        protected Void doInBackground(Void... unused) {
            runOnUiThread(new Runnable() {
                public void run() {
                    count += 50;
                    List<Quote> contacts = db.getAllQuotes(" LIMIT "+count+ ",50");
                    for (Quote cn : contacts) {

                        imageArry.add(cn);

                    }
                    int currentPosition = dataList.getFirstVisiblePosition();
                    adapter = new QuotesListAdapter(QuotesActivity.this, R.layout.quote_items, imageArry);
                    dataList.setSelectionFromTop(currentPosition + 1, 0);
                }

            });
            return (null);
        }

        protected void onPostExecute(Void unused) {

        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
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

    void loadData(String type){
        if (type.equals("isCategory")) {
            String categoryValue = getIntent().getExtras()
                    .getString("category");
            List<Quote> contacts = db.getQuotesByCategory(categoryValue);
            for (Quote cn : contacts) {

                imageArry.add(cn);

            }

        }
        if (type.equals("isAuthor")) {
            String authorValue = getIntent().getExtras().getString("name");
            List<Quote> contacts = db.getQuotesByAuthor(authorValue);
            for (Quote cn : contacts) {

                imageArry.add(cn);

            }


        }

        if (type.equals("isFavorite")) {
            actionBar.setTitle(getResources().getText(R.string.title_activity_favorites));
            List<Quote> contacts = db.getFavorites();
            for (Quote cn : contacts) {

                imageArry.add(cn);

            }
            ;
            if (imageArry.isEmpty()){

                noQuotes.setVisibility(View.VISIBLE);
            }

        }
        if (type.equals("allQuotes")) {

            List<Quote> contacts = db.getAllQuotes(" LIMIT 50");
            for (Quote cn : contacts) {

                imageArry.add(cn);

            }

            dataList.addFooterView(btnLoadMore);
        }
        adapter = new QuotesListAdapter(this, R.layout.quote_items, imageArry);
        dataList.setAdapter(adapter);
    }





}
