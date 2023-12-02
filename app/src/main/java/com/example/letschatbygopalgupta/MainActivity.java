package com.example.letschatbygopalgupta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        // ye apn is liye kr rahe hai taki agr user logined nai hai to usko login page me direct kr denge
        if(auth.getCurrentUser()==null){
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        }
    }
}