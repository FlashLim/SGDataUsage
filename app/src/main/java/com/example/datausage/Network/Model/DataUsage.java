package com.example.datausage.Network.Model;

public class DataUsage {
    private boolean success;
    private ResultForDataUsage result;

    public DataUsage(boolean success, ResultForDataUsage result) {
        this.success = success;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResultForDataUsage getResult() {
        return result;
    }

    public void setResult(ResultForDataUsage result) {
        this.result = result;
    }
}
