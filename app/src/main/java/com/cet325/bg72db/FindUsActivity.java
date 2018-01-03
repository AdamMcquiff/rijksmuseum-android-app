package com.cet325.bg72db;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Please restart the application",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Initiate LatLng object, setup map, create marker and move to it
            LatLng museumLatLng = new LatLng(
                    Float.parseFloat(getResources().getString(R.string.museum_lat)),
                    Float.parseFloat(getResources().getString(R.string.museum_lng))
            );
            map.setMyLocationEnabled(true);
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setAllGesturesEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(museumLatLng, 15.0f));
            map.addMarker(new MarkerOptions().position(museumLatLng)
                    .title(getResources().getString(R.string.museum_name))
                    .snippet(getResources().getString(R.string.museum_address)))
                    .showInfoWindow();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_info:
                Intent ticketInfoActivityIntent = new Intent(getApplicationContext(), TicketInformationActivity.class);
                startActivity(ticketInfoActivityIntent);
                return true;
            case R.id.paintings_link:
                Intent paintings_activity_intent = new Intent(getApplicationContext(), PaintingMasterActivity.class);
                startActivity(paintings_activity_intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0)
                    permissionLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionLocationAccepted) finish();
    }

}
