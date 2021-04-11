package com.CloudNTailor.sudoku;

import android.app.ActionBar;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.CloudNTailor.sudoku.GameActivity.GameActivity;
import com.CloudNTailor.sudoku.GameEngine.Converter;
import com.CloudNTailor.sudoku.GameEngine.LocalDm;
import com.CloudNTailor.sudoku.GameService.NotificationJobScheduler;
import com.CloudNTailor.sudoku.GameService.NotificationJobService;
import com.CloudNTailor.sudoku.Pref.MyGDPR;
import com.CloudNTailor.sudoku.Pref.Settings;
import com.CloudNTailor.sudoku.Pref.Constants;
import com.CloudNTailor.sudoku.StaticsActivity.StaticsActivity;
import com.CloudNTailor.sudoku.StaticsActivity.StaticsActivityTabV;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import at.markushi.ui.CircleButton;
import hotchemi.android.rate.AppRate;

import static com.CloudNTailor.sudoku.GameService.NotificationJobScheduler.NotificationPeriodCounter;

public class MainActivity extends Activity implements OnItemSelectedListener {


    private Button newButton;
    private Button continButton;
    private Button statButton;
    private Spinner difficultySpinner;
    private String[] stringArray;
    private String[] stringArrayLevelNames;
    private CircleButton soundButton;
    private CircleButton dayNightButton;
    private Boolean soundOnOff;
    InterstitialAd interstitialAds;
    private static final int JOB_ID = 0;
    private boolean day;
    private int indexOfSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        day=Settings.getBooleanValue(this,getResources().getString(R.string.pref_key_day_night),true);
        if(!day) {
            setTheme(R.style.DarkAppTheme);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            setTheme(R.style.AppTheme);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, this.getResources().getString(R.string.admob_pid));
        String langCode = Settings.getStringValue(this, getResources().getString(R.string.pref_key_language), null);
        String difLevel = Settings.getStringValue(this, getResources().getString(R.string.pref_key_difficulty), null);
        soundOnOff = Settings.getBooleanValue(this,getResources().getString(R.string.pref_key_sound_onoff),true);
        MobileAds.setAppMuted(!soundOnOff);



        rateApp();
        NotificationPeriodCounter=1;

        difficultySpinner= (Spinner) findViewById(R.id.spinnerdifficulty);

        soundButton = (CircleButton) findViewById(R.id.btn_volume);
        ActionBar actionBar = getActionBar();
        if(actionBar != null)
        	actionBar.hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        Configuration config = getResources().getConfiguration();
        Locale locale = config.locale;



        //Day-night button operation
        dayNightButton=(CircleButton) findViewById(R.id.btn_day_night);
        if(day)
            dayNightButton.setImageResource(R.mipmap.ic_day_green);
        else
            dayNightButton.setImageResource(R.mipmap.ic_night_green);


        if(langCode == null){


            langCode = locale.getLanguage();


            //Check if this language is within supported languages
            String[] builtInLanguages = getResources().getStringArray(R.array.language_codes);
            boolean found = false;

            for(String code : builtInLanguages){
                if(code.equals(langCode)){
                    found = true;
                    break;
                }
            }

            if(!found){//make en default
                langCode = Constants.DEFAULT_LANGUAGE;
            }

            Settings.saveStringValue(this, getResources().getString(R.string.pref_key_language), langCode);
        }

        MyGDPR.updateConsentStatus(MainActivity.this);

        interstitialAds = new InterstitialAd(MainActivity.this);
        interstitialAds.setAdUnitId(this.getResources().getString(R.string.admob_interstitial_pid_contin));

        interstitialAds.loadAd(new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, MyGDPR.getBundleAd(this)).build());

        if(difLevel==null)
        {
            difLevel="41";
            Settings.saveStringValue(this, getResources().getString(R.string.pref_key_difficulty), difLevel);
        }

         boolean jobScheduled = NotificationJobScheduler.isJobServiceOn(this);
         Log.e("NOTE",Boolean.toString(jobScheduled));
         NotificationJobScheduler.scheduleJob(this);


        int dp = (int) (getResources().getDimension(R.dimen.word_size) / getResources().getDisplayMetrics().density);

        Log.e("LSIZE",Integer.toString(dp));



         indexOfSpinner=-1;

         stringArray = getResources().getStringArray(R.array.level_codes);
         stringArrayLevelNames = getResources().getStringArray(R.array.level_names);
         List<String> difLevelsList = new ArrayList<String>();
         for(int i =0;i<stringArrayLevelNames.length;i++)
         {
             difLevelsList.add(stringArrayLevelNames[i]);
             if(Integer.parseInt(stringArray[i])==Integer.parseInt(difLevel))
                 indexOfSpinner=i;
         }

         if(!soundOnOff)
         {
             if(day)
                 soundButton.setImageResource(R.mipmap.ic_volume_off_green);
             else
                 soundButton.setImageResource(R.mipmap.ic_volume_off_black);
         }
         else
         {
             if(day)
                 soundButton.setImageResource(R.mipmap.ic_volume_on_green);
             else
                 soundButton.setImageResource(R.mipmap.ic_volume_on_black);
         }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_custom_textbox, difLevelsList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        difficultySpinner.setAdapter(dataAdapter);

        difficultySpinner.setSelection(indexOfSpinner);

        difficultySpinner.setOnItemSelectedListener(this);





        newButton=(Button)findViewById(R.id.textButtonStart);
        newButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    startActivity(intent);


            }
        });

        continButton=(Button) findViewById(R.id.textContinue);
        LocalDm db = new LocalDm();
        boolean avaibleGame = db.getSharedPreferenceBoolean(MainActivity.this,getString(R.string.key_saved_game_exists),false);
        String difflevelName="";
        int curtime=-1;
        if(avaibleGame)
        {
            continButton.setVisibility(View.VISIBLE);
            difflevelName = db.getSharedPreference(MainActivity.this,getString(R.string.key_saved_game_diff),"");
            curtime=  db.getSharedPreference(MainActivity.this,getString(R.string.key_saved_game_time),0);
            String timeMin = Converter.GetDurationFromSecondsLong(curtime);

            String s1= getString(R.string.continue_button_text);
            String s2= timeMin+"-"+difflevelName;

            int n = s1.length();
            int m = s2.length();
            Spannable span = new SpannableString(s1+"\n"+s2);

            span.setSpan(new RelativeSizeSpan(1f),0,n,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new RelativeSizeSpan(0.8f),n,(n+m+1),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            continButton.setText(span);


        }
        else
            continButton.setVisibility(View.INVISIBLE);

        continButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                if(interstitialAds.isLoaded())
                {
                    interstitialAds.show();
                    interstitialAds.setAdListener(new AdListener()
                    {
                        @Override
                        public void onAdClosed() {
                            Intent intent = new Intent(MainActivity.this, GameActivity.class);
                            Bundle b = new Bundle();
                            b.putBoolean("SavedGame", true);
                            intent.putExtras(b);
                            startActivity(intent);
                            super.onAdClosed();
                        }
                    });
                }
                else {

                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    Bundle b = new Bundle();
                    b.putBoolean("SavedGame", true);
                    intent.putExtras(b);
                    startActivity(intent);
                }


            }
        });

        statButton=(Button) findViewById(R.id.textStatic);
        statButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, StaticsActivityTabV.class);
                startActivity(intent);


            }
        });

        soundButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!soundOnOff) {

                    soundOnOff=true;
                    if(day)
                        soundButton.setImageResource(R.mipmap.ic_volume_on_green);
                    else
                        soundButton.setImageResource(R.mipmap.ic_volume_on_black);

                }
                else
                {
                    soundOnOff=false;
                    if(day)
                        soundButton.setImageResource(R.mipmap.ic_volume_off_green);
                    else
                        soundButton.setImageResource(R.mipmap.ic_volume_off_black);
                }
                Settings.saveBooleanValue(MainActivity.this, getResources().getString(R.string.pref_key_sound_onoff),soundOnOff);
                MobileAds.setAppMuted(!soundOnOff);
            }
        });


        dayNightButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(day) {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    day=false;
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    day=true;
                }
                Settings.saveBooleanValue(MainActivity.this, getResources().getString(R.string.pref_key_day_night),day);
                //MobileAds.setAppMuted(!soundOnOff);

                recreate();

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getCount()>0) {

            String item = parent.getItemAtPosition(position).toString();

            for (int i = 0; i < stringArrayLevelNames.length; i++) {
                if (stringArrayLevelNames[i] == item) {
                    Settings.saveStringValue(this, getResources().getString(R.string.pref_key_difficulty), stringArray[i]);
                    break;
                }
            }
        }
        else
        {
            Settings.saveStringValue(this, getResources().getString(R.string.pref_key_difficulty), stringArray[0]);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {



    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalDm db = new LocalDm();
        boolean avaibleGame = db.getSharedPreferenceBoolean(MainActivity.this,getString(R.string.key_saved_game_exists),false);
        String difflevelName="";
        int curtime=-1;
        if(avaibleGame)
        {
            continButton.setVisibility(View.VISIBLE);
            difflevelName = db.getSharedPreference(MainActivity.this,getString(R.string.key_saved_game_diff),"");
            curtime=  db.getSharedPreference(MainActivity.this,getString(R.string.key_saved_game_time),0);
            String timeMin = Converter.GetDurationFromSecondsLong(curtime);

            String s1= getString(R.string.continue_button_text);
            String s2= timeMin+"-"+difflevelName;

            int n = s1.length();
            int m = s2.length();
            Spannable span = new SpannableString(s1+"\n"+s2);

            span.setSpan(new RelativeSizeSpan(1f),0,n,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new RelativeSizeSpan(0.8f),n,(n+m+1),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            continButton.setText(span);


        }
        else
            continButton.setVisibility(View.INVISIBLE);
        requestNewInterstitial();
    }
    private void requestNewInterstitial() {
        interstitialAds.loadAd(new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, MyGDPR.getBundleAd(this)).build());
    }


    private void rateApp()
    {

        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this)
        ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }





}
