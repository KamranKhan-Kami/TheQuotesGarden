package com.axdev.thequotesgarden;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

public class MyApplication extends Application {
    public static final String TAG = MyApplication.class
            .getSimpleName();
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

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
//                }, 30000);
//            }
//        });
//
//
//
//        AnalyticsTrackers.initialize(this);
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }


    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(
                                    new StandardExceptionParser(this, null)
                                            .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build()
            );
        }
    }


    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

}