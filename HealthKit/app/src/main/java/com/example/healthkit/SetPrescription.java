package com.example.healthkit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SetPrescription extends AppCompatActivity implements SetPrescriptionDialogInput.SetPrescriptionDialogInputListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    //the recyclerview
    RecyclerView recyclerView;
    SetPrescriptionCardviewadapter adapter;
    List<SetPrescriptionCardview> medicineList;

    private TextView doctorName,doctorDegreeCatagory,doctorWorkplace,doctorPhoneEmail,dateText;
    private Button addmedicineBtn,savePrescriptionBtn;

    private DatabaseReference doctorReference,patientReference;
    private FirebaseAuth doctorAuth;
    private FirebaseAuth patientAuth;
    private String patientUID,prescriptionNumber;
    private String Day,patientNo,allowed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_prescription);
        drawerLayout = (DrawerLayout) findViewById(R.id.dpDrawerId);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.dpNavigationViewId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.menuReservationScheduleId)
                {
                    Toast.makeText(SetPrescription.this,"Schedule Clicked",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DoctorScheduleDaySelection.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(SetPrescription.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                }
                else if(id==R.id.menuLogoutbtnId)
                {
                    Toast.makeText(SetPrescription.this,"Log Out Clicked",Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                    startActivity(new Intent(getApplicationContext(),UserCatagory.class));
                }
                return true;
            }
        });

        ///Drawer & NavigationBar ends.


        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
        {
            return;
        }
        patientUID = (String) bundle.get("patientUid");
        Log.d("BAAAAAAAAAL",patientUID);
        prescriptionNumber = (String) bundle.get("prescriptionNumber");
        Day = (String) bundle.get("Day");
        patientNo= (String) bundle.get("patientNo");
        allowed = (String) bundle.get("allowed");


        doctorName = (TextView) findViewById(R.id.DoctorNameInPrescription);
        doctorDegreeCatagory = (TextView) findViewById(R.id.DoctorDegreeCatagoryInPrescription);
        doctorWorkplace = (TextView) findViewById(R.id.DoctorWorkplaceInPrescription);
        doctorPhoneEmail = (TextView) findViewById(R.id.DoctorPhoneEmailInPrescription);
        dateText = (TextView) findViewById(R.id.PrescriptionDate);
        addmedicineBtn = (Button) findViewById(R.id.AddMedicineBtn);
        savePrescriptionBtn = (Button) findViewById(R.id.SavePrescriptionBtn);
        String CurrentUser =SharedPrefManager.getInstance(getApplicationContext()).getUserEmail();
        CurrentUser = CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));
        doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
        loadDoctorInfo();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        dateText.setText(date);

        addmedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetPrescriptionDialogInput setPrescriptionDialogInput = new SetPrescriptionDialogInput();
                setPrescriptionDialogInput.show(getSupportFragmentManager(),"SetPrescriptionDialogInput");
            }
        });
        final String finalCurrentUser = CurrentUser;
        savePrescriptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetPrescription.this, "Prescription Saved", Toast.LENGTH_SHORT).show();
                doctorAuth = FirebaseAuth.getInstance();
                doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(finalCurrentUser);
                doctorReference.child("schedule").child(Day).child("patientno"+patientNo).child("allowed").setValue("false");

                String temp = prescriptionNumber;
                int cnt = Integer.parseInt(temp);
                cnt++;
                String putTemp = String.valueOf(cnt);

                patientReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(patientUID);
                patientReference.child("prescriptionNumber").setValue(putTemp);


                onBackPressed();

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        medicineList = new ArrayList<>();

        //creating recyclerview adapter
        adapter = new SetPrescriptionCardviewadapter(this, medicineList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        medicineList.clear();
        patientReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(patientUID).child("MyPrescription").child(prescriptionNumber);
        patientReference.addListenerForSingleValueEvent(valueEventListener);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) ||super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            // startActivity(new Intent(getApplicationContext(),SearchDoctor.class));
            super.onBackPressed();
        }
    }
    private void loadDoctorInfo(){
        final String doctor_email = SharedPrefManager.getInstance(getApplicationContext()).getUserEmail();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_GET_DOCTOR_by_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            String myname = obj.getString("doctor_name");
                            String mycatagory = obj.getString("doctor_catagory");
                            String myphone = obj.getString("doctor_phone_no");
                            String myemail = obj.getString("doctor_email");
                            String myworkplace = obj.getString("doctor_workplace");
                            String mydegree = obj.getString("doctor_degree");

                            doctorName.setText("Dr. "+myname);
                            doctorDegreeCatagory.setText(mydegree+", "+mycatagory);
                            doctorPhoneEmail.setText(myphone+", "+myemail);
                            doctorWorkplace.setText(myworkplace);


                        }catch (JSONException e) {
                            Log.d("ErrorFILTER",e.getMessage());Log.d("ERRORLINEFILTER",response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                        Log.d("ErrorFilter3",error.getMessage().toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("doctor_email",doctor_email);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            medicineList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SetPrescriptionCardview medicinelist = snapshot.getValue(SetPrescriptionCardview.class);
                    medicineList.add(medicinelist);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    public void setMedicineIntoDatabase(String medicineno,String medicinename,String morning,String noon,
                                        String night,String beforemeal,String aftermeal,String duration,String suggestion)
    {
        patientReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(patientUID);

        Log.d("SetPrescription", "setMedicineIntoDatabase: "+"/Patients/"+patientUID+"/MyPrescription/"+prescriptionNumber+"/medicineNo"+medicineno+"/medicineName:"+medicinename);
        Log.d("SetPrescription", "setMedicineIntoDatabase: "+"/Patients/"+patientUID+"/MyPrescription/"+"medicineNo"+medicineno+"/morning:"+morning);
        Log.d("SetPrescription", "setMedicineIntoDatabase: "+"/Patients/"+patientUID+"/MyPrescription/"+"medicineNo"+medicineno+"/noon:"+noon);

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("medicineName").setValue(medicinename);

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("morning").setValue(morning);

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("noon").setValue(noon);

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("night").setValue(night);

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("beforeMeal").setValue(beforemeal);

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("afterMeal").setValue(aftermeal);

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("duration").setValue(duration);

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("suggestion").setValue(suggestion);

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("doctorName").setValue(doctorName.getText().toString());

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("doctorDegreecatagory").setValue(doctorDegreeCatagory.getText().toString());

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("doctorWorkplace").setValue(doctorWorkplace.getText().toString());

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("doctorPhoneEmail").setValue(doctorPhoneEmail.getText().toString());

        patientReference.child("MyPrescription").child(prescriptionNumber)
                .child("medicineNo"+medicineno).child("medicineNo").setValue(medicineno);

        medicineList.clear();
        patientReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(patientUID).child("MyPrescription").child(prescriptionNumber);
        patientReference.addListenerForSingleValueEvent(valueEventListener);


    }

    @Override
    public void applyText(String medicineno, String medicinename, String morning, String noon,
                          String night, String beforemeal, String aftermeal, String duration, String suggestion) {
        if(TextUtils.isEmpty(medicineno))
        {
            Toast.makeText(this,"Empty medicine no",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(medicinename))
        {
            Toast.makeText(this,"Empty medicine name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "No: "+medicineno+" Name: "+medicinename+" Morning: "+morning
                    +" Noon: "+noon+" Night: "+night+" Before: "+beforemeal+" After: "+aftermeal+
                    " Duration: "+duration+" Suggestion: "+suggestion, Toast.LENGTH_SHORT).show();

            Log.d("setPrescription", "Outputtext : "+"No: "+medicineno+" Name: "+medicinename+" Morning: "+morning
                    +" Noon: "+noon+" Night: "+night+" Before: "+beforemeal+" After: "+aftermeal+
                    " Duration: "+duration+" Suggestion: "+suggestion);
            setMedicineIntoDatabase(medicineno,medicinename,morning,noon,night,beforemeal,aftermeal,duration,suggestion);
        }
    }
}
