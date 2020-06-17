package com.linkknown.ilearning.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.linkknown.ilearning.R;

import java.util.ArrayList;
import java.util.List;

public class RadarChartView extends LinearLayout {

    private RadarChart chart;

    public RadarChartView(Context context) {
        super(context);
    }

    public RadarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.layout_radar_chart_view, this);

        // 获取控件
        chart = findViewById(R.id.radarChart);
    }

    private void initRadarChartView(String[] labels) {
        //设置web线的颜色(即就是外面包着的那个颜色)
        chart.setWebColorInner(Color.BLACK);
        //设置中心线颜色(也就是竖着的线条)
        chart.setWebColor(Color.BLACK);
        chart.setWebAlpha(50);

        XAxis xAxis = chart.getXAxis();
        //设置x轴标签字体颜色
        xAxis.setLabelCount(4, true);
        xAxis.setAxisMaximum(4f);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextSize(12f);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return labels[(int) value % labels.length];
            }
        });
        YAxis yAxis = chart.getYAxis();
        //设置y轴的标签个数
        yAxis.setLabelCount(5, true);
        //设置y轴从0f开始
        yAxis.setAxisMinimum(0f);
        /*启用绘制Y轴顶点标签，这个是最新添加的功能
         * */
        yAxis.setDrawTopYLabelEntry(false);
        //设置字体大小
        yAxis.setTextSize(9f);
        //设置字体颜色
        yAxis.setTextColor(Color.RED);

        //启用线条，如果禁用，则无任何线条
        chart.setDrawWeb(true);

        //禁用图例和图表描述
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
    }

    /**
     * 设置数据
     */
    private void initRadarChartData(float[] values) {
        List<RadarEntry> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new RadarEntry(values[i]));
        }
        RadarDataSet set = new RadarDataSet(list, "成果");

        //使用标签
        set.setDrawValues(true);
        //设置填充颜色
        set.setFillColor(Color.BLUE);
        //设置填充透明度
        set.setFillAlpha(40);
        //设置启用填充
        set.setDrawFilled(true);
        //设置点击之后标签是否显示圆形外围
        set.setDrawHighlightCircleEnabled(true);
        //设置点击之后标签圆形外围的颜色
        set.setHighlightCircleFillColor(Color.RED);
        //设置点击之后标签圆形外围的透明度
        set.setHighlightCircleStrokeAlpha(40);
        //设置点击之后标签圆形外围的半径
        set.setHighlightCircleInnerRadius(20f);
        //设置点击之后标签圆形外围内圆的半径
        set.setHighlightCircleOuterRadius(10f);


        RadarData data = new RadarData(set);
        chart.setData(data);
        chart.invalidate();
    }

    public void show (String[] labels, float[] values) {
        // egg
//        String[] labels = new String[]{"勤奋", "天赋", "兴趣", "智力", "效果"};
//        float[] values = new float[] {10, 20, 30, 40, 50};
        initRadarChartView(labels);
        initRadarChartData(values);
    }
}
