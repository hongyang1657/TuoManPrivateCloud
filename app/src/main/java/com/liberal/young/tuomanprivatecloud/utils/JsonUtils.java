package com.liberal.young.tuomanprivatecloud.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * 生成json数据的工具类
 * Created by Administrator on 2017/3/22.
 */

public class JsonUtils {

    public static String login(String username,String password){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("username",username);
            data.put("password",password);

            object.put("method","login");
            object.put("version","1.0");
            object.put("client",2);
            object.put("time",getTimeStamp());
            object.put("token","");
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
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
        return calendar.get(Calendar.YEAR)+""+mon + dayOfMon;
    }
}
