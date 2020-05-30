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
    private Integer hoursSpent;

    public ValueDataEntry valueDataEntry() {
        return new ValueDataEntry(this.name, hoursSpent);
    }

    @Override
    public int compareTo(StatisticsDataEntry dataEntry) {
        if (hoursSpent > dataEntry.hoursSpent) {
            return -1;
        } else if (hoursSpent < dataEntry.hoursSpent) {
            return 1;
        } else {
            return 0;
        }
    }
}
