package com.CloudNTailor.sudoku.Models;

public class BestTimeModel {
    private String gameSize;
    private String totalPlayed;
    private String totalPlayedResult;
    private String totalWin;
    private String totalWinResult;
    private String totalWinWH;
    private String totalWinWHResult;
    private String averageTime;
    private String averageTimeResuld;
    private String bestTime;
    private String bestTimeResult;

    public BestTimeModel(String gameSize,String totalPlayed, String totalPlayedResult, String totalWin,String totalWinResult,
                         String totalWinWH,String totalWinWHResult,String averageTime,
                         String averageTimeResuld, String bestTime,String bestTimeResult) {
        this.gameSize = gameSize;
        this.totalPlayed = totalPlayed;
        this.totalPlayedResult = totalPlayedResult;
        this.totalWin = totalWin;
        this.totalWinResult = totalWinResult;
        this.totalWinWH = totalWinWH;
        this.totalWinWHResult=totalWinWHResult;
        this.averageTime = averageTime;
        this.averageTimeResuld = averageTimeResuld;
        this.bestTime = bestTime;
        this.bestTimeResult = bestTimeResult;
    }

    public String getGameSize() {
        return gameSize;
    }

    public void setGameSize(String value) {
        this.gameSize = value;
    }

    public String getTotalPlayed() {
        return totalPlayed;
    }

    public void setTotalPlayed(String value) {
        this.totalPlayed = value;
    }


    public String getTotalPlayedResult() {
        return totalPlayedResult;
    }

    public void setTotalPlayedResult(String value) {
        this.totalPlayedResult = value;
    }


    public String getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(String value) {
        this.totalWin = value;
    }


    public String getTotalWinResult() {
        return totalWinResult;
    }

    public void setTotalWinResult(String value) {
        this.totalWinResult = value;
    }

    public String getTotalWinWH() {
        return totalWinWH;
    }

    public void setTotalWinWH(String value) {
        this.totalWinWH = value;
    }


    public String getTotalWinWHResult() {
        return totalWinWHResult;
    }

    public void setTotalWinWHResult(String value) {
        this.totalWinWHResult = value;
    }


    public String getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(String value) {
        this.averageTime = value;
    }


    public String getAverageTimeResuld() {
        return averageTimeResuld;
    }

    public void setAverageTimeResuld(String value) {
        this.averageTimeResuld = value;
    }


    public String getBestTime() {
        return bestTime;
    }

    public void setBestTime(String value) {
        this.bestTime = value;
    }


    public String getBestTimeResult() {
        return bestTimeResult;
    }

    public void setBestTimeResult(String value) {
        this.bestTimeResult = value;
    }


}
