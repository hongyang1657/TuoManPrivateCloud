package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Administrator on 2017/3/16.
 */

public class MonthYieldFragment extends Fragment{

    private ColumnChartView columnChartView;
    private ColumnChartData data;
    private TextView tvMonthYield;
    private int[] temp = {100,200,100,60,130};
    private int[] runTimeOnMonth = {120,130,153,100,90};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_yield_fgm_layout,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        columnChartView = (ColumnChartView) view.findViewById(R.id.colume_chart);
        tvMonthYield = (TextView) view.findViewById(R.id.tv_month_yield);
        initChart();
    }

    private void initChart(){
        columnChartView.setViewportCalculationEnabled(false);
        Viewport v = new Viewport(columnChartView.getMaximumViewport());
        v.top = 260;
        v.right = 10;
        v.bottom = 0;
        columnChartView.setZoomEnabled(true);
        columnChartView.setZoomType(ZoomType.HORIZONTAL);
        columnChartView.setZoomLevel(2f, 4f, 4f);
        columnChartView.setMaxZoom(14f);
        columnChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        columnChartView.setMaximumViewport(v);
        columnChartView.setCurrentViewport(v);

        int[] colors=new int[]{Color.RED,Color.BLUE,Color.YELLOW,Color.BLACK};
        //columnChartView.setYRangeColors(colors);
        //columnChartView.setYAxisRange(50);
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values = null;
        List<SubcolumnValue> values1 = null;
        List<AxisValue> mvalues = new ArrayList<>();

        for (int i = 0; i <= 250; i += 50) {
            AxisValue value = new AxisValue(i);
            String label = i + "";
            value.setLabel(label);
            mvalues.add(value);
        }
        for (int i = 0; i < temp.length; ++i) {
            values = new ArrayList<>();
            values1 = new ArrayList<>();
            values.add(new SubcolumnValue(temp[i], getResources().getColor(R.color.colorTuomanRed)));
            values1.add(new SubcolumnValue(runTimeOnMonth[i],getResources().getColor(R.color.colorBlueShade)));
            Column column = new Column(values);
            Column column1 = new Column(values1);

            //column.setHasLabels(hasLabels);
            //column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
            columns.add(column1);
        }
        data = new ColumnChartData(columns);
            Axis axisX = new Axis();
            axisX.setValues(mvalues);
            axisX.setTextSize(10);
            axisX.setTextColor(Color.BLACK);
            axisX.setTypeface(Typeface.MONOSPACE);
            axisX.setHasTiltedLabels(true);
            axisX.setMaxLabelChars(3);
            Axis axisY = new Axis().setHasLines(false);
            axisY.setMaxLabelChars(3);//max label length, for example 60
            axisY.setTextColor(Color.BLACK);
            axisY.setTextSize(10);
            List<AxisValue> Yvalues = new ArrayList<>();
            for (int i = 0; i <=260; i += 50) {
                AxisValue value = new AxisValue(i);
                String label = i + "";
                value.setLabel(label);
                Yvalues.add(value);
            }
            axisY.setValues(Yvalues);
                axisX.setName("时间");
                axisY.setName("温度");
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
//设置条形上面的label颜色
        data.setValueLabelBackgroundAuto(true); // disable auto color
//
        data.setValueLabelBackgroundEnabled(true); // disable background color
        data.setValueLabelBackgroundColor(Color.YELLOW);

        columnChartView.setColumnChartData(data);
    }
}
