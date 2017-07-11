package com.stinkinsweet.foodlocale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    private  EditText txtName,txtPlace,txtDish,txtDesc,txtLoc;
    private TextView txtAdd;
    private Button btnAdd;
    private  String name,place,loc,dish,desc;
    private  Spinner spinnerAdd;
    private  FirebaseAuth firebaseAuth;
    private  GeoFire geoFire;
    private String LatLong,latitude="",longitude="";
    private ArrayAdapter<CharSequence> adapterAdd;
    private Toolbar toolbar;
    private String MyKey;
    private DatabaseReference databaseAll,databaseIndian,databaseItalian,databaseSouth,databaseChinese,databaseGeoFire,newAll;
    int PLACE_PICKER_REQUEST = 1;
    int i=0;
    Place placee;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        databaseGeoFire = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("GeoFire");
        geoFire = new GeoFire(databaseGeoFire);

        txtName = (EditText) findViewById(R.id.txtName);
        txtPlace = (EditText) findViewById(R.id.txtPlace);
        txtDish = (EditText) findViewById(R.id.txtDish);
        txtDesc = (EditText) findViewById(R.id.txtDesc);
        txtLoc = (EditText) findViewById(R.id.txtLoc);

        txtAdd = (TextView) findViewById(R.id.txtAdd);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        databaseAll = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("All");
        databaseIndian = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("Indian");
        databaseItalian = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("Italian");
        databaseSouth = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("South Indian");
        databaseChinese = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("Chinese");

        txtName = (EditText) findViewById(R.id.txtName);


        spinnerAdd = (Spinner) findViewById(R.id.spinnerAdd);

        adapterAdd = ArrayAdapter.createFromResource(this, R.array.cuisines, android.R.layout.simple_spinner_item);
        adapterAdd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdd.setAdapter(adapterAdd);

        spinnerAdd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (!(spinnerAdd.getSelectedItem().toString().equals("Cuisine:"))) {

                    if (spinnerAdd.getSelectedItem().toString().equals("Chinese")) {


                    } else if (spinnerAdd.getSelectedItem().toString().equals("Italian")) {


                    } else if (spinnerAdd.getSelectedItem().toString().equals("South Indian")) {


                    }

                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void autoComplete(View view) {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder().setLatLngBounds( BOUNDS_INDIA);

            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
//PLACE_AUTOCOMPLETE_REQUEST_CODE is integer for request code
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

            private void saveUserInformation()
    {
        String name=txtName.getText().toString().trim();
        String dish=txtDish.getText().toString().trim();
        String place=txtPlace.getText().toString().trim();
        String loc=txtLoc.getText().toString().trim();
        String desc=txtDesc.getText().toString().trim();
        String cuisine=spinnerAdd.getSelectedItem().toString();

if(cuisine.equals("Others"))
    cuisine="All";

            UserInformation userInformation = new UserInformation(name, dish, place, loc, desc,cuisine,latitude,longitude);


        FirebaseUser user=firebaseAuth.getCurrentUser();

        if(cuisine.equals("Indian")) {

            String MyKey = databaseIndian.push().getKey();
            databaseIndian.child(MyKey).setValue(userInformation);
            DatabaseReference newIndian=databaseIndian.child(MyKey).child("key");
            newIndian.setValue(MyKey);
        }
        else
        if(cuisine.equals("Italian")) {
            String MyKey = databaseItalian.push().getKey();
            databaseItalian.child(MyKey).setValue(userInformation);
            DatabaseReference newItalian = databaseItalian.child(MyKey).child("key");
            newItalian.setValue(MyKey);
        }
        else
        if(cuisine.equals("South Indian"))
        {
            String MyKey = databaseSouth.push().getKey();
        databaseSouth.child(MyKey).setValue(userInformation);
        DatabaseReference newSouth = databaseSouth.child(MyKey).child("key");
        newSouth.setValue(MyKey);
        }
        else
        if(cuisine.equals("Indian Chinese"))
        {
            String MyKey = databaseChinese.push().getKey();
            databaseChinese.child(MyKey).setValue(userInformation);
            DatabaseReference newChinese = databaseChinese.child(MyKey).child("key");
            newChinese.setValue(MyKey);
        }

        if(cuisine.equals("Indian")||cuisine.equals("Italian")||cuisine.equals("South Indian")||cuisine.equals("Indian Chinese")||cuisine.equals("All")) {
            userInformation.cuisine = "All";
            MyKey = databaseAll.push().getKey();
            databaseAll.child(MyKey).setValue(userInformation);
            newAll = databaseAll.child(MyKey).child("key");
            newAll.setValue(MyKey);
        }
            geoFire.setLocation(MyKey, new GeoLocation(Double.parseDouble(latitude), Double.parseDouble(longitude)));

    }

    private void saveUserInformation1()
    {
        String name=txtName.getText().toString().trim();
        String dish=txtDish.getText().toString().trim();
        String place=txtPlace.getText().toString().trim();
        String loc=txtLoc.getText().toString().trim();
        String desc=txtDesc.getText().toString().trim();
        String cuisine=spinnerAdd.getSelectedItem().toString();

        if(cuisine.equals("Others"))
            cuisine="All";

        UserInformation userInformation = new UserInformation(name, dish, place, loc, desc,cuisine,latitude,longitude);

        userInformation.cuisine = "All";
        MyKey = databaseAll.push().getKey();
        databaseAll.child(MyKey).setValue(userInformation);
        newAll = databaseAll.child(MyKey).child("key");
        newAll.setValue(MyKey);

        FirebaseUser user=firebaseAuth.getCurrentUser();

        if(cuisine.equals("Indian")) {
            userInformation.cuisine = "Indian";
           // String MyKey = databaseIndian.push().getKey();
            databaseIndian.child(MyKey).setValue(userInformation);
            DatabaseReference newIndian=databaseIndian.child(MyKey).child("key");
            newIndian.setValue(MyKey);
        }
        else
        if(cuisine.equals("Italian")) {
            userInformation.cuisine = "Italian";
            //String MyKey = databaseItalian.push().getKey();
            databaseItalian.child(MyKey).setValue(userInformation);
            DatabaseReference newItalian = databaseItalian.child(MyKey).child("key");
            newItalian.setValue(MyKey);
        }
        else
        if(cuisine.equals("South Indian"))
        {
            userInformation.cuisine = "South Indian";
           // String MyKey = databaseSouth.push().getKey();
            databaseSouth.child(MyKey).setValue(userInformation);
            DatabaseReference newSouth = databaseSouth.child(MyKey).child("key");
            newSouth.setValue(MyKey);
        }
        else
        if(cuisine.equals("Indian Chinese"))
        {
            userInformation.cuisine = "Indian Chinese";
           // String MyKey = databaseChinese.push().getKey();
            databaseChinese.child(MyKey).setValue(userInformation);
            DatabaseReference newChinese = databaseChinese.child(MyKey).child("key");
            newChinese.setValue(MyKey);
        }

        /*if(cuisine.equals("Indian")||cuisine.equals("Italian")||cuisine.equals("South Indian")||cuisine.equals("Indian Chinese")||cuisine.equals("All")) {
            userInformation.cuisine = "All";
            MyKey = databaseAll.push().getKey();
            databaseAll.child(MyKey).setValue(userInformation);
            newAll = databaseAll.child(MyKey).child("key");
            newAll.setValue(MyKey);
        }*/
        geoFire.setLocation(MyKey, new GeoLocation(Double.parseDouble(latitude), Double.parseDouble(longitude)));

    }

            public void Add(View view) {





                name=txtName.getText().toString();
                dish=txtDish.getText().toString();
                place=txtPlace.getText().toString();
                loc=txtLoc.getText().toString();
                desc=txtDesc.getText().toString();


                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                }else

                if (dish.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter name of the Dish", Toast.LENGTH_SHORT).show();
                }else
                if(spinnerAdd.getSelectedItem().toString().equals("Cuisine :"))
                {
                    Toast.makeText(getApplicationContext(), "Select Cuisine", Toast.LENGTH_SHORT).show();
                }
                else
                if (place.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Place", Toast.LENGTH_SHORT).show();
                }else
                if (loc.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Location", Toast.LENGTH_SHORT).show();
                }
                else

                if(!name.isEmpty()&&!place.isEmpty()&&!loc.isEmpty()&&!dish.isEmpty()) {

                     saveUserInformation1();
                    Toast.makeText(getApplicationContext(), "Thank You "+name+"\nDish Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent a=new Intent(Add.this,Second.class);
                    startActivity(a);
                }

        }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
            placee = PlacePicker.getPlace(this,data);
                LatLong =  placee.getLatLng().toString();
                if(LatLong.equals(null))
                {
                    LatLong="";
                }
                int start = LatLong.indexOf("(");
                int comma = LatLong.indexOf(",");
                int end = LatLong.indexOf(")");
                latitude=LatLong.substring(start+1,comma);
                longitude=LatLong.substring(comma+1,end);


txtLoc.setText(placee.getAddress().toString());
            }
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
            startActivity(new Intent(Add.this,Login.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }




}