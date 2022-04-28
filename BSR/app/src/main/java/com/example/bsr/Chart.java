package com.example.bsr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Chart extends AppCompatActivity {

    ImageView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart);

        arrow = findViewById(R.id.image_back);

        arrow.setOnClickListener(view -> {
            Intent intent = new Intent(Chart.this, Dashboard.class);
            startActivity(intent);

        });

        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> spending = new ArrayList<>();
        spending.add(new BarEntry(2019, 750));
        spending.add(new BarEntry(2020, 850));
        spending.add(new BarEntry(2021, 775));
        spending.add(new BarEntry(2022, 1000));

        BarDataSet barDataSet = new BarDataSet(spending, "Spending");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(20f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Chart Example");
        barChart.animateY(2000);


    }
}