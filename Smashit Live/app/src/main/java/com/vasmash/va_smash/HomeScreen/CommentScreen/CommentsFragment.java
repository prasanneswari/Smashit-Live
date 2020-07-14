package com.vasmash.va_smash.HomeScreen.CommentScreen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentAdapters.Adapter_CommentImg;
import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentAdapters.Adapter_commentlist;
import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentAdapters.Adapter_commentusrlist;
import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentModels.Model_Commentlist;
import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentModels.Model_replycomment;
import com.vasmash.va_smash.HomeScreen.ModelClass.Model_commentuserlst;
import com.vasmash.va_smash.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;
import com.vasmash.va_smash.login.fragments.LoginFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.claimedtopnav;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.claimhometopnav;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.clamiedget;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.countDownTimer;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.progressBarView;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentAdapters.Adapter_commentlist.mainmodel;
import static com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment.privious_player;
import static com.vasmash.va_smash.SearchClass.SearchVerticalData.commentsearchclick;
import static com.vasmash.va_smash.VASmashAPIS.APIs.comment_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.getfollowing_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.posteomment_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.replycomment_url;


public class CommentsFragment extends BottomSheetDialogFragment {

   /* public CommentsFragment() {
        // Required empty public constructor
    }*/

    //static public SendMessagecomment sm;
    Fragment_Data_Send fragment_data_send;


    public static TextView countcomment;
    static public RecyclerView commentlst,commentimg;
    static public EditText commenttxt;
    ImageView commentprofile,commentsend;
    public static ImageView oftersend;
    Button close;
    public static ImageView commentat;
    LinearLayout rootview;

    ArrayList<Model_Commentlist> model;
    static public Adapter_commentlist mainAdapter;
    Adapter_CommentImg commentimgAdapter;
    private RequestQueue mQueue;
    public static ArrayList<Model_Commentlist> tags;
    public static ArrayList<Model_replycomment> replymodel;
    static public ArrayList<String> userlistL;
    static public ArrayList<String> likecommentcount;
    static public ArrayList<String> likecommenttype;



    public static String proftoken;
    public static RecyclerView commentuserlst;
    public static ListView commentatlist;
    public static Adapter_commentusrlist commentuserAdapter;
    //static public SendMessagecomment sm;

    static public String commentcount="null";
    static public boolean commentatval;
    public static ArrayList<Model_commentuserlst> modelusers;
    boolean textFocus = false; //define somewhere globally in the class

/*
    int[] commenticons = new int[]{
            R.drawable.commentimg1,
            R.drawable.commentimg2,
            R.drawable.commentimg3,
            R.drawable.commentimg4,
            R.drawable.commentimg5,
            R.drawable.commentimg6,
            R.drawable.commentimg7,
            R.drawable.commentimg8
    };
*/

    String commentuser,postid,type;

    LottieAnimationView animationView;

    public CommentsFragment( String commentuser,String postid,String type,Fragment_Data_Send fragment_data_send) {
        this.commentuser=commentuser;
        this.postid=postid;
        this.type=type;
        this.fragment_data_send=fragment_data_send;

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final View contentView = View.inflate(getContext(), R.layout.fragment_comments, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent())
                .getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) contentView.getParent()).setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        // params.setMargins(0, 200, 0, 0);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BottomSheetDialog d = (BottomSheetDialog) dialog;
                        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                },0);
            }
        });
        // new KeyboardUtil(getActivity(), contentView,dialog);
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        FrameLayout bottomSheet = (FrameLayout) dialog.getWindow().findViewById(R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bottomsheetcurve);
        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(getActivity());
        proftoken = phoneauthshard.getString("token", "null");

        countcomment=contentView.findViewById(R.id.countcomments);
        commentlst=contentView.findViewById(R.id.commentlst);
        commentimg=contentView.findViewById(R.id.commentimg);
        commenttxt=contentView.findViewById(R.id.commenttxt);
        // commentprofile=contentView.findViewById(R.id.commentprofile);
        commentat=contentView.findViewById(R.id.commentat);
        //  commentwink=contentView.findViewById(R.id.commentwink);
        commentsend=contentView.findViewById(R.id.commentsend);
        close=contentView.findViewById(R.id.close);
        commentuserlst=contentView.findViewById(R.id.commentlst);
        rootview=contentView.findViewById(R.id.root_view);
        commentatlist=contentView.findViewById(R.id.commentatlist);
        oftersend=contentView.findViewById(R.id.oftersend);

        countcomment.setText(commentuser+" "+"Comments");
//        commenttxt.setTextColor(Color.parseColor("#fff"));


        rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //commenttxt.onEditorAction(EditorInfo.IME_ACTION_DONE);
                InputMethodManager im=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(commenttxt.getWindowToken(),0);

                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BottomSheetDialog d = (BottomSheetDialog) dialog;
                                FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                        },0);
                    }
                });

            }
        });


        //in onFinishInflate() or somewhere
        commenttxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                commenttxt.onTouchEvent(event);

                if(!textFocus) {
                    commenttxt.setSelection(commenttxt.getText().length());
                    textFocus = true;
                }
                return true;
            }
        });

        commenttxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                textFocus = false;
            }
        });



        jsoncomments(comment_url+postid,dialog);
/*
        // progressBarView.cancelLongPress();
        if (countDownTimer!=null) {
            countDownTimer.cancel();
        }
*/

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("countdowm111","::"+claimedtopnav+":::"+clamiedget+":::"+claimhometopnav);

                //Log.d("commentsearchclick",":::"+commentsearchclick);
                if (!commentsearchclick.equals("null")){
                    dialog.dismiss();
                }else {
                    //Log.d("entrincommentclose","::::");

                    if (claimedtopnav.equals("true")) {
                        progressBarView.setProgress(100);
                        privious_player.setPlayWhenReady(true);
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        dialog.dismiss();

                    } else if (clamiedget.equals("true")) {
                        progressBarView.setProgress(100);
                        privious_player.setPlayWhenReady(true);
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        dialog.dismiss();

                    } else if (claimhometopnav.equals("false")) {
                        progressBarView.setProgress(0);
                        privious_player.setPlayWhenReady(true);

                        if (countDownTimer != null) {
                            countDownTimer.start();
                        }
                        dialog.dismiss();

                    } else if (claimhometopnav.equals("true")) {
                        progressBarView.setProgress(100);
                        privious_player.setPlayWhenReady(true);
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        dialog.dismiss();
                    } else {
                        if (type.equals("0")){
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                            }
                        }else {
                            if (countDownTimer != null) {
                                countDownTimer.start();
                                dialog.dismiss();
                                privious_player.setPlayWhenReady(true);
                            } else {
                                dialog.dismiss();
                            }
                        }
                    }
                }
            }
        });


        //commentimg
        commentimg.setHasFixedSize(true);
        // use a linear layout manager
        // RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                try {
                    //Log.d("smoothScroller",  "smoothScroller" + smoothScroller);

                    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                        private static final float SPEED = 3500f;// Change this value (default=25f)

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return SPEED / displayMetrics.densityDpi;
                        }
                    };
                    smoothScroller.setTargetPosition(position);
                    startSmoothScroll(smoothScroller);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        commentimg.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
       /* model = new ArrayList<>();

        for (int i1 = 0; i1 < commentname.length; i1++) {
            Model_Commentlist mainMode = new Model_Commentlist(commentname[i1], commentcontent[i1]);
            model.add(mainMode);
        }*/
/*
        commentimgAdapter = new Adapter_CommentImg(getActivity(), commenticons);
        commentimg.setAdapter(commentimgAdapter);
*/


        oftersend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String commenttxtS = commenttxt.getText().toString();

                if (commenttxtS.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter the All Fields", Toast.LENGTH_SHORT).show();

                } else {

                    String loginS = "{\"postId\":\"" + postid + "\",\"comment\":\"" + commenttxtS + "\"}";
                    //Log.d("jsnresponse login", "---" + loginS);
                    String url=posteomment_url;
                    JSONObject lstrmdt;

                    try {
                        lstrmdt = new JSONObject(loginS);
                        //Log.d("jsnresponse....", "---" + loginS);
                        loader(dialog);
                        JSONSenderVolleycommentsend(lstrmdt,url,dialog);

                    } catch (JSONException ignored) {
                    }
                }
            }
        });
        commentat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentatval=true;
                addImageBetweentext(commentat.getDrawable());
                //  new CustomBottomSheetDialogFragment().show(getFragmentManager(),"Dialog");
                jsoncomments(getfollowing_url, dialog);

/*
                CommentuserListFragment fragment = new CommentuserListFragment();
                fragment.show(((AppCompatActivity)getActivity()).getSupportFragmentManager(), "bottom_sheet");
*/
              /*  commentatlist.setVisibility(View.VISIBLE);
                commentatlist.setHasFixedSize(true);
                // use a linear layout manager
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                commentatlist.setLayoutManager(mLayoutManager);


                commentuserAdapter = new Adapter_commentusrlist(userlistL, getActivity());
                commentatlist.setAdapter(commentuserAdapter);*/


            }
        });

/*
        commenttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oftersend.setVisibility(View.VISIBLE);
                commentsend.setVisibility(View.GONE);

*/
/*
                new CustomBottomSheetDialogFragment().show(getFragmentManager(),"Dialog");
                InputMethodManager im=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(commenttxt.getWindowToken(),0);
*//*


            }
        });
*/


        commenttxt.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (commenttxt.getText().toString().trim().equals("")){
                    oftersend.setVisibility(View.GONE);
                    commentsend.setVisibility(View.VISIBLE);
                    commentatlist.setVisibility(View.GONE);

                }else {

                    if (s.equals(".")){
                        commentatlist.setVisibility(View.VISIBLE);
                        oftersend.setVisibility(View.VISIBLE);
                        commentsend.setVisibility(View.GONE);
                        commenttxt.setTextColor(Color.parseColor("#26282F"));
                    }else {
                        commenttxt.setTextColor(Color.parseColor("#26282F"));
                        oftersend.setVisibility(View.VISIBLE);
                        commentsend.setVisibility(View.GONE);
                    }

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });



    }

    private ArrayList<Model_commentuserlst> getasserts(){
        ArrayList<Model_commentuserlst> model=new ArrayList<Model_commentuserlst>();
        for (int i=0;i<userlistL.size();i++){
            Model_commentuserlst model_deposit=new Model_commentuserlst(userlistL.get(i));
            model.add(model_deposit);
        }
        return model;
    }

    private void addImageBetweentext(Drawable drawable) {

        drawable .setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        int selectionCursor = commenttxt.getSelectionStart();
        commenttxt.getText().insert(selectionCursor, ".");
        selectionCursor = commenttxt.getSelectionStart();

        SpannableStringBuilder builder = new SpannableStringBuilder(commenttxt.getText());
        builder.setSpan(new ImageSpan(drawable), selectionCursor - ".".length(), selectionCursor,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        commenttxt.setText(builder);
        commenttxt.setSelection(selectionCursor);
    }

    private void jsoncomments(String url, Dialog dialog) {
        loader(dialog);

        // prepare the Request
        //Log.d("comment_url+getid", url);

        tags=new ArrayList<>();
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
                        animationView.cancelAnimation();
                        animationView.setVisibility(View.GONE);

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
                                        commentuserAdapter = new Adapter_commentusrlist(getActivity(), modelusers);
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
                                        replymodel=new ArrayList<>();
                                        if (jItem.has("replies")) {
                                            JSONArray replies = jItem.getJSONArray("replies");


                                            if (replies.length()!=0) {
                                                for (int k = 0; k < replies.length(); k++) {
                                                    Model_replycomment reply=new Model_replycomment();
                                                    reply.setPosition(String.valueOf(k));
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
                                                    //Log.d("commentsreplyyy","::"+repltarray.getString("comment"));

                                                    reply.setLikecount(repltarray.getString("likecomments"));
                                                    reply.setLiketype(likeUserComments);

                                                    replymodel.add(reply);
                                                }
                                            }
                                        }

                                        ts.setReplymodel(replymodel);
                                        tags.add(ts);

                                        //Log.d("tagssss","::"+tags.size());
                                        commentlst.setHasFixedSize(true);
                                        // use a linear layout manager
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                        commentlst.setLayoutManager(mLayoutManager);
                                        mainAdapter = new Adapter_commentlist(tags, likecommentcount, likecommenttype,fragment_data_send, getActivity());
                                        commentlst.setAdapter(mainAdapter);
                                        mainAdapter.notifyDataSetChanged();


                                        // model = new ArrayList<>();
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
                                            animationView.cancelAnimation();
                                            animationView.setVisibility(View.GONE);
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
                                        //Log.d("bodyerror", "---" + bodyerror);
                                        JSONObject obj = new JSONObject(bodyerror);
                                        if (obj.has("errors")) {
                                            animationView.cancelAnimation();
                                            animationView.setVisibility(View.GONE);
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
    public void JSONSenderVolleycommentsend(JSONObject lstrmdt, String url,Dialog dialog) {
        // Log.d("---reqotpurl-----", "---" + login_url);
       // Log.d("555555", "login" + url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("JSONSenderVolleylogin", "---" + response);
                        animationView.cancelAnimation();
                        animationView.setVisibility(View.GONE);

                        try {
                            if (response.length()!=0) {
                                String message = response.getString("message");
                                String status = response.getString("status");
                                commentcount = response.getString("count");
                                if (status.equals("1")) {
                                    commenttxt.getText().clear();
                                    countcomment.setText(commentcount + " " + "Comments");
                                    //sm.sendDatacomment(commentcount);

                                    if(fragment_data_send!=null)
                                        fragment_data_send.onDataSent(String.valueOf(commentcount));

                                    jsoncomments(comment_url + postid, dialog);

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
                                //Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                String id = null;
                                if (obj.has("id")) {
                                    id = obj.getString("id");
                                }

                                if (obj.has("errors")) {
                                    animationView.cancelAnimation();
                                    animationView.setVisibility(View.GONE);

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
                animationView.cancelAnimation();
                animationView.setVisibility(View.GONE);
            }
        });

    }

    public <T> void addToRequestQueue(Request<T> req) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(getActivity());
        }
        mQueue.add(req);
    }
    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        // Log.d("cncelss","::::");
        InputMethodManager im=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(commenttxt.getWindowToken(),0);

        if (!commentsearchclick.equals("null")){
            dialog.dismiss();
        }else {
            //Log.d("entrincommentfinish","::::");
            if (claimedtopnav.equals("true")) {
                progressBarView.setProgress(100);
                privious_player.setPlayWhenReady(true);

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    dialog.dismiss();
                }
            } else if (clamiedget.equals("true")) {
                progressBarView.setProgress(100);
                privious_player.setPlayWhenReady(true);

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    dialog.dismiss();
                }
            } else if (claimhometopnav.equals("false")) {
                progressBarView.setProgress(0);
                privious_player.setPlayWhenReady(true);

                if (countDownTimer != null) {
                    countDownTimer.start();
                    dialog.dismiss();
                }
            } else if (claimhometopnav.equals("true")) {
                progressBarView.setProgress(100);
                privious_player.setPlayWhenReady(true);

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    dialog.dismiss();
                }
            } else {
                //Log.d("entringelse","::::");
                if (type.equals("0")){
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                    }
                }else {
                    if (countDownTimer != null) {
                        countDownTimer.start();
                        dialog.dismiss();
                        privious_player.setPlayWhenReady(true);
                    } else {
                        dialog.dismiss();
                    }
                }
            }
        }
        // Toast.makeText("tag", "CANCEL", Toast.LENGTH_SHORT).show();
    }

/*
    public interface SendMessagecomment {
        void sendDatacomment(String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (sm != null ) {
                sm = (SendMessagecomment) getActivity();
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }
*/

    public void loader(Dialog dialog){
        //lotte loader
        animationView = (LottieAnimationView) dialog.findViewById(R.id.animation_view_1);
        animationView.setAnimation("data.json");
        animationView.loop(true);
        animationView.playAnimation();
        animationView.setVisibility(View.VISIBLE);
    }

}

