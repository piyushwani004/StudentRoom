package com.piyush004.studentroom.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.piyush004.studentroom.R;

public class LoginActivity extends AppCompatActivity {

    private Button button;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.button_sign_up);
        progressBar = findViewById(R.id.progress_Bar);
    }

    public void onClickSignUpText(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

}