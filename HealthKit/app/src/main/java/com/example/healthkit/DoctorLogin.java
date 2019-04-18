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

public class DoctorLogin extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Button signupBtn,loginBtn;
    private EditText doctorEmail,doctorPassword;
    private ProgressDialog progressDialog;
    ProgressDialog loginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

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
                    //Toast.makeText(DoctorLogin.this,"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }

                else if(id==R.id.menuDiabetesbtnId)
                {
                    //Toast.makeText(DoctorLogin.this,"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuLoginbtnId)
                {
                    //Toast.makeText(DoctorLogin.this,"login CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent3 = getIntent();
                    finish();
                    startActivity(intent3);
                }
                else if(id==R.id.menuAboutusbtnId)
                {
                    //Toast.makeText(DoctorLogin.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuExitbtnId)
                {
                    //Toast.makeText(DoctorLogin.this,"Exit CLICKED",Toast.LENGTH_SHORT).show();
                    finish();
                    moveTaskToBack(true);
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    //Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                   // startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                return true;
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        signupBtn = (Button) findViewById(R.id.signupBtnId);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DoctorSignup.class));
            }
        });


        doctorEmail = (EditText) findViewById(R.id.doctorEmailId);
        doctorPassword = (EditText) findViewById(R.id.doctorPasswordId);
        loginProgress = new ProgressDialog(this);

        loginBtn = (Button) findViewById(R.id.loginBtnId);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin();
                //loginProgress.setMessage("Login in progress");
               // loginProgress.show();
            }
        });
    }

    private void userLogin(){
        final String user_email = doctorEmail.getText().toString().trim();
        final String password = doctorPassword.getText().toString().trim();

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_DOCTOR_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                Log.d("DERROR",obj.getString("doctor_email"));
                                Log.d("DERROR",obj.getString("doctor_name"));
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                //obj.getInt("id"),
                                                obj.getString("doctor_name"),
                                                obj.getString("doctor_email")
                                        );
                                Intent i = new Intent(getApplicationContext(), DoctorProfile.class);
                                i.putExtra("information", obj.toString());
                                startActivity(i);
                                Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_LONG).show();
                                //finish();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                                Log.d("Error",obj.getString("message"));
                                Log.d("ERRORLINE",response);
                            }
                        } catch (JSONException e) {
                            Log.d("Error",e.getMessage());Log.d("ERRORLINE",response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

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
                params.put("doctor_email", user_email);
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
            startActivity(new Intent(getApplicationContext(),UserCatagory.class));
            //super.onBackPressed();
        }
    }
}
