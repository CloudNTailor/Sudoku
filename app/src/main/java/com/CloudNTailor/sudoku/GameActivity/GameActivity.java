package com.CloudNTailor.sudoku.GameActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.CloudNTailor.sudoku.GameEngine.Converter;
import com.CloudNTailor.sudoku.GameEngine.GameAction;
import com.CloudNTailor.sudoku.GameEngine.LocalDm;
import com.CloudNTailor.sudoku.GameEngine.SudokuGameProvider;
import com.CloudNTailor.sudoku.GameEngine.SudokuLayout;
import com.CloudNTailor.sudoku.Pref.MyGDPR;
import com.CloudNTailor.sudoku.Pref.Settings;
import com.CloudNTailor.sudoku.R;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import at.markushi.ui.CircleButton;

public  class GameActivity extends Activity implements SudokuLayout.OnCellHighlightedListener {

    private int[][] board;
    private int[][] finishedBoard;
    private int rows = 9;
    private int cols = 9;
    private SudokuLayout grid;
    private ProgressDialog dialog;
    private int selectedRow;
    private int selectedColumn;
    private CircleButton buttonOne;
    private CircleButton buttonTwo;
    private CircleButton buttonThree;
    private CircleButton buttonFour;
    private CircleButton buttonFive;
    private CircleButton buttonSix;
    private CircleButton buttonSeven;
    private CircleButton buttonEight;
    private CircleButton buttonNine;
    private CircleButton buttonClear;
    private Chronometer simpleChronometer;
    private RelativeLayout endGameMenu;
    private Button shareButton;
    private Button homeButton;
    private Button endScreenNewGame;
    private TextView pauseChronoText;
    private TextView scoreTimeText;
    private long timeWhenStopped = 0;
    private CircleButton btn_pause_game;
    private RelativeLayout pauseMenu;
    private Button resumeButton;
    private Button newButton;
    private Button exitButton;
    private TextView newBestScore;
    private int selectedRowF;
    private int selectedColumnF;
    private Button undoButton;
    private Button hintButton;

    private List<GameAction> allMove;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_game);

        allMove= new ArrayList<>();

        buttonOne=(CircleButton)findViewById(R.id.btn_one);
        buttonTwo=(CircleButton)findViewById(R.id.btn_two);
        buttonThree=(CircleButton)findViewById(R.id.btn_three);
        buttonFour=(CircleButton)findViewById(R.id.btn_four);
        buttonFive=(CircleButton)findViewById(R.id.btn_five);
        buttonSix=(CircleButton)findViewById(R.id.btn_six);
        buttonSeven=(CircleButton)findViewById(R.id.btn_seven);
        buttonEight=(CircleButton)findViewById(R.id.btn_eight);
        buttonNine=(CircleButton)findViewById(R.id.btn_nine);
        buttonClear=(CircleButton)findViewById(R.id.btn_clear);
        simpleChronometer= (Chronometer) findViewById(R.id.simpleChronometer);
        endGameMenu=(RelativeLayout)findViewById(R.id.scorePage);
        shareButton=(Button)findViewById(R.id.btn_share);
        homeButton=(Button)findViewById(R.id.btn_return_main);
        endScreenNewGame=(Button)findViewById(R.id.textEndButtonRestart);
        scoreTimeText=(TextView)findViewById(R.id.scorePoint);
        pauseChronoText=(TextView)findViewById(R.id.pauseChronoText);
        pauseMenu = (RelativeLayout) findViewById(R.id.pauseMenu);
        resumeButton=(Button)findViewById(R.id.textButtonResume);
        newButton=(Button)findViewById(R.id.textButtonRestart);
        exitButton=(Button)findViewById(R.id.textButtonExit);
        newBestScore =(TextView)findViewById(R.id.newBestText);
        undoButton = (Button)findViewById(R.id.btn_undo);
        hintButton = (Button)findViewById(R.id.btn_hint);
    /*    resumeButton=(Button)findViewById(R.id.textButtonResume);
        newButton=(Button)findViewById(R.id.textButtonRestart);
        exitButton=(Button)findViewById(R.id.textButtonExit);

        word_list_label_group = (RelativeLayout)findViewById(R.id.word_list_label_group);
        grd_word_list = (GridView)findViewById(R.id.grd_word_list);
        congratView = (RelativeLayout)findViewById(R.id.scorePage);
        upbanner=(RelativeLayout)findViewById(R.id.upbanner);
        scorePoint = (TextView)findViewById(R.id.scorePoint); */
        grid = (SudokuLayout) findViewById(R.id.game_board);




        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.admob_interstitial_pid));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startNewGame();
            }

        });

        requestNewInterstitial();
        mAdView = (AdView) findViewById(R.id.adView);

        mAdView.loadAd(new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, MyGDPR.getBundleAd(this)).build());




        cols = grid.getNumColumns();
        rows = grid.getNumRows();
        board = new int[rows][cols];
        finishedBoard= new int[rows][cols];
       //lock = new boolean[rows][cols];

        if (savedInstanceState != null && savedInstanceState.get("words") == null)
            savedInstanceState = null;


        if (savedInstanceState == null) {
           // isPaused = false;
            showDialog();
            Runnable r = new Runnable() {

                @Override
                public void run() {





                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            clearBoard();
                            selectNumbers();
                            grid.setOnCellHighlightedListener(GameActivity.this);
                            prepareBoard();
                            if (dialog != null)
                                dialog.dismiss();
                           // if (word_list_label_group != null)
                           //     word_list_label_group.setVisibility(View.VISIBLE);
                            grid.setVisibility(View.VISIBLE);
                           // upbanner.setVisibility(View.VISIBLE);
                            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(500);
                            grid.startAnimation(anim);
                            simpleChronometer.setBase(SystemClock.elapsedRealtime());
                            simpleChronometer.stop();
                            simpleChronometer.start();
                        }
                    });
                }
            };


            new Thread(r).start();

        } else {
         /*   isPaused = savedInstanceState.getBoolean("isPaused");
            timeWhenStopped = savedInstanceState.getLong("curTime");
            wordList = savedInstanceState.getStringArray("words");
            currentTime = savedInstanceState.getInt("time");
            clearBoard();
            List<Word> words = savedInstanceState.getParcelableArrayList("solution");
            for (Word word : words) {
                embedWord(word);
            }
            words = savedInstanceState.getParcelableArrayList("found");
            foundWords = new HashSet<>(words);
            grd_word_list.setLayoutAnimation(null);


            grid.setOnWordHighlightedListener(this);
            prepareBoard();
            grid.setVisibility(View.VISIBLE);
            upbanner.setVisibility(View.VISIBLE);

            if (isPaused) {
                simpleChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                pauseChronoText.setText(simpleChronometer.getText().toString());
                pauseMenu.setVisibility(View.VISIBLE);
                grid.setEnabled(false);
            } else {
                simpleChronometer.setBase(SystemClock.elapsedRealtime() - (currentTime * 1000));
                simpleChronometer.start();
            }
            if (word_list_label_group != null)
                word_list_label_group.setVisibility(View.VISIBLE);

            if (dialog != null)
                dialog.dismiss(); */
        }

        btn_pause_game = (CircleButton) findViewById(R.id.btn_pause);
        btn_pause_game.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                    pauseChronoText.setText(simpleChronometer.getText().toString());
                    timeWhenStopped = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
                    simpleChronometer.stop();
                    //btn_pause_game.setImageResource(R.mipmap.ic_start_green);
                    pauseMenu.setVisibility(View.VISIBLE);
                    grid.setEnabled(false);

            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                simpleChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                simpleChronometer.start();
                pauseMenu.setVisibility(View.INVISIBLE);
                grid.setEnabled(true);

            }
        });
        newButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               pauseMenu.setVisibility(View.INVISIBLE);
                grid.setEnabled(true);
               // if (mInterstitialAd.isLoaded()) {

                //    mInterstitialAd.show();
                //} else {

                    startNewGame();

                //}

            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        homeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        endScreenNewGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                endGameMenu.setVisibility(View.INVISIBLE);
                grid.setEnabled(true);
                 if (mInterstitialAd.isLoaded()) {

                    mInterstitialAd.show();
                } else {
                     startNewGame();
                 }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shareIt();
            }
        });

        buttonOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushedButton(1);

            }
        });
        buttonTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushedButton(2);
            }
        });
        buttonThree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushedButton(3);
            }
        });
        buttonFour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushedButton(4);
            }
        });
        buttonFive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushedButton(5);
            }
        });
        buttonSix.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushedButton(6);
            }
        });
        buttonSeven.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushedButton(7);
            }
        });
        buttonEight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushedButton(8);
            }
        });
        buttonNine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushedButton(9);
            }
        });
        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushedButton(0);
            }
        });
        undoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(allMove.size()>0) {
                    GameAction act = allMove.get(allMove.size() - 1);
                    View selectedView = grid.findChildByPosition((act.getI() * 9 + act.getJ()));
                    if (act.getCurVal() > 0) {
                        ((TextView) selectedView.findViewById(R.id.number)).setText(Integer.toString(act.getCurVal()));
                    } else {
                        ((TextView) selectedView.findViewById(R.id.number)).setText(" ");
                    }
                    allMove.remove(allMove.size() - 1);
                }
            }
        });
        hintButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Random rnd = new Random();
                int randomNum;
                int hintCell;
                List<Integer> freeCell = new ArrayList<>();

                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        if(board[i][j]==0)
                        {
                            freeCell.add((i*9)+j);
                        }
                    }
                }

                randomNum = rnd.nextInt(freeCell.size());

                hintCell=freeCell.get(randomNum);
                int row = hintCell / cols;
                int col = hintCell % cols;

                View selectedView = grid.findChildByPosition(hintCell);

                board[row][col]=finishedBoard[row][col];

                ((TextView) selectedView.findViewById(R.id.number)).setText(Integer.toString(finishedBoard[row][col]));
                ((TextView) selectedView.findViewById(R.id.number)).setTextColor(getResources().getColor(R.color.numbers_green));
                //gameFinished();
            }
        });
    }

    public void cellHighlighted(Integer position) {

            int row = position / cols;
            int col = position % cols;
            int curVal = board[row][col];

            //puzzleFinished();
    }

    private void requestNewInterstitial() {
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, MyGDPR.getBundleAd(this)).build());
    }

    private void showDialog(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait");//(this.getResources().getString(R.string.please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void clearBoard() {
        int defaultval=0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = defaultval;
            }
        }

    }

    private void prepareBoard() {
        prepareDifficultyLevel();
        grid.populateBoard(board);
        int[][] copyBoard = new int[9][9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                copyBoard[i][j]= board[i][j];
            }
        }

        grid.setFirstVersionBoard(copyBoard);
    }

    private void prepareDifficultyLevel()
    {
        int levelValue=Integer.parseInt(Settings.getStringValue(this, getResources().getString(R.string.pref_key_difficulty), null));
        Random rnd = new Random();
        int randomNumbers;
        int index=-1;
        List<Integer> possibleValues= new ArrayList<>();
        int i=0;
        int tempVal;
        int failTime=0;
        int[][] copyBoard = new int[9][9];
        while (i<levelValue)
        {

            for (int a = 0; a < board.length; a++) {
                for (int b = 0; b < board[a].length; b++) {
                    copyBoard[a][b]= board[a][b];
                }
            }
            possibleValues=findPossibleCellForErase();
            randomNumbers=rnd.nextInt(possibleValues.size());
            index=possibleValues.get(randomNumbers);

            int eRow = (int) Math.floor((double) index / (double) cols);
            int eCol = index % cols;
            tempVal=board[eRow][eCol];
            board[eRow][eCol]=0;
            if(solveThePuzzle()) {
                i++;
                copyBoard[eRow][eCol]=0;
            }
            else
            {
                failTime++;
                if(failTime>81)
                {
                    selectNumbers();
                    failTime=0;
                    i=0;
                    continue;
                }
            }
            for (int a = 0; a < board.length; a++) {
                for (int b = 0; b < board[a].length; b++) {
                    board[a][b]= copyBoard[a][b];
                }
            }


        }
    }

    private List<Integer> findPossibleCellForErase()
    {
        List<Integer> possibleValues= new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
               if(board[i][j] !=0)
                   possibleValues.add(((i*cols)+j));
            }
        }
        return possibleValues;
    }

    private void selectNumbers(){
        SudokuGameProvider smgp = new SudokuGameProvider();
        board = smgp.provideSudokuGame();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                finishedBoard[i][j]= board[i][j];
            }
        }

    }
    private void pushedButton(int value)
    {
        View selectedView=grid.getcurrentSelectedView();

        if(selectedView!=null) {
             selectedRowF=grid.getSelectedRow();
             selectedColumnF = grid.getSelectedColumn();
            if(value>0) {
                ((TextView) selectedView.findViewById(R.id.number)).setText(Integer.toString(value));
                ((TextView) selectedView.findViewById(R.id.number)).setTextColor(getResources().getColor(R.color.numbers_blue));
            }
            else
            {
                ((TextView) selectedView.findViewById(R.id.number)).setText(" ");
            }
            GameAction act = new GameAction();
            act.setI(selectedRowF);
            act.setJ(selectedColumnF);
            act.setCurVal(board[selectedRowF][selectedColumnF]);
            allMove.add(act);
            board[selectedRowF][selectedColumnF]=value;
            selectedView.setBackgroundColor( grid.getCellBackGroudColor(selectedRowF,selectedColumnF));
            grid.deleteSelectedView();
            if(value>0)
                gameFinished();
        }
    }

    private boolean checkBoardFinish()
    {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
               if(board[i][j]==0)
                   return false;
            }
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++)
            {
                if (!checkRow( i, j, board[i][j]))
                    if (!checkColumn( i, j, board[i][j]))
                        if (!checkSmallMatrix( i, j, board[i][j]))
                        {
                            return false;
                        }
            }
        }

        return true;
    }

    private void gameFinished()
    {
        if(checkBoardFinish())
        {
            String curTime =simpleChronometer.getText().toString();
            scoreTimeText.setText(curTime);
            addBestScore(curTime);
            endGameMenu.setVisibility(View.VISIBLE);
        }

    }

    private void addBestScore(String value)
    {
        LocalDm bestScoreOperation = new LocalDm();
        String currentDifLev = Settings.getStringValue(this, getResources().getString(R.string.pref_key_difficulty), null);
        String currentBestScore =  bestScoreOperation.getSharedPreference(this,currentDifLev,"00:00");

        int newV=Converter.GetSecondsFromDurationString(value);
        int currentV = Converter.GetSecondsFromDurationString(currentBestScore);

        if(currentV==0 || newV<currentV)
        {
            newBestScore.setVisibility(View.VISIBLE);
            bestScoreOperation.setSharedPreferenceString(this,currentDifLev,value);
        }
        else
            newBestScore.setVisibility(View.INVISIBLE);
    }

    private boolean checkSmallMatrix(int a,int b,int value)
    {
        int smallMatrixRow = (a / 3)*3;
        int smallMatrixColumn = (b / 3)*3;


        for(int i= smallMatrixRow;i< smallMatrixRow+3;i++)
            for(int y= smallMatrixColumn;y<smallMatrixColumn+3;y++)
            {
                if ( !(i==a&&i==b) && board[i] [y] == value)
                    return false;
            }


        return true;
    }

    private boolean checkRow(int a, int b, int value)
    {


        if(b>=0)
        {
            for(int i=b;i>=0;i-- )
            {
                if (i!=b&&board[a][i] == value)
                    return false;
            }
        }
        if(b<=8)
        {
            for(int i=b;i<=8;i++)
            {
                if (i!=b&& board[a][i] == value)
                    return false;
            }
        }


        return true;
    }
    private boolean checkColumn( int a, int b, int value)
    {


        if (a >= 0)
        {
            for (int i = a; i >= 0; i--)
            {
                if (i!=a&&board[i][b] == value)
                    return false;
            }
        }
        if (a <= 8)
        {
            for (int i = a; i <= 8; i++)
            {
                if (i!=a&&board[i][b] == value)
                    return false;
            }
        }


        return true;
    }


    private boolean solveThePuzzle()
    {
        boolean puzzleNotFinished =true;
        SudokuGameProvider smgp = new SudokuGameProvider();
        boolean foundAnyOnePossibleCell =false;
        while (puzzleNotFinished) {
            foundAnyOnePossibleCell=false;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++){
                    if(board[i][j]==0) {
                        List<Integer> possibleNums = smgp.findPossibleNumbers(board, i, j);
                        if (possibleNums.size() == 1) {
                            board[i][j] = possibleNums.get(0);
                            foundAnyOnePossibleCell = true;
                        }
                    }
                }
            }

            if(!foundAnyOnePossibleCell)
                return  false;
            puzzleNotFinished=!checkBoardFinish();

        }
        return true;

    }

    private void startNewGame() {
        clearBoard();
        selectNumbers();
        prepareBoard();
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        grid.startAnimation(anim);

        simpleChronometer.startAnimation(anim);
        grid.setEnabled(true);
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        simpleChronometer.stop();
        simpleChronometer.start();
    }

    private void shareIt(){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Your body here";
        String shareSub = "Your subject here";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }
}
