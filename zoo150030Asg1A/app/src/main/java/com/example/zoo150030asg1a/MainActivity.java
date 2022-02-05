package com.example.zoo150030asg1a;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String fruitList = ""; //stores order fruits are clicked
    private int numFruits = 0; //keep track of how many fruits have been clicked
    Button nextButton; //the button to go to the second view


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //layout inflated
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nextButton = findViewById(R.id.nextButton); //get the view for the button

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

    //@fruitClick
    //@param: the fruit view being clicked
    //@return: void
    //fruitClick makes the clicked fruit disappear, updates the number of fruits that have been clicked
    //if all fruits have been clicked, button becomes visible.
    public void fruitClick(View view)
    {
        view.setVisibility(view.INVISIBLE);
        TextView tView = (TextView)view;

        fruitList = fruitList + tView.getText() + "\n";

        numFruits++;

        if(numFruits == 5)
        {
            nextButton.setVisibility(nextButton.VISIBLE);
        }

    }

    //@buttonClick
    //@param: the button that is clicked
    //@return: void
    //buttonClick creates a new intent and puts the string of fruits into the intent before starting the second Activity.
    public void buttonClick(View view)
    {
        Intent intent = new Intent(this, clickListActivity.class);
        intent.putExtra("fruits", fruitList);
        startActivity(intent);

    }
}
