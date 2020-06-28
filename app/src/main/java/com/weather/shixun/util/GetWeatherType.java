package com.weather.shixun.util;

/**
 * 将和风的api生活指数转为中文
 */
public class GetWeatherType {
    public static String getWeatherType(String type){
        switch(type){
            case "comf" :
                type="舒适度指数";
                break;
            case "cw" :
                type="洗车指数";
                break;
            case "drsg" :
                type="穿衣指数";
                break;
            case "sport" :
                type="运动指数";
                break;
            case "flu" :
                type="感冒指数";
                break;
            case "trav" :
                type="旅游指数";
                break;
            case "uv" :
                type="紫外线指数";
                break;
            case "air" :
                type="空气污染扩散条件指数";
                break;
            case "ac" :
                type="空调开启指数";
                break;
            case "ag" :
                type="过敏指数";
                break;
            case "gl" :
                type="太阳镜指数";
                break;
            case "mu" :
                type="化妆指数";
                break;
            case "airc" :
                type="晾晒指数";
                break;
            case "ptfc" :
                type="交通指数";
                break;
            case "fsh" :
                type="钓鱼指数";
                break;
            case "spi" :
                type="防晒指数";
                break;
        }
        return type;
    }
}
