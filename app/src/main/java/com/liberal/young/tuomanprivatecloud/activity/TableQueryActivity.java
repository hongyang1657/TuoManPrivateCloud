package com.liberal.young.tuomanprivatecloud.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.bean.eventBus.MyEventBusToDayChart;
import com.liberal.young.tuomanprivatecloud.bean.eventBus.MyEventBusToMonthChart;
import com.liberal.young.tuomanprivatecloud.fragment.DayYieldFragment;
import com.liberal.young.tuomanprivatecloud.fragment.MainFragment;
import com.liberal.young.tuomanprivatecloud.fragment.MonthYieldFragment;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/16.
 */

public class TableQueryActivity extends BaseActivity {


    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_day)
    RadioButton tvDay;
    @BindView(R.id.tv_month)
    RadioButton tvMonth;
    @BindView(R.id.fl_blank_table)
    FrameLayout flBlankTable;

    private FragmentManager fm;
    private DayYieldFragment dayYieldFragment;
    private MonthYieldFragment monthYieldFragment;
    private int machineId;
    private MyApplication application;
    private WaitingDialog dialog;

    private List<String> dayDateList = new ArrayList<>();
    private List<Integer> dayYieldList = new ArrayList<>();    //每日产量
    private List<Integer> dayVoltageList = new ArrayList<>();  //每日的运行时间

    private List<String> monthDateList = new ArrayList<>();
    private List<Integer> monthYieldList = new ArrayList<>();    //每月产量
    private List<Integer> monthVoltageList = new ArrayList<>();  //每月的运行时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_query_layout);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        application = (MyApplication) getApplication();
        dialog = new WaitingDialog(this,application,"",false);
        machineId = getIntent().getIntExtra("machineId",-1);
        tvTitle.setText(getIntent().getStringExtra("titleName"));
        ivTitleLeft.setImageResource(R.mipmap.back);
        ivTitleRight.setImageResource(R.mipmap.more_title);
        fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        dayYieldFragment = new DayYieldFragment();
        monthYieldFragment = new MonthYieldFragment();
        transaction.replace(R.id.fl_blank_table, monthYieldFragment);
        transaction.add(R.id.fl_blank_table, dayYieldFragment);
        transaction.commit();

        doHttpMachineMonthData();

        dialog.waiting();
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right, R.id.tv_day, R.id.tv_month})
    public void onClick(View view) {
        FragmentTransaction transaction = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                break;
            case R.id.tv_day:
                transaction.hide(monthYieldFragment);
                transaction.show(dayYieldFragment);
                transaction.commit();
                break;
            case R.id.tv_month:
                transaction.hide(dayYieldFragment);
                transaction.show(monthYieldFragment);
                transaction.commit();
                break;
        }
    }

    //获取每日的产量和运行时间
    private int chartPointDayNum;     //数据的个数（天数）
    private void doHttpMachineMonthData() {
        String month = getTimeStamp();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.getYieldData("machineMonthData",machineId
                ,month,"",application.getAccessToken()));  //注销账号
        Request request = new Request.Builder()
                .url(MyConstant.SERVER_URL)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure:获取每日的产量和运行时间： " + e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.stopWaiting();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("hy_debug_message", "onResponse:获取每日的产量和运行时间： " + res);
                        doHttpMachineYearData();
                        dialog.stopWaiting();
                        try {
                            JSONObject object = new JSONObject(res);
                            JSONArray array = object.getJSONArray("result");
                            chartPointDayNum = array.length();
                            if (chartPointDayNum!=0){
                                for (int i=0;i<chartPointDayNum;i++){
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    dayDateList.add(i,jsonObject.getString("date"));
                                    dayVoltageList.add(i,jsonObject.getInt("voltage"));
                                    dayYieldList.add(i,jsonObject.getInt("yield"));
                                }
                                EventBus.getDefault().post(new MyEventBusToDayChart(dayDateList,dayYieldList,dayVoltageList));
                            }else {
                                //数据为0
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //获取每月的产量和运行时间
    private int chartPointMonthNum;
    private void doHttpMachineYearData() {
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR)+"";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.getYieldData("machineYearData",machineId
                ,"",year,application.getAccessToken()));  //注销账号
        Request request = new Request.Builder()
                .url(MyConstant.SERVER_URL)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure:获取每月的产量和运行时间： " + e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.stopWaiting();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("hy_debug_message", "onResponse:获取每月的产量和运行时间： " + res);
                        dialog.stopWaiting();
                        try {
                            JSONObject object = new JSONObject(res);
                            JSONArray array = object.getJSONArray("result");
                            chartPointMonthNum = array.length();
                            if (chartPointMonthNum!=0){
                                for (int i=0;i<chartPointMonthNum;i++){
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    monthDateList.add(i,jsonObject.getString("date"));
                                    monthVoltageList.add(i,jsonObject.getInt("voltage"));
                                    monthYieldList.add(i,jsonObject.getInt("yield"));
                                }
                                //把数据传到fragment
                                EventBus.getDefault().post(new MyEventBusToMonthChart(monthDateList,monthYieldList,monthVoltageList));
                            }else {
                                //数据为0
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    //获取时间戳 xxxx年xx月xx日
    private static String getTimeStamp(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        String mon;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayOfMon;
        if (month<10){
            mon = "0"+(month+1);
        }else {
            mon = String.valueOf(month+1);
        }
        if (day<10){
            dayOfMon = "0"+day;
        }else {
            dayOfMon = String.valueOf(day);
        }
        return calendar.get(Calendar.YEAR)+"-"+mon;
    }

}
