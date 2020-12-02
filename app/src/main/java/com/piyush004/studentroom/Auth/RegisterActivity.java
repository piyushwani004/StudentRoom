package com.piyush004.studentroom.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.piyush004.studentroom.Dashboard.HomeActivity;
import com.piyush004.studentroom.MainActivity;
import com.piyush004.studentroom.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonSignUp;
    private EditText editTextName, editTextEmail, editTextPassword, editTextVerifyPassword;
    private String name, email, pass, repass;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        buttonSignUp = findViewById(R.id.button_sign_up);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextVerifyPassword = findViewById(R.id.editTextVerifyPassword);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = editTextName.getText().toString();
                email = editTextEmail.getText().toString();
                pass = editTextPassword.getText().toString();
                repass = editTextVerifyPassword.getText().toString();

                if (name.isEmpty()) {
                    editTextName.setError("Please Enter Name");
                    editTextName.requestFocus();
                } else if (email.isEmpty()) {
                    editTextEmail.setError("Please Enter Email");
                    editTextEmail.requestFocus();
                } else if (pass.isEmpty()) {
                    editTextPassword.setError("Please Enter Password");
                    editTextPassword.requestFocus();
                } else if (repass.isEmpty()) {
                    editTextVerifyPassword.setError("Please Enter Password");
                    editTextVerifyPassword.requestFocus();
                } else if (!(isValidEmail(email))) {
                    editTextEmail.setError("Please Enter Valid Email..!!!");
                } else if (!(isValidPassword(pass))) {
                    editTextPassword.setError("Please Enter Valid Password..!!!");
                } else if (!(pass.equals(repass))) {
                    Toast.makeText(RegisterActivity.this, "Password Does Not Match", Toast.LENGTH_SHORT).show();
                    editTextVerifyPassword.setError("Password Does Not Match");
                } else if (!(email.isEmpty() && pass.isEmpty())) {
                    progressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.createUserWithEmailAndPassword(email, repass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {

                                Toast.makeText(RegisterActivity.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Register Successfully...", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            }
                        }
                    });

                }

            }
        });

    }

    public void onClickSignInText(View view) {

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }


}