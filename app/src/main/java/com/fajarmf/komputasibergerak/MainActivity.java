package com.fajarmf.komputasibergerak;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private SensorManager mSensorManager;
//    TextView mSensor, mSensorNumbers;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView) findViewById(R.id.listview);
        final Button btnGyro = (Button) findViewById(R.id.btnAccel);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        List<Sensor> mSensorAvailable = sensorManager.getSensorList(Sensor.TYPE_ALL);

        ArrayList<String> sensorLists = new ArrayList<String>();
        Sensor tmp;
        int i;
        for(i=0; i<mSensorAvailable.size(); i++){
            tmp = mSensorAvailable.get(i);
            sensorLists.add(tmp.getName());
        }

        ListView mlistView = (ListView) findViewById(R.id.listview);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.sensorlist, R.id.sensor, sensorLists);
        mlistView.setAdapter(arrayAdapter);

        btnGyro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, RealtimeAccel.class);
                startActivity(mIntent);
            }
        });

    }
}