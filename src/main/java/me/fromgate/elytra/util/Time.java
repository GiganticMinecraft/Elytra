package me.fromgate.elytra.util;

public class Time {

    public static Long parseTime(String time) {
        if (time == null || time.isEmpty()) return 0L;
        int hh = 0; 
        int mm = 0; 
        int ss = 0; 
        int tt = 0; 
        int ms = 0; 
        if (isInteger(time)) {
            ss = Integer.parseInt(time);
        } else if (time.matches("^[0-5][0-9]:[0-5][0-9]$")) {
            String[] ln = time.split(":");
            if (isInteger(ln[0])) mm = Integer.parseInt(ln[0]);
            if (isInteger(ln[1])) ss = Integer.parseInt(ln[1]);
        } else if (time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
            String[] ln = time.split(":");
            if (isInteger(ln[0])) hh = Integer.parseInt(ln[0]);
            if (isInteger(ln[1])) mm = Integer.parseInt(ln[1]);
            if (isInteger(ln[2])) ss = Integer.parseInt(ln[2]);
        } else if (time.matches("^\\d+ms")) {
            ms = Integer.parseInt(time.replace("ms", ""));
        } else if (time.matches("^\\d+h")) {
            hh = Integer.parseInt(time.replace("h", ""));
        } else if (time.matches("^\\d+m$")) {
            mm = Integer.parseInt(time.replace("m", ""));
        } else if (time.matches("^\\d+s$")) {
            ss = Integer.parseInt(time.replace("s", ""));
        } else if (time.matches("^\\d+t$")) {
            tt = Integer.parseInt(time.replace("t", ""));
        }
        return (hh * 3600000L) + (mm * 60000L) + (ss * 1000L) + (tt * 50L) + ms;
    }

    public static boolean isInteger(String intStr) {
        return intStr.matches("\\d+");
    }

}

