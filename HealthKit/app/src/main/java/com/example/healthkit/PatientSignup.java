package com.example.healthkit;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PatientSignup extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private EditText emailvalue,passwordvalue,namevalue,gendervalue,agevalue,bloodvalue,areavalue,phonevalue;
    private Button signUp,gotoLogin;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_signup);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutId);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.NavigationViewId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.menuBMIbtnId)
                {
                    Toast.makeText(PatientSignup.this,"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }
                else if(id==R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(PatientSignup.this,"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuLoginbtnId)
                {
                    Toast.makeText(PatientSignup.this,"login CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent3 = new Intent(getApplicationContext(),PatientLogin.class);
                    startActivity(intent3);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(PatientSignup.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                }
                else if(id==R.id.menuExitbtnId)
                {
                    Toast.makeText(PatientSignup.this,"Exit CLICKED",Toast.LENGTH_SHORT).show();
                    finish();
                    moveTaskToBack(true);
                }
                return true;
            }
        });

        emailvalue = (EditText) findViewById(R.id.patientEditEmailId);
        passwordvalue = (EditText) findViewById(R.id.patientEditPasswordId);
        namevalue = (EditText) findViewById(R.id.patientEditNameId);
        gendervalue =(EditText) findViewById(R.id.parientEditGenderId);
        agevalue= (EditText) findViewById(R.id.patientEditAgeId);
        bloodvalue = (EditText) findViewById(R.id.patientEditBloodId);
        areavalue = (EditText) findViewById(R.id.patienteditAreaId);
        phonevalue = (EditText) findViewById(R.id.patientEditphoneId);

        signUp = (Button) findViewById(R.id.patientSignUpBtnId);
        gotoLogin = (Button) findViewById(R.id.gotoLoginBtnId);
        progressDialog = new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                FirebaseDatabase.getInstance().getReference().child(namevalue.getText().toString())
                        .child("device_token").setValue(deviceToken);
                registerPatient();

            }
        });
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(),PatientLogin.class));
            }
        });
    }
    private void registerPatient(){

        final String email = emailvalue.getText().toString().trim();
        final String password=passwordvalue.getText().toString().trim();
        final String name = namevalue.getText().toString().trim();
        final String gender = gendervalue.getText().toString().trim();
        final String age = agevalue.getText().toString().trim();
        final String blood = bloodvalue.getText().toString().trim();
        final String area = areavalue.getText().toString().trim();
        final String phone = phonevalue.getText().toString().trim();


        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_PATIENTS_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            finish();

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
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("patient_name", name);
                params.put("patient_blood", blood);
                params.put("patient_email", email);
                params.put("patient_age", age);
                params.put("patient_gender", gender);
                params.put("patient_phone_no", phone);
                params.put("patient_area", area);
                params.put("password", password);
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
            startActivity(new Intent(getApplicationContext(),PatientLogin.class));
            //super.onBackPressed();
        }
    }
}
