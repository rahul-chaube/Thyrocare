package com.demo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.demo.R;
import com.demo.adapter.TestListAdapter;
import com.demo.model.TestList;
import com.demo.model.UserDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class TestDetailActivity extends AppCompatActivity {
    private static final String TestName = "testName";
    private static final String Complete = "complete";
    private static final String TotalAmount = "amount";
    private static final String Data = "data";
    private static final String Time = "time";
    String name, totalAmount;
    String arr[];
    long time;
    int completeHours;
    RecyclerView recyclerView;
    TextView textViewPatientName, textViewAmount, textViewDate;
    Realm realm;
    TestListAdapter testListAdapter = new TestListAdapter();
    UserDetail user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        realm = Realm.getDefaultInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = realm.where(UserDetail.class).findFirst();
        recyclerView = (RecyclerView) findViewById(R.id.detailRecyclerView);
        textViewPatientName = (TextView) findViewById(R.id.patientName);
        textViewAmount = (TextView) findViewById(R.id.testAmount);
        textViewDate = (TextView) findViewById(R.id.testDate);
        name = getIntent().getStringExtra(TestName);
        textViewPatientName.setText(user.getUserName());
        completeHours = getIntent().getIntExtra(Complete, 0);
        totalAmount = getIntent().getStringExtra(TotalAmount);
        arr = getIntent().getStringArrayExtra(Data);
        time = getIntent().getLongExtra(Time, 0L);
        textViewAmount.setText(totalAmount);
        String myFormat = "MMM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(time);
        textViewDate.setText(sdf.format(calendar.getTime()));
        Log.e("Detail ############## n", " Name" + name + " Array " + arr.toString() + " " + completeHours);
        initUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sendmail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareTestDetail:
                // Green item was selected
                sendMail();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void sendMail() {
        String myFormat = "MMM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.getTime();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email[] = {user.getEmail()};

            String Hello = "Hi " + this.user.getUserName() + " \n\n\n\n\n" +
                    "This is Testing Demo mail  \n" +
                    "Developer Rahul Chaube     \n " +
                    "Check below for test Detal \n\n " +
                    "Test Date                  " + sdf.format(calendar.getTime()) + "" +
                    "\n\n\n\n\n\n\n\n";
            Hello += "Test Name  \t\t       Time \t\t   Test Amount\n\n ";

            RealmResults<TestList> testLists = realm.where(TestList.class).in("testId", arr).findAll();
            for (TestList testList : testLists
                    ) {
                Hello += testList.getTestName() + " \t\t     " + testList.getHours() + "  \t\t   " + testList.getAmmount() + "\n\n ";
            }

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));

            intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Testing");
            intent.putExtra(Intent.EXTRA_TEXT, Hello);

/* Send it off to the Activity-Chooser */
            startActivity(Intent.createChooser(intent, "ContactUs"));
        }
    }

    void initUI() {
        RealmResults<TestList> testLists = realm.where(TestList.class).in("testId", arr).findAll();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (testLists != null)
            recyclerView.setAdapter(new TestListAdapter(getBaseContext(), testLists, 1));
    }
}
