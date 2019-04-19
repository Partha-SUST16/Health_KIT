package com.example.healthkit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class ScheduleDialogInput extends AppCompatDialogFragment {

    private ScheduleDialogInputListener listener;
    private EditText selectPlace,selectStarttime,selectEndtime,patientNo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.schedule_dialog_input,null);
        builder.setView(view).setTitle("Schedule Dialog")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String place = selectPlace.getText().toString().trim();
                        String start = selectStarttime.getText().toString().trim();
                        String end = selectEndtime.getText().toString().trim();
                        String patientno = patientNo.getText().toString().trim();

                        listener.applyText(place,start,end,patientno);
                    }
                });
        final Context cntx = builder.getContext();

        selectPlace = (EditText) view.findViewById(R.id.SelectHospitalId);
        selectStarttime = (EditText) view.findViewById(R.id.SelectStarttimeId);
        selectEndtime = (EditText) view.findViewById(R.id.SelectEndtimeId);
        patientNo = (EditText) view.findViewById(R.id.selectPatientNoId);


        selectStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(cntx, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String amPm;
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        selectStarttime.setText(String.format("%02d:%02d", hourOfDay%12, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
        selectEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(cntx, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String amPm;
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        selectEndtime.setText(String.format("%02d:%02d", hourOfDay%12, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener =(ScheduleDialogInputListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
            +"must Implement");
        }
    }

    public interface ScheduleDialogInputListener{
        void applyText(String place, String start, String end, String perPatienttime);
    }
}