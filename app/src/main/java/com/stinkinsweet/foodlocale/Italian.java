package com.stinkinsweet.foodlocale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Funkies PC on 8/18/2016.
 */
public class Italian extends AppCompatActivity {
    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private String post_key,cuisine,dish,latitude,longitude;
    private RecyclerView mFoodlist;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseItalian;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> firebaseRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_south);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        setTitle("Italian ");
        progressDialog=new ProgressDialog(this);
        mFoodlist=(RecyclerView)findViewById(R.id.food_list);
        mFoodlist.setHasFixedSize(true);
        mFoodlist.setLayoutManager(new LinearLayoutManager(this));
        Firebase.setAndroidContext(this);
        databaseItalian = FirebaseDatabase.getInstance().getReference().child("FoodInfo").child("Cuisine").child("Italian");


    }

    @Override
    protected void onStart() {
        super.onStart();
        // if (spinner.getSelectedItem().toString().equals("Cuisine :")){
        progressDialog.setMessage("Italian dishes on the way...");
        progressDialog.show();

      firebaseRecyclerViewAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>
                (Food.class,
                        R.layout.food_row,
                        FoodViewHolder.class, databaseItalian

                ) {


            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder,final Food model,final int position) {



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

                            post_key = getRef(position).getKey();
                            cuisine=model.getCuisine();
                            dish=model.getDish();
                            latitude=model.getLatitude();
                            longitude=model.getLongitude();
                            Intent i = new Intent(getApplication(), MapsActivity.class);
                            i.putExtra("post_key", post_key);
                            i.putExtra("cuisine_name",cuisine);
                            i.putExtra("dish_name",dish);
                            i.putExtra("latitude",latitude);
                            i.putExtra("longitude",longitude);

                            startActivity(i);
                        } else
                            Toast.makeText(getBaseContext(), "Turn ON Location", Toast.LENGTH_SHORT).show();


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
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public FoodViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setDish(String dish)
        {
            TextView food_dish=(TextView)mView.findViewById(R.id.food_dish);
            food_dish.setText(dish);
        }

        public void setPlace(String place)
        {
            TextView food_place=(TextView)mView.findViewById(R.id.food_place);
            food_place.setText(place);
        }

        public void setLoc(String loc)
        {
            TextView food_loc=(TextView)mView.findViewById(R.id.food_loc);
            food_loc.setText(loc);
        }

        public void setName(String name)
        {
            TextView food_name=(TextView)mView.findViewById(R.id.food_name);
            food_name.setText(name);
        }

        public void setDesc(String desc)
        {
            TextView food_desc=(TextView)mView.findViewById(R.id.food_desc);
            food_desc.setText(desc);
        }




    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
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
            startActivity(new Intent(Italian.this,Login.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
