package com.lamtt.learnenglish.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.login.Login;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lamtt.learnenglish.R;
import com.lamtt.learnenglish.database.DatabaseFirebase;
import com.lamtt.learnenglish.utils.Constant;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    private static final int MY_REQUEST_CODE = 1202;
    List<AuthUI.IdpConfig> providers;
    private final String TAG = "LoginActivity";
    // UI references.
    private EditText edUser, edPassword;
    private Button btnLogin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        edPassword = findViewById(R.id.edPassWord);
//        edUser = findViewById(R.id.edUser);
//        btnLogin = findViewById(R.id.btnLogin);
//        btnLogin.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (edPassword.getText().length() == 0) {
//                    edPassword.setError("Password is null");
//                } else if (edUser.getText().length() == 0) {
//                    edUser.setError("User is null");
//                } else {
//                    login();
//                }
//            }
//        });

        login();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK) {
                Log.d(TAG, "OK");
                //todo save sharepreferent
                //Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userUid = user.getUid();
                //Show Email
                Toast.makeText(this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();

                //Insert DB
                DatabaseFirebase dbfb = new DatabaseFirebase();
                dbfb.insertUser(user);
                dbfb.insertResultTest(userUid);
                Intent i = new Intent(LoginActivity.this, Home.class);
                i.putExtra(Constant.EMAIL_ACCOUNT, user.getEmail());
                startActivity(i);
                finish();
            } else {
//                Log.e(TAG, response.getError().getMessage());
//                Toast.makeText(this, "" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void login() {
        Log.d(TAG, "[Login]");
        initProvider();
    }

    private void initProvider() {
        //Init providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(), //Email Builder
                new AuthUI.IdpConfig.FacebookBuilder().build(), //Facebook Builder
                new AuthUI.IdpConfig.GoogleBuilder().build() //Google Builder
        );

        showSignInOptions();
    }

    private void showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .setTheme(R.style.AppTheme).build(),MY_REQUEST_CODE);
    }
}

