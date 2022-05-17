package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener, ExampleDialogDeleteBecauseEricWantedIt.ExampleDialogListener2, DatePickerDialog.OnDateSetListener{
    RecyclerView foodList;
    List<Food> foods;
    FoodListAdaptor adaptor;
    private Button opdialog;
    Integer[] images;
    TextView tv;
    int year, day, month,year2, day2, month2, id;
    private static final String SHARED_PREF_KEY = "shared_preferences";
    private static final String EVENT_LIST_KEY = "event_list_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        foodList = findViewById(R.id.food_list);
        foods = new ArrayList<>();
        loadEvents();
        tv = findViewById(R.id.date);
        images = new Integer[]{R.drawable.ic_baseline_add_photo_alternate, R.drawable.meat, R.drawable.vegetable, R.drawable.fruit, R.drawable.dairy, R.drawable.pastry, R.drawable.condiments};

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
    //TODO Maybe add a int or boolean to differentiate between Expiration and Time placed. Not sure
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(id == 1) {
            this.year = year;
            this.month = month;
            this.day = dayOfMonth;
        }
        else{
            this.year2 = year;
            this.month2 = month;
            this.day2 = dayOfMonth;
        }
    }

    private void saveEvents() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String eventListString = gson.toJson(foods);
        editor.putString(EVENT_LIST_KEY, eventListString);
        editor.apply();
    }
    public void onClick(View v) {
        id = 1;
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }
    public void onClick2(View v) {
        id = 2;
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker 2");
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
        ExampleDialog exampleDialog = new ExampleDialog(this);
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
    public void openEditDialog(Food food, int position, Context c){
        ExampleDialogDeleteBecauseEricWantedIt exampleDialogDeleteBecauseEricWantedIt = new ExampleDialogDeleteBecauseEricWantedIt(food, position, c);
        exampleDialogDeleteBecauseEricWantedIt.show(getSupportFragmentManager(), "example dialoge");
    }

    @Override
    public void applyTexts(String itemName, String exYear, String exMonth, String exDay, boolean notificationOnOff, String category, String quantity) {
        Integer image = R.drawable.meat;
        Resources res = getResources();
        String[] types = res.getStringArray(R.array.Category);

        for (int i = 0; i < types.length; i++) {
            if(category.equals(types[i])){
                image = images[i];
            }
        }

        Food item = new Food(itemName, month+"", day+"", year+"", category, image, notificationOnOff, quantity);

        foods.add(item);
        adaptor.foodsFull.add(item);
        adaptor.notifyItemChanged(foods.size()-1);
        tv.setText(month + "/"+ day +"/" +year);

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
    public void applyTexts2(String foodName, String exYear, String exMonth, String exDay, boolean notificationOnOff, String category, int p, String quantityFood) {
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
        foods.get(p).setxDay(day2+"");
        foods.get(p).setxMonth(month2+"");
        foods.get(p).setxYear(year2+"");


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