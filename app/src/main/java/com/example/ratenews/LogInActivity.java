package com.example.ratenews;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.Crash;

public class LogInActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Fabric.with(this, new Crashlytics());

        //Button login = findViewById(R.id.btn_login);

        //saving login state
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Crashlytics.log(user.getEmail());
        if (user != null) {
            // User is signed in
            String sEmail;
            sEmail = user.getEmail();
            Intent i = new Intent(LogInActivity.this, MainActivity.class);
            i.putExtra("email", sEmail);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d("Login state", "onAuthStateChanged:signed_out");
        }

        mAuth = FirebaseAuth.getInstance();
        initUI();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserAccount();
                //Intent i = new Intent(LogInActivity.this, MainActivity.class);
                //startActivity(i);
                //finish();
            }
        });

        Button register = findViewById(R.id.btn_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    private void loginUserAccount() {
        progressBar.setVisibility(View.VISIBLE);

        final String sEmail, sPassword;
        sEmail = email.getText().toString();
        sPassword = password.getText().toString();

        if (TextUtils.isEmpty(sEmail)) {
            Toast.makeText(getApplicationContext(), "Molimo unesi email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(sPassword)) {
            Toast.makeText(getApplicationContext(), "Molimo unesi lozinku...", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Uspešna prijava!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            intent.putExtra("email", sEmail);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Prijava nije uspela! Pokušaj ponovo.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void initUI() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
    }

}
