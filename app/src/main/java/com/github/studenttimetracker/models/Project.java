package com.github.studenttimetracker.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.LinkedList;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Collection<Task> getTasks() {
        if(tasks == null)
            tasks = new LinkedList<Task>();
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }
}
