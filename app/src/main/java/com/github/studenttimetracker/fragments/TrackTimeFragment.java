package com.github.studenttimetracker.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.studenttimetracker.R;
import com.github.studenttimetracker.services.ChronometerService;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TrackTimeFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracktime, container, false);

        final Button startButton = view.findViewById(R.id.startActivity);
        final Button endButton = view.findViewById(R.id.endActivity);
        final TextView activityNameInput = view.findViewById(R.id.activityNameInput);
        final TextView activityNameShow = view.findViewById(R.id.activityNameShow);
        final TextView chronometer = view.findViewById(R.id.myChronometer);
        final TextView spentTime = view.findViewById(R.id.spentTime);

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

        endButton.setVisibility(View.INVISIBLE);
        chronometer.setVisibility(View.INVISIBLE);
        activityNameShow.setVisibility(View.INVISIBLE);
        spentTime.setVisibility(View.INVISIBLE);

        // Setting onClicks()
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().startService(new Intent(getActivity(), ChronometerService.class));
                activityNameShow.setText(activityNameInput.getText());

                endButton.setVisibility(View.VISIBLE);
                chronometer.setVisibility(View.VISIBLE);
                activityNameInput.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.INVISIBLE);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().stopService(new Intent(getActivity(),ChronometerService.class));

                spentTime.setText(chronometer.getText());

                spentTime.setVisibility(View.VISIBLE);
                activityNameShow.setVisibility(View.VISIBLE);
                activityNameInput.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                endButton.setVisibility(View.INVISIBLE);
                chronometer.setVisibility(View.INVISIBLE);

            }
        });
        // End of onClicks()
        return view;
    }

}
