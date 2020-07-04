package com.vasmash.va_smash.WalletScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.getwallwtbal_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.p2ptrans_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.wallet_url;

public class WalletAmount extends AppCompatActivity {
    Dialog dialog;

    Button transferbalance,creaditcardbtn,gawalletbtn,creditbalbtn,gawalletbalbtn;
    LinearLayout transferopenlay,transferlay,creditlay,gawalletlay,anothercreditlay;
    EditText transferph,enteramount,creditcardnum,creditdate,creditcvv,gawalletcardnum,gawalletpwd;
    ImageView anothercrditaccount,transferdata;
    TextView addbalance,trasnferbtn,walletbal,gawallet;

    String token,userloginid;
    private RequestQueue mQueue;
    ArrayList<String> amountL;

    //this is the loading animationview.
    ViewDialog viewDialog;

    TextView coinname,editcoinname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_amount);
        addbalance=findViewById(R.id.addbalance);
        trasnferbtn=findViewById(R.id.transfer);
        transferbalance=findViewById(R.id.transferbtn);

        creaditcardbtn=findViewById(R.id.credit);
        gawalletbtn=findViewById(R.id.gawallet);
        creditbalbtn=findViewById(R.id.creaditbalbtn);
        gawalletbalbtn=findViewById(R.id.gawalletbal);

        transferopenlay=findViewById(R.id.transferlayopen);
        transferlay=findViewById(R.id.transferlay);
        creditlay=findViewById(R.id.creditbalencelay);
        gawalletlay=findViewById(R.id.gawalletlay);
        anothercreditlay=findViewById(R.id.anothercreditlay);

        transferph=findViewById(R.id.transferph);
        enteramount=findViewById(R.id.enteramount);

        creditcardnum=findViewById(R.id.creditcardnum);
        creditdate=findViewById(R.id.creditdate);
        creditcvv=findViewById(R.id.creditcvv);
        gawalletcardnum=findViewById(R.id.enterphonenum);
        gawalletpwd=findViewById(R.id.enterpwd);
        anothercrditaccount=findViewById(R.id.anotherbtn);
        transferdata=findViewById(R.id.transferdata);
        walletbal=findViewById(R.id.walletbal);
        gawallet=findViewById(R.id.gawalletamount);
        coinname=findViewById(R.id.coinname);
        editcoinname=findViewById(R.id.editcoinname);

        viewDialog = new ViewDialog(this);


        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(WalletAmount.this);
        token = phoneauthshard.getString("token", "null");
        userloginid = phoneauthshard.getString("id", "null");

        mQueue = Volley.newRequestQueue(getApplicationContext());
        //jsonParsewallet();
        jsonParsegawallet();

        addbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferopenlay.setVisibility(View.GONE);
                transferlay.setVisibility(View.VISIBLE);
                addbalance.setBackgroundResource(R.drawable.loginbutton);
                trasnferbtn.setBackgroundResource(R.drawable.transferback);
                addbalance.setTextColor(Color.parseColor("#FFFFFF"));
                trasnferbtn.setTextColor(Color.parseColor("#57617696"));
            }
        });

        trasnferbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferopenlay.setVisibility(View.VISIBLE);
                transferlay.setVisibility(View.GONE);
                addbalance.setBackgroundResource(R.drawable.transferback);
                trasnferbtn.setBackgroundResource(R.drawable.loginbutton);
                trasnferbtn.setTextColor(Color.parseColor("#FFFFFF"));
                addbalance.setTextColor(Color.parseColor("#57617696"));


            }
        });

        anothercrditaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anothercreditlay.setVisibility(View.VISIBLE);
            }
        });
        creaditcardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gawalletlay.setVisibility(View.GONE);
                creditlay.setVisibility(View.VISIBLE);

                gawalletbtn.setBackgroundResource(R.drawable.curve_shape_left);
                creaditcardbtn.setBackgroundResource(R.drawable.creaditback);
                creaditcardbtn.setTextColor(Color.parseColor("#FFFFFF"));
                gawalletbtn.setTextColor(Color.parseColor("#57617696"));

            }
        });
        gawalletbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gawalletlay.setVisibility(View.VISIBLE);
                creditlay.setVisibility(View.GONE);

                gawalletbtn.setBackgroundResource(R.drawable.gawalletrect);
                creaditcardbtn.setBackgroundResource(R.drawable.curve_shape_right);
                gawalletbtn.setTextColor(Color.parseColor("#FFFFFF"));
                creaditcardbtn.setTextColor(Color.parseColor("#57617696"));

            }
        });

        transferdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletAmount.this, AllTransactionsTab.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        transferbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String amountS = enteramount.getText().toString();
                final String phS = transferph.getText().toString();

                if (amountS.isEmpty() || amountS.isEmpty()) {
                    Toast.makeText(WalletAmount.this, "Please enter the All Fields", Toast.LENGTH_SHORT).show();

                } else {

                    //String loginS = "{\"username\":\"" + phS + "\",\"to\":\"" + userloginid + "\",\"amount\":\"" + amountS + "\"}";
                    String loginS = "{\"username\":\"" + phS + "\",\"amount\":\"" + amountS + "\"}";

                    Log.d("jsnresponse login", "---" + loginS);
                    String url=p2ptrans_url;
                    JSONObject lstrmdt;

                    try {
                        lstrmdt = new JSONObject(loginS);
                        Log.d("jsnresponse....", "---" + loginS);
                        viewDialog.showDialog();
                        JSONSenderp2p(lstrmdt,url);

                    } catch (JSONException ignored) {
                    }
                }
            }
        });


    }


    private void jsonParsewallet() {
        viewDialog.showDialog();

        Log.d("jsonParseuser", "wallet " + getwallwtbal_url);
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, getwallwtbal_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONSenderprofile", "---" + response);
                        viewDialog.hideDialog();

                        if (response.length()!=0) {

                            try {
                                String WalletBalance = response.getString("WalletBalance");
                                String vidAmount = response.getString("vidAmount");
                                walletbal.setText(WalletBalance);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                                            // Toast.makeText(RequestFragment.this, message, Toast.LENGTH_SHORT).show();

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

                headers.put("Authorization",token);

                System.out.println("headddddd"+headers);

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
    private void jsonParsegawallet() {
        Log.d("jsonParseuser", "wallet " + wallet_url);
        viewDialog.showDialog();

        // prepare the Request
        amountL=new ArrayList<>();
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, wallet_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        viewDialog.hideDialog();

                        Log.d("JSONgabal", "---" + response);

                        if (response.length()!=0) {
                            try {

                                //JSONArray array = response.getJSONArray("stackingTransactions");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject rec = response.getJSONObject(i);
                                    //Log.d("arrayindex111", ":::"+rec);
                                    //String userId = rec.getString("userId");
                                    String amount = rec.getString("amount");
                                    JSONObject coinId=rec.getJSONObject("coinId");
                                    String symbol=coinId.getString("symbol");
                                    coinname.setText(symbol);
                                    editcoinname.setText(symbol);
                                    walletbal.setText(amount);
                                    amountL.add(amount);
                                    Log.d("amountL", ":::"+amountL+"::::"+amountL.size());

/*
                                    for (int k=0;k<amountL.size();k++) {
                                        if (amountL.size() > 1) {
                                            String index = amountL.get(1);
                                            gawallet.setText(index);
                                        }
                                    }
*/
                                }
                            } catch(JSONException e){
                                e.printStackTrace();
                            }
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
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            // Toast.makeText(RequestFragment.this, message, Toast.LENGTH_SHORT).show();
                                            viewDialog.hideDialog();

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

                headers.put("Authorization",token);

                System.out.println("headddddd"+headers);

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



    public void JSONSenderp2p(JSONObject lstrmdt, String url) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        Log.d("555555", "p2p url" + url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONp2p", "---" + response);
                        viewDialog.hideDialog();

                        try {
                            if (response.length()!=0) {
                                String message = response.getString("message");
                                String status = response.getString("status");

                                if (status.equals("1")) {
                                    enteramount.getText().clear();
                                    transferph.getText().clear();
                                   // jsonParsewallet();
                                    jsonParsegawallet();
                                    Toast.makeText(WalletAmount.this, message, Toast.LENGTH_SHORT).show();

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
                                if (obj.has("errors")) {
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    Toast.makeText(WalletAmount.this, message, Toast.LENGTH_SHORT).show();
                                }else {
                                    viewDialog.hideDialog();
                                    String message = obj.getString("message");
                                    fallowpopup(message);
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
                headers.put("Authorization",token);
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
                viewDialog.hideDialog();
            }
        });

    }

    public <T> void addToRequestQueue(Request<T> req) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        mQueue.add(req);
    }

    public void fallowpopup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = WalletAmount.this;
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.responsepopup, null);


        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        TextView message = (TextView) layout.findViewById(R.id.msg);
        message.setText(msg);

        okbtn.setOnClickListener(new View.OnClickListener() {
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
