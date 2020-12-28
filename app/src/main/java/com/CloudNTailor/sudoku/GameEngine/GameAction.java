package com.CloudNTailor.sudoku.GameEngine;

public class GameAction {

    private int i;
    private int j;
    private int curVal;
    private String type; //"B" beggining values "I" input values "H" hint values



    public int getI(){return i;}
    public int getJ(){return j;}
    public int getCurVal(){return curVal;}
    public String getType(){return type;}

    public void setI(int val){this.i=val;}
    public void setJ(int val){this.j=val;}
    public void setCurVal(int val){this.curVal=val;}
    public void setType(String val) {this.type=val;}

    public void GameAction(int i,int j,int curVal,String type)
    {
        this.i=i;
        this.j=j;
        this.curVal=curVal;
        this.type=type;
    }

}
