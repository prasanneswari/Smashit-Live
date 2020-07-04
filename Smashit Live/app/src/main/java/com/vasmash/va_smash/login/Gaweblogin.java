package com.vasmash.va_smash.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.BottmNavigation.TopNavigationview;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.login.fragments.LoginFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.checkstatus_url;

public class Gaweblogin extends AppCompatActivity {

    WebView webView;
    Button closebtn;
    String gaurl,accountId;

    RequestQueue sch_RequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaweblogin);
        webView=findViewById(R.id.webview);
        closebtn=findViewById(R.id.closebtn);

        sch_RequestQueue = Volley.newRequestQueue(getApplicationContext());

        if(getIntent().getExtras() != null) {
            Intent intent = getIntent();
            gaurl = intent.getStringExtra("gaurl");
            accountId = intent.getStringExtra("accountId");

            webView.loadUrl(gaurl);

            webView.getSettings().setDomStorageEnabled(true);

            // Enable Javascript
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            // Force links and redirects to open in the WebView instead of in a browser
            webView.setWebViewClient(new WebViewClient());

/*
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(gaurl);

            webView.getSettings().setDomStorageEnabled(true);

            // Enable Javascript
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            // Force links and redirects to open in the WebView instead of in a browser
*/


            closebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String loginS = "{\"accountId\":\"" + accountId + "\"}";
                    Log.d("json check status", "---" + loginS);
                    String url = checkstatus_url;
                    JSONObject lstrmdt;

                    try {
                        lstrmdt = new JSONObject(loginS);
                        Log.d("jsnresponse....", "---" + loginS);
                        JSONSenderVolleylogin(lstrmdt, url);

                    } catch (JSONException ignored) {
                    }

                }
            });
        }
    }

    public void JSONSenderVolleylogin(JSONObject lstrmdt, String url) {
        Log.d("555555", "login" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONcheck status", "---" + response);
                        try {
                            String status=response.getString("status");
                            if (status.equals("1")) {
                                JSONObject object = response.getJSONObject("user");
                                String gaid = object.getString("_id");
                                String profilePic = object.getString("profilePic");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                String mobile = object.getString("mobile");
                                String country = object.getString("country");
                                String countryCode = object.getString("countryCode");

                                Intent intent=new Intent(Gaweblogin.this, RegisterformActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("gaid",gaid);
                                intent.putExtra("profilePic",profilePic);
                                intent.putExtra("name",name);
                                intent.putExtra("email",email);
                                intent.putExtra("mobile",mobile);
                                intent.putExtra("country",country);
                                intent.putExtra("countryCode",countryCode);
                                startActivity(intent);
                            }
                            else if (status.equals("0")) {
                                if (response.has("user")) {
                                    JSONObject user = response.getJSONObject("user");
                                    String _id = user.getString("_id");
                                    // String email = user.getString("email");
                                    // String name = user.getString("name");
                                    // String mobile = user.getString("mobile");
                                    //  String emailVerified = user.getString("emailVerified");
                                    String token = user.getString("token");

                                    //jsonckeckpin(username);
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Gaweblogin.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("token", token);
                                    editor.putString("id", _id);
                                    editor.apply();
                                    Intent i = new Intent(Gaweblogin.this, TopNavigationview.class);
                                    startActivity(i);
                                }

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
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 422:
                            try {

                                body = new String(error.networkResponse.data,"UTF-8");
                                Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                String id = null;
                                if (obj.has("id")) {
                                    id = obj.getString("id");
                                }

                                if (obj.has("errors")) {
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    String status = errors.getString("status");
                                    Toast.makeText(Gaweblogin.this, message, Toast.LENGTH_SHORT).show();

                                    if (status.equals("2")){
                                        Intent intent=new Intent(Gaweblogin.this, LoginFragment.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
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
        })
        {
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
            sch_RequestQueue = Volley.newRequestQueue(Gaweblogin.this);
        }
        sch_RequestQueue.add(req);
    }
}
