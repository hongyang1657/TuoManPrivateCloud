package com.liberal.young.tuomanprivatecloud.bean.eventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class MyEventBusToDayChart {
    private List<String> dayDateList;
    private List<Integer> dayYieldList;  //每日产量
    private List<Integer> dayVoltageList;  //每日的运行时间

    public MyEventBusToDayChart(List<String> dayDateList, List<Integer> dayYieldList, List<Integer> dayVoltageList) {
        this.dayDateList = dayDateList;
        this.dayYieldList = dayYieldList;
        this.dayVoltageList = dayVoltageList;
    }

    public List<String> getDayDateList() {
        return dayDateList;
    }

    public List<Integer> getDayYieldList() {
        return dayYieldList;
    }

    public List<Integer> getDayVoltageList() {
        return dayVoltageList;
    }
}
