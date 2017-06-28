package com.example.harsha.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by harsha on 28/01/17.
 */
public class SignUpActivity extends AppCompatActivity {

    Button signUpButton;
    EditText userNameETForSignUp, emailETForSignUp, passwordETForSignUp;
    TextView userNameTVSignUp,emailTVSignUp, passwordTVSignUp;

    String userNameToSignUp, emailToSignUp, passwordToSignUp;

    private DatabaseReference mdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpButton = (Button) findViewById(R.id.signup_btn);

        userNameETForSignUp = (EditText) findViewById(R.id.username_et_signup);
        emailETForSignUp = (EditText) findViewById(R.id.email_et_signup);
        passwordETForSignUp = (EditText) findViewById(R.id.password_et_signup);

        userNameTVSignUp =(TextView) findViewById(R.id.username_tv_signup);
        emailTVSignUp = (TextView) findViewById(R.id.email_tv_signup);
        passwordTVSignUp = (TextView) findViewById(R.id.password_tv_signup);

        mdatabase = FirebaseDatabase.getInstance().getReference();

        userNameETForSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!"".equalsIgnoreCase(userNameETForSignUp.getText().toString())){
                    userNameETForSignUp.setHint("");
                    userNameTVSignUp.setText("Name");
                }else {
                    userNameETForSignUp.setHint("Name");
                    userNameETForSignUp.setHintTextColor(getResources().getColor(R.color.colorAccent));
                    userNameTVSignUp.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailETForSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!"".equalsIgnoreCase(emailETForSignUp.getText().toString())){
                    emailETForSignUp.setHint("");
                    emailTVSignUp.setText("E-Mail");
                }else {
                    emailETForSignUp.setHint("E-Mail");
                    emailETForSignUp.setHintTextColor(getResources().getColor(R.color.colorAccent));
                    emailTVSignUp.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordETForSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!"".equalsIgnoreCase(passwordETForSignUp.getText().toString())){
                    passwordETForSignUp.setHint("");
                    passwordTVSignUp.setText("Password");
                }else {
                    passwordETForSignUp.setHint("Password");
                    passwordETForSignUp.setHintTextColor(getResources().getColor(R.color.colorAccent));
                    passwordTVSignUp.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isConnected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (!CommonUtils.isNull(networkInfo)) {
                    isConnected = networkInfo.isConnectedOrConnecting();
                }

                userNameToSignUp = userNameETForSignUp.getText().toString();
                emailToSignUp = emailETForSignUp.getText().toString();
                passwordToSignUp = passwordETForSignUp.getText().toString();

                if (CommonUtils.isEmptyString(userNameToSignUp)) {
                    userNameETForSignUp.requestFocus();
                    userNameETForSignUp.setHint("Name cannot be empty");
                    userNameETForSignUp.setHintTextColor(getResources().getColor(R.color.errorColor));
                    userNameTVSignUp.setText("Name");
                } else if (CommonUtils.isEmptyString(emailToSignUp)) {
                    emailETForSignUp.requestFocus();
                    emailETForSignUp.setHint("Email cannot be empty");
                    emailETForSignUp.setHintTextColor(getResources().getColor(R.color.errorColor));
                    emailTVSignUp.setText("E-Mail");
                } else if (!CommonUtils.isValidEmail(emailToSignUp)) {
                    emailETForSignUp.requestFocus();
                    emailETForSignUp.setText("");
                    emailETForSignUp.setHint("Email entered is not valid");
                    emailETForSignUp.setHintTextColor(getResources().getColor(R.color.errorColor));
                    emailTVSignUp.setText("E-Mail");
                } else if (CommonUtils.isEmptyString(passwordToSignUp)) {
                    passwordETForSignUp.requestFocus();
                    passwordETForSignUp.setHint("Password cannot be empty");
                    passwordETForSignUp.setHintTextColor(getResources().getColor(R.color.errorColor));
                    passwordTVSignUp.setText("Password");
                } else if(passwordToSignUp.length()<6){
                    passwordETForSignUp.requestFocus();
                    passwordETForSignUp.setText("");
                    passwordETForSignUp.setHint("Must contain 6 characters");
                    passwordETForSignUp.setHintTextColor(getResources().getColor(R.color.errorColor));
                    passwordTVSignUp.setText("Password");
                }else if(isConnected){
                    UserDetails userDetails = new UserDetails();
                    userDetails.setUserName(userNameToSignUp);
                    userDetails.setPassword(passwordToSignUp);
                    userDetails.setEmail(emailToSignUp);

                    DatabaseReference newRef = mdatabase.child("UserDetails").push();
                    newRef.setValue(userDetails);

                    Intent homeActivityIntent = new Intent(getApplicationContext(),HomeActivity.class);
                    homeActivityIntent.putExtra("username",userNameToSignUp);
                    startActivity(homeActivityIntent);
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
