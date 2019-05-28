package com.andrey.tickets.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andrey.tickets.R;

public class StartPageActivity extends AppCompatActivity {
    private Button mSignIn;
    private Button mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        mSignIn = (Button) findViewById(R.id.btn_sign_in);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPageActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        mSignUp = (Button) findViewById(R.id.btn_sign_up);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPageActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
