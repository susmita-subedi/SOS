package com.android.susmita.ssgaud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText emailId, password;
    private TextView forgotPassword;
    private Button signIn, signUp;

    //private String login_url = "http://10.0.2.2:8888/project/login.php";

    //For real device use this link
    private String login_url = "http://192.168.1.93:8888/project/login.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailId = (EditText) findViewById(R.id.etFEmailId);
        password = (EditText) findViewById(R.id.etFPassword);
        forgotPassword = (TextView) findViewById(R.id.tvForgot);
        signIn = (Button) findViewById(R.id.btSignIn);
        signUp = (Button) findViewById(R.id.btSignUp);

        //button sign in listener
        /*on click goes to location activity
        * check login details
        * check validaions*/
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue = emailId.getText().toString();
                String passwordValue = password.getText().toString();

                login(usernameValue, passwordValue);

            }
        });//end of sign in

        /*button sign up
        * on click go to register activity*/
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent - takes us to the requested activity. Here register is the requested activity
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        //forgot password click listenet
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotIntent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotIntent);
            }
        });



    }//end of oncreate

    //login function
    private void login(String username, String password ){
        String urlSuffix = "?username="+username+"&password="+password;

        class LoginUser extends AsyncTask<String, Void, String>{

            ProgressDialog loading;

            @Override
            protected  void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please wait", null, true, true);

            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equals("success")){
                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                    startActivity(intent);

                }
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();


            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(login_url+s);
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

        LoginUser ru = new LoginUser();
        ru.execute(urlSuffix);
    }


}//end of main activity
