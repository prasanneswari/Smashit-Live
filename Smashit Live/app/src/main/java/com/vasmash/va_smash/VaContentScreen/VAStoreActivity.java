package com.vasmash.va_smash.VaContentScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.VaContentScreen.Adapter.VastoreAdapter;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Vastore_content_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.vastore_kyc;
import static com.vasmash.va_smash.VASmashAPIS.APIs.vastore_userdata;
import static com.vasmash.va_smash.VASmashAPIS.APIs.vastoredata_url;

public class VAStoreActivity extends AppCompatActivity {
    private List<Vastore_content_model> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VastoreAdapter mAdapter;
    String token;
    private RequestQueue mQueue;
    ViewDialog viewDialog;
    Button adddata;
    TextView vastore_myproduct,vastore_allproduct,vastore_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vastore);
        mQueue = Volley.newRequestQueue(this);
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(this);
        token = phoneauthshard.getString("token", "null");
        viewDialog = new ViewDialog(this);
        adddata=findViewById(R.id.addpost);
        vastore_allproduct = (TextView) findViewById(R.id.vastore_allproduct);
        vastore_myproduct = (TextView) findViewById(R.id.vastore_myproduct);
        vastore_nodata = (TextView) findViewById(R.id.vastore_nodata);

        jsongetvastore();
        recyclerView = (RecyclerView) findViewById(R.id.vastore_recycle);

        adddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VAStoreActivity.this, AddContent.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                kyc();
            }
        });

        vastore_myproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product();
                vastore_myproduct.setBackground(getResources().getDrawable(R.drawable.homeback));
                vastore_allproduct.setBackground(getResources().getDrawable(R.drawable.empty));

            }
        });
        vastore_allproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsongetvastore();
                vastore_allproduct.setBackground(getResources().getDrawable(R.drawable.homeback));
                vastore_myproduct.setBackground(getResources().getDrawable(R.drawable.empty));

            }
        });


    }
    private void jsongetvastore() {
        Log.d("jsonParseuser", "store data" + vastoredata_url);
        viewDialog.showDialog();


        movieList.clear();
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, vastoredata_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Responsestoredata0000", response.toString());

                        viewDialog.hideDialog();


                        if (response.length() != 0) {
                            movieList = new ArrayList<>();
                            for (int j = 0; j < response.length() ; j++ ) {
                                Log.d("lengtharayyy",  ":::"+j);
                                ;

                                JSONObject jItem = null;
                                try {
                                    jItem = response.getJSONObject(j);

                                    Vastore_content_model vastore=new Vastore_content_model();

                                    String productname=jItem.getString("name");
                                    String productimg=jItem.getString("image");
                                    String producturl=jItem.getString("url");

                                    Log.e("products",productimg+" "+productname+" "+producturl);
                                    vastore.setProductname(productname);
                                    vastore.setProductimg(productimg);
                                    vastore.setPruducturl(producturl);
                                    movieList.add(vastore);
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            mAdapter = new VastoreAdapter(movieList,VAStoreActivity.this);
                            recyclerView.setLayoutManager(new GridLayoutManager(VAStoreActivity.this, 3));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("Error.Response1111111", error.toString());
                        String body;
                        //get status code here
                        //Log.d("statusCode", "---" + error.toString());
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 422:
                                    try {
                                        body = new String(error.networkResponse.data,"UTF-8");
                                        Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            viewDialog.hideDialog();
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            Toast.makeText(VAStoreActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 404:
                                    try {
                                        String bodyerror = new String(error.networkResponse.data,"UTF-8");
                                        Log.d("bodyerror", "---" + bodyerror);
                                        JSONObject obj = new JSONObject(bodyerror);
                                        if (obj.has("errors")) {
                                            viewDialog.hideDialog();
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            //Toast.makeText(GACalculator.this, message, Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }
                    }
                }
        )
        {
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                //Log.d("", "volleyError" + volleyError.getMessage());
                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Map<String, String> getParams() {
                //Log.d("paamssssssss","" +params);

                return new HashMap<>();
            }

            @Override
            public Map<String, String> getHeaders() {

                //  Authorization: Basic $auth
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Content-Type","application/json");
                headers.put("Authorization",token);


                return headers;
            }
        };

        // add it to the RequestQueue
        mQueue.add(getRequest);
        getRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                viewDialog.hideDialog();
            }
        });

    }
    public void finishActivity(View v){
        finish();
    }
    public void kyc() {
        //spinner2.setVisibility(View.VISIBLE);
//        viewDialog.showDialog();


            Log.d("jsonParseuser", "store data" + vastore_kyc);



            // prepare the Request
        RequestQueue requestQueue = Volley.newRequestQueue(VAStoreActivity.this);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                vastore_kyc,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("message", String.valueOf(response));
                        try {
                            String message=response.getString("message");
                            Log.e("message",message );
                            if (message.equals("KYC Completed Successfully")){



                                AlertDialog.Builder builder1 = new AlertDialog.Builder(VAStoreActivity.this);
                                builder1.setMessage("Apply for the kyc.");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                Intent intent = new Intent(VAStoreActivity.this, AddContent.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                            }
                            else if(message.equals("Your Request Is Pending")){

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(VAStoreActivity.this);
                                builder1.setMessage("Your Request Is Pending.");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });



                                AlertDialog alert11 = builder1.create();
                                alert11.show();



                            }
                            else {
                                Intent intent = new Intent(VAStoreActivity.this, KycActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                    }

                }



        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization",token);
                return headers;
            }
        };

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

        }



    public void product() {
        //spinner2.setVisibility(View.VISIBLE);
//        viewDialog.showDialog();


        Log.d("jsonParseuser", "store data" + vastore_userdata);
        movieList.clear();




        // prepare the Request
        RequestQueue requestQueue = Volley.newRequestQueue(VAStoreActivity.this);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                vastore_userdata,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("message", String.valueOf(response));
                        try {



                            JSONArray jsonArray=response.getJSONArray("UserPosters");


                            if (jsonArray.length() != 0) {
                                movieList = new ArrayList<>();
                                for (int j = 0; j < jsonArray.length() ; j++ ) {
                                    Log.d("lengtharayyy",  ":::"+j);
                                    ;

                                    JSONObject jItem = jsonArray.getJSONObject(j);

                                        Vastore_content_model vastore=new Vastore_content_model();

                                        String productname=jItem.getString("name");
                                        String productimg=jItem.getString("image");
                                        String producturl=jItem.getString("url");

                                        Log.e("products",productimg+" "+productname+" "+producturl);
                                        vastore.setProductname(productname);
                                        vastore.setProductimg(productimg);
                                        vastore.setPruducturl(producturl);
                                        movieList.add(vastore);


                                }
                                mAdapter = new VastoreAdapter(movieList,VAStoreActivity.this);
                                recyclerView.setLayoutManager(new GridLayoutManager(VAStoreActivity.this, 3));
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }









                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                    }

                }



        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Content-Type","application/json");
                headers.put("Authorization",token);
                return headers;
            }
        };

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }



}
