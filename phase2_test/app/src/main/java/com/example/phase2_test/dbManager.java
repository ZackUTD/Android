package com.example.phase2_test;

/*
 * Author: Zack Oldham
 * dbManager.java
 * This class defines the database manager which is responsible for retrieving information from,
 * and updating, the "database" text file.
 */



import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class dbManager
{
    private ArrayList<String> data;
    private File inputFile;

    public dbManager(Context context, String fileName)
    {
        inputFile = new File(context.getFilesDir(), fileName);
        data = new ArrayList<>();
        //writeDB();
    }

    /*
     * Read the database and return the data ArrayList.
     */
    public ArrayList<String> query() throws IOException
    {
        if(readDB())
        {
            return data;
        }

        return null;
    }

    /*
     * Read the database and return the position in it at which the given name pair occurs.
     */
    public String query(String fname, String lname) throws IOException
    {
        if(readDB())
        {
            int position = findContact(fname, lname);
            if(position == -1)
            {
                return null;
            }

            return data.get(position);
        }

        return null;
    }


    /*
     * Read the database, and attempt to find the provided name pair. If found, update the
     * information for that contact, otherwise add a new contact with that information.
     */
    public boolean updateDB(String updates, String first, String last) throws IOException
    {
        if(readDB())
        {
            if(first != null && last != null)
            {
                int position = findContact(first, last);

                if (position != -1)
                {
                    data.remove(position);
                }
                else
                {
                    return false;
                }
            }

            addContact(updates);
            writeDB();
            return true;
        }
        else
        {
            return false;
        }
    }

    /*
     * Delete a contact from the database file
     */
    public boolean deleteContact(String name)
    {
        if(name == null)
        {
            return false;
        }
        else
        {
            readDB();
            String namePair[] = name.split(", ");
            int position = findContact(namePair[1], namePair[0]);

            if(position != -1)
            {
                data.remove(position);
                writeDB();
                return true;
            }

            return false;
        }
    }


    /*
     * Erase all contacts from the database file
     */
    public void eraseDB()
    {
        data.clear();
        writeDB();
    }


    /*
     * Add a contact to the dataset in alphabetical order with respect to existing elements, if any.
     */
    private void addContact(String contact)
    {
        String fields[] = contact.split("\t");
        String first = fields[0].toLowerCase();
        String last = fields[1].toLowerCase();
        String curStr[];
        String curF;
        String curL;


        if(data.size() == 0)
        {
            data.add(contact);
            return;
        }

        for(int i = 0; i < data.size(); i++)
        {
            curStr = data.get(i).split("\t");
            curF = curStr[0].toLowerCase();
            curL = curStr[1].toLowerCase();

            if(last.compareTo(curL) < 0 || (last.compareTo(curL) == 0 && first.compareTo(curF) < 0))
            {
                data.add(i, contact);
                return;
            }
        }

        data.add(data.size(), contact);
    }


    /*
     * Reverse the order of the entries in the contact list (from alphabetical to reverse-
     * alphabetical or vis-a-versa)
     */
    public void reverse()
    {
        readDB();
        ArrayList<String> temp = new ArrayList<>();
        temp.addAll(data);
        data.clear();

        for(int i = temp.size() - 1; i >= 0; i--)
        {
            data.add(temp.get(i));
        }

        writeDB();
    }


    /*
     * Given a first/last name pair, determine if that name pair appears in the file.
     * If so, return its position in the ArrayList, otherwise return -1.
     */
    private int findContact(String first, String last)
    {
        if(data.isEmpty())
        {
            return -1;
        }

        String toks[] = null;

        for(int i = 0; i < data.size(); i++)
        {
            toks = data.get(i).split("\t");

            if(first.compareTo(toks[0]) == 0 && last.compareTo(toks[1]) == 0)
            {
                return i;
            }
        }

        return -1;
    }


    /*
     * Read all of information from the database into the data ArrayList.
     */
    private boolean readDB()
    {
        Scanner in = null;
        data = new ArrayList<>();

        try
        {
            in = new Scanner(inputFile);

            while(in.hasNext())
            {
                data.add(in.nextLine());
            }

            in.close();

        }
        catch(Exception ex)
        {
            return false;
        }

        return true;
    }


    /*
     * Write all of the data in the data ArrayList to the database.
     */
    private boolean writeDB()
    {
        PrintWriter out = null;

        try
        {
            out = new PrintWriter(inputFile);

            for(int i = 0; i < data.size(); i++)
            {
                out.println(data.get(i));
            }

            out.close();
        }
        catch(Exception ex)
        {
            return false;
        }

        return true;
    }
}
