package com.vasmash.va_smash.SearchClass.SearchFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_Searchlistdata;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_TradingTabs;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_usersearch;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Searchlist;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Trading;
import com.vasmash.va_smash.SearchClass.SearchData;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vasmash.va_smash.ProfileScreen.ProfileActivity.createvideosclick;
import static com.vasmash.va_smash.SearchClass.SearchData.both;
import static com.vasmash.va_smash.VASmashAPIS.APIs.search_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.usersearch_url;

public class VideosFragment extends Fragment {

    TextView individualtxt;
    RecyclerView individullist;
    private RequestQueue mQueue;
    String token;

    Adapter_TradingTabs searchindividualAdapter;
    ArrayList<Model_Trading> searchindividualmodel;

    public static ArrayList<String> fileL;
    ArrayList<Model_Searchlist> model;

    String searchadaptertext="null",searchtext="null";

    private int pastVisibleItems11, visibleItemCount11, totalItemCount11;
    private Boolean loading11 = true;
    private int currentPage=-1;
    private int page_no;
    private boolean loading;
    ProgressBar p_bar;


    public VideosFragment(String searchadaptertext, String searchtext) {
        Log.d("searchtext",":::"+searchadaptertext);
        this.searchadaptertext=searchadaptertext;
        this.searchtext=searchtext;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        individualtxt= view.findViewById(R.id.searchnametxt);
        individullist= view.findViewById(R.id.list);

        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = phoneauthshard.getString("token", "null");
        mQueue = Volley.newRequestQueue(getActivity());

        if (searchadaptertext.equals("null")) {
            individualtxt.setText(searchtext);
            searchapi(search_url + searchtext);
        }else {
            individualtxt.setText(searchadaptertext);
            searchapi(search_url + searchadaptertext);
        }
        return view;
    }


    private void searchapi(String homeapi_url){
        Log.d("search api::::", homeapi_url);
        model = new ArrayList<>();
        searchindividualmodel = new ArrayList<>();
        fileL=new ArrayList<>();

        model.clear();
        searchindividualmodel.clear();
        fileL.clear();

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, homeapi_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response search", response.toString());
                        createvideosclick="false";

                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    if (employee.has("_id")){
                                        Model_Trading searchhm=new Model_Trading();

                                        if (employee.has("_id")) {
                                            searchhm.setId(employee.getString("_id"));
                                        } else {
                                            searchhm.setId("");
                                        }
                                        if (employee.has("file")) {
                                            searchhm.setImage(employee.getString("file"));
                                            fileL.add(employee.getString("file"));
                                        } else {
                                            searchhm.setImage("");
                                            fileL.add("");
                                        }
                                        if (employee.has("likes")) {
                                            searchhm.setCount(employee.getString("likes"));
                                        } else {
                                            searchhm.setCount("");
                                        }
                                        if (employee.has("userLikes")) {
                                            searchhm.setLikescondition(employee.getString("userLikes"));
                                        } else {
                                            searchhm.setLikescondition("");
                                        }
                                        if (employee.has("type")) {
                                            searchhm.setType(employee.getString("type"));
                                        } else {
                                            searchhm.setType("");

                                        }
                                        if (employee.has("comments")) {
                                            searchhm.setComment(employee.getString("comments"));
                                        } else {
                                            searchhm.setComment("");
                                        }
                                        if (employee.has("shareCount")) {
                                            String shareCount = employee.getString("shareCount");
                                            searchhm.setSharecount(shareCount);
                                        } else {
                                            searchhm.setComment("");
                                        }
                                        if (employee.has("description")) {
                                            String description = employee.getString("description");
                                            searchhm.setDescription(description);

                                        }else {
                                            searchhm.setDescription("");

                                        }

                                        if (employee.has("thumb")) {
                                            searchhm.setGifimg(employee.getString("thumb"));
                                        }else {
                                            searchhm.setGifimg(" ");
                                        }

                                        if (employee.has("userId")) {
                                            JSONObject nameobj=employee.getJSONObject("userId");
                                            Log.d("nameobj","::::"+nameobj);
                                            String username = nameobj.getString("name");
                                            String homeprofilePic = nameobj.getString("profilePic");
                                            String userid = nameobj.getString("_id");

                                            Log.d("userid11111","::::"+userid);
                                            searchhm.setUsername(username);
                                            searchhm.setProfilepic(homeprofilePic);
                                            searchhm.setUserid(userid);

                                        }else {
                                            String username = " ";
                                            searchhm.setUsername(username);
                                            searchhm.setProfilepic("null");
                                            searchhm.setUserid("null");
                                        }
                                        if (employee.has("soundId")) {
                                            JSONArray sounds = employee.getJSONArray("soundId");
                                            Log.d("soundsss","::::"+sounds);
                                            for (int k1 = 0; k1 < sounds.length(); k1++) {
                                                JSONObject soundsobj = sounds.getJSONObject(k1);
                                                if(soundsobj.has("_id")) {
                                                    String soundid = soundsobj.getString("_id");
                                                    searchhm.setSoundid(soundid);
                                                }
                                                if (soundsobj.has("name")) {
                                                    String soundname = soundsobj.getString("name");
                                                    searchhm.setSoundname(soundname);
                                                }
                                                if (soundsobj.has("url")) {
                                                    String soundurl = soundsobj.getString("url");
                                                    searchhm.setSoundurl(soundurl);
                                                }
                                                if (soundsobj.has("postId")) {
                                                    String soundpostId = soundsobj.getString("postId");
                                                    searchhm.setSoundpostid(soundpostId);
                                                }
                                                if (soundsobj.has("userId")) {
                                                    String sounduserId = soundsobj.getString("userId");
                                                    searchhm.setSounduserid(sounduserId);
                                                }
                                            }
                                        }else {
                                            searchhm.setSoundname("");
                                            searchhm.setSoundurl("");
                                            searchhm.setSoundpostid("");
                                            searchhm.setSounduserid("");
                                        }
                                        searchindividualmodel.add(searchhm);
                                        Log.d("enteringggg","::::"+searchindividualmodel);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            //commentimg
                            individullist.setHasFixedSize(true);
                            // use a linear layout manager
                            // RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());

                            individullist.setLayoutManager(new GridLayoutManager(getActivity(),3));

                            searchindividualAdapter = new Adapter_TradingTabs(getActivity(), searchindividualmodel,fileL);
                            individullist.setAdapter(searchindividualAdapter);



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

                headers.put("Authorization",token);

                System.out.println("headddddd"+headers);

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
            }
        });

    }

}
