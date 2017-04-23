package com.liberal.young.tuomanprivatecloud.bean.eventBus;

import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class MyEventBusToMonthChart {
    private List<String> monthDateList;
    private List<Integer> monthYieldList;  //每日产量
    private List<Integer> monthVoltageList;  //每日的运行时间

    public MyEventBusToMonthChart(List<String> monthDateList, List<Integer> monthYieldList, List<Integer> monthVoltageList) {
        this.monthDateList = monthDateList;
        this.monthYieldList = monthYieldList;
        this.monthVoltageList = monthVoltageList;
    }

    public List<String> getMonthDateList() {
        return monthDateList;
    }

    public List<Integer> getMonthYieldList() {
        return monthYieldList;
    }

    public List<Integer> getMonthVoltageList() {
        return monthVoltageList;
    }
}
