package com.example.healthkit;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.layout_infowindow_map, null);
        TextView hospitalName = view.findViewById(R.id.HospitalNameID);
        TextView detailsBtn = view.findViewById(R.id.DetailsBtn);

        hospitalName.setText(marker.getTitle());

        return view;
    }
}
