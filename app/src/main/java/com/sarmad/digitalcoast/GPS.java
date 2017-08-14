package com.sarmad.digitalcoast;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sarmad on 15/4/17.
 */

public class GPS extends Service implements LocationListener {

//    public final int REQUEST_LOCATION = 2;
    Context context;
    Location location=null;
    LocationManager locationManager;
    double latitude, longitude;

    public GPS(Context context)
    {
        this.context = context;}

    @Override
    public void onCreate() {
        super.onCreate();
        location = getMyLocation();
    }

    public boolean isGPSEnabled()
    {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return true;

        return false;
    }

    public Location getMyLocation()
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(isGPSEnabled()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else {
            Log.d("ELSE:", "ENABLE GPS ALERT");
            showAlert();
        }
        return location;
    }

    public void showAlert()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Enable Location")
                .setMessage("Your location is not enabled. PLease go to settings, enable it and try again.")
                .setCancelable(true)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        alertDialog.show();
    }

    public double getMyLatitude()
    {
        if(location == null)
            location = getMyLocation();
        latitude = location.getLatitude();
        return latitude;
    }

    public double getMyLongitude()
    {
        int i=0;
        while(location == null) {
            if(i == 5)  break;
            location = getMyLocation();
            i++;
        }
        if(i==5)
            Toast.makeText(context, "Unable to get location", Toast.LENGTH_LONG);

        longitude = location.getLongitude();
        return longitude;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
