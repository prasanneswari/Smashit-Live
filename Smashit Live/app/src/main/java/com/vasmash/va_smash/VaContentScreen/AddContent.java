package com.vasmash.va_smash.VaContentScreen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.VASmashAPIS.APIs;
import com.vasmash.va_smash.login.fragments.LoginFragment;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vasmash.va_smash.VASmashAPIS.APIs.Catageriesapi;
import static com.vasmash.va_smash.VASmashAPIS.APIs.addupload_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.vaadddata_url;

public class AddContent extends AppCompatActivity {

    ImageView profileimg;
    ImageButton uploadimg;
    EditText addname,tags,descriptionvastore,addurl;
    Spinner catogry;
    Button savebtn;

    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    Bitmap frontthunm=null;

    public static String token;
    private RequestQueue mQueue;

    String  uploadid,spinnercatogryidpos;

    List<String> spinneridL;
    List<String> spinnercatogryL;
    public static boolean vacontent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        profileimg=findViewById(R.id.profile_image);
        uploadimg=findViewById(R.id.upload_pic_btn);
        addname=findViewById(R.id.addname);
        descriptionvastore=findViewById(R.id.descriptionvastore);
        addurl=findViewById(R.id.addurl);
        tags=findViewById(R.id.addtags);
        savebtn=findViewById(R.id.addsubmit);

        mQueue = Volley.newRequestQueue(AddContent.this.getApplicationContext());

        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(AddContent.this);
        token = phoneauthshard.getString("token", "null");


//        if (token.equals("null")){
//            Intent intent = new Intent(AddContent.this, LoginFragment.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }else {
//            jsoncatogryvalues();
//        }
        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestMultiplePermissions();
                showPictureDialog();

            }
        });
        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestMultiplePermissions();
                showPictureDialog();

            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendimg();
            }
        });

    }

    public void sendimg(){



        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIs.addupload_url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("postimg", String.valueOf(response));
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.e("postimg", String.valueOf(obj));
                            JSONObject postid=obj.getJSONObject("data");



                            String img_id = postid.getString("_id");

                            final String addnameS=addname.getText().toString();
                            final String tagsS=tags.getText().toString();
                            final String cost=descriptionvastore.getText().toString();
                            final String url=addurl.getText().toString();



                            if (addnameS.isEmpty() || tagsS.isEmpty() || cost.isEmpty()|| url.isEmpty() ) {
                                Toast.makeText(AddContent.this, "Please enter the All Fields", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                String AddS = "{\"name\":\"" + addnameS + "\",\"description\":\"" + cost + "\",\"_id\":\"" + img_id + "\",\"tags\":\"" + tagsS + "\",\"url\":\""+url+"\"}";
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
                        Toast.makeText(AddContent.this,"network error"+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".jpg", getFileDataFromDrawable(frontthunm)));
                // System.out.println("paramss000000 "+ params);

                return params;

            }
        };

        //adding the request to volley
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        volleyMultipartRequest.setRetryPolicy(policy);
        Volley.newRequestQueue(AddContent.this).add(volleyMultipartRequest);

    }



    public void JSONSenderVolleyadddata(JSONObject lstrmdt) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        Log.d("555555", "add data" + vaadddata_url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, vaadddata_url,lstrmdt,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONSenderadddata", "---" + response);
                        try {
                            if (response.length()!=0) {
                                String message = response.getString("message");

                                Toast.makeText(AddContent.this, message, Toast.LENGTH_SHORT).show();
                                // jsonprofiledetails();

                                Intent intent = new Intent(AddContent.this, TopNavigationview.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                vacontent=true;

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
                    profileimg.setImageBitmap(frontthunm);
                    uploadBitmap(frontthunm);
                    //Log.d("TAG", "frontbtnclick" + frontthunm);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == CAMERA) {
                frontthunm = (Bitmap) data.getExtras().get("data");
                profileimg.setImageBitmap(frontthunm);

                uploadBitmap(frontthunm);

                Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
            }

        }
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
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        //Log.d("555555", "newTicketurl URL" + url+"::bitmap"+bitmap);

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, addupload_url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            System.out.println("Success 2222222 "+response);

                            JSONObject obj = new JSONObject(new String(response.data));
                            System.out.println("Success 111111 "+obj);
                            uploadid=obj.getString("id");
                            String message=obj.getString("message");

                         /*   Intent intent = new Intent(getActivity(), TicketsData.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);*/
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Success 22222 "+ error.getMessage());
                        // Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));

                // System.out.println("paramss000000 "+ params);

                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);
    }

    public void finishActivity(View v){
        finish();
    }


}