package com.CloudNTailor.sudoku.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SudokuGameProvider {


    public int[][] provideSudokuGame()
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
        int randomNum;
        Random rnd = new Random();
        List<Integer> possibleNums;
        for(int i = 0;i<9;i++)
        {


            for (int y=0;y<9;y++)
            {


                possibleNums = findPossibleNumbers(array, i, y);

                if (possibleNums.size() == 0)
                {
                    //if can't find any possible number then we start this row again
                    for (int e = 0; e < y;e++)
                    {
                        array[i][ e] = 0;
                    }
                    i--;
                    break;
                }
                randomNum = rnd.nextInt(possibleNums.size());
                array[i][ y] = possibleNums.get(randomNum);


            }
        }

        return array;
    }

    private boolean checkSmallMatrix(int[][] array,int a,int b,int value)
    {
        int smallMatrixRow = (a / 3)*3;
        int smallMatrixColumn = (b / 3)*3;


        for(int i= smallMatrixRow;i< smallMatrixRow+3;i++)
            for(int y= smallMatrixColumn;y<smallMatrixColumn+3;y++)
            {
                if (array[i] [y] == value)
                return false;
            }


        return true;
    }

    private boolean checkRow(int[][] array, int a, int b, int value)
    {


        if(b>=0)
        {
            for(int i=b;i>=0;i-- )
            {
                if (array[a][i] == value)
                return false;
            }
        }
        if(b<=8)
        {
            for(int i=b;i<=8;i++)
            {
                if (array[a][i] == value)
                return false;
            }
        }


        return true;
    }
    private boolean checkColumn(int[][] array, int a, int b, int value)
    {


        if (a >= 0)
        {
            for (int i = a; i >= 0; i--)
            {
                if (array[i][b] == value)
                return false;
            }
        }
        if (a <= 8)
        {
            for (int i = a; i <= 8; i++)
            {
                if (array[i][b] == value)
                return false;
            }
        }


        return true;
    }

    private List<Integer> findPossibleNumbers(int[][] array,int i,int y)
    {
        List<Integer> possibleNumber = new ArrayList<>();
        for(int randomNum = 1; randomNum <= 9; randomNum++)
        {
            if (checkRow(array, i, y, randomNum))
                if (checkColumn(array, i, y, randomNum))
                    if (checkSmallMatrix(array, i, y, randomNum))
                    {
                        possibleNumber.add(randomNum);
                    }
        }

        return possibleNumber;
    }
}
