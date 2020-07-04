package com.vasmash.va_smash.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.login.Adapters.Language_popup_adapter;
import com.vasmash.va_smash.login.ModelClass.Languages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.vasmash.va_smash.VASmashAPIS.APIs.Languagesapi;

public class Languagelist extends AppCompatActivity {
    Dialog dialog;

    ListView lv;
    private ArrayList<Languages> modelArrayList;
    private RequestQueue mQueue;
    String ln = Languagesapi;
    Language_popup_adapter language_popup_adapter;
    //this is the loader animationview
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language);

        Button lang_btn = (Button)findViewById(R.id.pop_lang_btn);
        TextView popup_tittle =  findViewById(R.id.pop_title);
        lv =  findViewById(R.id.lang_list);
        popup_tittle.setText("Language");
        viewDialog = new ViewDialog(this);
        //here call laung api we will get launges
        langapi(ln);

        //here we will intent the data to category class
        lang_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent getintent=getIntent();
                        Intent intent = new Intent(Languagelist.this, Categorylist.class);
                        intent.putExtra("nameS",getintent.getStringExtra("nameS"));
                        intent.putExtra("passS",getintent.getStringExtra("passS"));
                        intent.putExtra("date",getintent.getStringExtra("repassS"));
                        intent.putExtra("countryS",getintent.getStringExtra("countryS"));
                        intent.putExtra("code",getintent.getStringExtra("code"));
                        intent.putExtra("id",getintent.getStringExtra("id"));
                       // intent.putExtra("laungarray",langarray);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
            }
        });

    }

    public void langapi(String ul) {
        viewDialog.showDialog();

        mQueue = Volley.newRequestQueue(Languagelist.this.getApplicationContext());
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, ul, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());
                        viewDialog.hideDialog();
                        modelArrayList = new ArrayList<>();

                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    Languages langu = new Languages();
                                    String _id = employee.getString("_id");
                                    String name = employee.getString("name");

                                    langu.setLang_name(_id);
                                    langu.setLang_code(name);
                                    modelArrayList.add(langu);

                                    Log.d("Response", "createddateL:::" + modelArrayList);
                                    Log.d("Response", "createddateL:::" + _id + name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            language_popup_adapter = new Language_popup_adapter(Languagelist.this, modelArrayList);
                            lv.setAdapter(language_popup_adapter);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("Error.Response", error.toString());
                        String body;
                        //get status code here
                        //Log.d("statusCode", "---" + error.toString());
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 422:
                                    try {
                                        body = new String(error.networkResponse.data, "UTF-8");
                                        Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            viewDialog.hideDialog();
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            //Toast.makeText(AddUsers.this, message, Toast.LENGTH_SHORT).show();
                                            popup(message);

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
        );

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

    public void popup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = Languagelist.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.responsepopup, null);


        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        TextView message = (TextView) layout.findViewById(R.id.msg);
        message.setText(msg);

        okbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();

    }


    public void finishActivity(View v){
        finish();
    }

}
