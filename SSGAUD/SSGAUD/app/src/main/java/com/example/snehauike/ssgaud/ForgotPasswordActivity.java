package com.example.snehauike.ssgaud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText frgtEmailId, frgtPassword, frgtConfrimPassword;
    private Button btUpdate;

    String frgt_url = "http://10.0.2.2:8888/project/forgotPassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        frgtEmailId = (EditText)findViewById(R.id.etFEmailId);
        frgtPassword = (EditText)findViewById(R.id.etFEmailId);
        frgtConfrimPassword = (EditText)findViewById(R.id.etFEmailId);
        btUpdate = (Button) findViewById(R.id.btUpdate);

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emialIdValue = frgtEmailId.getText().toString();
                String passwordValue = frgtPassword.getText().toString();
                String confrimPassword = frgtConfrimPassword.getText().toString();

                if(!isValidEmail(emialIdValue)){
                    frgtEmailId.requestFocus();
                    frgtEmailId.setError("Invalid Emai ID");
                }else if(frgtPassword.length() == 0){
                    frgtPassword.requestFocus();
                    frgtPassword.setError("Field cannot be empty");
                }else if(!confrimPassword.equals(passwordValue)){
                    frgtConfrimPassword.requestFocus();
                    frgtConfrimPassword.setError("Confrim password is not same");
                }else{
                    //insert data
                    forgotPassword(emialIdValue,passwordValue,confrimPassword);

                }


            }
        });//end of btUpdate function

    }

    //check for correct syntax of email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //forgotPassword function
    private void forgotPassword(String emailId, String password, String confrimPassword){
        String urlSuffix = "?emailId="+emailId+
                "&password="+password+"&confrimPassword="+confrimPassword;

        class ForgotPassword extends AsyncTask<String, Void, String>{

            ProgressDialog loading;

            @Override
            protected  void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(ForgotPasswordActivity.this, "Please wait", null, true, true);

            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equals("success")){
                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                    startActivity(intent);

                }
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(frgt_url+s);
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

        ForgotPassword fp = new ForgotPassword();
        fp.execute(urlSuffix);
    }

}
