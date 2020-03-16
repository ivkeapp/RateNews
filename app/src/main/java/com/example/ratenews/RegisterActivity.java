package com.example.ratenews;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ratenews.crud.api.AddUser;
import com.example.ratenews.crud.api.AddVote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, password, confirmPassword;
    private Button registerBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        initUI();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        progressBar.setVisibility(View.VISIBLE);

        final String sEmail, sPassword, sConfirmedPassword;
        sEmail = email.getText().toString();
        sPassword = password.getText().toString();
        sConfirmedPassword = confirmPassword.getText().toString();

        if (TextUtils.isEmpty(sEmail)) {
            Toast.makeText(getApplicationContext(), "Molimo unesi email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(sPassword)) {
            Toast.makeText(getApplicationContext(), "Molimo unesi lozinku...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(sConfirmedPassword)) {
            Toast.makeText(getApplicationContext(), "Molimo ponovi lozinku...", Toast.LENGTH_LONG).show();
            return;
        }
        if (!sPassword.equals(sConfirmedPassword)){
            Toast.makeText(getApplicationContext(), "Lozinke se ne podudaraju", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Uspešno si kreirao nalog!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            AddUser addUser = new AddUser();
                            addUser.addUser(sEmail, sPassword);

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.putExtra("email", sEmail);
                            intent.putExtra("password", sPassword);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Kreiranje naloga nije uspelo, pokušaj ponovo.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void initUI() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmed_password);
        registerBtn = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
    }

}
