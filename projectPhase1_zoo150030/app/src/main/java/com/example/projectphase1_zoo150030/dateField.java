package com.example.projectphase1_zoo150030;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/*
 * Author: Zack Oldham
 * dateField.java
 * This class defines the dateField fragment, which is responsible for holding either the input hint,
 * or the date that was selected using the corresponding dateSelector.
 */

public class dateField extends Fragment {

    private static final String ARG_PARAM1 = "date";
    private static final String ARG_PARAM2 = "dateType";
    private static final String ARG_PARAM3 = "mode";

    private String date;
    private boolean dateType;
    private boolean mode;
    private TextView field;
    private boolean empty;


    public dateField()
    {
        // Required empty public constructor
    }

    /*
     * Define a new dateField with the given inputs for the date, which type of date, and whether it
     * should be clickable.
     */
    public static dateField newInstance(String date, boolean inputType, boolean mode)
    {
        dateField fragment = new dateField();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, date);
        args.putBoolean(ARG_PARAM2, inputType);
        args.putBoolean(ARG_PARAM3, mode);

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
            mode = getArguments().getBoolean(ARG_PARAM3);
        }
    }


    /*
     * Set up the dateField. If the input date was null, display the date hint. The mode will determine
     * whether the field should be clickable. (It will always be clickable when 'empty')
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_field, container, false);

        field = view.findViewById(R.id.date);
        System.out.println("\n\n!!!DATE FIELD ID: " + R.id.date + "!!!\n\n");
        field.setOnClickListener(new dateClickListener());

        if(date == null)
        {
            empty = true;
            date = "01/01/1970";
            field.setClickable(true);
        }
        else
        {
            empty = false;
            field.setClickable(mode);

        }

        field.setText(date);
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
     * Return boolean flag stating whether the user has entered a date into this dateField.
     */
    public boolean isEmpty()
    {
        return empty;
    }

    /*
     * Return the text currently stored in this dateField.
     */
    public String getText()
    {
        return date;
    }

    /*
     * Set the clickability of the field TextView contained in this fragment to the flag passed in.
     */
    public void setClickable(boolean set)
    {
        field.setClickable(set);
    }


    /*
     * This class defines the clickListener for the TextView stored in this fragment.
     */
    public class dateClickListener implements Button.OnClickListener
    {

        /*
         * When the TextView field is clicked, ask contactDetails to show a datePicker in place of
         * this fragment.
         */
        @Override
        public void onClick(View v)
        {
            ((contactDetails)getActivity()).showDatePicker(dateType);
        }
    }
}
