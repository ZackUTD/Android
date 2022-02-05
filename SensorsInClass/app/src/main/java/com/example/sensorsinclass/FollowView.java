package com.example.sensorsinclass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View;

public class FollowView extends View
{

    private int xpos, ypos, radius;
    private Paint pCircle;
    private Path path;

    public FollowView(Context context)
    {
        super(context);
        setup();
    }

    public FollowView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setup();
    }


    private void setup()
    {
        xpos = 0;
        ypos = 0;
        pCircle = new Paint();
        pCircle.setColor(Color.BLUE);
        pCircle.setStrokeWidth(6);
        pCircle.setStyle(Paint.Style.FILL);

        path = new Path();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        canvas.drawPath(path, pCircle);
        //canvas.drawCircle(xpos, ypos, 100, pCircle);

    }

    public int getXpos()
    {
        return xpos;
    }

    public void setX(int x)
    {
        xpos = x;
    }

    public int getYpos()
    {
        return ypos;
    }

    public void setY(int y)
    {
        ypos = y;
    }

    public void setRadius(int r)
    {
        radius = r;
    }

    public void addToPath(float x, float y)
    {
        path.addCircle(x, y, 100, Path.Direction.CW);
    }
}
