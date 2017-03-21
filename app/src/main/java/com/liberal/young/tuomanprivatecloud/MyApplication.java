package com.liberal.young.tuomanprivatecloud;

import android.app.Application;
import android.content.Context;

import com.liberal.young.tuomanprivatecloud.activity.BaseActivity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2017/3/8.
 */

public class MyApplication extends Application{

    private static MyApplication myApplication;
    private static List<BaseActivity> activities;
    private static Map<String,Object> intentData = new HashMap<String,Object>();
    private static Map<String,Object> cacheData = new HashMap<String,Object>();
    private String UserLimits;    //设置一个全局的变量，用于判断登录的账号是什么权限

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        activities = new LinkedList<>();
        setUserLimits("0");
    }

    public String getUserLimits() {
        return UserLimits;
    }

    public void setUserLimits(String userLimits) {
        UserLimits = userLimits;
    }

    /**
     * 获取application
     *
     * @return
     */
    public static Context getApplication() {
        return myApplication;
    }

    /**
     * 添加一个Activity
     *
     * @param activity
     */
    public void addActivity(BaseActivity activity) {
        activities.add(activity);
    }

    /**
     * 结束一个Activity
     *
     * @param activity
     */
    public void removeActivity(BaseActivity activity) {
        activities.remove(activity);
    }

    /**
     * 结束当前所有Activity
     */
    public static void clearActivities() {
        ListIterator<BaseActivity> iterator = activities.listIterator();
        BaseActivity activity;
        while (iterator.hasNext()) {
            activity = iterator.next();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 退出应运程序
     */
    public static void quiteApplication() {
        clearActivities();
        System.exit(0);
    }
}
