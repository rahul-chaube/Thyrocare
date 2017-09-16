package com.demo.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.MyBroadcastReceiver;
import com.demo.R;
import com.demo.adapter.TestListAdapter;
import com.demo.model.TestInfoModel;
import com.demo.model.TestList;
import com.demo.model.TestModel;
import com.demo.utitlity.FirebaseConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Checkout extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference testHistory = database.getReference(FirebaseConstant.TestHistory);
    int completed;
    Realm realm;
    RecyclerView recyclerView;
    TextView textViewAmount;
    Button button;
    TestListAdapter testListAdapter=new TestListAdapter();
    RecyclerView.LayoutManager layoutManager;
    double totalAmount;
    List<TestInfoModel> tests=new ArrayList<>();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        recyclerView= (RecyclerView) findViewById(R.id.selectedTestRecyclerView);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        textViewAmount= (TextView) findViewById(R.id.textViewTotalAmount);
        button= (Button) findViewById(R.id.buttonSubmit);

        realm=Realm.getDefaultInstance();
        Bundle b=this.getIntent().getExtras();
        String[] array=b.getStringArray(FirebaseConstant.PASSTESTLIST);
        ArrayList<Integer> temp=new ArrayList<>();
        RealmResults<TestList> testLists=realm.where(TestList.class).in("testId",array).findAll();

        for (TestList testList:testLists
             ) {
            TestInfoModel testInfoModel=new TestInfoModel();
            testInfoModel.setTestName(testList.getTestName());
            testInfoModel.setAmmount(testList.getAmmount());
            testInfoModel.setDescription(testList.getDescription());
            testInfoModel.setSortDesc(testList.getSortDesc());
            testInfoModel.setHours(testList.getHours());
            tests.add(testInfoModel);
            totalAmount=totalAmount+testList.getAmmount();
            temp.add(testList.getHours());
            Log.e(" Checkout  ",testList.getTestName()+ " "+testList.getHours());
        }
        completed= Collections.max(temp);
        textViewAmount.setText("Total :"+totalAmount);
        testListAdapter=new TestListAdapter(Checkout.this,testLists,1);
        recyclerView.setAdapter(testListAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOnFirebase();
            }
        });

    }

    void saveOnFirebase()
    {

        TestModel testModel=new TestModel();
        testModel.setName("Test "+ (int) (Math.random() * 1000));
        testModel.setTotalamount(String.valueOf(totalAmount));
        testModel.setComplete(completed);
        testModel.setTime(System.currentTimeMillis());
        testModel.setData(tests);
        String key=testHistory.push().getKey();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            testHistory.child(user.getUid()).child(key).setValue(testModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    createNotification();
                    Toast.makeText(Checkout.this, " Test Added ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Checkout.this,Home.class));
                    finish();
                }
            });
        }

    }
    void createNotification()
    {

        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (completed * 100), pendingIntent);
        Toast.makeText(this, "Alarm is Set ", Toast.LENGTH_SHORT).show();

    }
}
