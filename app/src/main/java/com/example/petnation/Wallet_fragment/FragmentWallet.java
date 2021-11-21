package com.example.petnation.Wallet_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.petnation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentWallet extends Fragment {

    public Button btn_buyCoin;
    public TextView txt_wallet_balance;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wallet, container, false);

        btn_buyCoin=view.findViewById(R.id.btn_buyCoin);
        txt_wallet_balance=view.findViewById(R.id.txt_wallet_balance);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference=FirebaseDatabase.getInstance().getReference("users");
        
        setBalance();
        btn_buyCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),BuyCoinActivity.class));

            }
        });

        return view;
    }

    private void setBalance() {
        String uid=mAuth.getCurrentUser().getUid();
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("petcoin"))
                {
                    txt_wallet_balance.setText(snapshot.child("petcoin").child("balance").getValue().toString());
                }
                else
                {
                    txt_wallet_balance.setText("00000");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}