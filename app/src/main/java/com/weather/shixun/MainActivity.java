package com.weather.shixun;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.weather.shixun.Adapter.ForecastAdapter;
import com.weather.shixun.Adapter.ForecastByHourAdapter;
import com.weather.shixun.Adapter.LifeAdapter;
import com.weather.shixun.Adapter.MyAxisValueFormatter;
import com.weather.shixun.Adapter.XAxisValueFormatter;
import com.weather.shixun.Adapter.XAxisValueFormatterByHour;
import com.weather.shixun.Bean.Forecast;
import com.weather.shixun.Bean.ForecastByHour;
import com.weather.shixun.Bean.Life;
import com.weather.shixun.Bean.ShowCity;
import com.weather.shixun.OtherActivity.AddCityActivity;
import com.weather.shixun.OtherActivity.ShowCityActivity;
import com.weather.shixun.util.GetWeatherType;
import com.weather.shixun.util.SetBackground;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private static final String key = "3086e91d66c04ce588a7f538f917c7f4";
    private static final String unit = "°";
    private TextView City;
    private TextView Weather;
    private TextView Temp;
    private TextView Wind_speed;
    private TextView Direction;
    private TextView AirCon;
    private TextView AirLev;
    private String weatherData;
    private ListView listView1;
    private ListView listView2;
    private ListView listView3;
    private ImageButton addCity;
    private LineChart lineChart;
    private LineChart lineChart2;
    private TextView day1;
    private TextView day2;
    private TextView day3;
    private TextView day4;
    private TextView day5;
    private TextView day6;
    private TextView day7;

    private TextView time1;
    private TextView time2;
    private TextView time3;
    private TextView time4;
    private TextView time5;
    private TextView time6;
    private TextView time7;
    private TextView time8;


    private List<Forecast> forecastList = new ArrayList<>();
    private List<ForecastByHour> forecastByHourList = new ArrayList<>();
    private List<Life> lifeList = new ArrayList<>();




    //用于设置折线图从最低温度开始画,而不是从0开始
    private float min_tmp;
    private float min_tmp_hour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HeConfig.init("HE2006221928061531", "7c1f5d859b0b4179b1da04c1476c181a");

        City = findViewById(R.id.city);
        Weather = findViewById(R.id.weather);
        Temp = findViewById(R.id.temp);
        Wind_speed = findViewById(R.id.wind_speed);
        Direction = findViewById(R.id.direction);
        AirCon = findViewById(R.id.air);
        AirLev = findViewById(R.id.level);
        listView1 = findViewById(R.id.list_view);
        listView2 = findViewById(R.id.list_forecastByHour);
        listView3 = findViewById(R.id.list_life_view);
        addCity = findViewById(R.id.selectcity);
        day1 = findViewById(R.id.day_01);
        day2 = findViewById(R.id.day_02);
        day3 = findViewById(R.id.day_03);
        day4 = findViewById(R.id.day_04);
        day5 = findViewById(R.id.day_05);
        day6 = findViewById(R.id.day_06);
        day7 = findViewById(R.id.day_07);
        time1 = findViewById(R.id.time_01);
        time2 = findViewById(R.id.time_02);
        time3 = findViewById(R.id.time_03);
        time4 = findViewById(R.id.time_04);
        time5 = findViewById(R.id.time_05);
        time6 = findViewById(R.id.time_06);
        time7 = findViewById(R.id.time_07);
        time8 = findViewById(R.id.time_08);


        lineChart = findViewById(R.id.lineChart);
        lineChart.setNoDataText("暂无数据");
        lineChart.setNoDataTextColor(Color.WHITE);

        lineChart2 = findViewById(R.id.lineChart2);
        lineChart2.setNoDataText("暂无数据");
        lineChart2.setNoDataTextColor(Color.WHITE);


        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NotNull RefreshLayout refreshlayout) {

                refreshlayout.finishRefresh(2000);
            }
        });
        //自动刷新
        refreshLayout.autoRefresh();


        //进入添加城市界面
        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(MainActivity.this, ShowCityActivity.class));
                startActivity(intent);
            }
        });

    }

    public void getAllInfo(String location) {
        getNowWeather(MainActivity.this, location);
        getForecast(location);
        getHourForecast(location);
        getLife(location);
    }


    //请求当前城市的天气信息
    public void getNowWeather(Context context, String location) {
        HeConfig.switchToFreeServerNode();
        HeWeather.getWeatherNow(context, location, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable e) {
                Log.i("s", "Weather Now onError: ", e);
            }

            @Override
            public void onSuccess(Now dataObject) {
                //判断code是否正确
                if (Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus())) {
                    weatherData = new Gson().toJson(dataObject);
                    JsonParser parser = new JsonParser();
                    JsonObject object = (JsonObject) parser.parse(weatherData);

                    //获取两个嵌套json
                    JsonObject basic = object.get("basic").getAsJsonObject();
                    JsonObject nowWeather = object.get("now").getAsJsonObject();

                    //获取对于字符串
                    //获取父及省市
                    String parent_city = basic.get("parent_city").getAsString();

                    String location = basic.get("location").getAsString();
                    String weather = nowWeather.get("cond_txt").getAsString();
                    String tmp = nowWeather.get("tmp").getAsString() + unit;

                    String wind_speed = nowWeather.get("wind_spd").getAsString();
                    String direction = nowWeather.get("wind_dir").getAsString();
                    String level = nowWeather.get("wind_sc").getAsString() + "级";

                    City.setText(location);
                    //根据天气和时间设置背景图片
                    getWindow().setBackgroundDrawableResource(SetBackground.setBackground(weather));
                    Weather.setText(weather);
                    Temp.setText(tmp);
                    Wind_speed.setText(wind_speed);
                    Direction.setText(direction + level);

                    getAir(parent_city);

                } else {
                    //失败代码
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.d("s", "failed code: " + code);
                }
            }
        });
    }


    //请求当前城市的未来7天天气
    public void getForecast(String location) {
        String url = "https://free-api.heweather.net/s6/weather/forecast?key=" + key + "&location=" + location + "";
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
                String weatherForecast = response.body().string();
                JSONObject jsonObject = JSONObject.fromObject(weatherForecast);
                //提取出HeWeather6
                String HeWeather6 = jsonObject.getString("HeWeather6");
                //提取[中的内容]
                String HeWeather6Json = HeWeather6.substring(1, HeWeather6.length() - 1);
                //转为HeWeather6 json对象
                JSONObject heWeather6 = JSONObject.fromObject(HeWeather6Json);
                //循环获取0-6天的天气数据
                JSONArray forecastJson = heWeather6.getJSONArray("daily_forecast");
                //去除上次结果
                forecastList.clear();
                for (int i = 0; i < forecastJson.size(); i++) {
                    Forecast forecastBean = new Forecast();
                    forecastBean.setData(forecastJson.getJSONObject(i).getString("date"));
                    forecastBean.setMax_temp(forecastJson.getJSONObject(i).getString("tmp_max"));
                    forecastBean.setMin_temp(forecastJson.getJSONObject(i).getString("tmp_min"));
                    forecastBean.setWeather(forecastJson.getJSONObject(i).getString("cond_txt_d"));
                    forecastList.add(forecastBean);

                    min_tmp = Float.parseFloat(forecastJson.getJSONObject(0).getString("tmp_min"));
                    if ((min_tmp > Float.parseFloat(forecastJson.getJSONObject(i).getString("tmp_min")))) {
                        min_tmp = Float.parseFloat(forecastJson.getJSONObject(i).getString("tmp_min"));
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ForecastAdapter forecastAdapter = new ForecastAdapter(MainActivity.this, R.layout.item, forecastList);
                        listView1.setAdapter(forecastAdapter);
                        initForecastByDayChart();

                        day1.setText(forecastList.get(0).getWeather());
                        day2.setText(forecastList.get(1).getWeather());
                        day3.setText(forecastList.get(2).getWeather());
                        day4.setText(forecastList.get(3).getWeather());
                        day5.setText(forecastList.get(4).getWeather());
                        day6.setText(forecastList.get(5).getWeather());
                        day7.setText(forecastList.get(6).getWeather());
                    }
                });

            }
        });

    }


    //逐小时预报
    public void getHourForecast(String location) {
        String url = "https://free-api.heweather.net/s6/weather/hourly?key=" + key + "&location=" + location + "";
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
                String weatherForecast = response.body().string();
                JSONObject jsonObject = JSONObject.fromObject(weatherForecast);
                //提取出HeWeather6
                String HeWeather6 = jsonObject.getString("HeWeather6");
                //提取[中的内容]
                String HeWeather6Json = HeWeather6.substring(1, HeWeather6.length() - 1);
                //转为HeWeather6 json对象
                JSONObject heWeather6 = JSONObject.fromObject(HeWeather6Json);
                //循环获取8小时的数据
                final JSONArray hourly = heWeather6.getJSONArray("hourly");
                //去除上次结果
                forecastByHourList.clear();
                for (int i = 0; i < hourly.size(); i++) {
                    ForecastByHour forecastByHourBean = new ForecastByHour();
                    forecastByHourBean.setDate(hourly.getJSONObject(i).getString("time"));
                    forecastByHourBean.setTemp(hourly.getJSONObject(i).getString("tmp"));
                    forecastByHourBean.setWeather(hourly.getJSONObject(i).getString("cond_txt"));
                    forecastByHourList.add(forecastByHourBean);

                    min_tmp_hour = Float.parseFloat(hourly.getJSONObject(0).getString("tmp"));
                    if ((min_tmp_hour > Float.parseFloat(hourly.getJSONObject(i).getString("tmp")))) {
                        min_tmp_hour = Float.parseFloat(hourly.getJSONObject(i).getString("tmp"));
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ForecastByHourAdapter forecastByHourAdapter = new ForecastByHourAdapter(MainActivity.this, R.layout.byhouritem, forecastByHourList);
                        listView2.setAdapter(forecastByHourAdapter);
                        initForecastByHourChart();

                        time1.setText(forecastByHourList.get(0).getWeather());
                        time2.setText(forecastByHourList.get(1).getWeather());
                        time3.setText(forecastByHourList.get(2).getWeather());
                        time4.setText(forecastByHourList.get(3).getWeather());
                        time5.setText(forecastByHourList.get(4).getWeather());
                        time6.setText(forecastByHourList.get(5).getWeather());
                        time7.setText(forecastByHourList.get(6).getWeather());
                        time8.setText(forecastByHourList.get(7).getWeather());
                    }
                });

            }
        });

    }

    //空气质量
    public void getAir(String location) {
        String url = "https://free-api.heweather.net/s6/air?key=" + key + "&location=" + location + "";
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
                String weatherForecast = response.body().string();
                JSONObject jsonObject = JSONObject.fromObject(weatherForecast);
                //提取出HeWeather6
                String HeWeather6 = jsonObject.getString("HeWeather6");
                //提取[中的内容]
                String HeWeather6Json = HeWeather6.substring(1, HeWeather6.length() - 1);
                //转为HeWeather6 json对象
                JSONObject heWeather6 = JSONObject.fromObject(HeWeather6Json);
                //获取air_now_city空气数据
                String air = heWeather6.getString("air_now_city");
                JSONObject airJson = JSONObject.fromObject(air);

                final String airCon = airJson.getString("aqi");
                final String airLev = airJson.getString("qlty");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AirCon.setText(airCon);
                        AirLev.setText(airLev);
                    }
                });
            }
        });

    }

    //生活建议
    public void getLife(String location) {
        String url = "https://free-api.heweather.net/s6/weather/lifestyle/?key=" + key + "&location=" + location + "";
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
                String weatherForecast = response.body().string();
                JSONObject jsonObject = JSONObject.fromObject(weatherForecast);
                //提取出HeWeather6
                String HeWeather6 = jsonObject.getString("HeWeather6");
                //提取[中的内容]
                String HeWeather6Json = HeWeather6.substring(1, HeWeather6.length() - 1);
                //转为HeWeather6 json对象
                JSONObject heWeather6 = JSONObject.fromObject(HeWeather6Json);
                //循环获取0-6天的天气数据
                JSONArray lifeJson = heWeather6.getJSONArray("lifestyle");
                //去除上次结果
                lifeList.clear();
                for (int i = 0; i < lifeJson.size(); i++) {
                    Life lifeBean = new Life();
                    lifeBean.setTips(lifeJson.getJSONObject(i).getString("brf"));
                    lifeBean.setWhy(lifeJson.getJSONObject(i).getString("txt"));
                    String type = lifeJson.getJSONObject(i).getString("type");
                    //由于原json数据非中文,通过工具类转为中文
                    lifeBean.setType(GetWeatherType.getWeatherType(type));

                    lifeList.add(lifeBean);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LifeAdapter lifeAdapter = new LifeAdapter(MainActivity.this, R.layout.life_item, lifeList);
                        listView3.setAdapter(lifeAdapter);
                    }
                });

            }
        });

    }

    /**
     * 设置7天预报数据
     */
    private void initForecastByDayChart() {
        lineChart.getDescription().setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lineChart.setBackgroundColor(Color.argb((float) 0, 00, 00, 00));
        }

        //自定义适配器，适配于X轴
        IAxisValueFormatter xAxisFormatter = new XAxisValueFormatter();

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(xAxisFormatter);

        //自定义适配器，适配于Y轴
        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setValueFormatter(custom);

        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(min_tmp - 2f);

        forecastByDayChartData();
    }

    /**
     * 设置7天预报数据
     */
    private void forecastByDayChartData() {
        lineChart.setScaleEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.setDrawBorders(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.setHighlightPerTapEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setTextColor(Color.argb(255, 255, 255, 255));
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);


        //填充数据，在这里换成自己的数据源
        List<Entry> valsComp1 = new ArrayList<>();
        List<Entry> valsComp2 = new ArrayList<>();
        for (int i = 0; i < forecastList.size(); i++) {
            //获取 最高温
            valsComp1.add(new Entry(i, Float.parseFloat(forecastList.get(i).getMax_temp())));
            //获取最低温
            valsComp2.add(new Entry(i, Float.parseFloat(forecastList.get(i).getMin_temp())));
        }

        //这里，每重新new一个LineDataSet，相当于重新画一组折线
        //每一个LineDataSet相当于一组折线。比如:这里有两个LineDataSet：setComp1，setComp2。
        //则在图像上会有两条折线图，分别表示公司1 和 公司2 的情况.还可以设置更多
        LineDataSet setComp1 = new LineDataSet(valsComp1, "最高温");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setValueTextColor(Color.argb(255, 255, 255, 255));
        setComp1.setColor(Color.argb(255, 255, 255, 255));
        setComp1.setValueTextSize(12);
        setComp1.setDrawCircles(false);
        //将数值转为整数并且增加度数
        setComp1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                String str = value + "";
                if (str.length() == 0) {
                    return str;
                }
                return str.substring(0, str.indexOf(".")) + "°";//设置自己的返回位数
            }
        });
        setComp1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        LineDataSet setComp2 = new LineDataSet(valsComp2, "最低温");
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp2.setValueTextColor(Color.argb(255, 255, 255, 255));
        setComp2.setValueTextSize(12);
        setComp2.setDrawCircles(false);
        setComp2.setColor(Color.argb(255, 255, 255, 255));
        //将数值转为整数并且增加度数
        setComp2.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                String str = value + "";
                if (str.length() == 0) {
                    return str;
                }
                return str.substring(0, str.indexOf(".")) + "°";//设置自己的返回位数
            }
        });
        setComp2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);

        LineData lineData = new LineData(dataSets);

        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    /**
     * 设置3小时 预报数据
     */
    private void initForecastByHourChart() {
        lineChart2.getDescription().setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lineChart2.setBackgroundColor(Color.argb((float) 0, 00, 00, 00));
        }

        //自定义适配器，适配于X轴
        IAxisValueFormatter xAxisFormatter = new XAxisValueFormatterByHour();

        XAxis xAxis = lineChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(xAxisFormatter);

        //自定义适配器，适配于Y轴
        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = lineChart2.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setValueFormatter(custom);

        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(min_tmp_hour - 3f);

        forecastByHourChartData();
    }

    /**
     * 设置 3小时预报数据
     */
    private void forecastByHourChartData() {
        lineChart2.setScaleEnabled(false);
        lineChart2.getXAxis().setDrawGridLines(false);
        lineChart2.getAxisLeft().setDrawGridLines(false);
        lineChart2.getAxisLeft().setDrawLabels(false);
        lineChart2.setDrawBorders(false);
        lineChart2.getAxisLeft().setDrawAxisLine(false);
        lineChart2.setHighlightPerTapEnabled(false);
        lineChart2.setHighlightPerDragEnabled(false);
        lineChart2.getXAxis().setDrawAxisLine(false);
        lineChart2.getXAxis().setTextColor(Color.argb(255, 255, 255, 255));
        lineChart2.getLegend().setEnabled(false);
        lineChart2.getAxisRight().setEnabled(false);


        //填充数据，在这里换成自己的数据源
        List<Entry> valsComp1 = new ArrayList<>();
        for (int i = 0; i < forecastByHourList.size(); i++) {
            //获取当前温度
            valsComp1.add(new Entry(i, Float.parseFloat(forecastByHourList.get(i).getTemp())));
        }

        //这里，每重新new一个LineDataSet，相当于重新画一组折线
        //每一个LineDataSet相当于一组折线。比如:这里有两个LineDataSet：setComp1，setComp2。
        //则在图像上会有两条折线图，分别表示公司1 和 公司2 的情况.还可以设置更多
        LineDataSet setComp1 = new LineDataSet(valsComp1, "最高温");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setValueTextColor(Color.argb(255, 255, 255, 255));
        setComp1.setColor(Color.argb(255, 255, 255, 255));
        setComp1.setValueTextSize(12);
        setComp1.setCircleColor(Color.argb(225, 225, 225, 225));
        setComp1.setDrawCircles(true);
        //将数值转为整数并且增加度数
        setComp1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                String str = value + "";
                if (str.length() == 0) {
                    return str;
                }
                return str.substring(0, str.indexOf(".")) + "°";//设置自己的返回位数
            }
        });
        setComp1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);


        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setComp1);

        LineData lineData = new LineData(dataSets);

        lineChart2.setData(lineData);
        lineChart2.invalidate();
    }

    protected void onStart() {
        super.onStart();
        List<ShowCity> all = DataSupport.findAll(ShowCity.class);
        int dataBaseSize = all.size();
        Log.d("s", String.valueOf(dataBaseSize));
        if (dataBaseSize == 0) {
            //跳转到添加城市界面
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(MainActivity.this, AddCityActivity.class));
            startActivity(intent);
            Toast.makeText(MainActivity.this, "请添加至少一个城市!", Toast.LENGTH_SHORT).show();
        } else {
            //数据库不为零则说明有城市,则读取第一个城市
            String location = all.get(0).getLocation();
            getAllInfo(location);
        }
    }

    protected void onResume() {
        super.onResume();
        List<ShowCity> all = DataSupport.findAll(ShowCity.class);
        int dataBaseSize = all.size();
        if (dataBaseSize == 0) {
            //跳转到添加城市界面
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(MainActivity.this, AddCityActivity.class));
            startActivity(intent);
        } else {
//                        数据库不为零则说明有城市,则读取第一个城市
            ShowCity firstCity = DataSupport.findFirst(ShowCity.class);
            String location = firstCity.getLocation();
            getAllInfo(location);
        }
    }


}

