package com.example.chirag.virtualcachedesign.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.service.Response;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int GOOGLE_SIGN_IN = 8001;
    private static final String CALLBACK_URL = "http://localhost/oauth2callback";

    private Intent signInIntent = new Intent();
    private String url = "https://accounts.google.com/o/oauth2/auth?client_id=630371916595-669asffe699iek6nq4nq95k43b030q4q.apps.googleusercontent.com&redirect_uri=http%3A%2F%2Flocalhost%2Foauth2callback&scope=https://www.googleapis.com/auth/youtube&response_type=code&access_type=offline&approval_prompt=force";
    private String accessToken;
    private String userID;
    private SharedPreferences sharedPreferences;

    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInAccount account;

    @Bind(R.id.statusTextView)
    TextView statusTextView;

    @Bind(R.id.sign_in_button)
    SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //bind views
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Scope scope = new Scope("https://www.googleapis.com/auth/youtube");

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.FIREBASE_WEB_CLIENT_ID))
                .requestServerAuthCode(getString(R.string.FIREBASE_WEB_CLIENT_ID))
                .requestEmail()
                .requestScopes(scope)
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        sharedPreferences = getSharedPreferences("qualityTest", MODE_PRIVATE);

        //Firebase setup
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    if (sharedPreferences.contains("AccessToken") && sharedPreferences.contains("account")) {
                        accessToken = sharedPreferences.getString("AccessToken", "");
                        userID = sharedPreferences.getString("account", "");
                        String email = sharedPreferences.getString("email", "");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("AccessToken", accessToken);
                        intent.putExtra("account", userID);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        // Set the dimensions of the sign-in button.
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
    }

    //sign in button on click
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                signInButton.setVisibility(View.GONE);
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                retrieveToken(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void retrieveToken(final GoogleSignInAccount account) {
        final String email = account.getEmail();
        userID = account.getId();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("grant_type", "authorization_code")
                .add("client_id", "774270715832-nr96kgepiak48mij32pbn10u9p09k9hb.apps.googleusercontent.com")   // something like : ...apps.googleusercontent.com
                .add("client_secret", "IW0p_H_mXw6BmDZQ_14GahZc" )
                .add("redirect_uri", "")
                .add("code", account.getServerAuthCode()) // device code.
                .add("id_token", account.getIdToken())// This is what we received in Step 5, the jwt token.
                .build();

        final Request request = new Request.Builder()
                .url("https://www.googleapis.com/oauth2/v4/token")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                Log.e(LOG_TAG, e.toString());
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                try {
                    String res = response.body().string();
                    //JSONObject jsonObject = new JSONObject(res.substring(res.indexOf('{'), res.lastIndexOf('}')));
                    //accessToken = jsonObject.getString("access_token");

                    Gson gson = new GsonBuilder().create();
                    // Define Response class to correspond to the JSON response returned
                    Response resp = gson.fromJson(res, Response.class);
                    accessToken = resp.access_token;
                    //refreshToken = resp.refresh_token;
                    Log.i(LOG_TAG, accessToken);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // Edit the saved preferences
                    editor.putString("AccessToken", accessToken);
                    editor.putString("account", userID);
                    editor.putString("email", email);
                    Log.i(TAG, "User ID is: " + userID);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("AccessToken", accessToken);
                    intent.putExtra("account", userID);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

