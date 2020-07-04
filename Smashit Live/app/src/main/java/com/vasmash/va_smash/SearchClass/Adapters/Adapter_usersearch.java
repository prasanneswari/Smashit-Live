package com.vasmash.va_smash.SearchClass.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.vasmash.va_smash.ProfileScreen.Adapter.Adapter_follow;
import com.vasmash.va_smash.ProfileScreen.Model_Class.Model_userfollow_unfollow;
import com.vasmash.va_smash.ProfileScreen.OtherprofileActivity;
import com.vasmash.va_smash.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vasmash.va_smash.ProfileScreen.Userfollow_following.adapterfollowing;
import static com.vasmash.va_smash.ProfileScreen.Userfollow_following.followingbtn;
import static com.vasmash.va_smash.ProfileScreen.Userfollow_following.token;
import static com.vasmash.va_smash.VASmashAPIS.APIs.follow_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.unfollow_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.userfollow_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.userunfollow_url;

public class Adapter_usersearch extends RecyclerView.Adapter<Adapter_usersearch.ViewHolder> {

    ArrayList<Model_userfollow_unfollow> mainmodels,tempCustomer, suggestions;

    String followcount;
    Context context;
    private RequestQueue mQueue;
    public static String token;


    public Adapter_usersearch(ArrayList<Model_userfollow_unfollow> mainmodels, Context context) {
        this.mainmodels = mainmodels;
        this.tempCustomer = new ArrayList<Model_userfollow_unfollow>(mainmodels);
        this.suggestions = new ArrayList<Model_userfollow_unfollow>(mainmodels);

        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_follow_following, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.nameid.setText(mainmodels.get(position).getName());

        Picasso.with(context).load(mainmodels.get(position).getUserpic()).into(holder.userpic);

        String followinguser=mainmodels.get(position).getFollowinguser();

        if (followinguser.equals("true")){
            holder.follow.setVisibility(View.GONE);
            holder.unfollow.setVisibility(View.VISIBLE);
        }else {
            holder.follow.setVisibility(View.VISIBLE);
            holder.unfollow.setVisibility(View.GONE);
        }

        holder.userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = mainmodels.get(position).getUsersearchid();
                Intent intent = new Intent(context, OtherprofileActivity.class);
                intent.putExtra("posteduserid", userid);
                context.startActivity(intent);
            }
        });

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginS = "{\"userId\":\"" + mainmodels.get(position).getUsersearchid() + "\",\"isCurrent\":\""+true+"\"}";
                Log.d("jsnresponse follow", "---" + loginS);
                String url = userfollow_url;
                JSONObject lstrmdt;
                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);
                    JSONSenderVolleylogin(lstrmdt, url, holder);
                } catch (JSONException ignored) {
                }
            }
        });
        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginS = "{\"followingId\":\"" + mainmodels.get(position).getUsersearchid() + "\",\"isCurrent\":\""+true+"\"}";
                Log.d("jsnresponse follow", "---" + loginS);
                String url = userunfollow_url;
                JSONObject lstrmdt;
                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);
                    JSONSenderVolleylogin(lstrmdt, url, holder);
                } catch (JSONException ignored) {
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mainmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameid, name;
        CircleImageView userpic;
        Button follow, unfollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            nameid = itemView.findViewById(R.id.nameid);
            userpic = itemView.findViewById(R.id.userpic);
            follow = itemView.findViewById(R.id.follow);
            unfollow = itemView.findViewById(R.id.unfollow);
            mQueue = Volley.newRequestQueue(context);
            SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(context);
            token = phoneauthshard.getString("token", "null");


        }
    }

    public void JSONSenderVolleylogin(JSONObject lstrmdt, String url,ViewHolder holder) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        Log.d("555555", "url" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONfollowand unfollow", "---" + response);
                        try {
                            String message = response.getString("message");
                            String status = response.getString("status");
                            if (response.has("count")) {
                                followcount = response.getString("count");
                            }
                            if (status.equals("1")) {
                                //jsongetvastore(posteduserid);
                                // popup(message);
                                holder.follow.setVisibility(View.GONE);
                                holder.unfollow.setVisibility(View.VISIBLE);
                            } else if (status.equals("2")) {
                                // popup(message);
                                holder.follow.setVisibility(View.VISIBLE);
                                holder.unfollow.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                                String id = null;
                                if (obj.has("id")) {
                                    id = obj.getString("id");
                                }
                                if (obj.has("errors")) {
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
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
            }
        });
    }

    public <T> void addToRequestQueue(Request<T> req) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
        mQueue.add(req);
    }

}
