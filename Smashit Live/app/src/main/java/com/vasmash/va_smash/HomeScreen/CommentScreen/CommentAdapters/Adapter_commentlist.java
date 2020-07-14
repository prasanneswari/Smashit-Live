package com.vasmash.va_smash.HomeScreen.CommentScreen.CommentAdapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentModels.Model_Commentlist;
import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentModels.Model_replycomment;
import com.vasmash.va_smash.HomeScreen.CommentScreen.Fragment_Data_Send;
import com.vasmash.va_smash.HomeScreen.ModelClass.Model_commentuserlst;
import com.vasmash.va_smash.ProfileScreen.OtherprofileActivity;
import com.vasmash.va_smash.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.commentatlist;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.commentlst;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.commenttxt;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.commentuserAdapter;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.countcomment;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.mainAdapter;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.modelusers;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.oftersend;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.proftoken;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.replymodel;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.userlistL;
import static com.vasmash.va_smash.VASmashAPIS.APIs.comment_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.deletcomment_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.likescomment_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.replycomment_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.reportcomment_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.unlikecomment_url;

public class Adapter_commentlist extends RecyclerView.Adapter<Adapter_commentlist.ViewHolder> {

    public static ArrayList<Model_Commentlist> mainmodel;
    ArrayList<String> likecommentcount;
    ArrayList<String> likecommenttype;
    Context context;
    static public String userlist;
    private RequestQueue mQueue;
    public static String usericon="null",usericonreplies="null";
    Dialog dialog;

    //report
    EditText desE;
    Spinner reasonE;
    String selectedItemText;
    String[] reasonspinner = {"Select Reasons","Inappropriate Content", " Abusive  Content"};
    Fragment_Data_Send fragment_data_send;
    public static View viewholoder;
    public  int adaperpos;
    //int replypos;

    public Adapter_commentlist(ArrayList<Model_Commentlist> mainmodels, ArrayList<String> likecommentcount, ArrayList<String> likecommenttype, Fragment_Data_Send fragment_data_send, Context context) {
        this.mainmodel = mainmodels;
        this.context = context;
        this.likecommentcount=likecommentcount;
        this.likecommenttype=likecommenttype;
        this.fragment_data_send=fragment_data_send;
        //Log.d("adapterreplyy","::"+mainmodel.size());

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewholoder= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter_commentlist,parent,false);

        viewholoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(commenttxt.getWindowToken(),0);
            }
        });
        return new ViewHolder(viewholoder);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String username=mainmodel.get(position).getName();
        String commentpostid=mainmodel.get(position).getPostid();
        String commentid= mainmodel.get(position).getCommentid();
        usericon=mainmodel.get(position).getProfile();
        String isonwer=mainmodel.get(position).getIsowner();
        String commentuserid=mainmodel.get(position).getUserid();

        guestures(viewholoder,position,holder,username,commentpostid,commentid,isonwer,commentuserid,mainmodel,null,true);

        adaperpos=position;
        holder.commentcontent.setText(mainmodel.get(position).getContent());
        holder.commentname.setText(username);
        // holder.commentlistprofile.setImageResource(mainmodels.get(position).getProfile());

        // String likecounts=mainmodels.get(position).getLikecount();

        holder.likecount.setText(likecommentcount.get(position));

        //Log.d("commentprofile",":::"+usericon);
        if (!usericon.equals("null")) {
            // Log.d("entringggg",":::"+usericon);
            try {
                Picasso.with(context).load(usericon).into(holder.commentlistprofile);
            }catch (Exception e){
                Picasso.with(context).load(R.drawable.profilecircle).into(holder.commentlistprofile);
            }
        }
        if (likecommenttype.get(position).equals("true")){
            holder.commentunlike.setVisibility(View.VISIBLE);
            holder.commentlike.setVisibility(View.GONE);
        }else {
            //   holder.commentlike.setImageResource(R.drawable.commentunlike);
            holder.commentunlike.setVisibility(View.GONE);
            holder.commentlike.setVisibility(View.VISIBLE);
        }
        holder.commentlistprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid=mainmodel.get(position).getUserid();
                Intent intent = new Intent(context, OtherprofileActivity.class);
                intent.putExtra("posteduserid", userid);
                context.startActivity(intent);

            }
        });
        holder.commentlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // replylike=false;
                String useridclik=mainmodel.get(position).getUserid();
                String commentidclik=mainmodel.get(position).getCommentid();

                String loginS = "{\"userId\":\"" + useridclik + "\",\"commentId\":\"" + commentidclik + "\"}";
                //Log.d("jsnresponse login", "---" + loginS);
                String url=likescomment_url;
                JSONObject lstrmdt;
                try {
                    lstrmdt = new JSONObject(loginS);
                    //Log.d("jsnresponse....", "---" + loginS);
                    JSONSenderVolleycomment(lstrmdt,url,holder,position,null,null,null,false,null,false,null);

                } catch (JSONException ignored) {
                }
            }
        });

        holder.commentunlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //replyunlike=false;
                String idcomment=mainmodel.get(position).getCommentid();
                String loginS = "{\"_id\":\"" + idcomment + "\"}";
                //Log.d("unlike comment", "---" + loginS);
                String url=unlikecomment_url;
                JSONObject lstrmdt;

                try {
                    lstrmdt = new JSONObject(loginS);
                    //Log.d("jsnresponse....", "---" + loginS);
                    JSONSenderVolleycomment(lstrmdt,url,holder,position,null,null,null,false,null,true,null);
                } catch (JSONException ignored) {
                }
            }
        });

/*
        holder.viewreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.viewreply.setVisibility(View.GONE);
                holder.hidepely.setVisibility(View.VISIBLE);

                holder.replylist.setHasFixedSize(true);
                // use a linear layout manager
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                holder.replylist.setLayoutManager(mLayoutManager);
                // model = new ArrayList<>();
                ArrayList<Model_replycomment> replymodel=mainmodel.get(position).getReplymodel();
                Log.d("adapterreplyy","::"+replymodel.size());

                if (replymodel!=null) {
                    if (replymodel.size() != 0) {
                        holder.replylist.setVisibility(View.VISIBLE);
                        Adapter_replycommentlist mainAdapter = new Adapter_replycommentlist(replymodel,replylikecommentL,replylikeconditionL, context);
                        holder.replylist.setAdapter(mainAdapter);
                        //mainAdapter.notifyDataSetChanged();
                    }else {
                        holder.replylist.setVisibility(View.GONE);
                    }
                }else {
                    holder.replylist.setVisibility(View.GONE);
                }
            }
        });
*/
        // Log.d("adapterreplyy","::"+replymodel.size());

        ArrayList<Model_replycomment> replymodeMain=mainmodel.get(position).getReplymodel();
        if (replymodeMain!=null) {
            if (replymodeMain.size() != 0) {
                holder.viewreply.setVisibility(View.VISIBLE);
                holder.hidepely.setVisibility(View.GONE);
            }else {
                holder.viewreply.setVisibility(View.GONE);
            }
        }else {
            holder.viewreply.setVisibility(View.GONE);
        }

/*
        holder.hidepely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("entringviewre", ":::"+position);
                holder.hidepely.setVisibility(View.GONE);
                holder.viewreply.setVisibility(View.VISIBLE);
                holder.replylist.setVisibility(View.GONE);

            }
        });
*/
        holder.viewreply.setTag(position);

        holder.viewreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("entringviewre", ":::"+position);
                holder.viewreply.setVisibility(View.GONE);
                holder.hidepely.setVisibility(View.VISIBLE);
                holder.innerrepliesll.setVisibility(View.VISIBLE);
                replycomment(holder,(int)v.getTag());
                //replylay.setVisibility(View.VISIBLE);
            }
        });

        holder.hidepely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("entringviewre", ":::"+position);
                holder.hidepely.setVisibility(View.GONE);
                holder.viewreply.setVisibility(View.VISIBLE);
                holder.innerrepliesll.setVisibility(View.GONE);
                replymodeMain.clear();
            }
        });


    }

    private void replycomment(ViewHolder holder, int position) {
        //Log.d("mainposition","::"+position);

        ArrayList<Model_replycomment> replymodelInner=mainmodel.get(position).getReplymodel();

        if (replymodelInner!=null) {
            if (replymodelInner.size() != 0) {
                //holder.innerrepliesll.setVisibility(View.VISIBLE);
                //Log.d("inside","::"+replymodelInner.size());
                for (int i=0;i<replymodelInner.size();i++) {

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View itemView = inflater.inflate(R.layout.activity_adapter_commentreply, holder.innerrepliesll, false);
                    CircleImageView commentlistprofile = itemView.findViewById(R.id.commentlistprofile);
                    ImageView commentlike = itemView.findViewById(R.id.commentlike);
                    TextView commentname = itemView.findViewById(R.id.commentname);
                    TextView commentcontent = itemView.findViewById(R.id.commentcontent);
                    TextView likecount = itemView.findViewById(R.id.likecount);
                    ImageView commentunlike = itemView.findViewById(R.id.commentunlike);
                    LinearLayout replylay = itemView.findViewById(R.id.replylay);

                    replylay.setVisibility(View.VISIBLE);

                    // holder.viewreply.setVisibility(View.VISIBLE);
                    commentcontent.setText(replymodelInner.get(i).getContent());
                    commentname.setText(replymodelInner.get(i).getName());

                    usericonreplies=replymodelInner.get(i).getProfile();
                    likecount.setText(replymodelInner.get(i).getLikecount());

                    if (!usericonreplies.equals("null")) {
                        try {
                            Picasso.with(context).load(usericonreplies).into(commentlistprofile);
                        }catch (Exception e){
                            Picasso.with(context).load(R.drawable.profilecircle).into(commentlistprofile);
                        }
                    }
                    if (replymodelInner.get(i).getLiketype().equals("true")){
                        commentunlike.setVisibility(View.VISIBLE);
                        commentlike.setVisibility(View.GONE);
                    }else {
                        //   holder.commentlike.setImageResource(R.drawable.commentunlike);
                        commentunlike.setVisibility(View.GONE);
                        commentlike.setVisibility(View.VISIBLE);
                    }
                    commentlistprofile.setTag(i);
                    commentlistprofile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int  innerPos=(int)v.getTag();
                            String userid=replymodelInner.get(innerPos).getUserid();
                            Intent intent = new Intent(context, OtherprofileActivity.class);
                            intent.putExtra("posteduserid", userid);
                            context.startActivity(intent);

                        }
                    });

                    commentlike.setTag(i);
                    commentlike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int  innerPos=(int)v.getTag();
                           // Log.d("commentlikeposs",":::"+innerPos);
                            String useridclik=replymodelInner.get(innerPos).getUserid();
                            String commentidclik=replymodelInner.get(innerPos).getCommentid();

                            // Log.d("replypos....", "---" +innerPos+":::"+":::::"+commentidclik+":::"+position);
                            String loginS = "{\"userId\":\"" + useridclik + "\",\"commentId\":\"" + commentidclik + "\"}";
                            //Log.d("jsnresponse login", "---" + loginS);
                            String url=likescomment_url;
                            JSONObject lstrmdt;
                            try {
                                lstrmdt = new JSONObject(loginS);
                                //Log.d("jsnresponse....", "---" + loginS);
                                JSONSenderVolleycomment(lstrmdt,url,holder,innerPos,commentlike,commentunlike,likecount,true,replymodelInner,false,null);

                            } catch (JSONException ignored) {
                            }
                        }
                    });
                    commentunlike.setTag(i);
                    commentunlike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int  innerPos=(int)v.getTag();
                            String commentidclik=replymodelInner.get(innerPos).getCommentid();

                            //Log.d("unclickreplypos....", "---" +":::::"+commentidclik+":::"+position);
                            //replyunlike=true;
                            String loginS = "{\"_id\":\"" + commentidclik + "\"}";
                            //Log.d("unlike comment", "---" + loginS);
                            String url=unlikecomment_url;
                            JSONObject lstrmdt;

                            try {
                                lstrmdt = new JSONObject(loginS);
                                //Log.d("jsnresponse....", "---" + loginS);
                                JSONSenderVolleycomment(lstrmdt,url,holder,innerPos,commentlike,commentunlike,likecount,false, replymodelInner,false,null);

                            } catch (JSONException ignored) {
                            }
                        }
                    });

                    itemView.setTag(i);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int replyposclick=(int)v.getTag();
                            //Log.d("recyclarposs",":::"+replyposclick+"::::"+position);
                            // String username=replymodelInner.get(replyposclick).getName();
                            String commentpostid=replymodelInner.get(replyposclick).getPostid();
                            String commentid= replymodelInner.get(replyposclick).getCommentid();
                            String isonwer=replymodelInner.get(replyposclick).getIsowner();
                            String replyuserid=replymodelInner.get(replyposclick).getUserid();

                            guestures(v,replyposclick,holder,"Enter Replies",commentpostid,commentid,isonwer,replyuserid,null,replymodelInner,false);

                        }
                    });

                    holder.innerrepliesll.addView(itemView);

                }
            }else {
                holder.viewreply.setVisibility(View.GONE);
            }
        }else {
            holder.viewreply.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mainmodel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView commentlike,commentunlike;
        CircleImageView commentlistprofile;
        TextView commentname,commentcontent,likecount;
        LinearLayout viewreply,hidepely;
        RecyclerView replylist;
        LinearLayout innerrepliesll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentlistprofile=itemView.findViewById(R.id.commentlistprofile);
            commentlike=itemView.findViewById(R.id.commentlike);
            commentname=itemView.findViewById(R.id.commentname);
            commentcontent=itemView.findViewById(R.id.commentcontent);
            likecount=itemView.findViewById(R.id.likecount);
            commentunlike=itemView.findViewById(R.id.commentunlike);
            replylist=itemView.findViewById(R.id.replylist);
            viewreply=itemView.findViewById(R.id.viewreply);
            hidepely=itemView.findViewById(R.id.hidereply);
            innerrepliesll=itemView.findViewById(R.id.innerrepliesll);

        }
    }

    public void JSONSenderVolleycomment(JSONObject lstrmdt, String url, final ViewHolder holder, final int position, ImageView commentlike, ImageView commentunlike, TextView likecount, boolean replylikeunlike, ArrayList<Model_replycomment> replymodelInner,boolean commentlikeunlike,String postid) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        //Log.d("555555", "login" + url);
        //Log.d("replylikeunlike", "::::" + replylikeunlike+":::"+commentlikeunlike);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("JSONSenderVolleylogin", "---" + response);
                        try {
                            if (response.length()!=0) {

                                if (response.has("status")) {
                                    String status = response.getString("status");
                                    if (status.equals("1")) {
                                        String commentcount = response.getString("count");
                                        if (status.equals("1")) {
                                            // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                            commenttxt.getText().clear();
                                            countcomment.setText(commentcount + " " + "Comments");
                                            //sm.sendDatacomment(commentcount);

                                            if (fragment_data_send != null)
                                                fragment_data_send.onDataSent(String.valueOf(commentcount));


                                            JSONObject repltarray = response.getJSONObject("comment");
                                            Model_replycomment reply=new Model_replycomment();

                                            String commentid = repltarray.getString("_id");
                                            String postId = repltarray.getString("postId");

                                            if (repltarray.has("userId")) {
                                                JSONObject object = repltarray.getJSONObject("userId");
                                                String _id = object.getString("_id");
                                               // Log.d("userid", "::" + _id);
                                                reply.setUserid(_id);
                                                if (object.has("name")) {
                                                    reply.setName(object.getString("name"));
                                                    //userlistL.add(object.getString("name"));
                                                    reply.setProfile(object.getString("profilePic"));
                                                } else {
                                                    reply.setName("name");
                                                    //userlistL.add("name");
                                                    reply.setProfile(" ");
                                                }
                                            } else {
                                                reply.setName("name");
                                                //userlistL.add("name");
                                                reply.setProfile(" ");
                                            }

                                            reply.setContent(repltarray.getString("comment"));
                                            reply.setIsowner("true");
                                            reply.setPostid(postId);
                                            reply.setCommentid(commentid);
                                           // Log.d("commentsreplyyy","::"+repltarray.getString("comment"));

                                            reply.setLikecount("0");
                                            reply.setLiketype("false");
/*
                                            replymodelInner.add(reply);
                                            replycomment(holder,position+1);

*/

                                            jsoncomments(comment_url + postid,holder);
                                        }
                                    }
                                }
                                else if (response.has("liked")) {
                                    //Log.d("positionnlikesres",":::"+position);
                                    String message = response.getString("message");
                                    String liked = response.getString("liked");
                                    String count = response.getString("count");
                                    if (replylikeunlike && !commentlikeunlike){
                                        commentunlike.setVisibility(View.VISIBLE);
                                        commentlike.setVisibility(View.GONE);
                                        likecount.setText(count);
                                        replymodelInner.get(position).setLikecount(count);
                                        replymodelInner.get(position).setLiketype(liked);
                                    }else if (!commentlikeunlike && !replylikeunlike){
                                        holder.commentunlike.setVisibility(View.VISIBLE);
                                        holder.commentlike.setVisibility(View.GONE);
                                        holder.likecount.setText(count);
                                        likecommentcount.set(position, count);
                                        likecommenttype.set(position, liked);
                                        //mainAdapter.notifyDataSetChanged();
                                    }
                                }else {
                                    String unlikemessage = response.getString("message");
                                    String unlikecount = response.getString("count");
                                    String unlikecounttype = response.getString("unliked");

                                    if (!replylikeunlike && !commentlikeunlike){
                                        commentunlike.setVisibility(View.GONE);
                                        commentlike.setVisibility(View.VISIBLE);
                                        likecount.setText(unlikecount);
                                        replymodelInner.get(position).setLikecount(unlikecount);
                                        replymodelInner.get(position).setLiketype(unlikecounttype);
                                    }else if (commentlikeunlike && !replylikeunlike){
                                        holder.commentunlike.setVisibility(View.GONE);
                                        holder.commentlike.setVisibility(View.VISIBLE);
                                        holder.likecount.setText(unlikecount);
                                        likecommentcount.set(position, unlikecount);
                                        likecommenttype.set(position, unlikecounttype);
                                    }

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
                               // Log.d("body", "---" + body);
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

    public void guestures(View view, int position, ViewHolder holder, String username, String commentpostid, String commentid, String isonwer,String userid,ArrayList<Model_Commentlist> commentmodeldata,ArrayList<Model_replycomment> replymodelInner,boolean commentreplycondition){
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
                   // Log.d("singlelcick","::");
                    commenttxt.setHint(username);
                    commenttxt.setActivated(true);
                    commenttxt.setPressed(true);
                    commenttxt.hasFocus();
                    commenttxt.setCursorVisible(true);
                    commenttxt.requestFocus();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(commenttxt, InputMethodManager.SHOW_IMPLICIT);

                    oftersend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commentreply(holder,position,commentpostid,commentid);
                        }
                    });

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    //Log.d("longclick","::"+"::::"+"::::"+position);
                    if (isonwer.equals("true")) {
                        popup(position,commentpostid,commentid,commentmodeldata,replymodelInner,commentreplycondition);
                    } else {
                        reportpopup(position,userid,commentid);
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

    public void popup(int position, String commentpostid, String commentid, ArrayList<Model_Commentlist> commentmodeldata, ArrayList<Model_replycomment> replymodelInner, boolean commentreplycondition) {

        android.app.AlertDialog.Builder builder;
        final Context mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.commentdeletepopup, null);

        Button delete = (Button) layout.findViewById(R.id.delete);
        builder = new android.app.AlertDialog.Builder(mContext);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginS = "{\"_id\":\"" + commentid + "\",\"postId\":\"" + commentpostid + "\"}";
                //Log.d("delete comment", "---" + loginS);
                String url=deletcomment_url;
                JSONObject lstrmdt;
                try {
                    lstrmdt = new JSONObject(loginS);
                    //Log.d("jsnresponse....", "---" + loginS);
                    JSONSenderVolleydelete(lstrmdt,url,position,commentmodeldata,replymodelInner,commentreplycondition);
                } catch (JSONException ignored) {
                }
            }
        });
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();
    }

    public void reportpopup(int position, String userid, String commentid) {
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

                String loginS = "{\"userId\":\"" + userid + "\",\"commentId\":\"" + commentid + "\",\"reason\":\"" + selectedItemText + "\",\"description\":\"" + desE.getText().toString() + "\"}";
                //Log.d("jsnresponse reason", "---" + loginS);
                String url = reportcomment_url;
                JSONObject lstrmdt;
                try {
                    lstrmdt = new JSONObject(loginS);
                    //Log.d("jsnresponse....", "---" + loginS);

                    JSONSenderVolleydelete(lstrmdt,url,position, null, null, false);

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

    public void JSONSenderVolleydelete(JSONObject lstrmdt, String url, int position, ArrayList<Model_Commentlist> commentmodeldata, ArrayList<Model_replycomment> replymodelInner, boolean commentreplycondition) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        //Log.d("555555", "delete" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("jsondelete", "---" + response);
                        if (response.length()!=0){
                            try {
                                String message=response.getString("message");
                                String status=response.getString("status");
                                if (status.equals("1")){
                                    String commentcount=response.getString("count");
                                    countcomment.setText(commentcount+" "+"Comments");
                                    if(fragment_data_send!=null)
                                        fragment_data_send.onDataSent(String.valueOf(commentcount));

                                    if (commentreplycondition) {
                                        commentmodeldata.remove(position);
                                        dialog.dismiss();
                                        mainAdapter.notifyDataSetChanged();
                                    }else if (!commentreplycondition){
                                        replymodelInner.remove(position);
                                        dialog.dismiss();
                                        mainAdapter.notifyDataSetChanged();
                                    }
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
                                //Log.d("body", "---" + body);
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

    public void commentreply(ViewHolder holder, int position, String commentpostid, String commentid){
        final String commenttxtS = commenttxt.getText().toString();

        if (commenttxtS.isEmpty()) {
            Toast.makeText(context, "Please enter the All Fields", Toast.LENGTH_SHORT).show();

        } else {

            String loginS = "{\"postId\":\"" + commentpostid + "\",\"commentId\":\"" + commentid + "\",\"comment\":\"" + commenttxtS + "\"}";
           // Log.d("jsnresponse reply", "---" + loginS);
            String url=replycomment_url;
            JSONObject lstrmdt;

            try {
                lstrmdt = new JSONObject(loginS);
                //Log.d("jsnresponse....", "---" + loginS);
                //loader(dialog);
                JSONSenderVolleycomment(lstrmdt,url,holder,position,null,null,null,false,null,false,commentpostid);

            } catch (JSONException ignored) {
            }
        }
    }
    private void jsoncomments(String url, ViewHolder holder) {
        mainmodel=new ArrayList<>();
        replymodel=new ArrayList<>();

        userlistL=new ArrayList<>();
        userlistL.clear();
        likecommentcount=new ArrayList<>();
        likecommenttype=new ArrayList<>();


        likecommentcount.clear();
        likecommenttype.clear();

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        //Log.d("Responsestoredata0000", response.toString());

                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            for (int j = 0; j < response.length() ; j++ ) {
                                //Log.d("lengtharayyy",  ":::"+j);

                                Model_Commentlist ts=new Model_Commentlist();
                                try {
                                    JSONObject jItem = response.getJSONObject(j);


                                    if (jItem.has("followingId")){
                                        JSONObject followinobj=jItem.getJSONObject("followingId");
                                        String username=followinobj.getString("name");
                                        userlistL.add(username);
                                        modelusers=new ArrayList<Model_commentuserlst>();
                                        for (int i=0;i<userlistL.size();i++) {
                                            Model_commentuserlst model_deposit = new Model_commentuserlst(userlistL.get(i));
                                            modelusers.add(model_deposit);
                                        }

                                        commentatlist.setVisibility(View.VISIBLE);
                                        commentuserAdapter = new Adapter_commentusrlist(context, modelusers);
                                        // Binds the Adapter to the ListView
                                        commentatlist.setAdapter(commentuserAdapter);
                                        commenttxt.setTextColor(Color.parseColor("#000000"));


                                    }else {
                                        if (jItem.has("_id")) {
                                            JSONObject _idarray = jItem.getJSONObject("_id");
                                            if (_idarray.length() != 0) {
                                                String commentid = _idarray.getString("_id");
                                                String likeUserComments = _idarray.getString("likeUserComments");
                                                String isOwner = _idarray.getString("isOwner");
                                                String postId = _idarray.getString("postId");

                                                if (_idarray.has("user")) {
                                                    JSONObject object = _idarray.getJSONObject("user");
                                                    String _id = object.getString("_id");
                                                    //Log.d("userid", "::" + _id);
                                                    ts.setUserid(_id);
                                                    if (object.has("name")) {
                                                        ts.setName(object.getString("name"));
                                                        //userlistL.add(object.getString("name"));
                                                        ts.setProfile(object.getString("profilePic"));
                                                    } else {
                                                        ts.setName("name");
                                                        //userlistL.add("name");
                                                        ts.setUserid(" ");
                                                        ts.setProfile(" ");
                                                    }
                                                } else {
                                                    ts.setName("name");
                                                    //userlistL.add("name");
                                                    ts.setProfile(" ");
                                                }
                                                ts.setContent(_idarray.getString("comment"));
                                                ts.setCommentid(commentid);
                                                ts.setIsowner(isOwner);
                                                ts.setPostid(postId);
                                                //ts.setLikecount(jItem.getString("likecomments"));
                                                //ts.setLiketype(likeUserComments);
                                                likecommentcount.add(_idarray.getString("likecomments"));
                                                likecommenttype.add(likeUserComments);
                                            }
                                        }
                                        if (jItem.has("replies")) {
                                            JSONArray replies = jItem.getJSONArray("replies");
                                            replymodel=new ArrayList<>();
                                            if (replies.length()!=0) {
                                                for (int k = 0; k < replies.length(); k++) {
                                                    Model_replycomment reply=new Model_replycomment();
                                                    JSONObject repltarray = replies.getJSONObject(k);
                                                    String commentid = repltarray.getString("_id");
                                                    String likeUserComments = repltarray.getString("likeUserComments");
                                                    String isOwner = repltarray.getString("isOwner");
                                                    String postId = repltarray.getString("postId");

                                                    if (repltarray.has("user")) {
                                                        JSONObject object = repltarray.getJSONObject("user");
                                                        String _id = object.getString("_id");
                                                        //Log.d("userid", "::" + _id);
                                                        reply.setUserid(_id);
                                                        if (object.has("name")) {
                                                            reply.setName(object.getString("name"));
                                                            //userlistL.add(object.getString("name"));
                                                            reply.setProfile(object.getString("profilePic"));
                                                        } else {
                                                            reply.setName("name");
                                                            //userlistL.add("name");
                                                            ts.setUserid(" ");
                                                            reply.setProfile(" ");
                                                        }
                                                    } else {
                                                        reply.setName("name");
                                                        //userlistL.add("name");
                                                        reply.setProfile(" ");
                                                    }

                                                    reply.setContent(repltarray.getString("comment"));
                                                    reply.setIsowner(isOwner);
                                                    reply.setPostid(postId);
                                                    reply.setCommentid(commentid);

                                                    reply.setLikecount(repltarray.getString("likecomments"));
                                                    reply.setLiketype(likeUserComments);

                                                    replymodel.add(reply);
                                                }
                                            }
                                        }

                                        ts.setReplymodel(replymodel);
                                        mainmodel.add(ts);

                                        //Log.d("replymodel","::"+replymodel.size());
                                        commentlst.setHasFixedSize(true);
                                        // use a linear layout manager
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                                        commentlst.setLayoutManager(mLayoutManager);
                                        // model = new ArrayList<>();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            mainAdapter = new Adapter_commentlist(mainmodel, likecommentcount, likecommenttype,fragment_data_send, context);
                            commentlst.setAdapter(mainAdapter);
                            mainAdapter.notifyDataSetChanged();

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
                                            //  Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                                       // Log.d("bodyerror", "---" + bodyerror);
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
                headers.put("Authorization",proftoken);

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
