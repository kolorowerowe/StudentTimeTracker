package com.github.studenttimetracker.fragments;

import android.app.Notification;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.studenttimetracker.R;
import com.github.studenttimetracker.database.Repository;
import com.github.studenttimetracker.models.Project;
import com.github.studenttimetracker.models.Task;
import com.github.studenttimetracker.services.ChronometerService;
import com.github.studenttimetracker.utils.CalendarUtils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import static com.github.studenttimetracker.notifications.NotificationChannels.POMODORO_CHANNEL_ID;
import static com.github.studenttimetracker.services.ChronometerService.PROJECT_NAME;
import static com.github.studenttimetracker.services.ChronometerService.TASK_NAME;

public class TrackTimeFragment extends Fragment {

    private Repository repository;
    private static String NULL_ACTIVITY = "Select a project";
    private List<String> spinnerArrayList = new ArrayList<>(Collections.singletonList(NULL_ACTIVITY));
    private static long taskDuration = 0;

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
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());

        // BroadCast Receiver
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        taskDuration = intent.getLongExtra(ChronometerService.ELAPSED_TIME,0);
                        // 25min = 25*60s*1000ms +- 100ms
                        if(1_499_900<taskDuration && taskDuration<1_500_100){
                            Notification notification = new NotificationCompat.Builder(context,POMODORO_CHANNEL_ID)
                                    .setContentTitle("Pomodoro Break")
                                    .setContentText("You've been working for 25min!")
                                    .setSmallIcon(R.drawable.ic_timer)
                                    .build();
                            notificationManager.notify(2,notification);
                        }
                        String activityName = intent.getStringExtra(TASK_NAME);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CalendarUtils.hourFormat);
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                        chronometer.setText(simpleDateFormat.format(taskDuration));
                        taskNameShow.setText(String.valueOf(activityName));
                        spinner.setSelection(spinnerArrayList.indexOf(intent.getStringExtra(PROJECT_NAME)));
                        endButton.setEnabled(true);
                    }
                },new IntentFilter(ChronometerService.ACTION_CHRONOMETER_BROADCAST)
        );

        // Spinner Items
        try {
            for(Project project:repository.getProjectsAll()){
                spinnerArrayList.add(project.getProjectName());
            }
        } catch (SQLException e) { e.printStackTrace();}
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(),android.R.layout.simple_spinner_item,spinnerArrayList);
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
                requireActivity().startService(serviceIntent);

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
                requireActivity().stopService(serviceIntent);

                // Get variables
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CalendarUtils.timestampFormat);
                String duration = (String) chronometer.getText();
                String taskName = (String) taskNameShow.getText();
                String projectName = (String) spinner.getSelectedItem();
                spinner.setSelection(0);

                // Database Update
                Task task = new Task();
                task.setTaskName(taskName);
                Project project = new Project();
                project.setProjectName(projectName);
                try {
                    task.setProject(repository.getOneMatchingProject(project));
                    task.setTimeFrom(simpleDateFormat.format(timestamp.getTime() - taskDuration));
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
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(),android.R.layout.simple_spinner_item,spinnerArrayList);
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
        endButton.setEnabled(false);
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

    private void handleInputSpinnerChange(TextView taskNameInput, Spinner spinner, Button startButton){
        String textInput = taskNameInput.getText().toString().trim();
        String spinnerInput = spinner.getSelectedItem().toString();

        startButton.setEnabled(!textInput.isEmpty() && !spinnerInput.equals(NULL_ACTIVITY));
    }
}
