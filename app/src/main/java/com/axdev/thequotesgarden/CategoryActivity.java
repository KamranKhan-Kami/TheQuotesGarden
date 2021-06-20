package com.axdev.thequotesgarden;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;



public class
CategoryActivity extends ActionBarActivity {
    private ArrayList<Category> imageArry = new ArrayList<Category>();
    private CategoriesListAdapter adapter;
    private DataBaseHandler db;
    private ListView dataList;
    private AdView adView;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        db = new DataBaseHandler(this);
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

        List<Category> categories = db.getAllCategories();
        for (Category cat : categories) {

            imageArry.add(cat);

        }

        adapter = new CategoriesListAdapter(this, R.layout.category_items,
                imageArry);

        dataList = (ListView) findViewById(R.id.categoryList);
        dataList.setAdapter(adapter);
        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long idInDB) {

                Category srr = imageArry.get(position);

                if (srr.getName().equals("Funny")){
                    Intent i = new Intent(getApplicationContext(),
                            JokesWithoutAd.class);
                    i.putExtra("category", srr.getName());
                    i.putExtra("mode", "isCategory");
                    startActivity(i);
                }else {
                    Intent i = new Intent(getApplicationContext(),
                            QuotesWithoutAd.class);
                    i.putExtra("category", srr.getName());
                    i.putExtra("title","Quotes");
                    i.putExtra("mode", "isCategory");
                    startActivity(i);
                }


            }
        });

        adView = new AdView(this);
        adView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        adView.setAdSize(AdSize.BANNER);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layAdsCategories);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
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
