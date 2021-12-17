package com.r2s.notemanagementsystem.ui.slidemenu.fragment;

import                                                      android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.constant.CategoryConstant;
import com.r2s.notemanagementsystem.model.Status;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    PieChart pieChart;
    List<PieEntry> pieEntryList = new ArrayList<>();
    List<Status> statuses;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        pieChart = view.findViewById(R.id.pieChart);
        statuses = new ArrayList<>();
        initView();

        return view;
    }

    private void initView() {
        pieEntryList.clear();
        pieChart.setUsePercentValues(true);

        pieEntryList.add(new PieEntry(6, "Done"));
        pieEntryList.add(new PieEntry(4, "Process"));
        pieEntryList.add(new PieEntry(4, "Pending"));

        PieDataSet pieDataSet = new PieDataSet(pieEntryList,"Status");
        pieChart.setData(new PieData(pieDataSet));

        // setting size of text
        pieDataSet.setValueTextSize(16f);

        // Setting color for pie chart
        pieDataSet.setColors(CategoryConstant.COLOR_RGB);

        // Delete the hole inside pie chart
        pieChart.setDrawHoleEnabled(false);

        // Disable description
        pieChart.getDescription().setEnabled(false);

        // Disable rotation
        pieChart.setRotationEnabled(false);

        // load data
        pieChart.invalidate();
    }
}