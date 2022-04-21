package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener{
    RecyclerView foodList;
    List<Food> foods;
    List<String> names;
    List<Integer> images;
    FoodListAdaptor adaptor;
    private Button opdialog;
    private static final String SHARED_PREF_KEY = "shared_preferences";
    private static final String EVENT_LIST_KEY = "event_list_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        foodList = findViewById(R.id.food_list);

        foods = new ArrayList<>();
        loadEvents();
        names = new ArrayList<>();
        images = new ArrayList<>();

//code for recyclerview
        adaptor = new FoodListAdaptor(this, foods);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL,false);
        foodList.setLayoutManager(gridLayoutManager);
        foodList.setAdapter(adaptor);
        opdialog = (Button) findViewById(R.id.open_dialog);
        opdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void saveEvents() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String eventListString = gson.toJson(foods);
        editor.putString(EVENT_LIST_KEY, eventListString);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveEvents();
    }


    private void loadEvents(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String eventListString = sharedPreferences.getString(EVENT_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<Food>>(){}.getType();
        Gson gson = new Gson();
        foods = gson.fromJson(eventListString, type);
        if(foods == null){
            foods = new ArrayList<>();
        }
    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts(String itemName, String exYear, String exMonth, String exDay, boolean notificationOnOff, String category) {
        foods.add(new Food(itemName, R.drawable.chicken));
        adaptor.notifyItemChanged(foods.size()-1);
    }
}