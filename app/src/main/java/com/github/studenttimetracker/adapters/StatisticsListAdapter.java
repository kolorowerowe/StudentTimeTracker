package com.github.studenttimetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.studenttimetracker.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StatisticsListAdapter extends ArrayAdapter<StatisticsCard> {
    private List<StatisticsCard> cardList = new ArrayList<>();

    static class StatisticsCardHolder {
        TextView statistics_list_element_title;
        TextView statistics_list_element_secondary_text;
    }

    public StatisticsListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    @Override
    public void add(StatisticsCard object) {
        cardList.add(object);
        super.add(object);
    }

    public void addAllWithClear(@NonNull Collection<? extends StatisticsCard> collection) {
        this.clear();
        super.addAll(collection);
    }

    @Override
    public void clear() {
        cardList.clear();
        super.clear();
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public StatisticsCard getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        StatisticsCardHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.statistics_list_element, parent, false);
            viewHolder = new StatisticsCardHolder();
            viewHolder.statistics_list_element_title = row.findViewById(R.id.statistics_list_element_title);
            viewHolder.statistics_list_element_secondary_text = row.findViewById(R.id.statistics_list_element_secondary_text);
            row.setTag(viewHolder);
        } else {
            viewHolder = (StatisticsCardHolder) row.getTag();
        }
        StatisticsCard card = getItem(position);
        viewHolder.statistics_list_element_title.setText(card.getStatistics_list_element_title());
        viewHolder.statistics_list_element_secondary_text.setText(card.getStatistics_list_element_secondary_text());
        return row;
    }
}