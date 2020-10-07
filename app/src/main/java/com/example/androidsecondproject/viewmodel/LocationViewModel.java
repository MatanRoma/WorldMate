package com.example.androidsecondproject.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationViewModel extends MutableLiveData<Location> {

    private FusedLocationProviderClient mFusedLocation;
    private LocationRequest mLocationRequest;
    private static LocationViewModel mLocationInstance;

    LocationCallback mCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location= locationResult.getLocations().get(0);
            if(location!=null)
                setValue(location);
        }
    };

    public static LocationViewModel getInstance(Context context){
        if(mLocationInstance == null){
            mLocationInstance = new LocationViewModel(context);
        }
        return mLocationInstance;
    }

    @SuppressLint("MissingPermission")
    private LocationViewModel(Context context){

        mFusedLocation = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocation.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null)
                    setValue(location);
            }
        });
        addLocationRequest();
    }

    private void addLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5*60*1000);
        mLocationRequest.setFastestInterval(60*1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(3);

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActive() {
        super.onActive();
        mFusedLocation.requestLocationUpdates(mLocationRequest,mCallback,null);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if(mCallback!=null){
            mFusedLocation.removeLocationUpdates(mCallback);
        }
    }

}