package com.example.snehauike.sos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText username, passowrd;
    Button signIn, signUp;
    DbHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myDb = new DbHelper(this);
        username = (EditText) findViewById(R.id.etUsername);
        passowrd = (EditText) findViewById(R.id.etPassword);
        signIn = (Button) findViewById(R.id.btLogin);
        signUp = (Button) findViewById(R.id.btRegister);

        final StringBuffer password = myDb.encrypt(passowrd.getText().toString());


        signIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String storedPassword = myDb.getSingleEntry(username.getText().toString());
                        //if ((passowrd.getText().toString()).equals(storedPassword)) {
                        if ((password.toString()).equals(storedPassword)) {
                            Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();
                            Intent locationIntent = new Intent(LoginActivity.this, MapsActivity.class);
                            //Intent locationIntent = new Intent(LoginActivity.this, WhereAmIActivity.class);
                            startActivity(locationIntent);

                        } else {
                            Toast.makeText(LoginActivity.this, "Username/Password incorrect", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );

        signUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(registerIntent);
                    }
                }
        );
    }
}
