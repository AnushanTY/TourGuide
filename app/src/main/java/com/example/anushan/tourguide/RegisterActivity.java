package com.example.anushan.tourguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText Email,Password,ConfrimPassword,UserName;
    private Button Register;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String firebaseUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Email=(EditText)findViewById(R.id.Regemail);
        Password=(EditText)findViewById(R.id.Regpassword);
        ConfrimPassword=(EditText)findViewById(R.id.RegConfirmpassword);
        UserName=(EditText)findViewById(R.id.userName);

        Register=(Button) findViewById(R.id.RegRegister);

        progressDialog=new ProgressDialog(RegisterActivity.this);

        firebaseAuth=FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
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
                if (Password.getText().toString().length()<6) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Enter password should more than 6 char!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ConfrimPassword.getText().toString())) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Enter Confrim Password!", Toast.LENGTH_SHORT).show();

                    return;
                }
                if (!ConfrimPassword.getText().toString().equals(Password.getText().toString())) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Enter Confrim Password and password does not match!", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (TextUtils.isEmpty(UserName.getText().toString())) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Rrgister wait.........");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Success", "createUserWithEmail:success");
                                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                                    firebaseUserID= firebaseUser.getUid();
                                    User user= new User(Email.getText().toString(),UserName.getText().toString(),0);
                                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().getRoot();
                                    databaseReference.child("User").child(firebaseUserID).setValue(user, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                if (progressDialog.isShowing()) {
                                                    progressDialog.dismiss();
                                                }
                                                Toast.makeText(getApplicationContext(), "Data Insert Error!", Toast.LENGTH_SHORT).show();

                                            } else {
                                                if (progressDialog.isShowing()) {
                                                    progressDialog.dismiss();
                                                }
                                                Intent ne =new Intent(RegisterActivity.this,LoginActivity.class);
                                                startActivity(ne);
                                                finish();

                                            }

                                        }
                                    });


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Fail", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "UserAccount Alerdy Exit",
                                            Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        });


            }
        });



    }
}
