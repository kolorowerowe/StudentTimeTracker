package com.github.studenttimetracker.model;


import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatisticsQueryObject {

    private String name;
    private boolean hasNext;
    private boolean hasPrevious;
    private Date dateFrom;
    private Date dateTo;


}
