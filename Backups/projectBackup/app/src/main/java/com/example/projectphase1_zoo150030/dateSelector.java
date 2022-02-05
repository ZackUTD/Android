package com.example.projectphase1_zoo150030;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;


/*
 * Author: Zack Oldham
 * dateSelector.java
 * This class defines the dateSelector Fragment which shows a spinning date selection utility with
 * which the user can select a date.
 */
public class dateSelector extends Fragment
{


    private static final String ARG_PARAM1 = "date";
    private static final String ARG_PARAM2 = "dateType";

    private String date;
    private boolean dateType;
    private DatePicker dp;
    private String mm;
    private String dd;
    private String yy;



    public dateSelector()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inputDate Parameter 1.
     * @return A new instance of fragment dateSelector.
     */

    public static dateSelector newInstance(String inputDate, boolean inputType)
    {
        dateSelector fragment = new dateSelector();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, inputDate);
        args.putBoolean(ARG_PARAM2, inputType);
        fragment.setArguments(args);
        return fragment;
    }


    /*
     * If there are arguments to be gotten, assign them to their respective instance variables.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            date = getArguments().getString(ARG_PARAM1);
            dateType = getArguments().getBoolean(ARG_PARAM2);
        }
    }



    /*
     * Inflate the layout, and set the initial date for the date spinner. (Jan, 1, 2019 if no given date).
     * Also, define the click listeners for the 'OK' and 'Cancel' buttons at this time.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_selector, container, false);

        dp = view.findViewById(R.id.dateSelect);

        if(date != null)
        {
            String[] dateToks = date.split("/");

            int[] dateInts = new int[3];

            for(int i = 0; i < 3; i++)
            {
                dateInts[i] = Integer.valueOf(dateToks[i]);
            }

            dp.init(dateInts[2], dateInts[1], dateInts[0], null);
        }
        else
        {
            dp.init(2019, 0, 1, null);
        }

        Button okBtn = view.findViewById(R.id.okButton);
        okBtn.setOnClickListener(new okClickListener());

        Button cancelBtn = view.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new cancelClickListener());

        return view;
    }



    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }



    /*
     * This class defines the clickListener for the 'ok' button.
     */
    public class okClickListener implements Button.OnClickListener
    {
        /*
         * When the 'ok' button is clicked, call sendDate from contactDetails and provide the date
         * that the user has selected on the spinner, as well as the type of date that was being entered
         * (so that we replace the correct dateField fragment)
         */
        @Override
        public void onClick(View v)
        {
            mm = String.valueOf(dp.getMonth()+1);
            dd = String.valueOf(dp.getDayOfMonth());
            yy = String.valueOf(dp.getYear());

            String outputDate = mm + "/" + dd + "/" + yy;

            ((contactDetails)getActivity()).sendDate(outputDate, dateType);
        }
    }


    /*
     * This class defines the clickListener for the 'cancel' button.
     */
    public class cancelClickListener implements Button.OnClickListener
    {
        /*
         * When the 'cancel' button is clicked, send a null date to the contactDetails activity,
         * as well as the type of date that was being entered (so that we replace the correct fragment).
         */
        @Override
        public void onClick(View v)
        {
            ((contactDetails)getActivity()).sendDate(null, dateType);
        }
    }
}
