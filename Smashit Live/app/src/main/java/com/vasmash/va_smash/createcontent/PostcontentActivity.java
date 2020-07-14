package com.vasmash.va_smash.createcontent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.vasmash.va_smash.BottmNavigation.TopNavigationview;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.VASmashAPIS.APIs;
import com.vasmash.va_smash.VaContentScreen.VolleyMultipartRequest;
import com.vasmash.va_smash.createcontent.cameraedit.StickerView;
import com.vasmash.va_smash.login.Adapters.CountryAdapter;
import com.vasmash.va_smash.login.ModelClass.Languages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment.killerplyer;

public class PostcontentActivity extends AppCompatActivity {
    ImageView img,public_back,post_img;
    FrameLayout public_screen;
    TextView follow_content,lang,catag,hashtag,post_cate,post_lang;
    Button sendpost,savepost;
    Dialog dialog;
    File fil;
    StickerView stk;
    ArrayList<Languages> popuparray;
    ArrayList<Languages> catgery;
    private RequestQueue mQueue;
    CountryAdapter language_popup_adapter;
    Post_lang_cate_Adapter post_lang_cate_adapter;
    Post_lang_Adapter post_lang_adapter;
    ListView popup_recy,post_listview_lang,post_listview_cate;
    ArrayList<Languages> items;

    ArrayList<Languages> categ;
    String cat;
    CheckBox check0,check1,check2;
    EditText post_description;
    int cam=2;
    Bitmap bitmap;
    public static String post;
    String catg_id="0";
    String lang_id="0";
    String userid=null;
    String post_visibilty;
    public String defaultVideo;
    VideoView post_video;
    String path;
    Uri uri;
    String ct;
    String status="Public";
    int preSelectedIndex = -1;
    int preSelectedIndex1 = -1;
    // ProgressBar spinner,spinner2;

    ViewDialog viewDialog;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcontent);
        public_screen=(FrameLayout)findViewById(R.id.public_screen);
        follow_content=(TextView)findViewById(R.id.follow_content);
        lang=(TextView)findViewById(R.id.post_languages);
        catag=(TextView)findViewById(R.id.post_catagories);
        post_cate=(TextView)findViewById(R.id.post_cate);
        post_lang=(TextView)findViewById(R.id.post_lang);
        hashtag=(TextView)findViewById(R.id.post_hashtag);
        post_description=(EditText) findViewById(R.id.post_description);
        //spinner=(ProgressBar)findViewById(R.id.progressBar);
        //spinner2=(ProgressBar)findViewById(R.id.progressBar2);

        viewDialog = new ViewDialog(this);

        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(PostcontentActivity.this);
        post = phoneauthshard.getString("token", "null");
        userid = phoneauthshard.getString("id", "null");
       // Log.e("token",post);

        sendpost=(Button)findViewById(R.id.sendpost);
        savepost=(Button)findViewById(R.id.draftpost);
        follow_content.setText(status);



        img=(ImageView)findViewById(R.id.content_back);
        post_img=(ImageView)findViewById(R.id.post_img);
        public_back=(ImageView)findViewById(R.id.public_back);
        post_video=(VideoView) findViewById(R.id.post_video);
        post_listview_lang=(ListView) findViewById(R.id.post_listview_langes);
        post_listview_cate=(ListView) findViewById(R.id.post_listview_cate);
        post_listview_cate.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        post_listview_lang.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        check0=(CheckBox)findViewById(R.id.checkBox0);
        check1=(CheckBox)findViewById(R.id.checkBox1);
        check2=(CheckBox)findViewById(R.id.checkBox2);
        check0.setChecked(true);
        post_visibilty="0";
        post_lang.setBackground(getResources().getDrawable(R.drawable.homeback));
        cat= APIs.Languagesapi;
        ct="languages";
        langapi();
        cateapi();

        cam=getIntent().getIntExtra("cam",2);
        path=getIntent().getStringExtra("path");

//        path="/data/user/0/com.vasmash.va_smash/files/VA_Smash/Smash.mp4";

        //Log.e("pathpostt", String.valueOf(cam)+"   "+ path);

        if (cam==0){
            post_video.setVisibility(View.INVISIBLE);
            post_img.setVisibility(View.VISIBLE);
        }
        else if (cam==1){
            post_video.setVisibility(View.VISIBLE);
            post_img.setVisibility(View.INVISIBLE);
            uri = Uri.parse(path);
            post_video.setVideoURI(uri);
            post_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            post_video.start();
            //Log.e("cam11111", String.valueOf(cam)+"   "+path);
        }
        if(getIntent().hasExtra("byteArray")) {
            ImageView _imv= new ImageView(this);
            bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            post_img.setImageBitmap(bitmap);
        }
        post_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_lang.setBackground(getResources().getDrawable(R.drawable.empty));
                post_cate.setBackground(getResources().getDrawable(R.drawable.homeback));
                post_listview_lang.setVisibility(View.INVISIBLE);
                post_listview_cate.setVisibility(View.VISIBLE);

            }
        });
        post_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_cate.setBackground(getResources().getDrawable(R.drawable.empty));
                post_lang.setBackground(getResources().getDrawable(R.drawable.homeback));
                post_listview_cate.setVisibility(View.INVISIBLE);
                post_listview_lang.setVisibility(View.VISIBLE);

            }
        });
        check0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    check1.setChecked(false);
                    check2.setChecked(false);
                    post_visibilty="0";
                    status="Followers";
                    follow_content.setText(status);
                }
            }
        });
        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    check0.setChecked(false);
                    check2.setChecked(false);
                    post_visibilty="1";
                    status="Public";
                    follow_content.setText(status);
                }
            }
        });
        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    check1.setChecked(false);
                    check0.setChecked(false);
                    post_visibilty="2";
                    status="Private";
                    follow_content.setText(status);
                }
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        public_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                public_screen.setVisibility(View.INVISIBLE);

            }
        });

        follow_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                public_screen.setVisibility(View.VISIBLE);
                closeKeyBoard();

            }
        });


        post_listview_cate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                items.get(position);
                String value=items.get(position).getLang_name().toString();
                String valueid=items.get(position).getLang_code().toString();

                catg_id=value;
                //Log.e("wwwwww",catg_id+" "+valueid);
                // Toast.makeText(PostcontentActivity.this, catg_id+" "+valueid, Toast.LENGTH_SHORT).show();
                Languages model = items.get(position); //changed it to model because viewers will confused about it

                model.setSelected(true);

                items.set(position, model);

                if (preSelectedIndex > -1){

                    Languages preRecord = items.get(preSelectedIndex);
                    preRecord.setSelected(false);

                    items.set(preSelectedIndex, preRecord);

                }

                preSelectedIndex = position;

                //now update adapter so we are going to make a update method in adapter
                //now declare adapter final to access in inner method

                post_lang_cate_adapter.updateRecords(items);
            }
        });
        post_listview_lang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categ.get(position);
                String value=categ.get(position).getLang_name().toString();
                String valueid=categ.get(position).getLang_code().toString();


                lang_id=value;
                //Log.e("wwwwww",lang_id+" "+valueid);
                // Toast.makeText(PostcontentActivity.this, lang_id+" "+valueid, Toast.LENGTH_SHORT).show();
                Languages model = categ.get(position); //changed it to model because viewers will confused about it

                model.setSelected(true);

                categ.set(position, model);

                if (preSelectedIndex1 > -1){

                    Languages preRecord1 = categ.get(preSelectedIndex1);
                    preRecord1.setSelected(false);

                    categ.set(preSelectedIndex1, preRecord1);

                }

                preSelectedIndex1 = position;

                //now update adapter so we are going to make a update method in adapter
                //now declare adapter final to access in inner method

                post_lang_adapter.updateRecords(categ);
            }
        });



//        lang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickdialog("languages");
//            }
//        });
//        catag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickdialog("categoties");
//            }
//        });
        hashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postedit= post_description.getText().toString().trim();
                post_description.setText(postedit+ " "+"#");
                post_description.setSelection(post_description.getText().length());
                // Focus the field.
                post_description.requestFocus();
                // Show soft keyboard for the user to enter the value.
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(post_description, InputMethodManager.SHOW_IMPLICIT);

            }
        });


        sendpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post_description.getText().toString().trim().equals("")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(PostcontentActivity.this);
                    builder1.setMessage("Please write description");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });



                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                else {
                    if (catg_id.equals("0") || lang_id.equals("0")) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(PostcontentActivity.this);
                        builder1.setMessage("Select language and category");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });



                        AlertDialog alert11 = builder1.create();
                        alert11.show();

//                }
                    }
                    else{
                        loaddialog();
                        if (cam == 0) {
//                        viewDialog.showDialog();
                            //  spinner.setVisibility(View.VISIBLE);
                           // Log.e("twrwrwr0", catg_id + lang_id);
//                if (ca.equals("Categories") && la.equals("Laguages")){
                            sendimg();
                            sendpost.setClickable(false);
                        } else if (cam == 1) {
//                        viewDialog.showDialog();
                            // spinner.setVisibility(View.VISIBLE);
                            //Log.e("twrwrwr1", catg_id + lang_id);
                            sendvideo();
                            sendpost.setClickable(false);
                        }




                    }
                }
            }
        });
        savepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }



    public void dialog(String pnhs){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_post);
        TextView pointsearned=(TextView)dialog.findViewById(R.id.lastpopup_earnpoints);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                killerplyer();
                dialog.cancel();
                finish();
                Intent intent=new Intent(PostcontentActivity.this,  TopNavigationview.class);
                startActivity(intent);
            }
        }, 2000);
        dialog.show();


    }
    //    public void pickdialog(final String ct){
//        AlertDialog.Builder builder;
//        final Context mContext = this;
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.post_popup,(ViewGroup) findViewById(R.id.root));
//        popup_recy = (ListView) layout.findViewById(R.id.popup_recy);
//        TextView popup_tittle=(TextView)layout.findViewById(R.id.popup_tittle);
//        popup_tittle.setText(ct);
//        if(ct.equals("categoties")){
//            cat= APIs.Catageriesapi;
//        }
//        if(ct.equals("languages")){
//            cat= APIs.Languagesapi;
//
//        }
//        langapi(cat,ct);
//
//
//        popup_recy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String value=popuparray.get(position).getLang_name().toString();
//                String valueid=popuparray.get(position).getLang_code().toString();
//
//
//                if(ct.equals("categoties")){
//
//                    catag.setText(valueid);
//                    catg_id=value;
//
//
//                }
//                if (ct.equals("languages")){
//                    lang.setText(valueid);
//                    lang_id=value;
//
//                }
//
//
//                dialog.dismiss();
//
//            }
//        });
//
//        builder = new AlertDialog.Builder(mContext);
//        builder.setView(layout);
//        dialog = builder.create();
//        dialog.show();
//
//    }




    //   catageries
    public void cateapi() {
        // spinner2.setVisibility(View.VISIBLE);

//        viewDialog.showDialog();

        mQueue = Volley.newRequestQueue(PostcontentActivity.this.getApplicationContext());

        // prepare the Request
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Catageriesapi, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                       // Log.d("Response", response.toString());
//                        viewDialog.hideDialog();

                        // spinner2.setVisibility(View.INVISIBLE);
                        items = new ArrayList<>();

                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    Languages langu = new Languages();
                                    String _id = employee.getString("_id");
                                    String name = employee.getString("name");
                                    boolean sel=false;
                                    langu.setLang_name(_id);
                                    langu.setLang_code(name);
                                    langu.setSelected(sel);
                                    items.add(langu);





/*
                                    Log.d("Response", "createddateL:::" + items);
                                    Log.d("Response", "createddateL:::" + _id + name);
*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            post_lang_cate_adapter= new Post_lang_cate_Adapter(PostcontentActivity.this, items);
                            post_listview_cate.setAdapter(post_lang_cate_adapter);


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


    //languaes api
    public void langapi() {
        //spinner2.setVisibility(View.VISIBLE);
//        viewDialog.showDialog();

        mQueue = Volley.newRequestQueue(PostcontentActivity.this.getApplicationContext());

        // prepare the Request
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Languagesapi, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        //Log.d("Response", response.toString());

//                        viewDialog.hideDialog();

                        categ = new ArrayList<>();
                        //  spinner2.setVisibility(View.INVISIBLE);
                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    Languages langu = new Languages();
                                    String _id = employee.getString("_id");
                                    String name = employee.getString("name");
                                    boolean sel=false;

                                    langu.setLang_name(_id);
                                    langu.setLang_code(name);
                                    langu.setSelected(sel);
                                    categ.add(langu);





/*
                                    Log.d("Response", "createddateL:::" + categ);
                                    Log.d("Response", "createddateL:::" + _id + name);
*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            post_lang_adapter= new Post_lang_Adapter(PostcontentActivity.this, categ);
                            post_listview_lang.setAdapter(post_lang_adapter);
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


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    //send data to server api
    public void JSONSenderVolleychecked(JSONObject lstrmdt) {
        // Log.d("---reqotpurl-----", "---" + login_url);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, APIs.add_description_content,lstrmdt,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("JSONSenderprofile", "---" + response);

                        //  spinner.setVisibility(View.INVISIBLE);
                        sendpost.setClickable(true);
                        dialog.cancel();
                        try {
                            if (response.length()!=0) {
                                String message = response.getString("message");
                                if (message.equals("Post Added successfully")) {
//                                    viewDialog.hideDialog();

                                    //  Toast.makeText(PostcontentActivity.this, message, Toast.LENGTH_SHORT).show();

                                    dialog.cancel();
                                    dialog("10");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // spinner.setVisibility(View.INVISIBLE);
                String body;
                dialog.cancel();
                //get status code here
                //Log.d("statusCode", "---" + error.toString());
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 422:
                            try {
                                body = new String(error.networkResponse.data,"UTF-8");
                              //  Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                if (obj.has("errors")) {
//                                    viewDialog.hideDialog();
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
                headers.put("Authorization",post);
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
        req.setRetryPolicy(new RetryPolicy() {
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

//                viewDialog.hideDialog();
                dialog.cancel();
            }
        });

    }



    //send image to server api
    public void sendimg(){



        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIs.add_image_content,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                       // Log.e("postimg", String.valueOf(response));
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                           // Log.e("postimg", String.valueOf(obj));
                            JSONObject postid=obj.getJSONObject("data");

                            String img_id = postid.getString("_id");
                           // Log.e("idddd",img_id);
                            String categoryId=catg_id;
                            String contentLangId=lang_id;
                            String descp = post_description.getText().toString().trim();

                            String AddS = "{\"categoryId\":\"" + categoryId + "\",\"contentLangId\":\"" + lang_id + "\",\"_id\":\"" + img_id + "\",\"userId\":\"" + userid + "\",\"description\":\"" + descp + "\",\"visibility\":"+ post_visibilty + "}";
                           // Log.d("jsnresponse pernonal", "---" + AddS);
                            JSONObject lstrmdt;
                            try {
                                lstrmdt = new JSONObject(AddS);
                               // Log.d("jsnresponse....", "---" + AddS);
                                JSONSenderVolleychecked(lstrmdt);
                            } catch (JSONException ignored) {
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
                        dialog.cancel();
                        // spinner.setVisibility(View.INVISIBLE);
                        sendpost.setClickable(true);
                        System.out.println("Successhoto "+ error.getMessage());
                        Toast.makeText(PostcontentActivity.this,"network error"+ error.getMessage(), Toast.LENGTH_SHORT).show();
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
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        volleyMultipartRequest.setRetryPolicy(policy);
        Volley.newRequestQueue(PostcontentActivity.this).add(volleyMultipartRequest);

    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }



    // send video  to server api
    public void sendvideo(){

        File myFile = new File(getFilesDir().getAbsolutePath(),"VA_Smash");
        String path123 = myFile+"/Smash.mp4";
        uri=Uri.fromFile(new File(path));
       // Log.d("posturlll","::::"+uri);
        InputStream iStream = null;

        try {

            iStream = getContentResolver().openInputStream(uri);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIs.add_video_content,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                           // Log.e("postimg", String.valueOf(response));
                            try {
                                JSONObject obj = new JSONObject(new String(response.data));
                                //Log.e("postimg", String.valueOf(obj));
                                JSONObject postid=obj.getJSONObject("data");



                                String img_id = postid.getString("_id");

                                //Log.e("idddd",img_id);
                                String descp = post_description.getText().toString().trim();

                                String AddS = "{\"categoryId\":\"" + catg_id + "\",\"contentLangId\":\"" + lang_id + "\",\"_id\":\"" + img_id + "\",\"userId\":\"" + userid + "\",\"description\":\"" + descp + "\",\"visibility\":"+ post_visibilty + "}";
                                //Log.d("jsnresponse pernonal", "---" + AddS);
                                JSONObject lstrmdt;

                                try {
                                    lstrmdt = new JSONObject(AddS);
                                    //Log.d("jsnresponse....", "---" + AddS);
                                    JSONSenderVolleychecked(lstrmdt);
                                } catch (JSONException ignored) {
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            viewDialog.hideDialog();
                            dialog.cancel();
                            // spinner.setVisibility(View.INVISIBLE);
                            sendpost.setClickable(true);
                            System.out.println("Successvideo "+ error.getMessage());
                            Toast.makeText(PostcontentActivity.this,"network error"+ error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("video", new DataPart(imagename + ".mp4", inputData));
                    // System.out.println("paramss000000 "+ params);

                    return params;

                }
            };

            //adding the request to volley
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            volleyMultipartRequest.setRetryPolicy(policy);
            Volley.newRequestQueue(PostcontentActivity.this).add(volleyMultipartRequest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //loader
    public void loaddialog(){



        AlertDialog.Builder builder;

        LayoutInflater inflater = (LayoutInflater) PostcontentActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.loadpostdialog, null);
        RelativeLayout loadpopup=(RelativeLayout)layout.findViewById(R.id.loadpopup);
        animationView = (LottieAnimationView) layout.findViewById(R.id.animation_view_2);
        animationView.setAnimation("data.json");
        animationView.loop(true);
        animationView.playAnimation();
        loadpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("popuptest","done");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PostcontentActivity.this);
                builder1.setMessage("please wait untill upload content");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "wait",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });


        builder = new AlertDialog.Builder(PostcontentActivity.this);

        builder.setView(layout);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PostcontentActivity.this, CameraActivity.class);
        startActivity(intent);
        finish();
    }
}
