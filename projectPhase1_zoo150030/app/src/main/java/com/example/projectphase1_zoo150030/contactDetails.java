package com.example.projectphase1_zoo150030;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    private TextView errorView;
    private EditText First;
    private EditText Last;
    private EditText Number;
    private EditText AddrLine1;
    private EditText AddrLine2;
    private EditText City;
    private EditText State;
    private EditText Zip;
    private TextView firstTitle;
    private TextView lastTitle;
    private TextView numberTitle;
    private TextView dobTitle;
    private TextView dofcTitle;
    private TextView newTitle;
    private TextView detailsTitle;
    private TextView AddrTitle;
    private TextView cityTitle;
    private TextView stateTitle;
    private TextView zipTitle;
    private boolean isNew;
    private static final boolean MODE_EDIT = true;
    private static final boolean MODE_VIEW = false;
    private Contact currentContact;
    private contactAddress currentAddress;
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

            First = findViewById(R.id.First);
            Last = findViewById(R.id.Last);
            Number = findViewById(R.id.Number);
            AddrLine1 = findViewById(R.id.addrLine1);
            AddrLine2 = findViewById(R.id.addrLine2);
            City = findViewById(R.id.City);
            State = findViewById(R.id.State);
            Zip = findViewById(R.id.Zip);
            newTitle = findViewById(R.id.newContactTitle);
            detailsTitle = findViewById(R.id.detailsTitle);
            firstTitle = findViewById(R.id.FTitle);
            lastTitle = findViewById(R.id.LTitle);
            numberTitle = findViewById(R.id.NumTitle);
            dobTitle = findViewById(R.id.DoBTitle);
            dofcTitle = findViewById(R.id.DofCTitle);
            AddrTitle = findViewById(R.id.AddrTitle);
            cityTitle = findViewById(R.id.CityTitle);
            stateTitle = findViewById(R.id.StateTitle);
            zipTitle = findViewById(R.id.ZipTitle);
            delButton = findViewById(R.id.deleteButton);

            setupScreen(contactIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_details, menu);
        return true;
    }


    /*
     * If the mode is set to true for new Contact, then display empty fields for the user to fill.
     * Otherwise, fill the fields with the current information for the selected contact.
     * In the latter case, the fields should not be editable until the edit button is pressed.
     * The exception to these cases is when a pre-existing user is selected who does not have an address.
     * In this case, all fields will be set to be editable as if edit had been clicked. The user will be immediately
     * notified that they must enter an address, and will be required to enter one before progressing.
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
            AddrLine1.setEnabled(true);
            AddrLine2.setEnabled(true);
            City.setEnabled(true);
            State.setEnabled(true);
            Zip.setEnabled(true);
            newTitle.setVisibility(newTitle.VISIBLE);
            detailsTitle.setVisibility(detailsTitle.INVISIBLE);


            dobField = dateField.newInstance(null, true, MODE_EDIT);
            FragmentTransaction fragTrans = fragMgr.beginTransaction();
            fragTrans.add(R.id.DoBFrame, dobField).addToBackStack(null).commit();

            dofcField = dateField.newInstance(null, false, MODE_EDIT);
            fragTrans = fragMgr.beginTransaction();
            fragTrans.add(R.id.DofCFrame, dofcField).addToBackStack(null).commit();
        }
        else
        {
            //simply display the details of the selected contact, along with option to edit
            String cData = intent.getStringExtra("contact");

            if(cData != null)
            {
                currentContact = new Contact(cData);
                currentAddress = currentContact.getAddr();

                First.setText(currentContact.getFirst());
                Last.setText(currentContact.getLast());
                Number.setText(currentContact.getNum());

                boolean currentMode = currentAddress.getAddrL1().compareTo("null") == 0;
                if(!currentMode)
                {
                    AddrLine1.setText(currentAddress.getAddrL1());
                    AddrLine2.setText(currentAddress.getAddrL2());
                    City.setText(currentAddress.getCity());
                    State.setText(currentAddress.getState());
                    Zip.setText(currentAddress.getZip());
                    setMode(MODE_VIEW);
                }
                else
                {
                    onNoAddress();
                }

                dobField = dateField.newInstance(currentContact.getDoB(), true, currentMode);
                FragmentTransaction fragTrans1 = fragMgr.beginTransaction();
                fragTrans1.add(R.id.DoBFrame, dobField).commitNow();

                dofcField = dateField.newInstance(currentContact.getDofC(), false, currentMode);
                FragmentTransaction fragTrans2 = fragMgr.beginTransaction();
                fragTrans2.add(R.id.DofCFrame, dofcField).commitNow();


                delButton.setClickable(true);

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
                AddrTitle.setVisibility(AddrTitle.INVISIBLE);
                cityTitle.setVisibility(cityTitle.INVISIBLE);
                stateTitle.setVisibility(stateTitle.INVISIBLE);
                zipTitle.setVisibility(zipTitle.INVISIBLE);
                First.setVisibility(First.INVISIBLE);
                Last.setVisibility(Last.INVISIBLE);
                Number.setVisibility(Number.INVISIBLE);
                AddrLine1.setVisibility(AddrLine1.INVISIBLE);
                AddrLine2.setVisibility(AddrLine1.INVISIBLE);
                City.setVisibility(City.INVISIBLE);
                State.setVisibility(State.INVISIBLE);
                Zip.setVisibility(Zip.INVISIBLE);
                saveButton.setVisibility(saveButton.INVISIBLE);
                editButton.setVisibility(editButton.INVISIBLE);

                errorView.setText(R.string.inv_con);
                errorView.setVisibility(errorView.VISIBLE);
            }
        }
    }


    /*
     * If edit mode, all fields should be editable
     * Otherwise, all fields should not be editable.
     * The edit button should only be clickable when viewing,
     * and the save button should only be clickable when editing (and vis-a-versa).
     */
    public void setMode(boolean mode)
    {
        First.setEnabled(mode);
        Last.setEnabled(mode);
        Number.setEnabled(mode);
        AddrLine1.setEnabled(mode);
        AddrLine2.setEnabled(mode);
        City.setEnabled(mode);
        State.setEnabled(mode);
        Zip.setEnabled(mode);

        editButton.setClickable(!mode);
        saveButton.setClickable(mode);
    }


    /*
     * If a pre-existing contact is selected who does not have an address,
     * show a message to notify the user that they must enter an address, and then put the
     * fields into edit mode.
     */
    public void onNoAddress()
    {
        Snackbar addrMsg = Snackbar.make(errorView, R.string.AddrErr, Snackbar.LENGTH_LONG);
        addrMsg.show();

        setMode(MODE_EDIT);
    }


    /*
     * Return true if any non-address field is empty.
     */
    public boolean isContactEmpty()
    {
        boolean[] fields = new boolean[5];

        fields[0] = First.getText().toString().isEmpty();
        fields[1] = Last.getText().toString().isEmpty();
        fields[2] = Number.getText().toString().isEmpty();
        fields[3] = dobField.isEmpty();
        fields[4] = dofcField.isEmpty();

        boolean res = false;

        for(boolean b : fields)
        {
            res = res || b;
        }

        return res || isAddrEmpty();
    }

    /*
     * Return true if any address field is empty.
     */
    public boolean isAddrEmpty()
    {
        boolean[] addrFields = new boolean[4];

        addrFields[0] = AddrLine1.getText().toString().isEmpty();
        addrFields[1] = City.getText().toString().isEmpty();
        addrFields[2] = State.getText().toString().isEmpty();
        addrFields[3] = Zip.getText().toString().isEmpty();

        boolean res = false;

        for(boolean b : addrFields)
        {
            res = res || b;
        }

        return res;
    }



    /*
     * When the edit button is clicked, allow the user to type in the text fields to edit the contact
     * information. Disable the editButton and enable the save button.
     */
    public void onEditClick(View view)
    {
        setMode(MODE_EDIT);
        dobField.setClickable(MODE_EDIT);
        dofcField.setClickable(MODE_EDIT);
    }


    /*
     * When the user clicks the save button, if there are any empty fields, display the error snackbar.
     * Otherwise, return the updated information to the main activity so that the database file can be updated.
     */
    public void onSaveClick(View view)
    {
        if(isContactEmpty())
        {
            Snackbar errorMsg = Snackbar.make(errorView, R.string.fieldErr, Snackbar.LENGTH_LONG);
            errorMsg.show();
        }
        else
        {
            currentContact.setFirst(First.getText().toString());
            currentContact.setLast(Last.getText().toString());
            currentContact.setNum(Number.getText().toString());
            currentContact.setDoB(dobField.getText());
            currentContact.setDofC(dofcField.getText());

            currentAddress = new contactAddress();
            currentAddress.setAddrL1(AddrLine1.getText().toString());
            currentAddress.setAddrL2(AddrLine2.getText().toString());
            currentAddress.setCity(City.getText().toString());
            currentAddress.setState(State.getText().toString());
            currentAddress.setZip(Zip.getText().toString());

            currentContact.setAddr(currentAddress);

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


    /*
     * Immediately return to the main activity when the back button is pressed.
     */
    @Override
    public void onBackPressed()
    {
        finish();
    }

    /*
     * If the address has been filled out, send it to the addressMapper activity to be displayed.
     * If the address has any empty fields, display an error message and open the fields for editing.
     * NOTE: The address to be mapped is the address currently in the fields -- NOT NECESSARILY
     * WHAT IS STORED IN THE DATABASE
     */
    public void onMapAddr(MenuItem item)
    {
        if(!isAddrEmpty())
        {
            currentAddress = new contactAddress();
            currentAddress.setAddrL1(AddrLine1.getText().toString());
            currentAddress.setAddrL2(AddrLine2.getText().toString());
            currentAddress.setCity(City.getText().toString());
            currentAddress.setState(State.getText().toString());
            currentAddress.setZip(Zip.getText().toString());
            Intent intent = new Intent(this, addressMapper.class);
            intent.putExtra("address", currentAddress.toString());
            startActivity(intent);
        }
        else
        {
            onNoAddress();
        }
    }



    /*
     * Create an instance of a dateField with the given date, and replace the dateSelector that
     * called this method with the new dateField.
     */
    public void sendDate(String date, boolean dateType)
    {
        FragmentManager fragMgr = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMgr.beginTransaction();

        if(dateType)
        {
            dobField = dateField.newInstance(date, dateType, MODE_EDIT);
            fragTrans.replace(R.id.DoBFrame, dobField);
        }
        else
        {
            dofcField = dateField.newInstance(date, dateType, MODE_EDIT);
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
