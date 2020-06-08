package com.github.studenttimetracker.utils;

import com.github.studenttimetracker.model.StatisticsDataEntry;
import com.github.studenttimetracker.models.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsMapper {

    public static List<StatisticsDataEntry> getProjectsStatistics(List<Task> taskList) {

        Map<String, Integer> projectStatisticsMap = new HashMap<>();
        for (Task task : taskList) {
            if (task.getDuration() != 0){
                String projectName = task.getProject().getProjectName();

                if (projectStatisticsMap.containsKey(projectName)) {
                    projectStatisticsMap.put(projectName, task.getDuration() + projectStatisticsMap.get(projectName));
                } else {
                    projectStatisticsMap.put(projectName, task.getDuration());
                }
            }
        }


        List<StatisticsDataEntry> projectStatistics = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : projectStatisticsMap.entrySet()){
            projectStatistics.add(new StatisticsDataEntry(entry.getKey(), entry.getValue()));
        }

        if (projectStatistics.isEmpty()){
            projectStatistics.add(new StatisticsDataEntry("EMPTY", 0));
        }

        Collections.sort(projectStatistics);
        return projectStatistics;
    }
}
