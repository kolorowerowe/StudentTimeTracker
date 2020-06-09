package com.github.studenttimetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.github.studenttimetracker.R;
import com.github.studenttimetracker.adapters.StatisticsCard;
import com.github.studenttimetracker.adapters.StatisticsListAdapter;
import com.github.studenttimetracker.database.Repository;
import com.github.studenttimetracker.enums.StatisticsPeriodType;
import com.github.studenttimetracker.model.StatisticsDataEntry;
import com.github.studenttimetracker.model.StatisticsQueryObject;
import com.github.studenttimetracker.models.Task;
import com.github.studenttimetracker.utils.CalendarUtils;
import com.github.studenttimetracker.utils.StatisticsMapper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;

import org.joda.time.LocalDate;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class StatisticsFragment extends Fragment {

    private Repository repository;
    private StatisticsPeriodType unitType = StatisticsPeriodType.DAY;
    private Map<StatisticsPeriodType, Integer> currentIndexes = new EnumMap<>(StatisticsPeriodType.class);

    private TextView textView;
    private Button previousButton;
    private Button nextButton;

    private LocalDate firstDate;
    private List<StatisticsQueryObject> statisticsQueryObject;

    private Pie statisticsPieChart;
    private ListView statisticsListView;
    private StatisticsListAdapter statisticsListAdapter;
    private NestedScrollView statisticsCardView;

    public StatisticsFragment() {
        currentIndexes.put(StatisticsPeriodType.DAY, 0);
        currentIndexes.put(StatisticsPeriodType.WEEK, 0);
        currentIndexes.put(StatisticsPeriodType.MONTH, 0);
        currentIndexes.put(StatisticsPeriodType.YEAR, 0);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        try {
            repository = new Repository(getContext());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            DateFormat formatter = new SimpleDateFormat(CalendarUtils.timestampFormat);
            Task firstTask = repository.getEarliestTask();
            firstDate = LocalDate.fromDateFields(formatter.parse(firstTask.getTimeTo()));
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            firstDate = LocalDate.now().minusMonths(6);
        }
        statisticsQueryObject = CalendarUtils.getStatisticsQueryList(unitType, firstDate, LocalDate.now());

        //     DAY | WEEK | MONTH | YEAR
        final TabLayout tabs = view.findViewById(R.id.statistics_period_tabs);
        tabs.addOnTabSelectedListener(onTabSelectedListener);


        //    <- DD-MMM-YYY ->
        textView = view.findViewById(R.id.date_picker_text);

        previousButton = view.findViewById(R.id.date_picker_previous);
        previousButton.setOnClickListener(onPreviousButtonListener);

        nextButton = view.findViewById(R.id.date_picker_next);
        nextButton.setOnClickListener(onNextButtonListener);

        //   PIE CHART
        AnyChartView statisticsPieChartView = view.findViewById(R.id.statistics_chart);
        statisticsPieChart = AnyChart.pie();
        statisticsPieChart.setOnClickListener(pieChartListener);
        statisticsPieChart.labels().position("outside");
        statisticsPieChart.legend(false);
        statisticsPieChartView.setChart(statisticsPieChart);

        // LIST OF ACTIVITIES
        statisticsListView = view.findViewById(R.id.statistics_list);
        statisticsListAdapter = new StatisticsListAdapter(getContext(), R.layout.statistics_list_element);
        statisticsListView.setAdapter(statisticsListAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        refreshDatePicker();
    }

    private void refreshDatePicker() {
        textView.setText(statisticsQueryObject.get(currentIndexes.get(unitType)).getName());
        previousButton.setEnabled(statisticsQueryObject.get(currentIndexes.get(unitType)).isHasPrevious());
        nextButton.setEnabled(statisticsQueryObject.get(currentIndexes.get(unitType)).isHasNext());
        try {
            refreshPieData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshPieData() throws SQLException {

        DateFormat formatter = new SimpleDateFormat(CalendarUtils.timestampFormat);
        String dateFrom = formatter.format(statisticsQueryObject.get(currentIndexes.get(unitType)).getDateFrom());
        String dateTo = formatter.format(statisticsQueryObject.get(currentIndexes.get(unitType)).getDateTo());

        List<Task> projectList = repository.getTasksInDateRange(dateFrom, dateTo);

        List<StatisticsDataEntry> statisticsDataEntries = StatisticsMapper.getProjectsStatistics(projectList);

        List<DataEntry> dataEntries = new ArrayList<>();
        for (StatisticsDataEntry entry : statisticsDataEntries) {
            dataEntries.add(entry.valueDataEntry());
        }
        statisticsPieChart.data(dataEntries);

        statisticsListAdapter.clear();
        for (int i = 0; i < statisticsDataEntries.size(); i++) {
            statisticsListAdapter.add(new StatisticsCard(statisticsDataEntries.get(i).getName(), CalendarUtils.formatSeconds(statisticsDataEntries.get(i).getSeconds())));
        }

    }

    private OnTabSelectedListener onTabSelectedListener = new OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                default:
                    unitType = StatisticsPeriodType.DAY;
                    break;
                case 1:
                    unitType = StatisticsPeriodType.WEEK;
                    break;
                case 2:
                    unitType = StatisticsPeriodType.MONTH;
                    break;
                case 3:
                    unitType = StatisticsPeriodType.YEAR;
                    break;
            }
            statisticsQueryObject = CalendarUtils.getStatisticsQueryList(unitType, firstDate, LocalDate.now());
            refreshDatePicker();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private View.OnClickListener onPreviousButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentIndexes.put(unitType, currentIndexes.get(unitType) + 1);
            refreshDatePicker();
        }
    };

    private View.OnClickListener onNextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentIndexes.put(unitType, currentIndexes.get(unitType) - 1);
            refreshDatePicker();
        }
    };

    private ListenersInterface.OnClickListener pieChartListener = new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
        @Override
        public void onClick(Event event) {
            //TODO 17/05/2020: handle click better than toast
            Toast.makeText(getContext(), event.getData().get("x") + ":" + event.getData().get("value") + "", Toast.LENGTH_SHORT).show();
        }
    };

}
