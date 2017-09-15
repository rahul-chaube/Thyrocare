package com.demo.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.demo.R;
import com.demo.adapter.TestListAdapter;
import com.demo.model.TestInfoModel;
import com.demo.model.TestList;
import com.demo.utitlity.Pref;

import io.realm.Realm;
import io.realm.RealmResults;

public class CreateTestScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    TestListAdapter testListAdapter = new TestListAdapter();
    RecyclerView.LayoutManager layoutManager;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test_screen);
        realm = Realm.getDefaultInstance();
        recyclerView = (RecyclerView) findViewById(R.id.testList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RealmResults<TestList> testInfoModels = realm.where(TestList.class).findAll();
        testListAdapter = new TestListAdapter(CreateTestScreen.this, testInfoModels);
        recyclerView.setAdapter(testListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checkout, menu);
        return true;
    }
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menucheckout:
                Toast.makeText(this, "Checkout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Checkout.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
