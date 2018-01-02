package com.cet325.bg72db;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindUsActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActionBar actionBar = null;

    private static final int MY_LOCATION_REQUEST_CODE = 201;
    private boolean permissionLocationAccepted = false;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

    float totalDistance = 0;
    Location previousLocation = null;
    Location currentLocation = null;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_us);

        actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        ActivityCompat.requestPermissions(this, permissions, MY_LOCATION_REQUEST_CODE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Please restart the application",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            map = googleMap;
            map.setMyLocationEnabled(true);
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setAllGesturesEnabled(true);

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.360024, 4.885230), 15.0f));

            LatLng museum = new LatLng(52.360024, 4.885230);
            map.addMarker(new MarkerOptions().position(museum)
                    .title(getResources().getString(R.string.museum_name))
                    .snippet(getResources().getString(R.string.museum_address)))
                    .showInfoWindow();
            map.moveCamera(CameraUpdateFactory.newLatLng(museum));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE:
                permissionLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionLocationAccepted) finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}
