package com.example.datausage.Util;

import com.example.datausage.DataModel.AnnualDataUsage;
import com.example.datausage.Network.Model.DataUsageRecord;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class HelperTest {
    @Test
    public void regexYears1998() {
        String year = "1998";
        assertFalse(Helper.validateYear(year));
    }

    @Test
    public void regexYears2008() {
        String year = "2008";
        assertTrue(Helper.validateYear(year));
    }

    @Test
    public void regexYears2018() {
        String year = "2018";
        assertTrue(Helper.validateYear(year));
    }

    @Test
    public void regexYearsNull() {
        assertFalse(Helper.validateYear(null));
    }

    @Test
    public void regexYearsEmptyString() {
        assertFalse(Helper.validateYear(""));
    }

    @Test
    public void filterRecordsCorrectResult() {
        DataUsageRecord dataUsageRecord2004 = new DataUsageRecord("1.0023", "2004 - Q1", 1);
        DataUsageRecord dataUsageRecord2008 = new DataUsageRecord("1.0023", "2008 - Q1", 2);
        DataUsageRecord dataUsageRecord2008Q2 = new DataUsageRecord("1.0040", "2008 - Q2", 3);
        DataUsageRecord dataUsageRecord2018 = new DataUsageRecord("10.0234", "2018 - Q1", 4);

        List<DataUsageRecord> dataUsageRecordList = new ArrayList<>();
        dataUsageRecordList.add(dataUsageRecord2004);
        dataUsageRecordList.add(dataUsageRecord2008);
        dataUsageRecordList.add(dataUsageRecord2008Q2);
        dataUsageRecordList.add(dataUsageRecord2018);

        List<DataUsageRecord> dataUsageRecordList2008 = new ArrayList<>();
        dataUsageRecordList2008.add(dataUsageRecord2008);
        dataUsageRecordList2008.add(dataUsageRecord2008Q2);

        Double totalVolume2008 = Double.parseDouble(dataUsageRecord2008.getVolumeOfMobileData()) + Double.parseDouble(dataUsageRecord2008Q2.getVolumeOfMobileData());

        AnnualDataUsage annualDataUsage2008 = new AnnualDataUsage("2008", dataUsageRecordList2008, totalVolume2008);


        List<DataUsageRecord> dataUsageRecordList2018 = new ArrayList<>();
        dataUsageRecordList2018.add(dataUsageRecord2018);

        AnnualDataUsage annualDataUsage2018 = new AnnualDataUsage("2018", dataUsageRecordList2018, 10.0234);

        List<AnnualDataUsage> annualDataUsageList = new ArrayList<>();
        annualDataUsageList.add(annualDataUsage2018);
        annualDataUsageList.add(annualDataUsage2008);

        List<AnnualDataUsage> annualDataUsageList2 = Helper.filterRecords(dataUsageRecordList);

        assertEquals(annualDataUsageList.size(), annualDataUsageList2.size());

        for (int i = 0; i < annualDataUsageList.size(); i++) {
            assertEquals(annualDataUsageList.get(i).getQuarterRecords().size(), annualDataUsageList2.get(i).getQuarterRecords().size());

            for (int x = 0; x<annualDataUsageList.get(i).getQuarterRecords().size(); x++) {
                assertEquals(
                        annualDataUsageList.get(i).getQuarterRecords().get(x).getQuarter(),
                        annualDataUsageList2.get(i).getQuarterRecords().get(x).getQuarter()
                );
                assertEquals(annualDataUsageList.get(i).getQuarterRecords().get(x).getVolumeOfMobileData(), annualDataUsageList2.get(i).getQuarterRecords().get(x).getVolumeOfMobileData());
            }

            assertTrue(annualDataUsageList.get(i).getTotalMobileData() == annualDataUsageList2.get(i).getTotalMobileData());
            assertEquals(annualDataUsageList.get(i).getYear(), annualDataUsageList2.get(i).getYear());
        }
    }
}
