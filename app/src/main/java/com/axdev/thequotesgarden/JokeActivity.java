package com.axdev.thequotesgarden;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class JokeActivity  extends ActionBarActivity implements
        TextToSpeech.OnInitListener {

    private int ID;
    private String mode,text;
    private String fav = null;
//    private Joke jokes;
    private Quote qte;
    private DataBaseHandler db;
    private ArrayList<Quote> myList = new ArrayList<>();
    private TextView textAuth,textQuote;
    private ImageView imgIcon;
    private ImageButton btnNext,btnPrevious;
    private TextToSpeech tts;
    private RoundImage roundedImage;
    private AdView adView;
    private InterstitialAd interstitial;
    SharedPreferences sharedPrefs;
    Menu menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        db = new DataBaseHandler(this);

        textAuth = (TextView) findViewById(R.id.textAuth);
        textQuote = (TextView) findViewById(R.id.textQuote);
        imgIcon = (ImageView) findViewById(R.id.imgcon);
        btnNext = (ImageButton) findViewById(R.id.btn_next);
        btnPrevious = (ImageButton) findViewById(R.id.btn_prev);

        Typeface fontQuote = Typeface.createFromAsset(getAssets(),
        "fonts/Roboto-Light.ttf");
        Typeface fontAuth = Typeface.createFromAsset(getAssets(),
        "fonts/Roboto-Italic.ttf");
        textQuote.setTypeface(fontQuote);
        textQuote.setTextSize(18);
        textAuth.setTypeface(fontAuth);
        ID = getIntent().getExtras().getInt("id");
        mode = getIntent().getExtras().getString("mode");
        if(mode.equals("qteday")){
            qte = db.getQuote(ID);
            btnNext.setVisibility(View.GONE);
            btnPrevious.setVisibility(View.GONE);
        }
        else {
            myList = (ArrayList<Quote>) getIntent().getSerializableExtra("array");
            qte = myList.get(ID);
            Log.e("Qte",qte.getCategory()+"");
            Log.e("count",qte._fileName+"");
            Log.e("Fav",qte._fav+"");
        }
        if (qte.getCategory().equals("Funny")){
            actionBar.setTitle("Joke");
        }else{
            actionBar.setTitle("Quote");
        }
        textAuth.setText(qte.getName());
        textQuote.setText(qte.getQuote());
        checkPicure();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ID < (myList.size() - 1)) {

                    ID++;
                    qte = myList.get(ID);

                    fav = qte.getFav();

                    if (fav==null){
                        menuItem.findItem(R.id.action_favorite).setIcon(R.mipmap.not_fav);
                    }else {
                        if (fav.equals("0")) {
                            menuItem.findItem(R.id.action_favorite).setIcon(R.mipmap.not_fav);

                        }
                        if (fav.equals("1")) {
                            menuItem.findItem(R.id.action_favorite).setIcon(R.mipmap.fav);

                        }
                    }



                    if (qte.getType().equals("joke")){
                        actionBar.setTitle("Joke");
                    }else{
                        actionBar.setTitle("Quote");
                    }
                    textAuth.setText(qte.getName());
                    textQuote.setText(qte.getQuote());
                    checkPicure();
                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ID > 0) {

                    ID--;
                    qte = myList.get(ID);
                    fav = qte.getFav();

                    if (fav==null){
                        menuItem.findItem(R.id.action_favorite).setIcon(R.mipmap.not_fav);
                    }else {
                        if (fav.equals("0")) {
                            menuItem.findItem(R.id.action_favorite).setIcon(R.mipmap.not_fav);

                        }
                        if (fav.equals("1")) {
                            menuItem.findItem(R.id.action_favorite).setIcon(R.mipmap.fav);

                        }
                    }




                    if (qte.getType().equals("joke")){
                        actionBar.setTitle("Joke");
                    }else{
                        actionBar.setTitle("Quote");
                    }
                    textAuth.setText(qte.getName());
                    textQuote.setText(qte.getQuote());
                    checkPicure();
                }
            }
        });

        fav = qte.getFav();




        adView = new AdView(this);
        adView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        adView.setAdSize(AdSize.BANNER);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layAdsQuote);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void checkPicure(){
        boolean isExist = false;
        InputStream imageStream = null;
        try {
            imageStream = getAssets().open("authors/"+qte.getFileName()+".jpg");

            isExist =true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        if (isExist != false){
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            roundedImage = new RoundImage(theImage);
            imgIcon.setImageDrawable(roundedImage );
        }
        else {
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.mipmap.author);
            roundedImage = new RoundImage(bm);
            imgIcon.setImageDrawable(roundedImage);
        }

    }



    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            Locale loc = new Locale("en", "UK");
            tts.setLanguage(loc);
            tts.setSpeechRate((float) 0.8);
            speakOut();
        } else {
            Log.e("TTS", "Initilization Failed");
        }

    }

    private void speakOut() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean speaker = sharedPrefs.getBoolean("prefSpeaker", true);

        if (speaker.equals(true)) {
            text = qte.getQuote() + "\n" + qte.getName();
            if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
            else {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuItem = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quote, menu);
        if (fav==null){
            menu.findItem(R.id.action_favorite).setIcon(R.mipmap.not_fav);
        }else {
            if (fav.equals("0")) {
                menu.findItem(R.id.action_favorite).setIcon(R.mipmap.not_fav);

            }
            if (fav.equals("1")) {
                menu.findItem(R.id.action_favorite).setIcon(R.mipmap.fav);

            }
        }





        return true;
    }

    @TargetApi(11)
    private void copyToClipBoard(String qte) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(qte);
        } else {

            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text", qte);

            clipboard.setPrimaryClip(clip);
        }
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

            case R.id.action_share:

                doShare();

                break;

            case R.id.copy:
                String text = qte.getQuote() + "- " + qte.getName();
                copyToClipBoard(text);
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.copy_msg),
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.action_favorite:
                if (qte.getFav()==null) {
                    qte.setFav("1");
                    db.updateQuote(qte);
                    item.setIcon(R.mipmap.fav);
                }
                else if (qte.getFav().equals("0")) {
                    qte.setFav("1");
                    db.updateQuote(qte);
                    item.setIcon(R.mipmap.fav);
                } else if (qte.getFav().equals("1")) {
                    qte.setFav("0");
                    db.updateQuote(qte);
                    item.setIcon(R.mipmap.not_fav);

                }
        }

        return true;
    }

    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    public void doShare() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Quote");
        intent.putExtra(Intent.EXTRA_TEXT,
                qte.getQuote() + "  - " + qte.getName());
        JokeActivity.this.startActivity(Intent.createChooser(intent,
                getResources().getString(R.string.share)));

    }

}
