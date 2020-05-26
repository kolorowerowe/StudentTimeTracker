package com.github.studenttimetracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.github.studenttimetracker.R;
import com.github.studenttimetracker.database.DatabaseHelper;
import com.github.studenttimetracker.database.Repository;
import com.github.studenttimetracker.models.Project;
import com.github.studenttimetracker.models.Task;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        try {
            repository = new Repository(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final Project project = new Project();

        project.setId(100);
        project.setProjectName("project1New");

        Task task1 = new Task();
        task1.setTaskName("Task1New");

        Task task2 = new Task();
        task2.setTaskName("Task2New");

        project.setTasks(Arrays.asList(new Task[]{task1, task2}));

        try {
            List<Project> a = repository.getProjectsAll();
            List<Task> c1 = repository.getTasksAll();
            repository.createOrUpdateProject(project);
            repository.createOrUpdateTask(task1);
            repository.createOrUpdateTask(task2);

            List<Project> b = repository.getProjectsAll();
            List<Task> c = repository.getTasksAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
