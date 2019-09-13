package com.example.datausage.Network.Model;

import java.util.List;

public class ResultForDataUsage {
    final String resource_id;
    final List<DataUsageRecord> records;

    public ResultForDataUsage(String resource_id, List<DataUsageRecord> records) {
        this.resource_id = resource_id;
        this.records = records;
    }

    public String getResource_id() {
        return resource_id;
    }

    public List<DataUsageRecord> getRecords() {
        return records;
    }
}
