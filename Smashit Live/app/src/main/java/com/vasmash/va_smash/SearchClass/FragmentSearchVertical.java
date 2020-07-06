package com.vasmash.va_smash.SearchClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.ProfileScreen.OtherprofileActivity;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Trading;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;
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

import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.countDownTimer;
import static com.vasmash.va_smash.SearchClass.SearchVerticalData.soundname;
import static com.vasmash.va_smash.SearchClass.SearchVerticalData.soundurl;
import static com.vasmash.va_smash.VASmashAPIS.APIs.checkstatus_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.share_url;

/**
 * Created by : youngkaaa on 2017/2/22.
 * Contact me : 645326280@qq.com
 */


public class FragmentSearchVertical extends RecyclerView.Adapter<FragmentSearchVertical.CustomViewHolder> {

    Context context;

    private RequestQueue mQueue;
    private String proftoken;
    private long mLastClickTime = 0;
    ArrayList<Model_Trading> mainmodels;
    ArrayList<Tags> dess;
    String postid,usernamesearchclick="null";
    ViewDialog viewDialog;
    //ArrayList<String> shareL;

    public FragmentSearchVertical(ArrayList<Model_Trading> mainmodels/*, ArrayList<Tags> tags*//*,ArrayList<String> shareL*/, Context context) {
        this.mainmodels = mainmodels;
        //  this.dess = tags;
        //this.shareL = shareL;
        this.context = context;

    }

    @NonNull
    @Override
    public FragmentSearchVertical.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragmentsearchvertical, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        FragmentSearchVertical.CustomViewHolder viewHolder = new FragmentSearchVertical.CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        mQueue = Volley.newRequestQueue(context.getApplicationContext());
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(context);
        proftoken = phoneauthshard.getString("token", "null");
        viewDialog = new ViewDialog((Activity) context);

        usernamesearchclick=mainmodels.get(position).getUsername();
        Log.d("username",":::"+usernamesearchclick);
       // holder.sharetxt.setText(mainmodels.get(position).getSharecount());
        holder.homecontent.setText(mainmodels.get(position).getDescription());


        if (usernamesearchclick.equals("null")){
            holder.homename.setVisibility(View.GONE);
        }else {
            holder.homename.setVisibility(View.VISIBLE);
            holder.homename.setText(usernamesearchclick);
        }
/*
        if (dess.get(position).getName()!=null) {
            holder.hometags.setVisibility(View.VISIBLE);
            holder.hometags.setText(dess.get(position).getName());
        }else {
            holder.hometags.setVisibility(View.GONE);
        }
*/


/*
        holder.shareimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                postid = mainmodels.get(position).getId();
                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://www.vasmash.com"))
                        .setDomainUriPrefix("https://sh.vasmash.com")
                        // Open links with this app on Android
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                        .buildDynamicLink();

                Uri dynamicLinkUri = dynamicLink.getUri();
                Log.d("dynamiclinks", "::::" + dynamicLinkUri);

                createreflink(dynamicLinkUri,postid,holder,position);

            }
        });
*/


        if (mainmodels.get(position).getProfilepic() != null){

            Picasso.with(context).load(mainmodels.get(position).getProfilepic()).placeholder(R.drawable.uploadpictureold).into(holder.otherprofile);
        }else {
            //Picasso.with(context).load(R.drawable.uploadpictureold).into(holder.otherprofile);
            holder.otherprofile.setImageResource(R.drawable.uploadpictureold);
        }

        holder.otherprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (countDownTimer!=null) {
                    countDownTimer.cancel();
                }
                Intent intent = new Intent(context, OtherprofileActivity.class);
                intent.putExtra("posteduserid", mainmodels.get(position).getUserid());
                Log.e("posteduserid", mainmodels.get(position).getUserid());
                context.startActivity(intent);
            }
        });

/*
        if (soundname!=null) {
            holder.soundlay.setVisibility(View.VISIBLE);
            holder.song.setText(soundname + " - " + soundurl);
            holder.song.setSelected(true);
        }else {
            holder.soundlay.setVisibility(View.INVISIBLE);
        }
*/

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

    @Override
    public int getItemCount() {
        return mainmodels.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView  homename, hometags, sharetxt,song,homecontent;
        ImageView  shareimg, vastore,giftimg;
        RelativeLayout rootlay;
        CircleImageView otherprofile;

        public CustomViewHolder(View rootView) {
            super(rootView);

            //shareimg = (ImageView) rootView.findViewById(R.id.shareimg);

            homename = rootView.findViewById(R.id.homename);
            hometags = rootView.findViewById(R.id.hometags);
            sharetxt = rootView.findViewById(R.id.share);
            otherprofile = rootView.findViewById(R.id.otherprofile);
            rootlay=rootView.findViewById(R.id.root);
            homecontent=rootView.findViewById(R.id.homecontent);
           // giftimg=rootView.findViewById(R.id.giftimg);
            song=rootView.findViewById(R.id.song);
            song.setText("Original Sound - Ski_ssj"+"  "+"Original Sound - Ski_ssj");
            song.setSelected(true);

        }
    }


    public void shareItem(final String url) {
        Picasso.with(context).load(url).placeholder(R.drawable.uploadpictureold).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                    context.startActivity(Intent.createChooser(i, "Share Image"));
                } catch (Exception e) {
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    public void createreflink(Uri dynamicLinkUri, String postid, CustomViewHolder holder, int position){
        Task<ShortDynamicLink> dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(mainmodels.get(position).getImage()+"?"+postid+"-"+"true"))
                .setDomainUriPrefix("https://sh.vasmash.com")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.exam   ple.smash").build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("Example of a Dynamic Link")
                                .setDescription("This link works whether the app is installed or not!")
                                .build())
                .buildShortDynamicLink() .addOnCompleteListener((Activity) context, new OnCompleteListener<ShortDynamicLink>() {
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
                            jsoneashare(share_url + postid, holder,position);

                        } else {
                            // Error
                            // ...
                        }
                    }
                });


/*
        String sharelinktext="https://sh.vasmash.com/?"+"link="+mainmodels.get(position).getImage()+"?"+postid+"-"+"true";

        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        String sub="\n "+"*"+"Hi, have a look. I found this crazy stuff in VA-Smash!!"+"*"+"\n";
        String shareMessage= sub+" " +"\n"+ sharelinktext +"\n"+" "+ "\n"+"*"+ "I am enjoying and earning. Install to join me."+"*"+"\n\n";
        shareMessage = shareMessage ;
        i.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage.toString());
        context.startActivity(Intent.createChooser(i, "Share Via"));
        jsoneashare(share_url + postid, holder);

        //shorter link
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(sharelinktext))
                .buildShortDynamicLink()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.d("shortLink", "::::" + shortLink);
                            //viewDialog.hideDialog();

                        } else {
                        }
                    }
                });
*/

    }


    private void jsoneashare(String earningpoints_url, final CustomViewHolder holder,final int position) {
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
                                String sharepoints = response.getString("count");
                                holder.sharetxt.setText(sharepoints);
                                //shareL.set(position,sharepoints);
                                mainmodels.get(position).setSharecount(sharepoints);

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
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 422:
                                    try {
                                        body = new String(error.networkResponse.data, "UTF-8");
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
                                        String bodyerror = new String(error.networkResponse.data, "UTF-8");
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
        ) {
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
                headers.put("Authorization", proftoken);

                return headers;
            }
        };

        // add it to the RequestQueue
        mQueue.add(getRequest);
    }
}
