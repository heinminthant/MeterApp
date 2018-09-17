package com.example.hein.meterapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ReadMeter_CurrentUnit extends AppCompatActivity {

    String meterNo,binderNo,fileName,ledgerNo;
    Bundle extras;
    DatabaseHelper db;
    TextView txtMeter,txtName,txtAddress,txtReadingDate,txtPrevious,txtLedger,txtImageName;
    Button btnSubmit,btnBinder;
    ImageButton btnImage;
    EditText editCurrent;
    boolean binderSearch = false;
    int binderSerial = 0,previousUnit = 0;
    HashMap<String, String> meterData;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    boolean imageClicked = false;



    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_meter__current_unit);



        db = new DatabaseHelper(ReadMeter_CurrentUnit.this);


        txtMeter = findViewById(R.id.txtMeter);
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtReadingDate = findViewById(R.id.txtReadingDate);
        txtPrevious = findViewById(R.id.txtPrevious);
        txtLedger = findViewById(R.id.txtLedger);
        txtImageName = findViewById(R.id.txtImageName);

        editCurrent = findViewById(R.id.editCurrent);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnBinder = findViewById(R.id.btnBinder);
        btnBinder.setVisibility(View.INVISIBLE);
        btnImage = findViewById(R.id.imageButton);

        SimpleDateFormat currentDate2 = new SimpleDateFormat("dd-MM-yy",Locale.getDefault());
        Date date2 = new Date();


        txtReadingDate.setText(currentDate2.format(date2));

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        final String currentDate =  dateFormat.format(date);


        extras = getIntent().getExtras();
        if(extras!=null){
            meterNo = extras.getString("MeterNo");
            binderSearch = extras.getBoolean("BinderSearch");
            if(binderSearch==true){
                binderSerial = extras.getInt("BinderSerial");
                binderNo = extras.getString("BinderNo");
            }

        }


        if(binderSearch == true){
            btnBinder.setVisibility(View.VISIBLE);
            meterData = db.searchMeterBinder(binderSerial,binderNo);
        }
        else{
            meterData = db.searcMeterNo(meterNo);
        }

        txtMeter.setText(meterData.get("MeterNo"));
        txtLedger.setText(meterData.get("LedgerNo"));
        txtName.setText(meterData.get("ConsumerName"));
        txtAddress.setText(meterData.get("Address"));
        ledgerNo = db.getLedgerNo(meterNo);
        fileName = db.getImageFileName(meterNo,ledgerNo);


        txtPrevious.setText(meterData.get("PreviousUnit"));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(editCurrent.getText().toString().matches("")){
                    Toast.makeText(ReadMeter_CurrentUnit.this, "Please enter current unit", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (imageClicked == false) {
                        Toast.makeText(ReadMeter_CurrentUnit.this, "Please take a picture.", Toast.LENGTH_SHORT).show();
                    } else {


                        int currentUnit = Integer.parseInt(editCurrent.getText().toString());
                        previousUnit = db.getPrevious(meterNo);
                        if (previousUnit > currentUnit) {
                            Toast.makeText(ReadMeter_CurrentUnit.this, "Current Unit must be larger than Previous Unit", Toast.LENGTH_SHORT).show();
                        } else {
                            db.updateCurrentUnit(meterNo, currentUnit, currentDate);
                            db.insertImage(imageBitmap, meterNo, ledgerNo, fileName);
                            Toast.makeText(ReadMeter_CurrentUnit.this, "Succesfully added Current Unit", Toast.LENGTH_SHORT).show();
                            if (binderSearch == true) {
                                getIntent().putExtra("BinderSerial", binderSerial + 1);

                                if (db.checkMeterRead(meterNo) == true) {
                                    Toast.makeText(ReadMeter_CurrentUnit.this, "Enter Current Unit First", Toast.LENGTH_SHORT).show();
                                } else {
                                    getIntent().putExtra("MeterNo", db.getMeterBinder(binderSerial + 1, binderNo));
                                    startActivity(getIntent());

                                }
                            } else {
                                startActivity(new Intent(ReadMeter_CurrentUnit.this, ReadMeter_Options.class));
                            }

                        }
                    }
                }

            }
        });

        btnBinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent binderIntent = new Intent(ReadMeter_CurrentUnit.this,DataList.class);
                binderIntent.putExtra("BinderNo",binderNo);
                startActivity(binderIntent);
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked = true;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);

                }


            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras2 = data.getExtras();
            imageBitmap = (Bitmap) extras2.get("data");
            btnImage.setImageBitmap(imageBitmap);
            txtImageName.setText(fileName);

        }
    }
}
