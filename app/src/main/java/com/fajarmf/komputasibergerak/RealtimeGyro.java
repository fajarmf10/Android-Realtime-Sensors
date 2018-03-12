package com.fajarmf.komputasibergerak;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.util.ArrayList;
import java.util.List;

import static android.util.Half.EPSILON;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by ASUS on 3/12/2018.
 */

public class RealtimeGyro extends AppCompatActivity implements SensorEventListener {
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private Runnable mTimer3;
    private GraphView graphView1;
    private GraphView graphView2;
    private GraphView graphView3;
    private GraphViewSeries series1;
    private GraphViewSeries series2;
    private GraphViewSeries series3;
    private float sensorX = 0;
    private float sensorY = 0;
    private float sensorZ = 0;
    private List<GraphView.GraphViewData> seriesX;
    private List<GraphView.GraphViewData> seriesY;
    private List<GraphView.GraphViewData> seriesZ;
    int dataCount = 1;
    private SensorManager sManager;

    private static final float NS2S = 1.0f / 1000000000.0f;
    private final double[] deltaRotationVector = new double[4];
    private float timestamp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_gyroplot);
        seriesX = new ArrayList<GraphView.GraphViewData>();
        seriesY = new ArrayList<GraphView.GraphViewData>();
        seriesZ = new ArrayList<GraphView.GraphViewData>();

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        series1 = new GraphViewSeries(new GraphView.GraphViewData[]{});
        graphView1 = new LineGraphView(this, "X");
        graphView1.addSeries(series1);
        LinearLayout layout = (LinearLayout) findViewById(R.id.gyro1);
        layout.addView(graphView1);

        series2 = new GraphViewSeries(new GraphView.GraphViewData[]{});
        graphView2 = new LineGraphView(this, "Y");
        graphView2.addSeries(series2);
        layout = (LinearLayout) findViewById(R.id.gyro2);
        layout.addView(graphView2);

        series3 = new GraphViewSeries(new GraphView.GraphViewData[]{});
        graphView3 = new LineGraphView(this, "Z");
        graphView3.addSeries(series3);
        layout = (LinearLayout) findViewById(R.id.gyro3);
        layout.addView(graphView3);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
            sensorX = event.values[0];
            sensorY = event.values[1];
            sensorZ = event.values[2];
        seriesX.add(new GraphView.GraphViewData(dataCount, sensorX));
        seriesY.add(new GraphView.GraphViewData(dataCount, sensorY));
        seriesZ.add(new GraphView.GraphViewData(dataCount, sensorZ));
        dataCount++;

        if (seriesX.size() > 500) {
            seriesX.remove(0);
            seriesY.remove(0);
            seriesZ.remove(0);
            graphView1.setViewPort(dataCount - 500, 500);
            graphView2.setViewPort(dataCount - 500, 500);
            graphView3.setViewPort(dataCount - 500, 500);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStop() {
        sManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        mHandler.removeCallbacks(mTimer1);
        mHandler.removeCallbacks(mTimer2);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI);

        mTimer1 = new Runnable() {
            @Override
            public void run() {
                GraphView.GraphViewData[] gvd = new GraphView.GraphViewData[seriesX.size()];
                seriesX.toArray(gvd);
                series1.resetData(gvd);
                mHandler.post(this); //, 100);
            }
        };
        mHandler.postDelayed(mTimer1, 100);

        mTimer2 = new Runnable() {
            @Override
            public void run() {

                GraphView.GraphViewData[] gvd = new GraphView.GraphViewData[seriesY.size()];
                seriesY.toArray(gvd);
                series2.resetData(gvd);

                mHandler.post(this);
            }
        };
        mHandler.postDelayed(mTimer2, 100);


        mTimer3 = new Runnable() {
            @Override
            public void run() {

                GraphView.GraphViewData[] gvd = new GraphView.GraphViewData[seriesZ.size()];
                seriesZ.toArray(gvd);
                series3.resetData(gvd);

                mHandler.post(this);
            }
        };
        mHandler.postDelayed(mTimer3, 100);

    }
}
