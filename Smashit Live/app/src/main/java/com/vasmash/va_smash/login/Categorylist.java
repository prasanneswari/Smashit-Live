package com.vasmash.va_smash.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.BottmNavigation.TopNavigationview;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.login.Adapters.Category_popup_adapter;
import com.vasmash.va_smash.login.ModelClass.Languages;
import com.vasmash.va_smash.login.fragments.ForgetFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment.killerplyer;
import static com.vasmash.va_smash.VASmashAPIS.APIs.Catageriesapi;
import static com.vasmash.va_smash.VASmashAPIS.APIs.registerprofileapi;
import static com.vasmash.va_smash.login.Adapters.Category_popup_adapter.catcodeL;
import static com.vasmash.va_smash.login.Adapters.Language_popup_adapter.langcodeL;

public class Categorylist extends AppCompatActivity {
    Dialog dialog;

    ListView lv;
    ArrayList<String> catageriarray;
    private ArrayList<Languages> modelArrayList;
    RequestQueue sch_RequestQueue;
    SharedPreferences pref;
    private RequestQueue mQueue;
    String ln = Catageriesapi;
    Category_popup_adapter language_popup_adapter;
    ViewDialog viewDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        Button lang_btn = (Button)findViewById(R.id.pop_cat_btn);
        TextView popup_tittle =  findViewById(R.id.pop_title);
        viewDialog = new ViewDialog(this);

        lv =  findViewById(R.id.cat_list);
        popup_tittle.setText("Category");

        //here call the cat api we will get categoryes data
        catlangapi(ln);

        //this is the category post sumbit data
        lang_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //catageries.setText("");
                catageriarray = new ArrayList<>();

                Intent getintent = getIntent();
                String nameS = getintent.getStringExtra("nameS");
                String lastnameS = getintent.getStringExtra("lastname");
                String passS = getintent.getStringExtra("passS");
                String dateS = getintent.getStringExtra("date");
                String countryS = getintent.getStringExtra("countryS");
                String genderS = getintent.getStringExtra("code");
                //String laungS=getintent.getStringExtra("laungarray");
                String idS = getintent.getStringExtra("id");

                Log.d("langarray laung","::"+langcodeL);
                Log.d("catageriarray laung","::"+catcodeL);

                String AddS = "{\"id\":\"" + idS + "\",\"firstName\":\"" + nameS + "\",\"lastName\":\"" + lastnameS + "\",\"password\":\"" + passS + "\",\"dob\":\"" + dateS + "\",\"gender\":\"" + genderS + "\",\"country\":\"" + countryS + "\",\"languages\":" + langcodeL + ",\"categories\":" + catcodeL + "}";
                Log.d("jsnresponse pernonal", "---" + AddS);
                JSONObject update;
                try {
                    update = new JSONObject(AddS);
                    Log.d("register", "---" + AddS);
                    viewDialog.showDialog();
                    updateregst(update);
                } catch (JSONException ignored) {
                }
            }
        });
    }
    public void catlangapi(String ul) {
        viewDialog.showDialog();

        mQueue = Volley.newRequestQueue(Categorylist.this.getApplicationContext());
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

                            language_popup_adapter = new Category_popup_adapter(Categorylist.this, modelArrayList);
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

    public void updateregst(JSONObject update) {

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, registerprofileapi, update,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONSenderVolleylogin", "---" + response);
                        viewDialog.hideDialog();
                        try {

                            if (response.has("user")) {
                                JSONObject user = response.getJSONObject("user");
                                String _id = user.getString("_id");
                                String token = user.getString("token");
                                String profilePic = user.getString("profilePic");

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Categorylist.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("token", token);
                                editor.putString("id", _id);
                                editor.putString("userpic", profilePic);
                                editor.apply();
                                killerplyer();
                                Intent i = new Intent(Categorylist.this, TopNavigationview.class);
                                startActivity(i);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body;
                //get status code here
                //Log.d("statusCode", "---" + error.toString());
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            try {
                                body = new String(error.networkResponse.data, "UTF-8");
                                Log.d("body", "---" + body);
                                JSONObject obj1 = new JSONObject(body);
                                if (obj1.has("errors")) {
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj1.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    //dialogbox(message);
                                    popup(message);
                                   // Log.d("msg", message);

                                   // Toast.makeText(Categorylist.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;


                        case 422:
                            try {
                                body = new String(error.networkResponse.data, "UTF-8");
                                Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                if (obj.has("errors")) {
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                   // dialogbox(message);
                                    popup(message);
                                   // Log.d("body", message);
                                   // Toast.makeText(Categorylist.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                //do stuff with the body...
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                //return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
                return headers;
            }
        };
        jsonObjReq.setTag("");
        addToRequestQueue(jsonObjReq);
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
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

            }
        });
    }
    public <T> void addToRequestQueue(Request<T> req) {
        if (sch_RequestQueue == null) {
            sch_RequestQueue = Volley.newRequestQueue(this);
        }
        sch_RequestQueue.add(req);
    }

    public void popup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = Categorylist.this;
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
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(Categorylist.this, R.drawable.d_round_white_background));
        dialog.show();

    }
    public void finishActivity(View v){
        finish();
    }


}
