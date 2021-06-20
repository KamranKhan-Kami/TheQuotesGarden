package com.axdev.thequotesgarden;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by CH_M_USMAN on 10-Jun-17.
 */

public class AdsBroadcastReciever extends Service {
    public static InterstitialAd mInterstitialAd;



    @Override
    public IBinder onBind(Intent intent) {

        Log.e("Reciever ","started...");

        return null;
    }
}
