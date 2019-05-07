package com.lamtt.learnenglish.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.lamtt.learnenglish.R;
import com.lamtt.learnenglish.adapter.CategoryAdapter;
import com.lamtt.learnenglish.database.DatabaseHelper;
import com.lamtt.learnenglish.object.Category;
import com.lamtt.learnenglish.object.Phrase;
import com.lamtt.learnenglish.utils.Constant;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements View.OnClickListener {


    private GridView gridView;
    Button btnSignOut;
    DatabaseHelper mDatabaseHelper;
    List<Category> listCategory = new ArrayList<>();
    final String TAG = "Home";
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView tvRandomQuiz, tvSignout, tvEmailAccount;
    String emailAccount = "abc@gmail.com";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initNavigation();

        initDataBase();

        gridView = (GridView) findViewById(R.id.gridview);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, listCategory);
        gridView.setAdapter(categoryAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "[CATEGORY_TAG] : " + listCategory.get(position).getTag());
                Intent i = new Intent(Home.this, DetailPhraseActivity.class);
                i.putExtra(Constant.CATEGORY_TAG, listCategory.get(position).getTag());
                i.putExtra(Constant.CATEGORY_TITLE, listCategory.get(position).getVi());
                startActivity(i);
            }
        });

    }



    private void initDataBase() {
        mDatabaseHelper = new DatabaseHelper(this);
        try {
            mDatabaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        listCategory = mDatabaseHelper.getAllCategory();
    }



    private void initNavigation() {
        emailAccount = getIntent().getStringExtra(Constant.EMAIL_ACCOUNT);
        this.tvRandomQuiz = (TextView) findViewById(R.id.tv_random_quiz);
        this.tvSignout = findViewById(R.id.tv_sign_out);
        tvEmailAccount = findViewById(R.id.email_account);
        tvEmailAccount.setText(emailAccount);

        tvRandomQuiz.setOnClickListener(this);
        tvSignout.setOnClickListener(this);

        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setTitle((int)R.string.app_name);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                this.mDrawerLayout,
                R.string.navi_open, R.string.navi_close);
        this.mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sign_out:
                AuthUI.getInstance()
                        .signOut(Home.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                                showSignInOptions();
                                startActivity(new Intent(Home.this, LoginActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Home.this, "" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.tv_random_quiz:
                Intent i = new Intent(Home.this, QuizActivity.class);
                i.putExtra(Constant.CATEGORY_TAG, Constant.RANDOM_QUIZ_TAG);
                startActivity(i);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(Home.this,   "home", Toast.LENGTH_SHORT).show();
            if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                mDrawerLayout.closeDrawer(Gravity.START);
            }else {
                mDrawerLayout.openDrawer(Gravity.START);
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
