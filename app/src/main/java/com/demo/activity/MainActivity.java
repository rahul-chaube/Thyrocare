package com.demo.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.R;
import com.demo.utitlity.Pref;
import com.demo.utitlity.Utility;
import com.demo.model.UserDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText editTextEmail, editTextPassword;
    Button btnLogin;
    TextView textViewRegistor,textViewForget;

    String TAG = "LoginScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword = (EditText) findViewById(R.id.editTextConformPassword);
        textViewForget= (TextView) findViewById(R.id.textViewForget);
        btnLogin = (Button) findViewById(R.id.btnForget);
        textViewRegistor = (TextView) findViewById(R.id.textViewRegistor);
        textViewForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgetPassword.class));
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.e(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.e(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextEmail.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty()) {
//                    FirebaseOperation firebaseOperation=new FirebaseOperation();
//                    firebaseOperation.login(this,editTextEmail.getText().toString().trim(),editTextPassword.getText().toString().trim())
                    if (Utility.isNetworkAvailable(MainActivity.this))
                        login(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim());
                    else
                        Toasty.warning(MainActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, " Please Fill all Field", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textViewRegistor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void login(String email, String password) {
        Utility.createProgressBar(this);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Utility.hideProgressBar(MainActivity.this);
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toasty.error(MainActivity.this, "Login Failed ", Toast.LENGTH_SHORT, true).show();

                        } else {
                            profile();
                            Toasty.success(MainActivity.this, "Login Success Full", Toast.LENGTH_SHORT, true).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    void profile() throws NullPointerException {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String name = user.getDisplayName();
            final String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            final String uid = user.getUid();

            Realm realm = Realm.getDefaultInstance();
//            final UserDetail userDetail=realm.where(UserDetail.class).findFirst();
            final UserDetail userDetail1 = new UserDetail();
            userDetail1.setUserEmail(email);
            userDetail1.setUserName(name);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(UserDetail.class);
                    realm.insertOrUpdate(userDetail1);
                }
            });

            Pref.setLogin(this, true);
            Intent intent = new Intent(MainActivity.this, UserProfile.class);
            startActivity(intent);
            finish();
        }
    }

}
