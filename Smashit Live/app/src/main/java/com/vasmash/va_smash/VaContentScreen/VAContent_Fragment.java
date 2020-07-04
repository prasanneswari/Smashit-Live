package com.vasmash.va_smash.VaContentScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.VaContentScreen.Adapter.Adapter_Content;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Model_Content;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.vasmash.va_smash.VASmashAPIS.APIs.vastoredata_url;


public class VAContent_Fragment extends AppCompatActivity {


    RecyclerView topicslst,contentlst;
    SwipeRefreshLayout mSwipeRefreshLayout;

    Button uploadimg,adddata;
    // TODO: Rename and change types and number of parameters
    public static VAContent_Fragment newInstance() {
        VAContent_Fragment fragment = new VAContent_Fragment();

        return fragment;
    }


    //content lst data

    Adapter_Content contentadapter;
    ArrayList<Model_Content> contentmodel;


    private ArrayList<String> createdateL ;
    private ArrayList<String> imageL ;
    private ArrayList<String> nameL ;
    private ArrayList<String> tagL ;
    private ArrayList<String> contentcatogryL ;
    ArrayList<Tags> tags;


    private static String token;
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_vacontent_);

        //topicslst=view.findViewById(R.id.topicslst);
        contentlst=findViewById(R.id.contentlst);
        adddata=findViewById(R.id.adddata);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);


        mQueue = Volley.newRequestQueue(getApplicationContext());

        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(this);
        token = phoneauthshard.getString("token", "null");


        /*if (token.equals("null")){
            Intent intent = new Intent(getActivity(), LoginRegistrationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else {*/
            jsongetvastore();
      //  }


        adddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VAContent_Fragment.this, AddContent.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }

    private void jsongetvastore() {
        Log.d("jsonParseuser", "store data" + vastoredata_url);

        createdateL = new ArrayList<>();
        imageL = new ArrayList<>();
        nameL = new ArrayList<>();
        tagL=new ArrayList<>();
        contentcatogryL=new ArrayList<>();
        tags=new ArrayList<>();


        createdateL.clear();
        imageL.clear();
        nameL.clear();
        tagL.clear();
        contentcatogryL.clear();

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, vastoredata_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Responsestoredata0000", response.toString());


                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            for (int j = 0; j < response.length() ; j++ ) {
                                Log.d("lengtharayyy",  ":::"+j);


                                JSONObject jItem = null;
                                try {
                                    jItem = response.getJSONObject(j);
                                    //Log.d("response2222","jItem:::"+jItem);
                                    String createdDate = jItem.getString("createdDate");
                                    String expiryDate = jItem.getString("expiryDate");
                                    String _id = jItem.getString("_id");
                                    String userId = jItem.getString("userId");
                                    String image = jItem.getString("image");


                                    if (jItem.has("name")){

                                        String name=jItem.getString("name");
                                        nameL.add(name);
                                    }else {
                                        String name="....";
                                        nameL.add(name);
                                    }

                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                                    Date localdate = dateFormat.parse(createdDate);
                                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
                                    String dateStr = formatter.format(localdate);
                                    Log.d("dateStr@@@@", dateStr);

                                    JSONArray tagsarray = jItem.getJSONArray("tags");
                                    Tags ts=new Tags();

                                    if (tagsarray.length()!=0) {
                                        for (int k = 0; k < tagsarray.length(); k++) {
                                            JSONObject tagarray = tagsarray.getJSONObject(k);

                                            String tagid = tagarray.getString("_id");
                                            if (tagarray.has("tag")) {
                                                String tag = tagarray.getString("tag");
                                                Log.d("tag:::::",""+tag);

                                                Log.d("tagarry111",""+tagL);
                                                ts.setId(tagarray.getString("_id"));
                                                ts.setName(tagarray.getString("tag"));

                                                tags.add(ts);


                                            }else {
                                                String tag = " ";
                                                ts.setId("");
                                                ts.setName("");

                                                tags.add(ts);

                                                tagL.add(tag);
                                            }
                                        }
                                    }else {
                                        String tag = "..";
                                        ts.setId("");
                                        ts.setName("");
                                        tags.add(ts);

                                        tagL.add(tag);
                                    }


                                    if (jItem.has("categoryId")){
                                        JSONObject categoryobj=jItem.getJSONObject("categoryId");
                                        String cateid=categoryobj.getString("_id");
                                        if (categoryobj.has("name")) {
                                            String catename = categoryobj.getString("name");
                                            Log.d("catnameee","::::"+catename);
                                            contentcatogryL.add(catename);
                                        }else {
                                            String catename = "";
                                            contentcatogryL.add(catename);

                                        }
                                    }else {
                                        String catename = "";
                                        contentcatogryL.add(catename);
                                    }

                                            createdateL.add(dateStr);;
                                           imageL.add(image);

                                      //content list
                                    contentlst.setHasFixedSize(true);
                                    // use a linear layout manager
                                    RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(VAContent_Fragment.this,LinearLayoutManager.VERTICAL, false);
                                    contentlst.setLayoutManager(layoutManager1);
                                    contentmodel = new ArrayList<>();

                                    for (int i1 = 0; i1 < nameL.size(); i1++) {
                                        Model_Content mainMode = new Model_Content(nameL.get(i1), createdateL.get(i1),contentcatogryL.get(i1)/*, tagL.get(i1)*/, imageL.get(i1));
                                        contentmodel.add(mainMode);
                                    }
                                    contentadapter = new Adapter_Content(contentmodel,tags, VAContent_Fragment.this);
                                    contentlst.setAdapter(contentadapter);
                                    contentadapter.notifyDataSetChanged();

                                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                        @Override
                                        public void onRefresh() {
                                            shuffle();
                                            mSwipeRefreshLayout.setRefreshing(false);
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

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
                                            Toast.makeText(VAContent_Fragment.this, message, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 404:
                                    try {
                                        String bodyerror = new String(error.networkResponse.data,"UTF-8");
                                        Log.d("bodyerror", "---" + bodyerror);
                                        JSONObject obj = new JSONObject(bodyerror);
                                        if (obj.has("errors")) {
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            //Toast.makeText(GACalculator.this, message, Toast.LENGTH_SHORT).show();

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
                // headers.put("Authorization",token);

                return headers;
            }
        };

        // add it to the RequestQueue
        mQueue.add(getRequest);
    }

    public void shuffle(){
        contentadapter = new Adapter_Content(contentmodel, tags, VAContent_Fragment.this);
        contentlst.setAdapter(contentadapter);
    }


}
