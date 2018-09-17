package com.example.hein.meterapp;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DataList extends Activity {

    DatabaseHelper db;
    Button btnSearch;
    int binderSerial;
    TextView txtSearch;
    Bundle extras;
    ListView lv;
    ArrayList<HashMap<String,String>> meterList;
    ListAdapter adapter;
    Intent mDetailsIntent;
    String binderNo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        mDetailsIntent = new Intent(this,ReadMeter_CurrentUnit.class);
        btnSearch = findViewById(R.id.btnSearch);
        txtSearch = findViewById(R.id.editSearch);

        extras = getIntent().getExtras();
        if(extras!=null){
            binderNo = extras.getString("BinderNo");
        }


        int binderNo2 = Integer.parseInt(binderNo);

        db = new DatabaseHelper(DataList.this);
        meterList = db.getMeterData(binderNo2);
        lv = findViewById(R.id.user_list);
        adapter = new SimpleAdapter(DataList.this,meterList,R.layout.list_row,new String[]{"MeterNo","LedgerNo","ConsumerName"},new int[]{R.id.name,R.id.designation,R.id.location});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> value = (HashMap<String, String>) adapterView.getItemAtPosition(i);
//                Toast.makeText(DataList.this,value.get("MeterNo"),Toast.LENGTH_SHORT).show();
                mDetailsIntent.putExtra("BinderSearch",true);
                mDetailsIntent.putExtra("MeterNo",value.get("MeterNo"));
                mDetailsIntent.putExtra("BinderNo",binderNo);
                binderSerial = db.getBinderSerial(value.get("MeterNo"));
                mDetailsIntent.putExtra("BinderSerial",binderSerial);
                startActivity(mDetailsIntent);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              meterList =  db.searchMeterData(txtSearch.getText().toString());
              adapter = new SimpleAdapter(DataList.this,meterList,R.layout.list_row,new String[]{"MeterNo","LedgerNo","ConsumerName"},new int[]{R.id.name,R.id.designation,R.id.location});
              lv.setAdapter(adapter);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DataList.this,ReadMeter_Options.class));
        finish();
    }

}
