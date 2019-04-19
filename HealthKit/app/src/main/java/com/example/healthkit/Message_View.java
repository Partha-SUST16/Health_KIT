package com.example.healthkit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Message_View extends AppCompatActivity {

    private String  doctor_email, doctor_name, rev_username, username;
    List<chat_message_obj> msgList;
    public static String sendername;
    private RecyclerView recyclerView;
    private EditText messageET;
    private ImageView sendButton;
    private DatabaseReference patientD, doctorD,first,secend;
    private msg_adapter ma;
    private int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__view);
        msgList = new ArrayList<>();
        doctor_email = getIntent().getStringExtra("doctor_email");
        doctor_name = getIntent().getStringExtra("doctor_name");
        flag = getIntent().getExtras().getInt("flag");
        String db = doctor_email.substring(0,doctor_email.lastIndexOf('@'));
        String email = SharedPrefManager.getInstance(getApplicationContext()).getUserEmail();
        email=email.substring(0,email.lastIndexOf('@'));
        Log.d("MESSAGE_VIEW",Integer.toString(flag));
        Log.d("MESSAGE VIEW ",db);
        Log.d("MESSAGE_VIEW",email);
        if(flag==0){
            first = FirebaseDatabase.getInstance().getReference().child("Patients")
                    .child(email)
                    ;
        }
        else {
            first = FirebaseDatabase.getInstance().getReference().child("Doctors")
                    .child(email)
                    ;
        }
        sendername = SharedPrefManager.getInstance(getApplicationContext()).getUsername();
        recyclerView = (RecyclerView) findViewById(R.id.MessageRecyclerView);
        messageET = (EditText) findViewById(R.id.MessageEditText);
        sendButton = (ImageView) findViewById(R.id.MessageSendButton);

        ma = new msg_adapter(msgList,sendername);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ma);

        if(flag==0){
            patientD = FirebaseDatabase.getInstance().getReference().child("Patients")
                    .child(email)
                    .child("message").child(db);
        }else {
            patientD = FirebaseDatabase.getInstance().getReference().child("Doctors")
                    .child(email)
                    .child("message").child(db);
        }
        if(flag==0) {
            doctorD = FirebaseDatabase.getInstance().getReference().child("Doctors")
                    .child(db).child("message")
                    .child(email);
        }else{
            doctorD = FirebaseDatabase.getInstance().getReference().child("Patients")
                    .child(db).child("message")
                    .child(email);
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageET.getText().toString().trim();
                hideKeyboardwithoutPopulate(Message_View.this);
                if (!message.equals("")) {

                    chat_message_obj chatMessage = new chat_message_obj(message, sendername);
                    patientD.push().setValue(chatMessage);
                    doctorD.push().setValue(chatMessage);
//                    msgList.add(chatMessage);
//                    ma.notifyDataSetChanged();
                }
                messageET.setText("");
            }
        });
        try {
            patientD.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    msgList.add(dataSnapshot.getValue(chat_message_obj.class));
                    ma.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception e){

        }
    }
    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
