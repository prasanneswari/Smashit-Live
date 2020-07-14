package com.vasmash.va_smash.createcontent.videoeffects;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.VASmashAPIS.APIs;
import com.vasmash.va_smash.VaContentScreen.Adapter.VastoreAdapter;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Vastore_content_model;
import com.vasmash.va_smash.VaContentScreen.VAStoreActivity;
import com.vasmash.va_smash.createcontent.Sounds.Sound_catemodel;
import com.vasmash.va_smash.createcontent.cameraedit.MyCanvas;
import com.vasmash.va_smash.createcontent.cameraedit.Sticker_cate_Adapter;
import com.vasmash.va_smash.createcontent.cameraedit.Sticker_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.vastore_userdata;
import static com.vasmash.va_smash.VASmashAPIS.APIs.vastoredata_url;

public class StickerBSFragment extends BottomSheetDialogFragment {
    private  RecyclerView sticker_cate_recy,rvEmoji;
    private List<Sound_catemodel> personUtils12;
    Sticker_cate_Adapter sticker_cate_adapter;
    String stickercatid="5ec665fcb02b36798ea55a20";
    private RequestQueue mQueue;
    private ArrayList<Sticker_model> stickerurl;
    String token;

    public StickerBSFragment() {
        // Required empty public constructor
    }

    private StickerListener mStickerListener;

    public void setStickerListener(StickerListener stickerListener) {
        mStickerListener = stickerListener;
    }

    public interface StickerListener {
        void onStickerClick(Bitmap bitmap);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sticker_emoji_dialog, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        sticker_cate_recy = (RecyclerView)contentView.findViewById(R.id.sticker_cate_recyvideo);
        LinearLayoutManager layoutManager1= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        sticker_cate_recy.setLayoutManager(layoutManager1);
        productcatage();
        product(stickercatid);

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        rvEmoji = contentView.findViewById(R.id.rvEmoji);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
        List<Sticker_model> stickerurl;
        private Context context;
        int[] stickerList = new int[]{R.drawable.aa, R.drawable.bb,R.drawable.cc,R.drawable.dd,R.drawable.ee,R.drawable.ff};



        public StickerAdapter(Context context, List stickerurl) {
            this.context = context;
            this.stickerurl = stickerurl;

        }

        public StickerAdapter() {

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sticker, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Sticker_model stck = stickerurl.get(position);

            Picasso.with(getActivity()).load(stck.getStickerurl()).into( holder.imgSticker);
            // holder.imgSticker.setImageResource(stickerList[position]);
        }

        @Override
        public int getItemCount() {
            return stickerurl.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;
            Sticker_model sticker_model;

            ViewHolder(View itemView) {
                super(itemView);
                imgSticker = itemView.findViewById(R.id.imgSticker);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStickerListener != null) {

                        }
                        try {
                            //URL url = new URL(stickerurl.get(getAdapterPosition()).getStickerurl());
                            BitmapDrawable drawable = (BitmapDrawable) imgSticker.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            if(bitmap!=null){
                               // Log.e("imgSticker", String.valueOf(bitmap));
                                //  Bitmap bitmapimg= BitmapFactory.decodeFile(.getPath());
                                mStickerListener.onStickerClick(bitmap);
                                dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                });
            }
        }
    }

    private String convertEmoji(String emoji) {
        String returnedEmoji = "";
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            returnedEmoji = getEmojiByUnicode(convertEmojiToInt);
        } catch (NumberFormatException e) {
            returnedEmoji = "";
        }
        return returnedEmoji;
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }



    public void stickerlistcate() {
//        spinner2.setVisibility(View.VISIBLE);

        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // prepare the Request
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Stickers, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                       // Log.d("soundresponse1", response.toString());

//                        sounds_listview = (ListView) layout.findViewById(R.id.sounds_listview);


                        personUtils12 = new ArrayList<>();

                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    Sound_catemodel Sound_catemodel = new Sound_catemodel();
                                    String _id = employee.getString("_id");
                                    String name = employee.getString("name");
                                    boolean sel = false;

                                    Sound_catemodel.setSound_code(_id);
                                    Sound_catemodel.setSound_cate(name);
                                    Sound_catemodel.setSelected(sel);
                                    personUtils12.add(Sound_catemodel);

/*
                                    Log.d("sticker", "createddateL1:::" + personUtils12);
                                    Log.d("sticker", "sticker:::" + _id + name);
*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            sticker_cate_adapter = new Sticker_cate_Adapter(getActivity().getApplicationContext(), personUtils12);
                            sticker_cate_recy.setAdapter(sticker_cate_adapter);

                            sticker_cate_adapter.setonStickercateClickListener(new Sticker_cate_Adapter.OnStickercateClickListener() {
                                @Override
                                public void onStickercateClickListener(Sound_catemodel code) {
                                    String sticker_cate_code=code.getSound_code();
                                   // Log.e("sticker_cate_codevideo",sticker_cate_code);
                                    product(sticker_cate_code);
                                }
                            });

//                            sounds_cate_adapter.setonSoundcateClickListener(new Sounds_Cate_Adapter.OnSoundcateClickListener() {
//                                @Override
//                                public void onSoundcateClickListener(Sound_catemodel code) {
//
//                                    sound_cate_code=code.getSound_code();
//                                    Log.e("sound_cate_code",sound_cate_code);
//                                    soundlist(sound_cate_code);
//                                }
//                            });
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
                                      //  Log.d("body", "---" + body);
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
                }
        );

        // add it to the RequestQueue
        mQueue.add(getRequest);


    }

    public void stickerlist(String stickerid) {
//        spinner2.setVisibility(View.VISIBLE);
      //  Log.d("stickerParseuser1", "store data1" + APIs.Stickerslist+stickerid);
        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // prepare the Request
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Stickerslist+stickerid, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                       // Log.d("stickerresponse", response.toString());

//                        sounds_listview = (ListView) layout.findViewById(R.id.sounds_listview)
                        stickerurl = new ArrayList<>();





                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);
                                    String iii=employee.getString("stickers");

                                   // Log.d("iii", "createddateL:::" + iii);
                                    JSONArray js=new JSONArray(iii);

                                    for(int z = 0; z < js.length(); z++)
                                    {
                                        JSONObject sund = js.getJSONObject(z);

                                        Sticker_model sticker_model = new Sticker_model();
                                        String _id = sund.getString("_id");
                                        String name = sund.getString("url");
                                        boolean sel = false;

                                        sticker_model.setSticker_code(_id);
                                        sticker_model.setStickerurl(name);
                                        sticker_model.setSelected(sel);
                                        stickerurl.add(sticker_model);


/*
                                        Log.d("sticker2", "createddateL:::" + stickerurl);
                                        Log.d("sticker2", "soundlst:::" + _id + name);
*/
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            StickerAdapter   stickerAdapter = new StickerAdapter(getActivity(),stickerurl);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                            rvEmoji.setLayoutManager(gridLayoutManager);
                            rvEmoji.setAdapter(stickerAdapter);

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
                }
        );

        // add it to the RequestQueue
        mQueue.add(getRequest);


    }


    public void productcatage() {
        //spinner2.setVisibility(View.VISIBLE);
//        viewDialog.showDialog();


       // Log.d("jsonParseuserstickers", "stickers" + vastore_userdata);


        // prepare the Request
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, APIs.Stickers,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.e("message", String.valueOf(response));
                        try {



                            JSONArray jsonArray=response.getJSONArray("CategoryList");


                            personUtils12 = new ArrayList<>();

                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject employee = jsonArray.getJSONObject(i);

                                        Sound_catemodel Sound_catemodel = new Sound_catemodel();
                                        String _id = employee.getString("_id");
                                        String name = employee.getString("name");
                                        boolean sel = false;

                                        Sound_catemodel.setSound_code(_id);
                                        Sound_catemodel.setSound_cate(name);
                                        Sound_catemodel.setSelected(sel);
                                        personUtils12.add(Sound_catemodel);

/*
                                        Log.d("sticker", "createddateL1:::" + personUtils12);
                                        Log.d("sticker", "sticker:::" + _id + name);
*/
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                sticker_cate_adapter = new Sticker_cate_Adapter(getActivity().getApplicationContext(), personUtils12);
                                sticker_cate_recy.setAdapter(sticker_cate_adapter);

                                sticker_cate_adapter.setonStickercateClickListener(new Sticker_cate_Adapter.OnStickercateClickListener() {
                                    @Override
                                    public void onStickercateClickListener(Sound_catemodel code) {
                                        String sticker_cate_code=code.getSound_code();
                                       // Log.e("sticker_cate_codevideo",sticker_cate_code);
                                        product(sticker_cate_code);
                                    }
                                });

//                            sounds_cate_adapter.setonSoundcateClickListener(new Sounds_Cate_Adapter.OnSoundcateClickListener() {
//                                @Override
//                                public void onSoundcateClickListener(Sound_catemodel code) {
//
//                                    sound_cate_code=code.getSound_code();
//                                    Log.e("sound_cate_code",sound_cate_code);
//                                    soundlist(sound_cate_code);
//                                }
//                            });
                            }









                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                    }

                }



        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Content-Type","application/json");
                headers.put("Authorization",token);
                return headers;
            }
        };

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }




    public void product(String stickerid) {
        //spinner2.setVisibility(View.VISIBLE);
//        viewDialog.showDialog();


       // Log.d("stickers", "stickers" + APIs.Stickerslist);

        // prepare the Request
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,APIs.Stickerslist+stickerid,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.e("message", String.valueOf(response));
                        try {



                            JSONArray jsonArray=response.getJSONArray("CategoryData");


                            if (jsonArray.length() != 0) {
                                stickerurl = new ArrayList<>();
                                for (int j = 0; j < jsonArray.length() ; j++ ) {
                                   // Log.d("lengtharayyy", ":::" + j);

                                    JSONObject stik = jsonArray.getJSONObject(j);
                                    JSONArray jsonArraystickers = stik.getJSONArray("stickers");

                                    for (int z = 0; z < jsonArraystickers.length(); z++) {
                                        JSONObject sund = jsonArraystickers.getJSONObject(z);

                                        Sticker_model sticker_model = new Sticker_model();
                                        String _id = sund.getString("_id");
                                        String name = sund.getString("url");
                                        boolean sel = false;

                                        sticker_model.setSticker_code(_id);
                                        sticker_model.setStickerurl(name);
                                        sticker_model.setSelected(sel);
                                        stickerurl.add(sticker_model);


/*
                                        Log.d("sticker2", "createddateL:::" + stickerurl);
                                        Log.d("sticker2", "soundlst:::" + _id + name);
*/

                                    }
                                    StickerAdapter stickerAdapter = new StickerAdapter(getActivity(), stickerurl);
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                                    rvEmoji.setLayoutManager(gridLayoutManager);
                                    rvEmoji.setAdapter(stickerAdapter);


                                }


                            }









                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                    }

                }



        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Content-Type","application/json");
                headers.put("Authorization",token);
                return headers;
            }
        };

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }



}