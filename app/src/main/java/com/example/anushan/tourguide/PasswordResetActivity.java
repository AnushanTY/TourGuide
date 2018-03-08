package com.example.anushan.tourguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {
    private EditText Email;
    private Button ResetButon,Back;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        Email=(EditText)findViewById(R.id.Resetemail);
        ResetButon=(Button) findViewById(R.id.btn_reset_password);
        Back=(Button) findViewById(R.id.btn_back);

        progressDialog=new ProgressDialog(PasswordResetActivity.this);

        firebaseAuth=FirebaseAuth.getInstance();

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(PasswordResetActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        ResetButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("wait.........");
                progressDialog.show();

                String email = Email.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email", Toast.LENGTH_SHORT).show();
                    return;
                }


                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PasswordResetActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PasswordResetActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                }


                            }
                        });
            }
        });
    }
}
