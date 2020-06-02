package com.github.studenttimetracker.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

@Data
@DatabaseTable(tableName = "projects")
public class Project {
    @DatabaseField(columnName = "id",generatedId = true)
    private int id;

    @DatabaseField(columnName = "project_name")
    private String projectName;

    @ForeignCollectionField(eager = true)
    private java.util.Collection<Task> tasks;

    @DatabaseField(columnName = "color")
    private String color;

    @DatabaseField(columnName = "icon")
    private String icon;
}
