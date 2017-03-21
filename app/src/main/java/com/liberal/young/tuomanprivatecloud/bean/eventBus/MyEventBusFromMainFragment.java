package com.liberal.young.tuomanprivatecloud.bean.eventBus;

/**
 * Created by Administrator on 2017/3/14.
 */

public class MyEventBusFromMainFragment {
    private boolean isOnDeleteState = false;
    private boolean enterToDelete = false;

    public MyEventBusFromMainFragment(boolean isOnDeleteState,boolean enterToDelete){
        this.isOnDeleteState = isOnDeleteState;
        this.enterToDelete = enterToDelete;
    }

    public boolean isOnDeleteState() {
        return isOnDeleteState;
    }

    public boolean isEnterToDelete() {
        return enterToDelete;
    }
}
