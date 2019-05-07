package com.lamtt.learnenglish.database;


import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lamtt.learnenglish.object.Test;
import com.lamtt.learnenglish.object.User;

public class DatabaseFirebase {
    DatabaseReference mUsersRef;
    long maxIdNumberOfTest = 0;

    public DatabaseFirebase() {
    }

    public void insertUser(FirebaseUser user) {
        mUsersRef = FirebaseDatabase.getInstance().getReference("users");

        String userId = user.getUid();
        String email = user.getEmail();
        String name = user.getDisplayName();
        User user1 = new User(email, name);

        mUsersRef.child(userId).setValue(user1);
    }

    public void insertResultTest(String userUid) {
        mUsersRef = FirebaseDatabase.getInstance().getReference("users").child(userUid);
        mUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxIdNumberOfTest = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Test test = new Test(1, 10, 5, 50);

        mUsersRef.child(String.valueOf(maxIdNumberOfTest)).setValue(test);
    }

    public User getUser(FirebaseUser user) {
        String email = user.getEmail();
        String name = user.getDisplayName();
        User user1 = new User(email, name);
        return user1;
    }

}
