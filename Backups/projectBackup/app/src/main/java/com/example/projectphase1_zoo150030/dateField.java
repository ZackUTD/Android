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

    private String date;
    private boolean dateType;
    private TextView field;
    private boolean empty;


    public dateField()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param date Parameter 1.
     * @return A new instance of fragment dateField.
     */
    // TODO: Rename and change types and number of parameters
    public static dateField newInstance(String date, boolean inputType)
    {
        dateField fragment = new dateField();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, date);
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
     * Inflate the layout and set the date (input hint if given date is null). If the given date was
     * not null, then set the field view to be not clickable (this will be changed when the user
     * clicks the 'edit button' (see contactDetails.java)).
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_field, container, false);

        field = view.findViewById(R.id.date);
        if(date != null)
        {
            field.setText(date);
            field.setOnClickListener(new dateClickListener());

            if(date.compareTo(getString(R.string.DoBHint)) == 0 || date.compareTo(getString(R.string.DofCHint)) == 0)
            {
                empty = true;
            }
            else
            {
                empty = false;
                field.setClickable(false);
            }
        }

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
