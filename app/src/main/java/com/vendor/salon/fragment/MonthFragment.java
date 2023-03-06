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
import com.vendor.salon.data_Class.home.MonthlySalesItem;

import java.util.ArrayList;
import java.util.List;

public class MonthFragment extends Fragment {

    List<DailySalesItem> dailySales;
    //    Sales monthlySales;
    final String[] months = new String[]{"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    public MonthFragment(List<DailySalesItem> dailySales) {
        this.dailySales = dailySales;
    }
//    public MonthFragment(Sales monthlySales, int year) {
//        this.monthlySales = monthlySales;
//        this.year = year;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        if (dailySales != null && dailySales.size() > 0) {
            String[] days = new String[dailySales.size()];

            ArrayList<BarEntry> information = new ArrayList<>();
            for (int i = 0; i < dailySales.size(); i++) {
                days[i] = dailySales.get(i).getDate();
                information.add(new BarEntry(i, Float.parseFloat(String.valueOf(dailySales.get(i).getTotalSales()))));
            }


            BarDataSet dataSet = new BarDataSet(information, "Daily Sales (in K)");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setValueTextSize(9f);


            BarData bardata = new BarData(dataSet);
            BarChart barChartView = view.findViewById(R.id.month_barchart);
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
            xAxis.setAxisMaximum(days.length);
            xAxis.setLabelCount(days.length + 1, false);
            barChartView.setDragEnabled(true);
            barChartView.getAxisRight().setEnabled(false);

            YAxis leftAxis = barChartView.getAxisLeft();
//        leftAxis.setDrawAxisLine(false);
            leftAxis.setDrawGridLines(false);
            leftAxis.setSpaceTop(17f);

            bardata.setBarWidth(1f);
            barChartView.invalidate();
//       }

        }
        return view;
    }

}