package com.github.studenttimetracker.utils;

import com.github.studenttimetracker.enums.StatisticsPeriodType;
import com.github.studenttimetracker.model.StatisticsQueryObject;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CalendarUtils {

    public static final String timestampFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String hourFormat = "HH:mm:ss";

    public static String formatSeconds(int seconds) {
        String result = "";

        if (seconds <= 0){
            return "0s";
        }

        int secondResult = seconds % 60;
        if (secondResult > 0){
            result = secondResult + "s" + result;
        }

        int minutes = seconds/60;
        int minuteResult = minutes%60;
        if (minuteResult > 0){
            result = minuteResult + "m" + result;
        }

        int hours = minutes/60;
        if (hours > 0){
            result = hours + "h" + result;
        }
        return result;
    }

    public static List<StatisticsQueryObject> getStatisticsQueryList(StatisticsPeriodType unitType, LocalDate startDate, LocalDate endDate) {

        List<StatisticsQueryObject> list = new ArrayList<>();
        switch (unitType) {
            case DAY:
                while (endDate.isAfter(startDate.minusDays(1))) {
                    list.add(StatisticsQueryObject.builder()
                            .name(endDate.toString("dd MMM YYYY"))
                            .dateFrom(fromLocalDate(endDate))
                            .dateTo(fromLocalDate(endDate.plusDays(1)))
                            .hasNext(true)
                            .hasPrevious(true)
                            .build());

                    endDate = endDate.minusDays(1);
                }
                break;

            case WEEK:
                while (endDate.isAfter(startDate.minusWeeks(1))) {
                    list.add(StatisticsQueryObject.builder()
                            .dateFrom(getFirstDayFromWeek(endDate))
                            .dateTo(getFirstDayFromWeek(endDate.plusWeeks(1)))
                            .name(endDate.toString("dd MMM") + " - " + endDate.plusDays(6).toString("dd MMM YYYY"))
                            .hasNext(true)
                            .hasPrevious(true)
                            .build());

                    endDate = endDate.minusWeeks(1);
                }
                break;

            case MONTH:
                while (endDate.isAfter(startDate.minusMonths(1))) {
                    list.add(StatisticsQueryObject.builder()
                            .dateFrom(getFirstDayFromMonth(endDate))
                            .dateTo(getFirstDayFromMonth(endDate.plusMonths(1)))
                            .name(endDate.toString("MMMM YYYY"))
                            .hasNext(true)
                            .hasPrevious(true)
                            .build());

                    endDate = endDate.minusMonths(1);
                }
                break;

            case YEAR:
                while (endDate.isAfter(startDate.minusYears(1))) {
                    list.add(StatisticsQueryObject.builder()
                            .dateFrom(getFirstDayFromYear(endDate))
                            .dateTo(getFirstDayFromYear(endDate.plusYears(1)))
                            .name(endDate.toString("YYYY"))
                            .hasNext(true)
                            .hasPrevious(true)
                            .build());

                    endDate = endDate.minusYears(1);
                }
                break;

            default:
                break;

        }

        if (!list.isEmpty()) {
            StatisticsQueryObject first = list.get(0);
            first.setHasNext(false);
            list.set(0, first);

            StatisticsQueryObject last = list.get(list.size() - 1);
            last.setHasPrevious(false);
            list.set(list.size() - 1, last);
        }

        return list;
    }


    private static Date getFirstDayFromWeek(LocalDate localDate) {
        LocalDate firstDayOfWeek = localDate.withDayOfWeek(1);
        return fromLocalDate(firstDayOfWeek);
    }

    private static Date getFirstDayFromMonth(LocalDate localDate) {
        LocalDate firstDayOfMonth = localDate.withDayOfMonth(1);
        return fromLocalDate(firstDayOfMonth);
    }

    private static Date getFirstDayFromYear(LocalDate localDate) {
        LocalDate firstDayOfYear = localDate.withMonthOfYear(1);
        return fromLocalDate(firstDayOfYear);
    }

    private static Date fromLocalDate(LocalDate localDate) {
        return new Date(localDate.toDateTimeAtStartOfDay().getMillis());
    }

}
