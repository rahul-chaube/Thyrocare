package com.demo.activity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.demo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {
    ImageView imageViewProfile,imageViewHistory,imageViewCreateTest;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("userDetail");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageViewProfile= (ImageView) findViewById(R.id.profile);
        imageViewHistory= (ImageView) findViewById(R.id.lastTest);
        imageViewCreateTest= (ImageView) findViewById(R.id.newTest);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,UserProfile.class);
                startActivity(intent);
                finish();
            }
        });
        imageViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,HistoryScreen.class);
                startActivity(intent);
            }
        });
        imageViewCreateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,CreateTestScreen.class);
                startActivity(intent);
            }
        });


    }
}
