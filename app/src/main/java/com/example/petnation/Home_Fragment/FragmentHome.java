package com.example.petnation.Home_Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.petnation.Adapters.HomeAdapter;
import com.example.petnation.Models.HomeList;
import com.example.petnation.Models.User;
import com.example.petnation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    
    RecyclerView rv;
    ProgressBar progressBar;

    HomeAdapter adapter;
    DatabaseReference ref,ref2;
    List<HomeList> list;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String key,user_name;
    Map<String,String> userKey;

    public FragmentHome() {
        // Required empty public constructor
    }
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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

        View v=inflater.inflate(R.layout.fragment_home, container, false);
        rv=v.findViewById(R.id.rv_home);
        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        list = new ArrayList<>();
        userKey = new HashMap<>();
        ref= FirebaseDatabase.getInstance().getReference().child("users");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setHasFixedSize(true);
        ref2=FirebaseDatabase.getInstance().getReference().child("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                for(DataSnapshot snapshot : snap.getChildren()){

                    key = snapshot.getKey();

                    if(!mUser.getUid().equals(key)){
                        ref2.child(key).child("animals_reported").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snap2) {

                                for(DataSnapshot ds: snap2.getChildren()){
                                    key = snapshot.getKey();
                                    User u= snapshot.getValue(User.class);
                                    userKey.put(u.getName(),key);
                                    Log.v("user_id",u.getName()+key);
                                    HomeList hl= ds.getValue(HomeList.class);
                                    list.add(hl);
                                    Log.v("user_id",key);
//                                    adapter = new HomeAdapter(list,getActivity(), userKey);
//                                    adapter.notifyDataSetChanged();
//                                    rv.setAdapter(adapter);
                                }
                                adapter = new HomeAdapter(list,getActivity(), userKey);
                                adapter.notifyDataSetChanged();
                                rv.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }

}