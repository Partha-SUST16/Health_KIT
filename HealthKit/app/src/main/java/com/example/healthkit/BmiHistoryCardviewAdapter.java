package com.example.healthkit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class BmiHistoryCardviewAdapter extends RecyclerView.Adapter<BmiHistoryCardviewAdapter.BmiHistoryCardviewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<BmiHistoryCardview> productList;

    //getting the context and product list with constructor
    public BmiHistoryCardviewAdapter(Context mCtx, List<BmiHistoryCardview> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public BmiHistoryCardviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_bmi_history_cardview, null);
        return new BmiHistoryCardviewHolder(view);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    @Override
    public void onBindViewHolder(BmiHistoryCardviewHolder holder, final int position) {
        //getting the product of the specified position
        final BmiHistoryCardview product = productList.get(position);

        //binding the data with the viewholder views

        holder.dateText.setText(product.date);
        holder.resultText.setText(product.result);

        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));


    }
    class BmiHistoryCardviewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout;

        TextView dateText,resultText;
        public BmiHistoryCardviewHolder(View itemView) {
            super(itemView);


            dateText = itemView.findViewById(R.id.dateID);
            resultText = itemView.findViewById(R.id.resultID);

        }



    }
}
