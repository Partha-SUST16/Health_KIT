package com.example.healthkit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthkit.Utils.LetterImageView;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorScheduleWeek extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ListView listView;
    private FirebaseAuth patientAuth;
    String doctorUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_schedule_week);
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

                if(id==R.id.menuBMIbtnId)
                {
                    Toast.makeText(getApplicationContext(),"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }
                else if(id == R.id.menuPrescriptionbtnId)
                {
                    //Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

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
                    finish();
                    patientAuth.signOut();
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    //Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                else if(id == R.id.recentButtonId)
                {
                    //Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),RecentDoctors.class));
                }

                return true;
            }
        });
        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
        {
            return;
        }
        doctorUID = (String) bundle.get("doctorUID");
        listView = (ListView) findViewById(R.id.dayListviewId);
        setUpListview();
    }
    private void setUpListview()
    {
        String[] week = getResources().getStringArray(R.array.Week);
        WeekAdapter adapter = new WeekAdapter(this,R.layout.single_day_item,week);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                    {
                        Toast.makeText(getApplicationContext(),"Saturday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleCheck.class);
                        intent.putExtra("day","saturday");
                        intent.putExtra("doctorUID",doctorUID);
                        startActivity(intent);
                        break;
                    }
                    case 1:
                    {
                        Toast.makeText(getApplicationContext(),"Sunday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleCheck.class);
                        intent.putExtra("day","sunday");
                        intent.putExtra("doctorUID",doctorUID);
                        startActivity(intent);
                        break;
                    }
                    case 2:
                    {
                        Toast.makeText(getApplicationContext(),"Monday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleCheck.class);
                        intent.putExtra("day","monday");
                        intent.putExtra("doctorUID",doctorUID);
                        startActivity(intent);
                        break;
                    }
                    case 3:
                    {
                        Toast.makeText(getApplicationContext(),"Tuesday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleCheck.class);
                        intent.putExtra("day","tuesday");
                        intent.putExtra("doctorUID",doctorUID);
                        startActivity(intent);
                        break;
                    }
                    case 4:
                    {
                        Toast.makeText(getApplicationContext(),"Wednesday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleCheck.class);
                        intent.putExtra("day","wednesday");
                        intent.putExtra("doctorUID",doctorUID);
                        startActivity(intent);
                        break;
                    }
                    case 5:
                    {
                        Toast.makeText(getApplicationContext(),"Thursday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleCheck.class);
                        intent.putExtra("day","thursday");
                        intent.putExtra("doctorUID",doctorUID);
                        startActivity(intent);
                        break;
                    }
                    case 6:
                    {
                        Toast.makeText(getApplicationContext(),"Friday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleCheck.class);
                        intent.putExtra("day","friday");
                        intent.putExtra("doctorUID",doctorUID);
                        startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }
    public class WeekAdapter extends ArrayAdapter {
        private int resources;
        private LayoutInflater layoutInflater;
        String[] week = new String[]{};
        public WeekAdapter(@NonNull Context context, int resource, String[] objects) {
            super(context, resource, objects);
            this.resources=resource;
            this.week=objects;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null)
            {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(resources,null);
                holder.dayLogo = (LetterImageView) convertView.findViewById(R.id.letterImageviewId);
                holder.dayName = (TextView)convertView.findViewById(R.id.DayNameId);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.dayLogo.setOval(true);
            holder.dayLogo.setLetter(week[position].charAt(0));
            holder.dayName.setText(week[position]);
            return convertView;
        }
        public class ViewHolder{
            private LetterImageView dayLogo;
            private TextView dayName;
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
            //startActivity(new Intent(getApplicationContext(),DoctorProfileFromPatient.class));
            super.onBackPressed();
        }
    }
}
