package com.liberal.young.tuomanprivatecloud.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

/**
 * 生成json数据的工具类
 * Created by Administrator on 2017/3/22.
 */

public class JsonUtils {


    //登录或注销
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
    public static String deleteUser(List<Integer> idList, String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        try {
            for (int i=0;i<idList.size();i++){
                array.put(idList.get(i));
                L.i("hahahahha:"+idList.get(i));
            }
            data.put("id",array);

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
    public static String sendCode(String username){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("username",username);

            object.put("method","sendCode");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token","");
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

    /**
     * //机床更新
     * Update machine string.
     *
     * @param machineId     the machine id
     * @param userTop       the user top
     * @param closeTime     the close time
     * @param operableStart the operable start
     * @param operableEnd   the operable end
     * @param token         the token
     * @return the string
     */
    public static String updateMachine(int machineId,int userTop,String closeTime,String operableStart,String operableEnd,long forecast,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("machineId",machineId);
            data.put("userTop",userTop);
            data.put("closeTime",closeTime);
            data.put("operableStart",operableStart);
            data.put("operableEnd",operableEnd);
            data.put("forecast",forecast);

            object.put("method","updateMachine");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("更新机床数据json："+object.toString());
        return object.toString();
    }


    /**
     * 根据公司或员工分页查找机床
     * Page by company string.
     *
     * @param companyId the company id
     * @param pageNum   the page num
     * @param pageSize  the page size
     * @param token     the token
     * @return the string
     */
    public static String pageByCompany(String method,long companyId,int pageNum,int pageSize,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("companyId",companyId);
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
        L.i("根据公司分页查找机床json："+object.toString());
        return object.toString();
    }



    /**
     * //修改用户权限
     * Update role string.
     *
     * @param id     the id
     * @param roleId the role id 修改后的权限id
     * @param token  the token
     * @return the string
     */
    public static String updateRole(int id,int roleId,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("id",id);
            data.put("roleId",roleId);


            object.put("method","updateRole");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("按操作工查找生产线json："+object.toString());
        return object.toString();
    }


    //更新用户
    public static String updateUser(int id,String password,String phone,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("id",id);
            data.put("password",password);
            data.put("phone",phone);

            object.put("method","updateUser");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("更新用户json："+object.toString());
        return object.toString();
    }


    //------------机床开关机-----------
    public static String switchMachine(int[] machineIds,int status,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        try {
            for (int i=0;i<machineIds.length;i++){
                array.put(i,machineIds[i]);
            }

            data.put("machineIds",array);
            data.put("status",status);


            object.put("method","status");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("开关机床json："+object.toString());
        return object.toString();
    }


    //----------------获取产量信息--------------------
    public static String getYieldData(String method,int machineId,String month,String year,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("month",month);
            data.put("machineId",machineId);
            data.put("year",year);

            object.put("method",method);
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("请求产量json："+object.toString());
        return object.toString();
    }


    //绑定操作工
    public static String bindStaff(int machineId,int userId,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("userId",userId);
            data.put("machineId",machineId);

            object.put("method","bindStaff");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("绑定操作工json："+object.toString());
        return object.toString();
    }

    //------------------绑定机床（添加设备）-----------------------
    public static String bindMachine(String name,String workshop,String mac,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("name",name);
            data.put("workshop",workshop);
            data.put("mac",mac);

            object.put("method","bind");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("绑定机器json："+object.toString());
        return object.toString();
    }


    //查找公司下的车间
    public static String findWorkshops(long companyId,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("companyId",companyId);

            object.put("method","findWorkshops");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("查找公司下的车间json："+object.toString());
        return object.toString();
    }

    //获取表格数据（公司产量数据）
    public static String getCompanyData(long companyId,String month,String workshop,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("companyId",companyId);
            data.put("month",month);
            data.put("workshop",workshop);

            object.put("method","companyData");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("获取表格数据json："+object.toString());
        return object.toString();
    }

    //按车间查找
    public static String getPageByWorkshop(int companyId,String workshop,int pageNum,int pageSize,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("companyId",companyId);
            data.put("workshop",workshop);
            data.put("pageNum",pageNum);
            data.put("pageSize",pageSize);

            object.put("method","pageByWorkshop");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("获取表格数据json："+object.toString());
        return object.toString();
    }


    //意见反馈createSuggest
    public static String createSuggest(long userId,String description,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("userId",userId);
            data.put("description",description);

            object.put("method","createSuggest");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("意见反馈json："+object.toString());
        return object.toString();
    }


    //添加设备的时候用于让服务器识别设备的接口
    public static String getLinkStatus(String mac,String token){
        JSONObject data = new JSONObject();
        JSONObject object = new JSONObject();

        try {
            data.put("mac",mac);

            object.put("method","linkStatus");
            object.put("version","1.0");      //版本号
            object.put("client",2);        //表示android端
            object.put("time",getTimeStamp());
            object.put("token",token);
            object.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        L.i("getLinkStatus_json："+object.toString());
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

    //获取用户名
    public static String getUsername(String result){
        String token = null;
        JSONObject objResult = null;
        try {
            JSONObject object = new JSONObject(result);
            objResult = new JSONObject();
            objResult = object.getJSONObject("result");

            return objResult.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    //获取用户头像
    public static String getHeadUrl(String result){
        String token = null;
        JSONObject objResult = null;
        try {
            JSONObject object = new JSONObject(result);
            objResult = new JSONObject();
            objResult = object.getJSONObject("result");

            return objResult.getString("logo");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    //获取公司编号id
    public static int getCompanyId(String result){
        String token = null;
        JSONObject object = null;
        JSONObject objResult = null;
        int companyId = -1;
        try {
            object = new JSONObject(result);
            objResult = object.getJSONObject("result");
            companyId = objResult.getInt("companyId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return companyId;
    }

    public static int getId(String result){
        String token = null;
        JSONObject object = null;
        JSONObject objResult = null;
        int companyId = -1;
        try {
            object = new JSONObject(result);
            objResult = object.getJSONObject("result");
            companyId = objResult.getInt("id");
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
