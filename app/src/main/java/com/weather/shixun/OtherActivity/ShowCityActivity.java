package com.weather.shixun.OtherActivity;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.weather.shixun.Adapter.ForecastByHourAdapter;
import com.weather.shixun.Adapter.ShowCityAdapter;
import com.weather.shixun.Bean.ShowCity;
import com.weather.shixun.MainActivity;
import com.weather.shixun.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ShowCityActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView listView;
    private String temp;
    private ShowCityAdapter showCityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_city);

        listView = findViewById(R.id.listview_database);




        listView.setOnItemClickListener(ShowCityActivity.this);
        listView.setOnItemLongClickListener(ShowCityActivity.this);

        //跳转添加城市activity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(ShowCityActivity.this, AddCityActivity.class));
                startActivity(intent);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
        final List<ShowCity> allCityList = DataSupport.findAll(ShowCity.class);
        final List<ShowCity> allCityList2 = DataSupport.findAll(ShowCity.class);
        Toast.makeText(ShowCityActivity.this, "position是"+position+"id是"+id, Toast.LENGTH_SHORT).show();
        for (int i=0 ;i<allCityList.size();i++){
            Log.d("S",allCityList.get(i).getCity());
        }
        //点击事件,切换默认城市
        if (position == 0) {
            Toast.makeText(ShowCityActivity.this, allCityList.get(0).getCity() + "已经是默认城市啦", Toast.LENGTH_SHORT).show();
        } else {
            //交换默认城市
            new AlertDialog.Builder(ShowCityActivity.this)
                    .setTitle("提示")
                    .setMessage("确定将" + allCityList.get(position).getCity() + "作为默认城市吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //将点击的城市设置为默认城市
                            String temp="万源市";

                            ShowCity showCity=new ShowCity();
                            showCity.setCity("达州市");
                            showCity.update(id);
 
                            showCity.setCity(temp);
                            showCity.update(0);

                            List<ShowCity> allCityList2 = DataSupport.findAll(ShowCity.class);
                            showCityAdapter = new ShowCityAdapter(ShowCityActivity.this, R.layout.showcity_item, allCityList2);
                            listView.setAdapter(showCityAdapter);

                            for (int i=0 ;i<allCityList2.size();i++){
                                Log.d("S",allCityList2.get(i).getCity());
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).create().show();
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
        //长按事件
        final List<ShowCity> allCityList = DataSupport.findAll(ShowCity.class);
        new AlertDialog.Builder(ShowCityActivity.this)
                .setTitle("提示")
                .setMessage("确定删除" + allCityList.get((int) id).getCity() + "吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //判断该城市是否存在于数据库
                        if (allCityList.size() != 1) {
                            DataSupport.deleteAll(ShowCity.class,"city=?",allCityList.get(position).getCity());
                            List<ShowCity> allCityList2 = DataSupport.findAll(ShowCity.class);
                            showCityAdapter = new ShowCityAdapter(ShowCityActivity.this, R.layout.showcity_item, allCityList2);
                            listView.setAdapter(showCityAdapter);
                            Toast.makeText(ShowCityActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ShowCityActivity.this, "城市列表至少得保留一个!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create().show();
        return false;
    }

    protected void onResume(){
        super.onResume();
        //activity恢复生命周期刷新列表数据
        List<ShowCity> allCityList = DataSupport.findAll(ShowCity.class);
        showCityAdapter = new ShowCityAdapter(ShowCityActivity.this, R.layout.showcity_item, allCityList);
        listView.setAdapter(showCityAdapter);
}

    @Override
    protected void onStart() {
        super.onStart();
        List<ShowCity> allCityList = DataSupport.findAll(ShowCity.class);
        showCityAdapter = new ShowCityAdapter(ShowCityActivity.this, R.layout.showcity_item, allCityList);
        listView.setAdapter(showCityAdapter);
    }
}