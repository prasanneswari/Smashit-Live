package com.vasmash.va_smash.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.ProfileScreen.ImagePickerActivity;
import com.vasmash.va_smash.ProfileScreen.Profile_Fragment;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.VaContentScreen.VolleyMultipartRequest;
import com.vasmash.va_smash.login.Adapters.CountryAdapter;
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
import com.vasmash.va_smash.login.fragments.RegisterFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vasmash.va_smash.VASmashAPIS.APIs.Countrysapi;
import static com.vasmash.va_smash.VASmashAPIS.APIs.regupload;
//this is the registration class
public class RegisterformActivity extends AppCompatActivity {
    Dialog dialog;
    private RequestQueue mQueue;
    TextView gendername, contry;
    EditText name, pass, repass, date1;
    Button reg_sub;
    ImageView maleline,femaleline,otherline;
    ImageView imagelay;
    LinearLayout rootlay;


    CountryAdapter countryAdapter;
    String countr = Countrysapi;

    ArrayList<Languages> dataModelArrayList;
    ListView cntry;
    RequestQueue sch_RequestQueue;
    SharedPreferences pref;
    String code="1";
    String radiovalue="null";
    String valueid;

    ImageButton upload_pic_btn;
    CircleImageView profile_image;

    private int GALLERY = 1, CAMERA = 2;
    Bitmap frontthunm=null;
    String gaid="null",gaprofilepic="null",ganame="null",gaemail="null",gamobile="null",gacountry="null",gacountrycode="null";
    EditText editsearch;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public static final int REQUEST_IMAGE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerform);
        sch_RequestQueue = Volley.newRequestQueue(getApplicationContext());
        pref = getApplicationContext().getSharedPreferences("loginid", 0);


        name = (EditText) findViewById(R.id.regst_name);
        pass = (EditText) findViewById(R.id.regst_pass);
        repass = (EditText) findViewById(R.id.regst_repass);
        date1 = (EditText) findViewById(R.id.date);

        reg_sub = (Button) findViewById(R.id.regst_submit);

        contry = (TextView) findViewById(R.id.reg_contry);
        maleline = findViewById(R.id.male);
        femaleline = findViewById(R.id.female);
        otherline = findViewById(R.id.other);
        gendername=findViewById(R.id.gendername);


        upload_pic_btn=findViewById(R.id.upload_pic_btn);
        profile_image=findViewById(R.id.profile_image);
        imagelay=findViewById(R.id.imagelay);
        rootlay=findViewById(R.id.rootlay);

        //we will gwt the data for requestotp
        if( getIntent().getExtras() != null) {
            Intent intent = getIntent();
            gaid=intent.getStringExtra("gaid");
            gaprofilepic=intent.getStringExtra("profilePic");
            ganame=intent.getStringExtra("name");
            gaemail=intent.getStringExtra("email");
            gamobile=intent.getStringExtra("mobile");
            gacountry=intent.getStringExtra("country");
            gacountrycode=intent.getStringExtra("countryCode");
            Log.d("profilepicc","::::"+gaprofilepic);

            Picasso.with(RegisterformActivity.this).load(gaprofilepic).placeholder(R.drawable.uploadpictureold).networkPolicy(NetworkPolicy.NO_CACHE).into(imagelay);
            Picasso.with(RegisterformActivity.this).load(gaprofilepic).placeholder(R.drawable.uploadpictureold).networkPolicy(NetworkPolicy.NO_CACHE).into(profile_image);

            name.setText(ganame);

        }
        //this is the country code popup selection
        contry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countrydialog();
            }
        });
        //this is the date picker code
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterformActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);

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
                Log.d(" ", "onDateSet: mm/dd/yyy: " + year + "/" + month + "/" + day);
                String date = year + "/" + month + "/" + day;
                date1.setText(date);
            }
        };


        reg_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameS=name.getText().toString().trim();
                String passS=pass.getText().toString().trim();
                String countryS=contry.getText().toString().trim();
                String idS=pref.getString("id", null);
                String cpassS=repass.getText().toString().trim();

                String repassS=date1.getText().toString();
                int yearclck = Integer.parseInt(repassS.substring(0,4));
                Log.d("jsnresponse..ccpS..", "---" + code + " ::");
                //here passowrd and cofirm pwd is matched we will entring
                if(passS.equals(cpassS)) {
                    if (nameS.isEmpty() || passS.isEmpty() || repassS.isEmpty() || radiovalue.isEmpty() ) {
                        // Toast.makeText(RegisterformActivity.this, "Please enter the All Fields", Toast.LENGTH_SHORT).show();
                        popupresponse("Please enter the All Fields");
                    }
                    //here only allow above 13 years age
                    else if (yearclck>2007){
                        popupresponse("Sorry, looks like you're not eligible for VASmash...But thanks for checking us out!");
                    }
                    //here all data intent to launge class
                    else {
                        Intent intent = new Intent(RegisterformActivity.this, Languagelist.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("nameS", nameS);
                        intent.putExtra("passS", passS);
                        intent.putExtra("repassS", repassS);
                        intent.putExtra("countryS", valueid);
                        intent.putExtra("code", code);
                        Log.d("iddsss",":::"+"::::"+gaid+"::::"+idS);

                        /*if (useridget != null &&!useridget.equals("null")) {
                            intent.putExtra("id", useridget);
                        }else*/ if (!gaid.equals("null")){
                            intent.putExtra("id", gaid);
                        }else {
                            intent.putExtra("id", idS);
                        }

                        startActivity(intent);
                    }
                }else {
                    //dialogbox("password and confirm password is not matched");
                    popupresponse("password and confirm password is not matched");
                }

            }
        });

        upload_pic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
                showPictureDialog();
            }
        });
        //this is the profile selector
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
                showPictureDialog();
            }
        });
        //this is the keyboard hiding code
        rootlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(repass.getWindowToken(),0);

            }
        });


    }

    //this is the gender radio clickings
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        //boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.maleimg:
                maleline.setImageResource(R.drawable.continubuttom);
                femaleline.setImageResource(R.drawable.genderback);
                otherline.setImageResource(R.drawable.genderback);
                code="1";
                gendername.setText(": Male");
               /* if (checked)
                    if (male.getText().equals("Male")) {
                        code="1";
                    }*/
                break;
            case R.id.femaleimg:
                maleline.setImageResource(R.drawable.genderback);
                femaleline.setImageResource(R.drawable.continubuttom);
                otherline.setImageResource(R.drawable.genderback);
                code="0";
                gendername.setText(": Female");
               /* if (checked)
                    if (female.getText().equals("Female")) {
                        code="0";
                    }*/
                break;
            case R.id.otherimg:
                maleline.setImageResource(R.drawable.genderback);
                femaleline.setImageResource(R.drawable.genderback);
                otherline.setImageResource(R.drawable.continubuttom);
                code="2";
                gendername.setText(": Other");
                /*if (checked)
                    if (other.getText().equals("Others / Not Specify")) {
                        code="2";
                    }*/
                break;
        }
    }

    public void countrydialog() {
        AlertDialog.Builder builder;
        final Context mContext = this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.country, null);


        TextView popup_tittle = (TextView) layout.findViewById(R.id.country_pop_title);
        cntry = (ListView) layout.findViewById(R.id.country_list);
        Button close=layout.findViewById(R.id.close);
        editsearch= layout.findViewById(R.id.searchedit);
        ImageView searchclose=layout.findViewById(R.id.serchclose);

        countryapi(countr);
        popup_tittle.setText("Select Country");
        cntry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = dataModelArrayList.get(position).getLang_code().toString();
                valueid = dataModelArrayList.get(position).getLang_name().toString();
                contry.setText(value);
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

    public void countryapi(String ul) {
        Log.d("countryurl", "::::"+ul);


        mQueue = Volley.newRequestQueue(RegisterformActivity.this.getApplicationContext());

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, ul, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());


                        dataModelArrayList = new ArrayList<>();

                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    Languages langu = new Languages();
                                    String _id = employee.getString("_id");
                                    String name = employee.getString("name");

                                    langu.setLang_name(_id);
                                    langu.setLang_code(name);
                                    dataModelArrayList.add(langu);


                                    Log.d("Response", "createddateL:::" + dataModelArrayList);
                                    Log.d("Response", "createddateL:::" + _id + name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            countryAdapter = new CountryAdapter(RegisterformActivity.this, dataModelArrayList);
                            cntry.setAdapter(countryAdapter);

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

    }
    private void showPictureDialog(){
        androidx.appcompat.app.AlertDialog.Builder pictureDialog = new androidx.appcompat.app.AlertDialog.Builder(this);
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
    //this code intent to the photo gallary
    public void choosePhotoFromGallary() {
/*
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
*/
        Intent intent = new Intent(RegisterformActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    //this code intent to the photo camara
    private void takePhotoFromCamera() {
/*
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
*/
        Intent intent = new Intent(RegisterformActivity.this, ImagePickerActivity.class);
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
                    profile_image.setImageBitmap(frontthunm);
                    // BitmapDrawable background = new BitmapDrawable(getResources(), frontthunm);
                    imagelay.setImageBitmap(frontthunm);
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
    //this is the gallary an dcamara permissions
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
    //this is the response popup
    public void popupresponse(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = RegisterformActivity.this;
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


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    //this is the image upload volley multipart code
    private void uploadBitmap(final Bitmap bitmap) {
        String userid=pref.getString("id", null);Log.d("555555", "newTicketurl URL" + regupload+userid+"::bitmap"+bitmap);
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, regupload+userid,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            System.out.println("Success 111111 "+obj);
                            // Toast.makeText(getApplicationContext(), "Profile Pic is uploaded", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Success 22222 "+ error.getMessage());
                        //Toast.makeText(getApplicationContext(), "Profile Pic is not uploaded", Toast.LENGTH_SHORT).show();
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
        finish();
    }


}
