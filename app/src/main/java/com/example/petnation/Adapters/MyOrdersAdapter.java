package com.example.petnation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petnation.Models.HomeList;
import com.example.petnation.Models.OrderList;
import com.example.petnation.R;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.OrderViewHolder> {
    List<OrderList> list;

    public MyOrdersAdapter(List<OrderList> list) {
        this.list = list;
    }

    public MyOrdersAdapter() {
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        String foodname = list.get(position).getName();
        String foodbrand = list.get(position).getBrand();
        String foodquan = list.get(position).getQuantity();
        String foodprice = list.get(position).getAmount();
        holder.name.setText(foodname);
        holder.brand.setText("Brand: "+foodbrand);
        holder.quan.setText("Quantity: "+foodquan);
        holder.amount.setText("Price(pC): "+foodprice);
        if(foodname.equals("Dog Food")){
            holder.img.setImageResource(R.drawable.foodone);
        }else{
            holder.img.setImageResource(R.drawable.foodtwo);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView name, brand, amount, quan;
        ImageView img;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.order_name);
            brand = itemView.findViewById(R.id.order_brand);
            quan = itemView.findViewById(R.id.order_quantity);
            amount = itemView.findViewById(R.id.order_amount);
            img = itemView.findViewById(R.id.imageView8);
        }
    }
}
