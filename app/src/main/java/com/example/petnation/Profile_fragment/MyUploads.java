package com.example.petnation.Profile_fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.petnation.Adapters.HomeAdapter;
import com.example.petnation.Adapters.ProfileAdapter;
import com.example.petnation.Models.HomeList;
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

public class MyUploads extends AppCompatActivity {
    RecyclerView rv;
    ProfileAdapter adapter;
    List<HomeList> list;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_uploads);
        rv=findViewById(R.id.rv_profile_myUploads);


        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        list = new ArrayList<>();
        ref= FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("animals_reported");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyUploads.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setHasFixedSize(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    HomeList hl= ds.getValue(HomeList.class);
                    list.add(hl);
                    adapter = new ProfileAdapter(list,MyUploads.this);
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