package com.weather.shixun.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weather.shixun.Bean.Forecast;
import com.weather.shixun.Bean.Life;
import com.weather.shixun.R;

import java.util.List;

public class LifeAdapter extends ArrayAdapter<Life> {

    private int resoureId;
    private List<Life> objects;
    private Context context;

    public LifeAdapter( Context context, int resourceId,List<Life> objects) {
        super(context, resourceId, objects);
        // TODO Auto-generated constructor stub
        this.objects = objects;
        this.context = context;

    }

    private static class ViewHolder {
        Life life;
        TextView type;
        TextView tips;
        TextView why;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public Life getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.life_item, null);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.tips = (TextView) convertView.findViewById(R.id.tips);
            viewHolder.why = (TextView) convertView.findViewById(R.id.why);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Life life = objects.get(position);
        if (null != life) {
            viewHolder.tips.setText(life.getTips());
            viewHolder.why.setText(life.getWhy());
            viewHolder.type.setText(life.getType());
        }

        return convertView;
    }
}
