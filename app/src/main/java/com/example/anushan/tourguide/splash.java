package com.example.anushan.tourguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Anushan on 5/11/2018.
 */

public class splash extends Activity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                firebaseAuth=FirebaseAuth.getInstance();
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser!=null) {
                    usercheck();
                }

            }
        }, secondsDelayed * 1000);
    }
    public void usercheck() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long rolenumber = (Long) dataSnapshot.child("role").getValue();

                if (rolenumber == 0) {
                    Intent intent = new Intent(splash.this, UserMainDashboard.class);
                    startActivity(intent);
                } else if(rolenumber==1) {
                    Intent intent = new Intent(splash.this, AdminActivity.class);
                    startActivity(intent);


                }else {
                    Intent intent = new Intent(splash.this, LoginActivity.class);
                    startActivity(intent);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}