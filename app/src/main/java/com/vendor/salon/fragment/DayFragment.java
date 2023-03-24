package com.vendor.salon.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.home.DailySalesItem;

import java.util.ArrayList;
import java.util.List;

public class DayFragment extends Fragment {

    private List<DailySalesItem> salesData;

    public DayFragment(List<DailySalesItem> sales) {
        // Required empty public constructor
        this.salesData = sales;
    }

    public DayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        if (salesData != null && salesData.size() > 0) {
            String[] days = new String[salesData.size()];

            ArrayList<BarEntry> information = new ArrayList<>();
            for (int i = 0; i < salesData.size(); i++) {
                days[i] = salesData.get(i).getDate();
                information.add(new BarEntry(i, Float.parseFloat(String.valueOf(salesData.get(i).getTotalSales()))));
            }


            BarDataSet dataSet = new BarDataSet(information, "Daily Sales (in K)");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setValueTextSize(9f);


            BarData bardata = new BarData(dataSet);
            BarChart barChartView = view.findViewById(R.id.day_barchart);
            barChartView.setFitBars(true);

            barChartView.setData(bardata);
            barChartView.getDescription().setEnabled(false);
            barChartView.animateY(1000);

            XAxis xAxis = barChartView.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);

            xAxis.setCenterAxisLabels(false);
            xAxis.setLabelRotationAngle(-90);
            xAxis.setGranularityEnabled(true);
//        if (salesData)
            xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
            xAxis.setAxisMaximum(days.length );
            xAxis.setLabelCount( days.length + 1, false);
            barChartView.setDragEnabled(true);
            barChartView.getAxisRight().setEnabled(false);

            YAxis leftAxis = barChartView.getAxisLeft();
//        leftAxis.setDrawAxisLine(false);
            leftAxis.setDrawGridLines(false);
            leftAxis.setSpaceTop(17f);

            bardata.setBarWidth(1f);
            barChartView.invalidate();
//            barChartView.setPinchZoom(true);
//            barChartView.setExtraBottomOffset(50);
//            barChartView.getLegend().setEnabled(false);
//            barChartView.setDrawBarShadow(false);
//        barChartView.se;

//        barChartView.setDrawGridBackground(false);
        }

        return view;
    }
}