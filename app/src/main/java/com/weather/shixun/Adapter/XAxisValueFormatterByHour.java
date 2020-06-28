package com.weather.shixun.Adapter;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.weather.shixun.util.GetTime;
import com.weather.shixun.util.Today;

public class XAxisValueFormatterByHour implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String[] xStrs = GetTime.getTime();

        int position = (int) value;
        if (position >= 8) {
            position = 0;
        }
        return xStrs[position];
    }
}
