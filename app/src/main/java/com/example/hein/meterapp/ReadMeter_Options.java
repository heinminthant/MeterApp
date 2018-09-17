package com.example.hein.meterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ReadMeter_Options extends AppCompatActivity {

    EditText editLine,editSearchMeterNo;
    Intent meterDetailsIntent,dataIntent;
    Spinner spnBinder;
    DatabaseHelper db;
    Button btnMeterGo,btnBinderGo;
    Bundle extras;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_meter__options);

        extras = getIntent().getExtras();


        db = new DatabaseHelper(ReadMeter_Options.this);

        spnBinder = findViewById(R.id.spnBinder);
        final Integer[] binder = db.getBinder();
        ArrayAdapter<Integer> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, binder);
        spnBinder.setAdapter(adapter);



        editLine = findViewById(R.id.editLine);
        editLine.setKeyListener(null);

        editSearchMeterNo = findViewById(R.id.editSearchMeterNo);


        btnMeterGo = findViewById(R.id.btnMeterGo);
        btnBinderGo = findViewById(R.id.btnBinderGo);

        btnMeterGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.checkMeterRead(editSearchMeterNo.getText().toString()) == true){
                    meterDetailsIntent = new Intent(ReadMeter_Options.this,ReadMeter_CurrentUnit.class);
                    meterDetailsIntent.putExtra("MeterNo",editSearchMeterNo.getText().toString());
                    meterDetailsIntent.putExtra("BinderSearch",false);
                    startActivity(meterDetailsIntent);
                }
                else{
                    Toast.makeText(ReadMeter_Options.this, "Meter No does not exist or already have been added Current Unit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBinderGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String binderNo = spnBinder.getSelectedItem().toString();
                dataIntent = new Intent(ReadMeter_Options.this,DataList.class);
                dataIntent.putExtra("BinderNo",binderNo);
                startActivity(dataIntent);
            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ReadMeter_Options.this,MainMenu.class));
        finish();
    }

}








