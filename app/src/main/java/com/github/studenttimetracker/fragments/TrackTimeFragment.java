package com.github.studenttimetracker.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.studenttimetracker.R;
import com.github.studenttimetracker.database.Repository;
import com.github.studenttimetracker.models.Project;
import com.github.studenttimetracker.models.Task;
import com.github.studenttimetracker.recycleView.TimeEntry;
import com.github.studenttimetracker.recycleView.TimeEntryAdapter;
import com.github.studenttimetracker.services.ChronometerService;
import com.github.studenttimetracker.utils.CalendarUtils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import static com.github.studenttimetracker.services.ChronometerService.PROJECT_NAME;
import static com.github.studenttimetracker.services.ChronometerService.TASK_NAME;

public class TrackTimeFragment extends Fragment {

    private Repository repository;
    private List<TimeEntry> timeEntryList = new ArrayList<>();
    private static String NULL_ACTIVITY = "Select a project";
    private List<String> spinnerArrayList = new ArrayList<>(Collections.singletonList(NULL_ACTIVITY));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracktime, container, false);
        try {
            repository = new Repository(getContext());
        } catch (SQLException e) { e.printStackTrace();}

        // Getting variables
        final Button startButton = view.findViewById(R.id.startTask);
        final Button endButton = view.findViewById(R.id.endTask);
        final TextView taskNameInput = view.findViewById(R.id.taskNameInput);
        final TextView taskNameShow = view.findViewById(R.id.taskNameShow);
        final Chronometer chronometer = view.findViewById(R.id.myChronometer);
        final Spinner spinner = view.findViewById(R.id.projectSpinner);
        final Button addProjectButton = view.findViewById(R.id.addProject);
        final TextView projectNameInput = view.findViewById(R.id.projectName);

        // BroadCast Receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long elapsedTime = intent.getLongExtra(ChronometerService.ELAPSED_TIME,0);
                        String activityName = intent.getStringExtra(TASK_NAME);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CalendarUtils.hourFormat);
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                        chronometer.setText(simpleDateFormat.format(elapsedTime));
                        taskNameShow.setText(String.valueOf(activityName));
                    }
                },new IntentFilter(ChronometerService.ACTION_CHRONOMETER_BROADCAST)
        );

        // Setting the timeEntryRecycleView
        final RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final TimeEntryAdapter timeEntryAdapter = new TimeEntryAdapter(initTimeEntryRecycleView());
        recyclerView.setAdapter(timeEntryAdapter);

        // Spinner Items
        try {
            for(Project project:repository.getProjectsAll()){
                spinnerArrayList.add(project.getProjectName());
            }
        } catch (SQLException e) { e.printStackTrace();}
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,spinnerArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        // Setting up UI
        addProjectButton.setVisibility(View.VISIBLE);
        projectNameInput.setVisibility(View.VISIBLE);
        if(ChronometerService.active)
        {
            endButton.setVisibility(View.VISIBLE);
            chronometer.setVisibility(View.VISIBLE);
            taskNameShow.setVisibility(View.VISIBLE);

            startButton.setVisibility(View.INVISIBLE);
            taskNameInput.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.INVISIBLE);
        }
        else {
            endButton.setVisibility(View.INVISIBLE);
            chronometer.setVisibility(View.INVISIBLE);
            taskNameShow.setVisibility(View.INVISIBLE);

            startButton.setVisibility(View.VISIBLE);
            taskNameInput.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
        }

        // Setting onClicks()
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get variables
                String taskName = taskNameInput.getText().toString().trim();
                String projectName = spinner.getSelectedItem().toString();

                // set UI
                taskNameInput.setText("");
                taskNameShow.setText(taskName);

                // Start service
                Intent serviceIntent = new Intent(getActivity(),ChronometerService.class);
                serviceIntent.putExtra(TASK_NAME,taskName);
                serviceIntent.putExtra(PROJECT_NAME,projectName);
                getActivity().startService(serviceIntent);

                // set UI
                endButton.setVisibility(View.VISIBLE);
                chronometer.setVisibility(View.VISIBLE);
                taskNameShow.setVisibility(View.VISIBLE);
                taskNameInput.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.INVISIBLE);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop service
                Intent serviceIntent = new Intent(getActivity(),ChronometerService.class);
                long durationNumber = serviceIntent.getLongExtra(ChronometerService.ELAPSED_TIME,0);
                getActivity().stopService(serviceIntent);

                // Get variables
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CalendarUtils.timestampFormat);
                String duration = (String) chronometer.getText();
                String taskName = (String) taskNameShow.getText();
                String projectName = (String) spinner.getSelectedItem();
                spinner.setSelection(0);

                // Update local list
                // TODO Wywal TimeEntry - dłuższe, bardziej skąplikowane
                TimeEntry timeEntry = new TimeEntry(duration,taskName);
                timeEntryList.add(timeEntry);
                timeEntryAdapter.notifyDataSetChanged();

                // Database Update
                Task task = new Task();
                task.setTaskName(taskName);
                Project project = new Project();
                project.setProjectName(projectName);
                try {
                    task.setProject(repository.getOneMatchingProject(project));
                    task.setTimeFrom(simpleDateFormat.format(timestamp.getTime() - durationNumber));
                    task.setTimeTo(simpleDateFormat.format(timestamp));
                    repository.createOrUpdateTask(task);
                } catch (SQLException | ParseException e) { e.printStackTrace();}

                // set UI
                taskNameInput.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                taskNameShow.setVisibility(View.INVISIBLE);
                endButton.setVisibility(View.INVISIBLE);
                chronometer.setVisibility(View.INVISIBLE);

            }
        });

        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get variables & set UI
                String projectName = projectNameInput.getText().toString();
                projectNameInput.setText("");

                // Update Local List
                spinnerArrayList.add(projectName);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,spinnerArrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);

                // Database update
                Project project = new Project();
                project.setProjectName(projectName);
                project.setTasks(new LinkedList<Task>());
                try {
                    repository.createOrUpdateProject(project);
                } catch (SQLException e) { e.printStackTrace();}
            }
        });
        // End of onClicks()

        // Change Watchers
        startButton.setEnabled(false);
        addProjectButton.setEnabled(false);

        taskNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleInputSpinnerChange(taskNameInput,spinner,startButton);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleInputSpinnerChange(taskNameInput,spinner,startButton);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        projectNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textInput = projectNameInput.getText().toString().trim();

                addProjectButton.setEnabled(!textInput.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    // Init with data form DataBase
    private List<TimeEntry> initTimeEntryRecycleView(){

        List<Task> taskList = null;
        try {
            taskList = repository.getTasksAll();
        } catch (SQLException e) { e.printStackTrace();}

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CalendarUtils.hourFormat);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        assert taskList != null;
        for (Task task: taskList)
        {
         timeEntryList.add(new TimeEntry(simpleDateFormat.format(task.getDuration()*1000),task.getTaskName()));
        }
        return timeEntryList;
    }

    private void handleInputSpinnerChange(TextView taskNameInput, Spinner spinner, Button startButton){
        String textInput = taskNameInput.getText().toString().trim();
        String spinnerInput = spinner.getSelectedItem().toString();

        startButton.setEnabled(!textInput.isEmpty() && !spinnerInput.equals(NULL_ACTIVITY));
    }
}
