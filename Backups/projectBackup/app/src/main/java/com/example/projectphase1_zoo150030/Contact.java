package com.example.projectphase1_zoo150030;

/*
 * Author: Zack Oldham
 * Contact.java
 * The Contact class is an expert class designed strictly to hold information about a person (i.e. name, phone number, etc.).
 */


public class Contact
{
    private String first, last, prevFirst, prevLast, pNumber, DoB, DofC;

    public Contact(){}

    public Contact(String input)
    {
        String[] params = input.split(",");
        first = params[0];
        prevFirst = params[1];
        last = params[2];
        prevLast = params[3];
        pNumber = params[4];
        DoB = params[5];
        DofC = params[6];
    }



    public void setFirst(String f)
    {
        if(first == null)
        {
            prevFirst = f;
        }
        else
        {
            prevFirst = first;
        }

        first = f;
    }

    public String getFirst()
    {
        return first;
    }

    public String getPrevFirst()
    {
        return prevFirst;
    }

    public void setLast(String l)
    {
        if(last == null)
        {
            prevLast = l;
        }
        else
        {
            prevLast = last;
        }

        last = l;
    }

    public String getLast()
    {
        return last;
    }

    public String getPrevLast()
    {
        return prevLast;
    }

    public void setNum(String n)
    {
        pNumber = n;
    }

    public String getNum()
    {
        return pNumber;
    }

    public void setDoB(String dob)
    {
        DoB = dob;
    }

    public String getDoB()
    {
        return DoB;
    }

    public void setDofC(String dofc)
    {
        DofC = dofc;
    }

    public String getDofC()
    {
        return DofC;
    }

    public String toString()
    {
        return first + "," + prevFirst + "," + last + "," + prevLast + "," + pNumber+ "," + DoB + "," + DofC;
    }


}
