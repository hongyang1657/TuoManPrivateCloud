package com.liberal.young.tuomanprivatecloud.utils;

import okhttp3.MediaType;

/**
 * Created by Administrator on 2017/3/8.
 */

public class MyConstant {

    //title两边显示的类型：图片/文字
    public static final int TITLE_TYPE_TEXT = 1;
    public static final int TITLE_TYPE_IMG = 2;

    public static final String SERVER_URL = "http://115.29.172.223:8080/machine/api";  //服务器地址
    public static final String UPLOAD_PHOTO_URL = "http://115.29.172.223:8080/machine/logo";  //上传图片地址
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

}
