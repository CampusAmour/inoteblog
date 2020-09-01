package com.campusamour.inoteblog.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

// Random by date
public class RandomUtil {
    public static Map<String, Integer> requireRandomByDate() {
        Map<String, Integer> mapResult = new HashMap<>();

        Calendar nowadays = Calendar.getInstance();
        mapResult.put("year", nowadays.get(Calendar.YEAR));
        mapResult.put("month", nowadays.get(Calendar.MONTH));
        mapResult.put("dayOfMonth", nowadays.get(Calendar.DAY_OF_MONTH));
        mapResult.put("dayOfWeek", nowadays.get(Calendar.DAY_OF_WEEK));
        return mapResult;
    }
}
