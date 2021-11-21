package com.example.petnation.Profile_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.petnation.Authentication_Activities.ActivityLogin;
import com.example.petnation.Models.User;
import com.example.petnation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentProfile extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DatabaseReference reference;
    FirebaseAuth auth;
    TextView nameText,emailText,phoneText,logout,received_don,donated;
    Button my_uploads,orders;
    int dr;

    private String mParam1;
    private String mParam2;

    public FragmentProfile() {
        // Required empty public constructor
    }

    public static FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        String name="",phone="",email="";
        nameText = v.findViewById(R.id.name_text);
        orders = v.findViewById(R.id.order_btn);
        emailText = v.findViewById(R.id.email_text);
        phoneText = v.findViewById(R.id.phone_text);
        logout = v.findViewById(R.id.logout);
        donated = v.findViewById(R.id.donated_btn);
        received_don = v.findViewById(R.id.recieved_btn);
        my_uploads = v.findViewById(R.id.my_animals);
        auth = FirebaseAuth.getInstance();
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MyOrders.class));
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("users").child(auth.getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                nameText.setText("Username: "+user.getName());
                emailText.setText("Email Id: "+user.getEmail());
                phoneText.setText("Phone no.: "+user.getPhone());
                if(snapshot.child("petcoin").hasChild("Donations_Recieved")){
                     dr= Integer.parseInt(snapshot.child("petcoin").child("Donations_Recieved").getValue().toString());
                }else{
                    dr=0;
                }
                donated.setText("Donations Recieved: "+dr);
                int dr2;
                if(snapshot.child("petcoin").hasChild("Donated")){
                    dr2= Integer.parseInt(snapshot.child("petcoin").child("Donated").getValue().toString());
                }else{
                    dr2=0;
                }
                received_don.setText("Donated Petcoins: "+dr2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        my_uploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyUploads.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getActivity(), ActivityLogin.class));

            }
        });
        return v;
    }
}