package com.vasmash.va_smash.login.fragments;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.vasmash.va_smash.BottmNavigation.TopNavigationview;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.login.Gaweblogin;
import com.vasmash.va_smash.login.OTPVerification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.vasmash.va_smash.login.RegisterformActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment.killerplyer;
import static com.vasmash.va_smash.VASmashAPIS.APIs.facebooklogin_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.fcmtoken_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.googlesignin_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.login_pwdurl;
import static com.vasmash.va_smash.VASmashAPIS.APIs.loginwithga_url;
/**
 * A simple {@link Fragment} subclass.
 */
//this is login class
public class LoginFragment extends AppCompatActivity {

    RequestQueue sch_RequestQueue;
    Dialog dialog;

    EditText loginemail, loginpwd;
    TextView registerbtn, forgotpwd, galogin;
    Button loginbtn;
    ImageView guestbtn;
    String urlga = "null", accountId = "null";
    RelativeLayout rootlay;

    //this is the loader animationview
    ViewDialog viewDialog;
    String regid;
    SharedPreferences pref;

    //loaction permisions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;

    //facebook and google
    private static final String EMAIL = "email";
    LoginButton loginButton;
    private CallbackManager callbackManager;

    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        loginemail = findViewById(R.id.mailtxt);
        loginpwd = findViewById(R.id.pwdtxt);
        // requestotp=view.findViewById(R.id.requestotp);
        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.registerbtn);
        forgotpwd = findViewById(R.id.forgotpwd);
        rootlay = findViewById(R.id.rootlay);
        galogin = findViewById(R.id.galogin);
/*
        guestbtn=findViewById(R.id.loginasguest);
        guesttext=findViewById(R.id.guesttext);
*/
        viewDialog = new ViewDialog(LoginFragment.this);
        pref = getSharedPreferences("loginid", 0);

        sch_RequestQueue = Volley.newRequestQueue(getApplicationContext());

        //it checks where network available or not
        if (isNetworkAvailable()) {
            //Toast.makeText(Home.this, "Network connection is available", Toast.LENGTH_SHORT).show();
        } else if (!isNetworkAvailable()) {
            // Toast.makeText(Home.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Network connection is not available", Snackbar.LENGTH_LONG);
            snackbar.show();
        }


/*
    //this is for hashkey generater for facebook integration
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.vasmash.va_smash", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
*/

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        //this is the firebase post Api
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (task.isSuccessful()) {
                    String firebasetoken = task.getResult().getToken();
                    Log.d("firebasetoken", ":::" + firebasetoken);
                    String loginS = "{\"token\":\"" + firebasetoken + "\",\"device\":\"" + "android" + "\"}";
                    Log.d("jsnresponse firebase", "---" + loginS);
                    String url = fcmtoken_url;
                    JSONObject lstrmdt;
                    try {
                        lstrmdt = new JSONObject(loginS);
                        //Log.d("jsnresponse....", "---" + loginS);
                        notofication(lstrmdt, url);

                    } catch (JSONException ignored) {
                    }
                } else {

                }
            }
        });

        //this is the submit login post api
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mailS = loginemail.getText().toString();
                final String pwdS = loginpwd.getText().toString();
                if (mailS.isEmpty() || pwdS.isEmpty()) {
                    //Toast.makeText(getActivity(), "Please enter the All Fields", Toast.LENGTH_SHORT).show();
                    popup("Please fill all the fields to login");

                } else {

                    String loginS = "{\"username\":\"" + mailS + "\",\"password\":\"" + pwdS + "\"}";
                    Log.d("jsnresponse login", "---" + loginS);
                    String url = login_pwdurl;
                    JSONObject lstrmdt;

                    try {
                        lstrmdt = new JSONObject(loginS);
                        Log.d("jsnresponse....", "---" + loginS);
                        viewDialog.showDialog();
                        JSONSenderVolleylogin(lstrmdt, url);

                    } catch (JSONException ignored) {
                    }
                }
            }
        });

        //this is intent to the register
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginFragment.this, RegisterFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        });

        //this is intent to the forgot
        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginFragment.this, ForgetFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        //this is the hide keybord
        rootlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im = (InputMethodManager) LoginFragment.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(loginpwd.getWindowToken(), 0);

            }
        });
        //this is the galogin
        galogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getloginwithga();
            }
        });

        //facebook and google signin

        //Initializing Views
        signInButton = findViewById(R.id.sign_in_button);

        //this is facebook login
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);
        // Callback registration

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                String token=loginResult.getAccessToken().getToken();
                setFacebookData(loginResult,token);
            }
            @Override
            public void onCancel() {
                // App code
                Log.d("cancell", ":::");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("error", ":::" + exception.getMessage());
            }
        });


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        //this is google signin.
        String serverClientId = getString(R.string.server_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(serverClientId)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    public void JSONSenderVolleylogin(JSONObject lstrmdt, String url) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        Log.d("555555", "login" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONSenderVolleylogin", "---" + response);
                        viewDialog.hideDialog();
                        try {
                            if (response.has("status")) {
                                String userId = response.getString("id");
                                String status = response.getString("status");
                                String message = response.getString("message");

                                SharedPreferences.Editor editor = pref.edit();
                                regid = response.getString("id");
                                editor.putString("id", regid);
                                editor.commit();

                                if (status.equals("1")) {
                                    incomplete(message, userId);
                                }
                            } else if (response.has("user")) {
                                viewDialog.hideDialog();
                                JSONObject user = response.getJSONObject("user");
                                String _id = user.getString("_id");
                                String token = user.getString("token");
                                String profilePic = user.getString("profilePic");
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginFragment.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("token", token);
                                editor.putString("id", _id);
                                editor.putString("userpic", profilePic);
                                editor.apply();
                                killerplyer();
                                Intent i = new Intent(LoginFragment.this, TopNavigationview.class);
                                startActivity(i);
                                // Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //viewDialog.hideDialog();
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
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    loginemail.getText().clear();
                                    loginpwd.getText().clear();
                                    popup(message);
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
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

    public void notofication(JSONObject lstrmdt, String url) {
        Log.d("loginurl", "---" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("notificationres", "---" + response);
                        try {
                            if (response.has("user")) {
                                JSONObject user = response.getJSONObject("user");
                                String _id = user.getString("_id");
                                String token = user.getString("token");
                                String profilePic = user.getString("profilePic");
                                if (user.has("emailVerified")) {
                                    String emailVerified = user.getString("emailVerified");
                                    if (emailVerified.equals("true")) {
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginFragment.this);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("token", token);
                                        editor.putString("id", _id);
                                        editor.putString("userpic", profilePic);
                                        editor.apply();
                                        killerplyer();
                                        Intent i = new Intent(LoginFragment.this, TopNavigationview.class);
                                        startActivity(i);
                                        // Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else {
                                if (response.has("status")) {
                                    String status = response.getString("status");
                                    if (status.equals("1")) {
                                        String profilePic="null",firstName="null",lastName="null",email="null",id="null";
                                        if (response.has("id")) {
                                            id = response.getString("id");
                                        }
                                        if (response.has("data")) {
                                            JSONObject object = response.getJSONObject("data");
                                            if (object.has("profilePic")) {
                                                profilePic = object.getString("profilePic");
                                            }
                                            if (object.has("firstName")) {
                                                firstName = object.getString("firstName");
                                            }
                                            if (object.has("lastName")) {
                                                lastName = object.getString("lastName");
                                            }
                                            if (object.has("email")) {
                                                email = object.getString("email");
                                            }
                                        }

                                        Intent intent = new Intent(LoginFragment.this, RegisterformActivity.class);
                                        intent.putExtra("gaid",id);
                                        intent.putExtra("profilePic",profilePic);
                                        intent.putExtra("name",firstName);
                                        intent.putExtra("email",email);
                                        intent.putExtra("mobile","");
                                        intent.putExtra("country","");
                                        intent.putExtra("countryCode","");
                                        startActivity(intent);
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
                //viewDialog.hideDialog();
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
                                    LoginManager.getInstance().logOut();
                                    popup(message);
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
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
                Log.d("errorvaloyyer", ":::" + error);
                viewDialog.hideDialog();

            }
        });

    }

    public <T> void addToRequestQueue(Request<T> req) {
        if (sch_RequestQueue == null) {
            sch_RequestQueue = Volley.newRequestQueue(LoginFragment.this);
        }
        sch_RequestQueue.add(req);
    }

    public void getloginwithga() {
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, loginwithga_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("loginwith ga", response.toString());
                        try {
                            urlga = response.getString("url");
                            accountId = response.getString("accountId");
                            Intent intent = new Intent(LoginFragment.this, Gaweblogin.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("gaurl", urlga);
                            intent.putExtra("accountId", accountId);

                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                                        Log.d("body", "---" + body);
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
        sch_RequestQueue.add(getRequest);
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


    public void incomplete(String msg, String userid) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = LoginFragment.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.responsepopup, null);


        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        TextView message = (TextView) layout.findViewById(R.id.msg);
        message.setText(msg);

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginFragment.this, OTPVerification.class);
                i.putExtra("number", loginemail.getText().toString());
                startActivity(i);
                dialog.dismiss();
            }
        });


        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();

    }

    //this is the response pop code
    public void popup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = LoginFragment.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.responsepopup, null);


        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        TextView message = (TextView) layout.findViewById(R.id.msg);
        message.setText(msg);

        okbtn.setOnClickListener(new View.OnClickListener() {
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

    //it checks where network available or not
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) LoginFragment.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

/*
    //this is the double tap close app code
    int doubleBackToExitPressed = 1;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressed == 2) {
            finishAffinity();
            System.exit(0);
        } else {
            doubleBackToExitPressed++;
            Toast.makeText(LoginFragment.this, "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed = 1;

            }
        }, 2000);
    }
*/

    //this is the current location permissions
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginFragment.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    //facebook response
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("resultscode", "::::" + resultCode + ":::::" + data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);


        } else if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.w("Googleaccount", "code=" + account);
            String loginS = "{\"token\":\"" + account.getIdToken() + "\"}";
            Log.d("jsnresponse google", "---" + loginS);
            String url = googlesignin_url;
            JSONObject lstrmdt;
            try {
                lstrmdt = new JSONObject(loginS);
                //Log.d("jsnresponse....", "---" + loginS);
                notofication(lstrmdt, url);

            } catch (JSONException ignored) {
            }

            // Signed in successfully, show authenticated UI.
            //  startActivity(new Intent(RegisterFragment.this, RegisterformActivity.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginFragment.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // startActivity(new Intent(RegisterFragment.this, RegisterformActivity.class));
        }
        super.onStart();
    }
    private void setFacebookData(final LoginResult loginResult,String token) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        Log.i("Response", response.toString());

                        String loginS = "{\"token\":\"" + token + "\"}";
                        Log.d("jsnresponse facebook", "---" + loginS);
                        String url = facebooklogin_url;
                        JSONObject lstrmdt;
                        try {
                            lstrmdt = new JSONObject(loginS);
                            //Log.d("jsnresponse....", "---" + loginS);
                            notofication(lstrmdt, url);

                        } catch (JSONException ignored) {
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,email,first_name,last_name,gender,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void finishActivity(View v){
        finish();
    }

}