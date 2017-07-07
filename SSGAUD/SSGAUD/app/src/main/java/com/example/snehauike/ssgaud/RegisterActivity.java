package com.example.snehauike.ssgaud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, phoneNo, emailId, password, confrimPassword;
    private Button register;

    String reg_url = "http://10.0.2.2:8888/project/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText)findViewById(R.id.etName);
        phoneNo = (EditText)findViewById(R.id.etPhone);
        emailId = (EditText)findViewById(R.id.etFEmailId);
        password = (EditText)findViewById(R.id.etFPassword);
        confrimPassword = (EditText)findViewById(R.id.etFConfrimPassword);
        register = (Button) findViewById(R.id.btRegister);

        /*button done -
        * checks for validations
        * check for duplicate email id
        * save data in database
        * after register done successfully, go to login activity*/
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameValue = name.getText().toString();
                String phoneNoValue = phoneNo.getText().toString();
                String emailIdValue = emailId.getText().toString();
                String passwordValue = password.getText().toString();
                String confrimPassValue = confrimPassword.getText().toString();

                //validations
                if(name.length() == 0){
                    name.requestFocus();
                    name.setError("Field cannot be empty");
                }else if(!isValidEmail(emailIdValue)){
                    emailId.requestFocus();
                    emailId.setError("Invalid Emai ID");
                }else if(password.length() == 0){
                    password.requestFocus();
                    password.setError("Field cannot be empty");
                }else if(!confrimPassValue.equals(passwordValue)){
                    confrimPassword.requestFocus();
                    confrimPassword.setError("Confrim password is not same");
                }else{
                    //insert data
                    register(nameValue,phoneNoValue,emailIdValue,passwordValue,confrimPassValue);

                }

            }
        });//end of register
    }//end of onCreate

    //check for correct syntax of email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    //register function to insert data
    private void register(String name, String phoneNo, String emailId, String password, String cnfPassword){
        String urlSuffix = "?name="+name+"&phoneNo="+phoneNo+"&emailId="+emailId+
                "&password="+password+"&confrimPassword="+cnfPassword;

        class RegisterUser extends AsyncTask<String, Void, String>{

            ProgressDialog loading;

            @Override
            protected  void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this, "Please wait", null, true, true);

            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equals("success")){
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(reg_url+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }


}//end of RegiterActivity
