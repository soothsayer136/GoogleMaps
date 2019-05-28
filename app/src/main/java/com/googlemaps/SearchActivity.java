package com.googlemaps;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import model.LongitudeLatitude;

public class SearchActivity extends FragmentActivity implements OnMapReadyCallback {
 private GoogleMap mMap;
 private AutoCompleteTextView etCityname;
 private Button btnSearch;
 private List<LongitudeLatitude> longitudeLatitudesList;
 Marker markerName;
 CameraUpdate center, zoom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


       etCityname=findViewById(R.id.etCityname);
       btnSearch = findViewById(R.id.btnSearch);

       fillArrayListAndSetAdapter();

       btnSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (TextUtils.isEmpty(etCityname.getText().toString())){
                   etCityname.setError("Please enter the name");
                   return;
               }
               //Get the current location of the place
               int position = SearchArrayList(etCityname.getText().toString());
               if (position > -1)
                   loadMap(position);
                    else
                   Toast.makeText(SearchActivity.this, "Location not found by name : " + etCityname.getText().toString(), Toast.LENGTH_SHORT).show();

           }

           private void loadMap(int position) {
               //Remove old marker from the map
               if (markerName!= null)
               {
                   markerName.remove();
               }

               double latitude = longitudeLatitudesList.get(position).getLat();
               double longitude = longitudeLatitudesList.get(position).getLon();
               String marker = longitudeLatitudesList.get(position).getMarker();
               center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
               zoom =  CameraUpdateFactory.zoomTo(17);
               markerName = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(marker));
               mMap.moveCamera(center);
               mMap.animateCamera(zoom);
               mMap.getUiSettings().setZoomControlsEnabled(true);
           }

           private int SearchArrayList(String name) {
               for (int  i =0; i < longitudeLatitudesList.size(); i++) {
                   if (longitudeLatitudesList.get(i).getMarker().contains(name)){
                       return i;
                   }
               }
               return -1;

           }
       });


    }

    private void fillArrayListAndSetAdapter() {
        longitudeLatitudesList.add(new LongitudeLatitude(27.7134481, 85.3241922, "Naagpokhari"));
        longitudeLatitudesList.add(new LongitudeLatitude(27.7187481, 85.3241912, "Narayanihiti Place"));
        longitudeLatitudesList.add(new LongitudeLatitude(27.7134427, 85.3241991, "Hotel Brihaspati"));

        String[] data =  new String[longitudeLatitudesList.size()];

        for (int i = 0; i < data.length;  i++){

            data[i] = longitudeLatitudesList.get(i).getMarker();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                SearchActivity.this,
                android.R.layout.simple_list_item_1,
                data
        );
        etCityname.setAdapter(adapter);
        etCityname.setThreshold(1);


        //  This function will check weather

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
        center = CameraUpdateFactory.newLatLng(new LatLng(27.7172453, 85.3239605));
        zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }
}
