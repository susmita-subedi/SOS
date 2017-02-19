package com.example.snehauike.sos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText name, emailId, phoneNo, username, password, confirmPassword;
    Button createAccount;
    DbHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myDb = new DbHelper(this);//calls constructor of DbHelper class

        name = (EditText) findViewById(R.id.etName);
        emailId = (EditText) findViewById(R.id.etEmailId);
        phoneNo = (EditText) findViewById(R.id.etcontact);
        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        confirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        createAccount = (Button) findViewById(R.id.btCreate);
        //addData();
        final StringBuffer password1 = myDb.encrypt(password.getText().toString());
        final StringBuffer password2 = myDb.encrypt(confirmPassword.getText().toString());

        /*final String password1 = password.getText().toString();
        final String password2 = confirmPassword.getText().toString();*/

        createAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (name.length() == 0) {
                            name.requestFocus();
                            name.setError("Field cannot be empty");
                            return;
                        }
                        if (!isValidEmail(emailId.getText().toString())) {
                            emailId.requestFocus();
                            emailId.setError("Invalid Email");
                            return;
                        }
                        if (!isValidNumber(phoneNo.getText().toString())) {
                            phoneNo.requestFocus();
                            phoneNo.setError("Invalid Phone No");
                            return;
                        }
                        if (!(password1.toString().equals(password2.toString()))){
                            confirmPassword.setError("Passswords do not match");
                        }
                        if((myDb.isDataAlreadyPresent(myDb.COL_5,username.getText().toString()))){
                            username.requestFocus();
                            username.setError("Username already taken");
                        }


                        //insert data
                        if (name.length() != 0 && isValidEmail(emailId.getText().toString())
                                && isValidNumber(phoneNo.getText().toString())
                                && (password1.toString().equals(password2.toString()))
                                && (!(myDb.isDataAlreadyPresent(myDb.COL_5,username.getText().toString()))))
                        {
                            boolean isInserted = myDb.insertData(name.getText().toString(),
                                    phoneNo.getText().toString(),
                                    emailId.getText().toString(),
                                    username.getText().toString(),
                                    String.valueOf(password1));
                            if (isInserted == true) {
                                myDb.viewData();
                                Toast.makeText(RegisterActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(loginIntent);

                            } else {
                                Toast.makeText(RegisterActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );


    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidNumber(String phone) {
        String PHONE_PATTERN = "^[+]?[0-9]{10,13}$";

        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }




}
