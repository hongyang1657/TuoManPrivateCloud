package com.liberal.young.tuomanprivatecloud;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.WindowManager;

import com.liberal.young.tuomanprivatecloud.activity.BaseActivity;
import com.liberal.young.tuomanprivatecloud.activity.LoginActivity;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;

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
    private String accessToken;
    private String username;
    private String userHeadUrl;
    private long companyId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private Context context;
    private int width;
    private int height;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        activities = new LinkedList<>();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getUserLimits() {
        return UserLimits;
    }

    public void setUserLimits(String userLimits) {
        UserLimits = userLimits;
        SharedPreferences sharedPreferences = getSharedPreferences("LoginInformation",MODE_PRIVATE);
        sharedPreferences.edit().putString("userLimits",userLimits).commit();
        L.i("用户权限等级："+userLimits);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        L.i(accessToken);
        /*SharedPreferences sharedPreferences = getSharedPreferences("LoginInformation",MODE_PRIVATE);
        sharedPreferences.edit().putString("accessToken",accessToken).commit();*/
        //L.i("SharedPreferences:"+sharedPreferences.getString("accessToken","hehe"));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
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
