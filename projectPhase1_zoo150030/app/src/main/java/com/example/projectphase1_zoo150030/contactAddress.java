package com.example.projectphase1_zoo150030;

/*
 * Author: Zack Oldham
 * contactAddress
 * The contactAddress class is an expert class designed to contain the contents of a specific address.
 * A Contact posses a contactAddress.
 */


public class contactAddress
{
    private String addrL1, addrL2, city, state, zip;

    public contactAddress() {}

    public contactAddress(String args)
    {
        String[] argsArr = args.split(",");
        addrL1 = argsArr[0];
        addrL2 = argsArr[1];
        city = argsArr[2];
        state = argsArr[3];
        zip = argsArr[4];
    }

    public String getAddrL1()
    {
        return addrL1;
    }

    public void setAddrL1(String a1)
    {
        addrL1 = a1;
    }

    public String getAddrL2()
    {
        return addrL2;
    }

    public void setAddrL2(String a2)
    {
        addrL2 = a2;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String c)
    {
        city = c;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String s)
    {
        state = s;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(String z)
    {
        zip = z;
    }

    /*
     * Returns a comma separated string of the contents of the address
     */
    public String toString()
    {
        return addrL1 + "," + addrL2 + "," + city + "," + state + "," + zip;
    }


    /*
     * Returns a space separated string of the current address (required by GeoCoder.getFromLocationName)
     */
    public String toSearchableString()
    {
        return addrL1 + " " + addrL2 + " " + city + " " + state + " " + zip;
    }
}
