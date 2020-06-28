package com.weather.shixun.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据当前时间返回对应的8个预计时间
 */
public class GetTime {

    public static String[] getTime(){
        int time[]={1,4,7,10,13,16,19,22};
        int timeIndex[]={0,1,2,3,4,5,6,7};
        String[] trueTime = {"1时", "4时", "7时", "10时", "13时", "16时", "19时","22时"};

        SimpleDateFormat df = new SimpleDateFormat("HH");//设置日期格式
        int nowTime=Integer.parseInt(df.format(new Date()));
        int nowTimeIndex = 0;
        String[] res = new String[8];

        //处理当前时间对应的预计时间,当前时间若为17:00,则预计时间该为19-22-1-3-7-10-13-16
        for (int j=0;j<time.length;j++){
            if (nowTime<time[j]){
//                nowTime=time[j];
                nowTimeIndex=j;
                break;
            }
        }

        for (int i = 0; i < 8; i++) {
            if (nowTimeIndex==8){
                nowTimeIndex=0;
            }
            timeIndex[i]=nowTimeIndex++;
            res[i]=trueTime[timeIndex[i]];
        }

        return res;
    }
}
