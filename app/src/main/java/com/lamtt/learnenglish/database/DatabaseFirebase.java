package com.lamtt.learnenglish.database;


import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lamtt.learnenglish.object.Test;
import com.lamtt.learnenglish.object.User;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.TAG;

public class DatabaseFirebase {
    DatabaseReference mUsersRef;
    long maxIdNumberOfTest = 0;
    List<Test> testList;

    public DatabaseFirebase() {
    }

    public void insertUser(final FirebaseUser user) {
        mUsersRef = FirebaseDatabase.getInstance().getReference("users");

        String userId = user.getUid();
        String email = user.getEmail();
        String name = user.getDisplayName();
        final User user1 = new User(email, name);

        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.child("email").exists()) {

                    } else {
                        mUsersRef.child("users").setValue(user1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
           });
    }

    public void insertResultTest(String userUid, int isAnswer, int rightAnswer, String idTest) {
        mUsersRef = FirebaseDatabase.getInstance().getReference("users").child(userUid).child("tests");
        Test test = new Test(idTest, isAnswer, rightAnswer, 50);

        mUsersRef.push().setValue(test);

    }

    public User getUser(FirebaseUser user) {
        String email = user.getEmail();
        String name = user.getDisplayName();
        User user1 = new User(email, name);
        return user1;
    }

    public List<Test> getResultTest(String userUid) {
        testList = new ArrayList<>();
        mUsersRef = FirebaseDatabase.getInstance().getReference("users").child(userUid).child("tests");

        mUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                for(DataSnapshot testSnapshot : dataSnapshot.getChildren()) {
                    Test test = testSnapshot.getValue(Test.class);
                    testList.add(test);
                }
                return;

            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
        for(Test test : testList) {
            Log.d("Tests", "TestID: " + test.getTestId() + ", right: " + test.getTrueAnswer());
        }
        return testList;

    }

}
