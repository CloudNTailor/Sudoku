package com.CloudNTailor.sudoku.GameEngine;

public class Converter {
    public static int GetSecondsFromDurationString(String value){

        String [] parts = value.split(":");

        // Wrong format, no value for you.
        if(parts.length < 2 || parts.length > 3)
            return 0;

        int seconds = 0, minutes = 0, hours = 0;

        if(parts.length == 2){
            seconds = Integer.parseInt(parts[1]);
            minutes = Integer.parseInt(parts[0]);
        }
        else if(parts.length == 3){
            seconds = Integer.parseInt(parts[2]);
            minutes = Integer.parseInt(parts[1]);
            hours = Integer.parseInt(parts[0]);
        }

        return seconds + (minutes*60) + (hours*3600);
    }

    public  static String GetDurationFromSecondsLong(long Value)
    {
        String returnVal="";
        long p1 = Value % 60;
        long p2 = Value / 60;
        long p3 = p2 % 60;
        p2 = p2 / 60;

        if(p2>0)
        {
            returnVal=returnVal+padLeftZeros(Long.toString(p2),2)+":";
        }
        if(p3>0)
        {
            returnVal=returnVal+padLeftZeros(Long.toString(p3),2)+":";
        }
        else
        {
            returnVal=returnVal+"00"+":";
        }
        if(p1>0)
        {
            returnVal=returnVal+padLeftZeros(Long.toString(p1),2);
        }
        else
        {
            returnVal=returnVal+"00";
        }

        return returnVal;
    }
    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
