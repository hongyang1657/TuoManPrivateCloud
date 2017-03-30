package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;

import java.util.ArrayList;
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

    String[] data = {"1","2","3","4","5","6","7"};      // 日期 （X轴的刻度）
    int[] yieldList = {50,42,90,33,10,74,22};        //    一周的日产量数组（Y轴的刻度）
    int[] RunTimeList = {2,5,11,13,4,6,8};        //    一周的日产量数组（Y轴的刻度）
    private List<PointValue> mPointValues1 = new ArrayList<PointValue>();
    private List<PointValue> mPointValues2 = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    private boolean isShowingDate1 = false;
    private boolean isShowingDate2 = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.day_yield_fgm_layout, container, false);
        initView(view);
        ButterKnife.bind(this, view);
        return view;

    }

    private void initView(View view) {
        lineChartYield = (LineChartView) view.findViewById(R.id.line_chart_yield);
        lineChartRunTime = (LineChartView) view.findViewById(R.id.line_chart_run_time);
        lineChartYield.setOnClickListener(clickListener);
        lineChartRunTime.setOnClickListener(clickListener);
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        getAxisPoints2();
        initLineChart(lineChartYield,mPointValues1);//初始化
        initLineChart2(lineChartRunTime,mPointValues2);
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
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
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
        v.top = 100;    //坐标轴
        v.right = 7;
        v.bottom = 0;
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
        v.top = 20;    //坐标轴
        v.right = 7;
        v.bottom = 0;
        lineChartView.setZoomEnabled(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setZoomLevel(2f, 4f, 4f);
        lineChartView.setMaxZoom(2f);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setMaximumViewport(v);
        lineChartView.setCurrentViewport(v);

    }
}
