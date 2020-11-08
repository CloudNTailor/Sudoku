package com.CloudNTailor.sudoku.Pref;

import com.CloudNTailor.sudoku.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefFragment extends PreferenceFragment {




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

    }


}

