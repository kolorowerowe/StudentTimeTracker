package com.github.studenttimetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.studenttimetracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackTimeFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracktime, container, false);

        Button startButton = view.findViewById(R.id.startActivity);
        Button endButton = view.findViewById(R.id.endActivity);
        endButton.setVisibility(View.INVISIBLE);

        // Setting onClicks()
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentView = (View) v.getParent();

                TextView startDateShow = parentView.findViewById(R.id.startTime);
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("HH:MM");
                startDateShow.setText(dateFormat.format(date));

                TextView activityName = parentView.findViewById(R.id.activityName);
                activityName.setVisibility(View.INVISIBLE);
                TextView activityNameShow = parentView.findViewById(R.id.activityNameShow);
                activityNameShow.setText(activityName.getText());
                v.setVisibility(View.INVISIBLE);
                Button second = parentView.findViewById(R.id.endActivity);
                second.setVisibility(View.VISIBLE);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentView = (View) v.getParent();

                TextView endDateShow = parentView.findViewById(R.id.endTime);
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("HH:MM");
                endDateShow.setText(dateFormat.format(date));

                TextView activityName = parentView.findViewById(R.id.activityName);
                activityName.setVisibility(View.VISIBLE);
                Button startActivity = parentView.findViewById(R.id.startActivity);
                startActivity.setVisibility(View.VISIBLE);
                v.setVisibility(View.INVISIBLE);

            }
        });
        // End of onClicks()


        return view;
    }

}
