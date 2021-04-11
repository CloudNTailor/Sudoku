package com.CloudNTailor.sudoku.StaticsActivity;



import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import com.CloudNTailor.sudoku.Adaptors.TabsPagerAdapter;
import com.CloudNTailor.sudoku.GameEngine.LocalDm;
import com.CloudNTailor.sudoku.R;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

public class StaticsActivityTabV extends AppCompatActivity {
    LocalDm bestScoreOperation = new LocalDm();
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.DarkAppTheme);
        else
            setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_statistic_tab_v);
        setViewer();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statistic_delete_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                // do something
                deleteButtonActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void deleteButtonActivity()
    {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_erase_data_text)
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                })
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        eraseAllData();
                        //close();


                    }
                })
                .show();
    }

    private void eraseAllData()
    {

        bestScoreOperation.clearSharedPreference(this);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void setViewer()
    {
        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}
