package com.github.studenttimetracker.utils;

import com.github.studenttimetracker.enums.StatisticsPeriodType;
import com.github.studenttimetracker.model.StatisticsQueryObject;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CalendarUtilsTest {

    @Test
    public void getStatisticsQueryListTest() {
        LocalDate dateFrom = LocalDate.parse("2020-06-01");
        LocalDate dateTo = LocalDate.parse("2020-06-05");
        List<StatisticsQueryObject> list = CalendarUtils.getStatisticsQueryList(StatisticsPeriodType.DAY, dateFrom, dateTo);

        assertEquals(5, list.size());

        StatisticsQueryObject first = StatisticsQueryObject.builder()
                .dateFrom(LocalDate.parse("2020-06-05").toDate())
                .dateTo(LocalDate.parse("2020-06-06").toDate())
                .hasNext(false)
                .hasPrevious(true)
                .name("05 Jun 2020")
                .build();
        assertEquals(first, list.get(0));

        StatisticsQueryObject last = StatisticsQueryObject.builder()
                .dateFrom(LocalDate.parse("2020-06-01").toDate())
                .dateTo(LocalDate.parse("2020-06-02").toDate())
                .hasNext(true)
                .hasPrevious(false)
                .name("01 Jun 2020")
                .build();
        assertEquals(last, list.get(4));


    }
}