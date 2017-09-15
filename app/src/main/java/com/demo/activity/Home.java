package com.demo.activity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.R;
import com.demo.utitlity.Pref;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;

public class Home extends AppCompatActivity {
    ImageView imageViewProfile,imageViewHistory,imageViewCreateTest;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("userDetail");
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        realm=Realm.getDefaultInstance();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_signOut:
                Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.deleteAll();
                    }
                });

                Pref.setLogin(this,false);
                startActivity(new Intent(this,MainActivity.class));
                finish();
                // Red item was selected
                return true;
            case R.id.menu_admin:
                // Green item was selected
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
