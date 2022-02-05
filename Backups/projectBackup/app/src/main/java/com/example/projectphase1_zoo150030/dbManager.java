package com.example.projectphase1_zoo150030;

/*
 * Author: Zack Oldham
 * dbManager.java
 * This class defines the database manager which is responsible for retrieving information from,
 * and updating, the "database" text file.
 */



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class dbManager extends SQLiteOpenHelper
{
    private Context context;
    private SQLiteDatabase db;
    private Cursor results;
    private String[] select;
    private String where;
    private String[] whereArgs;

    private String order;

    private static final String DATABASE_NAME = "ContactsDB";
    private static final int VERSION = 1;

    private static final String TABLE_NAME = "Contacts";
    private static final String COL1 = "Fname";
    private static final String COL2 = "Lname";
    private static final String COL3 = "pNumber";
    private static final String COL4 = "DOB";
    private static final String COL5 = "DoFC";

    private static final String INIT_STRING = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (Fname TEXT NOT NULL, Lname TEXT NOT NULL, pNumber TEXT NOT NULL, DOB TEXT NOT NULL, DoFC TEXT NOT NULL)";

    private static final String DROP_STRING = "DROP TABLE IF EXISTS " + TABLE_NAME;



    public dbManager(Context con)
    {
        super(con, DATABASE_NAME, null, VERSION);
        context = con;
        db = getWritableDatabase();
        onCreate(db);

        order = "ASC";
    }

    /*
     * Initialize the database.
     */
    @Override
    public void onCreate(SQLiteDatabase new_db)
    {
        new_db.execSQL(INIT_STRING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase current_db, int oldVersion, int newVersion) { }

    /*
     * Delete the contents of the database and reinitialize them.
     */
    public void reInit()
    {
        db.execSQL(DROP_STRING);
        onCreate(db);
    }


    /*
     * Read all contacts from the database into an ArrayList in the order specified by the order variable, and return it.
     * Return null if the database is empty.
     */
    public ArrayList<Contact> query() throws IllegalArgumentException
    {
        ArrayList<Contact> data = new ArrayList<>();

        select = new String[2];
        select[0] = COL1;
        select[1] = COL2;
        where = null;
        whereArgs = null;
        String orderBy = COL2 + " COLLATE NOCASE " + order + ", " + COL1 + " COLLATE NOCASE " + order;

        results = db.query(TABLE_NAME, select, where, whereArgs, null, null, orderBy);

        Contact c = new Contact();

        while(results.moveToNext())
        {
            c.setFirst(results.getString(results.getColumnIndexOrThrow(COL1)));
            c.setLast(results.getString(results.getColumnIndexOrThrow(COL2)));

            data.add(c);
            c = new Contact();
        }

        if(data.isEmpty())
        {
            return null;
        }
        else
        {
            return data;
        }
    }

    /*
     * Set the order to the opposite of its current value.
     */
    public void reverseOrder()
    {
        if(order.compareTo("ASC") == 0)
        {
            order = "DESC";
        }
        else
        {
            order = "ASC";
        }
    }


    /*
     * Get the information of the given person, and return it in the form of a Contact object.
     * Return null if that name does not appear in the database.
     */
    public Contact query(String fname, String lname) throws IllegalArgumentException
    {
        select = null;
        where = COL1 + "=? AND " + COL2 + "=?";
        whereArgs = new String[2];
        whereArgs[0] = fname;
        whereArgs[1]= lname;

        results = db.query(TABLE_NAME, select, where, whereArgs, null, null, null);

        Contact c = null;

        if(results.moveToNext())
        {
            c = new Contact();
            c.setFirst(fname);
            c.setLast(lname);
            c.setNum(results.getString(results.getColumnIndexOrThrow(COL3)));
            c.setDoB(results.getString(results.getColumnIndexOrThrow(COL4)));
            c.setDofC(results.getString(results.getColumnIndexOrThrow(COL5)));
        }


         if(c == null)
         {
             throw new IllegalArgumentException("Contact does not exist");
         }

         return c;
    }



    /*
     * Insert a new contact into the database.
     */
    public void insertContact(Contact c) throws SQLException
    {
        ContentValues input = new ContentValues();
        input.put(COL1, c.getFirst());
        input.put(COL2, c.getLast());
        input.put(COL3, c.getNum());
        input.put(COL4, c.getDoB());
        input.put(COL5, c.getDofC());

        try
        {
            db.insertOrThrow(TABLE_NAME, null, input);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            throw new SQLException("Database connection err: Could not insert contact");
        }
    }


    /*
     * Attempt to update the given contact. If that name was not found, return false.
     */
    public void updateContact(Contact updatedContact) throws IllegalArgumentException
    {
        ContentValues input = new ContentValues();
        input.put(COL1, updatedContact.getFirst());
        input.put(COL2, updatedContact.getLast());
        input.put(COL3, updatedContact.getNum());
        input.put(COL4, updatedContact.getDoB());
        input.put(COL5, updatedContact.getDofC());

        where = COL1 + "=? AND " + COL2 + "=?";
        whereArgs = new String[2];
        whereArgs[0] = updatedContact.getPrevFirst();
        whereArgs[1] = updatedContact.getPrevLast();

        if((db.update(TABLE_NAME, input, where, whereArgs)) < 1)
        {
            throw new IllegalArgumentException("Contact does not exist");
        }
    }


    /*
     * Attempt to delete given contact from database. If contact was not in database to begin with,
     * return false.
     */

    public void deleteContact(Contact del_c) throws IllegalArgumentException
    {
        where = COL1 + "=? AND " + COL2 + "=?";
        whereArgs = new String[2];
        whereArgs[0] = del_c.getFirst();
        whereArgs[1] = del_c.getLast();

        if((db.delete(TABLE_NAME, where, whereArgs)) < 1)
        {
            throw new IllegalArgumentException("Contact does not exist");
        }
    }

    /*
     * Read data from the text file, if it exists, and insert it into the database.
     */
    public void importData(String fileName) throws IOException
    {
        Scanner in = null;
        String entry;
        String[] fields;
        Contact c;

        File inputFile = new File(context.getFilesDir(), fileName);

        try
        {
            in = new Scanner(inputFile);

            while(in.hasNext())
            {
                entry = in.nextLine();
                fields = entry.split("\t");
                c = new Contact();
                c.setFirst(fields[0]);
                c.setLast(fields[1]);
                c.setNum(fields[2]);
                c.setDoB(fields[3]);
                c.setDofC(fields[4]);
                insertContact(c);
            }

            in.close();
        }
        catch(Exception ex)
        {
            throw new IOException("IOException: Import File Not Found");
        }
    }
}
