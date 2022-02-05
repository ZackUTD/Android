package com.example.projectphase1_zoo150030;

/*
 * Author: Zack Oldham
 * Contact.java
 * The Contact class is an expert class designed strictly to hold information about a person (i.e. name, phone number, etc.).
 */


public class Contact
{
    private String first, last, prevFirst, prevLast, pNumber, DoB, DofC;
    private contactAddress addr;

    public Contact(){}

    public Contact(String input)
    {
        String[] params = input.split(":");
        String[] con = params[0].split(",");
        String add = params[1];

        first = con[0];
        prevFirst = con[1];
        last = con[2];
        prevLast = con[3];
        pNumber = con[4];
        DoB = con[5];
        DofC = con[6];

        addr = new contactAddress(add);
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

    public contactAddress getAddr()
    {
        return addr;
    }

    public void setAddr(contactAddress a)
    {
        addr = a;
    }

    public String toString()
    {
        return first + "," + prevFirst + "," + last + "," + prevLast + "," + pNumber+ "," + DoB +
                "," + DofC + ":" + addr.toString();
    }
}
