package com.vasmash.va_smash.SearchClass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
import com.vasmash.va_smash.SearchClass.Adapters.Tablayout_searchtabAdapter;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Searchlist;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Trading;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.countDownTimer;
import static com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment.catnameL;
import static com.vasmash.va_smash.ProfileScreen.ProfileActivity.createvideosclick;
import static com.vasmash.va_smash.SearchClass.Adapters.Adapter_Searchlistdata.searchadapterlist;
import static com.vasmash.va_smash.SearchClass.Adapters.Adapter_Searchlistdata.searchlistdata;
import static com.vasmash.va_smash.VASmashAPIS.APIs.search_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.searchdata_url;

public class SearchData extends AppCompatActivity {

    EditText searchview;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    ImageView searchclose;
    Button searchdataback,backserach;
    TextView searchtxt;
    RecyclerView searchlist/*individullist*/;
    LinearLayout searchdatalay,searchlistlay,searchtxtlay,searchindividuallay;

    Adapter_Searchlistdata commentimgAdapter;
    ArrayList<Model_Searchlist> model=null;

    private RequestQueue mQueue;
    String token;
    ArrayList<Model_Trading> searchindividualmodel;
    public static String searchtxtdata="null";
    String peopleintentval="null";

    ArrayList<Tags> des;
    List<String> addtrndins;
    static public ArrayList<String> both;
    public static ArrayList<String> likeL;
    public static ArrayList<String> commentL;
    public static ArrayList<String> userlikesL;
    public static ArrayList<String> sharecountL;
    public static ArrayList<String> fileL;


    private TabLayout searchtablayout;
    private ViewPager searchviewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);

        searchview=findViewById(R.id.searchedit);
        tabLayout= findViewById(R.id.tabLayouttrade);
        viewPager= findViewById(R.id.viewPagertrade);
        searchclose= findViewById(R.id.serchclose);
        searchtxt= findViewById(R.id.searchtxt);
        searchlist= findViewById(R.id.searchlist);
        searchdatalay= findViewById(R.id.searchdatalay);
        searchlistlay= findViewById(R.id.searchlistlay);
        searchtxtlay= findViewById(R.id.searchtxtlay);
        searchdataback= findViewById(R.id.searchdataback);
        backserach= findViewById(R.id.backsearch);
        searchindividuallay= findViewById(R.id.searchindividuallay);
        //  individullist= findViewById(R.id.list);

        searchtablayout=findViewById(R.id.tabLayoutmarket);
        searchviewPager=findViewById(R.id.viewPagermarket);


        addtrndins=new ArrayList<>();
        //it checks where network available or not
        if (isNetworkAvailable()) {
            //Toast.makeText(Home.this, "Network connection is available", Toast.LENGTH_SHORT).show();
        } else if (!isNetworkAvailable()) {
            // Toast.makeText(Home.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Network connection is not available", Snackbar.LENGTH_LONG);
            snackbar.show();
        }


        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(SearchData.this);
        token = phoneauthshard.getString("token", "null");
        mQueue = Volley.newRequestQueue(getApplicationContext());

        addtrndins.add("Trending");
        both = new ArrayList(addtrndins);
        both.addAll(catnameL);
        //Log.d("both111",":::"+both);


        for (int k = 0; k < both.size(); k++) {
            //Log.d("lengthhh",":::"+both.size());
            tabLayout.addTab(tabLayout.newTab().setText(both.get(k)));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //this is the tablayout,here shows the category data.
        final Tablayout_IBHomeAdapter adapter = new Tablayout_IBHomeAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (searchadapterlist){
            // viewPager.setCurrentItem(0);
            searchlistlay.setVisibility(View.GONE);
            searchdatalay.setVisibility(View.GONE);
            searchtxtlay.setVisibility(View.GONE);
            searchdataback.setVisibility(View.VISIBLE);
            backserach.setVisibility(View.GONE);
            searchindividuallay.setVisibility(View.VISIBLE);
            //individualtxt.setText(searchlistdata);
            searchview.setText(searchlistdata);
            // searchapi(search_url + searchlistdata);
            searchtxtdata="null";
            searchadapterlist=false;
        }
        searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlistlay.setVisibility(View.VISIBLE);
                searchdatalay.setVisibility(View.GONE);
                searchtxtlay.setVisibility(View.VISIBLE);
                searchdataback.setVisibility(View.VISIBLE);
                backserach.setVisibility(View.GONE);
            }
        });
        //here data will be enter in to the search
        searchview.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // you can call or do what you want with your EditText here
                // yourEditText...
                //Log.d("editetxtval",":::;"+s+":::::"+model);
                if (searchview.getText().toString().trim().equals("")){
                    if(model!=null){
                        if(model.size()>0){
                            model.clear();
                            commentimgAdapter.notifyDataSetChanged();
                        }
                    }
                }else {
                    searchlistlay.setVisibility(View.VISIBLE);
                    searchdatalay.setVisibility(View.GONE);
                    searchtxtlay.setVisibility(View.VISIBLE);
                    searchdataback.setVisibility(View.VISIBLE);
                    backserach.setVisibility(View.GONE);
                    searchapi(searchdata_url + s);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });

        searchclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchview.getText().clear();
            }
        });

        searchdataback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlistlay.setVisibility(View.GONE);
                searchdatalay.setVisibility(View.VISIBLE);
                searchtxtlay.setVisibility(View.GONE);
                searchdataback.setVisibility(View.GONE);
                searchview.getText().clear();
                backserach.setVisibility(View.VISIBLE);
            }
        });
        searchtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchview.getText().toString().trim().equals("")) {

                }else {
                    //individualtxt.setText(searchtxtdata);
                    //searchapi(search_url + searchtxtdata);
                    Intent intent=new Intent(SearchData.this,SearchData.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("key","true");
                    intent.putExtra("enteringval",searchview.getText().toString());
                    startActivity(intent);
                }
            }
        });
        searchtabview();

    }
    public void searchtabview(){

        if( getIntent().getExtras() != null) {
            Intent intent = getIntent();
            peopleintentval=intent.getStringExtra("key");
            if (peopleintentval.equals("true")) {
                searchtxtdata=intent.getStringExtra("enteringval");
                //Log.d("entringvl","::::"+searchtxtdata);
                searchlistlay.setVisibility(View.GONE);
                searchdatalay.setVisibility(View.GONE);
                searchtxtlay.setVisibility(View.GONE);
                searchdataback.setVisibility(View.VISIBLE);
                backserach.setVisibility(View.GONE);
                searchindividuallay.setVisibility(View.VISIBLE);
                searchlistdata="null";
                // searchview.setText(searchtxtdata);
            }
        }
        searchtablayout.addTab(searchtablayout.newTab().setText("Results"));//DNC
        searchtablayout.addTab(searchtablayout.newTab().setText("Users"));
        searchtablayout.addTab(searchtablayout.newTab().setText("Videos"));
        searchtablayout.addTab(searchtablayout.newTab().setText("Hashtags"));
        searchtablayout.addTab(searchtablayout.newTab().setText("Sounds"));
        searchtablayout.setTabGravity(TabLayout.GRAVITY_FILL);


        Tablayout_searchtabAdapter adapter = new Tablayout_searchtabAdapter(getSupportFragmentManager(),searchtablayout.getTabCount(),searchlistdata,searchtxtdata);
        searchviewPager.setAdapter(adapter);

        searchviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(searchtablayout));

        if (!peopleintentval.equals("null")) {
            if (peopleintentval.equals("userview")) {
                searchlistlay.setVisibility(View.GONE);
                searchdatalay.setVisibility(View.GONE);
                searchtxtlay.setVisibility(View.GONE);
                searchdataback.setVisibility(View.VISIBLE);
                backserach.setVisibility(View.GONE);
                searchindividuallay.setVisibility(View.VISIBLE);
                searchviewPager.setCurrentItem(1);

            } else if (peopleintentval.equals("hashview")) {
                searchlistlay.setVisibility(View.GONE);
                searchdatalay.setVisibility(View.GONE);
                searchtxtlay.setVisibility(View.GONE);
                searchdataback.setVisibility(View.VISIBLE);
                backserach.setVisibility(View.GONE);
                searchindividuallay.setVisibility(View.VISIBLE);
                searchviewPager.setCurrentItem(3);

            } else if (peopleintentval.equals("videoview")) {
                searchlistlay.setVisibility(View.GONE);
                searchdatalay.setVisibility(View.GONE);
                searchtxtlay.setVisibility(View.GONE);
                searchdataback.setVisibility(View.VISIBLE);
                backserach.setVisibility(View.GONE);
                searchindividuallay.setVisibility(View.VISIBLE);
                searchviewPager.setCurrentItem(2);

            } else if (peopleintentval.equals("soundview")) {
                searchlistlay.setVisibility(View.GONE);
                searchdatalay.setVisibility(View.GONE);
                searchtxtlay.setVisibility(View.GONE);
                searchdataback.setVisibility(View.VISIBLE);
                backserach.setVisibility(View.GONE);
                searchindividuallay.setVisibility(View.VISIBLE);
                searchviewPager.setCurrentItem(4);
            }
        }

        searchtablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchviewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public class Tablayout_IBHomeAdapter extends FragmentStatePagerAdapter {
        //integer to count number of tabs
        private int tabCount;
        //Constructor to the class
        public Tablayout_IBHomeAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount= tabCount;
        }
        //Overriding method getItem
        @Override
        public Fragment getItem(final int position) {
            //Returning the current tabs
            return SearchFragmentData.newInstance(position);
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }
    }


    public void searchapi(String homeapi_url){

        //Log.d("search api::::", homeapi_url);
        des=new ArrayList<>();
        model = new ArrayList<>();
        searchindividualmodel = new ArrayList<>();
        likeL=new ArrayList<>();
        commentL=new ArrayList<>();
        userlikesL=new ArrayList<>();
        sharecountL=new ArrayList<>();
        fileL=new ArrayList<>();
        commentL.clear();
        userlikesL.clear();
        sharecountL.clear();
        fileL.clear();
        likeL.clear();


        des.clear();
        model.clear();
        searchindividualmodel.clear();

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, homeapi_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        //Log.d("Response search", response.toString());
                        createvideosclick="false";

                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    Model_Searchlist hm = new Model_Searchlist();
                                    if (employee.has("name")) {
                                        hm.setName(employee.getString("name"));
                                        model.add(hm);
                                        searchlist.setHasFixedSize(true);
                                        // use a linear layout manager
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchData.this);
                                        searchlist.setLayoutManager(mLayoutManager);

                                        commentimgAdapter = new Adapter_Searchlistdata(SearchData.this, model);
                                        searchlist.setAdapter(commentimgAdapter);
                                        commentimgAdapter.notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(SearchData.this, "No Data", Toast.LENGTH_SHORT).show();
                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

    public void finishActivity(View v){
        finish();
        if (countDownTimer!=null) {
            countDownTimer.start();
        }
    }
    //it checks where network available or not
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
