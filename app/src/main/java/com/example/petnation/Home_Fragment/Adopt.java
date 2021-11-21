package com.example.petnation.Home_Fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petnation.R;
import com.squareup.picasso.Picasso;

public class Adopt extends AppCompatActivity {

    TextView address,uploader,desc,phone_no;
    ImageView image;

    String add,name,description,phone,imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt);
        address=findViewById(R.id.addressText);
        uploader=findViewById(R.id.uploader);
        phone_no=findViewById(R.id.phone_no);
        image=findViewById(R.id.imageView3);
        desc=findViewById(R.id.DescText);

        Bundle b =getIntent().getExtras();
        if(b!=null){
            add = b.getString("address_key");
            name = b.getString("name_key");
            description = b.getString("desc_key");
            imageUrl = b.getString("image_key");
            phone = b.getString("phone_key");
        }
        address.setText("Address: "+add);
        uploader.setText("Name: "+name);
        desc.setText("Description: "+description);
        phone_no.setText("Phone no.: "+phone);
        Picasso.get().load(imageUrl).into(image);
    }
}