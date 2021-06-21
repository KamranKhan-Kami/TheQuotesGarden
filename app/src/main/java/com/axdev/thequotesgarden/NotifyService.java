package com.axdev.thequotesgarden;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class NotifyService extends Service {
    public static String MyPrefrences = "MyPrefs";
    SharedPreferences sharedPreferences;

    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    private static final int NOTIFICATION = 1243;
    private Quote qte;
    private DataBaseHandler db;
    private ArrayList<Quote> imageArry = new ArrayList<Quote>();
    public static final String INTENT_NOTIFY = "com.axdev.thequotesgarden.INTENT_NOTIFY";
   // private NotificationManager mNM;


    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private final IBinder mBinder = new ServiceBinder();

    @SuppressWarnings("deprecation")
    private void showNotification() {
        sharedPreferences = getSharedPreferences(MyPrefrences, 0);
        int id;
        id = sharedPreferences.getInt("i",0);
        if (id>=5){
            db = new DataBaseHandler(this);
            List<Quote> contacts = db.getAllJokes("");
            for (Quote cn : contacts) {

                imageArry.add(cn);

            }
            id=0;
        }else {
            db = new DataBaseHandler(this);
            List<Quote> contacts = db.getQuoteofTheDay("");
            for (Quote cn : contacts) {

                imageArry.add(cn);

            }
        }
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("i", id+1);
        ed.apply();




        Random r = new Random();
        qte = imageArry.get(r.nextInt(imageArry.size()));
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id", qte.getID());
        editor.commit();

        Boolean prefNotif = preferences.getBoolean("prefNotif", true);
        if(prefNotif.equals(true)){
            CharSequence title = getResources().getString(
                    R.string.quote_of_day);
            CharSequence text = qte.getQuote();
            NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.notif)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true);
            Intent intent1 = new Intent(this.getApplicationContext(),
                    QuoteActivity.class);
            intent1.putExtra("id", qte.getID());
            intent1.putExtra("mode", "qteday");
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(QuoteActivity.class);
            stackBuilder.addNextIntent(intent1);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION, mBuilder.build());
        }

        stopSelf();
    }
}
