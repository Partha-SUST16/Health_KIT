package com.example.healthkit;

import android.content.Context;
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
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coinbase.api.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class messageListDoctor extends AppCompatActivity {

    private RecyclerView recyclerView;
    DatabaseReference dbDoctor,dbPatient;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    List<User_Information> patientList;
    id_previewAdapter adapter;
    String doctor_email,patient_email;
    /*********************/////////////////
    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list_doctor);

        doctor_email = SharedPrefManager.getInstance(getApplicationContext()).getUserEmail();
        doctor_email=doctor_email.substring(0,doctor_email.lastIndexOf('@'));

        drawerLayout = (DrawerLayout) findViewById(R.id.dpDrawerIdDoctor);
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
                    Toast.makeText(messageListDoctor.this,"Schedule Clicked",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DoctorScheduleDaySelection.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(messageListDoctor.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuLogoutbtnId)
                {
                    Toast.makeText(messageListDoctor.this,"Log Out Clicked",Toast.LENGTH_SHORT).show();
                    finish();
                    FirebaseAuth.getInstance().signOut();
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    Toast.makeText(getApplicationContext(),"We will implement it soon",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                return true;
            }
        });

        ///Drawer & NavigationBar ends.
        recyclerView = (RecyclerView) findViewById(R.id.id_preview_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        patientList = new ArrayList<>();

        adapter = new id_previewAdapter(patientList,this);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
               // String title1 = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.id_previewuid)).getText().toString();
                String title2 = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.id_previewName)).getText().toString();
                Intent i = new Intent(messageListDoctor.this,Message_View.class);
                i.putExtra("doctor_name",title2);
                i.putExtra("doctor_email",title2+"@gmail.com");
                i.putExtra("flag",1);
                startActivity(i);

            }

            @Override
            public void onLongClick(View view, int position) {


            }

        }));
        addToList();
    }
    private void addToList()
    {
        Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_SHORT).show();
        patientList.clear();
        dbDoctor = FirebaseDatabase.getInstance().getReference().child("Doctors")
                .child(doctor_email)
                .child("message");
        dbPatient = FirebaseDatabase.getInstance().getReference()
                .child("Patients");
        dbDoctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    patient_email = (String) ds.getKey();
                    Toast.makeText(getApplicationContext(),patient_email,Toast.LENGTH_SHORT).show();
                    User_Information ui = new User_Information();
                    ui.setName(patient_email);
                    ui.setArea(patient_email);
                    patientList.add(ui);
                    adapter.notifyDataSetChanged();
                    //seeInPatient();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void seeInPatient(){
        dbPatient.child(patient_email).addListenerForSingleValueEvent(valueEventListener);
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            patientList.clear();
            if (dataSnapshot.exists()) {
                //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                /*if(snapshot.getKey().equals(key)) {*/
                Log.d("Main test ",dataSnapshot.getKey());
                User_Information doctor = dataSnapshot.getValue(User_Information.class);
                patientList.add(doctor);
                // }
                //}
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
}
