package com.liberal.young.tuomanprivatecloud.bean;

import java.util.List;

/**
 * Created by Administrator-19:32 on 2017/4/10.
 * Description:  机床json类
 * Author: Young_H
 */
public class MachineResponse {
    private int code;
    private String msg;
    private List<MachineResult> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MachineResult> getResult() {
        return result;
    }

    public void setResult(List<MachineResult> result) {
        this.result = result;
    }
}
