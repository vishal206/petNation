package com.example.petnation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petnation.Home_Fragment.Adopt;
import com.example.petnation.Models.HomeList;
import com.example.petnation.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    List<HomeList> list;
    Context context;

    public ProfileAdapter(List<HomeList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_myuploads,parent,false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int pos) {
        String address = list.get(pos).getAddress();
        String name = list.get(pos).getName();
        String desc = list.get(pos).getDescription();
        String phone = list.get(pos).getPhone();
        String imageUrl = list.get(pos).getImage();
        holder.address.setText(address);
        holder.desc.setText(desc);
        Picasso.get().load(imageUrl).into(holder.image);



    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView uploader, address, desc, phone;
        Button adopt, donate;
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.address_2);
            desc = itemView.findViewById(R.id.description_2);

            image = itemView.findViewById(R.id.roundedImageView_2);
        }
    }
}