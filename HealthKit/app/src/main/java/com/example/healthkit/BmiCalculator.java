package com.example.healthkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BmiCalculator extends AppCompatActivity {

    private EditText heightInputFeet;
    private EditText heightInputInch;
    private EditText weightInput;
    private TextView resultOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);
        heightInputFeet = (EditText) findViewById(R.id.heightInputFeet);
        heightInputInch = (EditText) findViewById(R.id.heightInputInch);
        weightInput = (EditText) findViewById(R.id.weightInput);
        resultOutput = (TextView) findViewById(R.id.resultOutput);
    }

    public void BmiCalculation(View v){
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

            displayBMI(bmi,parfectWeightMan,parfectWeightWoman);
        }
        else
        {
            Toast.makeText(this,"Incomplete Input",Toast.LENGTH_SHORT).show();
        }
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
    public void resetMethod(View v){
        resultOutput.setText(null);
        heightInputInch.setText(null);
        heightInputFeet.setText(null);
        weightInput.setText(null);
    }
}
