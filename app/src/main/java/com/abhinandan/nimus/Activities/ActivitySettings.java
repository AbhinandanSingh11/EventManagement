package com.abhinandan.nimus.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abhinandan.nimus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivitySettings extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ImageView back;
    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser User = mAuth.getCurrentUser();

                if(User == null){
                    Toast.makeText(ActivitySettings.this, "You need to login first!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivitySettings.this,ActivityLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        };


        back = findViewById(R.id.ImageBackSettings);
        logout = findViewById(R.id.TextLogOut);

        back.setOnClickListener(view->{
            Intent intent = new Intent(ActivitySettings.this,MainActivity.class);
            intent.putExtra("val",1);
            startActivity(intent);
        });


        logout.setOnClickListener(view -> {
            Toast.makeText(this, "Clicked!!", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        });
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener!=null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}

