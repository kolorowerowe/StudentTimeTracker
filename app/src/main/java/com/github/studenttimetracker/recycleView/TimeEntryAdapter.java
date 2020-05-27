package com.github.studenttimetracker.recycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.studenttimetracker.R;

import java.util.List;

public class TimeEntryAdapter extends RecyclerView.Adapter<TimeEntryAdapter.ViewHolder>{
    private List<TimeEntry> listData;

    public TimeEntryAdapter(List<TimeEntry> listData) {
        this.listData = listData;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.time_entry, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewMain.setText(String.valueOf(listData.get(position).getMainText()));
        holder.textViewSub.setText(listData.get(position).getSubText());
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMain;
        public TextView textViewSub;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewMain = (TextView) itemView.findViewById(R.id.itemNameMain);
            this.textViewSub= (TextView) itemView.findViewById(R.id.itemNameSub);
        }
    }
}
