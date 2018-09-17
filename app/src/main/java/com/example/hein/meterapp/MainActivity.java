package com.example.hein.meterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText editUsername,editPassword;
    String township;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view){
        db = new DatabaseHelper(MainActivity.this);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        if(db.login(editUsername.getText().toString(),editPassword.getText().toString())){
            township = db.getTownship(editUsername.getText().toString());
            Intent menuIntent = new Intent(this,MainMenu.class);
            menuIntent.putExtra("Township",township);
            startActivity(menuIntent);
        }
        else{
            Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
        }

    }
}
