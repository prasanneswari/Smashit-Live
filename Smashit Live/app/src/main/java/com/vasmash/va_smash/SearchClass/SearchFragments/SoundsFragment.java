package com.vasmash.va_smash.SearchClass.SearchFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.ProfileScreen.Model_Class.Model_userfollow_unfollow;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_SoundFragment;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_hashtagsearch;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_usersearch;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Searchlatest;
import com.vasmash.va_smash.createcontent.CameraActivity;
import com.vasmash.va_smash.createcontent.Sounds.Sound_modelclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.usersound_url;


public class SoundsFragment extends Fragment {

    private TextView soundtext;
    private RecyclerView soundlist;
    private String searchadaptertext="null",searchtext="null";
    private ArrayList<Model_Searchlatest> songmodeldata;
    private RequestQueue mQueue;
    private String token;

    private int pastVisibleItems11, visibleItemCount11, totalItemCount11;
    private Boolean loading11 = true;
    private int currentPage=-1;
    private int page_no;
    private boolean loading;
    private ProgressBar p_bar;
    private Adapter_SoundFragment adapterhashtags;

    public SoundsFragment(String searchadaptertext, String searchtext) {
        // Required empty public constructor
        this.searchadaptertext=searchadaptertext;
        this.searchtext=searchtext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        soundtext= view.findViewById(R.id.searchnametxt);
        soundlist= view.findViewById(R.id.list);

        mQueue = Volley.newRequestQueue(getActivity());
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = phoneauthshard.getString("token", "null");

        soundpagination();

        return view;
    }

    private void soundpagination(){
        songmodeldata=new ArrayList<>();

        LinearLayoutManager layoutfollow = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        soundlist.setLayoutManager(layoutfollow);
        soundlist.setHasFixedSize(false);

        adapterhashtags = new Adapter_SoundFragment(songmodeldata, getActivity());
        soundlist.setAdapter(adapterhashtags);
        loading11 = false;
        loading=false;

        soundlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        if(layoutfollow.findLastCompletelyVisibleItemPosition() == songmodeldata.size()-1){
                            loading11 = true;
                            loading=true;
                            if (searchadaptertext.equals("null")) {
                                soundtext.setText(searchtext);
                                jsonsound(usersound_url+searchtext+"&skip="+songmodeldata.size());
                            }else {
                                soundtext.setText(searchadaptertext);
                                jsonsound(usersound_url+searchadaptertext+"&skip="+songmodeldata.size());
                            }
                        }
                    }
                }
            }
        });
        if (searchadaptertext.equals("null")) {
            soundtext.setText(searchtext);
            jsonsound(usersound_url+searchtext+"&skip=0");
        }else {
            soundtext.setText(searchadaptertext);
            jsonsound(usersound_url+searchadaptertext+"&skip=0");
        }

    }


    private void jsonsound(String url) {
       // Log.d("jsonParseuser", "soundurll" + url);

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                       // Log.d("Responssound", response.toString());

                        if (response.length() != 0) {
                            // Iterate the inner "data" array

                            try {

                                for (int k = 0; k < response.length(); k++) {
                                    JSONObject soundobj = response.getJSONObject(k);

                                    Model_Searchlatest songdata = new Model_Searchlatest();
                                    if (soundobj.has("album")) {
                                        songdata.setSongname(soundobj.getString("album"));
                                    }
                                    if (soundobj.has("url")) {
                                        songdata.setSongurl(soundobj.getString("url"));
                                    }
                                    if (soundobj.has("_id")) {
                                        songdata.setSongid(soundobj.getString("_id"));
                                    }
                                    boolean sel = false;
                                    songdata.setSelected(sel);
                                    if (soundobj.has("duration")) {
                                        songdata.setSongduration(soundobj.getString("duration"));
                                    }
                                    if (soundobj.has("posts")) {
                                        songdata.setSongviews(soundobj.getString("posts"));
                                    }
                                    if (soundobj.has("userId")) {
                                        JSONObject songobj = soundobj.getJSONObject("userId");
                                        songdata.setSongusername(songobj.getString("name"));
                                    }
                                    songmodeldata.add(songdata);
                                }
                                adapterhashtags.notifyDataSetChanged();
                                loading11=false;
                            } catch (Exception e) {
                                e.printStackTrace();
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
                                        //Log.d("body", "---" + body);
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
                                      //  Log.d("bodyerror", "---" + bodyerror);
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
