package com.github.studenttimetracker.database;

import android.content.Context;

import com.github.studenttimetracker.models.Project;
import com.github.studenttimetracker.models.Task;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Repository {
    private DatabaseHelper databaseHelper = null;
    private Dao<Project, Integer> projectDao;
    private Dao<Task, Integer> taskDao;

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

    public Project getProjectById(int id) throws SQLException {
        return projectDao.queryForId(id);
    }

    public List<Project> getProjectFromQuery(String fieldName, Object value) throws SQLException {
        return projectDao.queryForEq(fieldName, value);
    }

    public void DeleteProjectById(int id) throws SQLException {
        projectDao.deleteById(id);
    }
    /* Marcin  */
    public Project getOneMatchingProject(Project project) throws SQLException {
        List<Project> l = projectDao.queryForMatching(project);
        return l.get(0);
    }

    /* Tasks */
    public List<Task> getTasksAllOrdered() throws SQLException {
        return taskDao.queryBuilder().orderBy("time_from",false).query();
    }

    public void createOrUpdateTask(Task task) throws SQLException {
        taskDao.createOrUpdate(task);
    }

    public Task getEarliestTask() throws SQLException {
        return taskDao.queryBuilder().orderBy("time_to", true).queryForFirst();
    }

    public List<Task> getTasksInDateRange(String dateFrom, String dateTo) throws SQLException {
        return taskDao.queryBuilder().where().between("time_to", dateFrom, dateTo).query();
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
