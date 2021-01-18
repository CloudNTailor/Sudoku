package com.CloudNTailor.sudoku.GameActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatDelegate;

import com.CloudNTailor.sudoku.GameEngine.GameAction;
import com.CloudNTailor.sudoku.GameEngine.SudokuLayout;
import com.CloudNTailor.sudoku.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MistakeActivity extends Activity {

    private int[][] board;
    private int[][] finishedBoard;
    private int[][] hintsBoard;
    private int[][] begginingofBoard;

    private List<GameAction> allMove;

    private int rows = 9;
    private int cols = 9;

    private @ColorInt int defaultBlueNumberColor;
    private @ColorInt int defaultGreenNumberColor;

    private SudokuLayout grid;

    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.DarkAppTheme);
        else
            setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_mistake);

        grid=(SudokuLayout)findViewById(R.id.game_board_fin);


        board = new int[rows][cols];
        finishedBoard= new int[rows][cols];
        hintsBoard= newEmptyArray();
        begginingofBoard= new int[rows][cols];
        begginingofBoard=newEmptyArray();
        Bundle b= getIntent().getExtras();

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.numbers_blue_t, typedValue, true);
        defaultBlueNumberColor = typedValue.data;

        theme.resolveAttribute(R.attr.numbers_green_t, typedValue, true);
        defaultGreenNumberColor = typedValue.data;

        if(b!=null) {

            Gson gson = new Gson();
            String jsonToAr =  b.getString("allGameMove");

            if(jsonToAr!=null)
            {
                allMove=gson.fromJson(jsonToAr,new TypeToken<List<GameAction>>() {}.getType());
            }

            List<GameAction>  saveAllGameMoves = new ArrayList<>();
            List<GameAction>  saveFinishedPuzzle = new ArrayList<>();

            jsonToAr =   b.getString("currentGameMove");

            if(jsonToAr!=null)
            {
                saveAllGameMoves=gson.fromJson(jsonToAr,new TypeToken<List<GameAction>>() {}.getType());

                for(int i =0;i<saveAllGameMoves.size();i++)
                {
                    GameAction act = saveAllGameMoves.get(i);
                    board[act.getI()][act.getJ()]=act.getCurVal();

                    if(act.getType().equals("H"))
                        hintsBoard[act.getI()][act.getJ()]=act.getCurVal();
                    if(act.getType().equals("B"))
                        begginingofBoard[act.getI()][act.getJ()]=act.getCurVal();
                }

            }
            jsonToAr =  b.getString("finishedBoard");

            if(jsonToAr!=null)
            {
                saveFinishedPuzzle=gson.fromJson(jsonToAr,new TypeToken<List<GameAction>>() {}.getType());

                for(int i =0;i<saveFinishedPuzzle.size();i++)
                {
                    GameAction act = saveFinishedPuzzle.get(i);
                    if(act.getType().equals("F"))
                        finishedBoard[act.getI()][act.getJ()]=act.getCurVal();
                }
            }

        }

        loadFinishedGame();
        showMyMistake();
        grid.setVisibility(View.VISIBLE);
    }


    private void loadFinishedGame(){


        grid.populateBoard(begginingofBoard);
        grid.setFirstVersionBoard(begginingofBoard);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                if(board[i][j]>0)
                {
                    if(begginingofBoard[i][j]>0)
                        continue;


                    View selectedView = grid.findChildByPosition((i*9)+j);

                    ((TextView) selectedView.findViewById(R.id.number)).setText(Integer.toString(board[i][j]));
                    ((TextView) selectedView.findViewById(R.id.number)).setTextColor(defaultBlueNumberColor);
                    if(hintsBoard[i][j]>0)
                    {
                        ((TextView) selectedView.findViewById(R.id.number)).setTextColor(defaultGreenNumberColor);
                    }
                }
            }
        }


    }

    private void showMyMistake()
    {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                if(board[i][j]>0)
                {
                    if(board[i][j]==finishedBoard[i][j])
                        continue;


                    View selectedView = grid.findChildByPosition((i*9)+j);

                    ((TextView) selectedView.findViewById(R.id.number)).setBackgroundResource(grid.getCellMistakeBackGroundResource(i,j));

                }
            }
        }
    }

    private int[][] newEmptyArray()
    {
        int[][] array = { { 0,0,0,0,0,0,0,0,0 },
                { 0,0,0,0,0,0,0,0,0 },
                { 0,0,0,0,0,0,0,0,0 },
                { 0,0,0,0,0,0,0,0,0 },
                { 0,0,0,0,0,0,0,0,0 },
                { 0,0,0,0,0,0,0,0,0 },
                { 0,0,0,0,0,0,0,0,0 },
                { 0,0,0,0,0,0,0,0,0 },
                { 0,0,0,0,0,0,0,0,0 }};
        return array;
    }
}
