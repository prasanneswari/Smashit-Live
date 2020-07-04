package com.vasmash.va_smash.SearchClass.SearchFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.ProfileScreen.Adapter.Adapter_following;
import com.vasmash.va_smash.ProfileScreen.Model_Class.Model_userfollow_unfollow;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_SoundFragment;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_hashtagsearch;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Searchlatest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.hashtagsearch_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.userfollowing_url;


public class HashtagsFragment extends Fragment {

    RecyclerView hashtagslist;
    RelativeLayout hashagslay;
    TextView hashtext;
    private String searchadaptertext,searchtext;
    private ArrayList<Model_Searchlatest> userfollowingL;
    private Adapter_hashtagsearch adapterhashtags;
    private RequestQueue mQueue;
    private String token;


    private int pastVisibleItems11, visibleItemCount11, totalItemCount11;
    private Boolean loading11 = true;
    private int currentPage=-1;
    private int page_no;
    private boolean loading;
    private ProgressBar p_bar;

    public HashtagsFragment(String searchadaptertext, String searchtext) {
        this.searchadaptertext=searchadaptertext;
        this.searchtext=searchtext;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmentusersearch, container, false);
        hashtext= view.findViewById(R.id.searchnametxt);
        hashtagslist= view.findViewById(R.id.list);
        hashagslay=view.findViewById(R.id.recyclarlay);

        mQueue = Volley.newRequestQueue(getActivity());
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = phoneauthshard.getString("token", "null");


       // hashtagspagination();

        if (searchadaptertext.equals("null")) {
            hashtext.setText("#"+" "+searchtext);
            jsonhashtags(hashtagsearch_url+searchtext);
        }else {
            hashtext.setText("#"+" "+searchadaptertext);
            jsonhashtags(hashtagsearch_url+searchadaptertext);
        }

        return view;
    }

    private void hashtagspagination(){
        userfollowingL=new ArrayList<>();


        LinearLayoutManager layoutfollow = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        hashtagslist.setLayoutManager(layoutfollow);
        hashtagslist.setHasFixedSize(false);
        adapterhashtags = new Adapter_hashtagsearch(userfollowingL, getActivity());
        hashtagslist.setAdapter(adapterhashtags);

        loading11 = false;
        loading=false;

        hashtagslist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                page_no=scrollOffset / height;

                if(page_no!=currentPage ){
                    currentPage=page_no;
                }
                if (dy > 0)
                {
                    visibleItemCount11 = layoutfollow.getChildCount();
                    totalItemCount11 = layoutfollow.getItemCount();
                    pastVisibleItems11 = layoutfollow.findFirstVisibleItemPosition();

                    if (!loading11) {
                        if(layoutfollow.findLastCompletelyVisibleItemPosition() == userfollowingL.size()-1){
                            loading11 = true;
                            loading=true;
                            if (searchadaptertext.equals("null")) {
                                hashtext.setText("#"+" "+searchtext);
                                jsonhashtags(hashtagsearch_url+searchtext+"&limit=10&skip="+userfollowingL.size());
                            }else {
                                hashtext.setText("#"+" "+searchadaptertext);
                                jsonhashtags(hashtagsearch_url+searchadaptertext+"&limit=10&skip="+userfollowingL.size());
                            }
                        }
                    }
                }
            }
        });
        if (searchadaptertext.equals("null")) {
            hashtext.setText("#"+" "+searchtext);
            jsonhashtags(hashtagsearch_url+searchtext+"&limit=10&skip=0");
        }else {
            hashtext.setText("#"+" "+searchadaptertext);
            jsonhashtags(hashtagsearch_url+searchadaptertext+"&limit=10&skip=0");
        }

    }

    private void jsonhashtags(String url) {
        Log.d("jsonParseuser", "hashtags" + url);
        userfollowingL=new ArrayList<>();

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Responssound", response.toString());

                        if (response.length() != 0) {
                            // Iterate the inner "data" array

                            try {
                                for (int k = 0; k < response.length(); k++) {
                                    JSONObject hashtagobj = response.getJSONObject(k);
                                    Model_Searchlatest userfollow_unfollow = new Model_Searchlatest();
                                    userfollow_unfollow.setHashtagsname(hashtagobj.getString("name"));
                                    userfollow_unfollow.setHashtagsviews("10");
                                    userfollowingL.add(userfollow_unfollow);
                                }
                                hashagslay.setVisibility(View.VISIBLE);
                                LinearLayoutManager layoutfollow = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                hashtagslist.setLayoutManager(layoutfollow);
                                hashtagslist.setHasFixedSize(false);
                                adapterhashtags = new Adapter_hashtagsearch(userfollowingL, getActivity());
                                hashtagslist.setAdapter(adapterhashtags);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            hashagslay.setVisibility(View.GONE);
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
                                            // Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
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
                //headers.put("Content-Type","application/json");
                headers.put("Authorization",token);
                // Log.d("headresspro",":::::"+token);

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
