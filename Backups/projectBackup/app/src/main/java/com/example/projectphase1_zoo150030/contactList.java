package com.example.projectphase1_zoo150030;

/*
 * Author: Zack Oldham
 * Date: 02-23-2019
 * Project Phase 1
 * This application will consist of a contact manager that allows the user to view their their
 * contacts in a list as well as add or remove contacts. The contact data will be stored in a
 * text file "database".
 */


import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class contactList extends AppCompatActivity implements SensorEventListener
{
    private String selectedContact;
    private final int MIN_ACCEL = 15;
    private dbManager db;

    private SensorManager Smgr;
    private Sensor accel;

    private static final int MODE_REFRESH = 0;
    private static final int MODE_INSERT = 1;
    private static final int MODE_UPDATE = 2;
    private static final int MODE_DELETE = 3;
    private static final int MODE_REVERSE = 4;
    private static final int MODE_IMPORT = 5;
    private static final int MODE_REINIT = 6;
    private static final int MODE_GET_CONTACT = 7;


    /*
     * When the app is opened, initialize the Sensor Manager, accelerometer, and the sensor listener
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Smgr = (SensorManager)getSystemService(SENSOR_SERVICE);
        accel = Smgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Smgr.registerListener(this, accel, Smgr.SENSOR_DELAY_NORMAL);

        db = new dbManager(contactList.this);

        DataGenerator dg = new DataGenerator(MODE_REFRESH);
        dg.execute((Contact)null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
     * NOTE: You will find the options for adding a contact, reinitializing the database, and importing
     * contacts from an external file in the drop down menu located in the upper right-hand corner of the screen.
     */

    /*
     * When the add contact item is selected from the options list, the second activity will be
     * brought on to the screen in the edit mode so that the user can enter the information for
     * a new contact.
     */
    public void onAddContact(MenuItem item)
    {
        Intent addIntent = new Intent(this, contactDetails.class);
        addIntent.putExtra("mode", true);
        startActivityForResult(addIntent, 0);
    }


    /*
     * Import existing contacts from external datafile and add to database.
     */
    public void onImport(MenuItem item)
    {
        DataGenerator dg = new DataGenerator(MODE_IMPORT);
        dg.execute((Contact)null);
    }


    /*
     * Reinitialize the database.
     */
    public void onReInit(MenuItem item)
    {
        DataGenerator dg = new DataGenerator(MODE_REINIT);
        dg.execute((Contact)null);
    }


    /*
     * When a contact is selected from the list of contacts, the second activity will be brought
     * up to the screen with the details of the selected contact in the display mode
     * (Cannot be edited). An "edit" button will present should the user decide to edit the
     * presented details.
     */
    public void onSelectContact(View view)
    {
        TextView tv = (TextView)view;
        DataGenerator dg = new DataGenerator(MODE_GET_CONTACT);
        Contact selected = new Contact();
        String[] name = tv.getText().toString().split(", ");

        selected.setLast(name[0]);
        selected.setFirst(name[1]);

        dg.execute(selected);
    }

    /*
     * When the accelerometer sensor detects a change in the x, y, or z direction accelerations that
     * is greater than a reasonable minimum, reverse the contents of the list (alphabetical to reverse-alpha
     * or visa-versa).
     */
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.values[0] >= MIN_ACCEL || event.values[1] >= MIN_ACCEL || event.values[2] >= MIN_ACCEL)
        {
            DataGenerator dg = new DataGenerator(MODE_REVERSE);
            dg.execute((Contact)null);
        }
    }

    /*
     * Required overloaded method from SensorEventListener interface - not currently needed for this assignment.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        //do nothing
    }

    /*
     * Check the resultCode returned from the called activity, and perform actions corresponding to that code.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode > 0)
        {
            Contact fresh = new Contact(data.getStringExtra("freshContact"));
            DataGenerator dg = new DataGenerator(resultCode);
            dg.execute(fresh);
        }
    }

    /*
     * Class listGenerator
     * The listGenerator asyncTask is used for interacting with the database. This includes updates,
     * specific queries, full queries, deletes, and clearing the database.
     */
    public class DataGenerator extends AsyncTask<Contact, Void, ArrayList<Contact>>
    {
        private boolean error;
        private String errmsg;
        private int code;

        public DataGenerator(int cd)
        {
            code = cd;
        }


        /*
         * Determine which command has been issued, and perform that task, always refreshing when applicable
         */
        @Override
        protected ArrayList<Contact> doInBackground(Contact... args)
        {
            try
            {
                if(args[0] == null && ((code > MODE_REFRESH && code < MODE_REVERSE) || code == MODE_GET_CONTACT))
                {
                    throw new IllegalArgumentException("Null Contact Exception");
                }

                switch(code)
                {
                    case MODE_REFRESH:
                        return db.query();
                    case MODE_INSERT: //insert particular contact
                        db.insertContact(args[0]);
                        return db.query();
                    case MODE_UPDATE: //update a particular contact
                        db.updateContact(args[0]);
                        return db.query();
                    case MODE_DELETE: //delete a contact
                        db.deleteContact(args[0]);
                        return db.query();
                    case MODE_REVERSE: //reverse order of contacts
                        db.reverseOrder();
                        return db.query();
                    case MODE_IMPORT: //import data from file into database
                        db.importData("datafile.txt");
                        return db.query();
                    case MODE_REINIT: //reinitialize the database
                        db.reInit();
                        return db.query();
                    case MODE_GET_CONTACT: //get a particular contact
                        ArrayList<Contact> data = new ArrayList<>();
                        data.add(db.query(args[0].getFirst(), args[0].getLast()));
                        return data;
                    default:
                        throw new IllegalArgumentException("Invalid Database Operation");
                }
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
                error = true;
                errmsg = ex.getMessage();
                return null;
            }
        }

        /*
         *  If the error flag was set, display the error on the screen and hide the list.
         *  Otherwise, if the dataset was not null, determine what the code was, and act accordingly.
         *  If the the dataset was null, simply hide the list.
         */
        @Override
        protected void onPostExecute(ArrayList<Contact> ds)
        {
            ListView lv = findViewById(R.id.contactList);

            if(error)
            {
                lv.setVisibility(lv.INVISIBLE);

                TextView errMsg = findViewById(R.id.errorMsg);
                errMsg.setText(errmsg);
                errMsg.setVisibility(errMsg.VISIBLE);
            }
            else if(ds != null)
            {
                if(code != MODE_GET_CONTACT)
                {
                    lv.setVisibility(lv.VISIBLE);
                    contactsAdapter cA = new contactsAdapter(contactList.this, R.layout.contacts_layout, ds);
                    lv.setAdapter(cA);
                }
                else
                {
                    Intent displayIntent = new Intent(contactList.this, contactDetails.class);
                    displayIntent.putExtra("mode", false);

                    displayIntent.putExtra("contact", ds.get(0).toString());
                    startActivityForResult(displayIntent, 0);
                }
            }
            else
            {
                lv.setVisibility(lv.INVISIBLE);
            }
        }
    }
}
