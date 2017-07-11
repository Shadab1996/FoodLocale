package com.stinkinsweet.foodlocale;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private DatabaseReference databaseAll,databaseIndian,databaseItalian,databaseSouth,databaseChinese;
    private String food_loc = "";
    private List<android.location.Address> foodLoc = null;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private String dist;
    private TextView txtDish,txtLoc,txtDistance;
    private android.location.Address address;
   private String post_key;
   private String cuisine,dish,latitude,longitude;
    private LatLng latLng;
    private String lat,log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        databaseAll = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("All");
        databaseIndian = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("Indian");
        databaseItalian = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("Italian");
        databaseSouth = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("South Indian");
        databaseChinese = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("Chinese");

        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            return;
        }
         post_key = extra.getString("post_key");
         cuisine = extra.getString("cuisine_name");
        dish=extra.getString("dish_name");
        latitude=extra.getString("latitude");
        longitude=extra.getString("longitude");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        txtLoc=(TextView)findViewById(R.id.txtName);
        txtDish=(TextView)findViewById(R.id.txtDish);
        txtDistance=(TextView)findViewById(R.id.txtDistance);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        setTitle(dish);

    }
// NEW CODE START
    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();


        }
        Geocoder geocoder=new Geocoder(this);
        android.location.Address address;
        float[] results = new float[1];
        try {
            foodLoc = geocoder.getFromLocationName(food_loc, 1);
            for (int i = 0; foodLoc.size() == 0 && i < 10; i++) {
                foodLoc = geocoder.getFromLocationName(food_loc, 1);
            }
            if (foodLoc != null && foodLoc.size() > 0) {
                address = foodLoc.get(0);

                Location.distanceBetween(currentLatitude, currentLongitude,
                        address.getLatitude() , address.getLongitude(),
                        results);


            }
        }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        dist=String.format("%.2f", results[0]/1000);
      //  Toast.makeText(this," Distance from you " +dist+"KM", Toast.LENGTH_LONG).show();

        txtDistance.setText("Distance from you: "+dist+"KM");
        txtDish.setText(dish);
        txtLoc.setText(food_loc);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();


    }


    //NEW CODE END
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case result: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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
    private static final int result = 770;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            Toast.makeText(this, "turn on location", Toast.LENGTH_LONG).show();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
             //   mMap.setMyLocationEnabled(true);

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {


                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        result);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        final Geocoder geocoder = new Geocoder(this);


if(cuisine.equals("Indian")) {
    databaseIndian.child(post_key).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            food_loc = (String) dataSnapshot.child("loc").getValue();
            lat = (String) dataSnapshot.child("latitude").getValue();
            log = (String) dataSnapshot.child("longitude").getValue();

            if (food_loc != null || !food_loc.equals("")) {

                try {
                    foodLoc = geocoder.getFromLocationName(food_loc, 1);
                    for (int i = 0; foodLoc.size() == 0 && i < 10; i++) {
                        foodLoc = geocoder.getFromLocationName(food_loc, 1);
                    }
                    if (foodLoc != null && foodLoc.size() > 0) {
                        address = foodLoc.get(0);
                       if(latitude.equals("")&&longitude.equals(""))
                            latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        else
                        latLng=new LatLng(Double.parseDouble(lat),Double.parseDouble(log));
                        mMap.addMarker(new MarkerOptions().position(latLng).title(food_loc));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        // Zoom in, animating the camera.
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        //  Toast.makeText(getApplication(),currentLatitude +" , "+ currentLongitude + "", Toast.LENGTH_LONG).show();


                    } else {
                        // Toast.makeText(getBaseContext(),"Unable to find this location\n"+food_loc,Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                {


                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}

        else
if(cuisine.equals("South Indian")) {
    databaseSouth.child(post_key).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            food_loc = (String) dataSnapshot.child("loc").getValue();

            if (food_loc != null || !food_loc.equals("")) {

                try {
                    foodLoc = geocoder.getFromLocationName(food_loc, 1);
                    for (int i = 0; foodLoc.size() == 0 && i < 10; i++) {
                        foodLoc = geocoder.getFromLocationName(food_loc, 1);
                    }
                    if (foodLoc != null && foodLoc.size() > 0) {
                        address = foodLoc.get(0);
                        if(latitude.equals("")&&longitude.equals(""))
                            latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        else
                            latLng=new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                        mMap.addMarker(new MarkerOptions().position(latLng).title(food_loc));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        // Zoom in, animating the camera.
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        //  Toast.makeText(getApplication(),currentLatitude +" , "+ currentLongitude + "", Toast.LENGTH_LONG).show();


                    } else {
                        // Toast.makeText(getBaseContext(),"Unable to find this location\n"+food_loc,Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                {


                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}


        else
if(cuisine.equals("Indian Chinese")) {
    databaseChinese.child(post_key).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            food_loc = (String) dataSnapshot.child("loc").getValue();

            if (food_loc != null || !food_loc.equals("")) {

                try {
                    foodLoc = geocoder.getFromLocationName(food_loc, 1);
                    for (int i = 0; foodLoc.size() == 0 && i < 10; i++) {
                        foodLoc = geocoder.getFromLocationName(food_loc, 1);
                    }
                    if (foodLoc != null && foodLoc.size() > 0) {
                        address = foodLoc.get(0);
                        if(latitude.equals("")&&longitude.equals(""))
                            latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        else
                            latLng=new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                        mMap.addMarker(new MarkerOptions().position(latLng).title(food_loc));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        // Zoom in, animating the camera.
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        //  Toast.makeText(getApplication(),currentLatitude +" , "+ currentLongitude + "", Toast.LENGTH_LONG).show();


                    } else {
                        // Toast.makeText(getBaseContext(),"Unable to find this location\n"+food_loc,Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                {


                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}


        else
if(cuisine.equals("Italian")) {
    databaseItalian.child(post_key).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            food_loc = (String) dataSnapshot.child("loc").getValue();

            if (food_loc != null || !food_loc.equals("")) {

                try {
                    foodLoc = geocoder.getFromLocationName(food_loc, 1);
                    for (int i = 0; foodLoc.size() == 0 && i < 10; i++) {
                        foodLoc = geocoder.getFromLocationName(food_loc, 1);
                    }
                    if (foodLoc != null && foodLoc.size() > 0) {
                        address = foodLoc.get(0);
                        if(latitude.equals("")&&longitude.equals(""))
                            latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        else
                            latLng=new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                        mMap.addMarker(new MarkerOptions().position(latLng).title(food_loc));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        // Zoom in, animating the camera.
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);



                    } else {
                        // Toast.makeText(getBaseContext(),"Unable to find this location\n"+food_loc,Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                {


                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}

        else

if(cuisine.equals("All")) {
    databaseAll.child(post_key).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            food_loc = (String) dataSnapshot.child("loc").getValue();

            if (food_loc != null || !food_loc.equals("")) {

                try {
                    foodLoc = geocoder.getFromLocationName(food_loc, 1);
                    for (int i = 0; foodLoc.size() == 0 && i < 10; i++) {
                        foodLoc = geocoder.getFromLocationName(food_loc, 1);
                    }
                    if (foodLoc != null && foodLoc.size() > 0) {
                        address = foodLoc.get(0);

                        if(latitude.equals("")&&longitude.equals(""))
                            latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        else
                            latLng=new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                        mMap.addMarker(new MarkerOptions().position(latLng).title(food_loc));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        // Zoom in, animating the camera.
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        //  Toast.makeText(getApplication(),currentLatitude +" , "+ currentLongitude + "", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplication(),address.getLatitude() +" "+ address.getLongitude() + "", Toast.LENGTH_LONG).show();

                    } else {
                        // Toast.makeText(getBaseContext(),"Unable to find this location\n"+food_loc,Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                {


                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}












    }
    public void toGoogleMap(View view)
    {
        if(latitude.equals("")&&longitude.equals("")) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?&daddr=" + food_loc));
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?&daddr=" +latitude+","+longitude));
            startActivity(intent);
        }

    }

}


