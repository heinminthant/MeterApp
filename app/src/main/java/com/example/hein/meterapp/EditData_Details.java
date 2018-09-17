package com.example.hein.meterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EditData_Details extends AppCompatActivity {

    Bundle extras;
    String meterNo,ledgerNo;
    TextView txtMeterNo,txtLedgerNo,txtName,txtAddress,txtPrevious,txtDate,txtImageName;
    EditText editCurrent;
    Button btnSave;
    DatabaseHelper db;
    ImageView imgMeter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data__details);

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());
        Date date = new Date();

        db = new DatabaseHelper(EditData_Details.this);

        txtMeterNo = findViewById(R.id.txtMeterNo);
        txtLedgerNo = findViewById(R.id.txtLedgerNo);
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtPrevious = findViewById(R.id.txtPrevious);
        txtDate = findViewById(R.id.txtDate);
        imgMeter = findViewById(R.id.imgMeter);
        txtImageName = findViewById(R.id.txtImageName);

        editCurrent = findViewById(R.id.editCurrent);

        btnSave = findViewById(R.id.btnSave);


        extras = getIntent().getExtras();
        if(extras!=null){
            meterNo = extras.getString("MeterNo");
        }



        HashMap<String,String> meterData = db.searcMeterNo(meterNo);

        txtMeterNo.setText(meterData.get("MeterNo"));
        txtLedgerNo.setText(meterData.get("LedgerNo"));
        txtName.setText(meterData.get("ConsumerName"));
        txtAddress.setText(meterData.get("Address"));
        txtPrevious.setText(meterData.get("PreviousUnit"));
        txtDate.setText(currentDate.format(date));
        editCurrent.setText(meterData.get("CurrentUnit"));

        txtImageName.setText(db.getImageFileName(meterNo,meterData.get("LedgerNo")));
        imgMeter.setImageBitmap(db.getImage(db.getImageFileName(meterNo,meterData.get("LedgerNo"))));


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.editCurrent(meterNo,Integer.parseInt(editCurrent.getText().toString()));
                startActivity(new Intent(EditData_Details.this,EditData_Search.class));
            }
        });


    }
}