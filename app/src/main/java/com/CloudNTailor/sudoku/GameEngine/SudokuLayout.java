package com.CloudNTailor.sudoku.GameEngine;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.CloudNTailor.sudoku.R;

import java.util.List;
import java.util.PriorityQueue;

public class SudokuLayout extends LinearLayout {

    private int cols, rows;
    private final float density = getResources().getDisplayMetrics().density;
    private final float delta = (int) (50.0 * density + 0.5);
    private View currentSelectedView;
    private OnCellHighlightedListener onCellHighlightedListener;
    private int selectedRow;
    private int selectedColumn;
    private int[][] firstVersionBoard;

    public SudokuLayout(Context context) {
        super(context);
        cols = 9;
        rows = 9;
        init();
    }

    public SudokuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        cols = 9;
        rows = cols;
        init();
    }


    private void init() {
        setWillNotDraw(false);
        setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        lp.weight = 1.0f;
        for (int i = 0; i < cols; i++) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < cols; j++) {
                View view=null ;


                if(i==0||i==3||i==6)
                {
                    if(j==0||j==3||j==6)
                    {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_st, null);
                    }
                    else if(j==1||j==4||j==7)
                    {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_t, null);
                    }
                    else if(j==2||j==5||j==8)
                    {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_te, null);
                    }
                }
                else if(i==1||i==4||i==7)
                {
                    if(j==0||j==3||j==6)
                    {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_s, null);
                    }
                    else if(j==1||j==4||j==7)
                    {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell, null);
                    }
                    else if(j==2||j==5||j==8)
                    {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_e, null);
                    }
                }
                else if(i==2||i==5||i==8)
                {
                    if(j==0||j==3||j==6)
                    {
                         view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_sb, null);
                    }
                    else if(j==1||j==4||j==7)
                    {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_b, null);
                    }
                    else if(j==2||j==5||j==8)
                    {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_be, null);
                    }
                }

                view.setFocusable(true);

                row.addView(view, lp);
            }
            addView(row, lp);
        }

        setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (currentSelectedView != null)
                            ((TextView) currentSelectedView.findViewById(R.id.number)).setBackgroundResource(getCellBackGroundResource(selectedRow,selectedColumn));
                        isNewSelection(e.getX(), e.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        break;
                }

                return true;
            }
        });


    }

    public View findChildByPosition(int index) {
        int row = (int) Math.floor((double) index / (double) cols);
        int col = index % cols;
        LinearLayout rowView = (LinearLayout) getChildAt(row);
        return rowView.getChildAt(col);
    }

    public int getNumColumns() {
        return cols;
    }

    public int getNumRows() {
        return rows;
    }

    public void deleteSelectedView(){currentSelectedView=null;}

    public int getSelectedRow(){return selectedRow;}
    public int getSelectedColumn(){return selectedColumn;}
    public View getcurrentSelectedView() {
        return currentSelectedView;
    }

    public void setFirstVersionBoard(int[][] array){this.firstVersionBoard=array;}

    public interface OnCellHighlightedListener {
        void cellHighlighted(Integer positions);
    }

    public void setOnCellHighlightedListener(OnCellHighlightedListener onCellSelectedListener) {
        onCellHighlightedListener = onCellSelectedListener;
    }

    public void populateBoard(int[][] board) {
        int curval;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                curval = board[i][j];
                View v = findChildByPosition(i * cols + j);
                if (curval != 0) {
                    ((TextView) v.findViewById(R.id.number)).setText("" + Integer.toString(curval));
                    ((TextView) v.findViewById(R.id.number)).setTextColor(getResources().getColor(R.color.numbers_dark));
                } else {
                    ((TextView) v.findViewById(R.id.number)).setText(" ");
                }
            }
        }
    }


    private int point2XAxis(float x, float y) {
        for (int i = 0; i < cols * cols; i++) {
            if (containsPoint(x, y, findChildByPosition(i))) {
                return i;
            }
        }
        return -1;
    }

    private boolean containsPoint(float x, float y, View view) {
        Rect rect = new Rect();
        view.getDrawingRect(rect);
        rect.offset(view.getLeft(), ((ViewGroup) view.getParent()).getTop());
        return rect.contains((int) x, (int) y);
    }

    private void isNewSelection(float xPos, float yPos) {

        int position = point2XAxis((int) xPos, (int) yPos);
        if (position >= 0) {

            selectedRow = (int) Math.floor((double) position / (double) cols);
            selectedColumn = position % cols;
            if(firstVersionBoard[selectedRow][selectedColumn]!=0)
            {
                selectedRow=-1;
                selectedColumn=-1;
                currentSelectedView=null;
                return;
            }

            currentSelectedView = findChildByPosition(position);

            if(selectedRow==0||selectedRow==3||selectedRow==6)
            {
                if(selectedColumn==0||selectedColumn==3||selectedColumn==6)
                {
                    //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_sb, null);
                    ((TextView) currentSelectedView.findViewById(R.id.number)).setBackgroundResource(R.drawable.textborder_st_s);
                }
                else if(selectedColumn==1||selectedColumn==4||selectedColumn==7)
                {
                    ((TextView) currentSelectedView.findViewById(R.id.number)).setBackgroundResource(R.drawable.textborder_t_s);
                }
                else if(selectedColumn==2||selectedColumn==5||selectedColumn==8)
                {
                    //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_be, null);
                    ((TextView) currentSelectedView.findViewById(R.id.number)).setBackgroundResource(R.drawable.textborder_te_s);
                }
            }
            if(selectedRow==1||selectedRow==4||selectedRow==7)
            {
                if(selectedColumn==0||selectedColumn==3||selectedColumn==6)
                {
                    //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_sb, null);
                    ((TextView) currentSelectedView.findViewById(R.id.number)).setBackgroundResource(R.drawable.textborder_s_s);

                }
                else if(selectedColumn==1||selectedColumn==4||selectedColumn==7)
                {
                    ((TextView) currentSelectedView.findViewById(R.id.number)).setBackgroundResource(R.drawable.layout_border_s);
                }
                else if(selectedColumn==2||selectedColumn==5||selectedColumn==8)
                {
                    //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_be, null);
                    ((TextView) currentSelectedView.findViewById(R.id.number)).setBackgroundResource(R.drawable.textborder_e_s);
                }
            }
            else if(selectedRow==2||selectedRow==5||selectedRow==8)
            {
                if(selectedColumn==0||selectedColumn==3||selectedColumn==6)
                {
                    //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_sb, null);
                    ((TextView) currentSelectedView.findViewById(R.id.number)).setBackgroundResource(R.drawable.textborder_sb_s);
                }
                else if(selectedColumn==1||selectedColumn==4||selectedColumn==7)
                {
                    ((TextView) currentSelectedView.findViewById(R.id.number)).setBackgroundResource(R.drawable.textborder_b_s);
                }
                else if(selectedColumn==2||selectedColumn==5||selectedColumn==8)
                {
                    //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_be, null);
                    ((TextView) currentSelectedView.findViewById(R.id.number)).setBackgroundResource(R.drawable.textborder_be_s);
                }
            }



            //currentSelectedView.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor( getContext(), R.color.iceberg))));
        }
    }

    public int getCellBackGroudColor(int i, int y) {
        //if (i < 3 || i > 5) {
        //    if (y > 2 && y < 6)
        //        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor( getContext(), R.color.harbor_grey)));
        //}
        //else {
        //    if (y < 3 || y > 5)
        //    {
        //        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor( getContext(), R.color.harbor_grey)));
         //   }
        //}
         return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor( getContext(), R.color.cultured)));
    }

    public int getCellBackGroundResource( int i,int j)
    {
        int returnVal=-1;
        if(selectedRow==0||selectedRow==3||selectedRow==6)
        {
            if(selectedColumn==0||selectedColumn==3||selectedColumn==6)
            {
                //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_sb, null);
                returnVal=R.drawable.textborder_st;
            }
            else if(selectedColumn==1||selectedColumn==4||selectedColumn==7)
            {
                returnVal=R.drawable.textborder_t;
            }
            else if(selectedColumn==2||selectedColumn==5||selectedColumn==8)
            {
                //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_be, null);
                returnVal=R.drawable.textborder_te;
            }
        }
        if(selectedRow==1||selectedRow==4||selectedRow==7)
        {
            if(selectedColumn==0||selectedColumn==3||selectedColumn==6)
            {
                //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_sb, null);
                returnVal=R.drawable.textborder_s;

            }
            else if(selectedColumn==1||selectedColumn==4||selectedColumn==7)
            {
               returnVal=R.drawable.layout_border;
            }
            else if(selectedColumn==2||selectedColumn==5||selectedColumn==8)
            {
                //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_be, null);
                returnVal=R.drawable.textborder_e;
            }
        }
        else if(selectedRow==2||selectedRow==5||selectedRow==8)
        {
            if(selectedColumn==0||selectedColumn==3||selectedColumn==6)
            {
                //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_sb, null);
                returnVal=R.drawable.textborder_sb;
            }
            else if(selectedColumn==1||selectedColumn==4||selectedColumn==7)
            {
                returnVal=R.drawable.textborder_b;
            }
            else if(selectedColumn==2||selectedColumn==5||selectedColumn==8)
            {
                //view = LayoutInflater.from(getContext()).inflate(R.layout.sudokugrid_cell_be, null);
                returnVal=R.drawable.textborder_be;
            }
        }

        return  returnVal;
    }

}
