package com.lamtt.learnenglish.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lamtt.learnenglish.R;
import com.lamtt.learnenglish.adapter.ResultAdapter;
import com.lamtt.learnenglish.database.DatabaseFirebase;
import com.lamtt.learnenglish.database.DatabaseHelper;
import com.lamtt.learnenglish.object.Test;

import java.util.ArrayList;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;

public class ResultActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<Test> testList;
    ResultAdapter resultAdapter;
    DatabaseReference mUsersRef;
    CircleProgressView mCircleView;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setTitle("Thống kê");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = user.getUid();

        //Khoi tao test list
        testList = new ArrayList<>();
        //todo replace testResultTopic
        mUsersRef = FirebaseDatabase.getInstance().getReference("users").child(userUid).child("tests");

        mUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot testSnapshot : dataSnapshot.getChildren()) {
                    Test test = testSnapshot.getValue(Test.class);
                    testList.add(test);

                }
                for (Test test : testList) {
                    Log.d("Tests", "TestID: " + test.getTestId() + ", right: " + test.getTrueAnswer());
                }
                resultAdapter = new ResultAdapter(testList);

                listView = findViewById(R.id.list_view);
                listView.setAdapter(resultAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseHelper = new DatabaseHelper(this);
        int numActive = databaseHelper.getNumCategoryActive();
        int numCategory = databaseHelper.getSizeCategory();
        int percent = Math.round(numActive * 100f / numCategory);
        Log.d("[Percent] ", numActive + "//" + numCategory + "///" + percent);
        //Process view
        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        mCircleView.setValueAnimated(percent);
        mCircleView.setUnit("%");
        mCircleView.setUnitVisible(true);
        mCircleView.setAutoTextSize(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();

        }
        return super.onOptionsItemSelected(item);
    }

}
