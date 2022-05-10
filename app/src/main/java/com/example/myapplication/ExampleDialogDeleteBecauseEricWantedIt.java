package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class ExampleDialogDeleteBecauseEricWantedIt extends AppCompatDialogFragment {
    private EditText foodItemName, yearEd;
    private Spinner exMonthSpinner, exDaySpinner, categorySpinner;
    private Switch notificationSwitch;
    private ExampleDialogDeleteBecauseEricWantedIt.ExampleDialogListener2 listener;
    Food food;
    int position;
    Context context;
    String[] categoryChoices, months, days;


    public ExampleDialogDeleteBecauseEricWantedIt(Food food, int position, Context c) {
      this.food = food;
      this.position = position;
      this.context = c;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("New Item")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = foodItemName.getText().toString();
                        String exYear = yearEd.getText().toString();
                        String exMonth = exMonthSpinner.getSelectedItem().toString();
                        String exDay = exDaySpinner.getSelectedItem().toString();
                        String category = categorySpinner.getSelectedItem().toString();
                        boolean notificationOnOFf = notificationSwitch.isChecked();
                        listener.applyTexts2(username, exYear, exMonth, exDay, notificationOnOFf, category, position);
                    }
                }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((MainActivity)context).foods.remove(position);
                ((MainActivity)context).adaptor.foodsFull.remove(position);
                ((MainActivity)context).adaptor.notifyItemRemoved(position);
            }
        });

        Resources res = getResources();
        categoryChoices = res.getStringArray(R.array.Category);
        months = res.getStringArray(R.array.Month);
        days = res.getStringArray(R.array.Day);

        foodItemName = view.findViewById(R.id.food_name);
        notificationSwitch = view.findViewById(R.id.notification_switch);
        categorySpinner = view.findViewById(R.id.category_spinner);

        setItems();

        return builder.create();
    }

    public void setItems(){
        for (int i = 0; i < categoryChoices.length; i++) {
            if(food.getType().equals(categoryChoices[i])){
                categorySpinner.setSelection(i);
            }
        }

        for (int i = 0; i < months.length; i++) {
            if(food.getxMonth().equals(months[i])){
                exMonthSpinner.setSelection(i);
            }
        }

        for (int i = 0; i < days.length; i++) {
            if(food.getxDay().equals(days[i])){
                exDaySpinner.setSelection(i);
            }
        }

        foodItemName.setText(food.getName());
        yearEd.setText(food.getxYear());
        notificationSwitch.setChecked(food.isNotification());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogDeleteBecauseEricWantedIt.ExampleDialogListener2) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
//TODO Figured it out, use a listener in mainactivity to send data right here and set it to the text for date. WEEWOOOO.

    public interface ExampleDialogListener2 {
        void applyTexts2(String foodName, String exYear, String exMonth, String exDay, boolean notificationOnOff, String category, int p);
    }
}
