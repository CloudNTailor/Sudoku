package com.CloudNTailor.sudoku.Pref;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class SettingsActivity extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefFragment fragment = new PrefFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        Settings.saveBooleanValue(SettingsActivity.this, Constants.MADE_SETTINGS, true);
        super.onBackPressed();
    }



}
