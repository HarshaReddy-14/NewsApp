package com.example.harsha.newsapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button createNewBtn, loginBtn;
    EditText emailETLogin, passwordETLogin;
    TextView emailTVLogin, passwordTVLogin, forgotPasswordTV, errorTV;
    String emailToLogin, passwordToLogin, userName;
    private DatabaseReference mdatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.login_btn);
        createNewBtn = (Button) findViewById(R.id.create_new_btn);

        emailETLogin = (EditText) findViewById(R.id.email_et_login);
        passwordETLogin = (EditText) findViewById(R.id.password_et_login);

        emailTVLogin = (TextView) findViewById(R.id.username_tv_login);
        passwordTVLogin = (TextView) findViewById(R.id.password_tv_login);
        errorTV = (TextView) findViewById(R.id.error_msg_login);
        forgotPasswordTV = (TextView) findViewById(R.id.forgot_password);

        mdatabase = FirebaseDatabase.getInstance().getReference("UserDetails");

        sharedPreferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("userName","");
        if(sharedPreferences != null){
            if(name != null && !"".equals(name)){
                Intent homeActivityIntent = new Intent(getApplicationContext(),HomeActivity.class);
                homeActivityIntent.putExtra("username",name);
                startActivity(homeActivityIntent);
                finish();
            }
        }
        emailETLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!"".equalsIgnoreCase(emailETLogin.getText().toString())){
                    emailETLogin.setHint("");
                    emailTVLogin.setText("Email");
                }else {
                    emailETLogin.setHint("Email");
                    emailETLogin.setHintTextColor(getResources().getColor(R.color.colorAccent));
                    emailTVLogin.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordETLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!"".equalsIgnoreCase(passwordETLogin.getText().toString())){
                    passwordETLogin.setHint("");
                    passwordTVLogin.setText("Password");
                }else {
                    passwordETLogin.setHint("Password");
                    passwordETLogin.setHintTextColor(getResources().getColor(R.color.colorAccent));
                    passwordTVLogin.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isConnected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null){
                    isConnected = networkInfo.isConnectedOrConnecting();
                }
                emailToLogin = emailETLogin.getText().toString();
                passwordToLogin = passwordETLogin.getText().toString();

                if("".equals(emailToLogin)){
                    emailETLogin.requestFocus();
                    emailETLogin.setHint("Email cannot be empty.");
                    emailETLogin.setHintTextColor(getResources().getColor(R.color.errorColor));
                    emailTVLogin.setText("E-Mail");
                }
                else if("".equals(passwordToLogin)){
                    passwordETLogin.requestFocus();
                    passwordETLogin.setHint("Password cannot be empty.");
                    passwordETLogin.setHintTextColor(getResources().getColor(R.color.errorColor));
                    passwordTVLogin.setText("Password");
                }
                else if(isConnected){
                        mdatabase.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                                if(emailToLogin.equals(userDetails.getEmail()) && passwordToLogin.equals(userDetails.getPassword())){
                                    userName = userDetails.getUserName();
                                    editor = sharedPreferences.edit();
                                    editor.putString("email",emailToLogin);
                                    editor.putString("userName",userName);
                                    editor.commit();

                                    Intent homeActivityIntent = new Intent(getApplicationContext(),HomeActivity.class);
                                    homeActivityIntent.putExtra("username",userName);
                                    startActivity(homeActivityIntent);
                                    finish();
                                } else{
                                    errorTV.setText("Entered mail or password is wrong");
                                    emailETLogin.requestFocus();
                                    emailETLogin.setText("");
                                    emailETLogin.setHint("E-Mail");
                                    emailETLogin.setHintTextColor(getResources().getColor(R.color.colorAccent));
                                    passwordETLogin.setText("");
                                    passwordETLogin.setHint("Password");
                                    passwordETLogin.setHintTextColor(getResources().getColor(R.color.colorAccent));
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
}
