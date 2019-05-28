package com.andrey.tickets.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andrey.tickets.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    private EditText mRegisterName;
    private EditText mRegisterEmail;
    private EditText mRegisterPassword;

    private ProgressDialog mLoadingBar;

    private Button mCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        mRegisterName = (EditText) findViewById(R.id.register_name);
        mRegisterEmail = (EditText) findViewById(R.id.register_email);
        mRegisterPassword = (EditText) findViewById(R.id.register_password);

        mCreateAccount = (Button) findViewById(R.id.btn_createAccount);
        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mRegisterName.getText().toString();
                String email = mRegisterEmail.getText().toString();
                String password = mRegisterPassword.getText().toString();

                registerAccount(name, email, password);
            }
        });

        mLoadingBar = new ProgressDialog(this);
    }

    private void registerAccount(final String name, final String email, String password){
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Write your name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Write your email", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Write your password", Toast.LENGTH_SHORT).show();
        } else {
            mLoadingBar.setTitle("Creating new account");
            mLoadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if (task.isSuccessful()){
                                //Отправлять данные в базу
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                assert firebaseUser != null;
                                String userId = firebaseUser.getUid();

                                mReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userId);
                                hashMap.put("username", name);
                                mReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            } else{
                                Toast.makeText(SignUpActivity.this, "You cant create acc with this email or password", Toast.LENGTH_SHORT).show();
                            }
                            mLoadingBar.dismiss();
                        }
                    });
        }
    };
}
