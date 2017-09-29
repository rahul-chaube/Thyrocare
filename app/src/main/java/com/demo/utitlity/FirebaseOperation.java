package com.demo.utitlity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.demo.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

/**
 * Created by rahul on 28/9/17.
 */

public class FirebaseOperation {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String TAG = "FirebaseOperation";
    boolean status = false;


    public FirebaseOperation() {

    }

    public boolean login(final Context context, String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toasty.warning(context, "onAuthStateChanged:signed_in:" + user.getUid(), Toast.LENGTH_SHORT, true).show();
                } else {
                    // User is signed out
                    Toasty.warning(context, "onAuthStateChanged:signed_out", Toast.LENGTH_SHORT, true).show();
                }
                // ...
            }
        };

        Utility.createProgressBar(context);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Utility.hideProgressBar(context);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toasty.error(context, "Login Failed ", Toast.LENGTH_SHORT, true).show();
                            status = false;
                        } else {

                            Toasty.success(context, "Login Success Full", Toast.LENGTH_SHORT, true).show();
                            status = true;

                        }
                        Log.e("abc 111",status+" ");


                        // ...
                    }
                });
        Log.e("abc ",status+" ");
        return status;
    }

}
