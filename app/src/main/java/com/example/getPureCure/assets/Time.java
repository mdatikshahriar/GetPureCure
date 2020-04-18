package com.example.getPureCure.assets;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;

public class Time {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = Instant.now().toEpochMilli();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String getTimeDate(String time) {

        String year = time.substring(0,4);
        String month = time.substring(5,7);
        String day = time.substring(8,10);

        String hour = time.substring(11,13);
        String minute = time.substring(14,16);

        String amPm;

        int mon = Integer.parseInt(month);

        if(mon == 1)
            month = "January";
        else if (mon == 2)
            month = "February";
        else if (mon == 3)
            month = "March";
        else if (mon == 4)
            month = "April";
        else if (mon == 5)
            month = "May";
        else if (mon == 6)
            month = "June";
        else if (mon == 7)
            month = "July";
        else if (mon == 8)
            month = "August";
        else if (mon == 9)
            month = "September";
        else if (mon == 10)
            month = "October";
        else if (mon == 11)
            month = "November";
        else if (mon == 12)
            month = "December";

        day = String.valueOf(Integer.parseInt(day));

        int hr = Integer.parseInt(hour);

        if(hr > 12) {
            hour = String.valueOf(hr-12);
            amPm = "PM";
        } else if(hr == 12) {
            hour = String.valueOf(hr);
            amPm = "PM";
        } else {
            hour = String.valueOf(hr);
            amPm = "AM";
        }

        return hour + ":" + minute + " " + amPm + ", " + day + " " + month + ", " + year;
    }
}