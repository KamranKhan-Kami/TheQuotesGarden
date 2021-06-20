package com.axdev.thequotesgarden;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.ActionBarActivity;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.ads.InterstitialAd;


public class MainActivity extends ActionBarActivity {

    DataBaseHandler db;
    private AlertDialog dialog;
    public static final int IntialQteOfDayId = 8;
    private Button btn_quotes, btn_authors, btn_favorites, btn_categories, btn_qteday, btn_rateus;
    Button btn_jokes ;
    final Context context = this;
    SharedPreferences preferences;
    private static final int RESULT_SETTINGS = 1;

    public static InterstitialAd mInterstitialAd;

    public static String MyPrefrences = "MyPrefs";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
      //            loadqute();


        db = new DataBaseHandler(this);
        db.openDataBase() ;

        btn_quotes= (Button) findViewById(R.id.btn_quotes);
        btn_jokes = (Button) findViewById(R.id.btn_jokes);
        btn_authors= (Button) findViewById(R.id.btn_authors);
        btn_favorites= (Button) findViewById(R.id.btn_favorites);
        btn_categories= (Button) findViewById(R.id.btn_categories);
        btn_qteday= (Button) findViewById(R.id.btn_qteday);
        btn_rateus= (Button) findViewById(R.id.btn_rateus);

//        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.joke);
//        RoundImage roundedImage = new RoundImage(bm);
//        btn_jokes.setCompoundDrawablesWithIntrinsicBounds(null,roundedImage,null,null);


        btn_quotes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


//                startService(new Intent(MainActivity.this,AdsBroadcastReciever.class));


                Intent intent = new Intent(MainActivity.this,
                        QuotesActivity.class);
                intent.putExtra("mode", "allQuotes");
                intent.putExtra("title","Quotes");
                startActivity(intent);
            }
        });

        btn_jokes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(MainActivity.this,JokesActivity.class));

            }
        });

        btn_authors.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                List<Quote> contacts = db.getQuoteofTheDay("");
//                for (Quote cn : contacts) {
//                    Log.e("Category:",cn.getCategory()+"");
////                    imageArry.add(cn);
//
//                }
                Intent author = new Intent(MainActivity.this,
                        AuteursActivity.class);
                startActivity(author);
            }
        });

        btn_favorites.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent favorites = new Intent(MainActivity.this,
                        QuotesActivity.class);
                favorites.putExtra("mode", "isFavorite");
                startActivity(favorites);
            }
        });

        btn_categories.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent category = new Intent(MainActivity.this,
                        CategoryActivity.class);
                startActivity(category);
            }
        });

        btn_qteday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                preferences = PreferenceManager
                        .getDefaultSharedPreferences(context);

                Intent qteDay = new Intent(MainActivity.this,
                        QuoteActivity.class);
                qteDay.putExtra("id",
                        preferences.getInt("id", IntialQteOfDayId));
                qteDay.putExtra("mode", "qteday");
                startActivity(qteDay);
            }
        });

        btn_rateus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);
                builder.setMessage(getResources().getString(
                        R.string.ratethisapp_msg));
                builder.setTitle(getResources().getString(
                        R.string.ratethisapp_title));
                builder.setPositiveButton(
                        getResources().getString(R.string.rate_it),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                Intent fire = new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName()));           //dz.amine.thequotesgarden"));
                                startActivity(fire);

                            }
                        });

                builder.setNegativeButton(
                        getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();

                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Onstart "," started...");




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            Intent i = new Intent(this, UserSettingActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
        }

        return super.onOptionsItemSelected(item);
    }


    public static boolean verifyConection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }else
            return false;

    }

//    public void loadqute(){
//        sharedPreferences = getSharedPreferences(MyPrefrences, 0);
//        ArrayList<Quote> imageArry = new ArrayList<Quote>();
//        int id;
//        id = sharedPreferences.getInt("i",0);
//        Log.e("I=",id+"");
//        if (id>=5){
//            db = new DataBaseHandler(this);
//            List<Quote> contacts = db.getQuoteofTheDay("");
//            for (Quote cn : contacts) {
//                imageArry.add(cn);
//
//            }
//            id=0;
//        }else {
//            db = new DataBaseHandler(this);
//            List<Quote> contacts = db.getAllJokes("");
//            for (Quote cn : contacts) {
//
//                imageArry.add(cn);
//
//            }
//        }
//
//        SharedPreferences.Editor ed = sharedPreferences.edit();
//        ed.putInt("i", id+1);
//        ed.apply();
//
//
//
//        Random r = new Random();
//        Quote qte = imageArry.get(r.nextInt(imageArry.size()));
//        Log.e("Log",qte.getCategory()+"");
//
//    }
}
