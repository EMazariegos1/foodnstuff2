package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
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
import java.util.Calendar;
import java.util.Date;
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
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private int NOTIFICATION_ID;
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";
    private NotificationReceiver mReceiver = new NotificationReceiver();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        foodList = findViewById(R.id.food_list);
        sortingItems = findViewById(R.id.sortItems);

        prefs = getSharedPreferences(Activity.class.getSimpleName(), Context.MODE_PRIVATE);
        NOTIFICATION_ID = prefs.getInt("notificationNumber", 0);

        registerReceiver(mReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION));

        foods = new ArrayList<>();
        loadEvents();

        images = new Integer[]{R.drawable.ic_baseline_add_photo_alternate, R.drawable.meat, R.drawable.vegetable, R.drawable.fruit, R.drawable.dairy, R.drawable.pastry, R.drawable.condiments, R.drawable.bev, R.drawable.leftover};

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

        createNotificationChannel();
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

        if (notificationOnOff){
            if(itemName.equals("")){
                sendNotification("food");
            }
            else {
                sendNotification(itemName);
            }
        }

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

    //code for notification from here down
    public void sendNotification(String content) {
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(content);
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        SharedPreferences.Editor editor = prefs.edit();
        NOTIFICATION_ID++;
        editor.putInt("notificationNumber", NOTIFICATION_ID);
        editor.commit();
    }

    public void createNotificationChannel(){
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(String content){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Expiration Notification")
                .setContentText("your " +content+ " is going to expire soon")
                .setSmallIcon(R.mipmap.ic_launcher2_round)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setDeleteIntent(notificationPendingIntent);
        return notifyBuilder;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }
}