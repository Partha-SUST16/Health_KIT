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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DoctorSignup extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private EditText emailvalue,passwordvalue,namevalue,gendervalue,agevalue,workplacevalue,degreevalue,catagoryvalue,areavalue,phonevalue;
    private Button signUp,gotoLogin;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup);

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
                    Toast.makeText(DoctorSignup.this,"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }
                else if(id==R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(DoctorSignup.this,"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuLoginbtnId)
                {
                    Toast.makeText(DoctorSignup.this,"login CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent3 = new Intent(getApplicationContext(),PatientLogin.class);
                    startActivity(intent3);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(DoctorSignup.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                }
                else if(id==R.id.menuExitbtnId)
                {
                    Toast.makeText(DoctorSignup.this,"Exit CLICKED",Toast.LENGTH_SHORT).show();
                    finish();
                    moveTaskToBack(true);
                }
                return true;
            }
        });
        emailvalue = (EditText) findViewById(R.id.doctorEditEmailId);
        passwordvalue = (EditText) findViewById(R.id.doctorEditPasswordId);
        namevalue = (EditText) findViewById(R.id.doctorEditNameId);
        gendervalue =(EditText) findViewById(R.id.doctorEditGenderId);
        agevalue= (EditText) findViewById(R.id.doctorEditAgeId);
        catagoryvalue = (EditText) findViewById(R.id.doctorEditCatagoryId);
        workplacevalue = (EditText) findViewById(R.id.doctorEditWorkplaceId);
        degreevalue = (EditText) findViewById(R.id.doctorEditDegreeId);
        areavalue = (EditText) findViewById(R.id.doctoreditAreaId);
        phonevalue = (EditText) findViewById(R.id.doctorEditphoneId);

        signUp = (Button) findViewById(R.id.doctorSignUpBtnId);
        gotoLogin = (Button) findViewById(R.id.gotoLoginBtnId);
        progressDialog = new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDoctor();
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(),DoctorLogin.class));
            }
        });
    }
    private void registerDoctor(){
        final String name = namevalue.getText().toString().trim();
        final String gender = gendervalue.getText().toString().trim();
        final String age = agevalue.getText().toString().trim();
        final String catagory = catagoryvalue.getText().toString().trim().toLowerCase();
//        catagory = catagory.toLowerCase();
        final String area = areavalue.getText().toString().trim().toLowerCase();

        final String phone = phonevalue.getText().toString().trim();
        final String email = emailvalue.getText().toString().trim();
        final String workplace = workplacevalue.getText().toString().trim();
        final String degree = degreevalue.getText().toString().trim();

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_DOCTORS_REGISTER,
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
                params.put("doctor_name", name);
//                params.put("doctor_blood", blood);
                params.put("doctor_email", email);
                params.put("doctor_age", age);
                params.put("doctor_gender", gender);
                params.put("doctor_phone_no", phone);
                params.put("doctor_area", area);
                params.put("password", passwordvalue.getText().toString());
                params.put("doctor_workplace",workplace);
                params.put("doctor_catagory",catagory);
                params.put("doctor_degree",degree);
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
            startActivity(new Intent(getApplicationContext(),DoctorLogin.class));
            //super.onBackPressed();
        }
    }
}
