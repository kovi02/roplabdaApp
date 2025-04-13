package com.example.roplabdaapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class appListActivity extends AppCompatActivity {
        private static final String LOG_TAG = appListActivity.class.getName();
        private FirebaseUser user;
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_app_list);
            mAuth = FirebaseAuth.getInstance();
            // mAuth.signOut();
            user = FirebaseAuth.getInstance().getCurrentUser();

            if(user != null) {
                Log.d(LOG_TAG, "Authenticated user!");
            } else {
                Log.d(LOG_TAG, "Unauthenticated user!");
                finish();
            }
        }
    }