package com.example.phase2_test;

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
    private final int EXISTING_CONTACT = 0;
    private final int NEW_CONTACT = 1;
    dbManager db;
    private String selectedContact;
    private final int MIN_ACCEL = 12;

    private SensorManager Smgr;
    private Sensor accel;


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

        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //display the current contents of the database file
    public void refresh()
    {
        dataGenerator dg = new dataGenerator(true, false, false, false, null);
        dg.execute("");
    }



    /*
     * When the add contact item is selected from the options list, the second activity will be
     * brought on to the screen in the edit mode so that the user can enter the information for
     * a new contact. Upon returning to the main activity, refresh the list
     */
    public void onAddContact(MenuItem item)
    {
        Intent addIntent = new Intent(this, contactDetails.class);
        addIntent.putExtra("mode", NEW_CONTACT);
        startActivityForResult(addIntent, NEW_CONTACT);
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
        dataGenerator dg = new dataGenerator(true, false, false, false, null);
        selectedContact = tv.getText().toString();
        dg.execute(selectedContact);
    }


    /*
     * Empty the database and refresh the list
     */
    public void onDeleteContacts(MenuItem item)
    {
        dataGenerator dg = new dataGenerator(false, true, false, false,null);
        dg.execute("");
        refresh();
    }


    /*
     * When the accelerometer sensor detects a change in the x, y, or z direction accelerations that
     * is greater than a reasonable minimum, start an asyncTask to reverse the contents of the list.
     */
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.values[0] >= MIN_ACCEL || event.values[1] >= MIN_ACCEL || event.values[2] >= MIN_ACCEL)
        {
            dataGenerator dg = new dataGenerator(false, false, false, true, null);
            dg.execute("");
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
     * Check the resultCode returned from the called activity. If a RESULT_OK code is recieved, then
     * proceed to determine which requestCode was sent, and update the database file in corresponding
     * manner.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        dataGenerator dg = null;

        if(resultCode == RESULT_OK)
        {
            if(requestCode == NEW_CONTACT)
            {
                dg = new dataGenerator(false, false, false, false, null);
                dg.execute(data.getStringExtra("newContact"));
            }
            else
            {
                dg = new dataGenerator(false, false , false, false, selectedContact);
                dg.execute(data.getStringExtra("newContact"));
            }
        }
        else
        {
            dg = new dataGenerator(false, false, true, false, null);
            dg.execute(selectedContact);
        }

        refresh();
    }

    /*
     * Class listGenerator
     * The listGenerator asyncTask is used for interacting with the database. This includes updates,
     * specific queries, full queries, deletes, and clearing the database.
     */
    public class dataGenerator extends AsyncTask<String, Void, ArrayList<String>>
    {

        private boolean isQuery, isSingleQuery, isClear, isDel, err, rev;
        String origName;


        public dataGenerator(boolean q, boolean c, boolean d, boolean r, String oN)
        {
            isQuery = q;
            isClear = c;
            isDel = d;
            origName = oN;
            isSingleQuery = false;
            err = false;
            rev = r;
        }


        /*
         * Determine which command has been issued, and perform that task
         * (clear/delete/reverse/query/query specific/update new contact/update existing)
         */
        @Override
        protected ArrayList<String> doInBackground(String... inputs)
        {
            db = new dbManager(contactList.this, "db_file.txt");
            ArrayList<String> data = null;

            try
            {
                if(isClear)
                {
                    db.eraseDB();
                    return null;
                }
                else if(rev)
                {
                    db.reverse();
                    data = db.query();
                    return data;
                }
                else if(isDel)
                {
                    err = !db.deleteContact(selectedContact);
                    return null;
                }
                else if(isQuery)
                {
                    if(!inputs[0].isEmpty())
                    {
                        isSingleQuery = true;
                        String[] namePair = null;
                        data = new ArrayList<>();

                        namePair = inputs[0].split(", ");
                        data.add(db.query(namePair[1], namePair[0]));
                    }
                    else
                    {
                        data = db.query();
                    }

                    return data;
                }
                else
                {
                    if(origName != null)
                    {
                        String orig_namePair[] = origName.split(", ");

                        err = !db.updateDB(inputs[0], orig_namePair[1], orig_namePair[0]);
                    }
                    else
                    {
                        err = !db.updateDB(inputs[0], null, null);
                    }

                    return null;
                }
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
                err = true;
                return null;
            }
        }

        /*
         *  If the error flag is set and either isQuery or isClear is set, then a display the database
         *  error message. Otherwise just leave the screen blank. If the error flag is not set and isQuery
         *  is set, then determine if it was a specific query. If so, start the contactDetials activity
         *  and send the retrieved contact in an intent. Otherwise, the command was for a full query,
         *  so create a new instance of contactsAdapter and attach it to the listView.
         */
        @Override
        protected void onPostExecute(ArrayList<String> ds)
        {
            ListView lv = findViewById(R.id.contactList);

            if(err)
            {
                lv.setVisibility(lv.INVISIBLE);
                if(isQuery || isClear || isDel)
                {
                    TextView errMsg = findViewById(R.id.errorMsg);
                    errMsg.setVisibility(errMsg.VISIBLE);
                }
            }
            else if((isQuery || rev) && ds != null)
            {
                if (!isSingleQuery)
                {
                    contactsAdapter cA = new contactsAdapter(contactList.this, R.layout.contacts_layout, ds);
                    lv.setAdapter(cA);
                }
                else
                {
                    Intent displayIntent = new Intent(contactList.this, contactDetails.class);
                    displayIntent.putExtra("mode", EXISTING_CONTACT);
                    displayIntent.putExtra("contactData", ds.get(0));

                    startActivityForResult(displayIntent, EXISTING_CONTACT);
                }
            }
        }
    }
}
