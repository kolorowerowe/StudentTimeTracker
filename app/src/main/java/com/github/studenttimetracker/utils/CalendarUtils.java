package com.github.studenttimetracker.utils;

import com.github.studenttimetracker.enums.StatisticsUnitType;
import com.github.studenttimetracker.model.StatisticsQueryObject;

import java.util.ArrayList;
import java.util.Date;


public class CalendarUtils {


    public static ArrayList<StatisticsQueryObject> getStatisticsObjectType(StatisticsUnitType unitType, Date startDate) {

        ArrayList<StatisticsQueryObject> list = new ArrayList();

        list.add(StatisticsQueryObject.builder()
                .name(getCurrentDate(unitType))
                .hasNext(false)
                .hasPrevious(true)
                .build());

        list.add(StatisticsQueryObject.builder()
                .name(getPreviousDate(unitType))
                .hasNext(true)
                .hasPrevious(false)
                .build());

        return list;
    }


    private static String getCurrentDate(StatisticsUnitType unitType) {
        switch (unitType) {
            case DAY:
                return "Today";
            case WEEK:
                return "This week";
            case MONTH:
                return "This month";
            case YEAR:
                return "This year";
            default:
                return "INCORRECT";
        }
    }

    private static String getPreviousDate(StatisticsUnitType unitType) {
        switch (unitType) {
            case DAY:
                return "Yesterday";
            case WEEK:
                return "Week before";
            case MONTH:
                return "Month before";
            case YEAR:
                return "Year before";
            default:
                return "INCORRECT";
        }
    }
}
