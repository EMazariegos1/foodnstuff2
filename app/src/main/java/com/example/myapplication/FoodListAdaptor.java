package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FoodListAdaptor extends RecyclerView.Adapter<FoodListAdaptor.ViewHolder> implements Filterable {
    List<Food> foods;
    List<Food> foodsFull;
    Context context;
    LayoutInflater inflater;

    public FoodListAdaptor(Context c, List<Food> food){
        context = c;
        foods = food;
        foodsFull = new ArrayList<>(foods);
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

    @Override
    public Filter getFilter() {
        return foodsFilter;
    }

    Filter foodsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Food> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(foodsFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Food item: foodsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            foods.clear();
            foods.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder implements ExampleDialogDeleteBecauseEricWantedIt.ExampleDialogListener2 {
        ImageView foodImage;
        TextView foodName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//todo video that shows how to search items https://youtu.be/sJ-Z9G0SDhc
                    ((MainActivity)context).openEditDialog(foods.get(getAdapterPosition()), getAdapterPosition(), context);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((MainActivity)context).foods.remove(getAdapterPosition());
                    ((MainActivity)context).adaptor.foodsFull.remove(getAdapterPosition());
                    ((MainActivity)context).adaptor.notifyItemRemoved(getAdapterPosition());
                    return true;
                }
            });
        }

        @Override
        public void applyTexts2(String foodName, String exYear, String exMonth, String exDay, boolean notificationOnOff, String category, int p) {

        }
    }
}
