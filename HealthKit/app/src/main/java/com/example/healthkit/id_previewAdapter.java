package com.example.healthkit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class id_previewAdapter extends RecyclerView.Adapter<id_previewAdapter.MyViewHolder>  {

//    private ItemClickListener clickListener;
    private List<User_Information> obj;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView user_name,location,uid;
        ImageView user_image;

        public MyViewHolder(View itemView)
        {
            super(itemView);


            this.user_name = itemView.findViewById(R.id.id_previewName);

            this.user_image = itemView.findViewById(R.id.id_PreviewUserImage);

            // itemView.setTag(itemView);


        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.id_preview,viewGroup,false);
        id_previewAdapter.MyViewHolder viewHolder = new id_previewAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TextView user_name = holder.user_name;
        //ImageView userImage = holder.user_image;
        User_Information sin = obj.get(position);
        user_name.setText(sin.getName());

//        try {
//            Picasso.get().load(obj.get(position).getGlobal_imageUri()).fit().centerCrop().into(userImage);
//        }catch (Exception e){}

    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    public id_previewAdapter(List<User_Information> obj, Context context)
    {
        this.obj = obj;
        this.context = context;
    }


}
