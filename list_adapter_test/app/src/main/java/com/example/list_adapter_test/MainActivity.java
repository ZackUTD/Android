package com.example.list_adapter_test;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

    /*
     *If any error messages are present on the screen, remove them,
     *display the "loading..." text, and send the user's input to the dataLister.
     */
    public void DisplayClick(View view)
    {
        EditText sym = findViewById(R.id.symbolEntry);
        TextView et = findViewById(R.id.errorText);
        ListView lv = findViewById(R.id.stockList);
        lv.setAdapter(null);
        et.setVisibility(et.INVISIBLE);
        TextView lT = findViewById(R.id.loadingText);
        String symbol = String.valueOf(sym.getText());
        dataLister dL = new dataLister();

        //append input to url and attempt to find the url (lose pts otherwise!)

        lT.setVisibility(lT.VISIBLE);
        dL.execute("http://utdallas.edu/~jxc064000/2017Spring/" + symbol + ".txt");
    }

    //ArrayAdapter child class for creating and managing the rows in the ListView
    public class myArrayAdapter extends ArrayAdapter<String>
    {
        List<String> data;

        //Construct a myArrayAdapter
        public myArrayAdapter(Context context, int textViewResourceId, List<String> input)
        {
            super(context, textViewResourceId, input);
            data = input;
        }

        /*
         * Populate each row with a row of data from the input list.
         */
        @Override
        public View getView(int position, View cvtView, ViewGroup parent)
        {
            Context cx = this.getContext();
            LayoutInflater inflater = (LayoutInflater) cx.getSystemService(cx.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.stocks_layout, parent, false);
            String rowStr = data.get(position);
            String[] rowArr = rowStr.split(",");
            int fieldWidth = (int) (parent.getWidth() * 0.3);

            TextView tv = rowView.findViewById(R.id.Date);
            tv.setText(rowArr[0]);
            tv.setWidth(fieldWidth);

            tv = rowView.findViewById(R.id.Open);
            tv.setText(rowArr[1]);
            tv.setWidth(fieldWidth);

            tv = rowView.findViewById(R.id.High);
            tv.setText(rowArr[2]);
            tv.setWidth(fieldWidth);

            tv = rowView.findViewById(R.id.Low);
            tv.setText(rowArr[3]);
            tv.setWidth(fieldWidth);

            tv = rowView.findViewById(R.id.Close);
            tv.setText(rowArr[4]);
            tv.setWidth(fieldWidth);

            tv = rowView.findViewById(R.id.Vol);
            tv.setText(rowArr[5]);
            tv.setWidth(fieldWidth);

            tv = rowView.findViewById(R.id.AdjClose);
            tv.setText(rowArr[6]);
            tv.setWidth(fieldWidth);

            return rowView;

        }
    }

    /*
     * dataLister is an AsyncTask that collects all of the data from an input file,
     * stores it in an ArrayList, and sends that data to a myArrayAdapter which
     * is then connected to the existing ListView.
     */
    private class dataLister extends AsyncTask<String, Void, ArrayList<String>>
    {
        Boolean responseTimeout = false;

        /*
         * Open a connection to the file, and read the data into an ArrayList. If the connection
         * takes 45 seconds or longer to be created, set the responseTimeout flag.
         */
        @Override
        protected ArrayList<String> doInBackground(String... inputs)
        {
            Scanner in = null;
            HttpURLConnection conn = null;
            ArrayList<String> dataSet = null;

            int initTime = (int)System.currentTimeMillis();


            try
            {
                conn = (HttpURLConnection) new URL(inputs[0]).openConnection();
                conn.connect();

                int currentTime = (int)System.currentTimeMillis();

                if(currentTime - initTime >= 45000)
                {
                    responseTimeout = true;
                    return null;
                }

                in = new Scanner(conn.getInputStream());
                dataSet = new ArrayList<>();

                while(in.hasNext())
                {
                    dataSet.add(in.nextLine());
                }

                return dataSet;

            }
            catch(Exception ex)
            {
                System.out.println("Error: " + ex.getMessage());
            }

            return dataSet;
        }

        /*
         * Display the "loading..." text. If the ArrayList containing the data read from the file is
         * not null, create a new myArrayAdapter with the data, and connect it to the existing ListView.
         * If the ArrayList was null, and the responseTimeout flag was not set, then display the
         * error TextView with a message explaining that the entered symbol was not acceptable.
         * If the responseTimeout flag was set, then display the error TextView with a message
         * explaining that there was a problem setting up the connection.
         */
        @Override
        protected void onPostExecute(ArrayList<String> ds)
        {

            TextView tv = findViewById(R.id.loadingText);
            tv.setVisibility(tv.INVISIBLE);

            //if data was read without issue, create an adapter for the data and attach it to our ListView
            if(ds != null)
            {
                myArrayAdapter listAdapter = new myArrayAdapter(MainActivity.this, R.layout.stocks_layout, ds);
                ListView lv = findViewById(R.id.stockList);
                lv.setAdapter(listAdapter);
            }
            else
            {
                tv = findViewById(R.id.errorText);

                if(!responseTimeout)
                {
                    tv.setText(R.string.error1);
                }
                else
                {
                    tv.setText(R.string.error2);
                }

                tv.setVisibility(tv.VISIBLE);
            }
        }
    }
}

