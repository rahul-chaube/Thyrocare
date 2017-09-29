package com.demo.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class ChangePassword extends AppCompatActivity {
    Button button;
    EditText editTextPassword, editTextConfPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String TAG = "ChangePassword ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mAuth = FirebaseAuth.getInstance();
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
        button = (Button) findViewById(R.id.btnSave);
        editTextConfPassword = (EditText) findViewById(R.id.editTextConformPassword);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextPassword.getText().toString().isEmpty() && !editTextConfPassword.getText().toString().isEmpty()) {

                    if (editTextPassword.getText().toString().equals(editTextConfPassword.getText().toString())) {
                        changePassword(editTextPassword.getText().toString().trim());
                    } else {
                        Toasty.warning(ChangePassword.this, "Password not match ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePassword.this, " Please Fill all Field", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    void changePassword(String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(password.trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toasty.success(ChangePassword.this, "Password is updated!.", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toasty.error(ChangePassword.this, "Failed to update Password!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });

    }
}
