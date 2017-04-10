package com.liberal.young.tuomanprivatecloud.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liberal.young.tuomanprivatecloud.bean.JsonResponse;
import com.liberal.young.tuomanprivatecloud.bean.MachineResponse;

/**
 *
 * 解析json的工具类
 * Created by Administrator on 2017/3/28.
 */

public class JsonParseUtil {
    private String json;

    public JsonParseUtil(String json) {
        this.json = json;
    }

    public JsonResponse parsePageSearchJson(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JsonResponse jsonResponse = gson.fromJson(json,JsonResponse.class);
        return jsonResponse;
    }

    public MachineResponse parseMachineSearchJson(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        MachineResponse machineResponse = gson.fromJson(json,MachineResponse.class);
        return machineResponse;
    }
}
