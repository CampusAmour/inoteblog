package com.campusamour.inoteblog.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class DateUtil {
    // Random by date
    public static Map<String, Integer> requireRandomByDate() {
        Map<String, Integer> mapResult = new HashMap<>();

        Calendar nowadays = Calendar.getInstance();
        mapResult.put("year", nowadays.get(Calendar.YEAR));
        mapResult.put("month", nowadays.get(Calendar.MONTH));
        mapResult.put("dayOfMonth", nowadays.get(Calendar.DAY_OF_MONTH));
        mapResult.put("dayOfWeek", nowadays.get(Calendar.DAY_OF_WEEK));
        return mapResult;
    }
    public static Long getSecondsNextEarlyMorning() {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.SECOND, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.MILLISECOND, 0);

        return (tomorrow.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    /*
    public static void main(String[] args) {
        System.out.println(DateUtil.getSecondsNextEarlyMorning());
    }
    */
}
