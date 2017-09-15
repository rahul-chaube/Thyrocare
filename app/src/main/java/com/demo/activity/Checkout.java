package com.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.demo.R;
import com.demo.model.TestList;

import junit.framework.Test;

import io.realm.Realm;
import io.realm.RealmResults;

public class Checkout extends AppCompatActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        realm=Realm.getDefaultInstance();
        String arr[]={"-Ku5F4_ZUqft7mVXa7TX","-Ku5F4d7QXUS0A_ZJgWn","-Ku5bzdOrPlYDXHTCGcg"};
        RealmResults<TestList> testLists=realm.where(TestList.class).in("testId",arr).findAll();
        for (TestList testList:testLists
             ) {
            Log.e(" Checkout  ",testList.getTestName()+ " "+testList.getHours());
        }
    }
}
