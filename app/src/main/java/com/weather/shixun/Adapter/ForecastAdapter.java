package com.weather.shixun.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.weather.shixun.Bean.Forecast;
import com.weather.shixun.R;

import java.util.List;

public class ForecastAdapter extends ArrayAdapter<Forecast> {
    private int resoureId;
    private List<Forecast> objects;
    private Context context;

    private ListView listView;


    public ForecastAdapter( Context context, int resourceId,List<Forecast> objects) {
        super(context, resourceId, objects);
        // TODO Auto-generated constructor stub
        this.objects = objects;
        this.context = context;
    }

    private static class ViewHolder {
        Forecast forecast;
        TextView date;
        TextView weather;
        TextView min_max_temp;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public Forecast getItem(int position) {
        // TODO Auto-generated method stub
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item, null);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.weather = (TextView) convertView.findViewById(R.id.forecast_weather);
            viewHolder.min_max_temp = (TextView) convertView.findViewById(R.id.min_max_temp);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Forecast forecast = objects.get(position);
        if (null != forecast) {
            viewHolder.date.setText(forecast.getData());
            viewHolder.weather.setText(forecast.getWeather());
            viewHolder.min_max_temp.setText(forecast.getMin_temp() + "° / " + forecast.getMax_temp()+"°");
        }

        return convertView;
    }


}
