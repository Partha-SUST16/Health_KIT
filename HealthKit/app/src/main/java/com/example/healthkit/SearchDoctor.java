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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchDoctor extends AppCompatActivity {

    List<SearchDoctorCardview> doctorList;

    //the recyclerview
    RecyclerView recyclerView;
    SearchDoctorCardviewadapter adapter;

    private EditText searchByCatagory,searchByarea,searchbyName;
    private Button searchByCatagorybtn,searchbyAreabtn,refreshbtn,filterbtn,searchbyNamebtn;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);

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
                    Toast.makeText(SearchDoctor.this,"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }
                else if(id == R.id.menuPrescriptionbtnId)
                {
                    Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    //startActivity(new Intent(getApplicationContext(),MyPrescriptionList.class));
                }
                else if(id == R.id.menuSearchbtnId)
                {
                    Intent intent = new Intent(getApplicationContext(),SearchDoctor.class);
                    startActivity(intent);
                }
                else if(id==R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(SearchDoctor.this,"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(SearchDoctor.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuLogoutbtnId)
                {
                    Toast.makeText(SearchDoctor.this,"Log Out Clicked",Toast.LENGTH_SHORT).show();

                    //patientAuth.signOut();
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    //Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                   // startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                else if(id == R.id.recentButtonId)
                {
                    Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    //startActivity(new Intent(getApplicationContext(),RecentDoctors.class));
                }

                return true;
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //initializing the productlist
        doctorList = new ArrayList<>();

        //creating recyclerview adapter
        adapter = new SearchDoctorCardviewadapter(this, doctorList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        searchByCatagory = (EditText) findViewById(R.id.searchByCatagoryEdittextId);
        searchByarea = (EditText) findViewById(R.id.searchByAreaEdittextId);
        searchByCatagorybtn = (Button) findViewById(R.id.searchByCatagoryBtnId);
        searchbyAreabtn = (Button) findViewById(R.id.searchByAreaBtnId);
        refreshbtn = (Button) findViewById(R.id.RefreshBtnId);
        filterbtn = (Button) findViewById(R.id.FilterBtnId);
        searchbyName = findViewById(R.id.searchByNameEdittextId);
        searchbyNamebtn=findViewById(R.id.searchByNameBtnId);



        doctorList.clear();

        getAllDoctors();
        //doctorDatabaseReference.addListenerForSingleValueEvent(valueEventListener);
        searchbyNamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDoctorbyName();
            }
        });

        searchByCatagorybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getDoctorbyCatagory();
            }
        });

        searchbyAreabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                getDoctorbyArea();
            }
        });


        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllDoctors();
            }
        });



        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSearch();

            }
        });

    }
    private void getDoctorbyArea(){
        String area = searchByarea.getText().toString().trim();
        area = area.toLowerCase();
        if(area!=null)
        {
            final String finalArea = area;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.URL_GET_DOCTOR_by_AREA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            doctorList.clear();
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    SearchDoctorCardview doctor = new SearchDoctorCardview();
                                    doctor.name = obj.getString("doctor_name");
                                    doctor.catagory = obj.getString("doctor_catagory");
                                    doctor.email = obj.getString("doctor_email");
                                    doctor.workplace = obj.getString("doctor_workplace");
                                    doctorList.add(doctor);
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
                    params.put("doctor_area", finalArea);
                    return params;
                }

            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
        else {
            Toast.makeText(getApplicationContext(), "Input field Null", Toast.LENGTH_SHORT).show();
            return;}
    }
    private void getDoctorbyName(){
         final String doctor_name = searchbyName.getText().toString().trim().toLowerCase();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_GET_by_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        doctorList.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject obj = (JSONObject) jsonArray.get(i);
                                SearchDoctorCardview doctor = new SearchDoctorCardview();
                                doctor.name = obj.getString("doctor_name");
                                doctor.catagory = obj.getString("doctor_catagory");
                                doctor.email = obj.getString("doctor_email");
                                doctor.workplace = obj.getString("doctor_workplace");
                                doctorList.add(doctor);
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
                params.put("doctor_name", doctor_name);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }


    private void getDoctorbyCatagory(){
        String catagory = searchByCatagory.getText().toString().trim();
        catagory=catagory.toLowerCase();
        if(catagory!=null)
        {
            final String finalArea = catagory;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.URL_GET_DOCTOR_by_CATAGORY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            doctorList.clear();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    SearchDoctorCardview doctor = new SearchDoctorCardview();
                                    doctor.name = obj.getString("doctor_name");
                                    doctor.catagory = obj.getString("doctor_catagory");
                                    doctor.email = obj.getString("doctor_email");
                                    doctor.workplace = obj.getString("doctor_workplace");
                                    doctorList.add(doctor);
                                }
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                Log.d("ErrorCatagory1",e.getMessage());
                                Log.d("ERRORLINECatagory2",response);
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
                            Log.d("ErrorCatagory3",error.getMessage().toString());
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("doctor_catagory", finalArea);
                    return params;
                }

            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
        else {
            Toast.makeText(getApplicationContext(), "Input field Null", Toast.LENGTH_SHORT).show();
            return;}
    }
    private void filterSearch(){
        final String catagory = searchByCatagory.getText().toString().trim().toLowerCase();
        final String area = searchByarea.getText().toString().trim().toLowerCase();

        if(catagory!=null && area!=null)
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.URL_GET_DOCTOR_FILTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            doctorList.clear();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    SearchDoctorCardview doctor = new SearchDoctorCardview();
                                    doctor.name = obj.getString("doctor_name");
                                    doctor.catagory = obj.getString("doctor_catagory");
                                    doctor.email = obj.getString("doctor_email");
                                    doctor.workplace = obj.getString("doctor_workplace");
                                    doctorList.add(doctor);
                                }
                                adapter.notifyDataSetChanged();
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
                    params.put("doctor_catagory", catagory);
                    params.put("doctor_area",area);
                    return params;
                }

            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
        else {
            Toast.makeText(getApplicationContext(), "Input field Null", Toast.LENGTH_SHORT).show();
            return;}
    }
    private void getAllDoctors(){
        StringRequest stringRequest = new StringRequest(
                Constants.URL_GET_ALL_DOCTOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        doctorList.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject obj = (JSONObject) jsonArray.get(i);
                                SearchDoctorCardview doctor = new SearchDoctorCardview();
                                doctor.name = obj.getString("doctor_name");
                                doctor.catagory = obj.getString("doctor_catagory");
                                doctor.email = obj.getString("doctor_email");
                                doctor.workplace = obj.getString("doctor_workplace");
                                doctorList.add(doctor);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.d("Error",e.getMessage());Log.d("ERRORLINE",response);
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
                        Log.d("Error",error.getMessage().toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
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
            //startActivity(new Intent(getApplicationContext(),PatientProfile.class));
            super.onBackPressed();
        }
    }
}
