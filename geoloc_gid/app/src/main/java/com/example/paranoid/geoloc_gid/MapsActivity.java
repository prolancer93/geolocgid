package com.example.paranoid.geoloc_gid;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final String TAG = "myLogs";
//    private LocationManager locationManager;
//    StringBuilder sbGPS = new StringBuilder();
//    StringBuilder sbNet = new StringBuilder();

    private Location coordinates;
    private GeoObject[] mMarkers;
    public  Polyline polyline;
    public  GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mapFragment == null) {
            finish();
            return;
        }
    }

    public void init() {
        final MapsActivity self = this;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "onMapClick: " + latLng.latitude + "," + latLng.longitude);
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng to) {
                if(coordinates == null) return;
                LatLng from = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());

                if(self.polyline != null)
                    self.polyline.remove();
                GMapV2DirectionAsyncTask.route(from, to, self);
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition camera) {
                Log.d(TAG, "onCameraChange: " + camera.target.latitude + "," + camera.target.longitude);
            }
        });

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                coordinates = location;
//                for(GeoObject marker : mMarkers){
//                    Location loc = new Location("");
//                    loc.setLatitude(marker.lat);
//                    loc.setLongitude(marker.lng);
//                    float meters = location.distanceTo(loc);
//                    Log.i("INFO", Float.toString(meters));
//                }
            }
        });

        mMarkers = GeoObject.getAll(this);
        for(GeoObject marker : mMarkers){
            Marker m = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(marker.lat, marker.lng))
                .title(marker.name)
                .snippet(marker.dscr));
        }

//        mMap.addMarker(new MarkerOptions()
//        .icon(BitmapDescriptorFactory.fromResource(R.drawable.cast_ic_notification_0)));
    }

    public void onClickTest(View view) {
        if (coordinates != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(coordinates.getLatitude(),
                            coordinates.getLongitude()))
                    .zoom(15)
                    .bearing(45)
                    .tilt(20)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.animateCamera(cameraUpdate);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setIndoorEnabled(false);
        //mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        init();
    }
}
