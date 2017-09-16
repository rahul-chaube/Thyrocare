package com.demo.model;

import java.util.List;

/**
 * Created by rahul on 16/9/17.
 */

public class TestModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public List<TestInfoModel> getData() {
        return data;
    }

    public void setData(List<TestInfoModel> data) {
        this.data = data;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    private String name;
    private long time;


    private String totalamount;
    private List<TestInfoModel> data;
    private int complete;
}
