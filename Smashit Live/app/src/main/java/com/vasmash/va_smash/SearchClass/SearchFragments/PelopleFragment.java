package com.vasmash.va_smash.SearchClass.SearchFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_TradingTabs;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_hashtagsearch;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_usersearch;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Searchlatest;
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
import static com.vasmash.va_smash.VASmashAPIS.APIs.search_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.searchall_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.userfollowing_url;


public class PelopleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private LinearLayout userviewmore,hashtagviewmore,videoviewmore,soundviewmore,usernamelay,hashtaglay,videoslay,soundslay;
    private RecyclerView userlist,hashtaglist,videolist,soundlist;

    private String searchadaptertext="null",searchtext="null";


    private RequestQueue mQueue;
    private String token;

    private Adapter_TradingTabs searchindividualAdapter;
    private ArrayList<Model_Trading> searchindividualmodel;

    public static ArrayList<String> fileL;
    private ArrayList<Model_Searchlist> model;
    private ArrayList<Model_userfollow_unfollow> userfollowingL;
    private ArrayList<Model_Searchlatest> songmodeldata;
    private ArrayList<Model_Searchlatest> userhashtagL;


    public PelopleFragment(String searchadaptertext, String searchtext) {
        this.searchadaptertext=searchadaptertext;
        this.searchtext=searchtext;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pelople, container, false);

        userviewmore=view.findViewById(R.id.userviewmore);
        hashtagviewmore=view.findViewById(R.id.hashtagsviewmore);
        videoviewmore=view.findViewById(R.id.videoviewmore);
        soundviewmore=view.findViewById(R.id.soundviewmore);
        hashtaglist=view.findViewById(R.id.hashtagslist);
        userlist=view.findViewById(R.id.userlist);
        videolist=view.findViewById(R.id.videolist);
        soundlist=view.findViewById(R.id.soundlist);

        usernamelay=view.findViewById(R.id.usernamelay);
        hashtaglay=view.findViewById(R.id.hashaglay);
        videoslay=view.findViewById(R.id.videoslay);
        soundslay=view.findViewById(R.id.soundlay);


        mQueue = Volley.newRequestQueue(getActivity());
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = phoneauthshard.getString("token", "null");

        if (searchadaptertext.equals("null")) {
            searchapi(searchall_url + searchtext);
        }else {
            searchapi(searchall_url + searchadaptertext);
        }
        userviewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchData.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("key","userview");
                startActivity(intent);
            }
        });
        hashtagviewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchData.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("key","hashview");
                startActivity(intent);

            }
        });
        videoviewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchData.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("key","videoview");
                startActivity(intent);

            }
        });
        soundviewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchData.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("key","soundview");
                startActivity(intent);

            }
        });

        hashtagmethod();
        return view;
    }
    public void hashtagmethod(){
        ArrayList<Model_Searchlatest> userfollowingL;

        userfollowingL=new ArrayList<>();
        Model_Searchlatest userfollow_unfollow = new Model_Searchlatest();
        userfollow_unfollow.setHashtagsname("prasanna");
        userfollow_unfollow.setHashtagsname("chakravuyha");
        userfollowingL.add(userfollow_unfollow);
        LinearLayoutManager layoutfollow = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        hashtaglist.setLayoutManager(layoutfollow);
        hashtaglist.setHasFixedSize(false);

        Adapter_hashtagsearch adapterhashtags = new Adapter_hashtagsearch(userfollowingL, getActivity());
        hashtaglist.setAdapter(adapterhashtags);

    }


    public void searchapi(String homeapi_url){

        //Log.d("search api::::", homeapi_url);
        model = new ArrayList<>();
        searchindividualmodel = new ArrayList<>();
        userfollowingL = new ArrayList<>();
        songmodeldata=new ArrayList<>();
        userhashtagL=new ArrayList<>();
        fileL=new ArrayList<>();
        fileL.clear();


        model.clear();
        searchindividualmodel.clear();

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, homeapi_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                      //  Log.d("Response searchall", response.toString());

                        if (response.length() != 0) {
                            try {
                                if (response.has("posts")){
                                JSONArray searchallobj=response.getJSONArray("posts");
                                if (searchallobj.length()!=0) {
                                    videoslay.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < searchallobj.length(); i++) {
                                        JSONObject employee = searchallobj.getJSONObject(i);

                                        Model_Trading searchhm = new Model_Trading();
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

                                        } else {
                                            searchhm.setDescription("");

                                        }

                                        if (employee.has("thumb")) {
                                            searchhm.setGifimg(employee.getString("thumb"));
                                        } else {
                                            searchhm.setGifimg(" ");
                                        }

                                 if (employee.has("userId")) {
                                     JSONObject nameobj = employee.getJSONObject("userId");
                                    // Log.d("nameobj", "::::" + nameobj);
                                     String username = nameobj.getString("name");
                                     String homeprofilePic = nameobj.getString("profilePic");
                                     String userid = nameobj.getString("_id");

                                     //Log.d("userid11111", "::::" + userid);
                                     searchhm.setUsername(username);
                                     searchhm.setProfilepic(homeprofilePic);
                                     searchhm.setUserid(userid);

                                 } else {
                                     String username = " ";
                                     searchhm.setUsername(username);
                                     searchhm.setProfilepic("null");
                                     searchhm.setUserid("null");
                                 }

                                        if (employee.has("soundId")) {
                                            JSONArray sounds = employee.getJSONArray("soundId");
                                           // Log.d("soundsss","::::"+sounds);
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
                                        } else {
                                            searchhm.setSoundname("");
                                            searchhm.setSoundurl("");
                                            searchhm.setSoundpostid("");
                                            searchhm.setSounduserid("");
                                        }

                                        searchindividualmodel.add(searchhm);
                                    }
                                    //commentimg
                                    videolist.setHasFixedSize(true);
                                    // use a linear layout manager
                                    // RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
                                    videolist.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                                    searchindividualAdapter = new Adapter_TradingTabs(getActivity(), searchindividualmodel, fileL);
                                    videolist.setAdapter(searchindividualAdapter);
                                }
                                else {
                                    videoslay.setVisibility(View.GONE);
                                    }

                                }
                                if (response.has("users")){
                                    JSONArray usersobj=response.getJSONArray("users");
                                    if (usersobj.length()!=0) {
                                        usernamelay.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < usersobj.length(); i++) {
                                            JSONObject usersdata = usersobj.getJSONObject(i);

                                            Model_userfollow_unfollow userfollow_unfollow = new Model_userfollow_unfollow();

                                            if (usersdata.has("profilePic")) {
                                                userfollow_unfollow.setUserpic(usersdata.getString("profilePic"));
                                            }
                                            if (usersdata.has("name")) {
                                                userfollow_unfollow.setName(usersdata.getString("name"));
                                            }
                                            if (usersdata.has("_id")) {
                                                userfollow_unfollow.setUsersearchid(usersdata.getString("_id"));
                                            }
                                            if (usersdata.has("isFollowing")) {
                                                userfollow_unfollow.setFollowinguser(usersdata.getString("isFollowing"));
                                            }

                                            userfollowingL.add(userfollow_unfollow);
                                        }
                                        LinearLayoutManager layoutfollow = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        userlist.setLayoutManager(layoutfollow);
                                        userlist.setHasFixedSize(false);

                                        Adapter_usersearch adapterfollowing = new Adapter_usersearch(userfollowingL, getActivity());
                                        userlist.setAdapter(adapterfollowing);

                                    }else {
                                        usernamelay.setVisibility(View.GONE);
                                    }
                                }
                                if (response.has("sounds")){
                                    JSONArray soundsobj=response.getJSONArray("sounds");
                                    if (soundsobj.length()!=0) {
                                        soundslay.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < soundsobj.length(); i++) {
                                            JSONObject sounddata = soundsobj.getJSONObject(i);
                                            Model_Searchlatest songdata = new Model_Searchlatest();
                                            if (sounddata.has("album")) {
                                                songdata.setSongname(sounddata.getString("album"));
                                            }
                                            if (sounddata.has("name")) {
                                                songdata.setSongusername(sounddata.getString("name"));
                                            }
                                            if (sounddata.has("url")) {
                                                songdata.setSongurl(sounddata.getString("url"));
                                            }
                                            if (sounddata.has("_id")) {
                                                songdata.setSongid(sounddata.getString("_id"));
                                            }
                                            if (sounddata.has("posts")) {
                                                songdata.setSongviews(sounddata.getString("posts"));
                                            }
                                            boolean sel = false;
                                            songdata.setSelected(sel);
                                            if (sounddata.has("duration")) {
                                                songdata.setSongduration(sounddata.getString("duration"));
                                            }
                                            if (sounddata.has("userId")) {
                                                JSONObject songobj = sounddata.getJSONObject("userId");
                                                songdata.setSongusername(songobj.getString("name"));

                                            }

                                            songmodeldata.add(songdata);
                                        }
                                        LinearLayoutManager layoutfollow = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        soundlist.setLayoutManager(layoutfollow);
                                        soundlist.setHasFixedSize(false);

                                        Adapter_SoundFragment adapterhashtags = new Adapter_SoundFragment(songmodeldata, getActivity());
                                        soundlist.setAdapter(adapterhashtags);
                                    }else {
                                        soundslay.setVisibility(View.GONE);
                                    }
                                }
                                if (response.has("searchTerms")){
                                    JSONArray hashtagsobj=response.getJSONArray("searchTerms");
                                    if (hashtagsobj.length()!=0) {
                                        hashtaglay.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < hashtagsobj.length(); i++) {
                                            JSONObject hashtagdata = hashtagsobj.getJSONObject(i);
                                            Model_Searchlatest userhashags = new Model_Searchlatest();
                                            if (hashtagdata.has("name")) {
                                                userhashags.setHashtagsname(hashtagdata.getString("name"));
                                            }
                                            if (hashtagdata.has("count")) {
                                                userhashags.setHashtagsviews(hashtagdata.getString("count"));
                                            }
                                            userhashtagL.add(userhashags);
                                        }
                                        LinearLayoutManager layoutfollow = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        hashtaglist.setLayoutManager(layoutfollow);
                                        hashtaglist.setHasFixedSize(false);
                                        Adapter_hashtagsearch adapterhashtags = new Adapter_hashtagsearch(userhashtagL, getActivity());
                                        hashtaglist.setAdapter(adapterhashtags);
                                    }else {
                                        hashtaglay.setVisibility(View.GONE);
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
                                     //   Log.d("body", "---" + body);
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
