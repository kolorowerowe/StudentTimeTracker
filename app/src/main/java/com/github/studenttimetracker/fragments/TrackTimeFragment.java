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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.studenttimetracker.R;
import com.github.studenttimetracker.recycleView.TimeEntry;
import com.github.studenttimetracker.recycleView.TimeEntryAdapter;
import com.github.studenttimetracker.services.ChronometerService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static com.github.studenttimetracker.services.ChronometerService.ACTIVITY_NAME;

public class TrackTimeFragment extends Fragment {

    private List<TimeEntry> timeEntryList = new ArrayList<>();
    private static String NULL_ACTIVITY = "---";
    private List<String> spinnerArrayList = Arrays.asList(NULL_ACTIVITY,"Breakfast", "Studying", "Leisure", "Sport", "Gaming");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracktime, container, false);

        // Getting variables
        final Button startButton = view.findViewById(R.id.startActivity);
        final Button endButton = view.findViewById(R.id.endActivity);
        final TextView activityNameInput = view.findViewById(R.id.activityNameInput);
        final TextView activityNameShow = view.findViewById(R.id.activityNameShow);
        final TextView chronometer = view.findViewById(R.id.myChronometer);
        final Spinner spinner = view.findViewById(R.id.activitySpinner);

        // BroadCast Receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long elapsedTime = intent.getLongExtra(ChronometerService.ELAPSED_TIME,0);
                        String activityName = intent.getStringExtra(ACTIVITY_NAME);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                        chronometer.setText(simpleDateFormat.format(elapsedTime));
                        activityNameShow.setText(String.valueOf(activityName));
                    }
                },new IntentFilter(ChronometerService.ACTION_CHRONOMETER_BROADCAST)
        );

        // Setting the timeEntryRecycleView
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final TimeEntryAdapter timeEntryAdapter = new TimeEntryAdapter(initTimeEntryRecycleView());
        recyclerView.setAdapter(timeEntryAdapter);

        // Spinner Items
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,spinnerArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        // Setting up UI
        if(ChronometerService.active)
        {
            endButton.setVisibility(View.VISIBLE);
            chronometer.setVisibility(View.VISIBLE);
            activityNameShow.setVisibility(View.VISIBLE);

            startButton.setVisibility(View.INVISIBLE);
            activityNameInput.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.INVISIBLE);
        }
        else {
            endButton.setVisibility(View.INVISIBLE);
            chronometer.setVisibility(View.INVISIBLE);
            activityNameShow.setVisibility(View.INVISIBLE);

            startButton.setVisibility(View.VISIBLE);
            activityNameInput.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
        }

        // Setting onClicks()
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String activityName = getActivityName(activityNameInput,spinner);
                activityNameShow.setText(activityName);

                Intent serviceIntent = new Intent(getActivity(),ChronometerService.class);
                serviceIntent.putExtra(ACTIVITY_NAME,activityName);
                getActivity().startService(serviceIntent);

                endButton.setVisibility(View.VISIBLE);
                chronometer.setVisibility(View.VISIBLE);
                activityNameShow.setVisibility(View.VISIBLE);
                activityNameInput.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.INVISIBLE);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent serviceIntent = new Intent(getActivity(),ChronometerService.class);
                getActivity().stopService(serviceIntent);

                String duration = (String) chronometer.getText();
                String activity = (String) activityNameShow.getText();
                TimeEntry timeEntry = new TimeEntry(duration,activity);
                timeEntryList.add(timeEntry);
                timeEntryAdapter.notifyDataSetChanged();

                activityNameInput.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                activityNameShow.setVisibility(View.INVISIBLE);
                endButton.setVisibility(View.INVISIBLE);
                chronometer.setVisibility(View.INVISIBLE);

            }
        });
        // End of onClicks()

        // Change Watchers
        startButton.setEnabled(false);

        activityNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleInputSpinnerChange(activityNameInput,spinner,startButton);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleInputSpinnerChange(activityNameInput,spinner,startButton);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    // Init with data form DataBase
    private List<TimeEntry> initTimeEntryRecycleView(){
        timeEntryList.add(new TimeEntry("01:00:00", "Breakfast"));
        timeEntryList.add(new TimeEntry("00:30:00", "Studying"));
        timeEntryList.add(new TimeEntry("02:00:00", "Leisure"));
        timeEntryList.add(new TimeEntry("01:00:00", "Sport"));
        timeEntryList.add(new TimeEntry("01:30:00", "Gaming"));
        timeEntryList.add(new TimeEntry("03:00:00", "Studying"));
        timeEntryList.add(new TimeEntry("01:00:00", "Breakfast"));
        timeEntryList.add(new TimeEntry("00:30:00", "Studying"));
        timeEntryList.add(new TimeEntry("02:00:00", "Leisure"));
        timeEntryList.add(new TimeEntry("01:00:00", "Sport"));
        timeEntryList.add(new TimeEntry("01:30:00", "Gaming"));
        timeEntryList.add(new TimeEntry("03:00:00", "Studying"));

        return timeEntryList;
    }

    private String getActivityName(TextView activityNameInput, Spinner spinner){
        String textInput = activityNameInput.getText().toString().trim();
        String spinnerInput = spinner.getSelectedItem().toString();

        if(!textInput.equals("")) return textInput;
        else return spinnerInput;
    }

    private void handleInputSpinnerChange(TextView activityNameInput, Spinner spinner, Button startButton){
        String textInput = activityNameInput.getText().toString().trim();
        String spinnerInput = spinner.getSelectedItem().toString();

        startButton.setEnabled(!textInput.isEmpty() || !spinnerInput.equals(NULL_ACTIVITY));
    }
}
