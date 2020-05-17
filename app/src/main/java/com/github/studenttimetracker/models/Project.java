package com.github.studenttimetracker.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable(tableName = "projects")
public class Project {
    @DatabaseField(columnName = "id",generatedId = true)
    int id;

    @DatabaseField(columnName = "project_name")
    String projectName;

//    @ForeignCollectionField(eager = true)
//    java.util.Collection<Task> tasks;

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

//    public Collection<Task> getTasks() {
//        return tasks;
//    }
//
//    public void setTasks(Collection<Task> tasks) {
//        this.tasks = tasks;
//    }

//    public void saveToDb(DatabaseHelper dbHelper) throws java.sql.SQLException{
//        Dao<User, Long> dao = dbHelper.getUserDao();
//        dao.createOrUpdate(this);
//    }
//
//    public static User loadFromDb(DatabaseHelper dbHelper, Long id) throws SQLException {
//        Dao<User, Long> dao = dbHelper.getUserDao();
//        User user = dao.queryForId(id);
//        return user;
//    }
}
