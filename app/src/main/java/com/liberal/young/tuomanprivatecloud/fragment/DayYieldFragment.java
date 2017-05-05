package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.bean.eventBus.MyEventBusFromMainFragment;
import com.liberal.young.tuomanprivatecloud.bean.eventBus.MyEventBusToDayChart;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/16.
 */

public class DayYieldFragment extends Fragment {

    @BindView(R.id.tv_today_yield)
    TextView tvTodayYield;
    @BindView(R.id.tv_yestoday_yield)
    TextView tvYestodayYield;
    private LineChartView lineChartYield;
    private LineChartView lineChartRunTime;
    private LinearLayout llCharts;

    String[] data = {" "," "," "," "," "," "," "};      // 日期 （X轴的刻度）
    int[] yieldList = {0,0,0,0,0,0,0};        //    一周的日产量数组（Y轴的刻度）
    int[] RunTimeList = {0,0,0,0,0,0,0};        //    一周的运行时间数组（Y轴的刻度）
    private List<PointValue> mPointValues1 = new ArrayList<PointValue>();
    private List<PointValue> mPointValues2 = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    private boolean isShowingDate1 = false;
    private boolean isShowingDate2 = false;
    private int pointNum;   //有几条真实数据
    private int maxYAxis;   //产量Y轴最大值
    private int maxYAxisTime;  //运行时间

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.day_yield_fgm_layout, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View view) {
        llCharts = (LinearLayout) view.findViewById(R.id.ll_charts);
        tvTodayYield = (TextView) view.findViewById(R.id.tv_today_yield);
        tvYestodayYield = (TextView) view.findViewById(R.id.tv_yestoday_yield);
        lineChartYield = (LineChartView) view.findViewById(R.id.line_chart_yield);
        lineChartRunTime = (LineChartView) view.findViewById(R.id.line_chart_run_time);
        lineChartYield.setOnClickListener(clickListener);
        lineChartRunTime.setOnClickListener(clickListener);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.line_chart_yield:
                    if (!isShowingDate1){
                        line.setHasLabels(true);
                        isShowingDate1 = true;
                    }else {
                        line.setHasLabels(false);
                        isShowingDate1 = false;
                    }
                    break;
                case R.id.line_chart_run_time:
                    if (!isShowingDate2){
                        line2.setHasLabels(true);
                        isShowingDate2 = true;
                    }else {
                        line2.setHasLabels(false);
                        isShowingDate2 = false;
                    }
                    break;
            }
        }
    };


    private List<String> dayDateList;
    private List<Integer> dayYieldList;    //产量
    private List<Integer> dayVoltageList;  //运行时间（秒）
    @Subscribe
    public void onEventMainThread(MyEventBusToDayChart event) {
        dayDateList = event.getDayDateList();
        dayVoltageList = event.getDayVoltageList();
        dayYieldList = event.getDayYieldList();
        L.i("日11111："+dayDateList.get(0)+dayVoltageList.get(0)+dayYieldList.get(0));
        pointNum = dayDateList.size();
        if (pointNum<=7){
            for (int i=0;i<pointNum;i++){
                yieldList[6-i] = dayYieldList.get(pointNum-1-i);
                RunTimeList[6-i] = dayVoltageList.get(pointNum-1-i)/60;   //换算分钟
                data[6-i] = dayDateList.get(pointNum-1-i).substring(5);
            }
        }else {

        }
        tvTodayYield.setText(yieldList[6]+"件");
        tvYestodayYield.setText(yieldList[5]+"件");
        maxYAxis = getMax(yieldList);
        maxYAxisTime = getMax(RunTimeList);
        if (maxYAxis==0){
            maxYAxis = 100;
        }if (maxYAxisTime==0){
            maxYAxisTime = 100;
        }
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        getAxisPoints2();
        initLineChart(lineChartYield,mPointValues1);//初始化
        initLineChart2(lineChartRunTime,mPointValues2);
        llCharts.setVisibility(View.VISIBLE);
    }

    public static int getMax(int[] arr){
        int max=arr[0];
        for(int i=1;i<arr.length;i++){
            if(arr[i]>max){
                max=arr[i];
            }
        }
        return max;
    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables(){
        for (int i = 0; i < data.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(data[i]));
        }
    }

    /**
     * 图表1的每个点的显示
     */
    private void getAxisPoints(){
        for (int i = 0; i < yieldList.length; i++) {
            mPointValues1.add(new PointValue(i, yieldList[i]));
        }
    }

    /**
     * 图表2的每个点的显示
     */
    private void getAxisPoints2(){
        for (int i = 0; i < RunTimeList.length; i++) {
            mPointValues2.add(new PointValue(i, RunTimeList[i]));
        }
    }

    Line line;
    private void initLineChart(LineChartView lineChartView,List<PointValue> mPointValues){
        line = new Line(mPointValues).setColor(getResources().getColor(R.color.colorTuomanRed));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.SQUARE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        //line.setHasLabels(false);//曲线的数据坐标是否加上备注
        //line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setPointRadius(4);      //数据点的大小
        line.setFilled(false);   //是否有填充
        line.setPointColor(getResources().getColor(R.color.colorTuomanRed)); //设置点的颜色
        //line.setSquare(false);

        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);


        Axis axisX = new Axis();
        axisX.setTextSize(10);
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(getResources().getColor(R.color.colorAlpha));  //设置字体颜色
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        axisX.setHasLines(false); //x 轴分割线
        Axis axisY = new Axis().setHasLines(true);
        axisY.setTextColor(getResources().getColor(R.color.colorAlpha));  //设置字体颜色
        axisY.setMaxLabelChars(3);
        axisY.setHasLines(false);
        axisY.setTextSize(10);
        List<AxisValue> Yvalues = new ArrayList<>();
        for (int i = 0; i <= 250; i += 50) {
            AxisValue value = new AxisValue(i);
            String label = i + "";
            value.setLabel(label);
            Yvalues.add(value);
        }
        axisY.setValues(Yvalues);
        if (true) {
            //axisX.setName("时间");
            //axisY.setName("AQI");
            axisX.setMaxLabelChars(3);
        }
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        lineChartView.setLineChartData(data);

        lineChartView.setViewportCalculationEnabled(false);
        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.top = maxYAxis;    //坐标轴
        v.right = 7;
        v.bottom = -maxYAxis/10;
        lineChartView.setZoomEnabled(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setZoomLevel(2f, 4f, 4f);
        lineChartView.setMaxZoom(2f);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setMaximumViewport(v);
        lineChartView.setCurrentViewport(v);

    }

    Line line2;
    private void initLineChart2(LineChartView lineChartView,List<PointValue> mPointValues){
        line2 = new Line(mPointValues).setColor(getResources().getColor(R.color.colorBlueShade));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line2.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line2.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line2.setFilled(false);//是否填充曲线的面积
        line2.setHasLabels(false);//曲线的数据坐标是否加上备注
        //line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line2.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line2.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line2.setPointRadius(4);      //数据点的大小
        line2.setFilled(false);   //是否有填充
        line2.setPointColor(getResources().getColor(R.color.colorBlueShade)); //设置点的颜色
        //line.setSquare(false);

        lines.add(line2);
        LineChartData data = new LineChartData();
        data.setLines(lines);


        Axis axisX = new Axis();
        axisX.setTextSize(10);
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(getResources().getColor(R.color.colorText));  //设置字体颜色
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        axisX.setHasLines(false); //x 轴分割线
        Axis axisY = new Axis().setHasLines(true);
        axisY.setTextColor(getResources().getColor(R.color.colorAlpha));  //设置字体颜色
        axisY.setMaxLabelChars(3);
        axisY.setHasLines(false);
        axisY.setTextSize(10);
        List<AxisValue> Yvalues = new ArrayList<>();
        for (int i = 0; i <= 250; i += 50) {
            AxisValue value = new AxisValue(i);
            String label = i + "";
            value.setLabel(label);
            Yvalues.add(value);
        }
        axisY.setValues(Yvalues);
        if (true) {
            //axisX.setName("时间");
            //axisY.setName("AQI");
            axisX.setMaxLabelChars(3);
        }
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        lineChartView.setLineChartData(data);

        lineChartView.setViewportCalculationEnabled(false);
        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.top = maxYAxisTime;    //坐标轴
        v.right = 7;
        v.bottom = -maxYAxisTime/10;
        lineChartView.setZoomEnabled(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setZoomLevel(2f, 4f, 4f);
        lineChartView.setMaxZoom(2f);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setMaximumViewport(v);
        lineChartView.setCurrentViewport(v);

    }

    //获取时间戳 xxxx年xx月xx日
    private void getTimeStamp(){
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
    }

}
