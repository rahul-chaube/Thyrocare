package com.demo.activity;

import android.content.Intent;
import android.os.Debug;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.R;
import com.demo.model.TestList;
import com.demo.utitlity.FirebaseConstant;
import com.demo.utitlity.Pref;
import com.demo.utitlity.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Home extends AppCompatActivity {
    ImageView imageViewProfile,imageViewHistory,imageViewCreateTest;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference testRef = database.getReference(FirebaseConstant.TESTCASE);
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        realm=Realm.getDefaultInstance();
        imageViewProfile= (ImageView) findViewById(R.id.profile);
        imageViewHistory= (ImageView) findViewById(R.id.lastTest);
        imageViewCreateTest= (ImageView) findViewById(R.id.newTest);
        getTestList();
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
                Intent intent=new Intent(this,AdminPannel.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void getTestList()
   {
       testRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot snapshot:dataSnapshot.getChildren()
                       ) {
                   final TestList testList=new TestList();
                   testList.setTestId(snapshot.getKey());
                   testList.setTestName(snapshot.child(FirebaseConstant.TestName).getValue().toString());
                   testList.setSortDesc(snapshot.child(FirebaseConstant.SORTDESC).getValue().toString());
                   testList.setDescription(snapshot.child(FirebaseConstant.DESCRIPTION).getValue().toString());
                   testList.setAmmount(Double.parseDouble(snapshot.child(FirebaseConstant.TESTAmmount).getValue().toString()));
                   testList.setHours(Integer.parseInt(snapshot.child(FirebaseConstant.HOUSRS).getValue().toString()));
                   realm.executeTransaction(new Realm.Transaction() {
                       @Override
                       public void execute(Realm realm) {
                           realm.insertOrUpdate(testList);
                       }
                   });
               }

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
       RealmResults<TestList> testLists=realm.where(TestList.class).findAllAsync();
       for (TestList  testList:testLists
            ) {
           Log.e("********** ",testList.getTestId()+"                 " +testList.getTestName());
       }
   }


}
