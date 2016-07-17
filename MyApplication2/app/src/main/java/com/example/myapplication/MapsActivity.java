package com.example.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int REQUEST_LOCATION = 0;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location currentLocation;
    private Marker currentMarker, curMarker;

    GoogleMap.OnMarkerDragListener markerDragListener = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {
            Toast.makeText(MapsActivity.this, "請拖曳至指定位置 !", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMarkerDrag(Marker marker) {
            //call when dragging
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            Toast.makeText(MapsActivity.this, "位置確認 !", Toast.LENGTH_SHORT).show();
        }
    };

    GoogleMap.OnMapLongClickListener mapLongClickListener = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            if(currentMarker != null){
                currentMarker.remove();
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("位置資訊");
            markerOptions.position(latLng);
            markerOptions.draggable(true);
            currentMarker = mMap.addMarker(markerOptions);
        }
    };

    GoogleMap.InfoWindowAdapter infoWindowAdapter = new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = getLayoutInflater().inflate(R.layout.my_infowindow, null);
            TextView address_text = (TextView)view.findViewById(R.id.address_text);
            TextView lat_text = (TextView)view.findViewById(R.id.lat_text);
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.TAIWAN);
            try {
                List<Address> address = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                String addr = address.get(0).getCountryName()+address.get(0).getAdminArea()+address.get(0).getLocality()+address.get(0).getThoroughfare()+address.get(0).getFeatureName();  //台灣省
                address_text.setText("\n"+addr);
                lat_text.setText("緯度 : " + marker.getPosition().latitude +
                        "\n經度 : " + marker.getPosition().longitude);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return view;
        }
    };
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick( Marker marker) {
            curMarker = marker;
            AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
            alert.setTitle("位置確認");
            alert.setMessage("確定記錄位置座標嗎?");
            alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent data = new Intent();
                    data.putExtra("LATITUDE", curMarker.getPosition().latitude);
                    data.putExtra("LONGITUDE", curMarker.getPosition().longitude);
                    setResult(RESULT_OK, data);
                    Toast.makeText(MapsActivity.this, "定位完成 ！", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //取消
                }
            });
            alert.show();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        configGoogleApiClient();
        configLocationRequest();
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();

        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient,this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!googleApiClient.isConnected() && currentMarker != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(mapLongClickListener);
        mMap.setOnMarkerDragListener(markerDragListener);
        mMap.setInfoWindowAdapter(infoWindowAdapter);
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else{
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else{
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(MapsActivity.this, "Google Service Disconnected ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();

        // 裝置沒有安裝Google Play服務
        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(this,"Google Service Failed",
                    Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(currentMarker == null){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("位置資訊");
            markerOptions.position(latLng);
            markerOptions.draggable(true);
            currentMarker = mMap.addMarker(markerOptions);
            moveMap(latLng);
        }
    }


    private synchronized void configGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void configLocationRequest() {
        locationRequest = new LocationRequest();
        // 設定讀取位置資訊的間隔時間為一秒（1000ms）
        locationRequest.setInterval(1000);
        // 設定讀取位置資訊最快的間隔時間為一秒（1000ms）
        locationRequest.setFastestInterval(1000);
        // 設定優先讀取高精確度的位置資訊（GPS）
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void moveMap(LatLng place){
        CameraPosition cameraPosition = new CameraPosition.Builder().target(place).zoom(17).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }






}
