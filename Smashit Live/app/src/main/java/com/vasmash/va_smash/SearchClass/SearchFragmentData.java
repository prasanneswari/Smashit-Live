package com.vasmash.va_smash.SearchClass;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.ProfileScreen.OtherprofileActivity;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_TradingTabs;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Trading;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment.catidL;
import static com.vasmash.va_smash.SearchClass.SearchData.both;
import static com.vasmash.va_smash.VASmashAPIS.APIs.homeapi_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.otherprofileuser_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.search_url;

public class SearchFragmentData extends Fragment {

    RecyclerView tradingtab;
    Adapter_TradingTabs commentimgAdapter;
    ArrayList<Model_Trading> model;
    List<String> cataddL;
    List<String> catbothaddL;
    public static ArrayList<String> fileL;

    private RequestQueue mQueue;
    String token;
    LottieAnimationView animationView;

    int pastVisibleItems11, visibleItemCount11, totalItemCount11;
    Boolean loading11 = true;
    int currentPage=-1;
    int page_no;
    ProgressBar p_bar;
    private boolean loading;


    // TODO: Rename and change types and number of parameters
    public static SearchFragmentData newInstance(int position) {
        SearchFragmentData fragment = new SearchFragmentData();
        Bundle args = new Bundle();
        args.putInt("tabcount", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_trending_tab, container, false);

        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = phoneauthshard.getString("token", "null");
        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        tradingtab=view.findViewById(R.id.tradingtab);
        animationView = view.findViewById(R.id.animation_view_1);
        p_bar=view.findViewById(R.id.p_bar);

        cataddL=new ArrayList<>();
        int tabname = getArguments().getInt("tabcount");
        //Log.d("tabnameee",":::::"+both.get(tabname));

        String values=both.get(tabname);

        cataddL.add("1");
        catbothaddL = new ArrayList(cataddL);
        catbothaddL.addAll(catidL);
       // Log.d("cattboth111",":::"+catbothaddL);
        //here we select the particular tab category value
        for (int i = 0; i< both.size(); i++){
            if (values.equals(both.get(i))){
               // Log.d("catnameL.get(i)",":::"+both.get(i));
                String catid= catbothaddL.get(i);
               // Log.d("catiddddd",":::"+catid);
                searchpagination(both.get(i),catid);
            }
        }
        return view;
    }

    public void searchpagination(String both,String catid){
        model = new ArrayList<>();
        fileL=new ArrayList<>();

        //commentimg
        tradingtab.setHasFixedSize(true);
        // set a GridLayoutManager with 3 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        tradingtab.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        // use a linear layout manager
        // RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        commentimgAdapter = new Adapter_TradingTabs(getActivity(), model,fileL);
        tradingtab.setAdapter(commentimgAdapter);

        loading11 = false;
        loading=false;

        tradingtab.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    visibleItemCount11 = gridLayoutManager.getChildCount();
                    totalItemCount11 = gridLayoutManager.getItemCount();
                    pastVisibleItems11 = gridLayoutManager.findFirstVisibleItemPosition();
                   // Log.d("visibleItemCount","::::"+visibleItemCount11+":::"+totalItemCount11+"::"+pastVisibleItems11+":::"+loading11);

                    if (!loading11) {
                        // if ((visibleItemCount11 + pastVisibleItems11) >= totalItemCount11 && visibleItemCount11 >= 0 && totalItemCount11 >= tags.size()) {
                        //Log.d("checkpage","::"+gridLayoutManager.findLastCompletelyVisibleItemPosition()+":::"+model.size());
                        if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == model.size()-1){
                            loading11 = true;
                            loading=true;
                            if (both.equals("Trending")){
                                searchapi(homeapi_url+"/postsByCategory?trending=1&limit=10&skip="+model.size());
                            }else {
                                searchapi(homeapi_url +"/postsByCategory?categoryId="+catid+"&limit=10&skip="+model.size());
                            }
                        }
                    }
                }
            }
        });
        if (both.equals("Trending")){
            searchapi(homeapi_url+"/postsByCategory?trending=1&limit=10&skip=10");
        }else {
            searchapi(homeapi_url +"/postsByCategory?categoryId="+catid+"&limit=10&skip=0");
        }
    }
    public void searchapi(String homeapi_url){
       // Log.d("home api::::", homeapi_url);
        if (loading){
            p_bar.setVisibility(View.VISIBLE);
        }else {
            loader();
        }
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, homeapi_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                       // Log.d("Response homeee", response.toString());

                        if (loading){
                            p_bar.setVisibility(View.GONE);
                        }else {
                            animationView.cancelAnimation();
                            animationView.setVisibility(View.GONE);
                        }

                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    Model_Trading hm=new Model_Trading();
                                    if (employee.has("_id")) {
                                        hm.setId(employee.getString("_id"));
                                    } else {
                                        hm.setId("");
                                    }
                                    if (employee.has("file")) {
                                        hm.setImage(employee.getString("file"));
                                        fileL.add(employee.getString("file"));
                                       // Log.d("fileLLLLLL", ":::"+fileL);

                                    } else {
                                        hm.setImage("");
                                        fileL.add("");
                                    }
                                    if (employee.has("likes")) {
                                        hm.setCount(employee.getString("likes"));
                                    } else {
                                        hm.setCount("");
                                    }
                                    if (employee.has("userLikes")) {
                                        hm.setLikescondition(employee.getString("userLikes"));
                                    } else {
                                        hm.setLikescondition("");
                                    }
                                    if (employee.has("type")) {
                                        hm.setType(employee.getString("type"));
                                    } else {
                                        hm.setType("");

                                    }
                                    if (employee.has("comments")) {
                                        hm.setComment(employee.getString("comments"));
                                    } else {
                                        hm.setComment("");
                                    }
                                    if (employee.has("shareCount")) {
                                        String shareCount = employee.getString("shareCount");
                                        hm.setSharecount(shareCount);
                                    } else {
                                        hm.setComment("");
                                    }

                                    if (employee.has("description")) {
                                        String description = employee.getString("description");
                                        hm.setDescription(description);

                                    }else {
                                        hm.setDescription("");
                                    }
                                    if (employee.has("thumb")) {
                                        hm.setGifimg(employee.getString("thumb"));
                                    }else {
                                        hm.setGifimg(" ");
                                    }
                                    if (employee.has("comments")) {
                                        hm.setComment(employee.getString("comments"));
                                    } else {
                                        hm.setComment("");
                                    }
                                    if (employee.has("shareCount")) {
                                        String shareCount = employee.getString("shareCount");
                                        hm.setSharecount(shareCount);
                                    } else {
                                        hm.setComment("");
                                    }


                                    if (employee.has("userId")) {
                                        JSONObject nameobj=employee.getJSONObject("userId");
                                        //Log.d("nameobj","::::"+nameobj);
                                        String username = nameobj.getString("name");
                                        String homeprofilePic = nameobj.getString("profilePic");
                                        String userid = nameobj.getString("_id");

                                        //Log.d("userid11111","::::"+userid);
                                        hm.setUsername(username);
                                        hm.setProfilepic(homeprofilePic);
                                        hm.setUserid(userid);

                                    }else {
                                        String username = " ";
                                        hm.setUsername(username);
                                        hm.setProfilepic("null");
                                        hm.setUserid("null");
                                    }
                                    if (employee.has("soundId")) {
                                        //JSONArray sounds = employee.getJSONArray("soundId");
                                        //Log.d("soundsss","::::"+sounds);
                                        // for (int k1 = 0; k1 < sounds.length(); k1++) {
                                        JSONObject soundsobj = employee.getJSONObject("soundId");
                                        if(soundsobj.has("_id")) {
                                            String soundid = soundsobj.getString("_id");
                                            hm.setSoundid(soundid);
                                        }
                                        if (soundsobj.has("name")) {
                                            String soundname = soundsobj.getString("name");
                                            hm.setSoundname(soundname);
                                        }
                                        if (soundsobj.has("url")) {
                                            String soundurl = soundsobj.getString("url");
                                            hm.setSoundurl(soundurl);
                                        }
                                        if (soundsobj.has("postId")) {
                                            String soundpostId = soundsobj.getString("postId");
                                            hm.setSoundpostid(soundpostId);
                                        }
                                        if (soundsobj.has("userId")) {
                                            String sounduserId = soundsobj.getString("userId");
                                            hm.setSounduserid(sounduserId);
                                        }
                                        // }
                                    }else {
                                        hm.setSoundname("");
                                        hm.setSoundurl("");
                                        hm.setSoundpostid("");
                                        hm.setSounduserid("");
                                    }

                                    model.add(hm);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            commentimgAdapter.notifyDataSetChanged();
                            loading11 = false;
                        }else {
                            // Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                           // searchapi(search_url);
                            loading = true;
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
                                            if (loading){
                                                p_bar.setVisibility(View.GONE);
                                            }else {
                                                animationView.cancelAnimation();
                                                animationView.setVisibility(View.GONE);
                                            }

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
                if (loading){
                    p_bar.setVisibility(View.GONE);
                }else {
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.GONE);
                }
            }

        });


    }

    public void loader(){
        //lotte loader
        animationView.setAnimation("data.json");
        animationView.loop(true);
        animationView.playAnimation();
        animationView.setVisibility(View.VISIBLE);
    }
}
