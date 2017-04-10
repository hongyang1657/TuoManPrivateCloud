package com.liberal.young.tuomanprivatecloud.bean;

/**
 * Created by Administrator-19:45 on 2017/4/10.
 * Description:
 * Blog：www.qiuchengjia.cn
 * Author: Young_H
 */
/**
 * Created by Administrator-19:33 on 2017/4/10.
 * Description: 机床json数据
 * Author: Young_H
 */
public class MachineResult {
    private int id;
    private String name;
    private String workshop;
    private int companyId;
    private int forecast;
    private String mac;
    private int status;
    private String channelId;
    private int userTop;
    private String closeTime;
    private String operableStart;
    private String operableEnd;
    private boolean linkStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkshop() {
        return workshop;
    }

    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getForecast() {
        return forecast;
    }

    public void setForecast(int forecast) {
        this.forecast = forecast;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getUserTop() {
        return userTop;
    }

    public void setUserTop(int userTop) {
        this.userTop = userTop;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getOperableStart() {
        return operableStart;
    }

    public void setOperableStart(String operableStart) {
        this.operableStart = operableStart;
    }

    public String getOperableEnd() {
        return operableEnd;
    }

    public void setOperableEnd(String operableEnd) {
        this.operableEnd = operableEnd;
    }

    public boolean isLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(boolean linkStatus) {
        this.linkStatus = linkStatus;
    }
}
