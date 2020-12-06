package com.CloudNTailor.sudoku.StaticsActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.CloudNTailor.sudoku.Adaptors.BestTimeAdabtor;
import com.CloudNTailor.sudoku.GameEngine.Converter;
import com.CloudNTailor.sudoku.GameEngine.LocalDm;
import com.CloudNTailor.sudoku.Models.BestTimeModel;
import com.CloudNTailor.sudoku.Pref.MyGDPR;
import com.CloudNTailor.sudoku.R;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class StaticsActivity extends Activity {
    private AdView mAdView;
    final List<BestTimeModel> bestTimeTable = new ArrayList<BestTimeModel>();
    LocalDm bestScoreOperation = new LocalDm();
    Button btnDelete;
    ListView listView;
    BestTimeAdabtor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.hide();

        setContentView(R.layout.activity_statistic);
        listView = (ListView) findViewById(R.id.listViewBestTime);

        String[] stringArray = getResources().getStringArray(R.array.level_codes);
        String[] stringArrayLevelNames = getResources().getStringArray(R.array.level_names);

        for(int i = 0 ; i<stringArray.length;i++)
        {
            long totalTime = bestScoreOperation.getSharedPreferenceLong(this,stringArray[i]+"numsplayedTotalTime",0);
            int totalWin = bestScoreOperation.getSharedPreference(this,stringArray[i]+"gamefinished",0);
            int totalWinWh = bestScoreOperation.getSharedPreference(this,stringArray[i]+"gamefinishedWH",0);
            String avarageTime;
            if(totalWin>0)
            {
                long avarageTimeSec = Math.round(totalTime/totalWin);
                avarageTime= Converter.GetDurationFromSecondsLong(avarageTimeSec);
            }
            else
            {
                avarageTime="--:--";
            }

            bestTimeTable.add(new BestTimeModel(stringArrayLevelNames[i],
                    getResources().getString(R.string.info_total_play),Integer.toString(bestScoreOperation.getSharedPreference(this,stringArray[i]+"numsplayed",0)),
                    getResources().getString(R.string.info_total_win),Integer.toString(totalWin),
                    getResources().getString(R.string.info_total_win_wh),Integer.toString(totalWinWh),
                    getResources().getString(R.string.info_average_time),avarageTime,
                    getResources().getString(R.string.best_time),
                    bestScoreOperation.getSharedPreference(this,stringArray[i],"--:--")));
        }


        adapter = new BestTimeAdabtor(this, bestTimeTable);
        listView.setAdapter(adapter);

        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                deleteButtonActivity();
            }
        });

        mAdView = (AdView) findViewById(R.id.adView);

        mAdView.loadAd(new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, MyGDPR.getBundleAd(this)).build());

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
        fillUpList();
    }

    private void fillUpList()
    {

        String[] stringArray = getResources().getStringArray(R.array.level_names);

        for(int i = 0 ; i<stringArray.length;i++)
        {
            BestTimeModel btm = (BestTimeModel)adapter.getItem(i);

            btm.setAverageTimeResuld("--:--");
            btm.setBestTimeResult("--:--");
            btm.setTotalPlayedResult("0");
            btm.setTotalWinResult("0");
            btm.setTotalWinWHResult("0");

        }
        adapter.notifyDataSetChanged();
    }


}
