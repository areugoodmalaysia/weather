package com.weather.shixun.Adapter;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.weather.shixun.util.Today;

public class XAxisValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String[] xStrs = Today.whatToday();
        xStrs[0]="今天";

        int position = (int) value;
        if (position >= 7) {
            position = 0;
        }
        return xStrs[position];
    }
}
