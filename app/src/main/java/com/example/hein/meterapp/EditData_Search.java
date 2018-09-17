package com.example.hein.meterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditData_Search extends AppCompatActivity {

    EditText editSearchMeter;
    Button btnEditGo;
    Intent detailsIntent;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data__search);

        editSearchMeter = findViewById(R.id.editSearchMeter);
        btnEditGo = findViewById(R.id.btnEditGo);

        db = new DatabaseHelper(EditData_Search.this);

        btnEditGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(db.checkMeterEdit(editSearchMeter.getText().toString()) == true){
                    detailsIntent = new Intent(EditData_Search.this,EditData_Details.class);
                    detailsIntent.putExtra("MeterNo",editSearchMeter.getText().toString());
                    startActivity(detailsIntent);     
                }
                else{
                    Toast.makeText(EditData_Search.this, "MeterNo does not exist or cannot be edited.", Toast.LENGTH_SHORT).show();
                }
               

            }
        });
    }


}
