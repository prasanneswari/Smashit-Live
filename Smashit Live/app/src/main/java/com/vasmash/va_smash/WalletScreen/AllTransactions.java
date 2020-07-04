package com.vasmash.va_smash.WalletScreen;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.WalletScreen.Adapter.Adapter_AllTransactions;
import com.vasmash.va_smash.WalletScreen.ModelClass.ModelTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static com.vasmash.va_smash.VASmashAPIS.APIs.getalltransactions_url;
import static com.vasmash.va_smash.WalletScreen.AllTransactionsTab.both;

public class AllTransactions extends Fragment {


    RecyclerView transalist;
    ArrayList<ModelTransaction> model;
    Adapter_AllTransactions mainAdapter;
    String token,userloginid;
    private RequestQueue mQueue;

    //this is loading animationview
    private LottieAnimationView animationView;

    public static AllTransactions newInstance(int position) {
        AllTransactions fragment = new AllTransactions();
        Bundle args = new Bundle();
        args.putInt("tabcount", position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_all_transactions, container, false);

        transalist=view.findViewById(R.id.translist);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), HORIZONTAL);
        transalist.addItemDecoration(itemDecor);
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = phoneauthshard.getString("token", "null");
        userloginid = phoneauthshard.getString("id", "null");

        mQueue = Volley.newRequestQueue(getActivity());
        animationView = view.findViewById(R.id.animation_view_1);


        int tabname = getArguments().getInt("tabcount");
        Log.d("tabnameee",":::::"+both.get(tabname));

        String values=both.get(tabname);

        for (int i = 0; i< both.size(); i++){
            if (values.equals(both.get(i))){
                Log.d("catnameL.get(i)",":::"+both.get(i));
                if (both.get(i).equals("All")){
                    jsonalltransactions(getalltransactions_url);
                }else if (both.get(i).equals("Paid")){
                    jsonalltransactions(getalltransactions_url+"?type=1");
                }else if (both.get(i).equals("Receive")) {
                    jsonalltransactions(getalltransactions_url + "?type=2");
                }

            }
        }

        return view;

    }


    private void jsonalltransactions(String getalltransactions_url) {
        loader();

        Log.d("jsonParseuser", "all transactions " + getalltransactions_url);
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, getalltransactions_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JSONSendtransa", "---" + response);
                        animationView.cancelAnimation();
                        animationView.setVisibility(View.GONE);

                        model = new ArrayList<>();

                        if (response.length()!=0) {


                            for (int j = 0; j < response.length() ; j++ ) {
                                Log.d("lengtharayyy", ":::" + j);

                                ModelTransaction transaction=new ModelTransaction();
                                JSONObject jItem = null;
                                try {
                                    jItem = response.getJSONObject(j);
                                    String amount = jItem.getString("amount");
                                    String vidAmount = jItem.getString("vidAmount");
                                    String createdDate = jItem.getString("createdDate");

                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                                    Date localdate = dateFormat.parse(createdDate);
                                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
                                    String dateStr = formatter.format(localdate);
                                    Log.d("amountr@@@@", amount);
                                    String datesplitdep = dateStr.substring(0,10);
                                    Log.d("transaction", "date" + datesplitdep);


                                    if (jItem.has("userId")) {
                                        JSONArray userId = jItem.getJSONArray("userId");
                                        for (int k = 0; k < userId.length(); k++) {
                                            Log.d("lengtharakkk", ":::" + k);

                                            JSONObject useritems = null;
                                            try {
                                                useritems = userId.getJSONObject(k);
                                                String _id = useritems.getString("_id");

                                                if (useritems.has("name")) {
                                                    String name = useritems.getString("name");
                                                    transaction.setName(name);
                                                } else {
                                                    String name = " ";
                                                    transaction.setName(name);
                                                }

                                                if (useritems.has("profilePic")) {
                                                    String profilePic = useritems.getString("profilePic");
                                                    transaction.setProfilepic(profilePic);
                                                } else {
                                                    String profilePic = " ";
                                                    transaction.setProfilepic(profilePic);
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }else {
                                        transaction.setName(" ");
                                        transaction.setProfilepic("");

                                    }

                                    if (jItem.has("toId")) {
                                        JSONArray userId = jItem.getJSONArray("toId");
                                        for (int k = 0; k < userId.length(); k++) {
                                            Log.d("lengtharakkk", ":::" + k);

                                            JSONObject useritems = null;
                                            try {
                                                useritems = userId.getJSONObject(k);
                                                String _id = useritems.getString("_id");

                                                if (useritems.has("name")) {
                                                    String name = useritems.getString("name");
                                                    transaction.setTodousername(name);
                                                } else {
                                                    String name = " ";
                                                    transaction.setTodousername(name);
                                                }

                                                if (useritems.has("profilePic")) {
                                                    String profilePic = useritems.getString("profilePic");
                                                    transaction.setTodoprofilepic(profilePic);
                                                } else {
                                                    String profilePic = " ";
                                                    transaction.setTodoprofilepic(profilePic);
                                                }



                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }else {
                                        transaction.setTodousername("");
                                        transaction.setTodoprofilepic("");

                                    }
                                    transaction.setBalance(amount);
                                    transaction.setDate(datesplitdep);
                                    transaction.setType( jItem.getString("type"));

                                    model.add(transaction);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            transalist.setHasFixedSize(true);
                            // use a linear layout manager
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            transalist.setLayoutManager(mLayoutManager);
                            // model = new ArrayList<>();

                           /* for (int i1 = 0; i1 < amountL.size(); i1++) {
                                ModelTransaction mainMode = new ModelTransaction(dateL.get(i1),amountL.get(i1));
                                model.add(mainMode);
                            }*/

                            mainAdapter = new Adapter_AllTransactions(model, getActivity());
                            transalist.setAdapter(mainAdapter);
                            mainAdapter.notifyDataSetChanged();

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
                                            animationView.cancelAnimation();
                                            animationView.setVisibility(View.GONE);
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
                animationView.cancelAnimation();
                animationView.setVisibility(View.GONE);
            }
        });

    }

    public void loader(){
        //lotte loader
        animationView.setAnimation("data.json");
        animationView.loop(true);
        animationView.playAnimation();
        animationView.setVisibility(View.VISIBLE);
    }

}
