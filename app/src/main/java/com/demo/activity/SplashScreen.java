package com.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.demo.R;
import com.demo.utitlity.Pref;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                redirect();
            }
        }, 2000);
    }
    void redirect()
    {

        if(Pref.isLogin(this))
        {
            Intent intent=new Intent(this,Home.class);
            startActivity(intent);
        }
        else
        {
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
