package com.vasmash.va_smash.HomeScreen.homeadapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.vasmash.va_smash.BottmNavigation.TopNavigationview;
import com.vasmash.va_smash.HomeScreen.ModelClass.Homescreen_model;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.ProfileScreen.OtherprofileActivity;
import com.vasmash.va_smash.ProfileScreen.ProfileActivity;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.SearchData;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;
import com.vasmash.va_smash.VaContentScreen.VAStoreActivity;
import com.vasmash.va_smash.login.fragments.LoginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.countDownTimer;
import static com.vasmash.va_smash.VASmashAPIS.APIs.share_url;




public class FragmentInner extends RecyclerView.Adapter<FragmentInner.CustomViewHolder> /*implements CommentsFragment.SendMessagecomment*/ {
    Dialog dialog;

    ArrayList<Homescreen_model> mainmodels;
    ArrayList<Tags> des;

    Context context;

    static public String getid,postid,likescoutn;
    private RequestQueue mQueue;
    private String proftoken;
    int pos;
    ViewDialog viewDialog;


    String userprofilepic,username,sharecount,claimtype;
    // variable to track event time
    private long mLastClickTime = 0;

    public FragmentInner(ArrayList<Homescreen_model> mainmodels,ArrayList<Tags> desL, Context context) {
        this.mainmodels = mainmodels;
        this.des = desL;
        this.context = context;

    }

    @NonNull
    @Override
    public FragmentInner.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homesceen_modelview, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        FragmentInner.CustomViewHolder viewHolder = new FragmentInner.CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FragmentInner.CustomViewHolder holder, final int position) {
        final Homescreen_model item = mainmodels.get(position);
        pos=position;
        holder.setIsRecyclable(false);
        mQueue = Volley.newRequestQueue(context.getApplicationContext());
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(context);
        proftoken = phoneauthshard.getString("token", "null");
        viewDialog = new ViewDialog((Activity) context);


        postid=mainmodels.get(position).getPostid();
        //commentuser=mainmodels.get(position).getComments();
        username=mainmodels.get(position).getUsername();
        //sharecount=shareL.get(position);
        sharecount=mainmodels.get(position).getShare();


        userprofilepic=mainmodels.get(position).getUserprofilepic();
        Log.d("profilepichome",":::"+userprofilepic);
        if (userprofilepic !=null){
            Picasso.with(context).load(userprofilepic).into(holder.otherprofile);
/*
            Glide.with(context)
                    .load(userprofilepic)
                    .into(holder.otherprofile);
*/

        }else {
            // Picasso.with(context).load(R.drawable.uploadpictureold).into(holder.otherprofile);
            holder.otherprofile.setImageResource(R.drawable.uploadpictureold);
        }

        holder.homename.setText(username);
        holder.description.setText(mainmodels.get(position).getDescription());
        if (des.get(position).getName()!=null) {
            holder.description.setVisibility(View.VISIBLE);
            holder.hometags.setText(/*holder.hometags.getText() + " " +*/des.get(position).getName());
        }else {
            holder.description.setVisibility(View.GONE);
        }
/*
        holder.shareimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (proftoken.equals("null")){
                    popup();
                }else {
                    postid = mainmodels.get(position).getPostid();
                    createreflink(postid,holder,position);
                }

            }
        });
*/

        holder.vastore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (proftoken.equals("null")){
                    popup();
                }else {
                    if (countDownTimer!=null) {
                        countDownTimer.cancel();
                    }

                    Intent intent = new Intent(context, VAStoreActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        holder.otherprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid=mainmodels.get(position).getUserid();
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (proftoken.equals("null")){
                    popup();
                }else {
                    if (countDownTimer!=null) {
                        countDownTimer.cancel();
                    }

                    Intent intent = new Intent(context, OtherprofileActivity.class);
                    intent.putExtra("posteduserid", userid);
                    context.startActivity(intent);
                    Log.e("posteduserid", userid);
                }
            }
        });
        holder.homename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid=mainmodels.get(position).getUserid();
                Intent intent = new Intent(context, OtherprofileActivity.class);
                intent.putExtra("posteduserid", userid);
                context.startActivity(intent);

            }
        });

    }
/*
    @Override
    public int getItemCount() {
        return mainmodels.size();
    }
*/

    @Override
    public int getItemCount() {
        return (null != mainmodels ? mainmodels.size() : 0);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void createreflink(String postid, CustomViewHolder holder, int position){

        Task<ShortDynamicLink> dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(mainmodels.get(position).getFile()+"?"+postid+"-"+"true"))
                .setDomainUriPrefix("https://sh.vasmash.com")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.smash").build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("Example of a Dynamic Link")
                                .setDescription("This link works whether the app is installed or not!")
                                .build())
                .buildShortDynamicLink().addOnCompleteListener((Activity) context, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Intent i = new Intent(android.content.Intent.ACTION_SEND);
                            i.setType("text/plain");
                            String sub="\n Hi, have a look. I found this crazy stuff in VA-Smash!!\n";
                            String shareMessage= sub+" " +"\n"+ shortLink +"\n"+" "+ "\n I am enjoying and earning. Install to join me.\n\n";
                            i.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage.toString());
                            context.startActivity(Intent.createChooser(i, "Share Via"));
                            jsoneashare(share_url + postid, holder, position);

                        } else {
                            // Error
                            // ...
                        }
                    }
                });

    }

    public void popup() {

        android.app.AlertDialog.Builder builder;
        final Context mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.guestpopup, null);


        TextView okbtn = (TextView) layout.findViewById(R.id.okbtn);
        TextView regbtn = (TextView) layout.findViewById(R.id.register);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, LoginFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.d_round_white_background));
        dialog.show();

    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView description,homename,hometags,song,sharetxt;
        ImageView shareimg,vastore,giftimg;
        CircleImageView otherprofile;


        public CustomViewHolder(View rootView) {
            super(rootView);

            //shareimg =(ImageView) rootView.findViewById(R.id.shareimg);
            homename=rootView.findViewById(R.id.homename);
            hometags=rootView.findViewById(R.id.hometags);
           // sharetxt=rootView.findViewById(R.id.share);
            vastore=rootView.findViewById(R.id.vastore);
            otherprofile=rootView.findViewById(R.id.otherprofile);
            description=rootView.findViewById(R.id.homecontent);
            //giftimg=rootView.findViewById(R.id.giftimg);
            song=rootView.findViewById(R.id.song);
            song.setText("Original Sound - Ski_ssj"+"  "+"Original Sound - Ski_ssj");
            song.setSelected(true);

        }

    }


    public void shareItem(final String url) {
        Picasso.with(context).load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                    context.startActivity(Intent.createChooser(i, "Share Image"));
                }catch (Exception e){


                }
            }
            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void jsoneashare(String earningpoints_url, final CustomViewHolder holder, final int position) {
        // prepare the Request
        Log.d("earning share", earningpoints_url);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, earningpoints_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Responses share", response.toString());
                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            try {
                                String sharepoints=response.getString("count");
                                mainmodels.get(position).setShare(sharepoints);
                                holder.sharetxt.setText(sharepoints);
                            } catch (JSONException e) {
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
                                        Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
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

                                case 404:
                                    try {
                                        String bodyerror = new String(error.networkResponse.data,"UTF-8");
                                        Log.d("bodyerror", "---" + bodyerror);
                                        JSONObject obj = new JSONObject(bodyerror);
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
    }



    public abstract static class DoubleClickListener implements View.OnClickListener {
        private static final long DEFAULT_QUALIFICATION_SPAN = 200;
        private boolean isSingleEvent;
        private long doubleClickQualificationSpanInMillis;
        private long timestampLastClick;
        private Handler handler;
        private Runnable runnable;

        public DoubleClickListener() {
            doubleClickQualificationSpanInMillis = DEFAULT_QUALIFICATION_SPAN;
            timestampLastClick = 0;
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (isSingleEvent) {
                        onSingleClick();
                    }
                }
            };
        }

        @Override
        public void onClick(View v) {
            if((SystemClock.elapsedRealtime() - timestampLastClick) < doubleClickQualificationSpanInMillis) {
                isSingleEvent = false;
                handler.removeCallbacks(runnable);
                onDoubleClick();
                return;
            }

            isSingleEvent = true;
            handler.postDelayed(runnable, DEFAULT_QUALIFICATION_SPAN);
            timestampLastClick = SystemClock.elapsedRealtime();
        }

        public abstract void onDoubleClick();
        public abstract void onSingleClick();
    }
    public void paginationsave(ArrayList<Homescreen_model> pagemodel){
        for (Homescreen_model model : pagemodel){
            mainmodels.add(model);
        }
        notifyDataSetChanged();
    }

}
