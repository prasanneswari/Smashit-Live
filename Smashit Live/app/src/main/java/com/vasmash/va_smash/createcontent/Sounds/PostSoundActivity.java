package com.vasmash.va_smash.createcontent.Sounds;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.VASmashAPIS.APIs;
import com.vasmash.va_smash.VaContentScreen.VolleyMultipartRequest;
import com.vasmash.va_smash.createcontent.CameraActivity;
import com.vasmash.va_smash.createcontent.Post_lang_Adapter;
import com.vasmash.va_smash.createcontent.Post_lang_cate_Adapter;
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

public class PostSoundActivity extends AppCompatActivity {
    EditText name,artist,album;
    ListView sound_cate,sound_lang;
    TextView sound_category,sound_langues;
    Post_lang_cate_Adapter post_lang_cate_adapter;
    Post_lang_Adapter post_lang_adapter;
    ArrayList<Languages> items;
    ArrayList<Languages> categ;
    String catg_id="0";
    String lang_id="0";
    private RequestQueue mQueue;
    public static String postsound;

    int preSelectedIndex = -1;
    int preSelectedIndex1 = -1;
    String path;
    Uri uri;
    Button sendsound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sound);

        name=(EditText)findViewById(R.id.sound_name);
        album=(EditText)findViewById(R.id.sound_album);
        artist=(EditText)findViewById(R.id.sound_artist);
        sound_langues=(TextView)findViewById(R.id.sound_lang) ;
        sound_category=(TextView)findViewById(R.id.sound_cate) ;
        sendsound=(Button)findViewById(R.id.sendsound);


        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(PostSoundActivity.this);
        postsound = phoneauthshard.getString("token", "null");

        path=getIntent().getStringExtra("path");
        Log.e("path",path);

        sound_cate=(ListView) findViewById(R.id.sound_listview_cate);
        sound_lang=(ListView) findViewById(R.id.sound_listview_langes);
        sound_cate.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        sound_lang.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



        sound_langues.setBackground(getResources().getDrawable(R.drawable.homeback));
        langapi();
        cateapi();



        sound_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_langues.setBackground(getResources().getDrawable(R.drawable.empty));
                sound_category.setBackground(getResources().getDrawable(R.drawable.homeback));
                sound_lang.setVisibility(View.INVISIBLE);
                sound_cate.setVisibility(View.VISIBLE);

            }
        });
        sound_langues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_category.setBackground(getResources().getDrawable(R.drawable.empty));
                sound_langues.setBackground(getResources().getDrawable(R.drawable.homeback));
                sound_cate.setVisibility(View.INVISIBLE);
                sound_lang.setVisibility(View.VISIBLE);

            }
        });



        sound_cate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                items.get(position);
                String value=items.get(position).getLang_name().toString();
                String valueid=items.get(position).getLang_code().toString();

                catg_id=value;
                Log.e("wwwwww",catg_id+" "+valueid);
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
        sound_lang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categ.get(position);
                String value=categ.get(position).getLang_name().toString();
                String valueid=categ.get(position).getLang_code().toString();


                lang_id=value;
                Log.e("wwwwww",lang_id+" "+valueid);
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

        sendsound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendaudio();
            }
        });

    }


    public void cateapi() {
        // spinner2.setVisibility(View.VISIBLE);

//        viewDialog.showDialog();

        mQueue = Volley.newRequestQueue(PostSoundActivity.this.getApplicationContext());

        // prepare the Request
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Soundapicateget, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());
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





                                    Log.d("Response", "createddateL:::" + items);
                                    Log.d("Response", "createddateL:::" + _id + name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            post_lang_cate_adapter= new Post_lang_cate_Adapter(PostSoundActivity.this, items);
                            sound_cate.setAdapter(post_lang_cate_adapter);


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


    public void langapi() {
        //spinner2.setVisibility(View.VISIBLE);
//        viewDialog.showDialog();

        mQueue = Volley.newRequestQueue(PostSoundActivity.this.getApplicationContext());

        // prepare the Request
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Languagesapi, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());

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





                                    Log.d("Response", "createddateL:::" + categ);
                                    Log.d("Response", "createddateL:::" + _id + name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            post_lang_adapter= new Post_lang_Adapter(PostSoundActivity.this, categ);
                            sound_lang.setAdapter(post_lang_adapter);
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
    public void sendaudio(){


        uri= Uri.fromFile(new File(path));
        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(uri);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIs.Audiofile,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.e("postaudio1", String.valueOf(response));
                            try {
                                JSONObject obj = new JSONObject(new String(response.data));
                                Log.e("postaudio2", String.valueOf(obj));
//                                JSONObject postid=obj.getJSONObject("data");



                                String img_id = obj.getString("id");
                                String nametxt=name.getText().toString();
                                String albumtxt=album.getText().toString();
                                String artisttxt=artist.getText().toString();

                                Log.e("postaudio3",img_id);


                                String AddS = "{\"categoryId\":\"" + catg_id + "\",\"contentLangId\":\"" + lang_id + "\",\"_id\":\"" + img_id + "\",\"name\":\"" + nametxt + "\",\"artist\":\"" + artisttxt + "\",\"album\":"+ albumtxt + "}";
                                Log.d("jsnresponse pernonal", "---" + AddS);
                                JSONObject lstrmdt;

                                try {
                                    lstrmdt = new JSONObject(AddS);
                                    Log.d("postaudio4....", "---" + AddS);
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
                            // spinner.setVisibility(View.INVISIBLE);
                            System.out.println("Successaudio "+ error.getMessage());
                            Toast.makeText(PostSoundActivity.this,"network error"+ error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("audio", new DataPart(imagename + ".mp3", inputData));
                    // System.out.println("paramss000000 "+ params);

                    return params;

                }
            };

            //adding the request to volley
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            volleyMultipartRequest.setRetryPolicy(policy);
            Volley.newRequestQueue(PostSoundActivity.this).add(volleyMultipartRequest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void JSONSenderVolleychecked(JSONObject lstrmdt) {
        // Log.d("---reqotpurl-----", "---" + login_url);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, APIs.Audiodata,lstrmdt,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("postaudiodata1", "---" + response);

                        Intent i=new Intent(PostSoundActivity.this, CameraActivity.class);
                        startActivity(i);
                        try {
                            if (response.length()!=0) {
                                String message = response.getString("message");
//                                if (message.equals("Post Added successfully")) {
////                                    viewDialog.hideDialog();
//
//                                    //  Toast.makeText(PostcontentActivity.this, message, Toast.LENGTH_SHORT).show();
//
//
//                                }
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
                headers.put("Authorization",postsound);
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

            }
        });

    }
}
