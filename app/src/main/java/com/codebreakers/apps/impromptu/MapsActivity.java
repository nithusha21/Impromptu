package com.codebreakers.apps.impromptu;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button ranklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ranklist = (Button) findViewById( R.id.ListForm );
        ranklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next=new Intent(MapsActivity.this,RankistActivity.class);
                startActivity(next);

            }
        });

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



        double[] locs = {12.9867340,80.2239163,
                12.9935502,80.2178866,
                12.9898703,80.2118356,
                12.9827404,80.2121360,
                12.9752966,80.2206976,
                12.9878631,80.2230794};

        mMap = googleMap;
        LatLng[] Hotels=new LatLng[MainActivity.tester.names.length];
        LatLng[] Friends=new LatLng[6];

        for(int i=0;i<6;i++){
            Friends[i] = new LatLng(locs[i*2], locs[i*2+1]);
        }

        for(int i=0;i<Hotels.length;i++){
            Hotels[i] = new LatLng(MainActivity.tester.latAnswers[i], MainActivity.tester.lonAnswers[i]);
        }

        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
        for(int i=0;i<Hotels.length;i++) {
            mMap.addMarker(new MarkerOptions().position(Hotels[i]).title((9-i)+". "+MainActivity.tester.answers[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        for(int i=0;i<Friends.length;i++){
            mMap.addMarker(new MarkerOptions().position(Friends[i]).title((i+1)+"").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Friends[0]));
    }
}
