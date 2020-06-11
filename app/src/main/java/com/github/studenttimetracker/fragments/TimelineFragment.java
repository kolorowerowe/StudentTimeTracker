package com.github.studenttimetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.studenttimetracker.R;
import com.github.studenttimetracker.database.Repository;
import com.github.studenttimetracker.models.Task;
import com.github.studenttimetracker.recycleView.TimeEntryAdapter;
import com.github.studenttimetracker.utils.CalendarUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class TimelineFragment extends Fragment {

    private Repository repository;
    private List<Task> timeEntryList = new ArrayList<>();
    private TimeEntryAdapter timeEntryAdapter;
    private RequestQueue requestQueue;
    private static final String API_URL_QOD = "https://quotes.rest/qod";
    private TextView quoteText;


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

        // REST
        quoteText = view.findViewById(R.id.quoteText);
        requestQueue = Volley.newRequestQueue(requireContext());
        parseJSON();

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

    private void parseJSON() {
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, API_URL_QOD, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject content = response.getJSONObject("contents");
                            JSONArray quotesArray = content.getJSONArray("quotes");
                            JSONObject quoteObject = quotesArray.getJSONObject(0);

                            String quote = quoteObject.getString("quote");
                            String author = quoteObject.getString("author");
                            quoteText.append("Quote of the day:\n"+quote+"\n~"+author);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        quoteText.clearComposingText();
                        quoteText.append("Enable internet connection to see motivational quotes!");
                    }
                });
        requestQueue.add(request);
    }

}
