package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener, ExampleDialogDeleteBecauseEricWantedIt.ExampleDialogListener2{
    RecyclerView foodList;
    List<Food> foods;
    FoodListAdaptor adaptor;
    private Button opdialog;
    Integer[] images;
    Spinner sortingItems;
    private static final String SHARED_PREF_KEY = "shared_preferences";
    private static final String EVENT_LIST_KEY = "event_list_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        foodList = findViewById(R.id.food_list);
        sortingItems = findViewById(R.id.sortItems);

        foods = new ArrayList<>();
        loadEvents();

        images = new Integer[]{R.drawable.ic_baseline_add_photo_alternate, R.drawable.meat, R.drawable.vegetable, R.drawable.fruit, R.drawable.dairy, R.drawable.pastry, R.drawable.condiments};

//code for recyclerview
        adaptor = new FoodListAdaptor(this, foods);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        foodList.setLayoutManager(gridLayoutManager);
        foodList.setAdapter(adaptor);
        opdialog = (Button) findViewById(R.id.open_dialog);
        opdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        sortingItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                String category = (String) adapterView.getItemAtPosition(pos);
                foods.clear();
                for (int i = 0; i < adaptor.foodsFull.size(); i++) {
                    if(category.equals(adaptor.foodsFull.get(i).getType())){
                        foods.add(adaptor.foodsFull.get(i));
                    }
                    else if(category.equals("All")){
                        foods.clear();
                        foods.addAll(adaptor.foodsFull);
                    }
                    else if (category.equals("No Category") && adaptor.foodsFull.get(i).getType().equals("Categories")){
                        foods.add(adaptor.foodsFull.get(i));
                    }
                }
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
    public void openEditDialog(Food food, int position, Context c){
        ExampleDialogDeleteBecauseEricWantedIt exampleDialogDeleteBecauseEricWantedIt = new ExampleDialogDeleteBecauseEricWantedIt(food, position, c);
        exampleDialogDeleteBecauseEricWantedIt.show(getSupportFragmentManager(), "example dialoge");
    }

    @Override
    public void applyTexts(String itemName, String exYear, String exMonth, String exDay, boolean notificationOnOff, String category) {
        Integer image = R.drawable.meat;
        Resources res = getResources();
        String[] types = res.getStringArray(R.array.Category);

        for (int i = 0; i < types.length; i++) {
            if(category.equals(types[i])){
                image = images[i];
            }
        }

        Food item = new Food(itemName, exMonth, exDay, exYear, category, image, notificationOnOff);

        foods.add(item);
        adaptor.foodsFull.add(item);
        adaptor.notifyItemChanged(foods.size()-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.food_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptor.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void applyTexts2(String foodName, String exYear, String exMonth, String exDay, boolean notificationOnOff, String category, int p) {
        Integer image = foods.get(p).getImage();
        Resources res = getResources();
        String[] types = res.getStringArray(R.array.Category);

        for (int i = 0; i < types.length; i++) {
            if(category.equals(types[i])){
                image = images[i];
            }
        }

        foods.get(p).setImage(image);
        foods.get(p).setName(foodName);
        foods.get(p).setNotification(notificationOnOff);
        foods.get(p).setType(category);
        foods.get(p).setxDay(exDay);
        foods.get(p).setxMonth(exMonth);
        foods.get(p).setxYear(exYear);


      adaptor.foodsFull.get(p).setImage(image);
        adaptor.foodsFull.get(p).setName(foodName);
        adaptor.foodsFull.get(p).setNotification(notificationOnOff);
        adaptor.foodsFull.get(p).setType(category);
       adaptor.foodsFull.get(p).setxDay(exDay);
        adaptor.foodsFull.get(p).setxMonth(exMonth);
        adaptor.foodsFull.get(p).setxYear(exYear);


        adaptor.notifyItemChanged(foods.size()-1);
    }
}