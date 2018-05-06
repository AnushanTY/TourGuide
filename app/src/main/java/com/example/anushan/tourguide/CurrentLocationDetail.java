package com.example.anushan.tourguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CurrentLocationDetail extends Fragment implements OnMapReadyCallback {
    private FusedLocationProviderClient mFusedLocationClient;
    private  View view;
    private TextView lon,lat,mul;
    private GoogleMap mMap;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());



        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        view=inflater.inflate(R.layout.currentlocationdetail, container, false);
        lon=(TextView)view.findViewById(R.id.lon);
        lat=(TextView)view.findViewById(R.id.lat);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                         lon.setText(String.valueOf(location.getLongitude()));
                         lat.setText(String.valueOf(location.getLatitude()));
                        }else{
                            Log.w("hhhh","failed ");
                        }
                    }
                });

        mapView = (MapView) view.findViewById(R.id.mapviewforCurrentLocation);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        mul=(TextView)view.findViewById(R.id.multiAutoCompleteTextView);
        return  view;



    }

    @Override
    public void onStart() {
        super.onStart();
        statusCheck();
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            Geocoder geocoder = new Geocoder(view.getContext(), Locale.getDefault());
                            String result = null;
                            double v1= location.getLatitude();
                            double v2=location.getLongitude();

                            try {
                                List<Address> addressList = geocoder.getFromLocation(
                                        v1, v2, 1);
                                if (addressList != null && addressList.size() > 0) {
                                    Address address = addressList.get(0);
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                        sb.append(address.getAddressLine(i)).append("\n");//adress
                                    }
                                    sb.append(address.getLocality()).append("\n");//village

                                    sb.append(address.getPostalCode()).append("\n");
                                    sb.append(address.getCountryName());
                                    sb.append(address.getAdminArea()).append("\n"); //state

                                    sb.append(address.getSubAdminArea()).append("\n");//district

                                    sb.append(address.getSubLocality()).append("\n");

                                    result = sb.toString();
                                    mul.setText(result);
                                    Log.w("hhhhhhhh",result);
                                }
                            } catch (IOException e) {
                                // Log.e(TAG, "Unable connect to Geocoder", e);
                            }
                            final LatLng latlng = new LatLng(v1, v2);
                            float zoomLevel = (float) 10.0;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(result)
                                    .snippet(result)
                                    .icon(bitmapDescriptorFromVector(view.getContext(),R.drawable.ic_my_location_black_24dp)));



                        }else{
                            Log.w("hhhh","failed ");
                        }
                    }
                });


    }



    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
