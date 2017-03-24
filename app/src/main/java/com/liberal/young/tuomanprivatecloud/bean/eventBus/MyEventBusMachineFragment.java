package com.liberal.young.tuomanprivatecloud.bean.eventBus;

/**
 * Created by Administrator on 2017/3/24.
 */

public class MyEventBusMachineFragment {

    private boolean isBatching = false;
    private boolean isEnterToStart = false;

    public MyEventBusMachineFragment(boolean isBatching, boolean isEnterToStart) {
        this.isBatching = isBatching;
        this.isEnterToStart = isEnterToStart;
    }

    public boolean isBatching() {
        return isBatching;
    }

    public boolean isEnterToStart() {
        return isEnterToStart;
    }
}
