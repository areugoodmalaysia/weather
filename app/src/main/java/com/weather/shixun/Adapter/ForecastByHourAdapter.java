package com.weather.shixun.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weather.shixun.Bean.ForecastByHour;
import com.weather.shixun.R;

import java.util.List;


public class ForecastByHourAdapter extends ArrayAdapter<ForecastByHour> {

    private int resoureId;
    private List<ForecastByHour> objects;
    private Context context;


    public ForecastByHourAdapter(Context context, int resourceId, List<ForecastByHour> objects) {
        super(context, resourceId, objects);
        // TODO Auto-generated constructor stub
        this.objects = objects;
        this.context = context;

    }

    private static class ViewHolder {
        ForecastByHour forecastByHour;
        TextView date;
        TextView weather;
        TextView temp;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public ForecastByHour getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.byhouritem, null);
            viewHolder.date = (TextView) convertView.findViewById(R.id.dateByHour);
            viewHolder.weather = (TextView) convertView.findViewById(R.id.forecast_weatherByHour);
            viewHolder.temp = (TextView) convertView.findViewById(R.id.tempByHour);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ForecastByHour forecastByHour = objects.get(position);
        if (null != forecastByHour) {
            viewHolder.date.setText(forecastByHour.getDate());
            viewHolder.weather.setText(forecastByHour.getWeather());
            viewHolder.temp.setText(forecastByHour.getTemp() + "° ");
        }

        return convertView;
    }

}
