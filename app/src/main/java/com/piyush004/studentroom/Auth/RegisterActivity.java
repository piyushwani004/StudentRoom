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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.piyush004.studentroom.R;
import com.piyush004.studentroom.URoom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonSignUp;
    private EditText editTextName, editTextEmail, editTextPassword, editTextVerifyPassword;
    private String name, email, pass, repass;
    private TextInputLayout TextInputLayoutPass, TextInputLayoutRePass;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final URoom uRoom = new URoom();
        firebaseAuth = FirebaseAuth.getInstance();
        buttonSignUp = findViewById(R.id.button_sign_up);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextVerifyPassword = findViewById(R.id.editTextVerifyPassword);
        progressBar = findViewById(R.id.progressBar);
        TextInputLayoutPass = findViewById(R.id.EditTextLayoutPassword);
        TextInputLayoutRePass = findViewById(R.id.EditTextLayoutVerifyPassword);

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
                    Toast.makeText(RegisterActivity.this, "Passwords must contain at least six characters, including uppercase, lowercase letters and numbers", Toast.LENGTH_LONG).show();
                    editTextPassword.setError("Passwords must contain at least six characters, including uppercase, lowercase letters and numbers");
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
                                        "Registration Error " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();

                                String SPEmail = uRoom.emailSplit(email);
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("AppUsers").child(SPEmail);
                                databaseReference.child("Name").setValue(name);
                                databaseReference.child("Email").setValue(email);

                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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