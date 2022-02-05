package com.example.projectphase1_zoo150030;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;


/*
 * Author: Zack Oldham
 * addressLocator
 * The addressLocator class is an AsyncTask that calculates the longitude/latitude of a given address,
 * as well as the distance between that address and the current location.
 */



public class addressLocator extends AsyncTask<contactAddress, Void, Double[]>
{
    private GoogleMap map;
    private addressMapper context;

    /*
     * Constructs a new addressLocator with the given map and addressMapper
     */
    public addressLocator(GoogleMap m, addressMapper con)
    {
        map = m;
        context = con;
    }

    /*
     * Use reverse geocoding to determine the latitude/longitude of a given address. Then,
     * calculate the distance between that address and the current location. If any permissions are needed
     * to access the present location of the device, ask the user for permission to use the device's address.
     */
    @Override
    public Double[] doInBackground(contactAddress... args)
    {
        //Scanner in;
        //StringBuilder sb;
        //HttpURLConnection conn;
        Double[] results = null;

        if(args[0] == null)
        {
            return null;
        }


        try
        {
            Geocoder geoCoder = new Geocoder(context);
            List<Address> addrList = geoCoder.getFromLocationName(args[0].toSearchableString(), 1);

            if(addrList != null && addrList.size() > 0)
            {
                results = new Double[3];
                results[0] = addrList.get(0).getLatitude();
                results[1] = addrList.get(0).getLongitude();
            }


            LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            boolean isNetworkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Location myLocation = null;


            int perms = 0;

            if(isNetworkProviderEnabled)
            {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE},
                            perms);
                }

                myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }


            if(myLocation == null)
            {
                return null;
            }

            float[] dist = new float[3];
            Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(), results[0], results[1], dist);

            results[2] = (double)(dist[0]/1000);

            return results;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }

    }


    /*
     * Display the lat/long of the given address as well as the distance between the current location
     * and the address at the top of the screen above the map.
     */
    @Override
    public void onPostExecute(Double[] data)
    {
        TextView lat = context.findViewById(R.id.lat);
        TextView lng = context.findViewById(R.id.lng);
        TextView dist = context.findViewById(R.id.distBtwn);

        if(data != null)
        {
            LatLng addr = new LatLng(data[0], data[1]);
            map.addMarker(new MarkerOptions().position(addr).title("Current Contact's address"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(addr, 10.5f));


            String latData = String.format(Locale.ENGLISH, "Latitude: %.2f", data[0]);
            String lngData = String.format(Locale.ENGLISH, "Longitude: %.2f", data[1]);
            String dstData = String.format(Locale.ENGLISH, "Distance from you: %.2fkm", data[2]);

            lat.setText(latData);
            lng.setText(lngData);
            dist.setText(dstData);
        }
    }
}
