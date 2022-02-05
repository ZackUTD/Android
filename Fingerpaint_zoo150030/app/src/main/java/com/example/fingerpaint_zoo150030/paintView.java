package com.example.fingerpaint_zoo150030;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.Stack;


/*
 * Author: Zack Oldham
 * This class defines the paintView object which is a custom View for drawing paths.
 */


public class paintView extends View
{

    private int radius, color;
    private Paint paint;
    private Path path;
    private Stack<Path> pathStack;
    private Stack<Paint> paintStack;


    public paintView(Context context)
    {
        super(context);
        setupDefaults();
    }

    public paintView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setupDefaults();
    }

    /*
     * Initialize all variables with default values, with the exception of path.
     */
    private void setupDefaults()
    {
        radius = 3;
        color = Color.BLACK;
        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.FILL);
        pathStack = new Stack<>();
        paintStack = new Stack<>();
    }


    /*
     * Draw all paths in the stack on the screen (with their corresponding color).
     */
    @Override
    public void onDraw(Canvas canvas)
    {
        for(int i = 0; i < pathStack.size(); i++)
        {
            canvas.drawPath(pathStack.get(i), paintStack.get(i));
        }
    }


    /*
     * Set the current radius to the given value. All subsequent paths will have this radius until
     * it is modified.
     */
    public void setRadius(int r)
    {
       radius = r;
    }

    /*
     * Add a circle to path to simulate a brush moving over paper.
     */
    public void addToPath(float x, float y)
    {
        path.addCircle(x, y, radius, Path.Direction.CW);
    }


    /*
     * Create a new path with the currently selected color, and add it and its corresponding color
     * to their respective stacks.
     */
    public void newPath()
    {
        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.FILL);
        paintStack.push(paint);

        path = new Path();
        pathStack.push(path);
    }

    /*
     * If there is at least one path on the screen, remove it from the screen and the stack (also remove
     * its corresponding color from the stack).
     */
    public void undoPath()
    {
        if(!pathStack.isEmpty())
        {
            Path p = pathStack.pop();
            paintStack.pop();
            p.reset();
        }
    }

    /*
     * Set the current color to the given color. This color will be used for all subsequent paths until
     * modified.
     */
    public void setColor(int c)
    {
        color = c;
    }
}
