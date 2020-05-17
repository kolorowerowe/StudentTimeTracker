package com.github.studenttimetracker.enums;

public enum StatisticsPeriodType {

    DAY("Day"),
    WEEK("Week"),
    MONTH("Month"),
    YEAR("Year");

    private String value;

    StatisticsPeriodType(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }


}
