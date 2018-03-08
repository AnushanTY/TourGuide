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

public class LoginActivity extends AppCompatActivity {
    private EditText Email,Password;
    private TextView Forget_Password;
    private Button Login,Register;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser!=null){
            Intent intent =new Intent(LoginActivity.this, UserMainDashboard.class);
            startActivity(intent);

        }
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
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Success", "signInWithEmail:success");
                                    Intent intent =new Intent(LoginActivity.this, UserMainDashboard.class);
                                    startActivity(intent);
                                    finish();

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

}
