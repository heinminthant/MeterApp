package com.example.hein.meterapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CheckSummary extends AppCompatActivity {

    TextView txtCollecting,txtRemaining;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_summary);

        db = new DatabaseHelper(CheckSummary.this);

        txtCollecting = findViewById(R.id.txtCollecting);
        txtRemaining = findViewById(R.id.txtRemaining);

        txtRemaining.setText(Integer.toString(db.getRemaining()));
        txtCollecting.setText(Integer.toString(db.getCollecting()));
    }


}
