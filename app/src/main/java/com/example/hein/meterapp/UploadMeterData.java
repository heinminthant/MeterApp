package com.example.hein.meterapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UploadMeterData extends AppCompatActivity{

    private JSONArray result;
    private ArrayList<String> user;
    private String sentence;
    private RequestQueue requestQueue;
    private Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_meter_data);
        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

    }


    private void getData(){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(" http://192.168.0.106/SQLSync/dbtest.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                try {
                    j = new JSONObject(response);
                    result = j.getJSONArray("result");
                    for(int i = 0; i < result.length();i++){
                        try{
                            JSONObject json = result.getJSONObject(i);
                            int id = json.getInt("id");
                            String name = json.getString("name");


                            sentence += id + " " + name;

                            Toast.makeText(UploadMeterData.this, "Hello", Toast.LENGTH_SHORT).show();
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        requestQueue.add(stringRequest);
    }






}
