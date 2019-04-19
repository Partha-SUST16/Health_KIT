package com.example.healthkit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.example.healthkit.Message_View.sendername;

public class msg_adapter extends RecyclerView.Adapter<msg_adapter.MyViewHolder> {


    private List<chat_message_obj> cmo;
    String id = "";

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView leftText,rightText,name;
        private ImageView chatImage;
       // private CircleImageView chatImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.chatName);
            rightText = (TextView) itemView.findViewById(R.id.rightText);
            leftText = (TextView)itemView.findViewById(R.id.leftText);
            chatImage = itemView.findViewById(R.id.chatImage);


        }
    }
    public msg_adapter(List<chat_message_obj> cmo, String id)
    {
        this.cmo = cmo;
        this.id = id;
    }

    @NonNull
    @Override
    public msg_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull msg_adapter.MyViewHolder holder, int position) {

        chat_message_obj cmo1 = cmo.get(position);
        if(cmo1.getUsername().equalsIgnoreCase(sendername))
        {
            holder.rightText.setText(cmo1.getMessage());
            holder.name.setVisibility(View.GONE);
            holder.rightText.setVisibility(View.VISIBLE);
            holder.leftText.setVisibility(View.GONE);
           holder.chatImage.setVisibility(View.GONE);
        }
        else
        {
            holder.chatImage.setVisibility(View.VISIBLE);
            holder.leftText.setText(cmo1.getMessage());
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(cmo1.getUsername());
            holder.rightText.setVisibility(View.GONE);
            holder.leftText.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return cmo.size();
    }
}