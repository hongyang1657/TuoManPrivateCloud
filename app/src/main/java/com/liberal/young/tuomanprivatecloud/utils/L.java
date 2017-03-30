package com.liberal.young.tuomanprivatecloud.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/3/26.
 */

public class L {

    private static boolean debug = true;
    private static final String TAG = "hy_debug_message";

    public static void i(String str){

        if (debug){
            Log.i(TAG, "Message: "+str);
        }
    }
}
