package com.github.studenttimetracker.database;

import android.content.Context;

import com.github.studenttimetracker.models.Project;
import com.github.studenttimetracker.models.Task;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class Repository {
    private DatabaseHelper databaseHelper = null;
    private Dao<Project, Integer> projectDao = null;
    private Dao<Task, Integer> taskDao = null;

    public Repository(Context context) throws SQLException {
        getHelper(context);
        projectDao = databaseHelper.getProjectDao();
        taskDao = databaseHelper.getTaskDao();
    }

    public List<Project> getProjectsAll() throws SQLException {
        return projectDao.queryForAll();
    }

    public void createOrUpdateProject(Project project) throws SQLException {
        projectDao.createOrUpdate(project);
    }

    public Project getProjectFromId(int id) throws SQLException {
        return projectDao.queryForId(id);
    }

    public List<Project> getProjectFromQuery(String fieldName, Object value) throws SQLException {
        return projectDao.queryForEq(fieldName, value);
    }

    public void DeleteProjectFromId(int id) throws SQLException {
        projectDao.deleteById(id);
    }

    /* Tasks */
    public List<Task> getTasksAll() throws SQLException {
        return taskDao.queryForAll();
    }

    public void createOrUpdateTask(Task task) throws SQLException {
        taskDao.createOrUpdate(task);
    }

    public Task getTaskFromId(int id) throws SQLException {
        return taskDao.queryForId(id);
    }

    private DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
