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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorbyHospital extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    List<SearchDoctorCardview> doctorList;
    private String key;

    //the recyclerview
    RecyclerView recyclerView;
    SearchDoctorCardviewadapter adapter;
    TextView ambulence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorby_hospital);
        drawerLayout = (DrawerLayout) findViewById(R.id.ppDrawerId);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ambulence = findViewById(R.id.ambulenceNO);
        NavigationView navigationView = (NavigationView) findViewById(R.id.ppNavigationViewId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.menuBMIbtnId)
                {
                    Toast.makeText(DoctorbyHospital.this,"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }
                else if(id == R.id.menuPrescriptionbtnId)
                {
                    // startActivity(new Intent(getApplicationContext(),MyPrescriptionList.class));
                }
                else if(id == R.id.menuSearchbtnId)
                {
                    Intent intent = new Intent(getApplicationContext(),SearchDoctor.class);
                    startActivity(intent);
                }
                else if(id==R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(DoctorbyHospital.this,"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(DoctorbyHospital.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuLogoutbtnId)
                {
                    Toast.makeText(DoctorbyHospital.this,"Log Out Clicked",Toast.LENGTH_SHORT).show();
                    finish();

                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    //startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }

                return true;
            }
        });

        ///Drawer & NavigationBar ends.





        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //initializing the productlist
        doctorList = new ArrayList<>();

        //creating recyclerview adapter
        adapter = new SearchDoctorCardviewadapter(this, doctorList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
        ini();
        getA();
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
            startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
            //super.onBackPressed();
        }

    }
    private void getA(){
        String user_email = getIntent().getStringExtra("title");
        user_email = user_email.substring(0,user_email.lastIndexOf(':'));
        Log.d("dekha jak",user_email);
        final String finalUser_email = user_email;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_GET_AMBULENCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        doctorList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")){
                                ambulence.setText("Ambulence: "+jsonObject.getString("ambulence_number"));
                            } else {
                                ambulence.setText("No Available info");
                            }
                        } catch (JSONException e) {
                            Log.d("ErrorArea1",e.getMessage());Log.d("ERRORLINEAREA2",response);
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
                        Log.d("ErrorArea3",error.getMessage().toString());
                        ambulence.setText("No Available info");
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("hospital_name", finalUser_email);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void ini(){
        String user_email = getIntent().getStringExtra("title");
        user_email = user_email.substring(0,user_email.lastIndexOf(':'));
        Log.d("dekha jak",user_email);
        final String finalUser_email = user_email;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_GET_by_HOSPITAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        doctorList.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = (JSONObject) jsonArray.get(i);//new JSONObject(response);
                                SearchDoctorCardview doctor = new SearchDoctorCardview();
                                doctor.name = obj.getString("doctor_name");
                                doctor.catagory = obj.getString("doctor_catagory");
                                doctor.email = obj.getString("doctor_email");
                                doctor.workplace = obj.getString("doctor_workplace");
                                doctorList.add(doctor);
                                adapter.notifyDataSetChanged();
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.d("ErrorArea1",e.getMessage());Log.d("ERRORLINEAREA2",response);
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
                        Log.d("ErrorArea3",error.getMessage().toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("doctor_workplace", finalUser_email);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
