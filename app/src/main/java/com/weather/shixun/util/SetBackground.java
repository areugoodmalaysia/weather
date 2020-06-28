package com.weather.shixun.util;

import android.util.Log;

import androidx.annotation.DrawableRes;

import com.weather.shixun.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据时间及天气设置背景图片
 */
public class SetBackground {

    public static @DrawableRes
    int setBackground(String weather) {
        @DrawableRes int resId = 0;
        SimpleDateFormat df = new SimpleDateFormat("HH");//设置日期格式
        int nowTime = Integer.parseInt(df.format(new Date()));
        if (nowTime >= 7 && nowTime <= 19) {
            switch (weather) {
                case "晴":
                    resId = R.drawable.qing_b;
                    break;
                case "多云":
                case "阴":
                    resId = R.drawable.duoyun_yin_b;
                    break;
                case "雨":
                    resId = R.drawable.yu_b;
                    break;
            }
        } else {
            switch (weather) {
                case "晴":
                    resId = R.drawable.qing_w;
                    break;
                case "多云":
                case "阴":
                    resId = R.drawable.duoyun_yin_w;
                    break;
                case "雨":
                    resId = R.drawable.yu_w;
                    break;
            }
        }

        return resId;
    }
}
