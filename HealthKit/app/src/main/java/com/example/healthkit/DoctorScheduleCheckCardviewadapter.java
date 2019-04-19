package com.example.healthkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DoctorScheduleCheckCardviewadapter extends RecyclerView.Adapter<DoctorScheduleCheckCardviewadapter.DoctorScheduleCheckCardviewHolder>{
    //this context we will use to inflate the layout
    private final String TAG = "DoctorScheduleCheckCardviewHolder";
    private Context mCtx;

    private DatabaseReference doctorReference;
    private DatabaseReference patientReference;
    private FirebaseAuth patientAuth;
    private FirebaseAuth.AuthStateListener patientAuthListener;
    private String patientID;
    private DatabaseReference paID;
    String Day;
    String doctorUID ;
    //we are storing all the products in a list
    private List<DoctorScheduleEditCardview> productList;

    //getting the context and product list with constructor
    public DoctorScheduleCheckCardviewadapter(Context mCtx, List<DoctorScheduleEditCardview> productList, String Day, String doctorUID) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.Day=Day;
        this.doctorUID=doctorUID;
    }

    @Override
    public DoctorScheduleCheckCardviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_doctor_schedule_edit_cardview, null);
        return new DoctorScheduleCheckCardviewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(final DoctorScheduleCheckCardviewHolder holder, final int position) {

        //getting the product of the specified position
        final DoctorScheduleEditCardview product = productList.get(position);

        //binding the data with the viewholder views

        Log.d(TAG, "Availability: "+product.available);


        holder.patientNo.setText("Parient No: "+product.patientNo);
        holder.placeId.setText(product.place);
        holder.startId.setText("Start: "+product.start);
        holder.endId.setText("End: "+product.end);

        patientAuth = FirebaseAuth.getInstance();
        String CurrentUser = SharedPrefManager.getInstance(mCtx).getUserEmail();
        final String CurrentEmail = CurrentUser;
        CurrentUser=CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));

        if(product.patientEmail.toString().equals(CurrentEmail) && product.request.toString().equals("pending"))
        {
            holder.availavbeId.setText("Pending");
            int color = Integer.parseInt("ffff00", 16)+0xFF000000;
            holder.availavbeId.setTextColor(color);
        }
        else if(product.patientEmail.toString().equals(CurrentEmail) && product.request.toString().equals("true"))
        {
            holder.availavbeId.setText("My Reservation");
            int color = Integer.parseInt("0000ff", 16)+0xFF000000;
            holder.availavbeId.setTextColor(color);
        }
        else if(product.available.toString().equals("false"))
        {
            holder.availavbeId.setText("Reserved");
            int color = Integer.parseInt("ff0000", 16)+0xFF000000;
            holder.availavbeId.setTextColor(color);
        }
        else if(product.available.toString().equals("true"))
        {
            int color = Integer.parseInt("179303", 16)+0xFF000000;
            holder.availavbeId.setTextColor(color);
            holder.availavbeId.setText("Available");
        }
        else
        {
            Toast.makeText(mCtx,"Null Available", Toast.LENGTH_SHORT).show();
        }



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                patientAuth = FirebaseAuth.getInstance();
                String CurrentUser = SharedPrefManager.getInstance(mCtx).getUserEmail();
                CurrentUser=CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));

                if(product.available.toString().equals("false") &&product.request.toString().equals("true")&&product.patientEmail.toString().equals(CurrentEmail))
                {
                    holder.availavbeId.setText("My Reservation");
                    int color = Integer.parseInt("0000ff", 16)+0xFF000000;
                    holder.availavbeId.setTextColor(color);

                    if(product.allowed.toString().equals("pending"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mCtx);
                        dialog.setTitle( "Grant Access Request" )
                                .setMessage("Do you want to give access?")
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctorUID);
                                        doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("allowed").setValue("true");

                                    }
                                }).show();
                    }
                }
                else if(product.available.toString().equals("false") &&product.request.toString().equals("true"))
                {
                    holder.availavbeId.setText("Reserved");
                    int color = Integer.parseInt("ff0000", 16)+0xFF000000;
                    holder.availavbeId.setTextColor(color);
                }

                else if(product.available.toString().equals("false"))
                {
                    Toast.makeText(mCtx, "The appointment is Reserved", Toast.LENGTH_SHORT).show();
                }
                else if(product.available.toString().equals("true"))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mCtx);
                    final String finalCurrentUser = CurrentUser;
                    dialog.setTitle( "Reserve Appointment Request" )
                            .setMessage("Do you want to reserve this appointment?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    Toast.makeText(mCtx,"Reservation Done", Toast.LENGTH_SHORT).show();
                                    int pos= position;
                                    String posStr = Integer.toString(pos);
                        /////////////////////////////////////////////////////////

                                    if(!Day.equals(null))
                                    {
                                        Log.d(TAG, "MyPATH: "+"Doctors "+doctorUID+" schedule "+Day+" "+product.patientNo+" availavle");

                                        Log.d(TAG, "MyPATH: "+"Doctors "+doctorUID+" schedule "+Day+" "+posStr+" availavle");

                                        doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctorUID);
                                        doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("available").setValue("false");

                                        doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctorUID);
                                        doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("request").setValue("pending");


                                        holder.availavbeId.setText("Pending");
                                        int color = Integer.parseInt("ffff00", 16)+0xFF000000;
                                        holder.availavbeId.setTextColor(color);

                                       /* patientAuth = FirebaseAuth.getInstance();
                                        String CurrentUser = patientAuth.getCurrentUser().getUid();*/

                                        doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctorUID);
                                        doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientEmail").setValue(finalCurrentUser);


                                        patientReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(finalCurrentUser);
                                        patientReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    String patientname = SharedPrefManager.getInstance(mCtx).getUsername();
                                                    String patientphone = "0185552656212";
                                                    String patientemail = SharedPrefManager.getInstance(mCtx).getUserEmail();
                                                    String prescriptionNumber = null;

                                                    prescriptionNumber = dataSnapshot.child("prescriptionNumber").getValue().toString();

                                                    if(prescriptionNumber==null){
                                                        prescriptionNumber="0";
                                                    }


                                                    doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctorUID);
                                                    doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientName").setValue(patientname);

                                                    doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctorUID);
                                                    doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientPhone").setValue(patientphone);

                                                    doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctorUID);
                                                    doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("patientEmail").setValue(patientemail);

                                                    doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctorUID);
                                                    doctorReference.child("schedule").child(Day).child("patientno"+product.patientNo).child("prescriptionNumber").setValue(prescriptionNumber);
                                               }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }
                            }).show();

                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class DoctorScheduleCheckCardviewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout;

        TextView patientNo, placeId,startId,endId,availavbeId;


        public DoctorScheduleCheckCardviewHolder(View itemView) {
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
