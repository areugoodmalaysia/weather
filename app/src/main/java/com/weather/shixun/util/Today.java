package com.weather.shixun.util;

import java.util.Calendar;

/**
 * 将当前日期及未来六天转为星期几作为数组返回
 */
public class Today {

    public static String[] whatToday() {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int[] day = {1, 2, 3, 4, 5, 6, 7};
        String[] res = new String[7];
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        for (int i = 0; i < 7; i++) {
            if (today==7){
                today=0;
            }
            day[i] = today++;
            res[i]=weekDays[day[i]];
        }
        return res;
    }
}
