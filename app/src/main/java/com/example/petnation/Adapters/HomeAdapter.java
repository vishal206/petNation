package com.example.petnation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.petnation.Home_Fragment.Adopt;
//import com.example.petnation.Home_Fragment.Donate;
import com.example.petnation.Home_Fragment.Adopt;
import com.example.petnation.Home_Fragment.Donate;
import com.example.petnation.Models.HomeList;
import com.example.petnation.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    List<HomeList> list;
    Context context;
    Map<String,String> userid;




    public HomeAdapter(List<HomeList> list, Context context, Map<String,String> userid) {
        this.list = list;
        this.context = context;
        this.userid=userid;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list,parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int pos) {
        String address = list.get(pos).getAddress();
        String name = list.get(pos).getName();
        String desc = list.get(pos).getDescription();
        String phone = list.get(pos).getPhone();
        String imageUrl = list.get(pos).getImage();
//        holder.address.setText(address);
        holder.uploader.setText(name);
        holder.desc.setText(desc);
//        holder.phone.setText(phone);
        Picasso.get().load(imageUrl).into(holder.image);


        holder.donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Donate.class);
                intent.putExtra("name_key", name);
                intent.putExtra("user_id", userid.get(name));
                intent.putExtra("address_key", address);
                intent.putExtra("image_key", imageUrl);
                intent.putExtra("desc_key",desc);
                intent.putExtra("phone_key", phone);
                context.startActivity(intent);
            }
        });


        holder.adopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Adopt.class);
                intent.putExtra("name_key", name);
                intent.putExtra("address_key", address);
                intent.putExtra("image_key", imageUrl);
                intent.putExtra("desc_key",desc);
                intent.putExtra("phone_key", phone);
                context.startActivity(intent);
            }

        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView uploader, address, desc, phone;
        Button adopt;
        ImageButton donate;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            uploader = itemView.findViewById(R.id.uploaded_by_name);
            address = itemView.findViewById(R.id.address_2);
            desc = itemView.findViewById(R.id.description_2);
//            phone = itemView.findViewById(R.id.phone);
            image = itemView.findViewById(R.id.roundedImageView_2);
            adopt=itemView.findViewById(R.id.adopt_btn);
            donate= itemView.findViewById(R.id.donate_btn);
        }
    }
}
