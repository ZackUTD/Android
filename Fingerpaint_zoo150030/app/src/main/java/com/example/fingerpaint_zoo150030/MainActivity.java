package com.example.fingerpaint_zoo150030;

import android.graphics.Color;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import java.util.Stack;

/*
 * Author: Zack Oldham
 * This class defines the Main Activity of the Fingerpaint project. This activity handles changes to
 * the seekBar, touches on the screen, and clicks on any buttons.
 */


/*
 * Initialize variables and set touch/seekBarChanged listeners
 */
public class MainActivity extends AppCompatActivity implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener
{

    private paintView brush;
    private SeekBar slider;
    private View parent;
    private static final int MYGREEN = 0xFF00FF00;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        brush = findViewById(R.id.paintBrush);
        slider = findViewById(R.id.sizeBar);
        slider.setMax(100);
        slider.setOnSeekBarChangeListener(this);

        parent = findViewById(R.id.main);
        parent.setOnTouchListener(this);

    }


    /*
     * When a color button is clicked, set the color of the brush to the corresponding color.
     * When the "undo" button is pressed, remove the path most recently drawn.
     */
    public void onButtonClick(View view)
    {
        int id = view.getId();

        switch(id)
        {
            case R.id.rButton:
                brush.setColor(Color.RED);
                break;

            case R.id.gButton:
                //brush.setColor(Color.GREEN);
                brush.setColor(MYGREEN);
                break;

            case R.id.bButton:
                brush.setColor(Color.BLUE);
                break;

            case R.id.blkButton:
                brush.setColor(Color.BLACK);
                break;

            case R.id.undoButton:
                brush.undoPath();
                brush.invalidate();
                break;

            default:
                System.out.println("ERR: INVALID BUTTON PRESS");
        }
    }


    /*
     * When the thumb icon on the slider is moved, reset the size of the brush accordingly.
     * (Where the minimum is 3 pixels, and the maximum is one fourth the width of the screen)
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        int radiusRange = (parent.getWidth()/4) - 3;
        double progress_dbl = (double)progress;
        int newRadius = (int)(3 + (radiusRange * (progress_dbl/100)));
        brush.setRadius(newRadius);
    }


    /*
     * Unused method; needed to satisfy the OnSeekBarChangedListener interface
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    /*
     * Unused method; needed to satisfy the OnSeekBarChangedListener interface
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}



    /*
     * When the screen is touched (not on a button or on the slider), draw a path following the user's
     * finger, in the currently selected color. When their finger is lifted, cease drawing.
     * When they begin to draw again, keep previous paths on the screen while drawing the new one.
     */
    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                brush.newPath();
                brush.addToPath(event.getX(), event.getY());
                brush.invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                brush.addToPath(event.getX(), event.getY());
                brush.invalidate();
                return true;

            default:
                return false;
        }
    }



}
