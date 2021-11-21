package com.example.petnation.Profile_fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.petnation.Adapters.MyOrdersAdapter;
import com.example.petnation.Adapters.ProfileAdapter;
import com.example.petnation.Models.HomeList;
import com.example.petnation.Models.OrderList;
import com.example.petnation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {
    RecyclerView rv;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    FirebaseUser mUser;
    List<OrderList> list;
    MyOrdersAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        rv=findViewById(R.id.myorders_rv);
        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        list = new ArrayList<>();
        ref= FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("orders");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyOrders.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setHasFixedSize(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    OrderList ol= ds.getValue(OrderList.class);
                    list.add(ol);
                    adapter = new MyOrdersAdapter(list);
                    adapter.notifyDataSetChanged();
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}