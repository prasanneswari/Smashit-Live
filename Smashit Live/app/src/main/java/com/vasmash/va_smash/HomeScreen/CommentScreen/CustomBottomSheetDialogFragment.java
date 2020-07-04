package com.vasmash.va_smash.HomeScreen.CommentScreen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.commentatval;
import static com.vasmash.va_smash.HomeScreen.homeadapters.FragmentInner.postid;
import static com.vasmash.va_smash.VASmashAPIS.APIs.posteomment_url;

/**
 * Created by Sumeet Jain on 28-06-2019.
 */

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    EditText commenttxt;
    ImageView commentprofile,commentat,commentsend;
    private RequestQueue mQueue;
    public  String proftoken;
    static public String commentcount="null";



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final View contentView = View.inflate(getContext(), R.layout.editbottomsheet, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);

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
        // getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        commenttxt=contentView.findViewById(R.id.commenttxt);
        commentat=contentView.findViewById(R.id.commentat);
        commentsend=contentView.findViewById(R.id.commentsend);
        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(getActivity());
        proftoken = phoneauthshard.getString("token", "null");


        if (commentatval){
            addImageBetweentext(commentat.getDrawable());
            commentatval=false;
           // new KeyboardUtil(getActivity(), contentView,dialog);

            commenttxt.requestFocus();
            commenttxt.setFocusableInTouchMode(true);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

/*
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(commenttxt, InputMethodManager.SHOW_FORCED);
*/

        }else {
           // new KeyboardUtil(getActivity(), contentView,dialog);

            commenttxt.requestFocus();
            commenttxt.setFocusableInTouchMode(true);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

/*
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

*/
        }

        commentat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                commentatval=true;
                addImageBetweentext(commentat.getDrawable());

/*
                CommentuserListFragment fragment = new CommentuserListFragment();
                fragment.show(((AppCompatActivity)getActivity()).getSupportFragmentManager(), "bottom_sheet");
*/
/*
                commentuserlst.setHasFixedSize(true);
                // use a linear layout manager
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                commentuserlst.setLayoutManager(mLayoutManager);


                commentuserAdapter = new Adapter_commentusrlist(userlistL, getActivity());
                commentlst.setAdapter(commentuserAdapter);*/
            }
        });


        commentsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String commenttxtS = commenttxt.getText().toString();

                if (commenttxtS.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter the All Fields", Toast.LENGTH_SHORT).show();

                } else {

                    String loginS = "{\"postId\":\"" + postid + "\",\"comment\":\"" + commenttxtS + "\"}";
                    Log.d("jsnresponse login", "---" + loginS);
                    String url=posteomment_url;
                    JSONObject lstrmdt;

                    try {
                        lstrmdt = new JSONObject(loginS);
                        Log.d("jsnresponse....", "---" + loginS);
                        JSONSenderVolleycommentsend(lstrmdt,url);

                    } catch (JSONException ignored) {
                    }
                }
            }
        });


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


    public void JSONSenderVolleycommentsend(JSONObject lstrmdt, String url) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        Log.d("555555", "login" + url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONSenderVolleylogin", "---" + response);
                        try {
                            if (response.length()!=0) {
                                String message = response.getString("message");
                                commentcount = response.getString("count");
                                if (message.equals("Comment Saved Successful")) {
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    commenttxt.getText().clear();

                                    //countcomment.setText(commentcount+" "+"Comments");
                                    //jsoncomments();
                                    dismiss();
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
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

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
            mQueue = Volley.newRequestQueue(getActivity());
        }
        mQueue.add(req);
    }

}
