package com.weather.shixun.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weather.shixun.Bean.Life;
import com.weather.shixun.Bean.ShowCity;
import com.weather.shixun.R;

import java.util.List;

public class AddCityAdapter extends ArrayAdapter<ShowCity> {

    private int resoureId;
    private List<ShowCity> objects;
    private Context context;

    public AddCityAdapter(Context context, int resourceId, List<ShowCity> objects) {
        super(context, resourceId, objects);
        // TODO Auto-generated constructor stub
        this.objects = objects;
        this.context = context;

    }

    private static class ViewHolder {
        ShowCity showCity;
        TextView city;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public ShowCity getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.addcity_item, null);
            viewHolder.city = (TextView) convertView.findViewById(R.id.cityByInput);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ShowCity showCity = objects.get(position);
        if (null != showCity) {
            viewHolder.city.setText(showCity.getCity());
        }

        return convertView;
    }
}
