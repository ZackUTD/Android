package com.example.projectphase1_zoo150030;

import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*
 * Author: Zack Oldham
 * addressMapper
 * The addressMapper class is designed to display:
 * a) The latitude/longitude of a given address along
 * with the distance between it and the current location at the top of screen;
 * and b) a map with a marker at the given address. The map will also be zoomed in to a reasonable degree.
 */


public class addressMapper extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private contactAddress curAddress;

    /*
     * Get the address sent from the contactDetails acticvity and store it in curAddress.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_mapper);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        curAddress = null;

        if(intent != null)
        {
            String recv = intent.getStringExtra("address");
            if(recv != null)
            {
                curAddress = new contactAddress(recv);
            }
        }


    }


    /*
     * Add a marker on the map at the given address, display the longitude/latitude of that address,
     * and show the distance from current location to that address.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        if(curAddress != null)
        {
            addressLocator aL = new addressLocator(googleMap, this);
            aL.execute(curAddress);
        }


    }
}
