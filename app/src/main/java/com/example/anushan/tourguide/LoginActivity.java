package com.example.anushan.tourguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText Email,Password;
    private TextView Forget_Password;
    private Button Login,Register;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();




        setContentView(R.layout.activity_login);


        Email=(EditText)findViewById(R.id.email);
        Password=(EditText)findViewById(R.id.password);
        Forget_Password=(TextView) findViewById(R.id.forgetpassword);
        Login=(Button) findViewById(R.id.login);
        Register=(Button) findViewById(R.id.register);

        progressDialog=new ProgressDialog(LoginActivity.this);



        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        Forget_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this, PasswordResetActivity.class);
                startActivity(intent);

            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(Email.getText().toString())) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (TextUtils.isEmpty(Password.getText().toString())) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressDialog.setMessage("Login in wait.........");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                                    databaseReference.child("User").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.w("fffff", String.valueOf(dataSnapshot));
                                            Long rolenumber= (Long) dataSnapshot.child("role").getValue();
                                            if(rolenumber==0){
                                                Log.d("Success", "signInWithEmail:success");
                                                Intent intent =new Intent(LoginActivity.this, UserMainDashboard.class);
                                                startActivity(intent);
                                                if (progressDialog.isShowing()) {
                                                    progressDialog.dismiss();
                                                }
                                                finish();
                                            }else {
                                                Log.d("Success", "signInWithEmail:success");
                                                Intent intent =new Intent(LoginActivity.this, AdminActivity.class);
                                                startActivity(intent);
                                                if (progressDialog.isShowing()) {
                                                    progressDialog.dismiss();
                                                }
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Failed", "signInWithEmail:failure", task.getException());
                                    Email.setText("");
                                    Password.setText("");
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        });
            }
        });


    }




    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}
