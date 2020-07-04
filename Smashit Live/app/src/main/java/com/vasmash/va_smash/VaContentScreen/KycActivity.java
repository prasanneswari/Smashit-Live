package com.vasmash.va_smash.VaContentScreen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.vasmash.va_smash.VASmashAPIS.APIs;
import com.vasmash.va_smash.createcontent.PostcontentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.addupload_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.vaadddata_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.vastore_addContent;

public class KycActivity extends AppCompatActivity {
    EditText kycname,kycwebsite,kycaddress;
    ImageView kycimg;
    private int GALLERY = 1, CAMERA = 2;
    Bitmap frontthunm=null;
    SharedPreferences phoneauthshard;
    public static String token;
    private RequestQueue mQueue;
    String userid;
    Button kycsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);
        kycname=findViewById(R.id.kycname);
        kycwebsite=findViewById(R.id.kycwebsite);
        kycaddress=findViewById(R.id.kycaddress);
        kycimg=findViewById(R.id.kycimage);
        kycsubmit=findViewById(R.id.kycsubmit);
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(KycActivity.this);
        token = phoneauthshard.getString("token", "null");
        kycimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        kycsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendimg();
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
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {


            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    frontthunm = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    kycimg.setImageBitmap(frontthunm);

                    //Log.d("TAG", "frontbtnclick" + frontthunm);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == CAMERA) {
                frontthunm = (Bitmap) data.getExtras().get("data");
                kycimg.setImageBitmap(frontthunm);



                Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
            }

        }
    }
    public void sendimg(){



        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIs.vastore_uploadDocument,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("postimg", String.valueOf(response));
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.e("postimg", String.valueOf(obj));
                            JSONObject postid=obj.getJSONObject("data");



                            String img_id = postid.getString("_id");

                            final String akknameS=kycname.getText().toString();
                            final String webS=kycwebsite.getText().toString();
                            final String adresS=kycaddress.getText().toString();




                            if (akknameS.isEmpty() || webS.isEmpty() || adresS.isEmpty() ) {
                                Toast.makeText(KycActivity.this, "Please enter the All Fields", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                String AddS = "{\"_id\":\"" + img_id + "\",\"companyName\":\"" + akknameS + "\",\"companyAddress\":\"" + adresS + "\",\"companyWebSite\":\"" + webS + "\"}";
                                Log.d("jsnresponse add data", "---" + AddS);
                                JSONObject lstrmdt;

                                try {
                                    lstrmdt = new JSONObject(AddS);
                                    Log.d("jsnresponse....", "---" + AddS);
                                    JSONSenderVolleyadddata(lstrmdt);
                                } catch (JSONException ignored) {
                                }
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        viewDialog.hideDialog();

                        // spinner.setVisibility(View.INVISIBLE);

                        System.out.println("Successhoto "+ error.getMessage());
                        Toast.makeText(KycActivity.this,"network error"+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("document", new DataPart(imagename + ".jpg", getFileDataFromDrawable(frontthunm)));
                // System.out.println("paramss000000 "+ params);

                return params;

            }
        };

        //adding the request to volley
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        volleyMultipartRequest.setRetryPolicy(policy);
        Volley.newRequestQueue(KycActivity.this).add(volleyMultipartRequest);

    }



    public void JSONSenderVolleyadddata(JSONObject lstrmdt) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        Log.d("555555", "add data" + vaadddata_url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, vastore_addContent,lstrmdt,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONSenderadddata", "---" + response);
                        try {
                            if (response.length()!=0) {
                                String message = response.getString("message");

                                Toast.makeText(KycActivity.this, message, Toast.LENGTH_SHORT).show();
                                // jsonprofiledetails();

                                Intent intent = new Intent(KycActivity.this, TopNavigationview.class);
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
                                Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                if (obj.has("errors")) {
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    //Toast.makeText(PersonalInformation.this, message, Toast.LENGTH_SHORT).show();

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
    }

    public <T> void addToRequestQueue(Request<T> req) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        mQueue.add(req);
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public void finishActivity(View v){
        finish();
    }

}
