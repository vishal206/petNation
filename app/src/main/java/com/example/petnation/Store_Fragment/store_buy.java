package com.example.petnation.Store_Fragment;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class store_buy extends AppCompatActivity {
    int state,n;
    EditText quantity;
    Button pay;
    TextView amount,food_name,food_brand,food_desc,food_amount,text;
    ImageView food_img2;
    DatabaseReference ref;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_buy);
        auth = FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference("users").child(auth.getCurrentUser().getUid());
        food_img2 = findViewById(R.id.food_img2);
        pay = findViewById(R.id.button2);
        quantity = findViewById(R.id.quantity_text);
        amount = findViewById(R.id.textView18);
        food_name = findViewById(R.id.food_name);
        food_desc = findViewById(R.id.food_desc);
        food_brand = findViewById(R.id.food_brand);
        food_amount = findViewById(R.id.food_amount);
        text = findViewById(R.id.textView69);
//        text.setText("");
        Bundle b = getIntent().getExtras();
        state=1;
        if (b != null) {
            state=b.getInt("food_btn");
        }
        if(state==1){
//            int id = getResources().getIdentifier("foodone", "drawable", getPackageName());
            food_img2.setImageResource(R.drawable.foodone);
            food_name.setText("Dog Food");
            food_amount.setText("Amount: 50 pC");
            food_brand.setText("Brand: Drools");
            food_desc.setText("Description: Lorem Ipsum");

        }
        if(state==2){
//            int id = getResources().getIdentifier("foodtwo", "drawable", getPackageName());
//            food_img2.setImageResource(id);
            food_img2.setImageResource(R.drawable.foodtwo);
            food_name.setText("Cat Food");
            food_amount.setText("Amount: 100 pC");
            food_brand.setText("Brand: Meow Mix");
            food_desc.setText("Description: Lorem Ipsum");
        }
        String q = quantity.getText().toString().trim();
        pay.setOnClickListener(new View.OnClickListener() {
            Map<String, Object> foodMap = new HashMap<String, Object>();
            @Override
            public void onClick(View v) {
                String q = quantity.getText().toString().trim();
                if(q.isEmpty()) {
                    quantity.setError("Required Field!");
                    quantity.requestFocus();
                    return;
                }
                ref.child("petcoin").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String q = quantity.getText().toString().trim();
                        int n1 = Integer.parseInt(q);
                        foodMap.put("quantity",q);

                        int i= Integer.parseInt(snapshot.child("balance").getValue().toString());
                        int j;
                        if(state==1){
                            j=i-n1*50;
//                            i=n1*50;
                            amount.setText("Pay: "+n1*50);
                            foodMap.put("amount",String.valueOf(n1*50));
                            foodMap.put("Name","Dog Food");
                            foodMap.put("Brand","Drools");

                        }else{
                            j=i-n1*100;
//                            i=n1*100;
                            amount.setText("Pay: "+n1*100);
                        }
                        if(j<0){
                            Toast.makeText(store_buy.this, "Insufficient PetCoins in your account", Toast.LENGTH_SHORT).show();
                        }else{
//                            amount.setText("Pay: "+i);
                            ref.child("petcoin").child("balance").setValue(j);
                            Toast.makeText(store_buy.this, "Ordered!", Toast.LENGTH_SHORT).show();
                            text.setText("Your order will be delivered within few days!");
                        }

                        ref.child("orders").push().setValue(foodMap);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}