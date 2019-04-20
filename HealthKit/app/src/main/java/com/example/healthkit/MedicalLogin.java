package com.example.healthkit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MedicalLogin extends AppCompatActivity {

    private EditText email,pass;
    private Button btn;
    private GpsTracker gpsTracker;
    double lati,longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_login);
        btn = findViewById(R.id.loginBtnId);
        email = findViewById(R.id.doctorEmailId);
        pass = findViewById(R.id.doctorPasswordId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation(v);
                register();


            }
        });
    }
    void register(){
        final String m_email = email.getText().toString().trim();
        String m_pass = pass.getText().toString().trim();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(m_email,m_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent i = new Intent(getApplicationContext(),Circle.class);
                    i.putExtra("Lati",Double.toString(lati));
                    i.putExtra("Longi",Double.toString(longi));
                    startActivity(i);
                   // Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(),task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void addAllUser(){
        FirebaseDatabase.getInstance().getReference().child("Doctors").addValueEventListener(valueEventListener);
        FirebaseDatabase.getInstance().getReference().child("Patients").addValueEventListener(valueEventListener);
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String em = snapshot.getKey().toString();
                    User driver = new User();
                    driver.email = em;
                    Log.d("Medical",driver.email);
                    FirebaseDatabase.getInstance().getReference().child("Medical").child("alluser").push().setValue(driver);
                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };
    public void getLocation(View view){
        gpsTracker = new GpsTracker(getApplicationContext());
        if(gpsTracker.canGetLocation()){
            lati = gpsTracker.getLatitude();
            longi= gpsTracker.getLongitude();

        }else{
            gpsTracker.showSettingsAlert();
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}