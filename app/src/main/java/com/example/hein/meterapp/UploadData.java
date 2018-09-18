package com.example.hein.meterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UploadData extends AppCompatActivity {

    Button btnUpload;
    String url,result  = "";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);

        url = "http://192.168.0.106/SQLSync/dbtest.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btnUpload = findViewById(R.id.btnUpload);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        url,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            JSONArray meter = response.getJSONArray("meter");
                            for (int i = 0; i < meter.length(); i++) {
                                JSONObject meterData = meter.getJSONObject(i);

                                int id = meterData.getInt("id");
                                String name = meterData.getString("name");


                                result+= id + " " + name;
                            }
                            result += "\n";

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.append(error.getMessage());

                    }
                });
                requestQueue.add(jsonObjectRequest);
                Toast.makeText(UploadData.this, result, Toast.LENGTH_SHORT).show();

            }
        });
                }


    }

