package com.example.healthkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class DiabetesCalculator extends AppCompatActivity {

    private EditText sugarValue;
    private RadioButton before,after;
    private Button CalcBtn;
    private Button ResetBtn;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabetes_calculator);
        sugarValue = (EditText) findViewById(R.id.sugarValue);
        CalcBtn = (Button) findViewById(R.id.CalcBtn);
        ResetBtn = (Button) findViewById(R.id.ResetBtn);
        result = (TextView) findViewById(R.id.result);
        before = (RadioButton) findViewById(R.id.before);
        after = (RadioButton) findViewById(R.id.after);
    }
    public void DiabetesCalculation(View v){
        String sugarStr = sugarValue.getText().toString();
        if(sugarStr != null && !"".equals(sugarStr))
        {
            double sugarvalue = Double.parseDouble(sugarStr);
            displaySugar(sugarvalue);
        }
        else
            Toast.makeText(this,"Incomplete Input",Toast.LENGTH_SHORT).show();

    }

    public void displaySugar(double sugar){
        String sugarLevel = null;
        if(after.isChecked()) {
            if (sugar < 3.90) {
                sugarLevel = "Low";
            } else if (sugar < 7.8) {
                sugarLevel = "Normal";
            } else if (sugar < 8.9) {
                sugarLevel = "Pre Diabetes";
            } else if (sugar >= 8.9) {
                sugarLevel = "Diabetes";
            }
        }
        else if(before.isChecked()){
            if (sugar < 3.90) {
                sugarLevel = "Low";
            } else if (sugar < 6.12) {
                sugarLevel = "Normal";
            } else if (sugar < 6.95) {
                sugarLevel = "Pre Diabetes";
            } else if (sugar >= 6.95) {
                sugarLevel = "Diabetes";
            }
        }
        result.setText(sugarLevel);
    }

    public void ResetMethod(View v){
        sugarValue.setText(null);
        result.setText(null);
    }
}
