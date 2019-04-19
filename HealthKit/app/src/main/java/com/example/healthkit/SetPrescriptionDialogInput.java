package com.example.healthkit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SetPrescriptionDialogInput extends AppCompatDialogFragment {


    private EditText medicineNo,medicineName,Suggestion,Duration;
    private RadioButton medicineMorning,medicineNoon,medicineNight,beforeMeal,afterMeal;
    private SetPrescriptionDialogInputListener listener;
    private RadioGroup timesADay,beforeAfter;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.set_prescription_dialog_input, null);
        builder.setView(view).setTitle("Medicine Dialog")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String morning="",noon="",night="",beforeM="",afterM="";
                        String medicineno = medicineNo.getText().toString().trim();
                        String medicinename = medicineName.getText().toString().trim();
                        String suggestion = Suggestion.getText().toString().trim();
                        String duration = Duration.getText().toString().trim();
                        if(medicineMorning.isChecked())
                        {
                            morning="true";
                        }
                        else
                        {
                            morning="false";
                        }
                        if(medicineNoon.isChecked())
                        {
                            noon="true";
                        }
                        else
                        {
                            noon="false";
                        }
                        if(medicineNight.isChecked())
                        {
                            night="true";
                        }
                        else
                        {
                            night="false";
                        }
                        if(beforeMeal.isChecked())
                        {
                            beforeM="true";
                        }
                        else
                        {
                            beforeM="false";
                        }
                        if(afterMeal.isChecked())
                        {
                            afterM="true";
                        }
                        else
                        {
                            afterM="false";
                        }

                        listener.applyText(medicineno,medicinename,morning,noon,night,beforeM,afterM,duration,suggestion);
                    }
                });
        final Context cntx = builder.getContext();

        medicineNo = (EditText) view.findViewById(R.id.MedicineNoId);
        medicineName = (EditText) view.findViewById(R.id.MedicineNameId);
        medicineMorning = (RadioButton) view.findViewById(R.id.MedicineInMorningId);
        medicineNoon = (RadioButton) view.findViewById(R.id.MedicineInNoonId);
        medicineNight = (RadioButton) view.findViewById(R.id.MedicineInNightId);
        beforeMeal = (RadioButton) view.findViewById(R.id.medicineBeforeMealId);
        afterMeal = (RadioButton) view.findViewById(R.id.MedicineAfterMealId);
        Duration = (EditText) view.findViewById(R.id.MedicineDurationId);
        Suggestion = (EditText) view.findViewById(R.id.MedicineSuggestionId);
        //timesADay = (RadioGroup) view.findViewById(R.id.MedicineTimeId);
        //beforeAfter = (RadioGroup) view.findViewById(R.id.MedicineMealId);



        return builder.create();

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener =(SetPrescriptionDialogInputListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    +"must Implement");
        }
    }


    public interface SetPrescriptionDialogInputListener{
        void applyText(String medicineno, String medicinename, String medicinemorning, String medicinenoon,
                       String medicinenight, String beforemeal, String aftermeal, String duration, String suggestion);
    }
}
