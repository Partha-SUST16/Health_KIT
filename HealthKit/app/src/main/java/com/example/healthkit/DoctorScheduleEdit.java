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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorScheduleEdit extends AppCompatActivity implements ScheduleDialogInput.ScheduleDialogInputListener{

    private static  String TAG="DoctorScheduleEdit";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView dayNameText;
    private Button addAppointmentbtn,refreshButton;


    private FirebaseAuth doctorAuth;
    private FirebaseAuth.AuthStateListener doctorAuthListener;
    private DatabaseReference doctorReference;

    private RecyclerView recyclerView;
    List<DoctorScheduleEditCardview> patientList;
    DoctorScheduleEditCardviewadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_schedule_edit);
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
                    //Toast.makeText(DoctorScheduleEdit.this,"Schedule Clicked",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DoctorScheduleDaySelection.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    //Toast.makeText(DoctorScheduleEdit.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuLogoutbtnId)
                {
                    //Toast.makeText(DoctorScheduleEdit.this,"Log Out Clicked",Toast.LENGTH_SHORT).show();
                    finish();
                    doctorAuth.signOut();
                }
                else if(id == R.id.chatDoctor)
                {
                     Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                   // startActivity(new Intent(getApplicationContext(),messageListDoctor.class));
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    Toast.makeText(getApplicationContext(),"We will implement it soon",Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                return true;
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
        {
            return;
        }
        doctorAuth = FirebaseAuth.getInstance();
        String CurrentUser = SharedPrefManager.getInstance(getApplicationContext()).getUserEmail();
        CurrentUser = CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));
        doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
        String dayName = (String) bundle.get("day");
        Log.d(TAG, "getExtra: "+dayName);
        dayNameText = (TextView) findViewById(R.id.DayNameTextView);
        dayNameText.setText(dayName);


        addAppointmentbtn = (Button) findViewById(R.id.addAppointmentBtnId);
        addAppointmentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        refreshButton = findViewById(R.id.refreshID);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        patientList = new ArrayList<>();

        //creating recyclerview adapter
        adapter = new DoctorScheduleEditCardviewadapter(this, patientList,dayName);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        patientList.clear();
        doctorReference = FirebaseDatabase.getInstance().getReference("Doctors").child(CurrentUser).child("schedule").child(dayNameText.getText().toString());
        doctorReference.addListenerForSingleValueEvent(valueEventListener);

        //load();
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            patientList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DoctorScheduleEditCardview patientlist = snapshot.getValue(DoctorScheduleEditCardview.class);
                    patientList.add(patientlist);
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
            super.onBackPressed();
        }
    }

    @Override
    public void applyText(String place, String start, String end, String patientNo) {

        if(TextUtils.isEmpty(place))
        {
            Toast.makeText(this,"Empty Hospital Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(start))
        {
            Toast.makeText(this,"Empty Starting time",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(end))
        {
            Toast.makeText(this,"Empty Ending time",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(patientNo))
        {
            Toast.makeText(this,"Empty Patient No",Toast.LENGTH_SHORT).show();
        }
        else
        {
            distributeTime(place,start,end,patientNo);
            Log.d(TAG, "applyText: "+place+" "+start+" "+end+" "+patientNo);
        }

        /*
        if(place==null||start==null||end==null||patientNo==null)
        {
            Toast.makeText(this,"Empty Field",Toast.LENGTH_SHORT).show();
        }
        else
        {
            distributeTime(place,start,end,patientNo);
            Log.d(TAG, "applyText: "+place+" "+start+" "+end+" "+patientNo);
        }
        */
    }



    public void distributeTime(String Place,String StartingTime,String EndingTime,String PatientNo){

        String CurrentUser = SharedPrefManager.getInstance(getApplicationContext()).getUserEmail();
        CurrentUser = CurrentUser.substring(0,CurrentUser.lastIndexOf('@'));
        doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);

        doctorReference.child("schedule").child(dayNameText.getText().toString())
                .child("patientno"+PatientNo).child("start").setValue(StartingTime);
        doctorReference.child("schedule").child(dayNameText.getText().toString())
                .child("patientno"+PatientNo).child("end").setValue(EndingTime);
        doctorReference.child("schedule").child(dayNameText.getText().toString())
                .child("patientno"+PatientNo).child("available").setValue("true");
        doctorReference.child("schedule").child(dayNameText.getText().toString())
                .child("patientno"+PatientNo).child("place").setValue(Place);
        doctorReference.child("schedule").child(dayNameText.getText().toString())
                .child("patientno"+PatientNo).child("patientNo").setValue(PatientNo);
        doctorReference.child("schedule").child(dayNameText.getText().toString())
                .child("patientno"+PatientNo).child("patientUid").setValue("");
        doctorReference.child("schedule").child(dayNameText.getText().toString())
                .child("patientno"+PatientNo).child("patientName").setValue("");
        doctorReference.child("schedule").child(dayNameText.getText().toString())
                .child("patientno"+PatientNo).child("patientPhone").setValue("");
        doctorReference.child("schedule").child(dayNameText.getText().toString())
                .child("patientno"+PatientNo).child("patientEmail").setValue("");
        doctorReference.child("schedule").child(dayNameText.getText().toString())
                .child("patientno"+PatientNo).child("request").setValue("false");
        doctorReference.child("schedule").child(dayNameText.getText().toString())
                .child("patientno"+PatientNo).child("allowed").setValue("false");

        patientList.clear();
        doctorReference = FirebaseDatabase.getInstance().getReference("Doctors").child(CurrentUser).child("schedule").child(dayNameText.getText().toString());
        doctorReference.addListenerForSingleValueEvent(valueEventListener);

    }


    public void openDialog(){
        ScheduleDialogInput scheduleDialogInput = new ScheduleDialogInput();
        scheduleDialogInput.show(getSupportFragmentManager(),"ScheduleDialogInput");


    }
}
