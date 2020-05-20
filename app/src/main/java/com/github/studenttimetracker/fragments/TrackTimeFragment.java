package com.github.studenttimetracker.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.studenttimetracker.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        final Chronometer chronometer = view.findViewById(R.id.myChronometer);
        final TextView spentTime = view.findViewById(R.id.spentTime);
        final TextView activityNameShow = view.findViewById(R.id.activityNameShow);

        endButton.setVisibility(View.INVISIBLE);
        chronometer.setVisibility(View.INVISIBLE);
        activityNameShow.setVisibility(View.INVISIBLE);
        spentTime.setVisibility(View.INVISIBLE);

        // Setting onClicks()
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();

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
                long duration = (SystemClock.elapsedRealtime() - chronometer.getBase());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

                spentTime.setText(simpleDateFormat.format(duration));
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());

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
