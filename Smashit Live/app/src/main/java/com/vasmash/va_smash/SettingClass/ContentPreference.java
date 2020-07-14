package com.vasmash.va_smash.SettingClass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import com.vasmash.va_smash.SettingClass.AdapterClass.Adapter_Contentpreference;
import com.vasmash.va_smash.SettingClass.AdapterClass.AdapterlaungContent;
import com.vasmash.va_smash.login.ModelClass.Languages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vasmash.va_smash.SettingClass.AdapterClass.Adapter_Contentpreference.addcodeL;
import static com.vasmash.va_smash.SettingClass.AdapterClass.Adapter_Contentpreference.catcodeL;
import static com.vasmash.va_smash.SettingClass.AdapterClass.AdapterlaungContent.langaddcodeL;
import static com.vasmash.va_smash.SettingClass.AdapterClass.AdapterlaungContent.langcodeL;
import static com.vasmash.va_smash.VASmashAPIS.APIs.Catageriesapi;
import static com.vasmash.va_smash.VASmashAPIS.APIs.Languagesapi;
import static com.vasmash.va_smash.VASmashAPIS.APIs.preference_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.updateprference_url;

public class ContentPreference extends AppCompatActivity {
    ListView lv,catlist;
    Button laung,cat,submitbtn;
    AdapterlaungContent language_popup_adapter;
    Adapter_Contentpreference language_popup_adapter1;

    private ArrayList<Languages> modelArrayList;

    private RequestQueue mQueue;
    String ln = Languagesapi;
    String catapi = Catageriesapi;

    boolean laungeclick=true;
    ViewDialog viewDialog;
    Dialog dialog;

    static public String token="null";
    static public List<String> selecedlaungname;
    static public List<String> laungaddjavaL;
    static public List<String> selecedlaungcode;
    static public List<String> settingscatL;
    static public List<String> settingscatnameL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_preference);
        lv =  findViewById(R.id.lang_list);
        catlist =  findViewById(R.id.catlist);
        laung =  findViewById(R.id.laung);
        cat =  findViewById(R.id.cate);
        submitbtn =  findViewById(R.id.submitbtn);

        viewDialog = new ViewDialog(this);
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(this);
        token = phoneauthshard.getString("token", "null");

        getpreferencelaung(preference_url,ln);

        laung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catlist.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
                laung.setBackgroundResource(R.drawable.homeback);
                cat.setBackgroundResource(R.color.transparent);
                getpreferencelaung(preference_url,ln);
            }
        });

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catlist.setVisibility(View.VISIBLE);
                lv.setVisibility(View.GONE);
                laung.setBackgroundResource(R.color.transparent);
                cat.setBackgroundResource(R.drawable.homeback);
                getpreferencelaung(preference_url,catapi);

            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AddS = "{\"languages\":" + selecedlaungcode + ",\"categories\":" + settingscatL + "}";
                //Log.d("jsnresponse laung", "---" + AddS);
                JSONObject update;
                try {
                    update = new JSONObject(AddS);
                    //Log.d("register", "---" + AddS);
                    viewDialog.showDialog();
                    updateregst(update);
                } catch (JSONException ignored) {

                }
            }
        });
    }

    public void langapi(String ul) {
        laungaddjavaL=new ArrayList<>();
        laungaddjavaL.clear();
        viewDialog.showDialog();
        mQueue = Volley.newRequestQueue(ContentPreference.this.getApplicationContext());
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, ul, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        //Log.d("Response", response.toString());
                        viewDialog.hideDialog();

                        modelArrayList = new ArrayList<>();
                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    Languages langu = new Languages();
                                    String _id = employee.getString("_id");
                                    String name = employee.getString("name");

                                    langu.setLang_name(_id);
                                    langu.setLang_code(name);
                                    modelArrayList.add(langu);
                                    laungaddjavaL.add(name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (laungeclick) {
                                lv.setVisibility(View.VISIBLE);
                                catlist.setVisibility(View.GONE);
                               // Log.d("laungnamearray",":::"+laungaddjavaL);
                                language_popup_adapter = new AdapterlaungContent(ContentPreference.this, modelArrayList);
                                lv.setAdapter(language_popup_adapter);

/*
                                language_popup_adapter = new AdapterLaungSample(ContentPreference.this, laungaddjavaL ,selecedlaungname);
                                lv.setAdapter(language_popup_adapter);
*/
                                laungeclick=false;
                            }else {
                                lv.setVisibility(View.GONE);
                                catlist.setVisibility(View.VISIBLE);
                                language_popup_adapter1 = new Adapter_Contentpreference(ContentPreference.this, modelArrayList);
                                catlist.setAdapter(language_popup_adapter1);
                                laungeclick=true;
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
                                            viewDialog.hideDialog();
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

    public void updateregst(JSONObject update) {
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, updateprference_url, update,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("JSONSenderprferen", "---" + response);
                        viewDialog.hideDialog();
                        try {
                            String message=response.getString("message");
                            //if (message.equals("User Preferences Updated")){
                                Toast.makeText(ContentPreference.this, message, Toast.LENGTH_SHORT).show();
/*
                            language_popup_adapter1.notifyDataSetChanged();
                            language_popup_adapter.notifyDataSetChanged();
*/
                            //}
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            try {
                                body = new String(error.networkResponse.data, "UTF-8");
                               // Log.d("body", "---" + body);
                                JSONObject obj1 = new JSONObject(body);
                                if (obj1.has("errors")) {
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj1.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    //dialogbox(message);
                                    popup(message);
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;

                        case 422:
                            try {
                                String body1 = new String(error.networkResponse.data, "UTF-8");
                               // Log.d("body", "---" + body1);
                                JSONObject obj = new JSONObject(body1);
                                if (obj.has("errors")) {
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    // dialogbox(message);
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
        }) {
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


    public void getpreferencelaung(String ul,String laungurl) {
        viewDialog.showDialog();

        selecedlaungname=new ArrayList<>();
        selecedlaungcode=new ArrayList<>();
        settingscatL=new ArrayList<>();
        settingscatnameL=new ArrayList<>();
        selecedlaungname.clear();
        selecedlaungcode.clear();
        settingscatL.clear();
        settingscatnameL.clear();

        mQueue = Volley.newRequestQueue(getApplicationContext());
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ul, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        viewDialog.hideDialog();

                        // display response
                        langapi(laungurl);
                        //Log.d("preference respoooo", response.toString());
                        if (response.length() != 0) {
                            try {

                                JSONArray object = response.getJSONArray("contentLanguages");
                                for (int i = 0; i < object.length(); i++) {
                                    JSONObject employee = object.getJSONObject(i);
                                    JSONObject object1 = employee.getJSONObject("languageId");
                                    String languageId = object1.getString("_id");
                                    String name = object1.getString("name");
                                    //Log.d("nameLLLL", name);
                                    selecedlaungname.add(name);
                                    selecedlaungcode.add(languageId);
                                }
                                JSONArray categories = response.getJSONArray("categories");
                                    if (categories.length() != 0) {
                                        for (int i = 0; i < categories.length(); i++) {
                                            JSONObject employee = categories.getJSONObject(i);
                                            JSONObject object1 = employee.getJSONObject("categoryId");
                                           // Log.d("object1",":::"+object1);

                                            String categoryId = object1.getString("_id");
                                                String name = object1.getString("name");
                                                settingscatL.add(categoryId);
                                                settingscatnameL.add(name);
                                               // Log.d("settingssss",":::"+name);
                                        }
                                }
                                } catch (JSONException e) {
                                e.printStackTrace();
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
                                            viewDialog.hideDialog();
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
                })
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
                headers.put("Content-Type","application/json");
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
                viewDialog.hideDialog();
            }
        });

    }
    //this is the response popup
    public void popup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = ContentPreference.this;
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


    public void finishActivity(View v){
        finish();
    }

}
