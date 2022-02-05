package com.example.sensorsinclass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnTouchListener
{

    double x, y, z;
    TextView xV, yV, zV, aV;
    int mAccuracy;
    SensorManager mSensorManager;
    Sensor mAccelerometer;
    final float ALPHA = 0.25f;

    FollowView follow;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, mSensorManager.SENSOR_DELAY_NORMAL);


        follow = findViewById(R.id.circleView);
        //follow = new FollowView(this);

        xV = findViewById(R.id.xVal);
        yV = findViewById(R.id.yVal);
        zV = findViewById(R.id.zVal);
        aV = findViewById(R.id.accuracyText);

        View v = findViewById(R.id.main);
        v.setOnTouchListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /* Is the syntax here correct?? */
    @Override
    public void onResume()
    {
        super.onResume();
    }


    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float[] raw_vals = {event.values[0], event.values[1], event.values[2]};
        float[] filtered_vals = new float[3];
        filtered_vals = lowPass(raw_vals, filtered_vals);

        x = filtered_vals[0];
        y = filtered_vals[1];
        z = filtered_vals[2];

        updateScreen(true);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        mAccuracy = accuracy;
        //updateScreen(false);
    }



    public void updateScreen(boolean valChange)
    {
        if(valChange)
        {
            xV.setText(String.valueOf(x));

            yV.setText(String.valueOf(y));

            zV.setText(String.valueOf(z));
        }
        else
        {
            aV = findViewById(R.id.accuracyText);
            String acc = aV.getText().toString() + String.valueOf(mAccuracy);
            aV.setText(acc);
        }
    }

    protected float[] lowPass(float[] input, float[] output)
    {
        if ( output == null )
        {
            return input;
        }

        for ( int i=0; i<input.length; i++ )
        {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }

        return output;
    }


    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                follow.setVisibility(follow.VISIBLE);
                follow.setRadius(100);
                //follow.setX((int)event.getX());
                //follow.setY((int)event.getY());
                follow.addToPath(event.getX(), event.getY());
                follow.invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                follow.setRadius(100);
                //follow.setX((int)event.getX());
                //follow.setY((int)event.getY());
                follow.addToPath(event.getX(), event.getY());
                follow.invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                //follow.setVisibility(follow.INVISIBLE);
                //follow.setRadius(0);
                //follow.invalidate();
                return false;

            default:
                return false;
        }
    }
}
