package com.example.healthkit;
import android.app.Dialog;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Button emergencyBtn;

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayoutId);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.NavigationViewId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.menuBMIbtnId)
                {
                    Toast.makeText(MainActivity.this,"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }
                else if(id==R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(MainActivity.this,"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuLoginbtnId)
                {
                    Toast.makeText(MainActivity.this,"login CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent3 = new Intent(getApplicationContext(),UserCatagory.class);
                    startActivity(intent3);
                }
                else if(id==R.id.menuAboutusbtnId)
                {
                    Toast.makeText(MainActivity.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),AboutUs.class);
                    startActivity(intent);
                }
                else if(id==R.id.menuExitbtnId)
                {
                    Toast.makeText(MainActivity.this,"Exit CLICKED",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    Toast.makeText(getApplicationContext(),"We will implement it soon",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                return true;
            }
        });


        if(isServiceOk())
        {
            emergencyBtn = (Button) findViewById(R.id.EmergencyBtnId);
            emergencyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
            });
        }

    }


    public boolean isServiceOk(){
        Log.d(TAG, "isServiceOk: Checking Google Service Version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        if(available == ConnectionResult.SUCCESS)
        {
            Log.d(TAG, "isServiceOk: Google Play Service is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG, "isServiceOk: Fixable Error occured");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Can't Connect to Map",Toast.LENGTH_SHORT).show();
        }
        return true;
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
            finish();
            moveTaskToBack(true);
            //super.onBackPressed();
        }
    }
}