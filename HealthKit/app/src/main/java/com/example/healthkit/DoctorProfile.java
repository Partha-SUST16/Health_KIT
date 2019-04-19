package com.example.healthkit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class DoctorProfile extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView doctorname,doctorage,doctorarea,doctorcatagory,doctoremail,doctorgender,doctorphone,doctorworkplace,doctordegree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_doctor_profile);

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
                    Toast.makeText(DoctorProfile.this,"Schedule Clicked",Toast.LENGTH_SHORT).show();
                   Intent intent2 = new Intent(getApplicationContext(),DoctorScheduleDaySelection.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(DoctorProfile.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuLogoutbtnId)
                {
                    Toast.makeText(DoctorProfile.this,"Log Out Clicked",Toast.LENGTH_SHORT).show();
                    //finish();
                   // doctorAuth.signOut();
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                    startActivity(new Intent(getApplicationContext(),DoctorLogin.class));
                }
                else if(id == R.id.chatDoctor)
                {
                    //Toast.makeText(getApplicationContext(),"We will implement it soon",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),messageListDoctor.class));
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    //Toast.makeText(getApplicationContext(),"We will implement it soon",Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                return true;
            }
        });

        doctorname = (TextView) findViewById(R.id.doctorName);
        doctorage = (TextView) findViewById(R.id.doctorAge);
        doctorgender = (TextView) findViewById(R.id.doctorGender);
        doctorarea = (TextView) findViewById(R.id.doctorArea);
        doctorcatagory = (TextView) findViewById(R.id.doctorCatagory);
        doctorphone = (TextView) findViewById(R.id.doctorPhone);
        doctoremail = (TextView) findViewById(R.id.doctorEmail);
        doctorworkplace = (TextView) findViewById(R.id.doctorWorkplace);
        doctordegree = (TextView) findViewById(R.id.doctorDegree);
        ini();
    }
    private void ini(){
        try {
            JSONObject obj = new JSONObject(getIntent().getStringExtra("information"));
            doctorname.setText(obj.getString("doctor_name"));
            doctorage.setText(obj.getString("doctor_age"));
            doctorarea.setText(obj.getString("doctor_area"));
            doctorcatagory.setText(obj.getString("doctor_catagory"));
            doctordegree.setText(obj.getString("doctor_degree"));
            doctoremail.setText(obj.getString("doctor_email"));
            doctorgender.setText(obj.getString("doctor_gender"));
            doctorphone.setText(obj.getString("doctor_phone_no"));
            doctorworkplace.setText(obj.getString("doctor_workplace"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Error check",e.getMessage());

        }
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
            //super.onBackPressed();
        }
    }
}
