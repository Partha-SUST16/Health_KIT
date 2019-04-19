package com.example.healthkit;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Circle extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    private GoogleMap uMap;
    private double lati,longi;
    private String Lat,Lone,area,type,radius,location,test;
    String needHelp ;
    Double nl,nlo;
    private Switch HELP = null;
    private LatLng t_latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        needHelp="";

        Lat = getIntent().getExtras().getString("Lati").toString();
        Lone = getIntent().getExtras().getString("Longi").toString();
        Log.d("LAtLong :",Lat);
        Log.d("LAtLong :",Lone);

        FirebaseDatabase.getInstance().getReference().addValueEventListener(valueEventListener);
        HELP = (Switch) findViewById(R.id.help);
        HELP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // do something when check is selected
                   FirebaseDatabase.getInstance().getReference().child("needHelp").setValue("false");
                } else {
                    //do something when unchecked

                }
            }
        });
    }

    private float getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] distance = new float[2];
        Location.distanceBetween(lat1, lon1, lat2, lon2, distance);
        return distance[0];
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("LAtLong On:",Lat);
        Log.d("LAtLong On:",Lone);
        lati = Double.parseDouble(Lat);
        longi = Double.parseDouble(Lone);

        MarkerOptions markerOptions = new MarkerOptions();
        uMap = googleMap;
        LatLng temp = new LatLng(lati,longi);
        markerOptions.position(temp);
        markerOptions.title("Your Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        uMap.addMarker(markerOptions);
        uMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp,10));
        //mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getKey().equalsIgnoreCase("needhelp")) {
                        needHelp = snapshot.getValue(String.class);

                         Log.d("emni",needHelp);
                    }
                    String t;
                    if(snapshot.getKey().equalsIgnoreCase("lati")) {
                         t = snapshot.getValue(String.class);
                        nl = Double.parseDouble(t);
                        Log.d("emni_", t);
                    }
                    if(snapshot.getKey().equalsIgnoreCase("longi")) {
                        t = snapshot.getValue(String.class);
                        Log.d("emni__", t);
                        nlo = Double.parseDouble(t);
                    }
                    if(needHelp.equalsIgnoreCase("true"))
                        show();
                    else {
                        backtoown();
                    }
                }

            }
        }
        private void backtoown()
        {
            uMap.clear();
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng temp = new LatLng(lati,longi);
            markerOptions.position(temp);
            markerOptions.title("Your Location");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            uMap.addMarker(markerOptions);
            uMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp,10));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };
    private void show()
    {
        uMap.clear();
        Geocoder geocoder = new Geocoder(Circle.this);
        List<Address> addressList = null;
        MarkerOptions markerOptions = new MarkerOptions();
        try {
            t_latLng = new LatLng(nl, nlo);
            markerOptions.position(t_latLng);
            markerOptions.title("NEED HELP HERE!!!!");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            uMap.addMarker(markerOptions);
            uMap.moveCamera(CameraUpdateFactory.newLatLngZoom(t_latLng,10));
        }catch (Exception e)
        {

        }
        Vibrator v = (Vibrator) getSystemService(Circle.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(3000);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
