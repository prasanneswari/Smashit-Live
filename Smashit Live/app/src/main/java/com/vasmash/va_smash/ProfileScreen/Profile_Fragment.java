package com.vasmash.va_smash.ProfileScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
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
import com.vasmash.va_smash.VaContentScreen.VolleyMultipartRequest;
import com.vasmash.va_smash.login.Adapters.CountryAdapter;
import com.vasmash.va_smash.login.Adapters.CountryCodeAdapter;
import com.vasmash.va_smash.login.ModelClass.Languages;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vasmash.va_smash.VASmashAPIS.APIs.Countrysapi;
import static com.vasmash.va_smash.VASmashAPIS.APIs.updateprofile_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.updateprofilepic_url;

public class Profile_Fragment extends AppCompatActivity {
    private RequestQueue mQueue;

    EditText cityT;
    TextView countryT,gendername;
    EditText contactno;
    EditText usernameT;
    EditText emalidT,date1,lastname;
    ImageView male,female,other,maleimg,femleimg,otherimg;
    EditText ccp;
    Button updatebtn;
    CountryPicker picker;
    ImageButton upload_pic_btn;
    CircleImageView profile_image;
    ListView codelist, cntry;
    Dialog dialog;
    LinearLayout rootlay;
    ImageView imagelay;
    boolean countrycodeclick;

    String radiovalue="null";
    int  yearpicker;
    ArrayList<Languages> dataModelArrayList;
    CountryAdapter countryAdapter;


    //upload profile
    private int GALLERY = 1, CAMERA = 2;
    Bitmap frontthunm=null;
    public static String proftoken,intentprofilepic,countrycode="null",firstName="null",lastName="null";
    String profilePicupload="null";

    //catogry
    String valueid;
    int code;
    //this is the loading animationview
    ViewDialog viewDialog;
    EditText editsearch;

    String dataclick;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public static final int REQUEST_IMAGE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_profile_, container, false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_);

        usernameT=findViewById(R.id.username);
        emalidT=findViewById(R.id.emialid);
        cityT=findViewById(R.id.city);
        //  countryT=view.findViewById(R.id.country);
        countryT =findViewById(R.id.reg_contry);

        contactno=findViewById(R.id.contactno);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        other=findViewById(R.id.other);
        maleimg=findViewById(R.id.maleimg);
        femleimg=findViewById(R.id.femaleimg);
        otherimg=findViewById(R.id.otherimg);
        ccp = findViewById(R.id.countrycode);
        updatebtn=findViewById(R.id.updatebtn);
        lastname = (EditText) findViewById(R.id.lastname);
        date1 = (EditText) findViewById(R.id.date);


        upload_pic_btn=findViewById(R.id.upload_pic_btn);
        profile_image=findViewById(R.id.profile_image_placeholder);
        rootlay=findViewById(R.id.rootlay);
        imagelay=findViewById(R.id.imagelay);
        gendername=findViewById(R.id.gendername);

        mQueue = Volley.newRequestQueue(getApplicationContext());
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(this);
        proftoken = phoneauthshard.getString("token", "null");
        viewDialog = new ViewDialog(this);

        geteditdetails();
        //here it uploads profile pic
        upload_pic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
                showPictureDialog();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
                showPictureDialog();
            }
        });
        //here countrys are displayed
        ccp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countrycodeclick=true;
                countrydialog();
            }
        });

        countryT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");

            }
        });

        picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                countryT.setText(name);
                picker.dismiss();
            }
        });
        //this is the date picker
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Profile_Fragment.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getDatePicker().setMaxDate(new Date().getTime());

                // Divider changing:
                DatePicker dpView = dialog.getDatePicker();
                LinearLayout llFirst = (LinearLayout) dpView.getChildAt(0);
                LinearLayout llSecond = (LinearLayout) llFirst.getChildAt(0);
                for (int i = 0; i < llSecond.getChildCount(); i++) {
                    NumberPicker picker = (NumberPicker) llSecond.getChildAt(i); // Numberpickers in llSecond
                    // reflection - picker.setDividerDrawable(divider); << didn't seem to work.
                    Field[] pickerFields = NumberPicker.class.getDeclaredFields();
                    for (Field pf : pickerFields) {
                        if (pf.getName().equals("mSelectionDivider")) {
                            pf.setAccessible(true);
                            try {
                                pf.set(picker, getResources().getDrawable(R.color.red_color_picker));
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }


                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
              //  Log.d(" ", "onDateSet: mm/dd/yyy: " + year + "/" + month + "/" + day);
                String date = year + "/" + month + "/" + day;
                date1.setText(date);
            }
        };
        //here we select the gender
        maleimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=1;
                // Log.d("enteringgg..malee..", "---" + code+" ::");
                maleimg.setBackgroundResource(R.drawable.femaleicon);
                femleimg.setImageResource(R.drawable.femalelight);
                otherimg.setImageResource(R.drawable.otherlight);

                male.setBackgroundResource(R.drawable.continubuttom);
                female.setImageResource(R.drawable.genderback);
                other.setImageResource(R.drawable.genderback);
                gendername.setText(": Male");
            }
        });
        femleimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                code=0;
                //Log.d("enteringgg..femalee..", "---" + code+" ::");
                maleimg.setBackgroundResource(R.drawable.malelight);
                femleimg.setImageResource(R.drawable.maleicon);
                otherimg.setImageResource(R.drawable.otherlight);

                male.setBackgroundResource(R.drawable.genderback);
                female.setImageResource(R.drawable.continubuttom);
                other.setImageResource(R.drawable.genderback);
                gendername.setText(": Female");

            }
        });
        otherimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=2;
                maleimg.setBackgroundResource(R.drawable.malelight);
                femleimg.setImageResource(R.drawable.femalelight);
                otherimg.setImageResource(R.drawable.transgendericon);

                male.setBackgroundResource(R.drawable.genderback);
                female.setImageResource(R.drawable.genderback);
                other.setImageResource(R.drawable.continubuttom);
                gendername.setText(": Other");

            }
        });

        countryT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countrycodeclick=false;
                countrydialog();

            }
        });

        rootlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(contactno.getWindowToken(),0);

            }
        });
        //this is the user update edit profile
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernameS=usernameT.getText().toString();
                final String lastnameS=lastname.getText().toString();
                final String emalidS=emalidT.getText().toString();
                final String cityS=cityT.getText().toString();
                final String countryTS=countryT.getText().toString();
                final String contactnoS=contactno.getText().toString();

                final String ccpS=ccp.getText().toString();
                String datefomate=date1.getText().toString();
               // Log.d("enteringgg..ccpS..", "---" + datefomate+" ::");
                if (usernameS.isEmpty() || lastnameS.isEmpty() || countryTS.isEmpty() /*|| contactnoS.isEmpty() || ccpS.isEmpty()*/ ||radiovalue.isEmpty()) {
                    //  Toast.makeText(Profile_Fragment.this, "Please enter the All Fields", Toast.LENGTH_SHORT).show();
                    popup("Please enter the All Fields");
                }
/*
                else if (yearpicker < 12){
                    popup("Sorry, you must be at least 13 years old to create an account");
                }
*/
                else {
                  //  Log.d("countryTS", "countryid:::"+countryTS);
                    String AddS = "{\"firstName\":\"" + usernameS + "\",\"lastName\":\"" + lastnameS + "\",\"email\":\"" + emalidS + "\",\"mobile\":\"" + contactnoS + "\",\"gender\":"+code+",\"country\":\"" + valueid + "\",\"city\":\""+cityS+"\",\"countryCode\":\"" + ccpS + "\",\"dob\":\"" + datefomate + "\"}";
                    //Log.d("jsnresponse pernonal", "---" + AddS);
                    JSONObject lstrmdt;
                    try {
                        lstrmdt = new JSONObject(AddS);
                        //Log.d("jsnresponse....", "---" + AddS);
                        viewDialog.showDialog();
                        JSONSenderVolleychecked(lstrmdt);
                    } catch (JSONException ignored) {
                    }
                }
            }
        });

        //  return view;
    }
    //here get the data from profile activity
    public void geteditdetails(){

        if( getIntent().getExtras() != null) {
            Intent intent = getIntent();

            intentprofilepic=intent.getStringExtra("userimg");
            countrycode=intent.getStringExtra("countryCode");
            firstName=intent.getStringExtra("firstName");
            lastName=intent.getStringExtra("lastName");

            if (!(firstName.equals("null"))) {
                usernameT.setText(firstName);
            }
            if (!(lastName.equals("null"))) {
                lastname.setText(lastName);
            }

            //Log.d("profilrfragment","countrycode:::"+countrycode);
            if (!(countrycode.equals("null"))) {
                ccp.setText(intent.getStringExtra("countryCode"));
            }
            Picasso.with(Profile_Fragment.this).load(intentprofilepic).placeholder(R.drawable.uploadpiclight).networkPolicy(NetworkPolicy.NO_CACHE).into(profile_image);
            Picasso.with(Profile_Fragment.this).load(intentprofilepic).placeholder(R.drawable.uploadpiclight).networkPolicy(NetworkPolicy.NO_CACHE).into(imagelay);
            int gendeval=intent.getIntExtra("gender",0);
            if (gendeval==1) {
                maleimg.setBackgroundResource(R.drawable.femaleicon);
                femleimg.setImageResource(R.drawable.femalelight);
                otherimg.setImageResource(R.drawable.otherlight);

                male.setBackgroundResource(R.drawable.continubuttom);
                female.setImageResource(R.drawable.genderback);
                other.setImageResource(R.drawable.genderback);
                code =gendeval;
                gendername.setText(": Male");

            } else if (gendeval==0) {
                maleimg.setBackgroundResource(R.drawable.malelight);
                femleimg.setImageResource(R.drawable.maleicon);
                otherimg.setImageResource(R.drawable.otherlight);

                male.setBackgroundResource(R.drawable.genderback);
                female.setImageResource(R.drawable.continubuttom);
                other.setImageResource(R.drawable.genderback);
                code = gendeval;
                gendername.setText(": Female");

            } else if (gendeval==2) {
                maleimg.setBackgroundResource(R.drawable.malelight);
                femleimg.setImageResource(R.drawable.femalelight);
                otherimg.setImageResource(R.drawable.transgendericon);

                male.setBackgroundResource(R.drawable.genderback);
                female.setImageResource(R.drawable.genderback);
                other.setImageResource(R.drawable.continubuttom);
                code = gendeval;
                gendername.setText(": Other");

            }


            if (intent.getStringExtra("mobile")!=null) {
                contactno.setText(intent.getStringExtra("mobile"));
            }
            dataclick=intent.getStringExtra("dob");
            if (dataclick!=null) {
                String year = dataclick.substring(0, 4);
                String month = dataclick.substring(6, 7);
                String date = dataclick.substring(8, 10);
                date1.setText(dataclick);
            }

            emalidT.setText(intent.getStringExtra("email"));
            countryT.setText(intent.getStringExtra("contryname"));
            cityT.setText(intent.getStringExtra("city"));
            valueid = intent.getStringExtra("contryid");
        }
    }

    public void countrydialog() {
        android.app.AlertDialog.Builder builder;
        final Context mContext = this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.country, null);


        TextView popup_tittle = (TextView) layout.findViewById(R.id.country_pop_title);
        Button close=layout.findViewById(R.id.close);
        cntry = (ListView) layout.findViewById(R.id.country_list);
        codelist = (ListView) layout.findViewById(R.id.codelist);

        editsearch= layout.findViewById(R.id.searchedit);
        ImageView searchclose=layout.findViewById(R.id.serchclose);

        if (countrycodeclick){
            popup_tittle.setText("Select Country Code");
            countryapi(Countrysapi);
            //  countrycodeclick=false;
        }else {
            popup_tittle.setText("Select Country");
            countryapi(Countrysapi);
            // countrycodeclick=true;
        }
        cntry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = dataModelArrayList.get(position).getLang_code().toString();
                valueid = dataModelArrayList.get(position).getLang_name().toString();
                    countryT.setText(value);
                //Log.d("idLL","::"+valueid);
                dialog.dismiss();
            }
        });
        codelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = dataModelArrayList.get(position).getCallingcode().toString();
                valueid = dataModelArrayList.get(position).getLang_name().toString();
                ccp.setText(value);
                //Log.d("idLL","::"+valueid);
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        searchclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editsearch.getText().clear();
            }
        });


        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();

    }


    public void JSONSenderVolleychecked(JSONObject lstrmdt) {
        // Log.d("---reqotpurl-----", "---" + login_url);
       // Log.d("555555", "update profile" + updateprofile_url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, updateprofile_url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.d("JSONSenderprofile", "---" + response);
                        viewDialog.hideDialog();
                        try {
                            if (response.length()!=0) {
                                String message = response.getString("message");
                                    Intent intent = new Intent(Profile_Fragment.this, ProfileActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
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
                               // Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                if (obj.has("errors")) {
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    //Toast.makeText(PersonalInformation.this, message, Toast.LENGTH_SHORT).show();
                                    popup(message);
                                }

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;

                        case 500:
                            try {
                               String  body1 = new String(error.networkResponse.data,"UTF-8");
                               // Log.d("body", "---" + body1);
                                JSONObject obj = new JSONObject(body1);
                                if (obj.has("errors")) {
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    //Toast.makeText(PersonalInformation.this, message, Toast.LENGTH_SHORT).show();
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
                headers.put("Authorization",proftoken);
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

    public void countryapi(String ul) {


        mQueue = Volley.newRequestQueue(getApplicationContext());

        // prepare the Request
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, ul, null,
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
                                    String _id = employee.getString("_id");
                                    String code = employee.getString("code");
                                    String name = employee.getString("name");
                                    String callingCode = employee.getString("callingCode");
                                    String flag = employee.getString("flag");

                                    langu.setLang_name(code);
                                    langu.setLang_code(name);
                                    langu.setCallingcode(callingCode);
                                    langu.setFlag(flag);

                                    langu.setLang_name(_id);
                                    dataModelArrayList.add(langu);

/*
                                    Log.d("Response", "createddateL:::" + dataModelArrayList);
                                    Log.d("Response", "createddateL:::" + _id + name);
*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                           // Log.d("clikkkk","::::"+countrycodeclick);
                            if (countrycodeclick){
                                codelist.setVisibility(View.VISIBLE);
                                cntry.setVisibility(View.GONE);
                                CountryCodeAdapter countryAdapter1 = new CountryCodeAdapter(Profile_Fragment.this, dataModelArrayList);
                                codelist.setAdapter(countryAdapter1);

                                editsearch.addTextChangedListener(new TextWatcher() {
                                    public void afterTextChanged(Editable s) {
                                        countryAdapter1.getFilter().filter(s.toString());
                                        // Log.d("NEW TAGS", "*** Search value changed: " + countryAdapter.getCount());
                                        codelist.invalidate();
                                        countryAdapter1.notifyDataSetChanged();
                                        codelist.setAdapter(countryAdapter1);

                                    }

                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }
                                });


                                countrycodeclick=false;
                            }else {
                                codelist.setVisibility(View.GONE);
                                cntry.setVisibility(View.VISIBLE);

                                countryAdapter = new CountryAdapter(Profile_Fragment.this, dataModelArrayList);
                                cntry.setAdapter(countryAdapter);

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

                                countrycodeclick=true;

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
                                       // Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
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

            }
        });


    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
/*
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
*/
        Intent intent = new Intent(Profile_Fragment.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void takePhotoFromCamera() {
/*
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
*/
        Intent intent = new Intent(Profile_Fragment.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    frontthunm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    uploadBitmap(frontthunm);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

/*
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Log.d("TAG", "entering if:::" +data);

                Uri contentURI = data.getData();
                try {

                    frontthunm = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    uploadBitmap(frontthunm);
                    Log.d("TAG", "frontbtnclick" + frontthunm);
                } catch (IOException e) {
                    e.printStackTrace();
                   // Toast.makeText(Profile_Fragment.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == CAMERA) {
            Log.d("TAG", "requestCode:::" );

            frontthunm = (Bitmap) data.getExtras().get("data");
            Log.d("TAG", "frontthunm:::::" + frontthunm);
            uploadBitmap(frontthunm);
        }
*/


    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        //Toast.makeText(getActivity(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void uploadBitmap(final Bitmap bitmap) {
       viewDialog.showDialog();
       // Log.d("555555", "newTicketurl URL" + updateprofilepic_url+"::bitmap"+bitmap);
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, updateprofilepic_url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            System.out.println("Success 111111 "+obj);
                            viewDialog.hideDialog();
                            Toast.makeText(getApplicationContext(), "Profile Pic is uploaded", Toast.LENGTH_SHORT).show();

                            profilePicupload=obj.getString("profilePic");
                            String message=obj.getString("message");
                            //Log.d("uploadprofile",":::"+profilePicupload);

                            Picasso.with(Profile_Fragment.this).load(profilePicupload).placeholder(R.drawable.uploadpiclight).networkPolicy(NetworkPolicy.NO_CACHE).into(profile_image);
/*
                            BitmapDrawable background = new BitmapDrawable(getResources(), profilePic);
                            imagelay.setBackground(background);
*/
                            Picasso.with(Profile_Fragment.this).load(profilePicupload).placeholder(R.drawable.uploadpiclight).networkPolicy(NetworkPolicy.NO_CACHE).into(imagelay);

                           // Toast.makeText(Profile_Fragment.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Success 22222 "+ error.getMessage()+":::"+error.toString());
                        viewDialog.hideDialog();
                        Picasso.with(Profile_Fragment.this).load(intentprofilepic).placeholder(R.drawable.uploadpiclight).networkPolicy(NetworkPolicy.NO_CACHE).into(profile_image);
                        Picasso.with(Profile_Fragment.this).load(intentprofilepic).placeholder(R.drawable.uploadpiclight).networkPolicy(NetworkPolicy.NO_CACHE).into(imagelay);
                        Toast.makeText(getApplicationContext(), "Profile Pic is not uploaded", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("profilePic", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                // System.out.println("paramss000000 "+ params);

                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
        volleyMultipartRequest.setRetryPolicy(new RetryPolicy() {
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

    public void finishActivity(View v){
        Intent intent = new Intent();
        intent.putExtra("editTextValue", profilePicupload);
        setResult(RESULT_OK, intent);
        finish();

    }

    public void popup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = Profile_Fragment.this;
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


}
