package com.CloudNTailor.sudoku.Adaptors;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.CloudNTailor.sudoku.GameEngine.Converter;
import com.CloudNTailor.sudoku.GameEngine.LocalDm;
import com.CloudNTailor.sudoku.GameEngine.StaticsInfoFragment;
import com.CloudNTailor.sudoku.Models.BestTimeModel;
import com.CloudNTailor.sudoku.R;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    String[] stringArrayLevelNames;
    LocalDm bestScoreOperation = new LocalDm();

    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        stringArrayLevelNames = getLevelNames();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return StaticsInfoFragment.newInstance(getStats(0));
            case 1:
                return StaticsInfoFragment.newInstance(getStats(1));
            case 2:
                return StaticsInfoFragment.newInstance(getStats(2));
            case 3:
                return StaticsInfoFragment.newInstance(getStats(3));
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return stringArrayLevelNames[position];
    }
    @Override
    public int getCount() {
        return 4;
    }

    private String[] getLevelNames()
    {
        return mContext.getResources().getStringArray(R.array.level_names);
    }

    private BestTimeModel getStats(int i) {
        String[] stringArray = mContext.getResources().getStringArray(R.array.level_codes);

        long totalTime = bestScoreOperation.getSharedPreferenceLong(mContext, stringArray[i] + "numsplayedTotalTime", 0);
        int totalWin = bestScoreOperation.getSharedPreference(mContext, stringArray[i] + "gamefinished", 0);
        int totalWinWh = bestScoreOperation.getSharedPreference(mContext, stringArray[i] + "gamefinishedWH", 0);
        String avarageTime;
        if (totalWin > 0) {
            long avarageTimeSec = Math.round(totalTime / totalWin);
            avarageTime = Converter.GetDurationFromSecondsLong(avarageTimeSec);
        } else {
            avarageTime = "--:--";
        }

        BestTimeModel staticInfo = new BestTimeModel(stringArrayLevelNames[i],
                mContext.getResources().getString(R.string.info_total_play), Integer.toString(bestScoreOperation.getSharedPreference(mContext, stringArray[i] + "numsplayed", 0)),
                mContext.getResources().getString(R.string.info_total_win), Integer.toString(totalWin),
                mContext.getResources().getString(R.string.info_total_win_wh), Integer.toString(totalWinWh),
                mContext.getResources().getString(R.string.info_average_time), avarageTime,
                mContext.getResources().getString(R.string.best_time),
                bestScoreOperation.getSharedPreference(mContext, stringArray[i], "--:--"));
        return staticInfo;
    }
}
