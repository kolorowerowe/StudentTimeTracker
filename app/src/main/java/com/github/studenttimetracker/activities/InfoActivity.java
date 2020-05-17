package com.github.studenttimetracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.github.studenttimetracker.R;
import com.github.studenttimetracker.database.DatabaseHelper;
import com.github.studenttimetracker.models.Project;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Log.v("MainActivity", "Message");

        final Project project = new Project();

        project.setId(1);
        project.setProjectName("project1");

        try {
            DatabaseHelper helper = new DatabaseHelper(this);

            // This is how, a reference of DAO object can be done
            final Dao<Project, Integer> projectDao = helper.getProjectDao();

            //This is the way to insert data into a database table
            projectDao.create(project);
            Log.v("MainActivity", "Created");

            final List<Project> projectList = projectDao.queryForAll();

            Log.v("MainActivity", String.valueOf(projectList.size()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
