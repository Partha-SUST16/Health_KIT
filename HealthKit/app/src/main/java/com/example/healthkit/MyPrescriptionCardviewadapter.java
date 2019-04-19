package com.example.healthkit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

public class MyPrescriptionCardviewadapter extends RecyclerView.Adapter<MyPrescriptionCardviewadapter.MyPrescriptionCardviewHolder>{

    private Context mCtx;

    //we are storing all the products in a list
    private List<MyPrescriptionCardview> productList;

    //getting the context and product list with constructor
    public MyPrescriptionCardviewadapter(Context mCtx, List<MyPrescriptionCardview> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public MyPrescriptionCardviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.set_prescroiption_cardview, null);
        return new MyPrescriptionCardviewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyPrescriptionCardviewHolder holder, final int position) {
        //getting the product of the specified position
        final MyPrescriptionCardview product = productList.get(position);

        //binding the data with the viewholder views
        holder.textNo.setText("Medicine No: "+product.medicineNo);
        holder.textMedicineName.setText("Name: "+product.medicineName);
        holder.textDuration.setText("Duration: "+product.duration);
        holder.textSuggestion.setText("Suggestion: "+product.suggestion);

        if(product.morning.equals("true"))
        {
            holder.textMorning.setChecked(true);
            holder.textMorning.setClickable(false);
        }
        if(product.noon.equals("true"))
        {
            holder.textNoon.setChecked(true);
            holder.textNoon.setClickable(false);
        }
        if(product.night.equals("true"))
        {
            holder.textNight.setChecked(true);
            holder.textNight.setClickable(false);
        }
        if(product.beforeMeal.equals("true"))
        {
            holder.textBefore.setChecked(true);
            holder.textBefore.setClickable(false);
        }
        if(product.afterMeal.equals("true"))
        {
            holder.textAfter.setChecked(true);
            holder.textAfter.setClickable(false);
        }


        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(mCtx,"You have clicked: "+product.name,Toast.LENGTH_SHORT).show();
                Intent intent  = new Intent(mCtx,DoctorProfileFromPatient.class);
                intent.putExtra("doctorEmail",product.email);
                mCtx.startActivity(intent);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class MyPrescriptionCardviewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout;

        TextView textMedicineName;
        RadioButton textMorning,textNoon,textNight,textBefore,textAfter;
        TextView textDuration,textSuggestion,textNo;

        public MyPrescriptionCardviewHolder(View itemView) {
            super(itemView);

            textNo = itemView.findViewById(R.id.medicineNoId);
            textMedicineName = itemView.findViewById(R.id.medicineNameId);
            textMorning = itemView.findViewById(R.id.medicineInMorningId);
            textNoon = itemView.findViewById(R.id.medicineInNoonId);
            textNight = itemView.findViewById(R.id.medicineInNightId);
            textBefore = itemView.findViewById(R.id.medicineBeforeId);
            textAfter = itemView.findViewById(R.id.medicineAfterId);
            textDuration = itemView.findViewById(R.id.medicineDurationId);
            textSuggestion = itemView.findViewById(R.id.medicineSuggestionId);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }



    }
}
