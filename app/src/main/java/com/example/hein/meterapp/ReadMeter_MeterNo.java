package com.example.hein.meterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class    ReadMeter_MeterNo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_meter__meter_no);
    }

    public void searchMeter(View view){
        Intent searchIntent = new Intent(this,ReadMeter_CurrentUnit.class);
        startActivity(searchIntent);
    }
}
