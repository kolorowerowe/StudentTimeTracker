package com.github.studenttimetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.studenttimetracker.R;
import com.github.studenttimetracker.database.Repository;
import com.github.studenttimetracker.models.Task;
import com.github.studenttimetracker.recycleView.TimeEntryAdapter;
import com.github.studenttimetracker.utils.CalendarUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class TimelineFragment extends Fragment {

    private Repository repository;
    private List<Task> timeEntryList = new ArrayList<>();
    private TimeEntryAdapter timeEntryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new Repository(getContext());
        } catch (SQLException e) { e.printStackTrace();}

        timeEntryAdapter = new TimeEntryAdapter(initTimeEntryRecycleView());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        // Setting the timeEntryRecycleView
        final RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(timeEntryAdapter);

        return view;
    }

    // Init with data form DataBase
    private List<Task> initTimeEntryRecycleView(){

        List<Task> taskList = null;
        try {
            taskList = repository.getTasksAll();
        } catch (SQLException e) { e.printStackTrace();}

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CalendarUtils.hourFormat);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        assert taskList != null;
        timeEntryList.addAll(taskList);
        return timeEntryList;
    }

}
