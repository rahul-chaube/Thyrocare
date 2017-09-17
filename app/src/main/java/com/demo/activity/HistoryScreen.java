package com.demo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.demo.R;
import com.demo.adapter.TestHistoryAdapter;
import com.demo.model.TestInfoModel;
import com.demo.model.TestModel;
import com.demo.utitlity.FirebaseConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryScreen extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference testRef = database.getReference(FirebaseConstant.TestHistory);
    List<TestModel> testModels=new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView= (RecyclerView) findViewById(R.id.historyRecyclerView);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            testRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null)
                    {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()
                             ) {
                            List list=(List)snapshot.child(FirebaseConstant.TestHistoryData).getValue();

                            TestModel testModel=new TestModel();
                            testModel.setName(snapshot.child(FirebaseConstant.TestHistoryName).getValue().toString());
                            testModel.setTime(Long.parseLong(snapshot.child(FirebaseConstant.TestHistoryTime).getValue().toString()));
                            testModel.setComplete(Integer.parseInt(snapshot.child(FirebaseConstant.TestHistoryComplete).getValue().toString()));
                            testModel.setTotalamount(snapshot.child(FirebaseConstant.TestHistoryTotalAmount).getValue().toString());
                            testModel.setData(list);
                            testModels.add(testModel);
                        }
                        initUI();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        initUI();
    }
    void initUI()
    {
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new TestHistoryAdapter(this,testModels));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
