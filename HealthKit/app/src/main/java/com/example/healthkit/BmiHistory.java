package com.example.healthkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BmiHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    List<BmiHistoryCardview> dataList;
    BmiHistoryCardviewAdapter adapter;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_history);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();

        //creating recyclerview adapter
        adapter = new BmiHistoryCardviewAdapter(this, dataList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);


        String currentUser=SharedPrefManager.getInstance(getApplicationContext()).getUsername();

        dbReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(currentUser)
                .child("Bmi_Report");

        dbReference.addListenerForSingleValueEvent(valueEventListener);
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            dataList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BmiHistoryCardview dataa = new BmiHistoryCardview();
                    String tempDate =  snapshot.getKey();
                    tempDate = tempDate.substring(0,tempDate.lastIndexOf('_'));
                    dataa.date =  tempDate;
                    String tempResult = snapshot.getValue(String.class);
                    tempResult = tempResult.substring(0,4);
                    dataa.result = tempResult;
                    Log.d("checkkkkkk", "onDataChange: "+dataa.date+"    "+dataa.result);
                    dataList.add(dataa);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
