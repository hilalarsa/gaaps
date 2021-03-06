package com.pastelstudio.hilal13.gaapp5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pastelstudio.hilal13.gaapp5.model.driver;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Handler h = new Handler();
    int delay = 7*1000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;
    private TextView mTextMessage;

    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    PlaceAutocompleteFragment placeAutoComplete;

    public Double getLatitude,getLongitude;
    public String getNama,getStatus,getTelepon;
    public int markerStatus;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("driver");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //getData(); // ambil data dari firebase
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//------------------------------
        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("Maps", "Place selected: " + place.getName());
                Toast.makeText(MapsActivity.this,"Place seleced :" + place.getName() + " and has Lat Lng " + place.getLatLng(),Toast.LENGTH_LONG).show();

                LatLng newSearch = place.getLatLng();

                markerRefresh(newSearch);
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
                Toast.makeText(MapsActivity.this,"Error" + status,Toast.LENGTH_LONG).show();
            }


        });

    }



    public void getNewLocation()
    {
        for (int i = 1; i < 4; i++) {

            final String uid = "driver0" + i;
            final int finalI = i;
            mDatabase.child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //for (int i = 1; i < 3; i++) {

                                final driver drv1 = dataSnapshot.getValue(driver.class);
                                getLatitude = drv1.getLatitude();
                                getLongitude = drv1.getLongitude();
                                getNama = drv1.getNama();
                                getStatus = drv1.getStatus();
                                getTelepon = drv1.getTelepon();
//                                Toast.makeText(MapsActivity.this, "Data : " + dataSnapshot,
//                                        Toast.LENGTH_LONG).show();
//                                Toast.makeText(MapsActivity.this, "Driver : "+getNama+", Current Pos : " + getLatitude + " | " + getLongitude,
//                                        Toast.LENGTH_SHORT).show();

                                LatLng newPos = new LatLng(getLatitude, getLongitude);
                                Log.d("TAG", "Lat : " + getLatitude + " and Long : " + getLongitude);
//                              cek status

                                if(getStatus.equals("idle")) {
                                    markerStatus=R.mipmap.marker_ga;
                                }else if(getStatus.equals("working")){
                                    markerStatus=R.mipmap.marker_ga_disabled;
                                }

                            Marker marker1=mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(markerStatus))
                                    .position(newPos)
                                    .title(getNama)
                                    .draggable(false)
                                    .snippet("Status : "+getStatus));
                            marker1.setTag(uid);
                                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                            public void onInfoWindowClick(Marker arg0)
                                            {
                                                Intent intent = new Intent(getApplicationContext(), PesanActivity.class);
//                                                intent.putExtra("fullData", drv1);
                                                intent.putExtra("uid",arg0.getTag().toString());
                                                startActivity(intent);
                                            }
                                        });

                            //} //hapus sementara

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();


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

        LatLng sai = new LatLng(-7.5614016,112.6131175);
//        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sai,12.0f));
    }
    public void getData(){


    }
    public void markerRefresh(final LatLng pos){
//refresh marker tjoyy
//        langitude=pos.latitude;
//        longitude=pos.longitude;
        LatLng newPos = new LatLng(getLatitude,getLongitude);
        //mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_ga))
                .position(newPos)
                .title("Marker in Searched position")
                .draggable(true));;

//        mStreetViewPanorama.setPosition(pos);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,12.0f));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                // TODO Auto-generated method stub
                // Here your code
                Toast.makeText(MapsActivity.this, "Dragging Start",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // TODO Auto-generated method stub
                LatLng position = marker.getPosition();
                Toast.makeText(
                        MapsActivity.this,
                        "Lat " + position.latitude + " "
                                + "Long " + position.longitude,
                        Toast.LENGTH_LONG).show();
//                langitude=position.latitude;
//                longitude=position.longitude;
//                currentLatLng = new LatLng(position.latitude, position.longitude);
//                mStreetViewPanorama.setPosition(currentLatLng);

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // TODO Auto-generated method stub
                // Toast.makeText(MainActivity.this, "Dragging",
                // Toast.LENGTH_SHORT).show();
                System.out.println("Draagging");
            }

        });

    }

    @Override
    protected void onResume() {
        //start handler as activity become visible

        h.postDelayed(new Runnable() {
            public void run() {
                getNewLocation();
                mMap.clear();

                runnable=this;

                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    Toast.makeText(MapsActivity.this, "Data refreshed",
                            Toast.LENGTH_SHORT).show();
                    getNewLocation();
                    return true;

                case R.id.navigation_dashboard:
                    Toast.makeText(MapsActivity.this, "List driver",
                            Toast.LENGTH_SHORT).show();
                    Intent listdriver = new Intent(getApplicationContext(),ListDriver.class);
                    startActivity(listdriver);
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;

                case R.id.navigation_notifications:
                    Toast.makeText(MapsActivity.this, "Notifications",
                            Toast.LENGTH_SHORT).show();
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };
}
