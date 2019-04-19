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

public class SetPrescriptionCardviewadapter extends RecyclerView.Adapter<SetPrescriptionCardviewadapter.SetPrescriptionCardviewHolder>{
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<SetPrescriptionCardview> productList;

    //getting the context and product list with constructor
    public SetPrescriptionCardviewadapter(Context mCtx, List<SetPrescriptionCardview> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public SetPrescriptionCardviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.set_prescroiption_cardview, null);
        return new SetPrescriptionCardviewHolder(view);
    }
    @Override
    public void onBindViewHolder(SetPrescriptionCardviewHolder holder, final int position) {
        //getting the product of the specified position
        final SetPrescriptionCardview product = productList.get(position);

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


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class SetPrescriptionCardviewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout;

        TextView textMedicineName;
        RadioButton textMorning,textNoon,textNight,textBefore,textAfter;
        TextView textDuration,textSuggestion,textNo;

        public SetPrescriptionCardviewHolder(View itemView) {
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
