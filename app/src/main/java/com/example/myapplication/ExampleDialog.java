package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class ExampleDialog extends AppCompatDialogFragment{
   private EditText foodItemName, yearEd;
   private Spinner exMonthSpinner, exDaySpinner, categorySpinner;
   private Switch notificationSwitch;
   private ExampleDialogListener listener;
   private Context context;
   private int day, month, year;

   public ExampleDialog(Context c) {
      context = c;
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
                    String exYear =  "" + year;
                    String exMonth =  "" + month;
                    String exDay =  "" + day;
                    String category = categorySpinner.getSelectedItem().toString();
                    boolean notificationOnOFf = notificationSwitch.isChecked();
                    listener.applyTexts(username, exYear, exMonth, exDay, notificationOnOFf, category);
                 }
              });

      foodItemName = view.findViewById(R.id.food_name);
      notificationSwitch = view.findViewById(R.id.notification_switch);
      categorySpinner = view.findViewById(R.id.category_spinner);

      return builder.create();
   }


   @Override
   public void onAttach(Context context) {
      super.onAttach(context);

      try {
         listener = (ExampleDialogListener) context;
      } catch (ClassCastException e) {
         throw new ClassCastException(context.toString() +
                 "must implement ExampleDialogListener");
      }
   }
//TODO Figured it out, use a listener in mainactivity to send data right here and set it to the text for date. WEEWOOOO.

   public interface ExampleDialogListener {
      void applyTexts(String foodName, String exYear, String exMonth, String exDay, boolean notificationOnOff, String category);
   }
}
