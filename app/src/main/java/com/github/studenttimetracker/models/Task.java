package com.github.studenttimetracker.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Data;

@Data
@DatabaseTable(tableName = "tasks")
public class Task {
    @DatabaseField(columnName = "id",generatedId = true)
    private int id;

    @DatabaseField(columnName = "task_name")
    private String taskName;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "project_id")
    private Project project;

    @DatabaseField(columnName = "time_from")
    private String timeFrom;

    @DatabaseField(columnName = "time_to")
    private String timeTo;

    @DatabaseField(columnName = "duration")
    private int duration;

    public void setTimeTo(String timeTo) throws ParseException {
        this.timeTo = timeTo;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date1 = formatter.parse(this.timeFrom);
        Date date2 = formatter.parse(this.timeTo);

        long diff = date2.getTime() - date1.getTime();
        this.duration = (int) (diff / 1000);
    }
}
