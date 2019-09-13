package com.example.datausage.Network.Model;

import com.google.gson.annotations.SerializedName;

public class DataUsageRecord {
    @SerializedName("volume_of_mobile_data")
    private String volumeOfMobileData;

    @SerializedName("quarter")
    private String quarter;

    @SerializedName("_id")
    private int id;

    public DataUsageRecord(String volumeOfMobileData, String quarter, int id) {
        this.volumeOfMobileData = volumeOfMobileData;
        this.quarter = quarter;
        this.id = id;
    }

    public String getVolumeOfMobileData() {
        return volumeOfMobileData;
    }

    public String getQuarter() {
        return quarter;
    }

    public int getId() {
        return id;
    }

}
