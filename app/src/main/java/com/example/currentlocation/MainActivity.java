package com.example.currentlocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView latTV, lonTV, addressTv;
    private FusedLocationProviderClient client;
    private LocationRequest request;
    private LocationCallback callback;
    private static int REQUEST_CODE_FOR_LOCATION=1;
    private Geocoder geocoder;
    private String locationName;
    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latTV = findViewById(R.id.latitudeTVID);
        lonTV = findViewById(R.id.longitudeTVID);
        addressTv = findViewById(R.id.addressTVID);
        client = LocationServices.getFusedLocationProviderClient(this);
        request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000).setFastestInterval(1000);

        geocoder = new Geocoder(this);
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    latTV.setText(String.valueOf(lat));
                    lonTV.setText(String.valueOf(lon));

                    try {
                        List<Address> addresses = geocoder.getFromLocation(lat,lon,1);
                        locationName = addresses.get(0).getLocality()+" ,"+ addresses.get(0).getSubLocality()+", "+ addresses.get(0).getPostalCode()+","+addresses.get(0).getFeatureName();
                        addressTv.setText(locationName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE_FOR_LOCATION);
            return;
        }
        client.requestLocationUpdates(request, callback, null);

    }





}
