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
import android.widget.Button;
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
import java.util.List;
import java.util.TimeZone;

import static com.github.studenttimetracker.services.ChronometerService.ACTIVITY_NAME;

public class TrackTimeFragment extends Fragment {

    private List<TimeEntry> timeEntryList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracktime, container, false);

        // Setting the timeEntryRecycleView
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final TimeEntryAdapter timeEntryAdapter = new TimeEntryAdapter(initTimeEntryRecycleView());
        recyclerView.setAdapter(timeEntryAdapter);

        final Button startButton = view.findViewById(R.id.startActivity);
        final Button endButton = view.findViewById(R.id.endActivity);
        final TextView activityNameInput = view.findViewById(R.id.activityNameInput);
        final TextView activityNameShow = view.findViewById(R.id.activityNameShow);
        final TextView chronometer = view.findViewById(R.id.myChronometer);

        startButton.setEnabled(false);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long elapsedTime = intent.getLongExtra(ChronometerService.ELAPSED_TIME,0);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                        chronometer.setText(simpleDateFormat.format(elapsedTime));
                    }
                },new IntentFilter(ChronometerService.ACTION_CHRONOMETER_BROADCAST)
        );

        // Setting up UI
        if(ChronometerService.active)
        {
            endButton.setVisibility(View.VISIBLE);
            chronometer.setVisibility(View.VISIBLE);
            activityNameShow.setVisibility(View.VISIBLE);

            startButton.setVisibility(View.INVISIBLE);
            activityNameInput.setVisibility(View.INVISIBLE);
        }
        else {
            endButton.setVisibility(View.INVISIBLE);
            chronometer.setVisibility(View.INVISIBLE);
            activityNameShow.setVisibility(View.INVISIBLE);

            startButton.setVisibility(View.VISIBLE);
            activityNameInput.setVisibility(View.VISIBLE);
        }

        // Setting onClicks()
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String activityName = String.valueOf(activityNameInput.getText());
                activityNameShow.setText(activityName);

                Intent serviceIntent = new Intent(getActivity(),ChronometerService.class);
                serviceIntent.putExtra(ACTIVITY_NAME,activityName);
                getActivity().startService(serviceIntent);

                endButton.setVisibility(View.VISIBLE);
                chronometer.setVisibility(View.VISIBLE);
                activityNameShow.setVisibility(View.VISIBLE);
                activityNameInput.setVisibility(View.INVISIBLE);
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
                activityNameShow.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.VISIBLE);
                endButton.setVisibility(View.INVISIBLE);
                chronometer.setVisibility(View.INVISIBLE);

            }
        });
        // End of onClicks()

        // Text Watcher
        activityNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = activityNameInput.getText().toString().trim();
                startButton.setEnabled(!input.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
}
