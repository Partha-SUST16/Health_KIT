package com.example.healthkit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchDoctorCardviewadapter extends RecyclerView.Adapter<SearchDoctorCardviewadapter.SearchDoctorCardviewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<SearchDoctorCardview> productList;

    //getting the context and product list with constructor
    public SearchDoctorCardviewadapter(Context mCtx, List<SearchDoctorCardview> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public SearchDoctorCardviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_search_doctor_cardview, null);
        return new SearchDoctorCardviewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchDoctorCardviewHolder holder, final int position) {
        //getting the product of the specified position
        final SearchDoctorCardview product = productList.get(position);

        //binding the data with the viewholder views

        holder.textViewTitle.setText(product.name);
        holder.textViewCatagory.setText(product.catagory);
        holder.textViewHospital.setText(product.workplace);

        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx,"You have clicked: "+product.name, Toast.LENGTH_SHORT).show();
                emni(product.email);
            }
        });

    }

    void emni(final String email){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GET_DOCTOR_by_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                Log.d("DERROR",obj.getString("doctor_email"));
                                Log.d("DERROR",obj.getString("doctor_name"));
                                Intent i = new Intent(mCtx, DoctorProfileFromPatient.class);
                                i.putExtra("information", obj.toString());
                                mCtx.startActivity(i);
                                Toast.makeText(mCtx,"Successfull", Toast.LENGTH_LONG).show();
                                //finish();
                            }else{
                                Toast.makeText(
mCtx,                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                                Log.d("Error",obj.getString("message"));
                                Log.d("ERRORLINE",response);
                            }
                        } catch (JSONException e) {
                            Log.d("Error",e.getMessage());
                            Log.d("ERRORLINE",response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(
                               mCtx,
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                        Log.d("Error",error.getMessage().toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("doctor_email", email);

                return params;
            }

        };

        RequestHandler.getInstance(mCtx).addToRequestQueue(stringRequest);
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class SearchDoctorCardviewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout;

        TextView textViewTitle, textViewCatagory,textViewHospital;
        ImageView imageView;

        public SearchDoctorCardviewHolder(View itemView) {
            super(itemView);


            textViewTitle = itemView.findViewById(R.id.DoctorNameId);
            textViewCatagory = itemView.findViewById(R.id.DoctorCatagoryId);
            textViewHospital = itemView.findViewById(R.id.DoctorHospitalId);
            imageView = itemView.findViewById(R.id.imageView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }



    }

}