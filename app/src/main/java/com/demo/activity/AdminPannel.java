package com.demo.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.R;
import com.demo.model.TestInfoModel;
import com.demo.utitlity.FirebaseConstant;
import com.demo.utitlity.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class AdminPannel extends AppCompatActivity {
    EditText editTextTestName,editTextSortDesc,editTextTestDesc,editTextAmount,editTextTimeTake;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference testCaseRef = database.getReference(FirebaseConstant.TESTCASE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pannel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextTestName= (EditText) findViewById(R.id.textName);
        editTextSortDesc= (EditText) findViewById(R.id.testSortDesc);
        editTextTestDesc= (EditText) findViewById(R.id.desc);
        editTextAmount= (EditText) findViewById(R.id.testAmount);
        editTextTimeTake= (EditText) findViewById(R.id.timeTake);
        boolean addAllField=true;
        Button button= (Button) findViewById(R.id.createTest);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkField())
                    addOnFirebase();

            }
        });

    }
    void addOnFirebase()
    {
        Utility.createProgressBar(this);
        TestInfoModel testInfoModel=new TestInfoModel();
        testInfoModel.setTestName(editTextTestName.getText().toString());
        testInfoModel.setSortDesc(editTextSortDesc.getText().toString());
        testInfoModel.setDescription(editTextTestDesc.getText().toString());
        testInfoModel.setAmmount(Double.parseDouble(editTextAmount.getText().toString()));
        testInfoModel.setHours(Integer.parseInt(editTextTimeTake.getText().toString()));
        String key=testCaseRef.push().getKey();
        testCaseRef.child(key).setValue(testInfoModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toasty.success(AdminPannel.this, "Added", Toast.LENGTH_SHORT).show();
             finish();

            }
        });
        Utility.hideProgressBar(this);

    }
    boolean checkField()
    {
        boolean addAllField=true;
        if(editTextTestName.getText().toString().isEmpty())
        {
            Toasty.warning(this, "Enter Test Name", Toast.LENGTH_SHORT).show();
            addAllField=false;
        }
        else  if(editTextSortDesc.getText().toString().isEmpty())
        {
            addAllField=false;
            Toasty.warning(this, "Enter Test Sort Desc", Toast.LENGTH_SHORT).show();
        }
        else  if(editTextTestDesc.getText().toString().isEmpty())
        {
            addAllField=false;
            Toasty.warning(this, "Enter Test Description", Toast.LENGTH_SHORT).show();
        }
        else if(editTextAmount.getText().toString().isEmpty())
        {
            addAllField=false;
            Toasty.warning(this, "Enter Test Amount", Toast.LENGTH_SHORT).show();
        }
        else if(editTextTimeTake.getText().toString().isEmpty())
        {
            addAllField=false;
            Toasty.warning(this, "Enter Test Time", Toast.LENGTH_SHORT).show();
        }
//        else  if(Integer.parseInt(editTextAmount.getText().toString())>=0)
//        {
//            addAllField=false;
//            Toast.makeText(this, "Enter Value more than Zero ", Toast.LENGTH_SHORT).show();
//        }
        return addAllField;
    }
}
