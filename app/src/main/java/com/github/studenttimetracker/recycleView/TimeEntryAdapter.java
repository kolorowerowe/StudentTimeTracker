package com.github.studenttimetracker.recycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.studenttimetracker.R;
import com.github.studenttimetracker.models.Task;
import com.github.studenttimetracker.utils.CalendarUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class TimeEntryAdapter extends RecyclerView.Adapter<TimeEntryAdapter.ViewHolder>{
    private final List<Task> listData;

    public TimeEntryAdapter(List<Task> listData) {
        this.listData = listData;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.time_entry, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CalendarUtils.hourFormat);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        holder.textViewMain.setText(String.valueOf(simpleDateFormat.format(listData.get(position).getDuration()*1000)));
        holder.textViewSub.setText(listData.get(position).getTaskName());
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewMain;
        final TextView textViewSub;
        ViewHolder(View itemView) {
            super(itemView);
            this.textViewMain = itemView.findViewById(R.id.itemNameMain);
            this.textViewSub= itemView.findViewById(R.id.itemNameSub);
        }
    }
}
