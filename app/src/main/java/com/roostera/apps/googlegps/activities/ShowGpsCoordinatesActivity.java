package com.roostera.apps.googlegps.activities;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.roostera.apps.googlegps.R;

public class ShowGpsCoordinatesActivity extends AppCompatActivity {
    private TextView tvLatitud;
    private TextView tvLongitud;
    private Double latitud;
    private Double longitud;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readData();
        setupUI();
    }

    private void readData(){
        Bundle extras = getIntent().getExtras();
        location = extras.getParcelable("location");
        latitud = location.getLatitude();
        longitud = location.getLongitude();
    }

    private void setupUI(){
        setContentView(R.layout.activity_show_gps_coordinates);
        tvLatitud = (TextView) findViewById(R.id.tvLatitud);
        tvLongitud = (TextView) findViewById(R.id.tvLongitud);
        tvLatitud.setText(String.valueOf(latitud));
        tvLongitud.setText(String.valueOf(longitud));
    }
}