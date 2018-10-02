package com.example.hein.meterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainMenu extends AppCompatActivity {

    ImageView meterIcon,editIcon,logoutIcon,downloadIcon,uploadIcon,summaryIcon;
    Bundle extras;
    String township;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);




        meterIcon = findViewById(R.id.icoReadMeter);
        meterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent readIntent = new Intent(MainMenu.this,ReadMeter_Options.class);
                startActivity(readIntent);
            }
        });

        editIcon = findViewById(R.id.icoEditData);
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(MainMenu.this,EditData_Search.class);
                startActivity(editIntent);
            }
        });

        logoutIcon = findViewById(R.id.icoLogout);
        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logoutIntent = new Intent(MainMenu.this,MainActivity.class);
                startActivity(logoutIntent);
            }
        });

        downloadIcon = findViewById(R.id.icoDownload);
        downloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent downloadIntent = new Intent(MainMenu.this,DownloadMeterData.class);
                startActivity(downloadIntent);
            }
        });

        uploadIcon = findViewById(R.id.icoUpload);
        uploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadIntent = new Intent(MainMenu.this,DownloadMeterData.class);
                startActivity(uploadIntent);
            }
        });

        summaryIcon = findViewById(R.id.icoSummary);
        summaryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent summaryIntent = new Intent(MainMenu.this,CheckSummary.class);
                startActivity(summaryIntent);
            }
        });
    }


}
