package com.example.datausage.DataModel;

import android.support.annotation.NonNull;

import com.example.datausage.Network.Model.DataUsageRecord;

import java.util.List;

public class AnnualDataUsage {

    private List<DataUsageRecord> quarterRecords;

    private double totalMobileData;

    private String year;

    public AnnualDataUsage(String year, List<DataUsageRecord> quarterRecords, double totalMobileData) {
        this.year = year;
        this.quarterRecords = quarterRecords;
        this.totalMobileData = totalMobileData;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<DataUsageRecord> getQuarterRecords() {
        return quarterRecords;
    }

    public void setQuarterRecords(List<DataUsageRecord> quarterRecords) {
        this.quarterRecords = quarterRecords;
    }

    public double getTotalMobileData() {
        return totalMobileData;
    }

    public void setTotalMobileData(double totalMobileData) {
        this.totalMobileData = totalMobileData;
    }

}
