package com.liberal.young.tuomanprivatecloud.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/28.
 */

public class JsonResponse {
    private int code;
    private String msg;
    private List<JsonResult> result;

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<JsonResult> getResult() {
        return result;
    }

    public void setResult(List<JsonResult> result) {
        this.result = result;
    }


}
