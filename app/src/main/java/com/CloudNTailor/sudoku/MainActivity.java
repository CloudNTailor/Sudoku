package com.CloudNTailor.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.CloudNTailor.sudoku.GameActivity.GameActivity;
import com.CloudNTailor.sudoku.Pref.SettingsActivity;
import com.CloudNTailor.sudoku.Pref.Settings;
import com.CloudNTailor.sudoku.Pref.Constants;
import com.CloudNTailor.sudoku.StaticsActivity.StaticsActivity;

import java.sql.BatchUpdateException;
import java.util.Locale;

import at.markushi.ui.CircleButton;

public class MainActivity extends Activity {


    private Button newButton;
    private Button prefButton;
    private Button statButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String langCode = Settings.getStringValue(this, getResources().getString(R.string.pref_key_language), null);
        String difLevel = Settings.getStringValue(this, getResources().getString(R.string.pref_key_difficulty), null);
        ActionBar actionBar = getActionBar();
        if(actionBar != null)
        	actionBar.hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        if(langCode == null){
            Configuration config = getResources().getConfiguration();
            Locale locale = config.locale;
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

        if(difLevel==null)
        {
            difLevel="41";
            Settings.saveStringValue(this, getResources().getString(R.string.pref_key_difficulty), difLevel);
        }

        newButton=(Button)findViewById(R.id.textButtonStart);
        newButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    startActivity(intent);


            }
        });

        prefButton=(Button) findViewById(R.id.textSetting);
        prefButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);


            }
        });
        statButton=(Button) findViewById(R.id.textStatic);
        statButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, StaticsActivity.class);
                startActivity(intent);


            }
        });
    }
}
