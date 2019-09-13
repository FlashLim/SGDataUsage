package com.example.datausage.Util;

import com.example.datausage.DataModel.AnnualDataUsage;
import com.example.datausage.Network.Model.DataUsageRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Helper {
    public static String regex = "^(200[8-9]|201[0-8])";

    public static boolean validateYear(String year) {
        if (year != null && year.length() >3) {
            return Pattern.matches(regex, year.substring(0, 4));
        }
        return false;
    }

    public static List<AnnualDataUsage> filterRecords (List<DataUsageRecord> records) {
        HashMap<String, AnnualDataUsage> filteredRecords = new HashMap<>();
        List<AnnualDataUsage> list = new ArrayList<>(filteredRecords.values());

        if (records == null || records.size() == 0) {
            return list;
        }

        for (int i = 0; i<records.size(); i++) {
            String yearQuarter = records.get(i).getQuarter();
            if (Helper.validateYear(yearQuarter)) {
                String year = yearQuarter.substring(0, 4);
                if (filteredRecords.get(year) != null) {
                    filteredRecords.get(year).getQuarterRecords().add(records.get(i));
                    filteredRecords.get(year).setTotalMobileData(
                            filteredRecords.get(year).getTotalMobileData() + Double.parseDouble(records.get(i).getVolumeOfMobileData())
                    );
                } else {
                    List<DataUsageRecord> newYear = new ArrayList<>();
                    newYear.add(records.get(i));
                    filteredRecords.put(year, new AnnualDataUsage(year, newYear, Double.parseDouble(records.get(i).getVolumeOfMobileData())));
                }
            }
        }
        if (filteredRecords.size() > 0) {
            list.addAll(filteredRecords.values());

            Collections.sort(list, new Comparator<AnnualDataUsage>() {

                public int compare(AnnualDataUsage annual1, AnnualDataUsage annual2) {
                    // compare two instance of `Score` and return `int` as result.
                    return annual2.getYear().compareTo(annual1.getYear());
                }
            });
        }
        return list;
    }

    public static String round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return String.valueOf(bd.doubleValue());
    }
}
