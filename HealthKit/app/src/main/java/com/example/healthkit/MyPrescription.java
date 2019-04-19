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
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyPrescription extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    //the recyclerview
    RecyclerView recyclerView;
    MyPrescriptionCardviewadapter adapter;
    List<MyPrescriptionCardview> medicineList;

    private FirebaseAuth patientAuth;
    DatabaseReference medicineReferance;
    TextView drname,drdegreecatagory,drworkplace,drphoneemail;


    String prescriptionNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prescription);

        drawerLayout = (DrawerLayout) findViewById(R.id.ppDrawerId);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.ppNavigationViewId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.menuBMIbtnId)
                {
                    Toast.makeText(getApplicationContext(),"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }
                else if(id == R.id.menuPrescriptionbtnId)
                {
                    // Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),MyPrescriptionList.class));
                }
                else if(id == R.id.menuSearchbtnId)
                {
                    Intent intent = new Intent(getApplicationContext(),SearchDoctor.class);
                    startActivity(intent);
                }
                else if(id==R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(getApplicationContext(),"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(getApplicationContext(),"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuLogoutbtnId)
                {
                    Toast.makeText(getApplicationContext(),"Log Out Clicked",Toast.LENGTH_SHORT).show();
                    finish();
                    patientAuth.signOut();
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    //Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                   // startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                else if(id == R.id.recentButtonId)
                {
                    //Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),RecentDoctors.class));
                }

                return true;
            }
        });
        drname = (TextView) findViewById(R.id.DoctorNameInMyPrescription);
        drdegreecatagory = (TextView) findViewById(R.id.DoctorDegreeCatagoryInMyPrescription);
        drworkplace = (TextView) findViewById(R.id.DoctorWorkplaceInMyPrescription);
        drphoneemail = (TextView) findViewById(R.id.DoctorPhoneEmailInMyPrescription);




        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        medicineList = new ArrayList<>();

        //creating recyclerview adapter
        adapter = new MyPrescriptionCardviewadapter(this, medicineList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        ///Drawer & NavigationBar ends.
        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
        {
            return;
        }
        String temp = (String) bundle.get("clickedOn");
        int cnt = Integer.parseInt(temp);
        cnt++;
        prescriptionNo = temp;
        Log.d("MyPrescription", "onCreate: Prescrip "+prescriptionNo);

        String CurrentUser = SharedPrefManager.getInstance(getApplicationContext()).getUserEmail();
        CurrentUser = CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));
        Log.d("BALLLLLLL",CurrentUser);
        medicineReferance = FirebaseDatabase.getInstance().getReference().child("Patients").child(CurrentUser).child("MyPrescription").child(prescriptionNo).child("medicineNo1");

        medicineReferance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String DrName = dataSnapshot.child("doctorName").getValue(String.class);
                String DrDegreeCatagory = dataSnapshot.child("doctorDegreecatagory").getValue(String.class);
                String DrWorkplace = dataSnapshot.child("doctorWorkplace").getValue(String.class);
                String DrPhoneEmail = dataSnapshot.child("doctorPhoneEmail").getValue(String.class);

                Log.d("MyPrescription", "onDataChange:Dr "+DrName);
                drname.setText(DrName);
                drdegreecatagory.setText(DrDegreeCatagory);
                drworkplace .setText(DrWorkplace);
                drphoneemail.setText(DrPhoneEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        medicineList.clear();

        String CurrentPatient = SharedPrefManager.getInstance(getApplicationContext()).getUserEmail();
        CurrentPatient = CurrentPatient.substring(0,CurrentPatient.lastIndexOf('@'));
        medicineReferance = FirebaseDatabase.getInstance().getReference().child("Patients").child(CurrentPatient).child("MyPrescription").child(prescriptionNo);
        medicineReferance.addListenerForSingleValueEvent(valueEventListener);
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            medicineList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyPrescriptionCardview medicinelist = snapshot.getValue(MyPrescriptionCardview.class);
                    medicineList.add(medicinelist);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

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
            //startActivity(new Intent(getApplicationContext(),MyPrescriptionList.class));
            super.onBackPressed();
        }
    }
}
