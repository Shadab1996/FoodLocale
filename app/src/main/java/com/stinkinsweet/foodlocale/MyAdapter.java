package com.stinkinsweet.foodlocale;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.stinkinsweet.foodlocale.Find.isLocationEnabled;

/**
 * Created by Funkies PC on 01-Mar-17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    List<Food> food;
    Context context;

    String post_key,cuisine,dish,latitude,longitude;
    public MyAdapter(List<Food> food, Context context) {
        this.food = food;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_row_2,parent,false);

        return  new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Food food1=food.get(position);
        holder.txtDishName.setText(food1.getDish());
        holder.txtPlace.setText(food1.getPlace());
        holder.txtName.setText(food1.getName());
        holder.txtLoc.setText(food1.getLoc());
        holder.txtDesc.setText(food1.getDesc());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, food1.getName(), Toast.LENGTH_SHORT).show();

                    post_key = food1.getKey();
                    cuisine=food1.getCuisine();
                    dish=food1.getDish();
                    latitude=food1.getLatitude();
                    longitude=food1.getLongitude();
                    Intent i = new Intent(context, MapsActivity.class);
                    i.putExtra("post_key", post_key);
                    i.putExtra("cuisine_name",cuisine);
                    i.putExtra("dish_name",dish);
                    i.putExtra("latitude",latitude);
                    i.putExtra("longitude",longitude);
                    context.startActivity(i);




            }
        });

    }
    /*  i.putExtra("post_key", post_key);
               i.putExtra("cuisine_name", cuisine);
               i.putExtra("dish_name", dish);
               i.putExtra("latitude", latitude);
               i.putExtra("longitude", longitude);
   */
    @Override
    public int getItemCount() {
        return food.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDishName,txtPlace,txtLoc,txtName,txtDesc;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);



            txtDishName=(TextView)itemView.findViewById(R.id.food_dish);
            txtPlace=(TextView)itemView.findViewById(R.id.food_place);
            txtLoc=(TextView)itemView.findViewById(R.id.food_loc);
            txtName=(TextView)itemView.findViewById(R.id.food_name);
            txtDesc=(TextView)itemView.findViewById(R.id.food_desc);

            linearLayout=(LinearLayout)itemView.findViewById(R.id.linearLayout);
        }
    }

}
