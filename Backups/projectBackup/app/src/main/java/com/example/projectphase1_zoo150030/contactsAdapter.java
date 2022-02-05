package com.example.projectphase1_zoo150030;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/*
 * Author: Zack Oldham
 * Date: 02-23-2019
 * contactsAdapter.java
 * The contactsAdapter will format each row in the List it is paired attached to such that
 * the last name appears first, followed by the first name. The columns for first and last name
 * will each be half the screen in width.
 */

public class contactsAdapter extends ArrayAdapter<Contact>
{
    List<Contact> data;

    public contactsAdapter(Context context, int textViewResourceId, List<Contact> input)
    {
        super(context, textViewResourceId, input);
        data = input;
    }

    /*
     For each row in the data list display the contact last name first, comma separated.
     */
    @Override
    public View getView(int position, View cvtView, ViewGroup parent)
    {
        Context cx = this.getContext();
        LayoutInflater inflater = (LayoutInflater) cx.getSystemService(cx.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contacts_layout, parent, false);
        Contact c = data.get(position);
        String displayName = c.getLast() + ", " + c.getFirst();

        TextView tv = rowView.findViewById(R.id.contact);
        tv.setText(displayName);

        return rowView;
    }

}
