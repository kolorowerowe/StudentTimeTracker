package com.github.studenttimetracker.enums;

public enum StatisticsUnitType {

    DAY("Day"),
    WEEK("Week"),
    MONTH("Month"),
    YEAR("Year");

    private String value;

    StatisticsUnitType(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }


}
