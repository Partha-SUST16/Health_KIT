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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DoctorProfileFromPatient extends AppCompatActivity {

    private TextView doctorname,doctorage,doctorarea,doctorcatagory,doctoremail,doctorgender,doctorphone,doctorworkplace,doctordegree;
    private TextView doctormessage;
    private static String TAG = "DoctorProfileFromPatient";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Button reserveSchedulebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_from_patient);

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
                   SharedPrefManager.getInstance(getApplicationContext()).logout();
                   startActivity(new Intent(getApplicationContext(),DoctorLogin.class));

                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                else if(id == R.id.recentButtonId)
                {
                    // Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),RecentDoctors.class));
                }

                return true;
            }
        });
        doctorname = (TextView) findViewById(R.id.doctorNameId);
        doctorage = (TextView) findViewById(R.id.doctorAgeId);
        doctorgender = (TextView) findViewById(R.id.doctorGenderId);
        doctorarea = (TextView) findViewById(R.id.doctorAreaId);
        doctorcatagory = (TextView) findViewById(R.id.doctorCatagoryId);
        doctorphone = (TextView) findViewById(R.id.doctorPhoneId);
        doctoremail = (TextView) findViewById(R.id.doctorEmailId);
        doctorworkplace = (TextView) findViewById(R.id.doctorWorkplaceId);
        doctordegree = (TextView) findViewById(R.id.doctorDegreeId);
        doctormessage = (TextView) findViewById(R.id.message_doctor);
        ini();
        doctormessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DoctorProfileFromPatient.this,Message_View.class);
                i.putExtra("doctor_name",doctorname.getText().toString().trim());
                i.putExtra("doctor_email",doctoremail.getText().toString().trim());
                i.putExtra("flag",0);
                startActivity(i);
            }
        });
        reserveSchedulebtn = (Button)findViewById(R.id.reserveScheduleBtnId);
        reserveSchedulebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savetoAppointmentTable();
                Intent intent = new Intent(getApplicationContext(),DoctorScheduleWeek.class);
                String email = doctoremail.getText().toString().trim();
                email = email.substring(0,email.lastIndexOf('@'));
                intent.putExtra("doctorUID",email);
                startActivity(intent);

            }
        });
    }
    private void savetoAppointmentTable(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_SET_TO_APPOINTMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"HI++ "+e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.e("MAinActivity",response);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("doctor_email", doctoremail.getText().toString().trim());
//                params.put("doctor_blood", blood);
                params.put("patient_email",SharedPrefManager.getInstance(getApplicationContext()).getUserEmail());
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
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
            Log.d("EHAT IS NAME",obj.getString("doctor_workplace"));
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
            // startActivity(new Intent(getApplicationContext(),SearchDoctor.class));
            super.onBackPressed();
        }
    }
}
