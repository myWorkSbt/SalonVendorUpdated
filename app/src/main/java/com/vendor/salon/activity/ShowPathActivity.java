package com.vendor.salon.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.vendor.salon.R;
import com.vendor.salon.utilityMethod.NetworkChangeListener;

import java.util.ArrayList;
import java.util.List;

public class ShowPathActivity extends FragmentActivity implements OnMapReadyCallback {

    private LatLng starting_point;
    private LatLng end_point;
    private double st_lat, st_long, et_lat , et_lng ;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_path);

        st_lat = getIntent().getDoubleExtra("s_p_lat" , 0.0) ;
        st_long = getIntent().getDoubleExtra("s_p_lng" , 0.0);
        et_lat = getIntent().getDoubleExtra("e_lat" , 0.0);
        et_lng = getIntent().getDoubleExtra("e_lng" , 0.0) ;
        starting_point = new LatLng( st_lat,st_long);
        end_point = new LatLng( et_lat,et_lng );
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps_frag);
        mapFragment.getMapAsync(this);
    }


    private GoogleMap mMap;
    private String TAG = "so47492459";


    private BitmapDescriptor bitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

//        LatLng barcelona = new LatLng(41.385064,2.173403);
        if (st_lat == 0.0 || st_long == 0.0 ) {
            starting_point = new LatLng(27.1743 , 78.0195);
        }
        mMap.addMarker(new MarkerOptions().position(starting_point).title("Starting Point ").icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        if (et_lat == 0.0 || et_lng == 0.0 ) {
            end_point = new LatLng(28.535517,77.391029);
        }
        mMap.addMarker(new MarkerOptions().position(end_point).title("Destination Point "));

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyATW5jE8FZwyqSqf6-9tWIswj-JH-Gg7Fg")
                .build();
        DirectionsApiRequest req= DirectionsApi.getDirections(context,  starting_point.latitude+"," + starting_point.longitude , end_point.latitude + "," +  end_point.longitude  );
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            if (ex != null ) {
//                Log.e(TAG, ex.getLocalizedMessage());
            }
            else {
                Log.e(TAG, "A river or ocean between coordinates ");
                Toast.makeText(this, "No Path exists . ", Toast.LENGTH_SHORT).show();
            }
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragoza, 6));
    }


    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener , intentFilter );
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}