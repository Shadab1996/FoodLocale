package com.stinkinsweet.foodlocale;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.stinkinsweet.foodlocale.Find.isLocationEnabled;

/**
 * Created by Funkies PC on 8/18/2016.
 */
public class Second extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private Button btnAdd,btnFind;
    private TextView txtWelcome,txtOr;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        firebaseAuth=FirebaseAuth.getInstance();

        setContentView(R.layout.activity_second);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        btnAdd=(Button)findViewById(R.id.btnAdd);

        btnFind=(Button)findViewById(R.id.btnFind);
        txtWelcome=(TextView)findViewById(R.id.txtWelcome);
        txtOr=(TextView)findViewById(R.id.txtOr);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getEmail();
        int i=name.indexOf("@");
        txtWelcome.setText("Welcome "+name.substring(0,i));
        btnAdd.setOnClickListener(new View.OnClickListener(){
           public void onClick(View arg0)
           {
               Intent s1=new Intent(Second.this,Add.class);
               startActivity(s1);
           }
        });
        btnFind.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View arg0)
            {
                if(prefs.getBoolean("firstTime", false))
                {
                    if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant

                        return;
                    }
                }

                if (!prefs.getBoolean("firstTime", false)) {
                    // <---- run your one time code here


                    if (isLocationEnabled(getBaseContext()) == true) {
                        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant

                            return;
                        }

                    } else {
                        Toast.makeText(Second.this, "Turn on LOCATION", Toast.LENGTH_SHORT).show();


                    }


                    // mark first time has runned.

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstTime", true);
                    editor.commit();

                }

                if(prefs.getBoolean("firstTime", false))
                {
                    if (isLocationEnabled(getBaseContext()) == true)
                    {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent s2 = new Intent(Second.this, Find.class);
                                startActivity(s2);
                                // Do something after 5s = 5000ms
                            }
                        }, 3000);
                    }
                        else
                    Toast.makeText(Second.this, "Turn on LOCATION", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            startActivity(new Intent(Second.this,Login.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent s2 = new Intent(Second.this, Find.class);
                            startActivity(s2);
                            // Do something after 5s = 5000ms
                        }
                    }, 3000);

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {
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
                                    MY_PERMISSIONS_REQUEST_LOCATION);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }


                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }



}