package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView foodList;
    List<String> names;
    List<Integer> images;
    FoodListAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        foodList = findViewById(R.id.food_list);

        names = new ArrayList<>();
        images = new ArrayList<>();

        names.add("one");
        names.add("two");
        names.add("three");
        names.add("four");
        names.add("four");
        names.add("four");
        names.add("four");
        names.add("four");

        images.add(R.drawable.chicken);
        images.add(R.drawable.chicken);
        images.add(R.drawable.chicken);
        images.add(R.drawable.chicken);
        images.add(R.drawable.chicken);
        images.add(R.drawable.chicken);
        images.add(R.drawable.chicken);
        images.add(R.drawable.chicken);
//code for recyclerview
        adaptor = new FoodListAdaptor(this, names, images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL,false);
        foodList.setLayoutManager(gridLayoutManager);
        foodList.setAdapter(adaptor);
    }
}