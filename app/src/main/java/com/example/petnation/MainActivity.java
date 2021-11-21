package com.example.petnation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.petnation.Home_Fragment.FragmentHome;
import com.example.petnation.Profile_fragment.FragmentProfile;
import com.example.petnation.Store_Fragment.FragmentStore;
import com.example.petnation.Wallet_fragment.FragmentWallet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    FragmentHome fragment1=new FragmentHome();
    FragmentStore fragment2=new FragmentStore();
    FragmentAdd fragment3=new FragmentAdd();
    FragmentWallet fragment4=new FragmentWallet();
    FragmentProfile fragment5=new FragmentProfile();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment1).commit();
                return true;
            case R.id.store:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment2).commit();
                return true;
            case R.id.add:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment3).commit();
                return true;
            case R.id.wallet:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment4).commit();
                return true;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment5).commit();
                return true;
        }
        return false;
    }
}