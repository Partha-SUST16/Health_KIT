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

public class DoctorScheduleDaySelection extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FirebaseAuth doctorAuth;

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_schedule_day_selection);
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
                    //Toast.makeText(DoctorScheduleDaySelection.this,"Schedule Clicked",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DoctorScheduleDaySelection.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    //Toast.makeText(DoctorScheduleDaySelection.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuLogoutbtnId)
                {
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                    startActivity(new Intent(getApplicationContext(),DoctorLogin.class));
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    //Toast.makeText(getApplicationContext(),"We will implement it soon",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                else if(id == R.id.chatDoctor)
                {
                    // Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),messageListDoctor.class));
                }
                return true;
            }
        });
        doctorAuth = FirebaseAuth.getInstance();



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
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleEdit.class);
                        intent.putExtra("day","saturday");
                        startActivity(intent);
                        break;
                    }
                    case 1:
                    {
                        Toast.makeText(getApplicationContext(),"Sunday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleEdit.class);
                        intent.putExtra("day","sunday");
                        startActivity(intent);
                        break;
                    }
                    case 2:
                    {
                        Toast.makeText(getApplicationContext(),"Monday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleEdit.class);
                        intent.putExtra("day","monday");
                        startActivity(intent);
                        break;
                    }
                    case 3:
                    {
                        Toast.makeText(getApplicationContext(),"Tuesday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleEdit.class);
                        intent.putExtra("day","tuesday");
                        startActivity(intent);
                        break;
                    }
                    case 4:
                    {
                        Toast.makeText(getApplicationContext(),"Wednesday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleEdit.class);
                        intent.putExtra("day","wednesday");
                        startActivity(intent);
                        break;
                    }
                    case 5:
                    {
                        Toast.makeText(getApplicationContext(),"Thursday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleEdit.class);
                        intent.putExtra("day","thursday");
                        startActivity(intent);
                        break;
                    }
                    case 6:
                    {
                        Toast.makeText(getApplicationContext(),"Friday is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),DoctorScheduleEdit.class);
                        intent.putExtra("day","friday");
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
           // startActivity(new Intent(getApplicationContext(),DoctorProfile.class));
            super.onBackPressed();
        }
    }
}
