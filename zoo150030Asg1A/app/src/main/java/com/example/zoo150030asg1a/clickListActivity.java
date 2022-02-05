package com.example.zoo150030asg1a;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class clickListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_list); //Activity inflated

        Intent fruitIntent = getIntent(); //get an intent from the first activity
        if(fruitIntent != null) //if there was an intent created in the first activity, extract the data from it
        {
            String data = fruitIntent.getStringExtra("fruits");

            //if the data is not null, find the textView in the second activity, and set its text to the data from the first activity
            if(data != null)
            {
                TextView fOrder = findViewById(R.id.fruitOrder);
                fOrder.setText(data);
            }
        }


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






}
