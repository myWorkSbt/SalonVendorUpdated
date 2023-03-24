package com.vendor.salon.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vendor.salon.R;
import com.vendor.salon.data_Class.home.YearlySalesItem;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class YearFragment extends Fragment {

    private List<YearlySalesItem> yearlySalesItem = new ArrayList<>();

    public YearFragment(List<YearlySalesItem> yearlySalesItem) {
        this.yearlySalesItem = yearlySalesItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //

    }

    public YearFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_year, container, false);
        if (yearlySalesItem != null && yearlySalesItem.size() > 0) {
            String[] years = new String[yearlySalesItem.size()];
//            years[0] = "";

            ArrayList<BarEntry> information = new ArrayList<>();
//            information.add(new BarEntry(0f, 0f));
            if (yearlySalesItem != null) {
                for (int i = 0; i < yearlySalesItem.size(); i++) {
                    YearlySalesItem yearlySalesI = yearlySalesItem.get(i);
                    years[i] = yearlySalesI.getYear() + "";
                    information.add(new BarEntry(i , Float.parseFloat(String.valueOf(yearlySalesI.getTotalSales()))));
                }
            }

//

            BarDataSet dataSet = new BarDataSet(information, "Yearly Sales(in cr)");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setDrawValues(true);
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setValueTextSize(10f);


            BarData bardata = new BarData(dataSet);
            BarChart barChartView = view.findViewById(R.id.year_barchart);
            barChartView.setData(bardata);
            barChartView.getDescription().setEnabled(false);


            XAxis xAxis = barChartView.getXAxis();
            xAxis.setCenterAxisLabels(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setAxisMaximum(yearlySalesItem.size());
            xAxis.setValueFormatter(new IndexAxisValueFormatter(years));
            xAxis.setLabelCount(yearlySalesItem.size() + 1, false);

            barChartView.setDragEnabled(true);
            barChartView.getAxisRight().setEnabled(false);

            YAxis leftAxis = barChartView.getAxisLeft();
//        leftAxis.setDrawAxisLine(false);
//            leftAxis.setDrawGridLines(true);
            leftAxis.setAxisMinimum(0f);
            leftAxis.setSpaceTop(17f);

            barChartView.invalidate();
//            barChartView.setPinchZoom(true);
//            barChartView.getLegend().setEnabled(false);
            barChartView.animateY(1000);
//            barChartView.setDrawBarShadow(false);
            barChartView.setDrawValueAboveBar(true);
            barChartView.setDrawGridBackground(false);

        }
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}