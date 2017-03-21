package com.example.dream.locationtracker;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{

    private final String LOG_TAG = "Location Test App";
    private TextView txtOutput,txtOutput1;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static final int REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        txtOutput = (TextView) findViewById(R.id.txtOutput);
        txtOutput1 = (TextView) findViewById(R.id.txtOutput1);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_TAG,"Google Api Connected");

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(12*60*1000); // after every 12 minutes the location will be updated

        Log.i(LOG_TAG, "Google api Connected " + android.Manifest.permission.ACCESS_FINE_LOCATION);
        Log.i(LOG_TAG, "Google api Connected " + PackageManager.PERMISSION_GRANTED);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            Log.i(LOG_TAG, "Google api Connected inside if condition to check run time");

        } else {
            Log.i(LOG_TAG, "Google api Connected Get Request" );
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

        Log.i(LOG_TAG, "Google api Connected Get Request" );
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG,"Google api suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG,"Google api Fail");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG,location.toString());
        txtOutput.setText("Latitude = "+Double.toString(location.getLatitude()));
        txtOutput1.setText("Longitude = "+Double.toString(location.getLongitude()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
