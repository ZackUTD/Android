package com.example.projectphase1_zoo150030;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
 * Author: Zack Oldham
 * Date: 02-23-2019
 * contactDetails.java
 * This activity is responsible for editing a contact. If addContact is selected in the main activity,
 * then this activity will display empty fields for input. If an existing contact was selected from
 * the lit in main activity, then this activity will display the information of that contact in the
 * fields for the user to edit.
 */



public class contactDetails extends AppCompatActivity
{
    private Button editButton;
    private Button saveButton;
    private Button delButton;
    private Snackbar errorMsg;
    private TextView errorView;
    private EditText First;
    private EditText Last;
    private EditText Number;
    private TextView firstTitle;
    private TextView lastTitle;
    private TextView numberTitle;
    private TextView dobTitle;
    private TextView dofcTitle;
    private TextView newTitle;
    private TextView detailsTitle;
    private boolean isNew;
    private String cData;
    private Contact currentContact;
    private dateField dobField;
    private dateField dofcField;
    private dateSelector selectDate;

    private static final int RESULT_NEW = 1;
    private static final int RESULT_UPDATE = 2;
    private static final int RESULT_DELETE = 3;


    /*
     * When the activity is created, do all necessary findViewById calls, and setup the screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent contactIntent = getIntent();

        if(contactIntent != null)
        {
            editButton = findViewById(R.id.editButton);
            saveButton = findViewById(R.id.saveButton);
            errorView = findViewById(R.id.errorText);
            errorMsg = Snackbar.make(errorView, R.string.fieldErr, Snackbar.LENGTH_LONG);
            First = findViewById(R.id.First);
            Last = findViewById(R.id.Last);
            Number = findViewById(R.id.Number);
            newTitle = findViewById(R.id.newContactTitle);
            detailsTitle = findViewById(R.id.detailsTitle);
            firstTitle = findViewById(R.id.FTitle);
            lastTitle = findViewById(R.id.LTitle);
            numberTitle = findViewById(R.id.NumTitle);
            dobTitle = findViewById(R.id.DoBTitle);
            dofcTitle = findViewById(R.id.DofCTitle);
            delButton = findViewById(R.id.deleteButton);

            setupScreen(contactIntent);
        }
    }


    /*
     * If the mode is set to 1 for new Contact, then display empty fields for the user to fill.
     * Otherwise, fill the fields with the current information for the selected contact.
     * In the latter case, the fields should not be editable until the edit button is pressed.
     */
    public void setupScreen(Intent intent)
    {
        isNew = intent.getBooleanExtra("mode", true);

        FragmentManager fragMgr = getSupportFragmentManager();

        if(isNew)
        {
            currentContact = new Contact();
            saveButton.setClickable(true);
            editButton.setClickable(false);
            First.setEnabled(true);
            Last.setEnabled(true);
            Number.setEnabled(true);
            newTitle.setVisibility(newTitle.VISIBLE);
            detailsTitle.setVisibility(detailsTitle.INVISIBLE);


            dobField = dateField.newInstance(getString(R.string.DoBHint), true);
            FragmentTransaction fragTrans = fragMgr.beginTransaction();
            fragTrans.add(R.id.DoBFrame, dobField).addToBackStack(null).commit();

            dofcField = dateField.newInstance(getString(R.string.DofCHint), false);
            fragTrans = fragMgr.beginTransaction();
            fragTrans.add(R.id.DofCFrame, dofcField).addToBackStack(null).commit();

        }
        else
        {
            //simply display the details of the selected contact, along with option to edit
            cData = intent.getStringExtra("contact");

            if(cData != null)
            {
                currentContact = new Contact(cData);
                First.setText(currentContact.getFirst());
                Last.setText(currentContact.getLast());
                Number.setText(currentContact.getNum());

                dobField = dateField.newInstance(currentContact.getDoB(), true);
                FragmentTransaction fragTrans1 = fragMgr.beginTransaction();
                fragTrans1.add(R.id.DoBFrame, dobField).commitNow();

                dofcField = dateField.newInstance(currentContact.getDofC(), false);
                FragmentTransaction fragTrans2 = fragMgr.beginTransaction();
                fragTrans2.add(R.id.DofCFrame, dofcField).commitNow();

                saveButton.setClickable(false);
                editButton.setClickable(true);
                delButton.setClickable(true);

                First.setEnabled(false);
                Last.setEnabled(false);
                Number.setEnabled(false);

                newTitle.setVisibility(newTitle.INVISIBLE);
                detailsTitle.setVisibility(detailsTitle.VISIBLE);
            }
            else
            {
                firstTitle.setVisibility(firstTitle.INVISIBLE);
                lastTitle.setVisibility(lastTitle.INVISIBLE);
                numberTitle.setVisibility(numberTitle.INVISIBLE);
                dobTitle.setVisibility(dobTitle.INVISIBLE);
                dofcTitle.setVisibility(dofcTitle.INVISIBLE);
                First.setVisibility(First.INVISIBLE);
                Last.setVisibility(Last.INVISIBLE);
                Number.setVisibility(Number.INVISIBLE);
                saveButton.setVisibility(saveButton.INVISIBLE);
                editButton.setVisibility(editButton.INVISIBLE);

                errorView.setText(R.string.inv_con);
            }
        }
    }



    /*
     * When the edit button is clicked, allow the user to type in the text fields to edit the contact
     * information. Disable the editButton and enable the save button.
     */
    public void onEditClick(View view)
    {
        First.setEnabled(true);
        Last.setEnabled(true);
        Number.setEnabled(true);
        dobField.setClickable(true);
        dofcField.setClickable(true);

        editButton.setClickable(false);
        saveButton.setClickable(true);
    }


    /*
     * When the user clicks the save button, if there are any empty fields, display the error snackbar.
     * Otherwise, return the updated information to the mainactivity so that the database file can be updated.
     */
    public void onSaveClick(View view)
    {
        boolean[] fields = new boolean[5];

        fields[0] = First.getText().toString().isEmpty();
        fields[1] = Last.getText().toString().isEmpty();
        fields[2] = Number.getText().toString().isEmpty();
        fields[3] = dobField.isEmpty();
        fields[4] = dofcField.isEmpty();


        if(fields[0]|| fields[1] || fields[2] || fields[3] || fields[4])
        {
            errorMsg.show();
        }
        else
        {
            currentContact.setFirst(First.getText().toString());
            currentContact.setLast(Last.getText().toString());
            currentContact.setNum(Number.getText().toString());
            currentContact.setDoB(dobField.getText());
            currentContact.setDofC(dofcField.getText());

            Intent saveIntent = new Intent(this, contactList.class);
            saveIntent.putExtra("freshContact", currentContact.toString());

            if(isNew)
            {
                setResult(RESULT_NEW, saveIntent);
            }
            else
            {
                setResult(RESULT_UPDATE, saveIntent);
            }

            finish();
        }
    }


    /*
     * When the delete button is clicked, return to the mainActivity with a code to delete the selected contact.
     */
    public void onDeleteClick(View view)
    {
        Intent delIntent = new Intent(this, contactList.class);
        delIntent.putExtra("freshContact", currentContact.toString());
        setResult(RESULT_DELETE, delIntent);
        finish();
    }

    public void onMapAddr(View view)
    {

    }




    /*
     * Create an instance of a dateField with the given date, and replace the dateSelector that
     * called this method with the new dateField.
     */
    public void sendDate(String date, boolean dateType)
    {
        String input = null;

        if(date == null)
        {
            if(dateType)
            {
                input = getString(R.string.DoBHint);
            }
            else
            {
                input = getString(R.string.DofCHint);
            }
        }
        else
        {
            input = date;
        }

        FragmentManager fragMgr = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMgr.beginTransaction();

        if(dateType)
        {

            dobField = dateField.newInstance(input, dateType);
            fragTrans.replace(R.id.DoBFrame, dobField);
        }
        else
        {
            dofcField = dateField.newInstance(input, dateType);
            fragTrans.replace(R.id.DofCFrame, dofcField);
        }

        fragTrans.commitNow();
    }


    /*
     * Create a new instance of a datePicker and replace the calling dateField with this new datePicker.
     */
    public void showDatePicker(boolean dateType)
    {
        FragmentManager fragMgr = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMgr.beginTransaction();

        if(dateType)
        {
            selectDate = dateSelector.newInstance(currentContact.getDoB(), dateType);
            fragTrans.replace(R.id.DoBFrame, selectDate);
        }
        else
        {
            selectDate = dateSelector.newInstance(currentContact.getDofC(), dateType);
            fragTrans.replace(R.id.DofCFrame, selectDate);
        }

        fragTrans.commitNow();
    }
}
