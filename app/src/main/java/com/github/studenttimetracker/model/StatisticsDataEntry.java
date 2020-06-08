package com.github.studenttimetracker.model;

import com.anychart.chart.common.dataentry.ValueDataEntry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StatisticsDataEntry implements Comparable<StatisticsDataEntry> {
    private String name;
    private Integer seconds;

    public ValueDataEntry valueDataEntry() {
        return new ValueDataEntry(this.name, seconds);
    }

    @Override
    public int compareTo(StatisticsDataEntry dataEntry) {
        if (seconds > dataEntry.seconds) {
            return -1;
        } else if (seconds < dataEntry.seconds) {
            return 1;
        } else {
            return 0;
        }
    }
}
