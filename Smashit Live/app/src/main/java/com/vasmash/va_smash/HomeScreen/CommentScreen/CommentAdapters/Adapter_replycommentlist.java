package com.vasmash.va_smash.HomeScreen.CommentScreen.CommentAdapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentModels.Model_replycomment;
import com.vasmash.va_smash.HomeScreen.CommentScreen.Fragment_Data_Send;
import com.vasmash.va_smash.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.commenttxt;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.countcomment;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.mainAdapter;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.oftersend;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.proftoken;
import static com.vasmash.va_smash.VASmashAPIS.APIs.deletcomment_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.likescomment_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.reportcomment_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.unlikecomment_url;


public class Adapter_replycommentlist extends RecyclerView.Adapter<Adapter_replycommentlist.ViewHolder>{
    public ArrayList<Model_replycomment> mainmodel;

    Context context;
    static public String userlist;
    private RequestQueue mQueue;
    public  String usericon="null",isonwer;
    Dialog dialog;

    //report
    EditText desE;
    Spinner reasonE;
    String selectedItemText;
    String[] reasonspinner = {"Select Reasons","Inappropriate Content", " Abusive  Content"};
    Fragment_Data_Send fragment_data_send;
    View view;
    ArrayList<String> replylikecommentL;
    ArrayList<String> replylikeconditionL;


    public Adapter_replycommentlist(ArrayList<Model_replycomment> mainmodels, ArrayList<String> replylikecommentL, ArrayList<String> replylikeconditionL, Context context) {
        this.mainmodel = mainmodels;
        this.context = context;
        this.replylikecommentL=replylikecommentL;
        this.replylikeconditionL=replylikeconditionL;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView commentlike,commentunlike;
        CircleImageView commentlistprofile;
        TextView commentname,commentcontent,likecount;

        public ViewHolder(View v){

            super(v);

            commentlistprofile=itemView.findViewById(R.id.commentlistprofile);
            commentlike=itemView.findViewById(R.id.commentlike);
            commentname=itemView.findViewById(R.id.commentname);
            commentcontent=itemView.findViewById(R.id.commentcontent);
            likecount=itemView.findViewById(R.id.likecount);
            commentunlike=itemView.findViewById(R.id.commentunlike);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter_commentreply,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        Log.d("replyyadapter","::::"+mainmodel.size());
        guestures(view,position);

        holder.commentcontent.setText(mainmodel.get(position).getContent());
        holder.commentname.setText(mainmodel.get(position).getName());
        // holder.commentlistprofile.setImageResource(mainmodels.get(position).getProfile());

        // String likecounts=mainmodels.get(position).getLikecount();
        usericon=mainmodel.get(position).getProfile();
        isonwer=mainmodel.get(position).getIsowner();

        holder.likecount.setText(replylikecommentL.get(position));

        //Log.d("commentprofile",":::"+usericon);
        if (!usericon.equals("null")) {
            // Log.d("entringggg",":::"+usericon);
            try {
                Picasso.with(context).load(usericon).into(holder.commentlistprofile);
            }catch (Exception e){
                Picasso.with(context).load(R.drawable.profilecircle).into(holder.commentlistprofile);
            }
        }
        userlist=mainmodel.get(position).getName();
        if (replylikeconditionL.get(position).equals("true")){
            holder.commentunlike.setVisibility(View.VISIBLE);
            holder.commentlike.setVisibility(View.GONE);
        }else {
            //   holder.commentlike.setImageResource(R.drawable.commentunlike);
            holder.commentunlike.setVisibility(View.GONE);
            holder.commentlike.setVisibility(View.VISIBLE);
        }

        holder.commentlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String useridclik=mainmodel.get(position).getUserid();
                String commentidclik=mainmodel.get(position).getCommentid();

                String loginS = "{\"userId\":\"" + useridclik + "\",\"commentId\":\"" + commentidclik + "\"}";
                Log.d("jsnresponse login", "---" + loginS);
                String url=likescomment_url;
                JSONObject lstrmdt;
                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);
                    JSONSenderVolleycomment(lstrmdt,url,holder,position);

                } catch (JSONException ignored) {
                }
            }
        });

        holder.commentunlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idcomment=mainmodel.get(position).getCommentid();
                String loginS = "{\"_id\":\"" + idcomment + "\"}";
                Log.d("unlike comment", "---" + loginS);
                String url=unlikecomment_url;
                JSONObject lstrmdt;

                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);
                    JSONSenderVolleycomment(lstrmdt,url,holder,position);

                } catch (JSONException ignored) {
                }
            }
        });

    }

    @Override
    public int getItemCount(){
        return mainmodel.size();
    }


    public void JSONSenderVolleycomment(JSONObject lstrmdt, String url, final ViewHolder holder, final int position) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        Log.d("555555", "login" + url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONSenderVolleylogin", "---" + response);
                        try {
                            if (response.length()!=0) {

                                if (response.has("liked")) {
                                    String message = response.getString("message");
                                    String liked = response.getString("liked");
                                    String count = response.getString("count");

                                    holder.commentunlike.setVisibility(View.VISIBLE);
                                    holder.commentlike.setVisibility(View.GONE);
                                    holder.likecount.setText(count);

                                    //String index=likecommentcount.get(position);

                                    replylikecommentL.set(position, count);
                                    replylikeconditionL.set(position, liked);
                                   // mainAdapter.notifyDataSetChanged();
                                }else {
                                    String unlikemessage = response.getString("message");
                                    String unlikecount = response.getString("count");
                                    String unlikecounttype = response.getString("unliked");

                                    holder.commentunlike.setVisibility(View.GONE);
                                    holder.commentlike.setVisibility(View.VISIBLE);
                                    holder.likecount.setText(unlikecount);

                                    //String index=likecommentcount.get(position);

                                    replylikecommentL.set(position, unlikecount);
                                    replylikeconditionL.set(position, unlikecounttype);

                                   // mainAdapter.notifyDataSetChanged();

                                }

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
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 422:
                            try {

                                body = new String(error.networkResponse.data,"UTF-8");
                                Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                String id = null;
                                if (obj.has("id")) {
                                    id = obj.getString("id");
                                }

                                if (obj.has("errors")) {
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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
                headers.put("Authorization",proftoken);

                //return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
                return headers;
            }
        };
        jsonObjReq.setTag("");
        addToRequestQueue(jsonObjReq);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
        mQueue.add(req);
    }

    public void guestures(View view, int position){
        view.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    super.onFling(e1, e2, velocityX, velocityY);
                    return true;
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    super.onSingleTapUp(e);
                    Log.d("singlelcick","::");
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    Log.d("replylongggg","::"+position+":::"+isonwer);
                    if (isonwer.equals("true")){
                        popup(position);
                    }else {
                        reportpopup(position);
                    }
                }
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    return super.onDoubleTap(e);

                }
            });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);

                return true;
            }
        });
    }

    public void popup(int position) {

        android.app.AlertDialog.Builder builder;
        final Context mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.commentdeletepopup, null);

        Button delete = (Button) layout.findViewById(R.id.delete);
        builder = new android.app.AlertDialog.Builder(mContext);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idcomment=mainmodel.get(position).getCommentid();
                String postid=mainmodel.get(position).getPostid();

                String loginS = "{\"_id\":\"" + idcomment + "\",\"postId\":\"" + postid + "\"}";
                Log.d("delete comment", "---" + loginS);
                String url=deletcomment_url;
                JSONObject lstrmdt;
                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);
                    JSONSenderVolleydelete(lstrmdt,url,position);
                } catch (JSONException ignored) {
                }
            }
        });
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();
    }

    public void reportpopup(int position) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.reportpoup, null);

        Button nobtn = (Button) layout.findViewById(R.id.nobtn);
        Button reportbtn = (Button) layout.findViewById(R.id.reportbtn);
        reasonE = (Spinner) layout.findViewById(R.id.reason);
        desE = (EditText) layout.findViewById(R.id.des);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(context,android.R.layout.simple_spinner_item,reasonspinner);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        reasonE.setAdapter(aa);
        reasonE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idcomment=mainmodel.get(position).getCommentid();
                String userid=mainmodel.get(position).getUserid();

                String loginS = "{\"userId\":\"" + userid + "\",\"commentId\":\"" + idcomment + "\",\"reason\":\"" + selectedItemText + "\",\"description\":\"" + desE.getText().toString() + "\"}";
                Log.d("jsnresponse reason", "---" + loginS);
                String url = reportcomment_url;
                JSONObject lstrmdt;

                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);

                    JSONSenderVolleydelete(lstrmdt,url,position);

                } catch (JSONException ignored) {
                }
            }
        });
        nobtn.setOnClickListener(new View.OnClickListener() {
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


    public void JSONSenderVolleydelete(JSONObject lstrmdt, String url, int position) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        Log.d("555555", "delete" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("jsondelete", "---" + response);
                        if (response.length()!=0){
                            try {
                                String message=response.getString("message");
                                String status=response.getString("status");
                                if (status.equals("1")){
                                    String commentcount=response.getString("count");
                                    countcomment.setText(commentcount+" "+"Comments");
                                    if(fragment_data_send!=null)
                                        fragment_data_send.onDataSent(String.valueOf(commentcount));

                                    mainmodel.remove(position);
                                    dialog.dismiss();
                                    mainAdapter.notifyDataSetChanged();
                                }else if (status.equals("3")){
                                    dialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
                                String id = null;
                                if (obj.has("id")) {
                                    id = obj.getString("id");
                                }

                                if (obj.has("errors")) {
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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
                headers.put("Authorization",proftoken);

                //return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
                return headers;
            }
        };
        jsonObjReq.setTag("");
        addToRequestQueue(jsonObjReq);
    }

}

