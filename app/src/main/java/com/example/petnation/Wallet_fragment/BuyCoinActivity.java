package com.example.petnation.Wallet_fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petnation.MainActivity;
import com.example.petnation.Models.User;
import com.example.petnation.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BuyCoinActivity extends AppCompatActivity implements PaymentResultListener{

    EditText edt_amount;
    TextView txt_balance;
    Button btn_payment;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    int balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coin);

        edt_amount=findViewById(R.id.edt_amount);
        btn_payment=findViewById(R.id.btn_payment);
        txt_balance=findViewById(R.id.txt_balance);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference=FirebaseDatabase.getInstance().getReference("users");

        setEdt();
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(edt_amount.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Amount can't be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    startPayment();
//                    setBalance();
                }
            }
        });
    }

    private void startPayment() {



        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u=snapshot.getValue(User.class);
                String email=u.getEmail();
                String phone=u.getPhone();

                razorPayit(email,phone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void razorPayit(String email, String phone) {
        Checkout co=new Checkout();

        co.setKeyID("rzp_test_VoqS6Oniu7XpkV");
        try {

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("name","PETNATION");
//            orderRequest.put("amount", 50000); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
//            orderRequest.put("receipt", "order_rcptid_11");

            String payment=edt_amount.getText().toString();
            double total=Double.parseDouble(payment);
            total=total*100;
            orderRequest.put("Amount",total);

            JSONObject preFill=new JSONObject();
            preFill.put("email",email);
            preFill.put("contact",phone);

            orderRequest.put("prefill",preFill);

            co.open(this ,orderRequest);

        }catch (Exception e){

        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), "payment sucess", Toast.LENGTH_SHORT).show();
        setBalance();
    }

    @Override
    public void onPaymentError(int i, String s) {

    }


    private void setEdt() {
        String uid=mAuth.getCurrentUser().getUid();
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("petcoin"))
                {
                    txt_balance.setText(snapshot.child("petcoin").child("balance").getValue().toString());
                }
                else
                {
                    txt_balance.setText("0");
                    addDetails(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setBalance() {
        String uid=mAuth.getCurrentUser().getUid();
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    balance=Integer.parseInt(snapshot.child("petcoin").child("balance").getValue().toString());
                    int amount=Integer.parseInt(edt_amount.getText().toString());
                    if(amount<=0)
                        Toast.makeText(getApplicationContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show();
                    else{
                        amount+=balance;
                        addDetails(amount);
//                        Toast.makeText(getApplicationContext(), amount+"", Toast.LENGTH_SHORT).show();
                    }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void addDetails(int amount) {
        Map<String, Object> m = new HashMap<>();
        m.put("balance",amount);
        String uid=mAuth.getCurrentUser().getUid();
        reference.child(uid).child("petcoin").updateChildren(m).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BuyCoinActivity.this, MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "not updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}