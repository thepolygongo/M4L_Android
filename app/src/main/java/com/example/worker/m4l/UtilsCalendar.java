package com.example.worker.m4l;

import java.util.Calendar;

public class UtilsCalendar {

    public static Calendar getFistdayOfWeek(int week, int month, int year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_MONTH, week);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_WEEK,
                cal.getActualMinimum(Calendar.DAY_OF_WEEK));
        return cal;
    }

    public static int compare(Calendar cal1, Calendar cal2){

        int mm = cal1.get(Calendar.MONTH);
        int yy = cal1.get(Calendar.YEAR);
        int dd = cal1.get(Calendar.DAY_OF_MONTH);

        int mm2 = cal2.get(Calendar.MONTH);
        int yy2 = cal2.get(Calendar.YEAR);
        int dd2 = cal2.get(Calendar.DAY_OF_MONTH);

        if(yy > yy2) return 1;
        if(yy < yy2) return -1;

        if(mm > mm2) return 1;
        if(mm < mm2) return -1;

        if(dd > dd2) return 1;
        if(dd < dd2) return -1;

        return 0;
    }

    public static int isToday(Calendar cal1){ // 0 = true

        Calendar cal2 = Calendar.getInstance();
        int mm = cal1.get(Calendar.MONTH);
        int yy = cal1.get(Calendar.YEAR);
        int dd = cal1.get(Calendar.DAY_OF_MONTH);

        int mm2 = cal2.get(Calendar.MONTH);
        int yy2 = cal2.get(Calendar.YEAR);
        int dd2 = cal2.get(Calendar.DAY_OF_MONTH);

        if(yy > yy2) return 1;
        if(yy < yy2) return -1;

        if(mm > mm2) return 1;
        if(mm < mm2) return -1;

        if(dd > dd2) return 1;
        if(dd < dd2) return -1;

        return 0;
    }

    public static int compareMonth(Calendar cal1, Calendar cal2){

        int mm = cal1.get(Calendar.MONTH);
        int yy = cal1.get(Calendar.YEAR);
        int dd = cal1.get(Calendar.DAY_OF_MONTH);

        int mm2 = cal2.get(Calendar.MONTH);
        int yy2 = cal2.get(Calendar.YEAR);
        int dd2 = cal2.get(Calendar.DAY_OF_MONTH);

        if(yy > yy2) return 1;
        if(yy < yy2) return -1;

        if(mm > mm2) return 1;
        if(mm < mm2) return -1;

        return 0;
    }

    public static int compareWithCurrentWeek(Calendar cal){
        Calendar current = Calendar.getInstance();

        int mm = cal.get(Calendar.MONTH);
        int yy = cal.get(Calendar.YEAR);
        int ww = cal.get(Calendar.WEEK_OF_MONTH);

        int mm2 = current.get(Calendar.MONTH);
        int yy2 = current.get(Calendar.YEAR);
        int ww2 = current.get(Calendar.WEEK_OF_MONTH);

        if(yy > yy2) return 1;
        if(yy < yy2) return -1;

        if(mm > mm2) return 1;
        if(mm < mm2) return -1;

        if(ww > ww2) return 1;
        if(ww < ww2) return -1;
        return 0;
    }
}
