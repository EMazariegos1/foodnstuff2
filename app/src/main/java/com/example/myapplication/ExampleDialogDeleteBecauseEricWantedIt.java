package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialogDeleteBecauseEricWantedIt extends AppCompatDialogFragment {
    private EditText foodItemName, yearEd;
    private Spinner exMonthSpinner, exDaySpinner, categorySpinner;
    private Switch notificationSwitch;
    private ExampleDialog.ExampleDialogListener listener;
    Food food;
    int position;
    Context context;

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
                        listener.applyTexts(username, exYear, exMonth, exDay, notificationOnOFf, category);
                    }
                }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((MainActivity)context).foods.remove(position);
                ((MainActivity)context).adaptor.foodsFull.remove(position);
                ((MainActivity)context).adaptor.notifyItemRemoved(position);
            }
        });

        foodItemName = view.findViewById(R.id.food_name);
        yearEd = view.findViewById(R.id.expiration_year);
        exMonthSpinner = view.findViewById(R.id.month_spinner);
        exDaySpinner = view.findViewById(R.id.day_spinner);
        notificationSwitch = view.findViewById(R.id.notification_switch);
        categorySpinner = view.findViewById(R.id.category_spinner);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialog.ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
//TODO Figured it out, use a listener in mainactivity to send data right here and set it to the text for date. WEEWOOOO.

    public interface ExampleDialogListener2 {
        void applyTexts(String foodName, String exYear, String exMonth, String exDay, boolean notificationOnOff, String category);
    }
}
