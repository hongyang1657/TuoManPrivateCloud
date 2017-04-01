package com.liberal.young.tuomanprivatecloud.utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * 生成json数据的工具类
 * Created by Administrator on 2017/3/22.
 */

public class JsonUtils {


    public static String login(String username,String password,String method,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("username",username);
            data.put("password",password);

            object.put("method",method);
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("login："+object.toString());
        return object.toString();
    }

    //创建并添加客户
    public static String createUser(String username,String password,String phone,String token,int roleId){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("username",username);
            data.put("password",password);
            data.put("phone",phone);
            data.put("roleId",roleId);       //创建客户的等级


            object.put("method","createUser");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("添加客户json："+object.toString());
        return object.toString();
    }

    //分页查找
    public static String pageSearch(int pageNum,int pageSize,String method,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("pageNum",pageNum);
            data.put("pageSize",pageSize);


            object.put("method",method);
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("分页查找json："+object.toString());
        return object.toString();
    }

    //删除用户
    public static String deleteUser(int id,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("id",id);

            object.put("method","deleteUser");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("删除客户json："+object.toString());
        return object.toString();
    }

    //发送验证码
    public static String sendCode(String username,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("username",username);

            object.put("method","sendCode");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("发送验证码json："+object.toString());
        return object.toString();
    }

    //修改密码
    public static String changePassword(String methed,String username,String password,String newPassword,String code,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {

            if (!code.equals("")){
                data.put("username",username);
                data.put("password",newPassword);
                data.put("code",code);
            }else {
                data.put("password",password);
                data.put("newPassword",newPassword);
            }

            object.put("method",methed);
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("修改密码json："+object.toString());
        return object.toString();
    }

    //----------------------------------------------------------------------

    public static String getToken(String result){
        String token = null;
        JSONObject objResult = null;
        try {
            JSONObject object = new JSONObject(result);
            objResult = new JSONObject();
            objResult = object.getJSONObject("result");

            return objResult.getString("accessToken");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static int getRole(String result){
        String token = null;
        JSONObject objResult = null;
        try {
            JSONObject object = new JSONObject(result);
            objResult = new JSONObject();
            objResult = object.getJSONObject("result");

            return objResult.getInt("roleId");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getCode(String result){
        String token = null;
        JSONObject object = null;
        int code = -1;
        try {
            object = new JSONObject(result);
            code = object.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String getMsg(String result){
        String token = null;
        JSONObject object = null;
        String code = "";
        try {
            object = new JSONObject(result);
            code = object.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    //获取公司编号id
    public static int getCompanyId(String result){
        String token = null;
        JSONObject object = null;
        int companyId = -1;
        try {
            object = new JSONObject(result);
            companyId = object.getInt("companyId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return companyId;
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
