package com.stinkinsweet.foodlocale;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Funkies PC on 8/18/2016.
 */
public class Find extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final long LOCATION_REFRESH_TIME = 60000;
    private static final float LOCATION_REFRESH_DISTANCE = 200;
    LocationListener mLocationListener;
    private Location mlocation = null,currentLoc=null;
    private Criteria criteria;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GeoFire geofire;
    private GeoQuery geoquery;
    private DatabaseReference databaseAll;
    private String choice;
    private boolean voiceInput=false;

private String currentCuisine;

    private Context context;
    LocationManager mLocationManager;
    private TextView txtFind;
    private FirebaseAuth firebaseAuth;
    private ArrayAdapter<CharSequence> adapterSpinner;
    private Toolbar toolbar;
    private RecyclerView mFoodlist;
    private DatabaseReference mDatabase,databaseGeoFire,databaseItalian,mainDatabase,databaseIndian,databaseChinese,databaseSouth;
    private Spinner spinner;
    private String post_key, cuisine, dish, lat, lon,name,place,loc,desc;
    private int x;
    private Context ctx;
    private Firebase firebase;
    private ProgressDialog progressDialog;
    private ImageButton btnVoice;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Food> food;
    LocationManager newLocationManager;
    LocationListener newLocationListener;
    private int i=0;
private boolean populated=false;
    private static final int SPEECH_REQUEST_CODE = 0;
    private HashMap<String, String> foodMap = null;

    //  String[] cuisineArray={"Chinese","Italian","South Indian"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("All");
        databaseGeoFire = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("GeoFire");
        databaseItalian = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("Italian");
        databaseIndian = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("Indian");
        databaseSouth = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("South Indian");
        databaseChinese = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("Chinese");



        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        recyclerView = (RecyclerView) findViewById(R.id.food_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//FIREBASE UI
        //   mFoodlist=(RecyclerView)findViewById(R.id.food_list);
        //  mFoodlist.setHasFixedSize(true);
        //  mFoodlist.setLayoutManager(new LinearLayoutManager(this));
        Firebase.setAndroidContext(this);
//firebase=new Firebase("https://foodlocale.firebaseio.com/");
        btnVoice=(ImageButton)findViewById(R.id.btnVoice);
        txtFind = (TextView) findViewById(R.id.txtFind);
        final Handler handler = new Handler();

      /*  s = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cuisineArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(this);*/


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }



        spinner = (Spinner) findViewById(R.id.spinner);

        adapterSpinner = ArrayAdapter.createFromResource(this, R.array.cuisinesFind, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (!(spinner.getSelectedItem().toString().equals("All"))) {

                    if (spinner.getSelectedItem().toString().equals("Indian")) {
                        Intent f1 = new Intent(Find.this, Indian.class);
                        startActivity(f1);

                    }

                    if (spinner.getSelectedItem().toString().equals("Indian Chinese")) {
                        Intent f1 = new Intent(Find.this, Chinese.class);
                        startActivity(f1);

                    } else if (spinner.getSelectedItem().toString().equals("Italian")) {
                        Intent f1 = new Intent(Find.this, Italian.class);
                        startActivity(f1);

                    } else if (spinner.getSelectedItem().toString().equals("South Indian")) {
                        Intent f1 = new Intent(Find.this, South.class);
                        startActivity(f1);

                    }

                }


               /* if (parent.getItemAtPosition(i).toString() == "Chinese") {
                    Intent f1 = new Intent(Find.this, Chinese.class);
                    startActivity(f1);
                }
                if (parent.getItemAtPosition(i).toString() == "Italian") {
                    Intent f1 = new Intent(Find.this, Chinese.class);
                    startActivity(f1);
                }
                if (parent.getItemAtPosition(i).toString() == "South Indian") {
                    Intent f1 = new Intent(Find.this, Chinese.class);
                    startActivity(f1);
                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        // Drop down layout style - list view with radio button

        foodMap = new HashMap<>();
        foodMap.put(Soundex.soundex("Indian"),"Indian");
        foodMap.put(Soundex.soundex("Italian"),"Italian");
        foodMap.put(Soundex.soundex("Chinese"),"Chinese");
        foodMap.put(Soundex.soundex("South Indian"),"South Indian");

    }

    @Override
    protected void onStart() {
        super.onStart();
        while (i<=10)
        {
            mGoogleApiClient.connect();
            i++;
        }// if (spinner.getSelectedItem().toString().equals("Cuisine :")){
        progressDialog.setMessage("Loading dishes...");
        progressDialog.show();




        /*
            firebaseRecyclerViewAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>
                    (Food.class,
                            R.layout.food_row,
                            FoodViewHolder.class, mDatabase

                    ) {


                @Override
                protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, final int position) {



                    viewHolder.setDish(model.getDish());
                    viewHolder.setPlace(model.getPlace());
                    viewHolder.setLoc(model.getLoc());
                    viewHolder.setName(model.getName());
                    viewHolder.setDesc(model.getDesc());


                    viewHolder.setDish(model.getDish());
                    viewHolder.setPlace(model.getPlace());
                    viewHolder.setLoc(model.getLoc());
                    viewHolder.setName(model.getName());
                    viewHolder.setDesc(model.getDesc());


                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {



    if (isLocationEnabled(getBaseContext()) == true) {
        post_key = firebaseRecyclerViewAdapter.getRef(position).getKey();
        cuisine = model.getCuisine();
        dish = model.getDish();
        latitude = model.getLatitude();
        longitude = model.getLongitude();
        Intent i = new Intent(getApplication(), MapsActivity.class);
        i.putExtra("post_key", post_key);
        i.putExtra("cuisine_name", cuisine);
        i.putExtra("dish_name", dish);
        i.putExtra("latitude", latitude);
        i.putExtra("longitude", longitude);
        startActivity(i);

    } else {
        Toast.makeText(getBaseContext(), "Turn ON Location", Toast.LENGTH_SHORT).show();
    }

                        }
                    });
                }


            };
            mFoodlist.setAdapter(firebaseRecyclerViewAdapter);

            RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    progressDialog.dismiss();
                }

                @Override
                public void onChanged() {
                    //called When the data in the adapter is changed
                }
            };

            firebaseRecyclerViewAdapter.registerAdapterDataObserver(observer);

*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
            currentLoc=mLastLocation;
           mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                   mGoogleApiClient);
      //  Toast.makeText(context, mLastLocation.getLatitude()+" : "+mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        populateRecycler(mLastLocation);

     /*   final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(adapter.getItemCount()==0) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Could not fetch your location", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, 15000);
        */



    }
public void populateRecycler(Location mLastLocation)
{
    populated=true;
    food = new ArrayList<>();
    geofire = new GeoFire(databaseGeoFire);
    if(mLastLocation!=null) {
        geoquery = geofire.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15);


        geoquery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                //      Toast.makeText(getApplicationContext(), key+" : "+location.latitude, Toast.LENGTH_SHORT).show();
                mainDatabase=mDatabase;
                if(voiceInput==true)
                {
                    switch(choice)
                    {
                        case "Italian":
                        {
                            mainDatabase=databaseItalian;
                            break;
                        }
                        case "Indian":
                        {
                            mainDatabase=databaseIndian;
                            break;
                        }
                        case "Chinese":
                        {
                            mainDatabase=databaseChinese;
                            break;
                        }
                        case "SouthIndian":
                        {
                            mainDatabase=databaseSouth;
                            break;
                        }

                    }

                }
                mainDatabase.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(voiceInput==true) {
                           // Toast.makeText(Find.this, choice, Toast.LENGTH_SHORT).show();
                            post_key = (String) dataSnapshot.child("key").getValue();
                            cuisine = (String) dataSnapshot.child("cuisine").getValue();
                            dish = (String) dataSnapshot.child("dish").getValue();
                            lat = (String) dataSnapshot.child("latitude").getValue();
                            lon = (String) dataSnapshot.child("longitude").getValue();
                            place = (String) dataSnapshot.child("place").getValue();
                            desc = (String) dataSnapshot.child("desc").getValue();
                            name = (String) dataSnapshot.child("name").getValue();

                            if(cuisine!=null) {
                                Food foodNew = new Food(dish, place, loc, desc, name, cuisine, lat, lon, post_key);
                                food.add(foodNew);
                            }
                            adapter.notifyDataSetChanged();


                        }
                        else
                        {
                       // Toast.makeText(Find.this, choice + "Nahh", Toast.LENGTH_SHORT).show();
                        post_key = (String) dataSnapshot.child("key").getValue();
                        cuisine = (String) dataSnapshot.child("cuisine").getValue();
                        dish = (String) dataSnapshot.child("dish").getValue();
                        lat = (String) dataSnapshot.child("latitude").getValue();
                        lon = (String) dataSnapshot.child("longitude").getValue();
                        place = (String) dataSnapshot.child("place").getValue();
                        desc = (String) dataSnapshot.child("desc").getValue();
                        name = (String) dataSnapshot.child("name").getValue();


                        Food foodNew = new Food(dish, place, loc, desc, name, cuisine, lat, lon, post_key);
                        food.add(foodNew);

                        adapter.notifyDataSetChanged();

                    }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                adapter = new MyAdapter(food, getBaseContext());
                final Handler handler = new Handler();

                recyclerView.setAdapter(adapter);
                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });

    }


}

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logOut) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(Find.this, Login.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {


    }


    /*
        s.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=((TextView)view).getText().toString();

                if( item.equals("Chinese"))
                {
                    Intent f1 = new Intent(Find.this, Chinese.class);
                    startActivity(f1);
                }

                if( item.equals("Italian"))
                {
                    Intent f2=new Intent(Find.this,Italian.class);
                    startActivity(f2);
                }

                if( item.equals("South Indian"))
                {   Intent f3=new Intent(Find.this,South.class);
                    startActivity(f3);
                }

                }

        });*/

    @Override
    protected void onPostResume() {
        super.onPostResume();
if(spinner!=null)
        spinner.setSelection(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    public void startVoice(View v)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            //String spokenText = results.get(0);
            StringBuilder sb = new StringBuilder(1024);
            boolean found = false;
            //  sb.append("Words Detected are\n");
            for (String i : results) {
                //sb.append(i +"\n");
                sb.append(i);

                String key = Soundex.soundex(i);
                if (foodMap.containsKey(key)) {
                 /*   if (mp != null && mp.isPlaying())
                        mp.stop();

                    mp = MediaPlayer.create(getBaseContext(), Uri.parse(path + "/" + songsMap.get(key)));
                    mp.start();
                    seekUpdation();
                    */
                    choice=foodMap.get(key);
                    voiceInput=true;
                    populateRecycler(mLastLocation);
                    found = true;
                    break;
                }


            }
            if (!found) {
                Toast.makeText(this, "Oops! I did'nt understood that", Toast.LENGTH_SHORT).show();
                return;
                //Button btn=(Button)findViewById(R.id.btnVoice);
                // btn.performClick();
            }
           // Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}

