package com.vasmash.va_smash.WalletScreen;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.WalletScreen.Adapter.Adapter_Walletbal;
import com.vasmash.va_smash.WalletScreen.ModelClass.Model_Wallet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.wallet_url;


public class Wallet_Fragment extends Fragment {

    RecyclerView walletlist;

    ArrayList<Model_Wallet> model;
    Adapter_Walletbal mainAdapter;

    String[] bal={"10"};

    String token;
    private RequestQueue mQueue;

    static public ArrayList<String> coinnameL ;
    static public ArrayList<String> amountL ;


    // TODO: Rename and change types and number of parameters
    public static Wallet_Fragment newInstance() {
        Wallet_Fragment fragment = new Wallet_Fragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet_, container, false);

        walletlist = view.findViewById(R.id.walletlst);

        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = phoneauthshard.getString("token", "null");
        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        jsonParsewallet();


        return view;
    }

    private void jsonParsewallet() {
        Log.d("jsonParseuser", "wallet " + wallet_url);
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, wallet_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        coinnameL=new ArrayList<>();
                        amountL=new ArrayList<>();

                        coinnameL.clear();
                        amountL.clear();

                        Log.d("JSONSenderprofile", "---" + response);

                        if (response.length()!=0) {
                            try {

                                //JSONArray array = response.getJSONArray("stackingTransactions");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject rec = response.getJSONObject(i);
                                    //Log.d("arrayindex111", ":::"+rec);
                                    String userId = rec.getString("userId");
                                    String amount = rec.getString("amount");

                                    JSONObject coinId = rec.getJSONObject("coinId");
                                   /* String createdDate = coinId.getString("createdDate");
                                    String expiryDate = coinId.getString("expiryDate");
                                    String _id = coinId.getString("_id");*/
                                    String coinName = coinId.getString("coinName");
/*
                                    String coinCode = coinId.getString("coinCode");
                                    String symbol = coinId.getString("symbol");
*/

                                    coinnameL.add(coinName);
                                    amountL.add(amount);
                                    walletlist.setHasFixedSize(true);
                                    // use a linear layout manager
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    walletlist.setLayoutManager(mLayoutManager);
                                    model = new ArrayList<>();

                                    for (int i1 = 0; i1 < coinnameL.size(); i1++) {
                                        Model_Wallet mainMode = new Model_Wallet(coinnameL.get(i1),amountL.get(i));
                                        model.add(mainMode);
                                    }

                                    mainAdapter = new Adapter_Walletbal(model, getActivity());
                                    walletlist.setAdapter(mainAdapter);
                                    mainAdapter.notifyDataSetChanged();

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
    }


}
