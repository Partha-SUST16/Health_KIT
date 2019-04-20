package com.example.healthkit;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BmiCalculator extends AppCompatActivity {

    private EditText heightInputFeet;
    private EditText heightInputInch;
    private EditText weightInput;
    private TextView resultOutput;
    private Button calc,save,reset,history;
    private String currentBmi = null;

    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);
        heightInputFeet = (EditText) findViewById(R.id.heightInputFeet);
        heightInputInch = (EditText) findViewById(R.id.heightInputInch);
        weightInput = (EditText) findViewById(R.id.weightInput);
        resultOutput = (TextView) findViewById(R.id.resultOutput);
        calc = (Button) findViewById(R.id.calcBtn);
        reset = (Button) findViewById(R.id.resetBtn);
        save = (Button) findViewById(R.id.saveData);
        history = (Button) findViewById(R.id.bmiHistorybtn);


        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String heightStrfeet = heightInputFeet.getText().toString();
                String heightStrinch = heightInputInch.getText().toString();
                String weightStr = weightInput.getText().toString();
                if(heightStrfeet != null && !"".equals(heightStrfeet) && weightStr!=null && !"".equals(weightStr)&& heightStrinch != null && !"".equals(heightStrinch))
                {
                    double heightValueFeet = Double.parseDouble(heightStrfeet);
                    double heightValueInch = Double.parseDouble(heightStrinch);
                    double heightValue = (heightValueFeet*12)+heightValueInch;
                    double weightValue = Double.parseDouble(weightStr);
                    double bmi = (weightValue/(heightValue*heightValue*0.0254*0.0254));
                    double parfectWeightMan = 56.2+(heightValue-60)*1.4;
                    double parfectWeightWoman = 53.1+(heightValue-60)*1.35;
                    currentBmi = Double.toString(bmi);

                    displayBMI(bmi,parfectWeightMan,parfectWeightWoman);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Incomplete Input",Toast.LENGTH_SHORT).show();
                }
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultOutput.setText(null);
                heightInputInch.setText(null);
                heightInputFeet.setText(null);
                weightInput.setText(null);
            }
        });
        String currentUser=SharedPrefManager.getInstance(getApplicationContext()).getUsername();

        dbReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(currentUser)
                .child("Bmi_Report");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String strDate = dateFormat.format(date);
                String firstPart = strDate.substring(0,strDate.lastIndexOf(' '));
                String secondPart = strDate.substring(strDate.indexOf(' ')+1,strDate.length());
                Log.d("Anyyyyyyyyyyyyyyyy","onClick: "+firstPart+"          "+secondPart);
                String finaldate = firstPart+"_"+secondPart;

                if(!TextUtils.isEmpty(resultOutput.getText().toString()))
                {
                    dbReference.child(finaldate).setValue(currentBmi);
                }
                else{
                    Toast.makeText(BmiCalculator.this, "Error Input", Toast.LENGTH_SHORT).show();
                }

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),BmiHistory.class);
                startActivity(intent);
            }
        });
    }

    public void displayBMI(double bmi,double parfectWeightMan,double parfectWeightWoman){
        String bmiLevel=null;
        if(bmi<=15)
        {
            bmiLevel = "Very Severely Underweight";
        }
        else if(bmi>15 && bmi<=16)
        {
            bmiLevel = "Severely Underweight";
        }
        else if(bmi>16 && bmi<=18.5)
        {
            bmiLevel = "Underweight";
        }
        else if(bmi>18.5 && bmi<=25)
        {
            bmiLevel = "Normal";
        }
        else if(bmi>25 && bmi<=30)
        {
            bmiLevel = "OverWeight";
        }
        else if(bmi>30 && bmi<=35)
        {
            bmiLevel = "Obese Level 1";
        }
        else if(bmi>35 && bmi<=40)
        {
            bmiLevel="Obese Level 2";
        }
        else if(bmi>40)
        {
            bmiLevel = "Obese Level 3";
        }

        bmiLevel = "Your BMI:\n"+String.format("%.2f",bmi) +"\n\nYour Shape:\n"+bmiLevel +"\n\nPerfect Weight (Man):\n"+String.format("%.2f",parfectWeightMan)+" Kg"+"\n\nPerfect Weight (Woman): \n"+String.format("%.2f",parfectWeightWoman)+" Kg";
        resultOutput.setText(bmiLevel);
    }
}
