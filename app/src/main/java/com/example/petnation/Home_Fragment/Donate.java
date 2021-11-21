package com.example.petnation.Home_Fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petnation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Donate extends AppCompatActivity {
    TextView address,uploader,desc,phone_no,t;
    ImageView image;
    EditText pc;
    Button donate;
    String add,name,description,phone,imageUrl,userid;
    DatabaseReference ref,ref2;
    FirebaseAuth auth;
    int balance=0;
    int coin_donated,something;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        address=findViewById(R.id.addressText2);
        uploader=findViewById(R.id.uploader2);
        phone_no=findViewById(R.id.phone_no2);
        image=findViewById(R.id.imageView32);
        desc=findViewById(R.id.DescText2);
        donate=findViewById(R.id.donateBtn);
        pc = findViewById(R.id.donated_text);
        auth = FirebaseAuth.getInstance();
        t=findViewById(R.id.textView12);


        Bundle b =getIntent().getExtras();
        if(b!=null){
            add = b.getString("address_key");
            name = b.getString("name_key");
            description = b.getString("desc_key");
            imageUrl = b.getString("image_key");
            phone = b.getString("phone_key");
            userid = b.getString("user_id");
            t.setText(userid);
        }
        address.setText("Address: "+add);
        uploader.setText("Name: "+name);
        desc.setText("Description: "+description);
        phone_no.setText("Phone no.: "+phone);
        Picasso.get().load(imageUrl).into(image);

        if(userid==null){
            Toast.makeText(Donate.this, "userod: "+userid, Toast.LENGTH_SHORT).show();
            t.setText(":(");
        }else{

        ref = FirebaseDatabase.getInstance().getReference("users").child(userid);
        }
        ref2 = FirebaseDatabase.getInstance().getReference("users").child(auth.getCurrentUser().getUid());

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donate_pc();
            }
        });

    }

    private void donate_pc() {


        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                balance=Integer.parseInt(snapshot.child("petcoin").child("balance").getValue().toString());
                coin_donated= Integer.parseInt(pc.getText().toString());
                ref2.child("petcoin").child("balance").setValue(balance-coin_donated);
                if(snapshot.child("petcoin").hasChild("Donated")){
                    int b=Integer.parseInt(snapshot.child("petcoin").child("Donated").getValue().toString());
                    ref2.child("petcoin").child("Donated").setValue(b+coin_donated);
                }else{
                    ref2.child("petcoin").child("Donated").setValue(coin_donated);
                }

                ref.child("petcoin").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        int something2=Integer.parseInt(snapshot2.child("balance").getValue().toString());
                        ref.child("petcoin").child("balance").setValue(coin_donated+something2);
                        Map<String, Object> m = new HashMap<>();

                        if(snapshot2.hasChild("Donations_Recieved")){
                             something=Integer.parseInt(snapshot2.child("Donations_Recieved").getValue().toString());
                            m.put("Donations_Recieved",coin_donated+something);
                            ref.child("petcoin").child("Donations_Recieved").setValue(coin_donated+something);

                            t.setText(""+coin_donated+something);
                        }else{
                            m.put("Donations_Recieved",coin_donated);
                            ref.child("petcoin").updateChildren(m);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        ref.child("petcoin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                ref2.child("petcoin").child("balance")
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}