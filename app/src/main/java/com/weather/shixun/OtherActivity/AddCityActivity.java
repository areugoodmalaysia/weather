package com.weather.shixun.OtherActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.shixun.Adapter.AddCityAdapter;
import com.weather.shixun.Adapter.ForecastByHourAdapter;
import com.weather.shixun.Adapter.LifeAdapter;
import com.weather.shixun.Bean.Life;
import com.weather.shixun.Bean.ShowCity;
import com.weather.shixun.MainActivity;
import com.weather.shixun.R;
import com.weather.shixun.util.GetWeatherType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddCityActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String key = "0b24f564a1c706b216761ef5b3b36aae";
    private String keyword;
    private List<ShowCity> cityList = new ArrayList<>();
    private EditText input;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        input = findViewById(R.id.input);
        listView = findViewById(R.id.addcity_listview);
        listView.setOnItemClickListener(AddCityActivity.this);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //主要实现该方法监听
                keyword = editable.toString().trim();
                if (keyword.length() >= 2) {
                    getCity(keyword);
                }
            }

        });
    }


    //搜索城市
    public void getCity(String location) {
        String url = "https://restapi.amap.com/v3/config/district?key=" + key + "&keywords=" + location + "&subdistrict=1&extensions=base";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String city = response.body().string();
                JSONObject jsonObject = JSONObject.fromObject(city);
                //循环获取0-6天的天气数据
                JSONArray lifeJson = jsonObject.getJSONArray("districts");
                Log.d("S", String.valueOf(lifeJson.size()));

                //去除上次结果
                cityList.clear();
                for (int i = 0; i < lifeJson.size(); i++) {
                    ShowCity showCity = new ShowCity();
                    showCity.setCity(lifeJson.getJSONObject(i).getString("name"));
                    showCity.setLocation(lifeJson.getJSONObject(i).getString("center"));
                    cityList.add(showCity);
                    //查询是否有子类json数据
                    String sonJson = lifeJson.getJSONObject(i).getString("districts");
                    if (!sonJson.equals("[]")) {
                        JSONArray sonCity = lifeJson.getJSONObject(i).getJSONArray("districts");
                        for (int j = 0; j < sonCity.size(); j++) {
                            ShowCity showCity2 = new ShowCity();
                            showCity2.setCity(sonCity.getJSONObject(j).getString("name"));
                            showCity2.setLocation(sonCity.getJSONObject(j).getString("center"));
                            cityList.add(showCity2);
                        }
                    }

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AddCityAdapter addCityAdapter = new AddCityAdapter(AddCityActivity.this, R.layout.addcity_item, cityList);
                        listView.setAdapter(addCityAdapter);
                    }
                });

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //数据库类
        final String clickCity = cityList.get(position).getCity();
        final String location = cityList.get(position).getLocation();
        new AlertDialog.Builder(AddCityActivity.this)
                .setTitle("提示")
                .setMessage("确定将" + clickCity + "添加至城市列表中吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //判断该城市是否存在于数据库
                        final List<ShowCity> dateBaseCityList = DataSupport.findAll(ShowCity.class);
                        if (dateBaseCityList.size()==0){
                            ShowCity add = new ShowCity();
                            add.setCity(clickCity);
                            add.setLocation(location);
                            boolean res=add.save();
                            if (res){
                                Toast.makeText(AddCityActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
                                //成功则跳转至MainActivity
                                Intent intent=new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setClass(AddCityActivity.this,MainActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(AddCityActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            boolean status=true;
                            for (int i = 0; i < dateBaseCityList.size(); i++) {
                                if (dateBaseCityList.get(i).getCity().equals(clickCity)) {
                                    Toast.makeText(AddCityActivity.this, "你已经添加过" + clickCity + "了", Toast.LENGTH_SHORT).show();
                                    status=false;
                                    break;
                                }
                            }
                            if (status){
                                ShowCity add2 = new ShowCity();
                                add2.setCity(clickCity);
                                add2.setLocation(location);
                                boolean res=add2.save();
                                if (res){
                                    Toast.makeText(AddCityActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(AddCityActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create().show();
    }
}