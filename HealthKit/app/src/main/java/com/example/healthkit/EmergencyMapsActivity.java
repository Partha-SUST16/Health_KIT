package com.example.healthkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Permission;

public class EmergencyMapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnInfoWindowClickListener {
    private Button searchHospital;

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    private static final int REQUEST_USER_LOCATION_CODE = 99;
    private double hospitalLatitude, hospitalLongitude;
    private String hospital = "hospital";
    private int radiusOfCircle = 7000;
    private String radiusOfCircleString = "7000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_maps);

        searchHospital = (Button) findViewById(R.id.searchHospitalBtnId);
        searchHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object transferData[] = new Object[2];
                EmergencyNearbyPlaces emergencyNearbyPlaces = new EmergencyNearbyPlaces(EmergencyMapsActivity.this);

                mMap.clear();
                String url = getUrl(hospitalLatitude, hospitalLongitude, hospital);

                transferData[0] = mMap;
                transferData[1] = url;
                emergencyNearbyPlaces.execute(transferData);
                Toast.makeText(getApplicationContext(), "Searching For Nearby Hospitals", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();


                LatLng currentlatLng = new LatLng(hospitalLatitude, hospitalLongitude);
                // Instantiating CircleOptions to draw a circle around the marker
                CircleOptions circleOptions = new CircleOptions();

                // Specifying the center of the circle
                circleOptions.center(currentlatLng);

                // Radius of the circle
                circleOptions.radius(radiusOfCircle);

                // Border color of the circle
                circleOptions.strokeColor(Color.BLACK);

                // Fill color of the circle
                circleOptions.fillColor(0x30ff0000);

                // Border width of the circle
                circleOptions.strokeWidth(2);

                // Adding the circle to the GoogleMap
                mMap.addCircle(circleOptions);
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            builtGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }


    }

    private String getUrl(double latitude, double longitude, String hospitalname) {
        StringBuilder googleUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleUrl.append("location=" + latitude + "," + longitude);
        googleUrl.append("&radius=" + radiusOfCircleString);
        googleUrl.append("&type=" + hospitalname);
        googleUrl.append("&keyword=hospital");
        //googleUrl.append("&sensor=true");
        //googleUrl.append("&key="+"AIzaSyBg_DDx-7UJJ3SB3ORoAHhez4HTXyGaCJg");
        String api = "AIzaSyAf_2nyOyxxkBRK0bStKbWb_e1E2ZyDZOM";
        googleUrl.append("&key=" + api);

        Log.d("EmergencyMapsActivity", "Url: " + googleUrl.toString());
        return googleUrl.toString();

    }

    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);
            }
            return false;
        } else
            return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_USER_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            builtGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void builtGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).
                build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        hospitalLatitude = location.getLatitude();
        hospitalLongitude = location.getLongitude();

        lastLocation = location;
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }
        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        MarkerOptions currentLocationMarkerOption = new MarkerOptions();
        currentLocationMarkerOption.position(latLng);
        currentLocationMarkerOption.title("My Location");
        currentLocationMarkerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        //currentLocationMarker = mMap.addMarker(currentLocationMarkerOption);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        String temp = marker.getTitle();
        Intent intent  = new Intent(getApplicationContext(),DoctorbyHospital.class);
        Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
        intent.putExtra("title",temp);
        startActivity(intent);
    }
}