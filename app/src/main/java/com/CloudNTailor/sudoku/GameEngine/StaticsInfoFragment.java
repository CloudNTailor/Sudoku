package com.CloudNTailor.sudoku.GameEngine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.CloudNTailor.sudoku.Models.BestTimeModel;
import com.CloudNTailor.sudoku.R;

public class StaticsInfoFragment extends Fragment {


    private BestTimeModel mBestTime;

    public StaticsInfoFragment() {
        // Required empty public constructor
    }
    public StaticsInfoFragment(BestTimeModel bestTimeModel) {
        // Required empty public constructor
        mBestTime =bestTimeModel;
    }


    public static StaticsInfoFragment newInstance(BestTimeModel bestTimeModel) {
        return new StaticsInfoFragment(bestTimeModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.statictic_info_group_box, container, false);

        setStatics(root);

        return root;
    }

    private void setStatics (View root)
    {
        TextView textViewTotalPlayText = (TextView) root.findViewById(R.id.textViewTotalPlayText);
        TextView textViewTotalPlayResult = (TextView) root.findViewById(R.id.textViewTotalPlayResult);
        TextView textViewTotalWinText = (TextView) root.findViewById(R.id.textViewTotalWinText);
        TextView textViewTotalWinResult = (TextView) root.findViewById(R.id.textViewTotalWinResult);
        TextView textViewTotalWinWHText = (TextView) root.findViewById(R.id.textViewTotalWinWHText);
        TextView textViewTotalWinWHResult = (TextView) root.findViewById(R.id.textViewTotalWinWHResult);
        TextView textViewAverageTimeText = (TextView) root.findViewById(R.id.textViewAvarageTimeText);
        TextView textViewAverageTimeResult = (TextView) root.findViewById(R.id.textViewAvarageTimeResult);
        TextView textViewBestTimeText = (TextView) root.findViewById(R.id.textViewBestTimeText);
        TextView textViewBestTimeResult = (TextView) root.findViewById(R.id.textViewBestTimeResult);



       textViewTotalPlayText.setText(mBestTime.getTotalPlayed());
        textViewTotalPlayResult.setText(mBestTime.getTotalPlayedResult());
        textViewTotalWinText.setText(mBestTime.getTotalWin());
        textViewTotalWinResult.setText(mBestTime.getTotalWinResult());
        textViewTotalWinWHText.setText(mBestTime.getTotalWinWH());
        textViewTotalWinWHResult.setText(mBestTime.getTotalWinWHResult());
        textViewAverageTimeText.setText(mBestTime.getAverageTime());
        textViewAverageTimeResult.setText(mBestTime.getAverageTimeResuld());
        textViewBestTimeText.setText(mBestTime.getBestTime());
        textViewBestTimeResult.setText(mBestTime.getBestTimeResult());
    }

}
