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
import com.demo.model.UserDetail;
import com.demo.utitlity.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.realm.Realm;

public class Registration extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText editTextEmail,editTextPassword;
    Button btnLogin;
    TextView textViewLogin;
    String TAG="LoginScreen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail= (EditText) findViewById(R.id.editTextPassword);
        editTextPassword= (EditText) findViewById(R.id.editTextConformPassword);
        btnLogin= (Button) findViewById(R.id.btnForget);
        textViewLogin = (TextView) findViewById(R.id.textViewRegistor);
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
                if (!editTextEmail.getText().toString().isEmpty()&& !editTextPassword.getText().toString().isEmpty())
                {
                    if(Utility.isNetworkAvailable(Registration.this))
                        createNewUser(editTextEmail.getText().toString().trim(),editTextPassword.getText().toString().trim());
                    else
                        Toast.makeText(Registration.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Registration.this, " Please Fill all Field", Toast.LENGTH_SHORT).show();
                }
            }
        });
                textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this, MainActivity.class);
                startActivity(i);
                finish();
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
    void createNewUser(String email,String password)
    {
        Utility.createProgressBar(this);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Utility.hideProgressBar(Registration.this);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Registration.this, "Failed to to create new user "+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
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
                                Intent intent = new Intent(Registration.this, UserProfile.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        // ...
                    }
                });



    }

}
