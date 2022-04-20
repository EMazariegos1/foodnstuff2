package com.example.myapplication;

import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodListAdaptor extends RecyclerView.Adapter<FoodListAdaptor.ViewHolder> {
    List<String> names;
    List<Integer> images;
    List<Food> foods;
    Context context;
    LayoutInflater inflater;

    public FoodListAdaptor(Context c, List<String> name, List<Integer> image){
        context = c;
        names = name;
        images = image;
        this.inflater = LayoutInflater.from(c);
    }

    public FoodListAdaptor(Context c, List<Food> food){
        context = c;
        foods = food;
        this.inflater = LayoutInflater.from(c);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.food_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.foodImage.setImageResource(foods.get(position).getImage());
        holder.foodName.setText(foods.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO make clicking the cardview open the edit popup
                    //todo video that shows how to search items https://youtu.be/sJ-Z9G0SDhc
                    Toast.makeText(view.getContext(), "Button number "+ getAdapterPosition() + " was clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
