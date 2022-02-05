package com.example.inclass_2_12;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goClick(View view)
    {
        DLImage img = new DLImage();
        EditText et = findViewById(R.id.imgAddr);
        img.execute(String.valueOf(et.getText()));

    }

    /*
    The following private inner class uses an AsyncTask to download an image from the internet
    in the background, and then display it when it finishes
    */

    private class DLImage extends AsyncTask<String, Void, Bitmap>
    {
        String error;

        @Override
        protected Bitmap doInBackground(String... uList)
        {
            Bitmap bmp = null;
            HttpURLConnection conn = null;
            InputStream is = null;
            ByteArrayOutputStream out =  null;

            try
            {
                conn = (HttpURLConnection) new URL(uList[0]).openConnection();
                conn.connect();
                final int length = conn.getContentLength();

                if(length <= 0)
                {
                    this.cancel(true);
                }

                is = new BufferedInputStream(conn.getInputStream(), 8192);
                out = new ByteArrayOutputStream();
                byte bytes[] = new byte[8192];
                int count = 0;
                long read = 0;
                while((count = is.read(bytes)) != -1)
                {
                    read += count;
                    out.write(bytes, 0, count);
                }

                bmp = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
                return bmp;
            }
            catch(Exception ex)
            {
                System.out.println("Error: " + ex.getMessage());
            }

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bmp)
        {
            if(bmp != null)
            {
                ImageView iv = findViewById(R.id.webImg);
                iv.setImageBitmap(bmp);
            }
        }
    }
}
