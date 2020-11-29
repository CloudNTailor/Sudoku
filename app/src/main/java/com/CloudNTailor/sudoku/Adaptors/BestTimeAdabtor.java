package com.CloudNTailor.sudoku.Adaptors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.CloudNTailor.sudoku.Models.BestTimeModel;
import com.CloudNTailor.sudoku.R;

import java.util.List;

public class BestTimeAdabtor extends BaseAdapter
{
    private LayoutInflater bestTimeInflater;
    private List<BestTimeModel> bestTimeList;

    public BestTimeAdabtor(Activity activity, List<BestTimeModel> bestTimeList) {
        bestTimeInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.bestTimeList = bestTimeList;
    }

    public int getCount() {
        return bestTimeList.size();
    }

    @Override
    public Object getItem(int i) {
        return bestTimeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View lineView;
        lineView = bestTimeInflater.inflate(R.layout.listview_item, null);
        TextView textViewGridSize = (TextView) lineView.findViewById(R.id.textViewGridSize);
        TextView textViewTotalPlayText = (TextView) lineView.findViewById(R.id.textViewTotalPlayText);
        TextView textViewTotalPlayResult = (TextView) lineView.findViewById(R.id.textViewTotalPlayResult);
        TextView textViewTotalWinText = (TextView) lineView.findViewById(R.id.textViewTotalWinText);
        TextView textViewTotalWinResult = (TextView) lineView.findViewById(R.id.textViewTotalWinResult);
        TextView textViewTotalWinWHText = (TextView) lineView.findViewById(R.id.textViewTotalWinWHText);
        TextView textViewTotalWinWHResult = (TextView) lineView.findViewById(R.id.textViewTotalWinWHResult);
        TextView textViewAvarageTimeText = (TextView) lineView.findViewById(R.id.textViewAvarageTimeText);
        TextView textViewAvarageTimeResult = (TextView) lineView.findViewById(R.id.textViewAvarageTimeResult);
        TextView textViewBestTimeText = (TextView) lineView.findViewById(R.id.textViewBestTimeText);
        TextView textViewBestTimeResult = (TextView) lineView.findViewById(R.id.textViewBestTimeResult);


        BestTimeModel bestTime = bestTimeList.get(i);
        textViewGridSize.setText(bestTime.getGameSize());
        textViewTotalPlayText.setText(bestTime.getTotalPlayed());
        textViewTotalPlayResult.setText(bestTime.getTotalPlayedResult());
        textViewTotalWinText.setText(bestTime.getTotalWin());
        textViewTotalWinResult.setText(bestTime.getTotalWinResult());
        textViewTotalWinWHText.setText(bestTime.getTotalWinWH());
        textViewTotalWinWHResult.setText(bestTime.getTotalWinWHResult());
        textViewAvarageTimeText.setText(bestTime.getAverageTime());
        textViewAvarageTimeResult.setText(bestTime.getAverageTimeResuld());
        textViewBestTimeText.setText(bestTime.getBestTime());
        textViewBestTimeResult.setText(bestTime.getBestTimeResult());

        return lineView;
    }
}
