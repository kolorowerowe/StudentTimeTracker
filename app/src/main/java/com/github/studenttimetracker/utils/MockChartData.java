package com.github.studenttimetracker.utils;

import com.github.studenttimetracker.model.StatisticsDataEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MockChartData {

    private Random random;

    public MockChartData() {
        random = new Random();
    }

    public List<StatisticsDataEntry> getExampleData() {
        List<StatisticsDataEntry> exampleData = new ArrayList<>();

        exampleData.add(new StatisticsDataEntry("Running", random.nextInt(50)));
        exampleData.add(new StatisticsDataEntry("Sleeping", random.nextInt(500)));
        exampleData.add(new StatisticsDataEntry("Cooking", random.nextInt(100)));
        exampleData.add(new StatisticsDataEntry("Learning", random.nextInt(10)));

        Collections.sort(exampleData);
        return exampleData;
    }
}
