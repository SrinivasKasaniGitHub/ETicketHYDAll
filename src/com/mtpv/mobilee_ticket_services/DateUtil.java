package com.mtpv.mobilee_ticket_services;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    SimpleDateFormat format;

    public String getTodaysDate() {
        String pattern = "dd-MMM-yyyy";
        format = new SimpleDateFormat(pattern);
        String formattedDate = format.format(new Date());

        return formattedDate;
    }

    public long DaysCalucate(String dateStart, String dateStop) {
        // String dateStart ="0101120912";
        // String dateStop = "0101121041";
        long min = 0;
        long diffDays=0;
        // HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
             diffDays = diff / (24 * 60 * 60 * 1000);

         //   min = (diffDays * 24 * 60) + (diffHours * 60) + (diffMinutes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffDays;

    }

    public String getPresentTime() {
        String pattern = "HH:mm";
        format = new SimpleDateFormat(pattern);
        String formattedDate = format.format(new Date());

        return formattedDate;
    }

    public String getPresentDateandTime() {
        String pattern = "dd-MMM-yyyy@HH:mm:ss";
        format = new SimpleDateFormat(pattern);
        String formattedDate = format.format(new Date());

        return formattedDate;
    }

    public String getPresentyear() {
        String pattern = "yyyy";
        format = new SimpleDateFormat(pattern);
        String formattedDate = format.format(new Date());

        return formattedDate;
    }

    public String getPresentMonth() {
        String pattern = "MMM";
        format = new SimpleDateFormat(pattern);
        String formattedDate = format.format(new Date());

        return formattedDate;
    }

    public String getPresentDay() {
        String pattern = "dd";
        format = new SimpleDateFormat(pattern);
        String formattedDate = format.format(new Date());

        return formattedDate;
    }

    public boolean allCharactersSame(String s)
    {
        int n = s.length();
        for (int i = 1; i < n; i++)
            if (s.charAt(i) != s.charAt(0))
                return false;

        return true;
    }

}
