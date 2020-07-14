package com.vasmash.va_smash.login.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.vasmash.va_smash.login.Adapters.CountryCodeAdapter;
import com.vasmash.va_smash.login.ModelClass.Languages;
import com.vasmash.va_smash.login.OTPVerification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vasmash.va_smash.VASmashAPIS.APIs.Countrysapi;
import static com.vasmash.va_smash.VASmashAPIS.APIs.Sendotp;


/**
 * A simple {@link Fragment} subclass.
 */
//this is the registration Class
public class RegisterFragment extends AppCompatActivity {
    Dialog dialog;

    Button sendotp;
    LinearLayout rootlay;
    RequestQueue sch_RequestQueue;
    EditText enterphone,countrycode,entermailid;
    private String regid;
    SharedPreferences pref;

    //this is the loader animationview
    ViewDialog viewDialog;

    String countr = Countrysapi;

    ArrayList<Languages> dataModelArrayList;

    ListView lv, cntry;
    CountryCodeAdapter countryAdapter;
    String valueid,callingcode;
    EditText editsearch;
    LocationTrack locationTrack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);
        pref = getApplicationContext().getSharedPreferences("loginid", 0);
        viewDialog = new ViewDialog(this);

        licencepopup();
        enterphone=(EditText)findViewById(R.id.regst_emailid) ;
        countrycode=(EditText)findViewById(R.id.countrycode) ;
        entermailid=findViewById(R.id.emailid);

        sendotp=(Button)findViewById(R.id.regst_btn_otp);
        rootlay=findViewById(R.id.rootlay);

        sch_RequestQueue = Volley.newRequestQueue(getApplicationContext());
        //this is the current location data
        getlocation();


        //this is the send otp post api
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eml=enterphone.getText().toString();
                String code=countrycode.getText().toString();
                String entermailidS=entermailid.getText().toString();
                //this is the mobile number validation
                if (isValidMobile(eml)) {
                    if (code.isEmpty()) {
                        popup("Please Enter Country code");
                    } else {
                        String AddS = "{\"countryCode\":\"" + countrycode.getText().toString() + "\",\"username\":\"" + eml + "\"}";
                       // Log.d("jsnresponse pernonal", "---" + AddS);
                        JSONObject lstrmdt;
                        try {
                            lstrmdt = new JSONObject(AddS);
                            //Log.d("jsnresponse....", "---" + AddS);
                            // dialog_progress.show();
                            viewDialog.showDialog();
                            sendotp(lstrmdt);
                        } catch (JSONException ignored) {
                        }
                    }
                }
                //this is the emial validation
                else if (isEmailValid(entermailidS)){
                    String AddS = "{\"countryCode\":\"" + "" + "\",\"username\":\"" + entermailidS + "\"}";
                   // Log.d("jsnresponse pernonal", "---" + AddS);
                    JSONObject lstrmdt;
                    try {
                        lstrmdt = new JSONObject(AddS);
                        //Log.d("jsnresponse....", "---" + AddS);
                        // dialog_progress.show();
                        viewDialog.showDialog();
                        sendotp(lstrmdt);
                    } catch (JSONException ignored) {
                    }
                }else{
                    if (eml.isEmpty() && entermailidS.isEmpty()) {
                        //Toast.makeText(RegisterFragment.this, "Please enter the All Fields", Toast.LENGTH_SHORT).show();
                        popup("Please enter mobile number or email Id");
                    }
                    else {
                        String regexStr = "^[0-9]*$";
                        String mailgex="[a-zA-Z ]+";
                        if (enterphone.getText().toString().trim().matches(regexStr) && (!eml.isEmpty())) {
                            //write code here for success
                            popup("Please enter valid mobile number");
                        } else if (entermailid.getText().toString().trim().matches(mailgex)){
                            popup("Please enter valid email Id");
                        }
                    }
                }

            }
        });
        //this is the keybord hiding code
        rootlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im=(InputMethodManager)RegisterFragment.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(enterphone.getWindowToken(),0);

            }
        });
        enterphone.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //Log.d("editetxtval",":::;"+s.length());
                entermailid.getText().clear();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        entermailid.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                enterphone.getText().clear();
                countrycode.getText().clear();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        //hre we will select the country code
        countrycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countrydialog();
            }
        });
    }


    //this is the country code dialog
    public void countrydialog() {

        AlertDialog.Builder builder;
        final Context mContext = this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.country, null);

        TextView popup_tittle = (TextView) layout.findViewById(R.id.country_pop_title);
        cntry = (ListView) layout.findViewById(R.id.country_list);
        editsearch= layout.findViewById(R.id.searchedit);
        ImageView searchclose=layout.findViewById(R.id.serchclose);


        popup_tittle.setText("Select Country Code");
        Button close=layout.findViewById(R.id.close);

        countryapi(countr);
        // popup_tittle.setText("Select country");
        cntry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = dataModelArrayList.get(position).getLang_code().toString();
                valueid = dataModelArrayList.get(position).getLang_name().toString();
                callingcode = dataModelArrayList.get(position).getCallingcode().toString();

                countrycode.setText(callingcode);
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editsearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                countryAdapter.getFilter().filter(s.toString());
                // Log.d("NEW TAGS", "*** Search value changed: " + countryAdapter.getCount());
                cntry.invalidate();
                countryAdapter.notifyDataSetChanged();
                cntry.setAdapter(countryAdapter);

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        searchclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editsearch.getText().clear();
            }
        });

        builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();
    }
    private void sendotp(JSONObject lstrmdt) {
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, Sendotp,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.d("JSONSenderVolleylogin", "---" + response);
                        viewDialog.hideDialog();
                        try {
                            enterphone.setFocusable(true);
                            enterphone.setEnabled(true);
                            enterphone.setCursorVisible(true);
                            enterphone.setKeyListener(null);

                            SharedPreferences.Editor editor = pref.edit();
                            regid=response.getString("id");
                            editor.putString("id", regid);
                            editor.commit();
                            String msg=response.getString("message");
                            String status=response.getString("status");

                            //we will enter the mailid we will get status 2
                            if (status.equals("2")){
                                Intent i = new Intent(RegisterFragment.this, OTPVerification.class);
                                i.putExtra("number", entermailid.getText().toString());
                                startActivity(i);
                                finish();
                            }
                            //we will enter the phone number we will get status 1
                            else if (status.equals("1")){
                                Intent i = new Intent(RegisterFragment.this, OTPVerification.class);
                                i.putExtra("number", enterphone.getText().toString());
                                startActivity(i);
                                finish();
                                // Toast.makeText(RegisterFragment.this, regid , Toast.LENGTH_SHORT).show();
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
                                //Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                if (obj.has("errors")) {
                                    viewDialog.hideDialog();
                                    SharedPreferences.Editor editor = pref.edit();

                                    if (obj.has("id")) {
                                        regid = obj.getString("id");
                                        editor.putString("id", regid);
                                        editor.commit();
                                    }

                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    enterphone.getText().clear();
                                    countrycode.getText().clear();
                                    entermailid.getText().clear();
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
                viewDialog.hideDialog();
            }
        });
    }
    public <T> void addToRequestQueue(Request<T> req) {
        if (sch_RequestQueue == null) {
            sch_RequestQueue = Volley.newRequestQueue(this);
        }
        sch_RequestQueue.add(req);
    }

    public void countryapi(String ul) {

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, ul, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                       // Log.d("Response", response.toString());

                        dataModelArrayList = new ArrayList<>();

                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    Languages langu = new Languages();
                                    String code = employee.getString("code");
                                    String name = employee.getString("name");
                                    String callingCode = employee.getString("callingCode");
                                    String flag = employee.getString("flag");

                                    langu.setLang_name(code);
                                    langu.setLang_code(name);
                                    langu.setCallingcode(callingCode);
                                    langu.setFlag(flag);

                                    dataModelArrayList.add(langu);
                                    countryAdapter = new CountryCodeAdapter(RegisterFragment.this, dataModelArrayList);
                                    cntry.setAdapter(countryAdapter);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

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
                                        //Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
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
        sch_RequestQueue.add(getRequest);


    }


    public void popup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = RegisterFragment.this;
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

    //this is the mobile validation code
    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 5 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                // txtPhone.setError("Not Valid Number");
            } else {
                check = android.util.Patterns.PHONE.matcher(phone).matches();
            }
        } else {
            check = false;
        }
        return check;
    }
    //this is the email validation code
    public static boolean isEmailValid(String email) {
        boolean check;
        Pattern p;
        Matcher m;

       String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        return check;
    }

    //this is the licence popup code
    public void licencepopup() {

        android.app.AlertDialog.Builder builder;
        final Context mContext = RegisterFragment.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.licencespopup, null);

        Button agree = (Button) layout.findViewById(R.id.agree);
        Button decline = (Button) layout.findViewById(R.id.decline);
        ScrollView scrollView = layout.findViewById(R.id.scrollview);
        Button scrollbtn = layout.findViewById(R.id.scrollbtn);
        Button scrollup=layout.findViewById(R.id.scrollup);


        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterFragment.this, LoginFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        scrollbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(View.FOCUS_DOWN);
                scrollup.setVisibility(View.VISIBLE);
                scrollbtn.setVisibility(View.GONE);

            }
        });

        scrollup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(View.FOCUS_UP);
                scrollup.setVisibility(View.GONE);
                scrollbtn.setVisibility(View.VISIBLE);

            }
        });

        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();

    }


    //this is the location get code
    public void getlocation(){

        locationTrack = new LocationTrack(RegisterFragment.this);

        if (locationTrack.canGetLocation()) {

            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(RegisterFragment.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
               // Log.d("addresses", ":::::" + addresses);

                if (addresses.isEmpty()) {
                   // countrycode.setText("60");
                } else{
                    String countrycodes = addresses.get(0).getCountryCode();
                   // Log.d("TAG_DATA", getCountryZipCode(countrycodes));
                    countrycode.setText(getCountryZipCode(countrycodes));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            locationTrack.showSettingsAlert();
        }
    }
    public String getCountryZipCode(String country) {
        String CountryZipCode = "";

        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (String aRl : rl) {
            String[] g = aRl.split(",");
            if (g[1].trim().equals(country)) {
                CountryZipCode = "+" + g[0];
              //  Log.d("cuntrycode",":::"+CountryZipCode+":::"+g[1]);

                break;
            }
        }
        return CountryZipCode;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

}
