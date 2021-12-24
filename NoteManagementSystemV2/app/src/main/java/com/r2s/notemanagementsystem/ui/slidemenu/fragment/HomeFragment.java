package com.r2s.notemanagementsystem.ui.slidemenu.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.constant.CategoryConstant;
import com.r2s.notemanagementsystem.model.Dash;
import com.r2s.notemanagementsystem.model.Status;
import com.r2s.notemanagementsystem.viewmodel.CommunicateViewModel;
import com.r2s.notemanagementsystem.viewmodel.DashboardViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    PieChart pieChart;
    List<PieEntry> pieEntryList = new ArrayList<>();
    private List<Dash> mDashList;
    private DashboardViewModel mDashViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pieChart = view.findViewById(R.id.pieChart);

        mDashViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        mDashViewModel.getDashById().observe(getViewLifecycleOwner(), dashes -> {
            this.mDashList = dashes;

            for ( int i = 0; i < mDashList.size(); i++) {
                String status = mDashList.get(i).getStatus();
                int note = Integer.parseInt(mDashList.get(i).getNote());

                pieEntryList.add(new PieEntry(note, status));
            }

            PieDataSet pieDataSet = new PieDataSet(pieEntryList,"Status");
            pieChart.setData(new PieData(pieDataSet));

            // setting percent value
            pieChart.setUsePercentValues(true);

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
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mDashViewModel.refreshDataDash();
    }
}