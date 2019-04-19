package com.example.healthkit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DoctorScheduleEditCardviewadapter extends RecyclerView.Adapter<DoctorScheduleEditCardviewadapter.DoctorScheduleEditCardviewHolder> {

    //this context we will use to inflate the layout
    private final String TAG = "DoctorScheduleEditCardviewHolder";
    private Context mCtx;
    String Day;
    String prescriptionNumber="";

    private DatabaseReference doctorReference;
    private FirebaseAuth doctorAuth;
    String pemail;

    //we are storing all the products in a list
    private List<DoctorScheduleEditCardview> productList;

    //getting the context and product list with constructor
    public DoctorScheduleEditCardviewadapter(Context mCtx, List<DoctorScheduleEditCardview> productList, String Day) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.Day=Day;
    }

    @Override
    public DoctorScheduleEditCardviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_doctor_schedule_edit_cardview, null);
        return new DoctorScheduleEditCardviewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(final DoctorScheduleEditCardviewHolder holder, final int position) {
        //getting the product of the specified position
        final DoctorScheduleEditCardview product = productList.get(position);

        //binding the data with the viewholder views

        Log.d(TAG, "Availability: "+product.available);


        holder.patientNo.setText("Patient No: "+product.patientNo);
        holder.placeId.setText(product.place);
        holder.startId.setText("Start: "+product.start);
        holder.endId.setText("End: "+product.end);

        if(product.available.toString().equals("false") && product.request.toString().equals("pending"))
        {
            holder.availavbeId.setText("Pending");
            int color = Integer.parseInt("ffff00", 16)+0xFF000000;
            holder.availavbeId.setTextColor(color);
        }
        else if(product.available.toString().equals("false") && product.request.toString().equals("true"))
        {
            holder.availavbeId.setText("Reserved");
            int color = Integer.parseInt("ff0000", 16)+0xFF000000;
            holder.availavbeId.setTextColor(color);
        }
        else
        {
            int color = Integer.parseInt("179303", 16)+0xFF000000;
            holder.availavbeId.setTextColor(color);
            holder.availavbeId.setText("Available");
        }


        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(product.available.toString().equals("true"))
                {
                    Toast.makeText(mCtx, "The Appontment hasn't been Reserved yet", Toast.LENGTH_SHORT).show();
                }
                else if(product.available.toString().equals("false") && product.request.toString().equals("pending"))
                {
                    final Dialog popUp;
                    popUp = new Dialog(mCtx);

                    popUp.setContentView(R.layout.reserved_patient_info);

                    final TextView patientNo =(TextView) popUp.findViewById(R.id.patientNoid);
                    final TextView patientName =(TextView) popUp.findViewById(R.id.patientNameid);
                    final TextView patientEmail =(TextView) popUp.findViewById(R.id.patientEmailid);
                    final TextView patientPhone =(TextView) popUp.findViewById(R.id.patientPhoneid);
                    final Button deleteReservation = (Button) popUp.findViewById(R.id.deleteReservationid);
                    final Button approveReservation = (Button)popUp.findViewById(R.id.approveReservationid);
                    final Button setPrescription = (Button) popUp.findViewById(R.id.setPrescriptionBtnId);

                    if(product.allowed.toString().equals("pending"))
                    {
                        setPrescription.setText("Pending for Approval");
                    }
                    else if(product.allowed.toString().equals("true"))
                    {
                        setPrescription.setText("Set Prescription");
                    }
                    else if(product.allowed.toString().equals("false"))
                    {
                        setPrescription.setText("Request for Set Prescription");
                    }

                    if(product.request.toString().equals("pending"))
                    {
                        approveReservation.setText("Approve");
                    }
                    else if(product.request.toString().equals("true"))
                    {
                        approveReservation.setText("Approved");
                    }

                   // doctorAuth = FirebaseAuth.getInstance();
                    String CurrentUser = SharedPrefManager.getInstance(mCtx).getUserEmail();
                    CurrentUser = CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));
                    doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser)
                            .child("schedule").child(Day).child("patientno"+product.patientNo);

                    doctorReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String patientname = dataSnapshot.child("patientName").getValue().toString();
                                String patientphone = dataSnapshot.child("patientPhone").getValue().toString();
                                String patientemail = dataSnapshot.child("patientEmail").getValue().toString();
                                String patientno = dataSnapshot.child("patientNo").getValue().toString();
                                pemail=patientemail;
                                patientNo.setText("Patient No: "+patientno);
                                patientName.setText("Name: "+patientname);
                                patientEmail.setText("Email: "+patientemail);
                                patientPhone.setText("Phone No: "+patientphone);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    deleteReservation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String CurrentUser = SharedPrefManager.getInstance(mCtx).getUserEmail();
                            CurrentUser = CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));
                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientName").setValue("");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientPhone").setValue("");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientEmail").setValue("");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientUid").setValue("");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("available").setValue("true");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("request").setValue("false");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("allowed").setValue("false");

                            int color = Integer.parseInt("179303", 16)+0xFF000000;
                            holder.availavbeId.setTextColor(color);
                            holder.availavbeId.setText("Available");
                        }
                    });


                    approveReservation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(product.request.toString().equals("pending"))
                            {
                                String CurrentUser = SharedPrefManager.getInstance(mCtx).getUserEmail();
                                CurrentUser = CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));

                                doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                                doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("request").setValue("true");

                                approveReservation.setText("Approved");

                                int color = Integer.parseInt("ff0000", 16)+0xFF000000;
                                holder.availavbeId.setTextColor(color);
                                holder.availavbeId.setText("Reserved");
                                ((Activity)mCtx).finish();
                                Intent i = ((Activity)mCtx).getIntent();
                                mCtx.startActivity(i);
                            }

                        }
                    });

                    setPrescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });


                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(popUp.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    Log.d(TAG, "Width: "+lp.width);
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    popUp.getWindow().setAttributes(lp);
                    popUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    popUp.show();
                }
                else if(product.available.toString().equals("false") && product.request.toString().equals("true"))
                {
                    final Dialog popUp;
                    popUp = new Dialog(mCtx);

                    popUp.setContentView(R.layout.reserved_patient_info);

                    final TextView patientNo =(TextView) popUp.findViewById(R.id.patientNoid);
                    final TextView patientName =(TextView) popUp.findViewById(R.id.patientNameid);
                    final TextView patientEmail =(TextView) popUp.findViewById(R.id.patientEmailid);
                    final TextView patientPhone =(TextView) popUp.findViewById(R.id.patientPhoneid);
                    Button deleteReservation = (Button) popUp.findViewById(R.id.deleteReservationid);
                    final Button approveReservation = (Button)popUp.findViewById(R.id.approveReservationid);
                    final Button setPrescription = (Button) popUp.findViewById(R.id.setPrescriptionBtnId);


                    if(product.allowed.toString().equals("pending"))
                    {
                        setPrescription.setText("Pending for Approval");
                    }
                    else if(product.allowed.toString().equals("true"))
                    {
                        setPrescription.setText("Set Prescription");

                    }
                    else if(product.allowed.toString().equals("false"))
                    {
                        setPrescription.setText("Request for Set Prescription");
                    }


                    if(product.request.toString().equals("true"))
                    {
                        approveReservation.setText("Approved");
                    }

                    String CurrentUser = SharedPrefManager.getInstance(mCtx).getUserEmail();
                    CurrentUser = CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));
                    doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser)
                            .child("schedule").child(Day).child("patientno"+product.patientNo);

                    doctorReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String patientname = dataSnapshot.child("patientName").getValue().toString();
                                String patientphone = dataSnapshot.child("patientPhone").getValue().toString();
                                String patientemail = dataSnapshot.child("patientEmail").getValue().toString();
                                String patientno = dataSnapshot.child("patientNo").getValue().toString();
                                pemail=patientemail;
                                patientNo.setText("Patient No: "+patientno);
                                patientName.setText("Name: "+patientname);
                                patientEmail.setText("Email: "+patientemail);
                                patientPhone.setText("Phone No: "+patientphone);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    deleteReservation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String CurrentUser = SharedPrefManager.getInstance(mCtx).getUserEmail();
                            CurrentUser = CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));
                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientName").setValue("");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientPhone").setValue("");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientEmail").setValue("");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientUid").setValue("");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("available").setValue("true");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("request").setValue("false");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("allowed").setValue("false");

                            doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                            doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("prescriptionNumber").setValue("");

                            int color = Integer.parseInt("179303", 16)+0xFF000000;
                            holder.availavbeId.setTextColor(color);
                            holder.availavbeId.setText("Available");
                        }
                    });


                    approveReservation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //int color = Integer.parseInt("179303", 16)+0xFF000000;
                            approveReservation.setText("Approved");
                            //approveReservation.setTextColor(color);

                        }
                    });

                    setPrescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(product.allowed.toString().equals("false"))
                            {
                                String CurrentUser = SharedPrefManager.getInstance(mCtx).getUserEmail();
                                CurrentUser = CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));
                                doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
                                doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("allowed").setValue("pending");
                                ((Activity)mCtx).finish();
                                Intent i = ((Activity)mCtx).getIntent();
                                mCtx.startActivity(i);

                                setPrescription.setText("Pending for Approval");

                            }
                            else if(product.allowed.toString().equals("pending"))
                            {
                                setPrescription.setText("Pending for Approval");
                            }
                            else if(product.allowed.toString().equals("true"))
                            {
                                setPrescription.setText("Set Prescription");
                                pemail=pemail.substring(0,pemail.lastIndexOf('@'));
                                Intent intent = new Intent(mCtx,SetPrescription.class);
                                intent.putExtra("allowed",product.allowed);
                                intent.putExtra("patientNo",product.patientNo);
                                intent.putExtra("Day",Day);
                                intent.putExtra("patientUid",pemail);
                                intent.putExtra("prescriptionNumber",product.prescriptionNumber);
                                Log.d(TAG, "prescription put: "+product.prescriptionNumber);
                                mCtx.startActivity(intent);
                            }
                        }
                    });


                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(popUp.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    Log.d(TAG, "Width: "+lp.width);
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    popUp.getWindow().setAttributes(lp);
                    popUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    popUp.show();
                }




            }
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class DoctorScheduleEditCardviewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout;

        TextView patientNo, placeId,startId,endId,availavbeId;


        public DoctorScheduleEditCardviewHolder(View itemView) {
            super(itemView);

            patientNo = (TextView) itemView.findViewById(R.id.PatientNoId);
            placeId = (TextView) itemView.findViewById(R.id.placeNameId);
            startId = (TextView)itemView.findViewById(R.id.startingTimeId);
            endId = (TextView) itemView.findViewById(R.id.endingTimeId);
            availavbeId = (TextView) itemView.findViewById(R.id.availablityId);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }



    }
}
