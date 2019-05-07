package com.lamtt.learnenglish.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lamtt.learnenglish.R;
import com.lamtt.learnenglish.adapter.PageAdapter;
import com.lamtt.learnenglish.database.DatabaseHelper;
import com.lamtt.learnenglish.object.Phrase;
import com.lamtt.learnenglish.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class DetailPhraseActivity extends AppCompatActivity {

    String strTitle = "", strCategoryTag = "";
    List<Phrase> phraseList = new ArrayList<>();
    DatabaseHelper databaseHelper;
    ViewPager viewPager;
    final String TAG = "DetailPhraseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_phrase);

        strCategoryTag = getIntent().getStringExtra(Constant.CATEGORY_TAG);
        strTitle = getIntent().getStringExtra(Constant.CATEGORY_TITLE);
        Log.d(TAG, "[Title-tag] : " + strTitle + "-" + strCategoryTag);
        getSupportActionBar().setTitle(strTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseHelper = new DatabaseHelper(this);
        phraseList = databaseHelper.getListPhraseByTag(strCategoryTag);
        Log.d(TAG, "[Phrase size] : " + phraseList.size());

        viewPager = findViewById(R.id.view_pager);
        PagerAdapter adapter = new PageAdapter(getSupportFragmentManager(), phraseList);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.detail_phrase_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_practice) {

            Intent i = new Intent(DetailPhraseActivity.this, QuizActivity.class);
            i.putExtra(Constant.CATEGORY_TAG, strCategoryTag);
            startActivity(i);

        } else if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
