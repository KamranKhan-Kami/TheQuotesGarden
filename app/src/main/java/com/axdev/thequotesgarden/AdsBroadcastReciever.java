package com.axdev.thequotesgarden;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.ads.InterstitialAd;

public class AdsBroadcastReciever extends Service {
    public static InterstitialAd mInterstitialAd;



    @Override
    public IBinder onBind(Intent intent) {

        Log.e("Reciever ","started...");

        return null;
    }
}
